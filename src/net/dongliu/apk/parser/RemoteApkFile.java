package net.dongliu.apk.parser;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.dongliu.apk.parser.bean.ApkSignStatus;
import net.dongliu.apk.parser.utils.Utils;

import org.trimatek.remotezip.model.RemoteZipEntry;
import org.trimatek.remotezip.tools.RemoteZipFile;

/**
 * Parse apk file from url. This class is not thread-safe
 * 
 * @author Wu Zhenlin
 */
public class RemoteApkFile extends AbstractApkFile implements Closeable {

    private RemoteZipFile mRemoteZipFile;
    private String mUrl;

    public RemoteApkFile(String url) throws IOException {
        this.mUrl = url;
        this.mRemoteZipFile = new RemoteZipFile();
        if (!mRemoteZipFile.load(url))
            throw new FileNotFoundException("cannot open url: " + url);
    }

    @Override
    protected byte[] getCertificateData() throws IOException {
        for (RemoteZipEntry entry : mRemoteZipFile.getEntries()) {
            if (entry.getName().toUpperCase().endsWith(".EC") || entry.getName().toUpperCase().endsWith(".RSA")
                    || entry.getName().toUpperCase().endsWith(".DSA")) {
                return Utils.toByteArray(mRemoteZipFile.getInputStream(entry));
            }
        }
        throw new FileNotFoundException("no *.RSA/DSA/EC file in url: " + mUrl);
    }

    @Override
    public byte[] getFileData(String path) throws IOException {
        for (RemoteZipEntry entry : mRemoteZipFile.getEntries()) {
            if (path.equals(entry.getName())) {
                return Utils.toByteArray(mRemoteZipFile.getInputStream(entry));
            }
        }
        throw new FileNotFoundException("no " + path + " file in url: " + mUrl);
    }

    @Override
    public ApkSignStatus verifyApk() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() throws IOException {
        super.close();
        mRemoteZipFile = null;
    }

}
