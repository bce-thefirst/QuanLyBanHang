package com.market.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.market.controller.HomePageController;
import com.market.jdbc.Connect;

public class HomePageView extends JFrame {
	JTextField txtidProduct, txtQuantity, txtSearch, txtNameProduct, txtNameCus, txtPhoneNumber, txtDOB, txtSex,
			txtAddress, txtIdUser, txtUserName, txtPassWord, txtIdProductDelete;
	JButton btHomePage, btCart, btProfile, btExit, btAddToCart, btSearch, btPay, btSortByName, btSortByLowestPrice,
			btSortByHighestPrice, btSignInDetails, btCusInfor, btPurItems, btDeleteFromCart, btChangePass,
			btChangePass2, btChangeInfor2, btChangeInFor;
	DefaultTableModel dtmProduct, dtmPurItems, dtmWannaBuy, dtmSignInDetails, dtmCusInfor;
	JTable tblProduct, tblPurItems, tblWannaBuy, tblSignInDetails, tblCusInfor;
	JComboBox cbTypeProduct, cbSort;

	private HomePageController hpcl;
//	private SignInController signInController;

	JPanel pnPic;
	JLabel lblPic;

	Connection conn = Connect.getConnect();
	ResultSet result = null;
	PreparedStatement preStatement = null;

