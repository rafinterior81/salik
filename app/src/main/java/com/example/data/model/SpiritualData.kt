package com.example.data.model

data class KalamQuote(
    val id: Int,
    val author: String,
    val book: String,
    val quoteArabic: String = "",
    val quoteIndo: String,
    val commentary: String
)

data class AdabItem(
    val category: String,
    val title: String,
    val points: List<String>
)

data class QuranHadistItem(
    val source: String, // e.g. "QS. Al-Kahfi: 28" or "HR. Bukhari"
    val category: String, // e.g. "Zikir", "Ikhlas", "Tazkiyah"
    val arabic: String,
    val translation: String,
    val explanation: String
)

data class NafsStage(
    val stageName: String,
    val meaning: String,
    val traits: List<String>,
    val treatment: String
)

object SpiritualData {

    val kalamQuotes = listOf(
        KalamQuote(
            id = 1,
            author = "Imam Al-Ghazali",
            book = "Ihya Ulumuddin",
            quoteArabic = "النِّيَّةُ أَسَاسُ الْعَمَلِ، وَبِهَا يَصِيرُ الْعَادَةُ عِبَادَةً",
            quoteIndo = "Niat adalah dasar dari sebuah amal, dan dengan niat yang benar, kebiasaan sehari-hari dapat berubah menjadi ibadah bernilai tinggi.",
            commentary = "Dalam menempuh jalan suluk, seorang salik harus terus-menerus memurnikan niatnya dari tendensi duniawi agar seluruh detak jantungnya menjadi ibadah kepada Allah."
        ),
        KalamQuote(
            id = 2,
            author = "Syekh Ibnu Atha'illah Al-Iskandari",
            book = "Al-Hikam",
            quoteArabic = "مَا كَانَ عَمَلٌ بَرَزَ عَنْ قَلْبٍ زَاهِدٍ ، وَلَا كَثُرَ عَمَلٌ بَرَزَ عَنْ قَلْبٍ رَاغِبٍ",
            quoteIndo = "Amal yang lahir dari hati yang zuhud tidak akan terasa sedikit, dan amal yang timbul dari hati yang tamak pada dunia tidak akan bernilai banyak.",
            commentary = "Fokus suluk adalah kebersihan hati penempuhnya, bukan sekadar kuantitas gerakan fisik biasa tanpa kesadaran makrifat."
        ),
        KalamQuote(
            id = 3,
            author = "Syekh Ibnu Atha'illah Al-Iskandari",
            book = "Al-Hikam",
            quoteArabic = "إِذَا أَرَدْتَ أَنْ تَعْرِفَ قَدْرَكَ عِنْدَهُ فَانْظُرْ فِيمَا يُقِيمُكَ",
            quoteIndo = "Jika engkau ingin mengetahui kedudukanmu di sisi-Nya, maka perhatikanlah dalam amalan apa Dia menempatkan dirimu sekarang.",
            commentary = "Bila engkau disibukkan dengan zikir, salat malam, dan menuntut ilmu tasawuf, bersyukurlah bahwa Allah sedang menempatkanmu dalam rida-Nya."
        ),
        KalamQuote(
            id = 4,
            author = "Syekh Abdul Qadir Al-Jilani",
            book = "Futuhul Ghaib",
            quoteArabic = "مُتْ عَنْ نَفْسِكَ وَعَنْ خَلْقِكَ، تَحْيَ بِرَبِّكَ",
            quoteIndo = "Matikanlah dirimu dari hawa nafsumu dan dari bergantung pada mahluk, niscaya engkau akan hidup bersama Tuhanmu.",
            commentary = "Kematian ego (fana'ul hawa) adalah prasyarat mutlak untuk merasakan keagungan-Nya (baqa' billah) dalam perjalanan ruhani."
        ),
        KalamQuote(
            id = 5,
            author = "Maulana Jalaluddin Rumi",
            book = "Matsnawi",
            quoteArabic = "أَنَا مَحْوٌ، وَالْعِشْقُ هُوَ السَّبِيلُ إِلَى اللهِ",
            quoteIndo = "Aku sirna, dan Cinta-lah satu-satunya jalan lurus menuju haribaan Allah.",
            commentary = "Sufi adalah pencinta sejati yang terbakar rindu kepada Sang Kekasih. Zikir adalah ungkapan kerinduan Salik kepada Khalik-Nya."
        ),
        KalamQuote(
            id = 6,
            author = "Imam Al-Ghazali",
            book = "Kimia Kebahagiaan",
            quoteArabic = "مَنْ عَرَفَ نَفْسَهُ فَقَدْ عَرَفَ رَبَّهُ",
            quoteIndo = "Barangsiapa mengenal jati diri jiwanya sendiri, sungguh ia akan mengenal Tuhannya.",
            commentary = "Proses pengenalan jiwa diawali dengan Tazkiyatun Nafs (pembersihan jiwa) agar cermin hati dapat memantulkan cahaya ketuhanan."
        )
    )

