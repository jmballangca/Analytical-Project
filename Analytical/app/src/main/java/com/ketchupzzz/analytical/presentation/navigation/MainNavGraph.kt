package com.ketchupzzz.analytical.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ketchupzzz.analytical.presentation.auth.change_password.ChangePasswordScreen
import com.ketchupzzz.analytical.presentation.auth.change_password.ChangePasswordViewModel
import com.ketchupzzz.analytical.presentation.auth.edit_profile.EditProfileScreen
import com.ketchupzzz.analytical.presentation.auth.edit_profile.EditProfileViewModel
import com.ketchupzzz.analytical.presentation.main.category.CategoryScreen
import com.ketchupzzz.analytical.presentation.main.category.CategoryViewModel
import com.ketchupzzz.analytical.presentation.main.crossmath.CrossMathEvents
import com.ketchupzzz.analytical.presentation.main.crossmath.CrossMathScreen
import com.ketchupzzz.analytical.presentation.main.crossmath.CrossMathViewModel
import com.ketchupzzz.analytical.presentation.main.dashboard.DashboardEvents
import com.ketchupzzz.analytical.presentation.main.dashboard.DashboardScreen
import com.ketchupzzz.analytical.presentation.main.dashboard.DashboardViewmodel
import com.ketchupzzz.analytical.presentation.main.difficulty_screen.DifficultyScreen
import com.ketchupzzz.analytical.presentation.main.difficulty_screen.DifficultyViewModel
import com.ketchupzzz.analytical.presentation.main.finish_game.FinishGameScreen
import com.ketchupzzz.analytical.presentation.main.finish_game.FinishGameViewModel
import com.ketchupzzz.analytical.presentation.main.games.GameScreen
import com.ketchupzzz.analytical.presentation.main.games.GameViewModel
import com.ketchupzzz.analytical.presentation.main.games.data.QuizAndLevel
import com.ketchupzzz.analytical.presentation.main.gaming.GamingScreen
import com.ketchupzzz.analytical.presentation.main.gaming.GamingViewModel
import com.ketchupzzz.analytical.presentation.main.gaming.data.SubmissionData
import com.ketchupzzz.analytical.presentation.main.leaderboard.LeaderBoardViewModel
import com.ketchupzzz.analytical.presentation.main.leaderboard.LeaderboardScreen
import com.ketchupzzz.analytical.presentation.main.matching_card.MatchingCardEvents
import com.ketchupzzz.analytical.presentation.main.matching_card.MatchingCardScreen
import com.ketchupzzz.analytical.presentation.main.matching_card.MatchingCardState
import com.ketchupzzz.analytical.presentation.main.matching_card.MatchingCardViewModel
import com.ketchupzzz.analytical.presentation.main.profile.ProfileScreen
import com.ketchupzzz.analytical.presentation.main.profile.ProfileViewModel
import com.ketchupzzz.analytical.presentation.main.search.SearchScreen
import com.ketchupzzz.analytical.presentation.main.search.SearchViewModel
import com.ketchupzzz.analytical.presentation.main.shared.SubmissionsViewModel
import com.ketchupzzz.analytical.utils.UnknownError
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


