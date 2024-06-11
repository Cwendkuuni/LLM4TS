import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import osa.ora.server.admin.ui.*;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private UpdateUserPanel updateUserPanel;
 private Group group;
 private User user, userToDelete, userToReset;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 group = new Group("Group1");
 adminApp.addGroup(group);
 user = new User("User1", "user1@example.com", "Password1", "Phone1", "Title1", true, true, true);
 userToDelete = new User("User2", "user2@example.com", "Password2", "Phone2", "Title2", true, true, true);
 userToReset = new User("User3", "user3@example.com", "Password3", "Phone3", "Title3", true, true, true);
 group.addUser(user);
 group.addUser(userToDelete);
 group.addUser(userToReset);
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 @Test
 public void testUpdateModel_ValidData() {
 adminApp.updateModel();
 assertEquals(3, updateUserPanel.jComboBox1.getItemCount());
 assertEquals(user.getName(), updateUserPanel.jComboBox2.getItemAt(0).toString());
 }

 @Test
 public void testUpdateModel_EmptyData() {
 adminApp.adminApp.getGroups().clear();
 adminApp.updateModel();
 assertEquals(0, updateUserPanel.jComboBox1.getItemCount());
 }

 @Test
 public void testUpdateModel_NullData() {
 adminApp.adminApp.getGroups().clear();
 adminApp = null;
 updateUserPanel = new UpdateUserPanel(null, null);
 adminApp = new AdminApp();
 adminApp.updateModel();
 assertEquals(0, updateUserPanel.jComboBox1.getItemCount());
 }

 @Test
 public void testJButton1ActionPerformed_Cancel() {
 updateUserPanel.jButton1ActionPerformed(new ActionEvent(new Object(), new ActionEvent.ActionEventType(), 1, "Button1", new Object[]{}));
 assertTrue(updateUserPanel.isVisible());
 }

 @Test
 public void testJButton2ActionPerformed_DeleteNoUser() {
 updateUserPanel.jComboBox1 = updateUserPanel.jComboBox1;
 updateUserPanel.jComboBox2 = updateUserPanel.jComboBox2;
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(new Object(), ItemEvent.SELECTED, 0, ItemEvent.DESELECTED, null));
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), new ActionEvent.ActionEventType(), 1, "Button2", new Object[]{}));
 assertFalse(updateUserPanel.isVisible());
 }

 @Test
 public void testJButton2ActionPerformed_DeleteUser() {
 updateUserPanel.jComboBox1 = updateUserPanel.jComboBox1;
 updateUserPanel.jComboBox2 = updateUserPanel.jComboBox2;
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(group, ItemEvent.SELECTED, 0, ItemEvent.DOUBLE_CLICKED, null));
 updateUserPanel.jComboBox2ItemStateChanged(new ItemEvent(userToDelete, ItemEvent.SELECTED, 0, ItemEvent.DESELECTED, null));
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), new ActionEvent.ActionEventType(), 1, "Button2", new Object[]{}));
 assertEquals(2, group.getUsers().size());
 assertFalse(updateUserPanel.isVisible());
 }

 @Test
 public void testJButton3ActionPerformed_ResetPasswordNoUser() {
 updateUserPanel.jComboBox1 = updateUserPanel.jComboBox1;
 updateUserPanel.jComboBox2 = updateUserPanel.jComboBox2;
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(new Object(), ItemEvent.SELECTED, 0, ItemEvent.DOUBLE_CLICKED, null));
 updateUserPanel.jComboBox2ItemStateChanged(new ItemEvent(new Object(), ItemEvent.SELECTED, 1, ItemEvent.DESELECTED, null));
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(new Object(), new ActionEvent.ActionEventType(), 1, "Button3", new Object[]{}));
 assertTrue(updateUserPanel.isVisible());
 }

 @Test
 public void testJButton3ActionPerformed_ResetPasswordUser() {
 updateUserPanel.jComboBox1 = updateUserPanel.jComboBox1;
 updateUserPanel.jComboBox2 = updateUserPanel.jComboBox2;
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(group, ItemEvent.SELECTED, 0, ItemEvent.DOUBLE_CLICKED, null));
 updateUserPanel.jComboBox2ItemStateChanged(new ItemEvent(userToReset, ItemEvent.SELECTED, 0, ItemEvent.DESELECTED, null));
 updateUserPanel.jTextField3.setText("NewPassword1");
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(new Object(), new ActionEvent.ActionEventType(), 1, "Button3", new Object[]{}));
 assertEquals("NewPassword1", userToReset.getPassword());
 assertTrue(updateUserPanel.isVisible());
 }

 @Test
 public void testJButton4ActionPerformed_UpdateUserEmptyFields() {
 updateUserPanel.jComboBox1 = updateUserPanel.jComboBox1;
 updateUserPanel.jComboBox2 = updateUserPanel.jComboBox2;
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(group, ItemEvent.SELECTED, 0, ItemEvent.DOUBLE_CLICKED, null));
 updateUserPanel.jComboBox2ItemStateChanged(new ItemEvent(user, ItemEvent.SELECTED, 0, ItemEvent.DESELECTED, null));
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jTextField3.setText("");
 updateUserPanel.jTextField4.setText("");
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), new ActionEvent.ActionEventType(), 1, "Button4", new Object[]{}));
 assertTrue(updateUserPanel.isVisible());
 assertFalse(adminApp.getUserByName(user.getName()).isUpdated());
 }

 @Test
 public void testJButton4ActionPerformed_UpdateUserValidData() {
 updateUserPanel.jComboBox1 = updateUserPanel.jComboBox1;
 updateUserPanel.jComboBox2 = updateUserPanel.jComboBox2;
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(group, ItemEvent.SELECTED, 0, ItemEvent.DOUBLE_CLICKED, null));
 updateUserPanel.jComboBox2ItemStateChanged(new ItemEvent(user, ItemEvent.SELECTED, 0, ItemEvent.DESELECTED, null));
 updateUserPanel.jTextField1.setText("NewName");
 updateUserPanel.jTextField2.setText("NewEmail@example.com");
 updateUserPanel.jTextField3.setText("NewPhone");
 updateUserPanel.jTextField4.setText("NewTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), new ActionEvent.ActionEventType(), 1, "Button4", new Object[]{}));
 assertEquals("NewName", adminApp.getUserByName(user.getName()).getName());
 assertEquals("NewEmail@example.com", adminApp.getUserByName(user.getName()).getEmail());
 assertEquals("NewPhone", adminApp.getUserByName(user.getName()).getDirectPhone());
 assertEquals("NewTitle", adminApp.getUserByName(user.getName()).getJobTitle());
 assertTrue(adminApp.getUserByName(user.getName()).isShowMyStatus());
 assertTrue(adminApp.getUserByName(user.getName()).isOnlyStartChat());
 assertTrue(adminApp.getUserByName(user.getName()).isCanJoinChatRoom());
 assertTrue(updateUserPanel.isVisible());
 }

 @Test
 public void testJButton4ActionPerformed_UpdateUserInvalidEmail() {
 updateUserPanel.jComboBox1 = updateUserPanel.jComboBox1;
 updateUserPanel.jComboBox2 = updateUserPanel.jComboBox2;
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(group, ItemEvent.SELECTED, 0, ItemEvent.DOUBLE_CLICKED, null));
 updateUserPanel.jComboBox2ItemStateChanged(new ItemEvent(user, ItemEvent.SELECTED, 0, ItemEvent.DESELECTED, null));
 updateUserPanel.jTextField1.setText("NewName");
 updateUserPanel.jTextField2.setText("user1example.com");
 updateUserPanel.jTextField3.setText("NewPhone");
 updateUserPanel.jTextField4.setText("NewTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), new ActionEvent.ActionEventType(), 1, "Button4", new Object[]{}));
 assertFalse(adminApp.getUserByName(user.getName()).isUpdated());
 assertTrue(updateUserPanel.isVisible());
 }

 @Test
 public void testJButton4ActionPerformed_UpdateUserSameEmail() {
 updateUserPanel.jComboBox1 = updateUserPanel.jComboBox1;
 updateUserPanel.jComboBox2 = updateUserPanel.jComboBox2;
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(group, ItemEvent.SELECTED, 0, ItemEvent.DOUBLE_CLICKED, null));
 updateUserPanel.jComboBox2ItemStateChanged(new ItemEvent(user, ItemEvent.SELECTED, 0, ItemEvent.DESELECTED, null));
 updateUserPanel.jComboBox2ItemStateChanged(new ItemEvent(userToReset, ItemEvent.SELECTED, 0, ItemEvent.DESELECTED, null));
 updateUserPanel.jTextField1.setText("NewName");
 updateUserPanel.jTextField2.setText(userToDelete.getEmail());
 updateUserPanel.jTextField3.setText("NewPhone");
 updateUserPanel.jTextField4.setText("NewTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), new ActionEvent.ActionEventType(), 1, "Button4", new Object[]{}));
 assertFalse(adminApp.getUserByName(user.getName()).isUpdated());
 assertEquals(2, group.getUsers().size());
 assertTrue(updateUserPanel.isVisible());
 }
}