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
 public void setUp() {
 adminApp = new AdminApp();
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 Group group = new Group("Group1");
 adminApp.addGroup(group);
 User user = new User("User1", "user1@example.com", "1234567890", "User Title", true, true, true);
 group.addUser(user);
 updateUserPanel.updateModel();
 }

 @Test
 public void testUpdateModel() {
 Group expectedGroup = adminApp.getGroups().get(0);
 User expectedUser = expectedGroup.getUsers().get(0);
 assertEquals(expectedGroup.getName(), updateUserPanel.jComboBox1.getSelectedItem().toString());
 assertEquals(expectedUser.getName(), updateUserPanel.jTextField1.getText());
 assertEquals(expectedUser.getEmail(), updateUserPanel.jTextField2.getText());
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1.doClick();
 assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible());
 }

 @Test
 public void testJButton2ActionPerformedWithNullUser() {
 updateUserPanel.jButton2.doClick();
 assertFalse(updateUserPanel.controlPanel.getParentDialog().isVisible());
 }

 @Test
 public void testJButton2ActionPerformedWithValidUser() {
 User user = (User)updateUserPanel.jComboBox2.getSelectedItem();
 updateUserPanel.jButton2.doClick();
 assertFalse(updateUserPanel.controlPanel.getParentDialog().isVisible());
 Group group = adminApp.getGroups().get(0);
 assertEquals(group.getUsers().size() - 1, group.getUsers().size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJButton2ActionPerformedWithInvalidIndex() {
 updateUserPanel.jButton2.doClick();
 User user = (User)updateUserPanel.jComboBox2.getSelectedItem();
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jButton2.doClick();
 }

 @Test
 public void testJButton3ActionPerformed() {
 User user = (User)updateUserPanel.jComboBox2.getSelectedItem();
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jButton3.doClick();
 assertNotEquals(user.getName(), updateUserPanel.jTextField1.getText());
 assertNotEquals(user.getEmail(), updateUserPanel.jTextField2.getText());
 }

 @Test(expected = NullPointerException.class)
 public void testJButton3ActionPerformedWithNullUser() {
 updateUserPanel.jButton3.doClick();
 }

 @Test
 public void testJButton4ActionPerformedWithEmptyFields() {
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jButton4.doClick();
 assertFalse(updateUserPanel.controlPanel.getParentDialog().isVisible());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformedWithEmptyName() {
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jTextField2.setText("user@example.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("User Title");
 updateUserPanel.jButton4.doClick();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformedWithEmptyEmail() {
 updateUserPanel.jTextField1.setText("UserName");
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("User Title");
 updateUserPanel.jButton4.doClick();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformedWithExistingEmail() {
 Group group = new Group("Group1");
 adminApp.addGroup(group);
 User user1 = new User("User1", "user1@example.com", "1234567890", "User Title", true, true, true);
 group.addUser(user1);
 updateUserPanel.updateModel();
 User user2 = new User("User2", "user1@example.com", "1234567890", "User Title", true, true, true);
 updateUserPanel.jTextField1.setText("User2");
 updateUserPanel.jTextField2.setText("user1@example.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("User Title2");
 updateUserPanel.jButton4.doClick();
 }

 @Test
 public void testJButton4ActionPerformedWithValidInputs() {
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("User Title3");
 updateUserPanel.jButton4.doClick();
 Group group = adminApp.getGroups().get(0);
 User user = group.getUsers().get(group.getUsers().size() - 1);
 assertEquals("NewUserName", user.getName());
 assertEquals("new.user@example.com", user.getEmail());
 assertEquals("1234567891", user.getDirectPhone());
 assertEquals("User Title3", user.getJobTitle());
 }

 @After
 public void tearDown() {
 adminApp.getGroups().clear();
 updateUserPanel = null;
 adminApp = null;
 }
}