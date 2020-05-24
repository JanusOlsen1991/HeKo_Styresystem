package controller;

import model.RessourceLocator;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Filplaceringer {

    private final String FILE_NAME = "Filstier.txt";

    private static Filplaceringer INSTANCE = new Filplaceringer();

    public static Filplaceringer getInstance() {
        return INSTANCE;
    }

    private RessourceLocator ressources = new RessourceLocator();

    private Filplaceringer() {
        read();
    }

    private void write() {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME))) {

            for(Map.Entry<String, String> entry : ressources.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                bufferedWriter.write(key+","+value+"\n");
            }

        } catch (IOException ex1) {

        }
    }

    private void read() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_NAME))){
            String s = bufferedReader.readLine();
            String[] readInput = s.split(",");
            ressources.put(readInput[0], readInput[1]);

            while(s != null || s.trim().isEmpty()) {
                s = bufferedReader.readLine();
                readInput = s.split(",");
                ressources.put(readInput[0], readInput[1]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public String getPath(String key) {
        return ressources.get(key);
    }
    public void updateFilePath(String key, String value) {
        ressources.put(key, value);
        write();
    }

}