    val adabMurid = listOf(
        AdabItem(
            category = "Adab kepada Allah SWT",
            title = "Kesucian Niat & Kehambaan",
            points = listOf(
                "Merasakan muraqabah (selalu diawasi Allah) dalam setiap helaan nafas.",
                "Mengakui kehinaan diri di hadapan kebesaran dan kesucian Allah.",
                "Ikhlas total tanpa mengharap karamah atau pujian manusia.",
                "Senantiasa menjaga wudhu (mudawamul wudhu) guna memelihara kesucian lahir bathin."
            )
        ),
        AdabItem(
            category = "Adab kepada Mursyid (Guru Ruhani)",
            title = "Kepatuhan & Bakti",
            points = listOf(
                "Menghormati guru baik di hadapan beliau maupun di belakang beliau.",
                "Mendengar nasihat dan bimbingan Mursyid dengan khidmat (sam'an wa tha'atan).",
                "Menjauhi perdebatan atau membantah keputusan spiritual guru.",
                "Mendoakan keselamatan, kesehatan, dan kelimpahan rahmat bagi Mursyid setiap selesai wirid."
            )
        ),
        AdabItem(
            category = "Adab kepada Sesama Salik",
            title = "Ukhuwah & Menutup Aib",
            points = listOf(
                "Melihat sesama ikhwan thariqah dengan kacamata kasih sayang (husnudzon).",
                "Mendahulukan kepentingan saudara se-suluk di atas kepentingan pribadi (Itsar).",
                "Tidak bersaing dalam urusan maqam spiritual atau keistimewaan lahiriah.",
                "Saling menopang dan mengingatkan dalam kebaikan dengan kelembutan."
            )
        ),
        AdabItem(
            category = "Adab kepada Diri Sendiri",
            title = "Muhasabah & Riyadhoh",
            points = listOf(
                "Mengevaluasi niat dan dosa setiap malam menjelang tidur.",
                "Tidak memanjakan nafsu makan, tidur berlebihan, dan bicara sia-sia.",
                "Menerima takdir dengan kerelaan hati (ridha dan taslim).",
                "Istiqamah mengulang wirid harian meski dalam keadaan malas atau letih."
            )
        )
    )

    val quranHadistList = listOf(
        QuranHadistItem(
            source = "QS. Asy-Syams: 9-10",
            category = "Tazkiyah",
            arabic = "قَدْ أَفْلَحَ مَن زَكَّاهَا . وَقَدْ خَابَ مَن دَسَّاهَا",
            translation = "Sesungguhnya beruntunglah orang yang mensucikan jiwa itu, dan sesungguhnya merugilah orang yang mengotorinya.",
            explanation = "Ayat dasar thariqah tasawuf. Keselamatan akhirat digantungkan pada sejauh mana kita menyuling dan membersihkan nafsu kita."
        ),
        QuranHadistItem(
            source = "QS. Al-Kahfi: 28",
            category = "Suluk / Bersama Guru",
            arabic = "وَاصْبِرْ نَفْسَكَ مَعَ الَّذِينَ يَدْعُونَ رَبَّهُم بِالْغَدَاةِ وَالْعَشِيِّ يُرِيدُونَ وَجْهَهُۥ",
            translation = "Dan bersabarlah kamu bersama-sama dengan orang-orang yang menyeru Tuhannya di pagi dan senja hari dengan mengharap keridaan-Nya...",
            explanation = "Perintah untuk senantiasa hidup rukun, berzikir berjamaah, dan menuntut ilmu spiritual dalam naungan guru mursyid pencerah kalbu."
        ),
        QuranHadistItem(
            source = "HR. Bukhari & Muslim",
            category = "Segumpal Daging (Qolb)",
            arabic = "أَلَا وَإِنَّ فِي الْجَسَدِ مُضْغَةً إِذَا صَلَحَتْ صَلَحَ الْجَسَدُ كُلُّهُ وَإِذَا فَسَدَتْ فَسَدَ الْجَسَدُ كُلُّهُ أَلَا وَهِيَ الْقَلْبُ",
            translation = "Ingatlah bahwa di dalam jasad ada segumpal daging. Jika ia baik maka seluruh jasad akan baik, dan jika ia rusak maka seluruh jasad pun rusak. Ketahuilah ia adalah Qolbu.",
            explanation = "Tasawuf menitikberatkan perbaikan kalbu ini. Jika nafsu ego luluh, qolbu bercahaya dan mengarahkan raga menuju ketaatan tulus."
        ),
        QuranHadistItem(
            source = "HR. Tirmidzi",
            category = "Muraqabah / Zikir",
            arabic = "أَنَّ تَعْبُدَ اللَّهَ كَأَنَّكَ تَرَاهُ فَإِنْ لَمْ تَكُنْ تَرَاهُ فَإِنَّهُ يَرَاكَ",
            translation = "Engkau beribadah kepada Allah seakan-akan engkau melihat-Nya, jika engkau tidak melihat-Nya, maka sesungguhnya Dia melihatmu.",
            explanation = "Maqam Ihsan adalah puncak tertinggi salik. Inti dari zikir jahar dan khafi adalah mendatangkan kesadaran konstan bahwa Allah selalu mengawasi sanubari."
        )
    )

