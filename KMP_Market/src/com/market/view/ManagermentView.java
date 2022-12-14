package com.market.view;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.market.services.Auto_Increase_Id;

public class ManagermentView extends JFrame {
	JTextField txtIdPro, txtNamePro, txtTypePro, txtCostPro, txtPricePro, txtQuantityPro, txtUnitPro, txtDescription;
	JTextField txtSearchPro, txtSearchCus;
	JTextField txtIdCus, txtNameCus, txtPhone, txtBirth, txtSex, txtAddress, txtDayReg, txtPermission;
	JTextField txtIdAd, txtNameAd, txtUserName, txtPass, txtPhoneAd, txtBirthAd, txtSexAd, txtNative;

	JButton btRefresh, btSearchPro, btSearchCus, btAddPro, btAddAd, btUpdatePro, btUpdatePerCus, btUpdateAd,
			btDeletePro, btDeleteCus, btDeleteAd, btReckon, btTopOrder, btLogo, btConfirmBill;
	JButton btAddType, btImport, btRefreshAd;

	DefaultTableModel dtmProduct, dtmCustomer, dtmAdministrator;
	DefaultTableModel dtmTest;
	JTable tblProduct, tblCustomer, tblAdministrator;

	JComboBox cbFilterTP = new JComboBox();
	JComboBox cbChooseSort = new JComboBox();

	Connection conn = null;
	PreparedStatement preStatement = null;
	Statement statement = null;
	ResultSet result = null;

	Auto_Increase_Id aii = new Auto_Increase_Id();

	Font fGeneral = new Font("Arial", Font.BOLD, 20); // ????y l?? font ch??? chung

	public ManagermentView() {
		addControls();
		addEvents();
		connectDatabase();
		FillFilter();
		FillSort();
//		sortBy();
		showAllPro();
		showAllCus();
		showAllAd();

	}

	private void FillSort() {
		cbChooseSort.addItem("M??");
		cbChooseSort.addItem("T??n");
		cbChooseSort.addItem("Gi?? b??n");
		cbChooseSort.addItem("Gi?? nh???p");
	}

