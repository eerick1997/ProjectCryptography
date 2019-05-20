package com.crypto.artist.digitalportrait.CryptoUtils;

import android.util.Log;

import com.crypto.artist.digitalportrait.PhotoEditor.Utils.BitmapUtils;

import org.apache.commons.codec.binary.Base64;
import org.spongycastle.crypto.CipherParameters;
import org.spongycastle.crypto.engines.AESEngine;
import org.spongycastle.crypto.modes.CBCBlockCipher;
import org.spongycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

public class Crypto {

    public static final String pictureName = "SDCARD0\\assets\\meme2.png";

    public static void f(){
        try {
            String file = "matrix.png";
            String[] arg = new String[3];
            arg[0] = "suepk.txt";
            arg[1] = "sig.txt";
            arg[2] = "matrix.png";
            signGen(file);
            verify(arg);
        } catch (Exception ex) {
            Log.e("A", "f: ", ex);
        }
    }

    public static byte[] cipherData(PaddedBufferedBlockCipher cipher, byte[] data) throws Exception {
        byte[] outputBuffer = new byte[cipher.getOutputSize(data.length)];

        int length1 = cipher.processBytes(data,  0, data.length, outputBuffer, 0);
        int length2 = cipher.doFinal(outputBuffer, length1);

        byte[] result = new byte[length1+length2];

        System.arraycopy(outputBuffer, 0, result, 0, result.length);

        return result;
    }

    public static byte[] encrypt(byte[] plain, CipherParameters ivAndKey) throws Exception {
        PaddedBufferedBlockCipher aes = new PaddedBufferedBlockCipher(
                new CBCBlockCipher(
                        new AESEngine()
                )
        );

        aes.init(true, ivAndKey);

        return cipherData(aes, plain);

    }

    public static byte[] decrypt(byte[] cipher, CipherParameters ivAndKey) throws Exception {
        PaddedBufferedBlockCipher aes = new PaddedBufferedBlockCipher(
                new CBCBlockCipher(
                        new AESEngine()
                )
        );
        aes.init(false,  ivAndKey);

        return cipherData(aes, cipher);
    }


    public static void signGen(String file) throws Exception
    {
        FileInputStream fis = new FileInputStream(file);
        Security.insertProviderAt(new BouncyCastleProvider(),1);
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "SC");
        int keysize=1024;
        keyGen.initialize(keysize, new SecureRandom());
        KeyPair pair = keyGen.generateKeyPair();
        PrivateKey priv = pair.getPrivate();
        PublicKey pub = pair.getPublic();
        Signature rsa = Signature.getInstance("SHA256withRSA", "SC");
        rsa.initSign(priv);
        BufferedInputStream bufin = new BufferedInputStream(fis);
        byte[] buffer = new byte[keysize];
        int len;
        while (bufin.available() != 0)
        {
            len = bufin.read(buffer);
            rsa.update(buffer, 0, len);
        }
        bufin.close();
        byte[] realSig = Base64.encodeBase64(rsa.sign());
        String f1="sig.txt",f2="suepk.txt";
        FileOutputStream sigfos = new FileOutputStream(f1);
        sigfos.write(realSig);
        sigfos.close();
        byte[] key = Base64.encodeBase64(pub.getEncoded());
        FileOutputStream keyfos = new FileOutputStream(f2);
        keyfos.write(key);
        keyfos.close();
        System.out.println("Signature: "+f1+"\nPublic key: "+f2);
    }

    public static void verify(String args[]) throws Exception {
        FileInputStream keyfis = new FileInputStream(args[0]);
        byte[] encKey = new byte[keyfis.available()];
        keyfis.read(encKey);
        keyfis.close();
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(encKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "SC");
        PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
        FileInputStream sigfis = new FileInputStream(args[1]);
        byte[] sigToVerify = new byte[sigfis.available()];
        sigfis.read(sigToVerify);
        sigfis.close();
        Signature sig = Signature.getInstance("SHA256withRSA", "SC");
        sig.initVerify(pubKey);
        FileInputStream datafis = new FileInputStream(args[2]);
        BufferedInputStream bufin = new BufferedInputStream(datafis);
        byte[] buffer = new byte[1024];
        int len;
        while (bufin.available() != 0)
        {
            len = bufin.read(buffer);
            sig.update(buffer, 0, len);
        }
        bufin.close();
        boolean verifies=sig.verify(Base64.decodeBase64(sigToVerify));
        System.out.println("Digital Signature verification result: "+verifies);
    }
}


