package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import osa.ora.server.admin.AdminApp;
import osa.ora.server.admin.Group;
import osa.ora.server.beans.User;
import osa.ora.server.ui.UpdateUserPanel;

public class UpdateUserPanelTest {

 private AdminApp mockAdminApp;
 private UpdateUserPanel updateUserPanel;

 @Before
 public void setup() {
 mockAdminApp = mock(AdminApp.class);
 updateUserPanel = new UpdateUserPanel(mockAdminApp, null);
 }

 @Test
 public void testUpdateModel() {
 // Given
 Group mockGroup = mock(Group.class);
 when(mockAdminApp.getGroups()).thenReturn(Arrays.asList(mockGroup));
 when(mockGroup.getUsers()).thenReturn(new ArrayList<User>());

 // When
 updateUserPanel.updateModel();

 // Then
 assertEquals("Size of JComboBox1 should be 1", 1, updateUserPanel.jComboBox1.getItemCount());
 assertNull("JComboBox2 should be empty", updateUserPanel.jComboBox2.getSelectedItem());
 }

 @Test
 public void testUpdateModelWithUsers() {
 // Given
 Group mockGroup = mock(Group.class);
 List<User> mockUsers = new ArrayList<>();
 mockUsers.add(new User("user1", "user1@example.com", "1234567890", "title1", true, false, false));
 mockUsers.add(new User("user2", "user2@example.com", null, "title2", false, false, false));
 when(mockAdminApp.getGroups()).thenReturn(Arrays.asList(mockGroup));
 when(mockGroup.getUsers()).thenReturn(mockUsers);

 // When
 updateUserPanel.updateModel();

 // Then
 assertEquals("Size of JComboBox1 should be 1", 1, updateUserPanel.jComboBox1.getItemCount());
 assertEquals("JComboBox2 should contain user1", "user1", updateUserPanel.jComboBox2.getSelectedItem().getName());
 }

 @Test
 public void testJButton1ActionPerformed() {
 // Given
 updateUserPanel = spy(new UpdateUserPanel(mockAdminApp, null));
 doNothing().when(updateUserPanel).reset();

 // When
 updateUserPanel.jButton1.doClick();

 // Then
 verify(updateUserPanel).reset();
 }

 @Test(expected = IllegalStateException.class)
 public void testJButton2ActionPerformedWithNullUser() {
 // Given
 updateUserPanel = new UpdateUserPanel(mockAdminApp, null);
 updateUserPanel.jComboBox2.setSelectedItem(null);

 // When
 updateUserPanel.jButton2.doClick();

 // Then
 failsFast(); // This test should fail fast as the method jButton2ActionPerformed is not implemented and should throw an exception when the selected item is null
 }

 @Test
 public void testJButton2ActionPerformedWithValidUser() {
 // Given
 User mockUser = mock(User.class);
 when(mockUser.getId()).thenReturn(1);
 updateUserPanel = spy(new UpdateUserPanel(mockAdminApp, null));
 doNothing().when(mockAdminApp).delUser(mockUser);
 updateUserPanel.jComboBox1.setSelectedItem(new Group("group1"));
 updateUserPanel.jComboBox2.setSelectedItem(mockUser);

 // When
 updateUserPanel.jButton2.doClick();

 // Then
 verify(mockAdminApp).delUser(mockUser);
 }

 @Test
 public void testJButton3ActionPerformedWithNullUser() {
 // Given
 updateUserPanel = new UpdateUserPanel(mockAdminApp, null);
 updateUserPanel.jComboBox2.setSelectedItem(null);

 // When
 updateUserPanel.jButton3.doClick();

 // Then
 // The method should not be called as the selected item is null
 verify(mockAdminApp, never()).resetUserPass(null);
 }

 @Test
 public void testJButton3ActionPerformedWithValidUser() {
 // Given
 User mockUser = mock(User.class);
 when(mockUser.getId()).thenReturn(1);
 updateUserPanel = spy(new UpdateUserPanel(mockAdminApp, null));
 doNothing().when(mockAdminApp).resetUserPass(mockUser);
 updateUserPanel.jComboBox1.setSelectedItem(new Group("group1"));
 updateUserPanel.jComboBox2.setSelectedItem(mockUser);
 updateUserPanel.jTextField1.setText("newName");
 updateUserPanel.jTextField2.setText("newEmail@example.com");
 updateUserPanel.jTextField3.setText("newPhone");
 updateUserPanel.jTextField4.setText("newTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(false);

 // When
 updateUserPanel.jButton3.doClick();

 // Then
 verify(mockAdminApp).resetUserPass(mockUser);
 verify(mockUser).setName("newName");
 verify(mockUser).setEmail("newEmail@example.com");
 verify(mockUser).setDirectPhone("newPhone");
 verify(mockUser).setJobTitle("newTitle");
 verify(mockUser).setShowMyStatus(true);
 verify(mockUser).setOnlyStartChat(false);
 verify(mockUser).setCanJoinChatRoom(false);
 }

 @Test
 public void testJButton4ActionPerformedWithNullUser() {
 // Given
 updateUserPanel = new UpdateUserPanel(mockAdminApp, null);
 updateUserPanel.jComboBox2.setSelectedItem(null);

 // When
 updateUserPanel.jButton4.doClick();

 // Then
 // The method should not be called as the selected item is null
 verify(mockAdminApp, never()).updateUser(null);
 }

 @Test
 public void testJButton4ActionPerformedWithValidUser() {
 // Given
 User mockUser = mock(User.class);
 when(mockUser.getId()).thenReturn(1);
 updateUserPanel = spy(new UpdateUserPanel(mockAdminApp, null));
 doNothing().when(mockAdminApp).updateUser(mockUser);
 updateUserPanel.jComboBox1.setSelectedItem(new Group("group1"));
 updateUserPanel.jComboBox2.setSelectedItem(mockUser);
 updateUserPanel.jTextField1.setText("newName");
 updateUserPanel.jTextField2.setText("newEmail@example.com");
 updateUserPanel.jTextField3.setText("newPhone");
 updateUserPanel.jTextField4.setText("newTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(false);

 // When
 updateUserPanel.jButton4.doClick();

 // Then
 verify(mockAdminApp).updateUser(mockUser);
 verify(mockUser).setName("newName");
 verify(mockUser).setEmail("newEmail@example.com");
 verify(mockUser).setDirectPhone("newPhone");
 verify(mockUser).setJobTitle("newTitle");
 verify(mockUser).setShowMyStatus(true);
 verify(mockUser).setOnlyStartChat(false);
 verify(mockUser).setCanJoinChatRoom(false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformedWithInvalidEmail() {
 // Given
 User mockUser = mock(User.class);
 when(mockUser.getId()).thenReturn(1);
 updateUserPanel = new UpdateUserPanel(mockAdminApp, null);
 updateUserPanel.jComboBox1.setSelectedItem(new Group("group1"));
 updateUserPanel.jComboBox2.setSelectedItem(mockUser);
 updateUserPanel.jTextField1.setText("newName");
 updateUserPanel.jTextField2.setText("newemail@example.com"); // invalid email
 updateUserPanel.jTextField3.setText("newPhone");
 updateUserPanel.jTextField4.setText("newTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(false);

 // When
 updateUserPanel.jButton4.doClick();

 // Then
 failsFast(); // This test should fail fast as the method jButton4ActionPerformed is not implemented and should throw an IllegalArgumentException for an invalid email
 }
}