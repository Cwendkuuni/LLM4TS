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
        User user = new User("User1", "user1@example.com", "1234567890", true, true, true, "Software Engineer");
        group.addUser(user);
        adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));

        updateUserPanel.updateModel();

        assertEquals(1, updateUserPanel.jComboBox1.getItemCount());
        assertEquals("Group1", updateUserPanel.jComboBox1.getItemAt(0).toString());

        assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
        assertEquals("User1", updateUserPanel.jComboBox2.getItemAt(0).toString());
    }

    @Test
    public void testJComboBox1ItemStateChanged() {
        AdminApp adminApp = new AdminApp();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
        Group group1 = new Group("Group1");
        User user1 = new User("User1", "user1@example.com", "1234567890", true, true, true, "Software Engineer");
        group1.addUser(user1);

        Group group2 = new Group("Group2");
        User user2 = new User("User2", "user2@example.com", "2345678901", true, true, true, "Database Administrator");
        group2.addUser(user2);

        Vector<Group> groups = new Vector<Group>();
        groups.add(group1);
        groups.add(group2);

        adminApp.setGroups(groups);

        updateUserPanel.updateModel();

        updateUserPanel.jComboBox1.setSelectedIndex(1);

        assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
        assertEquals("User2", updateUserPanel.jComboBox2.getItemAt(0).toString());
    }

    @Test
    public void testJButton1ActionPerformed() {
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(null, null);
        updateUserPanel.jButton1.doClick();
        assertTrue(updateUserPanel.isVisible() == false);
    }

    @Test
    public void testJButton2ActionPerformed() {
        AdminApp adminApp = new AdminApp();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);

        Group group = new Group("Group1");
        User user = new User("User1", "user1@example.com", "1234567890", true, true, true, "Software Engineer");
        group.addUser(user);

        adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));

        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);

        updateUserPanel.jButton2.doClick();

        assertEquals(0, group.getUsers().size());
    }

    @Test
    public void testJButton3ActionPerformed() {
        AdminApp adminApp = new AdminApp();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);

        Group group = new Group("Group1");
        User user = new User("User1", "user1@example.com", "1234567890", true, true, true, "Software Engineer");
        group.addUser(user);

        adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));

        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);

        updateUserPanel.jTextField1.setText("NewName");
        updateUserPanel.jTextField2.setText("newemail@example.com");
        updateUserPanel.jTextField3.setText("12345678901");
        updateUserPanel.jTextField4.setText("Software Developer");

        updateUserPanel.jCheckBox1.setSelected(false);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(false);

        updateUserPanel.jButton3.doClick();

        assertEquals("NewName", user.getName());
        assertEquals("newemail@example.com", user.getEmail());
        assertEquals("12345678901", user.getDirectPhone());
        assertEquals("Software Developer", user.getJobTitle());
        assertFalse(user.isShowMyStatus());
        assertTrue(user.isOnlyStartChat());
        assertFalse(user.isCanJoinChatRoom());
    }

    @Test
    public void testJButton4ActionPerformed() {
        AdminApp adminApp = new AdminApp();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);

        Group group = new Group("Group1");
        User user = new User("User1", "user1@example.com", "1234567890", true, true, true, "Software Engineer");
        group.addUser(user);

        adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));

        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);

        updateUserPanel.jTextField1.setText("NewName");
        updateUserPanel.jTextField2.setText("newemail@example.com");
        updateUserPanel.jTextField3.setText("12345678901");
        updateUserPanel.jTextField4.setText("Software Developer");

        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);

        updateUserPanel.jButton4.doClick();

        assertEquals("NewName", user.getName());
        assertEquals("newemail@example.com", user.getEmail());
        assertEquals("12345678901", user.getDirectPhone());
        assertEquals("Software Developer", user.getJobTitle());
        assertTrue(user.isShowMyStatus());
        assertFalse(user.isOnlyStartChat());
        assertTrue(user.isCanJoinChatRoom());
    }

    @Test
    public void testFillUser() {
        AdminApp adminApp = new AdminApp();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);

        Group group = new Group("Group1");
        User user = new User("User1", "user1@example.com", "1234567890", true, true, true, "Software Engineer");
        group.addUser(user);

        adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));

        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);

        updateUserPanel.fillUser(user);

        assertEquals("User1", updateUserPanel.jTextField1.getText());
        assertEquals("user1@example.com", updateUserPanel.jTextField2.getText());
        assertEquals("1234567890", updateUserPanel.jTextField3.getText());
        assertEquals("Software Engineer", updateUserPanel.jTextField4.getText());
        assertTrue(updateUserPanel.jCheckBox1.isSelected());
        assertTrue(updateUserPanel.jCheckBox2.isSelected());
        assertTrue(updateUserPanel.jCheckBox3.isSelected());
    }

    @Test
    public void testReset() {
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(null, null);

        updateUserPanel.reset();

        assertEquals("", updateUserPanel.jTextField1.getText());
        assertEquals("", updateUserPanel.jTextField2.getText());
        assertEquals("", updateUserPanel.jTextField3.getText());
        assertEquals("", updateUserPanel.jTextField4.getText());
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertFalse(updateUserPanel.jCheckBox3.isSelected());
    }
}