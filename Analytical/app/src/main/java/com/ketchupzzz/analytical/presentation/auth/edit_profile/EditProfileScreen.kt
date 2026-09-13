package com.ketchupzzz.analytical.presentation.auth.edit_profile

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ketchupzzz.analytical.presentation.auth.register.RegistrationEvents
import com.ketchupzzz.analytical.ui.custom.PrimaryButton
import com.ketchupzzz.analytical.ui.custom.primaryTextFieldColors
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    state: EditProfileState,
    events: (EditProfileEvents) -> Unit,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    LaunchedEffect(state) {
        if (state.errors != null) {
            Toast.makeText(context,state.errors,Toast.LENGTH_SHORT).show()
        }
        if (state.success != null) {
            Toast.makeText(context,state.success,Toast.LENGTH_SHORT).show()
            delay(1000)
            navHostController.popBackStack()
        }
    }
    Scaffold(
        topBar = {
            val colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
            TopAppBar(
                title = { Text(text = "Edit Profile") },
                colors = colors,
                navigationIcon = { IconButton(onClick = { navHostController.popBackStack() })
                { Icon(imageVector = Icons.Rounded.ArrowBackIosNew, contentDescription = "Back") }

                })
        }
    ) {
        Column(modifier = modifier
            .fillMaxSize()
            .padding(it)
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Student Info", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = modifier.height(8.dp))
            TextField(
                value = state.students?.id ?: "",
                onValueChange = {  },
                label = { Text("Student ID") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = false,
                colors = primaryTextFieldColors(),)
            TextField(
                value = state.students?.email ?: "",
                onValueChange = {  },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = false,
                colors = primaryTextFieldColors(),)

            TextField(
                value = state.students?.schoolLevel?.name ?: "",
                onValueChange = {  },
                label = { Text("School Level") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = false,
                colors = primaryTextFieldColors(),)


            Text(text = "Personal Info", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = modifier.height(8.dp))

            TextField(
                value = state.firstName,
                onValueChange = { events(EditProfileEvents.OnTextFieldChanged(it,TextFieldChange.FIRST)) },
                label = { Text("First name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = primaryTextFieldColors(),

            )

            TextField(
                value = state.middleName,
                onValueChange = { events(EditProfileEvents.OnTextFieldChanged(it,TextFieldChange.MIDDLE)) },
                label = { Text("Middle name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = primaryTextFieldColors(),

            )

            TextField(
                value = state.lastName,
                onValueChange = { events(EditProfileEvents.OnTextFieldChanged(it,TextFieldChange.LAST)) },
                label = { Text("Last name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = primaryTextFieldColors(),

            )


            PrimaryButton(onClick = { events(EditProfileEvents.OnSave) }, isLoading = state.isLoading) {
                Text(text = "Save")
            }
        }
    }
}