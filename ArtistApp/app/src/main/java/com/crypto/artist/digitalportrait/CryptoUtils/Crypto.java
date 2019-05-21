package com.crypto.artist.digitalportrait.CryptoUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.crypto.artist.digitalportrait.Utilities.Preferences;

import org.apache.commons.codec.binary.Base64;
import org.spongycastle.crypto.CipherParameters;
import org.spongycastle.crypto.engines.AESEngine;
import org.spongycastle.crypto.modes.CBCBlockCipher;
import org.spongycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;

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

        int length1 = cipher.processBytes(data,  0, data.length, outputBuffer, 0);
        int length2 = cipher.doFinal(outputBuffer, length1);

        byte[] result = new byte[length1+length2];

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
        aes.init(false,  ivAndKey);

        return cipherData(aes, cipher);
    }

    public void signGenerator(Bitmap bitmap) throws Exception{
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER);
        keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
        KeyPair pair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();
        Signature RSA = Signature.getInstance(ALGORITHM_SIGN, PROVIDER);
        RSA.initSign(privateKey);

        BufferedInputStream bufferedInputStream = fromBitmapToBIS(bitmap);

        byte[] buffer = new byte[KEY_SIZE];
        int length;
        while(bufferedInputStream.available() != 0){
            length = bufferedInputStream.read(buffer);
            RSA.update(buffer, 0, length);
        }

        bufferedInputStream.close();
        this.signature = Base64.encodeBase64(RSA.sign());
        this.publicKey = Base64.encodeBase64(publicKey.getEncoded());
        this.privateKey = Base64.encodeBase64(privateKey.getEncoded());
        preferences.store("signature", this.signature);
        preferences.store("publicKey", this.publicKey);
        preferences.store("privateKey", this.privateKey);
        //Signature
        Log.i("Crypto F1:", "signImage: " + this.signature);
        //Public Key
        Log.i("Crypto F2:", "signImage: " + this.publicKey);
        //It's necessary store F1 and F2
    }


    public BufferedInputStream fromBitmapToBIS(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        InputStream is = new ByteArrayInputStream(stream. toByteArray());
        return new BufferedInputStream(is);
    }

    /*, byte[] sign, byte[] pubKey*/
    public boolean verifySign(Bitmap bitmap) throws Exception{
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(new Preferences(context).get("publicKey")));
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, PROVIDER);
        //Getting public key
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        //Verify signature
        Signature signature = Signature.getInstance(ALGORITHM_SIGN, PROVIDER);
        signature.initVerify(publicKey);

        BufferedInputStream bufferedInputStream = fromBitmapToBIS(bitmap);
        byte[] buffer = new byte[KEY_SIZE];
        int length;
        while (bufferedInputStream.available() != 0){
            length = bufferedInputStream.read(buffer);
            signature.update(buffer, 0, length);
        }
        bufferedInputStream.close();
        return (signature.verify(Base64.decodeBase64(new Preferences(context).get("signature"))));
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
}


