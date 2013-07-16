package com.puppetlabs.puppetdb.javaclient.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.net.ssl.TrustManagerFactory;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.puppetlabs.puppetdb.javaclient.APIPreferences;

/**
 * Provides an SSLSocketFactory that has been configured with a KeyStore initialized
 * from the PEM file settings {@link APIPreferences#getCaCertPEM()}, {@link APIPreferences#getCertPEM()}, and
 * {@link APIPreferences#getPrivateKeyPEM()}
 */
public class PEM_SSLSocketFactoryProvider implements Provider<SSLSocketFactory> {
	private static final String PASSWORD = "puppet";

	@Inject
	private APIPreferences preferences;

	@Override
	public SSLSocketFactory get() {
		Security.addProvider(new BouncyCastleProvider());

		try {
			X509CertificateHolder caCertHolder = readPEMObject(preferences.getCaCertPEM(), "ca-cert pem", X509CertificateHolder.class);
			X509CertificateHolder certHolder = readPEMObject(preferences.getCertPEM(), "cert pem", X509CertificateHolder.class);
			PEMKeyPair keyPair = readPEMObject(preferences.getPrivateKeyPEM(), "private key pem", PEMKeyPair.class);

			// We need a factory that can generate a X.509 certificate using BouncyCastleProvideer
			CertificateFactory factory = CertificateFactory.getInstance("X.509", "BC");
			Certificate caCert = getCertificate(caCertHolder, factory);
			Certificate cert = getCertificate(certHolder, factory);

			PrivateKey privateKey = getPrivateKey(keyPair);

			KeyStore truststore = KeyStore.getInstance("BKS");
			truststore.load(null);
			// initialize trust manager factory with the read truststore
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(truststore);

			KeyStore keystore = KeyStore.getInstance("JKS");
			keystore.load(null);
			truststore.setCertificateEntry("ca-cert-alias", caCert);
			truststore.setCertificateEntry("cert-alias", cert);
			keystore.setKeyEntry("key-alias", privateKey, PASSWORD.toCharArray(), new Certificate[] { cert });

			return new SSLSocketFactory(keystore, PASSWORD, truststore);
		}
		catch(RuntimeException e) {
			throw e;
		}
		catch(Exception e) {
			throw new ProvisionException("Unable to create SSLSocketFactory", e);
		}
	}

	private PrivateKey getPrivateKey(PEMKeyPair keyPair) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
		byte[] encodedPrivateKey = keyPair.getPrivateKeyInfo().getEncoded();
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
		return KeyFactory.getInstance("RSA").generatePrivate(privateKeySpec);
	}

	private Certificate getCertificate(X509CertificateHolder certHolder, CertificateFactory factory) throws CertificateException,
			IOException {
		return factory.generateCertificate(new ByteArrayInputStream(certHolder.getEncoded()));
	}

	private static <T> T readPEMObject(File pemFile, String prefName, Class<T> type) throws IOException, ProvisionException {
		if(pemFile == null)
			throw new ProvisionException("Missing preference setting for '" + prefName + '\'');
		PEMParser reader = new PEMParser(new InputStreamReader(new FileInputStream(pemFile)));
		try {
			Object obj = reader.readObject();
			return type.cast(obj);
		}
		finally {
			reader.close();
		}
	}
}
