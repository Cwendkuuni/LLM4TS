package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

    private AdminApp adminApp;
    private UpdateUserPanel updateUserPanel;
    private Group group;
    private User user;
    private User newUser;

    @Before
    public void setUp() {
        adminApp = new AdminApp();
        updateUserPanel = new UpdateUserPanel(adminApp, null);
        group = new Group("Group1", new Vector<User>());
        user = new User("User1", "user1@example.com", "1234567890", "John Doe", true, true, true);
        newUser = new User("User2", "user2@example.com", null, "Jane Doe", false, false, false);
        group.getUsers().add(user);
        updateUserPanel.adminApp = adminApp;
        updateUserPanel.updateModel();
    }

    @Test
    public void testUpdateModel() {
        assertNotNull(updateUserPanel.jComboBox1);
        assertNotNull(updateUserPanel.jComboBox2);
        assertEquals(group, updateUserPanel.adminApp.getGroups().get(0));
        assertEquals(user, updateUserPanel.jComboBox2.getSelectedItem());
    }

    @Test
    public void testJButton1ActionPerformed() {
        updateUserPanel.jButton1.doClick();
        assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible() == false);
        updateUserPanel.reset();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton2ActionPerformedWithNullUser() {
        updateUserPanel.jButton2.doClick();
    }

    @Test
    public void testJButton2ActionPerformedWithUser() {
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jButton2.doClick();
        assertEquals(group.getUsers().size(), 0);
        assertEquals(group.getUsers().get(0), newUser);
        assertTrue(JOptionPane.showConfirmDialog(updateUserPanel, "User Deleted Successfully!", "InformationMessage", JOptionPane.INFORMATION_MESSAGE) >= 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton3ActionPerformedWithNullUser() {
        updateUserPanel.jButton3.doClick();
    }

    @Test
    public void testJButton3ActionPerformedWithUser() {
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("newuser@example.com");
        updateUserPanel.jTextField3.setText("1234567891");
        updateUserPanel.jTextField4.setText("New Job Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton3.doClick();
        assertEquals(user.getName(), "NewUserName");
        assertEquals(user.getEmail(), "newuser@example.com");
        assertEquals(user.getDirectPhone(), "1234567891");
        assertEquals(user.getJobTitle(), "New Job Title");
        assertEquals(user.isShowMyStatus(), true);
        assertEquals(user.isOnlyStartChat(), false);
        assertEquals(user.isCanJoinChatRoom(), true);
        assertTrue(JOptionPane.showConfirmDialog(updateUserPanel, "User Updated Successfully!", "InformationMessage", JOptionPane.INFORMATION_MESSAGE) >= 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton4ActionPerformedWithNullGroup() {
        updateUserPanel.adminApp.getGroups().remove(0);
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jButton4.doClick();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton4ActionPerformedWithNullUser() {
        updateUserPanel.jComboBox2.setSelectedIndex(-1);
        updateUserPanel.jButton4.doClick();
    }

    @Test
    public void testJButton4ActionPerformedWithValidData() {
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("newuser@example.com");
        updateUserPanel.jTextField3.setText("1234567891");
        updateUserPanel.jTextField4.setText("New Job Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4.doClick();
        assertEquals(user.getName(), "NewUserName");
        assertEquals(user.getEmail(), "newuser@example.com");
        assertEquals(user.getDirectPhone(), "1234567891");
        assertEquals(user.getJobTitle(), "New Job Title");
        assertEquals(user.isShowMyStatus(), true);
        assertEquals(user.isOnlyStartChat(), false);
        assertEquals(user.isCanJoinChatRoom(), true);
        assertTrue(JOptionPane.showConfirmDialog(updateUserPanel, "User Updated Successfully!", "InformationMessage", JOptionPane.INFORMATION_MESSAGE) >= 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJComboBox1ItemStateChangedWithNullGroup() {
        updateUserPanel.adminApp.getGroups().remove(0);
        updateUserPanel.jComboBox1ItemStateChanged(null);
    }
}