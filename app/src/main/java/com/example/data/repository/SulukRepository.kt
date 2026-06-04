package com.example.data.repository

import com.example.data.dao.SulukDao
import com.example.data.entity.DailyJournal
import com.example.data.entity.ZikirCounter
import com.example.data.entity.KhalwatRetreat
import com.example.data.entity.CustomWorshipItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class SulukRepository(private val sulukDao: SulukDao) {

    // --- Journal ---
    fun getJournalByDate(date: String): Flow<DailyJournal?> = sulukDao.getJournalByDate(date)

    fun getAllJournals(): Flow<List<DailyJournal>> = sulukDao.getAllJournals()

    suspend fun upsertJournal(journal: DailyJournal) {
        sulukDao.upsertJournal(journal)
    }

    // --- Zikir ---
    fun getZikirsByDate(date: String): Flow<List<ZikirCounter>> = sulukDao.getZikirsByDate(date)

    suspend fun upsertZikir(zikir: ZikirCounter) {
        sulukDao.upsertZikir(zikir)
    }

    suspend fun updateZikirCount(id: Int, count: Int) {
        sulukDao.updateZikirCount(id, count)
    }

    suspend fun deleteZikir(id: Int) {
        sulukDao.deleteZikir(id)
    }

    // Helper to populate default zikir objects for a specific date if they don't exist
    suspend fun prepopulateDefaultZikirsForDate(date: String) {
        val defaultList = listOf(
            ZikirCounter(
                date = date,
                zikirKey = "istighfar",
                name = "Istighfar",
                arabic = "أَسْتَغْفِرُ اللهَ الْعَظِيمَ",
                translation = "Aku memohon ampun kepada Allah yang Maha Agung.",
                target = 100,
                currentCount = 0
            ),
            ZikirCounter(
                date = date,
                zikirKey = "sholawat",
                name = "Sholawat Nabi",
                arabic = "اللَّهُمَّ صَلِّ عَلَى سَيِّدِنَا مُحَمَّدٍ",
                translation = "Ya Allah, limpahkanlah rahmat kepada junjungan kami Nabi Muhammad.",
                target = 100,
                currentCount = 0
            ),
            ZikirCounter(
                date = date,
                zikirKey = "tahlil",
                name = "Tahlil / Nafyul Itsbat",
                arabic = "لَا إِلَهَ إِلَّا اللهُ",
                translation = "Tiada Tuhan selain Allah.",
                target = 100,
                currentCount = 0
            ),
            ZikirCounter(
                date = date,
                zikirKey = "tasbih_tahmid",
                name = "Tasbih & Tahmid",
                arabic = "سُبْحَانَ اللهِ وَبِحَمْدِهِ",
                translation = "Maha Suci Allah dan segala puji bagi-Nya.",
                target = 100,
                currentCount = 0
            ),
            ZikirCounter(
                date = date,
                zikirKey = "asma_allah",
                name = "Zikir Ism Dzat (Ya Allah)",
                arabic = "يَا اللهُ",
                translation = "Wahai Allah (Fase meresapi keagungan Allah dalam sanubari).",
                target = 300,
                currentCount = 0
            )
        )
        sulukDao.insertZikirs(defaultList)
    }

    // --- Khalwat ---
    fun getActiveRetreat(): Flow<KhalwatRetreat?> = sulukDao.getActiveRetreat()

    fun getAllRetreats(): Flow<List<KhalwatRetreat>> = sulukDao.getAllRetreats()

    suspend fun startKhalwat(targetDays: Int, note: String) {
        sulukDao.endAllRetreats() // Close any existing one
        val retreat = KhalwatRetreat(
            startDateMillis = System.currentTimeMillis(),
            targetDays = targetDays,
            isActive = true,
            currentDayOfRetreat = 1,
            note = note
        )
        sulukDao.upsertRetreat(retreat)
    }

    suspend fun endKhalwat() {
        sulukDao.endAllRetreats()
    }

    suspend fun updateKhalwatDay(retreat: KhalwatRetreat, day: Int) {
        val updated = retreat.copy(currentDayOfRetreat = day)
        sulukDao.upsertRetreat(updated)
    }

    // --- Custom Worship Items ---
    fun getAllCustomWorshipItems(date: String): Flow<List<CustomWorshipItem>> =
        sulukDao.getAllCustomWorshipItems(date)

    suspend fun upsertCustomWorshipItem(item: CustomWorshipItem) {
        sulukDao.upsertCustomWorshipItem(item)
    }

    suspend fun deleteCustomWorshipItem(id: Int) {
        sulukDao.deleteCustomWorshipItem(id)
    }

    suspend fun updateCustomWorshipItemStatus(id: Int, isChecked: Boolean) {
        sulukDao.updateCustomWorshipItemStatus(id, isChecked)
    }
}
