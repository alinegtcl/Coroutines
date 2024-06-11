package br.edu.ifsp.scl.pdm.pa2.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.pdm.pa2.coroutines.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.*
import kotlin.random.Random
import android.util.Log

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        amb.launchCoroutinesBt.setOnClickListener {

            val random = Random(System.currentTimeMillis())
            val SLEEP_LIMIT = 3000L

            var upperText = "Upper before sleep"
            var lowerText = "Lower before sleep"

            GlobalScope.launch(kotlinx.coroutines.Dispatchers.Main) {
                upperText = sleep("Upper", random.nextLong(SLEEP_LIMIT))
                Log.v(
                    getString(R.string.app_name),
                    "Coroutine thread ${Thread.currentThread().name}, " +
                            "Job: ${coroutineContext[Job]}"
                )
                amb.upperTv.text = upperText
            }

            GlobalScope.launch(Dispatchers.Unconfined) {
                lowerText = sleep("Lower", random.nextLong(SLEEP_LIMIT))
                Log.v(
                    getString(R.string.app_name),
                    "Coroutine thread ${Thread.currentThread().name}, Job: ${coroutineContext[Job]}"
                )
                runOnUiThread {
                    amb.lowerTv.text = lowerText
                }
            }
        }
    }
    private suspend fun sleep(name: String, time: Long): String {
        kotlinx.coroutines.delay(time)
        return "$name slept for $time ms."
    }

}