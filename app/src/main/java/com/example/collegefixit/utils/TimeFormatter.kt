package com.example.collegefixit.utils

import java.util.concurrent.TimeUnit

object TimeFormatter {
    
    /**
     * Format timestamp to human-readable format like "2 hours ago"
     * 
     * @param timestamp: Timestamp in milliseconds
     * @return Formatted time string (e.g., "2 hours ago", "3 days ago")
     */
    fun formatTimeAgo(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val timeDifferenceMs = now - timestamp
        
        return when {
            timeDifferenceMs < TimeUnit.MINUTES.toMillis(1) -> {
                "just now"
            }
            timeDifferenceMs < TimeUnit.HOURS.toMillis(1) -> {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifferenceMs)
                if (minutes == 1L) "1 minute ago" else "$minutes minutes ago"
            }
            timeDifferenceMs < TimeUnit.DAYS.toMillis(1) -> {
                val hours = TimeUnit.MILLISECONDS.toHours(timeDifferenceMs)
                if (hours == 1L) "1 hour ago" else "$hours hours ago"
            }
            timeDifferenceMs < TimeUnit.DAYS.toMillis(7) -> {
                val days = TimeUnit.MILLISECONDS.toDays(timeDifferenceMs)
                if (days == 1L) "1 day ago" else "$days days ago"
            }
            timeDifferenceMs < TimeUnit.DAYS.toMillis(30) -> {
                val weeks = TimeUnit.MILLISECONDS.toDays(timeDifferenceMs) / 7
                if (weeks == 1L) "1 week ago" else "$weeks weeks ago"
            }
            timeDifferenceMs < TimeUnit.DAYS.toMillis(365) -> {
                val months = TimeUnit.MILLISECONDS.toDays(timeDifferenceMs) / 30
                if (months == 1L) "1 month ago" else "$months months ago"
            }
            else -> {
                val years = TimeUnit.MILLISECONDS.toDays(timeDifferenceMs) / 365
                if (years == 1L) "1 year ago" else "$years years ago"
            }
        }
    }
}
