package us.masterjigsawgame.asyncronus;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
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

public class SingleGameDecryption extends AsyncTask<String, Void, byte[]>{

    private Context context;
    private ListenerDecrypt listenerDecrypt;
    private ProgressDialog dialog;

    public SingleGameDecryption(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, "Loading",
                "Please wait ...", true);
        dialog.show();
    }

    @Override
    protected byte[] doInBackground(String... strings) {
        SecretKey secretKey =  new SecretKeySpec(Base64.decode(new Komunikator(context).getKeyAssets(), Base64.DEFAULT),
                0, Base64.decode(new Komunikator(context).getKeyAssets(), Base64.DEFAULT).length, "AES");
        byte[] bitmap = null;
        try {
            InputStream inputStream = context.getAssets().open(Constant.FOLDER_KONTEN + "/" + strings[0]);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();

            Cipher AesCipher = Cipher.getInstance("AES");
            AesCipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] hasilDekripsi = AesCipher.doFinal(bytes);

//            Drawable image = new BitmapDrawable(context.getResources(),BitmapFactory.decodeByteArray(hasilDekripsi, 0, hasilDekripsi.length));
//            bitmap = drawableToBitmap(image);
//
//
//
//            bitmap = BitmapFactory.decodeByteArray(hasilDekripsi, 0, hasilDekripsi.length);
//            bitmap = Bitmap.createScaledBitmap(bitmap, 400,
//                    800, false);

            bitmap = hasilDekripsi;

            //bitmap = BitmapFactory.decodeByteArray(hasilDekripsi, 0, hasilDekripsi.length);
            //bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeByteArray(hasilDekripsi, 0, hasilDekripsi.length), 400, 300);

        } catch (IOException | BadPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(byte[] hasilDeskripsi) {
        dialog.dismiss();
        listenerDecrypt.onSelesaiDecrypt(hasilDeskripsi);
    }

    public void setListenerDecrypt(ListenerDecrypt listenerDecrypts){
        if(listenerDecrypt == null){
            this.listenerDecrypt = listenerDecrypts;
        }
    }

    public interface ListenerDecrypt{
        void onSelesaiDecrypt(byte[] hasilDeskripsi);
    }

}
