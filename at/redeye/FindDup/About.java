/*
 * About.java
 *
 * Created on 14. Juli 2009, 20:39
 */

package at.redeye.FindDup;

import at.redeye.FrameWork.base.BaseDialog;
import at.redeye.FrameWork.base.Root;

/**
 *
 * @author  martin
 */
public class About extends BaseDialog {

    /** Creates new form About */
    public About(Root root) 
    {
        super(root,"Über");
        initComponents();
        
        jLVersion.setText(MlM("Version") + " " + Version.getVersion());
        jLTitle.setText(MlM("Über") + " " + MlM(root.getAppTitle()));
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLTitle = new javax.swing.JLabel();
        jBCancel = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLVersion = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLTitle.setFont(new java.awt.Font("Dialog", 1, 18));
        jLTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLTitle.setText("Über Redeye Barcode Editor");

        jBCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/at/redeye/FrameWork/base/resources/icons/fileclose.gif"))); // NOI18N
        jBCancel.setText("Schließen");
        jBCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBCancelActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/at/redeye/FrameWork/base/resources/pictures/redeye.png"))); // NOI18N

        jLVersion.setFont(new java.awt.Font("Dialog", 1, 14));
        jLVersion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLVersion.setText("Version");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jBCancel)
                        .addContainerGap())
                    .addComponent(jLVersion, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLTitle)
                .addGap(4, 4, 4)
                .addComponent(jLVersion)
                .addGap(22, 22, 22)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(jBCancel)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jBCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCancelActionPerformed
    if( canClose() )    
        close();
}//GEN-LAST:event_jBCancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBCancel;
    private javax.swing.JLabel jLTitle;
    private javax.swing.JLabel jLVersion;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

}
