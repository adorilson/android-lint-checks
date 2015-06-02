package com.donvigo.androidmanifestparser;

import com.donvigo.androidmanifestparser.manifest.AndroidManifest;

/**
 * Created by vgaidarji on 12.03.14.
 */
public class InstalledPackageInfo {
    private String pathToPackage;
    private boolean isLauncher;
    private AndroidManifest androidManifest;

    public InstalledPackageInfo(String absolutePath, boolean isLauncher) {
        this.pathToPackage = absolutePath;
        this.isLauncher = isLauncher;
    }

    public InstalledPackageInfo(String absolutePath, AndroidManifest manifest) {
        this.pathToPackage = absolutePath;
        this.androidManifest = manifest;
    }

    public String getPathToPackage() {
        return pathToPackage;
    }

    public void setPathToPackage(String pathToPackage) {
        this.pathToPackage = pathToPackage;
    }

    public AndroidManifest getAndroidManifest() {
        return androidManifest;
    }

    public void setAndroidManifest(AndroidManifest androidManifest) {
        this.androidManifest = androidManifest;
    }

    public boolean isLauncher() {
        return isLauncher;
    }

    public void setLauncher(boolean isLauncher) {
        this.isLauncher = isLauncher;
    }
}
