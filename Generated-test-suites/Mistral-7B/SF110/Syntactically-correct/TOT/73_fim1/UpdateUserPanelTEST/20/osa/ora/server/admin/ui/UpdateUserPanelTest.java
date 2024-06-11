package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import osa.ora.server.admin.AdminApp;
import osa.ora.server.admin.Group;
import osa.ora.server.admin.User;
import osa.ora.server.beans.DefaultComboBoxModel;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private UpdateUserPanel updateUserPanel;
 private Group group;
 private List<User> users;

 @Before
 public void setUp() {
 adminApp = Mockito.mock(AdminApp.class);
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 group = Mockito.mock(Group.class);
 users = new ArrayList<>();
 users.add(Mockito.mock(User.class));
 group.setUsers(users);
 when(adminApp.getGroups()).thenReturn(Arrays.asList(group));
 }

 @Test
 public void testUpdateModel_GroupWithUsers_UpdateUserPanelShowsFirstUser() {
 updateUserPanel.updateModel();
 assertEquals("First user email", updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test
 public void testUpdateModel_GroupWithoutUsers_UpdateUserPanelShowsEmptyList() {
 updateUserPanel.updateModel();
 assertNull(updateUserPanel.jComboBox2.getSelectedItem());
 }

 @Test
 public void testUpdateModel_UpdateComboBox1_UpdatesComboBox2() {
 final User user = Mockito.mock(User.class);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(1);
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, ItemEvent.SELECTED, 1, 0, null));
 assertEquals("Second user email", updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test(expected = NullPointerException.class)
 public void testJButton3ActionPerformed_NullUser_ThrowsException() {
 updateUserPanel.jButton3ActionPerformed(Mockito.mock(ActionEvent.class));
 verify(adminApp).resetUserPass(null);
 }

 @Test
 public void testJButton3ActionPerformed_UserWithValidUpdate_UpdatesUser() {
 final User user = Mockito.mock(User.class);
 updateUserPanel.jTextField1.setText("NewName");
 updateUserPanel.jTextField2.setText("NewEmail");
 updateUserPanel.jTextField3.setText("NewPhone");
 updateUserPanel.jTextField4.setText("NewTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3ActionPerformed(Mockito.mock(ActionEvent.class));
 verify(adminApp).updateUser(user);
 }

 @Test
 public void testJButton2ActionPerformed_UserWithValidDelete_DeletesUser() {
 updateUserPanel.jComboBox2.setSelectedItem("UserToDelete");
 updateUserPanel.jButton2ActionPerformed(Mockito.mock(ActionEvent.class));
 verify(adminApp).delUser(Mockito.eq("UserToDelete"));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJButton2ActionPerformed_SelectedIndexOutOfBounds_ThrowsException() {
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jButton2ActionPerformed(Mockito.mock(ActionEvent.class));
 }

 @Test
 public void testJButton1ActionPerformed_CancelsUpdate() {
 updateUserPanel.jButton1ActionPerformed(Mockito.mock(ActionEvent.class));
 verify(adminApp, never()).updateUser(any(User.class));
 }

}