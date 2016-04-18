/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.promocionando.authenticators;

import javax.mail.PasswordAuthentication;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alfonso
 */
    public class AutentificadorDeMail extends javax.mail.Authenticator {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
           String username = "noreply@promocionando.com.mx";
           String password = "Kabongo75#";
           return new PasswordAuthentication(username,password);
        }
    }
