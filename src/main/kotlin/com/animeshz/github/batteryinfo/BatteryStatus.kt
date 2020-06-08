package com.animeshz.github.batteryinfo

import com.sun.jna.*
import kotlinx.coroutines.*


/**
 * @see getBatteryState to get instantaneous object of BatteryState.
 *
 * @param isAcConnected                 is the external AC supply connected?
 * @param isBatteryPresent              is the internal Battery present? true in laptops, false in desktops.
 * @param batteryFlow                   is the battery charging, discharging, or state is unknown, represented by enum [BatteryFlow].
 * @param maxCapacityMWh                maximum capacity of the battery (in mWh)
 * @param currentChargeMWh              current charge on the battery (in mWh)
 * @param flowRateMWps                  flow rate of the battery, negative when discharging and positive when charging (in mW/s)
 * @param timeToEmptyFromOS             OS determined time to empty the battery (may come from BIOS) (in s)
 * @param timeToEmpty                   time to empty (calculated) (in s)
 * @param timeToFull                    time to full the battery (calculated) (in s)
 * @param currentChargePercent          current percentage of charge in battery
 * @param currentChargeIntegralPercent  current percentage of charge in battery (rounded to [Int])
 */
data class BatteryStatus(
    val isAcConnected: Boolean,
    val isBatteryPresent: Boolean,
    val batteryFlow: BatteryFlow,
    val maxCapacityMWh: Int,
    val currentChargeMWh: Int,
    val flowRateMWps: Int,
    val timeToEmptyFromOS: Int,
    val timeToEmpty: Int,
    val timeToFull: Int,
    val currentChargePercent: Double,
    val currentChargeIntegralPercent: Int
)

/**
 * Possible types of Battery flow.
 */
enum class BatteryFlow {
    Charging,
    Discharging,
    UNKNOWN
}

/**
 * Performs blocking request into IO dispatcher to get [BatteryStatus] from low-level API
 */
suspend fun getBatteryState(): BatteryStatus? {
    return withContext(Dispatchers.IO) {
        val batteryState = SYSTEM_BATTERY_STATE()
        val retrieveValue = PowrProf.CallNtPowerInformation(
            5,
            Pointer.NULL,
            0,
            batteryState,
            batteryState.size().toLong()
        )

        if (retrieveValue == 0) batteryState.toBatteryStatus() else null
    }
}
