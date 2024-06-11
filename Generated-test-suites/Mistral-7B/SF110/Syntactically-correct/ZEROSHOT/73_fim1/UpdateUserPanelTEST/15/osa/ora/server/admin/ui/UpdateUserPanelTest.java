package osa.ora.server.admin.ui;

import static org.junit.Assert.*;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

import org.junit.Before;
import org.junit.Test;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private UpdateUserPanel updateUserPanel;
 private Vector<Group> groups;
 private Group group;
 private User user;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 groups = new Vector<>();
 Group group1 = new Group("Group 1");
 group = new User("User 1", "user1@example.com", "123456789", "Title 1", true, true, true);
 group.setShowMyStatus(true);
 group.setOnlyStartChat(true);
 group.setCanJoinChatRoom(true);
 group.setJobTitle("Job Title 1");
 group1.addUser(group);
 groups.add(group1);
 adminApp.setGroups(groups);
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 @Test
 public void testUpdateModel() {
 updateUserPanel.updateModel();
 assertEquals("Group 1", updateUserPanel.jComboBox1.getSelectedItem().toString());
 }

 @Test
 public void testInitComponents() {
 assertNotNull(updateUserPanel.jLabel1);
 assertNotNull(updateUserPanel.jLabel2);
 assertNotNull(updateUserPanel.jLabel3);
 assertNotNull(updateUserPanel.jCheckBox1);
 assertNotNull(updateUserPanel.jCheckBox2);
 assertNotNull(updateUserPanel.jCheckBox3);
 assertNotNull(updateUserPanel.jLabel4);
 assertNotNull(updateUserPanel.jComboBox1);
 assertNotNull(updateUserPanel.jTextField2);
 assertNotNull(updateUserPanel.jTextField3);
 assertNotNull(updateUserPanel.jButton1);
 assertNotNull(updateUserPanel.jButton2);
 assertNotNull(updateUserPanel.jComboBox2);
 assertNotNull(updateUserPanel.jButton3);
 assertNotNull(updateUserPanel.jLabel5);
 assertNotNull(updateUserPanel.jTextField1);
 assertNotNull(updateUserPanel.jTextField4);
 assertNotNull(updateUserPanel.jLabel6);
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1ActionPerformed(new ActionEvent(new Object(), 0, ""));
 assertTrue(updateUserPanel.isVisible() == false);
 }

 @Test
 public void testJButton2ActionPerformed() {
 updateUserPanel.jComboBox1 = new JComboBox<>(groups.toArray(new Group[0]));
 updateUserPanel.jComboBox2 = new JComboBox<>(new String[] { user.toString() });
 updateUserPanel.jComboBox1ItemStateChanged(null);
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 0, ""));
 assertEquals(groups.get(0).getUsers().size(), groups.get(0).getUsers().size() - 1);
 }

 @Test
 public void testJButton3ActionPerformed() {
 updateUserPanel.jComboBox1 = new JComboBox<>(groups.toArray(new Group[0]));
 updateUserPanel.jComboBox2 = new JComboBox<>(new String[] { user.toString() });
 updateUserPanel.jComboBox1ItemStateChanged(null);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("new.user1@example.com");
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(new Object(), 0, ""));
 assertEquals(groups.get(0).getUsers().get(0).getName(), "New User Name");
 assertEquals(groups.get(0).getUsers().get(0).getEmail(), "new.user1@example.com");
 }

 @Test
 public void testJButton4ActionPerformed() {
 updateUserPanel.jComboBox1 = new JComboBox<>(groups.toArray(new Group[0]));
 updateUserPanel.jComboBox2 = new JComboBox<>(new String[] { user.toString() });
 updateUserPanel.jComboBox1ItemStateChanged(null);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("new.user1@example.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, ""));
 assertEquals(groups.get(0).getUsers().get(0).getName(), "New User Name");
 assertEquals(groups.get(0).getUsers().get(0).getEmail(), "new.user1@example.com");
 assertEquals(groups.get(0).getUsers().get(0).getDirectPhone(), "1234567891");
 assertEquals(groups.get(0).getUsers().get(0).getJobTitle(), "New Job Title");
 assertEquals(groups.get(0).getUsers().get(0).isShowMyStatus(), true);
 assertEquals(groups.get(0).getUsers().get(0).isOnlyStartChat(), false);
 assertEquals(groups.get(0).getUsers().get(0).isCanJoinChatRoom(), true);
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 updateUserPanel.jComboBox1 = new JComboBox<>(groups.toArray(new Group[0]));
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, 0, 0));
 assertNotNull(updateUserPanel.jComboBox2);
 assertEquals(updateUserPanel.jComboBox2.getModel().getSize(), 1);
 }

 @Test
 public void testJComboBox2ItemStateChanged() {
 updateUserPanel.jComboBox1 = new JComboBox<>(groups.toArray(new Group[0]));
 updateUserPanel.jComboBox2 = new JComboBox<>(new String[] { user.toString() });
 updateUserPanel.jComboBox1ItemStateChanged(null);
 updateUserPanel.jComboBox2ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox2, 0, 0));
 assertNotNull(updateUserPanel.jTextField1);
 assertEquals(updateUserPanel.jTextField1.getText(), user.getName());
 }
}