package osa.ora.server.admin.ui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

 private UpdateUserPanel updateUserPanel;
 private AdminApp adminApp;
 private ControlPanel controlPanel;

 @Before
 public void setup() {
 adminApp = new AdminApp();
 controlPanel = new ControlPanel();
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 }

 @Test
 public void testUpdateModel() {
 Group group = new Group("TestGroup");
 adminApp.setGroups(new Vector<Group>());
 adminApp.getGroups().add(group);
 User user = new User("TestUser", "test@test.com", "123456789", "TestTitle");
 group.setUsers(new Vector<User>());
 group.getUsers().add(user);
 updateUserPanel.updateModel();

 assertNotNull(updateUserPanel.jComboBox1);
 assertNotNull(updateUserPanel.jComboBox2);

 assertEquals(group, updateUserPanel.adminApp.getGroups().get(0));
 assertEquals(user, updateUserPanel.adminApp.getGroups().get(0).getUsers().get(0));
 }

 @Test
 public void testInitComponents() {
 UpdateUserPanel spyUpdateUserPanel = spy(new UpdateUserPanel(adminApp, controlPanel));
 verifyNoMoreInteractions(spyUpdateUserPanel);
 }

 @Test
 public void testJButton1ActionPerformed() {
 doCallRealMethod().when(updateUserPanel).jButton1ActionPerformed(any(ActionEvent.class));
 updateUserPanel.controlPanel.getParentDialog().setVisible(true);
 assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible());
 updateUserPanel.reset();
 verify(updateUserPanel).controlPanel.getParentDialog().setVisible(false);
 }

 @Test
 public void testJButton2ActionPerformed() {
 when(adminApp.getGroups()).thenReturn(new Vector<Group>());
 when(adminApp.getGroups().get(0).getUsers()).thenReturn(new Vector<User>());
 Group group = new Group("TestGroup");
 adminApp.setGroups(new Vector<Group>());
 adminApp.getGroups().add(group);
 User user = new User("TestUser", "test@test.com", "123456789", "TestTitle");
 group.setUsers(new Vector<User>());
 group.getUsers().add(user);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1 = mock(JComboBox.class);
 updateUserPanel.jComboBox2 = mock(JComboBox.class);
 doReturn(user).when(updateUserPanel.jComboBox2).getSelectedItem();
 doCallRealMethod().when(updateUserPanel).jButton2ActionPerformed(any(ActionEvent.class));

 assertEquals(0, group.getUsers().size());
 }

 @Test
 public void testJButton3ActionPerformed() {
 User user = new User("TestUser", "test@test.com", "123456789", "TestTitle");
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2 = mock(JComboBox.class);
 doReturn(user).when(updateUserPanel.jComboBox2).getSelectedItem();
 when(adminApp.resetUserPass(user)).thenReturn(new User("TestUserUpdated", "test@test.com", "123456789", "TestTitle"));
 doCallRealMethod().when(updateUserPanel).jButton3ActionPerformed(any(ActionEvent.class));

 assertEquals("TestUserUpdated", user.getName());
 }

 @Test
 public void testJButton4ActionPerformed() {
 Group group = new Group("TestGroup");
 adminApp.setGroups(new Vector<Group>());
 adminApp.getGroups().add(group);
 User user = new User("TestUser", "test@test.com", "123456789", "TestTitle");
 group.setUsers(new Vector<User>());
 group.getUsers().add(user);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1 = mock(JComboBox.class);
 updateUserPanel.jComboBox2 = mock(JComboBox.class);
 doReturn(user).when(updateUserPanel.jComboBox2).getSelectedItem();
 doCallRealMethod().when(updateUserPanel).jButton4ActionPerformed(any(ActionEvent.class));

 assertEquals("TestUser Updated", user.getName());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 Group group = new Group("TestGroup");
 adminApp.setGroups(new Vector<Group>());
 adminApp.getGroups().add(group);
 User user = new User("TestUser", "test@test.com", "123456789", "TestTitle");
 group.setUsers(new Vector<User>());
 group.getUsers().add(user);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1 = mock(JComboBox.class);
 updateUserPanel.jComboBox2 = mock(JComboBox.class);
 doReturn(group).when(updateUserPanel.adminApp).getGroups().get(0);
 doReturn(user).when(updateUserPanel.jComboBox2).getSelectedItem();
 doCallRealMethod().when(updateUserPanel).jComboBox1ItemStateChanged(any(ItemEvent.class));

 verify(updateUserPanel.jTextField1).setText(user.getName());
 verify(updateUserPanel.jTextField2).setText(user.getEmail());
 verify(updateUserPanel.jTextField3).setText(user.getDirectPhone());
 verify(updateUserPanel.jTextField4).setText(user.getJobTitle());
 verify(updateUserPanel.jCheckBox1).setSelected(user.isShowMyStatus());
 verify(updateUserPanel.jCheckBox2).setSelected(user.isOnlyStartChat());
 verify(updateUserPanel.jCheckBox3).setSelected(user.isCanJoinChatRoom());
 }

 @Test
 public void testJComboBox2ItemStateChanged() {
 User user = new User("TestUser", "test@test.com", "123456789", "TestTitle");
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1 = mock(JComboBox.class);
 updateUserPanel.jComboBox2 = mock(JComboBox.class);
 doCallRealMethod().when(updateUserPanel).jComboBox2ItemStateChanged(any(ItemEvent.class));

 verify(updateUserPanel.jTextField1).setText("");
 verify(updateUserPanel.jTextField2).setText("");
 verify(updateUserPanel.jTextField3).setText("");
 verify(updateUserPanel.jTextField4).setText("");
 verify(updateUserPanel.jCheckBox1).setSelected(false);
 verify(updateUserPanel.jCheckBox2).setSelected(false);
 verify(updateUserPanel.jCheckBox3).setSelected(false);
 }
}