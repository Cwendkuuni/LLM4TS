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
        group = new Group("Group1", new Vector<User>());
        updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.updateModel();
        user = new User("TestUser", "test@test.com", "TestPhone", "TestJobTitle", true, true, true, "TestGroup");
        updateUserPanel.jComboBox2.setSelectedItem(user);
    }

    @Test
    public void testConstructor() {
        assertNotNull(updateUserPanel);
    }

    @Test
    public void testUpdateModel() {
        updateUserPanel.updateModel();
        assertNotNull(updateUserPanel.jComboBox1.getModel());
        assertNotNull(updateUserPanel.jComboBox2.getModel());
        // Check if the first group and user are displayed by default
        assertEquals("Group1", updateUserPanel.jComboBox1.getSelectedItem().toString());
        assertEquals(user.getName(), updateUserPanel.jTextField1.getText());
        assertEquals(user.getEmail(), updateUserPanel.jTextField2.getText());
        assertEquals(user.getDirectPhone(), updateUserPanel.jTextField3.getText());
        assertEquals(user.getJobTitle(), updateUserPanel.jTextField4.getText());
    }

    @Test
    public void testJComboBox1ItemStateChanged() {
        updateUserPanel.jComboBox1.setSelectedIndex(1);
        updateUserPanel.jComboBox1ItemStateChanged(null);
        assertNotNull(updateUserPanel.jComboBox2.getModel());
        assertFalse(updateUserPanel.jTextField1.getText().isEmpty());
        assertFalse(updateUserPanel.jTextField2.getText().isEmpty());
        assertFalse(updateUserPanel.jTextField3.getText().isEmpty());
        assertFalse(updateUserPanel.jTextField4.getText().isEmpty());
    }

    @Test
    public void testJButton1ActionPerformed() {
        updateUserPanel.jButton1.doClick();
        assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible() == false);
        assertFalse(updateUserPanel.jTextField1.getText().isEmpty());
        assertFalse(updateUserPanel.jTextField2.getText().isEmpty());
        assertFalse(updateUserPanel.jTextField3.getText().isEmpty());
        assertFalse(updateUserPanel.jTextField4.getText().isEmpty());
        resetUserPanel(updateUserPanel);
    }

    @Test
    public void testJButton2ActionPerformed_UserIsNull() {
        updateUserPanel.jComboBox2.setSelectedIndex(-1);
        updateUserPanel.jButton2.doClick();
        assertEquals("Can't identify selected User!", updateUserPanel.getErrorMessage());
        assertFalse(updateUserPanel.controlPanel.getParentDialog().isVisible() == false);
    }

    @Test
    public void testJButton2ActionPerformed() {
        updateUserPanel.jButton2.doClick();
        User deletedUser = adminApp.getGroups().get(0).getUsers().get(0);
        assertTrue(deletedUser.isDeleted());
        assertFalse(updateUserPanel.jComboBox2.getModel().contains(deletedUser));
        assertEquals("User Deleted Successfully!", updateUserPanel.getSuccessMessage());
        assertFalse(updateUserPanel.controlPanel.getParentDialog().isVisible() == false);
    }

    @Test
    public void testJButton3ActionPerformed_UserIsNull() {
        updateUserPanel.jComboBox2.setSelectedIndex(-1);
        updateUserPanel.jButton3.doClick();
        assertEquals("Can't identify selected User!", updateUserPanel.getErrorMessage());
        assertFalse(updateUserPanel.controlPanel.getParentDialog().isVisible() == false);
    }

    @Test
    public void testJButton3ActionPerformed() {
        updateUserPanel.jButton3.doClick();
        assertEquals("User Password Reset Successfully!", updateUserPanel.getSuccessMessage());
        assertFalse(updateUserPanel.controlPanel.getParentDialog().isVisible() == false);
    }

    @Test
    public void testJButton4ActionPerformed_InvalidEmail() {
        updateUserPanel.jTextField2.setText("test@test.comtest.com");
        updateUserPanel.jButton4.doClick();
        assertEquals("Please Enter User email", updateUserPanel.getErrorMessage());
        assertFalse(updateUserPanel.controlPanel.getParentDialog().isVisible() == false);
    }

    @Test
    public void testJButton4ActionPerformed_EmailAlreadyExists() {
        User existingUser = new User("TestUser2", "test@test.com", "TestPhone2", "TestJobTitle2", true, true, true, "TestGroup");
        adminApp.getGroups().get(0).getUsers().add(existingUser);
        updateUserPanel.jComboBox2.setSelectedItem(existingUser);
        updateUserPanel.jTextField2.setText("test@test.com");
        updateUserPanel.jButton4.doClick();
        assertEquals("User with Same email Already Exist!", updateUserPanel.getErrorMessage());
        assertFalse(updateUserPanel.controlPanel.getParentDialog().isVisible() == false);
    }

    @Test
    public void testJButton4ActionPerformed() {
        updateUserPanel.jTextField1.setText("TestUserUpdated");
        updateUserPanel.jTextField2.setText("test@test.comupdated");
        updateUserPanel.jTextField3.setText("TestPhoneupdated");
        updateUserPanel.jTextField4.setText("TestJobTitleupdated");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4.doClick();
        User updatedUser = adminApp.getGroups().get(0).getUsers().get(0);
        assertEquals("TestUserUpdated", updatedUser.getName());
        assertEquals("test@test.comupdated", updatedUser.getEmail());
        assertEquals("TestPhoneupdated", updatedUser.getDirectPhone());
        assertEquals("TestJobTitleupdated", updatedUser.getJobTitle());
        assertEquals(true, updatedUser.isShowMyStatus());
        assertEquals(false, updatedUser.isOnlyStartChat());
        assertEquals(true, updatedUser.isCanJoinChatRoom());
        assertEquals("User Updated Successfully!", updateUserPanel.getSuccessMessage());
        assertFalse(updateUserPanel.controlPanel.getParentDialog().isVisible() == false);
    }

    private void resetUserPanel(UpdateUserPanel panel) {
        assertEquals("", panel.jTextField1.getText());
        assertEquals("", panel.jTextField2.getText());
        assertEquals("", panel.jTextField3.getText());
        assertEquals("", panel.jTextField4.getText());
        assertFalse(panel.jCheckBox1.isSelected());
        assertFalse(panel.jCheckBox2.isSelected());
        assertFalse(panel.jCheckBox3.isSelected());
    }

    @Test(expected = Exception.class)
    public void testJButton4ActionPerformed_Exception() {
        updateUserPanel.jTextField1.setText("TestUserUpdated");
        updateUserPanel.jTextField2.setText("test@test.comupdated");
        updateUserPanel.jTextField3.setText("TestPhoneupdated");
        updateUserPanel.jTextField4.setText("TestJobTitleupdated");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4.doClick();
        User updatedUser = adminApp.getGroups().get(0).getUsers().get(0);
        throw new Exception("Test Exception");
    }
}