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

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.launch

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
    val database: SleepDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    //nullable mutableLive Data object
    private var tonight = MutableLiveData<SleepNight?>()

    //retrieves a list of all the nights
    private val nights = database.getAllNights()

    val nightsString = Transformations.map(nights) { nights ->
        Log.i("testing", "formating nights $nights")
        formatNights(nights, application.resources)
    }



    init {
        initializeTonight()
    }


    private  fun initializeTonight() {
        //Creates a scope using the viewModel Scope
        // launch creates a coroutine
        viewModelScope.launch {
            tonight.value = getTonightFromDatabas()
        }
    }



    private suspend fun getTonightFromDatabas(): SleepNight? {
        var night = database.getTonight()

        if (night?.endTimeMilli != night?.startTimeMilli) {
            night = null

        }
        return night
    }


    //this gets initialized when the start timer is called
    fun onStartTracking() {
        viewModelScope.launch {
            //    create a new night
            val newNight = SleepNight()
            //inserts the night into the database
            insert(newNight)
            Log.i("testing", "on track pressed")

            //set tonight to the new night that was inserted
            tonight.value = getTonightFromDatabas()
        }
    }

    fun onStopTracking() {
        viewModelScope.launch {
            Log.i("testing", "on stop pressed")
            //gets the time it was stopped
            val oldNight = tonight.value?: return@launch
            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)



        }
    }

    private suspend fun update(night: SleepNight) {
            database.update(night)

    }

    fun onClear(){
        viewModelScope.launch {
            clear()
            tonight.value = null
        }
    }

    private suspend fun clear(){
        database.clear()
    }



    private suspend fun insert(sleepNight: SleepNight) {
        //inserts into a listtrack
        database.insert(sleepNight)
    }


    //
}

