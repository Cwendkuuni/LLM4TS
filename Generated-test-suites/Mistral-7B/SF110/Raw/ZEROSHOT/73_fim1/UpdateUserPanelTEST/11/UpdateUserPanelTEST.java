package osa.ora.server.admin.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private UpdateUserPanel updateUserPanel;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 @Test
 public void testUpdateModel() {
 Group expectedGroup = new Group("Group1", new Vector<User>());
 adminApp.setGroups(new Vector<Group>(Arrays.asList(expectedGroup)));

 updateUserPanel.updateModel();

 JComboBox jComboBox1 = updateUserPanel.jComboBox1;
 assertEquals("Group1", jComboBox1.getSelectedItem());

 User expectedUser = new User("User1", "user1@example.com", "1234567890", "Job Title 1", true, true, true);
 expectedUser.setGroup(expectedGroup);
 expectedGroup.getUsers().add(expectedUser);

 JComboBox jComboBox2 = updateUserPanel.jComboBox2;
 assertEquals("User1", jComboBox2.getSelectedItem());
 }

 @Test
 public void testInitComponents() {
 // Check that all components are added to the UpdateUserPanel
 UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);

 JLabel jLabel1 = updateUserPanel.jLabel1;
 JLabel jLabel2 = updateUserPanel.jLabel2;
 JLabel jLabel3 = updateUserPanel.jLabel3;
 JLabel jLabel4 = updateUserPanel.jLabel4;
 JLabel jLabel5 = updateUserPanel.jLabel5;
 JLabel jLabel6 = updateUserPanel.jLabel6;
 JButton jButton1 = updateUserPanel.jButton1;
 JButton jButton2 = updateUserPanel.jButton2;
 JButton jButton3 = updateUserPanel.jButton3;
 JButton jButton4 = updateUserPanel.jButton4;
 JCheckBox jCheckBox1 = updateUserPanel.jCheckBox1;
 JCheckBox jCheckBox2 = updateUserPanel.jCheckBox2;
 JCheckBox jCheckBox3 = updateUserPanel.jCheckBox3;
 JComboBox jComboBox1 = updateUserPanel.jComboBox1;
 JComboBox jComboBox2 = updateUserPanel.jComboBox2;
 JTextField jTextField1 = updateUserPanel.jTextField1;
 JTextField jTextField2 = updateUserPanel.jTextField2;
 JTextField jTextField3 = updateUserPanel.jTextField3;
 JTextField jTextField4 = updateUserPanel.jTextField4;

 // Check that components are placed correctly
 assertEquals(10, jLabel1.getX(), 0.01f);
 assertEquals(50, jLabel1.getY(), 0.01f);
 // ... continue checking other components
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
 updateUserPanel.updateModel();
 Group expectedGroup = new Group("Group1", new Vector<User>());
 adminApp.setGroups(new Vector<Group>(Arrays.asList(expectedGroup)));
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox1ItemStateChanged(null);

 assertEquals("Group1", updateUserPanel.jComboBox1.getSelectedItem());
 User expectedUser = new User("User1", "user1@example.com", "1234567890", "Job Title 1", true, true, true);
 expectedUser.setGroup(expectedGroup);
 expectedGroup.getUsers().add(expectedUser);

 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem());
 }

 @Test
 public void testJButton1ActionPerformed() {
 UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
 updateUserPanel.controlPanel = new JFrame();

 updateUserPanel.jButton1ActionPerformed(mockEvent());

 assertTrue(updateUserPanel.controlPanel.isVisible());
 updateUserPanel.reset();
 }

 @Test
 public void testJButton2ActionPerformed() {
 UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
 updateUserPanel.controlPanel = new JFrame();

 Group group = new Group("Group1", new Vector<User>());
 User user = new User("User1", "user1@example.com", "1234567890", "Job Title 1", true, true, true);
 group.getUsers().add(user);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedItem("Group1");
 updateUserPanel.jComboBox2.setSelectedItem(user);

 updateUserPanel.jButton2ActionPerformed(mockEvent());

 assertNull(updateUserPanel.jComboBox2.getSelectedItem());
 assertTrue(group.getUsers().isEmpty());
 }

 @Test
 public void testJButton3ActionPerformed() {
 UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
 updateUserPanel.controlPanel = new JFrame();

 Group group = new Group("Group1", new Vector<User>());
 User user = new User("User1", "user1@example.com", "1234567890", "Job Title 1", true, true, true);
 group.getUsers().add(user);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedItem("Group1");
 updateUserPanel.jComboBox2.setSelectedItem(user);

 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);

 updateUserPanel.jButton3ActionPerformed(mockEvent());

 User updatedUser = group.getUsers().get(0);
 assertEquals("New User Name", updatedUser.getName());
 assertEquals("new.user@example.com", updatedUser.getEmail());
 assertEquals("1234567891", updatedUser.getDirectPhone());
 assertEquals("New Job Title", updatedUser.getJobTitle());
 assertFalse(updatedUser.isShowMyStatus());
 assertTrue(updatedUser.isOnlyStartChat());
 assertFalse(updatedUser.isCanJoinChatRoom());
 }

 @Test
 public void testJButton4ActionPerformed() {
 UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
 updateUserPanel.controlPanel = new JFrame();

 Group group = new Group("Group1", new Vector<User>());
 User user = new User("User1", "user1@example.com", "1234567890", "Job Title 1", true, true, true);
 group.getUsers().add(user);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedItem("Group1");
 updateUserPanel.jComboBox2.setSelectedItem(user);

 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);

 updateUserPanel.jButton4ActionPerformed(mockEvent());

 User updatedUser = group.getUsers().get(0);
 assertEquals("New User Name", updatedUser.getName());
 assertEquals("new.user@example.com", updatedUser.getEmail());
 assertEquals("1234567891", updatedUser.getDirectPhone());
 assertEquals("New Job Title", updatedUser.getJobTitle());
 assertTrue(updatedUser.isShowMyStatus());
 assertTrue(updatedUser.isOnlyStartChat());
 assertTrue(updatedUser.isCanJoinChatRoom());
 }

 private ActionEvent mockEvent() {
 return new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "BUTTON_NAME");
 }
}