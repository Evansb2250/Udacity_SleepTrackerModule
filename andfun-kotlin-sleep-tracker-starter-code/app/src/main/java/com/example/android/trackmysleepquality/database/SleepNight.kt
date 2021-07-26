/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
specifies data class is an entity in our room database.
must specify the name of the table
 */
// You can set composite primary key by  @Entity(primaryKeys = arrayOf("keyOne", "keyTwo"))
@Entity(tableName = "daily_sleep_quality_table")
data class SleepNight(
    @PrimaryKey(autoGenerate = true)  // annotation needed for room to specify column is the key, automatically generates key
    var nightId: Long = 0L , // id for the instance (must specify column is primary key
    @ColumnInfo(name = "start_time_milli") // specifiy name of the column
    var startTimeMilli: Long = System.currentTimeMillis(), // tracks the start time in miliseconds
    @ColumnInfo(name = "end_time_milli") // specify name of column
    var endTimeMilli: Long = startTimeMilli, // ending time
    @ColumnInfo(name="sleepQuality")
    var sleepQuality: Int = -1 // rating for the sleep quality
)