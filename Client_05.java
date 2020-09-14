package Client;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Client extends JFrame implements ActionListener {

	// DB�������� �ڿ�
	private String url = "jdbc:sqlserver://211.177.16.20:1433;DatabaseName=STORAGE;";
	private String user = "sa";
	private String password = "0123";
	private Connection conn = null;// Ŀ�ؼ� ��ü
	private Statement stmt = null;// Statement ��ü
	private ResultSet rs = null;

	// Inventory_GUI ���� ���� ����
	private JFrame Inventory_GUI = new JFrame();
	private JPanel Inven_pane;
	private JTable inventory_tb;
	private JComboBox inven_cmb;
	private JButton add_btn = new JButton("���� ��ǰ �԰�");
	private JButton new_btn = new JButton("�� ��ǰ �԰�");
	private JButton delgoods_btn = new JButton("��ǰ ����");
	private Vector<String> inventory_col = new Vector<>();
	private Vector<Vector> inventory_list = new Vector<>();
	private DefaultTableModel inventory_md;
	private JButton inventory_close_btn = new JButton("�ݱ�");
	private JButton update_btn = new JButton("Ȯ��");

	// new_GUI ���� ���� ����
	private JFrame new_GUI = new JFrame();
	private JPanel new_pane;
	private JTextField name_tf;
	private JTextField number_tf;
	private JTextField count_tf;
	private JTextField value_tf;
	private JButton newok_btn = new JButton("Ȯ��");
	private JComboBox new_cmb;
	private JButton new_close_btn = new JButton("�ݱ�");

	// add_GUI ���� ���� ����
	private JFrame add_GUI = new JFrame();
	private JPanel add_pane;
	private JTextField number_tf2;
	private JTextField count_tf2;
	private JButton addok_btn = new JButton("Ȯ��");
	private JButton add_close_btn = new JButton("�ݱ�");

	// pos_GUI ���� ���� ����
	private JFrame pos_GUI = new JFrame();
	private Vector<String> pos_col = new Vector<>();// ���̸� �����ϴ� ����<String>�ǹ�?
	private Vector<Vector> pos_list = new Vector<>();// �� ������ ������ ������
	private DefaultTableModel pos_md;
	private JPanel pos_pane;
	private JTable pos_tb;
	private JTextField pos_tf;
	private JTextArea price_area = new JTextArea();
	private JButton delete_btn = new JButton("���� ��ǰ ����");
	private JButton pay_btn = new JButton("����");
	private JButton reset_btn = new JButton("���");
	private JButton input_btn = new JButton("��������");
	private JButton sales_btn = new JButton("���� ��Ȳ");
	private JButton inventory_btn = new JButton("��� ����");
	private JButton end_btn = new JButton("����");
	private JLabel pos_label = new JLabel();
	private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private JPanel panel_eye = new JPanel();
	private JPanel panel_lip = new JPanel();
	private JPanel panel_face = new JPanel();
	private JPanel panel_care = new JPanel();
	private JPanel panel_mask = new JPanel();
	private JPanel panel_clean = new JPanel();
	private JPanel panel_body = new JPanel();
	private JPanel panel_ac = new JPanel();
	private ArrayList<JButton> list_eye = new ArrayList<JButton>(); // ������ ��ǰ��ư ��������
	private ArrayList<JButton> list_lip = new ArrayList<JButton>(); // ������ ��ǰ��ư ��������
	private ArrayList<JButton> list_face = new ArrayList<JButton>(); // ������ ��ǰ��ư ��������
	private ArrayList<JButton> list_care = new ArrayList<JButton>(); // ������ ��ǰ��ư ��������
	private int eye = 1, lip = 1, face = 1, care = 1;// ���߿� ��ư ���� �Ҵ� ���� �� �ְ� �� ����
	private int e = 1, l = 1, f = 1, c = 1;// ��ư ��������� ��, ���� ��ǰ��ư�� �Ǿ��� ��

	// sales_GUI ���� ���� ����
	private JFrame sales_GUI = new JFrame();
	private JPanel sales_pane;
	private JTable salse_tb;
	private DefaultTableModel salse_md;
	private Vector<String> sales_col = new Vector<>();
	private Vector<Vector> sales_list = new Vector<>();
	private JTextArea sum_area = new JTextArea();
	private JButton close_btn = new JButton("�ݱ�");

	// login_GUI ���� ���� ����
	private JFrame login_GUI = new JFrame();
	private JPanel login_pane;
	private JLabel label;
	private JTextField id_tf;
	private JLabel label_1;
	private JTextField password_tf;
	private JButton login_btn = new JButton("�α���");

	// �� ���� ������
	private int id;// ���
	private String pass = "";// �н�����

	Client() {
		connectDB();
		inventory_init();
		new_init();
		add_init();
		pos_init();
		sales_init();
		login_init();
		start();
		start_goods();
		get_btnname();
	}

	private void connectDB() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");// JDBC����̹� �ε�
			System.out.println("����̹� ���� ����");
			conn = DriverManager.getConnection(url, user, password); // Ŀ�ؼ� ��ü ����

			stmt = conn.createStatement();
			if (conn != null) {
				System.out.println("�����ͺ��̽� ���� ����");
			} else {
				System.out.println("���� ����");
			}
		} catch (Exception e) {
			System.out.println("������ ���̽� ���� ���� : " + e.getMessage());
		}
	}

	public void setDB() {// str�� ������
		try {
			rs = stmt.executeQuery("select goods_nm , goods_cd, value from TB_storage");
			while (rs.next()) {
				String name = rs.getString("goods_nm");
				String code = rs.getString("goods_cd");
				int number = rs.getInt("value");
				System.out.printf("%s  %s %d \n", name, code, number);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void setTable_inven() {// ���������̺� set
		String query = "select goods_cd, goods_nm, value, cur_count from TB_storage";

		try {
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				inventory_md.addRow(new Object[] { rs.getString("goods_cd"), rs.getString("goods_nm"),
						rs.getInt("value"), rs.getInt("cur_count") });
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void setSelectTable_inven(int code) { // ���������̺� �����׸� set

		if (code != 0) {
			String query = "select goods_cd, goods_nm, value, cur_count from TB_storage where ret_cd=" + code;

			try {
				rs = stmt.executeQuery(query);

				while (rs.next()) {
					inventory_md.addRow(new Object[] { rs.getString("goods_cd"), rs.getString("goods_nm"),
							rs.getInt("value"), rs.getInt("cur_count") });
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	private int  get_retcode(String retrieval) {

		 int code = 0;//�з��ڵ� ���� ����

		String query = " select ret_cd from TB_retrieval where ret_nm='" + retrieval+"' ";//�з��ڵ� ���� ����
		System.out.printf("%s\n", query);

		try {
			rs = stmt.executeQuery(query);
			
			if (rs.next()) {// �з� �ڵ� ���� ���(10~80�� ���
				code = rs.getInt("ret_cd");// �з��ڵ�ޱ�
				
			}
			else {//��ü���� ������ ���
				code=0;
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("�з��ڵ� : "+code);
		return code;
		
	}
	
 	public void inventory_createTableModel() {
		inventory_col.add("ǰ��");
		inventory_col.add("��ǰ��");
		inventory_col.add("����");
		inventory_col.add("�������");
		inventory_md = new DefaultTableModel(inventory_col, 0);
		inventory_tb.setModel(inventory_md);
	}

	public void pos_createTableModel() {// JTable����
		pos_col.add("No");
		pos_col.add("��ǰ��");
		pos_col.add("����");
		pos_col.add("����");
		pos_md = new DefaultTableModel(pos_col, 0);
		pos_tb.setModel(pos_md);
	}

	public void sales_createTableModel() {
		sales_col.add("����");
		sales_col.add("��ǰ�ڵ�");
		sales_col.add("����");
		sales_col.add("����");
		sales_col.add("����ڻ��");
		sales_col.add("�Ǹ��Ͻ�");
		salse_md = new DefaultTableModel(sales_col, 0);
		salse_tb.setModel(salse_md);
	}

	private void inventory_init() {// ������GUI

		Inventory_GUI.setTitle("������");
		Inventory_GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Inventory_GUI.setBounds(100, 100, 632, 479);
		Inven_pane = new JPanel();
		Inven_pane.setBorder(new EmptyBorder(5, 5, 5, 5));
		Inventory_GUI.setContentPane(Inven_pane);
		Inven_pane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 103, 427, 329);
		Inven_pane.add(scrollPane);

		inventory_tb = new JTable();
		scrollPane.setViewportView(inventory_tb);
		inventory_createTableModel();
		inventory_tb.getTableHeader().setReorderingAllowed(false);
		inventory_tb.getTableHeader().setResizingAllowed(false);

		String cbmenu[] = { "��ü����", "����", "��", "���̽�", "��Ų�ɾ�", "��/����ũ", "Ŭ��¡", "�ٵ�/���", "ȭ���ǰ" };
		inven_cmb = new JComboBox(cbmenu);
		inven_cmb.setBounds(12, 55, 136, 38);
		Inven_pane.add(inven_cmb);

		add_btn.setBounds(451, 156, 154, 47);
		Inven_pane.add(add_btn);

		new_btn.setBounds(451, 99, 155, 47);
		Inven_pane.add(new_btn);

		delgoods_btn.setBounds(451, 213, 154, 47);
		Inven_pane.add(delgoods_btn);

		update_btn.setFont(new Font("����", Font.BOLD, 11));
		update_btn.setBounds(160, 55, 59, 38);
		Inven_pane.add(update_btn);

		inventory_close_btn.setBounds(451, 385, 154, 47);
		Inven_pane.add(inventory_close_btn);

		Inventory_GUI.setVisible(false);

	}

	private void new_init() {// ����ǰ�԰�GUI
		new_GUI.setTitle("�� ��ǰ �԰�");
		new_GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		new_GUI.setBounds(100, 100, 429, 350);
		new_pane = new JPanel();
		new_pane.setBorder(new EmptyBorder(5, 5, 5, 5));
		new_GUI.setContentPane(new_pane);
		new_pane.setLayout(null);

		name_tf = new JTextField();
		name_tf.setBounds(97, 54, 291, 33);
		new_pane.add(name_tf);
		name_tf.setColumns(10);

		JLabel label = new JLabel("��ǰ��");
		label.setFont(new Font("����", Font.BOLD, 14));
		label.setBounds(25, 53, 60, 35);
		new_pane.add(label);

		JLabel label_1 = new JLabel("ǰ��");
		label_1.setFont(new Font("����", Font.BOLD, 14));
		label_1.setBounds(25, 98, 60, 35);
		new_pane.add(label_1);

		number_tf = new JTextField();
		number_tf.setColumns(10);
		number_tf.setBounds(97, 99, 291, 33);
		new_pane.add(number_tf);

		JLabel label_2 = new JLabel("����");
		label_2.setFont(new Font("����", Font.BOLD, 14));
		label_2.setBounds(25, 144, 60, 35);
		new_pane.add(label_2);

		count_tf = new JTextField();
		count_tf.setColumns(10);
		count_tf.setBounds(97, 145, 291, 33);
		new_pane.add(count_tf);

		JLabel label_3 = new JLabel("����");
		label_3.setFont(new Font("����", Font.BOLD, 14));
		label_3.setBounds(25, 195, 60, 35);
		new_pane.add(label_3);

		value_tf = new JTextField();
		value_tf.setColumns(10);
		value_tf.setBounds(97, 196, 291, 33);
		new_pane.add(value_tf);

		newok_btn.setFont(new Font("����", Font.BOLD, 14));
		newok_btn.setBounds(73, 250, 135, 43);
		new_pane.add(newok_btn);

		new_close_btn.setBounds(213, 250, 135, 43);
		new_pane.add(new_close_btn);

		String cbmenu[] = { "����", "��", "���̽�", "��Ų�ɾ�", "��/����ũ", "Ŭ��¡", "�ٵ�/���", "ȭ���ǰ" };
		new_cmb = new JComboBox(cbmenu);
		new_cmb.setBounds(253, 10, 135, 33);
		new_pane.add(new_cmb);

		new_GUI.setVisible(false);
	}

	private void add_init() {// ������ǰ�԰�GUI
		add_GUI.setTitle("���� ��ǰ �԰�");
		add_GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add_GUI.setBounds(100, 100, 350, 230);
		add_pane = new JPanel();
		add_pane.setBorder(new EmptyBorder(5, 5, 5, 5));
		add_GUI.setContentPane(add_pane);
		add_pane.setLayout(null);

		number_tf2 = new JTextField();
		number_tf2.setBounds(54, 54, 270, 27);
		add_pane.add(number_tf2);
		number_tf2.setColumns(10);

		JLabel lblNewLabel = new JLabel("ǰ��");
		lblNewLabel.setFont(new Font("����", Font.BOLD, 14));
		lblNewLabel.setBounds(12, 54, 30, 25);
		add_pane.add(lblNewLabel);

		JLabel label = new JLabel("����");
		label.setFont(new Font("����", Font.BOLD, 14));
		label.setBounds(12, 96, 30, 25);
		add_pane.add(label);

		count_tf2 = new JTextField();
		count_tf2.setColumns(10);
		count_tf2.setBounds(54, 96, 270, 27);
		add_pane.add(count_tf2);

		addok_btn.setBounds(57, 138, 108, 35);
		add_pane.add(addok_btn);

		add_close_btn.setBounds(170, 138, 108, 35);
		add_pane.add(add_close_btn);

		add_GUI.setVisible(false);
	}

	private void pos_init() {

		pos_GUI.setTitle("����");
		pos_GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pos_GUI.setBounds(100, 100, 1012, 654);
		pos_pane = new JPanel();
		pos_pane.setBorder(new EmptyBorder(5, 5, 5, 5));
		pos_GUI.setContentPane(pos_pane);
		pos_pane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(27, 34, 412, 336);
		pos_pane.add(scrollPane);

		pos_tb = new JTable();
		scrollPane.setViewportView(pos_tb);
		pos_createTableModel();
		pos_tb.getTableHeader().setReorderingAllowed(false);
		pos_tb.getTableHeader().setResizingAllowed(false);

		JLabel label = new JLabel("�Ѱ�");
		label.setBounds(62, 390, 50, 15);
		pos_pane.add(label);

		price_area.setBounds(124, 375, 315, 43);
		pos_pane.add(price_area);
		price_area.setEditable(false);

		delete_btn.setBounds(27, 474, 412, 43);
		pos_pane.add(delete_btn);

		pay_btn.setBounds(27, 527, 245, 63);
		pos_pane.add(pay_btn);

		reset_btn.setBounds(284, 527, 155, 63);
		pos_pane.add(reset_btn);

		input_btn.setBounds(348, 428, 91, 36);
		pos_pane.add(input_btn);

		sales_btn.setBounds(471, 537, 182, 43);
		pos_pane.add(sales_btn);

		inventory_btn.setBounds(665, 537, 182, 43);
		pos_pane.add(inventory_btn);

		end_btn.setBounds(859, 537, 110, 43);
		pos_pane.add(end_btn);

		tabbedPane.setBounds(473, 34, 493, 486);
		pos_pane.add(tabbedPane);

		tabbedPane.add(panel_eye);
		tabbedPane.add(panel_lip);
		tabbedPane.add(panel_face);
		tabbedPane.add(panel_care);
		tabbedPane.add(panel_mask);
		tabbedPane.add(panel_clean);
		tabbedPane.add(panel_body);
		tabbedPane.add(panel_ac);

		tabbedPane.addTab("����", panel_eye);
		tabbedPane.addTab("��", panel_lip);
		tabbedPane.addTab("���̽�", panel_face);
		tabbedPane.addTab("��Ų�ɾ�", panel_care);
		tabbedPane.addTab("��/����ũ", panel_mask);
		tabbedPane.addTab(" Ŭ��¡ ", panel_clean);
		tabbedPane.addTab("�ٵ�/���", panel_body);
		tabbedPane.addTab("ȭ���ǰ", panel_ac);

		// ����

		for (eye = 1; eye <= 16; eye++) {
			list_eye.add(new JButton("" + eye));
		}
		panel_eye.setLayout(new GridLayout(4, 4, 10, 10));// ��4��4����10��
		for (int i = 0; i < 16; i++) {
			JButton button = list_eye.get(i);
			panel_eye.add(button);
		}

		for (lip = 1; lip <= 16; lip++) {
			list_lip.add(new JButton("" + lip));
		}
		panel_lip.setLayout(new GridLayout(4, 4, 10, 10));// ��4��4����10��
		for (int i = 0; i < 16; i++) {
			JButton button = list_lip.get(i);
			panel_lip.add(button);
		}

		for (face = 1; face <= 16; face++) {
			list_face.add(new JButton("" + face));
		}
		panel_face.setLayout(new GridLayout(4, 4, 10, 10));// ��4��4����10��
		for (int i = 0; i < 16; i++) {
			JButton button = list_face.get(i);
			panel_face.add(button);
		}

		for (face = 1; face <= 16; face++) {
			list_face.add(new JButton("" + face));
		}
		panel_face.setLayout(new GridLayout(4, 4, 10, 10));// ��4��4����10��
		for (int i = 0; i < 16; i++) {
			JButton button = list_face.get(i);
			panel_face.add(button);
		}

		for (care = 1; care <= 16; care++) {
			list_care.add(new JButton("" + care));
		}
		panel_care.setLayout(new GridLayout(4, 4, 10, 10));// ��4��4����10��
		for (int i = 0; i < 16; i++) {
			JButton button = list_care.get(i);
			panel_care.add(button);
		}

		pos_tf = new JTextField();
		pos_tf.setBounds(27, 428, 309, 36);
		pos_pane.add(pos_tf);
		pos_tf.setColumns(10);

		pos_label.setFont(new Font("����", Font.PLAIN, 13));
		pos_label.setBounds(859, 10, 98, 24);
		pos_pane.add(pos_label);

		pos_GUI.setVisible(false);
	}

	private void sales_init() {

		sales_GUI.setTitle("�Ǹ���Ȳ");
		sales_GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sales_GUI.setBounds(100, 100, 456, 367);
		sales_pane = new JPanel();
		sales_pane.setBorder(new EmptyBorder(5, 5, 5, 5));
		sales_GUI.setContentPane(sales_pane);
		sales_pane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 26, 415, 206);
		sales_pane.add(scrollPane);

		salse_tb = new JTable();
		scrollPane.setViewportView(salse_tb);
		sales_createTableModel();
		salse_tb.getTableHeader().setReorderingAllowed(false);
		salse_tb.getTableHeader().setResizingAllowed(false);

		JLabel lblNewLabel = new JLabel("�Ѱ�");
		lblNewLabel.setFont(new Font("����", Font.BOLD, 14));
		lblNewLabel.setBounds(12, 242, 76, 15);
		sales_pane.add(lblNewLabel);

		sum_area.setBounds(99, 237, 328, 25);
		sales_pane.add(sum_area);

		close_btn.setBounds(148, 272, 139, 32);
		sales_pane.add(close_btn);

		sales_GUI.setVisible(false);
	}

	private void login_init() {
		login_GUI.setTitle("�α���");
		login_GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		login_GUI.setBounds(100, 100, 360, 263);
		login_pane = new JPanel();
		login_pane.setBorder(new EmptyBorder(5, 5, 5, 5));
		login_GUI.setContentPane(login_pane);
		login_pane.setLayout(null);

		login_btn.setBounds(122, 158, 104, 37);
		login_pane.add(login_btn);

		label = new JLabel("���");
		label.setFont(new Font("����", Font.PLAIN, 14));
		label.setBounds(24, 44, 80, 29);
		login_pane.add(label);

		id_tf = new JTextField();
		id_tf.setColumns(10);
		id_tf.setBounds(109, 43, 210, 30);
		login_pane.add(id_tf);

		label_1 = new JLabel("�н�����");
		label_1.setFont(new Font("����", Font.PLAIN, 14));
		label_1.setBounds(24, 103, 80, 29);
		login_pane.add(label_1);

		password_tf = new JPasswordField();
		password_tf.setColumns(10);
		password_tf.setBounds(109, 102, 210, 30);
		login_pane.add(password_tf);

		login_GUI.setVisible(true);
	}

	private void start() {
		add_btn.addActionListener(this);// ������ǰ�԰�
		new_btn.addActionListener(this);// ����ǰ�԰�
		delgoods_btn.addActionListener(this);// ��ǰ ����
		newok_btn.addActionListener(this);// ����ǰ�԰�Ȯ��
		addok_btn.addActionListener(this);// ������ǰ�԰�Ȯ��
		delete_btn.addActionListener(this);// ���û�ǰ����
		update_btn.addActionListener(this);//Ȯ�ι�ư
		pay_btn.addActionListener(this);// ����
		reset_btn.addActionListener(this);// �������
		input_btn.addActionListener(this);// ��������
		sales_btn.addActionListener(this);// �Ǹ���ȲȮ��
		inventory_btn.addActionListener(this);// ������
		end_btn.addActionListener(this);// ���α׷�����
		close_btn.addActionListener(this);// �Ǹ���Ȳ����
		login_btn.addActionListener(this);// �α���
		inventory_close_btn.addActionListener(this);// ������â�ݱ�
		add_close_btn.addActionListener(this);// ������ǰ�԰�â�ݱ�
		new_close_btn.addActionListener(this);// ����ǰ�԰�â�ݱ�
	}

	private void start_goods() {//��ư 16���� �̺�Ʈ �����ϱ� ���� �޼ҵ�

		for (int i = 0; i < 16; i++) {
			JButton button = list_eye.get(i);
			button.addActionListener(this);
		}
		for (int j = 0; j < 16; j++) {
			JButton button = list_lip.get(j);
			button.addActionListener(this);
		}
		for (int k = 0; k < 16; k++) {
			JButton button = list_face.get(k);
			button.addActionListener(this);
		}
		for (int m = 0; m < 16; m++) {
			JButton button = list_care.get(m);
			button.addActionListener(this);
		}

	}

	private void createbtn_eye() {// ��ư ���� ����
		eye++;
		list_eye.add(new JButton("" + eye));// ��ư����
		JButton button = list_eye.get(eye - 1);
		panel_eye.add(button);// ������ ��ư �гο� �߰�
		button.addActionListener(this);// �� ��ư�� �̺�Ʈ�� ��� ��������??

		int cr = (int) Math.sqrt(eye);// �׸��� ���̾ƿ� (�������� ���� )
		panel_eye.setLayout(new GridLayout(cr, cr, 10, 10));

	}

	private void createbtn_lip() {
		lip++;
		list_lip.add(new JButton("" + lip));// ��ư����
		JButton button = list_lip.get(lip - 1);
		panel_lip.add(button);// ������ ��ư �гο� �߰�

		int cr = (int) Math.sqrt(lip);// �׸��� ���̾ƿ� (�������� ���� )
		panel_lip.setLayout(new GridLayout(cr, cr, 10, 10));

	}

	private void createbtn_face() {
		face++;
		list_face.add(new JButton("" + face));// ��ư����
		JButton button = list_face.get(face - 1);
		panel_face.add(button);// ������ ��ư �гο� �߰�

		int cr = (int) Math.sqrt(face);// �׸��� ���̾ƿ� (�������� ���� )
		panel_face.setLayout(new GridLayout(cr, cr, 10, 10));

	}

	private void createbtn_care() {
		care++;
		list_care.add(new JButton("" + care));// ��ư����
		JButton button = list_care.get(care - 1);
		panel_care.add(button);// ������ ��ư �гο� �߰�

		int cr = (int) Math.sqrt(care);// �׸��� ���̾ƿ� (�������� ���� )
		panel_care.setLayout(new GridLayout(cr, cr, 10, 10));

	}
	
	private void get_btnname() {
		//��ư�� �̸� �������� �޼ҵ�
		
		String query_10 = " select goods_nm from TB_storage where ret_cd=10";
		String query_20 = " select goods_nm from TB_storage where ret_cd=20";
		String query_30 = " select goods_nm from TB_storage where ret_cd=30";
		String query_40 = " select goods_nm from TB_storage where ret_cd=40";

		
			try {	
				int i=0;
				int j=0;
				int k=0;
				int n=0;
				
				rs=stmt.executeQuery(query_10);
				while(rs.next()) {
					JButton button = list_eye.get(i);
					button.setText(rs.getString("goods_nm"));
					i++;
				}//���� ��ư ��
				
				rs=stmt.executeQuery(query_20);
				while(rs.next()) {
					JButton button = list_lip.get(j);
					button.setText(rs.getString("goods_nm"));
					j++;
				}//�� ��ư ��
				
				rs=stmt.executeQuery(query_30);		
				while(rs.next()) {
					JButton button = list_face.get(k);
					button.setText(rs.getString("goods_nm"));
					k++;
				}//���̽� ��ư ��
				
				rs=stmt.executeQuery(query_40);				
				while(rs.next()) {
					JButton button = list_care.get(n);
					button.setText(rs.getString("goods_nm"));
					n++;
				}//��Ų�ɾ� ��ư ��
				
			
			} catch (SQLException e) {
				e.printStackTrace();
			}

	
	}
	

	public static void main(String[] args) {
		new Client();
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == login_btn) {
			System.out.println("--------------------------");
			if (id_tf.getText().length() == 0 && password_tf.getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "����� �н����带 �Է��ϼ���.", "�˸�", JOptionPane.WARNING_MESSAGE);
			} else if (id_tf.getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "����� �Է��ϼ���.", "�˸�", JOptionPane.WARNING_MESSAGE);
			} else if (password_tf.getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "�н����带 �Է��ϼ���.", "�˸�", JOptionPane.WARNING_MESSAGE);//����ó��
			} 
			
			else {	
				id = Integer.parseInt(id_tf.getText().trim());
				pass = password_tf.getText().trim();

				System.out.println(" ��� : " + id + " | �н����� : " + pass);

				String query = "SELECT *FROM TB_membership" + " WHERE id=" + id + " AND password='" + pass + "'";

				System.out.println("\n�������� : " + query);

				try {
					rs = stmt.executeQuery(query);

					if (rs.next()) {
						String name = rs.getString("member_nm");
						System.out.printf("�̸� : %s \n", name);

						String pw = rs.getString("password");
						System.out.printf("�н����� : %s \n", pw);

						if (pass.equals(pw)) {
							JOptionPane.showMessageDialog(null, name + "�� �����ϼ̽��ϴ�", "�˸�", JOptionPane.WARNING_MESSAGE);
							login_GUI.setVisible(false);
							pos_GUI.setVisible(true);
							pos_label.setText("����� : " + name);
						}
					} else {
						JOptionPane.showMessageDialog(null, "��ġ�ϴ� ������ �����ϴ�.", "�˸�", JOptionPane.WARNING_MESSAGE);
						id_tf.setText("");
						password_tf.setText("");
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}

			System.out.println("�α��� ��ư Ŭ��");
			System.out.println("--------------------------");

		} 
		
		else if (e.getSource() == add_btn) {
			System.out.println("--------------------------");
			System.out.println("���� ��ǰ �԰� ��ư Ŭ��\n");
			System.out.println("--------------------------");
			add_GUI.setVisible(true);
		}
		
		else if (e.getSource() == new_btn) {
			System.out.println("--------------------------");
			System.out.println("�� ��ǰ �԰� ��ư Ŭ��\n");
			System.out.println("--------------------------");
			new_GUI.setVisible(true);
		}
		
		else if (e.getSource() == newok_btn) {
			System.out.println("--------------------------");
			String ret_nm = new_cmb.getSelectedItem().toString();// �޺��ڽ����� ���õ� ��
			System.out.printf("%s\n", ret_nm);

			int ret_cd = 0;// �з��ڵ� ���� ����

			String query = " select ret_cd from TB_retrieval where ret_nm='" + ret_nm + "';";
			System.out.printf("%s\n", query);

			try {
				rs = stmt.executeQuery(query);
				while (rs.next()) {// rs�� �� �ܾ ������.(next())
					ret_cd = rs.getInt("ret_cd");// �з��ڵ�ޱ�
					System.out.printf("%d \n", ret_cd);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			String name = name_tf.getText().trim();
			String code = number_tf.getText().trim();
			int count = Integer.parseInt(count_tf.getText().trim());
			int value = Integer.parseInt(value_tf.getText().trim());

			String query_insert = "insert into TB_storage values ( '" + code + "', " + ret_cd + ", '" + name + "', "
					+ value + ", " + count + ");";
			System.out.printf("%s\n", query_insert);

			try {
				int col_count = stmt.executeUpdate(query_insert);// ��񳻿������Ʈ
				System.out.println("����� �� ���� : " + col_count);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			String retrieval = inven_cmb.getSelectedItem().toString();// �޺��ڽ����� ���õ� ��
			int ret_cd2=get_retcode(retrieval);
			if(ret_cd2 != 0) {
				inventory_md.setRowCount(0);
				setSelectTable_inven(ret_cd2);
				name_tf.setText("");
				number_tf.setText("");
				count_tf.setText("");
				value_tf.setText("");
			}
			else {
				inventory_md.setRowCount(0);
				setTable_inven();
				name_tf.setText("");
				number_tf.setText("");
				count_tf.setText("");
				value_tf.setText("");
			}

			System.out.println("�� ��ǰ �԰� Ȯ�� ��ư Ŭ��\n");
			System.out.println("--------------------------");
		} 
		
		else if (e.getSource() == addok_btn) {
			System.out.println("--------------------------");

			String code = number_tf2.getText().trim();//��ǰ��ȣ
			int count = Integer.parseInt(count_tf2.getText().trim());//��ǰ����

			String query = "select goods_cd from TB_storage where goods_cd='" + code + "';";
			System.out.printf("%s \n", query);

			
			try {
				rs = stmt.executeQuery(query);//���� �ִ��� Ȯ��

				if (rs.next()) {// ������ �Ѱ�, ���� while���� �ʾƵ� ��
					
					String query_update = "update TB_storage set cur_count = ((select cur_count from TB_storage where goods_cd= '"
							+ code + "')+" + count + ") where goods_cd = '" + code + "';";
					System.out.printf("%s \n", query_update);

					int col_count2 = stmt.executeUpdate(query_update);
					System.out.println("����� �� ���� : " + col_count2);

				} else {
					JOptionPane.showMessageDialog(null, "��ġ�ϴ� ������ �����ϴ�.", "�˸�", JOptionPane.WARNING_MESSAGE);

				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			String retrieval = inven_cmb.getSelectedItem().toString();// �޺��ڽ����� ���õ� ��
			int ret_cd=get_retcode(retrieval);
			if(ret_cd != 0) {
				inventory_md.setRowCount(0);
				setSelectTable_inven(ret_cd);
				number_tf2.setText("");
				count_tf2.setText("");
			}
			else {
				inventory_md.setRowCount(0);
				setTable_inven();
				number_tf2.setText("");
				count_tf2.setText("");
			}


			System.out.println("���� ��ǰ �԰� Ȯ�� ��ư Ŭ��\n");
			System.out.println("--------------------------");
		} 
		
		else if (e.getSource() == delgoods_btn) {
			System.out.println("--------------------------");

			//��ǰ��ȣ ���� ���õ� ���� ��
			int row = inventory_tb.getSelectedRow();//�� ����
			int col = 0;// ù��° ���� �� : ��ǰ��ȣ

			if (row >= 0) {//���õ� ���� ���� ���
				String code = inventory_tb.getValueAt(row, col).toString();// ��ǰ��ȣ ��������


				try {
					String query = "delete from TB_storage where goods_cd='" + code + "'";
					System.out.printf("%s \n", query);

					int col_count = stmt.executeUpdate(query);
					System.out.println("����� �� ���� : " + col_count);

				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				String retrieval = inven_cmb.getSelectedItem().toString();// �޺��ڽ����� ���õ� ��
				int ret_cd=get_retcode(retrieval);
				if(ret_cd != 0) {
					inventory_md.setRowCount(0);
					setSelectTable_inven(ret_cd);
				}
				else {
					inventory_md.setRowCount(0);
					setTable_inven();
				}
			} 
			else {
				JOptionPane.showMessageDialog(null, "���õ� ���� �����ϴ�.", "�˸�", JOptionPane.WARNING_MESSAGE);
			}
			System.out.println("��ǰ ���� ��ư Ŭ��\n");
			System.out.println("--------------------------");

		} 
		
		else if (e.getSource() == delete_btn) {
			System.out.println("--------------------------");
			System.out.println("���� ��ǰ ���� ��ư Ŭ��\n");
			System.out.println("--------------------------");
		}
		
		else if (e.getSource() == pay_btn) {

			// ������ �������� �� 0�� �Ǵ� ��ǰ �����ϰų� �ǸźҰ��� �������� ���°�
			System.out.println("���� ��ư Ŭ��\n");
		}
		
		else if (e.getSource() == reset_btn) {
			System.out.println("���� ��� ��ư Ŭ��\n");
		}
		
		else if (e.getSource() == input_btn) {
			System.out.println("�������� ��ư Ŭ��\n");
		}
		
		else if (e.getSource() == sales_btn) {
			System.out.println("�Ǹ� ��Ȳ Ȯ�� ��ư Ŭ��\n");
			sales_GUI.setVisible(true);
		} 
		
		else if (e.getSource() == inventory_btn) {
			System.out.println("--------------------------");

			Inventory_GUI.setVisible(true);
			setTable_inven();
			System.out.println("������ ��ư Ŭ��\n");
			System.out.println("--------------------------");

		} 
		
		else if (e.getSource() == update_btn) {
			
			System.out.println("--------------------------");

			String retrieval = inven_cmb.getSelectedItem().toString();// �޺��ڽ����� ���õ� ��
			int ret_cd = get_retcode(retrieval);// �з��ڵ� ���� ����
			
			if(ret_cd != 0) {
				inventory_md.setRowCount(0);	
				setSelectTable_inven(ret_cd);
			}
			else {
				inventory_md.setRowCount(0);
				setTable_inven();
			}

			System.out.println("������Ʈ ��ư Ŭ��\n");
			System.out.println("--------------------------");
		
		}

		else if (e.getSource() == list_eye.get(0) || e.getSource() == list_eye.get(1)
				|| e.getSource() == list_eye.get(2) || e.getSource() == list_eye.get(3)
				|| e.getSource() == list_eye.get(4) || e.getSource() == list_eye.get(5)
				|| e.getSource() == list_eye.get(6) || e.getSource() == list_eye.get(7)
				|| e.getSource() == list_eye.get(8) || e.getSource() == list_eye.get(9)
				|| e.getSource() == list_eye.get(10) || e.getSource() == list_eye.get(11)
				|| e.getSource() == list_eye.get(12) || e.getSource() == list_eye.get(13)
				|| e.getSource() == list_eye.get(14) || e.getSource() == list_eye.get(15)) {
			// ���� ��ư�� �̸��� �Ȱ��� DB������ ã�Ƽ� �ֹ�����Ʈ ���̺� �ٿ��ֱ�//getText()�����

			
		}

		else if (e.getSource() == list_lip.get(0) || e.getSource() == list_lip.get(1)
				|| e.getSource() == list_lip.get(2) || e.getSource() == list_lip.get(3)
				|| e.getSource() == list_lip.get(4) || e.getSource() == list_lip.get(5)
				|| e.getSource() == list_lip.get(6) || e.getSource() == list_lip.get(7)
				|| e.getSource() == list_lip.get(8) || e.getSource() == list_lip.get(9)
				|| e.getSource() == list_lip.get(10) || e.getSource() == list_lip.get(11)
				|| e.getSource() == list_lip.get(12) || e.getSource() == list_lip.get(13)
				|| e.getSource() == list_lip.get(14) || e.getSource() == list_lip.get(15)) {
			// �׷� �ڵ����� ���� ��ư���� �̺�Ʈ�� ��� �ްǵ�?

		}

		else if (e.getSource() == end_btn) {
			System.out.println("���� ��ư Ŭ��\n");
		} else if (e.getSource() == inventory_close_btn) {
			System.out.println("--------------------------");

			inventory_md.setRowCount(0);
			Inventory_GUI.setVisible(false);
			System.out.println("������ â �ݱ� ��ư Ŭ�� \n");
			System.out.println("--------------------------");

		} 
		
		else if (e.getSource() == add_close_btn) {
			number_tf2.setText("");
			count_tf2.setText("");
			add_GUI.setVisible(false);
			System.out.println("������ǰ �԰� â �ݱ� ��ư Ŭ��\n");
		}
		
		else if (e.getSource() == new_close_btn) {
			name_tf.setText("");
			number_tf.setText("");
			count_tf.setText("");
			value_tf.setText("");
			new_GUI.setVisible(false);
			System.out.println("�� ��ǰ �԰� â �ݱ� ��ư Ŭ��\n");
		}

		else if (e.getSource() == close_btn) {
			sales_GUI.setVisible(false);
			System.out.println("������Ȳ �ݱ� ��ư Ŭ��\n");

		}
	}
}
