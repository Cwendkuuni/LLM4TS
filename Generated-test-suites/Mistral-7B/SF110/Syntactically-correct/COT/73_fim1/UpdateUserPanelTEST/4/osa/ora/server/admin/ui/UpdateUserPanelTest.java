package osa.ora.server.admin.ui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

    private UpdateUserPanel updateUserPanel;
    private AdminApp adminApp;
    private Group group;
    private User user;

    @Before
    public void setUp() {
        adminApp = new AdminApp();
        group = new Group("Group1", null);
        user = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
        group.addUser(user);
        updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.updateModel();
    }

    @Test
    public void testUpdateModel() {
        assertEquals(1, updateUserPanel.jComboBox1.getItemCount());
        assertEquals("Group1", updateUserPanel.jComboBox1.getItemAt(0).toString());
        assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
        assertEquals("User1", updateUserPanel.jComboBox2.getItemAt(0).toString());
    }

    @Test
    public void testJButton1ActionPerformed() {
        updateUserPanel.jButton1.doClick();
        assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible() == false);
        updateUserPanel.reset();
    }

    @Test(expected = NullPointerException.class)
    public void testJButton2ActionPerformedWithNullSelectedUser() {
        updateUserPanel.jButton2.doClick();
    }

    @Test
    public void testJButton2ActionPerformedWithValidSelectedUser() {
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jButton2.doClick();
        assertEquals(0, group.getUsers().size());
        JOptionPane.showMessageDialog(updateUserPanel, "User Deleted Successfully!");
    }

    @Test(expected = NullPointerException.class)
    public void testJButton3ActionPerformedWithNullSelectedUser() {
        updateUserPanel.jButton3.doClick();
    }

    @Test
    public void testJButton3ActionPerformedWithValidSelectedUser() {
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("newuser1@example.com");
        updateUserPanel.jTextField3.setText("1234567891");
        updateUserPanel.jTextField4.setText("NewTitle");
        updateUserPanel.jCheckBox1.setSelected(false);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(false);
        updateUserPanel.jButton3.doClick();
        assertEquals("NewUserName", user.getName());
        assertEquals("newuser1@example.com", user.getEmail());
        assertEquals("1234567891", user.getDirectPhone());
        assertEquals("NewTitle", user.getJobTitle());
        assertFalse(user.isShowMyStatus());
        assertTrue(user.isOnlyStartChat());
        assertFalse(user.isCanJoinChatRoom());
        JOptionPane.showMessageDialog(updateUserPanel, "User Updated Successfully!");
    }

    @Test
    public void testJButton4ActionPerformedWithNullSelectedUser() {
        updateUserPanel.jButton4.doClick();
    }

    @Test(expected = NullPointerException.class)
    public void testJButton4ActionPerformedWithNullSelectedGroup() {
        updateUserPanel.jComboBox1.setSelectedItem(null);
        updateUserPanel.jButton4.doClick();
    }

    @Test
    public void testJButton4ActionPerformedWithValidSelectedGroupAndUser() {
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("newuser1@example.com");
        updateUserPanel.jTextField3.setText("1234567891");
        updateUserPanel.jTextField4.setText("NewTitle");
        updateUserPanel.jCheckBox1.setSelected(false);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(false);
        updateUserPanel.jButton4.doClick();
        assertEquals("NewUserName", user.getName());
        assertEquals("newuser1@example.com", user.getEmail());
        assertEquals("1234567891", user.getDirectPhone());
        assertEquals("NewTitle", user.getJobTitle());
        assertFalse(user.isShowMyStatus());
        assertTrue(user.isOnlyStartChat());
        assertFalse(user.isCanJoinChatRoom());
        JOptionPane.showMessageDialog(updateUserPanel, "User Updated Successfully!");
    }
}