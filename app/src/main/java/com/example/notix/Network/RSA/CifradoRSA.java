package com.example.notix.Network.RSA;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

/**
 * <b>Criptografia Asimetrica (Clave publica) - Generador Clave Publica</b>
 * <br/>
 * <br/>
 *
 * En un <b>Cifrado asimetrico</b> hay dos participantes: el emisor y el
 * receptor. Los pasos a seguir son:
 *
 * <ul>
 * <li>Generar una <b>clave publica</b> y otra <b>privada</b>. La clave publica
 * se envia al emisor</li>
 * <li>El emisor <u>cifra</u> los datos con <b>clave publica</b> y se envian al
 * receptor</li>
 * <li>El receptor <u>descifra</u> los datos con <b>clave privada</b></li>
 * </ul>
 *
 * Esta clase genera primero cifra un mensaje con la <b>clave publica</b>. A
 * continuaci�n, lo descifra mediante la <b>clave privada</b>. En este caso
 * vamos a utilizar:
 *
 * <ul>
 * <li>El algoritmo RSA</li>
 * <li>El modo ECB: Existen dos, el ECB que es sencillo, y el CBC que necesita
 * un vector de inicializacion(IV)</li>
 * <li>El padding PKCS1Padding: Si el mensaje no es multiplo de la longitud del
 * algoritmo se indica un relleno.</li>
 * </ul>
 */
public class CifradoRSA {

    //	private static final String PUBLIC_KEY_FILE_PATH = "com/example/notix/keys/EjemploRSA_Public.key";
    private static final String PUBLIC_KEY_FILE_PATH = "C:\\Users\\in2dm3-v\\AndroidStudioProjects\\notix\\app\\src\\main\\res\\raw\\EjemploRSA_Public.key";

    /**
     * Cifra un texto con RSA, modo ECB y padding PKCS1Padding (asim�trica) y lo
     * retorna
     *
     * @param mensaje El mensaje a cifrar
     * @return El mensaje cifrado
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public byte[] cifrarTexto(String mensaje) {
        byte[] encodedMessage = null;
        try {
            // Clave publica
            //File ficheroPublica = new File(PUBLIC_KEY_FILE_PATH);
            //byte[] clavePublica = Files.readAllBytes(ficheroPublica.toPath());

            GetKey getKey = new GetKey();
            Thread thread = new Thread(getKey);
            try {
                thread.start();
                thread.join();
            } catch (InterruptedException e) {
                // Nothing to do here...
            }
            String response = getKey.getResponse();
            //String response = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCshfMcBm/6EA7QAtfTuNc+b16IW6lS82MQPfCP0HPmfwhIz0nGzuj6fsuwRi6xv/C3+WbWb4g8dT3NoxSRahzM36nSgvkQxyT9bJ2PGn8/igzQEf1P3L+5eqC8sw2CDZx7RgPA/BclXHv0kly2wmiwOcSw/gvg01pkCVqWYMNAkQIDAQAB";

            byte[] clavePublica = Base64.getDecoder().decode(response);
            System.out.println("Tamanio -> " + clavePublica.length + " bytes");


            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(clavePublica);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encodedMessage = cipher.doFinal(mensaje.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodedMessage;
    }



}