package com.example.IMSI;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    TextView infoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        infoView = (TextView) findViewById(R.id.info);

//        byte[] sgin = getSign(this, "com.example.Aidl");
//
//        infoView.setText(sgin.toString());

        getSingInfo();
    }

    private byte[] getSign(Context context, String packName) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm
                .getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> iter = apps.iterator();

        while (iter.hasNext()) {
            PackageInfo info = iter.next();
            String packageName = info.packageName;
            //按包名 取签名
            if (packageName.equals(packName)) {
                return info.signatures[0].toByteArray();

            }
        }
        return null;
    }


    public static String getPublicKey(byte[] signature) {
        try {

            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory
                    .generateCertificate(new ByteArrayInputStream(signature));

            String publickey = cert.getPublicKey().toString();
            publickey = publickey.substring(publickey.indexOf("modulus: ") + 9,
                    publickey.indexOf("\n", publickey.indexOf("modulus:")));

            Log.d("TRACK", publickey);
            return publickey;
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return null;


    }
///////////////////////////

//    private String showUninstallAPKSignatures(String apkPath) {
//        String PATH_PackageParser = "android.content.pm.PackageParser";
//        try {
//            // apk包的文件路径
//            // 这是一个Package 解释器, 是隐藏的
//            // 构造函数的参数只有一个, apk文件的路径
//            // PackageParser packageParser = new PackageParser(apkPath);
//            Class pkgParserCls = Class.forName(PATH_PackageParser);
//            Class[] typeArgs = new Class[1];
//            typeArgs[0] = String.class;
//            Constructor pkgParserCt = pkgParserCls.getConstructor(typeArgs);
//            Object[] valueArgs = new Object[1];
//            valueArgs[0] = apkPath;
//            Object pkgParser = pkgParserCt.newInstance(valueArgs);
//            MediaApplication.logD(DownloadApk.class, "pkgParser:" + pkgParser.toString());
//            // 这个是与显示有关的, 里面涉及到一些像素显示等等, 我们使用默认的情况
//            DisplayMetrics metrics = new DisplayMetrics();
//            metrics.setToDefaults();
//            // PackageParser.Package mPkgInfo = packageParser.parsePackage(new
//            // File(apkPath), apkPath,
//            // metrics, 0);
//            typeArgs = new Class[4];
//            typeArgs[0] = File.class;
//            typeArgs[1] = String.class;
//            typeArgs[2] = DisplayMetrics.class;
//            typeArgs[3] = Integer.TYPE;
//            Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod("parsePackage",
//                    typeArgs);
//            valueArgs = new Object[4];
//            valueArgs[0] = new File(apkPath);
//            valueArgs[1] = apkPath;
//            valueArgs[2] = metrics;
//            valueArgs[3] = PackageManager.GET_SIGNATURES;
//            Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser, valueArgs);
//
//            typeArgs = new Class[2];
//            typeArgs[0] = pkgParserPkg.getClass();
//            typeArgs[1] = Integer.TYPE;
//            Method pkgParser_collectCertificatesMtd = pkgParserCls.getDeclaredMethod("collectCertificates",
//                    typeArgs);
//            valueArgs = new Object[2];
//            valueArgs[0] = pkgParserPkg;
//            valueArgs[1] = PackageManager.GET_SIGNATURES;
//            pkgParser_collectCertificatesMtd.invoke(pkgParser, valueArgs);
//            // 应用程序信息包, 这个公开的, 不过有些函数, 变量没公开
//            Field packageInfoFld = pkgParserPkg.getClass().getDeclaredField("mSignatures");
//            Signature[] info = (Signature[]) packageInfoFld.get(pkgParserPkg);
//            MediaApplication.logD(DownloadApk.class, "size:" + info.length);
//            MediaApplication.logD(DownloadApk.class, info[0].toCharsString());
//            return info[0].toCharsString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private String getSign(Context context) {
//        PackageManager pm = context.getPackageManager();
//        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
//        Iterator<PackageInfo> iter = apps.iterator();
//        while (iter.hasNext()) {
//            PackageInfo packageinfo = iter.next();
//            String packageName = packageinfo.packageName;
//            if (packageName.equals(instance.getPackageName())) {
//                MediaApplication.logD(DownloadApk.class, packageinfo.signatures[0].toCharsString());
//                return packageinfo.signatures[0].toCharsString();
//            }
//        }
//        return null;
//    }


    public void getSingInfo() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo("com.tencent.mm", PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            parseSignature(sign.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseSignature(byte[] signature) {
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(signature));
            String pubKey = cert.getPublicKey().toString();
            String signNumber = cert.getSerialNumber().toString();
            System.out.println("signName:" + cert.getSigAlgName());
            System.out.println("pubKey:" + pubKey);
            System.out.println("signNumber:" + signNumber);
            System.out.println("subjectDN:" + cert.getSubjectDN().toString());
        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }

}
