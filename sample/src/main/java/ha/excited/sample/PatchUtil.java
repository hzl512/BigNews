package ha.excited.sample;

import java.io.File;

import ha.excited.BigNews;

final public class PatchUtil {
    public static boolean make(String path, String newApkPath, String patchPath) {
        //context.getPackageResourcePath()
        return BigNews.make(path, newApkPath, patchPath) && new File(newApkPath).exists();
    }

    public static boolean diff(String path, String newApkPath, String patchPath) {
        //context.getPackageResourcePath()
        return BigNews.diff(path, newApkPath, patchPath) && new File(patchPath).exists();
    }
}
