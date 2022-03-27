package fstm.projet.controller;

import fstm.projet.model.bo.*;
import fstm.projet.model.dao.DAOClient;
import fstm.projet.model.dao.DAORegion;
import fstm.projet.model.dao.DAOSymptom;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

/**
 * @author noureddine
 */
public class SocketController implements Runnable {
    private Socket socket;
    private InputStream is;
    private ObjectInputStream ois;
    private OutputStream os;
    private ObjectOutputStream oos;

    public SocketController(Socket socket) throws IOException {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            is = socket.getInputStream();
            ois = new ObjectInputStream(is);
            Object objInput =ois.readObject();
            if( objInput instanceof Client socke){
                insertionClient(socke);
            }else if(objInput instanceof SocketInscription socke){
                verifieClient(socke);
            }else if(objInput instanceof Socketinter socke){
                diagoniser(socke);
            }else if(objInput instanceof SocketUpdate socke){
                ClientUpdate(socke);
            }else if(objInput instanceof AskSymptoms){
               sendSymptoms();
            }else if(objInput instanceof AskRegions){
                sendRegions();
            }

            } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();

    } finally{
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void insertionClient(Client socke) {
        System.out.println("Receiving socket inscription");
        DAOClient daoClient = new DAOClient();
        daoClient.insert(socke);
        System.out.println("Bien inserer");
    }

    private void sendRegions() throws IOException {
        DAORegion daoSymptom=new DAORegion();
        os = socket.getOutputStream();
        oos = new ObjectOutputStream(os);
        System.out.println("Sending values to the ServerSocket");
        oos.writeObject(daoSymptom.retreiveR());
    }

    private void sendSymptoms() throws IOException {
        DAOSymptom daoSymptom=new DAOSymptom();
        os = socket.getOutputStream();
        oos = new ObjectOutputStream(os);
        oos.writeObject(daoSymptom.retreive());
    }

    private void ClientUpdate(SocketUpdate socke) {
        DAOClient daoClient=new DAOClient();
        daoClient.updateClient(socke.getClient(),socke.getTemp(),socke.getReg());
        System.out.println("Bien appdate");
    }

    private void diagoniser(Socketinter socke) throws IOException {
        System.out.println(socke.Mysymtoms.toString());
        Docteur doc =new Docteur(1,"achiban","nourddine");
        new DAOClient().updateMaladie(socke.MyClient.getMaladies(), socke.MyClient.getCmptCompte().getEmail());

        Diagnostic diag=new Diagnostic(1,socke.MyClient,socke.Mysymtoms,doc);

        System.out.println(diag);
        DroolsTest d= new DroolsTest();

        //Serialization
        double resu=d.Start_Rules(diag);
        String message;
        diag.set_possi_presence(resu);
        new DAOClient().updateDiagnostique(diag,socke.MyClient.getCmptCompte().getEmail() );
        os = socket.getOutputStream();
        oos = new ObjectOutputStream(os);
        System.out.println("Sending values to the ServerSocket");
        if(d.isEnvoy(doc)) {
            message ="possibilite de presence : " + resu*100 + " %100 vous etes une cas d'urgence !! vos informations ont envoy�es aux autorit�s compt�tentes" +"Diagnostic";
        } else {
            message = "possibilite de presence : "+ resu*100 + " %100";
        }
        oos.writeObject(message);
    }

    private void verifieClient(SocketInscription socke) throws IOException {
        System.out.println(socke.getEmail());
        DAOClient daoClient=new DAOClient();
        Client b =  daoClient.Authentification(socke.getEmail(), socke.getPassword());
        os = socket.getOutputStream();
        oos = new ObjectOutputStream(os);
        System.out.println("Sending values to the ServerSocket");
        oos.writeObject(b);
    }
}
