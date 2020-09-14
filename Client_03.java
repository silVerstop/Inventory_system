package Client;


import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
		private Vector<String> inventory_col=new Vector<>();
		private Vector<Vector> inventory_list=new Vector<>();
		private DefaultTableModel inventory_md;

		// new_GUI ���� ���� ����
		private JFrame new_GUI = new JFrame();
		private JPanel new_pane;
		private JTextField name_tf;
		private JTextField number_tf;
		private JTextField count_tf;
		private JTextField value_tf;
		private JButton newok_btn = new JButton("Ȯ��");
		private JComboBox new_cmb;

		// add_GUI ���� ���� ����
		private JFrame add_GUI = new JFrame();
		private JPanel add_pane;
		private JTextField number_tf2;
		private JTextField count_tf2;
		private JComboBox add_cmb = new JComboBox();
		private JButton addok_btn = new JButton("Ȯ��");

		//pos_GUI ���� ���� ����
		private JFrame pos_GUI = new JFrame();
		private Vector <String> pos_col = new Vector<>();//���̸� �����ϴ� ����<String>�ǹ�?
		private Vector <Vector> pos_list = new Vector<>();//�� ������ ������ ������
		private DefaultTableModel pos_md;
		private JPanel pos_pane;
		private JTable pos_tb;
		private JTextField pos_tf;
		private 	JTextArea price_area = new JTextArea();
		private JButton delete_btn = new JButton("���� ��ǰ ����");
		private JButton pay_btn = new JButton("����");
		private JButton reset_btn = new JButton("���");
		private JButton input_btn = new JButton("��������");
		private JButton sales_btn = new JButton("���� ��Ȳ");
		private JButton inventory_btn = new JButton("��� ����");
		private JButton end_btn = new JButton("����");
		private JLabel pos_label = new JLabel();

		private 	JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		
		//sales_GUI ���� ���� ����
		private JFrame sales_GUI = new JFrame();
		private JPanel sales_pane;
		private JTable salse_tb;
		private DefaultTableModel salse_md;
		private Vector<String> sales_col = new Vector<>();
		private Vector<Vector> sales_list = new Vector<>();
		private 	JTextArea sum_area = new JTextArea();
		private 	JButton close_btn = new JButton("Ȯ��");
		
		//login_GUI ���� ���� ����
		private JFrame login_GUI=new JFrame();
		private JPanel login_pane;
		private JLabel label;
		private JTextField id_tf;
		private JLabel label_1;
		private JTextField password_tf;
		private 	JButton login_btn = new JButton("�α���");

		//�� ���� ������
		private int id;//���
		private String pass="";//�н�����

		
	Client() {
		connectDB();
		inventory_init();
		new_init();
		add_init();
		pos_init();
		login_init();
		start();
	}

	private void connectDB() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");// JDBC����̹� �ε�
			System.out.println("����̹� ���� ����");
			conn = DriverManager.getConnection(url, user, password); // Ŀ�ؼ� ��ü ����

			stmt = conn.createStatement();

			
			if (conn != null) {
				System.out.println("��� ����");
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
	
	public void inventory_createTableModel() {
		inventory_col.add("ǰ��");
		inventory_col.add("��ǰ��");
		inventory_col.add("����");
		inventory_col.add("�������");
		inventory_md=new DefaultTableModel(inventory_col,0);
		inventory_tb.setModel(inventory_md);
	}
	
	
	public void pos_createTableModel( ) {//JTable����
		pos_col.add("No");
		pos_col.add("��ǰ��");
		pos_col.add("����");
		pos_col.add("����");
		pos_md = new DefaultTableModel(pos_col, 0);
		pos_tb.setModel(pos_md);
	}
	
	public void sales_createTableModel() {
		sales_col.add("ǰ��");
		sales_col.add("��ǰ��");
		sales_col.add("����");
		sales_col.add("����");
		sales_col.add("�����");
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

		
		String cbmenu[]= { "��ü����", "����", "��", "���̽�", "��Ų�ɾ�", "��/����ũ", "Ŭ��¡", "�ٵ�/���", "ȭ���ǰ" };
		inven_cmb=new JComboBox(cbmenu);
		inven_cmb.setBounds(12, 55, 136, 38);
		Inven_pane.add(inven_cmb);

		add_btn.setBounds(451, 156, 154, 47);
		Inven_pane.add(add_btn);

		new_btn.setBounds(451, 99, 155, 47);
		Inven_pane.add(new_btn);

		Inventory_GUI.setVisible(false);

	}
	
	private void new_init() {// ����ǰ�԰�GUI
		new_GUI.setTitle("�� ��ǰ �԰�");
		new_GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		new_GUI.setBounds(100, 100, 429, 330);
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
		newok_btn.setBounds(142, 240, 135, 43);
		new_pane.add(newok_btn);

		String cbmenu[]= { "����", "��", "���̽�", "��Ų�ɾ�", "��/����ũ", "Ŭ��¡", "�ٵ�/���", "ȭ���ǰ" };
		new_cmb=new JComboBox(cbmenu);
		new_cmb.setBounds(253, 10, 135, 33);
		new_pane.add(new_cmb);

		new_GUI.setVisible(false);
	}
	
	private void add_init() {// ������ǰ�԰�GUI
		add_GUI.setTitle("���� ��ǰ �԰�");
		add_GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add_GUI.setBounds(100, 100, 350, 217);
		add_pane = new JPanel();
		add_pane.setBorder(new EmptyBorder(5, 5, 5, 5));
		add_GUI.setContentPane(add_pane);
		add_pane.setLayout(null);

		number_tf2 = new JTextField();
		number_tf2.setBounds(54, 54, 270, 32);
		add_pane.add(number_tf2);
		number_tf2.setColumns(10);

		JLabel lblNewLabel = new JLabel("ǰ��");
		lblNewLabel.setFont(new Font("����", Font.BOLD, 14));
		lblNewLabel.setBounds(12, 54, 30, 30);
		add_pane.add(lblNewLabel);

		JLabel label = new JLabel("����");
		label.setFont(new Font("����", Font.BOLD, 14));
		label.setBounds(12, 96, 30, 30);
		add_pane.add(label);

		count_tf2 = new JTextField();
		count_tf2.setColumns(10);
		count_tf2.setBounds(54, 96, 270, 32);
		add_pane.add(count_tf2);

		String cbmenu[]= { "����", "��", "���̽�", "��Ų�ɾ�", "��/����ũ", "Ŭ��¡", "�ٵ�/���", "ȭ���ǰ" };
		add_cmb=new JComboBox(cbmenu);
		add_cmb.setBounds(221, 21, 103, 23);
		add_pane.add(add_cmb);

		addok_btn.setBounds(116, 138, 108, 32);
		add_pane.add(addok_btn);

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
		
		JPanel panel_eye = new JPanel();
		JPanel panel_lip = new JPanel();
		JPanel panel_face = new JPanel();
		JPanel panel_care = new JPanel();
		JPanel panel_mask = new JPanel();
		JPanel panel_clean = new JPanel();
		JPanel panel_body = new JPanel();
		JPanel panel_ac = new JPanel();
		
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
		
		//����
		JButton btn = new JButton("1");
		JButton btn1 = new JButton("2");//�� ���� ������ ��񿡼� �����ð�
		JButton btn2 = new JButton("3");
		JButton btn3 = new JButton("4");
		JButton btn4 = new JButton("5");
		JButton btn5 = new JButton("6");
		JButton btn6= new JButton("7");
		JButton btn7 = new JButton("8");
		JButton btn8 = new JButton("9");
		JButton btn9 = new JButton("10");
		JButton btn10 = new JButton("11");
		JButton btn11 = new JButton("12");
		JButton btn12 = new JButton("13");
		JButton btn13 = new JButton("14");
		JButton btn14 = new JButton("15");
		JButton btn15 = new JButton("16");
		
		panel_eye.setLayout(new GridLayout(4,4,10,10));//�׸��� ������� ��ư ��ġ
		panel_eye.add(btn);panel_eye.add(btn1);panel_eye.add(btn2);panel_eye.add(btn3);
		panel_eye.add(btn4);panel_eye.add(btn5);panel_eye.add(btn6);panel_eye.add(btn7);
		panel_eye.add(btn8);panel_eye.add(btn9);panel_eye.add(btn10);panel_eye.add(btn11);
		panel_eye.add(btn12);panel_eye.add(btn13);panel_eye.add(btn14);panel_eye.add(btn15);
		
		
		//��
		JButton btn16 = new JButton("1");
		JButton btn17 = new JButton("2");//�� ���� ������ ��񿡼� �����ð�
		JButton btn18 = new JButton("3");
		JButton btn19 = new JButton("4");
		JButton btn20= new JButton("5");
		JButton btn21 = new JButton("6");
		JButton btn22= new JButton("7");
		JButton btn23 = new JButton("8");
		JButton btn24 = new JButton("9");
		JButton btn25 = new JButton("10");
		JButton btn26 = new JButton("11");
		JButton btn27 = new JButton("12");
		JButton btn28 = new JButton("13");
		JButton btn29 = new JButton("14");
		JButton btn30 = new JButton("15");
		JButton btn31 = new JButton("16");
		
		panel_lip.setLayout(new GridLayout(4,4,10,10));//�׸��� ������� ��ư ��ġ
		panel_lip.add(btn16);panel_lip.add(btn17);panel_lip.add(btn18);panel_lip.add(btn19);
		panel_lip.add(btn20);panel_lip.add(btn21);panel_lip.add(btn22);panel_lip.add(btn23);
		panel_lip.add(btn24);panel_lip.add(btn25);panel_lip.add(btn26);panel_lip.add(btn27);
		panel_lip.add(btn28);panel_lip.add(btn29);panel_lip.add(btn30);panel_lip.add(btn31);
		
		//���̽�
		JButton btn32 = new JButton("1");
		JButton btn33 = new JButton("2");//�� ���� ������ ��񿡼� �����ð�
		JButton btn34 = new JButton("3");
		JButton btn35 = new JButton("4");
		JButton btn36= new JButton("5");
		JButton btn37 = new JButton("6");
		JButton btn38= new JButton("7");
		JButton btn39 = new JButton("8");
		JButton btn40 = new JButton("9");
		JButton btn41 = new JButton("10");
		JButton btn42 = new JButton("11");
		JButton btn43 = new JButton("12");
		JButton btn44 = new JButton("13");
		JButton btn45 = new JButton("14");
		JButton btn46 = new JButton("15");
		JButton btn47 = new JButton("16");
		
		panel_face.setLayout(new GridLayout(4,4,10,10));//�׸��� ������� ��ư ��ġ
		panel_face.add(btn32);panel_face.add(btn33);panel_face.add(btn34);panel_face.add(btn35);
		panel_face.add(btn36);panel_face.add(btn37);panel_face.add(btn38);panel_face.add(btn39);
		panel_face.add(btn40);panel_face.add(btn41);panel_face.add(btn42);panel_face.add(btn43);
		panel_face.add(btn44);panel_face.add(btn45);panel_face.add(btn46);panel_face.add(btn47);
		
		
		JButton btn48 = new JButton("1");
		JButton btn49 = new JButton("2");//�� ���� ������ ��񿡼� �����ð�
		JButton btn50 = new JButton("3");
		JButton btn51 = new JButton("4");
		JButton btn52= new JButton("5");
		JButton btn53 = new JButton("6");
		JButton btn54= new JButton("7");
		JButton btn55 = new JButton("8");
		JButton btn56 = new JButton("9");
		JButton btn57 = new JButton("10");
		JButton btn58 = new JButton("11");
		JButton btn59 = new JButton("12");
		JButton btn60 = new JButton("13");
		JButton btn61 = new JButton("14");
		JButton btn62 = new JButton("15");
		JButton btn63 = new JButton("16");
		
		panel_care.setLayout(new GridLayout(4,4,10,10));//�׸��� ������� ��ư ��ġ
		panel_care.add(btn48);panel_care.add(btn49);panel_care.add(btn50);panel_care.add(btn51);
		panel_care.add(btn52);panel_care.add(btn53);panel_care.add(btn54);panel_care.add(btn55);
		panel_care.add(btn56);panel_care.add(btn57);panel_care.add(btn58);panel_care.add(btn59);
		panel_care.add(btn60);panel_care.add(btn61);panel_care.add(btn62);panel_care.add(btn63);
		
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
		
		password_tf = new JTextField();
		password_tf.setColumns(10);
		password_tf.setBounds(109, 102, 210, 30);
		login_pane.add(password_tf);
		
		login_GUI.setVisible(true);
	}
	
	private void start() {
		add_btn.addActionListener(this);//������ǰ�԰�
		new_btn.addActionListener(this);//����ǰ�԰�
		newok_btn.addActionListener(this);//����ǰ�԰�Ȯ��
		addok_btn.addActionListener(this);//������ǰ�԰�Ȯ��
		delete_btn.addActionListener(this);//���û�ǰ����
		pay_btn.addActionListener(this);//����
		reset_btn.addActionListener(this);//�������
		input_btn.addActionListener(this);//��������
		sales_btn.addActionListener(this);//�Ǹ���ȲȮ��
		inventory_btn.addActionListener(this);//������
		end_btn.addActionListener(this);//���α׷�����
		close_btn.addActionListener(this);//�Ǹ���Ȳ����
		login_btn.addActionListener(this);//�α���
	}


	public static void main(String[] args) {
		new Client();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	
		if(e.getSource()==login_btn) {
			System.out.println("�α��� ��ư Ŭ��\n");
			
			if(id_tf.getText().length()==0&&password_tf.getText().length()==0){
				JOptionPane.showMessageDialog(null, "����� �н����带 �Է��ϼ���.", "�˸�", JOptionPane.WARNING_MESSAGE);
			}
			else if(id_tf.getText().length()==0)
			{
				JOptionPane.showMessageDialog(null, "����� �Է��ϼ���.", "�˸�", JOptionPane.WARNING_MESSAGE);
			}
			else if(password_tf.getText().length()==0) {
				JOptionPane.showMessageDialog(null, "�н����带 �Է��ϼ���.", "�˸�", JOptionPane.WARNING_MESSAGE);
			}
			else {
				id=Integer.parseInt(id_tf.getText().trim());
				pass=password_tf.getText().trim();
				
				System.out.println(" ��� : "+id+" | �н����� : "+pass);
				
				String query="SELECT member_nm FROM TB_membership"
						+ " WHERE id="+id+" AND password='"+pass+ "'";
				
				System.out.println("\n�������� : " + query);
				
				try {
					rs=stmt.executeQuery(query);
					
					while(rs.next()) {
						String name=rs.getString("member_nm");
						System.out.printf("�̸� : %s \n",name);
						
						if( name==null) {
							JOptionPane.showMessageDialog
							(null, "��ġ�ϴ� ������ �����ϴ�.", "�˸�", JOptionPane.WARNING_MESSAGE);
						}
						else {
							JOptionPane.showMessageDialog
							(null, name+"�� �����ϼ̽��ϴ�", "�˸�", JOptionPane.WARNING_MESSAGE);
							login_GUI.setVisible(false);
							pos_GUI.setVisible(true);
							pos_label.setText("����� : "+name);
						}
					
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		else if(e.getSource()==add_btn) {
			System.out.println("���� ��ǰ �԰� ��ư Ŭ��\n");
			add_GUI.setVisible(true);
		}
		else if(e.getSource()==new_btn) {
			System.out.println("�� ��ǰ �԰� ��ư Ŭ��\n");
			new_GUI.setVisible(true);
		}
		else if(e.getSource()==newok_btn) {
			
			String ret_nm = new_cmb.getSelectedItem().toString();//�޺��ڽ����� ���õ� ��
			System.out.printf("%s\n", ret_nm);
			int ret_cd = 0;
			String query =" select ret_cd from TB_retrieval where ret_nm='"+ret_nm+"';";
			System.out.printf("%s\n", query);
			try {
				rs=stmt.executeQuery(query);
				while(rs.next()) {
					ret_cd=rs.getInt("ret_cd");//�з��ڵ�ޱ�
					System.out.printf("%d \n",ret_cd);
				}
			} 
			catch (SQLException e1) {
				e1.printStackTrace();
			}
		    String name=	name_tf.getText().trim();
			String code=number_tf.getText().trim();
			int count=Integer.parseInt(count_tf.getText().trim());
			int value=Integer.parseInt(value_tf.getText().trim());
			
			String query_insert ="insert into TB_storage values ( '"+code+"', "+ret_cd+", '"+name+"', "+value+", "+count+");";
			System.out.printf("%s\n", query_insert);
			try {
				stmt.executeUpdate(query_insert);//��񳻿������Ʈ
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			name_tf.setText("");
			number_tf.setText("");
			count_tf.setText("");
			value_tf.setText("");
			new_GUI.setVisible(false);
			
			System.out.println("�� ��ǰ �԰� Ȯ�� ��ư Ŭ��\n");			
		}
		else if(e.getSource()==addok_btn) {
			System.out.println("���� ��ǰ �԰� Ȯ�� ��ư Ŭ��\n");
		}
		else if(e.getSource()==delete_btn) {
			System.out.println("���� ��ǰ ���� ��ư Ŭ��\n");
		}
		else if(e.getSource()==pay_btn) {
			System.out.println("���� ��ư Ŭ��\n");
		}
		else if(e.getSource()==reset_btn) {
			System.out.println("���� ��� ��ư Ŭ��\n");
		}
		else if(e.getSource()==input_btn) {
			System.out.println("�������� ��ư Ŭ��\n");
		}
		else if(e.getSource()==sales_btn) {
			System.out.println("�Ǹ� ��Ȳ Ȯ�� ��ư Ŭ��\n");
		}
		else if(e.getSource()==inventory_btn) {
			System.out.println("������ ��ư Ŭ��\n");
			Inventory_GUI.setVisible(true);
		}
		else if(e.getSource()==end_btn) {
			System.out.println("���� ��ư Ŭ��\n");
		}
		else if(e.getSource()==close_btn) {
			System.out.println("Ȯ�� ��ư Ŭ��\n");

		}
	}
}
