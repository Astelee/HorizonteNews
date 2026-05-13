package com.horizontenews.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // Este método é chamado sempre que o Firebase envia uma nova notificação
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Verifica se a mensagem contém uma notificação
        remoteMessage.notification?.let {
            enviarNotificacao(it.title ?: "Horizonte News", it.body ?: "Confira a nova notícia!")
        }
    }

    private fun enviarNotificacao(titulo: String, mensagem: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        
        // Configura o que acontece ao clicar na notificação (abre o app)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val canalId = "noticias_horizonte"
        val notificacaoBuilder = NotificationCompat.Builder(this, canalId)
            .setSmallIcon(R.drawable.ic_search_black) // Pode mudar para o ícone do seu app depois
            .setContentTitle(titulo)
            .setContentText(mensagem)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Canais de notificação são obrigatórios a partir do Android 8 (Oreo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(canalId, "Notícias", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(canal)
        }

        notificationManager.notify(0, notificacaoBuilder.build())
    }
}