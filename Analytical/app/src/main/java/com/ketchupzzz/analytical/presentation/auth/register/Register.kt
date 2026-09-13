package com.ketchupzzz.analytical.presentation.auth.register

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ketchupzzz.analytical.R
import com.ketchupzzz.analytical.models.SchoolLevel
import com.ketchupzzz.analytical.presentation.navigation.AppRouter
import com.ketchupzzz.analytical.ui.custom.primaryTextFieldColors
import com.ketchupzzz.analytical.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RegisterScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    state: RegisterState,
    events: (RegistrationEvents) -> Unit
) {

    val onBoardingState = rememberPagerState(0,0F) {
        3
    }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text(text = when (onBoardingState.currentPage) {
                        0 -> "Registration"
                        1 -> "Information"
                        2 -> "Authentication"
                        else -> ""
                    })
                }, navigationIcon = {
                    IconButton(onClick = {
                         navHostController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                })
            },
            bottomBar = {
                RegistrationController(
                    page = onBoardingState.currentPage,
                    onPrev = {
                        coroutineScope.launch {
                            if (onBoardingState.currentPage > 0) {
                                onBoardingState.animateScrollToPage(onBoardingState.currentPage - 1)
                            }
                        }
                    },
                    onNext = {
                        when (onBoardingState.currentPage) {
                            0 -> {
                                events(RegistrationEvents.CheckStudentID {
                                    when (it) {
                                        is UiState.Error -> {
                                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                                                .show()
                                        }

                                        is UiState.Loading -> {

                                        }

                                        is UiState.Success -> {
                                            coroutineScope.launch {
                                                onBoardingState.animateScrollToPage(
                                                    onBoardingState.currentPage + 1
                                                )
                                            }
                                        }
                                    }
                                })

                            }
                            1 -> {
                                coroutineScope.launch {
                                    onBoardingState.animateScrollToPage(
                                        onBoardingState.currentPage + 1
                                    )
                                }
                            }
                            2 -> {
                                events(RegistrationEvents.OnSubmit{
                                    when(it) {
                                        is UiState.Error -> {
                                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                                        }
                                        is UiState.Loading -> {}
                                        is UiState.Success -> {
                                            Toast.makeText(context, it.data, Toast.LENGTH_SHORT).show()
                                            navHostController.navigate(AppRouter.MainRoutes.route)
                                        }
                                    }
                                })
                            }
                            else ->{
                                Toast.makeText(context, "unknown error", Toast.LENGTH_SHORT).show()
                            }
                        }


                    })

            }

            ) {
            HorizontalPager(state = onBoardingState,modifier = Modifier.padding(it), userScrollEnabled = false) { page ->
                when(page) {
                    0 -> StudentRegistrationForm(
                        state = state,
                        onAction = events
                    )
                    1 -> StudentInformationForm(
                        state = state,
                        onAction = events
                    )
                    2 -> StudentAuthenticationForm(
                        state = state,
                        onAction = events
                    )
                }
            }
        }


}

@Composable
fun RegistrationController(
    modifier: Modifier = Modifier,
    page : Int,
    onPrev : () -> Unit,
    onNext: () -> Unit
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
       Row {
           repeat(3) {
               Box(modifier = Modifier
                   .padding(2.dp)
                   .background(
                       color = if (page == it) MaterialTheme.colorScheme.primary else Color.LightGray,
                       shape = CircleShape
                   )
                   .size(12.dp)
               )
           }
       }
        Row {
            if (page > 0) {
                TextButton(onClick = { onPrev() }) {
                    Text(text = "Previous")
                }
            }
            Button(onClick = { onNext() },shape = RoundedCornerShape(10.dp),) {
                Text(text = if (page < 2) "Next" else "Complete")
            }
        }

    }

}



