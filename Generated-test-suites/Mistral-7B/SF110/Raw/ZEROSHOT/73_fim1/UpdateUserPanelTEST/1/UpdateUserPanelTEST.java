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
 Group group = new Group("Group1", new Vector<User>());
 adminApp.setGroups(new Vector<Group>(Collections.singletonList(group)));
 updateUserPanel.updateModel();
 assertEquals(1, updateUserPanel.jComboBox1.getItemCount());
 updateUserPanel.updateModel(); // should update comboBox2 when group size > 0
 Group groupWithUsers = new Group("Group2", new Vector<User>(Arrays.asList(new User("User1", "user1@test.com"), new User("User2", "user2@test.com"))));
 adminApp.setGroups(new Vector<Group>(Collections.singletonList(groupWithUsers)));
 updateUserPanel.updateModel();
 assertEquals(2, updateUserPanel.jComboBox2.getItemCount());
 }

 @Test
 public void testInitComponents() {
 assertNotNull(updateUserPanel.jLabel1);
 assertNotNull(updateUserPanel.jLabel2);
 assertNotNull(updateUserPanel.jLabel3);
 // add other assertions for other components
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.controlPanel = new ControlPanel() {
 @Override
 public void getParentDialog() {
 JFrame frame = new JFrame();
 frame.setVisible(true);
 return frame;
 }
 };
 updateUserPanel.jButton1.doClick();
 assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible());
 updateUserPanel.reset();
 }

 @Test
 public void testJButton2ActionPerformed() {
 Group group = new Group("Group1", new Vector<User>());
 User user = new User("User1", "user1@test.com");
 group.getUsers().add(user);
 adminApp.setGroups(new Vector<Group>(Collections.singletonList(group)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton2.doClick();
 assertEquals(0, updateUserPanel.jComboBox2.getItemCount());
 updateUserPanel.reset();
 }

 @Test
 public void testJButton3ActionPerformed() {
 Group group = new Group("Group1", new Vector<User>());
 User user = new User("User1", "user1@test.com");
 group.getUsers().add(user);
 adminApp.setGroups(new Vector<Group>(Collections.singletonList(group)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@test.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("Software Tester");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3.doClick();
 assertEquals("NewUserName", user.getName());
 assertEquals("new.user@test.com", user.getEmail());
 assertEquals("1234567890", user.getDirectPhone());
 assertEquals("Software Tester", user.getJobTitle());
 assertTrue(user.isShowMyStatus());
 assertFalse(user.isOnlyStartChat());
 assertTrue(user.isCanJoinChatRoom());
 }

 @Test
 public void testJButton4ActionPerformed() {
 Group group = new Group("Group1", new Vector<User>());
 User user = new User("User1", "user1@test.com");
 group.getUsers().add(user);
 adminApp.setGroups(new Vector<Group>(Collections.singletonList(group)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@test.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("Software Tester");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 assertEquals("NewUserName", user.getName());
 assertEquals("new.user@test.com", user.getEmail());
 assertEquals("1234567890", user.getDirectPhone());
 assertEquals("Software Tester", user.getJobTitle());
 assertTrue(user.isShowMyStatus());
 assertFalse(user.isOnlyStartChat());
 assertTrue(user.isCanJoinChatRoom());
 }
}