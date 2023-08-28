import java.util.Random

data class PlayerModel (
    var id:Int = getAutoId(),
    var theme:String = "",
    var name:String = "",
    var time:String = "",
    var score:Double = 0.0
){
    companion object{
        fun getAutoId():Int{
            val random = Random()
            return random.nextInt(100)
        }
    }
}