package osa.ora.server.admin.ui;

import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

 UpdateUserPanel updateUserPanel = null;
 AdminApp adminApp = null;
 ControlPanel controlPanel = null;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 controlPanel = new ControlPanel();
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 }

 @Test
 public void testUpdateModel() {
 Group group = new Group("Group1", new Vector<>());
 adminApp.setGroups(new ArrayList<>(Arrays.asList(group)));
 updateUserPanel.updateModel();
 assertEquals(1, updateUserPanel.jComboBox1.getItemCount());
 Group groupFromJComboBox = (Group) updateUserPanel.jComboBox1.getSelectedItem();
 assertEquals("Group1", groupFromJComboBox.getName());
 User user = new User("User1", "user1@example.com", "123456789", "N/A", false, false, false);
 group.getUsers().add(user);
 updateUserPanel.updateModel();
 assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
 User userFromJComboBox = (User) updateUserPanel.jComboBox2.getSelectedItem();
 assertEquals("User1", userFromJComboBox.getName());
 }

 @Test
 public void testjButton1ActionPerformed() {
 updateUserPanel.jButton1ActionPerformed(new ActionEvent(new JButton(), 0, "Cancel"));
 assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible());
 }

 @Test
 public void testjButton2ActionPerformed() {
 Group group = new Group("Group1", new Vector<>());
 User user = new User("User1", "user1@example.com", "123456789", "N/A", false, false, false);
 group.getUsers().add(user);
 adminApp.setGroups(new ArrayList<>(Arrays.asList(group)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField2.setText("user1@example.com");
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(new JButton(), 0, "Delete"));
 assertEquals(0, group.getUsers().size());
 assertNull(updateUserPanel.jComboBox2.getSelectedItem());
 }

 @Test
 public void testjButton3ActionPerformed() {
 Group group = new Group("Group1", new Vector<>());
 User user = new User("User1", "user1@example.com", "123456789", "N/A", false, false, false);
 group.getUsers().add(user);
 adminApp.setGroups(new ArrayList<>(Arrays.asList(group)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(new JButton(), 0, "Reset Password"));
 assertEquals("NewUserName", user.getName());
 assertEquals("new.user@example.com", user.getEmail());
 assertEquals("1234567890", user.getDirectPhone());
 }

 @Test
 public void testjButton4ActionPerformed() {
 Group group = new Group("Group1", new Vector<>());
 User user = new User("User1", "user1@example.com", "123456789", "N/A", false, false, false);
 group.getUsers().add(user);
 adminApp.setGroups(new ArrayList<>(Arrays.asList(group)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("Software Tester");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new JButton(), 0, "Update"));
 assertEquals("NewUserName", user.getName());
 assertEquals("new.user@example.com", user.getEmail());
 assertEquals("1234567890", user.getDirectPhone());
 assertEquals("Software Tester", user.getJobTitle());
 assertEquals(true, user.isShowMyStatus());
 assertEquals(false, user.isOnlyStartChat());
 assertEquals(true, user.isCanJoinChatRoom());
 }

 @Test
 public void testjComboBox1ItemStateChanged() {
 Group group1 = new Group("Group1", new Vector<>());
 User user1 = new User("User1", "user1@example.com", "123456789", "N/A", false, false, false);
 User user2 = new User("User2", "user2@example.com", "234567890", "N/A", false, false, false);
 group1.getUsers().add(user1);
 group1.getUsers().add(user2);
 adminApp.setGroups(new ArrayList<>(Arrays.asList(group1)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, 0, -1, -1, null));
 assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
 updateUserPanel.jComboBox1.setSelectedIndex(1);
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, 0, -1, -1, null));
 assertEquals(0, updateUserPanel.jComboBox2.getItemCount());
 }
}