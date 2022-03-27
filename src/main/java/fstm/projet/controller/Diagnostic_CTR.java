package fstm.projet.controller;

import fstm.projet.model.bo.*;
import fstm.projet.view.Acueil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

public class Diagnostic_CTR {
	
	private static final String host = "172.17.36.48";
	private static final int port = 7000;
	public Diagnostic_CTR()
	{
		
		 
	}
	public static void charger_dia(Client c) throws IOException, ClassNotFoundException {
		
		Acueil fram1= new Acueil(c);
		fram1.setVisible(true);
	}
public static void diagoniser(Client c,Vector<Symptoms> sys,Vector<Maladie_chronique> mal) throws IOException, ClassNotFoundException {
		c.setMaladies(mal);
		Socketinter socketinter = new Socketinter(sys,c);
		Socket socket = new Socket(host, port);
		System.out.println("Connected.................");

		OutputStream os = socket.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(socketinter);

		InputStream is = socket.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(is);
		String obj1=(String)ois.readObject();

		JOptionPane.showMessageDialog(null, obj1, "Diagnostic", JOptionPane.INFORMATION_MESSAGE);
		/*Docteur doc=new Docteur(1,"alami","ahmed");
 	
	 DroolsTest	d= new DroolsTest();
	 c.setMaladies(mal);
	 new DAOClient().updateMaladie(mal, c.getCmptCompte().getEmail());
	 
	 Diagnostic diag = new Diagnostic(1,c,sys,doc);
	
	 double resu=d.Start_Rules(diag);
	 diag.set_possi_presence(resu);
	 Acueil.Resul.setText(resu*100 + " %100");
	 new DAODiagnostic().insert(diag);
	 new DAOClient().updateDiagnostique(diag,c.getCmptCompte().getEmail() );
	 
		if(d.isEnvoy(doc)) {
			JOptionPane.showMessageDialog(null, "possibilite de presence : "
			+ resu*100 + " %100 vous etes une cas d'urgence !! vos informations ont envoy�es aux autorit�s compt�tentes",
			"Diagnostic", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog( null,"possibilite de presence : "
					+ resu*100 + " %100","Diagnostic", JOptionPane.INFORMATION_MESSAGE);
		}*/
	 
	 
	 
}

public static ArrayList<Symptoms> afficheSy() throws IOException, ClassNotFoundException {
	Socket socket = new Socket(host, port);
	System.out.println("Connected.................");

	Symptoms n = new Symptoms();
	OutputStream os = socket.getOutputStream();
	ObjectOutputStream oos = new ObjectOutputStream(os);
	oos.writeObject(n);

	InputStream is = socket.getInputStream();
	ObjectInputStream ois = new ObjectInputStream(is);
	return (ArrayList<Symptoms>)ois.readObject();
	/*//DAOSymptom daoSymptom=new DAOSymptom();
	return daoSymptom.retreive();*/
}
public static ArrayList<Region> afficheRe() throws IOException, ClassNotFoundException {
	Region obj2 = new Region();
	Socket socket = new Socket(host, port);
	System.out.println("Connected.................");

	OutputStream os = socket.getOutputStream();
	ObjectOutputStream oos = new ObjectOutputStream(os);
	oos.writeObject(obj2);

	InputStream is = socket.getInputStream();
	ObjectInputStream ois = new ObjectInputStream(is);
	return (ArrayList<Region>)ois.readObject();
	/*DAORegion deDaoRegion=new DAORegion();
	return deDaoRegion.retreiveR();*/
}
public static void insertClient(String nom,String prenom,Boolean sexe,Calendar date,String email,String password) throws IOException, ClassNotFoundException {
	Compte cmpCompte=new Compte(email, password);
	Client cli=new Client(nom, prenom, sexe, date, cmpCompte);
	Socket socket = new Socket(host, port);
	System.out.println("Connected.................");

	OutputStream os = socket.getOutputStream();
	ObjectOutputStream oos = new ObjectOutputStream(os);

	oos.writeObject(cli);

	/*DAOClient daoClient=new DAOClient();
	daoClient.insert(cli);*/
}
public static void updateClient(Client c,double temp,Region reg) throws IOException, ClassNotFoundException {
	SocketUpdate socketUpdate = new SocketUpdate(c,temp,reg);
	Socket socket = new Socket(host, port);
	System.out.println("Connected.................");

	OutputStream os = socket.getOutputStream();
	ObjectOutputStream oos = new ObjectOutputStream(os);
	oos.writeObject(socketUpdate);
		/*DAOClient daoClient=new DAOClient();
	daoClient.updateClient(c, temp, reg);*/
	charger_dia(c);
}
public static Client authClient (String email,String passString) throws IOException, ClassNotFoundException {
	Socket socket = new Socket(host, port);
	System.out.println("Connected.................");

	OutputStream os = socket.getOutputStream();
	ObjectOutputStream oos = new ObjectOutputStream(os);
	SocketInscription n = new SocketInscription(email,passString);
	oos.writeObject(n);

	InputStream is = socket.getInputStream();
	ObjectInputStream ois = new ObjectInputStream(is);
	return (Client)ois.readObject();
		/*DAOClient daoClient=new DAOClient();
return daoClient.Authentification(email, passString);*/
	
}

public static void rempliTable(DefaultTableModel model,Client c) {
	for(Diagnostic diag:c.getDiagnostics()) {
		model.addRow(new String[] {diag.getDate().toString(),""+diag.get_possi_presence()});
		System.out.println(diag);
	}
}

}
