package net.dongliu.apk.parser.parser;

import net.dongliu.apk.parser.bean.CertificateMeta;
import sun.security.pkcs.PKCS7;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * parser certificate info. One apk may have multi certificates(certificate
 * chain).
 * 
 * @author dongliu
 */
public class CertificateParser {

    private final byte[] data;

    private List<CertificateMeta> certificateMetas;

    public CertificateParser(byte[] data) {
        this.data = data;
    }

    /**
     * get certificate info
     * 
     * @throws IOException
     * @throws CertificateEncodingException
     */
    public void parse() throws IOException, CertificateException {

        PKCS7 pkcs7 = new PKCS7(data);
        X509Certificate[] certificates = pkcs7.getCertificates();
        certificateMetas = new ArrayList<>();
        for (X509Certificate certificate : certificates) {
            CertificateMeta certificateMeta = new CertificateMeta();
            certificateMetas.add(certificateMeta);

            byte[] bytes = certificate.getEncoded();
            String certMd5 = md5Digest(bytes);
            String publicKeyString = byteToHexString(bytes);
            String certBase64Md5 = md5Digest(publicKeyString);
            String charsString = toCharsString(bytes);
            certificateMeta.setCharsString(charsString);
            certificateMeta.setData(bytes);
            certificateMeta.setCertBase64Md5(certBase64Md5);
            certificateMeta.setCertMd5(certMd5);
            certificateMeta.setStartDate(certificate.getNotBefore());
            certificateMeta.setEndDate(certificate.getNotAfter());
            certificateMeta.setSignAlgorithm(certificate.getSigAlgName());
            certificateMeta.setSignAlgorithmOID(certificate.getSigAlgOID());
        }
    }

    private String toCharsString(byte[] input) {
        byte[] csig = new byte[input.length * 2];
        for (int j = 0; j < input.length; j++) {
            byte v = input[j];
            int d = (v >> 4) & 0xf;
            csig[j * 2] = (byte) (d >= 10 ? ('a' + d - 10) : ('0' + d));
            d = v & 0xf;
            csig[j * 2 + 1] = (byte) (d >= 10 ? ('a' + d - 10) : ('0' + d));
        }
        return new String(csig);
    }

    private String md5Digest(byte[] input) throws IOException {
        MessageDigest digest = getDigest("Md5");
        digest.update(input);
        return getHexString(digest.digest());
    }

    private String md5Digest(String input) throws IOException {
        MessageDigest digest = getDigest("Md5");
        digest.update(input.getBytes(StandardCharsets.UTF_8));
        return getHexString(digest.digest());
    }

    private String byteToHexString(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (byte aBArray : bArray) {
            sTemp = Integer.toHexString(0xFF & (char) aBArray);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    private String getHexString(byte[] digest) {
        BigInteger bi = new BigInteger(1, digest);
        return String.format("%032x", bi);
    }

    private MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<CertificateMeta> getCertificateMetas() {
        return certificateMetas;
    }
}