	ImageIcon iconHomePage = new ImageIcon(
			new ImageIcon("Image\\HomePage.jpg").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
	ImageIcon iconCart = new ImageIcon(
			new ImageIcon("Image\\Cart.jpg").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
	ImageIcon iconProfile = new ImageIcon(
			new ImageIcon("Image\\Profile.jpg").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));

	String idProduct, quantity;

	Font font = new Font("arial", Font.BOLD, 20);
	Font fontTable = new Font("arial", Font.BOLD, 15);

	public HomePageView() throws SQLException {
		addControls();
	}

	// Hi???n t???t c??? s???n ph???m
	public void displayAllProduct() {
		try {
			String sql = "select * from production.Product";
			Statement statement = conn.createStatement();
			result = statement.executeQuery(sql);
			dtmProduct.setRowCount(0);
			while (result.next()) {
				Vector<Object> vec = new Vector<Object>();
				vec.add(result.getString("idProduct"));
				vec.add(result.getString("nameProduct"));
				vec.add(result.getBigDecimal("price"));
				vec.add(result.getString("unit"));
				dtmProduct.addRow(vec);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// X??c ?????nh t???ng ki???u l???a ch???n ( t??? ch??? s??? chuy???n sang chu???i)
	private String determindType(int type) {
		try {
			String sql = "select * from production.TypeProduct";
			PreparedStatement preStatement = conn.prepareStatement(sql);
			ResultSet result = preStatement.executeQuery();
			for (int i = 0; i <= type; i++) {
				result.next();
			}
			return result.getString("idType"); // tr??? v??? chu???i
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// xac ?????nh t??n lo???i s???n ph???m
	private String getNameType(String string) {
		try {
			String sqlType = "select * from production.TypeProduct where idType = '" + string + "'";
			PreparedStatement pStatement = conn.prepareStatement(sqlType);
			ResultSet rs = pStatement.executeQuery();
			rs.next();
			return rs.getString("nameType");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Hi???n s???n ph???m theo lo???i
	public void displayProductByType(int type) {
		try {
			String sql = "select * from production.Product where type='" + determindType(type - 1) + "'";
			// TYPE - 1: l?? s??? d??ng trong CSDL ( D??ng T???T C??? kh??ng t??nh trong CSDL)
			preStatement = conn.prepareStatement(sql);
			result = preStatement.executeQuery();
			dtmProduct.setRowCount(0);
			while (result.next()) {
				Vector<Object> vec = new Vector<Object>();
				vec.add(result.getString("idProduct"));
				vec.add(result.getString("nameProduct"));
				vec.add(getNameType(result.getString("type"))); // Hi???n th??? lo???i h??ng vi???t t???t b???ng t??n ?????y ?????
				vec.add(result.getInt("cost"));
				vec.add(result.getInt("price"));
				vec.add(result.getInt("quantityOnHand"));
				vec.add(result.getString("description"));
				dtmProduct.addRow(vec);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Hi???n lo???i s???n ph???m
	public void displayTypeProduct() throws SQLException {
		String sql = "select * from production.TypeProduct";
		Statement statement = conn.createStatement();
		result = statement.executeQuery(sql);
		while (result.next()) {
			String nameType = result.getString("nameType");
			cbTypeProduct.addItem(nameType);
		}
	}

	// l???y t??n s???n ph???m t??? textfield user ??i???n
	public String getNameProduct(String text) {
		if (text == null || text.equals("Nh???p s???n ph???m c???n t??m ki???m")) {
			return "";
		} else {
			return text;
		}
	}

	// s???p x???p s???n ph???m theo gi?? t??ng d???n
	public void diplayPriceAscending() {
		try {
			String sql = "select * from production.Product where nameProduct like N'%"
					+ getNameProduct(txtSearch.getText()) + "%'  order by price asc";
			Statement statement = conn.createStatement();
			result = statement.executeQuery(sql);
			dtmProduct.setRowCount(0);
			while (result.next()) {
				Vector<Object> vec = new Vector<Object>();
				vec.add(result.getString("idProduct"));
				vec.add(result.getString("nameProduct"));
				vec.add(result.getBigDecimal("price"));
				vec.add(result.getString("unit"));
				dtmProduct.addRow(vec);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// s???p x???p s???n ph???m theo gi?? gi???m d???n
	public void displayPriceDescending() {
		try {
			String sql = "select * from production.Product where nameProduct like N'%"
					+ getNameProduct(txtSearch.getText()) + "%'  order by price desc";
			Statement statement = conn.createStatement();
			result = statement.executeQuery(sql);
			dtmProduct.setRowCount(0);
			while (result.next()) {
				Vector<Object> vec = new Vector<Object>();
				vec.add(result.getString("idProduct"));
				vec.add(result.getString("nameProduct"));
				vec.add(result.getBigDecimal("price"));
				vec.add(result.getString("unit"));
				dtmProduct.addRow(vec);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processSearch() {
		try {
			CallableStatement callStatement = conn.prepareCall("{call virtual.proc_searchByName(?)}");
			callStatement.setString(1, txtSearch.getText());
			result = callStatement.executeQuery();
			dtmProduct.setRowCount(0);
			while (result.next()) {
				Vector<Object> vec = new Vector<Object>();
				vec.add(result.getString("idProduct"));
				vec.add(result.getString("nameProduct"));
				vec.add(result.getBigDecimal("price"));
				vec.add(result.getString("unit"));
				dtmProduct.addRow(vec);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// Th??m v??o gi??? h??ng
	public void processAddToCart(String idx) {
		if (Integer.parseInt(txtQuantity.getText()) <= 0) {
			JOptionPane.showMessageDialog(null, "Vui l??ng nh???p s??? l?????ng c???n mua", "Th??ng b??o",
					JOptionPane.ERROR_MESSAGE);
		} else {
			try {
				String sql2 = "select * from sales.Cart where idCustomer= '" + idx + "' and idProduct ='"
						+ txtidProduct.getText() + "'";
				Statement statement2 = conn.createStatement();
				ResultSet result2 = statement2.executeQuery(sql2);

				if (result2.next()) {
					if (insertCartExist(idx, txtidProduct.getText(), Integer.parseInt(txtQuantity.getText())))
						System.out.println("cap nhat gio hang thanh cong");

				} else {
					if (insertCartNotExist(idx, txtidProduct.getText(), txtQuantity.getText()))
						System.out.println("them gio hang thanh cong");
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public boolean insertCartExist(String idx, String txtidProduct, int Plus) {
		try {
			String sql = "update sales.Cart set quantity = quantity + " + Plus + " where idCustomer = '" + idx
					+ "' and idProduct = '" + txtidProduct + "'";
			Statement statement = conn.createStatement();
			int result = statement.executeUpdate(sql);
			if (result > 0) {
				JOptionPane.showMessageDialog(null, "Th??m v??o gi??? h??ng th??nh c??ng", "Th??ng b??o",
						JOptionPane.INFORMATION_MESSAGE);
				return true;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	// th??m s???n ph???m v??o gi??? h??ng n???u ch??a c??
	public boolean insertCartNotExist(String idx, String txtidProduct, String txtQuantity) {
		try {
			Statement statement = conn.createStatement();
			String insert = "insert into sales.Cart values('" + idx + "','" + txtidProduct + "','" + txtQuantity + "')";
			int results = statement.executeUpdate(insert);
			if (results > 0) {
				JOptionPane.showMessageDialog(null, "Th??m v??o gi??? h??ng th??nh c??ng", "Th??ng b??o",
						JOptionPane.INFORMATION_MESSAGE);
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Hi???n s???n ph???m trong gi??? h??ng
	public void displayProductInCart(String idx) {
		try {
			String sql = "select * from virtual.v_showCart where idCustomer='" + idx + "'";
			Statement statement = conn.createStatement();
			result = statement.executeQuery(sql);
			dtmWannaBuy.setRowCount(0);

			while (result.next()) {
				Vector<Object> vec = new Vector<Object>();
				vec.add(result.getString("idProduct"));
				vec.add(result.getString("nameProduct"));
				vec.add(result.getString("price"));
				vec.add(result.getString("quantity"));
				vec.add(result.getString("unit"));
				dtmWannaBuy.addRow(vec);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void displayInTxtField() {
		try {
			String sql = "select * from production.Product where idProduct=?";
			preStatement = conn.prepareStatement(sql);
			preStatement.setString(1, idProduct);
			result = preStatement.executeQuery();
			if (result.next()) {
				txtidProduct.setText(result.getString(1));
				this.lblPic.setIcon(new ImageIcon("Image\\" + result.getString(1) + ".jpg"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addTblProductMouseListener() {
		tblProduct.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				int row = tblProduct.getSelectedRow();
				if (row == -1) {
					return;
				} else {
					idProduct = tblProduct.getValueAt(row, 0) + "";
					displayInTxtField();
				}
			}
		});
	}

	// Hi???n ra th??ng tin ??ang nh???p
	public void diplaySignInDetails(String idx) {
		try {
			String sql = "select * from person.Users where idUser='" + idx + "'";
			Statement statement = conn.createStatement();
			result = statement.executeQuery(sql);
			while (result.next()) {
				txtIdUser.setText(result.getString("idUser"));
				txtUserName.setText(result.getString("username"));
				txtPassWord.setText(result.getString("password"));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
	}

	// Hi???n th??ng tin kh??ch h??ng
	public void displayCusInfor(String idx) throws SQLException {
		try {
			String sql = "select * from virtual.v_showAllCus where idCustomer='" + idx + "'";
			Statement statement = conn.createStatement();
			result = statement.executeQuery(sql);
			if (result.next()) {
				txtNameCus.setText(result.getString("nameCustomer"));
				txtPhoneNumber.setText(result.getString("phone"));
				txtDOB.setText(result.getString("birthday"));
				if (result.getInt("sex") == 0) {
					txtSex.setText("Nam");
				} else {
					txtSex.setText("N???");
				}
				txtAddress.setText(result.getString("address"));
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// Hi???n s???n ph???m ???? mua c???a kh??ch h??ng
	public void displayPurItems(String idx) throws SQLException {
		try {
			String sql = "select * from sales.Bill";
			Statement statement = conn.createStatement();
			result = statement.executeQuery(sql);
//			String sql3 = "select * from production.Product";
			dtmPurItems.setRowCount(0);
			while (result.next()) {
				if (idx.compareTo(result.getString("idCustomer")) == 0) {
					String idBill = result.getString("idBill");
					String sql2 = "select * from sales.BillDetails";
					Statement statement2 = conn.createStatement();
					ResultSet result2 = statement2.executeQuery(sql2);
					while (result2.next()) {
						if (idBill.compareTo(result2.getString("idBill")) == 0) {
							Vector<Object> vec = new Vector<Object>();
							vec.add(result2.getString("idProduct"));
							vec.add(result2.getInt("quantity"));
							vec.add(result2.getBigDecimal("current_price"));
							vec.add(result2.getBigDecimal("price"));
							dtmPurItems.addRow(vec);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Thay ?????i th??ng tin c?? nh??n
	public boolean processChangeInFor(String idx) {
		try {
			int sex = 0;
			if (txtSex.getText().equals("Nam")) {
				sex = 0;
			} else if (txtSex.getText().equals("N???")) {
				sex = 1;
			}
			String sql = "update person.Customer set nameCustomer=N'" + txtNameCus.getText() + "',phone='"
					+ txtPhoneNumber.getText() + "',birthday='" + txtDOB.getText() + "',sex='" + sex + "',address=N'"
					+ txtAddress.getText() + "' " + "where idCustomer='" + idx + "' ";
			Statement statement = conn.createStatement();
			int results = statement.executeUpdate(sql);
			JOptionPane.showMessageDialog(null, "Thay ?????i th??ng tin c?? nh??n th??nh c??ng", "Th??ng b??o", -1);
			if (results > 0)
				return true;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	public void displayInTxtFieldC() {
		try {
			String sql = "select * from virtual.v_showCart where idProduct=? ";
			preStatement = conn.prepareStatement(sql);
			preStatement.setString(1, idProduct);
			result = preStatement.executeQuery();
			if (result.next()) {
				txtIdProductDelete.setText(result.getString(1));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void DelFromCart(String idx) {
		try {
			if (JOptionPane.showConfirmDialog(rootPane, "B???n c?? ch???c ch???n mu???n x??a s???n ph???m kh???i gi??? h??ng", "Lua chon",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				String sql = "delete from sales.Cart where idCustomer='" + idx + "' and idProduct='"
						+ txtIdProductDelete.getText() + "'";
				Statement statement = conn.createStatement();
				int result = statement.executeUpdate(sql);
				JOptionPane.showMessageDialog(null, "X??a th??nh c??ng s???n ph???m kh???i gi??? h??ng", "Th??ng b??o", -1);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void addTblWannaBuyMouseListener(String idx) {
		tblWannaBuy.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				int row = tblWannaBuy.getSelectedRow();
				if (row == -1) {
					return;
				} else {
					idProduct = tblWannaBuy.getValueAt(row, 0) + "";
					displayInTxtFieldC();
				}
			}
		});
	}

	public void addControls() {
		JPanel pnBorder = new JPanel();
		Font font = new Font("arial", Font.BOLD, 20);
		Font fontTable = new Font("arial", Font.BOLD, 15);
		pnBorder.setLayout(new BorderLayout());
		Container con = getContentPane();

		JPanel pnNorth = new JPanel();
		pnNorth.setLayout(new BoxLayout(pnNorth, BoxLayout.X_AXIS));
		JPanel pnButton = new JPanel();
		pnButton.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel pnLogo = new JPanel();
		pnLogo.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel pnButton2 = new JPanel();
		pnButton2.setLayout(new FlowLayout(FlowLayout.RIGHT));

		JButton btHomePage = new JButton(/* "Trang Ch???" */);
		btHomePage.setIcon(iconHomePage);
		btHomePage.setBackground(Color.WHITE);
		btHomePage.setBorder(BorderFactory.createEmptyBorder());
		// btHomePage.setFont(font);

		JLabel lblLogo = new JLabel();
		ImageIcon logo = new ImageIcon(
				new ImageIcon("Image\\xeday.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
		lblLogo.setIcon(logo);

		JLabel lblNameProgram = new JLabel("KMP Market");
		lblNameProgram.setFont(new Font("arial", Font.ITALIC, 50));
		lblNameProgram.setForeground(Color.RED);

		JButton btCart = new JButton(/* Gi??? h??ng" */);
		btCart.setIcon(iconCart);
		btCart.setBackground(Color.WHITE);
		btCart.setBorder(BorderFactory.createEmptyBorder());
		// btCart.setFont(font);

		JButton btProfile = new JButton(/* "Th??ng tin" */);
		btProfile.setIcon(iconProfile);
		btProfile.setBackground(Color.WHITE);
		btProfile.setBorder(BorderFactory.createEmptyBorder());
		// btProfile.setFont(font);

		btHomePage.setPreferredSize(btCart.preferredSize());
		btProfile.setPreferredSize(btCart.preferredSize());

		pnButton.add(btHomePage);
		pnLogo.add(lblLogo);
		pnLogo.add(lblNameProgram);
		pnButton2.add(btCart);
		pnButton2.add(btProfile);

		pnNorth.add(pnButton);
		pnNorth.add(pnLogo);
		pnNorth.add(pnButton2);

		pnBorder.add(pnNorth, BorderLayout.NORTH);

		final JPanel pnCenter = new JPanel();

		pnCenter.setLayout(new CardLayout());

		// Panel Trang ch???
		final JPanel pnHomePage = new JPanel();
		// Panel Gi??? h??ng
		final JPanel pnCart = new JPanel();
		// Panel Th??ng tin
		final JPanel pnProfile = new JPanel();

		pnCenter.add(pnHomePage, "mycard1");

		pnCenter.add(pnCart, "mycard2");

		pnCenter.add(pnProfile, "mycard3");

		pnBorder.add(pnCenter, BorderLayout.CENTER);

		con.add(pnBorder);
		btHomePage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) pnCenter.getLayout();

				cl.show(pnCenter, "mycard1");

			}
		});
		btCart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) pnCenter.getLayout();

				cl.show(pnCenter, "mycard2");

			}
		});
		btProfile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) pnCenter.getLayout();

				cl.show(pnCenter, "mycard3");

			}
		});

		// Giao di???n ph???n th??ng tin
		pnProfile.setLayout(new BorderLayout());

		JPanel pnWest = new JPanel();
		pnWest.setLayout(new FlowLayout());
		pnProfile.add(pnWest, BorderLayout.WEST);

		JPanel pnHandle = new JPanel();
		pnHandle.setLayout(new BoxLayout(pnHandle, BoxLayout.Y_AXIS));
		pnWest.add(pnHandle);

		JPanel pnCenterProfile = new JPanel();
		pnCenterProfile.setLayout(new CardLayout());

		JPanel pnBorderProfile = new JPanel();

		JPanel pnBtSignInDetails = new JPanel();
		btSignInDetails = new JButton("Th??ng tin ????ng nh???p");
		btSignInDetails.setFont(font);
		pnBtSignInDetails.add(btSignInDetails);
		pnHandle.add(pnBtSignInDetails);

		JPanel pnBtCusInfor = new JPanel();
		btCusInfor = new JButton("Th??ng tin kh??ch h??ng");
		btCusInfor.setFont(font);
		pnBtCusInfor.add(btCusInfor);
		pnHandle.add(pnBtCusInfor);

		JPanel pnBtPurItems = new JPanel();
		btPurItems = new JButton("S???n ph???m ???? mua");
		btPurItems.setFont(font);
		pnBtPurItems.add(btPurItems);
		pnHandle.add(pnBtPurItems);

		// Panel th??ng tin ????ng nh???p
		JPanel pnSignInDetails = new JPanel();
		// pnSignInDetails.setBackground(Color.GREEN);

		// Panel th??ng tin kh??ch h??ng
		JPanel pnCusInfor = new JPanel();
		// pnCusInfor.setBackground(Color.RED);

		// Panel s???n ph???m ???? mua
		JPanel pnPurItems = new JPanel();
		// pnPurItems.setBackground(Color.GRAY);

		pnCenterProfile.add(pnSignInDetails, "pnSignInDetails");

		pnCenterProfile.add(pnCusInfor, "pnCusInfor");

		pnCenterProfile.add(pnPurItems, "pnPurItems");

		pnBorderProfile.add(pnCenterProfile, BorderLayout.CENTER);
		pnProfile.add(pnBorderProfile);

		btSignInDetails.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout clProfile = (CardLayout) pnCenterProfile.getLayout();

				clProfile.show(pnCenterProfile, "pnSignInDetails");

			}
		});
		btCusInfor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout clProfile = (CardLayout) pnCenterProfile.getLayout();

				clProfile.show(pnCenterProfile, "pnCusInfor");

			}
		});
		btPurItems.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout clProfile = (CardLayout) pnCenterProfile.getLayout();

				clProfile.show(pnCenterProfile, "pnPurItems");

			}
		});

		// T???o table cho button s???n ph???m ???? mua
		dtmPurItems = new DefaultTableModel();
		dtmPurItems.addColumn("M?? s???n ph???m");
		dtmPurItems.addColumn("S??? l?????ng");
		dtmPurItems.addColumn("Gi?? s???n ph???m");
		dtmPurItems.addColumn("Gi?? sau khi mua");
		tblPurItems = new JTable(dtmPurItems);
		tblPurItems.setFont(fontTable);
		tblPurItems.setRowHeight(30);
		JScrollPane scTablePurItems = new JScrollPane(tblPurItems, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scTablePurItems.setPreferredSize(new Dimension(900, 600));
		pnPurItems.add(scTablePurItems, BorderLayout.CENTER);

		// T???o table cho button th??ng tin ????ng nh???p
		JPanel pnDtmSignInDetails = new JPanel();
		pnDtmSignInDetails.setLayout(new BoxLayout(pnDtmSignInDetails, BoxLayout.Y_AXIS));
		pnSignInDetails.add(pnDtmSignInDetails, BorderLayout.CENTER);

		JPanel pnBtChangePass = new JPanel();
		pnBtChangePass.setLayout(new FlowLayout(FlowLayout.RIGHT));
		btChangePass = new JButton("Thay ?????i m???t kh???u");
		btChangePass.setFont(font);
		pnBtChangePass.add(btChangePass);

		// M?? kh??ch h??ng
		JPanel pnIdUser = new JPanel();
		pnIdUser.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblIdUser = new JLabel("M?? kh??ch h??ng: ");
		lblIdUser.setFont(font);
		txtIdUser = new JTextField(15);
		txtIdUser.setFont(font);
		pnIdUser.add(lblIdUser);
		pnIdUser.add(txtIdUser);
		pnDtmSignInDetails.add(pnIdUser);

		// T??i kho???n
		JPanel pnUserName = new JPanel();
		pnUserName.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblUserName = new JLabel("T??i kho???n: ");
		lblUserName.setFont(font);
		txtUserName = new JTextField(15);
		txtUserName.setFont(font);
		pnUserName.add(lblUserName);
		pnUserName.add(txtUserName);
		pnDtmSignInDetails.add(pnUserName);

		// M???t kh???u
		JPanel pnPassWord = new JPanel();
		pnPassWord.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblPassWord = new JLabel("M???t kh???u: ");
		lblPassWord.setFont(font);
		txtPassWord = new JTextField(15);
		txtPassWord.setFont(font);
		pnPassWord.add(lblPassWord);
		pnPassWord.add(txtPassWord);
		pnDtmSignInDetails.add(pnPassWord);

		lblUserName.setPreferredSize(lblIdUser.preferredSize());
		lblPassWord.setPreferredSize(lblIdUser.preferredSize());

		pnDtmSignInDetails.add(pnBtChangePass);
		pnSignInDetails.add(pnDtmSignInDetails);

		// T???o table cho button th??ng tin kh??ch h??ng
		JPanel pnDtmCusInfor = new JPanel();
		pnDtmCusInfor.setLayout(new BoxLayout(pnDtmCusInfor, BoxLayout.Y_AXIS));
		pnCusInfor.add(pnDtmCusInfor, BorderLayout.PAGE_END);

		JPanel pnBtChangeInfor = new JPanel();
		pnBtChangeInfor.setLayout(new FlowLayout(FlowLayout.RIGHT));
		btChangeInFor = new JButton("Thay ?????i th??ng tin");
		btChangeInFor.setFont(font);
		pnBtChangeInfor.add(btChangeInFor);

		// T??n kh??ch h??ng
		JPanel pnNameCus = new JPanel();
		pnNameCus.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblNameCus = new JLabel("T??n kh??ch h??ng: ");
		lblNameCus.setFont(font);
		txtNameCus = new JTextField(15);
		txtNameCus.setFont(font);
		pnNameCus.add(lblNameCus);
		pnNameCus.add(txtNameCus);
		pnDtmCusInfor.add(pnNameCus);

		// S??? ??i???n tho???i
		JPanel pnPhoneNumber = new JPanel();
		pnPhoneNumber.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblPhoneNumber = new JLabel("S??? ??i???n tho???i: ");
		lblPhoneNumber.setFont(font);
		txtPhoneNumber = new JTextField(15);
		txtPhoneNumber.setEditable(false);
		txtPhoneNumber.setFont(font);
		pnPhoneNumber.add(lblPhoneNumber);
		pnPhoneNumber.add(txtPhoneNumber);
		pnDtmCusInfor.add(pnPhoneNumber);

		// Ng??y sinh
		JPanel pnDOB = new JPanel();
		pnDOB.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblDOB = new JLabel("Ng??y sinh: ");
		lblDOB.setFont(font);
		txtDOB = new JTextField(15);
		txtDOB.setFont(font);
		pnDOB.add(lblDOB);
		pnDOB.add(txtDOB);
		pnDtmCusInfor.add(pnDOB);

		// Gi???i t??nh
		JPanel pnSex = new JPanel();
		pnSex.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblSex = new JLabel("Gi???i t??nh: ");
		lblSex.setFont(font);
		txtSex = new JTextField(15);
		txtSex.setFont(font);
		pnSex.add(lblSex);
		pnSex.add(txtSex);
		pnDtmCusInfor.add(pnSex);

		// ?????a ch???
		JPanel pnAddress = new JPanel();
		pnAddress.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblAddress = new JLabel("?????a ch???: ");
		lblAddress.setFont(font);
		txtAddress = new JTextField(15);
		txtAddress.setFont(font);
		pnAddress.add(lblAddress);
		pnAddress.add(txtAddress);
		pnDtmCusInfor.add(pnAddress);

		lblPhoneNumber.setPreferredSize(lblNameCus.preferredSize());
		lblDOB.setPreferredSize(lblNameCus.preferredSize());
		lblSex.setPreferredSize(lblNameCus.preferredSize());
		lblAddress.setPreferredSize(lblNameCus.preferredSize());

		pnDtmCusInfor.add(pnBtChangeInfor);
		pnCusInfor.add(pnDtmCusInfor);

		// Giao di???n Tranng ch???
		pnHomePage.setLayout(new BoxLayout(pnHomePage, BoxLayout.Y_AXIS));
		JPanel pnTool = new JPanel();
		pnTool.setLayout(new BoxLayout(pnTool, BoxLayout.X_AXIS));
		pnHomePage.add(pnTool);
		JPanel pnFilter = new JPanel();
		pnFilter.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblCbTypeProduct = new JLabel("L???c theo: ");
		lblCbTypeProduct.setFont(font);
		cbTypeProduct = new JComboBox();
		cbTypeProduct.addItem("T???t c???");
		cbTypeProduct.setBounds(100, 50, 150, 20);
		cbTypeProduct.setFont(font);
		pnFilter.add(lblCbTypeProduct);
		pnFilter.add(cbTypeProduct);
		pnTool.add(pnFilter);

		JPanel pnSearch = new JPanel();
		pnSearch.setLayout(new FlowLayout(FlowLayout.CENTER));
		txtSearch = new JTextField(20);
		txtSearch.setText("Nh???p s???n ph???m c???n t??m ki???m");
		txtSearch.setFont(font);
		btSearch = new JButton("T??m ki???m");
		btSearch.setFont(font);
		pnSearch.add(txtSearch);
		pnSearch.add(btSearch);
		pnTool.add(pnSearch);

		JPanel pnSort = new JPanel();
		pnSort.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JLabel lblSort = new JLabel("S???p x???p theo: ");
		lblSort.setFont(font);
		cbSort = new JComboBox();
		cbSort.setFont(font);
		cbSort.addItem("Gi?? t??ng d???n");
		cbSort.addItem("Gi?? gi???m d???n");
		pnSort.add(lblSort);
		pnSort.add(cbSort);
		pnTool.add(pnSort);

		dtmProduct = new DefaultTableModel();
		dtmProduct.addColumn("M?? s???n ph???m");
		dtmProduct.addColumn("T??n s???n ph???m");
		dtmProduct.addColumn("Gi??");
		dtmProduct.addColumn("????n v???");
		tblProduct = new JTable(dtmProduct);
		tblProduct.setRowHeight(30);
		tblProduct.setFont(fontTable);
		JScrollPane scTableHomePage = new JScrollPane(tblProduct, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pnHomePage.add(scTableHomePage, BorderLayout.CENTER);

		JPanel pnButtonHP = new JPanel();
		btAddToCart = new JButton("Th??m v??o gi??? h??ng");
		btAddToCart.setFont(font);
		btExit = new JButton("Tho??t");
		btExit.setFont(font);
		pnButtonHP.add(btAddToCart);
		pnButtonHP.add(btExit);

		JPanel pnInformationAndDetails = new JPanel();
		pnInformationAndDetails.setLayout(new BoxLayout(pnInformationAndDetails, BoxLayout.Y_AXIS));
		pnHomePage.add(pnInformationAndDetails, BorderLayout.PAGE_END);

		// tao dong dong chi tiet s???n pham gom thong tin va hinh ???nh
		JPanel pnDetails = new JPanel();
		pnDetails.setLayout(new FlowLayout());
		pnInformationAndDetails.add(pnDetails);

		// chia l??m 2 ph???n tr??i v?? ph???i
		JPanel pnDetailsSeperate = new JPanel();
		pnDetailsSeperate.setLayout(new BoxLayout(pnDetailsSeperate, BoxLayout.X_AXIS));
		pnDetails.add(pnDetailsSeperate);

		// l???y d??ng b??n tr??i hi???n th??ng tin s???n ph???m
		JPanel pnDetailsTxT = new JPanel();
		pnDetailsTxT.setLayout(new BoxLayout(pnDetailsTxT, BoxLayout.Y_AXIS));
		pnDetailsSeperate.add(pnDetailsTxT);

		JPanel pnidProduct = new JPanel();
		pnidProduct.setLayout(new FlowLayout());

		JLabel lblidProduct = new JLabel("M?? s???n ph???m:");
		lblidProduct.setFont(font);

		txtidProduct = new JTextField(15);
		txtidProduct.setFont(font);

		pnidProduct.add(lblidProduct);
		pnidProduct.add(txtidProduct);
		pnDetailsTxT.add(pnidProduct);

		JPanel pnQuantity = new JPanel();
		pnQuantity.setLayout(new FlowLayout());

		JLabel lblQuantity = new JLabel("S??? l?????ng c???n mua:");
		lblQuantity.setFont(font);

		txtQuantity = new JTextField(15);
		txtQuantity.setText("0");
		txtQuantity.setFont(font);

		pnQuantity.add(lblQuantity);
		pnQuantity.add(txtQuantity);
		pnDetailsTxT.add(pnQuantity);

		// tao dong ben phai chua anh;
		pnPic = new JPanel();
		pnPic.setLayout(new FlowLayout());
		pnDetailsSeperate.add(pnPic);
		// tao ch??? ??i???n ???nh
		lblPic = new JLabel();
		lblPic.setIcon(new ImageIcon("Image\\rc001.jpg"));
		pnPic.add(lblPic);

		pnInformationAndDetails.add(pnButtonHP);
		pnHomePage.add(pnInformationAndDetails);
		lblidProduct.setPreferredSize(lblQuantity.preferredSize());

		// Giao di???n gi??? h??ng
		pnCart.setLayout(new BoxLayout(pnCart, BoxLayout.Y_AXIS));
		JPanel pnListProduct = new JPanel();

		dtmWannaBuy = new DefaultTableModel();
		dtmWannaBuy.addColumn("M?? s???n ph???m");
		dtmWannaBuy.addColumn("T??n s???n ph???m");
		dtmWannaBuy.addColumn("Gi??");
		dtmWannaBuy.addColumn("S??? l?????ng");
		dtmWannaBuy.addColumn("????n v???");
		tblWannaBuy = new JTable(dtmWannaBuy);
		tblWannaBuy.setFont(fontTable);
		tblWannaBuy.setRowHeight(30);
		JScrollPane scTableCart = new JScrollPane(tblWannaBuy, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scTableCart.setPreferredSize(new Dimension(1000, 400));
		pnListProduct.add(scTableCart, BorderLayout.CENTER);
		pnCart.add(pnListProduct);

		JPanel pnInforAndButtons = new JPanel();
		pnInforAndButtons.setLayout(new BoxLayout(pnInforAndButtons, BoxLayout.Y_AXIS));
		pnCart.add(pnInforAndButtons, BorderLayout.PAGE_END);

		JPanel pnIdProductDelete = new JPanel();
		pnIdProductDelete.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblIdProductDelete = new JLabel("M?? s???n ph???m: ");
		lblIdProductDelete.setFont(font);
		txtIdProductDelete = new JTextField(15);
		txtIdProductDelete.setFont(font);
		pnIdProductDelete.add(lblIdProductDelete);
		pnIdProductDelete.add(txtIdProductDelete);
		pnInforAndButtons.add(pnIdProductDelete);

		JPanel pnButtonCart = new JPanel();
		pnButtonCart.setLayout(new FlowLayout(FlowLayout.RIGHT));
		btPay = new JButton("Thanh to??n");
		btPay.setFont(font);
		btDeleteFromCart = new JButton("X??a kh???i gi??? h??ng");
		btDeleteFromCart.setFont(font);
		pnButtonCart.add(btDeleteFromCart);
		pnButtonCart.add(btPay);
		pnInforAndButtons.add(pnButtonCart);
	}

	public void showWindow() {
		this.setTitle("KMP Market");
		this.setSize(1200, 760); // ?????t d?????i 760
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void addtblProductListener(MouseListener tblProductListener) {
		tblProduct.addMouseListener(tblProductListener);
	}

	public void addbtExitListener(ActionListener btExitListener) {
		btExit.addActionListener(btExitListener);
	}

	public void addbtAddToCartListener(ActionListener btAddToCartListener) {
		btAddToCart.addActionListener(btAddToCartListener);
	}

	public void addcbTypeProductListener(ActionListener cbTypeProductListener) {
		cbTypeProduct.addActionListener(cbTypeProductListener);
	}

	public void addbtSearchListener(ActionListener btSearchListener) {
		btSearch.addActionListener(btSearchListener);
	}

	public void addcbSortListener(ActionListener cbSortListener) {
		cbSort.addActionListener(cbSortListener);
	}

	public void addbtPayListener(ActionListener btPayListener) {
		btPay.addActionListener(btPayListener);
	}

	public void addbtSignInDetailsListener(ActionListener btSignInDetailsListener) {
		btSignInDetails.addActionListener(btSignInDetailsListener);
	}

	public void addbtCusInforListener(ActionListener btCusInforListener) {
		btCusInfor.addActionListener(btCusInforListener);
	}

	public void addbtPurItemsListener(ActionListener btPurItemsListener) {
		btPurItems.addActionListener(btPurItemsListener);
	}

	public void addbtChangePassListener(ActionListener btChangePassListener) {
		btChangePass.addActionListener(btChangePassListener);
	}

	public void addbtChangeInforListener(ActionListener btChangeInforListener) {
		btChangeInFor.addActionListener(btChangeInforListener);
	}

	public void addbtDelFromCartListener(ActionListener btDelFromCartListener) {
		btDeleteFromCart.addActionListener(btDelFromCartListener);
	}

	// getter and setter cbTypeProduct
	public JComboBox getCbTypeProduct() {
		return cbTypeProduct;
	}

	public void setCbTypeProduct(JComboBox cbTypeProduct) {
		this.cbTypeProduct = cbTypeProduct;
	}

	// getter and setter cbSort
	public JComboBox getCbSort() {
		return cbSort;
	}

	public void setCbSort(JComboBox cbSort) {
		this.cbSort = cbSort;
	}

	// getter and setter id
	public String getId() {
		return idProduct;
	}

	public void setId(String idProduct) {
		this.idProduct = idProduct;
	}

}
