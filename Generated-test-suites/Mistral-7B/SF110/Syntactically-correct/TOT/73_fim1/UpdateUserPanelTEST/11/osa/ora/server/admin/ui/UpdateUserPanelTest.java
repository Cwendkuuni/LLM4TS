package osa.ora.server.admin.ui;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private UpdateUserPanel updateUserPanel;
 private Group group;
 private User user, userToDelete, userToReset;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 group = new Group("Group1");
 user = new User("User1", "user1@example.com", "123456789", "User1 Title", true, true, true, adminApp);
 userToDelete = new User("User2", "user2@example.com", "password123", "User2 Title", false, false, false, adminApp);
 userToReset = new User("User3", "user3@example.com", "password456", "User3 Title", false, false, false, adminApp);
 group.addUser(user);
 group.addUser(userToDelete);
 group.addUser(userToReset);
 adminApp.addGroup(group);
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 @Test
 public void testInitComponents() {
 assertEquals("Username:", updateUserPanel.jLabel1.getText());
 assertEquals("Login Email:", updateUserPanel.jLabel2.getText());
 assertEquals("Phone:", updateUserPanel.jLabel3.getText());
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 assertNotNull(updateUserPanel.jComboBox1);
 assertNotNull(updateUserPanel.jTextField1);
 assertNotNull(updateUserPanel.jTextField2);
 assertNotNull(updateUserPanel.jTextField3);
 }

 @Test
 public void testUpdateModel() {
 updateUserPanel.updateModel();
 assertEquals(2, updateUserPanel.jComboBox1.getModel().getSize());
 assertEquals(2, updateUserPanel.jComboBox2.getModel().getSize());
 assertEquals(user.getName(), updateUserPanel.jComboBox2.getItemAt(0).toString());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 updateUserPanel.jComboBox1.setSelectedIndex(1);
 updateUserPanel.jComboBox1ItemStateChanged(null);
 assertEquals(1, updateUserPanel.jComboBox2.getModel().getSize());
 assertEquals(userToDelete.getName(), updateUserPanel.jComboBox2.getItemAt(0).toString());
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1ActionPerformed(null);
 assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible());
 }

 @Test
 public void testJButton2ActionPerformedWhenUserIsNull() {
 updateUserPanel.jButton2ActionPerformed(null);
 assertFalse(updateUserPanel.controlPanel.getParentDialog().isVisible());
 }

 @Test
 public void testJButton2ActionPerformedWhenUserIsNotNull() {
 updateUserPanel.jComboBox2.setSelectedItem(userToDelete);
 updateUserPanel.jButton2ActionPerformed(null);
 assertFalse(updateUserPanel.controlPanel.getParentDialog().isVisible());
 assertEquals(1, group.getUsers().size());
 assertEquals("User Deleted Successfully!", updateUserPanel.adminApp.getLastMessage());
 }

 @Test
 public void testJButton2ActionPerformedWhenUserIndexIsMinusOne() {
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jButton2ActionPerformed(null);
 assertFalse(updateUserPanel.controlPanel.getParentDialog().isVisible());
 assertEquals("Can't identify selected User!", updateUserPanel.adminApp.getLastMessage());
 }

 @Test
 public void testJButton3ActionPerformedWhenUserIsNull() {
 updateUserPanel.jButton3ActionPerformed(null);
 assertFalse(updateUserPanel.controlPanel.getParentDialog().isVisible());
 }

 @Test
 public void testJButton3ActionPerformedWhenUserIsNotNull() {
 updateUserPanel.jComboBox2.setSelectedItem(userToReset);
 updateUserPanel.jButton3ActionPerformed(null);
 assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible());
 assertEquals("User Password Reset Successfully!", updateUserPanel.adminApp.getLastMessage());
 }

 @Test
 public void testJButton4ActionPerformedWhenUserIsNull() {
 updateUserPanel.jButton4ActionPerformed(null);
 assertFalse(updateUserPanel.controlPanel.getParentDialog().isVisible());
 }

 @Test
 public void testJButton4ActionPerformedWhenUserNameIsEmpty() {
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jButton4ActionPerformed(null);
 assertFalse(updateUserPanel.controlPanel.getParentDialog().isVisible());
 assertEquals("Please Enter User Name", updateUserPanel.adminApp.getLastMessage());
 }

 @Test
 public void testJButton4ActionPerformedWhenUserEmailIsEmpty() {
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jButton4ActionPerformed(null);
 assertFalse(updateUserPanel.controlPanel.getParentDialog().isVisible());
 assertEquals("Please Enter User email", updateUserPanel.adminApp.getLastMessage());
 }

 @Test
 public void testJButton4ActionPerformedWhenUserEmailIsAlreadyExist() {
 userToDelete.setEmail("user3@example.com");
 updateUserPanel.jTextField1.setText("User1");
 updateUserPanel.jTextField2.setText("user3@example.com");
 updateUserPanel.jButton4ActionPerformed(null);
 assertFalse(updateUserPanel.controlPanel.getParentDialog().isVisible());
 assertEquals("User with Same email Already Exist!", updateUserPanel.adminApp.getLastMessage());
 }

 @Test
 public void testJButton4ActionPerformedWhenUserIsUpdated() {
 updateUserPanel.jTextField1.setText("UpdatedUser");
 updateUserPanel.jTextField2.setText("updateduser@example.com");
 updateUserPanel.jTextField3.setText("1111111111");
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jButton4ActionPerformed(null);
 assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible());
 assertEquals("User Updated Successfully!", updateUserPanel.adminApp.getLastMessage());
 }
}