@Composable
fun StudentRegistrationForm(modifier: Modifier = Modifier ,state: RegisterState,onAction: (RegistrationEvents) -> Unit) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.register),
            contentDescription = "Login",
            modifier = modifier
                .height(200.dp)
                .width(200.dp)
        )

        Text(text = "Register to join the quiz and unlock exciting challenges!", textAlign = TextAlign.Center, style = MaterialTheme.typography.labelLarge ,modifier = modifier.padding(16.dp))
        TextField(
            value = state.studentID,
            onValueChange = { onAction(RegistrationEvents.OnStudentIDChanged(it)) },
            label = { Text("Student ID") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = primaryTextFieldColors(),

        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentInformationForm(modifier: Modifier = Modifier,state: RegisterState,onAction: (RegistrationEvents) -> Unit) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.account),
            contentDescription = "Login",
            modifier = modifier
                .height(200.dp)
                .width(200.dp)
        )
        // change the text
        Text(text = "You're almost there! Complete your registration below.", textAlign = TextAlign.Center, style = MaterialTheme.typography.labelLarge ,modifier = modifier.padding(16.dp))
        TextField(
            value = state.firstName.value,
            onValueChange = { onAction(RegistrationEvents.OnFirstnameChanged(it)) },
            isError = state.firstName.isError,
            label = { Text("First name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = primaryTextFieldColors(),
            supportingText = {
                if (state.firstName.isError) {
                    Text(text = state.firstName.errorMessage ?: "", color = MaterialTheme.colorScheme.error)
                }
            }

        )

        // Middle name (optional)
        TextField(
            value = state.middleName.value,
            onValueChange = { onAction(RegistrationEvents.OnMiddlenameChanged(it))  },
            isError = state.middleName.isError,
            label = { Text("Middle name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = primaryTextFieldColors(),
            supportingText = {
                if (state.middleName.isError) {
                    Text(text = state.middleName.errorMessage ?: "", color = MaterialTheme.colorScheme.error)
                }
            }

        )

        // Last name
        TextField(
            value = state.lastName.value,
            onValueChange = { onAction(RegistrationEvents.OnLastnameChanged(it))  },
            isError = state.lastName.isError,
            label = { Text("Last name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = primaryTextFieldColors(),
            supportingText = {
                if (state.lastName.isError) {
                    Text(text = state.lastName.errorMessage ?: "", color = MaterialTheme.colorScheme.error)
                }
            }
        )


        //dropdown
        // School level
        val schoolLevels = SchoolLevel.entries.toTypedArray()

        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange =  {
            expanded = !expanded
        }) {
            TextField(
                readOnly = true,
                value = state.schoolLevel.displayName,
                onValueChange = { onAction(RegistrationEvents.OnSchoolLevelChanged(state.schoolLevel)) },
                label = { Text("School level") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.exposedDropdownSize()
            ) {
                schoolLevels.forEach { schoolLevel ->
                    DropdownMenuItem(
                        text = { Text(schoolLevel.displayName) },
                        onClick = {
                            onAction(RegistrationEvents.OnSchoolLevelChanged(schoolLevel))
                            expanded = false
                        }
                    )
                }
            }
        }
//        ExposedDropdownMenuBox(
//            expanded = expanded,
//            onExpandedChange = { expanded = !expanded }
//        ) {
//            TextField(
//                readOnly = true,
//                value = state.schoolLevel.name,
//                onValueChange = { onAction(RegistrationEvents.OnSchoolLevelChanged(state.schoolLevel)) },
//                label = { Text("School level") },
//                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//                modifier = Modifier.fillMaxWidth() // Add modifier here
//            )
//            ExposedDropdownMenu(
//                expanded = state.isSchoolLevelExpanded,
//                onDismissRequest = { expanded = false },
//                modifier = Modifier.exposedDropdownSize()
//            ) {
//                schoolLevels.forEach { schoolLevel ->
//                    DropdownMenuItem(
//                        text = { Text(schoolLevel.name) },
//                        onClick = {
//                            onAction(RegistrationEvents.OnSchoolLevelChanged(schoolLevel))
//                            expanded = false
//                        }
//                    )
//                }
//            }
//        }

    }
}

@Composable
fun StudentAuthenticationForm(modifier: Modifier = Modifier,state: RegisterState,onAction: (RegistrationEvents) -> Unit) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp),horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.auth),
            contentDescription = "Login",
            modifier = modifier
                .height(200.dp)
                .width(200.dp)
        )
        //say som
        Text(text = "Great let's finish your registration so you can start the your journey to the next level!", textAlign = TextAlign.Center, style = MaterialTheme.typography.labelLarge ,modifier = modifier.padding(16.dp))

        // Email
        TextField(
            value = state.email.value,
            onValueChange = { onAction(RegistrationEvents.OnEmailChanged(it)) },
            isError = state.email.isError,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            colors = primaryTextFieldColors(),
            supportingText = {
                Text(text = state.email.errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Start,
                )
            }
        )

        TextField(
            value = state.password.value,
            maxLines = 1,
            isError = state.password.isError,
            colors = primaryTextFieldColors(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = {
                onAction(RegistrationEvents.OnPasswordChanged(it))
            },
            trailingIcon = {
                IconButton(onClick = {
                    onAction(RegistrationEvents.OnTogglePasswordVisibility)
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
            shape = RoundedCornerShape(8.dp),
            singleLine = true,

            )

        // Confirm password
        TextField(
            value = state.confirmPassword.value,
            maxLines = 1,
            isError = state.confirmPassword.isError,
            colors = primaryTextFieldColors(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = {
                onAction(RegistrationEvents.OnConfirmPasswordChanged(it))
            },
            trailingIcon = {
                IconButton(onClick = {
                    onAction(RegistrationEvents.OnToggleConfirmPasswordVisibility)
                }) {
                    Icon(
                        painter = painterResource(id = if (state.isConfirmPasswordVisible) R.drawable.ic_eye_off else R.drawable.ic_eye),
                        contentDescription = if (state.isConfirmPasswordVisible) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (state.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            label = {
                Text(text = "Confirm password")
            },
            modifier = modifier.fillMaxWidth(),
            supportingText = {
                Text(text = state.confirmPassword.errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Start,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                )
            } ,
            shape = RoundedCornerShape(8.dp),
            singleLine = true,)


    }
}