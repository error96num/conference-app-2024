package io.github.droidkaigi.confsched.model

import io.github.droidkaigi.confsched.model.RoomType.RoomF
import io.github.droidkaigi.confsched.model.TimetableItem.Session
import io.github.droidkaigi.confsched.model.TimetableSessionType.NORMAL
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

public sealed class TimetableItem {
    public abstract val id: TimetableItemId
    public abstract val title: MultiLangText
    public abstract val startsAt: Instant
    public abstract val endsAt: Instant
    public abstract val category: TimetableCategory
    public abstract val sessionType: TimetableSessionType
    public abstract val room: TimetableRoom
    public abstract val targetAudience: String
    public abstract val language: TimetableLanguage
    public abstract val asset: TimetableAsset
    public abstract val levels: PersistentList<String>
    public abstract val speakers: PersistentList<TimetableSpeaker>
    public val day: DroidKaigi2024Day? get() = DroidKaigi2024Day.ofOrNull(startsAt)

    @Serializable
    public data class Session(
        override val id: TimetableItemId,
        override val title: MultiLangText,
        override val startsAt: Instant,
        override val endsAt: Instant,
        override val category: TimetableCategory,
        override val sessionType: TimetableSessionType,
        override val room: TimetableRoom,
        override val targetAudience: String,
        override val language: TimetableLanguage,
        override val asset: TimetableAsset,
        override val levels: PersistentList<String>,
        override val speakers: PersistentList<TimetableSpeaker>,
        val description: MultiLangText,
        val message: MultiLangText?,
    ) : TimetableItem() {
        public companion object
    }

    @Serializable
    public data class Special(
        override val id: TimetableItemId,
        override val title: MultiLangText,
        override val startsAt: Instant,
        override val endsAt: Instant,
        override val category: TimetableCategory,
        override val sessionType: TimetableSessionType,
        override val room: TimetableRoom,
        override val targetAudience: String,
        override val language: TimetableLanguage,
        override val asset: TimetableAsset,
        override val levels: PersistentList<String>,
        override val speakers: PersistentList<TimetableSpeaker>,
        val description: MultiLangText,
        val message: MultiLangText?,
    ) : TimetableItem()

    private val startsDateString: String by lazy {
        val localDate = startsAt.toLocalDateTime(TimeZone.currentSystemDefault())
        "${localDate.year}" + "." + "${localDate.monthNumber}".padStart(2, '0') + "." + "${localDate.dayOfMonth}".padStart(2, '0')
    }

    public val startsTimeString: String by lazy {
        startsAt.toTimetableTimeString()
    }

    public val endsTimeString: String by lazy {
        endsAt.toTimetableTimeString()
    }

    public val startsLocalTime: LocalTime by lazy {
        startsAt.toLocalTime()
    }

    public val endsLocalTime: LocalTime by lazy {
        endsAt.toLocalTime()
    }

    private val minutesString: String by lazy {
        val minutes = (endsAt - startsAt)
            .toComponents { minutes, _, _ -> minutes }
        "$minutes" + MultiLangText(jaTitle = "分", enTitle = "min").currentLangTitle
    }

    public val formattedTimeString: String by lazy {
        "$startsTimeString ~ $endsTimeString"
    }

    public val formattedDateTimeString: String by lazy {
        "$startsDateString / $formattedTimeString ($minutesString)"
    }

    public val formattedMonthAndDayString: String by lazy {
        val localDate = startsAt.toLocalDateTime(TimeZone.currentSystemDefault())
        "${localDate.monthNumber}".padStart(2, '0') + "/" + "${localDate.dayOfMonth}".padStart(2, '0')
    }

    public val url: String get() = if (defaultLang() == Lang.JAPANESE) {
        "https://2024.droidkaigi.jp/timetable/${id.value}"
    } else {
        "https://2024.droidkaigi.jp/en/timetable/${id.value}"
    }

    fun getSupportedLangString(isJapaneseLocale: Boolean): String {
        val japanese = if (isJapaneseLocale) "日本語" else "Japanese"
        val english = if (isJapaneseLocale) "英語" else "English"
        val japaneseWithInterpretation =
            if (isJapaneseLocale) "日本語 (英語通訳あり)" else "Japanese (with English Interpretation)"
        val englishWithInterpretation =
            if (isJapaneseLocale) "英語 (日本語通訳あり)" else "English (with Japanese Interpretation)"

        return when (language.langOfSpeaker) {
            "JAPANESE" -> if (language.isInterpretationTarget) japaneseWithInterpretation else japanese
            "ENGLISH" -> if (language.isInterpretationTarget) englishWithInterpretation else english
            else -> language.langOfSpeaker
        }
    }
}

private fun Instant.toTimetableTimeString(): String {
    val localDate = toLocalDateTime(TimeZone.currentSystemDefault())
    return "${localDate.hour}".padStart(2, '0') + ":" + "${localDate.minute}".padStart(2, '0')
}

private fun Instant.toLocalTime(): LocalTime {
    val localDateTime = toLocalDateTime(TimeZone.currentSystemDefault())
    return localDateTime.time
}

public fun Session.Companion.fake(): Session {
    return Session(
        id = TimetableItemId("2"),
        title = MultiLangText("DroidKaigiのアプリのアーキテクチャ", "DroidKaigi App Architecture"),
        startsAt = LocalDateTime.parse("2024-09-12T10:30:00")
            .toInstant(TimeZone.of("UTC+9")),
        endsAt = LocalDateTime.parse("2024-09-12T10:50:00")
            .toInstant(TimeZone.of("UTC+9")),
        category = TimetableCategory(
            id = 28654,
            title = MultiLangText(
                "Android FrameworkとJetpack",
                "Android Framework and Jetpack",
            ),
        ),
        sessionType = NORMAL,
        room = TimetableRoom(
            id = 1,
            name = MultiLangText("Room1", "Room2"),
            type = RoomF,
            sort = 1,
        ),
        targetAudience = "For App developer アプリ開発者向け",
        language = TimetableLanguage(
            langOfSpeaker = "JAPANESE",
            isInterpretationTarget = true,
        ),
        asset = TimetableAsset(
            videoUrl = "https://www.youtube.com/watch?v=hFdKCyJ-Z9A",
            slideUrl = "https://droidkaigi.jp/2021/",
        ),
        speakers = listOf(
            TimetableSpeaker(
                id = "1",
                name = "taka",
                iconUrl = "https://github.com/takahirom.png",
                bio = "Likes Android",
                tagLine = "Android Engineer",
            ),
            TimetableSpeaker(
                id = "2",
                name = "ry",
                iconUrl = "https://github.com/ry-itto.png",
                bio = "Likes iOS",
                tagLine = "iOS Engineer",
            ),
        ).toPersistentList(),
        description = MultiLangText(
            jaTitle = "これはディスクリプションです。\nこれはディスクリプションです。\nhttps://github.com/DroidKaigi/conference-app-2024 これはURLです。\nこれはディスクリプションです。",
            enTitle = "This is a description.\nThis is a description.\nhttps://github.com/DroidKaigi/conference-app-2024 This is a URL.\nThis is a description.",
        ),
        message = MultiLangText(
            jaTitle = "このセッションは事情により中止となりました",
            enTitle = "This session has been cancelled due to circumstances.",
        ),
        levels = listOf(
            "INTERMEDIATE",
        ).toPersistentList(),
    )
}
