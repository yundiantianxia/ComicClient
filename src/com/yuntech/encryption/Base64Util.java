package com.yuntech.encryption;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Base64Util {

    // 字符串编码
    private static final String UTF_8 = "UTF-8";

    /**
     * 加密字符串
     * @param inputData
     * @return
     */
    public static String decodeData(String inputData) {
        try {
            if (null == inputData) {
                return null;
            }
            Base64.Encoder encoder = Base64.getEncoder();
            byte[] textByte = inputData.getBytes("UTF-8");
            //编码
            String encodedText = encoder.encodeToString(textByte);
            
            return encodedText;
        } catch (UnsupportedEncodingException e) {
        	throw new RuntimeException("UnsupportedEncodingException e");
        }
    }

    /**
     * 解密加密后的字符串
     * @param inputData
     * @return
     */
    public static String encodeData(String inputData) {
        try {
            if (null == inputData) {
                return null;
            }
            Base64.Decoder decoder = Base64.getDecoder();
            //编码
            String decoderText = new String(decoder.decode(inputData.toString().replace("\r\n", "")),"UTF-8");
            return decoderText;
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(Base64Util.encodeData(("我是中文").toString().replace("\r\n", "")));
        String enStr = Base64Util.encodeData(("我是中文").toString().replace("\r\n", ""));
        System.out.println(Base64Util.decodeData(enStr));
    }
}
