package com.codingwithmitch.mvvmrecipeapp.presentation.components

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codingwithmitch.mvvmrecipeapp.R
import com.codingwithmitch.mvvmrecipeapp.domain.model.Recipe
import com.codingwithmitch.mvvmrecipeapp.presentation.components.util.SnackbarController
import com.codingwithmitch.mvvmrecipeapp.presentation.ui.recipe_list.PAGE_SIZE
import com.codingwithmitch.mvvmrecipeapp.presentation.ui.recipe_list.RecipeListEvent
import kotlinx.coroutines.launch

@Composable
fun RecipeList(
    loading: Boolean,
    recipes: List<Recipe>,
    onChangeRecipeScrollPosition: (Int) -> Unit,
    page: Int,
    onNextPage: (RecipeListEvent) -> Unit,
    scaffoldState: ScaffoldState,
    snackbarcontroller: SnackbarController,
    navController: NavController,
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .background(color = MaterialTheme.colors.background)
    ) {
        if(loading && recipes.isEmpty()) {
            LoadingRecipeListItemShimmer(padding = 8.dp, cardHeight = 250.dp)
        }
        else{
            LazyColumn{
                itemsIndexed(
                    items = recipes
                ){ index, recipe ->
                    onChangeRecipeScrollPosition(index)
                    if((index + 1) >= (page* PAGE_SIZE) && !loading) {
                        onNextPage(RecipeListEvent.NextPageEvent)
                    }
                    RecipeCard(recipe = recipe,
                        onClick = {
                            if(recipe.id != null) {
                                val bundle = Bundle()
                                bundle.putInt("recipeId", recipe.id)
                                navController.navigate(R.id.action_view_recipe, bundle)
                            }
                            else {
                                snackbarcontroller.getScope().launch {
                                    snackbarcontroller.showSnackbar(
                                        scaffoldState = scaffoldState,
                                        message = "Recipe Error",
                                        actionLabel = "Ok"
                                    )
                                }
                            }
                        }
                    )
                }
            }

        }
    }
}