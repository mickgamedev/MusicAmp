package ru.yandex.dunaev.mick.musicamp.util

import android.content.Context
import android.widget.Toast

fun Context.toast(st: String) = Toast.makeText(this, st, Toast.LENGTH_SHORT).show()