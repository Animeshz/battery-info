package com.animeshz.github.batteryinfo

import com.sun.jna.*
import com.sun.jna.win32.*


/**
 * Interface defining the PowrProf of the Windows library (in C++)
 */
interface PowrProf : StdCallLibrary {
    @Suppress("FunctionName")
    fun CallNtPowerInformation(informationLevel: Int, inBuffer: Pointer?, inBufferLen: Long, outBuffer: SYSTEM_BATTERY_STATE?, outBufferLen: Long): Int

    // TODO("Make this optional in linux and make another functionality for linux, Add dependency to OS checker")
    companion object : PowrProf by Native.load("PowrProf", PowrProf::class.java)!!
}
