package Client;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.ServerSocket;
import java.net.Socket;
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
import javax.swing.ListSelectionModel;
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
	private JTextField price_tf = new JTextField();
	private JTextField cnt_tf = new JTextField();
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
	private int eye = 0, lip = 0, face = 0, care = 0;// ���߿� ��ư ���� �Ҵ� ���� �� �ְ� �� ����

	// sales_GUI ���� ���� ����
	private JFrame sales_GUI = new JFrame();
	private JPanel sales_pane;
	private JTable sales_tb;
	private DefaultTableModel sales_md;
	private Vector<String> sales_col = new Vector<>();
	private Vector<Vector> sales_list = new Vector<>();
	private JTextField sum_tf = new JTextField();// �Ѱ���
	private JTextField amt_tf = new JTextField();// �Ѽ���
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
	private Integer i = new Integer(1);// ������ �ֹ�����Ʈ ����
	private Integer count = new Integer(1);
	private Vector goods_list = new Vector(); // �ֹ�����Ʈ�� ��� ��ǰ�� ���� ����
	private ArrayList cnt_list = new ArrayList();// ��ư���� ī��Ʈ�� ���� ����Ʈ
	
	//������� ����
	private ServerSocket server_socket;
	private Socket socket;
	private int port;

	Client() {
		connectDB();
		inventory_init();
		new_init();
		add_init();
		pos_init();
		sales_init();
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
				System.out.println("�����ͺ��̽� ���� ����");
			} else {
				System.out.println("���� ����");
			}
		} catch (Exception e) {
			System.out.println("������ ���̽� ���� ���� : " + e.getMessage());
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
			JOptionPane.showMessageDialog(null, e.getMessage(), "�˸�", JOptionPane.WARNING_MESSAGE);// ����ó��
		}
	}

	private void setTable_sales() {
		String query = "select goods_cd, qty ,amount , id, sale_dt from TB_outgoods";

		try {
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				sales_md.addRow(new Object[] { rs.getString("goods_cd"), rs.getInt("amount"), rs.getInt("qty"),
						rs.getInt("id"), rs.getString("sale_dt") });
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "�˸�", JOptionPane.WARNING_MESSAGE);// ����ó��
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
				JOptionPane.showMessageDialog(null, e.getMessage(), "�˸�", JOptionPane.WARNING_MESSAGE);// ����ó��
			}
		}

	}

	private int get_retcode(String retrieval) {

		int code = 0;// �з��ڵ� ���� ����

		String query = " select ret_cd from TB_retrieval where ret_nm='" + retrieval + "' ";// �з��ڵ� ���� ����
		System.out.printf("%s\n", query);

		try {
			rs = stmt.executeQuery(query);

			if (rs.next()) {// �з� �ڵ� ���� ���(10~80�� ���
				code = rs.getInt("ret_cd");// �з��ڵ�ޱ�

			} else {// ��ü���� ������ ���
				code = 0;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "�˸�", JOptionPane.WARNING_MESSAGE);// ����ó��
		}

		System.out.println("�з��ڵ� : " + code);
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

		pos_tb.getColumnModel().getColumn(0).setPreferredWidth(40);
		pos_tb.getColumnModel().getColumn(1).setPreferredWidth(120);

	}

	public void sales_createTableModel() {
		sales_col.add("��ǰ�ڵ�");
		sales_col.add("����");
		sales_col.add("����");
		sales_col.add("����ڻ��");
		sales_col.add("�Ǹ��Ͻ�");
		sales_md = new DefaultTableModel(sales_col, 0);
		sales_tb.setModel(sales_md);

		sales_tb.getColumnModel().getColumn(1).setPreferredWidth(40);
		sales_tb.getColumnModel().getColumn(2).setPreferredWidth(80);
		sales_tb.getColumnModel().getColumn(3).setPreferredWidth(80);
		sales_tb.getColumnModel().getColumn(4).setPreferredWidth(150);

	}

	private void inventory_init() {// ������GUI
		Inventory_GUI.setTitle("������");
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
		inventory_tb.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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

		Inventory_GUI.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Inventory_GUI.setVisible(false);
				Inventory_GUI.dispose();
			}
		});
	}

	private void new_init() {// ����ǰ�԰�GUI
		new_GUI.setTitle("�� ��ǰ �԰�");
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

		new_GUI.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				new_GUI.setVisible(false);
				new_GUI.dispose();
			}
		});
	}

	private void add_init() {// ������ǰ�԰�GUI
		add_GUI.setTitle("���� ��ǰ �԰�");
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

		add_GUI.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				add_GUI.setVisible(false);
				add_GUI.dispose();
			}
		});
	}

	private void pos_init() {

		pos_GUI.setTitle("����");
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
		pos_tb.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JLabel label = new JLabel("�Ѱ�");
		label.setBounds(249, 390, 32, 15);
		pos_pane.add(label);

		price_tf.setBounds(293, 375, 146, 43);
		pos_pane.add(price_tf);
		price_tf.setText("0");
		price_tf.setEditable(false);

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

		get_btn_eye();
		get_btn_lip();
		get_btn_face();
		get_btn_care();

		pos_tf = new JTextField();
		pos_tf.setBounds(27, 428, 309, 36);
		pos_pane.add(pos_tf);
		pos_tf.setColumns(10);

		pos_label.setFont(new Font("����", Font.PLAIN, 13));
		pos_label.setBounds(859, 10, 98, 24);
		pos_pane.add(pos_label);

		JLabel label_1 = new JLabel("�� ����");
		label_1.setBounds(32, 390, 43, 15);
		pos_pane.add(label_1);

		cnt_tf.setEditable(false);
		cnt_tf.setBounds(82, 375, 138, 43);
		cnt_tf.setText("0");
		pos_pane.add(cnt_tf);

		pos_GUI.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e1) {
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e1) {
					}
				}
				pos_GUI.setVisible(false);
				pos_GUI.dispose();
			}
		});
	}

	private void sales_init() {

		sales_GUI.setTitle("�Ǹ���Ȳ");
		sales_GUI.setBounds(100, 100, 456, 367);
		sales_pane = new JPanel();
		sales_pane.setBorder(new EmptyBorder(5, 5, 5, 5));
		sales_GUI.setContentPane(sales_pane);
		sales_pane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 26, 415, 206);
		sales_pane.add(scrollPane);

		sales_tb = new JTable();
		scrollPane.setViewportView(sales_tb);
		sales_createTableModel();
		sales_tb.getTableHeader().setReorderingAllowed(false);
		sales_tb.getTableHeader().setResizingAllowed(false);
		sales_tb.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // �� �ϳ��� ���õǵ���

		JLabel lblNewLabel = new JLabel("�Ѱ�");
		lblNewLabel.setFont(new Font("����", Font.BOLD, 14));
		lblNewLabel.setBounds(234, 247, 53, 15);
		sales_pane.add(lblNewLabel);

		sum_tf.setBounds(288, 239, 139, 32);
		sum_tf.setEditable(false);
		sales_pane.add(sum_tf);

		JLabel label = new JLabel("�� ����");
		label.setFont(new Font("����", Font.BOLD, 14));
		label.setBounds(22, 247, 53, 15);
		sales_pane.add(label);

		amt_tf.setColumns(10);
		amt_tf.setBounds(81, 239, 124, 32);
		amt_tf.setEditable(false);
		sales_pane.add(amt_tf);

		close_btn.setBounds(148, 288, 139, 32);
		sales_pane.add(close_btn);

		sales_GUI.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				sales_GUI.setVisible(false);
				sales_GUI.dispose();
			}
		});
	}

	private void login_init() {
		login_GUI.setTitle("�α���");
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

		login_GUI.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				login_GUI.dispose();

			}
		});
	}

	private void start() {
		add_btn.addActionListener(this);// ������ǰ�԰�
		new_btn.addActionListener(this);// ����ǰ�԰�
		delgoods_btn.addActionListener(this);// ��ǰ ����
		newok_btn.addActionListener(this);// ����ǰ�԰�Ȯ��
		addok_btn.addActionListener(this);// ������ǰ�԰�Ȯ��
		delete_btn.addActionListener(this);// ���û�ǰ����
		update_btn.addActionListener(this);// Ȯ�ι�ư
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

	private void createnew_btn(String name, String ret, int count) { // ���α׷� ������ �� ���� ���Ӱ� ��ư ���������� �̺�Ʈ�� ���� �ٿ��ְ� �гο� ����
		if ("����".equals(ret)) {

			eye++;
			list_eye.add(new JButton("" + eye));

			JButton button = list_eye.get(eye - 1);
			button.setText(name);
			cnt_list.add(new Curcount(name));
			button.addActionListener(new EventHandler());
			panel_eye.add(button);

			if (count == 0)
				button.setEnabled(false);

			int cr = (int) Math.sqrt(eye);
			System.out.println(eye);
			if (cr * cr < eye) {
				panel_eye.setLayout(new GridLayout(cr + 1, cr + 1, 10, 10));
			} else if (cr * cr == eye) {
				panel_eye.setLayout(new GridLayout(cr, cr, 10, 10));
			}
			panel_eye.revalidate();
			panel_eye.repaint();
		}

		else if ("��".equals(ret)) {

			lip++;
			list_lip.add(new JButton("" + lip));

			JButton button = list_lip.get(lip - 1);
			button.setText(name);
			cnt_list.add(new Curcount(name));
			button.addActionListener(new EventHandler());
			panel_lip.add(button);

			if (count == 0)
				button.setEnabled(false);
			
			int cr = (int) Math.sqrt(lip);
			if (cr * cr < lip) {
				panel_lip.setLayout(new GridLayout(cr + 1, cr + 1, 10, 10));
			} else if (cr * cr == lip) {
				panel_lip.setLayout(new GridLayout(cr, cr, 10, 10));
			}
			panel_lip.revalidate();
			panel_lip.repaint();

		}

		else if ("���̽�".equals(ret)) {
			
			face++;
			list_face.add(new JButton("" + face));

			JButton button = list_face.get(face - 1);
			button.setText(name);
			cnt_list.add(new Curcount(name));
			button.addActionListener(new EventHandler());
			panel_face.add(button);
			
			if (count == 0)
				button.setEnabled(false);

			int cr = (int) Math.sqrt(face);
			if (cr * cr < face) {
				panel_face.setLayout(new GridLayout(cr + 1, cr + 1, 10, 10));
			} else if (cr * cr == face) {
				panel_face.setLayout(new GridLayout(cr, cr, 10, 10));
			}
			panel_face.revalidate();
			panel_face.repaint();
		} 
		else if ("��Ų�ɾ�".equals(ret)) {
			
			care++;
			list_care.add(new JButton("" + care));

			JButton button = list_care.get(care - 1);
			button.setText(name);
			cnt_list.add(new Curcount(name));
			button.addActionListener(new EventHandler());
			panel_care.add(button);
			
			if (count == 0)
				button.setEnabled(false);

			int cr = (int) Math.sqrt(care);
			if (cr * cr < care) {
				panel_care.setLayout(new GridLayout(cr + 1, cr + 1, 10, 10));
			} else if (cr * cr == care) {
				panel_care.setLayout(new GridLayout(cr, cr, 10, 10));
			}
			panel_care.revalidate();
			panel_care.repaint();

		}

	}

	private void get_btn_eye() {
		String query_10 = " select goods_nm,cur_count from TB_storage where ret_cd=10";
		try {
			rs = stmt.executeQuery(query_10);
			while (rs.next()) {
				String name = rs.getString("goods_nm");
				int ct = rs.getInt("cur_count");

				list_eye.add(new JButton("" + eye));// ��ư����
				JButton button = list_eye.get(eye);
				button.setText(name);
				button.addActionListener(new EventHandler());
				panel_eye.add(button);// ������ ��ư �гο� �߰�

				if (ct == 0)
					button.setEnabled(false);

				cnt_list.add(new Curcount(name));// Curcount Ŭ���� ����
				eye++;
				System.out.println(eye);
				

				int cr = (int) Math.sqrt(eye);// �׸��� ���̾ƿ� (�������� ���� )
				if (cr * cr < eye) {// ���� ���� ����ϴ� ��ư ������ �������� �۰ų� ���� ��
					panel_eye.setLayout(new GridLayout(cr + 1, cr + 1, 10, 10));// �״�� �ᵵ ��
				} else if (cr * cr == eye) {// �ٵ� ũ�� ������� ��ư ���� 26�� �϶� cr�� 5����, 25�� 26���� �����ϱ� ��ĭ �÷��� ���ִ°�
					panel_eye.setLayout(new GridLayout(cr, cr, 10, 10));
					// �̰Ŵ� ������ �׸���� �ϴ°� ������ ���������� ��ư������ ���� ��ȭ��Ű�°� ������ ���,,,
				}
			} // ���� ��ư ��
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "�˸�", JOptionPane.WARNING_MESSAGE);// ����ó��
		}
	}

	private void get_btn_lip() {
		String query_20 = " select goods_nm,cur_count from TB_storage where ret_cd=20";
		try {
			rs = stmt.executeQuery(query_20);
			while (rs.next()) {
				String name = rs.getString("goods_nm");
				int ct = rs.getInt("cur_count");

				list_lip.add(new JButton("" + lip));// ��ư����
				JButton button = list_lip.get(lip);
				button.setText(name);
				button.addActionListener(new EventHandler());
				panel_lip.add(button);// ������ ��ư �гο� �߰�

				if (ct == 0)
					button.setEnabled(false);

				cnt_list.add(new Curcount(name));
				lip++;// �̰� ���� ��ư �������� �Ѱ� ũ��(1���� �����ؼ� ��ư ���� �� �̸� 1�� ����)
				System.out.println(lip);

				int cr = (int) Math.sqrt(lip);// �׷��� l �� ����ϴ°�
				if (cr * cr < lip) {
					panel_lip.setLayout(new GridLayout(cr + 1, cr + 1, 10, 10));
				} else if (cr * cr == lip) {
					panel_lip.setLayout(new GridLayout(cr, cr, 10, 10));
				}
			} // �� ��ư ��
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "�˸�", JOptionPane.WARNING_MESSAGE);// ����ó��
		}
	}

	private void get_btn_face() {
		String query_30 = " select goods_nm,cur_count from TB_storage where ret_cd=30";
		try {
			rs = stmt.executeQuery(query_30);
			while (rs.next()) {
				String name = rs.getString("goods_nm");
				int ct = rs.getInt("cur_count");

				list_face.add(new JButton("" + face));// ��ư����
				JButton button = list_face.get(face);
				button.setText(name);
				button.addActionListener(new EventHandler());
				panel_face.add(button);// ������ ��ư �гο� �߰�

				if (ct == 0)
					button.setEnabled(false);

				cnt_list.add(new Curcount(name));
				face++;
				System.out.println(face);

				int cr = (int) Math.sqrt(face);
				if (cr * cr < face) {
					panel_face.setLayout(new GridLayout(cr + 1, cr + 1, 10, 10));
				} else if (cr * cr == face) {
					panel_face.setLayout(new GridLayout(cr, cr, 10, 10));
				}
			} // ���̽� ��ư ��
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "�˸�", JOptionPane.WARNING_MESSAGE);// ����ó��
		}
	}

	private void get_btn_care() {
		String query_40 = " select goods_nm, cur_count from TB_storage where ret_cd=40";
		try {
			rs = stmt.executeQuery(query_40);
			while (rs.next()) {
				String name = rs.getString("goods_nm");
				int ct = rs.getInt("cur_count");

				list_care.add(new JButton("" + care));// ��ư����
				JButton button = list_care.get(care);
				button.setText(name);
				button.addActionListener(new EventHandler());
				panel_care.add(button);// ������ ��ư �гο� �߰�

				if (ct == 0)
					button.setEnabled(false);

				cnt_list.add(new Curcount(name));
				care++;
				System.out.println(care);


				int cr = (int) Math.sqrt(care);
				if (cr * cr < care) {
					panel_care.setLayout(new GridLayout(cr + 1, cr + 1, 10, 10));
				} else if (cr * cr == care) {
					panel_care.setLayout(new GridLayout(cr, cr, 10, 10));
				}
			} // ��Ų�ɾ� ��ư ��
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "�˸�", JOptionPane.WARNING_MESSAGE);// ����ó��
		}
	}

	private void get_goods(String name) {

		String query = "select goods_nm, value from TB_storage where goods_nm='" + name + "'";
		System.out.println(query);
		try {
			rs = stmt.executeQuery(query);

			if (rs.next()) {
				String goods_nm = rs.getString("goods_nm");
				int value = rs.getInt("value");

				if (goods_list.contains(name)) {
					int idx = goods_list.indexOf(name);
					int change_count = (int) pos_tb.getValueAt(idx, 2);
					int plus_count = change_count + 1;//
					int plus_value = value * plus_count;

					pos_tb.setValueAt(plus_count, idx, 2);
					pos_tb.setValueAt(plus_value, idx, 3);

					int val = Integer.parseInt(price_tf.getText());
					value += val;
					int cnt = Integer.parseInt(cnt_tf.getText()) + 1;
					price_tf.setText(Integer.toString(value));
					cnt_tf.setText(Integer.toString(cnt));
				} else {
					goods_list.add(name);
					Object[] row = { i, goods_nm, count, new Integer(value) };
					pos_md.addRow(row);
					i++;

					int val = Integer.parseInt(price_tf.getText());
					value += val;
					int cnt = Integer.parseInt(cnt_tf.getText()) + 1;
					price_tf.setText(Integer.toString(value));
					cnt_tf.setText(Integer.toString(cnt));
				}

			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "�˸�", JOptionPane.WARNING_MESSAGE);// ����ó��
		}
	}

	public static void main(String[] args) {
		new Client();
	}

	class Curcount {// ��ǰ���� ���� ������ Ŭ����, ��������ó�� ���ؼ� �������
		String name;
		int cur_cnt = 0;

		Curcount(String name) {
			this.name = name;
		}
	}

	private void set_count(String name, int cnt) {// ��ǰ ���� ī��Ʈ ���� �޼ҵ�
		String query = "select cur_count, ret_cd from TB_storage where goods_nm='" + name + "'";
		System.out.println(query);
		try {
			rs = stmt.executeQuery(query);

			if (rs.next()) {
				int cur_count = rs.getInt("cur_count");
				int retcd = rs.getInt("ret_cd");

				for (int k = 0; k < cnt_list.size(); k++) {
					Curcount c = (Curcount) cnt_list.get(k);
					if (c.name.equals(name)) {
						c.cur_cnt += cnt;// �ֹ�����Ʈ�� ��� ��ǰ�� ���� ����

						if (retcd == 10) {
							for (int i = 0; i < list_eye.size(); i++) {
								JButton btn = list_eye.get(i);
								if (name.equals(btn.getText())) {
									if (cur_count - c.cur_cnt == 0) {
										btn.setEnabled(false);
									} else {
										btn.setEnabled(true);
									}
								}
							}
						} else if (retcd == 20) {
							for (int i = 0; i < list_lip.size(); i++) {
								JButton btn = list_lip.get(i);
								if (name.equals(btn.getText())) {
									if (cur_count - c.cur_cnt == 0) {
										btn.setEnabled(false);
									} else {
										btn.setEnabled(true);
									}
								}
							}
						} else if (retcd == 30) {
							for (int i = 0; i < list_face.size(); i++) {
								JButton btn = list_face.get(i);
								if (name.equals(btn.getText())) {
									if (cur_count - c.cur_cnt == 0) {
										btn.setEnabled(false);
									} else {
										btn.setEnabled(true);
									}
								}
							}
						} else if (retcd == 40) {
							for (int i = 0; i < list_care.size(); i++) {
								JButton btn = list_care.get(i);
								if (name.equals(btn.getText())) {
									if (cur_count - c.cur_cnt == 0) {
										btn.setEnabled(false);
									} else {
										btn.setEnabled(true);
									}
								}
							}
						}
					}
				} // for�� ��

			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "�˸�", JOptionPane.WARNING_MESSAGE);// ����ó��
		}
	}

	class EventHandler extends JFrame implements ActionListener {// ����������ư������ ����

		public void actionPerformed(ActionEvent e) {
			get_goods(e.getActionCommand());
			String name = (String) e.getActionCommand();
			set_count(name, 1);

		}
	}

	private void delete_btn(String name, String ret) {// �з��� ��ư �̸����� �гο� ��ư ����
		if ("����".equals(ret)) {

			for (int i = 0; i < list_eye.size(); i++) {
				JButton button = list_eye.get(i);
				if (name.equals(button.getText())) {
					panel_eye.remove(button);// �гο����� �����
					list_eye.remove(i);// ����Ʈ������ ����
					eye--;// �� �� ��ư �����Ҷ� ���
				}
			}
			panel_eye.revalidate();
			panel_eye.repaint();// �̰� �ΰ��� �г� �������ִ°ŷ��ٵ� ��Ȯ�� ���� ������ �𸣰ڼ������ؾߵɵ�
		}

		if ("��".equals(ret)) {

			for (int i = 0; i < list_lip.size(); i++) {
				JButton button = list_lip.get(i);
				if (name.equals(button.getText())) {
					panel_lip.remove(button);
					list_lip.remove(i);
					lip--;// �� �� ��ư �����Ҷ� ���
				}
			}
			panel_lip.revalidate();
			panel_lip.repaint();
		}

		if ("���̽�".equals(ret)) {

			for (int i = 0; i < list_face.size(); i++) {
				JButton button = list_face.get(i);
				if (name.equals(button.getText())) {
					panel_face.remove(button);
					list_face.remove(i);
					face--;// �� �� ��ư �����Ҷ� ���
				}
			}
			panel_face.revalidate();
			panel_face.repaint();
		}

		if ("��Ų�ɾ�".equals(ret)) {

			for (int i = 0; i < list_care.size(); i++) {
				JButton button = list_care.get(i);
				if (name.equals(button.getText())) {
					panel_care.remove(button);
					list_care.remove(i);
					care--;// �� �� ��ư �����Ҷ� ���
				}
			}
			panel_care.revalidate();
			panel_care.repaint();
		}

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == login_btn) {
			System.out.println("--------------------------");
			if (id_tf.getText().length() == 0 && password_tf.getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "����� �н����带 �Է��ϼ���.", "�˸�", JOptionPane.WARNING_MESSAGE);
			} else if (id_tf.getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "����� �Է��ϼ���.", "�˸�", JOptionPane.WARNING_MESSAGE);
			} else if (password_tf.getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "�н����带 �Է��ϼ���.", "�˸�", JOptionPane.WARNING_MESSAGE);// ����ó��
			}

			else {
				id = Integer.parseInt(id_tf.getText().trim());
				pass = password_tf.getText().trim();

				System.out.println(" ��� : " + id + " | �н����� : " + pass);

				String query = "SELECT *FROM TB_membership" + " WHERE id=" + id + " AND password='" + pass + "'";

				System.out.println(query);

				try {
					rs = stmt.executeQuery(query);

					if (rs.next()) {
						String name = rs.getString("member_nm");
						String pw = rs.getString("password");					
						int number = rs.getInt("id");
						System.out.printf("�̸� : %s \n", name);
						System.out.printf("�н����� : %s \n", pw);

						if ((pass.equals(pw)) && (id == number)) {

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
					JOptionPane.showMessageDialog(null, e1.getMessage(), "�˸�", JOptionPane.WARNING_MESSAGE);// ����ó��
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
			
			try{
			String ret_nm = new_cmb.getSelectedItem().toString();// �޺��ڽ����� ���õ� ��
			System.out.printf("%s\n", ret_nm);

			int ret_cd = get_retcode(ret_nm);
			String name = name_tf.getText().trim();
			name=name.replace(" ","");
			String code = number_tf.getText().trim();
			int count = Integer.parseInt(count_tf.getText().trim());
			int value = Integer.parseInt(value_tf.getText().trim());

			String query_check = "SELECT * FROM TB_storage where  goods_cd='" + code + "' OR goods_nm='" + name + "'";
			try {
				rs = stmt.executeQuery(query_check);

				if (rs.next()) {
					JOptionPane.showMessageDialog(null, "���� ��ǰ�� ��ġ�մϴ�.\n" + "'���� ��ǰ �԰�' ��ư���� ó�����ּ���. ", "�˸�",
							JOptionPane.WARNING_MESSAGE);
				} else {
					try {
						String query_insert = "insert into TB_storage values ( '" + code + "', " + ret_cd + ", '" + name
								+ "', " + value + ", " + count + ");";
						System.out.printf("%s\n", query_insert);

						int col_count = stmt.executeUpdate(query_insert);// ��񳻿������Ʈ
						System.out.println("����� �� ���� : " + col_count);

						createnew_btn(name, ret_nm, count);

						JOptionPane.showMessageDialog(null, "�� ��ǰ�� ��ϵǾ����ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(null, "insert���� : " + e1.getMessage(), "�˸�",
								JOptionPane.WARNING_MESSAGE);// ����ó��
					}
				}
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(null, "select���� : " + e1.getMessage(), "�˸�", JOptionPane.WARNING_MESSAGE);// ����ó��
			}
			}catch(NumberFormatException notNumber) {
					JOptionPane.showMessageDialog(null, notNumber.getMessage(), "�˸�", JOptionPane.INFORMATION_MESSAGE);
			}

			String retrieval = inven_cmb.getSelectedItem().toString();// �޺��ڽ����� ���õ� ��
			int ret_cd2 = get_retcode(retrieval);
			if (ret_cd2 != 0) {
				inventory_md.setRowCount(0);
				setSelectTable_inven(ret_cd2);
			} else {
				inventory_md.setRowCount(0);
				setTable_inven();
			}
			name_tf.setText("");
			number_tf.setText("");
			count_tf.setText("");
			value_tf.setText("");

			System.out.println("�� ��ǰ �԰� Ȯ�� ��ư Ŭ��\n");
			System.out.println("--------------------------");
		}

		else if (e.getSource() == addok_btn) {
			System.out.println("--------------------------");
			
			try{
			String code = number_tf2.getText().trim();// ��ǰ��ȣ
			int count = Integer.parseInt(count_tf2.getText().trim());// ��ǰ����
			
			String query = "select goods_cd, goods_nm from TB_storage where goods_cd='" + code + "';";
			System.out.printf("%s \n", query);

			try {
				rs = stmt.executeQuery(query);// ���� �ִ��� Ȯ��

				if (rs.next()) {// ������ �Ѱ�, ���� while���� �ʾƵ� ��
					try {
						String query_update = "update TB_storage set cur_count = ((select cur_count from TB_storage "
								+ "where goods_cd= '" + code + "')+" + count + ") where goods_cd = '" + code + "';";
						System.out.printf("%s \n", query_update);

						int col_count2 = stmt.executeUpdate(query_update);
						System.out.println("����� �� ���� : " + col_count2);

						String query_btn = "select goods_nm, cur_count from TB_storage where goods_cd='" + code + "';";

						rs = stmt.executeQuery(query_btn);

						if (rs.next()) {
							String name = rs.getString("goods_nm");
							int update_cnt = rs.getInt("cur_count");

							if (update_cnt > 0) {
								for (int i = 0; i < list_eye.size(); i++) {
									JButton btn = list_eye.get(i);
									if (name.equals(btn.getText()))
										btn.setEnabled(true);
								}
								for (int i = 0; i < list_lip.size(); i++) {
									JButton btn = list_lip.get(i);
									if (name.equals(btn.getText()))
										btn.setEnabled(true);
								}
								for (int i = 0; i < list_face.size(); i++) {
									JButton btn = list_face.get(i);
									if (name.equals(btn.getText()))
										btn.setEnabled(true);
								}
								for (int i = 0; i < list_care.size(); i++) {
									JButton btn = list_care.get(i);
									if (name.equals(btn.getText()))
										btn.setEnabled(true);
								}
							}
						}
						JOptionPane.showMessageDialog(null, "��ǰ ������ ����Ǿ����ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(null, "update������ " + e1.getMessage(), "�˸�",
								JOptionPane.WARNING_MESSAGE);// ����ó��
					}
				} else {
					JOptionPane.showMessageDialog(null, "��ġ�ϴ� ������ �����ϴ�.", "�˸�", JOptionPane.WARNING_MESSAGE);
				}
			
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(null, "���� ��ǰ ���� select������  " + e1.getMessage(), "�˸�",
						JOptionPane.WARNING_MESSAGE);// ����ó��
			}
			}
			catch(NumberFormatException notNumber) {
				JOptionPane.showMessageDialog(null, "�ùٸ� ������ �Է����ּ���", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			}

			String retrieval = inven_cmb.getSelectedItem().toString();// �޺��ڽ����� ���õ� ��
			int ret_cd = get_retcode(retrieval);
			if (ret_cd != 0) {
				inventory_md.setRowCount(0);
				setSelectTable_inven(ret_cd);
			} else {
				inventory_md.setRowCount(0);
				setTable_inven();
			}
			number_tf2.setText("");
			count_tf2.setText("");

			System.out.println("���� ��ǰ �԰� Ȯ�� ��ư Ŭ��\n");
			System.out.println("--------------------------");
		}

		else if (e.getSource() == delgoods_btn) {
			System.out.println("--------------------------");
			// ��¥ �����Ұ��� �ѹ� �� Ȯ��
			int ans = JOptionPane.showConfirmDialog(this, "��ǰ�� �����Ͻðڽ��ϱ�?", "confirm", JOptionPane.YES_NO_OPTION);

			if (ans == JOptionPane.YES_OPTION) {
				// ��ǰ��ȣ ���� ���õ� ���� ��
				int row = inventory_tb.getSelectedRow();// �� ����

				if (row >= 0) {// ���õ� ���� ���� ���

					String code = inventory_tb.getValueAt(row, 0).toString();// ��ǰ��ȣ ��������
					String name = inventory_tb.getValueAt(row, 1).toString();// ��ǰ �̸� ��������
					String ret_nm = "";

					try {
						String query = "select ret_nm from TB_retrieval where ret_cd=(select ret_cd from TB_storage where goods_cd='"
								+ code + "')";
						rs = stmt.executeQuery(query);
						if (rs.next()) {
							ret_nm = rs.getString("ret_nm");
						}

						String query_delete = "delete from TB_storage where goods_cd='" + code + "'";
						System.out.printf("%s \n", query_delete);
						try {

							int col_count = stmt.executeUpdate(query_delete);
							System.out.println("����� �� ���� : " + col_count);

							delete_btn(name, ret_nm);
							
							for(int i=0;i<cnt_list.size();i++) {
								Curcount c= (Curcount)cnt_list.get(i);
								if(name.equals(c.name))
									cnt_list.remove(i);
							}
							
						} catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "delete������ " + e1.getMessage(), "�˸�",
									JOptionPane.WARNING_MESSAGE);// ����ó��

						}

					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(null, "select������ " + e1.getMessage(), "�˸�",
								JOptionPane.WARNING_MESSAGE);// ����ó��
					}
				} else {
					JOptionPane.showMessageDialog(null, "���õ� ���� �����ϴ�.", "�˸�", JOptionPane.WARNING_MESSAGE);
				}
			} else {
			}
			String retrieval = inven_cmb.getSelectedItem().toString();// �޺��ڽ����� ���õ� ��
			int ret_cd = get_retcode(retrieval);
			if (ret_cd != 0) {
				inventory_md.setRowCount(0);
				setSelectTable_inven(ret_cd);
			} else {
				inventory_md.setRowCount(0);
				setTable_inven();
			}
			System.out.println("��ǰ ���� ��ư Ŭ��\n");
			System.out.println("--------------------------");

		}

		else if (e.getSource() == delete_btn) {
			System.out.println("--------------------------");
			// i�� ���� �Ѱ� �� ����
			int row = pos_tb.getSelectedRow();// �� ���� //9
			if (row >= 0) {
				int val_org = (int) pos_tb.getValueAt(row, 3);// ���� ������ ������ !
				int cnt_org = (int) pos_tb.getValueAt(row, 2);// ���� ���� !
				String name = (String) pos_tb.getValueAt(row, 1);// �̸� !
				int j = i - row - 2;// ������ �� ������ ��ǰ ��ȣ ���� ����//9
				i = row + 1;// ������ �� ������ ��ǰ��ȣ ���� ���� ! i�� ���������� ����

				String delete_goods = (String) pos_tb.getValueAt(row, 1);
				goods_list.removeElement(delete_goods);
				pos_md.removeRow(row);
				for (int k = 0; k < j; k++, i++, row++) {
					pos_tb.setValueAt(i, row, 0);
				}

				set_count(name, -cnt_org);

				int cal = Integer.parseInt(price_tf.getText());
				int cnt = Integer.parseInt(cnt_tf.getText());
				cal = cal - val_org;
				cnt = cnt - cnt_org;
				price_tf.setText(Integer.toString(cal));
				cnt_tf.setText(Integer.toString(cnt));

			} else {
				JOptionPane.showMessageDialog(null, "���õ� ��ǰ�� �����ϴ�.", "�˸�", JOptionPane.WARNING_MESSAGE);
			}

			System.out.println("���� ��ǰ ���� ��ư Ŭ��\n");
			System.out.println("--------------------------");
		}

		else if (e.getSource() == pay_btn) {
			System.out.println("--------------------------");

			int u = pos_tb.getRowCount();
			if (u == 0) {
				JOptionPane.showMessageDialog(null, "�ֹ�����Ʈ�� ��� ��ǰ�� �����ϴ�.", "�˸�", JOptionPane.WARNING_MESSAGE);
			} else {
				for (int k = 0; k < u; k++) {
					String name = (String) pos_tb.getValueAt(k, 1);
					int count = (int) pos_tb.getValueAt(k, 2);
					System.out.println("�Է¼��� : " + count);
					int value = (int) pos_tb.getValueAt(k, 3);
					int col_count;

					try {
						String query = "insert into TB_outgoods(goods_cd,qty,amount,id,sale_dt) values ((select goods_cd from TB_storage where goods_nm= '"
								+ name + "'), " + value + ", " + count + ", " + id + ", (select convert(varchar,getdate(),20)))";
						System.out.printf("%s \n", query);

						col_count = stmt.executeUpdate(query);
						System.out.println("����� �� ���� : " + col_count);
						// ���� ���̺� �μ�Ʈ

						try {
							String query_count = "update TB_storage set cur_count = ((select cur_count from TB_storage where goods_nm='"
									+ name + "')-" + count + ") where goods_nm='" + name + "'";
							System.out.printf("%s \n", query_count);

							col_count = stmt.executeUpdate(query_count);// ��� ���̺� ���� ����
							System.out.println("����� �� ���� : " + col_count);

							for (int n = 0; n < cnt_list.size(); n++) {
								Curcount c = (Curcount) cnt_list.get(n);
								if (goods_list.contains(c.name)) { // �ֹ�����Ʈ�� �ִ� ��ǰ�� ��
									c.cur_cnt = 0; // ī��Ʈ 0���� �ʱ�ȭ
									System.out.println("��ǰ�̸� : " + c.name + " | ��ǰ�ֹ�����Ʈī��Ʈ : " + c.cur_cnt);
								}
							}

						} catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "update������ " + e1.getMessage(), "�˸�",
									JOptionPane.WARNING_MESSAGE);
						}
					} catch (SQLException e2) {
						JOptionPane.showMessageDialog(null, "insert������ " + e2.getMessage(), "�˸�",
								JOptionPane.WARNING_MESSAGE);
					}
				}

				pos_md.setRowCount(0);
				goods_list.removeAllElements();
				price_tf.setText("0");
				cnt_tf.setText("0");

				i = 1;// �ֹ�����Ʈ ���� ó������
			}
			System.out.println("���� ��ư Ŭ��\n");
			System.out.println("--------------------------");
		}

		else if (e.getSource() == reset_btn) {
			System.out.println("--------------------------");

			for (int k = 0; k < cnt_list.size(); k++) {
				Curcount c = (Curcount) cnt_list.get(k);
				if (goods_list.contains(c.name)) { // ��� ��ư�� ������ �� �ֹ�����Ʈ�� �ִ� ��ǰ�̶��
					c.cur_cnt = 0; // ī��Ʈ 0���� �ʱ�ȭ

					for (int i = 0; i < list_eye.size(); i++) {
						JButton btn = list_eye.get(i);
						if (c.name.equals(btn.getText())) // �� ��ǰ�� ��ư �̸��̶� ���ٸ�
							btn.setEnabled(true); // ���� ��ư �ٽ� Ȱ��ȭ
					}
					for (int i = 0; i < list_lip.size(); i++) {
						JButton btn = list_lip.get(i);
						if (c.name.equals(btn.getText()))
							btn.setEnabled(true);
					}
					for (int i = 0; i < list_face.size(); i++) {
						JButton btn = list_face.get(i);
						if (c.name.equals(btn.getText()))
							btn.setEnabled(true);
					}
					for (int i = 0; i < list_care.size(); i++) {
						JButton btn = list_care.get(i);
						if (c.name.equals(btn.getText()))
							btn.setEnabled(true);
					}
				}
			}

			i = 1;
			goods_list.removeAllElements();
			pos_md.setRowCount(0);
			price_tf.setText("0");
			cnt_tf.setText("0");

			System.out.println("���� ��� ��ư Ŭ��\n");
			System.out.println("--------------------------");

		}

		else if (e.getSource() == input_btn) {// ��������
			System.out.println("--------------------------");

			int row = pos_tb.getSelectedRow();
			if (row >= 0) {// ���õ� ���� ���� ���
				try {
					int edit = Integer.parseInt(pos_tf.getText());
					String name = (String) pos_tb.getValueAt(row, 1);
					int val_org = (int) pos_tb.getValueAt(row, 3);// ���� ������ ������ !
					int cnt_org = (int) pos_tb.getValueAt(row, 2);// ���� ���� !
					int plus_value = 0;// ������ ����

					String query = "select goods_nm, value, cur_count from TB_storage where goods_nm='" + name + "'";
					try {
						rs = stmt.executeQuery(query);
						if (rs.next()) {
							int cur_count = rs.getInt("cur_count");

							if (edit > cur_count) {
								JOptionPane.showMessageDialog
								(null, "�ش� ��ǰ�� ��� �����մϴ�.\n ���� ���� : " + cur_count + "��",
										"�˸�", JOptionPane.WARNING_MESSAGE);
								pos_tf.setText("");
							} else {
								int value = rs.getInt("value");// ������ ��ǰ����
								plus_value = edit * value;
								pos_tb.setValueAt(plus_value, row, 3);
								pos_tb.setValueAt(edit, row, 2);

								System.out.printf("%d", pos_tb.getValueAt(row, 2));
								pos_tf.setText("");

								int cn = edit - cnt_org;// ���ҽ�ų�� ������ �ȴ�
								set_count(name, cn);

								// ���հ�����
								int cal = Integer.parseInt(price_tf.getText());
								cal = cal - val_org + plus_value;
								int cnt = Integer.parseInt(cnt_tf.getText());
								cnt = cnt - cnt_org + edit;
								price_tf.setText(Integer.toString(cal));
								cnt_tf.setText(Integer.toString(cnt));

							}
						}
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(null, "select������ " + e1.getMessage(), "�˸�",
								JOptionPane.WARNING_MESSAGE);
					}

				} catch (NumberFormatException notNumber) {
					JOptionPane.showMessageDialog(null, "�ùٸ� ������ �Է����ּ���.", "�˸�", JOptionPane.WARNING_MESSAGE);
					pos_tf.setText("");
				}

			} else {
				JOptionPane.showMessageDialog(null, "��ǰ�� �������ּ���", "�˸�", JOptionPane.WARNING_MESSAGE);
				pos_tf.setText("");
			}
			System.out.println("�������� ��ư Ŭ��\n");
			System.out.println("--------------------------");

		}

		else if (e.getSource() == sales_btn) {
			System.out.println("--------------------------");
			sales_md.setRowCount(0);
			setTable_sales();

			int row = sales_tb.getRowCount();
			int cnt = 0, val = 0;
			for (int i = 0; i < row; i++) {
				cnt = cnt + (int) sales_tb.getValueAt(i, 1);
				val = val + (int) sales_tb.getValueAt(i, 2);
			}
			sum_tf.setText(Integer.toString(val));
			amt_tf.setText(Integer.toString(cnt));

			sales_GUI.setVisible(true);

			System.out.println("�Ǹ� ��Ȳ Ȯ�� ��ư Ŭ��\n");
			System.out.println("--------------------------");
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

			if (ret_cd != 0) {
				inventory_md.setRowCount(0);
				setSelectTable_inven(ret_cd);
			} else {
				inventory_md.setRowCount(0);
				setTable_inven();
			}

			System.out.println("������Ʈ ��ư Ŭ��\n");
			System.out.println("--------------------------");
		}

		else if (e.getSource() == end_btn) {
			System.out.println("--------------------------");
			int end = JOptionPane.showConfirmDialog
					(null, "�ý����� �����Ͻðڽ��ϱ�?", "���� Ȯ��", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			if (end == 0) {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e1) {
					}
				}
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e1) {
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e1) {
					}
				}
				System.exit(0);
			} 
			
			System.out.println("���� ��ư Ŭ��\n");
			System.out.println("--------------------------");
		}

		else if (e.getSource() == inventory_close_btn) {
			System.out.println("--------------------------");

			inventory_md.setRowCount(0);
			Inventory_GUI.setVisible(false);
			System.out.println("������ â �ݱ� ��ư Ŭ�� \n");
			System.out.println("--------------------------");

		}

		else if (e.getSource() == add_close_btn) {
			System.out.println("--------------------------");

			number_tf2.setText("");
			count_tf2.setText("");
			add_GUI.setVisible(false);

			System.out.println("������ǰ �԰� â �ݱ� ��ư Ŭ��\n");
			System.out.println("--------------------------");
		}

		else if (e.getSource() == new_close_btn) {
			System.out.println("--------------------------");

			name_tf.setText("");
			number_tf.setText("");
			count_tf.setText("");
			value_tf.setText("");
			new_GUI.setVisible(false);

			System.out.println("�� ��ǰ �԰� â �ݱ� ��ư Ŭ��\n");
			System.out.println("--------------------------");
		}

		else if (e.getSource() == close_btn) {
			System.out.println("--------------------------");

			sales_GUI.setVisible(false);

			System.out.println("������Ȳ �ݱ� ��ư Ŭ��\n");
			System.out.println("--------------------------");

		}

	}
}