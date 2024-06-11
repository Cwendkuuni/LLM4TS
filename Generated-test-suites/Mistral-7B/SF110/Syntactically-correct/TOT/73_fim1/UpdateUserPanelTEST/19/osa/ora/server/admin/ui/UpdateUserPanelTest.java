package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private UpdateUserPanel updateUserPanel;
 private Group mockGroup;
 private User mockUser;

 @Before
 public void setUp() {
 adminApp = Mockito.mock(AdminApp.class);
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 mockGroup = Mockito.mock(Group.class);
 mockUser = Mockito.mock(User.class);

 when(adminApp.getGroups()).thenReturn(Arrays.asList(mockGroup));
 when(mockGroup.getUsers()).thenReturn(Collections.singletonList(mockUser));
 }

 @Test
 public void testUpdateModel() {
 updateUserPanel.updateModel();
 verify(adminApp, times(1)).getGroups();
 verify(mockGroup, times(1)).getUsers();
 }

 @Test
 public void testFillUser() {
 updateUserPanel.fillUser(mockUser);
 String name = "John Doe";
 String email = "john.doe@example.com";
 String phone = "123-456-7890";
 String jobTitle = "Software Engineer";
 boolean showMyStatus = true;
 boolean onlyStartChat = false;
 boolean canJoinChatRoom = true;

 when(mockUser.getName()).thenReturn(name);
 when(mockUser.getEmail()).thenReturn(email);
 when(mockUser.getDirectPhone()).thenReturn(phone);
 when(mockUser.getJobTitle()).thenReturn(jobTitle);
 when(mockUser.isShowMyStatus()).thenReturn(showMyStatus);
 when(mockUser.isOnlyStartChat()).thenReturn(onlyStartChat);
 when(mockUser.isCanJoinChatRoom()).thenReturn(canJoinChatRoom);

 assertEquals(name, updateUserPanel.jTextField1.getText());
 assertEquals(email, updateUserPanel.jTextField2.getText());
 assertEquals(phone, updateUserPanel.jTextField3.getText());
 assertEquals(jobTitle, updateUserPanel.jTextField4.getText());
 assertTrue(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertTrue(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 updateUserPanel.jComboBox1.setSelectedItem(mockGroup);
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, ItemEvent.ITEM_SELECTED, 0, ItemEvent.SELECTED, null));
 verify(adminApp, times(1)).getGroups();
 verify(mockGroup, times(1)).getUsers();
 }

 @Test
 public void testReset() {
 updateUserPanel.reset();
 assertEquals("", updateUserPanel.jTextField1.getText());
 assertEquals("", updateUserPanel.jTextField2.getText());
 assertEquals("", updateUserPanel.jTextField3.getText());
 assertEquals("", updateUserPanel.jTextField4.getText());
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1.doClick();
 verify(adminApp, times(1)).delDialog();
 }

 @Test
 public void testJButton2ActionPerformed_UserExists() {
 updateUserPanel.jComboBox2.setSelectedItem(mockUser);
 updateUserPanel.jButton2.doClick();
 verify(adminApp, times(1)).delUser(mockUser);
 verify(mockGroup, times(1)).getUsers();
 }

 @Test(expected = IllegalStateException.class)
 public void testJButton2ActionPerformed_UserIsNull() {
 updateUserPanel.jButton2.doClick();
 verify(adminApp, times(0)).delUser(any());
 }
}