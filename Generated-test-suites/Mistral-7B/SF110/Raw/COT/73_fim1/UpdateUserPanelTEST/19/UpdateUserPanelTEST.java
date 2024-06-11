package osa.ora.server.admin.ui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {
    private UpdateUserPanel updateUserPanel;
    private AdminApp adminApp;
    private ControlPanel controlPanel;

    @Before
    public void setUp() {
        adminApp = new AdminApp();
        controlPanel = new ControlPanel();
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
    }

    @Test
    public void testUpdateModel() {
        updateUserPanel.updateModel();
        assertNotNull(updateUserPanel.jComboBox1);
        assertNotNull(updateUserPanel.jComboBox2);
    }

    @Test
    public void testInitComponents() {
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        assertNotNull(updateUserPanel.jLabel1);
        assertNotNull(updateUserPanel.jLabel2);
        assertNotNull(updateUserPanel.jLabel3);
        // Add more assertions for other components
    }

    @Test
    public void testJButton1ActionPerformed() {
        updateUserPanel.jButton1 = new JButton();
        updateUserPanel.jButton1.addActionListener(e -> updateUserPanel.jButton1ActionPerformed(e));
        updateUserPanel.jButton1.doClick();
        assertTrue(updateUserPanel.isVisible() == false);
    }

    @Test(expected = NullPointerException.class)
    public void testjButton2ActionPerformedWithNullUser() {
        updateUserPanel.jButton2ActionPerformed(null);
    }

    @Test
    public void testjButton2ActionPerformedWithValidUser() {
        Group group = new Group();
        User user = new User();
        updateUserPanel.adminApp.addGroup(group);
        updateUserPanel.adminApp.addUser(user);
        Group selectedGroup = updateUserPanel.jComboBox1.getItemAt(0);
        updateUserPanel.jComboBox1.setSelectedItem(selectedGroup);
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jButton2ActionPerformed(null);
        // Add more assertions for other UI states and messages
    }

    @Test(expected = IllegalArgumentException.class)
    public void testjButton3ActionPerformedWithNullUser() {
        updateUserPanel.jButton3ActionPerformed(null);
    }

    @Test
    public void testjButton3ActionPerformedWithValidUser() {
        Group group = new Group();
        User user = new User();
        updateUserPanel.adminApp.addGroup(group);
        updateUserPanel.adminApp.addUser(user);
        updateUserPanel.jComboBox1.setSelectedItem(group);
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jTextField1.setText("newName");
        updateUserPanel.jTextField2.setText("newEmail@email.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("newTitle");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton3ActionPerformed(null);
        User updatedUser = updateUserPanel.jComboBox2.getItemAt(0);
        assertEquals("newName", updatedUser.getName());
        assertEquals("newEmail@email.com", updatedUser.getEmail());
        assertEquals("1234567890", updatedUser.getDirectPhone());
        assertEquals("newTitle", updatedUser.getJobTitle());
        assertTrue(updatedUser.isShowMyStatus());
        assertTrue(updatedUser.isOnlyStartChat());
        assertTrue(updatedUser.isCanJoinChatRoom());
    }

    @Test(expected = NullPointerException.class)
    public void testjComboBox1ItemStateChangedWithNullEvent() {
        updateUserPanel.jComboBox1ItemStateChanged(null);
    }

    @Test
    public void testjComboBox1ItemStateChangedWithValidEvent() {
        Group group = new Group();
        updateUserPanel.adminApp.addGroup(group);
        updateUserPanel.jComboBox1.setSelectedItem(group);
        updateUserPanel.jComboBox1ItemStateChanged(null);
        assertNotNull(updateUserPanel.jComboBox2.getModel());
    }
}