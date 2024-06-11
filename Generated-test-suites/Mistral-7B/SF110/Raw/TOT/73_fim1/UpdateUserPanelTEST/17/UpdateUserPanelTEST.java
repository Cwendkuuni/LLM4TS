package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import osa.ora.server.admin.AdminApp;
import osa.ora.server.admin.Group;
import osa.ora.server.beans.User;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private UpdateUserPanel updateUserPanel;
 private Group group;
 private User user;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 adminApp = mock(AdminApp.class);
 group = new Group();
 user = new User();
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 @Test
 public void testUpdateModel() {
 List<Group> groups = Arrays.asList(group);
 when(adminApp.getGroups()).thenReturn(groups);
 updateUserPanel.updateModel();
 assertEquals(group, updateUserPanel.getAdminApp());
 assertNotNull(updateUserPanel.jComboBox1);
 assertNotNull(updateUserPanel.jComboBox2);
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 User testUser = new User();
 group.getUsers().add(testUser);
 when(adminApp.getGroups()).thenReturn(Arrays.asList(group));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox1ItemStateChanged(mock(ItemEvent.class));
 assertEquals(testUser, updateUserPanel.getSelectedUser());
 }

 @Test
 public void testReset() {
 updateUserPanel.reset();
 assertNull(updateUserPanel.jTextField1.getText());
 assertNull(updateUserPanel.jTextField2.getText());
 assertNull(updateUserPanel.jTextField3.getText());
 assertNull(updateUserPanel.jTextField4.getText());
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testFillUser() {
 user.setName("Test Name");
 user.setEmail("testEmail@test.com");
 user.setDirectPhone("1234567890");
 user.setJobTitle("Test Job Title");
 user.setShowMyStatus(true);
 user.setOnlyStartChat(false);
 user.setCanJoinChatRoom(true);
 updateUserPanel.fillUser(user);
 assertEquals("Test Name", updateUserPanel.jTextField1.getText());
 assertEquals("testEmail@test.com", updateUserPanel.jTextField2.getText());
 assertEquals("1234567890", updateUserPanel.jTextField3.getText());
 assertEquals("Test Job Title", updateUserPanel.jTextField4.getText());
 assertTrue(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertTrue(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test(expected = NullPointerException.class)
 public void testJComboBox2ItemStateChangedWithNullUser() {
 updateUserPanel.jComboBox2ItemStateChanged(mock(ItemEvent.class));
 }

 @Test
 public void testJButton3ActionPerformed() {
 when(adminApp.resetUserPass(user)).thenReturn(user);
 updateUserPanel.jButton3ActionPerformed(mock(ActionEvent.class));
 assertEquals("User Password Reset Successfully!", updateUserPanel.getLastMessage());
 }

 @Test
 public void testJButton4ActionPerformedWithValidInput() {
 when(adminApp.getGroups()).thenReturn(Arrays.asList(group));
 when(adminApp.updateUser(user)).thenReturn(user);
 updateUserPanel.jTextField1.setText("Test Name");
 updateUserPanel.jTextField2.setText("testEmail@test.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("Test Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));
 assertEquals("Test Name", user.getName);
 assertEquals("testEmail@test.com", user.getEmail());
 assertEquals("1234567890", user.getDirectPhone());
 assertEquals("Test Job Title", user.getJobTitle());
 assertTrue(user.isShowMyStatus());
 assertFalse(user.isOnlyStartChat());
 assertTrue(user.isCanJoinChatRoom());
 }

 @Test(expected = NullPointerException.class)
 public void testJButton4ActionPerformedWithNullUser() {
 updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformedWithEmptyName() {
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformedWithEmptyEmail() {
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformedWithDuplicateEmail() {
 List<User> users = new ArrayList<User>();
 users.add(user);
 group.setUsers(users);
 updateUserPanel.updateModel();
 updateUserPanel.jTextField2.setText("testEmail@test.com");
 updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));
 }
}