package com.dicoding.picodiploma.loginwithanimation

import androidx.test.espresso.IdlingResource
import java.util.concurrent.atomic.AtomicInteger

class CountingIdlingResource(private val resourceName: String) : IdlingResource {

    private val counter = AtomicInteger(0)

    @Volatile
    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String {
        return resourceName
    }

    override fun isIdleNow(): Boolean {
        return counter.get() == 0
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        resourceCallback = callback
    }

    fun increment() {
        counter.getAndIncrement()
    }

    fun decrement() {
        val counterVal = counter.decrementAndGet()
        if (counterVal == 0) {
            resourceCallback?.onTransitionToIdle()
        } else if (counterVal < 0) {
            throw IllegalArgumentException("Counter has been corrupted!")
        }
    }
}