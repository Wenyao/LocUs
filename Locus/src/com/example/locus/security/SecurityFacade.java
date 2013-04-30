package com.example.locus.security;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;

import android.util.Base64;
import android.util.Log;


public class SecurityFacade implements ISecurity{
	private static SecurityFacade instance = null ; 
	private static KeyPairGenerator kpg  = null ; 
	private static KeyPair keyPair = null ; 
	private static Key publicKey = null ; 
	private static Key privateKey = null ; 
	private static int keyLength = 1024 ; 
	
	/* Here initialize any variables that will be necessary later */
	private SecurityFacade() { 	
		
		try {
			kpg =  KeyPairGenerator.getInstance("RSA") ;
			kpg.initialize(keyLength) ; 
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 
	}

	
	public static SecurityFacade getInstance() { 
		if (instance == null ) { 
			instance = new SecurityFacade() ; 
		}
		return instance ; 
	}
	
	private  byte[] getByteArray (Object obj) {
		byte[] bytes = null;
		ByteArrayOutputStream baos;
		ObjectOutputStream oos;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos); 
			oos.writeObject(obj);
			oos.flush(); 
			oos.close(); 
			baos.close();
			bytes = baos.toByteArray();
		}
		catch (IOException e) {
			System.out.println("error in toByteArray()");
		}
		return bytes;
	}
	
	
	private  String getSerializedString (Object obj) {
		String serializedString = null ; 
		ByteArrayOutputStream baos;
		ObjectOutputStream oos;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos); 
			oos.writeObject(obj);
			oos.flush(); 
			oos.close(); 
			baos.close();
			//serializedString = baos.toString("UTF-8"); 
			serializedString = baos.toString(); 
			
		}
		catch (IOException e) {
			System.out.println("error in toByteArray()");
		}
		return serializedString;
	}
	
	
	
	/* This will only work for public keys */
	private String[] convertKey(Key secKey) { 
		KeyFactory kFact;

		/* These are the components of the Key */
		BigInteger modulus = null ; 
		BigInteger exp = null ; 
		RSAPublicKeySpec pubKSpec = null ;
		String[] keyComponents = new String[2] ; 
		
		try {
			kFact = KeyFactory.getInstance("RSA/ECB/PKCS1Padding");
		} catch (NoSuchAlgorithmException e) {
			//e.printStackTrace();
			return null ; 
		} 
		
		try {
			 pubKSpec = kFact.getKeySpec(secKey, RSAPublicKeySpec.class);
		} catch (InvalidKeySpecException e) {
			return null ; 
			//e.printStackTrace();
		} 
		
		modulus = pubKSpec.getModulus() ; 
		exp = pubKSpec.getPublicExponent() ; 
		
		
		byte[] modulusBArray = getByteArray(modulus);
		byte[] expBArray = getByteArray(exp);

		String modString = null ; 
		String expString = null ;
		
		try {
			//modString = new String(modulusBArray,"UTF-8");
			modString = new String(modulusBArray);
		} catch (Exception e) {
			//e.printStackTrace();
			return null ; 
		}
		
		try {
			//expString = new String(expBArray,"UTF-8");
			expString = new String(expBArray);
		} catch (Exception e) {
			//e.printStackTrace();
			return null ; 
		}
		Log.d("# modulus #",modString);
		Log.d("# exp #",expString);
		keyComponents[0] = modString ; 
		keyComponents[1] = expString ; 
		
		
		/* Again trying to get the strings using different way */
		String mstring = getSerializedString(modulus);
		String estring = getSerializedString(exp);
		
		if ( modString.equals(mstring)) { 
			Log.e("modulus_match","Both are matching"); 
		} else { 
			Log.e("modulus_match","Both are not matching");
		}
		
		if ( modString.equals(mstring)) { 
			Log.e("exp_match","Both are matching"); 
		} else { 
			Log.e("exp_match","Both are not matching");
		}
		keyComponents[0] = mstring ; 
		keyComponents[1] = estring ; 
		
		return keyComponents ;
	}
	
	
	private PublicKey buildKey(String[] keyComps) { 
		if ( keyComps == null )
			return null ; 
			
		byte[] modBArray = null ; 
		byte[] expBArray = null ;

		try {
			//modBArray = keyComps[0].getBytes("UTF-8") ;
			modBArray = keyComps[0].getBytes() ;
		} catch (Exception e) {
			Log.e("DEBUG","error in modBARray");
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return null ; 
		}  
		
		try {
			//expBArray = keyComps[1].getBytes("UTF-8");
			expBArray = keyComps[1].getBytes();
		} catch (Exception e) {
			Log.e("DEBUG","expBArray");
			//e.printStackTrace();
			return null ; 
		} 
		ObjectInputStream mois;
		ObjectInputStream eois ; 
		
		PublicKey pubKey = null ; 
		
		try {
		
		ByteArrayInputStream bais = null ; 
		bais = new ByteArrayInputStream(modBArray) ; 
		//mois = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(modBArray)));
		mois = new ObjectInputStream(bais);
		
		eois = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(expBArray)));
		BigInteger m = (BigInteger)mois.readObject() ; 
		BigInteger e = (BigInteger)eois.readObject() ; 
		RSAPublicKeySpec pubKSpec = new RSAPublicKeySpec(m, e); 
		KeyFactory kFactory = KeyFactory.getInstance("RSA") ; 
		pubKey = kFactory.generatePublic(pubKSpec); 
		mois.close() ; 
		eois.close() ;
		} catch (Exception e ) { 
			e.printStackTrace(); 
			return null ; 
		} 
		return pubKey ; 
	}
	

	/* This function should return the public key preferably in the String format 
	 * to store in the User object. Here this will return a string array that contains the 
	 * modulus and exponent of the key. i can't concatenate these and return the concatenated because 
	 * these strings formed from the byte array can contain any character and hence none of the characters 
	 * can be used as seperation delimeters  
	 */

	public String generate_keypair() { 
		if ( kpg == null ) 
			return null ; 
		
		keyPair = kpg.generateKeyPair() ; 
		if ( keyPair == null) 
			return null ; 
		
		publicKey = keyPair.getPublic() ; 
		privateKey = keyPair.getPrivate() ; 
		
		if ( (publicKey == null) || (privateKey == null)) { 
			return null ; 
		}
		return Base64.encodeToString(publicKey.getEncoded(),0); 
	}
	
	
	
	public String encrypt_data(String data, String pKey) { 
		
		/* First generate the Public Key object from the public key in string format */
		byte[] publicKeyBytes = Base64.decode(pKey,0); 
		PublicKey pubKey = null ; 
		KeyFactory keyFactory = null;
		Cipher cipher = null;
		
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null ; 
		}
		
		EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
	    
		try {
			 pubKey = keyFactory.generatePublic(publicKeySpec);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			return null ; 
		}
	  
		/* Here got the public key .. Now use it to encrypt the given data */
		
		try {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null ; 
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null ; 
		}
	
		try {
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			return null ; 
		}
		InputStream fis = null;
	 	try {
				fis = new ByteArrayInputStream(data.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
		}
	 	ByteArrayOutputStream fos = null ; 
	 	fos = new ByteArrayOutputStream();
	 	CipherOutputStream cos = new CipherOutputStream(fos, cipher);

	 	  byte[] block = new byte[32];
	 	  int i;
	 	  try {
				while ((i = fis.read(block)) != -1) {
				      try {
						cos.write(block, 0, i);
					} catch (IOException e) {
						e.printStackTrace();
					}
				  }
			} catch (IOException e) {
				e.printStackTrace();
			}
	 	  try {
				cos.close();
			} catch (IOException e) {
				e.printStackTrace();
				return null ; 
			}
	 	  
	 	  /* Get the encoded data in a byte array */
	 	  byte[] encBArray = fos.toByteArray() ; 
	 	  
	 	 String encrypted_data = null;
	 	 /* converting the encrypted data into string using Base64 encoding since 
	 	  * we will be transferring this data over the network. 
	 	  */
	 	 encrypted_data = Base64.encodeToString(encBArray, 0); 
	 	 Log.e("Encrypted Message",encrypted_data);
	 	/* WORKING  CODE for the ISO-8859-1 encoding .. This encoding works 
	 	 * but other encodings like UTF-8 etc fails here 
	 	try {
	 		encrypted_data = new String(encBArray,"ISO-8859-1");
	 	} catch (UnsupportedEncodingException e2) {
	 		// TODO Auto-generated catch block
	 		e2.printStackTrace();
	 	} 
	  	  
	  	Log.e("Encrypted Message",encrypted_data);
	  	  
	  	  
	  	 byte[] encodedBArray  = null ; 
	  	  
	  	  try {
	 		encodedBArray = encrypted_data.getBytes("ISO-8859-1");
	 	} catch (UnsupportedEncodingException e2) {
	 		// TODO Auto-generated catch block
	 		e2.printStackTrace();
	 	} 
	  	  */
		 return encrypted_data ; 
	}
	
	
	public String decrypt_data(String enc_data) { 
		Cipher cipher = null;
		InputStream fis = null;
		byte[] encodedBArray  = null ; 
		ByteArrayOutputStream fos = null ; 
		byte[] block = new byte[32];
	 	int i;
		
		encodedBArray = Base64.decode(enc_data, 0);
		
		try {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		
		try {
				cipher.init(Cipher.DECRYPT_MODE, privateKey);
		} catch (InvalidKeyException e1) {
				e1.printStackTrace();
		}
		
		fis = new ByteArrayInputStream(encodedBArray)  ;
		CipherInputStream cis = new CipherInputStream(fis, cipher);
		try {
			 fos = new ByteArrayOutputStream();
		 } catch (Exception e) {
					e.printStackTrace();
					return null ; 
		 }

		 try {
					while ((i = cis.read(block)) != -1) {	
					      fos.write(block, 0, i);
					}
		 } catch (IOException e) {
					e.printStackTrace();
		}
		try {
					fos.close();
		} catch (IOException e) {
					e.printStackTrace();
					return null ; 
		}
		 	  
		String decrypted_text = fos.toString() ;
		/* Now get the decrypted byte array and try to print the string */
	    System.out.println("The decrypted text is : " + decrypted_text );
		
		return decrypted_text ; 
	}
	
	
	/*
	public String[] generate_keypair() {
		if ( kpg == null ) 
			return null ; 
		
		keyPair = kpg.generateKeyPair() ; 
		if ( keyPair == null) 
			return null ; 
		
		publicKey = keyPair.getPublic() ; 
		privateKey = keyPair.getPrivate() ; 
		
		if ( (publicKey == null) || (privateKey == null)) { 
			return null ; 
		}
		
		return convertKey(publicKey); 	  
	}
	*/
	
	
	/* Given a pub key and the data this function will return the encrypted version of the data */
	/*
	public String encrypt_data(String data, String[] pubKeyComps) {
		PublicKey pKey = buildKey(pubKeyComps); 
		if ( pKey == null ) { 
			Log.e("recreated pKey","Error occured");
		}
		
		Cipher cipher = null ; 
		byte[] encryptedBytes = null ; 
		String encString = null ; 
		try {
		     cipher = Cipher.getInstance("RSA") ; 		
		     cipher.init(Cipher.ENCRYPT_MODE,pKey);
		     //encryptedBytes = cipher.doFinal(data.getBytes("UTF-8")); 
		     //encString = new String(encryptedBytes,"UTF-8") ;
		     encryptedBytes = cipher.doFinal(data.getBytes()); 
		     encString = new String(encryptedBytes) ;
		} catch (Exception e) {
			return null ; 
		} 
		return encString; 
	}
*/
	
	
}
