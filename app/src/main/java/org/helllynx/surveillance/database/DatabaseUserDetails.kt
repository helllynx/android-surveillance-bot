package org.helllynx.surveillance.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.helllynx.surveillance.domain.UserDetails

@Entity
data class DatabaseUserDetails constructor(
    @PrimaryKey
    val user: String,
    val avatar: String,
    val name: String,
    val userSince: String,
    val location: String
)

fun DatabaseUserDetails.asDomainModel(): UserDetails {
    return UserDetails(
        user = user,
        avatar = avatar,
        name = name,
        userSince = userSince,
        location = location
    )
}