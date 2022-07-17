package us.masterjigsawgame.decryption.dataset;


public class DataSet {
    public char[] dataSet = {
            '`',
            '~',
            '1',
            '!',
            '2',
            '@',
            '3',
            '#',
            '4',
            '$',
            '5',
            '%',
            '6',
            '^',
            '7',
            '&',
            '8',
            '*',
            '9',
            '(',
            '0',
            ')',
            '-',
            '_',
            '=',
            '+',
            'q',
            'Q',
            'w',
            'W',
            'e',
            'E',
            'r',
            'R',
            't',
            'T',
            'y',
            'Y',
            'u',
            'U',
            'i',
            'I',
            'o',
            'O',
            'p',
            'P',
            '[',
            '{',
            ']',
            '}',
            '\\',
            '|',
            'a',
            'A',
            's',
            'S',
            'd',
            'D',
            'f',
            'F',
            'g',
            'G',
            'h',
            'H',
            'j',
            'J',
            'k',
            'K',
            'l',
            'L',
            ';',
            ':',
            '\'',
            '\"',
            'z',
            'Z',
            'x',
            'X',
            'c',
            'C',
            'v',
            'V',
            'b',
            'B',
            'n',
            'N',
            'm',
            'M',
            ',',
            '<',
            '.',
            '>',
            '/',
            '?',
            ' ',
           // '\n'   //Hanya digunakan untuk selain kode iklan
    };

    public int getIndex(char karakter){
        for(int i=0; i<dataSet.length; i++){
            if(dataSet[i] == karakter){
                return i;
            }
        }
        throw new IllegalArgumentException("Karakter " +karakter+ " tidak ada pada data set");
    }

    public char getKarakter(int index){
        for(int i=0; i<dataSet.length; i++){
            if(i == index){
                return dataSet[i];
            }
        }
        throw new IllegalArgumentException("Index "+index+ " tidak ada pada data set");
    }
}
