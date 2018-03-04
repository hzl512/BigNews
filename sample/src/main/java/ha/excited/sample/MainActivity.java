package ha.excited.sample;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import static ha.excited.sample.AppUtils.fileCopy;

public class MainActivity extends Activity {

    private static final String PATH = Environment.getExternalStorageDirectory().getPath();
    private static final String NEW_APK = PATH + File.separator + "fxftvoice2.03.apk";
    private String OUT_APK = PATH + File.separator + "out.apk";//输出新的合并代码
    private String OLD_APK = PATH + File.separator + "old.apk";//在设备中安装的应用
    private static final String PATCH_FILE = PATH + File.separator + "patch";
    private static final String apkName="com.fxft.chemeitong";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogUtils.v("MainActivity-----------", getPackageResourcePath());

        findViewById(R.id.buttonDiff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diff();
            }
        });
        findViewById(R.id.buttonMake).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                make();
            }
        });
        ApplicationInfo info = AppUtils.getApplicationInfo(this, apkName);
        LogUtils.v("MainActivity", info.sourceDir);

        try {
            boolean state = fileCopy(info.sourceDir, OLD_APK);
            LogUtils.v("MainActivity", state + " is ok");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.v("MainActivity", " is not ok");
        }

    }

    /**
     * 生成差异包
     */
    private void diff() {
        ApplicationInfo info = AppUtils.getApplicationInfo(this, apkName);
        if (PatchUtil.diff(info.sourceDir, NEW_APK, PATCH_FILE)) {
            Toast.makeText(this, getString(R.string.diff_done) + PATCH_FILE, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.diff_failed), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 使用旧的安装包与差异包合并为新的安装包
     */
    private void make() {
        ApplicationInfo info = AppUtils.getApplicationInfo(this, apkName);
        if (PatchUtil.make(info.sourceDir, OUT_APK, PATCH_FILE)) {
            Toast.makeText(this, getString(R.string.make_done) + OUT_APK, Toast.LENGTH_SHORT).show();
            Log.v("MainActivity",OUT_APK);
            startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.fromFile(new File(OUT_APK)),
                    "application/vnd.android.package-archive"));
        } else {
            Toast.makeText(this, getString(R.string.make_failed), Toast.LENGTH_SHORT).show();
        }
    }



}
