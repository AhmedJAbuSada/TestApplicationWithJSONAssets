package com.teknasyontestapplication.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.teknasyontestapplication.data.model.PositionResponse
import com.teknasyontestapplication.data.model.Satellite
import com.teknasyontestapplication.databinding.ActivityDetailsBinding
import com.teknasyontestapplication.ui.MainApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


class DetailsActivity : AppCompatActivity() {

    private var mViewModel: DetailsViewModel? = null
    private var binding: ActivityDetailsBinding? = null
    private var executor: ScheduledExecutorService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = ViewModelProvider(this)[DetailsViewModel::class.java]

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        binding?.lifecycleOwner = this
        binding?.viewmodel = mViewModel
        setContentView(binding?.root)

        binding?.backImageView?.setOnClickListener {
            finish()
        }

        parser()
    }

    companion object {
        const val satelliteKey = "satellite_"
        const val satelliteObjKey = "satellite"
    }

    private fun parser() {
        try {

            runBlocking {
                GlobalScope.launch(Dispatchers.Main) {
                    val prefs = getSharedPreferences("SHARED_PREFS_FILE", MODE_PRIVATE)
                    val satellite = intent.getParcelableExtra<Satellite>(satelliteObjKey)
                    val satelliteId = satellite?.id
                    val satelliteIdPref = "$satelliteKey$satelliteId"

                    val details: Satellite?

                    if (prefs.contains(satelliteIdPref)) {
                        val turnsTypePositions = object : TypeToken<Satellite>() {}.type
                        details = Gson().fromJson(
                            prefs.getString(satelliteIdPref, ""),
                            turnsTypePositions
                        )
                    } else {
                        val json = MainApplication.loadJSONFromAsset(
                            this@DetailsActivity,
                            MainApplication.JsonFileType.DETAIL
                        )
                        val turnsType = object : TypeToken<MutableList<Satellite>>() {}.type
                        val detailsList: MutableList<Satellite> = Gson().fromJson(json, turnsType)

                        details = detailsList.find { it.id == satelliteId }

                        if (details != null) {
                            val editor = prefs.edit()
                            editor.putString(satelliteIdPref, Gson().toJson(details))
                            editor.apply()
                        }
                    }

                    mViewModel?.titleTextView?.emit(satellite?.name ?: "")
                    mViewModel?.dateTextView?.emit(details?.firstFlight ?: "")
                    mViewModel?.heightMassTextView?.emit("${details?.height ?: ""}/${details?.mass ?: ""}")
                    mViewModel?.costTextView?.emit(
                        NumberFormat.getNumberInstance(Locale.US)
                            .format(details?.costPerLaunch ?: 0)
                    )

                    val jsonPositions =
                        MainApplication.loadJSONFromAsset(
                            this@DetailsActivity,
                            MainApplication.JsonFileType.POSITION
                        )
                    val turnsTypePositions = object : TypeToken<PositionResponse>() {}.type
                    val positionsList: PositionResponse =
                        Gson().fromJson(jsonPositions, turnsTypePositions)
                    val position = positionsList.list?.find { it.id == satelliteId }
                    executor = Executors.newSingleThreadScheduledExecutor()
                    executor?.scheduleAtFixedRate({
                        GlobalScope.launch(Dispatchers.IO) {
                            mViewModel?.lastPositionTextView?.emit(
                                "${
                                    position?.position?.get(
                                        (0..2).random()
                                    )?.posX ?: 0.0
                                } , ${position?.position?.get(0)?.posY ?: 0.0}"
                            )
                        }
                    }, 0, 3, TimeUnit.SECONDS)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        executor?.shutdown()
    }
}