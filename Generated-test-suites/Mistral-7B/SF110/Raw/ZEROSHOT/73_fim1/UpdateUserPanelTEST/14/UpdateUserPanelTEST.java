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
 Group group = new Group("Group1");
 adminApp.setGroups(new Vector<Group>());
 adminApp.getGroups().add(group);
 User user = new User("User1", "user1@example.com", "12345678", "N/A", true, true, true);
 group.setUsers(new Vector<User>());
 group.getUsers().add(user);
 updateUserPanel.updateModel();

 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 Group group1 = new Group("Group1");
 Group group2 = new Group("Group2");
 adminApp.setGroups(new Vector<Group>());
 adminApp.getGroups().add(group1);
 adminApp.getGroups().add(group2);
 User user1 = new User("User1", "user1@example.com", "12345678", "N/A", true, true, true);
 User user2 = new User("User2", "user2@example.com", "23456789", "N/A", true, true, true);
 group1.setUsers(new Vector<User>());
 group1.getUsers().add(user1);
 group2.setUsers(new Vector<User>());
 group2.getUsers().add(user2);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedItem(group1);

 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1ActionPerformed(new ActionEvent(updateUserPanel, 0, ""));
 assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible() == false);
 }

 @Test
 public void testJButton2ActionPerformed() {
 Group group = new Group("Group1");
 adminApp.setGroups(new Vector<Group>());
 adminApp.getGroups().add(group);
 User user = new User("User1", "user1@example.com", "12345678", "N/A", true, true, true);
 group.setUsers(new Vector<User>());
 group.getUsers().add(user);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2.setSelectedIndex(0);

 updateUserPanel.jButton2ActionPerformed(new ActionEvent(updateUserPanel, 0, ""));
 assertEquals(0, group.getUsers().size());
 }

 @Test
 public void testJButton3ActionPerformed() {
 Group group = new Group("Group1");
 adminApp.setGroups(new Vector<Group>());
 adminApp.getGroups().add(group);
 User user = new User("User1", "user1@example.com", "12345678", "N/A", true, true, true);
 group.setUsers(new Vector<User>());
 group.getUsers().add(user);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("123456789");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(updateUserPanel, 0, ""));

 User updatedUser = group.getUsers().get(0);
 assertEquals("NewUserName", updatedUser.getName());
 assertEquals("new.user@example.com", updatedUser.getEmail());
 assertEquals("123456789", updatedUser.getPassword());
 assertEquals("N/A", updatedUser.getDirectPhone());
 assertEquals("New Job Title", updatedUser.getJobTitle());
 assertEquals(false, updatedUser.isShowMyStatus());
 assertEquals(false, updatedUser.isOnlyStartChat());
 assertEquals(true, updatedUser.isCanJoinChatRoom());
 }

 @Test
 public void testJButton4ActionPerformed() {
 Group group = new Group("Group1");
 adminApp.setGroups(new Vector<Group>());
 adminApp.getGroups().add(group);
 User user = new User("User1", "user1@example.com", "12345678", "N/A", true, true, true);
 group.setUsers(new Vector<User>());
 group.getUsers().add(user);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("123456789");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(updateUserPanel, 0, ""));

 User updatedUser = adminApp.getUsers().get(0);
 assertEquals("NewUserName", updatedUser.getName());
 assertEquals("new.user@example.com", updatedUser.getEmail());
 assertEquals("123456789", updatedUser.getPassword());
 assertEquals("N/A", updatedUser.getDirectPhone());
 assertEquals("New Job Title", updatedUser.getJobTitle());
 assertEquals(false, updatedUser.isShowMyStatus());
 assertEquals(false, updatedUser.isOnlyStartChat());
 assertEquals(true, updatedUser.isCanJoinChatRoom());
 }
}