package us.masterjigsawgame.asyncronus;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import us.masterjigsawgame.util.Constant;
import us.masterjigsawgame.util.Komunikator;

public class SingleDecryption extends AsyncTask<String, Void, Bitmap>{

    private Context context;
    private ListenerDecrypt listenerDecrypt;
    private int lebarBitmap;
    private int tinggiBitmap;
    private ProgressDialog dialog;

    public SingleDecryption(Context context, int lebarBitmap, int tinggiBitmap){
        this.context = context;
        this.lebarBitmap = lebarBitmap;
        this.tinggiBitmap = tinggiBitmap;
    }

    @Override
    protected void onPreExecute() {
        if(lebarBitmap == 0 || tinggiBitmap == 0) {
            dialog = ProgressDialog.show(context, "Loading",
                    "Please wait ...", true);
            dialog.show();
        }
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        SecretKey secretKey =  new SecretKeySpec(Base64.decode(new Komunikator(context).getKeyAssets(), Base64.DEFAULT),
                0, Base64.decode(new Komunikator(context).getKeyAssets(), Base64.DEFAULT).length, "AES");
        Bitmap bitmap = null;
        try {
            InputStream inputStream = context.getAssets().open(Constant.FOLDER_KONTEN + "/" + strings[0]);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();

            Cipher AesCipher = Cipher.getInstance("AES");
            AesCipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] hasilDekripsi = AesCipher.doFinal(bytes);
            if(lebarBitmap == 0 || tinggiBitmap == 0) {
                bitmap = BitmapFactory.decodeByteArray(hasilDekripsi, 0, hasilDekripsi.length);
            }else{
                bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeByteArray(hasilDekripsi, 0, hasilDekripsi.length), lebarBitmap, tinggiBitmap);
            }
        } catch (IOException | BadPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap hasilDeskripsi) {
        if(lebarBitmap == 0 || tinggiBitmap == 0) {
            dialog.dismiss();
        }
        listenerDecrypt.onSelesaiDecrypt(hasilDeskripsi);
    }

    public void setListenerDecrypt(ListenerDecrypt listenerDecrypts){
        if(listenerDecrypt == null){
            this.listenerDecrypt = listenerDecrypts;
        }
    }

    public interface ListenerDecrypt{
        void onSelesaiDecrypt(Bitmap hasilDeskripsi);
    }
}
