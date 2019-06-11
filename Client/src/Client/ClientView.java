package Client;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;

/**
 *
 * @author danie
 */
public class ClientView extends javax.swing.JFrame {
    JFileChooser filechooser;
    private Client client;
    private File file;
    
    public ClientView() 
    {
        initComponents();
        setAtribbutes();
    }
    
    private void setAtribbutes()
    {
        noisePercent.setMinimum(0);
        noisePercent.setMaximum(10);
        
        
        filechooser = new JFileChooser();
        filechooser.setDialogTitle("Escolha o arquivo");
        filechooser.setApproveButtonText("Selecionar");
        filechooser.setDialogType(JFileChooser.OPEN_DIALOG);
        filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        try{
            IP.setText(InetAddress.getLocalHost().getHostAddress()); 
        }catch (UnknownHostException ex) { 
            System.out.println(ex); 
        }
    }
    
    public void setLimitProgressBar(int min, int max)
    {
        progess.setMinimum(min);
        progess.setMaximum(max);
    }
    
    public void setValueProgressBar(int value)
    {
        progess.setValue(value);
    }
    
    public void addLog(String log)
    {
        LocalDateTime ldt =  LocalDateTime.now();
        String date = "[" + ldt.getHour() + ":" + ldt.getMinute() + ":" + ldt.getSecond() + "]  ";
        logs.append(date + log + "\n");
    }
    
    public int getNoisePercent()
    {
        return noisePercent.getValue();
    }
    
    public boolean sendWithNoise()
    {
        return noise.isSelected() == true;
    }
    
    public String getFileName()
    {
        return filename.getText() + fileextension.getText();
    }
    
    public File getFile()
    {
        if (file == null)
            file = EscolherArquivo();
        
        return file;
    }
    
    public File EscolherArquivo()
    {
        int resultado = filechooser.showOpenDialog(filechooser);
        if (resultado == JFileChooser.CANCEL_OPTION)
        {
            filechooser.enable(false);
            return null ;
        }

        File choosedFile = filechooser.getSelectedFile();
        String[] name = choosedFile.getName().split((Pattern.quote(".")));

        path.setText(choosedFile.getParent());
        filesize.setText(String.format("%.2f %s", (choosedFile.length() / 1024.0 / 1024.0), "MB"));
        filename.setText(choosedFile.getName().replace("."+name[name.length-1], ""));
        fileextension.setText("." + name[name.length-1]);

        return choosedFile;
    }
 
    public String getIP()
    {
        return IP.getText();
    }
    
    public int getPorta()
    {
        return Integer.parseInt(porta.getText());
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        IP = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        porta = new javax.swing.JTextField();
        ping = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        sendFile = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        fileextension = new javax.swing.JLabel();
        chosefile = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        filesize = new javax.swing.JLabel();
        noise = new javax.swing.JCheckBox();
        percetnoise = new javax.swing.JLabel();
        noisePercent = new javax.swing.JSlider();
        progess = new javax.swing.JProgressBar();
        path = new javax.swing.JTextField();
        filename = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        logs = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 255, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Conexão"));

        jLabel1.setText("IP:");

        IP.setText("192.168.0.15");

        jLabel2.setText("Porta:");

        porta.setText("12345");

        ping.setText("Ping");
        ping.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ping, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(IP, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(porta, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(IP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(porta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(ping)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados"));

        sendFile.setText("Enviar");
        sendFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendFileActionPerformed(evt);
            }
        });

        jLabel3.setText("Nome:");

        chosefile.setText("Selecionar arquivo");
        chosefile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chosefileActionPerformed(evt);
            }
        });

        jLabel6.setText("Tamanho =~");

        noise.setText("Ruido");

        percetnoise.setText("10%");

        noisePercent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noisePercentMouseReleased(evt);
            }
        });

        path.setEditable(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sendFile, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(filename)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileextension, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8))
                    .addComponent(chosefile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(noise)
                        .addGap(18, 18, 18)
                        .addComponent(noisePercent, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(percetnoise))
                    .addComponent(progess, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(filesize)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(path))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chosefile)
                .addGap(18, 18, 18)
                .addComponent(path, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(fileextension)
                    .addComponent(filename, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(filesize))
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(noise)
                        .addComponent(percetnoise))
                    .addComponent(noisePercent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(progess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sendFile)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Logs de envio"));

        logs.setEditable(false);
        logs.setColumns(20);
        logs.setRows(5);
        jScrollPane1.setViewportView(logs);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pingActionPerformed
        long start = System.currentTimeMillis();
        new Client(this, "PING").start();
        long end = System.currentTimeMillis();    
        
        this.addLog("Ping in " + (end - start) + " ms");
    }//GEN-LAST:event_pingActionPerformed

    private void chosefileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chosefileActionPerformed
        file = EscolherArquivo();
    }//GEN-LAST:event_chosefileActionPerformed

    private void sendFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendFileActionPerformed
        new Client(this, "SEND").start();
    }//GEN-LAST:event_sendFileActionPerformed

    private void noisePercentMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_noisePercentMouseReleased
        progess.setValue(noisePercent.getValue());
        percetnoise.setText(noisePercent.getValue()+"%");
    }//GEN-LAST:event_noisePercentMouseReleased
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField IP;
    private javax.swing.JButton chosefile;
    private javax.swing.JLabel fileextension;
    private javax.swing.JTextField filename;
    private javax.swing.JLabel filesize;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea logs;
    private javax.swing.JCheckBox noise;
    private javax.swing.JSlider noisePercent;
    private javax.swing.JTextField path;
    private javax.swing.JLabel percetnoise;
    private javax.swing.JButton ping;
    private javax.swing.JTextField porta;
    private javax.swing.JProgressBar progess;
    private javax.swing.JButton sendFile;
    // End of variables declaration//GEN-END:variables
}
