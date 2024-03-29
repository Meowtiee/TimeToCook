package ru.meowtee.timetocook.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.meowtee.timetocook.core.extensions.ioThread
import ru.meowtee.timetocook.data.db.RecipesDb
import ru.meowtee.timetocook.data.model.Receipt
import kotlin.properties.Delegates

class RandomReceiptViewModel : ViewModel() {
    private val _receipts = MutableStateFlow<List<Receipt>>(emptyList())
    val receipts: StateFlow<List<Receipt>> = _receipts

    private val db by lazy { RecipesDb.getInstance(context) }
    private var context: Context by Delegates.notNull()

    fun findReceipts() {
        ioThread {
            Log.e("Ahahahahaha", "${db.recipesDao().getAllRecipes()}")
            _receipts.value = db.recipesDao()
                .getAllRecipes()
                .takeIf { it.isNotEmpty() }
                ?.shuffled()
                ?.subList(0, 2) ?: emptyList()
        }
    }

    fun startDatabase(context: Context) {
        this.context = context
        ioThread {
            db.recipesDao().getAllRecipes()
        }
    }
}