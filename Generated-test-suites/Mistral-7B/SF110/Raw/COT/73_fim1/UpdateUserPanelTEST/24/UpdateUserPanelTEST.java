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

    @Before
    public void setUp() {
        adminApp = new AdminApp();
        group = new Group();
        user = new User();
        updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.updateModel();
    }

    @Test
    public void testUpdateModel() {
        assertNotNull(updateUserPanel.jComboBox1);
        assertNotNull(updateUserPanel.jComboBox2);
        assertNotNull(updateUserPanel.controlPanel);
        assertNotNull(updateUserPanel.adminApp);
        assertEquals(updateUserPanel.adminApp.getGroups().size(), updateUserPanel.jComboBox1.getModel().getSize());
        if (updateUserPanel.adminApp.getGroups().size() > 0) {
            assertEquals(updateUserPanel.adminApp.getGroups().get(0).getUsers().size(), updateUserPanel.jComboBox2.getModel().getSize());
            user = updateUserPanel.adminApp.getGroups().get(0).getUsers().get(0);
            updateUserPanel.fillUser(user);
            assertEquals(user.getName(), updateUserPanel.jTextField1.getText());
            assertEquals(user.getEmail(), updateUserPanel.jTextField2.getText());
            assertEquals(user.getDirectPhone(), updateUserPanel.jTextField3.getText());
            assertEquals(user.getJobTitle(), updateUserPanel.jTextField4.getText());
            assertEquals(user.isShowMyStatus(), updateUserPanel.jCheckBox1.isSelected());
            assertEquals(user.isOnlyStartChat(), updateUserPanel.jCheckBox2.isSelected());
            assertEquals(user.isCanJoinChatRoom(), updateUserPanel.jCheckBox3.isSelected());
        }
    }

    @Test
    public void testJButton1ActionPerformed() {
        updateUserPanel.jButton1.doClick();
        assertTrue(updateUserPanel.getParentDialog().isVisible());
        updateUserPanel.reset();
        assertFalse(updateUserPanel.jTextField1.getText().isEmpty());
        assertFalse(updateUserPanel.jTextField2.getText().isEmpty());
        assertFalse(updateUserPanel.jTextField3.getText().isEmpty());
        assertFalse(updateUserPanel.jTextField4.getText().isEmpty());
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertFalse(updateUserPanel.jCheckBox3.isSelected());
        assertFalse(updateUserPanel.jComboBox1.getSelectedIndex() == -1);
        assertFalse(updateUserPanel.jComboBox2.getSelectedIndex() == -1);
    }

    @Test(expected = NullPointerException.class)
    public void testJButton2ActionPerformedWithNullUser() {
        final Group group = (Group)updateUserPanel.jComboBox1.getSelectedItem();
        updateUserPanel.jComboBox2.setSelectedIndex(1);
        updateUserPanel.jButton2.doClick();
    }

    @Test
    public void testJButton2ActionPerformed() {
        final Group group = (Group)updateUserPanel.jComboBox1.getSelectedItem();
        User user = (User)updateUserPanel.jComboBox2.getSelectedItem();
        updateUserPanel.jButton2.doClick();
        assertFalse(updateUserPanel.jComboBox2.getSelectedIndex() == -1);
        assertFalse(updateUserPanel.jComboBox2.getModel().contains(user));
        assertFalse(group.getUsers().contains(user));
    }

    @Test
    public void testJButton3ActionPerformed() {
        final Group group = (Group)updateUserPanel.jComboBox1.getSelectedItem();
        User user = (User)updateUserPanel.jComboBox2.getSelectedItem();
        updateUserPanel.jTextField1.setText("Test User");
        updateUserPanel.jTextField2.setText("test.user@example.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("Test User Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton3.doClick();
        assertEquals("Test User", user.getName());
        assertEquals("test.user@example.com", user.getEmail());
        assertEquals("1234567890", user.getDirectPhone());
        assertEquals("Test User Title", user.getJobTitle());
        assertTrue(user.isShowMyStatus());
        assertFalse(user.isOnlyStartChat());
        assertTrue(user.isCanJoinChatRoom());
    }

    @Test(expected = NullPointerException.class)
    public void testJButton4ActionPerformedWithNullGroup() {
        User user = (User)updateUserPanel.jComboBox2.getSelectedItem();
        updateUserPanel.jTextField1.setText("Test User");
        updateUserPanel.jTextField2.setText("test.user@example.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("Test User Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4.doClick();
    }

    @Test
    public void testJButton4ActionPerformed() {
        final Group group = (Group)updateUserPanel.jComboBox1.getSelectedItem();
        User user = (User)updateUserPanel.jComboBox2.getSelectedItem();
        updateUserPanel.jTextField1.setText("Test User");
        updateUserPanel.jTextField2.setText("test.user@example.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("Test User Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4.doClick();
        assertEquals("Test User", user.getName());
        assertEquals("test.user@example.com", user.getEmail());
        assertEquals("1234567890", user.getDirectPhone());
        assertEquals("Test User Title", user.getJobTitle());
        assertTrue(user.isShowMyStatus());
        assertFalse(user.isOnlyStartChat());
        assertTrue(user.isCanJoinChatRoom());
        assertTrue(group.getUsers().contains(user));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton4ActionPerformedWithEmptyEmail() {
        final Group group = (Group)updateUserPanel.jComboBox1.getSelectedItem();
        User user = (User)updateUserPanel.jComboBox2.getSelectedItem();
        updateUserPanel.jTextField1.setText("Test User");
        updateUserPanel.jTextField2.setText("");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("Test User Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4.doClick();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton4ActionPerformedWithUserAlreadyExists() {
        final Group group = (Group)updateUserPanel.jComboBox1.getSelectedItem();
        User user1 = (User)updateUserPanel.jComboBox2.getSelectedItem();
        User user2 = new User();
        updateUserPanel.jTextField1.setText("Test User");
        updateUserPanel.jTextField2.setText("test.user@example.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("Test User Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4.doClick();
        updateUserPanel.jTextField2.setText("test.user@example.com");
        updateUserPanel.jButton4.doClick();
    }
}