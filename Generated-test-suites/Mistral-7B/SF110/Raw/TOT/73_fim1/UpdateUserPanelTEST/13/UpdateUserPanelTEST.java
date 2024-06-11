import org.junit.Before;
import org.junit.Test;
import osa.ora.server.admin.*;
import static org.junit.Assert.*;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private UpdateUserPanel updateUserPanel;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 @Test
 public void testInitComponents() {
 // Test if components are initialized correctly
 assertEquals(10, updateUserPanel.jLabel1.getX());
 assertEquals(50, updateUserPanel.jLabel1.getY());
 // Add more assertions for other components
 }

 @Test
 public void testUpdateModel() {
 // Test if updateModel method updates the JComboBoxes correctly
 adminApp.setGroups(new ArrayList<Group>());
 adminApp.getGroups().add(new Group("Group1", new ArrayList<User>()));
 updateUserPanel.updateModel();
 assertEquals(1, updateUserPanel.jComboBox1.getItemCount());
 assertEquals("Group1", updateUserPanel.jComboBox1.getItemAt(0).toString());

 updateUserPanel.jComboBox1.setSelectedIndex(0);
 adminApp.getGroups().get(0).getUsers().add(new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true));
 updateUserPanel.updateModel();
 assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
 assertEquals("User1", updateUserPanel.jComboBox2.getItemAt(0).toString());
 }

 @Test
 public void testjButton1ActionPerformed() {
 // Test if jButton1ActionPerformed method hides the dialog correctly
 updateUserPanel.jButton1.doClick();
 assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible() == false);
 }

 @Test
 public void testjButton2ActionPerformed_ValidUser() {
 // Test if jButton2ActionPerformed method deletes the user correctly
 adminApp.setGroups(new ArrayList<Group>());
 adminApp.getGroups().add(new Group("Group1", new ArrayList<User>()));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);

 User user = updateUserPanel.jComboBox2.getSelectedItem();
 updateUserPanel.jButton2.doClick();
 assertEquals(0, updateUserPanel.jComboBox2.getItemCount());
 }

 @Test
 public void testjButton2ActionPerformed_InvalidUserSelection() {
 // Test if jButton2ActionPerformed method does not delete the user when no user is selected
 updateUserPanel.updateModel();
 updateUserPanel.jButton2.doClick();
 assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
 }

 @Test
 public void testjButton3ActionPerformed_ValidUser() {
 // Test if jButton3ActionPerformed method resets the user password correctly
 adminApp.setGroups(new ArrayList<Group>());
 adminApp.getGroups().add(new Group("Group1", new ArrayList<User>()));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);

 User user = updateUserPanel.jComboBox2.getSelectedItem();
 updateUserPanel.jTextField2.setText("newpassword");
 updateUserPanel.jButton3.doClick();
 assertFalse(user.getPassword().equals("1234567890"));
 assertTrue(user.getPassword().equals("newpassword"));
 }

 @Test
 public void testjButton3ActionPerformed_InvalidUserSelection() {
 // Test if jButton3ActionPerformed method does not reset the password when no user is selected
 updateUserPanel.updateModel();
 updateUserPanel.jTextField2.setText("newpassword");
 updateUserPanel.jButton3.doClick();
 assertEquals("1234567890", updateUserPanel.jComboBox2.getSelectedItem().getPassword());
 }

 @Test
 public void testjButton4ActionPerformed_ValidUser() {
 // Test if jButton4ActionPerformed method updates the user correctly
 adminApp.setGroups(new ArrayList<Group>());
 adminApp.getGroups().add(new Group("Group1", new ArrayList<User>()));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 User user = updateUserPanel.jComboBox2.getSelectedItem();
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("newuser@example.com");
 updateUserPanel.jTextField3.setText("NewPhoneNumber");
 updateUserPanel.jTextField4.setText("NewJobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 assertTrue(user.getName().equals("NewUserName"));
 assertTrue(user.getEmail().equals("newuser@example.com"));
 assertTrue(user.getDirectPhone().equals("NewPhoneNumber"));
 assertTrue(user.isShowMyStatus() == updateUserPanel.jCheckBox1.isSelected());
 assertTrue(user.isOnlyStartChat() == updateUserPanel.jCheckBox2.isSelected());
 assertTrue(user.isCanJoinChatRoom() == updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testjButton4ActionPerformed_InvalidEmail() {
 // Test if jButton4ActionPerformed method does not update the user when an invalid email is entered
 adminApp.setGroups(new ArrayList<Group>());
 adminApp.getGroups().add(new Group("Group1", new ArrayList<User>()));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 User user = updateUserPanel.jComboBox2.getSelectedItem();
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("example.com");
 updateUserPanel.jTextField3.setText("NewPhoneNumber");
 updateUserPanel.jTextField4.setText("NewJobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 assertEquals(user.getEmail(), "user1@example.com");
 }

 @Test
 public void testjButton4ActionPerformed_DuplicateEmail() {
 // Test if jButton4ActionPerformed method does not update the user when the same email already exists
 adminApp.setGroups(new ArrayList<Group>());
 adminApp.getGroups().add(new Group("Group1", new ArrayList<User>()));
 adminApp.getGroups().add(new Group("Group2", new ArrayList<User>()));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 User user1 = updateUserPanel.jComboBox2.getSelectedItem();
 updateUserPanel.jComboBox1.setSelectedIndex(1);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 User user2 = updateUserPanel.jComboBox2.getSelectedItem();
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("user1@example.com");
 updateUserPanel.jTextField3.setText("NewPhoneNumber");
 updateUserPanel.jTextField4.setText("NewJobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 assertEquals(user1.getEmail(), "user1@example.com");
 assertEquals(user2.getEmail(), "user1@example.com");
 }
}