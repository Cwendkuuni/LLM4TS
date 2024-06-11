import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import static osa.ora.server.beans.TestData.*;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private UpdateUserPanel updateUserPanel;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 adminApp.setGroups(new Vector<Group>());
 adminApp.getGroups().add(new Group("Group1", new Vector<User>()));
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 @Test
 public void testUpdateUserPanelInitialization() {
 // Check if the components have been initialized properly
 assertEquals("Username: label not found", "Username:", updateUserPanel.jLabel1.getText());
 assertEquals("Login Email: label not found", "Login Email:", updateUserPanel.jLabel2.getText());
 // Check if all the components are initially empty or null
 assertTrue("jTextField1 should be empty", updateUserPanel.jTextField1.getText().isEmpty());
 assertTrue("jTextField2 should be empty", updateUserPanel.jTextField2.getText().isEmpty());
 assertTrue("jTextField3 should be empty", updateUserPanel.jTextField3.getText().isEmpty());
 assertTrue("jTextField4 should be empty", updateUserPanel.jTextField4.getText().isEmpty());
 // Check if checkboxes are not selected by default
 assertFalse("jCheckBox1 should not be selected", updateUserPanel.jCheckBox1.isSelected());
 assertFalse("jCheckBox2 should not be selected", updateUserPanel.jCheckBox2.isSelected());
 assertFalse("jCheckBox3 should not be selected", updateUserPanel.jCheckBox3.isSelected());
 // Check if userComboBoxes are empty
 assertEquals("Group combobox should have no items", 0, updateUserPanel.jComboBox1.getItemCount());
 assertEquals("User combobox should have no items", 0, updateUserPanel.jComboBox2.getItemCount());
 }

 @Test
 public void testUpdateUserPanelUpdateModel() {
 // Initialize the adminApp with some data
 adminApp.setGroups(new Vector<Group>());
 adminApp.getGroups().add(new Group("Group1", new Vector<User>()));
 adminApp.getGroups().get(0).getUsers().add(new User(1, "User1", "user1@example.com", "1234567890"));
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 updateUserPanel.updateModel();
 // Check if the user combobox has one item
 assertEquals("User combobox should have one item", 1, updateUserPanel.jComboBox2.getItemCount());
 // Check if the selected item is the user we added
 assertEquals("User1 should be the selected item", "User1", updateUserPanel.jComboBox2.getSelectedItem().getName());
 }

 @Test
 public void testUpdateUserPanelUpdateUser() {
 User user = new User(1, "User1", "user1@example.com", "1234567890");
 adminApp.getGroups().get(0).getUsers().set(0, user);
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 updateUserPanel.updateModel();
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 // Check if the user has been updated
 assertEquals("User name should be updated", "NewUserName", adminApp.getGroups().get(0).getUsers().get(0).getName());
 assertEquals("Email should be updated", "new.user@example.com", adminApp.getGroups().get(0).getUsers().get(0).getEmail());
 assertEquals("Phone should be updated", "1234567891", adminApp.getGroups().get(0).getUsers().get(0).getDirectPhone());
 assertEquals("Job title should be updated", "New Job Title", adminApp.getGroups().get(0).getUsers().get(0).getJobTitle());
 assertEquals("ShowMyStatus should be updated", true, adminApp.getGroups().get(0).getUsers().get(0).isShowMyStatus());
 assertEquals("OnlyStartChat should be updated", false, adminApp.getGroups().get(0).getUsers().get(0).isOnlyStartChat());
 assertEquals("CanJoinChatRoom should be updated", true, adminApp.getGroups().get(0).getUsers().get(0).isCanJoinChatRoom());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateUserPanelUpdateUserInvalidData() {
 User user = new User(1, "User1", "user1@example.com", "1234567890");
 adminApp.getGroups().get(0).getUsers().set(0, user);
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 updateUserPanel.updateModel();
 updateUserPanel.jTextField1.setText(null);
 updateUserPanel.jTextField2.setText(null);
 updateUserPanel.jTextField3.setText(null);
 updateUserPanel.jTextField4.setText(null);
 updateUserPanel.jButton4.doClick();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateUserPanelUpdateUserEmailExists() {
 User user1 = new User(1, "User1", "user1@example.com", "1234567890");
 User user2 = new User(2, "User2", "user1@example.com", "2345678901");
 adminApp.getGroups().get(0).getUsers().add(user1);
 adminApp.getGroups().get(0).getUsers().add(user2);
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("User1");
 updateUserPanel.jTextField2.setText("user1@example.com"); // email already exists
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 }
}