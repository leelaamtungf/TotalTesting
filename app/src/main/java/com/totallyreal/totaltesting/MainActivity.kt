package com.totallyreal.totaltesting

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.totallyreal.totaltesting.ui.theme.TotalTestingTheme

class MainActivity : ComponentActivity() {
    private var doneAd = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TotalTestingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column {
                        Row {
                            SkipAdButton()
                        }
                        if (doneAd.value) {
                            Greeting(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            )
                        } else {
                            var interstitialAd: InterstitialAd? = null
                            Row {
                                PolicyButton()
                                SkipAdButton()
                            }
                            fun loadAd(context: Context) {
                                InterstitialAd.load(
                                    context,
                                    "ca-app-pub-3940256099942544/1033173712",
                                    AdRequest.Builder().build(),
                                    object : InterstitialAdLoadCallback() {
                                        override fun onAdFailedToLoad(p0: LoadAdError) {
                                            super.onAdFailedToLoad(p0)
                                            interstitialAd = null
                                        }

                                        override fun onAdLoaded(p0: InterstitialAd) {
                                            interstitialAd = p0

                                        }
                                    }
                                )
                            }
                            fun showAd(context: Context, onAdDismissed: () -> Unit) {
                                if (interstitialAd !=null){
                                    interstitialAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                                        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                            super.onAdFailedToShowFullScreenContent(p0)
                                            interstitialAd = null
                                        }

                                        override fun onAdDismissedFullScreenContent() {
                                            super.onAdDismissedFullScreenContent()
                                            interstitialAd = null
                                            loadAd(context)
                                            onAdDismissed()
                                        }
                                    }
                                    interstitialAd!!.show(context as Activity)
                                }
                            }
                            loadAd(context = applicationContext)
                            val coroutineScope = rememberCoroutineScope()
                            showAd(context = applicationContext) {
                                Toast.makeText(applicationContext,"showing Ad",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    Text(
        text = "No more Ad :)",
        modifier = modifier
    )
}

@Composable
fun SkipAdButton() {
    Button(onClick = {
        //TODO
    }) { Text("Skip Ad") }
}

@Composable
fun PolicyButton() {
    Button(onClick = {

    }) { Text("Privacy Policy") }
}