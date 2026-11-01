// SPDX-License-Identifier: Apache-2.0

package net.farcaster.pixelbridge.notify

import net.farcaster.pixelbridge.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.util.Locale

class AppIconsTest {
    @Test
    fun musicPlayersAndKdeConnectResolveLabelsAndPackages() {
        val apps = listOf(
            Triple("AIMP", "com.aimp.player", R.drawable.ic_color_aimp),
            Triple("Poweramp", "com.maxmpz.audioplayer", R.drawable.ic_color_poweramp),
            Triple("VLC", "org.videolan.vlc", R.drawable.ic_color_vlc),
            Triple("Musicolet", "in.krosbits.musicolet", R.drawable.ic_color_musicolet),
            Triple("YouTube Music", "com.google.android.apps.youtube.music", R.drawable.ic_color_youtubemusic),
            Triple("Яндекс Музыка", "ru.yandex.music", R.drawable.ic_color_yandexmusic),
            Triple("Apple Music", "com.apple.android.music", R.drawable.ic_color_applemusic),
            Triple("Amazon Music", "com.amazon.mp3", R.drawable.ic_color_amazonmusic),
            Triple("Deezer", "deezer.android.app", R.drawable.ic_color_deezer),
            Triple("TIDAL", "com.aspiro.tidal", R.drawable.ic_color_tidal),
            Triple("SoundCloud", "com.soundcloud.android", R.drawable.ic_color_soundcloud),
            Triple("Pocket Casts", "au.com.shiftyjelly.pocketcasts", R.drawable.ic_color_pocketcasts),
            Triple("AntennaPod", "de.danoeh.antennapod", R.drawable.ic_color_antennapod),
            Triple("foobar2000", "com.foobar2000.foobar2000", R.drawable.ic_color_foobar2000),
            Triple("Samsung Music", "com.sec.android.app.music", R.drawable.ic_color_samsungmusic),
            Triple("KDE Connect", "org.kde.kdeconnect_tp", R.drawable.ic_color_kdeconnect),
        )
        apps.forEach { (label, pkg, icon) ->
            assertEquals(label, icon, AppIcons.colorForSource(label))
            assertEquals(pkg, icon, AppIcons.colorForSource(pkg))
        }
    }

    @Test
    fun sharedSmallMusicMasksDoNotOverwriteIndividualColorIcons() {
        assertEquals(AppIcons.forSource("AIMP"), AppIcons.forSource("Poweramp"))
        assertNotEquals(AppIcons.colorForSource("AIMP"), AppIcons.colorForSource("Poweramp"))
        assertNotEquals(AppIcons.colorForSource("KDE Connect"), AppIcons.colorForSource("Unknown"))
    }

    @Test
    fun requestedAppsResolveFromLabelsAndPackages() {
        val apps = listOf(
            Triple("YouTube", "com.google.android.youtube", R.drawable.ic_app_youtube),
            Triple("Telegram", "org.telegram.messenger", R.drawable.ic_app_telegram),
            Triple("ChatGPT", "com.openai.chatgpt", R.drawable.ic_app_openai),
            Triple("Reddit", "com.reddit.frontpage", R.drawable.ic_app_reddit),
            Triple("Spotify", "com.spotify.music", R.drawable.ic_app_spotify),
        )
        apps.forEach { (label, pkg, icon) ->
            assertEquals(label, icon, AppIcons.forSource(label))
            assertEquals(pkg, icon, AppIcons.forSource(pkg))
        }
    }

    @Test
    fun cardsUseColorArtworkRatherThanSystemMasks() {
        val apps = listOf(
            Triple("YouTube", "com.google.android.youtube", R.drawable.ic_color_youtube),
            Triple("Telegram", "org.telegram.messenger", R.drawable.ic_color_telegram),
            Triple("ChatGPT", "com.openai.chatgpt", R.drawable.ic_color_openai),
            Triple("Reddit", "com.reddit.frontpage", R.drawable.ic_color_reddit),
            Triple("Spotify", "com.spotify.music", R.drawable.ic_color_spotify),
        )
        apps.forEach { (label, pkg, icon) ->
            assertEquals(label, icon, AppIcons.colorForSource(label))
            assertEquals(pkg, icon, AppIcons.colorForSource(pkg))
        }
        assertEquals(R.drawable.ic_color_vk, AppIcons.colorForSource("ВКонтакте"))
        assertEquals(R.drawable.ic_launcher_foreground, AppIcons.colorForSource(null))
        assertEquals(R.drawable.ic_launcher_foreground, AppIcons.colorForSource("Unknown App"))
    }

    @Test
    fun labelsNormalizeCaseWhitespaceAndSeparators() {
        assertEquals(R.drawable.ic_app_telegram, AppIcons.forSource("  tElEgRaM  "))
        assertEquals(R.drawable.ic_app_openai, AppIcons.forSource("chat-gpt"))
        assertEquals(R.drawable.ic_app_protonmail, AppIcons.forSource("Proton_Mail"))
        assertEquals(R.drawable.ic_app_googlemessages, AppIcons.forSource("Google\u00a0Messages"))
    }

    @Test
    fun supportsLocalizedLabelsAndCommonVariants() {
        assertEquals(R.drawable.ic_app_vk, AppIcons.forSource("ВКонтакте"))
        assertEquals(R.drawable.ic_app_googlecalendar, AppIcons.forSource("Календарь"))
        assertEquals(R.drawable.ic_app_telegram, AppIcons.forSource("Telegram X"))
        assertEquals(R.drawable.ic_app_signal, AppIcons.forSource("Molly"))
        assertEquals(R.drawable.ic_app_whatsapp, AppIcons.forSource("WhatsApp Business"))
    }

    @Test
    fun normalizationDoesNotDependOnWatchLocale() {
        val previous = Locale.getDefault()
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"))
            assertEquals(R.drawable.ic_app_signal, AppIcons.forSource("SIGNAL"))
            assertEquals(R.drawable.ic_app_discord, AppIcons.forSource("DISCORD"))
        } finally {
            Locale.setDefault(previous)
        }
    }

    @Test
    fun unknownOrMissingSourcesKeepPixelBridgeIcon() {
        listOf(null, "", "  ", "Unknown App", "My Telegram Helper", "not.com.openai.chatgpt")
            .forEach { assertEquals(R.drawable.ic_launcher_foreground, AppIcons.forSource(it)) }
    }
}
