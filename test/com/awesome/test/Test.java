package com.awesome.test;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.List;

import org.trimatek.remotezip.Config;

import net.dongliu.apk.parser.RemoteApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import net.dongliu.apk.parser.bean.CertificateMeta;

public class Test {
    public static void main(String[] args) throws IOException, CertificateException {
//        Config.PROXY_URL = "localhost";
//        Config.PROXY_PORT = "8888";
        String url = "http://appdl.hicloud.com/dl/appdl/application/apk/54/549106246d4d44f18049950cb2738a39/com.uc108.mobile.snda1.huawei.1703131446.apk?sign=97001011e310000820000000@FFD23D8FC24E6BA8EF8249BC2C26E6D5&source=search&subsource=%E6%96%97%E5%9C%B0%E4%B8%BB&listId=15&position=38&hcrId=51CD84BFCE4843AC957BB04429689E84&extendStr=detail%3A1%3B%3Btrace%3A1cb9d75a1107447d87d331218b025093%3BsearchApp%7C%E6%96%97%E5%9C%B0%E4%B8%BB&encryptType=1";
//        String url = "http://183.62.114.167/appdl.hicloud.com/dl/appdl/application/apk/be/bee3dc13d5d64013aa4623dffc413f1d/com.dp.android.elong.1703301528.apk";
        RemoteApkFile raf = new RemoteApkFile(url);
        ApkMeta apkMeta = raf.getApkMeta();
        System.out.println(apkMeta);
        List<CertificateMeta> certs = raf.getCertificateMetaList();
        if (certs != null && certs.size() > 0) {
            CertificateMeta certificateMeta = certs.get(0);
            String signature = certificateMeta.toCharsString();
            System.out.println(signature);
        }
    }
}
