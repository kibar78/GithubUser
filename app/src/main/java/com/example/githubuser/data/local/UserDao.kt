package com.example.githubuser.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: UserEntity)

    @Update
    suspend fun update(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("SELECT * FROM userentity WHERE username = :username")
    suspend fun getFavoriteUserByUsername(username: String): UserEntity?

    @Query("SELECT * FROM userentity")
    suspend fun getAllFavorite(): List<UserEntity>
}