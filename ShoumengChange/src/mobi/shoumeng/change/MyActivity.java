package mobi.shoumeng.change;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import mobi.shoumeng.judge.AES;
import mobi.shoumeng.judge.Crypto;
import mobi.shoumeng.judge.DebugSetting;
import mobi.shoumeng.judge.util.PhoneJudgeUtilMain;

public class MyActivity extends Activity {

    private static final String KEY = "554a88c9c9a51379d2dbe569fda606cd";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yyb_login:
                saveJudge(this, 1, "com.tencent.tmgp.lyzh.shoumeng", "mode");
                break;
            case R.id.yyb_pay:
                saveJudge(this, 1, "com.tencent.tmgp.lyzh.shoumeng", "pay_mode");
                break;
            case R.id.sm_login:
                saveJudge(this, 0, "com.tencent.tmgp.lyzh.shoumeng", "mode");
                break;
            case R.id.sm_pay:
                saveJudge(this, 0, "com.tencent.tmgp.lyzh.shoumeng", "pay_mode");
                break;
        }
        Toast.makeText(this, "修改完成", Toast.LENGTH_SHORT).show();
    }

    private static void saveJudge(Context context, int mode, String packageName, String fileName) {
        String content = "";
        try {
            Crypto crypto = new AES();
            content = crypto.encrypt(KEY, mode + "");
            DebugSetting.toLog("本地存储-->" + content);
        } catch (Exception e) {
            if (DebugSetting.IS_DEBUG_MODE)
                e.printStackTrace();
        }

        PhoneJudgeUtilMain.writeChannelInfo(context, content, packageName, fileName);
    }

}
