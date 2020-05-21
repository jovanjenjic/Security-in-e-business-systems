package com.example.demo.pki;

import com.example.demo.pki.certificates.CertificateGenerator;
import com.example.demo.pki.data.IssuerData;
import com.example.demo.pki.data.SubjectData;
import com.example.demo.pki.keystores.KeyStoreWriter;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.*;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GenerateRootCertificate {

    public GenerateRootCertificate() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public void generate() {
        SubjectData subjectData = generateSubjectDataRoot();

        KeyPair keyPairIssuer = generateKeyPair();
        IssuerData issuerData = generateIssuerDataRoot(keyPairIssuer.getPrivate());

        CertificateGenerator certificateGenerator = new CertificateGenerator();
        X509Certificate x509Certificate = certificateGenerator.generateCertificate(subjectData, issuerData);

        KeyStoreWriter keyStore = new KeyStoreWriter();
        KeyStoreWriter keyStore1 = new KeyStoreWriter();
        KeyStoreWriter keyStore2 = new KeyStoreWriter();

        char[] password = new char[5];
        password[0] = '1';
        password[1] = '2';
        password[2] = '3';
        password[3] = '4';
        password[4] = '5';

        keyStore.loadKeyStore(null, password);
        keyStore1.loadKeyStore(null, password);
        keyStore2.loadKeyStore(null, password);

        keyStore.write("root", keyPairIssuer.getPrivate(), password, x509Certificate);
        keyStore.saveKeyStore("./src/main/resources/keystores/root.jks", password);

        SubjectData subjectData1 = generateSubjectData();
        CertificateGenerator certificateGenerator1 = new CertificateGenerator();
        X509Certificate x509Certificate2 = certificateGenerator1.generateCertificate(subjectData1, issuerData);

        keyStore1.write("ca", keyPairIssuer.getPrivate(), password, x509Certificate2);
        keyStore1.saveKeyStore("./src/main/resources/keystores/CA.jks", password);

        SubjectData subjectData2 = generateSubjectDataEndEntity();
        CertificateGenerator certificateGenerator2 = new CertificateGenerator();
        X509Certificate x509Certificate3 = certificateGenerator2.generateCertificate(subjectData2, issuerData);

        keyStore2.write("end-entity", keyPairIssuer.getPrivate(), password, x509Certificate3);
        keyStore2.saveKeyStore("./src/main/resources/keystores/endEntity.jks", password);


//        System.out.println(x509Certificate);
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        System.out.println(x509Certificate2);
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        System.out.println(x509Certificate3);
    }

    public SubjectData generateSubjectData() {
        try {
            KeyPair keyPairSubject = generateKeyPair();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date startDate = simpleDateFormat.parse("19-05-2020");
            Date endDate = simpleDateFormat.parse("19-06-2020");
            String serialNumber = "2";

            X500NameBuilder x500NameBuilder = new X500NameBuilder(BCStyle.INSTANCE);
            x500NameBuilder.addRDN(BCStyle.CN, "Marko Markovic");
            x500NameBuilder.addRDN(BCStyle.SURNAME, "Marko");
            x500NameBuilder.addRDN(BCStyle.GIVENNAME, "Markovic");
            x500NameBuilder.addRDN(BCStyle.E, "marko.markovic@gmail.com");
            x500NameBuilder.addRDN(BCStyle.C, "CA");
            x500NameBuilder.addRDN(BCStyle.UID, "0002");

            return new SubjectData(keyPairSubject.getPublic(), x500NameBuilder.build(), serialNumber, startDate, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public SubjectData generateSubjectDataEndEntity() {
        try {
            KeyPair keyPairSubject = generateKeyPair();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date startDate = simpleDateFormat.parse("19-05-2020");
            Date endDate = simpleDateFormat.parse("19-06-2020");
            String serialNumber = "2";

            X500NameBuilder x500NameBuilder = new X500NameBuilder(BCStyle.INSTANCE);
            x500NameBuilder.addRDN(BCStyle.CN, "Milos Milosevic");
            x500NameBuilder.addRDN(BCStyle.SURNAME, "Milos");
            x500NameBuilder.addRDN(BCStyle.GIVENNAME, "Milosevic");
            x500NameBuilder.addRDN(BCStyle.E, "milos.milosevic@gmail.com");
            x500NameBuilder.addRDN(BCStyle.C, "End-entity");
            x500NameBuilder.addRDN(BCStyle.UID, "0003");

            return new SubjectData(keyPairSubject.getPublic(), x500NameBuilder.build(), serialNumber, startDate, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public IssuerData generateIssuerDataRoot(PrivateKey privateKey) {
        X500NameBuilder x500NameBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        x500NameBuilder.addRDN(BCStyle.CN, "Pera Peric");
        x500NameBuilder.addRDN(BCStyle.SURNAME, "Pera");
        x500NameBuilder.addRDN(BCStyle.GIVENNAME, "Peric");
        x500NameBuilder.addRDN(BCStyle.E, "pera.peric@gmail.com");
        x500NameBuilder.addRDN(BCStyle.C, "Root");
        x500NameBuilder.addRDN(BCStyle.UID, "0001");
        return new IssuerData(privateKey, x500NameBuilder.build());
    }

    public SubjectData generateSubjectDataRoot() {
        try {
            KeyPair keyPairSubject = generateKeyPair();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date startDate = simpleDateFormat.parse("19-05-2020");
            Date endDate = simpleDateFormat.parse("19-06-2020");
            String serialNumber = "1";

            X500NameBuilder x500NameBuilder = new X500NameBuilder(BCStyle.INSTANCE);
            x500NameBuilder.addRDN(BCStyle.CN, "Pera Peric");
            x500NameBuilder.addRDN(BCStyle.SURNAME, "Pera");
            x500NameBuilder.addRDN(BCStyle.GIVENNAME, "Peric");
            x500NameBuilder.addRDN(BCStyle.E, "pera.peric@gmail.com");
            x500NameBuilder.addRDN(BCStyle.C, "Root");
            x500NameBuilder.addRDN(BCStyle.UID, "0001");

            return new SubjectData(keyPairSubject.getPublic(), x500NameBuilder.build(), serialNumber, startDate, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public KeyPair generateKeyPair() { // Bilo je private ja promenio u public
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyPairGenerator.initialize(2048, random);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        GenerateRootCertificate generateRootCertificate = new GenerateRootCertificate();
        generateRootCertificate.generate();
    }
}