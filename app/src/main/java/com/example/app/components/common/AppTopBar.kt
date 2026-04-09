package com.example.app.components.common

import androidx.annotation.ColorRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.R
import androidx.compose.ui.R.color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun TopBarPreview() {
    AppTopBar(title = "Mobile App", onBack = {

    }, leftActions = {
        TextButton(
            onClick = { },
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                "Cancel()", color = colorResource(color.vector_tint_color),
                fontSize = 13.sp,
                fontWeight = FontWeight.W600,
            )
        }
    })
}

/**
 * Top bar for
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onBack: (() -> Unit)? = null,
    @ColorRes backgroundColor: Int = android.R.color.background_dark,
    leftActions: @Composable RowScope.() -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(modifier = Modifier.then(modifier), navigationIcon = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = android.R.color.darker_gray),
                        contentDescription = "Back",
                    )
                }
            }
            leftActions()
        }
    }, title = {
        Text(
            title,
            textAlign = TextAlign.Center,
            fontSize = 17.sp,
            fontWeight = FontWeight.W600,
            color = Color.Black,
        )
    }, actions = actions, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = colorResource(backgroundColor)
    )
    )
}