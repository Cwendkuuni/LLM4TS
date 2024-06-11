package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
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
 Group group = new Group("Group1");
 adminApp.addGroup(group);
 User user1 = new User("User1", "user1@example.com", "1234567890", true, true, true);
 user1.setJobTitle("Job Title1");
 user1.setShowMyStatus(true);
 user1.setOnlyStartChat(true);
 user1.setCanJoinChatRoom(true);
 group.addUser(user1);
 adminApp.addGroup(group);

 updateUserPanel.updateModel();

 assertEquals("Group1", updateUserPanel.jComboBox1.getSelectedItem().toString());
 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test
 public void testjComboBox1ItemStateChanged() {
 Group group = new Group("Group1");
 adminApp.addGroup(group);
 User user1 = new User("User1", "user1@example.com", "1234567890", true, true, true);
 user1.setJobTitle("Job Title1");
 user1.setShowMyStatus(true);
 user1.setOnlyStartChat(true);
 user1.setCanJoinChatRoom(true);
 group.addUser(user1);
 adminApp.addGroup(group);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);

 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test
 public void testjButton1ActionPerformed() {
 updateUserPanel.jButton1ActionPerformed(new ActionEvent(new Object(), 0, "Cancel"));
 assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible());
 }

 @Test
 public void testjButton2ActionPerformed() {
 Group group = new Group("Group1");
 adminApp.addGroup(group);
 User user1 = new User("User1", "user1@example.com", "1234567890", true, true, true);
 user1.setJobTitle("Job Title1");
 user1.setShowMyStatus(true);
 user1.setOnlyStartChat(true);
 user1.setCanJoinChatRoom(true);
 group.addUser(user1);
 adminApp.addGroup(group);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);

 updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 0, "Delete"));
 assertEquals(0, group.getUsers().size());
 assertNull(updateUserPanel.jComboBox2.getSelectedItem());
 }

 @Test
 public void testjButton3ActionPerformed() {
 Group group = new Group("Group1");
 adminApp.addGroup(group);
 User user1 = new User("User1", "user1@example.com", "1234567890", true, true, true);
 user1.setJobTitle("Job Title1");
 user1.setShowMyStatus(true);
 user1.setOnlyStartChat(true);
 user1.setCanJoinChatRoom(true);
 group.addUser(user1);
 adminApp.addGroup(group);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);

 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(new Object(), 0, "Reset Password"));
 User user = group.getUsers().get(0);
 assertEquals("New User Name", user.getName());
 assertEquals("new.user@example.com", user.getEmail());
 }

 @Test
 public void testjButton4ActionPerformed() {
 Group group = new Group("Group1");
 adminApp.addGroup(group);
 User user1 = new User("User1", "user1@example.com", "1234567890", true, true, true);
 user1.setJobTitle("Job Title1");
 user1.setShowMyStatus(true);
 user1.setOnlyStartChat(true);
 user1.setCanJoinChatRoom(true);
 group.addUser(user1);
 adminApp.addGroup(group);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);

 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("New Phone");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, "Update"));
 User user = group.getUsers().get(0);
 assertEquals("New User Name", user.getName());
 assertEquals("new.user@example.com", user.getEmail());
 assertEquals("New Phone", user.getDirectPhone());
 assertEquals("New Job Title", user.getJobTitle());
 assertFalse(user.isShowMyStatus());
 assertFalse(user.isOnlyStartChat());
 assertFalse(user.isCanJoinChatRoom());
 }

}