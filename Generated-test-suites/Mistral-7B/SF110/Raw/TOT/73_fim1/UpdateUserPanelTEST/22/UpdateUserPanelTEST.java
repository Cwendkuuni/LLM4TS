package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import osa.ora.server.admin.AdminApp;
import osa.ora.server.admin.Group;
import osa.ora.server.admin.User;
import osa.ora.server.beans.MockDataProvider;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private UpdateUserPanel updateUserPanel;
 private ControlPanel controlPanel;

 @Before
 public void setUp() {
 adminApp = Mockito.mock(AdminApp.class);
 controlPanel = Mockito.mock(ControlPanel.class);
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 }

 @Test
 public void testUpdateUserPanel_InitialState() {
 // Test initial state of the UpdateUserPanel
 assertEquals("", updateUserPanel.jTextField1.getText());
 assertEquals("", updateUserPanel.jTextField2.getText());
 assertEquals("", updateUserPanel.jTextField3.getText());
 assertEquals("", updateUserPanel.jTextField4.getText());
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testUpdateUserPanel_UpdateUser() {
 // Test updateUser method with valid user data
 User user = new User("John Doe", "johndoe@example.com", "555-555-1234", "Software Engineer", true, true, true);
 Group group = new Group("TestGroup", Arrays.asList(user));
 when(adminApp.getGroups()).thenReturn(Collections.singletonList(group));
 when(adminApp.updateUser(user)).thenReturn(new User("John Doe Updated", "johndoe@example.com", "555-555-1234", "Software Engineer Updated", true, true, true));

 updateUserPanel.jComboBox1.setSelectedItem("TestGroup");
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("John Doe Updated");
 updateUserPanel.jTextField2.setText("johndoe@example.com");
 updateUserPanel.jTextField3.setText("555-555-1234");
 updateUserPanel.jTextField4.setText("Software Engineer Updated");
 updateUserPanel.jCheckBox1.doClick();
 updateUserPanel.jButton4.doClick();

 verify(adminApp).updateUser(user);
 assertTrue(user.isShowMyStatus());

 // Test updateUser method with empty name
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jButton4.doClick();

 verifyZeroInteractions(adminApp);
 assertEquals("", updateUserPanel.jTextField1.getText());
 }

 @Test
 public void testUpdateUserPanel_UpdateUser_EmailAlreadyExists() {
 // Test updateUser method with email already exists
 User user1 = new User("John Doe", "johndoe@example.com", "555-555-1234", "Software Engineer", true, true, true);
 User user2 = new User("Jane Doe", "johndoe@example.com", "555-555-1337", "Software Engineer", true, true, true);
 Group group = new Group("TestGroup", Arrays.asList(user1, user2));
 when(adminApp.getGroups()).thenReturn(Collections.singletonList(group));
 when(adminApp.updateUser(user1)).thenThrow(new RuntimeException("Email already exists"));

 updateUserPanel.jComboBox1.setSelectedItem("TestGroup");
 updateUserPanel.jComboBox2.setSelectedItem(user1);
 updateUserPanel.jTextField1.setText("John Doe Updated");
 updateUserPanel.jTextField2.setText("johndoe@example.com");
 updateUserPanel.jTextField3.setText("555-555-1234");
 updateUserPanel.jTextField4.setText("Software Engineer Updated");
 updateUserPanel.jCheckBox1.doClick();
 updateUserPanel.jButton4.doClick();

 verifyZeroInteractions(adminApp);
 assertEquals("Email already exists", updateUserPanel.jOptionPane.getMessage());
 }

 @Test
 public void testUpdateUserPanel_DeleteUser() {
 // Test deleteUser method
 User user = new User("John Doe", "johndoe@example.com", "555-555-1234", "Software Engineer", true, true, true);
 Group group = new Group("TestGroup", Arrays.asList(user));
 when(adminApp.getGroups()).thenReturn(Collections.singletonList(group));
 when(adminApp.delUser(user)).thenReturn(null);

 updateUserPanel.jComboBox1.setSelectedItem("TestGroup");
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jButton2.doClick();

 verify(adminApp).delUser(user);
 assertTrue(group.getUsers().isEmpty());
 }

 @Test
 public void testUpdateUserPanel_ResetPassword() {
 // Test resetPassword method
 User user = new User("John Doe", "johndoe@example.com", "555-555-1234", "Software Engineer", true, true, true);
 when(adminApp.resetUserPass(user)).thenReturn(new User("John Doe", "johndoe@example.com", "password123", "Software Engineer", true, true, true));

 updateUserPanel.jComboBox1.setSelectedItem("TestGroup");
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jButton3.doClick();

 verify(adminApp).resetUserPass(user);
 assertEquals("password123", updateUserPanel.jTextField2.getText());
 }
}