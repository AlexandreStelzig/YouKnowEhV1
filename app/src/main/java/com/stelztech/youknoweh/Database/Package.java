package com.stelztech.youknoweh.Database;

/**
 * Created by Alexandre on 4/25/2016.
 */
public class Package {

    private String idPackage;
    private String packageName;

    Package() {
    }

    public Package(String idPackage, String packageName) {
        this.idPackage = idPackage;
        this.packageName = packageName;
    }


    public String getIdPackage() {
        return idPackage;
    }

    public void setIdPackage(String idPackage) {
        this.idPackage = idPackage;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
