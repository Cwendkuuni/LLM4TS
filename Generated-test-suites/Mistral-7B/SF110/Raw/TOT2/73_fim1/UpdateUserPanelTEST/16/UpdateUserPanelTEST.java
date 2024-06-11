import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.admin.ui.UpdateUserPanel;
import osa.ora.server.admin.AdminApp;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

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
 assertNotNull(updateUserPanel.adminApp);
 assertNotNull(updateUserPanel.controlPanel);
 adminApp.setGroups(new ArrayList<>(Arrays.asList(new Group("Group1"), new Group("Group2"))));
 updateUserPanel.updateModel();
 assertEquals(2, updateUserPanel.jComboBox1.getItemCount());
 Group group = (Group) updateUserPanel.jComboBox1.getItemAt(0);
 assertEquals("Group1", group.getName());
 User user = new User("User1", "user1@example.com", "1234567890", "JobTitle1", true, true, true);
 group.setUsers(new Vector<>(Arrays.asList(user)));
 updateUserPanel.updateModel();
 assertEquals("User1", updateUserPanel.jComboBox2.getItemAt(0).getName());
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1.doClick();
 assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible());
 reset(updateUserPanel);
 }

 @Test(expected = NullPointerException.class)
 public void testJButton2ActionPerformedWithNullUser() {
 updateUserPanel.jButton2.doClick();
 }

 @Test
 public void testJButton2ActionPerformedWithValidUser() {
 User user = new User("User1", "user1@example.com", "1234567890", "JobTitle1", true, true, true);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jButton2.doClick();
 assertTrue(adminApp.getGroups().get(0).getUsers().remove(user));
 assertTrue(updateUserPanel.jComboBox2.getItemCount() < 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJButton2ActionPerformedWithInvalidIndex() {
 updateUserPanel.jComboBox2.setModel(new DefaultComboBoxModel<>(new Object[0]));
 updateUserPanel.jButton2.doClick();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton3ActionPerformedWithNullUser() {
 updateUserPanel.jButton3.doClick();
 }

 @Test
 public void testJButton3ActionPerformedWithValidUser() {
 User user = new User("User1", "user1@example.com", "1234567890", "JobTitle1", true, true, true);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("NewPhoneNumber");
 updateUserPanel.jTextField4.setText("NewJobTitle");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton3.doClick();
 User updatedUser = adminApp.getGroups().get(0).getUsers().get(0);
 assertEquals("NewUserName", updatedUser.getName());
 assertEquals("new.user@example.com", updatedUser.getEmail());
 assertEquals("NewPhoneNumber", updatedUser.getDirectPhone());
 assertEquals("NewJobTitle", updatedUser.getJobTitle());
 assertFalse(updatedUser.isShowMyStatus());
 assertFalse(updatedUser.isOnlyStartChat());
 assertFalse(updatedUser.isCanJoinChatRoom());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformedWithNullGroupName() {
 updateUserPanel.adminApp.setGroups(new ArrayList<>());
 updateUserPanel.jComboBox1.setSelectedItem("InvalidGroupName");
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("NewPhoneNumber");
 updateUserPanel.jTextField4.setText("NewJobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformedWithNullUserEmail() {
 updateUserPanel.jTextField2.setText(null);
 updateUserPanel.performUpdate();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformedWithEmptyUserEmail() {
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.performUpdate();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformedWithExistingEmail() {
 Group group = new Group("Group1");
 User existingUser = new User("User1", "user1@example.com", "1234567890", "JobTitle1", true, true, true);
 group.setUsers(new Vector<>(Arrays.asList(existingUser)));
 adminApp.setGroups(new ArrayList<>(Arrays.asList(group)));
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2.setModel(new DefaultComboBoxModel<>(group.getUsers().toArray()));
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("user1@example.com");
 updateUserPanel.jTextField3.setText("NewPhoneNumber");
 updateUserPanel.jTextField4.setText("NewJobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 }

 @Test
 public void testJButton4ActionPerformedWithNewEmail() {
 Group group = new Group("Group1");
 User existingUser = new User("User1", "user1@example.com", "1234567890", "JobTitle1", true, true, true);
 group.setUsers(new Vector<>(Arrays.asList(existingUser)));
 adminApp.setGroups(new ArrayList<>(Arrays.asList(group)));
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2.setModel(new DefaultComboBoxModel<>(group.getUsers().toArray()));
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("NewPhoneNumber");
 updateUserPanel.jTextField4.setText("NewJobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 User newUser = adminApp.getGroups().get(0).getUsers().get(1);
 assertEquals("NewUserName", newUser.getName());
 assertEquals("new.user@example.com", newUser.getEmail());
 assertEquals("NewPhoneNumber", newUser.getDirectPhone());
 assertEquals("NewJobTitle", newUser.getJobTitle());
 assertTrue(newUser.isShowMyStatus());
 assertTrue(newUser.isOnlyStartChat());
 assertTrue(newUser.isCanJoinChatRoom());
 }

 private void reset(UpdateUserPanel updateUserPanel) {
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jTextField3.setText("");
 updateUserPanel.jTextField4.setText("");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(false);
 }
}