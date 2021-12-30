package com.tms.lesson01.musicgalleryapplication.mvvm.ui.playlist.fragment

import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class TopArtistRecyclerItemDecoration(private val edgeOffset: Int = 0) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        super.getItemOffsets(outRect, view, parent, state)

        // Узнаём позицию обрабатываемого item
        val position = parent.getChildAdapterPosition(view)
        // Количество всех элементов в Recycler VIew
        val itemCount = parent.adapter?.itemCount ?: 0

        // Отступы по умолчанию
        outRect.left = 0
        outRect.right = 0

        when (position) {
            // Последний элемент
            itemCount - 1 -> {
                outRect.left = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    edgeOffset.toFloat(),
                    view.context.resources.displayMetrics
                ).toInt()
                outRect.right = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    edgeOffset.toFloat(),
                    view.context.resources.displayMetrics
                ).toInt()
            }
            else -> {
                outRect.left = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    edgeOffset.toFloat(),
                    view.context.resources.displayMetrics
                ).toInt()
            }
        }
    }
}