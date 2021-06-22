import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProjetServer {
	ServerSocket server;
	boolean error;
	public ProjetServer(){
		error = false;
		System.out.println("Server starting ...");
		try {
			server = new ServerSocket(8000);
		} catch (IOException e) {
			System.out.println("Server failed to start !!");
			server = null;
		}
	}
	void  listen(){
		System.out.println("Server is listening ...");
		while (true){
			error = false;
			try {
				Socket s = server.accept();
				System.out.println("a client has connected ...");
				auth(s);
				if (error){
					continue;
				}
				handleOperation(s);

			} catch (SocketException e){
				System.out.println("Client has disconnected !! ...");
				error = true;

			} catch (IOException e ) {
				System.out.println("Server failed to listen !! ...");
				error = true;

			}
		}
	}

	void auth(Socket s){
		InputStreamReader input = null;
		AuthBase base = new AuthBase();
		String login = null, pass = null ;
		try {
				input = new InputStreamReader(s.getInputStream());
				BufferedReader br = new BufferedReader(input);
					while (true ) {
						if(!s.isConnected()){
							break;
						}
						if(!base.isConnected()){
							PrintWriter pw = new PrintWriter(s.getOutputStream());
							pw.println("failed");
							pw.flush();
							break;
						}
							String argument = br.readLine();
							System.out.println(argument);
							String pattern = "login='(.*?)'&password='(.*?)'";
							Pattern r = Pattern.compile(pattern);
							Matcher m = r.matcher(argument);
							if(m.find()) {
								login = m.group(1);
								pass = m.group(2);
							}
							PrintWriter pw = new PrintWriter(s.getOutputStream());
							int b = base.getUserData(login, pass);
							if (b == 1) {
								pw.println("true");
								pw.flush();
								break;
							}else if(b == 0){
								pw.println("false");
								pw.flush();
							} else {
								pw.println("failed");
								pw.flush();
							}

					}
		} catch (IOException e) {
			System.out.println("connection reset !!");
			error = true;
				}


	}

	String getValue(Socket s){
		InputStreamReader input = null;
		try {
			input = new InputStreamReader(s.getInputStream());
			BufferedReader br = new BufferedReader(input);
			return br.readLine().trim();
		} catch (IOException e){
			error = true;
		}
		return null;
	}

	void putValue(Socket s, String txt) throws IOException{

		PrintWriter pw = new PrintWriter(s.getOutputStream());
		pw.println(txt);
		pw.flush();

	}

	void handleOperation(Socket s){
		while (true){
			String value = getValue(s);
			if(value == null){
				break;
			}
			System.out.println(value);
			Pattern p = Pattern.compile("op='(.*?)'");
			Matcher m = p.matcher(value);
			if(m.find()){
			switch (m.group(1)) {
				case "insert" -> insertOperation(value, s);
				case "update" -> updateOperation(value, s);
				case "delete" -> deleteOperation(value, s);
				case "show" -> showOperation(value, s);
				default -> {
					System.out.println("error :v");
					System.out.println(value);
				}
			}}
		}
	}
	void showOperation(String value, Socket s){
		System.out.println("show");
		AuthBase base = new AuthBase();
		String user = base.getAllUsers();
		try {
			putValue(s, user);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	void deleteOperation(String value, Socket s){
		int b;
		System.out.println("Delete");
		String pattern = ".*?id='(.*?)'";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(value);
		if(m.find()){
			String id = m.group(1);
			AuthBase base = new AuthBase();
			b = base.deleteUser( id);
			try {
				putValue(s, String.valueOf(b));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	void updateOperation(String value, Socket s){
		System.out.println("update");
		System.out.println(value);
		int b = 0, c = 0;

		String pattern = ".*?id='(.*?)'&login='(.*?)'&log='(.)'&password='(.*?)'&pass='(.)'";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(value);
		if(m.find()) {
			String id = m.group(1);
			String login = m.group(2);
			String log = m.group(3);
			String password = m.group(4);
			String pass = m.group(5);
			if(log.equals("y")){
				AuthBase base = new AuthBase();
				b = base.updateUser("login", login, id);
			}
			if(pass.equals("y")){
				AuthBase base = new AuthBase();
				if(password.isEmpty()){
					password = null;
				}
				c = base.updateUser("password", password, id);
			}
		}
		try {
			putValue(s, String.valueOf(b+c));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void insertOperation(String value, Socket s){
		System.out.println("insert");
		if(!value.equals("cancel")){
			String pattern = ".*?login='(.*?)'&password='(.*?)'";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(value);
			if (m.find()){

				AuthBase base = new AuthBase();
				String log = m.group(1);
				String pass = m.group(2);
				System.out.println(log);
				int b = base.insertUser(log, pass);
				try {
					putValue(s, String.valueOf(b));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	public static void main(String[] args) {

		new ProjetServer().listen();
	}
}
