package osa.ora.server.admin.ui;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

 private UpdateUserPanel updateUserPanel;
 private AdminApp adminApp;
 private Group group;
 private User user;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 group = new Group("Group1", new Vector<User>());
 user = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
 group.getUsers().add(user);
 updateUserPanel = new UpdateUserPanel(adminApp, new ControlPanel());
 updateUserPanel.updateModel();
 }

 @Test
 public void testUpdateModel() {
 assertEquals(group, updateUserPanel.getAdminApp().getGroups().get(0));
 assertEquals(user, updateUserPanel.getjComboBox2().getSelectedItem());
 }

 @Test
 public void testjComboBox1ItemStateChanged() {
 adminApp.getGroups().clear();
 adminApp.getGroups().add(group);
 updateUserPanel.updateModel();
 assertEquals(user, updateUserPanel.getjComboBox2().getSelectedItem());
 updateUserPanel.jComboBox1.setSelectedIndex(1);
 updateUserPanel.jComboBox1ItemStateChanged(null);
 assertNull(updateUserPanel.getjComboBox2().getSelectedItem());
 }

 @Test
 public void testjButton1ActionPerformed() {
 updateUserPanel.jButton1.doClick();
 assertTrue(updateUserPanel.isVisible() == false);
 }

 @Test
 public void testjButton2ActionPerformed() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton2.doClick();
 assertNull(updateUserPanel.getjComboBox2().getSelectedItem());
 }

 @Test
 public void testjButton3ActionPerformed() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("NewName");
 updateUserPanel.jTextField2.setText("new.email@example.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("New Title");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton3.doClick();
 User newUser = (User) updateUserPanel.getjComboBox2().getSelectedItem();
 assertEquals("NewName", newUser.getName());
 assertEquals("new.email@example.com", newUser.getEmail());
 assertEquals("1234567891", newUser.getDirectPhone());
 assertEquals("New Title", newUser.getJobTitle());
 assertFalse(newUser.isShowMyStatus());
 assertTrue(newUser.isOnlyStartChat());
 assertFalse(newUser.isCanJoinChatRoom());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testjButton3ActionPerformedInvalidUsername() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText(null);
 updateUserPanel.jTextField2.setText("user1@example.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("Title1");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3.doClick();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testjButton3ActionPerformedInvalidEmail() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("User1");
 updateUserPanel.jTextField2.setText(null);
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("Title1");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3.doClick();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testjButton3ActionPerformedDuplicateEmail() {
 Group group2 = new Group("Group2", new Vector<User>());
 User user2 = new User("User2", "user1@example.com", "1234567891", "Title2", true, true, true);
 group2.getUsers().add(user2);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(1);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("User1New");
 updateUserPanel.jTextField2.setText("user1@example.com");
 updateUserPanel.jTextField3.setText("1234567892");
 updateUserPanel.jTextField4.setText("Title2New");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3.doClick();
 }

}