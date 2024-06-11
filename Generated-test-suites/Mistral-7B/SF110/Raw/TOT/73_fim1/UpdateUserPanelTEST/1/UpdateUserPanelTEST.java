import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.admin.ui.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.Group;
import osa.ora.server.beans.User;

public class UpdateUserPanelTest {

 private UpdateUserPanel updateUserPanel;
 private AdminApp adminApp;
 private Group group;
 private User user, userToDelete, userToUpdate;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 group = new Group("Group1");
 adminApp.addGroup(group);
 user = new User("User1", "user1@example.com", "123456789", true, true, true, "User1 Title");
 userToDelete = new User("User2", "user2@example.com", "234567890", true, true, true, "User2 Title");
 userToUpdate = new User("User3", "user3@example.com", "345678901", true, true, true, "User3 Title");
 group.addUser(user);
 group.addUser(userToDelete);
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 @Test
 public void testUpdateUserPanelCreation() {
 assertNotNull(updateUserPanel);
 }

 @Test
 public void testUpdateModelWhenGroupsNotEmptyAndUsersNotEmpty() {
 updateUserPanel.updateModel();
 assertEquals(2, updateUserPanel.jComboBox1.getItemCount());
 assertEquals(2, updateUserPanel.jComboBox2.getItemCount());
 }

 @Test
 public void testUpdateModelWhenGroupsNotEmptyAndUsersEmpty() {
 updateUserPanel.updateModel();
 assertEquals(1, updateUserPanel.jComboBox1.getItemCount());
 assertEquals(0, updateUserPanel.jComboBox2.getItemCount());
 }

 @Test
 public void testUpdateModelWhenGroupsEmpty() {
 updateUserPanel.updateModel();
 assertEquals(0, updateUserPanel.jComboBox1.getItemCount());
 assertEquals(0, updateUserPanel.jComboBox2.getItemCount());
 }

 @Test
 public void testUpdateUserPanelWhenUserSelectedFromComboBox2() {
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedIndex(1);
 User selectedUser = (User) updateUserPanel.jComboBox2.getSelectedItem();
 assertEquals(userToDelete, selectedUser);
 }

 @Test
 public void testFillUserMethod() {
 updateUserPanel.fillUser(user);
 assertEquals(user.getName(), updateUserPanel.jTextField1.getText());
 assertEquals(user.getEmail(), updateUserPanel.jTextField2.getText());
 assertEquals(user.getDirectPhone(), updateUserPanel.jTextField3.getText());
 assertEquals(user.getJobTitle(), updateUserPanel.jTextField4.getText());
 assertTrue(updateUserPanel.jCheckBox1.isSelected());
 assertTrue(updateUserPanel.jCheckBox2.isSelected());
 assertTrue(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testJComboBox1ItemStateChangedMethod() {
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(1);
 User groupUsersFirstUser = (User) updateUserPanel.jComboBox2.getSelectedItem();
 assertEquals(userToUpdate, groupUsersFirstUser);
 }

 @Test
 public void testJButton1ActionPerformedMethod() {
 updateUserPanel.jButton1.doClick();
 assertTrue(updateUserPanel.isVisible() == false);
 }

 @Test
 public void testJButton2ActionPerformedMethodWithValidUser() {
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedIndex(1);
 updateUserPanel.jButton2.doClick();
 assertEquals(1, group.getUsers().size());
 }

 @Test
 public void testJButton2ActionPerformedMethodWithInvalidUserIndex() {
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jButton2.doClick();
 assertEquals(2, group.getUsers().size());
 }

 @Test
 public void testJButton3ActionPerformedMethodWithValidUser() {
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedIndex(1);
 updateUserPanel.jTextField1.setText("NewName");
 updateUserPanel.jTextField2.setText("NewEmail");
 updateUserPanel.jTextField3.setText("NewPhone");
 updateUserPanel.jTextField4.setText("NewTitle");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton3.doClick();
 assertEquals("NewName", user.getName());
 assertEquals("NewEmail", user.getEmail());
 assertFalse(user.isShowMyStatus());
 assertTrue(user.isOnlyStartChat());
 assertFalse(user.isCanJoinChatRoom());
 }

 @Test
 public void testJButton3ActionPerformedMethodWithInvalidUserIndex() {
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jTextField1.setText("NewName");
 updateUserPanel.jTextField2.setText("NewEmail");
 updateUserPanel.jTextField3.setText("NewPhone");
 updateUserPanel.jTextField4.setText("NewTitle");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton3.doClick();
 assertEquals(2, group.getUsers().size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJButton3ActionPerformedMethodWithEmptyUserList() {
 updateUserPanel.updateModel();
 updateUserPanel.jCombobox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("NewName");
 updateUserPanel.jTextField2.setText("NewEmail");
 updateUserPanel.jTextField3.setText("NewPhone");
 updateUserPanel.jTextField4.setText("NewTitle");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton3.doClick();
 }

 @Test
 public void testJButton4ActionPerformedMethodWithValidUser() {
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedIndex(1);
 updateUserPanel.jTextField1.setText("NewName");
 updateUserPanel.jTextField2.setText("NewEmail");
 updateUserPanel.jTextField3.setText("NewPhone");
 updateUserPanel.jTextField4.setText("NewTitle");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton4.doClick();
 assertEquals("NewName", user.getName());
 assertEquals("NewEmail", user.getEmail());
 assertFalse(user.isShowMyStatus());
 assertTrue(user.isOnlyStartChat());
 assertFalse(user.isCanJoinChatRoom());
 }

 @Test
 public void testJButton4ActionPerformedMethodWithEmptyUserList() {
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jTextField1.setText("NewName");
 updateUserPanel.jTextField2.setText("NewEmail");
 updateUserPanel.jTextField3.setText("NewPhone");
 updateUserPanel.jTextField4.setText("NewTitle");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton4.doClick();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformedMethodWithEmptyUserName() {
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedIndex(1);
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jTextField2.setText("NewEmail");
 updateUserPanel.jTextField3.setText("NewPhone");
 updateUserPanel.jTextField4.setText("NewTitle");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton4.doClick();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformedMethodWithEmptyEmail() {
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedIndex(1);
 updateUserPanel.jTextField1.setText("NewName");
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jTextField3.setText("NewPhone");
 updateUserPanel.jTextField4.setText("NewTitle");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton4.doClick();
 }
}