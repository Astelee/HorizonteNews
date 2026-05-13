package com.horizontenews.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.net.HttpURLConnection
import java.net.URL

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val titulo = remoteMessage.notification?.title ?: "Horizonte News"
        val mensagem = remoteMessage.notification?.body ?: "Confira a nova notícia!"
        val urlImagem = remoteMessage.notification?.imageUrl?.toString()

        enviarNotificacao(titulo, mensagem, urlImagem)
    }

    private fun enviarNotificacao(titulo: String, mensagem: String, urlImagem: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val canalId = "noticias_horizonte"
        val notificacaoBuilder = NotificationCompat.Builder(this, canalId)
            .setSmallIcon(R.drawable.ic_search_black)
            .setContentTitle(titulo)
            .setContentText(mensagem)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        if (urlImagem != null) {
            val bitmap = getBitmapFromUrl(urlImagem)
            if (bitmap != null) {
                notificacaoBuilder.setLargeIcon(bitmap)
                notificacaoBuilder.setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                        // A CORREÇÃO ESTÁ AQUI: null as Bitmap?
                        .bigLargeIcon(null as Bitmap?) 
                )
            }
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(canalId, "Notícias", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(canal)
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), notificacaoBuilder.build())
    }

    private fun getBitmapFromUrl(imageUrl: String): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}