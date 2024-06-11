import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

 private UpdateUserPanel updateUserPanel;
 private AdminApp adminApp;
 private ControlPanel controlPanel;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 controlPanel = new ControlPanel();
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 }

 @After
 public void tearDown() {
 updateUserPanel = null;
 adminApp = null;
 controlPanel = null;
 }

 @Test
 public void testUpdateModel() {
 Group group = new Group("Group1");
 adminApp.setGroups(new Vector<Group>());
 adminApp.getGroups().add(group);
 Vector<User> users = new Vector<User>();
 User user = new User("User1", "user1@example.com", "1234567890", "Job Title", true, true, true);
 users.add(user);
 group.setUsers(users);
 updateUserPanel.updateModel();
 assertNotNull(updateUserPanel.jComboBox1);
 assertNotNull(updateUserPanel.jComboBox2);
 assertEquals(group.getName(), updateUserPanel.jComboBox1.getSelectedItem().toString());
 assertEquals(user.getName(), updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 Group group = new Group("Group1");
 adminApp.setGroups(new Vector<Group>());
 adminApp.getGroups().add(group);
 Vector<User> users = new Vector<User>();
 User user1 = new User("User1", "user1@example.com", "1234567890", "Job Title", true, true, true);
 User user2 = new User("User2", "user2@example.com", "2345678901", "Job Title", true, true, true);
 users.add(user1);
 users.add(user2);
 group.setUsers(users);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox1ItemStateChanged(null);
 assertNotNull(updateUserPanel.jComboBox2);
 assertEquals(user1.getName(), updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1ActionPerformed(new ActionEvent(new JButton(), 0, "Cancel"));
 assertTrue(controlPanel.isVisible());
 }

 @Test
 public void testJButton2ActionPerformed() {
 Group group = new Group("Group1");
 adminApp.setGroups(new Vector<Group>());
 adminApp.getGroups().add(group);
 Vector<User> users = new Vector<User>();
 User user = new User("User1", "user1@example.com", "1234567890", "Job Title", true, true, true);
 User userToDelete = new User("User2", "user2@example.com", "2345678901", "Job Title", true, true, true);
 users.add(user);
 users.add(userToDelete);
 group.setUsers(users);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(1);
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(new JButton(), 0, "Delete"));
 assertFalse(users.contains(userToDelete));
 }

 @Test
 public void testJButton3ActionPerformed() {
 Group group = new Group("Group1");
 adminApp.setGroups(new Vector<Group>());
 adminApp.getGroups().add(group);
 Vector<User> users = new Vector<User>();
 User user = new User("User1", "user1@example.com", "1234567890", "Job Title", true, true, true);
 users.add(user);
 group.setUsers(users);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("New Phone Number");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(new JButton(), 0, "Reset Password"));
 User newUser = new User("New User Name", "new.user@example.com", "N/A", "New Job Title", false, true, false);
 User originalUser = new User("User1", "user1@example.com", "1234567890", "Job Title", true, true, true);
 assertEquals(newUser.getName(), users.get(0).getName());
 assertNotEquals(originalUser.getEmail(), users.get(0).getEmail());
 assertNotEquals(originalUser.getDirectPhone(), users.get(0).getDirectPhone());
 assertNotEquals(originalUser.getJobTitle(), users.get(0).getJobTitle());
 assertFalse(users.get(0).isShowMyStatus());
 assertTrue(users.get(0).isOnlyStartChat());
 assertFalse(users.get(0).isCanJoinChatRoom());
 }

 @Test
 public void testJButton4ActionPerformed() {
 Group group = new Group("Group1");
 adminApp.setGroups(new Vector<Group>());
 adminApp.getGroups().add(group);
 Vector<User> users = new Vector<User>();
 User user = new User("User1", "user1@example.com", "1234567890", "Job Title", true, true, true);
 users.add(user);
 group.setUsers(users);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("New Phone Number");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new JButton(), 0, "Update"));
 User newUser = new User("New User Name", "new.user@example.com", "N/A", "New Job Title", false, true, false);
 User originalUser = new User("User1", "user1@example.com", "1234567890", "Job Title", true, true, true);
 assertEquals(newUser.getName(), users.get(0).getName());
 assertEquals(newUser.getEmail(), users.get(0).getEmail());
 assertEquals(newUser.getDirectPhone(), users.get(0).getDirectPhone());
 assertEquals(newUser.getJobTitle(), users.get(0).getJobTitle());
 assertFalse(users.get(0).isShowMyStatus());
 assertTrue(users.get(0).isOnlyStartChat());
 assertFalse(users.get(0).isCanJoinChatRoom());
 }
}