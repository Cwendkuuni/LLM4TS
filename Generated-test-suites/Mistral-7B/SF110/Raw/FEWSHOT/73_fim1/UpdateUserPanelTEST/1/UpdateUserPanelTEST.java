package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

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
 Group group = new Group("TestGroup");
 adminApp.addGroup(group);
 User user = new User("TestUser", "test@example.com", "1234567890");
 group.addUser(user);
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.updateModel();

 assertEquals("TestUser", updateUserPanel.jComboBox2.getSelectedItem().getName());
 assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
 }

 @Test
 public void testjComboBox1ItemStateChanged() {
 Group group = new Group("TestGroup");
 adminApp.addGroup(group);
 User user1 = new User("User1", "user1@example.com", "password1");
 User user2 = new User("User2", "user2@example.com", "password2");
 group.addUser(user1);
 group.addUser(user2);
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.updateModel();

 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox1ItemStateChanged(null);

 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().getName());
 assertEquals(2, updateUserPanel.jComboBox2.getItemCount());
 }

 @Test
 public void testjButton1ActionPerformed() {
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.jButton1ActionPerformed(null);

 assertTrue(controlPanel.getParentDialog().isVisible());
 }

 @Test
 public void testjButton2ActionPerformed() throws Exception {
 Group group = new Group("TestGroup");
 adminApp.addGroup(group);
 User user = new User("TestUser", "test@example.com", "password");
 group.addUser(user);
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jButton2ActionPerformed(null);

 assertFalse(group.getUsers().contains(user));
 }

 @Test
 public void testjButton3ActionPerformed() throws Exception {
 Group group = new Group("TestGroup");
 adminApp.addGroup(group);
 User user = new User("TestUser", "test@example.com", "password");
 group.addUser(user);
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.test@example.com");
 updateUserPanel.jButton3ActionPerformed(null);

 assertEquals("NewUserName", user.getName());
 assertEquals("new.test@example.com", user.getEmail());
 }

 @Test
 public void testjButton4ActionPerformed() throws Exception {
 Group group = new Group("TestGroup");
 adminApp.addGroup(group);
 User user = new User("TestUser", "test@example.com", "password");
 group.addUser(user);
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.test@example.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("NewJobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(null);

 assertEquals("NewUserName", user.getName());
 assertEquals("new.test@example.com", user.getEmail());
 assertEquals("1234567891", user.getDirectPhone());
 assertEquals("NewJobTitle", user.getJobTitle());
 assertTrue(user.isShowMyStatus());
 assertFalse(user.isOnlyStartChat());
 assertTrue(user.isCanJoinChatRoom());
 }
}