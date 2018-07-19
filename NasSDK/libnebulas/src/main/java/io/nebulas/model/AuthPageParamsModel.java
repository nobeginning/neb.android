package io.nebulas.model;

import android.os.Bundle;

public class AuthPageParamsModel extends PageParamsModel {

    private byte[] appIcon;
    private String appName;

    public byte[] getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(byte[] appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("appName", appName);
        bundle.putByteArray("appIcon", appIcon);
        return bundle;
    }
}
