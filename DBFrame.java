import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBFrame  extends JFrame {
	JButton insert_btn, update_btn, delete_btn, show_btn;
	Socket s;
	public DBFrame(String login, Socket client){
		s = client;

		JLabel log_inf = new JLabel(" Logged in as :");
		JLabel log = new JLabel(login);
		log.setForeground(Color.RED);
		setLayout(new BorderLayout());
		JPanel info = new JPanel();
		info.setLayout(new FlowLayout(FlowLayout.LEFT));
		info.add(log_inf);
		info.add(log);
		add(info, BorderLayout.SOUTH);
		JPanel p1 = new JPanel();
		p1.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		p1.setBorder(new EmptyBorder(30, 10, 10, 10));
		JPanel p2 = new JPanel();
		p1.setLayout(new FlowLayout());
		JPanel p3 = new JPanel();
		p1.setLayout(new FlowLayout());
		JPanel p4 = new JPanel();
		p1.setLayout(new FlowLayout());

		insert_btn = new JButton("Insert User");
		insert_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("insert");
				new InsertUser();
			}
		});
		update_btn = new JButton("Update User");
		update_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("update");
				new UpdateUser();
			}
		});
		delete_btn = new JButton("Delete User");
		delete_btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("delete");

				new DeleteUser();
			}
		});
		show_btn = new JButton("Show Users");
		show_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("show");
				new ShowUser();
			}
		});
		p2.add(insert_btn);
		p3.add(update_btn);
		p3.add(delete_btn);
		p4.add(show_btn);
		p1.add(p2);
		p1.add(p3);
		p1.add(p4);
		add(p1, BorderLayout.CENTER);
		setTitle("db");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(270, 250);
		setVisible(true);
		setLocationRelativeTo(null);
	}
//
//	public static void main(String[] args) {
//		new DBFrame("admin", null);
//	}
String getValue(Socket s){
	InputStreamReader input ;
	try {
		input = new InputStreamReader(s.getInputStream());
		BufferedReader br = new BufferedReader(input);
		return br.readLine().trim();
	} catch (IOException e) {
		e.printStackTrace();
	}
	return null;
}

void putValue(Socket s, String txt) throws IOException{

		PrintWriter pw = new PrintWriter(s.getOutputStream());
		pw.println(txt);
		pw.flush();

}

	class InsertUser extends JFrame{
		JTextField logint;
		JTextField passt;
		JButton btn;
		InsertUser(){
			setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
			JPanel pan1 = new JPanel();
			pan1.setLayout(new FlowLayout(FlowLayout.CENTER));
			JPanel pan2 = new JPanel();
			pan2.setLayout(new FlowLayout(FlowLayout.CENTER));
			JPanel pan3 = new JPanel();
			pan3.setLayout(new FlowLayout(FlowLayout.CENTER));

			pan1.setBorder(new EmptyBorder(30, 0, 0, 50));
			pan2.setBorder(new EmptyBorder(0, 0, 10, 50));

			JLabel login = new JLabel("user");
			login.setPreferredSize(new Dimension(56, 18));
			pan1.add(login);
			logint = new JTextField(10);
			pan1.add(logint);
			JLabel pass = new JLabel("password");
			pan2.add(pass);
			passt = new JTextField(10);
			pan2.add(passt);
			btn = new JButton("Insert");
			btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String username = logint.getText();
					if(username.isEmpty()){
						JOptionPane.showMessageDialog(getContentPane(), "Login can't be empty !!", "Invalid login", JOptionPane.ERROR_MESSAGE);
					}else{
						try {
							String txt = String.format("op='insert'&login='%s'&password='%s'", logint.getText(), passt.getText());
							putValue(s, txt);
							String value = getValue(s);
							switch (Integer.parseInt(value) ){
								case 0: JOptionPane.showMessageDialog(getContentPane(), "user added successfully ");
										break;
								case 1:	JOptionPane.showMessageDialog(getContentPane(), "failed to add user ");
									break;
								default: JOptionPane.showMessageDialog(getContentPane(), "failed to add user !!");
									break;
							}
							System.out.println(value);
						} catch (IOException ioException) {
							System.out.println("failed");
						}
					System.out.println();
				}
				}
			});
			btn.setPreferredSize(new Dimension(100, 30));
			pan3.add(btn);
			add(pan1);
			add(pan2);
			add(pan3);
			setTitle("db: Insert User");
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			addWindowListener(new WindowClose(this));
			setSize(270, 250);
			setVisible(true);
			setLocationRelativeTo(insert_btn);

		}
	}
	class UpdateUser extends JFrame{
		String log_enabled = "n", pass_enabled = "n";

		UpdateUser(){
				setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
//				********* id
				JPanel id_pan = new JPanel();
				id_pan.setLayout(new FlowLayout());
				JLabel id = new JLabel("id");
				id.setBorder(new EmptyBorder(0, 0, 0, 67));
				id_pan.add(id);
				id_pan.setBorder(new EmptyBorder(30, 0, 0, 0));
				JTextField id_t = new JTextField(10);
				id_pan.add(id_t);
				add(id_pan);
//				****** login
				JPanel login_pan = new JPanel();
				login_pan.setLayout(new FlowLayout());
				JTextField login_t = new JTextField(10);
				login_t.setEnabled(false);
				JCheckBox login = new JCheckBox("login");
				login.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(login.isSelected()) {
							login_t.setEnabled(true);
							log_enabled = "y";
						}else{
							login_t.setEnabled(false);
							log_enabled = "n";
						}
					}
				});
				login.setBorder(new EmptyBorder(0, 0, 0, 35));
				login_pan.add(login);
				login_pan.add(login_t);
				add(login_pan);
