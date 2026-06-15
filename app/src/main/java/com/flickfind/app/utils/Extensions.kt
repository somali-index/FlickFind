package com.flickfind.app.utils

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment

fun View.show() { visibility = View.VISIBLE }
fun View.hide() { visibility = View.GONE }
fun View.invisible() { visibility = View.INVISIBLE }

fun View.fadeIn(duration: Long = 300) {
    alpha = 0f
    show()
    animate().alpha(1f).setDuration(duration).start()
}

fun View.fadeOut(duration: Long = 200, onEnd: (() -> Unit)? = null) {
    animate().alpha(0f).setDuration(duration).withEndAction {
        hide()
        onEnd?.invoke()
    }.start()
}

fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
fun Fragment.toast(msg: String) = requireContext().toast(msg)

fun String.toInitial(): String = if (isNotBlank()) first().uppercase() else "?"

fun Double.toRatingString(): String = "%.1f".format(this)

// Genre id -> Vietnamese name map
val GENRE_MAP = mapOf(
    28 to "Hành động",
    12 to "Phiêu lưu",
    16 to "Hoạt hình",
    35 to "Hài hước",
    80 to "Tội phạm",
    99 to "Tài liệu",
    18 to "Tâm lý",
    10751 to "Gia đình",
    14 to "Giả tưởng",
    36 to "Lịch sử",
    27 to "Kinh dị",
    10402 to "Âm nhạc",
    9648 to "Bí ẩn",
    10749 to "Tình cảm",
    878 to "Khoa học viễn tưởng",
    10770 to "TV Movie",
    53 to "Hồi hộp",
    10752 to "Chiến tranh",
    37 to "Miền Tây"
)
