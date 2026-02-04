package com.example.collegefixit.utils

import java.util.Calendar

object YearCalculator {
    
    /**
     * Extract academic year from email/userId (roll number)
     * 
     * @param userId: User ID or email containing roll number (e.g., "22B01012@iiitl.ac.in")
     * @return Formatted year string (e.g., "B.Tech 3rd year")
     */
    fun extractYearFromUserId(userId: String): String {
        return try {
            // Extract roll number from userId if it contains email format
            val rollNo = if (userId.contains("@")) {
                userId.substring(0, userId.indexOf('@'))
            } else {
                userId
            }
            
            // Extract admission year from roll number (characters at index 3-7)
            if (rollNo.length >= 7) {
                val admissionYear = rollNo.substring(3, 7).toInt()
                val calendar = Calendar.getInstance()
                val currentYear = calendar.get(Calendar.YEAR)
                val currentMonth = calendar.get(Calendar.MONTH)

                val acadYear = if (currentMonth >= Calendar.AUGUST) {
                    currentYear - admissionYear + 1
                } else {
                    currentYear - admissionYear
                }

                val suffix = when {
                    acadYear in 11..13 -> "th"
                    acadYear % 10 == 1 -> "st"
                    acadYear % 10 == 2 -> "nd"
                    acadYear % 10 == 3 -> "rd"
                    else -> "th"
                }
                "B.Tech ${acadYear}${suffix} year"
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }
    }
}
