import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.admin.ui.UpdateUserPanel;
import osa.ora.server.admin.AdminApp;
import osa.ora.server.beans.Group;
import osa.ora.server.beans.User;

public class UpdateUserPanelTest {

 private UpdateUserPanel updateUserPanel;
 private AdminApp adminApp;
 private Group group;
 private User user, user1;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 group = new Group("Group1");
 user = new User("User1", "user1@mail.com", "1234567890", "User1 Title", true, true, true);
 user1 = new User("User2", "user2@mail.com", "0987654321", "User2 Title", false, false, false);
 group.addUser(user);
 group.addUser(user1);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 @Test
 public void testUpdateModel() {
 updateUserPanel.updateModel();
 assertEquals("Group1", updateUserPanel.jComboBox1.getSelectedItem().toString());
 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test
 public void testInitComponents() {
 assertEquals(5, updateUserPanel.getComponentCount());
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1ActionPerformed(null);
 assertTrue(updateUserPanel.isVisible() == false);
 }

 @Test
 public void testJButton2ActionPerformed() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton2ActionPerformed(null);
 assertEquals(1, group.getUsers().size());
 assertNull(adminApp.getGroups().get(0).getUsers().get(0));
 }

 @Test(expected = NullPointerException.class)
 public void testJButton2ActionPerformedNullUser() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jButton2ActionPerformed(null);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testJButton2ActionPerformedIndexOutOfBounds() {
 updateUserPanel.jComboBox1.setSelectedIndex(1);
 updateUserPanel.jComboBox2.setSelectedIndex(2);
 updateUserPanel.jButton2ActionPerformed(null);
 }

 @Test
 public void testJButton3ActionPerformed() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("NewUserEmail@mail.com");
 updateUserPanel.jTextField3.setText("NewUserPhoneNumber");
 updateUserPanel.jTextField4.setText("NewUserTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3ActionPerformed(null);
 assertEquals("NewUserName", user.getName());
 assertEquals("NewUserEmail@mail.com", user.getEmail());
 assertEquals("NewUserPhoneNumber", user.getDirectPhone());
 assertEquals("NewUserTitle", user.getJobTitle());
 assertTrue(user.isShowMyStatus());
 assertTrue(user.isOnlyStartChat());
 assertTrue(user.isCanJoinChatRoom());
 }

 @Test(expected = NullPointerException.class)
 public void testJButton3ActionPerformedNullUserTextField() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText(null);
 updateUserPanel.jTextField2.setText("NewUserEmail@mail.com");
 updateUserPanel.jTextField3.setText("NewUserPhoneNumber");
 updateUserPanel.jTextField4.setText("NewUserTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3ActionPerformed(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton3ActionPerformedEmptyName() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jTextField2.setText("NewUserEmail@mail.com");
 updateUserPanel.jTextField3.setText("NewUserPhoneNumber");
 updateUserPanel.jTextField4.setText("NewUserTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3ActionPerformed(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton3ActionPerformedEmptyEmail() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("");
 updatePanel.jTextField3.setText("NewUserPhoneNumber");
 updateUserPanel.jTextField4.setText("NewUserTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3ActionPerformed(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton3ActionPerformedExistingEmail() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("user1@mail.com");
 updateUserPanel.jTextField3.setText("NewUserPhoneNumber");
 updateUserPanel.jTextField4.setText("NewUserTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3ActionPerformed(null);
 }

 @Test
 public void testJButton4ActionPerformed() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("NewUserEmail@mail.com");
 updateUserPanel.jTextField3.setText("NewUserPhoneNumber");
 updateUserPanel.jTextField4.setText("NewUserTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(null);
 assertEquals("NewUserName", user.getName());
 assertEquals("NewUserEmail@mail.com", user.getEmail());
 assertEquals("NewUserPhoneNumber", user.getDirectPhone());
 assertEquals("NewUserTitle", user.getJobTitle());
 assertTrue(user.isShowMyStatus());
 assertTrue(user.isOnlyStartChat());
 assertTrue(user.isCanJoinChatRoom());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 updateUserPanel.jComboBox1.setSelectedIndex(1);
 assertEquals("Group2", updateUserPanel.jComboBox1.getSelectedItem().toString());
 }

 @Test
 public void testJComboBox2ItemStateChanged() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(1);
 assertEquals("User2", updateUserPanel.jComboBox2.getSelectedItem().toString());
 }
}