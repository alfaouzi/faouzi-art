package com.example.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.R

enum class SocialPlatform(
    val defaultTitleAr: String,
    val defaultSubtitleAr: String,
    @DrawableRes val iconRes: Int,
    val brandColor: Color,
    val badgeBgColor: Color
) {
    FACEBOOK(
        defaultTitleAr = "تابعنا على فيسبوك",
        defaultSubtitleAr = "آخر المنشورات والطلبيات الخاصة",
        iconRes = R.drawable.ic_social_facebook,
        brandColor = Color(0xFF1877F2),
        badgeBgColor = Color(0x261877F2)
    ),
    INSTAGRAM(
        defaultTitleAr = "تابع أعمالنا على إنستغرام",
        defaultSubtitleAr = "صور المعرض وفيديوهات الورشة",
        iconRes = R.drawable.ic_social_instagram,
        brandColor = Color(0xFFE1306C),
        badgeBgColor = Color(0x26E1306C)
    ),
    WHATSAPP(
        defaultTitleAr = "تواصل معنا عبر واتساب",
        defaultSubtitleAr = "0790314282 (استفسارات وطلبيات مباشرة)",
        iconRes = R.drawable.ic_social_whatsapp,
        brandColor = Color(0xFF25D366),
        badgeBgColor = Color(0x2625D366)
    ),
    YOUTUBE(
        defaultTitleAr = "شاهد أعمالنا على يوتيوب",
        defaultSubtitleAr = "كواليس العمل والصناعة اليدوية",
        iconRes = R.drawable.ic_social_youtube,
        brandColor = Color(0xFFFF0000),
        badgeBgColor = Color(0x26FF0000)
    ),
    TIKTOK(
        defaultTitleAr = "تابعنا على تيك توك",
        defaultSubtitleAr = "فيديوهات سريعة وتقنيات الحرفة",
        iconRes = R.drawable.ic_social_tiktok,
        brandColor = Color(0xFF00F2FE),
        badgeBgColor = Color(0x2600F2FE)
    ),
    PINTEREST(
        defaultTitleAr = "اكتشف أعمالنا على Pinterest",
        defaultSubtitleAr = "أفكار الديكور والتحف المنزلية",
        iconRes = R.drawable.ic_social_pinterest,
        brandColor = Color(0xFFE60023),
        badgeBgColor = Color(0x26E60023)
    ),
    TELEGRAM(
        defaultTitleAr = "تواصل معنا عبر تيليغرام",
        defaultSubtitleAr = "قناة التحديثات والمعروضات الفنية",
        iconRes = R.drawable.ic_social_telegram,
        brandColor = Color(0xFF229ED9),
        badgeBgColor = Color(0x26229ED9)
    );
}

data class SocialLinkItem(
    val id: String,
    val platform: SocialPlatform,
    val title: String,
    val subtitle: String,
    val url: String,
    val isEditable: Boolean = false,
    val testTag: String
)

/**
 * Centralized configuration object containing all destinations for Faouzi Art workshop.
 * Modify these default URLs to update destinations across the entire application.
 */
object LinksConfig {
    const val WORKSHOP_NAME_AR = "ورشة فوزي للفنون"
    const val USERNAME = "@faouzi.art"
    const val PHONE_NUMBER = "0790314282"
    const val PHONE_TEL_URI = "tel:0790314282"
    const val LOCATION_ADDRESS = "حملة 3، باتنة، الجزائر"
    const val MAPS_URL = "https://www.google.com/maps/search/?api=1&query=%D8%AD%D9%85%D9%84%D8%A9+3%D8%8C+%D8%A8%D8%A7%D8%AA%D9%86%D8%A9%D8%8C+%D8%A7%D9%84%D8%AC%D8%B2%D8%A7%D8%A6%D8%B1"
    const val DEFAULT_WEBSITE_URL = "https://ais-pre-tgnnxbyeuzlyvwn5oxt2cw-968596947486.europe-west2.run.app"
    const val WORKSHOP_DESCRIPTION_AR =
        "ورشة فوزي للفنون تعرض أعمالًا فنية رائعة ومتنوعة من صنع الهدايا والديكور والتحف."

