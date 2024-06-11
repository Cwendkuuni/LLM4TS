package osa.ora.server.admin.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import osa.ora.server.admin. AdminApp;
import osa.ora.server.admin.UpdateUserPanel;
import osa.ora.server.beans.Group;
import osa.ora.server.beans.User;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private UpdateUserPanel updateUserPanel;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 @Test
 public void testUpdateModelWhenGroupsAndUsersArePresent() {
 Vector<Group> groups = new Vector<>();
 Group group = new Group("Group 1", new Vector<User>());
 groups.add(group);
 adminApp.setGroups(groups);

 Vector<User> users = new Vector<>();
 User user = new User("User 1", "user1@example.com", "1234567890", "title 1", true, true, true, "User 1");
 users.add(user);
 group.setUsers(users);

 updateUserPanel.updateModel();

 assertEquals("Group 1", updateUserPanel.jComboBox1.getSelectedItem().toString());
 assertEquals("User 1", updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test
 public void testUpdateModelWhenGroupsArePresentButNoUsers() {
 Vector<Group> groups = new Vector<>();
 Group group = new Group("Group 1", new Vector<User>());
 groups.add(group);
 adminApp.setGroups(groups);

 updateUserPanel.updateModel();

 assertNull(updateUserPanel.jComboBox2.getSelectedItem());
 }

 @Test
 public void testUpdateModelWhenGroupsAreNotPresent() {
 adminApp.setGroups(null);

 updateUserPanel.updateModel();

 assertNull(updateUserPanel.jComboBox1.getSelectedItem());
 assertNull(updateUserPanel.jComboBox2.getSelectedItem());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 Vector<Group> groups = new Vector<>();
 Group group1 = new Group("Group 1", new Vector<User>());
 Group group2 = new Group("Group 2", new Vector<User>());
 groups.add(group1);
 groups.add(group2);
 adminApp.setGroups(groups);

 Vector<User> users = new Vector<>();
 User user1 = new User("User 1", "user1@example.com", "1234567890", "title 1", true, true, true, "User 1");
 User user2 = new User("User 2", "user2@example.com", "1234567890", "title 2", true, true, true, "User 2");
 group1.setUsers(users);
 group1.getUsers().add(user1);
 group1.getUsers().add(user2);

 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedItem("Group 1");
 updateUserPanel.jComboBox1ItemStateChanged(null);

 assertEquals("User 1", updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test
 public void testJButton3ActionPerformedWithValidValues() {
 User user = new User("User 1", "user1@example.com", "1234567890", "title 1", true, true, true, "User 1");
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("User Name");
 updateUserPanel.jTextField2.setText("user2@example.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("title 2");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3ActionPerformed(null);

 User updatedUser = new User("User Name", "user2@example.com", "1234567891", "title 2", true, false, true, null);
 assertEquals(updatedUser, adminApp.getUsers().get(adminApp.getUsers().size() - 1));
 }

 @Test
 public void testJButton3ActionPerformedWithEmptyValues() {
 updateUserPanel.jComboBox2.setSelectedItem(new User("User 1", "user1@example.com", "1234567890", "title 1", true, true, true, "User 1"));
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jTextField3.setText("");
 updateUserPanel.jTextField4.setText("");
 updateUserPanel.jButton3ActionPerformed(null);

 assertNull(adminApp.getUsers().get(adminApp.getUsers().size() - 1));
 }

 @Test
 public void testJButton3ActionPerformedWithDuplicateEmail() {
 Vector<User> users = new Vector<>();
 User user1 = new User("User 1", "user1@example.com", "1234567890", "title 1", true, true, true, "User 1");
 User user2 = new User("User 2", "user1@example.com", "1234567890", "title 2", true, true, true, "User 2");
 users.add(user1);
 users.add(user2);
 adminApp.setUsers(users);

 updateUserPanel.jComboBox2.setSelectedItem(user1);
 updateUserPanel.jTextField1.setText("User Name");
 updateUserPanel.jTextField2.setText("user1@example.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("title 2");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3ActionPerformed(null);

 assertEquals(2, adminApp.getUsers().size());
 }

 @Test
 public void testJButton4ActionPerformedWithValidValues() {
 User user = new User("User 1", "user1@example.com", "1234567890", "title 1", true, true, true, "User 1");
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("User Name");
 updateUserPanel.jTextField2.setText("user2@example.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("title 2");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(null);

 User updatedUser = new User("User Name", "user2@example.com", "1234567891", "title 2", true, false, true, null);
 assertEquals(updatedUser, user);
 }

 @Test
 public void testJButton4ActionPerformedWithEmptyValues() {
 User user = new User("User 1", "user1@example.com", "1234567890", "title 1", true, true, true, "User 1");
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jTextField3.setText("");
 updateUserPanel.jTextField4.setText("");
 updateUserPanel.jButton4ActionPerformed(null);

 assertEquals(user, updateUserPanel.jComboBox2.getSelectedItem());
 }

 @Test
 public void testJButton4ActionPerformedWithDisabledComponents() {
 User user = new User("User 1", "user1@example.com", "1234567890", "title 1", true, true, true, "User 1");
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setEditable(false);
 updateUserPanel.jTextField2.setEditable(false);
 updateUserPanel.jTextField3.setEditable(false);
 updateUserPanel.jTextField4.setEditable(false);
 updateUserPanel.jCheckBox1.setEnabled(false);
 updateUserPanel.jCheckBox2.setEnabled(false);
 updateUserPanel.jCheckBox3.setEnabled(false);
 updateUserPanel.jButton4ActionPerformed(null);

 assertEquals(user, updateUserPanel.jComboBox2.getSelectedItem());
 assertFalse(adminApp.isUsersUpdated());
 }
}