package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.model.LinksConfig
import com.example.model.SocialLinkItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class FaouziArtUiState(
    val links: List<SocialLinkItem> = LinksConfig.createDefaultLinks(),
    val websiteUrl: String = LinksConfig.DEFAULT_WEBSITE_URL,
    val showQrDialog: Boolean = false,
    val editingItem: SocialLinkItem? = null,
    val userMessage: String? = null
)

class FaouziArtViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FaouziArtUiState())
    val uiState: StateFlow<FaouziArtUiState> = _uiState.asStateFlow()

    fun openQrDialog() {
        _uiState.update { it.copy(showQrDialog = true) }
    }

    fun closeQrDialog() {
        _uiState.update { it.copy(showQrDialog = false) }
    }

    fun startEditingLink(item: SocialLinkItem) {
        _uiState.update { it.copy(editingItem = item) }
    }

    fun cancelEditingLink() {
        _uiState.update { it.copy(editingItem = null) }
    }

    fun saveEditedLink(id: String, newUrl: String) {
        _uiState.update { state ->
            val updatedLinks = state.links.map { link ->
                if (link.id == id) link.copy(url = newUrl) else link
            }
            state.copy(
                links = updatedLinks,
                editingItem = null,
                userMessage = "تم حفظ الرابط بنجاح"
            )
        }
    }

    fun updateWebsiteUrl(newUrl: String) {
        _uiState.update { it.copy(websiteUrl = newUrl) }
    }

    fun clearUserMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }
}
