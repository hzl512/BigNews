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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {

    private static final String PATH = Environment.getExternalStorageDirectory().getPath();
    private static final String NEW_APK = PATH + File.separator + "fxftvoice2.03.apk";
    String OUT_APK = PATH + File.separator + "out.apk";
    private static final String PATCH_FILE = PATH + File.separator + "patch";

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
        ApplicationInfo info = AppUtils.getApplicationInfo(this, "com.fxft.chemeitong");
//        OUT_APK=info.sourceDir;
//        LogUtils.v("MainActivity", info.sourceDir);

//        try {
//            boolean state = fileCopy(info.sourceDir, OUT_APK);
//            LogUtils.v("MainActivity", state + " is ok");
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtils.v("MainActivity", " is not ok");
//        }

    }

    private void diff() {
        ApplicationInfo info = AppUtils.getApplicationInfo(this, "com.fxft.chemeitong");
        if (PatchUtil.diff(info.sourceDir, NEW_APK, PATCH_FILE)) {
            Toast.makeText(this, getString(R.string.diff_done) + PATCH_FILE, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.diff_failed), Toast.LENGTH_SHORT).show();
        }
    }

    private void make() {
        ApplicationInfo info = AppUtils.getApplicationInfo(this, "com.fxft.chemeitong");
        if (PatchUtil.make(info.sourceDir, OUT_APK, PATCH_FILE)) {
            Toast.makeText(this, getString(R.string.make_done) + OUT_APK, Toast.LENGTH_SHORT).show();
            Log.v("MainActivity",OUT_APK);
            startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.fromFile(new File(OUT_APK)),
                    "application/vnd.android.package-archive"));
        } else {
            Toast.makeText(this, getString(R.string.make_failed), Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean fileCopy(String oldFilePath, String newFilePath) throws IOException {
        //如果原文件不存在
        if (fileExists(oldFilePath) == false) {
            return false;
        }
        //获得原文件流
        FileInputStream inputStream = new FileInputStream(new File(oldFilePath));
        byte[] data = new byte[1024];
        //输出流
        FileOutputStream outputStream = new FileOutputStream(new File(newFilePath));
        //开始处理流
        while (inputStream.read(data) != -1) {
            outputStream.write(data);
        }
        inputStream.close();
        outputStream.close();
        return true;
    }

    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

}
