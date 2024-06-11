package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import org.junit.*;
import org.mockito.*;

public class UpdateUserPanelTest {

 private UpdateUserPanel updateUserPanel;
 private AdminApp adminAppMock;
 private ControlPanel controlPanelMock;

 @Before
 public void setup() {
 adminAppMock = mock(AdminApp.class);
 controlPanelMock = mock(ControlPanel.class);

 updateUserPanel = new UpdateUserPanel(adminAppMock, controlPanelMock);
 }

 @Test
 public void testUpdateModel() {
 // Test with null groups list
 adminAppMock.getGroups = () -> null;
 updateUserPanel.updateModel();
 assertEquals(0, updateUserPanel.jComboBox1.getItemCount());

 // Test with empty groups list
 adminAppMock.getGroups = () -> new ArrayList<>();
 updateUserPanel.updateModel();
 assertEquals(0, updateUserPanel.jComboBox1.getItemCount());

 // Test with non-empty groups list
 Group groupMock = mock(Group.class);
 when(adminAppMock.getGroups).thenReturn(Arrays.asList(groupMock));
 updateUserPanel.updateModel();
 assertEquals(1, updateUserPanel.jComboBox1.getItemCount());

 User userMock1 = mock(User.class);
 User userMock2 = mock(User.class);
 when(groupMock.getUsers).thenReturn(Arrays.asList(userMock1, userMock2));
 updateUserPanel.updateModel();
 assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
 assertEquals(userMock1, updateUserPanel.jComboBox2.getItemAt(0));
 }

 @Test
 public void testInitComponents() {
 // Test label text
 assertEquals("Username:", updateUserPanel.jLabel1.getText());
 assertEquals("Login Email:", updateUserPanel.jLabel2.getText());
 assertEquals("Phone:", updateUserPanel.jLabel3.getText());
 assertEquals("User Group:", updateUserPanel.jLabel4.getText());

 // Test checkbox labels
 assertEquals("Show User Status To Others", updateUserPanel.jCheckBox1.getText());
 assertEquals("User Can Only Start Chat With Others", updateUserPanel.jCheckBox2.getText());
 assertEquals("Can be a Member of Chat Rooms", updateUserPanel.jCheckBox3.getText());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 // Test null selected item
 doReturn(null).when(adminAppMock).getGroups();
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, 0, 0, false, null));
 assertEquals(0, updateUserPanel.jComboBox2.getItemCount());

 // Test non-empty groups list
 doReturn(Arrays.asList(mock(Group.class))).when(adminAppMock).getGroups();
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, 0, 0, false, null));
 assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1ActionPerformed(new ActionEvent(updateUserPanel.jButton1, 0, ""));
 verify(controlPanelMock).getParentDialog().setVisible(false);
 }

 @Test
 public void testJButton2ActionPerformed() throws Exception {
 // Test null selected user
 updateUserPanel.jComboBox2ActionPerformed(new ActionEvent(updateUserPanel.jComboBox2, 0, ""));
 verifyNoMoreInteractions(adminAppMock);

 // Test valid user deletion
 User userMock = mock(User.class);
 when(adminAppMock.delUser(userMock)).thenReturn(userMock);
 updateUserPanel.jComboBox2 = mock(JComboBox.class);
 updateUserPanel.jComboBox2.setSelectedItem(userMock);
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(updateUserPanel.jButton2, 0, ""));
 verify(adminAppMock).delUser(userMock);
 }

 @Test(expected = Exception.class)
 public void testJButton2ActionPerformedWithNullSelectedUser() {
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(updateUserPanel.jButton2, 0, ""));
 fail("Expected Exception was not thrown");
 }

 @Test
 public void testJButton3ActionPerformed() throws Exception {
 // Test null selected user
 updateUserPanel.jComboBox2ActionPerformed(new ActionEvent(updateUserPanel.jComboBox2, 0, ""));
 verifyNoMoreInteractions(adminAppMock);

 // Test valid user password reset
 User userMock = mock(User.class);
 when(adminAppMock.resetUserPass(userMock)).thenReturn(userMock);
 updateUserPanel.jComboBox2 = mock(JComboBox.class);
 updateUserPanel.jComboBox2.setSelectedItem(userMock);
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(updateUserPanel.jButton3, 0, ""));
 verify(adminAppMock).resetUserPass(userMock);
 }

 @Test(expected = Exception.class)
 public void testJButton3ActionPerformedWithNullSelectedUser() {
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(updateUserPanel.jButton3, 0, ""));
 fail("Expected Exception was not thrown");
 }

 @Test
 public void testJButton4ActionPerformed() throws Exception {
 // Test empty text fields
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jTextField3.setText("");
 updateUserPanel.jTextField4.setText("");
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(updateUserPanel.jButton4, 0, ""));
 assertEquals(0, updateUserPanel.jComboBox1.getItemCount());
 verifyNoMoreInteractions(adminAppMock);

 // Test invalid email
 updateUserPanel.jTextField1.setText("test");
 updateUserPanel.jTextField2.setText("test@test.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("Test Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(updateUserPanel.jButton4, 0, ""));
 assertEquals(0, updateUserPanel.jComboBox1.getItemCount());
 verifyNoMoreInteractions(adminAppMock);

 // Test invalid username
 updateUserPanel.jTextField1.setText("test@test.com");
 updateUserPanel.jTextField2.setText("test");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("Test Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(updateUserPanel.jButton4, 0, ""));
 assertEquals(0, updateUserPanel.jComboBox1.getItemCount());
 verifyNoMoreInteractions(adminAppMock);

 // Test valid user update
 Group groupMock = mock(Group.class);
 User userMock = mock(User.class);
 when(adminAppMock.getGroups).thenReturn(Arrays.asList(groupMock));
 when(groupMock.getUsers).thenReturn(Arrays.asList(userMock));
 updateUserPanel.jTextField1.setText("testUser");
 updateUserPanel.jTextField2.setText("testUser@test.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("Test Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jComboBox1 = mock(JComboBox.class);
 updateUserPanel.jComboBox1.setSelectedItem(groupMock);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(updateUserPanel.jButton4, 0, ""));
 verify(adminAppMock).updateUser(userMock);
 }
}