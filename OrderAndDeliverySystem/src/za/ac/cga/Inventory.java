package za.ac.cga;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class Inventory extends javax.swing.JFrame {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    
    // category data
    String catID;
    String catName;
    String catDescription;
    
    // product data
    String prodID;
    String prodName;
    double prodPrice;
    String prodColor;
    String prodSize;
    int prodQty;
    String prodCategory;
    
    /**
     * Creates new form Category
     */
    public Inventory() {
        initComponents();
        connect();
        showCategoryData();
        showProductData();
        showReport();
    }
          
    private void connect()
    {
        System.out.println("Connecting to product, category database...");
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/order_and_delivery_system","root", "N@z728001");
            
            System.out.println("Connection to product, catgeory established.");
            
        } catch (ClassNotFoundException e) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE,null,e);
            JOptionPane.showMessageDialog(this,"Connection to database failed.");
        } catch (SQLException ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,"SQL connection failed.");
        }
    }
    
    public void showCategoryData()
    {
        try {
            pst = con.prepareStatement("SELECT * FROM category ORDER BY categoryId");
            rs = pst.executeQuery();
            
            ResultSetMetaData rsd = rs.getMetaData();
            int t;
            
            t = rsd.getColumnCount();
            
            DefaultTableModel d = (DefaultTableModel)categoryTbl.getModel();
            d.setRowCount(0);
            
            while(rs.next())
            {
                Vector v = new Vector();
                
                for(int i=0; i<=t; i++)
                {
                    v.add(rs.getString("categoryId"));
                    v.add(rs.getString("name"));
                    v.add(rs.getString("description"));
                }
                
                d.addRow(v);
            }
            
        } catch (SQLException e) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this,"Displaying category data failed.");
        }
    }
    
    public void showProductData()
    {
        try {
            pst = con.prepareStatement("SELECT * FROM product");
            rs = pst.executeQuery();
            
            ResultSetMetaData rsd = rs.getMetaData();
            int t;
            
            t = rsd.getColumnCount();
            
            DefaultTableModel d = (DefaultTableModel)productTbl.getModel();
            d.setRowCount(0);
            
            while(rs.next())
            {
                Vector v = new Vector();
                
                for(int i=0; i<=t; i++)
                {
                    v.add(rs.getString("productId"));
                    v.add(rs.getString("productName"));
                    v.add(rs.getDouble("unitPrice"));
                    v.add(rs.getString("color"));
                    v.add(rs.getString("product_size"));
                    v.add(rs.getInt("quantityInStock"));
                    v.add(rs.getString("categoryId"));
                }
                
                d.addRow(v);
            }
            
        } catch (SQLException e) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this,"Displaying product data failed.");
        }
    }
    
    public void showReport()
    {
        /*
        Most popular products
        */
        try {
            pst = con.prepareStatement("SELECT productName, unitPrice, categoryId, AVG(rating) AS Rating FROM product, product_review \n" +
                                        "WHERE product.productId = product_review.productId \n" +
                                        "GROUP BY productName ORDER BY Rating DESC LIMIT 10;");
            rs = pst.executeQuery();
            
            ResultSetMetaData rsd = rs.getMetaData();
            int t;
            
            t = rsd.getColumnCount();
            
            DefaultTableModel d = (DefaultTableModel)tblReport.getModel();
            d.setRowCount(0);
            
            while(rs.next())
            {
                Vector v = new Vector();
                
                for(int i=0; i<=t; i++)
                {
                    v.add(rs.getString("productName"));
                    v.add(rs.getDouble("unitPrice"));
                    v.add(rs.getString("categoryId"));
                    v.add(rs.getDouble("Rating"));
                }
                
                d.addRow(v);
            }
            
        } catch (SQLException e) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this,"Displaying report data failed.");
        }
    }
    
    public void clearCategoryFields()
    {
        txfFName.setText("");
        txfID.setText("");
        taDescription.setText("");
    }
    
    public void clearProductFields()
    {
        txfProdCatID.setText("");
        txfProdID.setText("");
        txfProdName.setText("");
        txfProdColor.setText("");
        spnProdPrice.setValue(0);
        spnProdQty.setValue(0);
        cmbProdSize.setSelectedIndex(-1);
    }
    
    public void getCategoryData()
    {
        catID = txfID.getText();
        catName = txfFName.getText();
        catDescription = taDescription.getText();
    }
    
    public void getProductData()
    {
        prodID = txfProdID.getText();
        prodName = txfProdName.getText();
        prodPrice = Double.parseDouble(spnProdPrice.getValue().toString());
        prodColor = txfProdColor.getText();
        prodSize = cmbProdSize.getSelectedItem().toString();
        prodQty = Integer.parseInt(spnProdQty.getValue().toString());
        prodCategory = txfProdCatID.getText();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        categoryTbl = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        productTbl = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        btnCategoryAdd = new javax.swing.JButton();
        btnCategoryUpdate = new javax.swing.JButton();
        btnCategoryClear = new javax.swing.JButton();
        txfID = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txfFName = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        taDescription = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        btnProductAdd = new javax.swing.JButton();
        btnProductUpdate = new javax.swing.JButton();
        btnProductClear = new javax.swing.JButton();
        txfProdID = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txfProdName = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        spnProdPrice = new javax.swing.JSpinner();
        jLabel10 = new javax.swing.JLabel();
        txfProdCatID = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        spnProdQty = new javax.swing.JSpinner();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txfProdColor = new javax.swing.JTextField();
        cmbProdSize = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblReport = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(95, 158, 160), 6));

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(95, 158, 160), 6));

        categoryTbl.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        categoryTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Description"
            }
        ));
        categoryTbl.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        categoryTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                categoryTblMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(categoryTbl);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Product Table");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Category Table");

        productTbl.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        productTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Price", "Color", "Size", "Quanitity", "Category ID"
            }
        ));
        productTbl.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        productTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                productTblMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(productTbl);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(95, 158, 160), 6));

        jPanel6.setBackground(new java.awt.Color(204, 204, 204));
        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnCategoryAdd.setBackground(new java.awt.Color(0, 153, 153));
        btnCategoryAdd.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCategoryAdd.setText("Add");
        btnCategoryAdd.setToolTipText("");
        btnCategoryAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCategoryAddActionPerformed(evt);
            }
        });

        btnCategoryUpdate.setBackground(new java.awt.Color(0, 153, 153));
        btnCategoryUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCategoryUpdate.setText("Update");
        btnCategoryUpdate.setToolTipText("");
        btnCategoryUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCategoryUpdateActionPerformed(evt);
            }
        });

        btnCategoryClear.setBackground(new java.awt.Color(0, 204, 204));
        btnCategoryClear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCategoryClear.setText("Clear");
        btnCategoryClear.setToolTipText("");
        btnCategoryClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCategoryClearActionPerformed(evt);
            }
        });

        txfID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("ID:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Name:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Description:");

        txfFName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txfFName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfFNameActionPerformed(evt);
            }
        });

        taDescription.setColumns(20);
        taDescription.setLineWrap(true);
        taDescription.setRows(5);
        jScrollPane2.setViewportView(taDescription);

        jLabel2.setBackground(new java.awt.Color(0, 102, 102));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 102));
        jLabel2.setText("Category Entry Form:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel5))
                                .addGap(0, 202, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txfID, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txfFName, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(btnCategoryAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCategoryUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCategoryClear, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(txfFName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txfID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCategoryAdd)
                    .addComponent(btnCategoryUpdate)
                    .addComponent(btnCategoryClear))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(204, 204, 204));
        jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnProductAdd.setBackground(new java.awt.Color(0, 153, 153));
        btnProductAdd.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnProductAdd.setText("Add");
        btnProductAdd.setToolTipText("");
        btnProductAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductAddActionPerformed(evt);
            }
        });

        btnProductUpdate.setBackground(new java.awt.Color(0, 153, 153));
        btnProductUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnProductUpdate.setText("Update");
        btnProductUpdate.setToolTipText("");
        btnProductUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductUpdateActionPerformed(evt);
            }
        });

        btnProductClear.setBackground(new java.awt.Color(0, 204, 204));
        btnProductClear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnProductClear.setText("Clear");
        btnProductClear.setToolTipText("");
        btnProductClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductClearActionPerformed(evt);
            }
        });

        txfProdID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("ID:");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Name:");

        txfProdName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txfProdName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfProdNameActionPerformed(evt);
            }
        });

        jLabel11.setBackground(new java.awt.Color(0, 102, 102));
        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 102, 102));
        jLabel11.setText("Product Entry Form:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("Price:");

        txfProdCatID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txfProdCatID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfProdCatIDActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("Category ID:");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setText("Quantity:");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("Color:");

        txfProdColor.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txfProdColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfProdColorActionPerformed(evt);
            }
        });

        cmbProdSize.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "XS", "S", "M", "L", "XL", "One size fits all." }));
        cmbProdSize.setSelectedIndex(-1);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setText("Size:");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(spnProdPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel14)
                                    .addComponent(txfProdColor, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spnProdQty)
                                .addContainerGap())))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(txfProdID, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txfProdName, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txfProdCatID, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12)))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(cmbProdSize, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnProductAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnProductUpdate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnProductClear, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel11))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txfProdID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txfProdName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txfProdCatID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spnProdPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnProdQty, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txfProdColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbProdSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProductAdd)
                    .addComponent(btnProductUpdate)
                    .addComponent(btnProductClear))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Product Inventory ");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        btnBack.setBackground(new java.awt.Color(0, 204, 204));
        btnBack.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBack.setText("Back");
        btnBack.setToolTipText("Return to Home");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 153, 153));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setText("Product Review");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("Most popular products:");

        tblReport.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        tblReport.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Name", "Price", "Category", "Rating"
            }
        ));
        jScrollPane3.setViewportView(tblReport);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1042, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBack)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBack, javax.swing.GroupLayout.Alignment.TRAILING))
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
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void categoryTblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_categoryTblMouseClicked
        DefaultTableModel dtm = (DefaultTableModel)categoryTbl.getModel();
        int selected = categoryTbl.getSelectedRow();

        txfID.setText(dtm.getValueAt(selected, 0).toString());
        txfFName.setText(dtm.getValueAt(selected, 1).toString());
        taDescription.setText(dtm.getValueAt(selected, 2).toString());
        
    }//GEN-LAST:event_categoryTblMouseClicked

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        try {
            con.close();
            System.out.println("Connection to product, catgeory closed.");
        } catch (SQLException ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.dispose();
        Home h = new Home();
        h.setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    private void productTblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productTblMouseClicked
        DefaultTableModel dtm = (DefaultTableModel)productTbl.getModel();
        int selected = productTbl.getSelectedRow();

        txfProdID.setText(dtm.getValueAt(selected, 0).toString());
        txfProdName.setText(dtm.getValueAt(selected, 1).toString());
        spnProdPrice.setValue(Double.parseDouble(dtm.getValueAt(selected, 2).toString()));
        txfProdColor.setText(dtm.getValueAt(selected, 3).toString());
        cmbProdSize.setSelectedItem(dtm.getValueAt(selected, 4).toString());
        spnProdQty.setValue(Integer.parseInt(dtm.getValueAt(selected, 5).toString()));
        txfProdCatID.setText(dtm.getValueAt(selected, 6).toString());
    }//GEN-LAST:event_productTblMouseClicked

    private void btnProductAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductAddActionPerformed
        getProductData();

        try {
            pst = con.prepareStatement("INSERT INTO product (productName, unitPrice, color, product_size, quantityInStock, categoryId) VALUES(?,?,?,?,?,?)");
            pst.setString(1, prodName);
            pst.setDouble(2, prodPrice);
            pst.setString(3, prodColor);
            pst.setString(4, prodSize);
            pst.setInt(5, prodQty);
            pst.setString(6, prodCategory);

            int rowsAffected = pst.executeUpdate();
            System.out.println("Number of product rows inserted: " + rowsAffected);

            JOptionPane.showMessageDialog(this,"Record inserted successfully.");
            showProductData();
            clearProductFields();
        } catch (SQLException ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,"Product insert failed.");
        }
    }//GEN-LAST:event_btnProductAddActionPerformed

    private void btnProductUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductUpdateActionPerformed
        getProductData();
        
        System.out.println(prodColor);

        try {
            pst = con.prepareStatement("UPDATE product SET productName=?, unitPrice=?, color=?, product_size=?, quantityInStock=?, categoryId=? WHERE productId=?");

            pst.setString(1, prodName);
            pst.setDouble(2, prodPrice);
            pst.setString(3, prodColor);
            pst.setString(4, prodSize);
            pst.setInt(5, prodQty);
            pst.setString(6, prodCategory);
            pst.setString(7, prodID);

            int rowsAffected = pst.executeUpdate();
            System.out.println("Number of customer rows updated: " + rowsAffected);

            JOptionPane.showMessageDialog(this,rowsAffected + " Record/s updated successfully.");
            showProductData();
            clearProductFields();
        } catch (SQLException ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,"Update failed.");
        }
    }//GEN-LAST:event_btnProductUpdateActionPerformed

    private void btnProductClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductClearActionPerformed
        clearProductFields();
    }//GEN-LAST:event_btnProductClearActionPerformed

    private void txfProdNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfProdNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfProdNameActionPerformed

    private void txfFNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfFNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfFNameActionPerformed

    private void btnCategoryClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCategoryClearActionPerformed
        clearCategoryFields();
    }//GEN-LAST:event_btnCategoryClearActionPerformed

    private void btnCategoryUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCategoryUpdateActionPerformed

        getCategoryData();

        try {
            pst = con.prepareStatement("UPDATE category SET name=?, description=? WHERE categoryId=?");

            pst.setString(1, catName);
            pst.setString(2, catDescription);
            pst.setString(3, catID);

            int rowsAffected = pst.executeUpdate();
            System.out.println("Number of customer rows updated: " + rowsAffected);

            JOptionPane.showMessageDialog(this,rowsAffected + " Record/s updated successfully.");
            showCategoryData();
            clearCategoryFields();
        } catch (SQLException ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,"Update failed.");
        }
    }//GEN-LAST:event_btnCategoryUpdateActionPerformed

    private void btnCategoryAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCategoryAddActionPerformed

        getCategoryData();

        try {
            pst = con.prepareStatement("INSERT INTO category VALUES(?,?,?)");
            pst.setString(1, catID);
            pst.setString(2, catName);
            pst.setString(3, catDescription);

            int rowsAffected = pst.executeUpdate();
            System.out.println("Number of category rows inserted: " + rowsAffected);

            JOptionPane.showMessageDialog(this,"Record inserted successfully.");
            showCategoryData();
            clearCategoryFields();
        } catch (SQLException ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,"Category insert failed.");
        }
    }//GEN-LAST:event_btnCategoryAddActionPerformed

    private void txfProdCatIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfProdCatIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfProdCatIDActionPerformed

    private void txfProdColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfProdColorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfProdColorActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
        Product_Review pr = new Product_Review();
        pr.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(Inventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Inventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Inventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Inventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Inventory().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCategoryAdd;
    private javax.swing.JButton btnCategoryClear;
    private javax.swing.JButton btnCategoryUpdate;
    private javax.swing.JButton btnProductAdd;
    private javax.swing.JButton btnProductClear;
    private javax.swing.JButton btnProductUpdate;
    private javax.swing.JTable categoryTbl;
    private javax.swing.JComboBox<String> cmbProdSize;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable productTbl;
    private javax.swing.JSpinner spnProdPrice;
    private javax.swing.JSpinner spnProdQty;
    private javax.swing.JTextArea taDescription;
    private javax.swing.JTable tblReport;
    private javax.swing.JTextField txfFName;
    private javax.swing.JTextField txfID;
    private javax.swing.JTextField txfProdCatID;
    private javax.swing.JTextField txfProdColor;
    private javax.swing.JTextField txfProdID;
    private javax.swing.JTextField txfProdName;
    // End of variables declaration//GEN-END:variables
}
