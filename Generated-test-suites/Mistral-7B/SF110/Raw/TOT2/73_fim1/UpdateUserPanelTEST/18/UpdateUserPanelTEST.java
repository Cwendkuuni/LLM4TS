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
 public void testUpdateModel() {
 Group group = new Group("Group1");
 adminApp.setGroups(new Vector<Group>() {{
 add(group);
 }});
 User user = new User("User1", "user1@example.com", "1234567890", "Job Title 1", true, true, true);
 group.setUsers(new Vector<User>() {{
 add(user);
 }});
 updateUserPanel.updateModel();

 assertEquals(group, updateUserPanel.adminApp.getGroups().get(0));
 assertEquals(user, updateUserPanel.jComboBox2.getSelectedItem());
 }

 @Test
 public void testJButton1ActionPerformedCancel() {
 updateUserPanel.jButton1ActionPerformed(new ActionEvent(new Object(), 0, "Cancel"));
 assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible());
 }

 @Test
 public void testJButton2ActionPerformedDeleteUserNullUser() {
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 0, ""));
 assertFalse(updateUserPanel.jComboBox2.getModel().getSize() > 0);
 }

 @Test
 public void testJButton2ActionPerformedDeleteUserInvalidIndex() {
 User user = new User("User1", "user1@example.com", "1234567890", "Job Title 1", true, true, true);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 0, ""));
 assertFalse(updateUserPanel.jComboBox2.getModel().getSize() > 0);
 }

 @Test
 public void testJButton2ActionPerformedDeleteUserSuccess() {
 Group group = new Group("Group1");
 adminApp.setGroups(new Vector<Group>() {{
 add(group);
 }});
 User user = new User("User1", "user1@example.com", "1234567890", "Job Title 1", true, true, true);
 group.setUsers(new Vector<User>() {{
 add(user);
 }});
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 0, "Delete"));
 assertEquals(0, group.getUsers().size());
 }

 @Test
 public void testJButton3ActionPerformedResetPasswordNullUser() {
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(new Object(), 0, ""));
 assertFalse(updateUserPanel.jComboBox2.getModel().getSize() > 0);
 }

 @Test
 public void testJButton3ActionPerformedResetPasswordUserNotFound() {
 User user = new User("User1", "user1@example.com", "1234567890", "Job Title 1", true, true, true);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(new Object(), 0, ""));
 assertFalse(user.isPasswordReset());
 }

 @Test
 public void testJButton3ActionPerformedResetPasswordSuccess() {
 Group group = new Group("Group1");
 adminApp.setGroups(new Vector<Group>() {{
 add(group);
 }});
 User user = new User("User1", "user1@example.com", "1234567890", "Job Title 1", true, true, true);
 group.setUsers(new Vector<User>() {{
 add(user);
 }});
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(new Object(), 0, ""));
 assertTrue(user.isPasswordReset());
 }

 @Test
 public void testJButton4ActionPerformedUpdateUserNullUser() {
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, ""));
 assertFalse(updateUserPanel.jComboBox2.getModel().getSize() > 0);
 }

 @Test
 public void testJButton4ActionPerformedUpdateUserInvalidInputs() {
 Group group = new Group("Group1");
 adminApp.setGroups(new Vector<Group>() {{
 add(group);
 }});
 User user = new User("User1", "user1@example.com", "1234567890", "Job Title 1", true, true, true);
 group.setUsers(new Vector<User>() {{
 add(user);
 }});
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, ""));
 assertFalse(updateUserPanel.jComboBox2.getModel().getSize() > 0);
 }

 @Test
 public void testJButton4ActionPerformedUpdateUserSameEmail() {
 Group group1 = new Group("Group1");
 Group group2 = new Group("Group2");
 adminApp.setGroups(new Vector<Group>() {{
 add(group1);
 add(group2);
 }});
 User user1 = new User("User1", "user1@example.com", "1234567890", "Job Title 1", true, true, true);
 User user2 = new User("User2", "user1@example.com", "1234567891", "Job Title 2", true, true, true);
 group1.setUsers(new Vector<User>() {{
 add(user1);
 }});
 group2.setUsers(new Vector<User>() {{
 add(user2);
 }});
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedItem(group1);
 updateUserPanel.jComboBox2.setSelectedItem(user1);
 updateUserPanel.jTextField1.setText("User3");
 updateUserPanel.jTextField2.setText("user1@example.com");
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, ""));
 assertFalse(updateUserPanel.jComboBox2.getModel().getSize() > 0);
 }

 @Test
 public void testJButton4ActionPerformedUpdateUserSuccess() {
 Group group = new Group("Group1");
 adminApp.setGroups(new Vector<Group>() {{
 add(group);
 }});
 User user = new User("User1", "user1@example.com", "1234567890", "Job Title 1", true, true, true);
 group.setUsers(new Vector<User>() {{
 add(user);
 }});
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("UserUpdated");
 updateUserPanel.jTextField2.setText("userUpdated@example.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("Job Title Updated");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, ""));
 User updatedUser = group.getUsers().get(0);
 assertEquals("UserUpdated", updatedUser.getName());
 assertEquals("userUpdated@example.com", updatedUser.getEmail());
 assertEquals("1234567891", updatedUser.getDirectPhone());
 assertFalse(updatedUser.isShowMyStatus());
 assertTrue(updatedUser.isOnlyStartChat());
 assertFalse(updatedUser.isCanJoinChatRoom());
 }
}