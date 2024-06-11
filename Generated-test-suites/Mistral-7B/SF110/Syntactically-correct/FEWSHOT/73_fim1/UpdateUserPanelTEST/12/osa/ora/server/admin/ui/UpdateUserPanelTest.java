package osa.ora.server.admin.ui;

import static org.junit.Assert.*;

import org.junit.Test;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

 UpdateUserPanel updateUserPanel;
 AdminApp adminApp = new AdminApp();
 ControlPanel controlPanel = new ControlPanel();

 @Test
 public void testUpdateUserPanel() {
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 assertNotNull(updateUserPanel);
 }

 @Test
 public void testUpdateModel() {
 Group group = new Group("Group1");
 User user1 = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
 User user2 = new User("User2", "user2@example.com", "2345678901", "Title2", true, true, true);
 user1.setGroup(group);
 group.getUsers().add(user1);
 group.getUsers().add(user2);
 adminApp.setGroups(new ArrayList<Group>() {{
 add(group);
 }});

 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.updateModel();

 assertEquals("Group1", updateUserPanel.jComboBox1.getSelectedItem().toString());
 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test
 public void testjComboBox1ItemStateChanged() {
 Group group = new Group("Group1");
 User user1 = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
 User user2 = new User("User2", "user2@example.com", "2345678901", "Title2", true, true, true);
 user1.setGroup(group);
 group.getUsers().add(user1);
 group.getUsers().add(user2);
 adminApp.setGroups(new ArrayList<Group>() {{
 add(group);
 }});

 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox1ItemStateChanged(null);

 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test
 public void testjComboBox2ItemStateChanged() {
 Group group = new Group("Group1");
 User user1 = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
 User user2 = new User("User2", "user2@example.com", "2345678901", "Title2", true, true, true);
 user1.setGroup(group);
 group.getUsers().add(user1);
 group.getUsers().add(user2);
 adminApp.setGroups(new ArrayList<Group>() {{
 add(group);
 }});

 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jComboBox2ItemStateChanged(null);

 assertEquals("User1", updateUserPanel.jTextField1.getText());
 assertEquals("user1@example.com", updateUserPanel.jTextField2.getText());
 }

 @Test
 public void testjButton1ActionPerformed() {
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.updateModel();
 updateUserPanel.jButton1.doClick();
 assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible());
 }

 @Test
 public void testjButton2ActionPerformed() throws Exception {
 Group group = new Group("Group1");
 User user1 = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
 User user2 = new User("User2", "user2@example.com", "2345678901", "Title2", true, true, true);
 user1.setGroup(group);
 group.getUsers().add(user1);
 group.getUsers().add(user2);
 adminApp.setGroups(new ArrayList<Group>() {{
 add(group);
 }});
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton2.doClick();
 assertTrue(group.getUsers().isEmpty());
 }

 @Test
 public void testjButton3ActionPerformed() throws Exception {
 Group group = new Group("Group1");
 User user = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
 user.setGroup(group);
 group.getUsers().add(user);
 adminApp.setGroups(new ArrayList<Group>() {{
 add(group);
 }});
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jButton3.doClick();
 assertEquals("NewUserName", user.getName());
 assertEquals("new.user@example.com", user.getEmail());
 }

 @Test
 public void testjButton4ActionPerformed() throws Exception {
 Group group = new Group("Group1");
 User user = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
 user.setGroup(group);
 group.getUsers().add(user);
 adminApp.setGroups(new ArrayList<Group>() {{
 add(group);
 }});
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("NewTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 assertEquals("NewUserName", user.getName());
 assertEquals("new.user@example.com", user.getEmail());
 assertEquals("1234567891", user.getDirectPhone());
 assertEquals("NewTitle", user.getJobTitle());
 assertTrue(user.isShowMyStatus());
 assertFalse(user.isOnlyStartChat());
 assertTrue(user.isCanJoinChatRoom());
 }
}