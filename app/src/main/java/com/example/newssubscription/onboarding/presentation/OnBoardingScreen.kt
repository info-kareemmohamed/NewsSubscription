package com.example.newssubscription.onboarding.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newssubscription.R
import com.example.newssubscription.app.ui.Dimens.MediumPadding_24
import com.example.newssubscription.onboarding.data.Page
import com.example.newssubscription.onboarding.presentation.components.OnBoardingButton
import com.example.newssubscription.onboarding.presentation.components.OnBoardingPage
import com.example.newssubscription.onboarding.presentation.components.OnBoardingTextButton
import com.example.newssubscription.onboarding.presentation.components.PagerIndicator
import com.example.newssubscription.onboarding.presentation.mvvm.OnBoardingViewModel


@Composable
fun OnBoardingScreenRoot(
    viewModel: OnBoardingViewModel = hiltViewModel(),
) {
    OnBoardingScreen(
        currentPage = viewModel.currentPage,
        pages = viewModel.pages,
        isLastPage = viewModel.isLastPage,
        onChangePage = viewModel::changePage,
        navigateToLoginScreen = viewModel::saveAppEntry
    )
}


@Composable
private fun OnBoardingScreen(
    currentPage: Int,
    pages: List<Page>,
    isLastPage: Boolean,
    onChangePage: (Int) -> Unit,
    navigateToLoginScreen: () -> Unit
) {

    val pagerState = rememberPagerState(currentPage) { pages.size }

    // Update the pager state when the current page in the state changes
    LaunchedEffect(currentPage) {
        pagerState.animateScrollToPage(currentPage)
    }

    // Update ViewModel when page changes (sync pager state to ViewModel)
    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != currentPage) {
            onChangePage(pagerState.currentPage)
        }
    }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HorizontalPager(state = pagerState) { index ->
            OnBoardingPage(page = pages[index])
        }
        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MediumPadding_24)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PagerIndicator(
                modifier = Modifier.width(52.dp),
                pagesCount = pages.size,
                currentPage = pagerState.currentPage
            )
            Row {
                if (currentPage > 0) {
                    OnBoardingTextButton(text = stringResource(R.string.back)) {
                        onChangePage(currentPage - 1)
                    }
                }
                if (!isLastPage)
                    OnBoardingButton(text = stringResource(R.string.next)) {
                        onChangePage(currentPage + 1)

                    }

                // Get Started button shown only on the last page
                AnimatedVisibility(visible = isLastPage) {
                    OnBoardingButton(text = stringResource(R.string.get_started)) {
                        navigateToLoginScreen()
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(0.5f))
    }
}


