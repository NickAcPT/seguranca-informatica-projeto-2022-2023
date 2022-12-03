package pt.ua.segurancainformatica.licensing.common.utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.*;
import java.util.Arrays;
import java.util.EnumSet;

public class CertificateUtils {
    private CertificateUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isValid(@NotNull Certificate certificate) throws KeyStoreException, InvalidAlgorithmParameterException, CertificateException, IOException, NoSuchAlgorithmException {
        // acesso á pasta de certificados
        String filename = System.getProperty("java.home") + "/lib/security/cacerts".replace('/', File.separatorChar);
        KeyStore ks= KeyStore.getInstance("JKS");
        char[] pwdArray = "changeit".toCharArray();
        ks.load(new FileInputStream(filename), pwdArray);
        PKIXParameters par = new PKIXParameters(ks);


        // caminho de certificação
        X509CertSelector cs = new X509CertSelector();
        cs.setCertificate((X509Certificate)certificate);
        CertPathBuilder cpb = CertPathBuilder.getInstance("PKIX");

        PKIXBuilderParameters pkixBParams = new PKIXBuilderParameters(par.getTrustAnchors(),cs);
        pkixBParams.setRevocationEnabled(false);


        //Fornecer os certificados intermediários
        CollectionCertStoreParameters ccsp =
                new CollectionCertStoreParameters(Arrays.asList(certificate));
        CertStore store = CertStore.getInstance("Collection", ccsp);
        pkixBParams.addCertStore(store);


        CertPath cp = null;
        try {
            CertPathBuilderResult cpbr = cpb.build(pkixBParams);
            cp = cpbr.getCertPath();
            System.out.println("Certification path built with success!");
        } catch (CertPathBuilderException ex) {
            System.out.println("It was not possible to build a certification path!");
        }
        PKIXParameters pkixParams = new PKIXParameters(par.getTrustAnchors());
        CertPathValidator cpv = CertPathValidator.getInstance("PKIX");

        par.setRevocationEnabled(false);
        Security.setProperty("ocsp.enable", "true");
        PKIXRevocationChecker rc = (PKIXRevocationChecker) cpv.getRevocationChecker();
        // Configuração para validar todos os certificados em cadeia utilizando apenas OCSP
        rc.setOptions(EnumSet.of(PKIXRevocationChecker.Option.SOFT_FAIL,
                PKIXRevocationChecker.Option.NO_FALLBACK));
        PKIXCertPathValidatorResult result = null;
        try {
        //Faz a validação
            result = (PKIXCertPathValidatorResult) cpv.validate(cp, pkixParams);
            System.out.println("Certificado Válido");
            System.out.println("Issuer of trust anchor certificate: " +
                    result.getTrustAnchor().getTrustedCert().getIssuerDN().getName());
            return true;
        } catch (CertPathValidatorException cpve) {
            System.out.println("Validation failure, cert[" + cpve.getIndex() + "] :" +
                    cpve.getMessage());

        }
        return false;
    }
/*
    public static void main(String[] args) throws KeyStoreException, IOException, InvalidAlgorithmParameterException, CertificateException, NoSuchAlgorithmException {
        String filename = System.getProperty("java.home") + "/lib/security/cacerts".replace('/', File.separatorChar);
        KeyStore ks= KeyStore.getInstance("JKS");
        char[] pwdArray = "changeit".toCharArray();
        ks.load(new FileInputStream(filename), pwdArray);
        PKIXParameters par = new PKIXParameters(ks);
        System.out.println(par.getTrustAnchors());
    }
*/
}
