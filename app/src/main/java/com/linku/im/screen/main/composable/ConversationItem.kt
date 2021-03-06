package com.linku.im.screen.main.composable

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.linku.im.extension.ifTrue
import com.linku.im.extension.times
import com.linku.im.screen.main.MainState

@Composable
fun ConversationItem(
    modifier: Modifier = Modifier,
    conversation: MainState.ConversationMainUI? = null,
    unreadCount: Int = 0,
    pinned: Boolean = false,
    onClick: () -> Unit = {}
) {
    val shimmerColor = MaterialTheme.colorScheme.outline * 0.3f
    val onShimmerColor = Color.White
    val shimmerAnimationSpec: InfiniteRepeatableSpec<Float> by lazy {
        infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                delayMillis = 400
            ),
            repeatMode = RepeatMode.Restart
        )
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .let { pinned.ifTrue { it.background(MaterialTheme.colorScheme.surface) } ?: it }
            .clickable(
                enabled = (conversation != null),
                onClick = onClick
            )
            .padding(
                horizontal = 12.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleHeadPicture(
            model = conversation?.image,
            name = conversation?.name,
            placeholder = { TextImage(text = conversation?.name) }
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier
                .padding(
                    end = 12.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = conversation?.name ?: "",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .placeholder(
                        visible = conversation == null,
                        color = shimmerColor,
                        shape = RoundedCornerShape(4.dp),
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = onShimmerColor,
                            animationSpec = shimmerAnimationSpec,
                        )
                    ),
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = conversation?.content ?: "",
                maxLines = 1,
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .placeholder(
                        visible = (conversation == null),
                        color = shimmerColor,
                        shape = RoundedCornerShape(4.dp),
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = onShimmerColor,
                            animationSpec = shimmerAnimationSpec
                        ),
                    )
                    .fillMaxWidth(),
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        when {
            (unreadCount != 0) -> {
                Surface(
                    shape = RoundedCornerShape(100),
                    color = MaterialTheme.colorScheme.tertiary
                ) {
                    Text(
                        text = unreadCount.toString(),
                        color = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            pinned -> {
                Surface(
                    shape = RoundedCornerShape(100),
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CircleHeadPicture(
    model: Any?,
    name: String?,
    modifier: Modifier = Modifier,
    placeholder: @Composable (String?) -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(100),
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxHeight()
            .aspectRatio(1f)
    ) {
        SubcomposeAsyncImage(
            model = model,
            contentDescription = name,
            modifier = Modifier
                .fillMaxSize(),
            error = {
                placeholder(name)
            },
            loading = {
                placeholder(name)
            }
        )
    }
}

@Composable
fun TextImage(text: String?) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        if (!text.isNullOrBlank()) {
            Text(
                text = text.first().toString(),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center,
            )
        }
    }
}

