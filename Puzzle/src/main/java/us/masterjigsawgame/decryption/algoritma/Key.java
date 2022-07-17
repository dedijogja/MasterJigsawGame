package us.masterjigsawgame.decryption.algoritma;

import java.util.ArrayList;


class Key {
    public String key;
    private String namaClass;

    Key(String namaClass) {
        this.namaClass = namaClass;
    }

    public String getKey() {
        if(key==null){
            throw new IllegalArgumentException("<"+namaClass+"> Key harus di set terlebih dahulu sebelum menjalankan fungsi getKey()");
        }
        char[] charKunci = key.toCharArray();
        ArrayList<Character> arrayListCharKunciFix = new ArrayList<>();
        for (char aCharKunci : charKunci) {
            if (cekKarakter(aCharKunci)) {
                if (aCharKunci == '0') {
                    arrayListCharKunciFix.add('9');
                } else {
                    arrayListCharKunciFix.add(aCharKunci);
                }
            } else {
                String nilaiInt = String.valueOf((int) aCharKunci);
                char[] charNilaiInt = nilaiInt.toCharArray();
                for (char aCharNilaiInt : charNilaiInt) {
                    arrayListCharKunciFix.add(aCharNilaiInt);
                }
            }
        }
        char[] charKunciFix = new char[arrayListCharKunciFix.size()];
        for(int i=0; i<arrayListCharKunciFix.size(); i++){
            if(arrayListCharKunciFix.get(i) == '0'){
                charKunciFix[i] = '9';
            }else{
                charKunciFix[i] = arrayListCharKunciFix.get(i);
            }
        }
        return String.valueOf(charKunciFix);
    }

    private boolean cekKarakter(char karakter){
        try {
            Integer.parseInt(String.valueOf(karakter));
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public void setKey(String key) {
        if(key == null || "".equals(key)){
            throw new IllegalArgumentException("<"+namaClass+"> Key tidak boleh di set ke null/kosong");
        }else if(key.toCharArray().length<4){
            throw new IllegalArgumentException("<"+namaClass+"> Panjang key tidak boleh kurang dari 4");
        }else if(key.toCharArray().length>16){
            throw new IllegalArgumentException("<"+namaClass+"> Panjang key tidak boleh lebih dari 16");
        }else{
            this.key = key;
        }
    }

}
