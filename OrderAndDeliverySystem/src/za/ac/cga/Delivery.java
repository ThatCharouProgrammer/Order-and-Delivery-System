package za.ac.cga;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class Delivery extends javax.swing.JFrame {
    
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    
    int delID;
    String delProvince;
    String delDriverID;
    String delPackerID;
    String delVIN;
    String delStatus;
    String delDepartDate;
    String delArrivalDate;
    
    String vin;
    String make;
    String transmission;
    String fuelType;
    String vehicleType;

    /**
     * Creates new form Delivery
     */
    public Delivery() {
        initComponents();
        connect();
        showDeliveryData();
        showVehicleData();
        showReport();
    }
    
    public void getDeliveryData()
    {
        delPackerID = txfPackID.getText();
        delDriverID = txfDriverID.getText();
        delVIN = txfDelVIN.getText();
        delProvince = txfProvince.getText();
        delStatus = cmbDelStatus.getSelectedItem().toString();
        delDepartDate = txfDepartDate.getText();
        delArrivalDate = txfArrivalDate.getText();
    }
    
    public void getVehicleData()
    {
        vin = txfVVin.getText();
        make = txfVMake.getText();
        transmission = cmbVTransmission.getSelectedItem().toString();
        fuelType = cmbVFuel.getSelectedItem().toString();
        vehicleType = cmbVType.getSelectedItem().toString();
    }
    
    private void connect()
    {
        System.out.println("Connecting to database...");
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/order_and_delivery_system","root", "N@z728001");
            
            System.out.println("Connection established.");
            
        } catch (ClassNotFoundException e) {
            Logger.getLogger(Delivery.class.getName()).log(Level.SEVERE,null,e);
            JOptionPane.showMessageDialog(this,"Connection to database failed.");
        } catch (SQLException ex) {
            Logger.getLogger(Delivery.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,"SQL connection failed.");
        }
    }
    
    public void showDeliveryData()
    {
        try {
            pst = con.prepareStatement("SELECT * FROM delivery");
            rs = pst.executeQuery();
            
            ResultSetMetaData rsd = rs.getMetaData();
            int t;
            
            t = rsd.getColumnCount();
            
            DefaultTableModel d = (DefaultTableModel)tblDelivery.getModel();
            d.setRowCount(0);
            
            while(rs.next())
            {
                Vector v = new Vector();
                
                for(int i=0; i<=t; i++)
                {
                    v.add(rs.getString("deliveryId"));
                    v.add(rs.getString("province"));
                    v.add(rs.getString("dEmployeeId"));
                    v.add(rs.getString("pEmployeeId"));
                    v.add(rs.getString("vin"));
                    v.add(rs.getString("deliveryStatus"));
                    v.add(rs.getDate("departureDate"));
                    v.add(rs.getDate("arrivalDate"));
                }
                
                d.addRow(v);
            }
            
        } catch (SQLException e) {
            Logger.getLogger(Delivery.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this,"Displaying delivery data failed.");
        }
    }
    
    public void showVehicleData()
    {
        try {
            pst = con.prepareStatement("SELECT * FROM vehicle");
            rs = pst.executeQuery();
            
            ResultSetMetaData rsd = rs.getMetaData();
            int t;
            
            t = rsd.getColumnCount();
            
            DefaultTableModel d = (DefaultTableModel)tblVehicle.getModel();
            d.setRowCount(0);
            
            while(rs.next())
            {
                Vector v = new Vector();
                
                for(int i=0; i<=t; i++)
                {
                    v.add(rs.getString("vin"));
                    v.add(rs.getString("make"));
                    v.add(rs.getString("transmissionType"));
                    v.add(rs.getString("fuelType"));
                    v.add(rs.getString("vehicleType"));
                }
                
                d.addRow(v);
            }
            
        } catch (SQLException e) {
            Logger.getLogger(Delivery.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this,"Displaying vehicle data failed.");
        }
    }
    
    public void showReport()
    {
        /*
        Drivers that made the most deliveries:
        Display the employees ID, fullname and number of deliveries made
        */
        try {
            pst = con.prepareStatement("SELECT dEmployeeId, firstName, surname, COUNT(dEmployeeId) AS Number_of_Deliveries\n" +
"                                           FROM delivery, employee AS T\n" +
"                                               WHERE dEmployeeId = T.employeeId AND dEmployeeId = \n" +
"                                                   (SELECT DISTINCT employeeId FROM driver WHERE dEmployeeId = employeeId)\n" +
"                                               GROUP BY dEmployeeId \n" +
"                                               ORDER BY Number_of_Deliveries DESC;");
            
            rs = pst.executeQuery();
            
            ResultSetMetaData rsd = rs.getMetaData();
            int t;
            
            t = rsd.getColumnCount();
            
            DefaultTableModel d = (DefaultTableModel)reportTbl.getModel();
            d.setRowCount(0);
            
            while(rs.next())
            {
                Vector v = new Vector();
                
                for(int i=0; i<=t; i++)
                {
                    v.add(rs.getString("dEmployeeId"));
                    v.add(rs.getString("firstName"));
                    v.add(rs.getString("surname"));
                    v.add(rs.getString("Number_of_Deliveries"));
                }
                
                d.addRow(v);
            }
            
        } catch (SQLException e) {
            Logger.getLogger(Delivery.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this,"Displaying report data failed.");
        }
    }
    
    public void clearDeliveryFields()
    {
        txfPackID.setText("");
        txfDriverID.setText("");
        txfDelVIN.setText("");
        txfDepartDate.setText("");
        txfArrivalDate.setText("");
        txfProvince.setText("");
        cmbDelStatus.setSelectedIndex(-1);
    }
    
    public void clearVehicleFields()
    {
        txfVMake.setText("");
        txfVVin.setText("");
        cmbVFuel.setSelectedIndex(-1);
        cmbVTransmission.setSelectedIndex(-1);
        cmbVType.setSelectedIndex(-1);
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
        jLabel2 = new javax.swing.JLabel();
        txfPackID = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txfDriverID = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txfDelVIN = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txfDepartDate = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        cmbDelStatus = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txfProvince = new javax.swing.JTextField();
        txfArrivalDate = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        btnDelAdd = new javax.swing.JButton();
        btnDelUpdate = new javax.swing.JButton();
        btnDelClear = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDelivery = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txfVVin = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txfVMake = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        cmbVTransmission = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        btnVehicleAdd = new javax.swing.JButton();
        btnVehicleUpdate = new javax.swing.JButton();
        btnVehicleClear = new javax.swing.JButton();
        cmbVFuel = new javax.swing.JComboBox<>();
        cmbVType = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblVehicle = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        reportTbl = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(95, 158, 160), 6));

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(95, 158, 160), 6));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Packer ID:");

        txfPackID.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Driver ID:");

        txfDriverID.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Province:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("VIN:");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Delivery Status:");

        cmbDelStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DELIVERED", "IN TRANSIT", " " }));
        cmbDelStatus.setSelectedIndex(-1);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Departure Date:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Arrival Date:");

        btnDelAdd.setBackground(new java.awt.Color(0, 153, 153));
        btnDelAdd.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDelAdd.setText("Add");
        btnDelAdd.setToolTipText("");
        btnDelAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelAddActionPerformed(evt);
            }
        });

        btnDelUpdate.setBackground(new java.awt.Color(0, 153, 153));
        btnDelUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDelUpdate.setText("Update");
        btnDelUpdate.setToolTipText("");
        btnDelUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelUpdateActionPerformed(evt);
            }
        });

        btnDelClear.setBackground(new java.awt.Color(0, 204, 204));
        btnDelClear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDelClear.setText("Clear");
        btnDelClear.setToolTipText("");
        btnDelClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(55, 55, 55)
                        .addComponent(jLabel3))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txfPackID, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txfDriverID, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4)
                    .addComponent(txfDelVIN)
                    .addComponent(jLabel5)
                    .addComponent(txfProvince))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbDelStatus, 0, 110, Short.MAX_VALUE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(txfDepartDate)
                            .addComponent(txfArrivalDate))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnDelUpdate)
                            .addComponent(btnDelAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDelClear, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txfPackID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txfDriverID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txfProvince, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txfDelVIN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cmbDelStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnDelAdd))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(btnDelUpdate))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txfDepartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnDelClear))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txfArrivalDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Delivery Entry Form");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1)
        );

        tblDelivery.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Province", "Driver ID", "Packer ID", "VIN", "Delivery Status", "Departure Date", "Arrival Date"
            }
        ));
        tblDelivery.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDeliveryMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDelivery);

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(95, 158, 160), 6));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("VIN:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Make:");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Transmission:");

        cmbVTransmission.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AUTOMATIC", "MANUAL" }));
        cmbVTransmission.setSelectedIndex(-1);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Fuel:");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Vehicle Type:");

        btnVehicleAdd.setBackground(new java.awt.Color(0, 153, 153));
        btnVehicleAdd.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnVehicleAdd.setText("Add");
        btnVehicleAdd.setToolTipText("");
        btnVehicleAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVehicleAddActionPerformed(evt);
            }
        });

        btnVehicleUpdate.setBackground(new java.awt.Color(0, 153, 153));
        btnVehicleUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnVehicleUpdate.setText("Update");
        btnVehicleUpdate.setToolTipText("");
        btnVehicleUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVehicleUpdateActionPerformed(evt);
            }
        });

        btnVehicleClear.setBackground(new java.awt.Color(0, 204, 204));
        btnVehicleClear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnVehicleClear.setText("Clear");
        btnVehicleClear.setToolTipText("");
        btnVehicleClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVehicleClearActionPerformed(evt);
            }
        });

        cmbVFuel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PETROL", "DIESEL" }));
        cmbVFuel.setSelectedIndex(-1);

        cmbVType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TRUCK", "BAKKIE", " " }));
        cmbVType.setSelectedIndex(-1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(cmbVTransmission, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbVFuel, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbVType, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnVehicleClear, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(42, 42, 42)
                                .addComponent(jLabel14))
                            .addComponent(txfVVin, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(txfVMake, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnVehicleAdd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnVehicleUpdate, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(62, 62, 62)
                                .addComponent(jLabel15)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txfVVin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txfVMake, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbVTransmission, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbVFuel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbVType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnVehicleClear)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnVehicleAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnVehicleUpdate)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel16.setText("Vehicle Entry Form");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel16)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel16)
        );

        tblVehicle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "VIN", "Make", "Transmission", "Fuel", "Vehicle Type"
            }
        ));
        tblVehicle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblVehicleMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblVehicle);

        reportTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Driver ID", "First Name", "Surname", "No. of Deliveries"
            }
        ));
        jScrollPane3.setViewportView(reportTbl);

        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel17.setText("Report - Drivers that made the most deliveries");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING)
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnBack)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
                                .addComponent(jScrollPane2)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBack)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void btnDelAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelAddActionPerformed
        getDeliveryData();

        try {
            pst = con.prepareStatement("INSERT INTO delivery (province, dEmployeeId, pEmployeeId, vin, deliveryStatus, departureDate, arrivalDate) VALUES(?,?,?,?,?,?,?)");
            pst.setString(1, delProvince);
            pst.setString(2, delDriverID);
            pst.setString(3, delPackerID);
            pst.setString(4, delVIN);
            pst.setString(5, delStatus);
            pst.setString(6, delDepartDate);
            pst.setString(7, delArrivalDate);

            int rowsAffected = pst.executeUpdate();
            System.out.println("Number of delivery rows inserted: " + rowsAffected);

            JOptionPane.showMessageDialog(this,"Record inserted successfully.");
            showDeliveryData();
            showReport();
            clearDeliveryFields();
        } catch (SQLException ex) {
            Logger.getLogger(Delivery.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,"Delivery insert failed.");
        }
    }//GEN-LAST:event_btnDelAddActionPerformed

    private void btnDelUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelUpdateActionPerformed

        getDeliveryData();

        try {
            pst = con.prepareStatement("UPDATE delivery SET province=?, dEmployeeId=?, pEmployeeId=?, vin=?, deliveryStatus=?, departureDate=?, arrivalDate=?  WHERE deliveryId=?");

            pst.setString(1, delProvince);
            pst.setString(2, delDriverID);
            pst.setString(3, delPackerID);
            pst.setString(4, delVIN);
            pst.setString(5, delStatus);
            pst.setString(6, delDepartDate);
            pst.setString(7, delArrivalDate);
            pst.setInt(8, delID);

            int rowsAffected = pst.executeUpdate();
            System.out.println("Number of delivery rows updated: " + rowsAffected);

            JOptionPane.showMessageDialog(this,rowsAffected + " Record/s updated successfully.");
            showDeliveryData();
            showReport();
            clearDeliveryFields();
        } catch (SQLException ex) {
            Logger.getLogger(Delivery.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,"Update failed.");
        }
    }//GEN-LAST:event_btnDelUpdateActionPerformed

    private void btnDelClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelClearActionPerformed
        clearDeliveryFields();
    }//GEN-LAST:event_btnDelClearActionPerformed

    private void btnVehicleAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVehicleAddActionPerformed
        getVehicleData();

        try {
            pst = con.prepareStatement("INSERT INTO vehicle (vin, make, transmissionType, fuelType, vehicleType) VALUES(?,?,?,?,?)");
            pst.setString(1, vin);
            pst.setString(2, make);
            pst.setString(3, transmission);
            pst.setString(4, fuelType);
            pst.setString(5, vehicleType);

            int rowsAffected = pst.executeUpdate();
            System.out.println("Number of vehicle rows inserted: " + rowsAffected);

            JOptionPane.showMessageDialog(this,"Record inserted successfully.");
            showVehicleData();
            clearVehicleFields();
        } catch (SQLException ex) {
            Logger.getLogger(Delivery.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,"Vehicle insert failed.");
        }
    }//GEN-LAST:event_btnVehicleAddActionPerformed

    private void btnVehicleUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVehicleUpdateActionPerformed
        getVehicleData();

        try {
            pst = con.prepareStatement("UPDATE vehicle SET make=?, transmissionType=?, fuelType=?, vehicleType=?  WHERE vin=?");

            pst.setString(1, make);
            pst.setString(2, transmission);
            pst.setString(3, fuelType);
            pst.setString(4, vehicleType);
            pst.setString(5, vin);

            int rowsAffected = pst.executeUpdate();
            System.out.println("Number of vehicle rows updated: " + rowsAffected);

            JOptionPane.showMessageDialog(this,rowsAffected + " Record/s updated successfully.");
            showVehicleData();
            clearVehicleFields();
            txfVVin.setEnabled(true);
        } catch (SQLException ex) {
            Logger.getLogger(Delivery.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,"Update failed.");
        }
    }//GEN-LAST:event_btnVehicleUpdateActionPerformed

    private void btnVehicleClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVehicleClearActionPerformed
        clearVehicleFields();
        txfVVin.setEnabled(true);
    }//GEN-LAST:event_btnVehicleClearActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        try {
            con.close();
            System.out.println("Connection closed.");
        } catch (SQLException ex) {
            Logger.getLogger(Delivery.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.dispose();
        Home h = new Home();
        h.setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    private void tblDeliveryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDeliveryMouseClicked
        clearDeliveryFields();
        
        DefaultTableModel dtm = (DefaultTableModel)tblDelivery.getModel();
        int selected = tblDelivery.getSelectedRow();
        
        delID = Integer.parseInt(dtm.getValueAt(selected, 0).toString());
        
        txfProvince.setText(dtm.getValueAt(selected, 1).toString());
        txfDriverID.setText(dtm.getValueAt(selected, 2).toString());
        txfPackID.setText(dtm.getValueAt(selected, 3).toString());
        txfDelVIN.setText(dtm.getValueAt(selected, 4).toString());
        cmbDelStatus.setSelectedItem(dtm.getValueAt(selected, 5));
        txfDepartDate.setText(dtm.getValueAt(selected, 6).toString());
        txfArrivalDate.setText(dtm.getValueAt(selected, 7).toString());

    }//GEN-LAST:event_tblDeliveryMouseClicked

    private void tblVehicleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblVehicleMouseClicked
        clearVehicleFields();
        
        txfVVin.setEnabled(false);
        
        DefaultTableModel dtm = (DefaultTableModel)tblVehicle.getModel();
        int selected = tblVehicle.getSelectedRow();
        
        vin = dtm.getValueAt(selected, 0).toString();
        
        txfVVin.setText(vin);
        txfVMake.setText(dtm.getValueAt(selected, 1).toString());
        cmbVTransmission.setSelectedItem(dtm.getValueAt(selected, 2));
        cmbVFuel.setSelectedItem(dtm.getValueAt(selected, 3));
        cmbVType.setSelectedItem(dtm.getValueAt(selected, 4));
    }//GEN-LAST:event_tblVehicleMouseClicked

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
            java.util.logging.Logger.getLogger(Delivery.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Delivery.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Delivery.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Delivery.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Delivery().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnDelAdd;
    private javax.swing.JButton btnDelClear;
    private javax.swing.JButton btnDelUpdate;
    private javax.swing.JButton btnVehicleAdd;
    private javax.swing.JButton btnVehicleClear;
    private javax.swing.JButton btnVehicleUpdate;
    private javax.swing.JComboBox<String> cmbDelStatus;
    private javax.swing.JComboBox<String> cmbVFuel;
    private javax.swing.JComboBox<String> cmbVTransmission;
    private javax.swing.JComboBox<String> cmbVType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
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
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable reportTbl;
    private javax.swing.JTable tblDelivery;
    private javax.swing.JTable tblVehicle;
    private javax.swing.JTextField txfArrivalDate;
    private javax.swing.JTextField txfDelVIN;
    private javax.swing.JTextField txfDepartDate;
    private javax.swing.JTextField txfDriverID;
    private javax.swing.JTextField txfPackID;
    private javax.swing.JTextField txfProvince;
    private javax.swing.JTextField txfVMake;
    private javax.swing.JTextField txfVVin;
    // End of variables declaration//GEN-END:variables
}
