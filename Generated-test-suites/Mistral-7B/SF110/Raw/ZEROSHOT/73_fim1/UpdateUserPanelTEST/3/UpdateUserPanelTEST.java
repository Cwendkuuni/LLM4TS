package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private ControlPanel controlPanel;
 private UpdateUserPanel updateUserPanel;

 @Before
 public void setUp() {
 adminApp = Mockito.mock(AdminApp.class);
 controlPanel = Mockito.mock(ControlPanel.class);
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 }

 @Test
 public void testUpdateModelWithEmptyGroupsList() {
 adminApp.setGroups(new ArrayList<>());
 updateUserPanel.updateModel();
 assertEquals("Item 1", updateUserPanel.jComboBox2.getSelectedItem());
 }

 @Test
 public void testUpdateModelWithGroupsAndUsers() {
 Group group = Mockito.mock(Group.class);
 User user = Mockito.mock(User.class);
 adminApp.setGroups(new ArrayList<>());
 adminApp.getGroups().add(group);
 group.setUsers(new ArrayList<>());
 group.getUsers().add(user);
 updateUserPanel.updateModel();
 assertEquals("Item 1", updateUserPanel.jComboBox1.getSelectedItem());
 assertEquals("User 1", updateUserPanel.jComboBox2.getSelectedItem());
 }

 @Test
 public void testInitComponents() {
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 Group group = Mockito.mock(Group.class);
 User user = Mockito.mock(User.class);
 adminApp.setGroups(new ArrayList<>());
 adminApp.getGroups().add(group);
 group.setUsers(new ArrayList<>());
 group.getUsers().add(user);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox1ItemStateChanged(null);
 assertEquals("User 1", updateUserPanel.jComboBox2.getSelectedItem());
 }

 @Test
 public void testJButton1ActionPerformed() {
 ControlDialog dialog = Mockito.mock(ControlDialog.class);
 updateUserPanel.controlPanel = dialog;
 updateUserPanel.jButton1ActionPerformed(Mockito.mock(ActionEvent.class));
 verify(dialog).setVisible(false);
 }

 @Test
 public void testJButton2ActionPerformedWithValidUser() throws Exception {
 Group group = Mockito.mock(Group.class);
 User user = Mockito.mock(User.class);
 adminApp.setGroups(new ArrayList<>());
 adminApp.getGroups().add(group);
 group.setUsers(new ArrayList<>());
 group.getUsers().add(user);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton2ActionPerformed(Mockito.mock(ActionEvent.class));
 assertEquals(0, group.getUsers().size());
 }

 @Test(expected = Exception.class)
 public void testJButton2ActionPerformedWithNullUser() throws Exception {
 Group group = Mockito.mock(Group.class);
 User user = null;
 adminApp.setGroups(new ArrayList<>());
 adminApp.getGroups().add(group);
 group.setUsers(new ArrayList<>());
 group.getUsers().add(user);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton2ActionPerformed(Mockito.mock(ActionEvent.class));
 fail("Exception should be thrown");
 }

 @Test
 public void testJButton3ActionPerformed() throws Exception {
 Group group = Mockito.mock(Group.class);
 User user = Mockito.mock(User.class);
 adminApp.setGroups(new ArrayList<>());
 adminApp.getGroups().add(group);
 group.setUsers(new ArrayList<>());
 group.getUsers().add(user);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("new.user@email.com");
 updateUserPanel.jButton3ActionPerformed(Mockito.mock(ActionEvent.class));
 assertEquals("New User Name", user.getName());
 assertEquals("new.user@email.com", user.getEmail());
 }

 @Test
 public void testJButton4ActionPerformedWithValidUserData() throws Exception {
 Group group = Mockito.mock(Group.class);
 User user = Mockito.mock(User.class);
 adminApp.setGroups(new ArrayList<>());
 adminApp.getGroups().add(group);
 group.setUsers(new ArrayList<>());
 group.getUsers().add(user);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("new.user@email.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(Mockito.mock(ActionEvent.class));
 assertEquals("New User Name", user.getName());
 assertEquals("new.user@email.com", user.getEmail());
 assertEquals("1234567890", user.getDirectPhone());
 assertEquals("New Job Title", user.getJobTitle());
 assertTrue(user.isShowMyStatus());
 assertTrue(user.isOnlyStartChat());
 assertTrue(user.isCanJoinChatRoom());
 }

 @Test(expected = Exception.class)
 public void testJButton4ActionPerformedWithEmptyUserName() throws Exception {
 Group group = Mockito.mock(Group.class);
 User user = Mockito.mock(User.class);
 adminApp.setGroups(new ArrayList<>());
 adminApp.getGroups().add(group);
 group.setUsers(new ArrayList<>());
 group.getUsers().add(user);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jTextField2.setText("new.user@email.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(Mockito.mock(ActionEvent.class));
 fail("Exception should be thrown");
 }

 @Test(expected = Exception.class)
 public void testJButton4ActionPerformedWithEmptyUserEmail() throws Exception {
 Group group = Mockito.mock(Group.class);
 User user = Mockito.mock(User.class);
 adminApp.setGroups(new ArrayList<>());
 adminApp.getGroups().add(group);
 group.setUsers(new ArrayList<>());
 group.getUsers().add(user);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(Mockito.mock(ActionEvent.class));
 fail("Exception should be thrown");
 }

 @Test(expected = Exception.class)
 public void testJButton4ActionPerformedWithUserEmailAlreadyExists() throws Exception {
 Group group = Mockito.mock(Group.class);
 User user = Mockito.mock(User.class);
 AdminApp adminApp2 = Mockito.mock(AdminApp.class);
 ControlPanel controlPanel2 = Mockito.mock(ControlPanel.class);
 User anotherUser = Mockito.mock(User.class);
 adminApp.setGroups(new ArrayList<>());
 adminApp.getGroups().add(group);
 group.setUsers(new ArrayList<>());
 group.getUsers().add(user);
 group.getUsers().add(anotherUser);
 updateUserPanel.adminApp = adminApp2;
 updateUserPanel.controlPanel = controlPanel2;
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(1);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("another.user@email.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(Mockito.mock(ActionEvent.class));
 fail("Exception should be thrown");
 }
}