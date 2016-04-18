/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.promocionando.mdb;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import mx.riyoce.promocionando.authenticators.AutentificadorDeMail;
import mx.riyoce.promocionando.entities.Attachment;
import mx.riyoce.promocionando.entities.Correo;

/**
 *
 * @author admin
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/PromoQueue"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class SendMail implements MessageListener {

    public SendMail() {
    }

    @Override
    public void onMessage(Message message) {

        try {
            Object msgObj = ((ObjectMessage) message).getObject();
            if (msgObj != null) {
                Correo m = (Correo) msgObj;
                System.out.println();
                System.out.println("Enviando mensaje " + m.getMailDe() + " a " + m.getMailPara());
                System.out.println("From: " + m.getMailDe());
                System.out.println("Subject: " + m.getAcerca());
                System.out.println("To: " + m.getMailPara());
                System.out.println(m.getCuerpo());

                Properties props = new Properties();
                props.put("50.28.14.168", "smtp");
                props.put("mail.smtp.port", "26");
                props.put("mail.smtp.host", "mail.promocionando.com.mx");
                props.put("mail.smtp.auth", "true");
                //props.put("mail.smtp.starttls.enable", "true");

                Authenticator auth = new AutentificadorDeMail();
                Session mailSession = Session.getInstance(props, auth);
                Transport transport = mailSession.getTransport();
                MimeMessage forward = new MimeMessage(mailSession);
                forward.setFrom(new InternetAddress(m.getMailDe()));
                forward.setSubject(m.getAcerca());

                InternetHeaders headers = new InternetHeaders();
                headers.addHeader("Content-type", "text/html; charset=UTF-8");
                //mbp.setContent(m.getCuerpo(), "text/html; charset=UTF-8");
                MimeBodyPart mbp = new MimeBodyPart(headers, m.getCuerpo().getBytes("UTF-8"));

                MimeMultipart multipart = new MimeMultipart("mixed");
                multipart.addBodyPart(mbp);

                for (Attachment a : m.getAttachments()) {
                    mbp = new MimeBodyPart();
                    DataSource xs = new javax.mail.util.ByteArrayDataSource(a.getContent(), a.getMime());
                    mbp.setDataHandler(new DataHandler(xs));
                    mbp.setFileName(a.getFilename());
                    mbp.setDisposition(javax.mail.Part.ATTACHMENT);
                    multipart.addBodyPart(mbp);
                }

                forward.setContent(multipart);
                forward.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(m.getMailPara()));
                transport.connect();
                transport.sendMessage(forward, forward.getRecipients(javax.mail.Message.RecipientType.TO));
                transport.close();
            }

        } catch (JMSException jms) {
            System.out.println(jms.getMessage());
        } catch (javax.mail.NoSuchProviderException nsp) {
            System.out.println(nsp.getMessage());
        } catch (javax.mail.MessagingException me) {
            System.out.println(me.getMessage());
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SendMail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
