package com.codingwithmitch.mvvmrecipeapp.presentation.ui.recipe_list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingwithmitch.mvvmrecipeapp.domain.model.Recipe
import com.codingwithmitch.mvvmrecipeapp.presentation.ui.recipe_list.RecipeListEvent.*
import com.codingwithmitch.mvvmrecipeapp.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

const val PAGE_SIZE = 30

const val STATE_KEY_PAGE = "recipe.state.page.key"
const val STATE_KEY_QUERY = "recipe.state.query.key"
const val STATE_KEY_LIST_POSITION = "recipe.state.query.list_position"
const val STATE_KEY_SELECTED_CATEGORY = "recipe.state.query.selected_category"

@HiltViewModel
class RecipeListViewModel @Inject
constructor(private val repository: RecipeRepository,
            @Named("auth_token") private val token: String,
            private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val recipes: MutableState<List<Recipe>> = mutableStateOf(listOf())

    val query = mutableStateOf("Chicken")

    val selectedCategory: MutableState<FoodCategory?> = mutableStateOf(null)

    var firstVisibleItemIndex: Int = 0
    var firstVisibleItemScrollOffset: Int = 0

    val loading = mutableStateOf(false)

    val page = mutableStateOf(1)

    var recipeListScrollPosition = 0

    init {
        savedStateHandle.get<Int>(STATE_KEY_PAGE)?.let{ p -> setPage(p)}
        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let{ q -> setQuery(q)}
        savedStateHandle.get<Int>(STATE_KEY_LIST_POSITION)?.let{ p -> setListScrollPosition(p)}
        savedStateHandle.get<FoodCategory>(STATE_KEY_SELECTED_CATEGORY)?.let{ c -> setSelectedCategory(c)}

        if(recipeListScrollPosition != 0) {
            onTriggerEvent(RestoreStateEvent)
        }
        else{
            onTriggerEvent(NewSearchEvent)
        }
    }

    fun onTriggerEvent(event: RecipeListEvent) {
        viewModelScope.launch {
            try{
                when(event) {
                    is NewSearchEvent -> {
                        newSearch()
                    }
                    is NextPageEvent -> {
                        nextPage()
                    }
                    is RestoreStateEvent -> {
                        restoreState()
                    }
                }
            }catch (e: Exception){
                Log.e("TAG", "onTriggerEvent: " +e.cause)
            }
        }
    }

    private suspend fun newSearch() {
        viewModelScope.launch {
            loading.value = true

            resetSearchState()

            delay(2000) //just adding delay so progress bar will be visible

            val result = repository.search(
                token = token,
                page = 1,
                query = query.value
            )
            recipes.value = result

            loading.value = false
        }
    }

    private suspend fun nextPage() {
        //prevent duplicate events due to recompose happening to quickly
        if(recipeListScrollPosition + 1 >= (page.value * PAGE_SIZE)) {
            loading.value = true
            incrementPage()
            Log.d("TAG", "nextPage: triggered: ${page.value}")

            //just to show pagination
            delay(1000)

            if(page.value > 1) {
                val result = repository.search(
                    token = token,
                    page = page.value,
                    query = query.value
                )
                Log.d("TAG", "nextPage: ${result}")
                appendRecipes(result)
            }
            loading.value = false
        }
    }

    private suspend fun restoreState() {
        loading.value = true
        val results: MutableList<Recipe> = mutableListOf()
        for(p in 1..page.value) {
            val result = repository.search(
                token = token,
                page = p,
                query = query.value
            )
            results.addAll(result)
            if(p == page.value) {
                //done
                recipes.value = results
                loading.value = false
            }
        }
    }

    private fun appendRecipes(recipes: List<Recipe>) {
        val current = ArrayList(this.recipes.value)
        current.addAll(recipes)
        this.recipes.value = current
    }

    private fun incrementPage() {
        setPage(page = page.value + 1)
    }

    fun onChangeRecipeScrollPosition(position: Int) {
        setListScrollPosition(position = position)
    }

    private fun resetSearchState() {
        recipes.value = listOf()
        page.value
        onChangeRecipeScrollPosition(0)
        if(selectedCategory.value?.value != query.value) {
            clearSelectedCategory()
        }
    }

    private fun clearSelectedCategory() {
        setSelectedCategory(null)
    }

    fun onQueryChanged(query: String) {
        setQuery(query)
    }

    fun onSelectedCategoryChanged(category: String) {
        val newCategory = getFoodCategory(category)
        setSelectedCategory(newCategory)
        onQueryChanged(category)
    }

    fun onChangeCategoryScrollPosition(a: Int, b: Int) {
        firstVisibleItemIndex = a
        firstVisibleItemScrollOffset = b
    }

    private fun setListScrollPosition(position: Int) {
        recipeListScrollPosition = position
        savedStateHandle.set(STATE_KEY_LIST_POSITION, recipeListScrollPosition)
    }

    private fun setPage(page: Int) {
        this.page.value = page
        savedStateHandle.set(STATE_KEY_PAGE, page)
    }

    private fun setSelectedCategory(category: FoodCategory?) {
        selectedCategory.value = category
        savedStateHandle.set(STATE_KEY_SELECTED_CATEGORY, category)
    }

    private fun setQuery(query: String) {
        this.query.value = query
        savedStateHandle.set(STATE_KEY_QUERY, query)
    }
}