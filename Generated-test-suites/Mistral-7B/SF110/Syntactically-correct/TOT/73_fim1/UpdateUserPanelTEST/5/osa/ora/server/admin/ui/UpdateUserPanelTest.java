package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import osa.ora.server.admin.AdminApp;
import osa.ora.server.admin.Group;
import osa.ora.server.admin.User;
import osa.ora.server.beans.GroupBean;
import osa.ora.server.beans.UserBean;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private UpdateUserPanel updateUserPanel;
 private Group group;
 private User user;

 @Before
 public void setUp() {
 adminApp = mock(AdminApp.class);
 group = new Group("Group1", new ArrayList<User>());
 user = new User("User1", "user1@example.com", "123456", "N/A", true, true, true, null);
 group.getUsers().add(user);

 updateUserPanel = new UpdateUserPanel(adminApp, null);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 }

 @Test
 public void testUpdateUser() {
 // Given
 String newName = "NewUserName";
 String newEmail = "newuser@example.com";
 String newPhone = "123-456-7890";
 String newJobTitle = "New Job Title";

 updateUserPanel.jTextField1.setText(newName);
 updateUserPanel.jTextField2.setText(newEmail);
 updateUserPanel.jTextField3.setText(newPhone);
 updateUserPanel.jTextField4.setText(newJobTitle);
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();

 // When
 User expectedUser = new User(newName, newEmail, null, newJobTitle, true, false, true, null);

 verify(adminApp).updateUser(user);
 assertEquals(expectedUser, user);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateUserNullUser() {
 updateUserPanel.jButton4.doClick();
 verifyZeroInteractions(adminApp);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateUserNullName() {
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jTextField2.setText(newEmail);
 updateUserPanel.jTextField3.setText(newPhone);
 updateUserPanel.jTextField4.setText(newJobTitle);
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 verifyZeroInteractions(adminApp);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateUserNullEmail() {
 updateUserPanel.jTextField1.setText(newName);
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jTextField3.setText(newPhone);
 updateUserPanel.jTextField4.setText(newJobTitle);
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 verifyZeroInteractions(adminApp);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateUserEmptyEmail() {
 updateUserPanel.jTextField1.setText(newName);
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jTextField3.setText(newPhone);
 updateUserPanel.jTextField4.setText(newJobTitle);
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 verifyZeroInteractions(adminApp);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateUserNullPhone() {
 updateUserPanel.jTextField1.setText(newName);
 updateUserPanel.jTextField2.setText(newEmail);
 updateUserPanel.jTextField3.setText("");
 updateUserPanel.jTextField4.setText(newJobTitle);
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 verifyZeroInteractions(adminApp);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateUserEmptyPhone() {
 updateUserPanel.jTextField1.setText(newName);
 updateUserPanel.jTextField2.setText(newEmail);
 updateUserPanel.jTextField3.setText("");
 updateUserPanel.jTextField4.setText(newJobTitle);
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 verifyZeroInteractions(adminApp);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateUserNullJobTitle() {
 updateUserPanel.jTextField1.setText(newName);
 updateUserPanel.jTextField2.setText(newEmail);
 updateUserPanel.jTextField3.setText(newPhone);
 updateUserPanel.jTextField4.setText("");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 verifyZeroInteractions(adminApp);
 }

 @Test
 public void testDeleteUser() {
 // Given
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2.setSelectedIndex(0);

 // When
 updateUserPanel.jButton2.doClick();

 // Then
 verify(adminApp).delUser(user);
 assertEquals(0, group.getUsers().size());
 }

 @Test(expected = IllegalStateException.class)
 public void testDeleteNonSelectedUser() {
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jButton2.doClick();
 }

 @Test
 public void testResetPassword() {
 // Given
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2.setSelectedItem(user);

 // When
 updateUserPanel.jButton3.doClick();

 // Then
 verify(adminApp).resetUserPass(user);
 }

 @Test(expected = NullPointerException.class)
 public void testResetPasswordNullUser() {
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jButton3.doClick();
 }
}