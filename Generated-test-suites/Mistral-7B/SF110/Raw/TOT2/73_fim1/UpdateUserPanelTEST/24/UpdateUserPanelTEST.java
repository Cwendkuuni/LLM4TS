import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.admin.ui.*;
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

 @Test
 public void testUpdateModel() {
 Group group = new Group("Group1");
 User user = new User("User1", "user1@example.com", "123456789", "User1 Title", true, true, true);
 group.addUser(user);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));

 updateUserPanel.updateModel();

 assertEquals("Group1", updateUserPanel.jComboBox1.getSelectedItem().toString());
 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1ActionPerformed(null);
 assertTrue(updateUserPanel.isVisible() == false);
 }

 @Test
 public void testJButton2ActionPerformedNullUser() {
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jButton2ActionPerformed(null);
 assertEquals("Can't identify selected User!", updateUserPanel.getToolTipText());
 }

 @Test
 public void testJButton2ActionPerformedInvalidUser() {
 User user = new User("User1", "user1@example.com", "123456789", "User1 Title", true, true, true);
 updateUserPanel.jComboBox1.setSelectedItem(user);
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jButton2ActionPerformed(null);
 assertEquals("Can't identify selected User!", updateUserPanel.getToolTipText());
 }

 @Test
 public void testJButton2ActionPerformedNullGroup() {
 Group group = null;
 updateUserPanel.jComboBox1.setSelectedItem(group);
 User user = new User("User1", "user1@example.com", "123456789", "User1 Title", true, true, true);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jButton2ActionPerformed(null);
 assertEquals("Can't identify selected Group!", updateUserPanel.getToolTipText());
 }

 @Test
 public void testJButton2ActionPerformed() {
 Group group = new Group("Group1");
 User user = new User("User1", "user1@example.com", "123456789", "User1 Title", true, true, true);
 group.addUser(user);
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jButton2ActionPerformed(null);
 assertEquals(0, group.getUsers().size());
 }

 @Test
 public void testJButton3ActionPerformedNullUser() {
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jButton3ActionPerformed(null);
 assertEquals("Can't identify selected User!", updateUserPanel.getToolTipText());
 }

 @Test
 public void testJButton3ActionPerformedNullEmail() {
 User user = new User("User1", null, "123456789", "User1 Title", true, true, true);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jButton3ActionPerformed(null);
 assertEquals("Please Enter User email", updateUserPanel.getToolTipText());
 }

 @Test
 public void testJButton3ActionPerformedExistingEmail() {
 Group group = new Group("Group1");
 User user1 = new User("User1", "user1@example.com", "123456789", "User1 Title", true, true, true);
 User user2 = new User("User2", "user1@example.com", "123456789", "User2 Title", true, true, true);
 group.addUser(user1);
 group.addUser(user2);
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2.setSelectedItem(user1);
 updateUserPanel.jTextField2.setText(user2.getEmail());
 updateUserPanel.jButton3ActionPerformed(null);
 assertEquals("User with Same email Already Exist!", updateUserPanel.getToolTipText());
 }

 @Test
 public void testJButton3ActionPerformed() {
 Group group = new Group("Group1");
 User user = new User("User1", "user1@example.com", "123456789", "User1 Title", true, true, true);
 group.addUser(user);
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("NewPhoneNumber");
 updateUserPanel.jTextField4.setText("NewUserTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3ActionPerformed(null);
 User newUser = group.getUsers().get(0);
 assertEquals("NewUserName", newUser.getName());
 assertEquals("new.user@example.com", newUser.getEmail());
 assertEquals("NewPhoneNumber", newUser.getDirectPhone());
 assertEquals("NewUserTitle", newUser.getJobTitle());
 assertTrue(newUser.isShowMyStatus());
 assertFalse(newUser.isOnlyStartChat());
 assertTrue(newUser.isCanJoinChatRoom());
 }

 @After
 public void tearDown() {
 adminApp = null;
 controlPanel = null;
 updateUserPanel = null;
 }
}