import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import java.util.*;

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

 @After
 public void tearDown() {
 updateUserPanel = null;
 controlPanel = null;
 adminApp = null;
 }

 @Test
 public void testUpdateModel() {
 Group group = new Group("Test Group");
 User user = new User("Test User", "test@example.com", "1234567890", "Software Tester", true, true, true);
 group.addUser(user);
 adminApp.setGroups(Arrays.asList(group));
 updateUserPanel.updateModel();

 assertEquals(1, updateUserPanel.jComboBox1.getItemCount());
 assertEquals(user.getName(), updateUserPanel.jComboBox2.getItemAt(0).toString());
 }

 @Test
 public void testInitComponents() {
 updateUserPanel = spy(new UpdateUserPanel(adminApp, controlPanel));

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
 public void testJButton1ActionPerformed() {
 doCallRealMethod().when(updateUserPanel).jButton1ActionPerformed(any(ActionEvent.class));
 updateUserPanel.jButton1.doClick();

 verify(controlPanel.getParentDialog()).setVisible(false);
 assertTrue(updateUserPanel.reset());
 }

 @Test
 public void testJButton2ActionPerformed() {
 Group group = new Group("Test Group");
 User user = new User("Test User", "test@example.com", "1234567890", "Software Tester", true, true, true);
 group.addUser(user);
 adminApp.setGroups(Arrays.asList(group));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 doCallRealMethod().when(updateUserPanel).jButton2ActionPerformed(any(ActionEvent.class));

 assertEquals(0, group.getUsers().size());
 assertTrue(updateUserPanel.reset());
 }

 @Test
 public void testJButton3ActionPerformed() {
 Group group = new Group("Test Group");
 User user = new User("Test User", "test@example.com", "1234567890", "Software Tester", true, true, true);
 group.addUser(user);
 adminApp.setGroups(Arrays.asList(group));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("new.email@example.com");
 doCallRealMethod().when(updateUserPanel).jButton3ActionPerformed(any(ActionEvent.class));

 assertEquals("New User Name", user.getName());
 assertEquals("new.email@example.com", user.getEmail());
 }

 @Test
 public void testJButton4ActionPerformed() {
 Group group = new Group("Test Group");
 User user = new User("Test User", "test@example.com", "1234567890", "Software Tester", true, true, true);
 group.addUser(user);
 adminApp.setGroups(Arrays.asList(group));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("new.email@example.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("Software Engineer");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);
 doCallRealMethod().when(updateUserPanel).jButton4ActionPerformed(any(ActionEvent.class));

 assertEquals("New User Name", user.getName());
 assertEquals("new.email@example.com", user.getEmail());
 assertEquals("1234567891", user.getDirectPhone());
 assertEquals("Software Engineer", user.getJobTitle());
 assertFalse(user.isShowMyStatus());
 assertTrue(user.isOnlyStartChat());
 assertFalse(user.isCanJoinChatRoom());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 Group group1 = new Group("Group 1");
 User user1 = new User("User 1", "user1@example.com", "1234567890", "Admin", true, true, true);
 group1.addUser(user1);
 Group group2 = new Group("Group 2");
 User user2 = new User("User 2", "user2@example.com", "1234567891", "Manager", true, true, true);
 group2.addUser(user2);
 adminApp.setGroups(Arrays.asList(group1, group2));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 doCallRealMethod().when(updateUserPanel).jComboBox1ItemStateChanged(any(ItemEvent.class));

 assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
 assertEquals("User 1", updateUserPanel.jComboBox2.getItemAt(0).toString());
 }
}