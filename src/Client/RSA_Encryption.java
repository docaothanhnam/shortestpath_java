/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

/**
 *
 * @author HIAI
 */
public class RSA_Encryption {
    
    public static void main(String[] args)
    {
//        String data = "xin chào";
//        Encrpytion(data);
//        System.out.println(Decrpytion(Encrpytion(data)));
        
    }
    
    public String Encrpytion(String data)
    {
        try {
                // Đọc file chứa public key
                FileInputStream fis = new FileInputStream("keyRSA\\publicKey.rsa");
                byte[] b = new byte[fis.available()];
                fis.read(b);
                fis.close();

                // Tạo public key
                X509EncodedKeySpec spec = new X509EncodedKeySpec(b);
                KeyFactory factory = KeyFactory.getInstance("RSA");
                PublicKey pubKey = factory.generatePublic(spec);

                // Mã hoá dữ liệu
                Cipher c = Cipher.getInstance("RSA");
                c.init(Cipher.ENCRYPT_MODE, pubKey);
                String msg = data;      // data: dữ liệu cần mã hóa
                byte encryptOut[] = c.doFinal(msg.getBytes());
                String strEncrypt = Base64.getEncoder().encodeToString(encryptOut);
                
                return strEncrypt;
//                System.out.println("Chuỗi sau khi mã hoá: " + strEncrypt);

        } catch (Exception ex) {
                ex.printStackTrace();
        }
        return null;
    }
    public String Decrpytion(String data)
    {
        try {
                // Đọc file chứa private key
                FileInputStream fis = new FileInputStream("keyRSA\\privateKey.rsa");
                byte[] b = new byte[fis.available()];
                fis.read(b);
                fis.close();

                // Tạo private key
                PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b);
                KeyFactory factory = KeyFactory.getInstance("RSA");
                PrivateKey priKey = factory.generatePrivate(spec);

                // Giải mã dữ liệu
                Cipher c = Cipher.getInstance("RSA");
                c.init(Cipher.DECRYPT_MODE, priKey);
                byte decryptOut[] = c.doFinal(Base64.getDecoder().decode(data));    // data Dữ liệu cần giải mã
//                System.out.println("Dữ liệu sau khi giải mã: " + new String(decryptOut));
                String abc = new String(decryptOut);
                
                return abc;
                
        } catch (Exception ex) {
                ex.printStackTrace();
        }
        return null;
    }
    
}
