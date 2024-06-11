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
 public void setup() {
 adminApp = new AdminApp();
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 @Test
 public void testUpdateUserPanelInitialization() {
 // Testing if the UpdateUserPanel is initialized correctly
 assertEquals("UpdateUserPanel initialization failed", 1, updateUserPanel.getComponentCount());
 }

 @Test
 public void testUpdateUserPanelComponents() {
 // Testing if all the components are added correctly
 assertEquals("Components not added correctly", 14, updateUserPanel.getComponents().length);
 }

 @Test
 public void testUpdateUserPanelJComboBox1() {
 // Testing JComboBox1 with valid data
 Group group = new Group("Group1");
 adminApp.addGroup(group);
 updateUserPanel.updateModel();
 User user = new User("User1", "user1@email.com", "1234567890", "Job1", true, true, true);
 group.addUser(user);
 adminApp.updateUser(user);
 assertEquals("JComboBox1 not populated correctly with valid data", "User1", updateUserPanel.jComboBox2.getSelectedItem().getName());

 // Testing JComboBox1 with empty data
 updateUserPanel.updateModel();
 assertEquals("JComboBox1 not populated correctly with empty data", 0, updateUserPanel.jComboBox2.getItemCount());
 }

 @Test
 public void testUpdateUserPanelJButton1ActionPerformed() {
 // Testing JButton1 ActionPerformed with null data
 updateUserPanel.jButton1ActionPerformed(new ActionEvent(new Object(), 1, "Cancel"));
 // Check if the parent dialog is hidden
 assertTrue("Parent dialog not hidden after JButton1 clicked", updateUserPanel.controlPanel.getParentDialog().isVisible() == false);
 }

 @Test(expected = NullPointerException.class)
 public void testUpdateUserPanelJButton2ActionPerformedWithNullUser() {
 // Testing JButton2 ActionPerformed with null user
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 1, "Delete"));
 }

 @Test
 public void testUpdateUserPanelJButton2ActionPerformedWithValidUser() {
 // Testing JButton2 ActionPerformed with valid user
 User user = new User("User1", "user1@email.com", "1234567890", "Job1", true, true, true);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 1, "Delete"));
 // Check if the user is removed from the JComboBox2
 assertEquals("User not removed from JComboBox2 after JButton2 clicked", 0, updateUserPanel.jComboBox2.getItemCount());
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testUpdateUserPanelJComboBox2ItemStateChangedWithInvalidIndex() {
 // Testing JComboBox2 ItemStateChanged with invalid index
 updateUserPanel.jComboBox1.setSelectedIndex(1);
 updateUserPanel.jComboBox2ItemStateChanged(new ItemEvent(new Object(), 1, 2));
 }

 @Test
 public void testUpdateUserPanelJButton3ActionPerformed() {
 // Testing JButton3 ActionPerformed with valid user
 User user = new User("User1", "user1@email.com", "1234567890", "Job1", true, true, true);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("User2");
 updateUserPanel.jTextField2.setText("user2@email.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("Job2");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(new Object(), 1, "Reset Password"));
 // Check if the user's properties are updated
 User updatedUser = new User("User2", "user2@email.com", "1234567891", "Job2", false, true, false);
 assertEquals("User properties not updated after JButton3 clicked", updatedUser, user);
 }

 @Test(expected = NullPointerException.class)
 public void testUpdateUserPanelJButton4ActionPerformedWithNullUser() {
 // Testing JButton4 ActionPerformed with null user
 updateUserPanel.jComboBox2.setSelectedItem(null);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 1, "Update"));
 }

 @Test
 public void testUpdateUserPanelJButton4ActionPerformedWithValidUser() {
 // Testing JButton4 ActionPerformed with valid user
 User user = new User("User1", "user1@email.com", "1234567890", "Job1", true, true, true);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("User2");
 updateUserPanel.jTextField2.setText("user2@email.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("Job2");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 1, "Update"));
 // Check if the user's properties are updated in the AdminApp
 User updatedUser = new User("User2", "user2@email.com", "1234567891", "Job2", false, true, false);
 assertEquals("User properties not updated in AdminApp after JButton4 clicked", updatedUser, adminApp.getUser(user.getId()));
 }
}