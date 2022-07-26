package com.codingwithmitch.mvvmrecipeapp.presentation.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.codingwithmitch.mvvmrecipeapp.presentation.App
import com.codingwithmitch.mvvmrecipeapp.presentation.components.IMAGE_HEIGHT
import com.codingwithmitch.mvvmrecipeapp.presentation.components.LoadingRecipeShimmer
import com.codingwithmitch.mvvmrecipeapp.presentation.components.RecipeView
import com.codingwithmitch.mvvmrecipeapp.presentation.components.util.SnackbarController
import com.codingwithmitch.mvvmrecipeapp.presentation.theme.AppTheme
import com.codingwithmitch.mvvmrecipeapp.presentation.ui.recipe.RecipeEvent.GetRecipeEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    @Inject
    lateinit var application: App

    private val snackbarController = SnackbarController(lifecycleScope)

    val viewModel: RecipeViewModel by viewModels()

    private var recipeId: MutableState<Int> = mutableStateOf(-1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        println("RecipeFragment: $viewModel")
//        CoroutineScope(Main).launch {
//            delay(1000)
//            arguments?.getInt("recipeId")?.let{ rId ->
//                recipeId.value = rId
//            }
//        }
        arguments?.getInt("recipeId")?.let{ recipeId ->
            viewModel.onTriggerEvent(GetRecipeEvent(recipeId))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {

                val loading = viewModel.loading.value

                val recipe = viewModel.recipe.value

                val scaffoldState = rememberScaffoldState()

                AppTheme(
                    darkTheme = application.isDark.value,
                    displayProgressBar = loading,
                    scaffoldState = scaffoldState
                ) {
                    Scaffold(
                        scaffoldState = scaffoldState,
                        snackbarHost = {
                            scaffoldState.snackbarHostState
                        }
                    ) {
                        Box(modifier = Modifier
                            .fillMaxSize()
                        ) {
                            if(loading && recipe == null) {
                                LoadingRecipeShimmer(
                                    cardHeight = IMAGE_HEIGHT.dp,
                                )
                            }
                            else{
                                recipe?.let { it ->
                                    if(it.id == 1) {
                                        snackbarController.showSnackbar(
                                            scaffoldState = scaffoldState,
                                            message = "An error occurred with this recipe",
                                            actionLabel = "Ok"
                                        )
                                    }
                                    else{
                                        RecipeView(recipe = it)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}