package za.ac.cga;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class Employee extends javax.swing.JFrame {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    
    // employee data
    String empID;
    
    String empFName;
    String empSName;
    String empPhoneNum;
    String empType;
    
    // driver data
    String driverLCode;
    String driverLExpDate;
    Double driverSalary;
    
    // packer data
    Double packerHRate;
    
    public void getEmployeeData()
    {
        empID = txfEmpID.getText();
        empFName = txfEmpFName.getText();
        empSName = txfEmpSName.getText();
        empPhoneNum = txfEmpPhoneNum.getText();
        
        if (rbDriver.isSelected()) {
            empType = "D";
            
            driverLCode = cmbEmpLicenseCode.getSelectedItem().toString();
            driverLExpDate = txfEmpLicenseExpDate.getText();
            driverSalary = Double.parseDouble(spnEmpSalary.getValue().toString());
        }
        if (rbPacker.isSelected()) {
            empType = "P";
            packerHRate = Double.parseDouble(spnEmpHRate.getValue().toString());
        }
    }
    
    /**
     * Creates new form Employee
     */
    public Employee() {
        initComponents();
        connect();
        
        // display data for relevant tables
        showEmployeeData();
        showDriverData();
        showPackerData();
        
        // set enabled to false for components
        txfEmpID.setEnabled(false);
        txfEmpFName.setEnabled(false);
        txfEmpSName.setEnabled(false);
        txfEmpPhoneNum.setEnabled(false);
        
        cmbEmpLicenseCode.setEnabled(false);
        txfEmpLicenseExpDate.setEnabled(false);
        spnEmpSalary.setEnabled(false);
        
        spnEmpHRate.setEnabled(false);
    }
    
    private void connect()
    {
        System.out.println("Connecting to employee, driver, packer database...");
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/order_and_delivery_system","root", "N@z728001");
            
            System.out.println("Connection to employee, driver, packer tables established.");
            
            //con.close();
            
        } catch (ClassNotFoundException e) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE,null,e);
            JOptionPane.showMessageDialog(this,"Connection to database failed.");
        } catch (SQLException ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,"SQL connection failed.");
        }
    }
    
    public void showEmployeeData()
    {
        try {
            pst = con.prepareStatement("SELECT * FROM employee ORDER BY surname");
            rs = pst.executeQuery();
            
            ResultSetMetaData rsd = rs.getMetaData();
            int t;
            
            t = rsd.getColumnCount();
            
            DefaultTableModel d = (DefaultTableModel)employeeTbl.getModel();
            d.setRowCount(0);
            
            while(rs.next())
            {
                Vector v = new Vector();
                
                for(int i=0; i<=t; i++)
                {
                    v.add(rs.getString("employeeId"));
                    v.add(rs.getString("firstName"));
                    v.add(rs.getString("surname"));
                    v.add(rs.getString("phoneNumber"));
                    v.add(rs.getString("employeeType"));
                }
                
                d.addRow(v);
            }
            
        } catch (SQLException e) {
            Logger.getLogger(Employee.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this,"Displaying employee data failed.");
        }
    }
    
    public void showDriverData()
    {
        try {
            pst = con.prepareStatement("SELECT * FROM driver ORDER BY employeeId");
            rs = pst.executeQuery();
            
            ResultSetMetaData rsd = rs.getMetaData();
            int t;
            
            t = rsd.getColumnCount();
            
            DefaultTableModel d = (DefaultTableModel)driverTbl.getModel();
            d.setRowCount(0);
            
            while(rs.next())
            {
                Vector v = new Vector();
                
                for(int i=0; i<=t; i++)
                {
                    v.add(rs.getString("employeeId"));
                    v.add(rs.getString("licenseCode"));
                    v.add(rs.getDate("licenseExpiryDate"));
                    v.add(rs.getDouble("salary"));
                }
                
                d.addRow(v);
            }
            
        } catch (SQLException e) {
            Logger.getLogger(Employee.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this,"Displaying driver data failed.");
        }
    }

    public void showPackerData()
    {
        try {
            pst = con.prepareStatement("SELECT * FROM packer ORDER BY employeeId");
            rs = pst.executeQuery();
            
            ResultSetMetaData rsd = rs.getMetaData();
            int t;
            
            t = rsd.getColumnCount();
            
            DefaultTableModel d = (DefaultTableModel)packerTbl.getModel();
            d.setRowCount(0);
            
            while(rs.next())
            {
                Vector v = new Vector();
                
                for(int i=0; i<=t; i++)
                {
                    v.add(rs.getString("employeeId"));
                    v.add(rs.getDouble("hourlyRate"));
                }
                
                d.addRow(v);
            }
            
        } catch (SQLException e) {
            Logger.getLogger(Employee.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this,"Displaying packer data failed.");
        }
    }
    
    public void clearFields()
    {     
        txfEmpID.setText("");
        txfEmpFName.setText("");
        txfEmpSName.setText("");
        txfEmpPhoneNum.setText("");
        rbDriver.setSelected(false);
        rbPacker.setSelected(false);
        
        cmbEmpLicenseCode.setSelectedIndex(-1);
        txfEmpLicenseExpDate.setText("");
        spnEmpSalary.setValue(0);
        
        spnEmpHRate.setValue(0);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgrpEmpType = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        driverTbl = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        employeeTbl = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        packerTbl = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txfEmpID = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txfEmpFName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txfEmpSName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txfEmpPhoneNum = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txfEmpLicenseExpDate = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        rbDriver = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        rbPacker = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel12 = new javax.swing.JLabel();
        cmbEmpLicenseCode = new javax.swing.JComboBox<>();
        spnEmpSalary = new javax.swing.JSpinner();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel13 = new javax.swing.JLabel();
        spnEmpHRate = new javax.swing.JSpinner();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(95, 158, 160), 6));

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(95, 158, 160), 6));

        driverTbl.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        driverTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "License Code", "License Expirary Date", "Salary"
            }
        ));
        driverTbl.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        driverTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                driverTblMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(driverTbl);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("Employee Table:");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setText("Driver Table:");

        employeeTbl.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        employeeTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "First Name", "Surname", "Phone Number", "Type"
            }
        ));
        employeeTbl.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        employeeTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                employeeTblMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(employeeTbl);

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setText("Packer Table:");

        packerTbl.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        packerTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Hourly Rate"
            }
        ));
        packerTbl.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        packerTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                packerTblMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(packerTbl);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 711, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 711, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(95, 158, 160), 6));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("ID:");

        txfEmpID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("First Name:");

        txfEmpFName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txfEmpFName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfEmpFNameActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Surname:");

        txfEmpSName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Phone No.:");

        txfEmpPhoneNum.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("License Code:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("License Expirary Date:");

        txfEmpLicenseExpDate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Salary:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("Hourly Rate:");

        jPanel6.setBackground(new java.awt.Color(204, 204, 204));
        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnAdd.setBackground(new java.awt.Color(0, 153, 153));
        btnAdd.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAdd.setText("Add");
        btnAdd.setToolTipText("");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnUpdate.setBackground(new java.awt.Color(0, 153, 153));
        btnUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.setToolTipText("");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnBack.setBackground(new java.awt.Color(0, 204, 204));
        btnBack.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBack.setText("Back");
        btnBack.setToolTipText("Return to Home");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnClear.setBackground(new java.awt.Color(0, 204, 204));
        btnClear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnClear.setText("Clear");
        btnClear.setToolTipText("Clear textfields");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        bgrpEmpType.add(rbDriver);
        rbDriver.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rbDriver.setText("Driver");
        rbDriver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbDriverActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Employee Type:");

        bgrpEmpType.add(rbPacker);
        rbPacker.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rbPacker.setText("Packer");
        rbPacker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbPackerActionPerformed(evt);
            }
        });

        jLabel12.setText("Driver:");

        cmbEmpLicenseCode.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A1", "A", "B", "C1", "C", "EB", "EC1", "EC" }));
        cmbEmpLicenseCode.setSelectedIndex(-1);

        jLabel13.setText("Packer:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator2)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jLabel6))
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txfEmpSName)
                            .addComponent(txfEmpPhoneNum)
                            .addComponent(txfEmpID)
                            .addComponent(txfEmpFName)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(cmbEmpLicenseCode, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txfEmpLicenseExpDate))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(85, 85, 85)
                                .addComponent(rbDriver)
                                .addGap(28, 28, 28)
                                .addComponent(rbPacker))
                            .addComponent(jLabel2)
                            .addComponent(jLabel12)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(39, 39, 39)
                                .addComponent(jLabel8))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spnEmpSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel13)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spnEmpHRate, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbDriver)
                    .addComponent(rbPacker))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txfEmpID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txfEmpFName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txfEmpSName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txfEmpPhoneNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addGap(8, 8, 8)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbEmpLicenseCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txfEmpLicenseExpDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spnEmpSalary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spnEmpHRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setText("Employee Entry Form");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel1)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void driverTblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_driverTblMouseClicked
        
    }//GEN-LAST:event_driverTblMouseClicked

    private void txfEmpFNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfEmpFNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfEmpFNameActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed

        getEmployeeData();
        //0147852963753 | Bobby     | Jacobs    | 0951753789  | D
        try {
            pst = con.prepareStatement("INSERT INTO employee VALUES(?,?,?,?,?)");
            pst.setString(1, empID);
            pst.setString(2, empFName);
            pst.setString(3, empSName);
            pst.setString(4, empPhoneNum);
            pst.setString(5, empType);

            int rowsAffected = pst.executeUpdate();
            System.out.println("Number of customer rows inserted: " + rowsAffected);
            
            if (rbDriver.isSelected()) {
                pst = con.prepareStatement("INSERT INTO driver VALUES(?,?,?,?)");
                pst.setString(1, empID);
                pst.setString(2, driverLCode);
                pst.setString(3, driverLExpDate);
                pst.setDouble(4, driverSalary);

                rowsAffected = pst.executeUpdate();
                System.out.println("Number of driver rows inserted: " + rowsAffected);
            }
            if (rbPacker.isSelected()) {
                pst = con.prepareStatement("INSERT INTO packer VALUES(?,?)");
                pst.setString(1, empID);
                pst.setDouble(2, packerHRate);

                rowsAffected = pst.executeUpdate();
                System.out.println("Number of packer rows inserted: " + rowsAffected);
            }

            JOptionPane.showMessageDialog(this,"Record inserted successfully.");
            showEmployeeData();
            showDriverData();
            showPackerData();
            clearFields();
        } catch (SQLException ex) {
            Logger.getLogger(Employee.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,"Employee insert failed.");
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed

        getEmployeeData();

        try {
            pst = con.prepareStatement("UPDATE employee SET firstName=?,surname=?,phoneNumber=? WHERE employeeId=?");

            pst.setString(1, empFName);
            pst.setString(2, empSName);
            pst.setString(3, empPhoneNum);
            pst.setString(4, empID);

            int rowsAffected = pst.executeUpdate();
            System.out.println("Number of employee rows updated: " + rowsAffected);
            
            if (rbDriver.isSelected()) {
                pst = con.prepareStatement("UPDATE driver SET licenseCode=?,licenseExpiryDate=?,salary=? WHERE employeeId=?");
                pst.setString(1, driverLCode);
                pst.setString(2, driverLExpDate);
                pst.setDouble(3, driverSalary);
                pst.setString(4, empID);

                rowsAffected = pst.executeUpdate();
                System.out.println("Number of driver rows updated: " + rowsAffected);
            }
            if (rbPacker.isSelected()) {
                pst = con.prepareStatement("UPDATE packer SET hourlyRate=? WHERE employeeId=?");
                pst.setDouble(1, packerHRate);
                pst.setString(2, empID);

                rowsAffected = pst.executeUpdate();
                System.out.println("Number of packer rows updated: " + rowsAffected);
            }

            JOptionPane.showMessageDialog(this,"Record/s updated successfully.");
            showEmployeeData();
            showDriverData();
            showPackerData();
            clearFields();
        } catch (SQLException ex) {
            Logger.getLogger(Employee.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,"Update/s failed.");
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        try {
            con.close();
            System.out.println("Connection to employee closed.");
        } catch (SQLException ex) {
            Logger.getLogger(Employee.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.dispose();
        Home h = new Home();
        h.setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearFields();
    }//GEN-LAST:event_btnClearActionPerformed

    private void employeeTblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_employeeTblMouseClicked
        
        clearFields();
        
        DefaultTableModel dtm = (DefaultTableModel)employeeTbl.getModel();
        int selected = employeeTbl.getSelectedRow();

        String selectedEmpType = dtm.getValueAt(selected, 4).toString();
        String selectedID = dtm.getValueAt(selected, 0).toString();
        
        if (selectedEmpType.equals("D")) {
            
            for(int i=0; i<driverTbl.getRowCount(); i++)
            {
                if (driverTbl.getModel().getValueAt(i, 0).equals(selectedID)) {
                    cmbEmpLicenseCode.setSelectedItem(driverTbl.getValueAt(i, 1).toString());
                    txfEmpLicenseExpDate.setText(driverTbl.getValueAt(i, 2).toString());
                    spnEmpSalary.setValue(Double.parseDouble(driverTbl.getValueAt(i, 3).toString()));
                }
            }
            
            txfEmpID.setEnabled(false);
            txfEmpFName.setEnabled(true);
            txfEmpSName.setEnabled(true);
            txfEmpPhoneNum.setEnabled(true);
        
            cmbEmpLicenseCode.setEnabled(true);
            txfEmpLicenseExpDate.setEnabled(true);
            spnEmpSalary.setEnabled(true);
        
            spnEmpHRate.setEnabled(false);
            
            txfEmpID.setText(dtm.getValueAt(selected, 0).toString());
            txfEmpFName.setText(dtm.getValueAt(selected, 1).toString());
            txfEmpSName.setText(dtm.getValueAt(selected, 2).toString());
            txfEmpPhoneNum.setText(dtm.getValueAt(selected, 3).toString());
            rbDriver.setSelected(true);
        }
        
        if (selectedEmpType.equals("P")) {
            
            for(int i=0; i<packerTbl.getRowCount(); i++)
            {
                if (packerTbl.getModel().getValueAt(i, 0).equals(selectedID)) {
                    spnEmpHRate.setValue(Double.parseDouble(packerTbl.getValueAt(i, 1).toString()));
                }
            }
            
            txfEmpID.setEnabled(false);
            txfEmpFName.setEnabled(true);
            txfEmpSName.setEnabled(true);
            txfEmpPhoneNum.setEnabled(true);
            rbPacker.setSelected(true);
        
            cmbEmpLicenseCode.setEnabled(false);
            txfEmpLicenseExpDate.setEnabled(false);
            spnEmpSalary.setEnabled(false);
        
            spnEmpHRate.setEnabled(true);
            
            txfEmpID.setText(dtm.getValueAt(selected, 0).toString());
            txfEmpFName.setText(dtm.getValueAt(selected, 1).toString());
            txfEmpSName.setText(dtm.getValueAt(selected, 2).toString());
            txfEmpPhoneNum.setText(dtm.getValueAt(selected, 3).toString());
            rbPacker.setSelected(true);
        } 
    }//GEN-LAST:event_employeeTblMouseClicked

    private void packerTblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_packerTblMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_packerTblMouseClicked

    private void rbDriverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbDriverActionPerformed
        
        clearFields();
        
        txfEmpID.setEnabled(true);
        txfEmpFName.setEnabled(true);
        txfEmpSName.setEnabled(true);
        txfEmpPhoneNum.setEnabled(true);
        rbDriver.setSelected(true);
        
        cmbEmpLicenseCode.setEnabled(true);
        txfEmpLicenseExpDate.setEnabled(true);
        spnEmpSalary.setEnabled(true);
        
        spnEmpHRate.setEnabled(false);
    }//GEN-LAST:event_rbDriverActionPerformed

    private void rbPackerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPackerActionPerformed
        
        clearFields();
        
        txfEmpID.setEnabled(true);
        txfEmpFName.setEnabled(true);
        txfEmpSName.setEnabled(true);
        txfEmpPhoneNum.setEnabled(true);
        rbPacker.setSelected(true);
        
        cmbEmpLicenseCode.setEnabled(false);
        txfEmpLicenseExpDate.setEnabled(false);
        spnEmpSalary.setEnabled(false);
        
        spnEmpHRate.setEnabled(true);
    }//GEN-LAST:event_rbPackerActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Employee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Employee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Employee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Employee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Employee().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgrpEmpType;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cmbEmpLicenseCode;
    private javax.swing.JTable driverTbl;
    private javax.swing.JTable employeeTbl;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable packerTbl;
    private javax.swing.JRadioButton rbDriver;
    private javax.swing.JRadioButton rbPacker;
    private javax.swing.JSpinner spnEmpHRate;
    private javax.swing.JSpinner spnEmpSalary;
    private javax.swing.JTextField txfEmpFName;
    private javax.swing.JTextField txfEmpID;
    private javax.swing.JTextField txfEmpLicenseExpDate;
    private javax.swing.JTextField txfEmpPhoneNum;
    private javax.swing.JTextField txfEmpSName;
    // End of variables declaration//GEN-END:variables
}
