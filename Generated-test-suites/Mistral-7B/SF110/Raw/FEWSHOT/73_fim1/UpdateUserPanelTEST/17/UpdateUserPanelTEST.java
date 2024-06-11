package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import org.junit.Test;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

    @Test
    public void testUpdateModel() {
        AdminApp adminApp = new AdminApp();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
        Group group = new Group("Group1");
        User user = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
        group.addUser(user);
        adminApp.setGroups(new Vector<Group>() {{
            add(group);
        }});

        updateUserPanel.updateModel();

        assertEquals(user, updateUserPanel.jComboBox2.getSelectedItem());
    }

    @Test
    public void testJComboBox1ItemStateChanged() {
        AdminApp adminApp = new AdminApp();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
        Group group = new Group("Group1");
        User user = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
        group.addUser(user);
        adminApp.setGroups(new Vector<Group>() {{
            add(group);
        }});

        updateUserPanel.updateModel();

        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox1ItemStateChanged(null);

        assertEquals(user, updateUserPanel.jComboBox2.getSelectedItem());
    }

    @Test
    public void testJButton2ActionPerformed() {
        AdminApp adminApp = new AdminApp();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
        Group group = new Group("Group1");
        User user = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
        group.addUser(user);
        adminApp.setGroups(new Vector<Group>() {{
            add(group);
        }});

        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);

        updateUserPanel.jButton2.doClick();

        assertEquals(0, group.getUsers().size());
        assertNull(updateUserPanel.jComboBox2.getSelectedItem());
    }

    @Test
    public void testJButton3ActionPerformed() {
        AdminApp adminApp = new AdminApp();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
        Group group = new Group("Group1");
        User user = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
        group.addUser(user);
        adminApp.setGroups(new Vector<Group>() {{
            add(group);
        }});

        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);

        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("new.user@example.com");
        updateUserPanel.jTextField3.setText("1234567891");
        updateUserPanel.jTextField4.setText("NewTitle");

        updateUserPanel.jCheckBox1.setSelected(false);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(false);

        updateUserPanel.jButton3.doClick();

        User updatedUser = group.getUsers().get(0);
        assertEquals("NewUserName", updatedUser.getName());
        assertEquals("new.user@example.com", updatedUser.getEmail());
        assertEquals("1234567891", updatedUser.getDirectPhone());
        assertEquals("NewTitle", updatedUser.getJobTitle());
        assertFalse(updatedUser.isShowMyStatus());
        assertTrue(updatedUser.isOnlyStartChat());
        assertFalse(updatedUser.isCanJoinChatRoom());
    }

    @Test
    public void testJButton4ActionPerformed() {
        AdminApp adminApp = new AdminApp();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
        Group group = new Group("Group1");
        User user = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
        group.addUser(user);
        adminApp.setGroups(new Vector<Group>() {{
            add(group);
        }});

        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);

        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("new.user@example.com");
        updateUserPanel.jTextField3.setText("1234567891");
        updateUserPanel.jTextField4.setText("NewTitle");

        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);

        updateUserPanel.jButton4.doClick();

        User updatedUser = group.getUsers().get(0);
        assertEquals("NewUserName", updatedUser.getName());
        assertEquals("new.user@example.com", updatedUser.getEmail());
        assertEquals("1234567891", updatedUser.getDirectPhone());
        assertEquals("NewTitle", updatedUser.getJobTitle());
        assertTrue(updatedUser.isShowMyStatus());
        assertTrue(updatedUser.isOnlyStartChat());
        assertTrue(updatedUser.isCanJoinChatRoom());
    }

    @Test(expected = NullPointerException.class)
    public void testJButton1ActionPerformed() {
        AdminApp adminApp = new AdminApp();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.jButton1.doClick();
    }

    @Test(expected = NullPointerException.class)
    public void testJButton2ActionPerformedWithoutUser() {
        AdminApp adminApp = new AdminApp();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.jButton2.doClick();
    }

    @Test(expected = NullPointerException.class)
    public void testJButton3ActionPerformedWithoutUser() {
        AdminApp adminApp = new AdminApp();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.jButton3.doClick();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton4ActionPerformedWithEmptyUsername() {
        AdminApp adminApp = new AdminApp();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
        Group group = new Group("Group1");
        User user = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
        group.addUser(user);
        adminApp.setGroups(new Vector<Group>() {{
            add(group);
        }});

        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);

        updateUserPanel.jTextField1.setText("");
        updateUserPanel.jTextField2.setText("new.user@example.com");
        updateUserPanel.jTextField3.setText("1234567891");
        updateUserPanel.jTextField4.setText("NewTitle");

        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);

        updateUserPanel.jButton4.doClick();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton4ActionPerformedWithEmptyEmail() {
        AdminApp adminApp = new AdminApp();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
        Group group = new Group("Group1");
        User user = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
        group.addUser(user);
        adminApp.setGroups(new Vector<Group>() {{
            add(group);
        }});

        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);

        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("");
        updateUserPanel.jTextField3.setText("1234567891");
        updateUserPanel.jTextField4.setText("NewTitle");

        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);

        updateUserPanel.jButton4.doClick();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton4ActionPerformedWithSameEmail() {
        AdminApp adminApp = new AdminApp();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
        Group group = new Group("Group1");
        User user1 = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
        User user2 = new User("User2", "user1@example.com", "1234567891", "Title2", true, true, true);
        group.addUser(user1);
        group.addUser(user2);
        adminApp.setGroups(new Vector<Group>() {{
            add(group);
        }});

        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(1);

        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("user1@example.com");
        updateUserPanel.jTextField3.setText("1234567892");
        updateUserPanel.jTextField4.setText("NewTitle");

        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);

        updateUserPanel.jButton4.doClick();
    }
}