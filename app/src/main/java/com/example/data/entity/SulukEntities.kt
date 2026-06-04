package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_journal")
data class DailyJournal(
    @PrimaryKey val date: String, // Format: YYYY-MM-DD
    val shalatSubuh: Boolean = false,
    val shalatDzuhur: Boolean = false,
    val shalatAshar: Boolean = false,
    val shalatMaghrib: Boolean = false,
    val shalatIsya: Boolean = false,
    val tahajjud: Boolean = false,
    val dhuha: Boolean = false,
    val rawatib: Boolean = false,
    val shalatIsyraq: Boolean = false, // Shalat Isyraq
    val wudhu: Boolean = false, // Mudawamah Wudhu (menjaga kesucian)
    val tilawahPage: Int = 0,
    val zikirPagi: Boolean = false,
    val zikirSore: Boolean = false,
    val riyadhohPuasa: Boolean = false,
    val riyadhohSabar: Boolean = false, // Latihan menahan amarah/nafsu
    val fastingType: String = "", // "none", "daud", "senin_kamis", "diam", "tidak_bernyawa", "lainnya"
    val fastingNotes: String = "", // Jurnal tambahan puasa
    val notes: String = ""
)

@Entity(tableName = "zikir_counter")
data class ZikirCounter(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String, // Format: YYYY-MM-DD
    val zikirKey: String, // Unique key for pre-populated (e.g. "istighfar") or "custom"
    val name: String,
    val arabic: String = "",
    val translation: String = "",
    val target: Int = 100,
    val currentCount: Int = 0
)

@Entity(tableName = "khalwat_retreat")
data class KhalwatRetreat(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val startDateMillis: Long = System.currentTimeMillis(),
    val targetDays: Int = 40,
    val isActive: Boolean = true,
    val currentDayOfRetreat: Int = 1,
    val note: String = ""
)

@Entity(tableName = "custom_worship_items")
data class CustomWorshipItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String, // Format: YYYY-MM-DD
    val category: String, // "sunnah_qiyam", "tazkiyah_tilawah", "riyadhoh_nafs"
    val label: String,
    val isChecked: Boolean = false
)

