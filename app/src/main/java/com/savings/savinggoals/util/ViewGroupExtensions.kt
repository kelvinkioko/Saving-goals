package com.savings.savinggoals.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks

fun ViewGroup.inflate(
    @LayoutRes resource: Int,
    root: ViewGroup? = null,
    attachToRoot: Boolean = false
): View = LayoutInflater.from(context).inflate(resource, root, attachToRoot)

@FlowPreview
@ExperimentalCoroutinesApi
inline fun View.debouncedClick(scope: CoroutineScope, crossinline onClickAction: () -> Unit) {
    this.clicks().debounce(350).onEach {
        onClickAction.invoke()
    }.launchIn(scope)
}
