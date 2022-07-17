package us.masterjigsawgame.decryption.algoritma;


import us.masterjigsawgame.decryption.dataset.DataSet;
import us.masterjigsawgame.decryption.intrefaces.Deskriptor;

public class DeskriptorAlgoritma implements Deskriptor {

    private String textUntukDiDeskripsi;
    private Key key = new Key(this.getClass().getSimpleName());
    private DataSet dataSet = new DataSet();

    public void setKey(String key) {
        this.key.setKey(key);
    }

    public void setTextUntukDiDeskripsi(String textUntukDiDeskripsi) {
        this.textUntukDiDeskripsi = textUntukDiDeskripsi;
    }

    @Override
    public String dapatkanTextAsli() {
        char[] charTextAsli = textUntukDiDeskripsi.toCharArray();
        char[] charKey = key.getKey().toCharArray();

        int panjangCharTextAsli = charTextAsli.length;
        int panjangCharKey = charKey.length;

        char[] charTextHasilEnskripsi = new char[panjangCharTextAsli];
        int indexKey = 0;
        for(int indexCharAsli=0; indexCharAsli<panjangCharTextAsli; indexCharAsli++){
            int index = dataSet.getIndex(charTextAsli[indexCharAsli]);
            if(indexKey<panjangCharKey){
                if(index == 0){
                    index = dataSet.dataSet.length;
                }else if(index- Integer.parseInt(String.valueOf(charKey[indexKey])) <
                        0){
                    index = dataSet.dataSet.length+dataSet.getIndex(charTextAsli[indexCharAsli]);
                }
                charTextHasilEnskripsi[indexCharAsli] = dataSet.getKarakter(index
                        - Integer.parseInt(String.valueOf(charKey[indexKey])));
                indexKey ++;
            }else{
                indexKey=0;
                if(index == 0){
                    index = dataSet.dataSet.length;
                }else if(index- Integer.parseInt(String.valueOf(charKey[indexKey])) <
                        0){
                    index = dataSet.dataSet.length+dataSet.getIndex(charTextAsli[indexCharAsli]);

                }
                charTextHasilEnskripsi[indexCharAsli] = dataSet.getKarakter(index
                        - Integer.parseInt(String.valueOf(charKey[indexKey])));
            }
        }
        return String.valueOf(charTextHasilEnskripsi);
    }
}
