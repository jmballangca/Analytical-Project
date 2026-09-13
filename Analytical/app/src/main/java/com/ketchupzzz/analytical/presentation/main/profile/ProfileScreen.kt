package com.ketchupzzz.analytical.presentation.main.profile

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ketchupzzz.analytical.models.Students
import com.ketchupzzz.analytical.models.submissions.getPoints
import com.ketchupzzz.analytical.models.submissions.groupSubmissionsByLevel
import com.ketchupzzz.analytical.presentation.navigation.AppRouter
import com.ketchupzzz.analytical.utils.Profile
import com.ketchupzzz.analytical.utils.getScoreMessage
import com.ketchupzzz.analytical.utils.getStudentFullname
import com.ketchupzzz.analytical.utils.toast
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    state: ProfileState,
    events: (ProfileEvents) -> Unit,
    navHostController: NavHostController,
    mainNav: NavHostController
) {
    val context = LocalContext.current
    LaunchedEffect(state) {
        if (state.students != null) {
            events.invoke(ProfileEvents.OnGetSubmissions(state.students.id!!))
        }
        if (state.isProfileUploaded != null) {
            context.toast(state.isProfileUploaded)
        }
    }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            events(ProfileEvents.OnUploadProfile(it))
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Profile(imageURL = state.students?.profile ?: "", size = 80.dp) {
            imagePickerLauncher.launch("image/*")
        }
        Text(text = "${state.students?.getStudentFullname()}", style = MaterialTheme.typography.titleLarge)
        Text(text = "${state.students?.id} (${state.students?.schoolLevel})")
        StudentRecord(
            state = state
        )
        StudentStatistics(state.students, state = state)
        ProfileSettings(state,events,mainNav,navHostController=navHostController)
    }
}

@Composable
fun ProfileSettings(
    state: ProfileState,
    events: (ProfileEvents) -> Unit,
    mainNav: NavHostController,
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {

        Text(text = "Settings",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = modifier.height(16.dp))
        SettingsAction(title = "Edit Profile", icon = Icons.Rounded.Edit) {
            navHostController.navigate(
                AppRouter.EditProfileScreen.route
            )
        }
        SettingsAction(title = "Change Password", icon = Icons.Rounded.Password) {
            navHostController.navigate(
                AppRouter.ChangePasswordScreen.route
            )
        }
        val logoutColor = MaterialTheme.colorScheme.error
        val textLogoutColor = MaterialTheme.colorScheme.onError

        SettingsAction(title = "Log out", icon = Icons.Rounded.Logout,
            isLoading = state.isLoading,
            color = ButtonDefaults.buttonColors(
            containerColor = logoutColor,
            contentColor = textLogoutColor
        )) {
            events.invoke(ProfileEvents.OnLoggedOut(navHostController = mainNav))
        }
    }
}

@Composable
fun SettingsAction(
    modifier: Modifier = Modifier,
    title : String,
    icon : ImageVector,
    isLoading : Boolean = false,
    color : ButtonColors = ButtonDefaults.outlinedButtonColors(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ),
    onClick : () -> Unit
) {
    Button(onClick = { onClick() }, shape = MaterialTheme.shapes.small, colors = color, enabled = !isLoading) {
        Row(modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontWeight = FontWeight.Bold)
            if (isLoading) {
                CircularProgressIndicator(modifier = modifier.size(24.dp))
            } else {
                Icon(imageVector = icon, contentDescription = "Icon")
            }

        }
    }
}

@Composable
fun StudentRecord(
    state: ProfileState,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        val title = MaterialTheme.typography.labelSmall
        val content = MaterialTheme.typography.titleMedium
        val color = MaterialTheme.colorScheme.onSurface
        val contentTextWeight = FontWeight.Bold

        Column(
            modifier = modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val points = state.submissions.groupSubmissionsByLevel().getPoints()
            Text(text = "Points", style = title, color = color)
            Text(text = "$points", style =  content, color = color,fontWeight = contentTextWeight)
        }
        VerticalDivider(color = color, thickness = 5.dp)
        Column(
            modifier = modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Matches", style = title, color = color)
            Text(text = "${state.submissions.size}", style =  content, color = color,fontWeight = contentTextWeight)
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun StudentStatistics(
    students: Students?,
    state: ProfileState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Average Score Per Category",
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        var data = listOf(
            Pie(label = "Quiz Game",data = state.mathGame?.average ?: 0.0, color = Color.Green, selectedColor = Color.Green),
            Pie(label = "Memory Game",data = state.memoryGame?.average ?: 0.0, color = Color.Cyan, selectedColor = Color.Blue),
            Pie(label = "Puzzle Game",data = state.puzzleGame?.average ?: 0.0, color = Color.Yellow, selectedColor = Color.Gray),
            Pie(label = "Math  Game",data = state.mathGame?.average ?: 0.0, color = Color.Magenta, selectedColor = Color(0xFFFFA500)),
        )
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PieChart(
                modifier = Modifier.size(150.dp),
                data = data,
                onPieClick = {
                    println("${it.label} Clicked")
                    val pieIndex = data.indexOf(it)
                    data = data.mapIndexed { mapIndex, pie -> pie.copy(selected = pieIndex == mapIndex) }
                },
                selectedScale = 1.2f,
                scaleAnimEnterSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                colorAnimEnterSpec = tween(300),
                colorAnimExitSpec = tween(300),
                scaleAnimExitSpec = tween(300),
                spaceDegreeAnimExitSpec = tween(300),
                style = Pie.Style.Fill
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                data.forEach { pie ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(pie.color, shape = CircleShape)
                        )
                        Text(text = "${pie.label} (${String.format("%.2f",pie.data)})", style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ))
                    }
                }
            }
        }

    }
}