	private void sortBy(int type) {
		if (cbChooseSort.getSelectedItem().equals("T??n")) {
			try {
				String sql = "select * from production.Product where nameProduct like '%"
						+ getNameProduct(txtSearchPro.getText()) + "%' and type ='" + determindType(type)
						+ "' order by nameProduct";
				PreparedStatement pre = conn.prepareStatement(sql);
				ResultSet result = pre.executeQuery();
				dtmProduct.setRowCount(0);
				while (result.next()) {
					Vector<Object> vec = new Vector<Object>();
					vec.add(result.getString("idProduct"));
					vec.add(result.getString("nameProduct"));
					vec.add(getNameType(result.getString("type")));
					vec.add(result.getString("unit"));
					vec.add(result.getInt("cost"));
					vec.add(result.getInt("price"));
					vec.add(result.getInt("quantityOnHand"));
					vec.add(result.getString("description"));
					dtmProduct.addRow(vec);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (cbChooseSort.getSelectedItem().equals("Gi?? b??n")) {
			try {
				String sql = "select * from production.Product where nameProduct like '%"
						+ getNameProduct(txtSearchPro.getText()) + "%' order by price";
				PreparedStatement pre = conn.prepareStatement(sql);
				ResultSet result = pre.executeQuery();
				dtmProduct.setRowCount(0);
				while (result.next()) {
					Vector<Object> vec = new Vector<Object>();
					vec.add(result.getString("idProduct"));
					vec.add(result.getString("nameProduct"));
					vec.add(getNameType(result.getString("type")));// Hi???n th??? lo???i h??ng vi???t t???t b???ng t??n ?????y ?????
					vec.add(result.getString("unit"));
					vec.add(result.getInt("cost"));
					vec.add(result.getInt("price"));
					vec.add(result.getInt("quantityOnHand"));
					vec.add(result.getString("description"));
					dtmProduct.addRow(vec);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (cbChooseSort.getSelectedItem().equals("Gi?? nh???p")) {
			try {
				String sql = "select * from production.Product where nameProduct like '%"
						+ getNameProduct(txtSearchPro.getText()) + "%' order by cost";
				PreparedStatement pre = conn.prepareStatement(sql);
				ResultSet result = pre.executeQuery();
				dtmProduct.setRowCount(0);
				while (result.next()) {
					Vector<Object> vec = new Vector<Object>();
					vec.add(result.getString("idProduct"));
					vec.add(result.getString("nameProduct"));
					vec.add(getNameType(result.getString("type"))); // Hi???n th??? lo???i h??ng vi???t t???t b???ng t??n ?????y ?????
					vec.add(result.getString("unit"));
					vec.add(result.getInt("cost"));
					vec.add(result.getInt("price"));
					vec.add(result.getInt("quantityOnHand"));
					vec.add(result.getString("description"));
					dtmProduct.addRow(vec);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				String sql = "select * from production.Product where nameProduct like '%"
						+ getNameProduct(txtSearchPro.getText()) + "%'order by idProduct";
				PreparedStatement pre = conn.prepareStatement(sql);
				ResultSet result = pre.executeQuery();
				dtmProduct.setRowCount(0);
				while (result.next()) {
					Vector<Object> vec = new Vector<Object>();
					vec.add(result.getString("idProduct"));
					vec.add(result.getString("nameProduct"));
					vec.add(getNameType(result.getString("type")));
					vec.add(result.getString("unit"));
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
	}

	private String getNameProduct(String text) {
		if (text == null || text.equals("Nh???p t??n s???n ph???m")) {
			return "";
		} else {
			return text;
		}
	}

	public void FillFilter() {
		try {
			String sql = "select * from production.TypeProduct";
			preStatement = conn.prepareStatement(sql);
			ResultSet rs = preStatement.executeQuery();
			cbFilterTP.addItem("T???T C???"); // T??? th??m (kh??ng c?? trong CSDL)
			while (rs.next()) {
				this.cbFilterTP.addItem(rs.getString("nameType"));
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

	// Hi???n th??? s???n ph???m sau khi l???c
	private void showTypePro(int type) {
		try {
			String sql = "select * from production.Product where type='" + determindType(type - 1)
					+ "' and nameProduct like '%" + txtSearchPro.getText() + "%'";
			// TYPE - 1: l?? s??? d??ng trong CSDL ( D??ng T???T C??? kh??ng t??nh trong CSDL)
			preStatement = conn.prepareStatement(sql);
			result = preStatement.executeQuery();
			dtmProduct.setRowCount(0);
			while (result.next()) {
				Vector<Object> vec = new Vector<Object>();
				vec.add(result.getString("idProduct"));
				vec.add(result.getString("nameProduct"));
				vec.add(getNameType(result.getString("type")));
				vec.add(result.getString("unit"));
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

	// S???n ph???m
	public boolean checkExIdPro(String ma) {
		try {
			String sql = "select * from production.Product where idProduct=?";
			preStatement = conn.prepareStatement(sql);
			preStatement.setString(1, ma);
			ResultSet rs = preStatement.executeQuery();
			return rs.next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Kh??ch h??ng
	public boolean checkExIdCus(String ma) {
		try {
			String sql = "select * from person.Customer where idCustomer=?";
			preStatement = conn.prepareStatement(sql);
			preStatement.setString(1, ma);
			ResultSet rs = preStatement.executeQuery();
			return rs.next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Qu???n tr??? vi??n
	public boolean checkExIdAd(String ma) {
		try {
			String sql = "select * from person.Administrator where idAdmin=?";
			preStatement = conn.prepareStatement(sql);
			preStatement.setString(1, ma);
			ResultSet rs = preStatement.executeQuery();
			return rs.next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private String getIdType(String string) {
		try {
			String sqlType = "select * from production.TypeProduct where nameType = N'" + string + "'";
			PreparedStatement pStatement = conn.prepareStatement(sqlType);
			ResultSet rs = pStatement.executeQuery();
			rs.next();
			return rs.getString("idType");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// S???N PH???M
	private void showDetailsPro(String ma) {
		try {
			String sql1 = "select * from production.Product where idProduct=?";
			preStatement = conn.prepareStatement(sql1);
			preStatement.setString(1, ma);
			ResultSet rs = preStatement.executeQuery();
			if (rs.next()) {
				txtIdPro.setText(rs.getString("idProduct"));
				txtNamePro.setText(rs.getString("nameProduct"));
				txtTypePro.setText(getNameType(rs.getString("type")));
				txtUnitPro.setText(rs.getString("unit"));
				txtCostPro.setText(rs.getInt("cost") + "");
				txtPricePro.setText(rs.getInt("price") + "");
				txtQuantityPro.setText(rs.getInt("quantityOnHand") + "");
				txtDescription.setText(rs.getString("description"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showAllPro() {
		try {
			String sql = "select * from production.Product";
			preStatement = conn.prepareStatement(sql);
			result = preStatement.executeQuery();
			dtmProduct.setRowCount(0);
			while (result.next()) {
				Vector<Object> vec = new Vector<Object>();
				vec.add(result.getString("idProduct"));
				vec.add(result.getString("nameProduct"));
				vec.add(getNameType(result.getString("type")));
				vec.add(result.getString("unit"));
				vec.add(result.getInt("cost"));
				vec.add(result.getInt("price"));
				vec.add(result.getInt("quantityOnHand"));
				vec.add(result.getString("description"));
				dtmProduct.addRow(vec);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

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

	// KH??CH H??NG
	private void showDetailsCus(String ma) {
		try {
			String sql1 = "select * from virtual.v_showFullCus where idCustomer = ?";
			preStatement = conn.prepareStatement(sql1);
			preStatement.setString(1, ma);
			ResultSet rs = preStatement.executeQuery();
			if (rs.next()) {
				txtIdCus.setText(rs.getString("idCustomer"));
				txtNameCus.setText(rs.getString("nameCustomer"));
				txtPhone.setText(rs.getString("phone"));
				txtBirth.setText(rs.getString("birthday"));
				if (rs.getInt("sex") == 0) {
					txtSex.setText("Nam");
				} else {
					txtSex.setText("N???");
				}
				txtAddress.setText(rs.getString("address"));
				txtDayReg.setText(rs.getString("dayRegister"));
				if (rs.getInt("permission") == 0) {
					txtPermission.setText("Kh??ng");
				} else {
					txtPermission.setText("C??");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showAllCus() {
		try {
			String sql = "select * from virtual.v_showFullCus";
			preStatement = conn.prepareStatement(sql);
			result = preStatement.executeQuery();
			dtmCustomer.setRowCount(0);
			while (result.next()) {
				Vector<Object> vec = new Vector<Object>();
				vec.add(result.getString("idCustomer"));
				vec.add(result.getString("nameCustomer"));
				vec.add(result.getString("phone"));
				vec.add(result.getString("birthday"));
				if (result.getInt("sex") == 0) {
					vec.add("Nam");
				} else {
					vec.add("N???");
				}
				vec.add(result.getString("address"));
				vec.add(result.getString("dayRegister"));
				if (result.getInt("permission") == 0) {
					vec.add("Kh??ng");
				} else {
					vec.add("C??");
				}
				dtmCustomer.addRow(vec);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// Qu???n tr??? vi??n
	private void showDetailsAd(String ma) {
		try {
			String sql1 = "select * from person.Administrator where idAdmin = ?";
			preStatement = conn.prepareStatement(sql1);
			preStatement.setString(1, ma);
			ResultSet rs = preStatement.executeQuery();
			if (rs.next()) {
				txtIdAd.setText(rs.getString("idAdmin"));
				txtNameAd.setText(rs.getString("nameAdmin"));
				txtUserName.setText(rs.getString("username"));
				txtPass.setText(rs.getString("password"));
				txtPhoneAd.setText(rs.getString("phone"));
				txtBirthAd.setText(rs.getString("birthday"));
				if (rs.getInt("sex") == 0) {
					txtSexAd.setText("Nam");
				} else {
					txtSexAd.setText("N???");
				}
				txtNative.setText(rs.getString("address"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showAllAd() {
		try {
			String sql = "select * from person.Administrator";
			preStatement = conn.prepareStatement(sql);
			result = preStatement.executeQuery();
			dtmAdministrator.setRowCount(0);
			while (result.next()) {
				Vector<Object> vec = new Vector<Object>();
				vec.add(result.getString("idAdmin"));
				vec.add(result.getString("nameAdmin"));
				vec.add(result.getString("username"));
				vec.add(result.getString("password"));
				vec.add(result.getString("phone"));
				vec.add(result.getString("birthday"));
				if (result.getInt("sex") == 0) {
					vec.add("Nam");
				} else {
					vec.add("N???");
				}
				vec.add(result.getString("address"));
				dtmAdministrator.addRow(vec);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// K???T N???I CSDL
	private void connectDatabase() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyBanHang;integratedSecurity=true;";
			conn = DriverManager.getConnection(connectionUrl);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void addEvents() {

		cbFilterTP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cbFilterTP.getSelectedIndex() == 0) {
					showAllPro();
				} else {
					showTypePro(cbFilterTP.getSelectedIndex());
				}
			}
		});

		cbChooseSort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sortBy(cbFilterTP.getSelectedIndex());
			}
		});
		btImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImportPro importPro = new ImportPro();
				importPro.showWindow();
			}
		});
		btRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAllCus();
			}
		});
		btRefreshAd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAllAd();
			}
		});
		btAddType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddType adt = new AddType();
				adt.showWindow();
			}
		});

		btAddPro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddPro();
			}
		});

		btUpdatePro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UpdatePro();
			}
		});

		btDeletePro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeletePro();
			}
		});
		btReckon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Reckon rec = new Reckon();
				rec.showWindow();
			}
		});
		btTopOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TopOrder top = new TopOrder();
				top.showWindow();
			}
		});

		btSearchPro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SearchPro();
			}
		});

		btSearchCus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SearchCus();
			}
		});
		btConfirmBill.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ConfirmBill cfb = new ConfirmBill();
				cfb.showWindow();
				cfb.showBillNotConfirm();
			}
		});

		btDeleteCus.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DeleteCus();
			}

		});

		btUpdatePerCus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UpdatePermission upPer = new UpdatePermission();
				upPer.showWindow();
			}
		});
		// Qu???n tr??? vi??n
		btAddAd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddAd();
			}
		});

		btUpdateAd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UpdateAd();
			}
		});

		btDeleteAd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeleteAd();
			}
		});
		// S???n ph???m
		tblProduct.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = tblProduct.getSelectedRow();
				if (row == -1)
					return;
				String ma = (String) tblProduct.getValueAt(row, 0); // s??? 0 l?? l???y ch??? s??? ?????u ti??n c???a h??ng
				showDetailsPro(ma);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}
		});
		// Kh??ch h??ng
		tblCustomer.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = tblCustomer.getSelectedRow();
				if (row == -1)
					return;
				String ma = (String) tblCustomer.getValueAt(row, 0); // s??? 0 l?? l???y ch??? s??? ?????u ti??n c???a h??ng
				showDetailsCus(ma);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}
		});

		// Qu???n tr??? vi??n
		tblAdministrator.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = tblAdministrator.getSelectedRow();
				if (row == -1)
					return;
				String ma = (String) tblAdministrator.getValueAt(row, 0); // s??? 0 l?? l???y ch??? s??? ?????u ti??n c???a h??ng
				showDetailsAd(ma);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}
		});
	}

	// S???n ph???m
	private void AddPro() {
		if (txtIdPro.getText().equals(""))
			JOptionPane.showMessageDialog(null, "Vui l??ng nh???p m?? s???n ph???m", "Th??ng b??o", JOptionPane.ERROR_MESSAGE);
		else if (checkExIdPro(txtIdPro.getText())) {
			JOptionPane.showMessageDialog(null, "M?? [" + txtIdPro.getText() + "] ???? t???n t???i. H??y nh???p l???i!");
		} else {
			try {
				String sql = "insert into production.Product values (?,?,?,?,?,?,?,?)";
				preStatement = conn.prepareStatement(sql);
				if (txtIdPro.getText().equals("")) {
					preStatement.setString(1, aii.getIdProduct(getIdType(txtTypePro.getText())));
				} else {
					preStatement.setString(1, txtIdPro.getText());
				}
				preStatement.setString(2, txtNamePro.getText());
				preStatement.setString(3, getIdType(txtTypePro.getText()));
				preStatement.setString(4, txtUnitPro.getText());
				preStatement.setInt(5, Integer.parseInt(txtCostPro.getText()));
				preStatement.setInt(6, Integer.parseInt(txtPricePro.getText()));
				preStatement.setInt(7, Integer.parseInt(txtQuantityPro.getText()));
				preStatement.setString(8, txtDescription.getText());

				int x = preStatement.executeUpdate();
				if (x > 0) {
					showAllPro();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void UpdatePro() {
		if (!checkExIdPro(txtIdPro.getText())) {
			JOptionPane.showMessageDialog(null, "M?? [" + txtIdPro.getText() + "] ch??a t???n t???i. Ki???m tra l???i!");
		} else {
			int NotifyUp = JOptionPane.showConfirmDialog(null,
					"B???n c?? mu???n s???a m?? [" + txtIdPro.getText() + "] n??y kh??ng ?", "X??c nh???n s???a",
					JOptionPane.YES_NO_OPTION);
			if (NotifyUp == JOptionPane.YES_OPTION)
				try {
					String sql = "update production.Product set nameProduct=?, type=?, unit=?, cost=?, price=?, quantityOnHand=?, description=? where idProduct=?";
					preStatement = conn.prepareStatement(sql);
					preStatement.setString(1, txtNamePro.getText());
					preStatement.setString(2, getIdType(txtTypePro.getText()));
					preStatement.setString(3, txtUnitPro.getText());
					preStatement.setInt(4, Integer.parseInt(txtCostPro.getText()));
					preStatement.setInt(5, Integer.parseInt(txtPricePro.getText()));
					preStatement.setInt(6, Integer.parseInt(txtQuantityPro.getText()));
					preStatement.setString(7, txtDescription.getText());
					preStatement.setString(8, txtIdPro.getText());
					int x = preStatement.executeUpdate();
					if (x > 0) {
						showAllPro();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}

	private void DeleteCus() {
		if (!checkExIdCus(txtIdCus.getText())) {
			JOptionPane.showMessageDialog(null, "M?? kh??ng t???n t???i, vui l??ng ch???n l???i ?????i t?????ng", "Th??ng b??o x??a",
					JOptionPane.ERROR_MESSAGE);
		} else {
			int ret = JOptionPane.showConfirmDialog(null,
					"B???n c?? ch???c ch???n mu???n x??a m?? [" + txtIdCus.getText() + "] n??y kh??ng?", "X??c nh???n x??a?",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (ret == JOptionPane.YES_OPTION) {
				try {
					String sql = "delete from person.Users where idUser=?";
					preStatement = conn.prepareStatement(sql);
					preStatement.setString(1, txtIdCus.getText());
					int x = preStatement.executeUpdate();
					if (x > 0) {
						showAllCus();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	private void DeletePro() {
		if (!checkExIdPro(txtIdPro.getText())) {
			JOptionPane.showMessageDialog(null, "M?? kh??ng t???n t???i, vui l??ng ch???n s???n ph???m", "Th??ng b??o x??a",
					JOptionPane.ERROR_MESSAGE);
		} else {
			int ret = JOptionPane.showConfirmDialog(null,
					"B???n c?? ch???c ch???n mu???n x??a m?? [" + txtIdPro.getText() + "] n??y kh??ng?", "X??c nh???n x??a?",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (ret == JOptionPane.YES_OPTION) {
				try {
					String sql = "delete from production.Product where idProduct=?";
					preStatement = conn.prepareStatement(sql);
					preStatement.setString(1, txtIdPro.getText());
					int x = preStatement.executeUpdate();
					if (x > 0) {
						showAllPro();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

	}

	// Qu???n tr??? vi??n
	private void AddAd() {
		if (checkExIdAd(txtIdAd.getText())) {
			JOptionPane.showMessageDialog(null, "M?? [" + txtIdPro.getText() + "] ???? t???n t???i. H??y nh???p l???i!");
		} else {
			try {
				String sql = "insert into person.Administrator values (?,?,?,?,?,?,?,?)";
				preStatement = conn.prepareStatement(sql);
				preStatement.setString(1, txtIdAd.getText());
				preStatement.setString(2, txtNameAd.getText());
				preStatement.setString(3, txtUserName.getText());
				preStatement.setString(4, txtPass.getText());
				preStatement.setString(5, txtPhoneAd.getText());
				preStatement.setString(6, txtBirthAd.getText());
				if (txtSexAd.getText().equals("Nam")) {
					preStatement.setInt(7, 0);
				} else {
					preStatement.setInt(7, 1);
				}
				preStatement.setString(8, txtNative.getText());
				int x = preStatement.executeUpdate();
				if (x > 0) {
					showAllAd();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void UpdateAd() {
		if (!checkExIdAd(txtIdAd.getText())) {
			JOptionPane.showMessageDialog(null, "M?? [" + txtIdAd.getText() + "] ch??a t???n t???i. Ki???m tra l???i!");
		} else {
			int NotifyUp = JOptionPane.showConfirmDialog(null,
					"B???n c?? mu???n s???a m?? [" + txtIdAd.getText() + "] n??y kh??ng ?", "X??c nh???n s???a",
					JOptionPane.YES_NO_OPTION);
			if (NotifyUp == JOptionPane.YES_OPTION)
				try {
					String sql = "update person.Administrator set nameAdmin=?, username=?, password=?, phone=?, birthday=?, sex=?, address=? where idAdmin=?";
					preStatement = conn.prepareStatement(sql);
					preStatement.setString(1, txtNameAd.getText());
					preStatement.setString(2, txtUserName.getText());
					preStatement.setString(3, txtPass.getText());
					preStatement.setString(4, txtPhoneAd.getText());
					preStatement.setString(5, txtBirthAd.getText());
					if (txtSexAd.getText().equals("Nam")) {
						preStatement.setInt(6, 0);
					} else {
						preStatement.setInt(6, 1);
					}
					preStatement.setString(7, txtNative.getText());
					preStatement.setString(8, txtIdAd.getText());
					int x = preStatement.executeUpdate();
					if (x > 0) {
						showAllAd();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}

	private void DeleteAd() {
		// DeleteDTM(dtmProduct);
		if (!checkExIdAd(txtIdAd.getText())) {
			JOptionPane.showMessageDialog(null, "M?? kh??ng t???n t???i. H??y nh???p l???i!", "Th??ng b??o x??a",
					JOptionPane.OK_OPTION);
		} else {
			int ret = JOptionPane.showConfirmDialog(null,
					"B???n c?? ch???c ch???n mu???n x??a m?? [" + txtIdAd.getText() + "] n??y kh??ng?", "X??c nh???n x??a?",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (ret == JOptionPane.YES_OPTION) {
				try {
					String sql = "delete from person.Administrator where idAdmin=?";
					preStatement = conn.prepareStatement(sql);
					preStatement.setString(1, txtIdAd.getText());
					int x = preStatement.executeUpdate();
					if (x > 0) {
						showAllPro();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

	}

	// T??m ki???m s???n ph???m theo t??n
	protected void SearchPro() {
		try {
			CallableStatement callStatement = conn.prepareCall("{call virtual.proc_searchByName(?)}");
			callStatement.setString(1, txtSearchPro.getText());
			result = callStatement.executeQuery();
			dtmProduct.setRowCount(0);
			while (result.next()) {
				Vector<Object> vec = new Vector<Object>();
				vec.add(result.getString("idProduct"));
				vec.add(result.getString("nameProduct"));
				vec.add(getNameType(result.getString("type")));
				vec.add(result.getString("unit"));
				vec.add(result.getInt("cost"));
				vec.add(result.getInt("price"));
				vec.add(result.getInt("quantityOnHand"));
				vec.add(result.getString("description"));
				dtmProduct.addRow(vec);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// T??m ki???m kh??ch h??ng theo t??n
	protected void SearchCus() {
		try {
//			CallableStatement callStatement = conn.prepareCall("{call TimKiemKhachHangTheoTenn(?)}");
//			callStatement.setString(1, txtSearchCus.getText());
//			result = callStatement.executeQuery();
			String sql = "select * from virtual.v_showFullCus where nameCustomer like '%" + txtSearchCus.getText()
					+ "%'";
			preStatement = conn.prepareStatement(sql);
			result = preStatement.executeQuery();
			dtmCustomer.setRowCount(0);
			while (result.next()) {
				Vector<Object> vec = new Vector<Object>();
				vec.add(result.getString("idCustomer"));
				vec.add(result.getString("nameCustomer"));
				vec.add(result.getString("phone"));
				vec.add(result.getString("birthday"));
				if (result.getInt("sex") == 0) {
					vec.add("Nam");
				} else {
					vec.add("N???");
				}
				vec.add(result.getString("address"));
				vec.add(result.getString("dayRegister"));
				if (result.getInt("permission") == 0) {
					vec.add("Kh??ng");
				} else {
					vec.add("C??");
				}
				dtmCustomer.addRow(vec);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void addControls() {

		JPanel pnProduct = new JPanel();
		JPanel pnCustomer = new JPanel();
		JPanel pnAdministrator = new JPanel();

		JTabbedPane tbManager = new JTabbedPane();
		tbManager.setBounds(0, 0, 1190, 900);
		tbManager.add("S???N PH???M", pnProduct);
		tbManager.add("KH??CH H??NG", pnCustomer);
		tbManager.add("QU???N TR??? VI??N", pnAdministrator);
		tbManager.setFont(fGeneral);
		this.add(tbManager);
		pnProduct.setLayout(new BoxLayout(pnProduct, BoxLayout.Y_AXIS));

		// B???NG 1: S???N PH???M
		// Dong 1: LOGO + T??N (CH??A ?????T)
		JPanel pnLogo = new JPanel();
		pnLogo.setLayout(new FlowLayout(FlowLayout.CENTER));
		pnLogo.setPreferredSize(new Dimension(100, 100));
		pnProduct.add(pnLogo);
		ImageIcon logo = new ImageIcon(
				new ImageIcon("Image\\xeday.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
		JLabel lblLogo = new JLabel();
		lblLogo.setIcon(logo);
		pnLogo.add(lblLogo);
		JLabel lblNameProgram = new JLabel("KMP Market");
		lblNameProgram.setFont(new Font("arial", Font.ITALIC, 40));
		lblNameProgram.setForeground(Color.BLACK);
		pnLogo.add(lblNameProgram);

		// Dong 2
		Font fComboBox = new Font("Arial", Font.BOLD, 18);
		JPanel pnFunction1 = new JPanel();
		pnFunction1.setLayout(new BoxLayout(pnFunction1, BoxLayout.X_AXIS));
		pnProduct.add(pnFunction1);

		// 2.1 Chuc nang loc
		JPanel pnFilterTP = new JPanel();
		pnFilterTP.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnFunction1.add(pnFilterTP);

		// Hi???n th??? ch??? l???c
		JLabel lblFilterTP = new JLabel("L???c theo: ");
		lblFilterTP.setFont(fComboBox);
		pnFilterTP.add(lblFilterTP);

		// Hi???n c??c l???a ch???n
		cbFilterTP.setFont(fComboBox);
		pnFilterTP.add(cbFilterTP);

		// 2.2 Chuc nang tim kiem
		JPanel pnSearchPro = new JPanel();
		pnSearchPro.setLayout(new FlowLayout(FlowLayout.CENTER));
		pnFunction1.add(pnSearchPro);
		// T???o ?? t??m ki???m
		txtSearchPro = new JTextField(30);
		txtSearchPro.setText("Nh???p t??n s???n ph???m");

		txtSearchPro.setFont(fGeneral);
		pnSearchPro.add(txtSearchPro);
		// T???o n??t t??m ki???m
		btSearchPro = new JButton("T??m ki???m");
		btSearchPro.setFont(fComboBox);
		pnSearchPro.add(btSearchPro);

		// 2.3 Chuc nang sap xep
		JPanel pnSort = new JPanel();
		pnSort.setLayout(new FlowLayout(FlowLayout.RIGHT));
		pnFunction1.add(pnSort);

		// Hi???n th??? ch??? s???p x???p theo
		JLabel lblSort = new JLabel("S???p x???p theo: ");
		lblSort.setFont(fComboBox);
		pnSort.add(lblSort);

		// C??c l???a ch???n
//		String chooseSort[] = { "M??", "T??n", "Gi?? nh???p", "Gi?? b??n" };
//		JComboBox cbChooseSort = new JComboBox();

		cbChooseSort.setFont(fComboBox);
		pnSort.add(cbChooseSort);

		// Dong 3
		JPanel pnFunction2 = new JPanel();
		pnFunction2.setLayout(new FlowLayout());
		pnProduct.add(pnFunction2);

		// Chia ????? tr???
		JPanel pnSeperate = new JPanel();
		pnSeperate.setLayout(new BoxLayout(pnSeperate, BoxLayout.X_AXIS));
		pnFunction2.add(pnSeperate);

		// T???o khung b??n tr??i
		JPanel pnSeperateLeft = new JPanel();
		pnSeperateLeft.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnSeperate.add(pnSeperateLeft);

		// T???o c??c thao t??c x??? l??
		JPanel pnHandlePro = new JPanel();
		pnHandlePro.setLayout(new BoxLayout(pnHandlePro, BoxLayout.Y_AXIS));
		pnSeperateLeft.add(pnHandlePro);

		// T???o hi???n th??? th??ng tin
		JPanel pnInfoPro = new JPanel();
		pnInfoPro.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel lblInfoPro = new JLabel("Th??ng tin chi ti???t c???a t???ng s???n ph???m");
		pnInfoPro.add(lblInfoPro);
		pnHandlePro.add(pnInfoPro);

		// T???o hi???n th??? m??
		JPanel pnIdPro = new JPanel();
		pnIdPro.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblIdPro = new JLabel("M??: ");
		txtIdPro = new JTextField(15);
		pnIdPro.add(lblIdPro);
		pnIdPro.add(txtIdPro);
//		txtIdPro.setEditable(false);
		pnHandlePro.add(pnIdPro);

		// T???o hi???n th??? t??n
		JPanel pnNamePro = new JPanel();
		pnNamePro.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblNamePro = new JLabel("T??n: ");
		txtNamePro = new JTextField(15);
		pnNamePro.add(lblNamePro);
		pnNamePro.add(txtNamePro);
		pnHandlePro.add(pnNamePro);

		// T???o hi???n th??? lo???i
		JPanel pnTypePro = new JPanel();
		pnTypePro.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblTypePro = new JLabel("Lo???i: ");
		txtTypePro = new JTextField(15);
		pnTypePro.add(lblTypePro);
		pnTypePro.add(txtTypePro);
//		txtTypePro.setEditable(false);
		pnHandlePro.add(pnTypePro);

		// T???o hi???n th??? gi?? nh???p
		JPanel pnCostPro = new JPanel();
		pnCostPro.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblCostPro = new JLabel("Gi?? nh???p: ");
		txtCostPro = new JTextField(15);
		pnCostPro.add(lblCostPro);
		pnCostPro.add(txtCostPro);
		pnHandlePro.add(pnCostPro);

		// T???o hi???n th??? gi?? b??n
		JPanel pnPricePro = new JPanel();
		pnPricePro.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblPricePro = new JLabel("Gi?? b??n: ");
		txtPricePro = new JTextField(15);
		pnPricePro.add(lblPricePro);
		pnPricePro.add(txtPricePro);
		pnHandlePro.add(pnPricePro);

		// T???o hi???n th??? s??? l?????ng
		JPanel pnQuantityPro = new JPanel();
		pnQuantityPro.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblQuantityPro = new JLabel("S??? l?????ng: ");
		txtQuantityPro = new JTextField(15);
		pnQuantityPro.add(lblQuantityPro);
		pnQuantityPro.add(txtQuantityPro);
		txtQuantityPro.setEditable(false);
		pnHandlePro.add(pnQuantityPro);

		// T???o hi???n th??? ????n v??? t??nh
		JPanel pnUnitPro = new JPanel();
		pnUnitPro.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblUnitPro = new JLabel("????n v??? t??nh: ");
		txtUnitPro = new JTextField(15);
		pnUnitPro.add(lblUnitPro);
		pnUnitPro.add(txtUnitPro);
		pnHandlePro.add(pnUnitPro);

		// T???o hi???n th??? m?? t???
		JPanel pnDescription = new JPanel();
		pnDescription.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblDescription = new JLabel("M?? t???: ");
		txtDescription = new JTextField(15);
		pnDescription.add(lblDescription);
		pnDescription.add(txtDescription);
		pnHandlePro.add(pnDescription);

		lblIdPro.setPreferredSize(lblUnitPro.getPreferredSize());
		lblNamePro.setPreferredSize(lblUnitPro.getPreferredSize());
		lblTypePro.setPreferredSize(lblUnitPro.getPreferredSize());
		lblCostPro.setPreferredSize(lblUnitPro.getPreferredSize());
		lblPricePro.setPreferredSize(lblUnitPro.getPreferredSize());
		lblQuantityPro.setPreferredSize(lblUnitPro.getPreferredSize());
		lblDescription.setPreferredSize(lblUnitPro.getPreferredSize());

		// Nh???p h??ng
		JPanel pnImport = new JPanel();
		pnImport.setPreferredSize(new Dimension(200, 40));
		pnHandlePro.add(pnImport);
		// T???o n??t nh???p h??ng
		btImport = new JButton("Nh???p h??ng");
		btImport.setFont(fGeneral);
		pnImport.add(btImport);

		// X??? l?? th??m s???n ph???m m???i
		JPanel pnAdd = new JPanel();
		pnAdd.setPreferredSize(new Dimension(200, 40));
		pnHandlePro.add(pnAdd);
		// T???o n??t th??m s???n ph???m m???i
		btAddPro = new JButton("Th??m s???n ph???m");
		btAddPro.setFont(fGeneral);
		pnAdd.add(btAddPro);

		// X??? l?? th??m lo???i s???n ph???m m???i
		JPanel pnAddType = new JPanel();
		pnAddType.setPreferredSize(new Dimension(200, 40));
		pnHandlePro.add(pnAddType);
		// T???o n??t th??m lo???i s???n ph???m m???i
		btAddType = new JButton("Th??m lo???i m???i");
		btAddType.setFont(fGeneral);
		pnAddType.add(btAddType);

		// X??? l?? s???a
		JPanel pnUpdate = new JPanel();
		pnUpdate.setPreferredSize(new Dimension(200, 40));
		pnHandlePro.add(pnUpdate);
		// T???o n??t s???a
		btUpdatePro = new JButton("S???a s???n ph???m");
		btUpdatePro.setFont(fGeneral);
		pnUpdate.add(btUpdatePro);

		// X??? l?? x??a
		JPanel pnDeletePro = new JPanel();
		pnDeletePro.setPreferredSize(new Dimension(200, 40));
		pnHandlePro.add(pnDeletePro);
		// T???o n??t x??a
		btDeletePro = new JButton("X??a s???n ph???m");
		btDeletePro.setFont(fGeneral);
		pnDeletePro.add(btDeletePro);

		// X??? l?? th???ng k??
		JPanel pnReckon = new JPanel();
		pnReckon.setPreferredSize(new Dimension(200, 40));
		pnHandlePro.add(pnReckon);
		// T???o n??t th???ng k??
		btReckon = new JButton("Th???ng k??");
		btReckon.setFont(fGeneral);
		pnReckon.add(btReckon);

		// T???o ghi ch?? t??c gi???
//		JPanel pnNote1 = new JPanel();
//		pnNote1.setLayout(new FlowLayout(FlowLayout.CENTER));
//		JLabel lblNote1 = new JLabel("Thi???t k??? b???i Chu Qu?? T???c");
//		pnNote1.add(lblNote1);
//		pnHandlePro.add(pnNote1);
//
//		JPanel pnNote2 = new JPanel();
//		pnNote2.setLayout(new FlowLayout(FlowLayout.CENTER));
//		JLabel lblNote2 = new JLabel("@Chu");
//		pnNote2.add(lblNote2);
//		pnHandlePro.add(pnNote2);

		// T???o khung b??n ph???i
		JPanel pnSeperateRight = new JPanel();
		pnSeperateRight.setLayout(new FlowLayout(FlowLayout.RIGHT));
		pnSeperateRight.setBackground(Color.BLACK);
		pnSeperate.add(pnSeperateRight);

		dtmProduct = new DefaultTableModel();
		dtmProduct.addColumn("M??");
		dtmProduct.addColumn("T??n");
		dtmProduct.addColumn("Lo???i");
		dtmProduct.addColumn("????n v??? t??nh");
		dtmProduct.addColumn("Gi?? nh???p");
		dtmProduct.addColumn("Gi?? b??n");
		dtmProduct.addColumn("S??? l?????ng");
		dtmProduct.addColumn("M?? t???");
		tblProduct = new JTable(dtmProduct);

		JScrollPane scTable = new JScrollPane(tblProduct, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scTable.setPreferredSize(new Dimension(896, 520));
		pnSeperateRight.add(scTable);

		// B???NG 2: KH??CH H??NG
		// Dong 1
		JPanel pnLine1 = new JPanel();
		pnLine1.setLayout(new FlowLayout());
		pnLine1.setPreferredSize(new Dimension(1200, 122));
		pnCustomer.add(pnLine1);

		// Chia ????? tr???
		JPanel pnSeperateCus = new JPanel();
		pnSeperateCus.setLayout(new BoxLayout(pnSeperateCus, BoxLayout.X_AXIS));
		pnLine1.add(pnSeperateCus);

		// T???o khung b??n tr??i ch???a logo
		JPanel pnSeperateCusLeft = new JPanel();
		pnSeperateCusLeft.setPreferredSize(new Dimension(300, 120));
		pnSeperateCusLeft.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnSeperateCus.add(pnSeperateCusLeft);

		// B???t ?????u t???o logo
		JLabel lblLogoCus = new JLabel();
		lblLogoCus.setIcon(logo);
		pnSeperateCusLeft.add(lblLogoCus);

		// T???o khung b??n ph???i ch???a ?? t??m ki???m
		JPanel pnSeperateCusRight = new JPanel();
		pnSeperateCusRight.setLayout(new FlowLayout(FlowLayout.RIGHT));
		pnSeperateCus.add(pnSeperateCusRight);

		// T??M KI???M
		JPanel pnSearchCus = new JPanel();
		pnSearchCus.setLayout(new FlowLayout(FlowLayout.RIGHT));
		pnSeperateCusRight.add(pnSearchCus);

		// T???o ?? t??m ki???m
		txtSearchCus = new JTextField(20);
		txtSearchCus.setText("Nh???p t??n kh??ch h??ng");

		txtSearchCus.setFont(fGeneral);
		pnSearchCus.add(txtSearchCus);

		// T???o n??t t??m ki???m
		btSearchCus = new JButton("T??m ki???m");
		btSearchCus.setFont(fComboBox);
		pnSearchCus.add(btSearchCus);

		// L??m m???i
		JPanel pnRefresh = new JPanel();
		pnRefresh.setLayout(new FlowLayout(FlowLayout.RIGHT));
		pnSeperateCusRight.add(pnRefresh);

		// T???o n??t l??m m???i
		btRefresh = new JButton("L??m m???i");
		btRefresh.setFont(fComboBox);
		pnRefresh.add(btRefresh);

		// Dong 2
		JPanel pnLine2 = new JPanel();
		pnLine2.setLayout(new FlowLayout());
		pnCustomer.add(pnLine2);

		// Chia ????? tr???
		JPanel pnSeperate2 = new JPanel();
		pnSeperate2.setLayout(new BoxLayout(pnSeperate2, BoxLayout.X_AXIS));
		pnLine2.add(pnSeperate2);

		// T???o khung b??n tr??i ch???a ch???c n??ng kh??ch h??ng
		JPanel pnSeperateLeft2 = new JPanel();
		pnSeperateLeft2.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnSeperate2.add(pnSeperateLeft2);

		// T???o c??c thao t??c x??? l??
		JPanel pnHandleCus = new JPanel();
		pnHandleCus.setLayout(new BoxLayout(pnHandleCus, BoxLayout.Y_AXIS));
		pnSeperateLeft2.add(pnHandleCus);

		// T???o hi???n th??? th??ng tin kh??ch h??ng
		JPanel pnInfoCus = new JPanel();
		pnInfoCus.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel lblInfoCus = new JLabel("Th??ng tin chi ti???t c???a t???ng kh??ch h??ng");
		pnInfoCus.add(lblInfoCus);
		pnHandleCus.add(pnInfoCus);

		// T???o hi???n th??? m??
		JPanel pnIdCus = new JPanel();
		pnIdCus.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblIdCus = new JLabel("M??: ");
		txtIdCus = new JTextField(15);
		pnIdCus.add(lblIdCus);
		pnIdCus.add(txtIdCus);
		pnHandleCus.add(pnIdCus);

		// T???o hi???n th??? t??n
		JPanel pnNameCus = new JPanel();
		pnNameCus.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblNameCus = new JLabel("T??n: ");
		txtNameCus = new JTextField(15);
		pnNameCus.add(lblNameCus);
		pnNameCus.add(txtNameCus);
		pnHandleCus.add(pnNameCus);

		// T???o hi???n th??? s??? ??i???n tho???i
		JPanel pnPhone = new JPanel();
		pnPhone.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblPhone = new JLabel("S??? ??i???n tho???i: ");
		txtPhone = new JTextField(15);
		pnPhone.add(lblPhone);
		pnPhone.add(txtPhone);
		pnHandleCus.add(pnPhone);

		// T???o hi???n th??? ng??y sinh
		JPanel pnBirth = new JPanel();
		pnBirth.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblBirth = new JLabel("Ng??y sinh: ");
		txtBirth = new JTextField(15);
		pnBirth.add(lblBirth);
		pnBirth.add(txtBirth);
		pnHandleCus.add(pnBirth);

		// T???o hi???n th??? gi???i t??nh
		JPanel pnSex = new JPanel();
		pnSex.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblSex = new JLabel("Gi???i t??nh: ");
		txtSex = new JTextField(15);
		pnSex.add(lblSex);
		pnSex.add(txtSex);
		pnHandleCus.add(pnSex);

		// T???o hi???n th??? ?????a ch???
		JPanel pnAddress = new JPanel();
		pnAddress.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblAddress = new JLabel("?????a ch???: ");
		txtAddress = new JTextField(15);
		pnAddress.add(lblAddress);
		pnAddress.add(txtAddress);
		pnHandleCus.add(pnAddress);

		// T???o hi???n th??? ng??y ????ng k??
		JPanel pnDayReg = new JPanel();
		pnDayReg.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblDayReg = new JLabel("Ng??y ????ng k??: ");
		txtDayReg = new JTextField(15);
		pnDayReg.add(lblDayReg);
		pnDayReg.add(txtDayReg);
		pnHandleCus.add(pnDayReg);

		// T???o hi???n th??? c???p ph??p
		JPanel pnPermission = new JPanel();
		pnPermission.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblPermission = new JLabel("C???p ph??p: ");
		txtPermission = new JTextField(15);
		pnPermission.add(lblPermission);
		pnPermission.add(txtPermission);
		pnHandleCus.add(pnPermission);

		lblIdCus.setPreferredSize(lblPhone.getPreferredSize());
		lblNameCus.setPreferredSize(lblPhone.getPreferredSize());
		lblBirth.setPreferredSize(lblPhone.getPreferredSize());
		lblSex.setPreferredSize(lblPhone.getPreferredSize());
		lblAddress.setPreferredSize(lblPhone.getPreferredSize());
		lblDayReg.setPreferredSize(lblPhone.getPreferredSize());
		lblPermission.setPreferredSize(lblPhone.getPreferredSize());

		// x??? l?? x??c nh???n ????n
		JPanel pnConfirmBill = new JPanel();
		pnHandleCus.add(pnConfirmBill);
		// tao nut xac nhan don
		btConfirmBill = new JButton("X??c nh???n ????n h??ng");
		btConfirmBill.setFont(fGeneral);
		pnConfirmBill.add(btConfirmBill);

		// X??? l?? s???a quy???n truy c???p
		JPanel pnUpdatePerCus = new JPanel();
		pnHandleCus.add(pnUpdatePerCus);
		// T???o n??t s???a
		btUpdatePerCus = new JButton("S???a quy???n truy c???p");
		btUpdatePerCus.setFont(fGeneral);
		pnUpdatePerCus.add(btUpdatePerCus);

		// X??? l?? x??a
		JPanel pnDeleteCus = new JPanel();
		pnHandleCus.add(pnDeleteCus);
		// T???o n??t x??a
		btDeleteCus = new JButton("X??a kh??ch h??ng");
		btDeleteCus.setFont(fGeneral);
		pnDeleteCus.add(btDeleteCus);

		// X??? l?? th???ng k?? ti??u d??ng
		JPanel pnTopOder = new JPanel();
		pnTopOder.setPreferredSize(new Dimension(200, 100));
		pnHandleCus.add(pnTopOder);
		// T???o n??t th???ng k?? ti??u d??ng
		btTopOrder = new JButton("Top ti??u d??ng");
		btTopOrder.setFont(fGeneral);
		pnTopOder.add(btTopOrder);

		// T???o ghi ch?? t??c gi???
		JPanel pnNote3 = new JPanel();
		pnNote3.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel lblNote3 = new JLabel("Thi???t k??? b???i Chu Qu?? T???c");
		pnNote3.add(lblNote3);
		pnHandleCus.add(pnNote3);

		JPanel pnNote4 = new JPanel();
		pnNote4.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel lblNote4 = new JLabel("@Chu");
		pnNote4.add(lblNote4);
		pnHandleCus.add(pnNote4);

		// T???o khung b??n ph???i ch???a b???ng kh??ch h??ng
		JPanel pnSeperateRight2 = new JPanel();
		pnSeperateRight2.setLayout(new FlowLayout(FlowLayout.RIGHT));
		pnSeperateRight2.setBackground(Color.BLACK);
		pnSeperate2.add(pnSeperateRight2);

		dtmCustomer = new DefaultTableModel();
		dtmCustomer.addColumn("M??");
		dtmCustomer.addColumn("T??n");
		dtmCustomer.addColumn("S??? ??i???n tho???i");
		dtmCustomer.addColumn("Ng??y sinh");
		dtmCustomer.addColumn("Gi???i t??nh");
		dtmCustomer.addColumn("?????a ch???");
		dtmCustomer.addColumn("Ng??y ????ng k??");
		dtmCustomer.addColumn("C???p ph??p");
		tblCustomer = new JTable(dtmCustomer);

		JScrollPane scTableCus = new JScrollPane(tblCustomer, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scTableCus.setPreferredSize(new Dimension(880, 530));
		pnSeperateRight2.add(scTableCus);

		// B???NG 3: QU???N TR??? VI??N
		pnAdministrator.setLayout(new BorderLayout());

		// T???o khung b???n tr??i ch???a thao t??c
		JPanel pnWest = new JPanel();
		pnWest.setLayout(new FlowLayout());
		pnAdministrator.add(pnWest, BorderLayout.WEST);

		// T???o thao t??c x??? l?? qu???n tr??? vi??n
		JPanel pnHandleAd = new JPanel();
		pnHandleAd.setLayout(new BoxLayout(pnHandleAd, BoxLayout.Y_AXIS));
		pnWest.add(pnHandleAd);

		// T???o hi???n th??? th??ng tin qu???n tr??? vi??n
		JPanel pnInfoAd = new JPanel();
		pnInfoAd.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel lblInfoAd = new JLabel("Th??ng tin chi ti???t c???a t???ng qu???n tr??? vi??n");
		pnInfoAd.add(lblInfoAd);
		pnHandleAd.add(pnInfoAd);

		// T???o hi???n th??? m??
		JPanel pnIdAd = new JPanel();
		pnIdAd.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblIdAd = new JLabel("M??: ");
		txtIdAd = new JTextField(15);
		pnIdAd.add(lblIdAd);
		pnIdAd.add(txtIdAd);
		pnHandleAd.add(pnIdAd);

		// T???o hi???n th??? t??n
		JPanel pnNameAd = new JPanel();
		pnNameAd.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblNameAd = new JLabel("T??n: ");
		txtNameAd = new JTextField(15);
		pnNameAd.add(lblNameAd);
		pnNameAd.add(txtNameAd);
		pnHandleAd.add(pnNameAd);

		// T???o hi???n th??? t??n ????ng nh???p
		JPanel pnUserName = new JPanel();
		pnUserName.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblUserName = new JLabel("T??n ????ng nh???p: ");
		txtUserName = new JTextField(15);
		pnUserName.add(lblUserName);
		pnUserName.add(txtUserName);
		pnHandleAd.add(pnUserName);

		// T???o hi???n th??? m???t kh???u
		JPanel pnPass = new JPanel();
		pnPass.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblPass = new JLabel("M???t kh???u: ");
		txtPass = new JTextField(15);
		pnPass.add(lblPass);
		pnPass.add(txtPass);
		pnHandleAd.add(pnPass);

		// T???o hi???n th??? s??? ??i???n tho???i
		JPanel pnPhoneAd = new JPanel();
		pnPhoneAd.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblPhoneAd = new JLabel("S??? ??i???n tho???i: ");
		txtPhoneAd = new JTextField(15);
		pnPhoneAd.add(lblPhoneAd);
		pnPhoneAd.add(txtPhoneAd);
		pnHandleAd.add(pnPhoneAd);

		// T???o hi???n th??? ng??y sinh
		JPanel pnBirthAd = new JPanel();
		pnBirthAd.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblBirthAd = new JLabel("Ng??y sinh: ");
		txtBirthAd = new JTextField(15);
		pnBirthAd.add(lblBirthAd);
		pnBirthAd.add(txtBirthAd);
		pnHandleAd.add(pnBirthAd);

		// T???o hi???n th??? gi???i t??nh
		JPanel pnSexAd = new JPanel();
		pnSexAd.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblSexAd = new JLabel("Gi???i t??nh: ");
		txtSexAd = new JTextField(15);
		pnSexAd.add(lblSexAd);
		pnSexAd.add(txtSexAd);
		pnHandleAd.add(pnSexAd);

		// T???o hi???n th??? qu?? qu??n
		JPanel pnNative = new JPanel();
		pnNative.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblNative = new JLabel("Qu?? qu??n: ");
		txtNative = new JTextField(15);
		pnNative.add(lblNative);
		pnNative.add(txtNative);
		pnHandleAd.add(pnNative);

		lblIdAd.setPreferredSize(lblUserName.getPreferredSize());
		lblNameAd.setPreferredSize(lblUserName.getPreferredSize());
		lblPass.setPreferredSize(lblUserName.getPreferredSize());
		lblPhoneAd.setPreferredSize(lblUserName.getPreferredSize());
		lblBirthAd.setPreferredSize(lblUserName.getPreferredSize());
		lblSexAd.setPreferredSize(lblUserName.getPreferredSize());
		lblNative.setPreferredSize(lblUserName.getPreferredSize());

		// X??? l?? th??m
		JPanel pnAddAd = new JPanel();
		pnAddAd.setLayout(new FlowLayout());
		pnHandleAd.add(pnAddAd);
		// T???o n??t th??m
		btAddAd = new JButton("Th??m qu???n tr??? vi??n");
		btAddAd.setFont(fGeneral);
		pnAddAd.add(btAddAd);

		// X??? l?? s???a
		JPanel pnUpdateAd = new JPanel();
		pnHandleAd.add(pnUpdateAd);
		// T???o n??t s???a
		btUpdateAd = new JButton("S???a qu???n tr??? vi??n");
		btUpdateAd.setFont(fGeneral);
		pnUpdateAd.add(btUpdateAd);

		// X??? l?? x??a
		JPanel pnDeleteAd = new JPanel();
		pnDeleteAd.setLayout(new FlowLayout());
//		pnDeleteAd.setPreferredSize(new Dimension(200, 220));
		pnHandleAd.add(pnDeleteAd);
		// T???o n??t x??a
		btDeleteAd = new JButton("X??a qu???n tr??? vi??n");
		btDeleteAd.setFont(fGeneral);
		pnDeleteAd.add(btDeleteAd);

		// X??? l?? l??m m???i
		JPanel pnRefreshAd = new JPanel();
		pnRefreshAd.setLayout(new FlowLayout());
		pnRefreshAd.setPreferredSize(new Dimension(200, 120));
		pnHandleAd.add(pnRefreshAd);
		// T???o n??t l??m m???i
		btRefreshAd = new JButton("L??m m???i");
		btRefreshAd.setFont(fGeneral);
		pnRefreshAd.add(btRefreshAd);

		// T???o ghi ch?? t??c gi???
		JPanel pnNote5 = new JPanel();
		pnNote5.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel lblNote5 = new JLabel("Thi???t k??? b???i Chu Qu?? T???c");
		pnNote5.add(lblNote5);
		pnHandleAd.add(pnNote5);

		JPanel pnNote6 = new JPanel();
		pnNote6.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel lblNote6 = new JLabel("@Chu");
		pnNote6.add(lblNote6);
		pnHandleAd.add(pnNote6);

		// T???o khung trung t??m ch???a b???ng qu???n tr??? vi??n
		JPanel pnCenter = new JPanel();
		pnCenter.setBackground(Color.BLACK);
		pnAdministrator.add(pnCenter, BorderLayout.CENTER);
		dtmAdministrator = new DefaultTableModel();
		dtmAdministrator.addColumn("M??");
		dtmAdministrator.addColumn("T??n");
		dtmAdministrator.addColumn("T??n ????ng nh???p");
		dtmAdministrator.addColumn("M???t kh???u");
		dtmAdministrator.addColumn("S??? ??i???n tho???i");
		dtmAdministrator.addColumn("Ng??y sinh");
		dtmAdministrator.addColumn("Gi???i t??nh");
		dtmAdministrator.addColumn("Qu?? qu??n");
		tblAdministrator = new JTable(dtmAdministrator);
		JScrollPane scTableAd = new JScrollPane(tblAdministrator, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scTableAd.setPreferredSize(new Dimension(888, 676));
		pnCenter.add(scTableAd);
	}

	public void showWindow() {
		this.setTitle("H??? TH???NG QU???N L??");
		this.setSize(1200, 760);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

}
