/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.netbeans.modules.hibernate.loaders.cfg.multiview;

import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 *
 * @author  Dongmei Cao
 */
public class CollectionCachePanel extends javax.swing.JPanel {
    
    // TODO: hard code here for now
    private static final String[] usageTypes = new String[] {
							"read-only", // NOI18N
							"read-write", // NOI18N
							"nonstrict-read-write", // NOI18N
							"transactional" // NOI18N
						};
    
    /** Creates new form ClassCachePanel */
    public CollectionCachePanel() {
        initComponents();
        usageComboBox.setModel( new DefaultComboBoxModel(usageTypes) );
    }
    
    public void initValues( String className, String region, String usage ) {
        this.classTextField.setText( className );
        this.regionTextField.setText( region );
        this.usageComboBox.setSelectedItem( usage );
    }
    
    public void addBrowseClassActionListener( ActionListener listener ) {
        this.browseButton.addActionListener( listener );
    }
    
    public JTextField getClassTextField() {
        return this.classTextField;
    }
    
    public JTextField getRegionTextField() {
        return this.regionTextField;
    }
    
    public JComboBox getUsageComboBox() {
        return this.usageComboBox;
    }
    
    public String getClassName() {
        return this.classTextField.getText().trim();
    }
    
    public String getRegion() {
        return this.regionTextField.getText().trim();
    }
    
    public String getUsage() {
        return (String)this.usageComboBox.getSelectedItem();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        classLabel = new javax.swing.JLabel();
        classTextField = new javax.swing.JTextField();
        regionLabel = new javax.swing.JLabel();
        regionTextField = new javax.swing.JTextField();
        usageLabel = new javax.swing.JLabel();
        usageComboBox = new javax.swing.JComboBox();
        browseButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        classLabel.setLabelFor(classTextField);
        org.openide.awt.Mnemonics.setLocalizedText(classLabel, org.openide.util.NbBundle.getMessage(CollectionCachePanel.class, "ClassCachePanel.classLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 12, 0, 0);
        add(classLabel, gridBagConstraints);
        classLabel.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(CollectionCachePanel.class, "ClassCachePanel.classLabel.text")); // NOI18N
        classLabel.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(CollectionCachePanel.class, "ClassCachePanel.classLabel.text")); // NOI18N

        classTextField.setText(org.openide.util.NbBundle.getMessage(CollectionCachePanel.class, "ClassCachePanel.classTextField.text")); // NOI18N
        classTextField.setPreferredSize(new java.awt.Dimension(200, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 12, 0, 0);
        add(classTextField, gridBagConstraints);

        regionLabel.setLabelFor(regionTextField);
        org.openide.awt.Mnemonics.setLocalizedText(regionLabel, org.openide.util.NbBundle.getMessage(CollectionCachePanel.class, "ClassCachePanel.regionLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 12, 0, 0);
        add(regionLabel, gridBagConstraints);
        regionLabel.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(CollectionCachePanel.class, "ClassCachePanel.regionLabel.text")); // NOI18N
        regionLabel.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(CollectionCachePanel.class, "ClassCachePanel.regionLabel.text")); // NOI18N

        regionTextField.setText(org.openide.util.NbBundle.getMessage(CollectionCachePanel.class, "ClassCachePanel.regionTextField.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 12, 0, 0);
        add(regionTextField, gridBagConstraints);

        usageLabel.setLabelFor(usageComboBox);
        org.openide.awt.Mnemonics.setLocalizedText(usageLabel, org.openide.util.NbBundle.getMessage(CollectionCachePanel.class, "ClassCachePanel.usageLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 12, 0, 0);
        add(usageLabel, gridBagConstraints);
        usageLabel.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(CollectionCachePanel.class, "ClassCachePanel.usageLabel.text")); // NOI18N
        usageLabel.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(CollectionCachePanel.class, "ClassCachePanel.usageLabel.text")); // NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 12, 0, 0);
        add(usageComboBox, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(browseButton, org.openide.util.NbBundle.getMessage(CollectionCachePanel.class, "ClassCachePanel.browseButton.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 12, 0, 12);
        add(browseButton, gridBagConstraints);
        browseButton.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(CollectionCachePanel.class, "ClassCachePanel.browseButton.text")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.JLabel classLabel;
    private javax.swing.JTextField classTextField;
    private javax.swing.JLabel regionLabel;
    private javax.swing.JTextField regionTextField;
    private javax.swing.JComboBox usageComboBox;
    private javax.swing.JLabel usageLabel;
    // End of variables declaration//GEN-END:variables
    
}