@Composable
fun MainNavGraph(
    navHostController: NavHostController,
    mainNavHostController: NavHostController
) {

    NavHost(
        navController = navHostController,
        startDestination = AppRouter.DashboardScreen.route
    ) {
        composable(route = AppRouter.DashboardScreen.route) {
            val viewModel = hiltViewModel<DashboardViewmodel>()
            val state = viewModel.state
            val events = viewModel::events
            DashboardScreen(navHostController = navHostController, state = state, events = events)
        }
        composable(route = AppRouter.LeaderboardScreen.route) {
            val viewModel = hiltViewModel<LeaderBoardViewModel>()
            LeaderboardScreen(
                state= viewModel.state, events = viewModel::events,
                navHostController = navHostController
            )
        }

        composable(route = AppRouter.Search.route) {
            val viewModel = hiltViewModel<SearchViewModel>()
            SearchScreen(
                state= viewModel.state,
                events = viewModel::events,
                navHostController = navHostController
            )
        }

        composable("category/{args}") { backStackEntry ->
            val args = backStackEntry.arguments?.getString("args")
            val decodedArgs = URLDecoder.decode(args, StandardCharsets.UTF_8.toString())
            val argsMap = parseJsonToMap(decodedArgs)
            val schoolLevel = argsMap["schoolLevel"] ?: ""
             val category = argsMap["category"] ?: ""
            val viewmodel = hiltViewModel<CategoryViewModel>()
            CategoryScreen(level = schoolLevel, category = category,
                state = viewmodel.state, events = viewmodel::events, navHostController = navHostController)
        }

        composable(route = AppRouter.ProfileScreen.route) {
            val viewModel = hiltViewModel<ProfileViewModel>()
            ProfileScreen(
                navHostController = navHostController,
                state = viewModel.state,
                events = viewModel::events,
                mainNav = mainNavHostController
            )
        }
        composable(route = AppRouter.EditProfileScreen.route) {
            val viewModel = hiltViewModel<EditProfileViewModel>()
            EditProfileScreen(
                navHostController = navHostController,
                state = viewModel.state,
                events = viewModel::events,
            )
        }

        composable(route = AppRouter.ChangePasswordScreen.route) {
            val viewModel = hiltViewModel<ChangePasswordViewModel>()
            ChangePasswordScreen(
                navHostController = navHostController,
                state = viewModel.state,
                events = viewModel::events,
            )
        }

        composable(route = AppRouter.DIFFICULTY.route) {b ->
            val id = b.arguments?.getString("id") ?: ""
            val viewmodel = hiltViewModel<DifficultyViewModel>()
            DifficultyScreen(
                navHostController = navHostController,
                state = viewmodel.state,
                events = viewmodel::events,
                id = id
            )
        }

        composable(route = AppRouter.GameScreen.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val difficulty = backStackEntry.arguments?.getString("difficulty") ?: ""
            val viewModel = hiltViewModel<GameViewModel>()
            GameScreen(
                navHostController = navHostController,
                state = viewModel.state,
                e = viewModel::events,
                id = id,
                difficulty = difficulty
            )
        }

        composable(
            route = AppRouter.GamingScreen.route,
            arguments = listOf(navArgument("quizAndLevel") { type = NavType.StringType })
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("quizAndLevel")
            val decodedJson = URLDecoder.decode(json, StandardCharsets.UTF_8.toString())
            val quizAndLevel = Gson().fromJson(decodedJson, QuizAndLevel::class.java)

            val viewModel = hiltViewModel<GamingViewModel>()
            if (quizAndLevel != null) {
                GamingScreen(
                    s = viewModel.state,
                    e = viewModel::events,
                    navHostController = navHostController,
                    args = quizAndLevel
                )
            } else {
                UnknownError(
                    title = "Page not found!",
                    actions = {
                        navHostController.popBackStack()
                    }
                )
            }
        }

        composable(
            route = AppRouter.MemoryGame.route,
            arguments = listOf(navArgument("quizAndLevel") { type = NavType.StringType })
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("quizAndLevel")
            val decodedJson = URLDecoder.decode(json, StandardCharsets.UTF_8.toString())
            val quizAndLevel = Gson().fromJson(decodedJson, QuizAndLevel::class.java)

            val viewModel = hiltViewModel<MatchingCardViewModel>()

            if (quizAndLevel != null) {
                MatchingCardScreen(
                    state = viewModel.state,
                    events = viewModel::events,
                    navHostController = navHostController,
                    args = quizAndLevel
                )
            } else {
                UnknownError(
                    title = "Page not found!",
                    actions = {
                        navHostController.popBackStack()
                    }
                )
            }
        }


        composable(
            route = AppRouter.CrossMath.route,
            arguments = listOf(navArgument("quizAndLevel") { type = NavType.StringType })
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("quizAndLevel")
            val decodedJson = URLDecoder.decode(json, StandardCharsets.UTF_8.toString())
            val quizAndLevel = Gson().fromJson(decodedJson, QuizAndLevel::class.java)
            val viewModel = hiltViewModel<CrossMathViewModel>()
            if (quizAndLevel != null) {
                CrossMathScreen(
                    state = viewModel.state,
                    events = viewModel::events,
                    navHostController = navHostController,
                    quiz = quizAndLevel
                )
            } else {
                UnknownError(
                    title = "Page not found!",
                    actions = {
                        navHostController.popBackStack()
                    }
                )
            }
        }

        composable(
            route = AppRouter.FinishGameScreen.route,
            arguments = listOf(navArgument("args") { type = NavType.StringType })
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("args")
            val decodedJson = URLDecoder.decode(json, StandardCharsets.UTF_8.toString())
            val args = Gson().fromJson(decodedJson, SubmissionData::class.java)

            val viewModel = hiltViewModel<FinishGameViewModel>()
            if (args != null) {
                FinishGameScreen(args = args, state = viewModel.state, events = viewModel::events, navHostController = navHostController)
            } else {
                UnknownError(
                    title = "Page not found!",
                    actions = {
                        navHostController.popBackStack()
                    }
                )
            }
        }
    }
}

fun parseJsonToMap(json: String): Map<String, String> {
    val mapType = object : TypeToken<Map<String, String>>() {}.type
    return Gson().fromJson(json, mapType)
}
