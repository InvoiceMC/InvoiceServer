package org.invoice.display

sealed interface DisplayEffect {
    fun play()
    fun stop()
}