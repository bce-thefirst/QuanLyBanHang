package com.market.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.market.jdbc.Connect;
import com.market.services.Auto_Increase_Id;

public class ImportPro {
	JFrame frame = new JFrame();
	private JButton btConfirm, btNext;
	private JTextField txtIdImpDetail, txtIdImport, txtIdPro, txtQuanPro, txtCostPro, txtPricePro, txtNote;

	Connection conn = null;
	PreparedStatement preStatement = null;
	Statement statement = null;
	ResultSet result = null;
	String IdImport;

	Auto_Increase_Id aii = new Auto_Increase_Id();

	public ImportPro() {
		connectDatabase();
		IdImport = aii.getId("production.Import");
		addControl();
		addEvent();
	}

	private void addEvent() {
		btNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Next();
			}
		});
	}

	private void connectDatabase() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyBanHang;integratedSecurity=true;";
			conn = DriverManager.getConnection(connectionUrl);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private boolean checkExType(String id) {
		try {
			String sql1 = "select * from production.TypeProduct where idType=?";
			PreparedStatement pStatement = conn.prepareStatement(sql1);
			pStatement.setString(1, id);
			ResultSet rs = pStatement.executeQuery();
			return rs.next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean checkExIdImp(String id) {
		try {
			String sql2 = "select * from production.Import where idImport=?";
			PreparedStatement pStatement = conn.prepareStatement(sql2);
			pStatement.setString(1, id);
			ResultSet rs = pStatement.executeQuery();
			return rs.next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void Next() {
		try {
			if (checkExIdImp(this.IdImport)) {
				String sql = "insert into production.ImportDetails values (?,?,?,?,?,?,?)";
				preStatement = conn.prepareStatement(sql);
				preStatement.setString(1, this.IdImport);
				preStatement.setString(2, txtIdImpDetail.getText());
				preStatement.setString(3, txtIdPro.getText());
				preStatement.setInt(4, Integer.parseInt(txtQuanPro.getText()));
				preStatement.setInt(5, Integer.parseInt(txtCostPro.getText()));
				preStatement.setInt(6, Integer.parseInt(txtPricePro.getText()));
				preStatement.setString(7, txtNote.getText());
				int x = preStatement.executeUpdate();
				if (x > 0) {
					if (upQuantityOnHandInProduct(txtIdPro.getText(), Integer.parseInt(txtQuanPro.getText())) > 0)
						if (sumCost(Integer.parseInt(txtQuanPro.getText()),
								Integer.parseInt(txtCostPro.getText())) > 0) {

							JOptionPane.showMessageDialog(null, "Th??m m???i th??nh c??ng!");
							txtIdImpDetail.setText(aii.getId("production.ImportDetails"));
							txtIdPro.setText("");
							txtQuanPro.setText("");
							txtCostPro.setText("");
							txtPricePro.setText("");
							txtNote.setText("");
						}
				}
			} else {
				insertNewImp();
				String sql = "insert into production.ImportDetails values (?,?,?,?,?,?,?)";
				preStatement = conn.prepareStatement(sql);
				preStatement.setString(1, txtIdImpDetail.getText());
				preStatement.setString(2, this.IdImport);
				preStatement.setString(3, txtIdPro.getText());
				preStatement.setInt(4, Integer.parseInt(txtQuanPro.getText()));
				preStatement.setInt(5, Integer.parseInt(txtCostPro.getText()));
				preStatement.setInt(6, Integer.parseInt(txtPricePro.getText()));
				preStatement.setString(7, txtNote.getText());
				int x = preStatement.executeUpdate();
				if (x > 0) {
					if (upQuantityOnHandInProduct(txtIdPro.getText(), Integer.parseInt(txtQuanPro.getText())) > 0)
						if (sumCost(Integer.parseInt(txtQuanPro.getText()),
								Integer.parseInt(txtCostPro.getText())) > 0) {

							JOptionPane.showMessageDialog(null, "Th??m m???i th??nh c??ng!");
							txtIdImpDetail.setText(aii.getId("production.ImportDetails"));
							txtIdPro.setText("");
							txtQuanPro.setText("");
							txtCostPro.setText("");
							txtPricePro.setText("");
							txtNote.setText("");
						}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private int upQuantityOnHandInProduct(String idProduct, int quantity) {

		try {
			String upQuantityOnHandInProduct = "update production.Product set quantityOnHand = quantityOnHand + "
					+ quantity + " where idProduct = '" + idProduct + "'";
			Connection cnn = Connect.getConnect();
			PreparedStatement ps = cnn.prepareStatement(upQuantityOnHandInProduct);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return -1;
	}

	private int sumCost(int parseInt, int parseInt2) {
		try {
			String test = "update production.Import set totalCost = totalCost + " + parseInt * parseInt2
					+ " where idImport= '" + this.IdImport + "'";
			preStatement = conn.prepareStatement(test);
			return preStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	private void insertNewImp() {
		try {
			String sqlImp = "insert into production.Import([idImport],[idAdmin],[totalCost]) values (?,?,?)";
			preStatement = conn.prepareStatement(sqlImp);
			preStatement.setString(1, this.IdImport);
			preStatement.setString(2, "ad004");
			preStatement.setInt(3, 0);
			int rs = preStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addControl() {
		// Hi???n th??? th??ng tin nh???p
		JLabel TitleLb = new JLabel("Th??ng tin chi ti???t ????n nh???p");
		TitleLb.setBounds(300, 10, 500, 30);
		TitleLb.setForeground(Color.red.brighter());
		TitleLb.setFont(new Font("arial", Font.ROMAN_BASELINE, 20));
		frame.add(TitleLb);

		// M?? ????n nh???p
		JLabel lblIdImport = new JLabel("M?? ????n nh???p");
		lblIdImport.setBounds(25, 50, 200, 25);
		lblIdImport.setFont(new Font("arial", Font.ROMAN_BASELINE, 20));
		frame.add(lblIdImport);

		// M?? ????n nh???p
		txtIdImport = new JTextField();
		txtIdImport.setText(IdImport);
		txtIdImport.setBounds(200, 50, 450, 25);
		txtIdImport.setFont(new Font("arial", Font.ROMAN_BASELINE, 20));
		frame.add(txtIdImport);

		// Chi ti???t ????n nh???p
		JLabel lblDetail = new JLabel("Chi ti???t ????n nh???p");
		lblDetail.setBounds(25, 100, 300, 25);
		lblDetail.setFont(new Font("arial", Font.ROMAN_BASELINE, 20));
		frame.add(lblDetail);

		// Chi ti???t ????n nh???p
		txtIdImpDetail = new JTextField();
		txtIdImpDetail.setText(aii.getId("production.ImportDetails"));
		txtIdImpDetail.setBounds(200, 100, 450, 25);
		txtIdImpDetail.setFont(new Font("arial", Font.ROMAN_BASELINE, 20));
		frame.add(txtIdImpDetail);

		// Nh???p m?? s???n ph???m
		JLabel lblIdPro = new JLabel("M?? s???n ph???m");
		lblIdPro.setBounds(25, 150, 200, 25);
		lblIdPro.setFont(new Font("arial", Font.ROMAN_BASELINE, 20));
		frame.add(lblIdPro);

		// TextField m?? s???n ph???m
		txtIdPro = new JTextField();
		txtIdPro.setBounds(200, 150, 450, 25);
		txtIdPro.setFont(new Font("arial", Font.ROMAN_BASELINE, 20));
		frame.add(txtIdPro);

		// S??? l?????ng
		JLabel lblQuanPro = new JLabel("S??? l?????ng");
		lblQuanPro.setBounds(25, 200, 120, 25);
		lblQuanPro.setFont(new Font("arial", Font.ROMAN_BASELINE, 20));
		frame.add(lblQuanPro);

		// s??? l?????ng
		txtQuanPro = new JTextField();
		txtQuanPro.setBounds(200, 200, 450, 25);
		txtQuanPro.setFont(new Font("arial", Font.ROMAN_BASELINE, 20));
		frame.add(txtQuanPro);

		// gi?? nh???p
		JLabel lblCostPro = new JLabel("Gi?? nh???p");
		lblCostPro.setBounds(25, 250, 100, 25);
		lblCostPro.setFont(new Font("arial", Font.ROMAN_BASELINE, 20));
		frame.add(lblCostPro);

		// gi?? nh???p
		txtCostPro = new JTextField();
		txtCostPro.setBounds(200, 250, 450, 25);
		txtCostPro.setFont(new Font("arial", Font.ROMAN_BASELINE, 20));
		frame.add(txtCostPro);

		// gi?? b??n
		JLabel lblPricePro = new JLabel("Gi?? b??n");
		lblPricePro.setBounds(25, 300, 100, 25);
		lblPricePro.setFont(new Font("arial", Font.ROMAN_BASELINE, 20));
		frame.add(lblPricePro);

		// gi?? b??n
		txtPricePro = new JTextField();
		txtPricePro.setBounds(200, 300, 450, 25);
		txtPricePro.setFont(new Font("arial", Font.ROMAN_BASELINE, 20));
		frame.add(txtPricePro);

		// ghi ch??
		JLabel lblNote = new JLabel("Ghi ch??");
		lblNote.setBounds(25, 350, 150, 25);
		lblNote.setFont(new Font("arial", Font.ROMAN_BASELINE, 20));
		frame.add(lblNote);

		// ghi ch??
		txtNote = new JTextField();
		txtNote.setBounds(200, 350, 450, 25);
		txtNote.setFont(new Font("arial", Font.ROMAN_BASELINE, 20));
		frame.add(txtNote);

		// N??t x??c nh???n
		btConfirm = new JButton("X??c nh???n");
		btConfirm.setBounds(200, 400, 120, 25);
		btConfirm.setFont(new Font("arial", Font.ROMAN_BASELINE, 17));

		// N??t ti???p theo
		btNext = new JButton("Ti???p theo");
		btNext.setBounds(527, 400, 120, 25);
		btNext.setFont(new Font("arial", Font.ROMAN_BASELINE, 17));
		frame.add(btNext);
		frame.add(btConfirm);
	}

	public void showWindow() {
		frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
		frame.setSize(900, 500);
		frame.setLocationRelativeTo(null); // ?????t ?????u tr??nh l???i
		frame.setLayout(null);
		frame.setResizable(false); // ?????t size c??? ?????nh
		frame.setVisible(true);
	}
}
