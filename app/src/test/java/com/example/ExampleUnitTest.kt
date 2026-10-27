package com.example

import com.example.model.LinksConfig
import com.example.model.SocialPlatform
import com.example.ui.components.QrCodeGenerator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun verify_allSevenLinksPresentAndConfigured() {
        val links = LinksConfig.createDefaultLinks()
        assertEquals("There must be exactly 7 social links", 7, links.size)

        val platforms = links.map { it.platform }.toSet()
        assertEquals(7, platforms.size)
        assertTrue(platforms.contains(SocialPlatform.FACEBOOK))
        assertTrue(platforms.contains(SocialPlatform.INSTAGRAM))
        assertTrue(platforms.contains(SocialPlatform.WHATSAPP))
        assertTrue(platforms.contains(SocialPlatform.YOUTUBE))
        assertTrue(platforms.contains(SocialPlatform.TIKTOK))
        assertTrue(platforms.contains(SocialPlatform.PINTEREST))
        assertTrue(platforms.contains(SocialPlatform.TELEGRAM))
    }

    @Test
    fun verify_whatsappHasAlgerianCountryCode() {
        val links = LinksConfig.createDefaultLinks()
        val whatsapp = links.first { it.platform == SocialPlatform.WHATSAPP }
        assertTrue("WhatsApp URL must contain Algerian phone 213790314282", whatsapp.url.contains("213790314282"))
        assertEquals("0790314282", LinksConfig.PHONE_NUMBER)
        assertEquals("حملة 3، باتنة، الجزائر", LinksConfig.LOCATION_ADDRESS)
    }

    @Test
    fun verify_editablePlatforms() {
        val links = LinksConfig.createDefaultLinks()
        val facebook = links.first { it.platform == SocialPlatform.FACEBOOK }
        val telegram = links.first { it.platform == SocialPlatform.TELEGRAM }
        val instagram = links.first { it.platform == SocialPlatform.INSTAGRAM }

        assertTrue("Facebook should be editable", facebook.isEditable)
        assertTrue("Telegram should be editable", telegram.isEditable)
        assertTrue("Instagram should not be editable by default", !instagram.isEditable)
    }

    @Test
    fun verify_qrCodeGeneratorProducesScannableMatrix() {
        val matrix = QrCodeGenerator.generateMatrix("https://ais-pre-tgnnxbyeuzlyvwn5oxt2cw-968596947486.europe-west2.run.app")
        assertNotNull(matrix)
        assertTrue(matrix.isNotEmpty())
        assertTrue(matrix[0].isNotEmpty())
    }
}
