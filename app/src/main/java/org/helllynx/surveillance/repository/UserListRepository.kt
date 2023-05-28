package org.helllynx.surveillance.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import org.helllynx.surveillance.database.UsersDatabase
import org.helllynx.surveillance.database.asDomainModel
import org.helllynx.surveillance.domain.UserListItem
import org.helllynx.surveillance.network.UserListService
import org.helllynx.surveillance.network.model.asDatabaseModel
import timber.log.Timber
import javax.inject.Inject

class UserListRepository @Inject constructor(
    private val userListService: UserListService,
    private val database: UsersDatabase
) {

    val users: LiveData<List<UserListItem>> =
        Transformations.map(database.usersDao.getDatabaseUsers()) {
            it.asDomainModel()
        }

    suspend fun refreshUserList() {
        try {
            val users = userListService.getUserList()
            database.usersDao.insertAll(users.asDatabaseModel())
        } catch (e: Exception) {
            Timber.w(e)
        }
    }
}