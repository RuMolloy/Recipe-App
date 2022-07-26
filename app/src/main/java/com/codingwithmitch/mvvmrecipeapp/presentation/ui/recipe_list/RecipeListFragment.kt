package com.codingwithmitch.mvvmrecipeapp.presentation.ui.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.codingwithmitch.mvvmrecipeapp.presentation.App
import com.codingwithmitch.mvvmrecipeapp.presentation.components.HeartButtonState
import com.codingwithmitch.mvvmrecipeapp.presentation.components.RecipeList
import com.codingwithmitch.mvvmrecipeapp.presentation.components.SearchAppBar
import com.codingwithmitch.mvvmrecipeapp.presentation.components.util.SnackbarController
import com.codingwithmitch.mvvmrecipeapp.presentation.theme.AppTheme
import com.codingwithmitch.mvvmrecipeapp.presentation.ui.recipe_list.RecipeListEvent.NewSearchEvent
import com.codingwithmitch.mvvmrecipeapp.presentation.ui.recipe_list.RecipeListEvent.NextPageEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalComposeUiApi
@AndroidEntryPoint
class RecipeListFragment : Fragment() {

    @Inject
    lateinit var app: App

    private val snackbarController = SnackbarController(lifecycleScope)

    val viewModel: RecipeListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {

//                val isShowing = remember{ mutableStateOf(false) }
//
//                val snackbarHostState = remember{SnackbarHostState()}
//
//                Column{
//                    Button(
//                        onClick = {
//                            lifecycleScope.launch {
//                                snackbarHostState.showSnackbar(
//                                    message = "Hey look a snackbar!",
//                                    actionLabel = "Hide",
//                                    duration = SnackbarDuration.Short
//                                )
//                            }
//                        }
//                    ){
//                        Text("Show snackbar")
//                    }
//                    DecoupledSnackbarDemo(snackbarHostState = snackbarHostState)
////                    SnackbarDemo(
////                        isShowing = isShowing.value,
////                        onHideSnackbar = {
////                            isShowing.value = false
////                        }
////                    )
//                }


                val recipes = viewModel.recipes.value

                val query = viewModel.query.value

                val selectedCategory = viewModel.selectedCategory.value

                val loading = viewModel.loading.value

                val state = remember { mutableStateOf(HeartButtonState.IDLE) }

                val page = viewModel.page.value

                val scaffoldState = rememberScaffoldState()

                    AppTheme(
                        darkTheme = app.isDark.value,
                        displayProgressBar = loading,
                        scaffoldState = scaffoldState
                    ) {
                        Scaffold(
                            topBar = {
                                SearchAppBar(
                                    query = query,
                                    onQueryChanged = viewModel::onQueryChanged,
                                    onExecuteSearch = {
                                        if(viewModel.selectedCategory.value?.value == "Milk") {
                                            snackbarController.getScope().launch{
                                                snackbarController.showSnackbar(
                                                    scaffoldState = scaffoldState,
                                                    message = "Invalid Category: Milk",
                                                    actionLabel = "Hide",
                                                )
                                            }
                                        }
                                        else {
                                            viewModel.onTriggerEvent(NewSearchEvent)
                                        }
                                    },
                                    firstVisibleItemIndex = viewModel.firstVisibleItemIndex,
                                    firstVisibleItemScrollOffset = viewModel.firstVisibleItemScrollOffset,
                                    selectedCategory = selectedCategory,
                                    onSelectedCategoryChanged = viewModel::onSelectedCategoryChanged,
                                    onChangeCategoryScrollPosition = viewModel::onChangeCategoryScrollPosition,
                                    onToggleTheme = {
                                        app.toggleTheme()
                                    }
                                )
                            },
                            scaffoldState = scaffoldState,
                            snackbarHost = { scaffoldState.snackbarHostState }
                        ){
                            RecipeList(
                                loading = loading,
                                recipes = recipes,
                                onChangeRecipeScrollPosition = viewModel::onChangeRecipeScrollPosition,
                                page = page,
                                onNextPage = {
                                    viewModel.onTriggerEvent(NextPageEvent)
                                },
                                scaffoldState = scaffoldState,
                                snackbarcontroller = snackbarController,
                                navController = findNavController(),
                            )
                        }
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(200.dp),
//                        horizontalArrangement = Arrangement.Center
//                    ){
//                        AnimatedHeartButton(
//                            modifier = Modifier,
//                            buttonState = state,
//                            onToggle = {
//                                state.value = if(state.value == IDLE) ACTIVE else IDLE
//                            }
//                        )
//                    }
//                    PulsingDemo()
//                    Box(modifier = Modifier.fillMaxSize()){
//                        LazyColumn{
//                            itemsIndexed(
//                                items = recipes
//                            ){ index, recipe ->
//                                RecipeCard(recipe = recipe, onClick = {})
//                            }
//                        }
//                        CircularIndeterminateProgressBar(isDisplayed = loading)
//                    }
                    }
                }
            }
        }
    }

@Composable
fun MyBottomBar() {
    BottomNavigation(elevation = 12.dp) {
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.BrokenImage,
                    contentDescription = "Icon"
                )
            },
            selected = false,
            onClick = { }
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Icon"
                )
            },
            selected = true,
            onClick = { }
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.AccountBalanceWallet,
                    contentDescription = "Icon"
                )
            },
            selected = false,
            onClick = { }
        )
    }
}

@Composable
fun MyDrawer() {
    Column() {
        Text("Item1")
        Text("Item2")
        Text("Item3")
        Text("Item4")
        Text("Item5")
    }
}

@Composable
fun DecoupledSnackbarDemo(
    snackbarHostState: SnackbarHostState
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val snackbar = createRef()
        SnackbarHost(
            modifier = Modifier.constrainAs(snackbar){
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            hostState = snackbarHostState,
            snackbar = {
                Snackbar(action = {
                    TextButton(
                        onClick = { snackbarHostState.currentSnackbarData?.dismiss() }
                    ) {
                        Text(
                            text = snackbarHostState.currentSnackbarData?.actionLabel?:"",
                            style = TextStyle(color = Color.White)
                        )
                    }
                }) {
                    Text(text = snackbarHostState.currentSnackbarData?.message?:"")
                }
            }
        )
    }
}

@Composable
fun SnackbarDemo(
    isShowing: Boolean,
    onHideSnackbar:() -> Unit
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val snackbar = createRef()
        if(isShowing) {
            Snackbar(
                modifier = Modifier.constrainAs(snackbar){
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                action = {
                    Text(
                        text = "Hide",
                        modifier = Modifier.clickable(onClick = onHideSnackbar),
                        style = MaterialTheme.typography.h5
                    )
                }
            ) {
                Text(text = "Hey look a snackbar!")
            }
        }
    }
}