package com.example.kotlinlearning

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import kotlinx.coroutines.launch
import java.util.*



open class SingletonHolder<T, A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator
    @Volatile private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}



object DateConverter {
    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}

@Entity
data class TodoItem(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val title: String,
    val text: String,
    val date: Date
)

@Dao
interface TodoItemDao {
    @Query("Select * From TodoItem WHERE id > :lastId LIMIT :limit ")
    suspend fun getRange(limit:Int,lastId:Int = 0):List<TodoItem>
    @Insert
    suspend fun insert(vararg todo:TodoItem)
    @Update
    suspend fun update(todo:TodoItem)
    @Delete
    suspend fun delete(todo:TodoItem)
}

@Database(entities = [TodoItem::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase:RoomDatabase(){
    companion object : SingletonHolder<AppDatabase, Context>({
        Room.databaseBuilder(it.applicationContext, AppDatabase::class.java, "train.db").build()
    })
    abstract fun todoDao():TodoItemDao
}

class LiveDataHolder<T>(data:T? = null,var onChange:((T?) -> Unit?)? = null){
    private var hasUpdatedSince = false
    var data:T? = data
    get(){

        hasUpdatedSince = false
        return data
    }
    set(value) {
        hasUpdatedSince = true
        field = value
        onChange?.invoke(value)
    }
}

class DbTalker(applicationContext: Context):ViewModel(){
    private var lastId:Int = 0
    private val db = AppDatabase.getInstance(applicationContext)
    private val todoDao = db.todoDao()
    fun getTodo(limit:Int = 25):LiveDataHolder<List<TodoItem>>{
        var result = LiveDataHolder<List<TodoItem>>()
        viewModelScope.launch {
        result.data = todoDao.getRange(limit,lastId)
        lastId += limit
        }
        return result
    }
    fun insertTodoItem(todo:TodoItem) = viewModelScope.launch {
        if(todo.id != null) todoDao.insert(todo.copy(id = null,title = todo.title,text = todo.text,date = todo.date))
        else todoDao.insert(todo)
    }
    fun updateTodoItem(todo:TodoItem) = viewModelScope.launch {todoDao.update(todo)}
    fun deleteTodoItem(todo:TodoItem) = viewModelScope.launch {todoDao.delete(todo)}
}


