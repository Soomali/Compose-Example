package com.example.kotlinlearning

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import android.widget.LinearLayout



class SharedSingleton {
    companion object {
        val instance = SharedSingleton()
        var text = "HeHeHeHe"
    }
}




class TextResolver(var text:String){
    private var count:Int = 0
    fun restoreText():String{
        val x = "$text $count Text"
        x.plus(15)
        return x
    }
    fun resolveText(){
        if(text.isNotEmpty()){
            this.text = text.subSequence(0,text.length - 1).toString()
        }else{
            this.text = "Emptied for $count"
            count++
        }
    }

}








abstract class ElementReference<T,K:ElementData>(val id:Int,val properties:K,val context:Context){
    abstract  fun createElement():T?
}
abstract class ElementData(open var width:Int,open var height:Int,open var layOut:Int)
data class ButtonData(var backgroundColor:Int?,var onClick:() -> Unit?, var text: String?, override var width: Int,
                      override var height: Int,  override var layOut: Int
):ElementData(width, height, layOut)

class ButtonReference(id: Int, properties:ButtonData,
                      context: Context
) : ElementReference<Button,ButtonData>(id,
    properties,context
) {
    private var isAdded = false
    var button:Button?
    fun <K>addInto(layout:K){
        var _layout = layout as? ConstraintLayout ?: layout as? LinearLayout ?: return
        if(!isAdded){
            _layout.addView(button)
        }
        isAdded = true
    }
    init {
        button = createElement()
    }

    override fun createElement(): Button? {
        val button = Button(context)
        button.id = id

        when(properties.layOut){
            0 ->  button.layoutParams = ConstraintLayout.LayoutParams(properties.width,properties.height)
            1 ->  button.layoutParams = LinearLayout.LayoutParams(properties.width,properties.height)
        }


        if(properties.onClick != null) button.setOnClickListener {
            properties.onClick()
        }
        button.text = properties.text ?: "Button"

        if(properties.backgroundColor != null) button.setBackgroundColor(properties.backgroundColor!!)
        return button

    }

}

class ButtonAdapter(private val buttonList:MutableList<com.example.kotlinlearning.ButtonReference> = mutableListOf() ) : BaseAdapter() {

    override fun getCount(): Int {
        return buttonList.count()
    }
    fun add(ref: com.example.kotlinlearning.ButtonReference){
        buttonList.add(ref)
    }
    override fun getItem(p0: Int): Button {
        var ref = buttonList[p0] as com.example.kotlinlearning.ButtonReference
        return ref.button!!
    }

    override fun getItemId(p0: Int): Long {
        var x = buttonList[p0] as com.example.kotlinlearning.ButtonReference
        return x.id.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        return getItem(p0)
    }
}


/*{}
class Trying(var x:Int) {
    val y = x.div(x/2)
    init {
        var y = x
    }
    val k get() = y * x
fun x(y:Int?) =  y?.rem(16) ?: null
fun c(k:Byte){

    this.k.rem(k)
}
}

 */