import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import static osa.ora.server.admin.ui.UpdateUserPanel.*;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private ControlPanel controlPanel;
 private UpdateUserPanel updateUserPanel;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 controlPanel = new ControlPanel();
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 }

 @After
 public void tearDown() {
 adminApp = null;
 controlPanel = null;
 updateUserPanel = null;
 }

 @Test
 public void testConstructor() {
 UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 assertNotNull(updateUserPanel);
 }

 @Test
 public void testUpdateModel() {
 Group group = new Group("Group1");
 adminApp.addGroup(group);
 User user = new User("User1", "user1@email.com");
 group.addUser(user);
 adminApp.updateModel();
 assertNotNull(updateUserPanel.jComboBox1);
 assertNotNull(updateUserPanel.jComboBox2);
 assertEquals("Group1", updateUserPanel.jComboBox1.getSelectedItem().toString());
 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 Group group = new Group("Group1");
 adminApp.addGroup(group);
 User user1 = new User("User1", "user1@email.com");
 User user2 = new User("User2", "user2@email.com");
 group.addUser(user1);
 group.addUser(user2);
 adminApp.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox1ItemStateChanged(null);
 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().toString());
 updateUserPanel.jComboBox1.setSelectedIndex(1);
 updateUserPanel.jComboBox1ItemStateChanged(null);
 assertEquals("User2", updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test
 public void testReset() {
 UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.reset();
 assertTrue(updateUserPanel.jTextField1.getText().isEmpty());
 assertTrue(updateUserPanel.jTextField2.getText().isEmpty());
 assertTrue(updateUserPanel.jTextField3.getText().isEmpty());
 assertTrue(updateUserPanel.jTextField4.getText().isEmpty());
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testFillUser() {
 User user = new User("User1", "user1@email.com");
 user.setName("Updated Name");
 user.setEmail("updatedemail@email.com");
 user.setDirectPhone("1234567890");
 user.setJobTitle("Software Engineer");
 user.setShowMyStatus(true);
 user.setOnlyStartChat(false);
 user.setCanJoinChatRoom(true);
 updateUserPanel.fillUser(user);
 assertEquals("Updated Name", updateUserPanel.jTextField1.getText());
 assertEquals("updatedemail@email.com", updateUserPanel.jTextField2.getText());
 assertEquals("1234567890", updateUserPanel.jTextField3.getText());
 assertEquals("Software Engineer", updateUserPanel.jTextField4.getText());
 assertTrue(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertTrue(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test(expected = NullPointerException.class)
 public void testJButton2ActionPerformedNullUser() {
 updateUserPanel.jButton2ActionPerformed(null);
 }

 @Test
 public void testJButton2ActionPerformed() {
 User user = new User("User1", "user1@email.com");
 Group group = new Group("Group1");
 adminApp.addGroup(group);
 group.addUser(user);
 adminApp.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton2ActionPerformed(null);
 assertFalse(adminApp.getGroups().get(0).getUsers().contains(user));
 }

 @Test
 public void testJButton3ActionPerformedNullUser() {
 updateUserPanel.jButton3ActionPerformed(null);
 }

 @Test
 public void testJButton3ActionPerformed() {
 User user = new User("User1", "user1@email.com");
 Group group = new Group("Group1");
 adminApp.addGroup(group);
 group.addUser(user);
 adminApp.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 user.setName("Updated Name");
 user.setEmail("updatedemail@email.com");
 user.setDirectPhone("1234567890");
 user.setJobTitle("Software Engineer");
 updateUserPanel.jTextField1.setText("Updated Name");
 updateUserPanel.jTextField2.setText("updatedemail@email.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("Software Engineer");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3ActionPerformed(null);
 assertEquals("Updated Name", user.getName());
 assertEquals("updatedemail@email.com", user.getEmail());
 assertEquals("1234567890", user.getDirectPhone());
 assertEquals("Software Engineer", user.getJobTitle());
 assertTrue(user.isShowMyStatus());
 assertFalse(user.isOnlyStartChat());
 assertTrue(user.isCanJoinChatRoom());
 }

 @Test
 public void testJButton4ActionPerformedNullUser() {
 updateUserPanel.jButton4ActionPerformed(null);
 }

 @Test
 public void testJButton4ActionPerformed() {
 User user = new User("User1", "user1@email.com");
 Group group = new Group("Group1");
 adminApp.addGroup(group);
 group.addUser(user);
 adminApp.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 User newUser = new User("NewUserName", "newuseremail@email.com");
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("newuseremail@email.com");
 updateUserPanel.jTextField3.setText("9876543210");
 updateUserPanel.jTextField4.setText("Software Developer");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(null);
 assertEquals("NewUserName", newUser.getName());
 assertEquals("newuseremail@email.com", newUser.getEmail());
 assertEquals("9876543210", newUser.getDirectPhone());
 assertEquals("Software Developer", newUser.getJobTitle());
 assertTrue(newUser.isShowMyStatus());
 assertFalse(newUser.isOnlyStartChat());
 assertTrue(newUser.isCanJoinChatRoom());
 assertEquals("NewUserName", group.getUsers().get(0).getName());
 assertEquals("newuseremail@email.com", group.getUsers().get(0).getEmail());
 assertEquals("9876543210", group.getUsers().get(0).getDirectPhone());
 assertEquals("Software Developer", group.getUsers().get(0).getJobTitle());
 }

 @Test
 public void testJComboBox2ItemStateChangedNullUser() {
 updateUserPanel.jComboBox2ItemStateChanged(null);
 }

 @Test
 public void testJComboBox2ItemStateChanged() {
 User user1 = new User("User1", "user1@email.com");
 User user2 = new User("User2", "user2@email.com");
 Group group = new Group("Group1");
 adminApp.addGroup(group);
 group.addUser(user1);
 group.addUser(user2);
 adminApp.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jButton1ActionPerformed(null);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jComboBox2ItemStateChanged(null);
 assertEquals("User1", updateUserPanel.jTextField1.getText());
 assertEquals("user1@email.com", updateUserPanel.jTextField2.getText());
 assertEquals("", updateUserPanel.jTextField3.getText());
 assertEquals("", updateUserPanel.jTextField4.getText());
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 updateUserPanel.jComboBox2.setSelectedIndex(1);
 updateUserPanel.jComboBox2ItemStateChanged(null);
 assertEquals("User2", updateUserPanel.jTextField1.getText());
 assertEquals("user2@email.com", updateUserPanel.jTextField2.getText());
 assertEquals("", updateUserPanel.jTextField3.getText());
 assertEquals("", updateUserPanel.jTextField4.getText());
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

}