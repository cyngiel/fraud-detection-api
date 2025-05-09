package com.frauddetection.api.service.mc;

import com.mastercard.developer.signers.OkHttpSigner;
import com.mastercard.developer.utils.AuthenticationUtils;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Data;
import okhttp3.Request;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

/**
 * This class is responsible for signing requests to the MasterCard API.
 * It uses the OkHttpSigner from the MasterCard SDK to sign requests with a private key.
 */
@Data
@ApplicationScoped
public class MCSigner {

    OkHttpSigner signer;

    @ConfigProperty(name = "keystore.sandbox.path")
    String keystorePath;
    @ConfigProperty(name = "keystore.sandbox.alias")
    String keystoreAlias;
    @ConfigProperty(name = "keystore.sandbox.password")
    String keystorePassword;
    @ConfigProperty(name = "keystore.sandbox.customerkey")
    String consumerKey;

    @PostConstruct
    void initSigner() throws UnrecoverableKeyException, CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException {
        signer = new OkHttpSigner(consumerKey, getSignedKey());
    }

    public PrivateKey getSignedKey() throws UnrecoverableKeyException, CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException {
        return AuthenticationUtils.loadSigningKey(keystorePath, keystoreAlias, keystorePassword);
    }

    public void sign(Request.Builder request) throws IOException {
        signer.sign(request);
    }
}
