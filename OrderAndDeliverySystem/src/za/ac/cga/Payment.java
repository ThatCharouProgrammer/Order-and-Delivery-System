package za.ac.cga;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class Payment extends javax.swing.JFrame {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    
    // payment data
    int payID;
    
    int payOrdId; 
    String payCustID;
    String payDate;
    Double payAmt;
    String payType;
    
    // credit card data
    String crAccNo;
    String crExpDate;
    String crSecCode;
      
    // cash data
    String cTransNo;
    
    public void getPaymentData()
    {
        payOrdId = Integer.parseInt(txfPayID.getText());
        payCustID = txfPayCustID.getText();
        payDate = txfPayDate.getText();
        payAmt = Double.parseDouble(spnPayAmt.getValue().toString());
        
        if (rbCreditCard.isSelected()) {
            payType = "CR";
            
            crAccNo = txfAccNo.getText();
            crExpDate = txfExpDate.getText();
            crSecCode = txfSecCode.getText();
        }
        
        if (rbCash.isSelected()) {
            payType = "C";
            
            cTransNo = txfTransNo.getText();
        } 
    }
    
    /**
     * Creates new form Payment
     */
    public Payment() {
        initComponents();
        connect();
        showPaymentData();
        showCreditCardData();
        showCashData();
        
        // set enabled to false for components
        txfPayID.setEnabled(false);
        txfPayCustID.setEnabled(false);
        txfPayDate.setEnabled(false);
        spnPayAmt.setEnabled(false);
        
        txfAccNo.setEnabled(false);
        txfExpDate.setEnabled(false);
        txfSecCode.setEnabled(false);
        
        txfTransNo.setEnabled(false);
        
        btnUpdate1.setEnabled(false);
    }
    
    private void connect()
    {
        System.out.println("Connecting to payment, credit card, cash database...");
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/order_and_delivery_system","root", "N@z728001");
            
            System.out.println("Connection to payment, credit card, cash tables established.");
            
            //con.close();
            
        } catch (ClassNotFoundException e) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE,null,e);
            JOptionPane.showMessageDialog(this,"Connection to database failed.");
        } catch (SQLException ex) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,"SQL connection failed.");
        }
    }
    
    public Double calcPaymentTotal(int ordID)
    {
        Double paymentTotal = 0.00;
        
        try {
            pst = con.prepareStatement("SELECT SUM(paymentAmount) AS paymentTotal FROM payment WHERE orderId = ?");
            pst.setInt(1, ordID);
            rs = pst.executeQuery();         

            while(rs.next())
            {
                paymentTotal =  rs.getDouble("paymentTotal");  
            }
            //System.out.println(paymentTotal);

        } catch (SQLException e) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this,"Calculating payment total failed.");
        }
        
        return paymentTotal;
    }
    
    public void orderPaid(Double paymentTotal, int ordID)
    {
        Double ordTotal = 0.00;
        
        try {
            pst = con.prepareStatement("SELECT totalOrderPrice FROM order_tbl WHERE orderId = ?");
            pst.setInt(1, ordID);
            rs = pst.executeQuery();         

            while(rs.next())
            {
                ordTotal =  rs.getDouble("totalOrderPrice");  
            }

        } catch (SQLException e) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this,"Getting order total failed.");
        }
        
        if (paymentTotal >= ordTotal) 
        {
            try {
            pst = con.prepareStatement("UPDATE order_tbl SET paid=? WHERE orderId=?");

            pst.setString(1, "YES");
            pst.setInt(2, ordID);

            int rowsAffected = pst.executeUpdate();
            System.out.println("Number of order rows successfully updated: " + rowsAffected);
            
            //JOptionPane.showMessageDialog(this,"Order details updated successfully.");
            showPaymentData();
            showCreditCardData();
            showCashData();
            clearFields();
            } catch (SQLException ex) {
                Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,"Update/s failed.");
            }
        }
    }
    
    
       
    public void showPaymentData()
    {
        try {
            pst = con.prepareStatement("SELECT * FROM payment");
            rs = pst.executeQuery();
            
            ResultSetMetaData rsd = rs.getMetaData();
            int t;
            
            t = rsd.getColumnCount();
            
            DefaultTableModel d = (DefaultTableModel)paymentTbl.getModel();
            d.setRowCount(0);
            
            while(rs.next())
            {
                Vector v = new Vector();
                
                for(int i=0; i<=t; i++)
                {
                    v.add(rs.getString("paymentId"));
                    v.add(rs.getString("customerId"));
                    v.add(rs.getString("orderId"));
                    v.add(rs.getDate("paymentDate"));
                    v.add(rs.getDouble("paymentAmount"));
                    v.add(rs.getString("paymentType"));
                }
                
                d.addRow(v);
            }
            
        } catch (SQLException e) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this,"Displaying payment data failed.");
        }
    }
    
    public void showCreditCardData()
    {
        try {
            pst = con.prepareStatement("SELECT * FROM credit_card");
            rs = pst.executeQuery();
            
            ResultSetMetaData rsd = rs.getMetaData();
            int t;
            
            t = rsd.getColumnCount();
            
            DefaultTableModel d = (DefaultTableModel)creditCardTbl.getModel();
            d.setRowCount(0);
            
            while(rs.next())
            {
                Vector v = new Vector();
                
                for(int i=0; i<=t; i++)
                {
                    v.add(rs.getString("accountNumber"));
                    v.add(rs.getString("paymentId"));
                    v.add(rs.getDate("expiryDate"));
                    v.add(rs.getString("securityCode"));
                }
                
                d.addRow(v);
            }
            
        } catch (SQLException e) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this,"Displaying credit card data failed.");
        }
    }
    
    public void showCashData()
    {
        try {
            pst = con.prepareStatement("SELECT * FROM cash ORDER BY cashTransactionNumber");
            rs = pst.executeQuery();
            
            ResultSetMetaData rsd = rs.getMetaData();
            int t;
            
            t = rsd.getColumnCount();
            
            DefaultTableModel d = (DefaultTableModel)cashTbl.getModel();
            d.setRowCount(0);
            
            while(rs.next())
            {
                Vector v = new Vector();
                
                for(int i=0; i<=t; i++)
                {
                    v.add(rs.getString("cashTransactionNumber"));
                    v.add(rs.getString("paymentId"));
                }
                
                d.addRow(v);
            }
            
        } catch (SQLException e) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this,"Displaying credit card data failed.");
        }
    }
    
    public void clearFields()
    {     
        txfPayID.setText("");
        txfPayCustID.setText("");
        txfPayDate.setText("");
        spnPayAmt.setValue(0);
        rbCash.setSelected(false);
        rbCreditCard.setSelected(false);
        
        txfAccNo.setText("");
        txfExpDate.setText("");
        txfSecCode.setText("");
        
        txfTransNo.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgPayment = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        creditCardTbl = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        paymentTbl = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        cashTbl = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txfPayID = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txfPayCustID = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txfPayDate = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txfSecCode = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        rbCreditCard = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        rbCash = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel12 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel13 = new javax.swing.JLabel();
        spnPayAmt = new javax.swing.JSpinner();
        txfExpDate = new javax.swing.JTextField();
        txfAccNo = new javax.swing.JTextField();
        txfTransNo = new javax.swing.JTextField();
        btnUpdate1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(95, 158, 160), 6));

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(95, 158, 160), 6));

        creditCardTbl.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        creditCardTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Account Number", "Payment ID", "Expiry Date", "Security Code"
            }
        ));
        creditCardTbl.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        creditCardTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                creditCardTblMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(creditCardTbl);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("Payment Table:");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setText("Credit Card Table:");

        paymentTbl.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        paymentTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Customer ID", "Order ID", "Date", "Amount", "Type"
            }
        ));
        paymentTbl.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        paymentTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                paymentTblMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(paymentTbl);

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setText("Cash Table:");

        cashTbl.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        cashTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Transaction Number", "Payment ID"
            }
        ));
        cashTbl.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cashTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cashTblMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(cashTbl);

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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(95, 158, 160), 6));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Order ID:");

        txfPayID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Customer ID:");

        txfPayCustID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txfPayCustID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfPayCustIDActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Payment Date:");

        txfPayDate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Payment Amount:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Account No. :");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Security Code :");

        txfSecCode.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Expirary Date:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("Transaction Number:");

        jPanel6.setBackground(new java.awt.Color(204, 204, 204));
        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnAdd.setBackground(new java.awt.Color(0, 153, 153));
        btnAdd.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAdd.setText("Add New Payment");
        btnAdd.setToolTipText("");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
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
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnBack, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(btnClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bgPayment.add(rbCreditCard);
        rbCreditCard.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rbCreditCard.setText("Credit Card");
        rbCreditCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCreditCardActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Payment Type:");

        bgPayment.add(rbCash);
        rbCash.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rbCash.setText("Cash");
        rbCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCashActionPerformed(evt);
            }
        });

        jLabel12.setText("Credit Card:");

        jLabel13.setText("Cash:");

        txfExpDate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txfAccNo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txfTransNo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        btnUpdate1.setBackground(new java.awt.Color(0, 153, 153));
        btnUpdate1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUpdate1.setText("Update Credit Card Detials");
        btnUpdate1.setToolTipText("");
        btnUpdate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdate1ActionPerformed(evt);
            }
        });

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
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txfPayDate)
                            .addComponent(txfPayCustID)
                            .addComponent(spnPayAmt)
                            .addComponent(txfPayID)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txfAccNo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(txfSecCode, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txfExpDate))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnUpdate1))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(85, 85, 85)
                                .addComponent(rbCreditCard)
                                .addGap(28, 28, 28)
                                .addComponent(rbCash))
                            .addComponent(jLabel2)
                            .addComponent(jLabel12)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txfTransNo, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel13))
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
                    .addComponent(rbCreditCard)
                    .addComponent(rbCash))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txfPayCustID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txfPayID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txfPayDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(spnPayAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(txfSecCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txfAccNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txfExpDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUpdate1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txfTransNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setText("Payment Entry Form");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(32, 32, 32))
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

    private void creditCardTblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_creditCardTblMouseClicked
        
    }//GEN-LAST:event_creditCardTblMouseClicked

    private void paymentTblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paymentTblMouseClicked

        clearFields();
        
        DefaultTableModel dtm = (DefaultTableModel)paymentTbl.getModel();
        int selected = paymentTbl.getSelectedRow();

        String selectedPayType = dtm.getValueAt(selected, 5).toString();
        String selectedPaymentID = dtm.getValueAt(selected, 0).toString();
        
        txfPayCustID.setText(dtm.getValueAt(selected, 1).toString());
        txfPayID.setText(dtm.getValueAt(selected, 2).toString());
        txfPayDate.setText(dtm.getValueAt(selected, 3).toString());
        spnPayAmt.setValue(Double.parseDouble(dtm.getValueAt(selected, 4).toString()));
        
        if (selectedPayType.equals("CR")) {
            
            for(int i=0; i<creditCardTbl.getRowCount(); i++)
            {
                if (creditCardTbl.getModel().getValueAt(i, 1).equals(selectedPaymentID)) {
                    txfAccNo.setText(creditCardTbl.getValueAt(i, 0).toString());
                    txfExpDate.setText(creditCardTbl.getValueAt(i, 2).toString());
                    txfSecCode.setText(creditCardTbl.getValueAt(i, 3).toString());
                }
            }
            
            txfPayID.setEnabled(false);
            txfPayCustID.setEnabled(false);
            txfPayDate.setEnabled(false);
            spnPayAmt.setEnabled(false);
        
            txfAccNo.setEnabled(true);
            txfExpDate.setEnabled(true);
            txfSecCode.setEnabled(true);
        
            txfTransNo.setEnabled(false);
            
            rbCreditCard.setSelected(true);
            
            btnUpdate1.setEnabled(true);
        }
        
        if (selectedPayType.equals("C")) {
            
            for(int i=0; i<cashTbl.getRowCount(); i++)
            {
                if (cashTbl.getModel().getValueAt(i, 1).equals(selectedPaymentID)) {
                    txfTransNo.setText(cashTbl.getValueAt(i, 0).toString());
                }
            }
            
            txfPayID.setEnabled(false);
            txfPayCustID.setEnabled(false);
            txfPayDate.setEnabled(false);
            spnPayAmt.setEnabled(false);
        
            txfAccNo.setEnabled(false);
            txfExpDate.setEnabled(false);
            txfSecCode.setEnabled(false);
        
            txfTransNo.setEnabled(true);
            
            rbCash.setSelected(true);
            
            btnUpdate1.setEnabled(false);
        } 
    }//GEN-LAST:event_paymentTblMouseClicked

    private void cashTblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cashTblMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cashTblMouseClicked

    private void txfPayCustIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfPayCustIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfPayCustIDActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        
        getPaymentData();
        
        // get max payID for inserting in cash or credit_card
        DefaultTableModel dtm = (DefaultTableModel)paymentTbl.getModel();
        payID = paymentTbl.getRowCount() + 1;
        
        txfAccNo.setEnabled(false);

        try {
            pst = con.prepareStatement("INSERT INTO payment (customerId, orderId, paymentDate, paymentAmount,paymentType) VALUES(?,?,?,?,?)");
            pst.setString(1, payCustID);
            pst.setInt(2, payOrdId);
            pst.setString(3, payDate);
            pst.setDouble(4, payAmt);
            pst.setString(5, payType);

            int rowsAffected = pst.executeUpdate();
            System.out.println("Number of payment rows inserted: " + rowsAffected);

            if (rbCreditCard.isSelected()) {
                pst = con.prepareStatement("INSERT INTO credit_card VALUES(?,?,?,?)");
                pst.setString(1, crAccNo);
                pst.setInt(2, payID);
                pst.setString(3, crExpDate);
                pst.setString(4, crSecCode);

                rowsAffected = pst.executeUpdate();
                System.out.println("Number of credit_card rows inserted: " + rowsAffected);
            }
            if (rbCash.isSelected()) {
                pst = con.prepareStatement("INSERT INTO cash VALUES(?,?)");
                pst.setString(1, cTransNo);
                pst.setInt(2, payID);

                rowsAffected = pst.executeUpdate();
                System.out.println("Number of cash rows inserted: " + rowsAffected);
            }

            JOptionPane.showMessageDialog(this,"Record inserted successfully.");
            showPaymentData();
            showCreditCardData();
            showCashData();
            clearFields();
            orderPaid(calcPaymentTotal(payOrdId), payOrdId);
        } catch (SQLException ex) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,"Payment insert failed.");
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        try {
            con.close();
            System.out.println("Connection to payment closed.");
        } catch (SQLException ex) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.dispose();
        Home h = new Home();
        h.setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearFields();
    }//GEN-LAST:event_btnClearActionPerformed

    private void rbCreditCardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCreditCardActionPerformed

        clearFields();

        txfPayID.setEnabled(true);
        txfPayCustID.setEnabled(true);
        txfPayDate.setEnabled(true);
        spnPayAmt.setEnabled(true);
        
        txfAccNo.setEnabled(true);
        txfExpDate.setEnabled(true);
        txfSecCode.setEnabled(true);
        
        txfTransNo.setEnabled(false);
        
        btnUpdate1.setEnabled(false);
    }//GEN-LAST:event_rbCreditCardActionPerformed

    private void rbCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCashActionPerformed

        clearFields();

        txfPayID.setEnabled(true);
        txfPayCustID.setEnabled(true);
        txfPayDate.setEnabled(true);
        spnPayAmt.setEnabled(true);
        
        txfAccNo.setEnabled(false);
        txfExpDate.setEnabled(false);
        txfSecCode.setEnabled(false);
        
        txfTransNo.setEnabled(true);
        
        btnUpdate1.setEnabled(false);
    }//GEN-LAST:event_rbCashActionPerformed

    private void btnUpdate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdate1ActionPerformed
        getPaymentData();

        try {
            pst = con.prepareStatement("UPDATE credit_card SET expiryDate=?, securityCode=? WHERE accountNumber=?");

            pst.setString(1, crExpDate);
            pst.setString(2, crSecCode);
            pst.setString(3, crAccNo);

            int rowsAffected = pst.executeUpdate();
            System.out.println("Number of credit card rows updated: " + rowsAffected);
            
            JOptionPane.showMessageDialog(this,"Card details updated successfully.");
            showPaymentData();
            showCreditCardData();
            showCashData();
            clearFields();
        } catch (SQLException ex) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,"Update/s failed.");
        }
    }//GEN-LAST:event_btnUpdate1ActionPerformed

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
            java.util.logging.Logger.getLogger(Payment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Payment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Payment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Payment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Payment().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgPayment;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnUpdate1;
    private javax.swing.JTable cashTbl;
    private javax.swing.JTable creditCardTbl;
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
    private javax.swing.JTable paymentTbl;
    private javax.swing.JRadioButton rbCash;
    private javax.swing.JRadioButton rbCreditCard;
    private javax.swing.JSpinner spnPayAmt;
    private javax.swing.JTextField txfAccNo;
    private javax.swing.JTextField txfExpDate;
    private javax.swing.JTextField txfPayCustID;
    private javax.swing.JTextField txfPayDate;
    private javax.swing.JTextField txfPayID;
    private javax.swing.JTextField txfSecCode;
    private javax.swing.JTextField txfTransNo;
    // End of variables declaration//GEN-END:variables
}
