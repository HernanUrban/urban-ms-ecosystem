package com.urban.authserver.security;

import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;


@Component
public class SecretKeyProvider {

    public String getKey() throws KeyStoreException, IOException,
            NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
        return new String(getKeyPair().getPublic().getEncoded(), "UTF-8");
    }

    private KeyPair getKeyPair() throws
            KeyStoreException, IOException,
            NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
        FileInputStream is = new FileInputStream(getClass().getClassLoader().getResource
            ("security/urbankeystore.jks").getFile().replaceAll("%20", " "));

        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(is, "Spassword".toCharArray());

        String alias = "urbankeystore";

        Key key = keystore.getKey(alias, "Kpassword".toCharArray());
        if (key instanceof PrivateKey) {
            Certificate cert = keystore.getCertificate(alias);
            PublicKey publicKey = cert.getPublicKey();
            return new KeyPair(publicKey, (PrivateKey) key);
        } else throw new UnrecoverableKeyException();
    }

}
