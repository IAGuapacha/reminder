package com.iaguapacha.reminder.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iaguapacha.reminder.data.model.LabelEntity

@Dao
interface LabelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLabel(label: LabelEntity)

    @Delete
    suspend fun deleteLabel(label: LabelEntity)

    @Query("SELECT * FROM LabelEntity WHERE labelId = :labelId")
    suspend fun getLabelById(labelId: Long): LabelEntity?

    @Query("SELECT * FROM LabelEntity")
    suspend fun getAllLabels(): List<LabelEntity>
}