package osa.ora.server.admin.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import osa.ora.server.admin.AdminApp;
import osa.ora.server.admin.beans.Group;
import osa.ora.server.admin.beans.User;

public class UpdateUserPanelTest {

 private UpdateUserPanel updateUserPanel;
 private AdminApp adminApp;
 private ControlPanel controlPanel;

 @Before
 public void setup() {
 adminApp = mock(AdminApp.class);
 controlPanel = mock(ControlPanel.class);
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 }

 @Test
 public void testUpdateModelWithValidData() {
 // Given
 Group group = new Group("Group1");
 User user = new User("User1", "user1@example.com", "1234567890", "Title1");
 List<Group> groups = Arrays.asList(group);
 List<User> users = Arrays.asList(user);
 when(adminApp.getGroups()).thenReturn(groups);
 when(groups.get(0).getUsers()).thenReturn(users);

 // When
 updateUserPanel.updateModel();

 // Then
 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().getName());
 }

 @Test
 public void testUpdateModelWithEmptyData() {
 // Given
 Group group = new Group("Group1");
 when(adminApp.getGroups()).thenReturn(Arrays.asList(group));
 when(group.getUsers()).thenReturn(new ArrayList<>());

 // When
 updateUserPanel.updateModel();

 // Then
 assertNull(updateUserPanel.jComboBox2.getSelectedItem());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 // Given
 User user = new User("User1", "user1@example.com", "1234567890", "Title1");
 Group group = new Group("Group1", Arrays.asList(user));
 when(adminApp.getGroups()).thenReturn(Arrays.asList(group));
 when(group.getUsers()).thenReturn(Arrays.asList(user));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.itemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, 0, true));

 // Then
 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().getName());
 }

 @Test
 public void testJButton1ActionPerformed() {
 // Given
 updateUserPanel.controlPanel = mock(ControlPanel.class);
 when(controlPanel.getParentDialog()).thenReturn(mock(JDialog.class));

 // When
 updateUserPanel.jButton1.doClick();

 // Then
 verify(controlPanel.getParentDialog()).setVisible(false);
 updateUserPanel.reset();
 }

 @Test
 public void testJButton2ActionPerformedWithNullSelectedUser() {
 // Given
 updateUserPanel.jComboBox2.setSelectedIndex(-1);

 // When
 updateUserPanel.jButton2.doClick();

 // Then
 verify(adminApp, never()).delUser(any(User.class));
 }

 @Test
 public void testJButton2ActionPerformedWithValidSelectedUser() {
 // Given
 User user = new User("User1", "user1@example.com", "1234567890", "Title1");
 Group group = new Group("Group1", Arrays.asList(user));
 when(adminApp.getGroups()).thenReturn(Arrays.asList(group));
 when(group.getUsers()).thenReturn(Arrays.asList(user));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.itemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, 0, true));
 updateUserPanel.jComboBox2.setSelectedIndex(0);

 // When
 updateUserPanel.jButton2.doClick();

 // Then
 verify(adminApp).delUser(user);
 }

 @Test
 public void testJButton3ActionPerformed() {
 // Given
 User user = new User("User1", "user1@example.com", "1234567890", "Title1");
 when(adminApp.resetUserPass(user)).thenReturn(user);
 updateUserPanel.jComboBox2.setSelectedItem(user);

 // When
 updateUserPanel.jButton3.doClick();

 // Then
 verify(adminApp).resetUserPass(user);
 }

 @Test
 public void testJButton4ActionPerformedWithInvalidData() {
 // Given
 User user = new User("User1", "user1@example.com", "1234567890", "Title1");
 String newName = "";
 String newEmail = "";
 when(adminApp.updateUser(user)).thenReturn(null);
 updateUserPanel.jTextField1.setText(newName);
 updateUserPanel.jTextField2.setText(newEmail);

 // When
 updateUserPanel.jButton4.doClick();

 // Then
 verify(adminApp, never()).updateUser(user);
 JOptionPane.showMessageDialog(updateUserPanel, "Please Enter User Name", "Error", JOptionPane.ERROR_MESSAGE);
 }

 @Test
 public void testJButton4ActionPerformedWithValidData() {
 // Given
 User user = new User("User1", "user1@example.com", "1234567890", "Title1");
 String newName = "NewUserName";
 String newEmail = "newuser1@example.com";
 when(adminApp.updateUser(user)).thenReturn(new User("NewUserName", newEmail, user.getDirectPhone(), user.getJobTitle()));
 updateUserPanel.jTextField1.setText(newName);
 updateUserPanel.jTextField2.setText(newEmail);

 // When
 updateUserPanel.jButton4.doClick();

 // Then
 verify(adminApp).updateUser(user);
 JOptionPane.showMessageDialog(updateUserPanel, "User Updated Successfully!");
 }

 @Test
 public void testJComboBox2ItemStateChangedWithNullSelectedUser() {
 // Given
 updateUserPanel.jComboBox2.setSelectedItem(null);

 // When
 updateUserPanel.jComboBox2ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox2, 0, true));

 // Then
 updateUserPanel.reset();
 }

 @Test
 public void testJComboBox2ItemStateChangedWithValidSelectedUser() {
 // Given
 User user = new User("User1", "user1@example.com", "1234567890", "Title1");
 updateUserPanel.jComboBox2.setSelectedItem(user);

 // When
 updateUserPanel.jComboBox2ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox2, 0, true));

 // Then
 assertEquals("User1", updateUserPanel.jTextField1.getText());
 assertEquals("user1@example.com", updateUserPanel.jTextField2.getText());
 assertEquals("1234567890", updateUserPanel.jTextField3.getText());
 assertEquals("Title1", updateUserPanel.jTextField4.getText());
 updateUserPanel.jCheckBox1.setSelected(user.isShowMyStatus());
 updateUserPanel.jCheckBox2.setSelected(user.isOnlyStartChat());
 updateUserPanel.jCheckBox3.setSelected(user.isCanJoinChatRoom());
 }
}