package com.example.Sign;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;


public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    String packageName = "com.tencent.mm";

    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        textView = (TextView) findViewById(R.id.info);

        sign1();

        getCertMsg(this, packageName);

        getSingInfo(packageName);


        //新浪sign
        byte[] sign = getSign(this.getApplicationContext(), packageName);

        if (sign != null) {
            String str = "";
            try {
                str = MD5.hexdigest(sign);
                Log.d("weibosdk", "gen md5 = " + str);
            } catch (Exception e) {
            }
            textView.setText(sign+"----"+str);
        }


    }

    private void sign1() {
        try {
            final String packname = getPackageName();
            PackageInfo packageInfo = getPackageManager().getPackageInfo(packname, PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            int code = sign.hashCode();
            Log.i("s", "签名的哈希值正确" + code);
            textView.setText(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String[] getCertMsg(Context context, String packageName) {
        String[] certMsg = new String[2];
        PackageInfo pis;
        try {
            pis = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Signature[] sigs = pis.signatures;    //签名
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
//获取证书
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(
                    new ByteArrayInputStream(sigs[0].toByteArray()));
//获取证书发行者   可根据证书发行者来判断该应用是否被二次打包（被破解的应用重新打包后，签名与原包一定不同，据此可以判断出该应用是否被人做过改动）
            certMsg[0] = cert.getIssuerDN().toString();
            certMsg[1] = cert.getSubjectDN().toString();
            Log.i("y", "IssuerDN: " + certMsg[0] + "  SubjectDN: " + certMsg[1]);
            textView.setText("IssuerDN: " + certMsg[0] + "  SubjectDN: " + certMsg[1]);
        } catch (CertificateException e) {
            Log.i("y", "CertificateException" + e.getMessage());
        } catch (Exception e) {
            Log.i("y", "Exception: " + e.getMessage());
        }
        return certMsg;
    }

    //////////////////////////////
    public void getSingInfo(String packName) {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(
                    packName, PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            parseSignature(sign.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseSignature(byte[] signature) {
        try {
            CertificateFactory certFactory = CertificateFactory
                    .getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory
                    .generateCertificate(new ByteArrayInputStream(signature));
            String pubKey = cert.getPublicKey().toString();
            String signNumber = cert.getSerialNumber().toString();

            System.out.println("pubKey:" + pubKey);
            System.out.println("signNumber:" + signNumber);
            System.out.println("pubKey:" + cert.getSigAlgName());
            System.out.println("getSigAlgOID:" + cert.getSigAlgOID());
            System.out.println("getType:" + cert.getType());
            System.out.println("getIssuerDN:" + cert.getIssuerDN());
            System.out.println("getBasicConstraints:" + cert.getBasicConstraints());
            System.out.println("getKeyUsage:" + cert.getKeyUsage());
            System.out.println("getVersion:" + cert.getVersion());
        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }


    public static byte[] getSign(Context context, String pkgName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(pkgName, 64);
            for (Signature toByteArray : packageInfo.signatures) {
                byte[] str = toByteArray.toByteArray();
                if (str != null) {
                    return str;
                }
            }
            return null;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }




}
