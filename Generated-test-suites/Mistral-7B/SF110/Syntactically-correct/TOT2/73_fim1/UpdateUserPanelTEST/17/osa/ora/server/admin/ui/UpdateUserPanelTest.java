import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.admin.ui.UpdateUserPanel;
import osa.ora.server.admin.AdminApp;
import osa.ora.server.beans.Group;
import osa.ora.server.beans.User;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private UpdateUserPanel updateUserPanel;
 private Group group;
 private User user, user1;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 group = new Group("Group1");
 user = new User("User1", "user1@example.com", "1234567890", "User1 Title", true, true, true);
 user1 = new User("User2", "user2@example.com", null, "User2 Title", false, false, false);
 group.getUsers().add(user);
 group.getUsers().add(user1);
 adminApp.setGroups(Arrays.asList(group));
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 @Test
 public void testUpdateModel() {
 updateUserPanel.updateModel();
 assertEquals(2, updateUserPanel.jComboBox1.getItemCount());
 assertEquals(2, updateUserPanel.jComboBox2.getItemCount());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox1ItemStateChanged(null);
 assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().getName());
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1ActionPerformed(null);
 assertTrue(updateUserPanel.isVisible() == false);
 }

 @Test
 public void testJButton2ActionPerformed_ValidUser() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton2ActionPerformed(null);
 assertEquals(1, group.getUsers().size());
 }

 @Test
 public void testJButton2ActionPerformed_InvalidUser() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jButton2ActionPerformed(null);
 assertEquals(1, group.getUsers().size());
 }

 @Test(expected = NullPointerException.class)
 public void testJButton2ActionPerformed_NullUser() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jComboBox2.setModel(null);
 updateUserPanel.jButton2ActionPerformed(null);
 }

 @Test
 public void testJButton3ActionPerformed_ValidUser() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("User1NewName");
 updateUserPanel.jTextField2.setText("user1NewEmail@example.com");
 updateUserPanel.jTextField3.setText("1234567890NewPhone");
 updateUserPanel.jTextField4.setText("User1 New Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3ActionPerformed(null);
 assertEquals("User1NewName", user.getName());
 assertEquals("user1NewEmail@example.com", user.getEmail());
 assertEquals("1234567890NewPhone", user.getDirectPhone());
 assertEquals("User1 New Title", user.getJobTitle());
 assertEquals(true, user.isShowMyStatus());
 assertEquals(true, user.isOnlyStartChat());
 assertEquals(true, user.isCanJoinChatRoom());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton3ActionPerformed_EmptyName() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jTextField2.setText("user1@example.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("User1 Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3ActionPerformed(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton3ActionPerformed_EmptyEmail() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("User1NewName");
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("User1 New Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3ActionPerformed(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton3ActionPerformed_SameEmail() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(1);
 updateUserPanel.jTextField1.setText("User1NewName");
 updateUserPanel.jTextField2.setText("user1@example.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("User1 New Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3ActionPerformed(null);
 }

 @Test
 public void testJButton4ActionPerformed() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(1);
 updateUserPanel.jTextField1.setText("User1NewName");
 updateUserPanel.jTextField2.setText("user1NewEmail@example.com");
 updateUserPanel.jTextField3.setText("1234567890NewPhone");
 updateUserPanel.jTextField4.setText("User1 New Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(null);
 assertEquals("User1NewName", user1.getName());
 assertEquals("user1NewEmail@example.com", user1.getEmail());
 assertEquals("1234567890NewPhone", user1.getDirectPhone());
 assertEquals("User1 New Title", user1.getJobTitle());
 assertEquals(true, user1.isShowMyStatus());
 assertEquals(true, user1.isOnlyStartChat());
 assertEquals(true, user1.isCanJoinChatRoom());
 }

 @After
 public void tearDown() {
 updateUserPanel = null;
 adminApp = null;
 }
}