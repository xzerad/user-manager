import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Auth extends JFrame implements ActionListener {
	JButton btn;
	JPasswordField passt;
	JTextField logint;
	Socket sock;
	boolean log, error;
	public Auth(Socket s){
		sock = s;
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		JPanel pan1 = new JPanel();
		pan1.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel pan2 = new JPanel();
		pan2.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel pan3 = new JPanel();
		pan3.setLayout(new FlowLayout(FlowLayout.CENTER));

		pan1.setBorder(new EmptyBorder(30, 50, 0, 50));
		pan2.setBorder(new EmptyBorder(0, 50, 10, 50));

		JLabel login = new JLabel("user");
		login.setPreferredSize(new Dimension(56, 18));
		pan1.add(login);
		logint = new JTextField(10);
		pan1.add(logint);
		JLabel pass = new JLabel("password");
		pan2.add(pass);
		passt = new JPasswordField(10);
		pan2.add(passt);
		btn = new JButton("Login");
		btn.addActionListener(this);
		btn.setPreferredSize(new Dimension(100, 30));
		pan3.add(btn);
		add(pan1);
		add(pan2);
		add(pan3);
		setTitle("Authentication");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(400, 200);
		setVisible(true);
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btn){
			InputStreamReader input = null;
			try {
				if(sock != null){
					PrintWriter pw = new PrintWriter(sock.getOutputStream());
					String s = String.format("login='%s'&password='%s'", logint.getText(), new String(passt.getPassword()));

					pw.println(s);
					pw.flush();


					input = new InputStreamReader(sock.getInputStream());
					BufferedReader br = new BufferedReader(input);
					String ligne = br.readLine();
					System.out.println(ligne);
					if(ligne.equals("true")) {
						JOptionPane.showMessageDialog(getContentPane(), "you are logged in ");
						log = true;
					}else if(ligne.equals("false")){
						JOptionPane.showMessageDialog(getContentPane(), "invalid login or password", "Error", JOptionPane.ERROR_MESSAGE);
					}else{
						JOptionPane.showMessageDialog(getContentPane(), "unable to connect to database", "Error", JOptionPane.ERROR_MESSAGE);
						error = true;
					}
				}else{
					JOptionPane.showMessageDialog(getContentPane(), "can't connect to server", "Error", JOptionPane.ERROR_MESSAGE);

				}

			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	boolean is_auth(){
		return log;
	}
	boolean hasError(){
		return error;
	}
	void destroy (){
		this.dispose();
	}

	String getLogin(){
		return logint.getText();
	}

}





























