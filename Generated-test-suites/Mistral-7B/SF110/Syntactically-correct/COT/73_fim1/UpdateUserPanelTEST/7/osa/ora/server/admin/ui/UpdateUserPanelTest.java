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
        group = new Group("Group1");
        user = new User("JohnDoe", "john.doe@example.com", "1234567890", "John Doe", true, true, true);
        group.getUsers().add(user);

        updateUserPanel = new UpdateUserPanel(adminApp, new ControlPanel());
        updateUserPanel.updateModel();
    }

    @Test
    public void testUpdateUserPanelConstructor() {
        assertNotNull(updateUserPanel);
    }

    @Test
    public void testUpdateModel() {
        assertNotNull(updateUserPanel.jComboBox1);
        assertEquals(4, updateUserPanel.jComboBox1.getItemCount());
        assertNotNull(updateUserPanel.jComboBox2);
        assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
        assertNotNull(updateUserPanel.jTextField2);
        assertNotNull(updateUserPanel.jTextField3);
        assertNotNull(updateUserPanel.jButton1);
        assertNotNull(updateUserPanel.jButton2);
        assertNotNull(updateUserPanel.jButton3);
        assertNotNull(updateUserPanel.jButton4);
    }

    @Test
    public void testJComboBox1ItemStateChanged() {
        updateUserPanel.jComboBox1.setSelectedIndex(1);
        updateUserPanel.jComboBox1ItemStateChanged(null);
        assertNotNull(updateUserPanel.jComboBox2);
        assertEquals(2, updateUserPanel.jComboBox2.getItemCount());
    }

    @Test(expected = NullPointerException.class)
    public void testJButton1ActionPerformedWithNullAdminApp() {
        updateUserPanel = new UpdateUserPanel(null, new ControlPanel());
        updateUserPanel.jButton1ActionPerformed(null);
    }

    @Test
    public void testJButton1ActionPerformed() {
        updateUserPanel.jButton1ActionPerformed(null);
        assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible());
        assertFalse(updateUserPanel.jTextField1.getText().equals(""));
        assertFalse(updateUserPanel.jTextField2.getText().equals(""));
        assertFalse(updateUserPanel.jTextField3.getText().equals(""));
        assertFalse(updateUserPanel.jTextField4.getText().equals(""));
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertFalse(updateUserPanel.jCheckBox3.isSelected());
    }

    @Test(expected = NullPointerException.class)
    public void testJButton2ActionPerformedWithNullAdminApp() {
        updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.jButton2ActionPerformed(null);
    }

    @Test
    public void testJButton2ActionPerformedWithInvalidUser() {
        updateUserPanel.jComboBox1.setSelectedIndex(1);
        updateUserPanel.jComboBox2ItemStateChanged(null);
        updateUserPanel.jComboBox2.setSelectedIndex(1);
        updateUserPanel.jButton2ActionPerformed(null);
        assertEquals(1, group.getUsers().size());
        assertFalse(updateUserPanel.jComboBox2.isEnabled());
        assertFalse(updateUserPanel.jButton2.isEnabled());
    }

    @Test
    public void testJButton2ActionPerformedWithValidUser() {
        updateUserPanel.jComboBox1.setSelectedIndex(1);
        updateUserPanel.jComboBox2ItemStateChanged(null);
        updateUserPanel.jComboBox2.setSelectedIndex(1);
        updateUserPanel.jButton2ActionPerformed(null);
        assertEquals(0, group.getUsers().size());
        assertTrue(updateUserPanel.jComboBox2.isEnabled());
        assertTrue(updateUserPanel.jButton2.isEnabled());
    }

    @Test(expected = NullPointerException.class)
    public void testJButton3ActionPerformedWithNullAdminApp() {
        updateUserPanel = new UpdateUserPanel(adminApp, new ControlPanel());
        updateUserPanel.jButton3.setActionCommand("resetPassword");
        updateUserPanel.jButton3ActionPerformed(null);
    }

    @Test
    public void testJButton3ActionPerformed() {
        updateUserPanel.jComboBox1.setSelectedIndex(1);
        updateUserPanel.jComboBox2ItemStateChanged(null);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jButton3ActionPerformed(new ActionEvent(new Object(), 0, "resetPassword"));
        assertTrue(updateUserPanel.jTextField1.getText().equals("John Doe"));
        assertTrue(updateUserPanel.jTextField2.getText().equals("john.doe@example.com"));
        assertTrue(updateUserPanel.jTextField3.getText().equals("1234567890"));
        assertTrue(updateUserPanel.jTextField4.getText().equals(""));
        assertTrue(updateUserPanel.jCheckBox1.isSelected());
        assertTrue(updateUserPanel.jCheckBox2.isSelected());
        assertTrue(updateUserPanel.jCheckBox3.isSelected());
    }

    @Test(expected = NullPointerException.class)
    public void testJButton4ActionPerformedWithNullAdminApp() {
        updateUserPanel = new UpdateUserPanel(adminApp, new ControlPanel());
        updateUserPanel.jButton4.setActionCommand("update");
        updateUserPanel.jButton4ActionPerformed(null);
    }

    @Test
    public void testJButton4ActionPerformedWithInvalidEmail() {
        updateUserPanel.jComboBox1.setSelectedIndex(1);
        updateUserPanel.jComboBox2ItemStateChanged(null);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField2.setText("example.com");
        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("New Job Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, "update"));
        assertEquals(1, group.getUsers().size());
        assertFalse(updateUserPanel.jComboBox2.isEnabled());
        assertFalse(updateUserPanel.jButton4.isEnabled());
        assertFalse(adminApp.getErrorMessage().isEmpty());
    }

    @Test
    public void testJButton4ActionPerformed() {
        updateUserPanel.jComboBox1.setSelectedIndex(1);
        updateUserPanel.jComboBox2ItemStateChanged(null);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField2.setText("new.email@example.com");
        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField3.setText("1234567891");
        updateUserPanel.jTextField4.setText("New Job Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, "update"));
        assertEquals(1, group.getUsers().size());
        assertTrue(updateUserPanel.jComboBox2.isEnabled());
        assertTrue(updateUserPanel.jButton4.isEnabled());
        assertFalse(adminApp.getErrorMessage().isEmpty());
        User updatedUser = group.getUsers().get(0);
        assertEquals("NewUserName", updatedUser.getName());
        assertEquals("new.email@example.com", updatedUser.getEmail());
        assertEquals("1234567891", updatedUser.getDirectPhone());
        assertEquals("New Job Title", updatedUser.getJobTitle());
        assertTrue(updatedUser.isShowMyStatus());
        assertTrue(updatedUser.isOnlyStartChat());
        assertTrue(updatedUser.isCanJoinChatRoom());
    }
}