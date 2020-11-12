package com.savings.savinggoals.buildsrc

object Versioning {
    const val major = 1
    const val minor = 6
    const val path = 5

    @JvmStatic
    fun generateVersionName() = "$major.$minor.$path"
}