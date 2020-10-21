/*
 * Copyright 2020 Sander Verdonschot <sander.verdonschot at gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.mangara.puzzles.gui;

import com.github.mangara.puzzles.data.Logiquiz;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class EditGroupsDialog extends javax.swing.JDialog {

    private List<List<String>> groups = null;

    public EditGroupsDialog(java.awt.Frame parent) {
        super(parent, true);
        initComponents();
    }

    public void copyGroupsFrom(Logiquiz puzzle) {
        this.groups = puzzle.getGroups();
        updateGroupTabs();
    }

    private void saveButtonActionPerformed(ActionEvent evt) {
        // TODO: actually save
        setVisible(false);
    }

    private void cancelButtonActionPerformed(ActionEvent evt) {
        setVisible(false);
    }

    private JTabbedPane groupTabPane;
    
    private void initComponents() {
        setTitle("Edit groups");
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        initGroupPane();
        initButtonPanel();

        pack();
    }

    private void initGroupPane() {
        groupTabPane = new JTabbedPane();
        getContentPane().add(groupTabPane);
    }
    
    private void initButtonPanel() {
        JButton saveButton = new JButton("Save groups");
        saveButton.addActionListener((e) -> saveButtonActionPerformed(e));
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener((e) -> cancelButtonActionPerformed(e));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(saveButton);
        getContentPane().add(buttonPanel);
    }

    private void updateGroupTabs() {
        groupTabPane.removeAll();
        
        for (int i = 0; i < groups.size(); i++) {
            List<String> group = groups.get(i);
            groupTabPane.addTab(String.format("Group %d", i + 1), generateGroupPanel(group));
        }
        
        groupTabPane.setSelectedIndex(0);
        pack();
    }

    private Component generateGroupPanel(List<String> group) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        
        for (String entry : group) {
            JTextField textField = new JTextField(entry, 25);
            textField.setMaximumSize(textField.getPreferredSize());
            panel.add(textField);
        }
        
        panel.add(Box.createVerticalGlue());
        return new JScrollPane(panel);
    }

}
