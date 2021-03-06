package com.linku.im.screen.main.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.DrawerState
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Info
import androidx.compose.material.icons.sharp.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.linku.im.BuildConfig
import com.linku.im.R
import com.linku.im.extension.debug
import com.linku.im.extension.times
import com.linku.im.screen.Screen
import com.linku.im.ui.Inbox
import com.linku.im.ui.theme.Typography
import kotlinx.coroutines.launch

private val drawerItems = listOf(
    DrawerItem(R.string.inbox, Screen.MainScreen, Icons.Inbox),
    DrawerItem(R.string.settings, Screen.ProfileScreen, Icons.Sharp.Settings),
    DrawerItem(R.string.info, Screen.InfoScreen, Icons.Sharp.Info)
)

@Composable
fun Drawer(
    title: String?,
    drawerState: DrawerState,
    onNavigate: (Screen) -> Unit,
    onHeaderClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            Surface(
                color = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(
                    topEnd = 18.dp,
                    bottomEnd = 18.dp,
                    topStart = 18.dp,
                    bottomStart = 18.dp
                ),
                modifier = Modifier.padding(
                    top = 8.dp,
                    end = 8.dp,
                    bottom = 8.dp,
                    start = 8.dp
                ),
                elevation = 0.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    ClickableText(
                        text = AnnotatedString(title ?: ""),
                        onClick = { onHeaderClick() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .placeholder(
                                visible = title == null,
                                color = MaterialTheme.colorScheme.outline * 0.3f,
                                shape = RoundedCornerShape(4.dp),
                                highlight = PlaceholderHighlight.shimmer(
                                    highlightColor = Color.White
                                )
                            ),
                        style = Typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    DrawerBody {
                        scope.launch { drawerState.close() }
                        onNavigate(it.screen)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    debug {
                        Text(
                            text = "version " + BuildConfig.VERSION_NAME,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        },
        drawerBackgroundColor = Color.Transparent,
        drawerContentColor = Color.Transparent,
        content = content,
        drawerElevation = 0.dp
    )
}

@Composable
private fun DrawerBody(
    modifier: Modifier = Modifier,
    onItemClick: (DrawerItem) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        drawerItems.forEachIndexed { index, item ->
            Spacer(modifier = Modifier.height(8.dp))
            DrawerItem(selected = index == 0, item = item) {
                onItemClick(item)
            }
        }
    }
}