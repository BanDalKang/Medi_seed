package com.mediseed.mediseed.ui.bottomSheet

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mediseed.mediseed.utils.Event

open class SingleLiveEvent<T> : MutableLiveData<Event<T>>() {

    override fun observe(owner: LifecycleOwner, observer: Observer<in Event<T>>) {
        super.observe(owner) { event ->
            event?.getContentIfNotHandled()?.let { value ->
                observer.onChanged(Event(value))
            }
        }
    }

    override fun setValue(value: Event<T>?) {
        super.setValue(value)
    }

    fun call(value: T) {
        setValue(Event(value))
    }
}