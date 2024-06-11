package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private ControlPanel controlPanel;
 private UpdateUserPanel updateUserPanel;

 @Before
 public void setUp() {
 adminApp = mock(AdminApp.class);
 controlPanel = mock(ControlPanel.class);
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 }

 @Test
 public void testUpdateModel_whenGroupsAndUsersAreNotNull_thenUserComboBoxIsPopulatedWithUsersFromFirstGroup() {
 List<Group> groups = new ArrayList<>();
 Group firstGroup = new Group("FirstGroup");
 firstGroup.setUsers(new ArrayList<>(Arrays.asList(new User("User1"), new User("User2"))));
 groups.add(firstGroup);
 when(adminApp.getGroups()).thenReturn(groups);

 updateUserPanel.updateModel();

 assertEquals("User1", updateUserPanel.jComboBox2.getModel().getSelectedItem());
 }

 @Test
 public void testUpdateModel_whenGroupsIsEmpty_thenUserComboBoxIsPopulatedWithGroups() {
 when(adminApp.getGroups()).thenReturn(new ArrayList<>());

 updateUserPanel.updateModel();

 assertEquals("Item 1", updateUserPanel.jComboBox1.getSelectedItem());
 }

 @Test
 public void testUpdateModel_whenUserIsSelected_thenUserFieldsArePopulatedWithUserData() {
 User user = new User("User1", "user1@example.com", "1234567890", "Job1", true, true, true);
 when(adminApp.getGroups().get(0).getUsers()).thenReturn(Arrays.asList(user));
 when(adminApp.getGroups().get(0).getUsers().get(0)).thenReturn(user);

 updateUserPanel.updateModel();

 assertEquals("User1", updateUserPanel.jTextField1.getText());
 assertEquals("user1@example.com", updateUserPanel.jTextField2.getText());
 assertEquals("1234567890", updateUserPanel.jTextField3.getText());
 assertEquals("Job1", updateUserPanel.jTextField4.getText());
 assertTrue(updateUserPanel.jCheckBox1.isSelected());
 assertTrue(updateUserPanel.jCheckBox2.isSelected());
 assertTrue(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testJComboBox1ItemStateChanged_whenNewGroupIsSelected_thenUserComboBoxIsPopulatedWithUsersFromSelectedGroup() {
 List<Group> groups = new ArrayList<>();
 Group firstGroup = new Group("FirstGroup");
 firstGroup.setUsers(new ArrayList<>(Arrays.asList(new User("User1"), new User("User2"))));
 groups.add(firstGroup);
 when(adminApp.getGroups()).thenReturn(groups);

 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedItem("SecondGroup");
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, ItemEvent.ItemStateChanged, 0, ItemEvent.DESELECTED));

 List<User> users = new ArrayList<>(Arrays.asList(new User("User3"), new User("User4")));
 when(adminApp.getGroups().get(1).getUsers()).thenReturn(users);

 updateUserPanel.updateModel();

 assertEquals("User3", updateUserPanel.jComboBox2.getModel().getSelectedItem());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton2ActionPerformed_whenSelectedUserIsNull_thenExceptionIsThrown() {
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(updateUserPanel.jButton2, ActionEvent.ACTION_PERFORMED, ""));
 }

 @Test
 public void testJButton2ActionPerformed_whenSelectedUserIsDeleted_thenUserIsRemovedFromGroup() {
 User user = new User("User1", "user1@example.com", "1234567890", "Job1", true, true, true);
 Group group = new Group("FirstGroup");
 group.setUsers(new ArrayList<>(Arrays.asList(user, new User("User2"))));
 when(adminApp.getGroups()).thenReturn(Arrays.asList(group));

 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedItem("FirstGroup");
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(updateUserPanel.jButton2, ActionEvent.ACTION_PERFORMED, ""));

 assertEquals(1, group.getUsers().size());
 assertFalse(group.getUsers().contains(user));
 }

 @Test
 public void testJButton3ActionPerformed_whenSelectedUserIsNull_thenNoUserIsUpdated() {
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(updateUserPanel.jButton3, ActionEvent.ACTION_PERFORMED, ""));
 }

 @Test
 public void testJButton3ActionPerformed_whenUserIsUpdated_thenUserIsUpdatedInAdminApp() {
 User user = new User("User1", "user1@example.com", "1234567890", "Job1", true, true, true);
 Group group = new Group("FirstGroup");
 group.setUsers(new ArrayList<>(Arrays.asList(user)));
 when(adminApp.getGroups()).thenReturn(Arrays.asList(group));

 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedItem("FirstGroup");
 updateUserPanel.jComboBox2.setSelectedItem(user);

 String newName = "New User Name";
 String newEmail = "new.user@example.com";
 String newPhone = "9876543210";
 String newJobTitle = "New Job Title";

 updateUserPanel.jTextField1.setText(newName);
 updateUserPanel.jTextField2.setText(newEmail);
 updateUserPanel.jTextField3.setText(newPhone);
 updateUserPanel.jTextField4.setText(newJobTitle);
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(updateUserPanel.jButton3, ActionEvent.ACTION_PERFORMED, ""));

 assertEquals(newName, user.getName());
 assertEquals(newEmail, user.getEmail());
 assertEquals(newPhone, user.getDirectPhone());
 assertEquals(newJobTitle, user.getJobTitle());
 assertTrue(user.isShowMyStatus());
 assertFalse(user.isOnlyStartChat());
 assertTrue(user.isCanJoinChatRoom());
 }
}