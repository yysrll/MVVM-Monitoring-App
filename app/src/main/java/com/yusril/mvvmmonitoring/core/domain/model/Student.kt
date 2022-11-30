package com.yusril.mvvmmonitoring.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Student(
    var id: Int,
    var nim: String,
    var name: String? = null,
    var gpa: String,
    var year: String? = null,
    var sks: Int,
    var krsId: Int? = null,
    var krsIsCurrent: Boolean? = false,
    var krsIsLocked: Boolean? = false,
    var krsIsApproved: Boolean? = false,
) : Parcelable