//				****** pass
				JPanel pass_pan = new JPanel();
				login_pan.setLayout(new FlowLayout());
				JTextField pass_t = new JTextField(10);
				pass_t.setEnabled(false);
				JCheckBox pass = new JCheckBox("password");
				pass.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(pass.isSelected()) {
							pass_t.setEnabled(true);
							pass_enabled = "y";
						}else{
							pass_t.setEnabled(false);
							pass_enabled = "n";
						}
					}
				});
				pass_pan.add(pass);
				pass_pan.add(pass_t);
				add(pass_pan);
//				****** btn
				JPanel btn_pan = new JPanel();
				btn_pan.setLayout(new FlowLayout());
				JButton btn_update = new JButton("update");
				btn_update.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String idText = id_t.getText();
						if(idText.isEmpty()){
							JOptionPane.showMessageDialog(getContentPane(), "id can't be empty !!", "Invalid login", JOptionPane.ERROR_MESSAGE);
						}else{
							try {
								putValue(s, String.format("op='update'&id='%s'&login='%s'&log='%s'&password='%s'&pass='%s'", id_t.getText(), login_t.getText(),log_enabled, pass_t.getText(), pass_enabled));
								String value = getValue(s);
								System.out.println(value);
								if(Integer.parseInt(value) == 0){
									JOptionPane.showMessageDialog(getContentPane(), "user's data updated !!");
								}else{
									JOptionPane.showMessageDialog(getContentPane(), "failed to update user's data", "Error", JOptionPane.ERROR_MESSAGE);

								}
							} catch (IOException ioException) {
								ioException.printStackTrace();
							}

						}
					}
				});
				btn_pan.add(btn_update);
				add(btn_pan);
				setTitle("db: Update User");
				setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				addWindowListener(new WindowClose(this));
				setSize(270, 250);
				setVisible(true);
				setLocationRelativeTo(update_btn);
			}
	}
	class DeleteUser extends JFrame{
		DeleteUser(){

			setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
			JPanel pan = new JPanel();
			pan.setBorder(new EmptyBorder(55, 0, 0, 0));
			JPanel b_pan = new JPanel();
			pan.setLayout(new FlowLayout(FlowLayout.CENTER));
			JLabel id = new JLabel("id");
			JTextField id_t = new JTextField(10);
			pan.add(id);
			pan.add(id_t);
			add(pan);
			JButton del_btn = new JButton("Delete");
			del_btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String idText = id_t.getText();
					if(idText.isEmpty()){
						JOptionPane.showMessageDialog(getContentPane(), "id can't be empty !!", "Invalid login", JOptionPane.ERROR_MESSAGE);
					}else{
						String txt = String.format("op='delete'&id='%s'", id_t.getText());
						try {
							putValue(s, txt);
							String value = getValue(s);
							if(Integer.parseInt(value) == 0){
									JOptionPane.showMessageDialog(getContentPane(), "user has been deleted !!");
								}else{
									JOptionPane.showMessageDialog(getContentPane(), "failed to to delete user", "Error", JOptionPane.ERROR_MESSAGE);

								}
						} catch (IOException ioException) {
							ioException.printStackTrace();
						}
					}
				}
			});
			b_pan.add(del_btn);
			b_pan.setBorder(new EmptyBorder(0, 0, 55, 0));
			add(b_pan);

			setTitle("db: Delete User");
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			addWindowListener(new WindowClose(this));
			setSize(270, 250);
			setVisible(true);
			setLocationRelativeTo(delete_btn);
		}

	}

	class ShowUser extends JFrame{
		ArrayList<ArrayList<String>> parseData(String s){
			ArrayList<ArrayList<String>> data = new ArrayList<>();
			Pattern p = Pattern.compile("number=(.*?)&");
			Matcher m = p.matcher(s);

			if(m.find()){
				s = s.substring(m.end());
				int l = Integer.parseInt(m.group(1));
				for(int i =0;i<l; i++){
					p = Pattern.compile("id='(.*?)'&login='(.*?)'&password='(.*?)'");
					m = p.matcher(s);
					if(m.find()) {
						ArrayList<String> tmp = new ArrayList<>();
						tmp.add(m.group(1));
						tmp.add(m.group(2));
						tmp.add(m.group(3));
						data.add(tmp);
						s = s.substring(m.end());

					}
				}
				return data;
			}
			return null;
		}
		ShowUser(){
			JTable t = new JTable();

			String txt = "op='show'";
			Table tbm;
			try {
				putValue(s, txt);
				String value = getValue(s);
				tbm = new Table(parseData(value));
				System.out.println(value);
			} catch (IOException ioException) {
				ioException.printStackTrace();
				tbm = new Table(null);

			}

			add(new JScrollPane(t));
			t.setModel(tbm);
			setTitle("db: Show User");
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setSize(270, 250);
			setVisible(true);
			setLocationRelativeTo(show_btn);
			revalidate();
		}

	}

	class WindowClose extends WindowAdapter{
		private final JFrame f;

		WindowClose(JFrame frame){
			f = frame;
		}
		@Override
		public void windowClosing(WindowEvent e) {
			super.windowClosing(e);
			if(JOptionPane.showConfirmDialog(f.getContentPane(), "Are you sure ?") == JOptionPane.OK_OPTION){
				f.setVisible(false);
				f.dispose();
				System.out.println("cancel");
			}
		}
	}

	class Table extends AbstractTableModel{
		ArrayList<ArrayList<String>> data;
		Table(ArrayList<ArrayList<String>> d){
			data = d;
		}
		@Override
		public int getRowCount() {
			if(data == null)
				return 0;
			return data.size();
		}

		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if(data == null)
				return null;
			return data.get(rowIndex).get(columnIndex);
		}
		public String getColumnName(int c) {
			String[] col = {"id", "login", "password"};
			return col[c];
		}
		}



}
