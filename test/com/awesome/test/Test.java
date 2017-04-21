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
//        String url = "http://appdl.hicloud.com/dl/appdl/application/apk/54/549106246d4d44f18049950cb2738a39/com.uc108.mobile.snda1.huawei.1703131446.apk";
//        String url = "http://183.62.114.167/appdl.hicloud.com/dl/appdl/application/apk/be/bee3dc13d5d64013aa4623dffc413f1d/com.dp.android.elong.1703301528.apk";
//        String url = "http://imtt.dd.qq.com/16891/919A960316C9E0A76D5E9D9BCC5F0E10.apk?fsname=com.tencent.tmgp.sgame_1.18.1.7_18010702.apk&csr=97c2";
        String url = "http://l10.gdl.netease.com/qnyh_netease_netease.icoou_cpa_dev_1.1.9.apk";
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
