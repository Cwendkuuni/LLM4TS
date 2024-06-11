import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.admin.ui.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

 private UpdateUserPanel updateUserPanel;
 private AdminApp adminApp;
 private Group group;
 private User user;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 group = new Group("Group1");
 user = new User("User1", "user1@example.com", "1234567890", "John Doe", true, true, true);
 group.addUser(user);
 adminApp.setGroups(new Vector<Group>( Arrays.asList(group) ));
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 @Test
 public void testUpdateModelWithEmptyGroupsList() {
 updateUserPanel.updateModel();
 assertEquals("", updateUserPanel.jComboBox2.getModel().getClass().getSimpleName());
 }

 @Test
 public void testUpdateModelWithEmptyUsersList() {
 group.setUsers(new Vector<User>());
 updateUserPanel.updateModel();
 assertEquals("", updateUserPanel.jComboBox2.getModel().getClass().getSimpleName());
 }

 @Test
 public void testUpdateModelWithUsersList() {
 updateUserPanel.updateModel();
 assertEquals(UpdateUserPanel.this.getClass(), updateUserPanel.jComboBox2.getRenderer().getClass());
 assertEquals(user.getName(), updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test
 public void testInitComponents() {
 assertEquals(4, updateUserPanel.getComponentCount());
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1ActionPerformed(null);
 assertTrue(updateUserPanel.isVisible());
 assertTrue(adminApp.getDialogs().isEmpty());
 }

 @Test
 public void testJButton2ActionPerformedWithNullUser() {
 updateUserPanel.jButton2ActionPerformed(null);
 assertFalse(adminApp.getDialogs().isEmpty());
 }

 @Test
 public void testJButton2ActionPerformedWithValidUser() {
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jButton2ActionPerformed(null);
 assertFalse(group.getUsers().contains(user));
 }

 @Test
 public void testJButton3ActionPerformedWithNullUser() {
 updateUserPanel.jButton3ActionPerformed(null);
 assertFalse(adminApp.getDialogs().isEmpty());
 }

 @Test
 public void testJButton3ActionPerformedWithValidUser() {
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("newUserEmail@example.com");
 updateUserPanel.jButton3ActionPerformed(null);
 assertTrue(group.getUsers().contains(user));
 assertTrue(user.getName().equals("NewUserName"));
 assertTrue(user.getEmail().equals("newUserEmail@example.com"));
 }

 @Test
 public void testJButton4ActionPerformedWithNullUser() {
 updateUserPanel.jButton4ActionPerformed(null);
 assertFalse(adminApp.getDialogs().isEmpty());
 }

 @Test
 public void testJButton4ActionPerformedWithValidUser() {
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("newUserEmail@example.com");
 updatePanel.jTextField3.setText("1234567891");
 updatePanel.jTextField4.setText("NewJobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(null);
 assertTrue(group.getUsers().contains(user));
 assertTrue(user.getName().equals("NewUserName"));
 assertTrue(user.getEmail().equals("newUserEmail@example.com"));
 assertTrue(user.getDirectPhone().equals("1234567891"));
 assertTrue(user.isShowMyStatus());
 assertTrue(user.isOnlyStartChat());
 assertTrue(user.isCanJoinChatRoom());
 }
}