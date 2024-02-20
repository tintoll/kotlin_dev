package day3_oop

import java.util.Properties
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

interface Sender {
    fun send(item: Item)
}

class PrintSender : Sender {
    override fun send(item: Item) {
        println("Reminder")
        println("[${item.title}]")
        println(item.content)
    }
}

class GmailSender(user:String, pw:String,sender: String, receiver:String ):Sender {
    private val message = MimeMessage(Session.getDefaultInstance(Properties().also {
        it["mail.smtp.host"] = "smtp.gmail.com"
        it["mail.smtp.auth"] = "true"
        it["mail.smtp.starttls.enable"] = "true"
        it["mail.smtp.port"] = "587"
        it["mail.smtp.ssl.trust"] = "smtp.gmail.com"
    }, object : Authenticator() {
        override fun getPasswordAuthentication() = PasswordAuthentication(user, pw)
    })).also {
        it.setFrom(sender)
        it.addRecipient(MimeMessage.RecipientType.TO, InternetAddress(receiver))
    }
    override fun send(item: Item) {
        message.subject = item.title
        message.setText(item.content)
        Transport.send(message)
    }
}