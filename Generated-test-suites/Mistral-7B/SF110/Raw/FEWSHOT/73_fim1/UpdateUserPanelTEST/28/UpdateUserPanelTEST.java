package osa.ora.server.admin.ui;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
 // Test case for updating model when groups and users are available
 Group group = new Group("Group1");
 User user1 = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
 User user2 = new User("User2", "user2@example.com", "2345678901", "Title2", true, true, true);
 group.addUser(user1);
 group.addUser(user2);
 adminApp.setGroups(new ArrayList<>(Arrays.asList(group)));

 updateUserPanel.updateModel();

 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem());
 assertEquals("User2", updateUserPanel.jComboBox2.getItemAt(1));
 }

 @Test
 public void testUpdateModelWhenGroupsAreEmpty() {
 updateUserPanel.updateModel();
 assertNull(updateUserPanel.jComboBox2.getModel());
 }

 @Test
 public void testUpdateModelWhenUsersAreEmpty() {
 Group group = new Group("Group1");
 adminApp.setGroups(new ArrayList<>(Arrays.asList(group)));
 updateUserPanel.updateModel();

 assertEquals("", updateUserPanel.jTextField1.getText());
 assertEquals("", updateUserPanel.jTextField2.getText());
 assertEquals("", updateUserPanel.jTextField3.getText());
 assertEquals("", updateUserPanel.jTextField4.getText());
 }

 // Test cases for other methods can be added here

}