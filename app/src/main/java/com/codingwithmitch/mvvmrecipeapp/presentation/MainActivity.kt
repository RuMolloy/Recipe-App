package com.codingwithmitch.mvvmrecipeapp.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.codingwithmitch.mvvmrecipeapp.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    @Inject
    lateinit var someRandomString: String

    @Inject
    lateinit var app: App

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("TAG", "onCreate: $someRandomString")
        Log.d("TAG", "onCreate: $app")

//        val service = Retrofit.Builder()
//            .baseUrl("https://food2fork.ca/api/recipe/")
//            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
//            .build()
//            .create(RecipeService::class.java)
//
//        CoroutineScope(IO).launch {
//            val response = service.get(
//                token = "Token 9c8b06d329136da358c2d00e76946b0111ce2c48",
//                id = 583
//            )
//            Log.d("TAG", "onCreate: " +response.title)
//        }

//        setContent {
//            ScrollableColumn(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .fillMaxWidth()
//                    .background(color = Color(0xFFF8F8F8))
//            ) {
//                Image(
//                    bitmap = imageFromResource(
//                        res = resources,
//                        resId = R.drawable.happy_meal
//                    ),
//                    modifier = Modifier.height(250.dp),
//                    contentScale = ContentScale.Fit
//                )
//                Column (
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    Row (
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Text(
//                            text = "Happy Meal",
//                            style = TextStyle(
//                                fontSize = TextUnit.Companion.Sp(24)
//                            )
//                        )
//                        Text(
//                            text = "€7.99",
//                            style = TextStyle(
//                                color = Color(0xFF6B8E23),
//                                fontSize = TextUnit.Companion.Sp(18)
//                            ),
//                            modifier = Modifier.align(Alignment.CenterVertically)
//                        )
//                    }
//                    Spacer(modifier = Modifier.padding(top = 8.dp))
//                    Text(
//                        text = "1200 Calories",
//                        style = TextStyle(
//                            fontSize = TextUnit.Companion.Sp(18)
//                        )
//                    )
//                    Spacer(modifier = Modifier.padding(top = 12.dp))
//                    Button(
//                        onClick = {},
//                        modifier = Modifier.align(Alignment.CenterHorizontally),
//                    ) {
//                        Text(text = "ORDER NOW")
//                    }
//                }
//            }
//        }
    }
}