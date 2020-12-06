package com.savings.savinggoals.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.savings.savinggoals.BuildConfig
import com.savings.savinggoals.database.SavingGoalDatabase
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.channels.FileChannel

const val fileName = "TSG_schema_backup"
const val maximumDatabaseFile = 1

fun backupDatabase(context: Context) {
    val appDatabase = SavingGoalDatabase.getDatabase(context)
    appDatabase.close()
    val coreDataBaseFile: File = context.getDatabasePath("saving_goal_schema")
    val backupDir = File(context.externalCacheDir.toString(), "backup")

    val backupDataBasePath = backupDir.path + File.separator.toString() + fileName
    if (!backupDir.exists()) {
        backupDir.mkdirs()
    } else {
        // Directory Exists. Delete a file if count is 5 already. Because we will be creating a new.
        // This will create a conflict if the last backup file was also on the same date. In that case,
        // we will reduce it to 4 with the function call but the below code will again delete one more file.
        checkAndDeleteBackupFile(backupDir, backupDataBasePath)
    }
    val saveFile = File(backupDataBasePath)
    if (saveFile.exists()) {
        saveFile.delete()
    }
    try {
        if (saveFile.createNewFile()) {
            val bufferSize = 8 * 1024
            val buffer = ByteArray(bufferSize)
            var bytesRead = bufferSize
            val savedb: OutputStream = FileOutputStream(backupDataBasePath)
            val indb: InputStream = FileInputStream(coreDataBaseFile)
            while (indb.read(buffer, 0, bufferSize).also { bytesRead = it } > 0) {
                savedb.write(buffer, 0, bytesRead)
            }
            savedb.flush()
            indb.close()
            savedb.close()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun checkAndDeleteBackupFile(directory: File, path: String?) {
    // This is to prevent deleting extra file being deleted which is mentioned in previous comment lines.
    val currentDateFile = File(path)
    var fileIndex = -1
    var lastModifiedTime = System.currentTimeMillis()
    if (!currentDateFile.exists()) {
        val files: Array<File> = directory.listFiles()
        if (files != null && files.size >= maximumDatabaseFile) {
            for (i in files.indices) {
                val file: File = files[i]
                val fileLastModifiedTime: Long = file.lastModified()
                if (fileLastModifiedTime < lastModifiedTime) {
                    lastModifiedTime = fileLastModifiedTime
                    fileIndex = i
                }
            }
            if (fileIndex != -1) {
                val deletingFile: File = files[fileIndex]
                if (deletingFile.exists()) {
                    deletingFile.delete()
                }
            }
        }
    }
}

fun restoreDatabase(context: Context) {
    val appDatabase = SavingGoalDatabase.getDatabase(context)
    appDatabase.close()

    val file = File(context.externalCacheDir.toString(),
        "backup" + File.separator.toString() + fileName
    )

    val fileUri = FileProvider.getUriForFile(
        context,
        BuildConfig.APPLICATION_ID + ".tsgprovider",
        file
    )

    val inputStream = context.contentResolver.openInputStream(fileUri)

    // Delete the existing restoreFile and create a new one.
    deleteRestoreBackupFile(context)
    backupDatabaseForRestore(context)
    /*
     * Get database file directory
     * */
    val oldDB: String = context.getDatabasePath("saving_goal_schema").absolutePath
    val destRestore = File(oldDB)
    if (inputStream != null) {
        try {
            destRestore.delete()
            destRestore.createNewFile()
            val bufferSize = 8 * 1024
            val buffer = ByteArray(bufferSize)
            var bytesRead: Int

            val savedb: OutputStream = FileOutputStream(oldDB)
            val indb: InputStream = FileInputStream(file)
            while (indb.read(buffer, 0, bufferSize).also { bytesRead = it } > 0) {
                savedb.write(buffer, 0, bytesRead)
                println("Database restoring")
            }
            savedb.flush()
            indb.close()
            savedb.close()

            // copyFile(inputStream as FileInputStream, FileOutputStream(oldDB))
            // Take the user to home screen and there we will validate if the database file was actually restored correctly.
            println("Database restored successfully")
            deleteRestoreBackupFile(context)
        } catch (e: IOException) {
            println("ex for is of restore: $e")
            e.printStackTrace()
        }
    } else {
        println("Restore - file does not exists")
    }
}

fun copyFile(fromFile: FileInputStream, toFile: FileOutputStream) {
    var fromChannel: FileChannel? = null
    var toChannel: FileChannel? = null
    try {
        fromChannel = fromFile.channel
        toChannel = toFile.channel
        fromChannel.transferTo(0, fromChannel.size(), toChannel)
    } finally {
        try {
            fromChannel?.close()
        } finally {
            toChannel?.close()
        }
    }
}

const val rollBackFileName = "TSG_schema_rollback"

fun deleteRestoreBackupFile(context: Context) {
    val backupDir = File(context.externalCacheDir.toString(), "backup")
    val rollBackPath = backupDir.path + File.separator + rollBackFileName
    // This is to prevent deleting extra file being deleted which is mentioned in previous comment lines.
    val restoreFile = File(rollBackPath)
    if (restoreFile.exists()) {
        restoreFile.delete()
    }
}

fun backupDatabaseForRestore(context: Context) {
    val coreDataBaseFile: File = context.getDatabasePath("saving_goal_schema")
    val rollBackDir = File(context.externalCacheDir.toString(), "backup")
    val rollBackPath = rollBackDir.path + File.separator + rollBackFileName
    if (!rollBackDir.exists()) {
        rollBackDir.mkdirs()
    }
    val saveFile = File(rollBackPath)
    if (saveFile.exists()) {
        println("Backup Restore - File exists. Deleting it and then creating new file.")
        saveFile.delete()
    }
    try {
        if (saveFile.createNewFile()) {
            val bufferSize = 8 * 1024
            val buffer = ByteArray(bufferSize)
            var bytesRead = bufferSize
            val savedb: OutputStream = FileOutputStream(rollBackPath)
            val indb: InputStream = FileInputStream(coreDataBaseFile)
            while (indb.read(buffer, 0, bufferSize).also { bytesRead = it } > 0) {
                savedb.write(buffer, 0, bytesRead)
            }
            savedb.flush()
            indb.close()
            savedb.close()
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        println("ex for restore file: $e")
    }
}

private fun validFile(context: Context, fileUri: Uri): Boolean {
    val cr: ContentResolver = context.contentResolver
    val mime = cr.getType(fileUri)
    return "application/octet-stream" == mime
}
