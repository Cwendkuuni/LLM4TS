package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private UpdateUserPanel updateUserPanel;
 private Group group;
 private User user, user1;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 group = new Group("Group1", new Vector<User>());
 user = new User("User1", "user1@email.com", "1234567890", "Job Title1", true, true, true);
 user1 = new User("User2", "user2@email.com", "9876543210", "Job Title2", true, true, true);
 group.getUsers().add(user);
 group.getUsers().add(user1);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 @Test
 public void testUpdateModel() {
 updateUserPanel.updateModel();
 assertEquals(group, updateUserPanel.getAdminApp().getGroups().get(0));
 assertEquals(2, updateUserPanel.getjComboBox2().getModel().getSize());
 assertEquals(user, updateUserPanel.getjComboBox2().getItemAt(0));
 }

 @Test
 public void testInitComponents() {
 assertEquals(4, updateUserPanel.getComponentCount());
 assertNotNull(updateUserPanel.jLabel1);
 assertNotNull(updateUserPanel.jLabel2);
 assertNotNull(updateUserPanel.jCheckBox1);
 assertNotNull(updateUserPanel.jComboBox1);
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox1ItemStateChanged(null);
 assertEquals(user, updateUserPanel.getjComboBox2().getItemAt(0));
 updateUserPanel.jComboBox1.setSelectedIndex(1);
 updateUserPanel.jComboBox1ItemStateChanged(null);
 assertEquals(user1, updateUserPanel.getjComboBox2().getItemAt(0));
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1ActionPerformed(null);
 assertTrue(updateUserPanel.isVisible());
 updateUserPanel.controlPanel.getParentDialog().setVisible(true);
 updateUserPanel.reset();
 assertFalse(updateUserPanel.isVisible());
 }

 @Test
 public void testJButton2ActionPerformed() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton2ActionPerformed(null);
 assertEquals(1, group.getUsers().size());
 assertFalse(updateUserPanel.isVisible());
 }

 @Test
 public void testJButton3ActionPerformed() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("NewName");
 updateUserPanel.jTextField2.setText("newemail@email.com");
 updateUserPanel.jButton3ActionPerformed(null);
 assertEquals("NewName", user.getName());
 assertEquals("newemail@email.com", user.getEmail());
 }

 @Test
 public void testJButton4ActionPerformed() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("NewName");
 updateUserPanel.jTextField2.setText("newemail@email.com");
 updateUserPanel.jTextField3.setText("NewPhone");
 updateUserPanel.jTextField4.setText("NewJobTitle");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton4ActionPerformed(null);
 assertEquals("NewName", user.getName());
 assertEquals("newemail@email.com", user.getEmail());
 assertEquals("NewPhone", user.getDirectPhone());
 assertEquals("NewJobTitle", user.getJobTitle());
 assertFalse(user.isShowMyStatus());
 assertFalse(user.isOnlyStartChat());
 assertFalse(user.isCanJoinChatRoom());
 }
}