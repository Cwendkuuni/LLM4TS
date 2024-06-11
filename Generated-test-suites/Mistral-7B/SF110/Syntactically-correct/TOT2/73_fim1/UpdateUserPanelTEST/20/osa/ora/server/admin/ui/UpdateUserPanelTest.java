import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.admin.ui.UpdateUserPanel;
import osa.ora.server.admin.AdminApp;
import osa.ora.server.beans.Group;
import osa.ora.server.beans.User;
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
 Group group = new Group();
 Vector<User> users = new Vector<>();
 User user = new User("user1", "user1@email.com", "1234567890", "Job Title");
 users.add(user);
 group.setUsers(users);
 adminApp.setGroups(Arrays.asList(group));
 updateUserPanel.updateModel();
 assertNotNull(updateUserPanel.jComboBox1);
 assertNotNull(updateUserPanel.jComboBox2);
 }

 @Test
 public void testInitComponents() {
 assertNotNull(updateUserPanel.jLabel1);
 assertNotNull(updateUserPanel.jLabel2);
 assertNotNull(updateUserPanel.jLabel3);
 assertNotNull(updateUserPanel.jCheckBox1);
 assertNotNull(updateUserPanel.jCheckBox2);
 assertNotNull(updateUserPanel.jCheckBox3);
 assertNotNull(updateUserPanel.jLabel4);
 assertNotNull(updateUserPanel.jComboBox1);
 assertNotNull(updateUserPanel.jTextField2);
 assertNotNull(updateUserPanel.jTextField3);
 assertNotNull(updateUserPanel.jButton1);
 assertNotNull(updateUserPanel.jButton2);
 assertNotNull(updateUserPanel.jComboBox2);
 assertNotNull(updateUserPanel.jButton3);
 assertNotNull(updateUserPanel.jButton4);
 assertNotNull(updateUserPanel.jLabel5);
 assertNotNull(updateUserPanel.jTextField1);
 assertNotNull(updateUserPanel.jTextField4);
 assertNotNull(updateUserPanel.jLabel6);
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 Group group = new Group();
 Vector<User> users = new Vector<>();
 User user1 = new User("user1", "user1@email.com", "1234567890", "Job Title");
 User user2 = new User("user2", "user2@email.com", "2345678901", "Job Title");
 user1.setId(1);
 user2.setId(2);
 users.add(user1);
 users.add(user2);
 group.setUsers(users);
 adminApp.setGroups(Arrays.asList(group));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox1ItemStateChanged(null);
 assertNotNull(updateUserPanel.jComboBox2);
 assertFalse(updateUserPanel.jComboBox2.getItemCount() == 0);
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1ActionPerformed(new ActionEvent(new Object(), new int[0], 0));
 assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible() == false);
 }

 @Test(expected = NullPointerException.class)
 public void testJButton2ActionPerformed_NullUserSelected() {
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), new int[0], 0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJButton2ActionPerformed_NoUserSelected() {
 updateUserPanel.jComboBox2.setModel(new DefaultComboBoxModel<User>(Arrays.asList(new User(), new User())));
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), new int[0], 0));
 }

 @Test
 public void testJButton3ActionPerformed() {
 User user = new User("user", "user@email.com", "1234567890", "Job Title");
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(new Object(), new int[0], 0));
 assertEquals("Password reset successfully.", updateUserPanel.adminApp.getMessage());
 }

 @Test(expected = NullPointerException.class)
 public void testJButton3ActionPerformed_NullUserSelected() {
 updateUserPanel.jComboBox2.setSelectedItem(null);
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(new Object(), new int[0], 0));
 }

 @Test
 public void testJButton4ActionPerformed() {
 Group group = new Group();
 Vector<User> users = new Vector<>();
 User user = new User("user", "user@email.com", "1234567890", "Job Title");
 users.add(user);
 group.setUsers(users);
 adminApp.setGroups(Arrays.asList(group));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("newName");
 updateUserPanel.jTextField2.setText("newEmail@email.com");
 updateUserPanel.jTextField3.setText("newPhone");
 updateUserPanel.jTextField4.setText("newJobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), new int[0], 0));
 assertNotSame(user.getName(), "newName");
 assertNotSame(user.getEmail(), "newEmail@email.com");
 assertNotSame(user.getDirectPhone(), "newPhone");
 assertNotSame(user.getJobTitle(), "newJobTitle");
 assertTrue(user.isShowMyStatus() == updateUserPanel.jCheckBox1.isSelected());
 assertTrue(user.isOnlyStartChat() == updateUserPanel.jCheckBox2.isSelected());
 assertTrue(user.isCanJoinChatRoom() == updateUserPanel.jCheckBox3.isSelected());
 group.getUsers().set(group.getUsers().indexOf(user), user);
 }

 @Test(expected = NullPointerException.class)
 public void testJButton4ActionPerformed_NullGroupSelected() {
 updateUserPanel.jComboBox1.setSelectedItem(null);
 updateUserPanel.jComboBox2.setSelectedItem(new User());
 updateUserPanel.jTextField1.setText("newName");
 updateUserPanel.jTextField2.setText("newEmail@email.com");
 updateUserPanel.jTextField3.setText("newPhone");
 updateUserPanel.jTextField4.setText("newJobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), new int[0], 0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJButton4ActionPerformed_NoUserSelected() {
 updateUserPanel.jComboBox1.setSelectedItem(new Group());
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), new int[0], 0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformed_InvalidEmail() {
 updateUserPanel.jComboBox1.setSelectedItem(new Group());
 updateUserPanel.jComboBox2.setSelectedItem(new User());
 updateUserPanel.jTextField1.setText("newName");
 updateUserPanel.jTextField2.setText("invalidEmail");
 updateUserPanel.jTextField3.setText("newPhone");
 updateUserPanel.jTextField4.setText("newJobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), new int[0], 0));
 }
}