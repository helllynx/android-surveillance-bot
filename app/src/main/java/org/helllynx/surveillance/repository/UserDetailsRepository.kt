package org.helllynx.surveillance.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import org.helllynx.surveillance.database.UsersDatabase
import org.helllynx.surveillance.database.asDomainModel
import org.helllynx.surveillance.domain.UserDetails
import org.helllynx.surveillance.network.UserDetailsService
import org.helllynx.surveillance.network.model.asDatabaseModel
import timber.log.Timber
import javax.inject.Inject

class UserDetailsRepository @Inject constructor(
    private val userDetailsService: UserDetailsService,
    private val database: UsersDatabase
) {

    fun getUserDetails(user: String): LiveData<UserDetails> {
        return Transformations.map(database.usersDao.getUserDetails(user)) {
            it?.asDomainModel()
        }
    }


    suspend fun refreshUserDetails(user: String) {
        try {
            val userDetails = userDetailsService.getUserDetails(user)
            database.usersDao.insertUserDetails(userDetails.asDatabaseModel())
        } catch (e: Exception) {
            Timber.w(e)
        }
    }

}