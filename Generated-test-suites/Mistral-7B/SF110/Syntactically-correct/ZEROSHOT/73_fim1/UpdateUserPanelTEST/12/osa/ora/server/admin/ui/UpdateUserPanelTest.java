package osa.ora.server.admin.ui;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private ControlPanel controlPanel;
 private UpdateUserPanel updateUserPanel;

 @Before
 public void setUp() {
 adminApp = Mockito.mock(AdminApp.class);
 controlPanel = Mockito.mock(ControlPanel.class);
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 }

 @Test
 public void testUpdateModel() {
 Group group = Mockito.mock(Group.class);
 Mockito.when(adminApp.getGroups()).thenReturn(Arrays.asList(group));
 updateUserPanel.updateModel();
 assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
 User user = Mockito.mock(User.class);
 Mockito.when(group.getUsers()).thenReturn(Arrays.asList(user));
 Mockito.when(user.getName()).thenReturn("Test User");
 Mockito.when(user.getEmail()).thenReturn("test@email.com");
 Mockito.when(user.getDirectPhone()).thenReturn("123-456-7890");
 Mockito.when(user.isShowMyStatus()).thenReturn(true);
 Mockito.when(user.isOnlyStartChat()).thenReturn(false);
 Mockito.when(user.isCanJoinChatRoom()).thenReturn(true);
 updateUserPanel.updateModel();
 assertEquals("Test User", updateUserPanel.jTextField1.getText());
 assertEquals("test@email.com", updateUserPanel.jTextField2.getText());
 assertEquals("123-456-7890", updateUserPanel.jTextField3.getText());
 assertTrue(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertTrue(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 Group group = Mockito.mock(Group.class);
 Mockito.when(adminApp.getGroups()).thenReturn(Arrays.asList(group));
 User user = Mockito.mock(User.class);
 Mockito.when(group.getUsers()).thenReturn(Arrays.asList(user));
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox1ItemStateChanged(null);
 assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
 Mockito.when(user.getName()).thenReturn("Test User");
 Mockito.when(user.getEmail()).thenReturn("test@email.com");
 Mockito.when(user.getDirectPhone()).thenReturn("123-456-7890");
 Mockito.when(user.isShowMyStatus()).thenReturn(true);
 Mockito.when(user.isOnlyStartChat()).thenReturn(false);
 Mockito.when(user.isCanJoinChatRoom()).thenReturn(true);
 updateUserPanel.updateModel();
 assertEquals("Test User", updateUserPanel.jTextField1.getText());
 assertEquals("test@email.com", updateUserPanel.jTextField2.getText());
 assertEquals("123-456-7890", updateUserPanel.jTextField3.getText());
 assertTrue(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertTrue(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1.doClick();
 verify(controlPanel).getParentDialog().setVisible(false);
 }

 @Test
 public void testJButton2ActionPerformed() {
 Group group = Mockito.mock(Group.class);
 Mockito.when(adminApp.getGroups()).thenReturn(Arrays.asList(group));
 User user = Mockito.mock(User.class);
 Mockito.when(group.getUsers()).thenReturn(Arrays.asList(user));
 Mockito.when(user.getId()).thenReturn(1);
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jButton2.doClick();
 Mockito.verify(adminApp).delUser(user);
 }

 @Test
 public void testJButton3ActionPerformed() {
 Group group = Mockito.mock(Group.class);
 Mockito.when(adminApp.getGroups()).thenReturn(Arrays.asList(group));
 User user = Mockito.mock(User.class);
 Mockito.when(group.getUsers()).thenReturn(Arrays.asList(user));
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jButton3.doClick();
 Mockito.verify(adminApp).resetUserPass(user);
 }

 @Test
 public void testJButton4ActionPerformed() {
 Group group = Mockito.mock(Group.class);
 Mockito.when(adminApp.getGroups()).thenReturn(Arrays.asList(group));
 User user = Mockito.mock(User.class);
 Mockito.when(group.getUsers()).thenReturn(Arrays.asList(user));
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("new.email@email.com");
 updateUserPanel.jTextField3.setText("New Phone Number");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 Mockito.verify(user).setName("New User Name");
 Mockito.verify(user).setEmail("new.email@email.com");
 Mockito.verify(user).setDirectPhone("New Phone Number");
 Mockito.verify(user).setJobTitle("New Job Title");
 Mockito.verify(user).setShowMyStatus(true);
 Mockito.verify(user).setOnlyStartChat(false);
 Mockito.verify(user).setCanJoinChatRoom(true);
 Mockito.verify(adminApp).updateUser(user);
 }
}