package com.tencent.wxcloudrun.util;

import com.tencent.wxcloudrun.contants.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * 加解密类
 * 
 * @author Administrator
 *
 */
@Slf4j
public class EncryptUtil {

    /**
     * 加密
     * 
     * @param sSrc
     * @return
     * @throws Exception
     */
    public static String Encrypt(String sSrc) {
        try {
            String sKey = CommonConstant.AES128_PUBLIC_KEY;
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

            return Base64.encodeBase64String(encrypted);
        } catch (Exception e) {
            log.error("",e);
        }
        return null;
    }

    /**
     * 解密
     * 
     * @param sSrc
     * @return
     * @throws Exception
     */
    public static String Decrypt(String sSrc) {
        try {
            String key = CommonConstant.AES128_PUBLIC_KEY;
            byte[] raw = key.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = Base64.decodeBase64(sSrc);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                return new String(original, StandardCharsets.UTF_8);
            } catch (Exception e) {
                log.error("",e);
                return null;
            }
        } catch (Exception ex) {
            log.error(ex.toString());
            return null;
        }
    }
}
