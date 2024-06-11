package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private ControlPanel controlPanel;
 private UpdateUserPanel updateUserPanel;
 private Group group;
 private List<User> users;

 @Before
 public void setup() {
 adminApp = Mockito.mock(AdminApp.class);
 controlPanel = Mockito.mock(ControlPanel.class);
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 group = Mockito.mock(Group.class);
 users = new ArrayList<User>();
 users.add(new User("user1", "user1@email.com", "1234567890", "User1Title", true, true, true));
 group.setUsers(users);
 }

 @Test
 public void testInitComponents() {
 // Check if components are added correctly
 verify(updateUserPanel, times(1)).add(updateUserPanel.jLabel1);
 verify(updateUserPanel, times(1)).add(updateUserPanel.jLabel2);
 verify(updateUserPanel, times(1)).add(updateUserPanel.jLabel3);
 verify(updateUserPanel, times(1)).add(updateUserPanel.jCheckBox1);
 verify(updateUserPanel, times(1)).add(updateUserPanel.jCheckBox2);
 verify(updateUserPanel, times(1)).add(updateUserPanel.jCheckBox3);
 verify(updateUserPanel, times(1)).add(updateUserPanel.jLabel4);
 verify(updateUserPanel, times(1)).add(updateUserPanel.jComboBox1);
 verify(updateUserPanel, times(1)).add(updateUserPanel.jTextField2);
 verify(updateUserPanel, times(1)).add(updateUserPanel.jTextField3);
 verify(updateUserPanel, times(1)).add(updateUserPanel.jButton1);
 verify(updateUserPanel, times(1)).add(updateUserPanel.jButton2);
 verify(updateUserPanel, times(1)).add(updateUserPanel.jComboBox2);
 verify(updateUserPanel, times(1)).add(updateUserPanel.jButton3);
 verify(updateUserPanel, times(1)).add(updateUserPanel.jLabel5);
 verify(updateUserPanel, times(1)).add(updateUserPanel.jTextField1);
 verify(updateUserPanel, times(1)).add(updateUserPanel.jTextField4);
 verify(updateUserPanel, times(1)).add(updateUserPanel.jLabel6);

 // Check if labels have correct text
 assertEquals("Username:", updateUserPanel.jLabel1.getText());
 assertEquals("Login Email:", updateUserPanel.jLabel2.getText());
 assertEquals("Phone:", updateUserPanel.jLabel3.getText());

 // Check if checkboxes are initialized correctly
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testUpdateModelWithEmptyGroupList() {
 updateUserPanel.updateModel();
 // Check if comboBox2 is empty
 assertEquals(0, updateUserPanel.jComboBox2.getItemCount());
 }

 @Test
 public void testUpdateModelWithSingleUser() {
 // Set up group with single user
 users.clear();
 users.add(users.get(0));
 when(adminApp.getGroups()).thenReturn(Collections.singletonList(group));
 updateUserPanel.updateModel();

 // Check if comboBox1 has correct items
 assertEquals(4, updateUserPanel.jComboBox1.getItemCount());
 assertEquals("Item 1", updateUserPanel.jComboBox1.getItemAt(0));
 assertEquals("Item 2", updateUserPanel.jComboBox1.getItemAt(1));
 assertEquals("Item 3", updateUserPanel.jComboBox1.getItemAt(2));
 assertEquals("Item 4", updateUserPanel.jComboBox1.getItemAt(3));

 // Check if comboBox2 has correct items
 assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
 assertEquals("user1", updateUserPanel.jComboBox2.getSelectedItem().getName());
 assertEquals("user1@email.com", updateUserPanel.jComboBox2.getSelectedItem().getEmail());
 }

 @Test
 public void testUpdateModelWithMultipleUsers() {
 // Set up group with multiple users
 when(adminApp.getGroups()).thenReturn(Arrays.asList(group));
 updateUserPanel.updateModel();

 // Check if comboBox1 has correct items
 assertEquals(4, updateUserPanel.jComboBox1.getItemCount());
 assertEquals("Item 1", updateUserPanel.jComboBox1.getItemAt(0));
 assertEquals("Item 2", updateUserPanel.jComboBox1.getItemAt(1));
 assertEquals("Item 3", updateUserPanel.jComboBox1.getItemAt(2));
 assertEquals("Item 4", updateUserPanel.jComboBox1.getItemAt(3));

 // Check if comboBox2 has correct items
 assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
 assertEquals("user1", updateUserPanel.jComboBox2.getSelectedItem().getName());
 assertEquals("user1@email.com", updateUserPanel.jComboBox2.getSelectedItem().getEmail());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 // Set up group with multiple users
 when(adminApp.getGroups()).thenReturn(Arrays.asList(group));
 when(group.getUsers()).thenReturn(users);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(1);
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, ItemEvent.ITEM_STATE_CHANGED, 1, 0));

 // Check if comboBox2 is updated
 assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
 assertEquals("user2", updateUserPanel.jComboBox2.getSelectedItem().getName());
 assertEquals("user2@email.com", updateUserPanel.jComboBox2.getSelectedItem().getEmail());
 }

 @Test
 public void testJButton1ActionPerformed() {
 when(controlPanel.getParentDialog()).thenReturn(Mockito.mock(JDialog.class));
 updateUserPanel.jButton1ActionPerformed(new ActionEvent(updateUserPanel.jButton1, ActionEvent.ACTION_PERFORMED, ""));
 verify(controlPanel.getParentDialog()).setVisible(false);
 }

 @Test
 public void testJButton2ActionPerformedWithNullUser() {
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(updateUserPanel.jButton2, ActionEvent.ACTION_PERFORMED, ""));
 verify(adminApp, never()).delUser(Mockito.any(User.class));
 }

 @Test
 public void testJButton2ActionPerformedWithValidUser() {
 when(adminApp.getGroups()).thenReturn(Collections.singletonList(group));
 when(group.getUsers()).thenReturn(users);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(updateUserPanel.jButton2, ActionEvent.ACTION_PERFORMED, ""));

 verify(adminApp).delUser(Mockito.eq(users.get(0)));
 verify(group).getUsers().remove(0);
 }

 @Test
 public void testJButton3ActionPerformedWithNullUser() {
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(updateUserPanel.jButton3, ActionEvent.ACTION_PERFORMED, ""));
 verify(adminApp, never()).resetUserPass(Mockito.any(User.class));
 }

 @Test
 public void testJButton3ActionPerformedWithValidUser() {
 when(adminApp.getGroups()).thenReturn(Collections.singletonList(group));
 when(group.getUsers()).thenReturn(users);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(updateUserPanel.jButton3, ActionEvent.ACTION_PERFORMED, ""));

 verify(adminApp).resetUserPass(Mockito.eq(users.get(0)));
 }

 @Test
 public void testJButton4ActionPerformedWithEmptyFields() {
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jTextField3.setText("");
 updateUserPanel.jTextField4.setText("");
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(updateUserPanel.jButton4, ActionEvent.ACTION_PERFORMED, ""));

 verify(adminApp, never()).updateUser(Mockito.any(User.class));
 }

 @Test
 public void testJButton4ActionPerformedWithInvalidEmail() {
 updateUserPanel.jTextField1.setText("user1");
 updateUserPanel.jTextField2.setText("user1@email");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("User1Title");
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(updateUserPanel.jButton4, ActionEvent.ACTION_PERFORMED, ""));

 verify(adminApp, never()).updateUser(Mockito.any(User.class));
 }

 @Test
 public void testJButton4ActionPerformedWithValidUser() {
 updateUserPanel.jTextField1.setText("user2");
 updateUserPanel.jTextField2.setText("user2@email.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("User2Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(updateUserPanel.jButton4, ActionEvent.ACTION_PERFORMED, ""));

 verify(adminApp).updateUser(Mockito.eq(users.get(0)));
 assertEquals("user2", updateUserPanel.jComboBox2.getSelectedItem().getName());
 assertEquals("user2@email.com", updateUserPanel.jComboBox2.getSelectedItem().getEmail());
 assertEquals(true, updateUserPanel.jComboBox2.getSelectedItem().isShowMyStatus());
 assertEquals(true, updateUserPanel.jComboBox2.getSelectedItem().isOnlyStartChat());
 assertEquals(true, updateUserPanel.jComboBox2.getSelectedItem().isCanJoinChatRoom());
 }
}