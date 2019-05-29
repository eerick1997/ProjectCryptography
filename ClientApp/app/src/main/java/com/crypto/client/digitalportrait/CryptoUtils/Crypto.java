package com.crypto.client.digitalportrait.CryptoUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.crypto.client.digitalportrait.Utilities.Preferences;

import org.apache.commons.codec.binary.Base64;
import org.spongycastle.crypto.CipherParameters;
import org.spongycastle.crypto.engines.AESEngine;
import org.spongycastle.crypto.modes.CBCBlockCipher;
import org.spongycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
//import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import static javax.crypto.Cipher.DECRYPT_MODE;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
//import org.apache.commons.codec.binary.Base64;
import static javax.crypto.Cipher.ENCRYPT_MODE;

import org.spongycastle.util.encoders.Base64;



public class Crypto {

    private Context context;
    private static final String TAG = "Crypto";
    private static final String ALGORITHM = "RSA";
    private static final String ALGORITHM_SIGN = "SHA256withRSA";
    private static final String PROVIDER = "SC";
    private static final int KEY_SIZE = 1024;
    private byte[] publicKey;
    private byte[] privateKey;
    private byte[] signature;
    private Preferences preferences;

    public Crypto(Context context) {
        this.context = context;
        this.preferences = new Preferences(this.context);
    }

    public byte[] cipherData(PaddedBufferedBlockCipher cipher, byte[] data) throws Exception {
        byte[] outputBuffer = new byte[cipher.getOutputSize(data.length)];

        int length1 = cipher.processBytes(data, 0, data.length, outputBuffer, 0);
        int length2 = cipher.doFinal(outputBuffer, length1);

        byte[] result = new byte[length1 + length2];

        System.arraycopy(outputBuffer, 0, result, 0, result.length);

        return result;
    }

    public byte[] encrypt(byte[] plain, CipherParameters ivAndKey) throws Exception {
        PaddedBufferedBlockCipher aes = new PaddedBufferedBlockCipher(
                new CBCBlockCipher(
                        new AESEngine()
                )
        );

        aes.init(true, ivAndKey);

        return cipherData(aes, plain);

    }

    public byte[] decrypt(byte[] cipher, CipherParameters ivAndKey) throws Exception {
        PaddedBufferedBlockCipher aes = new PaddedBufferedBlockCipher(
                new CBCBlockCipher(
                        new AESEngine()
                )
        );
        aes.init(false, ivAndKey);

        return cipherData(aes, cipher);
    }
    public void RSAkeysGenerator() throws Exception {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER);
        keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
        KeyPair pair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();
        this.publicKey = Base64.encodeBase64(publicKey.getEncoded());
        this.privateKey = Base64.encodeBase64(privateKey.getEncoded());

        preferences.store("publicKey", this.publicKey);
        preferences.store("privateKey", this.privateKey);

