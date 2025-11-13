package softserve.academy.myapplication.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.navigation3.ui.NavDisplay

// Вертикальна анімація: екран заїжджає знизу, сповзає вниз при pop
val slideVerticalAnimation = NavDisplay.transitionSpec {
    slideInVertically(
        initialOffsetY = { it },
        animationSpec = tween(600)
    ) togetherWith ExitTransition.KeepUntilTransitionsFinished
} + NavDisplay.popTransitionSpec {
    EnterTransition.None togetherWith slideOutVertically(
        targetOffsetY = { it },
        animationSpec = tween(600)
    )
} + NavDisplay.predictivePopTransitionSpec {
    EnterTransition.None togetherWith slideOutVertically(
        targetOffsetY = { it },
        animationSpec = tween(400)
    )
}

// Горизонтальна анімація: екран заїжджає з правого боку, від’їжджає праворуч
val slideHorizontalAnimation = NavDisplay.transitionSpec {
    slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = tween(600)
    ) togetherWith ExitTransition.KeepUntilTransitionsFinished
} + NavDisplay.popTransitionSpec {
    EnterTransition.None togetherWith slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(600)
    )
}