package com.animeshz.github.batteryinfo

import kotlin.math.*


internal fun SYSTEM_BATTERY_STATE.toBatteryStatus(): BatteryStatus {
    val batteryFlow = when {
        charging.toInt() != 0 -> BatteryFlow.Charging
        discharging.toInt() != 0 -> BatteryFlow.Discharging
        else -> BatteryFlow.UNKNOWN
    }

    val timeToEmpty = if (batteryFlow != BatteryFlow.Discharging) -1 else (-remainingCapacity * 3600 / rate.toDouble()).toInt()

    val timeToFull = if (batteryFlow != BatteryFlow.Charging) -1 else (maxCapacity - remainingCapacity) * 3600 / rate

    val currentChargePercent = remainingCapacity.toDouble() * 100 / maxCapacity

    return BatteryStatus(
        acOnLine.toInt() != 0,
        batteryPresent.toInt() != 0,
        batteryFlow,
        maxCapacity,
        remainingCapacity,
        rate,
        estimatedTime,
        timeToEmpty,
        timeToFull,
        currentChargePercent,
        currentChargePercent.roundToInt()
    )
}
