/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view;

import com.mysql.jdbc.Connection;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import static java.lang.Thread.sleep;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.PrinterName;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author damara
 */
public class OrderMasuk extends javax.swing.JFrame {
    Connection con;
    Statement st;
    ResultSet rs;
    Vector originalTableModel;
    
    //Variabel Global
    String tglSekarang ;
    String wktSekarang ;
    
    /**
     * Creates new form DataKonsumen
     */
    public OrderMasuk() {
        initComponents();
        
        // CALL
        data();
        increment();
        tampilWaktu();
        total_bayar();
        Kunci_field();
        
        // focus
        namaKonsumen.requestFocus();
        tumbal_id.setVisible(false);
        tumbal_subtotal.setVisible(false);
    }
    
    //    AUTO INCREMENT
    private void increment(){
        try{
            con = Koneksi.getKoneksi();
            String sql = "SELECT COUNT(no_pemesanan) as id FROM laporan";
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                String kode = rs.getString("id");
                int a = Integer.parseInt(kode);
                int b = a+1;
                noPemesanan.setText("ORDERPJ22"+b);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,e);
        }
    }
    
    //MENAMPILKAN WAKTU & TANGGAL
    private void tampilWaktu(){
        Thread t2;
        t2 = new Thread(){
            @Override
            public void run(){
                for(;;){
                    try {
                        
                        Calendar cal = new GregorianCalendar();
                        wktSekarang = (cal.get(Calendar.HOUR)+" : "
                                +cal.get(Calendar.MINUTE)+" : "
                                +cal.get(Calendar.SECOND));
                        tglSekarang = (cal.get(Calendar.YEAR)+"-"
                                +(cal.get(Calendar.MONTH)+1)+"-"
                                +cal.get(Calendar.DATE));
                        //jLabel4.setText(wktsekarang);
                        tanggal.setText(tglSekarang);
                        sleep(1000);
                    } catch (InterruptedException ex) {
                      
                    }
                }
            };
        };
        t2.start();
     }
    
    //HITUNGAN BAYAR
    private void subtot_bayar(){
        int jmlBarang = Integer.parseInt(jumlahBarang.getText());
        int hrgBarang = Integer.parseInt(hargaBarang.getText());
        
        int hitung_subtotal = (jmlBarang * hrgBarang);
        tumbal_subtotal.setText(Integer.toString(hitung_subtotal));
    }
    
    private void total_bayar() {
        String tot = totBayar.getText();
        try{
            con = Koneksi.getKoneksi();
            String sql = "SELECT SUM(subtotal_harga) AS subtotal FROM transaksi";
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if(rs.next()){
                tot = rs.getString("subtotal");
                totBayar.setText(tot);
            }
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    // MENAMPILKAN DATA TRANSAKSI
    private void data() {
        try {
            con = Koneksi.getKoneksi();
            String sql = "SELECT * FROM transaksi";
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                Object[] row = new Object[7];
                row[0] = rs.getString("id");
                row[1] = rs.getString("nama_barang");
                row[2] = rs.getString("tipe_barang");
                row[3] = rs.getString("ukuran_barang");
                row[4] = rs.getString("harga_barang");
                row[5] = rs.getString("jumlah_barang");
                row[6] = rs.getString("subtotal_harga");
                model.addRow(row);
                
                String nama_kon = rs.getString("nama_konsumen");
                namaKonsumen.setText(nama_kon);
                String telp_kon = rs.getString("telp_konsumen");
                telpKonsumen.setText(telp_kon);
                String alamat_kon = rs.getString("alamat_konsumen");
                alamat.setText(alamat_kon);
            }
        } catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    private void simpan_transaksi() {
        String no_pemesanan = noPemesanan.getText();
        String tgl = tanggal.getText();
        String nama_kon = namaKonsumen.getText();
        String telp_kon = telpKonsumen.getText();
        String alamat_kon = alamat.getText();
        String tot_bayar = totBayar.getText();
        
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        String nama_brg, tipe_brg, ukuran_brg, jumlah_brg, harga_brg, subtotal;
        
        try {
            java.sql.Connection conn=(Connection)Koneksi.getKoneksi();
            for (int i=0;i<model.getRowCount();i++){
                nama_brg = model.getValueAt(i, 1).toString();
                tipe_brg = model.getValueAt(i, 2).toString();
                ukuran_brg = model.getValueAt(i, 3).toString();
                harga_brg = model.getValueAt(i, 4).toString();
                jumlah_brg = model.getValueAt(i, 5).toString();
                subtotal = model.getValueAt(i, 6).toString();
                
                String sql = "INSERT INTO laporan (no_pemesanan, tgl_pemesanan, nama_konsumen, telp_konsumen, alamat_konsumen, "
                        + "nama_barang, tipe_barang, ukuran_barang, harga_barang, jumlah_barang, subtotal_harga, total_harga) VALUES"
                        + "('"+no_pemesanan+"','"+tgl+"','"+nama_kon+"','"+telp_kon+"','"+alamat_kon+"',? ,? ,? ,? ,? ,? ,'"+tot_bayar+"')";
                java.sql.PreparedStatement pst=conn.prepareStatement(sql);
                pst.setString(1,nama_brg);
                pst.setString(2,tipe_brg);
                pst.setString(3,ukuran_brg);
                pst.setString(4,harga_brg);
                pst.setString(5,jumlah_brg);
                pst.setString(6,subtotal);
                pst.execute();
            }
            JOptionPane.showMessageDialog(this, "TRANSAKSI DISIMPAN");
            model.setRowCount(0);
            data();
            // increment();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    //AUTO PRINT BOSS
    private void PrintReportToPrinter(JasperPrint jp) throws JRException {
        // TODO Auto-generated method stub
        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
        // printRequestAttributeSet.add(MediaSizeName.ISO_A4); //setting page size
        printRequestAttributeSet.add(new Copies(1));

        PrinterName printerName = new PrinterName("Microsoft Print to PDF", null); //gets printer
        //PrinterName printerName = new PrinterName("POS58 Printer", null);

        PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
        printServiceAttributeSet.add(printerName);

        JRPrintServiceExporter exporter = new JRPrintServiceExporter();

        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet);
        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
        exporter.exportReport();
    }
    
    private void clear(){
        namaBarang.setText("");
        tipeBarang.setText("");
        ukuranBarang.setText("");
        jumlahBarang.setText("");
        hargaBarang.setText("");
        tumbal_id.setText("");
        tumbal_subtotal.setText("");
    }
    
    private void reset(){
        namaKonsumen.setText("");
        telpKonsumen.setText("");
        alamat.setText("");
        namaBarang.setText("");
        tipeBarang.setText("");
        ukuranBarang.setText("");
        jumlahBarang.setText("");
        hargaBarang.setText("");
        tumbal_id.setText("");
        tumbal_subtotal.setText("");
        totBayar.setText("");
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        jPanel1 = new javax.swing.JPanel();
        exit = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        noPemesanan = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        tanggal = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        totBayar = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        namaKonsumen = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        telpKonsumen = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        alamat = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        namaBarang = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        tipeBarang = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        ukuranBarang = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jumlahBarang = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        hargaBarang = new javax.swing.JTextField();
        printUlang = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        delete = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        simpanPrint = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        reset = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        simpanTransaksi = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        tumbal_subtotal = new javax.swing.JTextField();
        tumbal_id = new javax.swing.JTextField();
        home = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Order Masuk - Putra Jaya");
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(14, 53, 117));

        exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/image/exit.png"))); // NOI18N
        exit.setToolTipText("Exit");
        exit.setBorderPainted(false);
        exit.setContentAreaFilled(false);
        exit.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/image/exit1.png"))); // NOI18N
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/image/minimize.png"))); // NOI18N
        jButton1.setToolTipText("Minimize");
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/image/minimize1.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(66, 151, 216));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/image/logo header putih kuning.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Montserrat", 1, 34)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Toko Kue Bude Rama");

        jLabel3.setFont(new java.awt.Font("Montserrat", 0, 15)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Kepuasan Pelanggan Adalah Prioritas Utama");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(703, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("No Pemesanan");

        noPemesanan.setEditable(false);
        noPemesanan.setBackground(new java.awt.Color(156, 199, 233));
        noPemesanan.setFont(new java.awt.Font("Open Sans", 0, 12)); // NOI18N
        noPemesanan.setMargin(new java.awt.Insets(2, 8, 2, 2));

        jLabel6.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Tanggal Transaksi");

        tanggal.setEditable(false);
        tanggal.setBackground(new java.awt.Color(156, 199, 233));
        tanggal.setFont(new java.awt.Font("Open Sans", 0, 12)); // NOI18N
        tanggal.setMargin(new java.awt.Insets(2, 8, 2, 2));

        jLabel7.setFont(new java.awt.Font("Open Sans SemiBold", 0, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("TOTAL BAYAR");

        totBayar.setEditable(false);
        totBayar.setBackground(new java.awt.Color(250, 178, 40));
        totBayar.setFont(new java.awt.Font("Open Sans", 0, 38)); // NOI18N
        totBayar.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totBayar.setMargin(new java.awt.Insets(2, 2, 2, 20));

        jPanel3.setBackground(new java.awt.Color(66, 151, 216));

        jLabel8.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("No Telepon");

        namaKonsumen.setBackground(new java.awt.Color(156, 199, 233));
        namaKonsumen.setFont(new java.awt.Font("Open Sans", 0, 12)); // NOI18N
        namaKonsumen.setMargin(new java.awt.Insets(2, 8, 2, 2));

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, namaBarang, org.jdesktop.beansbinding.ObjectProperty.create(), namaKonsumen, org.jdesktop.beansbinding.BeanProperty.create("nextFocusableComponent"));
        bindingGroup.addBinding(binding);

        namaKonsumen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                namaKonsumenKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                namaKonsumenKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                namaKonsumenKeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Nama Konsumen");

        telpKonsumen.setEditable(false);
        telpKonsumen.setBackground(new java.awt.Color(156, 199, 233));
        telpKonsumen.setFont(new java.awt.Font("Open Sans", 0, 12)); // NOI18N
        telpKonsumen.setMargin(new java.awt.Insets(2, 8, 2, 2));

        jLabel10.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Alamat");

        alamat.setEditable(false);
        alamat.setBackground(new java.awt.Color(156, 199, 233));
        alamat.setFont(new java.awt.Font("Open Sans", 0, 12)); // NOI18N
        alamat.setMargin(new java.awt.Insets(2, 8, 2, 2));

        jLabel11.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Nama Kue");

        namaBarang.setBackground(new java.awt.Color(156, 199, 233));
        namaBarang.setFont(new java.awt.Font("Open Sans", 0, 12)); // NOI18N
        namaBarang.setMargin(new java.awt.Insets(2, 8, 2, 2));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, tipeBarang, org.jdesktop.beansbinding.ObjectProperty.create(), namaBarang, org.jdesktop.beansbinding.BeanProperty.create("nextFocusableComponent"));
        bindingGroup.addBinding(binding);

        namaBarang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                namaBarangKeyPressed(evt);
            }
        });

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setBackground(new java.awt.Color(156, 199, 233));
        jTable1.setFont(new java.awt.Font("Open Sans", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "Nama Kue", "Tipe Kue", "Ukuran Kue", "Harga", "Jumlah", "Sub Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(156, 199, 233));
        jTable1.setIntercellSpacing(new java.awt.Dimension(20, 10));
        jTable1.setRowHeight(25);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMinWidth(0);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(0);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        }

        jLabel12.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Tipe Kue");

        tipeBarang.setBackground(new java.awt.Color(156, 199, 233));
        tipeBarang.setFont(new java.awt.Font("Open Sans", 0, 12)); // NOI18N
        tipeBarang.setMargin(new java.awt.Insets(2, 8, 2, 2));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ukuranBarang, org.jdesktop.beansbinding.ObjectProperty.create(), tipeBarang, org.jdesktop.beansbinding.BeanProperty.create("nextFocusableComponent"));
        bindingGroup.addBinding(binding);

        tipeBarang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tipeBarangKeyPressed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Ukuran Kue");

        ukuranBarang.setBackground(new java.awt.Color(156, 199, 233));
        ukuranBarang.setFont(new java.awt.Font("Open Sans", 0, 12)); // NOI18N
        ukuranBarang.setMargin(new java.awt.Insets(2, 8, 2, 2));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jumlahBarang, org.jdesktop.beansbinding.ObjectProperty.create(), ukuranBarang, org.jdesktop.beansbinding.BeanProperty.create("nextFocusableComponent"));
        bindingGroup.addBinding(binding);

        ukuranBarang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ukuranBarangKeyPressed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Jumlah Kue");

        jumlahBarang.setBackground(new java.awt.Color(156, 199, 233));
        jumlahBarang.setFont(new java.awt.Font("Open Sans", 0, 12)); // NOI18N
        jumlahBarang.setMargin(new java.awt.Insets(2, 8, 2, 2));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, hargaBarang, org.jdesktop.beansbinding.ObjectProperty.create(), jumlahBarang, org.jdesktop.beansbinding.BeanProperty.create("nextFocusableComponent"));
        bindingGroup.addBinding(binding);

        jumlahBarang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jumlahBarangKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jumlahBarangKeyTyped(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Harga");

        hargaBarang.setBackground(new java.awt.Color(156, 199, 233));
        hargaBarang.setMargin(new java.awt.Insets(2, 8, 2, 2));
        hargaBarang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hargaBarangKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                hargaBarangKeyTyped(evt);
            }
        });

        printUlang.setBackground(new java.awt.Color(14, 53, 117));
        printUlang.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        printUlang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        printUlang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                printUlangMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                printUlangMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                printUlangMouseExited(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Open Sans SemiBold", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Print Ulang Struk");

        javax.swing.GroupLayout printUlangLayout = new javax.swing.GroupLayout(printUlang);
        printUlang.setLayout(printUlangLayout);
        printUlangLayout.setHorizontalGroup(
            printUlangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printUlangLayout.createSequentialGroup()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        printUlangLayout.setVerticalGroup(
            printUlangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, printUlangLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        delete.setBackground(new java.awt.Color(14, 53, 117));
        delete.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        delete.setToolTipText("Hapus Data Pembelian");
        delete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        delete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                deleteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                deleteMouseExited(evt);
            }
        });

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/image/delete.png"))); // NOI18N

        javax.swing.GroupLayout deleteLayout = new javax.swing.GroupLayout(delete);
        delete.setLayout(deleteLayout);
        deleteLayout.setHorizontalGroup(
            deleteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
        );
        deleteLayout.setVerticalGroup(
            deleteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        simpanPrint.setBackground(new java.awt.Color(14, 53, 117));
        simpanPrint.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        simpanPrint.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        simpanPrint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                simpanPrintMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                simpanPrintMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                simpanPrintMouseExited(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Open Sans SemiBold", 0, 15)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Simpan & Print");

        javax.swing.GroupLayout simpanPrintLayout = new javax.swing.GroupLayout(simpanPrint);
        simpanPrint.setLayout(simpanPrintLayout);
        simpanPrintLayout.setHorizontalGroup(
            simpanPrintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
        );
        simpanPrintLayout.setVerticalGroup(
            simpanPrintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, simpanPrintLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        reset.setBackground(new java.awt.Color(14, 53, 117));
        reset.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        reset.setToolTipText("Clear");
        reset.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                resetMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                resetMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                resetMouseExited(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Open Sans SemiBold", 0, 16)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("C");

        javax.swing.GroupLayout resetLayout = new javax.swing.GroupLayout(reset);
        reset.setLayout(resetLayout);
        resetLayout.setHorizontalGroup(
            resetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
        );
        resetLayout.setVerticalGroup(
            resetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, resetLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        simpanTransaksi.setBackground(new java.awt.Color(14, 53, 117));
        simpanTransaksi.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        simpanTransaksi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        simpanTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                simpanTransaksiMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                simpanTransaksiMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                simpanTransaksiMouseExited(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Open Sans SemiBold", 0, 15)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Simpan Transaksi");

        javax.swing.GroupLayout simpanTransaksiLayout = new javax.swing.GroupLayout(simpanTransaksi);
        simpanTransaksi.setLayout(simpanTransaksiLayout);
        simpanTransaksiLayout.setHorizontalGroup(
            simpanTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, simpanTransaksiLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        simpanTransaksiLayout.setVerticalGroup(
            simpanTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(simpanTransaksiLayout.createSequentialGroup()
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tumbal_subtotal.setBackground(new java.awt.Color(66, 151, 216));

        tumbal_id.setBackground(new java.awt.Color(66, 151, 216));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(printUlang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(184, 184, 184)
                        .addComponent(tumbal_id, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tumbal_subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(simpanTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(reset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(simpanPrint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1193, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(46, 46, 46)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(namaKonsumen, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                            .addComponent(telpKonsumen)
                            .addComponent(alamat)
                            .addComponent(namaBarang))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tipeBarang, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                            .addComponent(ukuranBarang)
                            .addComponent(jumlahBarang)
                            .addComponent(hargaBarang))))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(namaKonsumen, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tipeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(telpKonsumen, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ukuranBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(alamat, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jumlahBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(namaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hargaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(printUlang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(simpanPrint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(reset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(simpanTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(26, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tumbal_subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tumbal_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(36, 36, 36))))
        );

        home.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/image/back.png"))); // NOI18N
        home.setToolTipText("Back To Home Page");
        home.setBorderPainted(false);
        home.setContentAreaFilled(false);
        home.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/image/left-arrow.png"))); // NOI18N
        home.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                homeMouseClicked(evt);
            }
        });
        home.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(home, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exit, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(87, 87, 87)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel6)
                                .addComponent(jLabel5))
                            .addGap(40, 40, 40)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(noPemesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel7)
                                    .addGap(18, 18, 18))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(totBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(39, 39, 39)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(exit, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(home, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel7))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(totBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(noPemesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        bindingGroup.bind();

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        // TODO add your handling code here:
        int option = JOptionPane.YES_NO_OPTION;
            int optionconfirm = JOptionPane.showConfirmDialog(this, "Apa Anda Yakin Ingin Keluar?", "Konfirmasi", option);
            if(optionconfirm == 0) {
                System.exit(0);
            }
    }//GEN-LAST:event_exitActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.setState(ICONIFIED);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void homeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_homeMouseClicked

    private void homeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeActionPerformed
        // TODO add your handling code here:
        new Home().show();
        this.setVisible(false);
    }//GEN-LAST:event_homeActionPerformed

    private void printUlangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printUlangMouseClicked
        // TODO add your handling code here:
        if((namaKonsumen.getText().equals("")) || 
           (totBayar.getText().equals(""))) {
            JOptionPane.showMessageDialog(this,"Silahkan Lakukan Transaksi Terlebih Dahulu");
        } else {
            try{
                String reportName = "src/com/report/transaksi.jasper";
                JasperPrint jPrint = JasperFillManager.fillReport(reportName, null, Koneksi.getKoneksi());
                JasperViewer.viewReport(jPrint, false);
                
                // PrintReportToPrinter(jPrint);
            }catch(JRException e){
                JOptionPane.showMessageDialog(null,e);
            }
        }
    }//GEN-LAST:event_printUlangMouseClicked

    private void printUlangMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printUlangMouseEntered
        // TODO add your handling code here:
        printUlang.setBackground(new Color(250,178,40));
        printUlang.setOpaque(true);
    }//GEN-LAST:event_printUlangMouseEntered

    private void printUlangMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printUlangMouseExited
        // TODO add your handling code here:
        printUlang.setBackground(new Color(14,53,117));
    }//GEN-LAST:event_printUlangMouseExited

    private void deleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteMouseClicked
        // TODO add your handling code here:
        if(namaKonsumen.getText().equals("") || 
            namaBarang.getText().equals("") || 
            tipeBarang.getText().equals("") ||
            ukuranBarang.getText().equals("") ||
            jumlahBarang.getText().equals("") ||
            hargaBarang.getText().equals("")){
            JOptionPane.showMessageDialog(this,"Silahkan Pilih Data Terlebih Dahulu Pada Tabel");
        }
        
        else {
            int option = JOptionPane.YES_NO_OPTION;
            int optionconfirm = JOptionPane.showConfirmDialog(this, "Apa Anda Yakin Ingin Menghapus Transaksi?", "Konfirmasi", option);
            if(optionconfirm == 0) {
                try {
                    con = Koneksi.getKoneksi();
                    String sql = "DELETE FROM transaksi WHERE id='"+tumbal_id.getText()+"'";
                    st = con.createStatement();
                    st.execute(sql);
                    
                    JOptionPane.showMessageDialog(this, "Data Transaksi Berhasil Dihapus");
                    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                    model.setRowCount(0);
                    data();
                    total_bayar();
                    clear();
                    namaKonsumen.requestFocus();
                }
                catch (HeadlessException | SQLException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }
            }
        }
    }//GEN-LAST:event_deleteMouseClicked

    private void deleteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteMouseEntered
        // TODO add your handling code here:
        delete.setBackground(new Color(250,178,40));
        delete.setOpaque(true);
    }//GEN-LAST:event_deleteMouseEntered

    private void deleteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteMouseExited
        // TODO add your handling code here:
        delete.setBackground(new Color(14,53,117));
    }//GEN-LAST:event_deleteMouseExited

    private void simpanPrintMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_simpanPrintMouseClicked
        // TODO add your handling code here:
        if((namaKonsumen.getText().equals("")) || 
           (totBayar.getText().equals(""))) {
            JOptionPane.showMessageDialog(this,"Silahkan Lakukan Transaksi Terlebih Dahulu");
        } else {
            simpan_transaksi();
            try{
                String reportName = "src/com/report/transaksi.jasper";
                JasperPrint jPrint = JasperFillManager.fillReport(reportName, null, Koneksi.getKoneksi());
                JasperViewer.viewReport(jPrint, false);
                
                // PrintReportToPrinter(jPrint);
            }catch(JRException e){
                JOptionPane.showMessageDialog(null,e);
            }
            simpanPrint.setVisible(false);
            simpanTransaksi.setVisible(false);
        }
    }//GEN-LAST:event_simpanPrintMouseClicked

    private void simpanPrintMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_simpanPrintMouseEntered
        // TODO add your handling code here:
        simpanPrint.setBackground(new Color(250,178,40));
        simpanPrint.setOpaque(true);
    }//GEN-LAST:event_simpanPrintMouseEntered

    private void simpanPrintMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_simpanPrintMouseExited
        // TODO add your handling code here:
        simpanPrint.setBackground(new Color(14,53,117));
    }//GEN-LAST:event_simpanPrintMouseExited

    private void resetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resetMouseClicked
        // TODO add your handling code here:
        if((!namaKonsumen.getText().equals("")) || 
           (!namaBarang.getText().equals("")) || 
           (!tipeBarang.getText().equals("")) ||
           (!ukuranBarang.getText().equals("")) ||
           (!jumlahBarang.getText().equals("")) ||
           (!hargaBarang.getText().equals(""))) {
  
            int option = JOptionPane.YES_NO_OPTION;
            int optionconfirm = JOptionPane.showConfirmDialog(this, "Apa Anda Yakin Ingin Membersihkan Transaksi?", "Konfirmasi", option);
            if(optionconfirm == 0) {
                reset();
                try {
                    con = Koneksi.getKoneksi();
                    String sql = "TRUNCATE TABLE transaksi";
                    st = con.createStatement();
                    st.execute(sql);

                    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                    model.setRowCount(0);
                    data();
                    increment();
                    tampilWaktu();
                    namaKonsumen.setEditable(true);
                    namaKonsumen.requestFocus();
                    Kunci_field();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }
                
                simpanTransaksi.setVisible(true);
                simpanPrint.setVisible(true);
            }
        }
    }//GEN-LAST:event_resetMouseClicked

    private void resetMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resetMouseEntered
        // TODO add your handling code here:
        reset.setBackground(new Color(250,178,40));
        reset.setOpaque(true);
    }//GEN-LAST:event_resetMouseEntered

    private void resetMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resetMouseExited
        // TODO add your handling code here:
        reset.setBackground(new Color(14,53,117));
    }//GEN-LAST:event_resetMouseExited

    private void simpanTransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_simpanTransaksiMouseClicked
        // TODO add your handling code here:
        if((namaKonsumen.getText().equals("")) || 
           (totBayar.getText().equals(""))) {
            JOptionPane.showMessageDialog(this,"Silahkan Lakukan Transaksi Terlebih Dahulu");
        } else {
            simpan_transaksi();
            simpanTransaksi.setVisible(false);
            simpanPrint.setVisible(false);
        }
    }//GEN-LAST:event_simpanTransaksiMouseClicked

    private void simpanTransaksiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_simpanTransaksiMouseEntered
        // TODO add your handling code here:
        simpanTransaksi.setBackground(new Color(250,178,40));
        simpanTransaksi.setOpaque(true);
    }//GEN-LAST:event_simpanTransaksiMouseEntered

    private void simpanTransaksiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_simpanTransaksiMouseExited
        // TODO add your handling code here:
        simpanTransaksi.setBackground(new Color(14,53,117));
    }//GEN-LAST:event_simpanTransaksiMouseExited

    private void hargaBarangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hargaBarangKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==KeyEvent.VK_ENTER) {
            if(namaKonsumen.getText().equals("") || 
                namaBarang.getText().equals("") || 
                tipeBarang.getText().equals("") ||
                ukuranBarang.getText().equals("") ||
                jumlahBarang.getText().equals("") ||
                hargaBarang.getText().equals("")){
            JOptionPane.showMessageDialog(this,"Ada Data Yang Masih Kosong");
            } else {
                subtot_bayar();
                try {
                    con = Koneksi.getKoneksi();
                    String sql = "INSERT INTO transaksi (no_pemesanan, tgl_pemesanan, nama_konsumen, telp_konsumen, alamat_konsumen, "
                        + "nama_barang, tipe_barang, ukuran_barang, jumlah_barang, harga_barang, subtotal_harga)"
                        + "VALUES ('"+noPemesanan.getText()+"','"
                        +tanggal.getText()+"','"
                        +namaKonsumen.getText()+"','"
                        +telpKonsumen.getText()+"','"
                        +alamat.getText()+"','"
                        +namaBarang.getText()+"','"
                        +tipeBarang.getText()+"','"
                        +ukuranBarang.getText()+"','"
                        +jumlahBarang.getText()+"','"
                        +hargaBarang.getText()+"','"
                        +tumbal_subtotal.getText()+"')";
                    st = con.createStatement();
                    st.execute(sql);
                    
                    // JOptionPane.showMessageDialog(this, "Data Berhasil Ditambahkan");
                    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                    model.setRowCount(0);
                    data();
                    clear();
                    total_bayar();
                    namaKonsumen.setEditable(false);
                    namaBarang.requestFocus();
                } catch (HeadlessException | SQLException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }
            }          
        }
    }//GEN-LAST:event_hargaBarangKeyPressed

    private void namaKonsumenKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_namaKonsumenKeyReleased
        // TODO add your handling code here:
        String nama_kon = namaKonsumen.getText();
        String telp_kon, alamat_kon ;
        try{
            con = Koneksi.getKoneksi();
            String sql1 = "SELECT * FROM pelanggan WHERE nama_konsumen = '"
                    +nama_kon+"'";
            st = con.createStatement();
            rs = st.executeQuery(sql1);
            if(rs.next()){
                telp_kon = rs.getString("telp_konsumen");
                alamat_kon = rs.getString("alamat_konsumen");

                namaKonsumen.setText(nama_kon);
                telpKonsumen.setText(telp_kon);
                telpKonsumen.setForeground(Color.black);
                alamat.setText(alamat_kon);
                alamat.setForeground(Color.black);
                Buka_field();
            } else {
                telpKonsumen.setText("Data Pelanggan Tidak Ditemukan");
                telpKonsumen.setForeground(Color.red);
                alamat.setText("Data Pelanggan Tidak Ditemukan");
                alamat.setForeground(Color.red);
                Kunci_field();
            }
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_namaKonsumenKeyReleased
    public void Kunci_field(){
        namaBarang.setEditable(false);
        tipeBarang.setEditable(false);
        ukuranBarang.setEditable(false);
        jumlahBarang.setEditable(false);
        hargaBarang.setEditable(false);
    }
    public void Buka_field(){
        namaBarang.setEditable(true);
        tipeBarang.setEditable(true);
        ukuranBarang.setEditable(true);
        jumlahBarang.setEditable(true);
        hargaBarang.setEditable(true);
    }
    private void namaKonsumenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_namaKonsumenKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_namaKonsumenKeyTyped

    private void namaKonsumenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_namaKonsumenKeyPressed
        // TODO add your handling code here:
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                namaBarang.requestFocus();
                break;
            case KeyEvent.VK_TAB:
                namaBarang.requestFocus();
                break;
        }
    }//GEN-LAST:event_namaKonsumenKeyPressed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int i = jTable1.getSelectedRow();
        TableModel model = jTable1.getModel();
        tumbal_id.setText(model.getValueAt(i, 0).toString());
        namaBarang.setText(model.getValueAt(i, 1).toString());
        tipeBarang.setText(model.getValueAt(i, 2).toString());
        ukuranBarang.setText(model.getValueAt(i, 3).toString());
        hargaBarang.setText(model.getValueAt(i, 4).toString());
        jumlahBarang.setText(model.getValueAt(i, 5).toString());
    }//GEN-LAST:event_jTable1MouseClicked

    private void jumlahBarangKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jumlahBarangKeyTyped
        // TODO add your handling code here:
        char karakter = evt.getKeyChar();
        if(!Character.isDigit(karakter)){
            //getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_jumlahBarangKeyTyped

    private void hargaBarangKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hargaBarangKeyTyped
        // TODO add your handling code here:
        char karakter = evt.getKeyChar();
        if(!Character.isDigit(karakter)){
            //getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_hargaBarangKeyTyped

    private void namaBarangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_namaBarangKeyPressed
        // TODO add your handling code here:
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                tipeBarang.requestFocus();
                break;
            case KeyEvent.VK_TAB:
                tipeBarang.requestFocus();
                break;
        }
    }//GEN-LAST:event_namaBarangKeyPressed

    private void tipeBarangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tipeBarangKeyPressed
        // TODO add your handling code here:
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                ukuranBarang.requestFocus();
                break;
            case KeyEvent.VK_TAB:
                ukuranBarang.requestFocus();
                break;
        }
    }//GEN-LAST:event_tipeBarangKeyPressed

    private void ukuranBarangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ukuranBarangKeyPressed
        // TODO add your handling code here:
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                jumlahBarang.requestFocus();
                break;
            case KeyEvent.VK_TAB:
                jumlahBarang.requestFocus();
                break;
        }
    }//GEN-LAST:event_ukuranBarangKeyPressed

    private void jumlahBarangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jumlahBarangKeyPressed
        // TODO add your handling code here:
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                hargaBarang.requestFocus();
                break;
            case KeyEvent.VK_TAB:
                hargaBarang.requestFocus();
                break;
        }
    }//GEN-LAST:event_jumlahBarangKeyPressed

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
            java.util.logging.Logger.getLogger(OrderMasuk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OrderMasuk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OrderMasuk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OrderMasuk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OrderMasuk().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField alamat;
    private javax.swing.JPanel delete;
    private javax.swing.JButton exit;
    private javax.swing.JTextField hargaBarang;
    private javax.swing.JButton home;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jumlahBarang;
    private javax.swing.JTextField namaBarang;
    private javax.swing.JTextField namaKonsumen;
    private javax.swing.JTextField noPemesanan;
    private javax.swing.JPanel printUlang;
    private javax.swing.JPanel reset;
    private javax.swing.JPanel simpanPrint;
    private javax.swing.JPanel simpanTransaksi;
    private javax.swing.JTextField tanggal;
    private javax.swing.JTextField telpKonsumen;
    private javax.swing.JTextField tipeBarang;
    private javax.swing.JTextField totBayar;
    private javax.swing.JTextField tumbal_id;
    private javax.swing.JTextField tumbal_subtotal;
    private javax.swing.JTextField ukuranBarang;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
