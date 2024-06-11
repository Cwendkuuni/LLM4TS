package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import org.junit.Test;
import osa.ora.server.admin.AdminApp;
import osa.ora.server.admin.Group;
import osa.ora.server.beans.User;

public class UpdateUserPanelTest {

    private UpdateUserPanel updateUserPanel;
    private AdminApp adminApp;
    private Group group;
    private User user;

    @Test
    public void testInitComponents() {
        adminApp = new AdminApp();
        group = new Group();
        updateUserPanel = new UpdateUserPanel(adminApp, null);
        assertNotNull(updateUserPanel.jLabel1);
        assertNotNull(updateUserPanel.jLabel2);
        assertNotNull(updateUserPanel.jLabel3);
        assertNotNull(updateUserPanel.jCheckBox1);
        assertNotNull(updateUserPanel.jCheckBox2);
        assertNotNull(updateUserPanel.jCheckBox3);
    }

    @Test
    public void testUpdateModel() {
        adminApp = new AdminApp();
        group.setName("TestGroup");
        group.setUsers(new Vector<User>());
        adminApp.setGroups(new Vector<Group>());
        adminApp.getGroups().add(group);
        updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.updateModel();
        assertEquals(group.getUsers().size(), updateUserPanel.jComboBox2.getModel().getSize());
    }

    @Test
    public void testJComboBox1ItemStateChanged() {
        adminApp = new AdminApp();
        group.setName("TestGroup");
        group.setUsers(new Vector<User>());
        adminApp.setGroups(new Vector<Group>());
        adminApp.getGroups().add(group);
        group.getUsers().add(new User());
        group.getUsers().add(new User());
        updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox1ItemStateChanged(null);
        assertEquals(group.getUsers().get(0), updateUserPanel.jComboBox2.getSelectedItem());

        updateUserPanel.jComboBox1.setSelectedIndex(1);
        updateUserPanel.jComboBox1ItemStateChanged(null);
        assertEquals(group.getUsers().get(1), updateUserPanel.jComboBox2.getSelectedItem());
    }

    @Test
    public void testJButton1ActionPerformed() {
        adminApp = new AdminApp();
        group.setName("TestGroup");
        group.setUsers(new Vector<User>());
        adminApp.setGroups(new Vector<Group>());
        adminApp.getGroups().add(group);
        updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jButton1ActionPerformed(null);
        assertTrue(updateUserPanel.isVisible() == false);
        assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible() == false);
    }

    @Test
    public void testJButton2ActionPerformed() {
        adminApp = new AdminApp();
        group = new Group();
        group.setName("TestGroup");
        group.setUsers(new Vector<User>());
        adminApp.setGroups(new Vector<Group>());
        adminApp.getGroups().add(group);
        updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.updateModel();
        user = new User();
        group.getUsers().add(user);
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jButton2ActionPerformed(null);
        assertEquals(group.getUsers().size(), updateUserPanel.jComboBox2.getModel().getSize());

        user = new User();
        group.getUsers().remove(0);
        group.getUsers().add(user);
        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(1);
        updateUserPanel.jButton2ActionPerformed(null);
        assertEquals(group.getUsers().size(), updateUserPanel.jComboBox2.getModel().getSize());
    }

    @Test
    public void testJButton3ActionPerformed() {
        adminApp = new AdminApp();
        group = new Group();
        group.setName("TestGroup");
        group.setUsers(new Vector<User>());
        adminApp.setGroups(new Vector<Group>());
        adminApp.getGroups().add(group);
        updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.updateModel();
        user = new User();
        group.getUsers().add(user);
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jButton3ActionPerformed(null);
        assertEquals("", updateUserPanel.jTextField1.getText());
        assertEquals("", updateUserPanel.jTextField2.getText());
        assertEquals("", updateUserPanel.jTextField3.getText());
        assertEquals("", updateUserPanel.jTextField4.getText());
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertFalse(updateUserPanel.jCheckBox3.isSelected());
    }

    @Test
    public void testJButton4ActionPerformed() {
        adminApp = new AdminApp();
        group = new Group();
        group.setName("TestGroup");
        group.setUsers(new Vector<User>());
        adminApp.setGroups(new Vector<Group>());
        adminApp.getGroups().add(group);
        updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.updateModel();
        user = new User();
        user.setName("TestUser");
        user.setEmail("test@test.com");
        user.setDirectPhone("1234567890");
        user.setJobTitle("TestTitle");
        group.getUsers().add(user);
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField1.setText("TestUser");
        updateUserPanel.jTextField2.setText("test@test.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("TestTitle");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(null);
        assertEquals(user.getName(), updateUserPanel.jTextField1.getText());
        assertEquals(user.getEmail(), updateUserPanel.jTextField2.getText());
        assertEquals(user.getDirectPhone(), updateUserPanel.jTextField3.getText());
        assertEquals(user.getJobTitle(), updateUserPanel.jTextField4.getText());
        assertTrue(user.isShowMyStatus() == updateUserPanel.jCheckBox1.isSelected());
        assertTrue(user.isOnlyStartChat() == updateUserPanel.jCheckBox2.isSelected());
        assertTrue(user.isCanJoinChatRoom() == updateUserPanel.jCheckBox3.isSelected());
    }
}