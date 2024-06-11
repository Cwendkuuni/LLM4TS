package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

import org.junit.Before;
import org.junit.Test;

import java.awt.event.*;
import java.util.*;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private ControlPanel controlPanel;
 private UpdateUserPanel updateUserPanel;
 private Group group;
 private User user;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 controlPanel = new ControlPanel();
 group = new Group("Group1", new Vector<User>());
 user = new User("User1", "user1@example.com", "User1Pass", "1234567890", true, true, true, new Vector<String>());
 group.getUsers().add(user);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 }

 @Test
 public void testUpdateModel() {
 updateUserPanel.updateModel();
 assertEquals(1, updateUserPanel.jComboBox1.getItemCount());
 updateUserPanel.updateModel();
 assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
 }

 @Test
 public void testInitComponents() {
 assertEquals(10, updateUserPanel.jLabel1.getX());
 assertEquals(50, updateUserPanel.jLabel1.getY());
 assertNotNull(updateUserPanel.jButton1);
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1.doClick();
 assertTrue(controlPanel.getParentDialog().isVisible());
 updateUserPanel.reset();
 }

 @Test
 public void testJButton2ActionPerformed() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton2.doClick();
 assertEquals(0, group.getUsers().size());
 }

 @Test
 public void testJButton3ActionPerformed() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jButton3.doClick();
 assertEquals("NewUserName", user.getName());
 assertEquals("new.user@example.com", user.getEmail());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformedInvalidEmail() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("example.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("JobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformedNullName() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText(null);
 updateUserPanel.jTextField2.setText("user2@example.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("JobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformedEmptyName() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jTextField2.setText("user2@example.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("JobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 }

 @Test
 public void testJButton4ActionPerformed() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("JobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 assertEquals("NewUserName", user.getName());
 assertEquals("new.user@example.com", user.getEmail());
 assertEquals("1234567890", user.getDirectPhone());
 assertEquals("JobTitle", user.getJobTitle());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setModel(new DefaultComboBoxModel<User>(group.getUsers()));
 assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
 }
}