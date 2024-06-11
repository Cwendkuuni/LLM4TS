package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import org.junit.Test;

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
 Group group = new Group();
 group.setName("Group1");
 User user = new User();
 user.setName("User1");
 user.setEmail("user1@email.com");
 group.addUser(user);
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
 updateUserPanel.updateModel();
 assertEquals(user.getName(), updateUserPanel.jTextField1.getText());
 assertEquals(user.getEmail(), updateUserPanel.jTextField2.getText());
 }

 @Test
 public void testFillUser() {
 User user = new User();
 user.setName("User1");
 user.setEmail("user1@email.com");
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.fillUser(user);
 assertEquals(user.getName(), updateUserPanel.jTextField1.getText());
 assertEquals(user.getEmail(), updateUserPanel.jTextField2.getText());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 Group group = new Group();
 group.setName("Group1");
 User user1 = new User();
 user1.setName("User1");
 user1.setEmail("user1@email.com");
 User user2 = new User();
 user2.setName("User2");
 user2.setEmail("user2@email.com");
 group.addUser(user1);
 group.addUser(user2);
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox1ItemStateChanged(null);
 assertEquals(user1.getName(), updateUserPanel.jTextField1.getText());
 assertEquals(user1.getEmail(), updateUserPanel.jTextField2.getText());
 }

 @Test
 public void testReset() {
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.reset();
 assertEquals("", updateUserPanel.jTextField1.getText());
 assertEquals("", updateUserPanel.jTextField2.getText());
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.jButton1ActionPerformed(new ActionEvent(new Object(), 0, null));
 assertTrue(controlPanel.isVisible());
 }

 @Test
 public void testJButton2ActionPerformedWithUser() {
 Group group = new Group();
 group.setName("Group1");
 User user = new User();
 user.setName("User1");
 user.setEmail("user1@email.com");
 group.addUser(user);
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 0, null));
 assertFalse(updateUserPanel.isVisible());
 }

 @Test
 public void testJButton2ActionPerformedWithoutUser() {
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 0, null));
 assertTrue(updateUserPanel.isVisible());
 }

 @Test
 public void testJButton3ActionPerformed() {
 Group group = new Group();
 group.setName("Group1");
 User user = new User();
 user.setName("User1");
 user.setEmail("user1@email.com");
 group.addUser(user);
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(new Object(), 0, null));
 assertTrue("Password reset failed", updateUserPanel.adminApp.isPasswordResetSuccess());
 }

 @Test
 public void testJButton4ActionPerformedWithValidValues() {
 Group group = new Group();
 group.setName("Group1");
 User user = new User();
 user.setName("User1");
 user.setEmail("user1@email.com");
 group.addUser(user);
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("UpdatedUserName");
 updateUserPanel.jTextField2.setText("updateduseremail@email.com");
 updateUserPanel.jTextField3.setText("updatedphone");
 updateUserPanel.jTextField4.setText("updatedtitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, null));
 assertEquals("UpdatedUserName", user.getName());
 assertEquals("updateduseremail@email.com", user.getEmail());
 assertTrue(user.isShowMyStatus());
 assertTrue(user.isOnlyStartChat());
 assertTrue(user.isCanJoinChatRoom());
 }

 @Test
 public void testJButton4ActionPerformedWithInvalidValues() {
 Group group = new Group();
 group.setName("Group1");
 User user = new User();
 user.setName("User1");
 user.setEmail("user1@email.com");
 group.addUser(user);
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText(null);
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jTextField3.setText(null);
 updateUserPanel.jTextField4.setText("");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, null));
 assertFalse("User updated failed", updateUserPanel.isVisible());
 }

 @Test
 public void testJButton4ActionPerformedWithInvalidEmail() {
 Group group = new Group();
 group.setName("Group1");
 User user = new User();
 user.setName("User1");
 user.setEmail("user1@email.com");
 group.addUser(user);
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("UpdatedUserName");
 updateUserPanel.jTextField2.setText("user1@email.com");
 updateUserPanel.jTextField3.setText("updatedphone");
 updateUserPanel.jTextField4.setText("updatedtitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, null));
 assertFalse("User updated failed", updateUserPanel.isVisible());
 }
}