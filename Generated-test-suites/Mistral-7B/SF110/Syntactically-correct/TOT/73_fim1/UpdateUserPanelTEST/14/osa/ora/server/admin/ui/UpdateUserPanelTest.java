package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

 private AdminApp adminAppMock;
 private ControlPanel controlPanelMock;
 private UpdateUserPanel updateUserPanel;

 @Before
 public void setup() {
 adminAppMock = mock(AdminApp.class);
 controlPanelMock = mock(ControlPanel.class);
 updateUserPanel = new UpdateUserPanel(adminAppMock, controlPanelMock);
 }

 @Test
 public void testUpdateModel_whenGroupsAndUsersArePresent_thenUpdateModelSetsValidData() {
 Group groupMock = mock(Group.class);
 when(adminAppMock.getGroups()).thenReturn(Arrays.asList(groupMock));
 User userMock = mock(User.class);
 when(groupMock.getUsers()).thenReturn(Arrays.asList(userMock));
 when(userMock.getName()).thenReturn("Test User");
 when(userMock.getEmail()).thenReturn("test@example.com");
 when(userMock.getDirectPhone()).thenReturn("555-1234");
 when(userMock.isShowMyStatus()).thenReturn(true);
 when(userMock.isOnlyStartChat()).thenReturn(false);
 when(userMock.isCanJoinChatRoom()).thenReturn(true);
 updateUserPanel.updateModel();
 assertEquals("Test User", updateUserPanel.jTextField1.getText());
 assertEquals("test@example.com", updateUserPanel.jTextField2.getText());
 assertEquals("555-1234", updateUserPanel.jTextField3.getText());
 assertTrue(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testUpdateModel_whenGroupsIsEmpty_thenUpdateModelDoesNotSetAnyData() {
 when(adminAppMock.getGroups()).thenReturn(Collections.emptyList());
 updateUserPanel.updateModel();
 assertNull(updateUserPanel.jTextField1.getText());
 assertNull(updateUserPanel.jTextField2.getText());
 assertNull(updateUserPanel.jTextField3.getText());
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testUpdateModel_whenUserIsPresent_butGroupsIsEmpty_thenUpdateModelDoesNotSetAnyData() {
 Group groupMock = mock(Group.class);
 User userMock = mock(User.class);
 when(adminAppMock.getGroups()).thenReturn(Collections.emptyList());
 when(groupMock.getUsers()).thenReturn(Collections.singletonList(userMock));
 updateUserPanel.updateModel();
 assertNull(updateUserPanel.jTextField1.getText());
 assertNull(updateUserPanel.jTextField2.getText());
 assertNull(updateUserPanel.jTextField3.getText());
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testJComboBox1ItemStateChanged_whenUserAndGroupArePresent_thenUserListIsPopulated() {
 Group groupMock = mock(Group.class);
 User userMock1 = mock(User.class);
 User userMock2 = mock(User.class);
 when(adminAppMock.getGroups()).thenReturn(Arrays.asList(groupMock));
 when(groupMock.getUsers()).thenReturn(Arrays.asList(userMock1, userMock2));
 updateUserPanel.updateModel();
 verify(updateUserPanel, times(1)).jComboBox1ItemStateChanged(any(ItemEvent.class));
 assertEquals(2, updateUserPanel.jComboBox1.getItemCount());
 }

 @Test
 public void testJComboBox1ItemStateChanged_whenUserIsNotPresent_thenUserListIsNotPopulated() {
 Group groupMock = mock(Group.class);
 when(adminAppMock.getGroups()).thenReturn(Collections.singletonList(groupMock));
 updateUserPanel.updateModel();
 verify(updateUserPanel, times(1)).jComboBox1ItemStateChanged(any(ItemEvent.class));
 assertEquals(0, updateUserPanel.jComboBox1.getItemCount());
 }

 @Test
 public void testJComboBox1ItemStateChanged_whenGroupIsNotPresent_thenUserListIsNotPopulated() {
 User userMock = mock(User.class);
 when(adminAppMock.getGroups()).thenReturn(Collections.emptyList());
 when(userMock.getName()).thenReturn("Test User");
 updateUserPanel.updateModel();
 verify(updateUserPanel, times(1)).jComboBox1ItemStateChanged(any(ItemEvent.class));
 assertEquals(0, updateUserPanel.jComboBox1.getItemCount());
 }

 @Test
 public void testJButton1ActionPerformed_whenUserIsPresent_thenDialogIsHiddenAndDataIsReset() {
 User userMock = mock(User.class);
 when(userMock.getName()).thenReturn("Test User");
 updateUserPanel.jTextField1.setText("Test User");
 updateUserPanel.jButton1ActionPerformed(mock(ActionEvent.class));
 assertTrue(updateUserPanel.isVisible() == false);
 assertNull(updateUserPanel.jTextField1.getText());
 }

 @Test
 public void testJButton1ActionPerformed_whenUserIsNotPresent_thenDialogIsNotHiddenOrDataIsReset() {
 updateUserPanel.jButton1ActionPerformed(mock(ActionEvent.class));
 assertFalse(updateUserPanel.isVisible() == false);
 }

 @Test
 public void testJButton2ActionPerformed_whenUserIsPresent_thenUserIsRemoved() {
 Group groupMock = mock(Group.class);
 User userMock = mock(User.class);
 when(adminAppMock.getGroups()).thenReturn(Arrays.asList(groupMock));
 when(groupMock.getUsers()).thenReturn(Arrays.asList(userMock));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2 = mock(JComboBox.class);
 when(updateUserPanel.jComboBox2.getSelectedItem()).thenReturn(userMock);
 doAnswer(invocation -> {
 userMock.delete();
 return null;
 }).when(userMock).delete();
 updateUserPanel.jButton2ActionPerformed(mock(ActionEvent.class));
 verify(updateUserPanel, times(1)).jComboBox2ItemStateChanged(any(ItemEvent.class));
 verify(groupMock, times(1)).getUsers();
 assertEquals(0, updateUserPanel.jComboBox2.getItemCount());
 }

 @Test
 public void testJButton2ActionPerformed_whenUserIsNotPresent_thenNothingHappens() {
 updateUserPanel.jButton2ActionPerformed(mock(ActionEvent.class));
 verifyZeroInteractions(updateUserPanel.jComboBox2);
 }

 @Test
 public void testJButton3ActionPerformed_whenUserIsPresent_thenPasswordIsReset() {
 User userMock = mock(User.class);
 when(userMock.getName()).thenReturn("Test User");
 when(userMock.getEmail()).thenReturn("test@example.com");
 updateUserPanel.jTextField1.setText("Test User");
 updateUserPanel.jTextField2.setText("test@example.com");
 updateUserPanel.jButton3ActionPerformed(mock(ActionEvent.class));
 assertTrue(updateUserPanel.isVisible() == false);
 verify(userMock, times(1)).resetPassword();
 }

 @Test
 public void testJButton3ActionPerformed_whenUserIsNotPresent_thenNothingHappens() {
 updateUserPanel.jButton3ActionPerformed(mock(ActionEvent.class));
 verifyZeroInteractions(updateUserPanel.jTextField1);
 verifyZeroInteractions(updateUserPanel.jTextField2);
 }

 @Test
 public void testJButton4ActionPerformed_whenValidDataIsProvided_andUserIsPresent_thenUserIsUpdated() {
 Group groupMock = mock(Group.class);
 User userMock = mock(User.class);
 when(adminAppMock.getGroups()).thenReturn(Arrays.asList(groupMock));
 when(groupMock.getUsers()).thenReturn(Arrays.asList(userMock));
 updateUserPanel.updateModel();
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("new.email@example.com");
 updateUserPanel.jTextField3.setText("555-5678");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));
 verify(userMock, times(1)).setName("New User Name");
 verify(userMock, times(1)).setEmail("new.email@example.com");
 verify(userMock, times(1)).setDirectPhone("555-5678");
 verify(userMock, times(1)).setJobTitle("New Job Title");
 verify(groupMock, times(1)).getUsers();
 }

 @Test
 public void testJButton4ActionPerformed_whenValidDataIsNotProvided_thenNothingHappens() {
 updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));
 verifyZeroInteractions(updateUserPanel.adminAppMock);
 verifyZeroInteractions(updateUserPanel.controlPanelMock);
 }

 @Test
 public void testJButton4ActionPerformed_whenUserIsNotPresent_thenNothingHappens() {
 updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));
 verifyZeroInteractions(updateUserPanel.adminAppMock);
 verifyZeroInteractions(updateUserPanel.controlPanelMock);
 }

}