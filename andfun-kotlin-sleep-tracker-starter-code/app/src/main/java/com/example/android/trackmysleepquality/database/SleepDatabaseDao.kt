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

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

//interface for interacting with the DAO
// needs to be annotatedt with the DAO keyword
@Dao
interface SleepDatabaseDao {

 //need insert annotation and function definition that is called from the code
 @Insert //Room will generate the data needed for this function
 suspend fun insert(night: SleepNight) {
 }   // suspend is used for coroutines which allows us to create Asychronous program fluently.
 // suspend means this function can be blocked or suspended
// Can only be called within a courotine


 @Update  //updates a previously entered night instance in the stable
 suspend fun update(night: SleepNight) {
 }

//used to specify a more complex SQL command

 @Query("SELECT * from daily_sleep_quality_table WHERE nightId =:key")
 suspend fun get(key: Long): SleepNight?

 /*
 uses the Delete From keyword to remove all entries in the daily_sleep_quality_table
  */
 @Query("DELETE FROM daily_sleep_quality_table ")
 suspend fun clear()


 /*
 Query to get all the nights
  */
 @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC")
  fun getAllNights(): LiveData<List<SleepNight>>


/*
gets all the instances of sleep night
orders it by descending order
limits for only 1 item to be selected
 */
  @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC LIMIT 1")
  fun getTonight():SleepNight? // sleepNight is nullable



}
