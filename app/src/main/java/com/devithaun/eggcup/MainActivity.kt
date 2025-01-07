package com.devithaun.eggcup

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devithaun.eggcup.ui.theme.EggCupTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EggCupTheme {
                EggCup()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EggCup() {
    var selectedOption by remember { mutableStateOf("") }
    var timerValue by remember { mutableLongStateOf(0L) }
    var timeRemaining by remember { mutableLongStateOf(0L) }
    var timerRunning by remember { mutableStateOf(false) }

    // TODO: alarm sound
//    val mediaPlayer = MediaPlayer.create(LocalContext.current, R.raw.xxx)
    var countDownTimer: CountDownTimer? = null

    fun startTimer(timeInMillis: Long) {
        countDownTimer?.cancel()
        timeRemaining = timeInMillis
        timerRunning = true

        countDownTimer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(p0: Long) {
                timeRemaining = p0
            }

            override fun onFinish() {
                timerRunning = false
                timeRemaining = 0L
//                mediaPlayer.start()
            }
        }
        countDownTimer?.start()
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("EggCup") })
    }) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            if (!timerRunning) {
                Text(
                    text = "Let's boil an egg!",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                listOf(
                    "Soft-Boiled" to 300000L,
                    "Medium-Boiled" to 420000L,
                    "Hard-Boiled" to 600000L
                ).forEach { (option, time) ->
                    Button(onClick =  {
                        selectedOption = option
                        timerValue = time
                        startTimer(time)
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)) {
                        Text(text = option)
                    }
                }
            }
            else {
                EggAnim()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Cooking your $selectedOption egg...",
                    fontSize = 20.sp
                )
                Text(
                    text = "Time remaining: ${timeRemaining / 1000} seconds",
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        countDownTimer?.cancel()
                        timerRunning = false
                        timeRemaining = 0L
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(text = "Stop Cookin'?", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun EggAnim() {
    val infiniteTransition = rememberInfiniteTransition("egg")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ), label = "egg_anim"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary.copy(0.0f))
            .scale(scale)
    ) {
        Image(
            painter = painterResource(id = R.drawable.egg),
            contentDescription = "Egg"
        )
    }
}