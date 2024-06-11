package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import org.junit.Test;

public class UpdateUserPanelTest {

 private UpdateUserPanel updateUserPanel;
 private AdminApp adminApp;
 private ControlPanel controlPanel;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 controlPanel = new ControlPanel();
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 }

 @Test
 public void testUpdateModel() {
 // Test case for empty adminApp.getGroups()
 updateUserPanel.updateModel();
 assertEquals(0, updateUserPanel.jComboBox1.getItemCount());

 // Test case for non-empty adminApp.getGroups()
 adminApp.addGroup("Group1");
 adminApp.addUserToGroup("Group1", new User("User1", "user1@example.com", "123456789"));
 updateUserPanel.updateModel();
 assertEquals(1, updateUserPanel.jComboBox1.getItemCount());
 assertEquals("Group1", updateUserPanel.jComboBox1.getItemAt(0).toString());

 adminApp.addGroup("Group2");
 updateUserPanel.updateModel();
 assertEquals(2, updateUserPanel.jComboBox1.getItemCount());

 // Test case for non-empty group with non-empty users
 Group group = adminApp.getGroup("Group1");
 updateUserPanel.updateModel();
 assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
 assertEquals("User1", updateUserPanel.jComboBox2.getItemAt(0).toString());
 }

 @Test
 public void testInitComponents() {
 // Test case for labels
 assertEquals("Username:", updateUserPanel.jLabel1.getText());
 assertEquals("Login Email:", updateUserPanel.jLabel2.getText());
 assertEquals("Phone:", updateUserPanel.jLabel3.getText());
 assertEquals("User Group:", updateUserPanel.jLabel4.getText());

 // Test case for checkboxes
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());

 // Test case for text fields
 assertEquals("", updateUserPanel.jTextField1.getText());
 assertEquals("", updateUserPanel.jTextField2.getText());
 assertEquals("", updateUserPanel.jTextField3.getText());
 assertEquals("", updateUserPanel.jTextField4.getText());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 Group group = new Group("Group1");
 adminApp.addGroup(group);
 User user = new User("User1", "user1@example.com", "123456789");
 user.setGroupName(group);
 adminApp.addUser(user);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox1ItemStateChanged(null);

 assertEquals("User1", updateUserPanel.jComboBox2.getItemAt(0).toString());
 assertEquals("User1", updateUserPanel.fillUser().getName());
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1ActionPerformed(null);
 assertTrue(updateUserPanel.controlPanel.isVisible());
 }

 @Test
 public void testJButton2ActionPerformed() {
 // Test case for null user or index == -1
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jButton2ActionPerformed(null);
 assertFalse(updateUserPanel.isVisible());

 // Test case for valid user deletion
 Group group = new Group("Group1");
 adminApp.addGroup(group);
 User user1 = new User("User1", "user1@example.com", "123456789");
 user1.setGroupName(group);
 adminApp.addUser(user1);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton2ActionPerformed(null);
 assertEquals(0, group.getUsers().size());
 assertEquals(0, updateUserPanel.jComboBox2.getItemCount());
 }

 @Test
 public void testJButton3ActionPerformed() {
 // Test case for null user
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jButton3ActionPerformed(null);
 assertFalse(updateUserPanel.isVisible());

 // Test case for valid user password reset
 Group group = new Group("Group1");
 adminApp.addGroup(group);
 User user = new User("User1", "user1@example.com", "123456789");
 user.setGroupName(group);
 adminApp.addUser(user);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton3ActionPerformed(null);
 assertEquals("Password reset successfully!", updateUserPanel.adminApp.getLastMessage());
 }

 @Test
 public void testJButton4ActionPerformed() {
 // Test case for null user
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jButton4ActionPerformed(null);
 assertFalse(updateUserPanel.isVisible());

 // Test case for invalid email
 updateUserPanel.jTextField2.setText("user1@example.comuser1@example.com");
 updateUserPanel.jButton4ActionPerformed(null);
 assertFalse(updateUserPanel.isVisible());

 // Test case for valid user update
 Group group = new Group("Group1");
 adminApp.addGroup(group);
 User user = new User("User1", "user1@example.com", "123456789");
 user.setGroupName(group);
 adminApp.addUser(user);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("User2");
 updateUserPanel.jTextField2.setText("user2@example.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("Developer");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(null);
 assertEquals("User2", user.getName());
 assertEquals("user2@example.com", user.getEmail());
 assertEquals("1234567890", user.getDirectPhone());
 assertEquals("Developer", user.getJobTitle());
 assertEquals(true, user.isShowMyStatus());
 assertEquals(true, user.isOnlyStartChat());
 assertEquals(true, user.isCanJoinChatRoom());
 }

 @After
 public void tearDown() {
 adminApp = null;
 controlPanel = null;
 updateUserPanel = null;
 }
}