package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.database.SulukDatabase
import com.example.data.entity.DailyJournal
import com.example.data.entity.ZikirCounter
import com.example.data.entity.KhalwatRetreat
import com.example.data.entity.CustomWorshipItem
import com.example.data.repository.SulukRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import android.media.MediaPlayer
import android.media.AudioAttributes

@OptIn(ExperimentalCoroutinesApi::class)
class SulukViewModel(
    application: Application,
    private val repository: SulukRepository
) : AndroidViewModel(application) {

    // Date manager
    private val _selectedDate = MutableStateFlow(getTodayDateString())
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    // Daily Journal Flow for chosen date
    val dailyJournal: StateFlow<DailyJournal?> = _selectedDate
        .flatMapLatest { date -> repository.getJournalByDate(date) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Zikir Lists Flow for chosen date
    val zikirList: StateFlow<List<ZikirCounter>> = _selectedDate
        .flatMapLatest { date -> repository.getZikirsByDate(date) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Khalwat Retreart Flow
    val activeRetreat: StateFlow<KhalwatRetreat?> = repository.getActiveRetreat()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Full history of retreats
    val allRetreats: StateFlow<List<KhalwatRetreat>> = repository.getAllRetreats()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Custom worship items for chosen date
    val customWorshipItems: StateFlow<List<CustomWorshipItem>> = _selectedDate
        .flatMapLatest { date -> repository.getAllCustomWorshipItems(date) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Azan Player State ---
    private var mediaPlayer: MediaPlayer? = null
    private var progressJob: kotlinx.coroutines.Job? = null
    private var lastTriggeredAzanKey: String = ""

    private val _azanPrayerName = MutableStateFlow<String?>(null)
    val azanPrayerName: StateFlow<String?> = _azanPrayerName.asStateFlow()

    private val _isAzanPlaying = MutableStateFlow(false)
    val isAzanPlaying: StateFlow<Boolean> = _isAzanPlaying.asStateFlow()

    private val _azanPlaybackProgress = MutableStateFlow(0f)
    val azanPlaybackProgress: StateFlow<Float> = _azanPlaybackProgress.asStateFlow()

    init {
        // Automatically seed empty dates with standard items and base journal
        viewModelScope.launch {
            _selectedDate.collect { date ->
                // Check and pre-populate zikirs
                val list = repository.getZikirsByDate(date).first()
                if (list.isEmpty()) {
                    repository.prepopulateDefaultZikirsForDate(date)
                }
                // Check and pre-populate empty journal
                val journal = repository.getJournalByDate(date).first()
                if (journal == null) {
                    repository.upsertJournal(DailyJournal(date = date))
                }
            }
        }
    }

    // --- Actions ---

    fun changeDate(dateString: String) {
        _selectedDate.value = dateString
    }

    fun selectPreviousDay() {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            val date = sdf.parse(_selectedDate.value)
            if (date != null) {
                val cal = Calendar.getInstance()
                cal.time = date
                cal.add(Calendar.DAY_OF_YEAR, -1)
                _selectedDate.value = sdf.format(cal.time)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun selectNextDay() {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            val date = sdf.parse(_selectedDate.value)
            if (date != null) {
                val cal = Calendar.getInstance()
                cal.time = date
                cal.add(Calendar.DAY_OF_YEAR, 1)
                _selectedDate.value = sdf.format(cal.time)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateJournal(updatedJournal: DailyJournal) {
        viewModelScope.launch {
            repository.upsertJournal(updatedJournal)
        }
    }

    fun toggleShalatSubuh() {
        dailyJournal.value?.let {
            updateJournal(it.copy(shalatSubuh = !it.shalatSubuh))
        }
    }

    fun toggleShalatDzuhur() {
        dailyJournal.value?.let {
            updateJournal(it.copy(shalatDzuhur = !it.shalatDzuhur))
        }
    }

    fun toggleShalatAshar() {
        dailyJournal.value?.let {
            updateJournal(it.copy(shalatAshar = !it.shalatAshar))
        }
    }

    fun toggleShalatMaghrib() {
        dailyJournal.value?.let {
            updateJournal(it.copy(shalatMaghrib = !it.shalatMaghrib))
        }
    }

    fun toggleShalatIsya() {
        dailyJournal.value?.let {
            updateJournal(it.copy(shalatIsya = !it.shalatIsya))
        }
    }

    fun toggleTahajjud() {
        dailyJournal.value?.let {
            updateJournal(it.copy(tahajjud = !it.tahajjud))
        }
    }

    fun toggleDhuha() {
        dailyJournal.value?.let {
            updateJournal(it.copy(dhuha = !it.dhuha))
        }
    }

    fun toggleRawatib() {
        dailyJournal.value?.let {
            updateJournal(it.copy(rawatib = !it.rawatib))
        }
    }

    fun toggleShalatIsyraq() {
        dailyJournal.value?.let {
            updateJournal(it.copy(shalatIsyraq = !it.shalatIsyraq))
        }
    }

    fun toggleWudhu() {
        dailyJournal.value?.let {
            updateJournal(it.copy(wudhu = !it.wudhu))
        }
    }

    fun updateFastingType(type: String) {
        dailyJournal.value?.let {
            updateJournal(it.copy(fastingType = type))
        }
    }

    fun updateFastingNotes(notes: String) {
        dailyJournal.value?.let {
            updateJournal(it.copy(fastingNotes = notes))
        }
    }

    fun toggleZikirPagi() {
        dailyJournal.value?.let {
            updateJournal(it.copy(zikirPagi = !it.zikirPagi))
        }
    }

    fun toggleZikirSore() {
        dailyJournal.value?.let {
            updateJournal(it.copy(zikirSore = !it.zikirSore))
        }
    }

    fun togglePuasa() {
        dailyJournal.value?.let {
            updateJournal(it.copy(riyadhohPuasa = !it.riyadhohPuasa))
        }
    }

    fun toggleRiyadhohSabar() {
        dailyJournal.value?.let {
            updateJournal(it.copy(riyadhohSabar = !it.riyadhohSabar))
        }
    }

    fun updateTilawahPages(pages: Int) {
        dailyJournal.value?.let {
            updateJournal(it.copy(tilawahPage = pages))
        }
    }

    // --- Zikir Counter Actions ---

    fun incrementZikirCount(zikirId: Int, current: Int) {
        viewModelScope.launch {
            repository.updateZikirCount(zikirId, current + 1)
        }
    }

    fun decrementZikirCount(zikirId: Int, current: Int) {
        if (current <= 0) return
        viewModelScope.launch {
            repository.updateZikirCount(zikirId, current - 1)
        }
    }

    fun resetZikirCount(zikirId: Int) {
        viewModelScope.launch {
            repository.updateZikirCount(zikirId, 0)
        }
    }

    fun addCustomZikir(name: String, target: Int, arabic: String, translation: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.upsertZikir(
                ZikirCounter(
                    date = _selectedDate.value,
                    zikirKey = "custom",
                    name = name,
                    arabic = arabic,
                    translation = translation,
                    target = target,
                    currentCount = 0
                )
            )
        }
    }

    fun deleteZikirCounter(id: Int) {
        viewModelScope.launch {
            repository.deleteZikir(id)
        }
    }

    // --- Khalwat Actions ---

    fun beginKhalwatRetreat(days: Int, notes: String) {
        viewModelScope.launch {
            repository.startKhalwat(days, notes)
        }
    }

    fun stopKhalwatRetreat() {
        viewModelScope.launch {
            repository.endKhalwat()
        }
    }

    fun advanceKhalwatDay() {
        viewModelScope.launch {
            activeRetreat.value?.let {
                val targetDay = it.currentDayOfRetreat + 1
                if (targetDay <= it.targetDays) {
                    repository.updateKhalwatDay(it, targetDay)
                } else {
                    // Completed retreat successfully!
                    repository.endKhalwat()
                }
            }
        }
    }

    fun decreaseKhalwatDay() {
        viewModelScope.launch {
            activeRetreat.value?.let {
                if (it.currentDayOfRetreat > 1) {
                    repository.updateKhalwatDay(it, it.currentDayOfRetreat - 1)
                }
            }
        }
    }

    // --- Custom Worship Item Actions ---

    fun toggleCustomWorshipItem(item: CustomWorshipItem) {
        viewModelScope.launch {
            repository.updateCustomWorshipItemStatus(item.id, !item.isChecked)
        }
    }

    fun addCustomWorshipItem(label: String, category: String) {
        if (label.isBlank()) return
        viewModelScope.launch {
            repository.upsertCustomWorshipItem(
                CustomWorshipItem(
                    date = _selectedDate.value,
                    category = category,
                    label = label,
                    isChecked = false
                )
            )
        }
    }

    fun deleteCustomWorshipItem(id: Int) {
        viewModelScope.launch {
            repository.deleteCustomWorshipItem(id)
        }
    }

    // --- Azan Media Control Logic ---

    fun getActivePrayerTimeName(): String? {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val currentMinutes = hour * 60 + minute

        // Prayer times matching Beranda cards:
        // Fajr (04:35), Duh'r (12:05), Asr (15:20), Magrib (18:05), Isha'a (19:15)
        val fajrMinutes = 4 * 60 + 35
        val duhrMinutes = 12 * 60 + 5
        val asrMinutes = 15 * 60 + 20
        val magribMinutes = 18 * 60 + 5
        val isyaMinutes = 19 * 60 + 15

        val windowSize = 15 // Active for 15 minutes after the time starts

        return when {
            currentMinutes in fajrMinutes until (fajrMinutes + windowSize) -> "Fajr"
            currentMinutes in duhrMinutes until (duhrMinutes + windowSize) -> "Duh'r"
            currentMinutes in asrMinutes until (asrMinutes + windowSize) -> "Asr"
            currentMinutes in magribMinutes until (magribMinutes + windowSize) -> "Magrib"
            currentMinutes in isyaMinutes until (isyaMinutes + windowSize) -> "Isha'a"
            else -> null
        }
    }

    fun checkAzanTrigger() {
        val prayerName = getActivePrayerTimeName()
        if (prayerName != null) {
            val todayStr = getTodayDateString()
            val triggerKey = "$prayerName-$todayStr"
            if (lastTriggeredAzanKey != triggerKey) {
                lastTriggeredAzanKey = triggerKey
                playAzan(prayerName)
            }
        }
    }

    fun playAzan(prayerName: String) {
        _azanPrayerName.value = prayerName
        _isAzanPlaying.value = true
        _azanPlaybackProgress.value = 0f

        viewModelScope.launch {
            try {
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                mediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    // Beautiful public Adhan stream URL (Makkah Adhan via QuranicAudio)
                    setDataSource("https://download.quranicaudio.com/adhan/makkah.mp3")
                    prepareAsync()
                    setOnPreparedListener { mp ->
                        if (_isAzanPlaying.value) {
                            mp.start()
                            startProgressPolling()
                        } else {
                            mp.release()
                        }
                    }
                    setOnCompletionListener {
                        stopAzan()
                    }
                    setOnErrorListener { _, _, _ ->
                        startFakePlaybackProgress()
                        true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                startFakePlaybackProgress()
            }
        }
    }

    private fun startProgressPolling() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (_isAzanPlaying.value) {
                mediaPlayer?.let { mp ->
                    try {
                        if (mp.isPlaying && mp.duration > 0) {
                            _azanPlaybackProgress.value = mp.currentPosition.toFloat() / mp.duration
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                kotlinx.coroutines.delay(500)
            }
        }
    }

    private fun startFakePlaybackProgress() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            var progress = 0f
            while (progress < 1.0f && _isAzanPlaying.value) {
                progress += 0.015f
                _azanPlaybackProgress.value = progress.coerceAtMost(1f)
                kotlinx.coroutines.delay(500)
            }
            if (progress >= 1.0f) {
                stopAzan()
            }
        }
    }

    fun stopAzan() {
        _isAzanPlaying.value = false
        _azanPrayerName.value = null
        _azanPlaybackProgress.value = 0f
        progressJob?.cancel()
        viewModelScope.launch {
            try {
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun pauseAzan() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.pause()
                    _isAzanPlaying.value = false
                }
            } ?: run {
                _isAzanPlaying.value = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _isAzanPlaying.value = false
        }
    }

    fun resumeAzan() {
        try {
            mediaPlayer?.let {
                if (!it.isPlaying) {
                    it.start()
                    _isAzanPlaying.value = true
                    startProgressPolling()
                }
            } ?: run {
                playAzan(_azanPrayerName.value ?: "Magrib")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCleared() {
        super.onCleared()
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        progressJob?.cancel()
    }

    // --- Date Format Helpers ---

    fun getFormattedDateLabel(): String {
        val sdfInput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sdfOutput = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id", "ID"))
        return try {
            val date = sdfInput.parse(_selectedDate.value)
            if (date != null) sdfOutput.format(date) else _selectedDate.value
        } catch (e: Exception) {
            _selectedDate.value
        }
    }

    companion object {
        fun getTodayDateString(): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return sdf.format(Date())
        }
    }
}

class SulukViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SulukViewModel::class.java)) {
            val database = SulukDatabase.getDatabase(application)
            val repository = SulukRepository(database.sulukDao())
            @Suppress("UNCHECKED_CAST")
            return SulukViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
