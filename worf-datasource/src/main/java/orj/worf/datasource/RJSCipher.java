package orj.worf.datasource;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * 加密算法。
 * @author zhuangpuxiang
 */
public class RJSCipher {
	
	public static String DESEDE = "DESede";
	private static String KEY = "abcdefgh1234567887654321";
	
	/**
	 * 加密  非 URLEncoder.encode编码
	 * @param input 加密数据
	 * @param key 秘钥
	 * @return
	 */
	public static String encrypt(String input, String key){
		byte[] crypted = null;
		try{
			SecretKeySpec skey = new SecretKeySpec(key.getBytes(), DESEDE);
			Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skey);
			crypted = cipher.doFinal(input.getBytes());
		}catch(Exception e){
			System.out.println(e.toString());
		}
		
		return new String(Base64.encodeBase64(crypted));
	}
	
	
	/**
	 * 解密
	 * @param input 解密数据
	 * @param key 秘钥
	 * @return
	 */
	public static String decrypt(String key,String input){
		byte[] output = null;
		try{
			SecretKeySpec skey = new SecretKeySpec(key.getBytes(), DESEDE);
			Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skey);
			output = cipher.doFinal(Base64.decodeBase64(input));
		}catch(Exception e){
			System.out.println(e.toString());
		}
		return new String(output);
	}
	
	
	/**
	 * 加密  UTF-8编码处理
	 * @param input 加密数据
	 * @param key 秘钥
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String encryptData(String input) throws UnsupportedEncodingException{
		byte[] crypted = null;
		try{
			SecretKeySpec skey = new SecretKeySpec(KEY.getBytes(), DESEDE);
			Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skey);
			crypted = cipher.doFinal(input.getBytes());
		}catch(Exception e){
			System.out.println(e.toString());
		}
		String result = new String(Base64.encodeBase64(crypted));
		result = URLEncoder.encode(result, "UTF-8");
		return result;
	}
	
	
	/**
	 * 数据解密 URLDecoder 加密的时候有带它的的，所以这里加入这个解密的过程。
	 * @param key 秘钥
	 * @param input 待解密数据
	 * @return 解密后的结果
	 * @throws UnsupportedEncodingException
	 */
	public static String decryptData(String input) throws UnsupportedEncodingException{
		return RJSCipher.decrypt(KEY,URLDecoder.decode(input,"UTF-8"));
	}
	
	
	
}
