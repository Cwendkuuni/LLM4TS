import org.junit.Before;
import org.junit.Test;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UpdateUserPanelTest {

 private UpdateUserPanel updateUserPanel;
 private AdminApp adminApp;
 private Group group;
 private User user;
 private User user2;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 group = new Group("Group1");
 user = new User("User1", "user1@example.com", "Phone1", "Title1", true, true, true);
 user2 = new User("User2", "user2@example.com", "Phone2", "Title2", true, true, true);
 group.addUser(user);
 group.addUser(user2);
 adminApp.addGroup(group);
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 @Test
 public void testUpdateModel() {
 updateUserPanel.updateModel();
 assertEquals("Group1", updateUserPanel.jComboBox1.getSelectedItem().toString());
 }

 @Test
 public void testInitComponents() {
 updateUserPanel.initComponents();
 assertEquals("Username:", updateUserPanel.jLabel1.getText());
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1ActionPerformed(null);
 assertEquals("", updateUserPanel.jTextField1.getText());
 assertEquals("", updateUserPanel.jTextField2.getText());
 assertEquals("", updateUserPanel.jTextField3.getText());
 assertEquals("", updateUserPanel.jTextField4.getText());
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testJButton2ActionPerformedWithValidUser() {
 updateUserPanel.jComboBox1.setSelectedItem("Group1");
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jButton2ActionPerformed(null);
 assertNull(adminApp.getGroups().get(0).getUsers().get(0));
 assertNull(adminApp.getGroups().get(0).getUsers().get(1));
 }

 @Test
 public void testJButton2ActionPerformedWithInvalidUser() {
 updateUserPanel.jComboBox1.setSelectedItem("Group1");
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jButton2ActionPerformed(null);
 assertEquals("User2", updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test
 public void testJButton3ActionPerformedWithValidUser() {
 updateUserPanel.jComboBox1.setSelectedItem("Group1");
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("NewName");
 updateUserPanel.jTextField2.setText("NewEmail");
 updateUserPanel.jTextField3.setText("NewPhone");
 updateUserPanel.jTextField4.setText("NewTitle");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton3ActionPerformed(null);
 assertEquals("NewName", updateUserPanel.jComboBox2.getSelectedItem().getName());
 assertEquals("NewEmail", updateUserPanel.jComboBox2.getSelectedItem().getEmail());
 assertEquals("NewPhone", updateUserPanel.jComboBox2.getSelectedItem().getDirectPhone());
 assertEquals("NewTitle", updateUserPanel.jComboBox2.getSelectedItem().getJobTitle());
 assertFalse(updateUserPanel.jComboBox2.getSelectedItem().isShowMyStatus());
 assertFalse(updateUserPanel.jComboBox2.getSelectedItem().isOnlyStartChat());
 assertFalse(updateUserPanel.jComboBox2.getSelectedItem().isCanJoinChatRoom());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton3ActionPerformedWithNullUser() {
 updateUserPanel.jComboBox1.setSelectedItem("Group1");
 updateUserPanel.jButton3ActionPerformed(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton3ActionPerformedWithEmptyName() {
 updateUserPanel.jComboBox1.setSelectedItem("Group1");
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jTextField2.setText("user2@example.com");
 updateUserPanel.jTextField3.setText("Phone2");
 updateUserPanel.jTextField4.setText("Title2");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3ActionPerformed(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton3ActionPerformedWithEmptyEmail() {
 updateUserPanel.jComboBox1.setSelectedItem("Group1");
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("NewName");
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jTextField3.setText("Phone2");
 updateUserPanel.jTextField4.setText("Title2");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3ActionPerformed(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton3ActionPerformedWithEmailAlreadyExists() {
 updateUserPanel.jComboBox1.setSelectedItem("Group1");
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("NewName");
 updateUserPanel.jTextField2.setText("user2@example.com");
 updateUserPanel.jTextField3.setText("Phone2");
 updateUserPanel.jTextField4.setText("Title2");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3ActionPerformed(null);
 }

 @Test
 public void testJButton4ActionPerformed() {
 updateUserPanel.jComboBox1.setSelectedItem("Group1");
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("NewName");
 updateUserPanel.jTextField2.setText("NewEmail");
 updateUserPanel.jTextField3.setText("NewPhone");
 updateUserPanel.jTextField4.setText("NewTitle");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton4ActionPerformed(null);
 assertEquals("NewName", updateUserPanel.jComboBox2.getSelectedItem().getName());
 assertEquals("NewEmail", updateUserPanel.jComboBox2.getSelectedItem().getEmail());
 assertEquals("NewPhone", updateUserPanel.jComboBox2.getSelectedItem().getDirectPhone());
 assertEquals("NewTitle", updateUserPanel.jComboBox2.getSelectedItem().getJobTitle());
 assertFalse(updateUserPanel.jComboBox2.getSelectedItem().isShowMyStatus());
 assertFalse(updateUserPanel.jComboBox2.getSelectedItem().isOnlyStartChat());
 assertFalse(updateUserPanel.jComboBox2.getSelectedItem().isCanJoinChatRoom());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 updateUserPanel.jComboBox1.setSelectedItem("Group2");
 assertEquals("Group2", updateUserPanel.jComboBox1.getSelectedItem().toString());
 }
}