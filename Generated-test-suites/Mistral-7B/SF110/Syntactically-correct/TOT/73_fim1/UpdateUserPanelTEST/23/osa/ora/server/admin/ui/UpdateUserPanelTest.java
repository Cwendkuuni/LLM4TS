import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.admin.ui.UpdateUserPanel;
import osa.ora.server.admin.AdminApp;
import osa.ora.server.beans.Group;
import osa.ora.server.beans.User;

public class UpdateUserPanelTest {

 private UpdateUserPanel updateUserPanel;
 private AdminApp adminApp;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 @Test
 public void testUpdateUserPanelInitialization() {
 // Testing if UpdateUserPanel is initialized correctly
 // Checking if all components are added to the panel
 assertEquals(15, updateUserPanel.getComponentCount());
 }

 @Test
 public void testUpdateModelWhenNoGroupsArePresent() {
 // Testing updateModel method when no groups are present
 // Checking if jComboBox2 is empty
 assertNull(updateUserPanel.jComboBox2.getModel().getSize());
 }

 @Test
 public void testUpdateModelWhenOneGroupIsPresent() {
 // Testing updateModel method when one group is present
 // Checking if jComboBox1 has a valid model
 assertNotNull(updateUserPanel.jComboBox1.getModel());
 // Checking if jComboBox2 has a valid model with one user
 assertEquals(1, updateUserPanel.jComboBox2.getModel().getSize());
 }

 @Test
 public void testUpdateModelWhenOneGroupIsPresentAndOneUserIsPresent() {
 // Testing updateModel method when one group is present and one user is present
 // Checking if jTextField1 is initialized with user name
 String expectedUserName = "John Doe";
 User user = new User("John Doe", "john.doe@example.com", "1234567890", "Software Engineer", true, true, true);
 updateUserPanel.fillUser(user);
 assertEquals(expectedUserName, updateUserPanel.jTextField1.getText());
 // Checking if jTextField2 is initialized with user email
 String expectedUserEmail = "john.doe@example.com";
 assertEquals(expectedUserEmail, updateUserPanel.jTextField2.getText());
 // Checking if jTextField3 is initialized with user phone
 String expectedUserPhone = "1234567890";
 assertEquals(expectedUserPhone, updateUserPanel.jTextField3.getText());
 // Checking if jTextField4 is initialized with user job title
 String expectedUserJobTitle = "Software Engineer";
 assertEquals(expectedUserJobTitle, updateUserPanel.jTextField4.getText());
 // Checking if checkboxes are initialized with user properties
 assertTrue(updateUserPanel.jCheckBox1.isSelected()); // showMyStatus
 assertTrue(updateUserPanel.jCheckBox2.isSelected()); // onlyStartChat
 assertTrue(updateUserPanel.jCheckBox3.isSelected()); // canJoinChatRoom
 }

 @Test(expected = NullPointerException.class)
 public void testUpdateUserPanelWhenNoGroupIsSelected() {
 // Testing if an exception is thrown when no group is selected
 updateUserPanel.jButton4.doClick();
 }

 @Test(expected = NullPointerException.class)
 public void testUpdateUserPanelWhenNoUserIsSelected() {
 // Testing if an exception is thrown when no user is selected
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton4.doClick();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateUserPanelWithInvalidEmail() {
 // Testing if an exception is thrown when invalid email is provided
 // Setting up a group and a user for testing
 Group group = new Group("Test Group");
 User user = new User("Test User", "test@.com", null, null, false, false, false);
 adminApp.addGroup(group);
 adminApp.addUser(user);
 updateUserPanel.adminApp = adminApp;
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 // Testing with an invalid email
 updateUserPanel.jTextField2.setText("test@.com");
 updateUserPanel.jTextField1.setText("Test User");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("Software Engineer");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 }

 @Test
 public void testUpdateUserPanelWithValidData() {
 // Testing if user is updated with valid data
 // Setting up a group and a user for testing
 Group group = new Group("Test Group");
 User user = new User("John Doe", "john.doe@example.com", "1234567890", "Software Engineer", true, false, false);
 adminApp.addGroup(group);
 adminApp.addUser(user);
 updateUserPanel.adminApp = adminApp;
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 // Changing user data
 updateUserPanel.jTextField1.setText("Test User");
 updateUserPanel.jTextField2.setText("test.user@example.com");
 updateUserPanel.jTextField3.setText("1111111111");
 updateUserPanel.jTextField4.setText("Software Developer");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 // Checking if user is updated in the group
 Group updatedGroup = adminApp.getGroups().get(0);
 User updatedUser = updatedGroup.getUsers().get(0);
 assertNotSame(user.getName(), updatedUser.getName());
 assertNotSame(user.getEmail(), updatedUser.getEmail());
 assertNotSame(user.getDirectPhone(), updatedUser.getDirectPhone());
 assertNotSame(user.getJobTitle(), updatedUser.getJobTitle());
 assertFalse(user.isShowMyStatus());
 assertFalse(user.isOnlyStartChat());
 assertFalse(user.isCanJoinChatRoom());
 assertTrue(updatedUser.isShowMyStatus());
 assertTrue(updatedUser.isOnlyStartChat());
 assertTrue(updatedUser.isCanJoinChatRoom());
 }
}