import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import java.sql.*;

public class AuthBase {
	Connection con;
	Statement st;
	boolean driver, connected;
	public AuthBase(){
		driver = connected = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connected to Driver");
			driver = true;
			String url = "jdbc:mysql://localhost/user";
			String user = "root";
			String pwd = "";
			try {
				con = DriverManager.getConnection(url, user, pwd);
				System.out.println("Connected to Data base");
				connected = true;
				try {
					st = con.createStatement();
				} catch (SQLException throwables) {
					System.out.println("can't Create statement ");

				}

			} catch (SQLException throwables ) {
				System.out.println("can't Connect to Data base ");
				connected = false;
			}
		} catch (ClassNotFoundException e) {
			System.out.println("can't Connect to Driver " );
			driver = false;
		}

	}

	public ResultSet getUser(String login , String password){
		if(connected){
			try {
				String req = "SELECT login, password FROM user where login = ? and password = ?";
				PreparedStatement ps = con.prepareStatement(req);
				ps.setString(1, login);
				ps.setString(2, password);
				return ps.executeQuery();
			} catch (SQLException throwables) {

//				throwables.printStackTrace();
			}
		}
		return null;
	}

	public int getUserData (String l , String p){
		if(connected){
			ResultSet res = getUser(l.trim(), p.trim());
			if(res != null){
				try {
					if(res.next()){
						return 1;
					}else{
						return 0;
					}
				} catch (SQLException throwables) {
					System.out.println("error");
					return 0;
				}
			}else {
				return 2;
			}
		}
		return 0;
	}

	String getAllUsers(){
		String result ="&";
		int ligne = 0;
		if(connected){
			try {
				String req = "SELECT * FROM user ";
				PreparedStatement ps = con.prepareStatement(req);
				ResultSet rs = ps.executeQuery();
				if(rs != null){
					while (rs.next()){
						ligne++;
						result += String.format("id='%s'&login='%s'&password='%s'", rs.getObject(1), rs.getObject(2), rs.getObject(3));

					}
					return "number="+String.valueOf(ligne)+ result;
					}

			} catch (SQLException throwables) {
				return "0";
			}

	}
		return "0";
	}

	int deleteUser(String id){
		if(connected){
			try {
				String req = "delete FROM user  where id = ?";
				PreparedStatement ps = con.prepareStatement(req);
				ps.setInt(1, Integer.parseInt(id));
				ps.executeUpdate();
			} catch (SQLException throwables) {
				throwables.printStackTrace();
				return 1;
			}
		}
		return 0;
	}

	int updateUser(String name, String value, String id ){
		if(connected){
			try {
				String req = "update user set "+name+" = ? where id = ?";
				PreparedStatement ps = con.prepareStatement(req);
				ps.setString(1, value);
				ps.setInt(2, Integer.parseInt(id));
				ps.executeUpdate();
			} catch (MySQLIntegrityConstraintViolationException throwables){
				return 1;
			} catch (SQLException throwables) {
				throwables.printStackTrace();
				return 2;
			}
		}
		return 0;
	}

	int insertUser(String login, String password){

		if(connected){
			try {
				String req = "insert into user(login, password) values (? ,  ?)";
				PreparedStatement ps = con.prepareStatement(req);
				if(login.isEmpty()){
					login = null;
				}
				if(password.isEmpty()){
					password = null;
				}
				ps.setString(1, login);
				ps.setString(2, password);
				ps.execute();

			} catch (MySQLIntegrityConstraintViolationException throwables){
				return 1;

			} catch (SQLException throwables) {
				throwables.printStackTrace();
				return 2;
			}
		}
		return 0;
	}

	public boolean isConnected(){
		boolean test = true;
			if(con == null) {
				test = false;
			}
		return connected&&test;
	}

	public static void main(String[] args) {
		int b = new AuthBase().insertUser("radwan", "dra");
		System.out.println(b);
	}
}