    // 1. Facebook URL
    const val URL_FACEBOOK = "https://www.facebook.com/share/1BYkfqkps9/"

    // 2. Instagram URL
    const val URL_INSTAGRAM = "https://instagram.com/faouzi.artisanat"

    // 3. WhatsApp URL (Algeria international country code +213)
    const val URL_WHATSAPP = "https://api.whatsapp.com/send?phone=213790314282"

    // 4. YouTube URL
    const val URL_YOUTUBE = "https://www.youtube.com/@faouzi.artisanat"

    // 5. TikTok URL
    const val URL_TIKTOK = "https://www.tiktok.com/@f.artisanat"

    // 6. Pinterest URL
    const val URL_PINTEREST = "https://www.pinterest.com/f_artisanat/"

    // 7. Telegram URL
    const val URL_TELEGRAM = "https://t.me/faouzi_art"

    /**
     * Initial immutable list of the seven verified workshop social media buttons.
     */
    fun createDefaultLinks(): List<SocialLinkItem> = listOf(
        SocialLinkItem(
            id = "facebook",
            platform = SocialPlatform.FACEBOOK,
            title = SocialPlatform.FACEBOOK.defaultTitleAr,
            subtitle = SocialPlatform.FACEBOOK.defaultSubtitleAr,
            url = URL_FACEBOOK,
            isEditable = true, // Facebook link has not been independently verified
            testTag = "btn_social_facebook"
        ),
        SocialLinkItem(
            id = "instagram",
            platform = SocialPlatform.INSTAGRAM,
            title = SocialPlatform.INSTAGRAM.defaultTitleAr,
            subtitle = SocialPlatform.INSTAGRAM.defaultSubtitleAr,
            url = URL_INSTAGRAM,
            isEditable = false,
            testTag = "btn_social_instagram"
        ),
        SocialLinkItem(
            id = "whatsapp",
            platform = SocialPlatform.WHATSAPP,
            title = SocialPlatform.WHATSAPP.defaultTitleAr,
            subtitle = SocialPlatform.WHATSAPP.defaultSubtitleAr,
            url = URL_WHATSAPP,
            isEditable = false,
            testTag = "btn_social_whatsapp"
        ),
        SocialLinkItem(
            id = "youtube",
            platform = SocialPlatform.YOUTUBE,
            title = SocialPlatform.YOUTUBE.defaultTitleAr,
            subtitle = SocialPlatform.YOUTUBE.defaultSubtitleAr,
            url = URL_YOUTUBE,
            isEditable = false,
            testTag = "btn_social_youtube"
        ),
        SocialLinkItem(
            id = "tiktok",
            platform = SocialPlatform.TIKTOK,
            title = SocialPlatform.TIKTOK.defaultTitleAr,
            subtitle = SocialPlatform.TIKTOK.defaultSubtitleAr,
            url = URL_TIKTOK,
            isEditable = false,
            testTag = "btn_social_tiktok"
        ),
        SocialLinkItem(
            id = "pinterest",
            platform = SocialPlatform.PINTEREST,
            title = SocialPlatform.PINTEREST.defaultTitleAr,
            subtitle = SocialPlatform.PINTEREST.defaultSubtitleAr,
            url = URL_PINTEREST,
            isEditable = false,
            testTag = "btn_social_pinterest"
        ),
        SocialLinkItem(
            id = "telegram",
            platform = SocialPlatform.TELEGRAM,
            title = SocialPlatform.TELEGRAM.defaultTitleAr,
            subtitle = SocialPlatform.TELEGRAM.defaultSubtitleAr,
            url = URL_TELEGRAM,
            isEditable = true, // Telegram link has not been independently verified
            testTag = "btn_social_telegram"
        )
    )
}