        Log.i("Crypto F2:", "signImage: " + this.publicKey);
        sendKeytoBD();

    }
    public void signGenerator(Bitmap bitmap) throws Exception {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
       /* KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER);
        keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
        KeyPair pair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();*/
        Signature RSA = Signature.getInstance(ALGORITHM_SIGN, PROVIDER);
        RSA.initSign(this.privateKey);

        BufferedInputStream bufferedInputStream = fromBitmapToBIS(bitmap);

        byte[] buffer = new byte[KEY_SIZE];
        int length;
        while (bufferedInputStream.available() != 0) {
            length = bufferedInputStream.read(buffer);
            RSA.update(buffer, 0, length);
        }

        bufferedInputStream.close();
        this.signature = Base64.encodeBase64(RSA.sign());
       /* this.publicKey = Base64.encodeBase64(publicKey.getEncoded());
        this.privateKey = Base64.encodeBase64(privateKey.getEncoded());*/
        preferences.store("signature", this.signature);
        /*preferences.store("publicKey", this.publicKey);
        preferences.store("privateKey", this.privateKey);*/
        //Signature
        Log.i("Crypto F1:", "signImage: " + this.signature);
        //Public Key
        //Log.i("Crypto F2:", "signImage: " + this.publicKey);
        //It's necessary store F1 and F2


    }
    private void sendKeytoBD() throws Exception {//agrega el documento publickey a la coleccion del cliente1
        final Crypto crypto = new Crypto(this.context);
        final String TAG = "Crypto";

        Map<String, Object> pubkey = new HashMap<>();
        pubkey.put(PUBLICKEY, new String(Base64.encode(this.publicKey)));
        Log.i("PUBLICKEY", new String(this.publicKey));

        FirebaseFirestore DB = FirebaseFirestore.getInstance();
        DB.collection(CLIENTE1).add(pubkey)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        Toast.makeText(getApplicationContext(), getString(R.string.order_sent_successfully), Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(getApplicationContext(), getString(R.string.order_not_sent_successfully), Toast.LENGTH_LONG).show();
                    }
                });
        finish();

    }

    public BufferedInputStream fromBitmapToBIS(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        InputStream is = new ByteArrayInputStream(stream.toByteArray());
        return new BufferedInputStream(is);
    }


    public boolean verifySign(Bitmap bitmap, byte[] publicKeyClient, byte[] signatureClient) throws Exception {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyClient));
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, PROVIDER);
        //Getting public key
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        //Verify signature
        Signature signature = Signature.getInstance(ALGORITHM_SIGN, PROVIDER);
        signature.initVerify(publicKey);

        BufferedInputStream bufferedInputStream = fromBitmapToBIS(bitmap);
        byte[] buffer = new byte[KEY_SIZE];
        int length;
        while (bufferedInputStream.available() != 0) {
            length = bufferedInputStream.read(buffer);
            signature.update(buffer, 0, length);
        }
        bufferedInputStream.close();
        return (signature.verify(Base64.decodeBase64(signatureClient)));
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public byte[] getSignature() {
        return signature;
    }
    public String RSAEncrypt(PublicKey key,String data) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException{

        Cipher ci = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        ci.init(Cipher.ENCRYPT_MODE, key);
        byte[] bytes = Base64.decode(data);
        int inputLen = bytes.length;
        int offLen = 0;
        int i = 0;
        ByteArrayOutputStream bops = new ByteArrayOutputStream();
        while(inputLen - offLen > 0){
            byte [] cache;
            if(inputLen - offLen > 117){
                cache = ci.doFinal(bytes, offLen,117);
            }else{
                cache = ci.doFinal(bytes, offLen,inputLen - offLen);
            }
            bops.write(cache);
            i++;
            offLen = 117 * i;
        }
        bops.close();
        byte[] encryptedData = bops.toByteArray();
        String encodeToString = Base64.toBase64String(encryptedData);
        return encodeToString;

    }
    public byte[] RSADecrypt(PrivateKey key,String data) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, IOException, Exception{

        Cipher ci = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        ci.init(Cipher.DECRYPT_MODE,key);

        byte[] bytes = Base64.decode(data);
        int inputLen = bytes.length;
        int offLen = 0;
        int i = 0;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while(inputLen - offLen > 0){
            byte[] cache;
            if(inputLen - offLen > 128){
                cache = ci.doFinal(bytes,offLen,128);
            }else{
                cache = ci.doFinal(bytes,offLen,inputLen - offLen);
            }
            byteArrayOutputStream.write(cache);
            i++;
            offLen = 128 * i;

        }
        byteArrayOutputStream.close();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return byteArray;
    }
    public String RSAEncrypt(PrivateKey key,String data) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException{

        Cipher ci = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        ci.init(Cipher.ENCRYPT_MODE, key);
        byte[] bytes = Base64.decode(data);
        int inputLen = bytes.length;
        int offLen = 0;
        int i = 0;
        ByteArrayOutputStream bops = new ByteArrayOutputStream();
        while(inputLen - offLen > 0){
            byte [] cache;
            if(inputLen - offLen > 117){
                cache = ci.doFinal(bytes, offLen,117);
            }else{
                cache = ci.doFinal(bytes, offLen,inputLen - offLen);
            }
            bops.write(cache);
            i++;
            offLen = 117 * i;
        }
        bops.close();
        byte[] encryptedData = bops.toByteArray();
        String encodeToString = Base64.toBase64String(encryptedData);
        return encodeToString;
    }
    public byte[] RSADecrypt(PublicKey key,String data) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, IOException, Exception{

        Cipher ci = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        ci.init(Cipher.DECRYPT_MODE,key);

        byte[] bytes = Base64.decode(data);
        int inputLen = bytes.length;
        int offLen = 0;
        int i = 0;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while(inputLen - offLen > 0){
            byte[] cache;
            if(inputLen - offLen > 128){
                cache = ci.doFinal(bytes,offLen,128);
            }else{
                cache = ci.doFinal(bytes,offLen,inputLen - offLen);
            }
            byteArrayOutputStream.write(cache);
            i++;
            offLen = 128 * i;

        }
        byteArrayOutputStream.close();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return byteArray;
    }

}


