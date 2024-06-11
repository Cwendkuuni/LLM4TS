import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.admin.ui.UpdateUserPanel;
import osa.ora.server.admin.AdminApp;
import osa.ora.server.beans.Group;
import osa.ora.server.beans.User;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private UpdateUserPanel updateUserPanel;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 @After
 public void tearDown() {
 adminApp.getGroups().clear();
 }

 @Test
 public void testUpdateModel() {
 Group group = new Group("testGroup");
 adminApp.addGroup(group);
 User user = new User("testUser", "testEmail@example.com");
 group.addUser(user);
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 updateUserPanel.updateModel();
 assertEquals("testGroup", updateUserPanel.jComboBox1.getSelectedItem().toString());
 assertEquals("testUser", updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test
 public void testReset() {
 updateUserPanel.reset();
 assertNull(updateUserPanel.jTextField1.getText());
 assertNull(updateUserPanel.jTextField2.getText());
 assertNull(updateUserPanel.jTextField3.getText());
 assertNull(updateUserPanel.jTextField4.getText());
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 Group group = new Group("testGroup");
 adminApp.addGroup(group);
 User user1 = new User("user1", "user1@example.com");
 User user2 = new User("user2", "user2@example.com");
 group.addUser(user1);
 group.addUser(user2);
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, ItemEvent.SELECTED, 0, false));
 assertEquals(user1, updateUserPanel.jComboBox2.getSelectedItem());
 }

 @Test(expected = NullPointerException.class)
 public void testJButton2ActionPerformedWithNullUser() {
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(new JButton(), 0, "test"));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJButton2ActionPerformedWithNoSelectedUser() {
 Group group = new Group("testGroup");
 adminApp.addGroup(group);
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 updateUserPanel.updateModel();
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(new JButton(), 0, "test"));
 }

 @Test
 public void testJButton3ActionPerformedWithValidUser() {
 Group group = new Group("testGroup");
 adminApp.addGroup(group);
 User user = new User("testUser", "testEmail@example.com");
 group.addUser(user);
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("testName");
 updateUserPanel.jTextField2.setText("testEmail@example.com");
 updateUserPanel.jTextField3.setText("testPhone");
 updateUserPanel.jTextField4.setText("testJobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(new JButton(), 0, "test"));
 User updatedUser = (User)updateUserPanel.jComboBox2.getSelectedItem();
 assertEquals("testName", updatedUser.getName());
 assertEquals("testEmail@example.com", updatedUser.getEmail());
 assertEquals("testPhone", updatedUser.getDirectPhone());
 assertEquals("testJobTitle", updatedUser.getJobTitle());
 assertTrue(updatedUser.isShowMyStatus());
 assertTrue(updatedUser.isOnlyStartChat());
 assertTrue(updatedUser.isCanJoinChatRoom());
 }

 @Test(expected = NullPointerException.class)
 public void testJButton4ActionPerformedWithNullUserGroup() {
 UpdateUserPanel updateUserPanel = new UpdateUserPanel(null, null);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new JButton(), 0, "test"));
 }

 @Test(expected = NullPointerException.class)
 public void testJButton4ActionPerformedWithNullUser() {
 Group group = new Group("testGroup");
 adminApp.addGroup(group);
 UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
 updateUserPanel.updateModel();
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new JButton(), 0, "test"));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJButton4ActionPerformedWithNoSelectedUser() {
 Group group = new Group("testGroup");
 adminApp.addGroup(group);
 UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
 updateUserPanel.updateModel();
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new JButton(), 0, "test"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformedWithEmptyUserName() {
 Group group = new Group("testGroup");
 adminApp.addGroup(group);
 User user = new User("testUser", "testEmail@example.com");
 group.addUser(user);
 UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jTextField2.setText("testEmail@example.com");
 updateUserPanel.jTextField3.setText("testPhone");
 updateUserPanel.jTextField4.setText("testJobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new JButton(), 0, "test"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformedWithEmptyUserEmail() {
 Group group = new Group("testGroup");
 adminApp.addGroup(group);
 User user = new User("testUser", "testEmail@example.com");
 group.addUser(user);
 UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("testName");
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jTextField3.setText("testPhone");
 updateUserPanel.jTextField4.setText("testJobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new JButton(), 0, "test"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformedWithSameEmailUser() {
 Group group = new Group("testGroup");
 adminApp.addGroup(group);
 User user1 = new User("user1", "user1@example.com");
 User user2 = new User("user2", "user1@example.com");
 group.addUser(user1);
 group.addUser(user2);
 UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2.setSelectedItem(user1);
 updateUserPanel.jTextField1.setText("testName1");
 updateUserPanel.jTextField2.setText("user1@example.com");
 updateUserPanel.jTextField3.setText("testPhone");
 updateUserPanel.jTextField4.setText("testJobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new JButton(), 0, "test"));
 }
}