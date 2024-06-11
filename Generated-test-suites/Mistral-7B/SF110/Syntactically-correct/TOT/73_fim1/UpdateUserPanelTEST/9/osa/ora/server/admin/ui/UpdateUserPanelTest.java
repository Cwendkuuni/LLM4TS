package osa.ora.server.admin.ui;

import org.junit.Before;
import org.junit.Test;
import osa.ora.server.admin.AdminApp;
import osa.ora.server.admin.Group;
import osa.ora.server.admin.User;
import static org.junit.Assert.*;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private UpdateUserPanel updateUserPanel;
 private Group group;
 private User user;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 group = new Group("Group1");
 user = new User("User1", "user1@example.com", "1234567890", "Job Title", true, true, true);
 group.addUser(user);
 adminApp.setGroups(Arrays.asList(group));
 updateUserPanel = new UpdateUserPanel(adminApp, new ControlPanel());
 }

 @Test
 public void testUpdateUserPanelInit() {
 // Test if the components are initialized correctly
 assertEquals("Username:", updateUserPanel.jLabel1.getText());
 assertEquals("Login Email:", updateUserPanel.jLabel2.getText());
 assertEquals("Phone:", updateUserPanel.jLabel3.getText());
 // ... add more assertions for other components here

 // Test if the combo boxes are initialized correctly
 assertEquals("Item 1", updateUserPanel.jComboBox1.getModel().getElementAt(0));
 assertEquals("Item 1", updateUserPanel.jComboBox2.getModel().getElementAt(0));
 }

 @Test
 public void testUpdateUserPanelUpdateModel() {
 // Test if the updateModel method updates the combo boxes correctly
 updateUserPanel.updateModel();
 assertEquals(group.getUsers().get(0), updateUserPanel.jComboBox2.getSelectedItem());
 }

 @Test
 public void testUpdateUserPanelJComboBox1ItemStateChanged() {
 // Test if the jComboBox1ItemStateChanged method updates the combo boxes correctly
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(1);
 updateUserPanel.jComboBox1ItemStateChanged(null);
 assertEquals(group.getUsers().get(1), updateUserPanel.jComboBox2.getSelectedItem());
 }

 @Test
 public void testUpdateUserPanelButton1ActionPerformed() {
 // Test if the jButton1ActionPerformed method hides the dialog correctly
 updateUserPanel.jButton1.doClick();
 assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible() == false);
 }

 @Test
 public void testUpdateUserPanelButton2ActionPerformed() {
 // Test if the jButton2ActionPerformed method deletes the user correctly
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton2.doClick();
 assertFalse(group.getUsers().contains(user));
 }

 @Test
 public void testUpdateUserPanelButton3ActionPerformed() {
 // Test if the jButton3ActionPerformed method resets the user password correctly
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton3.doClick();
 assertTrue(user.getPassword().equals("new_password"));
 }

 @Test
 public void testUpdateUserPanelButton4ActionPerformed() {
 // Test if the jButton4ActionPerformed method updates the user correctly
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("New User Email");
 updateUserPanel.jTextField3.setText("New Phone Number");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 assertEquals("New User Name", user.getName());
 assertEquals("New User Email", user.getEmail());
 assertEquals("New Phone Number", user.getDirectPhone());
 assertEquals("New Job Title", user.getJobTitle());
 assertTrue(user.isShowMyStatus());
 assertFalse(user.isOnlyStartChat());
 assertTrue(user.isCanJoinChatRoom());
 }

 @Test(expected = IllegalStateException.class)
 public void testUpdateUserPanelButton4ActionPerformedInvalidInputs() {
 // Test if the jButton4ActionPerformed method throws an exception for invalid inputs
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jButton4.doClick();
 }

 @Test(expected = IllegalStateException.class)
 public void testUpdateUserPanelButton4ActionPerformedSameEmail() {
 // Test if the jButton4ActionPerformed method throws an exception for the same email
 User user2 = new User("User2", "user1@example.com", "password", "Job Title", true, true, true);
 group.addUser(user2);
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(1);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("user1@example.com");
 updateUserPanel.jTextField3.setText("New Phone Number");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 }
}