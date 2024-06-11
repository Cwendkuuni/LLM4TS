package osa.ora.server.admin.ui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class UpdateUserPanelTest {

    private UpdateUserPanel updateUserPanel;
    private AdminApp adminApp;
    private ControlPanel controlPanel;
    private Group group;
    private User user, user2;

    @Before
    public void setUp() {
        adminApp = new AdminApp();
        controlPanel = new ControlPanel();
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        group = new Group("Group1");
        user = new User("User1", "user1@example.com", "1234567890", "User1 Title", true, true, true);
        user2 = new User("User2", "user2@example.com", null, "User2 Title", false, false, false);
        group.addUser(user);
        group.addUser(user2);
        adminApp.setGroups(new Vector<Group>());
        adminApp.getGroups().add(group);
    }

    @Test
    public void testUpdateUserPanelConstructor() {
        assertNotNull(updateUserPanel);
    }

    @Test
    public void testUpdateModel() {
        updateUserPanel.updateModel();
        assertEquals(2, updateUserPanel.jComboBox2.getModel().getSize());
        assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().getName());
    }

    @Test
    public void testInitComponents() {
        // Basic functionality test
        updateUserPanel.initComponents();
        assertNotNull(updateUserPanel.jLabel1);
        assertNotNull(updateUserPanel.jLabel2);
        assertNotNull(updateUserPanel.jCheckBox1);
        assertNotNull(updateUserPanel.jComboBox1);

        // Edge case and exception handling tests
        try {
            updateUserPanel.initComponents();
            fail("Exception should be thrown as the constructor has already been called");
        } catch (Exception e) {
            // Expected exception
        }
    }

    @Test
    public void testJButton1ActionPerformed() {
        updateUserPanel.jButton1ActionPerformed(new ActionEvent(new JButton(), 0, "Cancel"));
        assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible() == false);
        assertEquals("", updateUserPanel.jTextField1.getText());
        assertEquals("", updateUserPanel.jTextField2.getText());
        assertEquals("", updateUserPanel.jTextField3.getText());
        assertEquals("", updateUserPanel.jTextField4.getText());
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertFalse(updateUserPanel.jCheckBox3.isSelected());
    }

    @Test
    public void testJButton2ActionPerformed() {
        updateUserPanel.jComboBox1.setSelectedItem("Group1");
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(new JButton(), 0, "Delete"));

        // Test if user is deleted successfully
        assertEquals(1, group.getUsers().size());
        assertFalse(group.getUsers().contains(user));

        // Test if message dialog is shown
        assertTrue(JOptionPane.showConfirmDialog(updateUserPanel, "User Deleted Successfully!") == JOptionPane.OK_OPTION);

        // Test if exception is handled properly
        updateUserPanel.jComboBox2.setSelectedItem(null);
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(new JButton(), 0, "Delete"));
        assertTrue(JOptionPane.showConfirmDialog(updateUserPanel, "Can't identify selected User!") == JOptionPane.ERROR_MESSAGE);
    }

    @Test
    public void testJButton3ActionPerformed() {
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jButton3ActionPerformed(new ActionEvent(new JButton(), 0, "Reset Password"));

        // Test if password is reset successfully
        assertNotNull(user.getPassword());
        assertEquals("", user.getPassword());

        // Test if message dialog is shown
        assertTrue(JOptionPane.showConfirmDialog(updateUserPanel, "User Password Reset Successfully!") == JOptionPane.OK_OPTION);

        // Test if exception is handled properly
        updateUserPanel.jComboBox2.setSelectedItem(null);
        updateUserPanel.jButton3ActionPerformed(new ActionEvent(new JButton(), 0, "Reset Password"));
        assertTrue(JOptionPane.showConfirmDialog(updateUserPanel, "Can't identify selected User!") == JOptionPane.ERROR_MESSAGE);
    }

    @Test
    public void testJButton4ActionPerformed() {
        updateUserPanel.jComboBox1.setSelectedItem("Group1");
        updateUserPanel.jComboBox2.setSelectedItem(user);

        // Test if user is updated successfully
        updateUserPanel.jTextField1.setText("TestUser1");
        updateUserPanel.jTextField2.setText("testuser1@example.com");
        updateUserPanel.jTextField3.setText("1234567891");
        updateUserPanel.jTextField4.setText("TestUser1 Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new JButton(), 0, "Update"));

        assertEquals("TestUser1", user.getName());
        assertEquals("testuser1@example.com", user.getEmail());
        assertEquals("1234567891", user.getDirectPhone());
        assertTrue(user.isShowMyStatus());
        assertTrue(user.isOnlyStartChat());
        assertTrue(user.isCanJoinChatRoom());

        // Test if message dialog is shown
        assertTrue(JOptionPane.showConfirmDialog(updateUserPanel, "User Updated Successfully!") == JOptionPane.OK_OPTION);

        // Test if exception is handled properly
        updateUserPanel.jComboBox2.setSelectedItem(null);
        updateUserPanel.jTextField2.setText("user1@example.com");
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new JButton(), 0, "Update"));
        assertTrue(JOptionPane.showConfirmDialog(updateUserPanel, "Server Error!") == JOptionPane.ERROR_MESSAGE);
    }
}