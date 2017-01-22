package io.phineas.pogostatusmanager;

/**
 * Created by Phineas (phineas.io) on 20/07/2016
 */
public enum Version {

    LATEST(1.0);

    double version;

    Version(double version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return String.valueOf(version);
    }
}
