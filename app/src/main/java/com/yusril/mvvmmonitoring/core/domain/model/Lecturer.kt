package com.yusril.mvvmmonitoring.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lecturer(
    var name: String,
    var nidn: String,
    var token: String
) : Parcelable
