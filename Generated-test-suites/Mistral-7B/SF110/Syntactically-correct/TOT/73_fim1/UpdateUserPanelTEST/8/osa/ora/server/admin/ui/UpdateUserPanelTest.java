import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.admin.ui.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private UpdateUserPanel updateUserPanel;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 @After
 public void tearDown() {
 updateUserPanel = null;
 adminApp = null;
 }

 @Test
 public void testUpdateUserPanelInitialization() {
 UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
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
 assertNotNull(updateUserPanel.jLabel5);
 assertNotNull(updateUserPanel.jTextField1);
 assertNotNull(updateUserPanel.jTextField4);
 assertNotNull(updateUserPanel.jLabel6);
 }

 @Test
 public void testUpdateUserPanelUpdateButtonActionPerformed() {
 User user = new User("TestUser", "testuser@example.com", "1234567890", "Test Title", true, true, true);
 Group group = new Group("TestGroup", new Vector<User>());
 adminApp.addGroup(group);
 adminApp.addUser(user, group);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("UpdatedUserName");
 updateUserPanel.jTextField2.setText("updateduser@example.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("Updated Title");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton4.doClick();
 assertEquals("UpdatedUserName", user.getName());
 assertEquals("updateduser@example.com", user.getEmail());
 assertEquals("1234567891", user.getDirectPhone());
 assertFalse(user.isShowMyStatus());
 assertTrue(user.isOnlyStartChat());
 assertFalse(user.isCanJoinChatRoom());
 }

 @Test
 public void testUpdateUserPanelUpdateButtonActionPerformedWithEmptyFields() {
 User user = new User("TestUser", "testuser@example.com", "1234567890", "Test Title", true, true, true);
 Group group = new Group("TestGroup", new Vector<User>());
 adminApp.addGroup(group);
 adminApp.addUser(user, group);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jTextField3.setText("");
 updateUserPanel.jTextField4.setText("");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton4.doClick();
 assertEquals("TestUser", user.getName());
 assertEquals("testuser@example.com", user.getEmail());
 assertEquals("1234567890", user.getDirectPhone());
 assertFalse(user.isShowMyStatus());
 assertFalse(user.isOnlyStartChat());
 assertFalse(user.isCanJoinChatRoom());
 }

 @Test
 public void testUpdateUserPanelUpdateButtonActionPerformedWithInvalidEmail() {
 User user = new User("TestUser", "testuser@example.com", "1234567890", "Test Title", true, true, true);
 Group group = new Group("TestGroup", new Vector<User>());
 adminApp.addGroup(group);
 adminApp.addUser(user, group);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("UpdatedUserName");
 updateUserPanel.jTextField2.setText("testuser@.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("Updated Title");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserUserPanel.jButton4.doClick();
 assertFalse(updateUserPanel.jButton4.isEnabled());
 JOptionPane.showMessageDialog(updateUserPanel, "Error Happen Invalid Email Format", "Error", 0, adminApp.getErrorIcon());
 }

 @Test
 public void testUpdateUserPanelDeleteButtonActionPerformed() {
 User user = new User("TestUser", "testuser@example.com", "1234567890", "Test Title", true, true, true);
 Group group = new Group("TestGroup", new Vector<User>());
 adminApp.addGroup(group);
 adminApp.addUser(user, group);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jButton2.doClick();
 assertFalse(group.getUsers().contains(user));
 JOptionPane.showMessageDialog(updateUserPanel, "User Deleted Successfully!", "Success", 0, adminApp.getInfoIcon());
 }

}