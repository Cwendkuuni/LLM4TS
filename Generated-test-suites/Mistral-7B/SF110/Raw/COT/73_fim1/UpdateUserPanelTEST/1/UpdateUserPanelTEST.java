package osa.ora.server.admin.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import osa.ora.server.admin.AdminApp;
import osa.ora.server.admin.Group;
import osa.ora.server.admin.User;
import osa.ora.server.beans.DefaultComboBoxModel;

public class UpdateUserPanelTest {
    private UpdateUserPanel updateUserPanel;
    private AdminApp adminApp;
    private ControlPanel controlPanel;
    private Group group;
    private User user;

    @Before
    public void setup() {
        adminApp = new AdminApp();
        controlPanel = new ControlPanel();
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        group = new Group("Group1");
        user = new User("User1", "user1@example.com", "123456789", "Title1", true, true, true);
        group.addUser(user);
        adminApp.setGroups(new ArrayList<>(Arrays.asList(group)));
    }

    @Test
    public void testUpdateUserPanelConstructor() {
        assertNotNull(updateUserPanel);
    }

    @Test
    public void testUpdateModel() {
        updateUserPanel.updateModel();
        assertEquals(group, updateUserPanel.getAdminApp().getGroups().get(0));
        assertEquals(user, updateUserPanel.getJComboBox2().getSelectedItem());
        fillUserComponents(user);
    }

    @Test
    public void testInitComponents() {
        // Check label text
        assertEquals("Username:", updateUserPanel.getJLabel1().getText());
        assertEquals("Login Email:", updateUserPanel.getJLabel2().getText());
        assertEquals("Phone:", updateUserPanel.getJLabel3().getText());

        // Check checkbox labels
        assertEquals("Show User Status To Others", updateUserPanel.getJCheckBox1().getText());
        assertEquals("User Can Only Start Chat With Others", updateUserPanel.getJCheckBox2().getText());
        assertEquals("Can be a Member of Chat Rooms", updateUserPanel.getJCheckBox3().getText());

        // Check comboBox1 contents
        assertEquals(4, updateUserPanel.getJComboBox1().getItemCount());
        assertEquals("Item 1", updateUserPanel.getJComboBox1().getItemAt(0));

        // Check text fields are empty
        assertEquals("", updateUserPanel.getJTextField1().getText());
        assertEquals("", updateUserPanel.getJTextField2().getText());
        assertEquals("", updateUserPanel.getJTextField3().getText());
        assertEquals("", updateUserPanel.getJTextField4().getText());

        // Check buttons are disabled
        assertFalse(updateUserPanel.getJButton1().isEnabled());
        assertFalse(updateUserPanel.getJButton2().isEnabled());
        assertFalse(updateUserPanel.getJButton3().isEnabled());
        assertFalse(updateUserPanel.getJButton4().isEnabled());
    }

    @Test
    public void testJButton1ActionPerformed() {
        updateUserPanel.jButton1ActionPerformed(new ActionEvent(new Object(), 0, ""));
        assertTrue(updateUserPanel.isVisible() == false);
        assertEquals("", updateUserPanel.getJTextField1().getText());
        assertEquals("", updateUserPanel.getJTextField2().getText());
        assertEquals("", updateUserPanel.getJTextField3().getText());
        assertEquals("", updateUserPanel.getJTextField4().getText());
    }

    @Test(expected = NullPointerException.class)
    public void testJButton2ActionPerformedNullUser() {
        updateUserPanel.jComboBox2.setSelectedIndex(1);
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 0, ""));
    }

    @Test
    public void testJButton2ActionPerformed() {
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 0, ""));

        assertEquals(3, group.getUsers().size());
        assertNull(updateUserPanel.getJComboBox2().getSelectedItem());
    }

    @Test
    public void testJButton3ActionPerformed() {
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("newuser@example.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("NewTitle");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton3ActionPerformed(new ActionEvent(new Object(), 0, ""));

        assertEquals("NewUserName", user.getName());
        assertEquals("newuser@example.com", user.getEmail());
        assertEquals("1234567890", user.getDirectPhone());
        assertEquals("NewTitle", user.getJobTitle());
        assertTrue(user.isShowMyStatus());
        assertTrue(user.isOnlyStartChat());
        assertTrue(user.isCanJoinChatRoom());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton4ActionPerformedNullName() {
        updateUserPanel.jTextField1.setText("");
        updateUserPanel.jTextField2.setText("user@example.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton4ActionPerformedNullEmail() {
        updateUserPanel.jTextField1.setText("UserName");
        updateUserPanel.jTextField2.setText("");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton4ActionPerformedDuplicateEmail() {
        User duplicateUser = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
        group.getUsers().add(duplicateUser);

        updateUserPanel.jTextField1.setText("UserName");
        updateUserPanel.jTextField2.setText("user1@example.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, ""));
    }

    @Test
    public void testJButton4ActionPerformed() {
        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("newuser@example.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("NewTitle");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, ""));

        assertEquals("NewUserName", user.getName());
        assertEquals("newuser@example.com", user.getEmail());
        assertEquals("1234567890", user.getDirectPhone());
        assertEquals("NewTitle", user.getJobTitle());
        assertTrue(user.isShowMyStatus());
        assertTrue(user.isOnlyStartChat());
        assertTrue(user.isCanJoinChatRoom());
        assertSame(user, updateUserPanel.getJComboBox2().getSelectedItem());
    }

    @Test
    public void testJComboBox1ItemStateChanged() {
        updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(new Object(), 0, 1));
        assertEquals(user, updateUserPanel.getJComboBox2().getSelectedItem());
        fillUserComponents(user);
    }

    @Test
    public void testJComboBox2ItemStateChanged() {
        updateUserPanel.jComboBox2ItemStateChanged(new ItemEvent(new Object(), 0, 0));
        assertEquals("", updateUserPanel.getJTextField1().getText());
        assertEquals("", updateUserPanel.getJTextField2().getText());
        assertEquals("", updateUserPanel.getJTextField3().getText());
        assertEquals("", updateUserPanel.getJTextField4().getText());
    }

    private void fillUserComponents(User user) {
        assertEquals(user.getName(), updateUserPanel.getJTextField1().getText());
        assertEquals(user.getEmail(), updateUserPanel.getJTextField2().getText());
        assertEquals(user.getDirectPhone(), updateUserPanel.getJTextField3().getText());
        assertEquals(user.getJobTitle(), updateUserPanel.getJTextField4().getText());
        assertEquals(user.isShowMyStatus(), updateUserPanel.jCheckBox1.isSelected());
        assertEquals(user.isOnlyStartChat(), updateUserPanel.jCheckBox2.isSelected());
        assertEquals(user.isCanJoinChatRoom(), updateUserPanel.jCheckBox3.isSelected());
    }
}