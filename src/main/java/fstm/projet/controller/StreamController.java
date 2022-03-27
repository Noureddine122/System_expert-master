package fstm.projet.controller;

import fstm.projet.model.dao.DAORegion;
import org.json.JSONException;

import java.io.IOException;

public class StreamController extends Thread {

    @Override
    public void run() {
        try {
            new DAORegion().updateDatabase();
            System.out.println("Update done, let's sleep");
            sleep(86400000);// 24 heurs
        } catch (JSONException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
