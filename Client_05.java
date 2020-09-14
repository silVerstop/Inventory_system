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

	// DB연결위한 자원
	private String url = "jdbc:sqlserver://211.177.16.20:1433;DatabaseName=STORAGE;";
	private String user = "sa";
	private String password = "0123";
	private Connection conn = null;// 커넥션 객체
	private Statement stmt = null;// Statement 객체
	private ResultSet rs = null;

	// Inventory_GUI 관련 변수 설정
	private JFrame Inventory_GUI = new JFrame();
	private JPanel Inven_pane;
	private JTable inventory_tb;
	private JComboBox inven_cmb;
	private JButton add_btn = new JButton("기존 상품 입고");
	private JButton new_btn = new JButton("새 상품 입고");
	private JButton delgoods_btn = new JButton("상품 삭제");
	private Vector<String> inventory_col = new Vector<>();
	private Vector<Vector> inventory_list = new Vector<>();
	private DefaultTableModel inventory_md;
	private JButton inventory_close_btn = new JButton("닫기");
	private JButton update_btn = new JButton("확인");

	// new_GUI 관련 변수 설정
	private JFrame new_GUI = new JFrame();
	private JPanel new_pane;
	private JTextField name_tf;
	private JTextField number_tf;
	private JTextField count_tf;
	private JTextField value_tf;
	private JButton newok_btn = new JButton("확인");
	private JComboBox new_cmb;
	private JButton new_close_btn = new JButton("닫기");

	// add_GUI 관련 변수 설정
	private JFrame add_GUI = new JFrame();
	private JPanel add_pane;
	private JTextField number_tf2;
	private JTextField count_tf2;
	private JButton addok_btn = new JButton("확인");
	private JButton add_close_btn = new JButton("닫기");

	// pos_GUI 관련 변수 설정
	private JFrame pos_GUI = new JFrame();
	private Vector<String> pos_col = new Vector<>();// 열이름 저장하는 벡터<String>의미?
	private Vector<Vector> pos_list = new Vector<>();// 이 벡터의 쓰임은 뭔가용
	private DefaultTableModel pos_md;
	private JPanel pos_pane;
	private JTable pos_tb;
	private JTextField pos_tf;
	private JTextArea price_area = new JTextArea();
	private JButton delete_btn = new JButton("선택 상품 삭제");
	private JButton pay_btn = new JButton("결제");
	private JButton reset_btn = new JButton("취소");
	private JButton input_btn = new JButton("수량정정");
	private JButton sales_btn = new JButton("매출 현황");
	private JButton inventory_btn = new JButton("재고 관리");
	private JButton end_btn = new JButton("종료");
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
	private ArrayList<JButton> list_eye = new ArrayList<JButton>(); // 포스기 상품버튼 동적생성
	private ArrayList<JButton> list_lip = new ArrayList<JButton>(); // 포스기 상품버튼 동적생성
	private ArrayList<JButton> list_face = new ArrayList<JButton>(); // 포스기 상품버튼 동적생성
	private ArrayList<JButton> list_care = new ArrayList<JButton>(); // 포스기 상품버튼 동적생성
	private int eye = 1, lip = 1, face = 1, care = 1;// 나중에 버튼 동적 할당 받을 수 있게 할 변수
	private int e = 1, l = 1, f = 1, c = 1;// 버튼 만들어졌을 때, 실제 상품버튼이 되었을 때

	// sales_GUI 관련 변수 설정
	private JFrame sales_GUI = new JFrame();
	private JPanel sales_pane;
	private JTable salse_tb;
	private DefaultTableModel salse_md;
	private Vector<String> sales_col = new Vector<>();
	private Vector<Vector> sales_list = new Vector<>();
	private JTextArea sum_area = new JTextArea();
	private JButton close_btn = new JButton("닫기");

	// login_GUI 관련 변수 설정
	private JFrame login_GUI = new JFrame();
	private JPanel login_pane;
	private JLabel label;
	private JTextField id_tf;
	private JLabel label_1;
	private JTextField password_tf;
	private JButton login_btn = new JButton("로그인");

	// 그 외의 변수들
	private int id;// 사번
	private String pass = "";// 패스워드

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
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");// JDBC드라이버 로드
			System.out.println("드라이버 연결 성공");
			conn = DriverManager.getConnection(url, user, password); // 커넥션 객체 생성

			stmt = conn.createStatement();
			if (conn != null) {
				System.out.println("데이터베이스 연결 성공");
			} else {
				System.out.println("연결 실패");
			}
		} catch (Exception e) {
			System.out.println("데이터 베이스 연결 오류 : " + e.getMessage());
		}
	}

	public void setDB() {// str은 쿼리문
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

	private void setTable_inven() {// 재고관리테이블 set
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

	private void setSelectTable_inven(int code) { // 재고관리테이블 선택항목 set

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

		 int code = 0;//분류코드 받을 변수

		String query = " select ret_cd from TB_retrieval where ret_nm='" + retrieval+"' ";//분류코드 받을 쿼리
		System.out.printf("%s\n", query);

		try {
			rs = stmt.executeQuery(query);
			
			if (rs.next()) {// 분류 코드 있을 경우(10~80일 경우
				code = rs.getInt("ret_cd");// 분류코드받기
				
			}
			else {//전체보기 눌렀을 경우
				code=0;
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("분류코드 : "+code);
		return code;
		
	}
	
 	public void inventory_createTableModel() {
		inventory_col.add("품번");
		inventory_col.add("상품명");
		inventory_col.add("가격");
		inventory_col.add("현재수량");
		inventory_md = new DefaultTableModel(inventory_col, 0);
		inventory_tb.setModel(inventory_md);
	}

	public void pos_createTableModel() {// JTable설정
		pos_col.add("No");
		pos_col.add("상품명");
		pos_col.add("수량");
		pos_col.add("가격");
		pos_md = new DefaultTableModel(pos_col, 0);
		pos_tb.setModel(pos_md);
	}

	public void sales_createTableModel() {
		sales_col.add("순번");
		sales_col.add("상품코드");
		sales_col.add("수량");
		sales_col.add("가격");
		sales_col.add("담당자사번");
		sales_col.add("판매일시");
		salse_md = new DefaultTableModel(sales_col, 0);
		salse_tb.setModel(salse_md);
	}

	private void inventory_init() {// 재고관리GUI

		Inventory_GUI.setTitle("재고관리");
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

		String cbmenu[] = { "전체보기", "아이", "립", "페이스", "스킨케어", "팩/마스크", "클렌징", "바디/헤어", "화장소품" };
		inven_cmb = new JComboBox(cbmenu);
		inven_cmb.setBounds(12, 55, 136, 38);
		Inven_pane.add(inven_cmb);

		add_btn.setBounds(451, 156, 154, 47);
		Inven_pane.add(add_btn);

		new_btn.setBounds(451, 99, 155, 47);
		Inven_pane.add(new_btn);

		delgoods_btn.setBounds(451, 213, 154, 47);
		Inven_pane.add(delgoods_btn);

		update_btn.setFont(new Font("굴림", Font.BOLD, 11));
		update_btn.setBounds(160, 55, 59, 38);
		Inven_pane.add(update_btn);

		inventory_close_btn.setBounds(451, 385, 154, 47);
		Inven_pane.add(inventory_close_btn);

		Inventory_GUI.setVisible(false);

	}

	private void new_init() {// 새상품입고GUI
		new_GUI.setTitle("새 상품 입고");
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

		JLabel label = new JLabel("상품명");
		label.setFont(new Font("굴림", Font.BOLD, 14));
		label.setBounds(25, 53, 60, 35);
		new_pane.add(label);

		JLabel label_1 = new JLabel("품번");
		label_1.setFont(new Font("굴림", Font.BOLD, 14));
		label_1.setBounds(25, 98, 60, 35);
		new_pane.add(label_1);

		number_tf = new JTextField();
		number_tf.setColumns(10);
		number_tf.setBounds(97, 99, 291, 33);
		new_pane.add(number_tf);

		JLabel label_2 = new JLabel("수량");
		label_2.setFont(new Font("굴림", Font.BOLD, 14));
		label_2.setBounds(25, 144, 60, 35);
		new_pane.add(label_2);

		count_tf = new JTextField();
		count_tf.setColumns(10);
		count_tf.setBounds(97, 145, 291, 33);
		new_pane.add(count_tf);

		JLabel label_3 = new JLabel("가격");
		label_3.setFont(new Font("굴림", Font.BOLD, 14));
		label_3.setBounds(25, 195, 60, 35);
		new_pane.add(label_3);

		value_tf = new JTextField();
		value_tf.setColumns(10);
		value_tf.setBounds(97, 196, 291, 33);
		new_pane.add(value_tf);

		newok_btn.setFont(new Font("굴림", Font.BOLD, 14));
		newok_btn.setBounds(73, 250, 135, 43);
		new_pane.add(newok_btn);

		new_close_btn.setBounds(213, 250, 135, 43);
		new_pane.add(new_close_btn);

		String cbmenu[] = { "아이", "립", "페이스", "스킨케어", "팩/마스크", "클렌징", "바디/헤어", "화장소품" };
		new_cmb = new JComboBox(cbmenu);
		new_cmb.setBounds(253, 10, 135, 33);
		new_pane.add(new_cmb);

		new_GUI.setVisible(false);
	}

	private void add_init() {// 기존상품입고GUI
		add_GUI.setTitle("기존 상품 입고");
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

		JLabel lblNewLabel = new JLabel("품번");
		lblNewLabel.setFont(new Font("굴림", Font.BOLD, 14));
		lblNewLabel.setBounds(12, 54, 30, 25);
		add_pane.add(lblNewLabel);

		JLabel label = new JLabel("수량");
		label.setFont(new Font("굴림", Font.BOLD, 14));
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

		pos_GUI.setTitle("포스");
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

		JLabel label = new JLabel("총계");
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

		tabbedPane.addTab("아이", panel_eye);
		tabbedPane.addTab("립", panel_lip);
		tabbedPane.addTab("페이스", panel_face);
		tabbedPane.addTab("스킨케어", panel_care);
		tabbedPane.addTab("팩/마스크", panel_mask);
		tabbedPane.addTab(" 클렌징 ", panel_clean);
		tabbedPane.addTab("바디/헤어", panel_body);
		tabbedPane.addTab("화장소품", panel_ac);

		// 아이

		for (eye = 1; eye <= 16; eye++) {
			list_eye.add(new JButton("" + eye));
		}
		panel_eye.setLayout(new GridLayout(4, 4, 10, 10));// 행4열4간격10씩
		for (int i = 0; i < 16; i++) {
			JButton button = list_eye.get(i);
			panel_eye.add(button);
		}

		for (lip = 1; lip <= 16; lip++) {
			list_lip.add(new JButton("" + lip));
		}
		panel_lip.setLayout(new GridLayout(4, 4, 10, 10));// 행4열4간격10씩
		for (int i = 0; i < 16; i++) {
			JButton button = list_lip.get(i);
			panel_lip.add(button);
		}

		for (face = 1; face <= 16; face++) {
			list_face.add(new JButton("" + face));
		}
		panel_face.setLayout(new GridLayout(4, 4, 10, 10));// 행4열4간격10씩
		for (int i = 0; i < 16; i++) {
			JButton button = list_face.get(i);
			panel_face.add(button);
		}

		for (face = 1; face <= 16; face++) {
			list_face.add(new JButton("" + face));
		}
		panel_face.setLayout(new GridLayout(4, 4, 10, 10));// 행4열4간격10씩
		for (int i = 0; i < 16; i++) {
			JButton button = list_face.get(i);
			panel_face.add(button);
		}

		for (care = 1; care <= 16; care++) {
			list_care.add(new JButton("" + care));
		}
		panel_care.setLayout(new GridLayout(4, 4, 10, 10));// 행4열4간격10씩
		for (int i = 0; i < 16; i++) {
			JButton button = list_care.get(i);
			panel_care.add(button);
		}

		pos_tf = new JTextField();
		pos_tf.setBounds(27, 428, 309, 36);
		pos_pane.add(pos_tf);
		pos_tf.setColumns(10);

		pos_label.setFont(new Font("굴림", Font.PLAIN, 13));
		pos_label.setBounds(859, 10, 98, 24);
		pos_pane.add(pos_label);

		pos_GUI.setVisible(false);
	}

	private void sales_init() {

		sales_GUI.setTitle("판매현황");
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

		JLabel lblNewLabel = new JLabel("총계");
		lblNewLabel.setFont(new Font("굴림", Font.BOLD, 14));
		lblNewLabel.setBounds(12, 242, 76, 15);
		sales_pane.add(lblNewLabel);

		sum_area.setBounds(99, 237, 328, 25);
		sales_pane.add(sum_area);

		close_btn.setBounds(148, 272, 139, 32);
		sales_pane.add(close_btn);

		sales_GUI.setVisible(false);
	}

	private void login_init() {
		login_GUI.setTitle("로그인");
		login_GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		login_GUI.setBounds(100, 100, 360, 263);
		login_pane = new JPanel();
		login_pane.setBorder(new EmptyBorder(5, 5, 5, 5));
		login_GUI.setContentPane(login_pane);
		login_pane.setLayout(null);

		login_btn.setBounds(122, 158, 104, 37);
		login_pane.add(login_btn);

		label = new JLabel("사번");
		label.setFont(new Font("굴림", Font.PLAIN, 14));
		label.setBounds(24, 44, 80, 29);
		login_pane.add(label);

		id_tf = new JTextField();
		id_tf.setColumns(10);
		id_tf.setBounds(109, 43, 210, 30);
		login_pane.add(id_tf);

		label_1 = new JLabel("패스워드");
		label_1.setFont(new Font("굴림", Font.PLAIN, 14));
		label_1.setBounds(24, 103, 80, 29);
		login_pane.add(label_1);

		password_tf = new JPasswordField();
		password_tf.setColumns(10);
		password_tf.setBounds(109, 102, 210, 30);
		login_pane.add(password_tf);

		login_GUI.setVisible(true);
	}

	private void start() {
		add_btn.addActionListener(this);// 기존상품입고
		new_btn.addActionListener(this);// 새상품입고
		delgoods_btn.addActionListener(this);// 상품 삭제
		newok_btn.addActionListener(this);// 새상품입고확인
		addok_btn.addActionListener(this);// 기존상품입고확인
		delete_btn.addActionListener(this);// 선택상품삭제
		update_btn.addActionListener(this);//확인버튼
		pay_btn.addActionListener(this);// 결제
		reset_btn.addActionListener(this);// 결제취소
		input_btn.addActionListener(this);// 수량정정
		sales_btn.addActionListener(this);// 판매현황확인
		inventory_btn.addActionListener(this);// 재고관리
		end_btn.addActionListener(this);// 프로그램종료
		close_btn.addActionListener(this);// 판매현황종료
		login_btn.addActionListener(this);// 로그인
		inventory_close_btn.addActionListener(this);// 재고관리창닫기
		add_close_btn.addActionListener(this);// 기존상품입고창닫기
		new_close_btn.addActionListener(this);// 새상품입고창닫기
	}

	private void start_goods() {//버튼 16개에 이벤트 설정하기 위한 메소드

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

	private void createbtn_eye() {// 버튼 동적 생성
		eye++;
		list_eye.add(new JButton("" + eye));// 버튼생성
		JButton button = list_eye.get(eye - 1);
		panel_eye.add(button);// 생성된 버튼 패널에 추가
		button.addActionListener(this);// 이 버튼에 이벤트를 어떻게 넣을거지??

		int cr = (int) Math.sqrt(eye);// 그리드 레이아웃 (제곱수에 따라서 )
		panel_eye.setLayout(new GridLayout(cr, cr, 10, 10));

	}

	private void createbtn_lip() {
		lip++;
		list_lip.add(new JButton("" + lip));// 버튼생성
		JButton button = list_lip.get(lip - 1);
		panel_lip.add(button);// 생성된 버튼 패널에 추가

		int cr = (int) Math.sqrt(lip);// 그리드 레이아웃 (제곱수에 따라서 )
		panel_lip.setLayout(new GridLayout(cr, cr, 10, 10));

	}

	private void createbtn_face() {
		face++;
		list_face.add(new JButton("" + face));// 버튼생성
		JButton button = list_face.get(face - 1);
		panel_face.add(button);// 생성된 버튼 패널에 추가

		int cr = (int) Math.sqrt(face);// 그리드 레이아웃 (제곱수에 따라서 )
		panel_face.setLayout(new GridLayout(cr, cr, 10, 10));

	}

	private void createbtn_care() {
		care++;
		list_care.add(new JButton("" + care));// 버튼생성
		JButton button = list_care.get(care - 1);
		panel_care.add(button);// 생성된 버튼 패널에 추가

		int cr = (int) Math.sqrt(care);// 그리드 레이아웃 (제곱수에 따라서 )
		panel_care.setLayout(new GridLayout(cr, cr, 10, 10));

	}
	
	private void get_btnname() {
		//버튼에 이름 가져오는 메소드
		
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
				}//아이 버튼 셋
				
				rs=stmt.executeQuery(query_20);
				while(rs.next()) {
					JButton button = list_lip.get(j);
					button.setText(rs.getString("goods_nm"));
					j++;
				}//립 버튼 셋
				
				rs=stmt.executeQuery(query_30);		
				while(rs.next()) {
					JButton button = list_face.get(k);
					button.setText(rs.getString("goods_nm"));
					k++;
				}//페이스 버튼 셋
				
				rs=stmt.executeQuery(query_40);				
				while(rs.next()) {
					JButton button = list_care.get(n);
					button.setText(rs.getString("goods_nm"));
					n++;
				}//스킨케어 버튼 셋
				
			
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
				JOptionPane.showMessageDialog(null, "사번과 패스워드를 입력하세요.", "알림", JOptionPane.WARNING_MESSAGE);
			} else if (id_tf.getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "사번을 입력하세요.", "알림", JOptionPane.WARNING_MESSAGE);
			} else if (password_tf.getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "패스워드를 입력하세요.", "알림", JOptionPane.WARNING_MESSAGE);//오류처리
			} 
			
			else {	
				id = Integer.parseInt(id_tf.getText().trim());
				pass = password_tf.getText().trim();

				System.out.println(" 사번 : " + id + " | 패스워드 : " + pass);

				String query = "SELECT *FROM TB_membership" + " WHERE id=" + id + " AND password='" + pass + "'";

				System.out.println("\n쿼리내용 : " + query);

				try {
					rs = stmt.executeQuery(query);

					if (rs.next()) {
						String name = rs.getString("member_nm");
						System.out.printf("이름 : %s \n", name);

						String pw = rs.getString("password");
						System.out.printf("패스워드 : %s \n", pw);

						if (pass.equals(pw)) {
							JOptionPane.showMessageDialog(null, name + "님 접속하셨습니다", "알림", JOptionPane.WARNING_MESSAGE);
							login_GUI.setVisible(false);
							pos_GUI.setVisible(true);
							pos_label.setText("담당자 : " + name);
						}
					} else {
						JOptionPane.showMessageDialog(null, "일치하는 정보가 없습니다.", "알림", JOptionPane.WARNING_MESSAGE);
						id_tf.setText("");
						password_tf.setText("");
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}

			System.out.println("로그인 버튼 클릭");
			System.out.println("--------------------------");

		} 
		
		else if (e.getSource() == add_btn) {
			System.out.println("--------------------------");
			System.out.println("기존 상품 입고 버튼 클릭\n");
			System.out.println("--------------------------");
			add_GUI.setVisible(true);
		}
		
		else if (e.getSource() == new_btn) {
			System.out.println("--------------------------");
			System.out.println("새 상품 입고 버튼 클릭\n");
			System.out.println("--------------------------");
			new_GUI.setVisible(true);
		}
		
		else if (e.getSource() == newok_btn) {
			System.out.println("--------------------------");
			String ret_nm = new_cmb.getSelectedItem().toString();// 콤보박스에서 선택된 값
			System.out.printf("%s\n", ret_nm);

			int ret_cd = 0;// 분류코드 받을 변수

			String query = " select ret_cd from TB_retrieval where ret_nm='" + ret_nm + "';";
			System.out.printf("%s\n", query);

			try {
				rs = stmt.executeQuery(query);
				while (rs.next()) {// rs값 한 단어씩 가져옴.(next())
					ret_cd = rs.getInt("ret_cd");// 분류코드받기
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
				int col_count = stmt.executeUpdate(query_insert);// 디비내용업데이트
				System.out.println("적용된 열 갯수 : " + col_count);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			String retrieval = inven_cmb.getSelectedItem().toString();// 콤보박스에서 선택된 값
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

			System.out.println("새 상품 입고 확인 버튼 클릭\n");
			System.out.println("--------------------------");
		} 
		
		else if (e.getSource() == addok_btn) {
			System.out.println("--------------------------");

			String code = number_tf2.getText().trim();//상품번호
			int count = Integer.parseInt(count_tf2.getText().trim());//상품갯수

			String query = "select goods_cd from TB_storage where goods_cd='" + code + "';";
			System.out.printf("%s \n", query);

			
			try {
				rs = stmt.executeQuery(query);//행이 있는지 확인

				if (rs.next()) {// 유일한 한개, 따라서 while쓰지 않아도 됨
					
					String query_update = "update TB_storage set cur_count = ((select cur_count from TB_storage where goods_cd= '"
							+ code + "')+" + count + ") where goods_cd = '" + code + "';";
					System.out.printf("%s \n", query_update);

					int col_count2 = stmt.executeUpdate(query_update);
					System.out.println("적용된 열 갯수 : " + col_count2);

				} else {
					JOptionPane.showMessageDialog(null, "일치하는 정보가 없습니다.", "알림", JOptionPane.WARNING_MESSAGE);

				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			String retrieval = inven_cmb.getSelectedItem().toString();// 콤보박스에서 선택된 값
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


			System.out.println("기존 상품 입고 확인 버튼 클릭\n");
			System.out.println("--------------------------");
		} 
		
		else if (e.getSource() == delgoods_btn) {
			System.out.println("--------------------------");

			//상품번호 열의 선택된 행의 값
			int row = inventory_tb.getSelectedRow();//행 선택
			int col = 0;// 첫번째 열의 값 : 상품번호

			if (row >= 0) {//선택된 행이 있을 경우
				String code = inventory_tb.getValueAt(row, col).toString();// 상품번호 가져오기


				try {
					String query = "delete from TB_storage where goods_cd='" + code + "'";
					System.out.printf("%s \n", query);

					int col_count = stmt.executeUpdate(query);
					System.out.println("적용된 열 갯수 : " + col_count);

				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				String retrieval = inven_cmb.getSelectedItem().toString();// 콤보박스에서 선택된 값
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
				JOptionPane.showMessageDialog(null, "선택된 행이 없습니다.", "알림", JOptionPane.WARNING_MESSAGE);
			}
			System.out.println("상품 삭제 버튼 클릭\n");
			System.out.println("--------------------------");

		} 
		
		else if (e.getSource() == delete_btn) {
			System.out.println("--------------------------");
			System.out.println("선택 상품 삭제 버튼 클릭\n");
			System.out.println("--------------------------");
		}
		
		else if (e.getSource() == pay_btn) {

			// 결제시 수량감소 후 0이 되는 상품 삭제하거나 판매불가로 포스에서 빼는거
			System.out.println("결제 버튼 클릭\n");
		}
		
		else if (e.getSource() == reset_btn) {
			System.out.println("결제 취소 버튼 클릭\n");
		}
		
		else if (e.getSource() == input_btn) {
			System.out.println("수량정정 버튼 클릭\n");
		}
		
		else if (e.getSource() == sales_btn) {
			System.out.println("판매 현황 확인 버튼 클릭\n");
			sales_GUI.setVisible(true);
		} 
		
		else if (e.getSource() == inventory_btn) {
			System.out.println("--------------------------");

			Inventory_GUI.setVisible(true);
			setTable_inven();
			System.out.println("재고관리 버튼 클릭\n");
			System.out.println("--------------------------");

		} 
		
		else if (e.getSource() == update_btn) {
			
			System.out.println("--------------------------");

			String retrieval = inven_cmb.getSelectedItem().toString();// 콤보박스에서 선택된 값
			int ret_cd = get_retcode(retrieval);// 분류코드 받을 변수
			
			if(ret_cd != 0) {
				inventory_md.setRowCount(0);	
				setSelectTable_inven(ret_cd);
			}
			else {
				inventory_md.setRowCount(0);
				setTable_inven();
			}

			System.out.println("업데이트 버튼 클릭\n");
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
			// 눌린 버튼의 이름과 똑같은 DB데이터 찾아서 주문리스트 테이블에 붙여넣기//getText()쓰면됨

			
		}

		else if (e.getSource() == list_lip.get(0) || e.getSource() == list_lip.get(1)
				|| e.getSource() == list_lip.get(2) || e.getSource() == list_lip.get(3)
				|| e.getSource() == list_lip.get(4) || e.getSource() == list_lip.get(5)
				|| e.getSource() == list_lip.get(6) || e.getSource() == list_lip.get(7)
				|| e.getSource() == list_lip.get(8) || e.getSource() == list_lip.get(9)
				|| e.getSource() == list_lip.get(10) || e.getSource() == list_lip.get(11)
				|| e.getSource() == list_lip.get(12) || e.getSource() == list_lip.get(13)
				|| e.getSource() == list_lip.get(14) || e.getSource() == list_lip.get(15)) {
			// 그럼 자동으로 생긴 버튼에는 이벤트를 어떻게 달건데?

		}

		else if (e.getSource() == end_btn) {
			System.out.println("종료 버튼 클릭\n");
		} else if (e.getSource() == inventory_close_btn) {
			System.out.println("--------------------------");

			inventory_md.setRowCount(0);
			Inventory_GUI.setVisible(false);
			System.out.println("재고관리 창 닫기 버튼 클릭 \n");
			System.out.println("--------------------------");

		} 
		
		else if (e.getSource() == add_close_btn) {
			number_tf2.setText("");
			count_tf2.setText("");
			add_GUI.setVisible(false);
			System.out.println("기존상품 입고 창 닫기 버튼 클릭\n");
		}
		
		else if (e.getSource() == new_close_btn) {
			name_tf.setText("");
			number_tf.setText("");
			count_tf.setText("");
			value_tf.setText("");
			new_GUI.setVisible(false);
			System.out.println("새 상품 입고 창 닫기 버튼 클릭\n");
		}

		else if (e.getSource() == close_btn) {
			sales_GUI.setVisible(false);
			System.out.println("매출현황 닫기 버튼 클릭\n");

		}
	}
}
