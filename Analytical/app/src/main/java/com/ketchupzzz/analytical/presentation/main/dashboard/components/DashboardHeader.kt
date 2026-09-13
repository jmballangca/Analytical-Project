package com.ketchupzzz.analytical.presentation.main.dashboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ketchupzzz.analytical.R
import com.ketchupzzz.analytical.models.Students
import com.ketchupzzz.analytical.models.submissions.Submissions
import com.ketchupzzz.analytical.models.submissions.getPoints
import com.ketchupzzz.analytical.models.submissions.groupSubmissionsByCategoryAndGetAverageScore
import com.ketchupzzz.analytical.models.submissions.groupSubmissionsByLevel
import com.ketchupzzz.analytical.ui.theme.AnalyticalTheme
import com.ketchupzzz.analytical.utils.getStudentFullname


@Composable
fun DashboardHeader(
    modifier: Modifier = Modifier,
    students: Students ? = null,
    submissions : List<Submissions>,
    onSearch : () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            students?.profile,
            error = painterResource(id = R.drawable.user),
            placeholder = painterResource(id = R.drawable.user),
            contentDescription = "${students?.getStudentFullname()}",
            modifier = modifier
                .size(48.dp)
                .clip(CircleShape)
                .border(
                    color = MaterialTheme.colorScheme.onPrimary,
                    width = 1.dp,
                    shape = CircleShape
                ),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = students?.getStudentFullname() ?: "Unknown User",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            val points = submissions.groupSubmissionsByLevel().getPoints()
            Text(
                text = "${points} pts",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurface
            )

        }
        FilledIconButton(
            onClick = {onSearch()}
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search"
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun DashboardHeaderPreview() {
    AnalyticalTheme {
        DashboardHeader(
            students =  Students(
                id = "",
                fname = "juan",
                mname = "Dela",
                lname = "Cruz"
            ) ,
            onSearch = {},
            submissions = emptyList()
        )
    }
}