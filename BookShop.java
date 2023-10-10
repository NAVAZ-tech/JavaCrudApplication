package CrudApplication;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import net.proteanit.sql.DbUtils;

import javax.swing.border.EtchedBorder;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
public class BookShop {

	private JFrame frame;
	private JTextField txtbname;
	private JTextField txtedition;
	private JTextField txtprice;
	private JTextField txtbid;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BookShop window = new BookShop();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BookShop() {
		initialize();
		Connect();
		table_load();
	}

	Connection con;
	PreparedStatement pat;
	ResultSet rs;
	private JTable table;
	
	public void Connect() {
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        con = DriverManager.getConnection("jdbc:mysql://localhost:3308/book", "root", "root");
	        if (con != null) {
	            System.out.println("Database connected successfully.");
	        } else {
	            System.out.println("Failed to connect to the database.");
	        }
	    } catch (ClassNotFoundException ex) {
	        ex.printStackTrace();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}


	void table_load() {
	    try {
	        pat = con.prepareStatement("select * from book");
	        rs = pat.executeQuery();
	        
	        // Create a DefaultTableModel to hold the data
	        DefaultTableModel model = new DefaultTableModel();
	        
	        // Add columns to the model
	        model.addColumn("ID");
	        model.addColumn("Book Name");
	        model.addColumn("Edition");
	        model.addColumn("Price");
	        
	        // Add rows to the model based on the ResultSet
	        while (rs.next()) {
	        	String id=rs.getString("id");
	            String bname = rs.getString("bname");
	            String edition = rs.getString("edition");
	            String price = rs.getString("price");
	            model.addRow(new Object[]{id,bname, edition, price});
	        }
	        
	        // Set the model for your JTable
	        table.setModel(model);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 619, 422);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Book Shop");
		lblNewLabel.setBackground(new Color(255, 0, 0));
		lblNewLabel.setBounds(221, 11, 177, 46);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		frame.getContentPane().add(lblNewLabel);
		
		JPanel panel = new JPanel();
		panel.setBounds(31, 73, 305, 148);
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Registration", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Book Name");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1.setBounds(10, 24, 89, 26);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Edition");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1_1.setBounds(10, 61, 89, 26);
		panel.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Price");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1_1_1.setBounds(10, 98, 89, 26);
		panel.add(lblNewLabel_1_1_1);
		
		txtbname = new JTextField();
		txtbname.setBounds(120, 24, 115, 20);
		panel.add(txtbname);
		txtbname.setColumns(10);
		
		txtedition = new JTextField();
		txtedition.setColumns(10);
		txtedition.setBounds(120, 66, 115, 20);
		panel.add(txtedition);
		
		txtprice = new JTextField();
		txtprice.setColumns(10);
		txtprice.setBounds(120, 103, 115, 20);
		panel.add(txtprice);
		
		JButton btnNewButton = new JButton("SAVE");
		btnNewButton.setBounds(41, 237, 78, 34);
		btnNewButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String bname, edition, price;

		        bname = txtbname.getText();
		        edition = txtedition.getText();
		        price = txtprice.getText();
		        
		        try {
		            pat = con.prepareStatement("INSERT INTO book (bname, edition, price) VALUES (?, ?, ?)");
		            pat.setString(1, bname);
		            pat.setString(2, edition);
		            pat.setString(3, price);
		            
		            int rowsAffected = pat.executeUpdate();
		            if (rowsAffected > 0) {
		                JOptionPane.showMessageDialog(null, "Record Added ^-^ ");
		                table_load();
		                txtbname.setText("");
		                txtedition.setText("");
		                txtprice.setText("");
		                txtbname.requestFocus();
		            } else {
		                JOptionPane.showMessageDialog(null, "Failed to add record.");
		            }
		        } catch (SQLException el) {
		            el.printStackTrace();
		            JOptionPane.showMessageDialog(null, "Error: " + el.getMessage());
		        }
		    }
		});

		frame.getContentPane().add(btnNewButton);
		
		JButton btnExit = new JButton("EXIT");
		btnExit.setBounds(144, 237, 78, 34);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		frame.getContentPane().add(btnExit);
		
		JButton btnClear = new JButton("CLEAR");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				 txtbname.setText("");
	                txtedition.setText("");
	                txtprice.setText("");
	                txtbname.requestFocus();
				
			}
		});
		btnClear.setBounds(246, 237, 78, 34);
		frame.getContentPane().add(btnClear);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(31, 293, 305, 79);
		panel_1.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("Book ID");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_2.setBounds(10, 27, 76, 27);
		panel_1.add(lblNewLabel_2);
		
		txtbid = new JTextField();
		txtbid.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String id=txtbid.getText();
				try {
					pat=con.prepareStatement("select bname,edition,price from book where id=?");
					pat.setString(1,id);
					 rs=pat.executeQuery();
					 if(rs.next()==true) {
						 String name=rs.getString(1);
						 String edition=rs.getString(2);
						 String price=rs.getString(3);
						 
						 txtbname.setText(name);
						 txtedition.setText(edition);
						 txtprice.setText(price);
						 
					 }else {
						 txtbname.setText("");
						 txtedition.setText("");
						 txtprice.setText("");
					 }
					 } catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		txtbid.setBounds(112, 32, 111, 20);
		panel_1.add(txtbid);
		txtbid.setColumns(10);
		
		JButton btnUpdate = new JButton("UPDATE");
		btnUpdate.setBounds(360, 311, 93, 34);
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String bname, edition, price,bid;

		        bname = txtbname.getText();
		        edition = txtedition.getText();
		        price = txtprice.getText();
		        bid=txtbid.getText();
		        
		        try {
		            pat = con.prepareStatement("Update book set bname=? ,edition=?,price=? where id = ?");
		            pat.setString(1, bname);
		            pat.setString(2, edition);
		            pat.setString(3, price);
		            pat.setString(4, bid);
		            int rowsAffected = pat.executeUpdate();
		            if (rowsAffected > 0) {
		                JOptionPane.showMessageDialog(null, "Record Updated  ^-^ ");
		                table_load();
		                txtbname.setText("");
		                txtedition.setText("");
		                txtprice.setText("");
		                txtbname.requestFocus();
		            } else {
		                JOptionPane.showMessageDialog(null, "Failed to add record.");
		            }
		        } catch (SQLException el) {
		            el.printStackTrace();
		            JOptionPane.showMessageDialog(null, "Error: " + el.getMessage());
		        }
				
				
				
				
			}
		});
		frame.getContentPane().add(btnUpdate);
		
		JButton btnDelete = new JButton("DELETE");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String bid;

		        bid=txtbid.getText();
		        
		        try {
		            pat = con.prepareStatement("delete from book where id = ?");
		          
		            pat.setString(1, bid);
		            
		            int rowsAffected = pat.executeUpdate();
		            if (rowsAffected > 0) {
		                JOptionPane.showMessageDialog(null, "Record Deleted  ^__^ ");
		                table_load();
		                txtbname.setText("");
		                txtedition.setText("");
		                txtprice.setText("");
		                txtbname.requestFocus();
		            } else {
		                JOptionPane.showMessageDialog(null, "Failed to add record.");
		            }
		        } catch (SQLException el) {
		            el.printStackTrace();
		            JOptionPane.showMessageDialog(null, "Error: " + el.getMessage());
		        }
				
				
				
			}
		});
		btnDelete.setBounds(475, 311, 78, 34);
		frame.getContentPane().add(btnDelete);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(360, 68, 212, 165);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
	}
}
