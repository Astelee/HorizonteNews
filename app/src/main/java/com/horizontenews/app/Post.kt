import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

// ... resto do seu código ...

data class Post(
    val id: String,
    val title: String,
    val content: String,
    val url: String,
    val published: String,           
    val labels: List<String>? = null 
) {
    // Nova função para gerar o "Há X tempo"
    fun getTempoRelativo(): String {
        return try {
            // Define o formato de data que o Blogger envia
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            
            val dataPublicacao = sdf.parse(published)?.time ?: 0L
            val agora = System.currentTimeMillis()

            // Gera o texto "há X minutos/horas" automaticamente em português
            val rts = DateUtils.getRelativeTimeSpanString(
                dataPublicacao, 
                agora, 
                DateUtils.MINUTE_IN_MILLIS
            )
            
            rts.toString()
        } catch (e: Exception) {
            "Recente" // Caso dê algum erro na conversão
        }
    }

    // ... suas outras funções (firstImage, firstLabel) ...
}