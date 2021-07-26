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

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//creates a database using sleepNight as a table
// indicates version number
@Database(entities = [SleepNight::class], version = 1, exportSchema = false)
abstract class SleepDatabase : RoomDatabase() { //extends RoomDatabase

    abstract val sleepDatabaseDao: SleepDatabaseDao
    companion object {

        //create a private nullable variable to hod the state of the instance
        @Volatile
        private var INSTANCE: SleepDatabase? = null
        //The aim is to have only one instance of the database.

        fun getInstance(context: Context): SleepDatabase {
            //permits only 1 thread to have access
            synchronized(this) {

                var instance = INSTANCE

                if (instance == null) {
                    //invokes Room DATABASEBUILDER  must supply context
                    // requires migrationg strategy   --->  .fallbackToDestructiveMigration
                    // call the ---> .build function to build database
                        //NOTE TO SELF requires context.applicationContext
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SleepDatabase::class.java,
                        "sleep_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                //returns instance
                return instance
            }
        }
    }

}
