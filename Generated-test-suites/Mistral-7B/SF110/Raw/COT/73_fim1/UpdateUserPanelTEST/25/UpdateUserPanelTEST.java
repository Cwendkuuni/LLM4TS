package osa.ora.server.admin.ui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import osa.ora.server.admin.AdminApp;
import osa.ora.server.admin.Group;
import osa.ora.server.beans.User;

public class UpdateUserPanelTest {

    private AdminApp adminApp;
    private UpdateUserPanel updateUserPanel;

    @Before
    public void setup() {
        adminApp = new AdminApp();
        updateUserPanel = new UpdateUserPanel(adminApp, null);
        Group group = new Group("Group1");
        userSetup(group);
        adminApp.setGroups(new Vector<Group>() {{
            add(group);
        }});
        updateUserPanel.updateModel();
    }

    @Test
    public void testUpdateUserPanelConstructor() {
        assertNotNull(updateUserPanel);
    }

    @Test
    public void testUpdateModel() {
        assertEquals(1, updateUserPanel.jComboBox1.getItemCount());
        updateUserPanel.updateModel();
        assertEquals(1, updateUserPanel.jComboBox1.getItemCount());
        assertEquals("Group1", updateUserPanel.jComboBox1.getItemAt(0).toString());
        assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
        assertEquals("User1", updateUserPanel.jComboBox2.getItemAt(0).toString());
    }

    @Test
    public void testJButton1ActionPerformed() {
        updateUserPanel.jButton1.doClick();
        assertTrue(updateUserPanel.isVisible() == false);
        resetUserPanel(updateUserPanel);
    }

    @Test
    public void testJButton2ActionPerformedNullUser() {
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jButton2.doClick();
        assertFalse(updateUserPanel.isVisible() == false);
    }

    @Test
    public void testJButton2ActionPerformedValidUser() {
        updateUserPanel.jComboBox2.setSelectedIndex(1);
        updateUserPanel.jButton2.doClick();
        assertFalse(updateUserPanel.isVisible() == false);
        User user = adminApp.getUsers().get(1);
        Group group = adminApp.getGroups().get(0);
        assertEquals(0, group.getUsers().size());
        assertEquals(1, adminApp.getUsers().size());
        User updatedUser = adminApp.getUsers().get(0);
        assertNull(updatedUser.getName());
        assertNull(updatedUser.getEmail());
        assertNull(updatedUser.getDirectPhone());
        assertNull(updatedUser.getJobTitle());
        assertFalse(updatedUser.isShowMyStatus());
        assertFalse(updatedUser.isOnlyStartChat());
        assertFalse(updatedUser.isCanJoinChatRoom());
    }

    @Test(expected = Exception.class)
    public void testJButton2ActionPerformedNullGroup() {
        adminApp.setGroups(null);
        updateUserPanel.updateModel();
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jButton2.doClick();
    }

    @Test
    public void testJButton3ActionPerformedNullUser() {
        updateUserPanel.jButton3.doClick();
        assertFalse(updateUserPanel.isVisible() == false);
    }

    @Test
    public void testJButton3ActionPerformedValidUser() {
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField1.setText("TestUser");
        updateUserPanel.jTextField2.setText("test@test.com");
        updateUserPanel.jTextField3.setText("123456789");
        updateUserPanel.jTextField4.setText("TestTitle");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton3.doClick();
        User user = adminApp.getUsers().get(0);
        assertEquals("TestUser", user.getName());
        assertEquals("test@test.com", user.getEmail());
        assertEquals("123456789", user.getDirectPhone());
        assertEquals("TestTitle", user.getJobTitle());
        assertTrue(user.isShowMyStatus());
        assertTrue(user.isOnlyStartChat());
        assertTrue(user.isCanJoinChatRoom());
    }

    @Test
    public void testJButton4ActionPerformedNullUser() {
        updateUserPanel.jButton4.doClick();
        assertFalse(updateUserPanel.isVisible() == false);
    }

    @Test
    public void testJButton4ActionPerformedValidUser() {
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField1.setText("TestUser");
        updateUserPanel.jTextField2.setText("test@test.com");
        updateUserPanel.jTextField3.setText("123456789");
        updateUserPanel.jTextField4.setText("TestTitle");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4.doClick();
        User user = adminApp.getUsers().get(0);
        assertEquals("TestUser", user.getName());
        assertEquals("test@test.com", user.getEmail());
        assertEquals("123456789", user.getDirectPhone());
        assertEquals("TestTitle", user.getJobTitle());
        assertTrue(user.isShowMyStatus());
        assertTrue(user.isOnlyStartChat());
        assertTrue(user.isCanJoinChatRoom());
    }

    private void userSetup(Group group) {
        User user1 = new User("User1", "user1@test.com", "123456789", "User1Title", true, true, true);
        User user2 = new User("TestUser", "test@test.com", null, null, false, false, false);
        group.setUsers(new Vector<User>() {{
            add(user1);
            add(user2);
        }});
    }

    private void resetUserPanel(UpdateUserPanel updateUserPanel) {
        updateUserPanel.jTextField1.setText("");
        updateUserPanel.jTextField2.setText("");
        updateUserPanel.jTextField3.setText("");
        updateUserPanel.jTextField4.setText("");
        updateUserPanel.jCheckBox1.setSelected(false);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(false);
    }
}