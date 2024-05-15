package com.credenza3.credenzapassport.model

enum class ScanType(name: String) {
    AirDrop("AIR_DROP"),
    RequestLoyaltyPoints("REQUEST_LOYALTY_POINTS");

    companion object {
        fun fromName(name: String): ScanType? =
            entries.firstOrNull { it.name == name }
    }
}