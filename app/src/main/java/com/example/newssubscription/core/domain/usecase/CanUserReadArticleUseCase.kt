package com.example.newssubscription.core.domain.usecase

import com.example.newssubscription.core.domain.repository.UserRepository
import javax.inject.Inject

class CanUserReadArticleUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(articleUrl: String, articlesShownCount: Int): Boolean {

        // Retrieve the user from the repository or return false if not found
        val user = userRepository.getUser() ?: return false

        // If the user is premium, they can read any article
        if (user.premium) return true

        // Check if the user has already read this article
        val hasReadBefore = user.visitedArticleUrls.contains(articleUrl)
        val canRead: Boolean

        if (!hasReadBefore && user.hasFreeRead) {
            // If the user has free reads available, allow reading and update the user
            val updatedUser = user.copy(
                hasFreeRead = user.articlesShownCount + 1 < articlesShownCount,
                articlesShownCount = user.articlesShownCount + 1,
                visitedArticleUrls = user.visitedArticleUrls + articleUrl,
            )
            userRepository.updateUser(updatedUser)
            canRead = true
        } else {
            // No free reads, only allow if the article has been read before
            canRead = hasReadBefore
        }
        return canRead
    }
}

