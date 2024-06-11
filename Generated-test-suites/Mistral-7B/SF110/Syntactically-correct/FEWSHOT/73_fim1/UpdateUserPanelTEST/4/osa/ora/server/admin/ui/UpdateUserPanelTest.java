package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import org.junit.Test;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

    private AdminApp adminApp;
    private ControlPanel controlPanel;
    private UpdateUserPanel updateUserPanel;

    @ org.junit.Before
    public void setUp() {
        adminApp = new AdminApp();
        controlPanel = new ControlPanel();
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        updateUserPanel.updateModel();
    }

    @Test
    public void testUpdateUserPanelComponentInitialization() {
        assertNotNull(updateUserPanel.jLabel1);
        assertNotNull(updateUserPanel.jLabel2);
        assertNotNull(updateUserPanel.jLabel3);
        assertNotNull(updateUserPanel.jCheckBox1);
        assertNotNull(updateUserPanel.jCheckBox2);
        assertNotNull(updateUserPanel.jCheckBox3);
        assertNotNull(updateUserPanel.jLabel4);
        assertNotNull(updateUserPanel.jComboBox1);
        assertNotNull(updateUserPanel.jTextField2);
        assertNotNull(updateUserPanel.jTextField3);
        assertNotNull(updateUserPanel.jButton1);
        assertNotNull(updateUserPanel.jButton2);
        assertNotNull(updateUserPanel.jComboBox2);
        assertNotNull(updateUserPanel.jButton3);
        assertNotNull(updateUserPanel.jLabel5);
        assertNotNull(updateUserPanel.jTextField1);
        assertNotNull(updateUserPanel.jTextField4);
        assertNotNull(updateUserPanel.jLabel6);
    }

    @Test
    public void testUpdateModel() {
        assertEquals(updateUserPanel.jComboBox1.getItemCount(), adminApp.getGroups().size());
        if (adminApp.getGroups().size() > 0) {
            assertEquals(updateUserPanel.jComboBox2.getItemCount(), adminApp.getGroups().get(0).getUsers().size());
            assertNotNull(updateUserPanel.jComboBox2.getItemAt(0));
        }
    }

    @Test
    public void testInitComponents() {
        assertEquals(updateUserPanel.jLabel1.getText(), "Username:");
        assertEquals(updateUserPanel.jLabel2.getText(), "Login Email:");
        assertEquals(updateUserPanel.jLabel3.getText(), "Phone:");
        assertEquals(updateUserPanel.jCheckBox1.getText(), "Show User Status To Others");
        assertEquals(updateUserPanel.jCheckBox2.getText(), "User Can Only Start Chat With Others");
        assertEquals(updateUserPanel.jCheckBox3.getText(), "Can be a Member of Chat Rooms");
        assertEquals(updateUserPanel.jLabel4.getText(), "User Group:");
        assertEquals(updateUserPanel.jComboBox1.getModel().getClass().getName(), "DefaultComboBoxModel");
        assertTrue(updateUserPanel.jComboBox1.isFocusable());
        assertTrue(updateUserPanel.jTextField2.isFocusable());
        assertTrue(updateUserPanel.jTextField3.isFocusable());
        assertTrue(updateUserPanel.jTextField1.isFocusable());
        assertTrue(updateUserPanel.jTextField4.isFocusable());
        assertEquals(updateUserPanel.jButton1.getActionCommands()[0], "Cancel");
        assertEquals(updateUserPanel.jButton2.getActionCommands()[0], "Delete");
        assertEquals(updateUserPanel.jButton3.getActionCommands()[0], "Reset Password");
        assertEquals(updateUserPanel.jButton4.getActionCommands()[0], "Update");
    }

    @Test
    public void testJButton1ActionPerformed() {
        updateUserPanel.jButton1ActionPerformed(new ActionEvent(new Object(), 1, "Cancel"));
        assertTrue(controlPanel.isVisible());
        reset();
    }

    @Test
    public void testJButton2ActionPerformed() {
        Group group = new Group();
        User user = new User();
        adminApp.addGroup(group);
        adminApp.addUser(group, user);
        updateUserPanel.jComboBox1.setSelectedItem(group);
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 1, "Delete"));
        assertEquals(adminApp.getGroups().get(0).getUsers().size(), 0);
        assertTrue(updateUserPanel.jComboBox1.getItemAt(0) instanceof Group);
        assertTrue(updateUserPanel.jComboBox2.getItemAt(0) == null);
        JOptionPane.showMessageDialog(updateUserPanel, "User Deleted Successfully!");
    }

    @Test
    public void testJButton3ActionPerformed() {
        Group group = new Group();
        User user = new User();
        adminApp.addGroup(group);
        adminApp.addUser(group, user);
        updateUserPanel.jComboBox1.setSelectedItem(group);
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jTextField2.setText("newemail@example.com");
        updateUserPanel.jButton3ActionPerformed(new ActionEvent(new Object(), 1, "Reset Password"));
        assertEquals(user.getEmail(), "newemail@example.com");
        JOptionPane.showMessageDialog(updateUserPanel, "User Password Reset Successfully!");
    }

    @Test
    public void testJButton4ActionPerformed() {
        Group group = new Group();
        User user = new User();
        adminApp.addGroup(group);
        adminApp.addUser(group, user);
        updateUserPanel.jComboBox1.setSelectedItem(group);
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jTextField1.setText("New Username");
        updateUserPanel.jTextField2.setText("newemail@example.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("Software Engineer");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 1, "Update"));
        assertEquals(user.getName(), "New Username");
        assertEquals(user.getEmail(), "newemail@example.com");
        assertEquals(user.getDirectPhone(), "1234567890");
        assertEquals(user.getJobTitle(), "Software Engineer");
        assertEquals(user.isShowMyStatus(), true);
        assertEquals(user.isOnlyStartChat(), false);
        assertEquals(user.isCanJoinChatRoom(), true);
        JOptionPane.showMessageDialog(updateUserPanel, "User Updated Successfully!");
    }

    @Test
    public void testJComboBox1ItemStateChanged() {
        updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(new Object(), 1, 1));
        Group newGroup = new Group();
        adminApp.addGroup(newGroup);
        updateUserPanel.updateModel();
        assertEquals(updateUserPanel.jComboBox2.getItemCount(), newGroup.getUsers().size());
        updateUserPanel.jComboBox1.setSelectedIndex(1);
        updateUserPanel.jComboBox2.setSelectedItem(null);
        updateUserPanel.reset();
    }

    @Test
    public void testJComboBox2ItemStateChanged() {
        Group group = new Group();
        User user = new User();
        adminApp.addGroup(group);
        adminApp.addUser(group, user);
        updateUserPanel.jComboBox1.setSelectedItem(group);
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 1, "Delete"));
        updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(new Object(), 1, 1));
        assertEquals(updateUserPanel.jComboBox2.getItemCount(), 0);
        updateUserPanel.reset();
    }

    private void reset() {
        updateUserPanel.jTextField1.setText("");
        updateUserPanel.jTextField2.setText("");
        updateUserPanel.jTextField3.setText("");
        updateUserPanel.jTextField4.setText("");
        updateUserPanel.jCheckBox1.setSelected(false);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(false);
    }
}