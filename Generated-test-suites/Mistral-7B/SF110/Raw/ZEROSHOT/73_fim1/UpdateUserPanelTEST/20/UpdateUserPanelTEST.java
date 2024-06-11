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
 private Group group;
 private User user;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 controlPanel = new ControlPanel();
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 group = new Group("Group1", new Vector<User>());
 user = new User("User1", "user1@example.com", "1234567890", "User1 Title", true, false, false, "N/A");
 group.getUsers().add(user);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
 }

 @After
 public void tearDown() {
 adminApp = null;
 controlPanel = null;
 updateUserPanel = null;
 group = null;
 user = null;
 }

 @Test
 public void testUpdateModel() {
 updateUserPanel.updateModel();
 assertEquals("Group1", updateUserPanel.jComboBox1.getSelectedItem().toString());
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
 assertNotNull(updateUserPanel.jLabel5);
 assertNotNull(updateUserPanel.jTextField1);
 assertNotNull(updateUserPanel.jTextField4);
 assertNotNull(updateUserPanel.jLabel6);
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox1ItemStateChanged(null);
 assertEquals(user, updateUserPanel.jComboBox2.getSelectedItem());
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1ActionPerformed(null);
 assertTrue(controlPanel.getParentDialog().isVisible());
 }

 @Test
 public void testJButton2ActionPerformed() {
 updateUserPanel.jButton2.setEnabled(true);
 updateUserPanel.jButton2ActionPerformed(null);
 assertEquals(0, group.getUsers().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton2ActionPerformedWithNullUser() {
 updateUserPanel.jButton2.setEnabled(true);
 updateUserPanel.jComboBox2.setModel(new DefaultComboBoxModel<User>(new Vector<User>()));
 updateUserPanel.jButton2ActionPerformed(null);
 }

 @Test
 public void testJButton3ActionPerformed() {
 updateUserPanel.jButton3ActionPerformed(null);
 assertEquals("User1 Password Reset Successfully!", updateUserPanel.getText());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton3ActionPerformedWithNullUser() {
 updateUserPanel.jButton3ActionPerformed(null);
 }

 @Test
 public void testJButton4ActionPerformed() {
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("New User Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(null);
 assertEquals("NewUserName", user.getName);
 assertEquals("new.user@example.com", user.getEmail());
 assertEquals("1234567890", user.getDirectPhone());
 assertEquals("New User Title", user.getJobTitle());
 assertTrue(user.isShowMyStatus());
 assertTrue(user.isOnlyStartChat());
 assertTrue(user.isCanJoinChatRoom());
 }

}