    val nafsStages = listOf(
        NafsStage(
            stageName = "1. Nafs Ammarah",
            meaning = "Jiwa yang memerintahkan keburukan",
            traits = listOf("Kikir", "Tamak/Sifat loba", "Dengki/Hasad", "Amarah tak terkontrol", "Sombong/Takabbur"),
            treatment = "Mujahadah ketat, banyak istighfar, puasa sunnah, menundukkan pandangan dan ego lahiriah."
        ),
        NafsStage(
            stageName = "2. Nafs Lawwamah",
            meaning = "Jiwa yang suka mencela diri saat berdosa",
            traits = listOf("Menyesal setelah dosa", "Khawatir akan azab", "Kadang taat kadang maksiat", "Belum kokoh"),
            treatment = "Mendisiplinkan shalat rawatib, menjaga wirid pagi-sore, menghadiri majelis taklim guru."
        ),
        NafsStage(
            stageName = "3. Nafs Mulhamah",
            meaning = "Jiwa yang memperoleh ilham kebaikan",
            traits = listOf("Dermawan", "Qana'ah (puas akan rezeki)", "Ilmu hikmah", "Tawadhu sejati"),
            treatment = "Fokus pada menyepi (khalwat ringan), bersyukur, memperbanyak tahlil, menjaga keheningan jiwa."
        ),
        NafsStage(
            stageName = "4. Nafs Mutma'innah",
            meaning = "Jiwa yang tenang dan damai dalam rida",
            traits = listOf("Iklhas mutlak", "Sabar paripurna", "Tawakkal sempurna", "Dzikir tak terputus"),
            treatment = "Istiqamah menjaga kemurnian tauhid, fana' (lebur) dalam kecintaan kepada Allah SWT harian."
        )
    )

    val riyadhohGuidelines = listOf(
        "**Shalat Berjamaah Awal Waktu**: Sumbu utama kedisplinan lahiriah seorang murid.",
        "**Qiyamul Lail (Tahajjud & Witir)**: Sunyi malam merupakan jamuan istimewa untuk berkomunikasi mesra dengan Ilahi.",
        "**Mudawamatul Wudhu**: Bersungguh-sungguh memperbarui wudhu setiap kali batal. Menjaga wudhu berarti membentengi diri dari bisikan syetan lahir bathin.",
        "**Hifdzul Lisan (Menjaga Lidah)**: Berbicara hanya jika bernilai ibadah atau kemaslahatan nyata. Menghindari ghibah, dusta, dan debat kusir.",
        "**Qillatud-Dhahak (Sedikit Tertawa)**: Menjaga kehormatan hati agar tidak mati oleh canda gurau berlebih yang melalaikan dari maut.",
        "**Khidmat**: Mengambil peran melayani orang tua, guru, ikhwan salik, dan mahluk Allah tanpa pamrih lahiriah."
    )
}
