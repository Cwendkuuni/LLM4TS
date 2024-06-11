package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import org.junit.Test;

public class UpdateUserPanelTest {

 UpdateUserPanel updateUserPanel;
 AdminApp adminApp = new AdminApp();
 ControlPanel controlPanel = new ControlPanel();

 @Test
 public void testUpdateModel() {
 adminApp.setGroups(new ArrayList<>());
 adminApp.getGroups().add(new Group("Group1", new ArrayList<>()));
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.updateModel();

 assertEquals("Group1", updateUserPanel.jComboBox1.getSelectedItem().toString());
 assertEquals("Size: 1", updateUserPanel.jComboBox2.getModel().getClass().getSimpleName());
 }

 @Test
 public void testUpdateModelWithEmptyGroups() {
 adminApp.setGroups(new ArrayList<>());
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.updateModel();

 assertEquals("", updateUserPanel.jComboBox1.getSelectedItem().toString());
 assertEquals("Size: 0", updateUserPanel.jComboBox2.getModel().getClass().getSimpleName());
 }

 @Test
 public void testUpdateModelWithEmptyUsers() {
 adminApp.setGroups(new ArrayList<>());
 adminApp.getGroups().add(new Group("Group1", new ArrayList<>()));
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);

 assertTrue(updateUserPanel.jTextField1.getText().isEmpty());
 assertTrue(updateUserPanel.jTextField2.getText().isEmpty());
 assertTrue(updateUserPanel.jTextField3.getText().isEmpty());
 assertTrue(updateUserPanel.jTextField4.getText().isEmpty());
 }

 @Test
 public void testUpdateModelWithUsers() {
 adminApp.setGroups(new ArrayList<>());
 adminApp.getGroups().add(new Group("Group1", new ArrayList<>()));
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);

 User user = new User("User1", "user1@example.com", "N/A", "N/A", false, false, false, "N/A");
 adminApp.getGroups().get(0).getUsers().add(user);

 String expectedName = user.getName();
 String expectedEmail = user.getEmail();
 String expectedDirectPhone = user.getDirectPhone();
 String expectedJobTitle = user.getJobTitle();
 boolean expectedShowMyStatus = user.isShowMyStatus();
 boolean expectedOnlyStartChat = user.isOnlyStartChat();
 boolean expectedCanJoinChatRoom = user.isCanJoinChatRoom();

 assertEquals(expectedName, updateUserPanel.jTextField1.getText());
 assertEquals(expectedEmail, updateUserPanel.jTextField2.getText());
 assertEquals(expectedDirectPhone, updateUserPanel.jTextField3.getText());
 assertEquals(expectedJobTitle, updateUserPanel.jTextField4.getText());
 assertEquals(expectedShowMyStatus, updateUserPanel.jCheckBox1.isSelected());
 assertEquals(expectedOnlyStartChat, updateUserPanel.jCheckBox2.isSelected());
 assertEquals(expectedCanJoinChatRoom, updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 adminApp.setGroups(new ArrayList<>());
 adminApp.getGroups().add(new Group("Group1", new ArrayList<>()));
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setModel(new DefaultComboBoxModel<User>(new ArrayList<>()));
 updateUserPanel.jComboBox1.addActionListener(e -> updateUserPanel.jComboBox1ItemStateChanged(e));
 updateUserPanel.jComboBox1ItemStateChanged(null);

 assertEquals("Size: 1", updateUserPanel.jComboBox2.getModel().getClass().getSimpleName());
 }

 @Test
 public void testJComboBox2ItemStateChanged() {
 adminApp.setGroups(new ArrayList<>());
 adminApp.getGroups().add(new Group("Group1", new ArrayList<>()));
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setModel(new DefaultComboBoxModel<User>(new ArrayList<>()));
 updateUserPanel.jComboBox2.addActionListener(e -> updateUserPanel.jComboBox2ItemStateChanged(e));
 updateUserPanel.jComboBox2ItemStateChanged(null);

 assertTrue(updateUserPanel.jTextField1.getText().isEmpty());
 assertTrue(updateUserPanel.jTextField2.getText().isEmpty());
 assertTrue(updateUserPanel.jTextField3.getText().isEmpty());
 assertTrue(updateUserPanel.jTextField4.getText().isEmpty());
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.jButton1.addActionListener(e -> updateUserPanel.jButton1ActionPerformed(e));
 updateUserPanel.jButton1ActionPerformed(null);

 assertTrue(updateUserPanel.controlPanel.isVisible());
 }

 @Test
 public void testJButton2ActionPerformed() {
 User userToDelete = new User("UserToDelete", "userToDelete@example.com", "N/A", "N/A", false, false, false, "N/A");
 adminApp.setGroups(new ArrayList<>());
 adminApp.getGroups().add(new Group("Group1", new ArrayList<User>() {{
 add(new User("User1", "user1@example.com", "N/A", "N/A", false, false, false, "N/A"));
 add(userToDelete);
 }}));
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(1);
 updateUserPanel.jButton2ActionPerformed(null);

 assertFalse(adminApp.getGroups().get(0).getUsers().contains(userToDelete));
 }

 @Test
 public void testJButton3ActionPerformed() {
 User userToResetPass = new User("UserToResetPass", "userToResetPass@example.com", "N/A", "N/A", false, false, false, "N/A");
 adminApp.setGroups(new ArrayList<>());
 adminApp.getGroups().add(new Group("Group1", new ArrayList<User>() {{
 add(new User("User1", "user1@example.com", "N/A", "N/A", false, false, false, "N/A"));
 add(userToResetPass);
 }}));
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(1);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("New Direct Phone");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3ActionPerformed(null);

 User user = adminApp.getGroups().get(0).getUsers().get(1);
 assertEquals("NewUserName", user.getName());
 assertEquals("new.user@example.com", user.getEmail());
 assertEquals("New Direct Phone", user.getDirectPhone());
 assertEquals("New Job Title", user.getJobTitle());
 assertTrue(user.isShowMyStatus());
 assertFalse(user.isOnlyStartChat());
 assertTrue(user.isCanJoinChatRoom());
 }

}