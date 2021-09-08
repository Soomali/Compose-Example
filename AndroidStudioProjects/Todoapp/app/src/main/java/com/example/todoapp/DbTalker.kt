package com.example.todoapp


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.*
import androidx.room.RoomDatabase
import kotlinx.coroutines.launch
import java.util.*


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
data class Todo(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title: String,
    val text: String,
    val date: Date = Date()
)

@Dao
interface TodoItemDao {
    @Query("Select * From Todo WHERE id > :lastId LIMIT :limit ")
    suspend fun getRange(limit:Int,lastId:Int = 0):List<Todo>
    @Insert
    suspend fun insert(vararg todo: Todo)
    @Update
    suspend fun update(todo:Todo)
    @Delete
    suspend fun delete(todo:Todo)
}

@Database(entities = [Todo::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase:RoomDatabase(){
    companion object : SingletonHolder<AppDatabase, Context>({
        Room.databaseBuilder(it.applicationContext, AppDatabase::class.java, "train.db").build()
    })
    abstract fun todoDao():TodoItemDao
}

class DbTalker(applicationContext: Context):ViewModel(){
    private var lastId:Int = 0
    private val db = AppDatabase.getInstance(applicationContext)
    private val todoDao = db.todoDao()
    fun getTodo(limit:Int = 25, result:LiveDataHolder<List<Todo>>){
        viewModelScope.launch {
            result.data = todoDao.getRange(limit,lastId)
            lastId += limit
        }

    }
    fun insertTodoItem(todo:Todo) = viewModelScope.launch {
        if(todo.id != null) todoDao.insert(todo.copy(id = null,title = todo.title,text = todo.text,date = todo.date))
        else todoDao.insert(todo)
    }
    fun updateTodoItem(todo:Todo) = viewModelScope.launch {todoDao.update(todo)}
    fun deleteTodoItem(todo:Todo) = viewModelScope.launch {todoDao.delete(todo)}
}
