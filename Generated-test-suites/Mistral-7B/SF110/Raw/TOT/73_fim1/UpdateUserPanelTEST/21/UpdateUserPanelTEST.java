package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class UpdateUserPanelTest {

 private AdminApp mockAdminApp;
 private ControlPanel mockControlPanel;
 private UpdateUserPanel updateUserPanel;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 mockAdminApp = mock(AdminApp.class);
 mockControlPanel = mock(ControlPanel.class);
 updateUserPanel = new UpdateUserPanel(mockAdminApp, mockControlPanel);
 }

 @Test
 public void testUpdateModel() {
 Group group = new Group("Group1", Arrays.asList(new User("User1", "user1@example.com", "1234567890", "John Doe", true, false, false), new User("User2", "user2@example.com", null, "Jane Doe", true, false, false), new User("User3", "user3@example.com", null, "Mark Smith", true, false, false)));
 when(mockAdminApp.getGroups()).thenReturn(Arrays.asList(group));
 updateUserPanel.updateModel();

 assertEquals(3, updateUserPanel.jComboBox1.getItemCount());
 assertEquals("Group1", updateUserPanel.jComboBox1.getItemAt(0).toString());

 assertFalse(updateUserPanel.jComboBox2.getItemCount() > 0);

 group.getUsers().clear();
 group.getUsers().add(new User("User1", "user1@example.com", "1234567890", "John Doe", true, false, false));
 updateUserPanel.updateModel();

 assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
 assertEquals("User1", updateUserPanel.jComboBox2.getItemAt(0).toString());
 }

 @Test
 public void testInitComponents() {
 // Check if components are added to the panel
 verify(updateUserPanel, times(1)).add(any(Component.class));
 }

 @Test
 public void testjButton1ActionPerformed() {
 updateUserPanel.jButton1ActionPerformed(mock(ActionEvent.class));

 verify(mockControlPanel).getParentDialog().setVisible(false);
 verifyNoMoreInteractions(updateUserPanel);
 }

 @Test
 public void testjButton2ActionPerformed_ValidUserDeletion() {
 Group group = new Group("Group1", Arrays.asList(new User("User1", "user1@example.com", "1234567890", "John Doe", true, false, false), new User("User2", "user2@example.com", null, "Jane Doe", true, false, false), new User("User3", "user3@example.com", null, "Mark Smith", true, false, false)));
 when(mockAdminApp.getGroups()).thenReturn(Arrays.asList(group));
 when(mockAdminApp.delUser(any(User.class))).thenReturn(new User("User1Deleted", "user1@example.com", null, "John Doe", false, false, false));

 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, ItemEvent.SELECTED, 0));
 updateUserPanel.jComboBox2ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox2, ItemEvent.SELECTED, 0));
 updateUserPanel.jButton2ActionPerformed(mock(ActionEvent.class));

 assertEquals(2, group.getUsers().size());
 assertFalse(updateUserPanel.jComboBox2.isVisible());
 assertTrue(updateUserPanel.jTextField1.isEditable());

 verify(mockControlPanel).getParentDialog().setVisible(false);
 verify(mockAdminApp).delUser(updateUserPanel.jComboBox2.getSelectedItem());
 verify(updateUserPanel.jComboBox2).setModel(new DefaultComboBoxModel<User>(group.getUsers()));
 }

 @Test
 public void testjButton2ActionPerformed_InvalidUserDeletion() {
 Group group = new Group("Group1", Arrays.asList(new User("User1", "user1@example.com", "1234567890", "John Doe", true, false, false), new User("User2", "user2@example.com", null, "Jane Doe", true, false, false), new User("User3", "user3@example.com", null, "Mark Smith", true, false, false)));
 when(mockAdminApp.getGroups()).thenReturn(Arrays.asList(group));
 when(mockAdminApp.delUser(any(User.class))).thenThrow(new Exception("Error deleting user"));

 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, ItemEvent.SELECTED, 0));
 updateUserPanel.jComboBox2ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox2, ItemEvent.SELECTED, 0));
 updateUserPanel.jButton2ActionPerformed(mock(ActionEvent.class));

 assertEquals(3, group.getUsers().size());
 assertFalse(updateUserPanel.jTextField1.isEditable());
 assertTrue(updateUserPanel.jComboBox2.isVisible());

 verifyNoMoreInteractions(mockAdminApp);
 }

 @Test
 public void testjButton3ActionPerformed() {
 User user = new User("User1", "user1@example.com", "1234567890", "John Doe", true, false, false);
 when(mockAdminApp.getGroups()).thenReturn(Arrays.asList(new Group("Group1", Arrays.asList(user))));

 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, ItemEvent.SELECTED, 0));
 updateUserPanel.jComboBox2ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox2, ItemEvent.SELECTED, 0));
 updateUserPanel.fillUser(user);

 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton3ActionPerformed(mock(ActionEvent.class));

 assertEquals("New User Name", user.getName());
 assertEquals("new.user@example.com", user.getEmail());
 assertEquals("1234567891", user.getDirectPhone());
 assertFalse(user.isShowMyStatus());
 assertTrue(user.isOnlyStartChat());
 assertFalse(user.isCanJoinChatRoom());
 }

 @Test
 public void testjButton3ActionPerformed_InvalidPasswordReset() {
 User user = new User("User1", "user1@example.com", "1234567890", "John Doe", true, false, false);
 when(mockAdminApp.getGroups()).thenReturn(Arrays.asList(new Group("Group1", Arrays.asList(user))));

 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, ItemEvent.SELECTED, 0));
 updateUserPanel.jComboBox2ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox2, ItemEvent.SELECTED, 0));
 updateUserPanel.fillUser(user);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton3ActionPerformed(mock(ActionEvent.class));

 assertEquals("New User Name", user.getName());
 assertEquals("1234567891", user.getDirectPhone());
 assertFalse(user.isShowMyStatus());
 assertTrue(user.isOnlyStartChat());
 assertFalse(user.isCanJoinChatRoom());
 assertFalse(user.getEmail().equals(""));
 }

 @Test
 public void testjButton4ActionPerformed() {
 Group group = new Group("Group1", Arrays.asList(new User("User1", "user1@example.com", "1234567890", "John Doe", true, false, false), new User("User2", "user2@example.com", null, "Jane Doe", true, false, false), new User("User3", "user3@example.com", null, "Mark Smith", true, false, false)));
 when(mockAdminApp.getGroups()).thenReturn(Arrays.asList(group));
 when(mockAdminApp.updateUser(any(User.class))).thenReturn(new User("User1Updated", "user1@example.com", "1234567891", "John Doe", true, false, false));

 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, ItemEvent.SELECTED, 0));
 updateUserPanel.jComboBox2ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox2, ItemEvent.SELECTED, 0));
 updateUserPanel.fillUser(group.getUsers().get(0));
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));

 assertEquals("New User Name", group.getUsers().get(0).getName());
 assertEquals("new.user@example.com", group.getUsers().get(0).getEmail());
 assertEquals("1234567891", group.getUsers().get(0).getDirectPhone());
 assertTrue(group.getUsers().get(0).isShowMyStatus());
 assertFalse(group.getUsers().get(0).isOnlyStartChat());
 assertTrue(group.getUsers().get(0).isCanJoinChatRoom());
 }

 @Test
 public void testjButton4ActionPerformed_InvalidUpdate() {
 Group group = new Group("Group1", Arrays.asList(new User("User1", "user1@example.com", "1234567890", "John Doe", true, false, false), new User("User2", "user2@example.com", null, "Jane Doe", true, false, false), new User("User3", "user3@example.com", null, "Mark Smith", true, false, false)));
 when(mockAdminApp.getGroups()).thenReturn(Arrays.asList(group));
 when(mockAdminApp.updateUser(any(User.class))).thenThrow(new Exception("Error updating user"));

 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, ItemEvent.SELECTED, 0));
 updateUserPanel.jComboBox2ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox2, ItemEvent.SELECTED, 0));
 updateUserPanel.fillUser(group.getUsers().get(0));
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));

 assertEquals("User1", group.getUsers().get(0).getName());
 assertEquals("user1@example.com", group.getUsers().get(0).getEmail());
 assertEquals("1234567890", group.getUsers().get(0).getDirectPhone());
 assertFalse(group.getUsers().get(0).isShowMyStatus());
 assertFalse(group.getUsers().get(0).isOnlyStartChat());
 assertFalse(group.getUsers().get(0).isCanJoinChatRoom());
 }
}