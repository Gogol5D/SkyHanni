package at.hannibal2.skyhanni.features.event.hoppity

import at.hannibal2.skyhanni.utils.SimpleTimeMark.Companion.asTimeMark
import at.hannibal2.skyhanni.utils.SkyBlockTime
import kotlin.time.Duration

enum class HoppityEggType(
    val mealName: String,
    private val mealColor: String,
    val resetsAt: Int,
    var lastResetDay: Int = -1,
    private var claimed: Boolean = false,
) {
    BREAKFAST("Breakfast", "§6", 7),
    LUNCH("Lunch", "§9", 14),
    DINNER("Dinner", "§a", 21),
    ;

    fun timeUntil(): Duration {
        val now = SkyBlockTime.now()
        if (now.hour >= resetsAt) {
            return now.copy(day = now.day + 1, hour = resetsAt, minute = 0, second = 0)
                .asTimeMark().timeUntil()
        }
        return now.copy(hour = resetsAt, minute = 0, second = 0).asTimeMark().timeUntil()
    }

    fun markClaimed() {
        claimed = true
    }

    fun markSpawned() {
        claimed = false
    }

    fun isClaimed() = claimed
    val formattedName get() = "${if (isClaimed()) "§7§m" else mealColor}$mealName:$mealColor"
    val coloredName get() = "$mealColor$mealName"

    companion object {
        fun allFound() = entries.forEach { it.markClaimed() }

        fun getMealByName(mealName: String) = entries.find { it.mealName == mealName }

        fun checkClaimed() {
            val currentSbTime = SkyBlockTime.now()
            val currentSbDay = currentSbTime.day
            val currentSbHour = currentSbTime.hour

            for (eggType in entries) {
                if (currentSbHour < eggType.resetsAt || eggType.lastResetDay == currentSbDay) continue
                eggType.markSpawned()
                eggType.lastResetDay = currentSbDay
                if (HoppityEggLocator.currentEggType == eggType) {
                    HoppityEggLocator.currentEggType = null
                    HoppityEggLocator.currentEggNote = null
                    HoppityEggLocator.sharedEggLocation = null
                }
            }
        }

        fun eggsRemaining(): Boolean {
            return entries.any { !it.claimed }
        }

        fun allEggsRemaining(): Boolean {
            return entries.all { !it.claimed }
        }
    }
}
