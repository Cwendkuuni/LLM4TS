import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import osa.ora.server.admin.AdminApp;
import osa.ora.server.admin.UI.UpdateUserPanel;
import osa.ora.server.beans.Group;
import osa.ora.server.beans.User;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private UpdateUserPanel updateUserPanel;

 @Before
 public void setup() {
 adminApp = new AdminApp();
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 @Test
 public void testUpdateModel() {
 Group group = new Group("Group1");
 User user = new User("User1", "user1@example.com", "12345", true, false, true);
 adminApp.setGroups(new Vector<>(Arrays.asList(group)));
 group.setUsers(new Vector<>(Arrays.asList(user)));
 updateUserPanel.updateModel();

 Assert.assertEquals(group, updateUserPanel.getGroup());
 Assert.assertEquals(user, updateUserPanel.getSelectedUser());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 Group group = new Group("Group1");
 User user1 = new User("User1", "user1@example.com", "12345", true, false, true);
 User user2 = new User("User2", "user2@example.com", "67890", true, false, true);
 group.setUsers(new Vector<>(Arrays.asList(user1, user2)));
 updateUserPanel.updateModel();

 updateUserPanel.jComboBox1.setSelectedIndex(1);
 updateUserPanel.jComboBox1ItemStateChanged(null);

 Assert.assertEquals(user2, updateUserPanel.getSelectedUser());
 }

 @Test
 public void testJButton2ActionPerformed_ValidUserRemoved() {
 Group group = new Group("Group1");
 User user = new User("User1", "user1@example.com", "12345", true, false, true);
 group.setUsers(new Vector<>(Arrays.asList(user)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jButton2.doClick();

 Assert.assertFalse(updateUserPanel.getSelectedUser() instanceof User);
 Assert.assertEquals(group.getUsers().size(), 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton2ActionPerformed_InvalidSelection() {
 updateUserPanel.jButton2.doClick();
 }

 @Test
 public void testJButton3ActionPerformed_ValidUserPasswordReset() {
 Group group = new Group("Group1");
 User user = new User("User1", "user1@example.com", "12345", true, false, true);
 group.setUsers(new Vector<>(Arrays.asList(user)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField2.setText("newemail@example.com");
 updateUserPanel.jButton3.doClick();

 Assert.assertEquals("newemail@example.com", user.getEmail());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton3ActionPerformed_InvalidEmail() {
 updateUserPanel.jTextField2.setText("invalidemail");
 updateUserPanel.jButton3.doClick();
 }

 @Test
 public void testJButton4ActionPerformed_ValidUserUpdated() {
 Group group = new Group("Group1");
 User user = new User("User1", "user1@example.com", "12345", true, false, true);
 group.setUsers(new Vector<>(Arrays.asList(user)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("User2");
 updateUserPanel.jTextField2.setText("user2@example.com");
 updateUserPanel.jTextField3.setText("11111");
 updateUserPanel.jTextField4.setText("Developer");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();

 User updatedUser = group.getUsers().get(0);
 Assert.assertEquals("User2", updatedUser.getName());
 Assert.assertEquals("user2@example.com", updatedUser.getEmail());
 Assert.assertEquals("11111", updatedUser.getDirectPhone());
 Assert.assertEquals("Developer", updatedUser.getJobTitle());
 Assert.assertTrue(updatedUser.isShowMyStatus());
 Assert.assertFalse(updatedUser.isOnlyStartChat());
 Assert.assertTrue(updatedUser.isCanJoinChatRoom());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformed_InvalidName() {
 updateUserPanel.jTextField1.setText(" ");
 updateUserPanel.jTextField2.setText("user2@example.com");
 updateUserPanel.jTextField3.setText("11111");
 updateUserPanel.jTextField4.setText("Developer");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformed_InvalidEmail() {
 updateUserPanel.jTextField1.setText("User2");
 updateUserPanel.jTextField2.setText("invalidemail");
 updateUserPanel.jTextField3.setText("11111");
 updateUserPanel.jTextField4.setText("Developer");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformed_InvalidPhone() {
 updateUserPanel.jTextField1.setText("User2");
 updateUserPanel.jTextField2.setText("user2@example.com");
 updateUserPanel.jTextField3.setText("invalidphone");
 updateUserPanel.jTextField4.setText("Developer");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformed_InvalidJobTitle() {
 updateUserPanel.jTextField1.setText("User2");
 updateUserPanel.jTextField2.setText("user2@example.com");
 updateUserPanel.jTextField3.setText("11111");
 updateUserPanel.jTextField4.setText(" ");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 }
}