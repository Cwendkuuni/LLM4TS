package osa.ora.server.admin.ui;

import static org.junit.Assert.*;

import org.junit.Test;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

 UpdateUserPanel updateUserPanel;
 AdminApp adminApp = new AdminApp();
 ControlPanel controlPanel = new ControlPanel();

 @Test
 public void testUpdateModel() {
 Group group = new Group();
 group.setName("Group1");
 User user1 = new User();
 user1.setName("User1");
 user1.setEmail("user1@email.com");
 user1.setDirectPhone("1234567890");
 user1.setShowMyStatus(true);
 user1.setOnlyStartChat(true);
 user1.setCanJoinChatRoom(true);
 group.addUser(user1);
 adminApp.setGroups(new ArrayList<Group>(Arrays.asList(group)));

 UpdateUserPanel testUpdateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 testUpdateUserPanel.updateModel();

 assertNotNull(testUpdateUserPanel.jComboBox1);
 assertNotNull(testUpdateUserPanel.jComboBox2);

 assertEquals(1, testUpdateUserPanel.jComboBox1.getItemCount());
 assertEquals("Group1", testUpdateUserPanel.jComboBox1.getItemAt(0).toString());

 assertEquals(1, testUpdateUserPanel.jComboBox2.getItemCount());
 assertEquals("User1", testUpdateUserPanel.jComboBox2.getItemAt(0).toString());
 }

 @Test
 public void testReset() {
 UpdateUserPanel testUpdateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 testUpdateUserPanel.reset();

 assertEquals("", testUpdateUserPanel.jTextField1.getText());
 assertEquals("", testUpdateUserPanel.jTextField2.getText());
 assertEquals("", testUpdateUserPanel.jTextField3.getText());
 assertEquals("", testUpdateUserPanel.jTextField4.getText());
 assertFalse(testUpdateUserPanel.jCheckBox1.isSelected());
 assertFalse(testUpdateUserPanel.jCheckBox2.isSelected());
 assertFalse(testUpdateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testFillUser() {
 User user = new User();
 user.setName("UserName");
 user.setEmail("user@email.com");
 user.setDirectPhone("1234567890");
 user.setJobTitle("Job Title");
 user.setShowMyStatus(true);
 user.setOnlyStartChat(true);
 user.setCanJoinChatRoom(true);

 UpdateUserPanel testUpdateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 testUpdateUserPanel.fillUser(user);

 assertEquals("UserName", testUpdateUserPanel.jTextField1.getText());
 assertEquals("user@email.com", testUpdateUserPanel.jTextField2.getText());
 assertEquals("1234567890", testUpdateUserPanel.jTextField3.getText());
 assertEquals("Job Title", testUpdateUserPanel.jTextField4.getText());
 assertTrue(testUpdateUserPanel.jCheckBox1.isSelected());
 assertTrue(testUpdateUserPanel.jCheckBox2.isSelected());
 assertTrue(testUpdateUserPanel.jCheckBox3.isSelected());
 }
}