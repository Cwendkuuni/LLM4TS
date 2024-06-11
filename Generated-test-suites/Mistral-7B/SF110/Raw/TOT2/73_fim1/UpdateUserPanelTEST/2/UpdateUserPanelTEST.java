import org.junit.Before;
import org.junit.Test;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import static org.junit.Assert.*;

public class UpdateUserPanelTest {

 private UpdateUserPanel updateUserPanel;
 private AdminApp adminApp;
 private Group group;
 private User user, user1;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 group = new Group("Group1");
 user = new User("User1", "user1@example.com", "1234567890", "JobTitle1", true, true, true);
 user1 = new User("User2", "user2@example.com", "9876543210", "JobTitle2", false, false, false);
 group.addUser(user);
 group.addUser(user1);
 adminApp.setGroups(new ArrayList<Group>() {{
 add(group);
 }});
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 @Test
 public void testUpdateModel() {
 updateUserPanel.updateModel();
 assertEquals(2, updateUserPanel.jComboBox1.getItemCount());
 assertEquals(user.getName(), updateUserPanel.jComboBox2.getItemAt(0).toString());
 }

 @Test
 public void testJTextField2Validation() {
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jButton4ActionPerformed(null);
 assertFalse(updateUserPanel.jButton4.isEnabled());
 }

 @Test
 public void testJTextField2ValidationWithValidEmail() {
 updateUserPanel.jTextField2.setText("test@example.com");
 updateUserPanel.jButton4ActionPerformed(null);
 assertTrue(updateUserPanel.jButton4.isEnabled());
 }

 @Test(expected = AssertionError.class)
 public void testJTextField2ValidationWithInvalidEmail() {
 updateUserPanel.jTextField2.setText("test.com");
 updateUserPanel.jButton4ActionPerformed(null);
 fail("JButton4 should be disabled due to invalid email format.");
 }

 @Test
 public void testJTextField3Validation() {
 updateUserPanel.jTextField3.setText("");
 updateUserPanel.jButton4ActionPerformed(null);
 assertTrue(updateUserPanel.jButton4.isEnabled());
 }

 @Test
 public void testJComboBox1Change() {
 updateUserPanel.jComboBox1.setSelectedIndex(1);
 assertEquals(user1.getName(), updateUserPanel.jTextField1.getText());
 }

 @Test
 public void testJComboBox2Change() {
 updateUserPanel.jComboBox2.setSelectedIndex(1);
 assertEquals(user1.getEmail(), updateUserPanel.jTextField2.getText());
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1ActionPerformed(null);
 assertTrue(updateUserPanel.isVisible() == false);
 }

 @Test
 public void testJButton2ActionPerformed() {
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jButton2ActionPerformed(null);
 assertEquals(group.getUsers().size(), 1);
 }

 @Test
 public void testJButton3ActionPerformed() {
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("NewUsername");
 updateUserPanel.jButton3ActionPerformed(null);
 assertEquals(user.getName(), "NewUsername");
 }

 @Test
 public void testJButton4ActionPerformed() {
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("NewUsername");
 updateUserPanel.jTextField2.setText("NewEmail@example.com");
 updateUserPanel.jTextField3.setText("NewPhone");
 updateUserPanel.jTextField4.setText("NewJobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(null);
 assertEquals(user.getName(), "NewUsername");
 assertEquals(user.getEmail(), "NewEmail@example.com");
 assertEquals(user.getDirectPhone(), "NewPhone");
 assertEquals(user.getJobTitle(), "NewJobTitle");
 assertEquals(user.isShowMyStatus(), true);
 assertEquals(user.isOnlyStartChat(), false);
 assertEquals(user.isCanJoinChatRoom(), true);
 }
}