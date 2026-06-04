package com.example.data.dao

import androidx.room.*
import com.example.data.entity.DailyJournal
import com.example.data.entity.ZikirCounter
import com.example.data.entity.KhalwatRetreat
import com.example.data.entity.CustomWorshipItem
import kotlinx.coroutines.flow.Flow

@Dao
interface SulukDao {

    // --- Journal Operations ---
    @Query("SELECT * FROM daily_journal WHERE date = :date LIMIT 1")
    fun getJournalByDate(date: String): Flow<DailyJournal?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertJournal(journal: DailyJournal)

    @Query("SELECT * FROM daily_journal ORDER BY date DESC")
    fun getAllJournals(): Flow<List<DailyJournal>>

    // --- Zikir Operations ---
    @Query("SELECT * FROM zikir_counter WHERE date = :date")
    fun getZikirsByDate(date: String): Flow<List<ZikirCounter>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertZikir(zikir: ZikirCounter)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertZikirs(zikirs: List<ZikirCounter>)

    @Query("UPDATE zikir_counter SET currentCount = :count WHERE id = :id")
    suspend fun updateZikirCount(id: Int, count: Int)

    @Query("DELETE FROM zikir_counter WHERE id = :id")
    suspend fun deleteZikir(id: Int)

    // --- Khalwat Operations ---
    @Query("SELECT * FROM khalwat_retreat WHERE isActive = 1 LIMIT 1")
    fun getActiveRetreat(): Flow<KhalwatRetreat?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRetreat(retreat: KhalwatRetreat)

    @Query("UPDATE khalwat_retreat SET isActive = 0")
    suspend fun endAllRetreats()

    @Query("SELECT * FROM khalwat_retreat ORDER BY startDateMillis DESC")
    fun getAllRetreats(): Flow<List<KhalwatRetreat>>

    // --- Custom Worship Item Operations ---
    @Query("SELECT * FROM custom_worship_items WHERE date = :date")
    fun getAllCustomWorshipItems(date: String): Flow<List<CustomWorshipItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCustomWorshipItem(item: CustomWorshipItem)

    @Query("DELETE FROM custom_worship_items WHERE id = :id")
    suspend fun deleteCustomWorshipItem(id: Int)

    @Query("UPDATE custom_worship_items SET isChecked = :isChecked WHERE id = :id")
    suspend fun updateCustomWorshipItemStatus(id: Int, isChecked: Boolean)
}
