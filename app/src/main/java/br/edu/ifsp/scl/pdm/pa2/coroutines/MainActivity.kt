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

            GlobalScope.launch(kotlinx.coroutines.Dispatchers.Default) {
                Log.v(
                    getString(R.string.app_name),
                    "Top Coroutine thread ${Thread.currentThread().name}, " +
                            "Job: ${coroutineContext[Job]}"
                )
                val upper = sleep("Upper", random.nextLong(SLEEP_LIMIT))
                withContext(Dispatchers.Main + Job()) {
                    Log.v(
                        getString(R.string.app_name),
                        "But this code is executing in thread ${Thread.currentThread().name}, Job: ${coroutineContext[Job]}"
                    )
                    amb.upperTv.text = upper
                }
                Log.v(
                    getString(R.string.app_name),
                    "Top Coroutine thread after main ${Thread.currentThread().name}, " +
                            "Job: ${coroutineContext[Job]}"
                )
                launch(Dispatchers.IO) {
                    Log.v(
                        getString(R.string.app_name),
                        "Lower async coroutine thread ${Thread.currentThread().name}, Job: ${coroutineContext[Job]}"
                    )
                    sleep("Lower", random.nextLong(SLEEP_LIMIT)).let {
                        runOnUiThread {
                            amb.lowerTv.text = it
                        }
                    }

                    Log.v(getString(R.string.app_name), "Lower coroutine completed")
                }
                Log.v(getString(R.string.app_name), "Top Coroutine completed")
            }
            Log.v(
                getString(R.string.app_name),
                "Main thread ${Thread.currentThread().name}"
            )
        }
    }

    private suspend fun sleep(name: String, time: Long): String {
        kotlinx.coroutines.delay(time)
        return "$name slept for $time ms."
    }

}