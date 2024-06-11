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
 User user = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
 group.addUser(user);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));

 updateUserPanel.updateModel();

 assertEquals("Group1", updateUserPanel.jComboBox1.getSelectedItem().toString());
 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 Group group1 = new Group("Group1");
 User user1 = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
 Group group2 = new Group("Group2");
 User user2 = new User("User2", "user2@example.com", "2345678901", "Title2", true, true, true);
 Group group3 = new Group("Group3");
 User user3 = new User("User3", "user3@example.com", "3456789012", "Title3", true, true, true);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group1, group2, group3)));

 updateUserPanel.updateModel();

 assertEquals("Group1", updateUserPanel.jComboBox1.getSelectedItem().toString());

 updateUserPanel.jComboBox1.setSelectedIndex(1);
 updateUserPanel.jComboBox1ItemStateChanged(null);

 assertEquals("Group2", updateUserPanel.jComboBox1.getSelectedItem().toString());
 assertEquals("User2", updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1ActionPerformed(new ActionEvent(updateUserPanel, 0, "Cancel", "Cancel", 0, new Object[0], null));

 assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible());
 }

 @Test
 public void testJButton2ActionPerformed() {
 Group group = new Group("Group1");
 User user = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
 group.addUser(user);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);

 updateUserPanel.jButton2ActionPerformed(new ActionEvent(updateUserPanel, 0, "Delete", "Delete", 0, new Object[0], null));

 assertEquals(0, group.getUsers().size());
 }

 @Test
 public void testJButton3ActionPerformed() {
 Group group = new Group("Group1");
 User user = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
 group.addUser(user);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);

 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("newuser@example.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("NewTitle");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(updateUserPanel, 0, "Reset Password", "Reset Password", 0, new Object[0], null));

 User newUser = updateUserPanel.jComboBox2.getSelectedItem();
 assertNotEquals(user, newUser);
 }

 @Test
 public void testJButton4ActionPerformed() {
 Group group = new Group("Group1");
 User user = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
 group.addUser(user);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);

 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("newuser@example.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("NewTitle");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(updateUserPanel, 0, "Update", "Update", 0, new Object[0], null));

 User newUser = updateUserPanel.jComboBox2.getSelectedItem();
 assertEquals("NewUserName", newUser.getName());
 assertEquals("newuser@example.com", newUser.getEmail());
 assertEquals("1234567891", newUser.getDirectPhone());
 assertEquals("NewTitle", newUser.getJobTitle());
 assertEquals(false, newUser.isShowMyStatus());
 assertEquals(false, newUser.isOnlyStartChat());
 assertEquals(false, newUser.isCanJoinChatRoom());
 }

}