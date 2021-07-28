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
import androidx.lifecycle.*
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

    //encapsulates the live data, but prevents mutableData from being exposed
    private val _navgiateToSleepQuality = MutableLiveData<SleepNight>()
    //Live data has to be val.
    val navgiateToSleepQuality:LiveData<SleepNight>
    get() = _navgiateToSleepQuality


    //controls the visibility of start button
    val startButtonVisible = Transformations.map(tonight){
        it == null
    }

    //controls the visibility of start button
    val stopButtonVisible = Transformations.map(tonight){
        it != null
    }


    //controls the visibility of the clear button
    val clearButtonVisible = Transformations.map(nights){
        it?.isNotEmpty()
    }


    //Controls when the snack bar appears
    private var _showSnackBarEvent = MutableLiveData<Boolean>()

    //live Data variable to display snackBar
    val showSnackBarEvent:LiveData<Boolean> get() = _showSnackBarEvent


    fun doneShowingSnackBar(){
        _showSnackBarEvent.value = false
    }



    //modifies the string is supposed to look
    val nightsString = Transformations.map(nights) { nights ->
        Log.i("testing", "formating nights $nights")
        formatNights(nights, application.resources)
    }


    //Reset the _navigateToSleepQuality to null
    fun doneNavigating(){
        _navgiateToSleepQuality.value = null
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


            //set tonight to the new night that was inserted
            tonight.value = getTonightFromDatabas()
        }
    }

    fun onStopTracking() {
        viewModelScope.launch {

            //gets the time it was stopped
            val oldNight = tonight.value?: return@launch
            oldNight.endTimeMilli = System.currentTimeMillis()


            //update field with the time the timer was stopped.
            update(oldNight)


            // add the sleepNight obj if it isn't null
            // triggers the observer to notify the GUI
            _navgiateToSleepQuality.value = oldNight



        }
    }

    private suspend fun update(night: SleepNight) {
            database.update(night)

    }

    fun onClear(){
        viewModelScope.launch {
            clear()
            tonight.value = null
            _showSnackBarEvent.value = true
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

