package com.hospital.utils;


import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import com.hospital.entry.RSAKey;
import org.apache.tomcat.util.codec.binary.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class RSAUtils {
    public static final BASE64Encoder ENCODER = new BASE64Encoder();
    public static final BASE64Decoder DECODER = new BASE64Decoder();
    /** */
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /** */
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;
    
    public static RSAKey genKeyPair() throws Exception {
    	 KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
         // 密钥位数
         keyPairGen.initialize(1024);
         // 密钥对
         KeyPair keyPair = keyPairGen.generateKeyPair();
         // 公钥
         PublicKey publicKey = keyPair.getPublic();
         // 私钥
         PrivateKey privateKey = keyPair.getPrivate();
         //得到公钥字符串
         String publicKeyString = ENCODER.encode(publicKey.getEncoded());
         //得到私钥字符串
         String privateKeyString = ENCODER.encode(privateKey.getEncoded());
         
         return new RSAKey(publicKeyString, privateKeyString) ;
    }

    /**
     * 生成密钥对(公钥和私钥)
     *
     * @return
     * @throws Exception
     */
    public static void genKeyPair(String dir) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 密钥位数
        keyPairGen.initialize(1024);
        // 密钥对
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 公钥
        PublicKey publicKey = keyPair.getPublic();
        // 私钥
        PrivateKey privateKey = keyPair.getPrivate();
        //得到公钥字符串
        String publicKeyString = ENCODER.encode(publicKey.getEncoded());
        //得到私钥字符串
        String privateKeyString = ENCODER.encode(privateKey.getEncoded());
        //将密钥对写入到文件
        FileWriter pubfw = new FileWriter(dir + ".pubkey");
        FileWriter prifw = new FileWriter(dir + ".prikey");
        BufferedWriter pubbw = new BufferedWriter(pubfw);
        BufferedWriter pribw = new BufferedWriter(prifw);
        pubbw.write(publicKeyString);
        pribw.write(privateKeyString);
        pubbw.flush();
        pubbw.close();
        pubfw.close();
        pribw.flush();
        pribw.close();
        prifw.close();
        System.out.println("公钥生成成功!公钥文件为：" + dir + ".pubkey");
        System.out.println("私钥生成成功!私钥文件为：" + dir + ".prikey");
    }

    /**
     * 加载公钥
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static PublicKey loadPublicKey(String filePath) throws Exception {
        FileReader fr = new FileReader(filePath);
        BufferedReader br = new BufferedReader(fr);
        StringBuilder keyString = new StringBuilder();
        String str;
        while ((str = br.readLine()) != null) {
            keyString.append(str);
        }
        br.close();
        fr.close();
        return getPublicKey(keyString.toString());
    }

    /**
     * 加载私钥
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static PrivateKey loadPrivateKey(String filePath) throws Exception {
        FileReader fr = new FileReader(filePath);
        BufferedReader br = new BufferedReader(fr);
        StringBuilder keyString = new StringBuilder();
        String str;
        while ((str = br.readLine()) != null) {
            keyString.append(str);
        }
        br.close();
        fr.close();
        return getPrivateKey(keyString.toString());
    }

    /**
     * 获取公钥
     *
     * @param publicKeyString
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String publicKeyString) throws Exception {
        byte[] keyBytes = DECODER.decodeBuffer(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 获取私钥
     *
     * @param privateKeyString
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String privateKeyString) throws Exception {
        byte[] keyBytes = DECODER.decodeBuffer(privateKeyString);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }
    
    

    /**
     * 加密
     */
    public static byte[] encrypt(Key key, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        out.close();
        return out.toByteArray();
    }
    
   

    /**
     * 解密
     */
    public static byte[] decrypt(Key key, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int inputLen = data.length;
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        out.close();
        return out.toByteArray();
    }
    
    
    public static String encryptStringByPublicKey(String  publicKeyString,String data) throws Exception {
    	PublicKey key = getPublicKey(publicKeyString);
    	byte[] b = encrypt(key, data.getBytes());
    	return ENCODER.encode(b);    	
    }
    
    public static String decryptStringByPrivateKey(String privateKeyString,String data) throws Exception {
    	PrivateKey key = getPrivateKey(privateKeyString);
    	byte[] b1 = DECODER.decodeBuffer(data);
    	byte[] b = decrypt(key, b1);
    	return new String(b);
    }
    
    public static String encryptStringByPrivateKey(String  privateKeyString ,String data) throws Exception {
    	PrivateKey key = getPrivateKey(privateKeyString);
    	byte[] b = encrypt(key, data.getBytes());
    	return ENCODER.encode(b);    	
    }
    
    public static String decryptStringByPublicKey(String publicKeyString,String data) throws Exception {
    	PublicKey key = getPublicKey(publicKeyString);
    	byte[] b1 = DECODER.decodeBuffer(data);
    	byte[] b = decrypt(key, b1);
    	return new String(b);
    }

    /*
     *私钥对原始数据进行签名
     */
    private static byte[] signature(PrivateKey privateKey , byte[] data) throws Exception {
//        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
//        KeyFactory factory = KeyFactory.getInstance(keyType);
//        PrivateKey privatekey = (PrivateKey) factory.generatePrivate(keySpec);

        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }
    /*
     * 公钥、原始数据、原始签名数据进行验证
     */
    private static boolean verify(PublicKey publicKey, byte[] data, byte[] sign) throws Exception {
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(sign);
    }

    public static String sign(String privateKey,String content) throws Exception {
        PrivateKey pk=getPrivateKey(privateKey) ;
        byte[] contentBytes = content.getBytes("utf-8");
        byte[] signs = signature(pk,contentBytes);
        //return Base64.encodeBase64String(signs);
        return ENCODER.encode(signs);


    }

    public static boolean verify(String publicKey,String data, String sign) throws Exception,UnsupportedEncodingException {
        PublicKey pk=getPublicKey(publicKey) ;
        byte[] contentBytes = data.getBytes("utf-8");
        byte[] signBytes = DECODER.decodeBuffer(sign);
        System.out.println(contentBytes.toString());
        return verify(pk,contentBytes,signBytes);
    }

    //用md5生成内容摘要，再用RSA的私钥加密，进而生成数字签名
    private static String getMd5Sign(String content , PrivateKey privateKey) throws Exception {
        byte[] contentBytes = content.getBytes("utf-8");
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(privateKey);
        signature.update(contentBytes);
        byte[] signs = signature.sign();
        return Base64.encodeBase64String(signs);
    }

    //对用md5和RSA私钥生成的数字签名进行验证
    private static boolean verifyWhenMd5Sign(String content, String sign, PublicKey publicKey) throws Exception {
        byte[] contentBytes = content.getBytes("utf-8");
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(publicKey);
        signature.update(contentBytes);
        return signature.verify(Base64.decodeBase64(sign));
    }

    public static void main(String[] args) throws Exception{
        String publicKeyStringG = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCzQMqRnHBjbwTW4Yr7cKDTrAyVd+zA2YMujB9x\n" +
                "Rx9wNjvzyII9kH2CgAH4ZsTdZ+ym/uyh5SpM/dV0OayoT5aOEJOs7k4dKCWcRDzEC0rDNXkOjbMT\n" +
                "H76SzsfjwyV1mEQOdS+ISEmo9AjNpWbas42Ehs8On3EFY6QsvsyTY6ySJwIDAQAB";

        String privateKeyStringG = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALNAypGccGNvBNbhivtwoNOsDJV3\n" +
                "7MDZgy6MH3FHH3A2O/PIgj2QfYKAAfhmxN1n7Kb+7KHlKkz91XQ5rKhPlo4Qk6zuTh0oJZxEPMQL\n" +
                "SsM1eQ6NsxMfvpLOx+PDJXWYRA51L4hISaj0CM2lZtqzjYSGzw6fcQVjpCy+zJNjrJInAgMBAAEC\n" +
                "gYAHggo6CRxn/cltGqtQlsoVs5ofVJOVDEdDVtkmTH1TnOHr9xUFcRzgr23lsqBCSc0OvZS5vziD\n" +
                "WwpqwNOrhRHgNVobDgV7Vw8hJra4E3BUi1IsiqbDfpdOMpGHj219WORJEfqCllEShIlmRrbQGGbd\n" +
                "X57xci5462lcCe6hIgTMiQJBAPKM4sC8eN/qfXG2m5+Rm+9Ob9VeStIGJ6TY65DT+I4fb1UFzfPq\n" +
                "GRxH9NPoiBZql3gVVX28R5vl6m1A24pZ+MsCQQC9MV8yRnConlR2NVIX7/3+AlgjXaDqIwj452dG\n" +
                "Fbq+E6bXdX7wVih0EMni1OLeJIhKedVgwfmSL4dN2Kboh8yVAkAWns0G7IzZVSUd1cntt8azKr2D\n" +
                "SKfxrmFEHnbZqusjVgssAr6SYOK2oH9Uw/rtHEPEDzlJ4KVNBMQ4LDRNUtbBAkALqyHd7e5A2BMJ\n" +
                "f1xi0ZBuvCJHfBzSk/qXr131Op3b30Zo/3wmwomYLvjknpCndsVZIo6AezYzR+8mY/hXWxjJAkEA\n" +
                "4bjkbFAUFFR4MIMVQ2lJ6Pycb7Zu4b/CJJwA/n8MCAeECwZQLyQ6pCEwYEjCdjOiUWk/Z6PpdT6i\n" +
                "oVC83OH9vw==";

        /*String publicKeyStringG ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCUAV07JUfteXS0S4pRWwnDwAkj\n" +
                "KA6HnEnyBUETO/TWwIGRuJDMTKjZ2/600OxMBmHNtgkeDO0TQfArD+bMk0eOQQDq\n" +
                "CU8dCEX2METqRifgwi2vJALJ+W9Ja7l5i1rpBn82KT3wjat+DuCwtCED1WWyZhYZ\n" +
                "Q9AwfeQX941rAlOzaQIDAQAB";
        String privateKeyStringG="MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANz9+t8wuAJQvpmW\n" +
                "Fru8nGhdJcxzUBwxCR7V1XMC4p2Z9U3BA8C5fui0pZJOmlpYqS/rqS1XxkUxp/7f\n" +
                "lWBiPRrd6tjQ+8LCp6d9Ef7doxB4L4LGI9grqLww1PHs0Nw6rpwW2V5SSFM2I0Gd\n" +
                "sx3p8s+/EbNbYf4newcHpnGcEvizAgMBAAECgYBpZHr7bEmlWQq9RpYK7LWPFDVN\n" +
                "nwTqnPwjh4lnc03OKbmSXmXCyPRcWKLJ9tCoG7Z8UyB4rm6xWzAqXkjoEZHf1kro\n" +
                "TlBMgEhAwNvxxU2ehoFY+qBrL3Fq8fmAfIK3gMwhJ7J0GSMLmPJA6c3HQiRieplQ\n" +
                "AKouhzdwDKb6f5aJkQJBAPYqjTM3l0pHvkuVQJTnasE86imZ5RpJXOIaXxU8L79M\n" +
                "kkY/5p9E3MRBThrYRy63U25DaK/P4w6bi3w3balihpkCQQDl0ft8JZEajWS9oehR\n" +
                "E3xVkc+ctz7bgXFAB/KARkfShtbpP5v0Ydq3IMAZBGwjbdY9yevOSh/ee1lntr10\n" +
                "TGUrAkEA40PrsmJbabeMp0k3I4H3XEn5wciT7E3Y0+IW8IrtZgaycOrj5xzzWLkV\n" +
                "+9C6/xU/kGfpiY87CCxwfZDKzHvq2QJBAMrZmvYrWnZrUv4miJ33awlEWFHlVG//\n" +
                "QuzXlu4uGnryVYMviAIe1SRKngP26VQkQLPtJFvDBXokufdXTsX2IF0CQQDJAQKH\n" +
                "iLaIVJDrmH12JJR1/zveSdg4laRS/HhsVcif9PVZpR6a075sECGkiA4nYH5QrNvT\n" +
                "Ezf7NdE4q2R2p+hT";*/

        //String s = "987653231231456ad中国人1111你好啊！@￥~~！！@##￥%…………&&^^^^／／&&**())))_+_+";
        String s = "eyJtZXRob2QiOiIxMTExMSIsImludXB0IjpbeyJuYW1lIjoiMSJ9LHsibmFtZSI6IjIiLCJzZXgiOiIzIn0seyJhZ2UiOjE4fV0sIm91dHB1dCI6eyJmaXJzdCI6ImEiLCJzZWNvbmQiOiJiIiwidGhyaWQiOlsxLDIsMyw0LDUsNl0sImZvdXIiOjF9LCJyZXQiOiJzdWNjZXNzIn0=";
        //s = s +s +s +s +s +s ;
        //String s = "11111111";
        System.out.println(ENCODER.encode(s.getBytes("utf-8")));
        System.out.println(Base64.encodeBase64String(s.getBytes("utf-8")));

        //String jm1 = encryptStringByPublicKey(publicKeyStringG,s);
        //System.out.println("加密:"+jm1);
        /*String jm1="r9g88MgH0CXIqCXc4OX+kVTnWRaqLOACju/b4JkKXgynYzLmho5RAeTuo50I3v6NOzzWHgdCCNjWigk907uzgrHo+qiNBTUePV6txD2Cv0t9QZu+7uVrd16SXJ3zznFEQyl6uh7tO8DEn/SQUd9mrkgosCUGq7gpli09rs7PvsptK4QMg1YarZ2bDfjqPtDTCQ2kB0nYxeNDHvsPasyyHgmKunJIwE6qqD/PYDfDS/gGpjLo7i2hW0PLZi3iJT1jXBp+1AE65UCtDwoYVZN/umHWRlucniLLkQ99IAJdCFJJLDz464H4h2cta5csm8kbIVr+ORk5NVvblwwRGl2zAASYRGjMNXM4+ATsDKBAHgjltK6yD4k+ZZx1qZZFZVq5/SA9HIA9vLntlQUZ+PhrbMcfqsrmBBNQFnlEZBhslc7D7EOk/QBZjJB+nt31/MNWj/4im8/3gHOjxfQcbN7LPLbCJmq8cvO7mTdSjUp2DT6oRjjdjXnSXMkPnxjR+9pXAKbz+9aCSzZnD+5J2/yOXxUADRqtbPBJSaMVFGNXWWQEGIqDMJEYf+y40PD5ZohecJOtQ+hEb9TsVbaWKWen7Cbrwb6qihHZqslrZkWP62s82xEc3qzpyl3R2NUfUgN3d4g+4UizsQEu7Hoi8OYjlGrWmuKeH7DDB1DZAUmdqv8=";
        String jm2 = decryptStringByPrivateKey(privateKeyStringG,jm1);
        System.out.println("解密:"+jm2);*/
        String signData = sign(privateKeyStringG,s);
        System.out.println("签名\n"+signData);
        System.out.println(signData.length());
        Boolean b = verify(publicKeyStringG,s,signData);
        System.out.println("签名结果"+b);
        /*PrivateKey privateKey = getPrivateKey(privateKeyStringG);
        PublicKey publicKey = getPublicKey(publicKeyStringG);
        String signData2 = getMd5Sign(s,privateKey);
        System.out.println("签名2\t" + signData2);

        Boolean b = verifyWhenMd5Sign(s,signData2,publicKey);
        System.out.println(b);*/
    }
}

