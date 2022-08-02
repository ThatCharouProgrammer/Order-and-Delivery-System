package za.ac.cga;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class Order extends javax.swing.JFrame {
    
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    
    String ordID; // 
    String ordCustID;
    String ordDelID;
    String ordDate;
    Double ordTotPrice; //
    String ordPaid;
    
    String detID;
    String detProdID;
    int detQty;
    Double detTotPrice;
    String detEmpID;
    
    public void getOrderData()
    {
        ordCustID = txfCustID.getText();
        ordDelID = txfDeliveryID.getText();
        ordDate = txfDate.getText();
        //ordPaid = cmbPaid.getSelectedItem().toString();
    }
    
    public void getOrderDetailsData()
    {
        detID = txfOrdDetOrdID.getText();
        detProdID = txfOrdDetProdID.getText();
        detQty = Integer.parseInt(spnOrdDetQty.getValue().toString());
        
        // get total price
        try {
            pst = con.prepareStatement("SELECT unitPrice FROM product WHERE productId = ?");
            pst.setString(1, detProdID);
            rs = pst.executeQuery();         
            
            while(rs.next())
            {
                detTotPrice =  rs.getDouble("unitPrice");  
            }

        } catch (SQLException e) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this,"Gettig product price failed.");
        }
        
        detTotPrice = detTotPrice * detQty;
        
        // get packer id 
        
        try {
            pst = con.prepareStatement("SELECT employeeId FROM packer");
            rs = pst.executeQuery(); 
            
            ResultSetMetaData rsd = rs.getMetaData();
            int t;
            
            t = rsd.getColumnCount();
                        
            Vector v = new Vector();
            while(rs.next())
            {
                v.add(rs.getString("employeeId"));
            }
            
            int index = (int)(Math.random()*v.size());
            
            detEmpID = v.get(index).toString();
            
            

        } catch (SQLException e) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this,"Gettig packer ID failed.");
        }
    }

    /**
     * Creates new form Order
     */
    public Order() {
        initComponents();
        
        connect();
        showOrderData();
        showOrderDetailsData();
        showReport1();
        showReport2();
        showReport3();
    }
    
    private void connect()
    {
        System.out.println("Connecting to database...");
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/order_and_delivery_system","root", "N@z728001");
            
            System.out.println("Connection established.");
            
        } catch (ClassNotFoundException e) {
            Logger.getLogger(Order.class.getName()).log(Level.SEVERE,null,e);
            JOptionPane.showMessageDialog(this,"Connection to database failed.");
        } catch (SQLException ex) {
            Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,"SQL connection failed.");
        }
    }
    
    public void showOrderData()
    {
        try {
            pst = con.prepareStatement("SELECT * FROM order_tbl");
            rs = pst.executeQuery();
            
            ResultSetMetaData rsd = rs.getMetaData();
            int t;
            
            t = rsd.getColumnCount();
            
            DefaultTableModel d = (DefaultTableModel)tblOrder.getModel();
            d.setRowCount(0);
            
            while(rs.next())
            {
                Vector v = new Vector();
                
                for(int i=0; i<=t; i++)
                {
                    v.add(rs.getString("orderId"));
                    v.add(rs.getString("customerId"));
                    v.add(rs.getString("deliveryId"));
                    v.add(rs.getDate("orderDate"));
                    v.add(rs.getDouble("totalOrderPrice"));
                    v.add(rs.getString("paid"));
                }
                
                d.addRow(v);
            }
            
        } catch (SQLException e) {
            Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this,"Displaying order data failed.");
        }
    }
    
    public void showOrderDetailsData()
    {
        try {
            pst = con.prepareStatement("SELECT * FROM order_details");
            rs = pst.executeQuery();
            
            ResultSetMetaData rsd = rs.getMetaData();
            int t;
            
            t = rsd.getColumnCount();
            
            DefaultTableModel d = (DefaultTableModel)tblOrderDetails.getModel();
            d.setRowCount(0);
            
            while(rs.next())
            {
                Vector v = new Vector();
                
                for(int i=0; i<=t; i++)
                {
                    v.add(rs.getString("orderDetailsId"));
                    v.add(rs.getString("orderId"));
                    v.add(rs.getString("productId"));
                    v.add(rs.getInt("quantityRequested"));
                    v.add(rs.getDouble("totalPrice"));
                    v.add(rs.getString("employeeId"));
                }
                
                d.addRow(v);
            }
            
        } catch (SQLException e) {
            Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this,"Displaying order data failed.");
        }
    }
    
    public void showReport1()
    {
        /* 
            Customer order and delivery report
        */
        try {
            pst = con.prepareStatement("SELECT customer.customerId, firstname, surname, orderId, totalOrderPrice, paid, delivery.deliveryId, deliveryStatus FROM order_tbl\n" 
                                        + "INNER JOIN customer ON order_tbl.customerId = customer.customerId\n" 
                                        + "INNER JOIN delivery ON order_tbl.deliveryId = delivery.deliveryId;");
            
            rs = pst.executeQuery();
            
            ResultSetMetaData rsd = rs.getMetaData();
            int t;
            
            t = rsd.getColumnCount();
            
            DefaultTableModel d = (DefaultTableModel)tblReport1.getModel();
            d.setRowCount(0);
            
            while(rs.next())
            {
                Vector v = new Vector();
                
                for(int i=0; i<=t; i++)
                {
                    v.add(rs.getString("customerId"));
                    v.add(rs.getString("firstname"));
                    v.add(rs.getString("surname"));
                    v.add(rs.getString("orderId"));
                    v.add(rs.getDouble("totalOrderPrice"));
                    v.add(rs.getString("paid"));
                    v.add(rs.getString("deliveryId"));
                    v.add(rs.getString("deliveryStatus"));
                }
                
                d.addRow(v);
            }
            
        } catch (SQLException e) {
            Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this,"Displaying report data failed.");
        }
    }

    public void showReport2()
    {
        /* 
            Most ordered product for each order: 
            For each order has many order details. From the list of items ordered for each order with the highest quantity, display the
            order ID, name and unit price of the product ordered, quantity of that item requested and the total price for that order detail.
        */
        try {
            pst = con.prepareStatement("SELECT orderId, productName, unitPrice, quantityRequested, totalPrice\n" +
                                        "FROM product, order_details AS T\n" +
                                        "WHERE T.productId = product.productId AND quantityRequested = \n" +
                                        "(SELECT MAX(quantityRequested) FROM order_details WHERE orderId = T.orderId)	\n" +
                                        "GROUP BY orderId;");
            
            rs = pst.executeQuery();
            
            ResultSetMetaData rsd = rs.getMetaData();
            int t;
            
            t = rsd.getColumnCount();
            
            DefaultTableModel d = (DefaultTableModel)tblReport2.getModel();
            d.setRowCount(0);
            
            while(rs.next())
            {
                Vector v = new Vector();
                
                for(int i=0; i<=t; i++)
                {
                    v.add(rs.getString("orderId"));
                    v.add(rs.getString("productName"));
                    v.add(rs.getDouble("unitPrice"));
                    v.add(rs.getInt("quantityRequested"));
                    v.add(rs.getDouble("totalPrice"));
                }
                
                d.addRow(v);
            }
            
        } catch (SQLException e) {
            Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this,"Displaying report data failed.");
        }
    }
    
    public void showReport3()
    {
        int completedOrders = 0;
        
        /* 
            Number of completed orders
        */
        try {
            pst = con.prepareStatement("SELECT COUNT(customer.customerId) AS COMPLETED_ORDERS FROM customer\n" +
                                        "INNER JOIN order_tbl ON customer.customerId = order_tbl.customerId AND paid = \"YES\"\n" +
                                        "INNER JOIN delivery ON order_tbl.deliveryId = delivery.deliveryId AND deliveryStatus = \"DELIVERED\";");
            
            rs = pst.executeQuery();
            
            ResultSetMetaData rsd = rs.getMetaData();
            int t;
            
            t = rsd.getColumnCount();
                       
            while(rs.next())
            {
                //Vector v = new Vector();
                
                for(int i=0; i<=t; i++)
                {
                    //v.add(rs.getString("COMPLETE_ORDERS"));
                    completedOrders = rs.getInt("COMPLETED_ORDERS");
                }

            }
            
        } catch (SQLException e) {
            Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this,"Getting completed orders report data failed.");
        }
        
        lblCompletedOrders.setText("Completed Orders: " + completedOrders + "");
    }
    
    public void clearOrderFields()
    {
        txfCustID.setText("");
        txfDate.setText("");
        txfDeliveryID.setText("");
        cmbPaid.setSelectedIndex(-1);
        lblOrderID.setText("Order ID:");
        lblOrderTotal.setText("Order Total:");
    }
    
    public void clearOrdDetFields()
    {
        txfOrdDetOrdID.setText("");
        txfOrdDetProdID.setText("");
        spnOrdDetQty.setValue(0);
    }
    
    public void updateProdQtyTbl(String prodID, int qty)
    {
       try {
            pst = con.prepareStatement("UPDATE product SET quantityInStock=(quantityInStock-?) WHERE productId=?");

            pst.setInt(1, qty);
            pst.setString(2, prodID);

            int rowsAffected = pst.executeUpdate();
            System.out.println("Number of product rows successfully updated: " + rowsAffected);
            
            showOrderData();
            showOrderDetailsData();
            showReport1();
            showReport2();
            clearOrdDetFields();
            } catch (SQLException ex) {
                Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,"Update/s failed.");
            } 
    }
    
    public void updateOrderTbl(Double orderDetailTotal, String orderID)
    {
        try {
            pst = con.prepareStatement("UPDATE order_tbl SET totalOrderPrice=(totalOrderPrice+?) WHERE orderId=?");

            pst.setDouble(1, orderDetailTotal);
            pst.setString(2, orderID);

            int rowsAffected = pst.executeUpdate();
            System.out.println("Number of order rows successfully updated: " + rowsAffected);
            
            showOrderData();
            showOrderDetailsData();
            showReport1();
            showReport2();
            clearOrdDetFields();
            } catch (SQLException ex) {
                Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,"Update/s failed.");
            } 
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        txfCustID = new javax.swing.JTextField();
        btnOrdAdd = new javax.swing.JButton();
        btnOrdUpdate = new javax.swing.JButton();
        txfDeliveryID = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txfDate = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        cmbPaid = new javax.swing.JComboBox<>();
        btnOrdClear = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        lblOrderTotal = new javax.swing.JLabel();
        lblOrderID = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblOrder = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txfOrdDetOrdID = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txfOrdDetProdID = new javax.swing.JTextField();
        spnOrdDetQty = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        btnOrdDetAdd = new javax.swing.JButton();
        btnOrdDetClear = new javax.swing.JButton();
        lblOrdDetEmpID = new javax.swing.JLabel();
        lblOrdDetID = new javax.swing.JLabel();
        lblOrdDetTot = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblOrderDetails = new javax.swing.JTable();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblReport1 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblReport2 = new javax.swing.JTable();
        btnBack = new javax.swing.JButton();
        lblCompletedOrders = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(95, 158, 160), 6));

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(95, 158, 160), 6));

        btnOrdAdd.setBackground(new java.awt.Color(0, 153, 153));
        btnOrdAdd.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnOrdAdd.setText("Add");
        btnOrdAdd.setToolTipText("");
        btnOrdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrdAddActionPerformed(evt);
            }
        });

        btnOrdUpdate.setBackground(new java.awt.Color(0, 153, 153));
        btnOrdUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnOrdUpdate.setText("Update");
        btnOrdUpdate.setToolTipText("Select order from table to update");
        btnOrdUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrdUpdateActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Delivery ID:");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Date:");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setText("Customer ID:");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setText("Paid:");

        cmbPaid.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NO", "YES" }));
        cmbPaid.setSelectedIndex(-1);

        btnOrdClear.setBackground(new java.awt.Color(0, 204, 204));
        btnOrdClear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnOrdClear.setText("Clear");
        btnOrdClear.setToolTipText("");
        btnOrdClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrdClearActionPerformed(evt);
            }
        });

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        lblOrderTotal.setText("Order Total:");

        lblOrderID.setText("Order ID:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txfCustID, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(txfDeliveryID, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txfDate)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbPaid, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(btnOrdAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(btnOrdUpdate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnOrdClear, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblOrderTotal)
                            .addComponent(lblOrderID))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txfCustID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txfDeliveryID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txfDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOrdAdd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(cmbPaid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnOrdUpdate)
                            .addComponent(btnOrdClear))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblOrderID)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblOrderTotal)))
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Order Entry Form");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        tblOrder.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        tblOrder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Customer ID", "Delivery ID", "Order Date", "Total", "Paid"
            }
        ));
        tblOrder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblOrderMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblOrder);

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(95, 158, 160), 6));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Order ID:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Product ID:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Quantity Required:");

        btnOrdDetAdd.setBackground(new java.awt.Color(0, 153, 153));
        btnOrdDetAdd.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnOrdDetAdd.setText("Add");
        btnOrdDetAdd.setToolTipText("");
        btnOrdDetAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrdDetAddActionPerformed(evt);
            }
        });

        btnOrdDetClear.setBackground(new java.awt.Color(0, 204, 204));
        btnOrdDetClear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnOrdDetClear.setText("Clear");
        btnOrdDetClear.setToolTipText("");
        btnOrdDetClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrdDetClearActionPerformed(evt);
            }
        });

        lblOrdDetEmpID.setText("Employee ID:");

        lblOrdDetID.setText("Order Detail ID:");

        lblOrdDetTot.setText("Total:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblOrdDetID, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblOrdDetEmpID, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(jLabel9)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel10))
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(txfOrdDetOrdID, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txfOrdDetProdID, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel4))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(spnOrdDetQty, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOrdDetAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(lblOrdDetTot)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnOrdDetClear)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txfOrdDetOrdID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txfOrdDetProdID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnOrdDetQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOrdDetAdd))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(btnOrdDetClear))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblOrdDetID)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblOrdDetEmpID)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblOrdDetTot)))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel16.setText("Order Details Entry Form");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        tblOrderDetails.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        tblOrderDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Order ID", "Product ID", "Quantity", "Total", "Employee ID"
            }
        ));
        tblOrderDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblOrderDetailsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblOrderDetails);

        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(95, 158, 160), 4));

        tblReport1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Customer ID", "First Name", "Surname", "Order ID", "Total Order Price", "Paid", "Delivery ID", "Delivery Status"
            }
        ));
        jScrollPane3.setViewportView(tblReport1);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1190, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Report 1 - Customer Order & Delivery Overview", jPanel1);

        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(95, 158, 160), 4));

        tblReport2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Order ID", "Product", "Unit Price", "Quantity", "Total"
            }
        ));
        jScrollPane4.setViewportView(tblReport2);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1196, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Report 2 - Orders with the highest quantity", jPanel6);

        btnBack.setBackground(new java.awt.Color(0, 204, 204));
        btnBack.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBack.setText("Back");
        btnBack.setToolTipText("");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        lblCompletedOrders.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCompletedOrders.setText("Completed Orders : ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 830, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane2)))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblCompletedOrders)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBack)
                    .addComponent(lblCompletedOrders))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnOrdDetAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrdDetAddActionPerformed
        getOrderDetailsData();

        try {
            pst = con.prepareStatement("INSERT INTO order_details (orderId, productId, quantityRequested, totalPrice, employeeId) VALUES(?,?,?,?,?)");
            pst.setString(1, detID);
            pst.setString(2, detProdID);
            pst.setInt(3, detQty);
            pst.setDouble(4, detTotPrice);
            pst.setString(5, detEmpID);

            int rowsAffected = pst.executeUpdate();
            System.out.println("Number of order detail rows inserted: " + rowsAffected);

            JOptionPane.showMessageDialog(this,"Record inserted successfully.");
            showOrderDetailsData();
            showReport1();
            showReport2();
            clearOrdDetFields();
            updateProdQtyTbl(detProdID, detQty);
            updateOrderTbl(detTotPrice, detID);
        } catch (SQLException ex) {
            Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,"Order detail insert failed.");
        }
    }//GEN-LAST:event_btnOrdDetAddActionPerformed

    private void btnOrdDetClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrdDetClearActionPerformed
        clearOrdDetFields();
    }//GEN-LAST:event_btnOrdDetClearActionPerformed

    private void btnOrdClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrdClearActionPerformed
        clearOrderFields();
        btnOrdUpdate.setEnabled(false);
    }//GEN-LAST:event_btnOrdClearActionPerformed

    private void btnOrdUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrdUpdateActionPerformed

        getOrderData();

        try {
            pst = con.prepareStatement("UPDATE order_tbl SET paid=? WHERE orderId=?");

            pst.setString(1, "YES");
            pst.setString(2, ordID);

            int rowsAffected = pst.executeUpdate();
            System.out.println("Number of order rows updated: " + rowsAffected);

            JOptionPane.showMessageDialog(this,rowsAffected + " Record/s updated successfully.");
            showOrderData();
            showReport1();
            showReport2();
            clearOrderFields();
        } catch (SQLException ex) {
            Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,"Update failed.");
        }
        
        btnOrdUpdate.setEnabled(false);
    }//GEN-LAST:event_btnOrdUpdateActionPerformed

    private void btnOrdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrdAddActionPerformed
        
        getOrderData();

        try {
            pst = con.prepareStatement("INSERT INTO order_tbl (customerId, deliveryId, orderDate, totalOrderPrice, paid) VALUES(?,?,?,?,?)");
            pst.setString(1, ordCustID);
            pst.setString(2, ordDelID);
            pst.setString(3, ordDate);
            pst.setDouble(4, 0.00);
            pst.setString(5, "NO");

            int rowsAffected = pst.executeUpdate();
            System.out.println("Number of order rows inserted: " + rowsAffected);

            JOptionPane.showMessageDialog(this,"Record inserted successfully.");
            showOrderData();
            showReport1();
            showReport2();
            clearOrderFields();
        } catch (SQLException ex) {
            Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,"Order insert failed.");
        }
    }//GEN-LAST:event_btnOrdAddActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        try {
            con.close();
            System.out.println("Connection closed.");
        } catch (SQLException ex) {
            Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.dispose();
        Home h = new Home();
        h.setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    private void tblOrderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOrderMouseClicked
        clearOrderFields();
        
        DefaultTableModel dtm = (DefaultTableModel)tblOrder.getModel();
        int selected = tblOrder.getSelectedRow();
        
        ordID = dtm.getValueAt(selected, 0).toString();
        lblOrderID.setText("Order ID: " + ordID);
        txfCustID.setText(dtm.getValueAt(selected, 1).toString());
        txfDeliveryID.setText(dtm.getValueAt(selected, 2).toString());
        txfDate.setText(dtm.getValueAt(selected, 3).toString());
        lblOrderTotal.setText("Order Total: R" + dtm.getValueAt(selected, 4).toString());
        cmbPaid.setSelectedItem(dtm.getValueAt(selected, 5));
        
    }//GEN-LAST:event_tblOrderMouseClicked

    private void tblOrderDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOrderDetailsMouseClicked
        clearOrderFields();
        
        DefaultTableModel dtm = (DefaultTableModel)tblOrderDetails.getModel();
        int selected = tblOrderDetails.getSelectedRow();
        
        lblOrdDetID.setText("Order Detail ID: " + dtm.getValueAt(selected, 0).toString());
        txfOrdDetOrdID.setText(dtm.getValueAt(selected, 1).toString());
        txfOrdDetProdID.setText(dtm.getValueAt(selected, 2).toString());
        spnOrdDetQty.setValue(Integer.parseInt(dtm.getValueAt(selected, 3).toString()));
        lblOrdDetTot.setText("Total: R" + dtm.getValueAt(selected, 4).toString());
        lblOrdDetEmpID.setText("Employee ID: " + dtm.getValueAt(selected, 5).toString());
    }//GEN-LAST:event_tblOrderDetailsMouseClicked

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
            java.util.logging.Logger.getLogger(Order.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Order.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Order.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Order.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Order().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnOrdAdd;
    private javax.swing.JButton btnOrdClear;
    private javax.swing.JButton btnOrdDetAdd;
    private javax.swing.JButton btnOrdDetClear;
    private javax.swing.JButton btnOrdUpdate;
    private javax.swing.JComboBox<String> cmbPaid;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblCompletedOrders;
    private javax.swing.JLabel lblOrdDetEmpID;
    private javax.swing.JLabel lblOrdDetID;
    private javax.swing.JLabel lblOrdDetTot;
    private javax.swing.JLabel lblOrderID;
    private javax.swing.JLabel lblOrderTotal;
    private javax.swing.JSpinner spnOrdDetQty;
    private javax.swing.JTable tblOrder;
    private javax.swing.JTable tblOrderDetails;
    private javax.swing.JTable tblReport1;
    private javax.swing.JTable tblReport2;
    private javax.swing.JTextField txfCustID;
    private javax.swing.JTextField txfDate;
    private javax.swing.JTextField txfDeliveryID;
    private javax.swing.JTextField txfOrdDetOrdID;
    private javax.swing.JTextField txfOrdDetProdID;
    // End of variables declaration//GEN-END:variables
}
