package com.savings.savinggoals.buildsrc

object Versioning {
    const val major = 1
    const val minor = 0
    const val path = 0

    @JvmStatic
    fun generateVersionName() = "$major.$minor.$path"
}