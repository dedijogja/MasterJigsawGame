package us.masterjigsawgame.util;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Constant {

    public static final String KODE_INDEX = "index";

    public static final String FOLDER_KONTEN = "puzzle";

    public static final String KODE_INDEX_CUSTOM = "72513";

    public static String GAGAL_LOAD_IKLAN = "GAGAL_LOAD";
    public static String BERHASIL_LOAD_IKLAN = "BERHASIL_LOAD";

    public static String[] LIST_SEMUA_KONTEN(Context context) throws IOException {
        List<String> modelist = new ArrayList<>();
        String[] fileNames = context.getAssets().list(FOLDER_KONTEN);
        Collections.addAll(modelist, fileNames);
        String[] dap = new String[modelist.size()];
        for(int i = 0; i<modelist.size(); i++){
            dap[i] = modelist.get(i);
        }
        return dap;
    }

    public static byte[] CUSTOM_BYTE = null;

}
