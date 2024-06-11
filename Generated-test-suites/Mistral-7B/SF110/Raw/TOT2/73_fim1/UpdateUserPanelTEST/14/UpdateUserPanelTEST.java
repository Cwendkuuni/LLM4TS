import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.admin.ui.UpdateUserPanel;
import osa.ora.server.admin.AdminApp;
import osa.ora.server.beans.Group;
import osa.ora.server.beans.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
 Group group = new Group("Group1");
 User user = new User("User1", "user1@example.com", "1234567890", true, false, true);
 List<Group> groups = new ArrayList<>();
 List<User> users = new ArrayList<>();
 users.add(user);
 group.setUsers(users);
 groups.add(group);
 adminApp.setGroups(groups);

 updateUserPanel.updateModel();

 assertEquals("Group1", updateUserPanel.jComboBox1.getSelectedItem().toString());
 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test
 public void testInitComponents() {
 UpdateUserPanel spyUpdateUserPanel = spy(new UpdateUserPanel(adminApp, null));
 verifyNoMoreInteractions(spyUpdateUserPanel);

 List<String> jComboBox1Items = Arrays.asList("Item 1", "Item 2", "Item 3", "Item 4");
 for (String item : jComboBox1Items) {
 verify(spyUpdateUserPanel.jComboBox1).addItem(eq(item));
 }
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1.doClick();
 verify(updateUserPanel.controlPanel.getParentDialog()).setVisible(eq(false));
 }

 @Test(expected = NullPointerException.class)
 public void testJButton2ActionPerformedNullUserSelected() {
 updateUserPanel.jButton2.doClick();
 }

 @Test
 public void testJButton2ActionPerformedValidUserSelected() {
 Group group = new Group("Group1");
 User user = new User("User1", "user1@example.com", "1234567890", true, false, true);
 List<Group> groups = new ArrayList<>();
 List<User> users = new ArrayList<>();
 users.add(user);
 group.setUsers(users);
 adminApp.setGroups(groups);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);

 updateUserPanel.jButton2.doClick();

 verify(adminApp).delUser(eq(user));
 verify(updateUserPanel.jComboBox2).setSelectedIndex(0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJButton2ActionPerformedOutOfBoundsIndex() {
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jButton2.doClick();
 }

 @Test
 public void testJButton3ActionPerformed() {
 updateUserPanel.jButton3.doClick();
 verifyNoMoreInteractions(adminApp);
 }

 @Test(expected = NullPointerException.class)
 public void testJButton3ActionPerformedNullUserSelected() {
 updateUserPanel.jButton3.doClick();
 }

 @Test
 public void testJButton3ActionPerformedValidUserSelected() {
 User user = new User("User1", "user1@example.com", "1234567890", true, false, true);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("New User Email");
 updateUserPanel.jButton3.doClick();

 User newUser = new User("New User Name", "New User Email", "1234567890", true, false, true);
 verify(adminApp).resetUserPass(eq(user));
 verify(updateUserPanel.jTextField1).setText("New User Name");
 verify(updateUserPanel.jTextField2).setText("New User Email");
 }

 @Test
 public void testJButton4ActionPerformed() {
 Group group = new Group("Group1");
 User user = new User("User1", "user1@example.com", "1234567890", true, false, true);
 List<Group> groups = new ArrayList<>();
 List<User> users = new ArrayList<>();
 users.add(user);
 group.setUsers(users);
 adminApp.setGroups(groups);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("New User Email");
 updateUserPanel.jTextField3.setText("New User Direct Phone");
 updateUserPanel.jTextField4.setText("New User Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();

 User newUser = new User("New User Name", "New User Email", "New User Direct Phone", true, true, true);
 assertEquals(newUser, updateUserPanel.jComboBox2.getSelectedItem());
 }

 @Test(expected = NullPointerException.class)
 public void testJButton4ActionPerformedNullGroupNameSelected() {
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("New User Email");
 updateUserPanel.jTextField3.setText("New User Direct Phone");
 updateUserPanel.jTextField4.setText("New User Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 }

 @Test
 public void testJButton4ActionPerformedNonExistentUser() {
 Group group = new Group("Group1");
 User user = new User("User1", "user1@example.com", "1234567890", true, false, true);
 List<Group> groups = new ArrayList<>();
 List<User> users = new ArrayList<>();
 users.add(user);
 group.setUsers(users);
 adminApp.setGroups(groups);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(users.size());
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("New User Email");
 updateUserPanel.jTextField3.setText("New User Direct Phone");
 updateUserPanel.jTextField4.setText("New User Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();

 assertFalse(adminApp.getGroups().get(0).getUsers().contains(new User("New User Name", "New User Email", null, true, true, true)));
 }

 @Test
 public void testJButton4ActionPerformedDuplicateEmail() {
 Group group = new Group("Group1");
 User user1 = new User("User1", "user1@example.com", "1234567890", true, false, true);
 User user2 = new User("User2", "user1@example.com", "1234567891", true, false, true);
 List<Group> groups = new ArrayList<>();
 List<User> users1 = new ArrayList<>();
 users1.add(user1);
 group.setUsers(users1);
 List<User> users2 = new ArrayList<>();
 users2.add(user2);
 groups.add(group);
 adminApp.setGroups(groups);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("user1@example.com");
 updateUserPanel.jTextField3.setText("New User Direct Phone");
 updateUserPanel.jTextField4.setText("New User Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();

 assertFalse(adminApp.getGroups().get(0).getUsers().contains(new User("New User Name", "user1@example.com", null, true, true, true)));
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 Group group1 = new Group("Group1");
 User user1 = new User("User1", "user1@example.com", "1234567890", true, false, true);
 User user2 = new User("User2", "user2@example.com", "1234567891", true, false, true);
 List<Group> groups = new ArrayList<>();
 List<User> users1 = new ArrayList<>();
 users1.add(user1);
 group1.setUsers(users1);
 List<User> users2 = new ArrayList<>();
 users2.add(user2);
 Group group2 = new Group("Group2");
 groups.add(group1);
 groups.add(group2);
 adminApp.setGroups(groups);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton1.doClick();
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(1);

 assertEquals("Group2", updateUserPanel.jComboBox1.getSelectedItem().toString());
 }
}