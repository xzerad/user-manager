import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class ProjetClient extends JFrame {

	Socket client;
	boolean error;
	String login;
	public ProjetClient(){
		connect();
		if(client != null) {
			auth();
			if(! error) {
				creatFrame();
			}
		}else {
				JOptionPane.showMessageDialog(getContentPane(), "can't connect to server", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}

	private void creatFrame(){
		if(!login.equals("admin")){
			JOptionPane.showMessageDialog(getContentPane(), "you can only log in as admin !! " , "Error", JOptionPane.ERROR_MESSAGE);
		}else {
		new DBFrame(login, client);
	}}

	private void connect(){
		System.out.println("Demarrage client ...");
		try {
			client = new Socket("localhost", 8000);
			System.out.println("i'm connected");

		} catch (IOException e) {
			System.out.println("failed to connect to server!! ...");
			client = null;
		}
	}
	private void auth(){

		Auth a = new Auth(client);
		while (! a.is_auth() && !error){
			error = a.hasError();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(! error){
			login = a.getLogin();
		}
		a.destroy();
	}

	public static void main(String[] args) {
		new ProjetClient();
	}
}
