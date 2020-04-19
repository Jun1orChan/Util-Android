package org.jun1or.util_android;

import org.jun1or.util.CustomNameSPUtil;

public class SPUtilEx extends CustomNameSPUtil {

    private static SPUtilEx INSTANCE;

    private SPUtilEx() {

    }

    @Override
    public String getFileName() {
        return "test_pref";
    }

    public static SPUtilEx getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (SPUtilEx.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SPUtilEx();
                }
            }
        }
        return INSTANCE;
    }
}
