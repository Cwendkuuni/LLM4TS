package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
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
 group = new Group("Group1");
 user = new User("User1", "user1@example.com", "1234567890", "JobTitle1", true, true, true);
 group.addUser(user);
 adminApp.setGroups(new ArrayList<Group>() {{
 add(group);
 }});
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 }

 @Test
 public void testUpdateModel() {
 updateUserPanel.updateModel();
 assertEquals(group, updateUserPanel.getAdminApp().getGroups().get(0));
 assertEquals(user, updateUserPanel.getAdminApp().getGroups().get(0).getUsers().get(0));
 }

 @Test
 public void testInitComponents() {
 assertNotNull(updateUserPanel.getJLabel1());
 assertNotNull(updateUserPanel.getJLabel2());
 assertNotNull(updateUserPanel.getJLabel3());
 assertNotNull(updateUserPanel.getJCheckBox1());
 assertNotNull(updateUserPanel.getJCheckBox2());
 assertNotNull(updateUserPanel.getJCheckBox3());
 assertNotNull(updateUserPanel.getJLabel4());
 assertNotNull(updateUserPanel.getJComboBox1());
 assertNotNull(updateUserPanel.getJTextField2());
 assertNotNull(updateUserPanel.getJTextField3());
 assertNotNull(updateUserPanel.getJButton1());
 assertNotNull(updateUserPanel.getJButton2());
 assertNotNull(updateUserPanel.getJComboBox2());
 assertNotNull(updateUserPanel.getJButton3());
 assertNotNull(updateUserPanel.getJLabel5());
 assertNotNull(updateUserPanel.getJTextField1());
 assertNotNull(updateUserPanel.getJTextField4());
 assertNotNull(updateUserPanel.getJLabel6());
 }

 @Test
 public void testjButton1ActionPerformed() {
 updateUserPanel.jButton1ActionPerformed(new ActionEvent(new Object(), 0, ""));
 assertEquals(false, controlPanel.getParentDialog().isVisible());
 }

 @Test
 public void testjButton2ActionPerformed() {
 updateUserPanel.jComboBox2 = new JComboBox<>();
 updateUserPanel.jComboBox2.setModel(new DefaultComboBoxModel<User>(new User[0]));
 updateUserPanel.jComboBox2.addItem(user);
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 0, ""));
 assertEquals(0, group.getUsers().size());
 }

 @Test
 public void testjButton3ActionPerformed() {
 updateUserPanel.jTextField2.setText("newEmail@example.com");
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(new Object(), 0, ""));
 assertEquals("newEmail@example.com", user.getEmail());
 }

 @Test
 public void testjButton4ActionPerformed() {
 updateUserPanel.jTextField1.setText("newName");
 updateUserPanel.jTextField2.setText("newEmail@example.com");
 updateUserPanel.jTextField3.setText("newPhone");
 updateUserPanel.jTextField4.setText("newJobTitle");
 updateUserPanel.getJCheckBox1().setSelected(true);
 updateUserPanel.getJCheckBox2().setSelected(true);
 updateUserPanel.getJCheckBox3().setSelected(true);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, ""));
 assertEquals("newName", user.getName());
 assertEquals("newEmail@example.com", user.getEmail());
 assertEquals("newPhone", user.getDirectPhone());
 assertEquals("newJobTitle", user.getJobTitle());
 assertTrue(user.isShowMyStatus());
 assertTrue(user.isOnlyStartChat());
 assertTrue(user.isCanJoinChatRoom());
 }

 @Test
 public void testjComboBox1ItemStateChanged() {
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(new Object(), 0, 0));
 assertEquals(group, updateUserPanel.getAdminApp().getGroups().get(0));
 assertEquals(user, updateUserPanel.getAdminApp().getGroups().get(0).getUsers().get(0));
 }

 @Test
 public void testReset() {
 updateUserPanel.reset();
 assertNull(updateUserPanel.getJTextField1().getText());
 assertNull(updateUserPanel.getJTextField2().getText());
 assertNull(updateUserPanel.getJTextField3().getText());
 assertNull(updateUserPanel.getJTextField4().getText());
 assertFalse(updateUserPanel.getJCheckBox1().isSelected());
 assertFalse(updateUserPanel.getJCheckBox2().isSelected());
 assertFalse(updateUserPanel.getJCheckBox3().isSelected());
 }

 @Test
 public void testFillUser() {
 updateUserPanel.fillUser(user);
 assertEquals(user.getName(), updateUserPanel.getJTextField1().getText());
 assertEquals(user.getEmail(), updateUserPanel.getJTextField2().getText());
 assertEquals(user.getDirectPhone(), updateUserPanel.getJTextField3().getText());
 assertEquals(user.getJobTitle(), updateUserPanel.getJTextField4().getText());
 assertTrue(updateUserPanel.getJCheckBox1().isSelected());
 assertTrue(updateUserPanel.getJCheckBox2().isSelected());
 assertTrue(updateUserPanel.getJCheckBox3().isSelected());
 }

 @Test
 public void testjComboBox2ItemStateChanged() {
 updateUserPanel.jComboBox2ItemStateChanged(new ItemEvent(new Object(), 0, 0));
 assertEquals(user.getName(), updateUserPanel.getJTextField1().getText());
 assertEquals(user.getEmail(), updateUserPanel.getJTextField2().getText());
 assertEquals(user.getDirectPhone(), updateUserPanel.getJTextField3().getText());
 assertEquals(user.getJobTitle(), updateUserPanel.getJTextField4().getText());
 assertTrue(updateUserPanel.getJCheckBox1().isSelected());
 assertTrue(updateUserPanel.getJCheckBox2().isSelected());
 assertTrue(updateUserPanel.getJCheckBox3().isSelected());
 }
}