package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Filplaceringer {
    private static Filplaceringer INSTANCE = new Filplaceringer();

    public static Filplaceringer getInstance() {
        return INSTANCE;
    }

    private String dispensationGives;

    private Filplaceringer() {
        if (INSTANCE != null) {

        }
    }

    public void start(String filename) {


        String filepath = null;


        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filename);
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            filepath = bufferedReader.readLine();
            //TODO sæt teksterne hernedaf
            //text.setText(filepath);

            bufferedReader.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
