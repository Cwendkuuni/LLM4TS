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

    private AdminApp adminApp;
    private ControlPanel controlPanel;
    private UpdateUserPanel updateUserPanel;
    private Group group;
    private User user;
    private Vector<User> users;

    @Before
    public void setUp() {
        adminApp = new AdminApp();
        controlPanel = new ControlPanel();
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        group = new Group("Group1");
        users = new Vector<>();
        User user1 = new User("User1", "user1@example.com", "12345", "User1 Title", true, true, true);
        User user2 = new User("User2", "user2@example.com", null, "User2 Title", false, false, false);
        users.add(user1);
        users.add(user2);
        group.setUsers(users);
        adminApp.setGroups(new Vector<>(Arrays.asList(group)));
    }

    @Test
    public void testUpdateUserPanelConstructor() {
        assertNotNull(updateUserPanel);
    }

    @Test
    public void testUpdateModel() {
        updateUserPanel.updateModel();
        assertNotNull(updateUserPanel.jComboBox1.getModel());
        assertEquals(1, updateUserPanel.jComboBox1.getModel().getSize());
        assertSame(group, updateUserPanel.jComboBox1.getSelectedItem());
        assertNotNull(updateUserPanel.jComboBox2.getModel());
        assertEquals(1, updateUserPanel.jComboBox2.getModel().getSize());
        assertSame(user1, updateUserPanel.jComboBox2.getSelectedItem());
    }

    @Test
    public void testInitComponents() {
        updateUserPanel.initComponents();
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
        assertNotNull(updateUserPanel.jButton4);
        assertNotNull(updateUserPanel.jLabel5);
        assertNotNull(updateUserPanel.jTextField1);
        assertNotNull(updateUserPanel.jTextField4);
        assertNotNull(updateUserPanel.jLabel6);
    }

    @Test
    public void testJButton1ActionPerformed() {
        updateUserPanel.jButton1ActionPerformed(new ActionEvent(new Object(), 0, ""));
        assertTrue(updateUserPanel.isVisible() == false);
        updateUserPanel.reset();
    }

    @Test
    public void testJButton2ActionPerformed_WithValidUser() {
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 0, ""));
        assertEquals(0, users.size());
        assertTrue(JOptionPane.showOptionDialog(updateUserPanel, "User Deleted Successfully!", "Information Dialog", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE) > 0);
    }

    @Test
    public void testJButton2ActionPerformed_WithInvalidUserSelection() {
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(-1);
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 0, ""));
        assertTrue(JOptionPane.showOptionDialog(updateUserPanel, "Can't identify selected User!", "Error Dialog", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE) > 0);
        assertEquals(users.size(), 2);
    }

    @Test
    public void testJButton3ActionPerformed() {
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField2.setText("new@email.com");
        updateUserPanel.jButton3ActionPerformed(new ActionEvent(new Object(), 0, ""));
        assertEquals(user1.getEmail(), "user1@example.com");
        assertTrue(JOptionPane.showOptionDialog(updateUserPanel, "User Password Reset Successfully!", "Information Dialog", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE) > 0);
        assertEquals(user1.getEmail(), "new@email.com");
    }

    @Test(expected = Exception.class)
    public void testJButton3ActionPerformed_WithoutUserSelection() {
        updateUserPanel.jButton3ActionPerformed(new ActionEvent(new Object(), 0, ""));
    }

    @Test
    public void testJButton4ActionPerformed() {
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField1.setText("newUserName");
        updateUserPanel.jTextField2.setText("newUserEmail@example.com");
        updateUserPanel.jTextField3.setText("newPhoneNumber");
        updateUserPanel.jTextField4.setText("newUserTitle");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, ""));
        String expectedUserName = "newUserName";
        String expectedUserEmail = "newUserEmail@example.com";
        String expectedUserTitle = "newUserTitle";
        boolean expectedShowMyStatus = true;
        boolean expectedOnlyStartChat = false;
        boolean expectedCanJoinChatRoom = true;
        assertEquals(user1.getName(), expectedUserName);
        assertEquals(user1.getEmail(), expectedUserEmail);
        assertEquals(user1.getJobTitle(), expectedUserTitle);
        assertEquals(user1.isShowMyStatus(), expectedShowMyStatus);
        assertEquals(user1.isOnlyStartChat(), expectedOnlyStartChat);
        assertEquals(user1.isCanJoinChatRoom(), expectedCanJoinChatRoom);
        assertTrue(JOptionPane.showOptionDialog(updateUserPanel, "User Updated Successfully!", "Information Dialog", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE) > 0);
    }

    @Test(expected = Exception.class)
    public void testJButton4ActionPerformed_WithInvalidEmail() {
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField1.setText("newUserName");
        updateUserPanel.jTextField2.setText("user1@example.com"); // Same as existing user email
        updateUserPanel.jTextField3.setText("newPhoneNumber");
        updateUserPanel.jTextField4.setText("newUserTitle");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, ""));
    }

    @Test
    public void testJComboBox1ItemStateChanged() {
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(new Object(), 0, 1));
        assertEquals(1, updateUserPanel.jComboBox2.getModel().getSize());
        assertSame(user1, updateUserPanel.jComboBox2.getSelectedItem());
    }

    @Test
    public void testJComboBox2ItemStateChanged() {
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(new Object(), 0, 1));
        updateUserPanel.jComboBox2ItemStateChanged(new ItemEvent(new Object(), 0, 1));
        assertEquals(user1.getName(), updateUserPanel.jTextField1.getText());
        assertEquals(user1.getEmail(), updateUserPanel.jTextField2.getText());
        assertEquals(user1.getDirectPhone(), updateUserPanel.jTextField3.getText());
        assertEquals(user1.getJobTitle(), updateUserPanel.jTextField4.getText());
        assertEquals(user1.isShowMyStatus(), updateUserPanel.jCheckBox1.isSelected());
        assertEquals(user1.isOnlyStartChat(), updateUserPanel.jCheckBox2.isSelected());
        assertEquals(user1.isCanJoinChatRoom(), updateUserPanel.jCheckBox3.isSelected());
    }
}