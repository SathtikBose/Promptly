package com.buildstack.promptly.presentation.chat

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

fun parseMarkdown(text: String): AnnotatedString {
    return buildAnnotatedString {
        var currentIndex = 0
        val regex = Regex("(\\*\\*(.*?)\\*\\*|\\*(.*?)\\*|`(.*?)`)")
        val matches = regex.findAll(text)
        
        for (match in matches) {
            val startIndex = match.range.first
            if (startIndex > currentIndex) {
                append(text.substring(currentIndex, startIndex))
            }
            when {
                match.value.startsWith("**") -> {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(match.groupValues[2])
                    }
                }
                match.value.startsWith("*") -> {
                    withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                        append(match.groupValues[3])
                    }
                }
                match.value.startsWith("`") -> {
                    withStyle(style = SpanStyle(fontFamily = FontFamily.Monospace, background = Color.DarkGray.copy(alpha=0.2f))) {
                        append(match.groupValues[4])
                    }
                }
            }
            currentIndex = match.range.last + 1
        }
        if (currentIndex < text.length) {
            append(text.substring(currentIndex))
        }
    }
}
