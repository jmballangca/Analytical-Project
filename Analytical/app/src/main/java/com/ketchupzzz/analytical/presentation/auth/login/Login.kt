package com.ketchupzzz.analytical.presentation.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ketchupzzz.analytical.R
import com.ketchupzzz.analytical.presentation.navigation.AppRouter

import com.ketchupzzz.analytical.ui.custom.PrimaryButton
import com.ketchupzzz.analytical.ui.theme.AnalyticalTheme
import com.ketchupzzz.analytical.ui.custom.primaryTextFieldColors


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    state : LoginState,
    events: (LoginEvents) -> Unit
) {
    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) {
            navHostController.navigate(AppRouter.MainRoutes.route) {
                popUpTo(navHostController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    Surface(modifier = modifier.fillMaxSize()) {
        Column(modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Login BG",
                modifier = modifier
                    .height(200.dp)
            )

            LoginForm(
                modifier = modifier,
                navHostController = navHostController,
                state = state,
                events = events,
                onRegister = {
                    navHostController.navigate(AppRouter.RegisterScreen.route)
                }
            )
        }
    }
}



@Composable
fun LoginForm(modifier: Modifier = Modifier,navHostController: NavHostController,state : LoginState, events : (LoginEvents) -> Unit ,onRegister : () -> Unit) {
    Box(modifier = modifier
        .wrapContentSize()
        .background(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp,

                )
        )) {
        Column(modifier = modifier
            .padding(8.dp)
            .wrapContentSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(state.studentID.value,
                onValueChange = {
                    events(LoginEvents.OnStudentIDChanged(it))
                },
                modifier = modifier.fillMaxWidth(),
                isError = state.studentID.isError,
                label = {
                    Text(text = "Student ID")
                },
                supportingText = {
                    Text(
                        text =  state.studentID.errorMessage ?: "",
                        textAlign = TextAlign.Start
                    )
                },
                colors = primaryTextFieldColors(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )
            TextField(
                value = state.password.value,
                maxLines = 1,
                isError = state.password.isError,

                onValueChange = {
                    events(LoginEvents.OnPasswordChanged(it))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = {
                        events(LoginEvents.OnTogglePasswordVisibility)
                    }) {
                        Icon(
                            painter = painterResource(id = if (state.isPasswordVisible) R.drawable.ic_eye_off else R.drawable.ic_eye),
                            contentDescription = if (state.isPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                label = {
                    Text(text = stringResource(R.string.password))
                },
                modifier = modifier.fillMaxWidth(),
                supportingText = {
                    Text(text = state.password.errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Start,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                    )
                } ,
                colors = primaryTextFieldColors(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,

                )
            TextButton(onClick = {
                navHostController.navigate(AppRouter.ForgotPasswordScreen.route)
            },modifier = modifier.align(Alignment.End)) {
                Text(text = stringResource(R.string.forgot_password))
            }

            PrimaryButton(onClick = {
                events.invoke(LoginEvents.OnLogin)
            },
                isLoading = state.isLoading
            ){
                Text(text = stringResource(R.string.login),fontWeight = FontWeight.Bold)
            }

            TextButton(onClick = {
                onRegister()
            }) {
                Text(
                    text = buildAnnotatedString {
                    append(stringResource(R.string.not_a_member_yet))
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append(stringResource(R.string.register))
                    }
                },
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }

        }

    }

}

@Preview(showBackground = true)
@Composable
private fun LoginFormPreview() {
    AnalyticalTheme {
        LoginScreen(
            state = LoginState(),
            navHostController = rememberNavController(),
            events = {},

        )
    }
}