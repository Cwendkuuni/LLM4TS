package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

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
 User user = new User("User1", "user1@example.com", "1234567890", "Job Title1", true, true, true);
 group.addUser(user);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
 updateUserPanel.updateModel();
 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().getName());
 }

 @Test
 public void testInitComponents() {
 // Check if components are added to the panel
 assertEquals(13, updateUserPanel.getComponentCount());
 // Check if labels are displayed correctly
 assertEquals("Username:", updateUserPanel.jLabel1.getText());
 assertEquals("Login Email:", updateUserPanel.jLabel2.getText());
 assertEquals("Phone:", updateUserPanel.jLabel3.getText());
 // Check if checkboxes are initially unselected
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testReset() {
 updateUserPanel.reset();
 assertTrue(updateUserPanel.jTextField1.getText().isEmpty());
 assertTrue(updateUserPanel.jTextField2.getText().isEmpty());
 assertTrue(updateUserPanel.jTextField3.getText().isEmpty());
 assertTrue(updateUserPanel.jTextField4.getText().isEmpty());
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 Group group = new Group("Group1");
 User user1 = new User("User1", "user1@example.com", "1234567890", "Job Title1", true, true, true);
 User user2 = new User("User2", "user2@example.com", "2345678901", "Job Title2", false, false, false);
 group.addUser(user1);
 group.addUser(user2);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox1ItemStateChanged(null);
 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().getName());
 // Test when no users in the group
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox1ItemStateChanged(null);
 assertTrue(updateUserPanel.jTextField1.getText().isEmpty());
 assertTrue(updateUserPanel.jTextField2.getText().isEmpty());
 assertTrue(updateUserPanel.jTextField3.getText().isEmpty());
 assertTrue(updateUserPanel.jTextField4.getText().isEmpty());
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testJComboBox2ItemStateChanged() {
 User user = new User("User1", "user1@example.com", "1234567890", "Job Title1", true, true, true);
 updateUserPanel.jComboBox2.setModel(new DefaultComboBoxModel<User>(Arrays.asList(user)));
 updateUserPanel.jComboBox2ItemStateChanged(null);
 assertEquals(user.getName(), updateUserPanel.jTextField1.getText());
 assertEquals(user.getEmail(), updateUserPanel.jTextField2.getText());
 assertFalse(updateUserPanel.jTextField3.getText().isEmpty());
 assertEquals(user.getDirectPhone(), updateUserPanel.jTextField3.getText());
 assertEquals(user.isShowMyStatus(), updateUserPanel.jCheckBox1.isSelected());
 assertEquals(user.isOnlyStartChat(), updateUserPanel.jCheckBox2.isSelected());
 assertEquals(user.isCanJoinChatRoom(), updateUserPanel.jCheckBox3.isSelected());
 }

 @Test(expected = NullPointerException.class)
 public void testJButton2ActionPerformedWithNullUser() {
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(new JButton(), 0, ""));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testJButton2ActionPerformedWithInvalidIndex() {
 Group group = new Group("Group1");
 User user = new User("User1", "user1@example.com", "1234567890", "Job Title1", true, true, true);
 group.addUser(user);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox1ItemStateChanged(null);
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(new JButton(), 0, ""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton3ActionPerformedWithNullUser() {
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(new JButton(), 0, ""));
 }

 @Test
 public void testJButton3ActionPerformed() {
 User user = new User("User1", "user1@example.com", "1234567890", "Job Title1", true, true, true);
 updateUserPanel.jComboBox2.setModel(new DefaultComboBoxModel<User>(Arrays.asList(user)));
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(new JButton(), 0, ""));
 assertTrue(user.getName().equals(updateUserPanel.jTextField1.getText()));
 assertTrue(user.getEmail().equals(updateUserPanel.jTextField2.getText()));
 assertTrue(updateUserPanel.jTextField3.getText().equals("1234567890"));
 assertTrue(user.isShowMyStatus() == updateUserPanel.jCheckBox1.isSelected());
 assertTrue(user.isOnlyStartChat() == updateUserPanel.jCheckBox2.isSelected());
 assertTrue(user.isCanJoinChatRoom() == updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testJButton4ActionPerformed() {
 Group group = new Group("Group1");
 User user = new User("User1", "user1@example.com", "1234567890", "Job Title1", true, true, true);
 group.addUser(user);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox1ItemStateChanged(null);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new JButton(), 0, ""));
 User newUser = group.getUsers().get(0);
 assertEquals("NewUserName", newUser.getName());
 assertEquals("new.user@example.com", newUser.getEmail());
 assertEquals("1234567891", newUser.getDirectPhone());
 assertEquals("New Job Title", newUser.getJobTitle());
 assertTrue(newUser.isShowMyStatus() == updateUserPanel.jCheckBox1.isSelected());
 assertTrue(newUser.isOnlyStartChat() != updateUserPanel.jCheckBox2.isSelected());
 assertTrue(newUser.isCanJoinChatRoom() == updateUserPanel.jCheckBox3.isSelected());
 }
}