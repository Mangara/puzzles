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
package com.github.mangara.puzzles.gui.logiquiz;

import com.github.mangara.puzzles.data.logiquiz.Logiquiz;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class EditGroupsDialog extends javax.swing.JDialog {

    private final LogiquizDrawPanel drawPanel;
    private List<List<String>> groups;

    public EditGroupsDialog(java.awt.Frame parent, LogiquizDrawPanel drawPanel) {
        super(parent, true);
        this.drawPanel = drawPanel;
        this.groups = new ArrayList<>();
        initComponents();
    }

    public void copyGroupsFrom(Logiquiz puzzle) {
        groups = modifiableCopy(puzzle.getGroups());
        updateGroupTabs();
    }
    
    private List<List<String>> modifiableCopy(List<List<String>> groups) {
        List<List<String>> result = new ArrayList<>(groups.size());
        
        for (List<String> group : groups) {
            result.add(new ArrayList<>(group));
        }
        
        return result;
    }

    private void saveButtonActionPerformed(ActionEvent evt) {
        drawPanel.setPuzzle(new Logiquiz(groups, drawPanel.getPuzzle().getClues()));
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
            groupTabPane.addTab(String.format("Group %d", i + 1), generateGroupPanel(group, i));
        }

        groupTabPane.setSelectedIndex(0);
        pack();
    }

    private Component generateGroupPanel(List<String> group, int groupIndex) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        for (int entryIndex = 0; entryIndex < group.size(); entryIndex++) {
            String entry = group.get(entryIndex);
            JTextField textField = new JTextField(entry, 25);
            textField.setMaximumSize(textField.getPreferredSize());
            textField.getDocument().addDocumentListener(new EntryDocumentListener(groups, groupIndex, entryIndex));
            panel.add(textField);
        }

        panel.add(Box.createVerticalGlue());
        return new JScrollPane(panel);
    }

    

    private class EntryDocumentListener implements DocumentListener {

        private final List<List<String>> groups;
        private final int groupIndex;
        private final int entryIndex;

        public EntryDocumentListener(List<List<String>> groups, int groupIndex, int entryIndex) {
            this.groups = groups;
            this.groupIndex = groupIndex;
            this.entryIndex = entryIndex;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            update(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            update(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            update(e);
        }

        private void update(DocumentEvent e) {
            try {
                Document doc = e.getDocument();
                String newEntry = doc.getText(0, doc.getLength());
                groups.get(groupIndex).set(entryIndex, newEntry);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }

    }
}
