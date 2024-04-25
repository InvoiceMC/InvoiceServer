package org.invoice.display

import org.invoice.display.effects.PulsingDisplayEffect
import org.invoice.display.effects.BouncingDisplayEffect

/**
 * The main interface for all display effects.
 *
 * @see BouncingDisplayEffect
 * @see PulsingDisplayEffect
 * @since 0.0.1
 * @author Outspending
 */
sealed interface DisplayEffect {
    fun play()
    fun stop()
}