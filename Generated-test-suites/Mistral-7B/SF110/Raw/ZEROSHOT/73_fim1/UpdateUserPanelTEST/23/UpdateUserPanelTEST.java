package osa.ora.server.admin.ui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

 private UpdateUserPanel updateUserPanel;
 private AdminApp adminApp;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 @Test
 public void testUpdateModel() {
 Group group = new Group("Group1", null);
 User user = new User("User1", "user1@email.com", "1234567890", "Job Title1", true, true, true);
 group.getUsers().add(user);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));

 updateUserPanel.updateModel();

 assertEquals("Group1", updateUserPanel.jComboBox1.getSelectedItem().toString());
 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().toString());
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
 assertNotNull(updateUserPanel.jButton4);
 assertNotNull(updateUserPanel.jLabel5);
 assertNotNull(updateUserPanel.jTextField1);
 assertNotNull(updateUserPanel.jTextField4);
 assertNotNull(updateUserPanel.jLabel6);
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 Group group = new Group("Group1", null);
 User user1 = new User("User1", "user1@email.com", "1234567890", "Job Title1", true, true, true);
 User user2 = new User("User2", "user2@email.com", "2345678901", "Job Title2", true, true, true);
 group.getUsers().add(user1);
 group.getUsers().add(user2);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox1ItemStateChanged(null);

 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.controlPanel = new ControlPanel(adminApp);
 updateUserPanel.jButton1ActionPerformed(null);
 assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible() == false);
 }

 @Test
 public void testJButton2ActionPerformed() {
 Group group = new Group("Group1", null);
 User user = new User("User1", "user1@email.com", "1234567890", "Job Title1", true, true, true);
 group.getUsers().add(user);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton2ActionPerformed(null);

 assertEquals(0, group.getUsers().size());
 }

 @Test
 public void testJButton3ActionPerformed() {
 Group group = new Group("Group1", null);
 User user = new User("User1", "user1@email.com", "1234567890", "Job Title1", true, true, true);
 group.getUsers().add(user);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updatePanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("new.user@email.com");
 updateUserPanel.jTextField3.setText("New Phone Number");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jButton3ActionPerformed(null);

 String newUserName = "New User Name";
 String newUserEmail = "new.user@email.com";
 String newUserDirectPhone = "New Phone Number";
 String newUserJobTitle = "New Job Title";

 User expectedUser = new User(newUserName, newUserEmail, newUserDirectPhone, newUserJobTitle, true, true, true);
 User actualUser = group.getUsers().get(0);
 assertEquals(expectedUser.getName(), actualUser.getName());
 assertEquals(expectedUser.getEmail(), actualUser.getEmail());
 assertEquals(expectedUser.getDirectPhone(), actualUser.getDirectPhone());
 assertEquals(expectedUser.getJobTitle(), actualUser.getJobTitle());
 }

 @Test
 public void testJButton4ActionPerformed() {
 Group group = new Group("Group1", null);
 User user = new User("User1", "user1@email.com", "1234567890", "Job Title1", true, true, true);
 group.getUsers().add(user);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("new.user@email.com");
 updateUserPanel.jTextField3.setText("New Phone Number");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(null);

 String newUserName = "New User Name";
 String newUserEmail = "new.user@email.com";
 String newUserDirectPhone = "New Phone Number";
 String newUserJobTitle = "New Job Title";
 boolean newUserShowMyStatus = true;
 boolean newUserOnlyStartChat = false;
 boolean newUserCanJoinChatRoom = true;

 User expectedUser = new User(newUserName, newUserEmail, newUserDirectPhone, newUserJobTitle, newUserShowMyStatus, newUserOnlyStartChat, newUserCanJoinChatRoom);
 User actualUser = group.getUsers().get(0);
 assertEquals(expectedUser.getName(), actualUser.getName());
 assertEquals(expectedUser.getEmail(), actualUser.getEmail());
 assertEquals(expectedUser.getDirectPhone(), actualUser.getDirectPhone());
 assertEquals(expectedUser.getJobTitle(), actualUser.getJobTitle());
 assertEquals(expectedUser.isShowMyStatus(), actualUser.isShowMyStatus());
 assertEquals(expectedUser.isOnlyStartChat(), actualUser.isOnlyStartChat());
 assertEquals(expectedUser.isCanJoinChatRoom(), actualUser.isCanJoinChatRoom());
 }

}