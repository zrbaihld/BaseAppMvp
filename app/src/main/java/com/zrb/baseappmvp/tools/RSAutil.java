package com.zrb.baseappmvp.tools;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import javax.crypto.Cipher;

/**
 * Created by zrb on 2017/6/12.
 */

public class RSAutil {
    private static final String RSA_PUBLICE =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDFxa2eIAaSXJN76typaSaoaI" +
                    "v0ZCr7J+ok0ps+13wtkHyIXqcvzfD23mjKY2p/bD1fGWncWgGdEDF7beyiO9EP91RtyToKWWPud" +
                    "/83/A4VoX4gAweXsufSKqqDJSzdlxGtb6+OY9nFlHzu5rbu5Yt84sSmwBg7W91FjrEMOOc2twIDAQAB";
    private static final String ALGORITHM = "RSA";

    /**
     * 得到公钥
     *
     * @param algorithm
     * @param bysKey
     * @return
     */
    private static PublicKey getPublicKeyFromX509(String algorithm,
                                                  String bysKey) throws NoSuchAlgorithmException, Exception {
        byte[] decodedKey = Base64.decode(bysKey, Base64.DEFAULT);
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(x509);
    }

    /**
     * 使用公钥加密
     *
     * @param RSA_PUBLICE 公钥字符串
     * @return 加密后的字符串
     */
    public static String encryptByPublic() {
//        RSA_PUBLICE = RSA_PUBLICE.replace("-----BEGIN PUBLIC KEY-----", "");
//        RSA_PUBLICE = RSA_PUBLICE.replace("-----END PUBLIC KEY-----", "");

        Date date = new Date();
        long time = date.getTime() / 1000;
        String plainText = "899186f7879ef9f1cf011b415f548c03" + time;
//        String plainText = "BE3DDC9C754C247B82FF4EE031A655D8" ;
        String s = "";
        PublicKey pubkey;
        try {
            pubkey = getPublicKeyFromX509(ALGORITHM, RSA_PUBLICE);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);

            byte plaintext[] = plainText.getBytes("UTF-8");
            byte[] output = cipher.doFinal(plaintext);

            s = new String(Base64.encode(output, Base64.NO_WRAP));

            byte[] decode = Base64.decode(s, Base64.DEFAULT);


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return s;
    }

    /**
     * 使用公钥解密
     *
     * @param content 密文
     * @param key     商户私钥
     * @return 解密后的字符串
     */
    public static String decryptByPublic(String content) {
        try {
            PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, RSA_PUBLICE);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, pubkey);
            InputStream ins = new ByteArrayInputStream(Base64.decode(content, Base64.DEFAULT));
            ByteArrayOutputStream writer = new ByteArrayOutputStream();
            byte[] buf = new byte[128];
            int bufl;
            while ((bufl = ins.read(buf)) != -1) {
                byte[] block = null;
                if (buf.length == bufl) {
                    block = buf;
                } else {
                    block = new byte[bufl];
                    for (int i = 0; i < bufl; i++) {
                        block[i] = buf[i];
                    }
                }
                writer.write(cipher.doFinal(block));
            }
            return new String(writer.toByteArray(), "utf-8");
        } catch (Exception e) {
            return null;
        }
    }

}
