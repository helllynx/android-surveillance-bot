package org.helllynx.surveillance.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UsersDao {

    // user List
    @Query("select * from DatabaseUserListItem")
    fun getDatabaseUsers(): LiveData<List<DatabaseUserListItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<DatabaseUserListItem>)

    // single user
    @Query("select * from DatabaseUserDetails WHERE user LIKE :user")
    fun getUserDetails(user: String): LiveData<org.helllynx.surveillance.database.DatabaseUserDetails>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserDetails(databaseUserDetails: org.helllynx.surveillance.database.DatabaseUserDetails)
}

@Database(entities = [DatabaseUserListItem::class, org.helllynx.surveillance.database.DatabaseUserDetails::class], version = 1, exportSchema = false)
abstract class UsersDatabase : RoomDatabase() {
    abstract val usersDao: UsersDao
}