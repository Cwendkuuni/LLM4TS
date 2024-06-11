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

 private AdminApp adminAppMock;
 private ControlPanel controlPanelMock;
 private UpdateUserPanel updateUserPanel;
 private Group mockGroup;
 private User mockUser1, mockUser2;

 @Before
 public void setUp() {
 adminAppMock = mock(AdminApp.class);
 controlPanelMock = mock(ControlPanel.class);
 mockGroup = Mockito.mock(Group.class);
 mockUser1 = Mockito.mock(User.class);
 mockUser2 = Mockito.mock(User.class);

 updateUserPanel = new UpdateUserPanel(adminAppMock, controlPanelMock);

 when(mockGroup.getUsers()).thenReturn(new Vector<User>(Arrays.asList(mockUser1, mockUser2)));
 when(adminAppMock.getGroups()).thenReturn(Collections.singletonList(mockGroup));
 when(adminAppMock.getErrorIcon()).thenReturn(Mockito.mock(ImageIcon.class));

 updateUserPanel.updateModel();
 }

 @Test
 public void testUpdateModel() {
 assertEquals(1, updateUserPanel.jComboBox1.getItemCount());
 assertNull(updateUserPanel.jComboBox2.getSelectedItem());

 updateUserPanel.updateModel();

 assertEquals(1, updateUserPanel.jComboBox1.getItemCount());
 assertEquals(mockUser1, updateUserPanel.jComboBox2.getItemAt(0));
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 verify(adminAppMock, never()).getGroups();

 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox1ItemStateChanged(null);

 verify(adminAppMock).getGroups();
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1.doClick();

 verify(controlPanelMock).getParentDialog().setVisible(false);
 verify(updateUserPanel).reset();
 }

 @Test
 public void testJButton2ActionPerformed_UserIsNull() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jButton2.doClick();

 assertEquals("Can't identify selected User!", updateUserPanel.getErrorMessage());
 }

 @Test
 public void testJButton2ActionPerformed_UserDeletedSuccessfully() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton2.doClick();

 verify(mockUser1).getId();
 verify(mockGroup).getUsers().remove(0);
 assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
 }

 @Test
 public void testJButton3ActionPerformed_UserPasswordResetSuccessfully() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton3.doClick();

 verify(adminAppMock).resetUserPass(mockUser1);
 }

 @Test
 public void testJButton4ActionPerformed_UserNameEmpty() {
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jTextField2.setText("test@test.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("Developer");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();

 assertEquals("Please Enter User Name", updateUserPanel.getErrorMessage());
 }

 @Test
 public void testJButton4ActionPerformed_UserEmailAlreadyExists() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("test");
 updateUserPanel.jTextField2.setText("test@test.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("Developer");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();

 assertEquals("User with Same email Already Exist!", updateUserPanel.getErrorMessage());
 }

 @Test
 public void testJButton4ActionPerformed_UserUpdatedSuccessfully() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("test");
 updateUserPanel.jTextField2.setText("test1@test.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("Developer");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();

 verify(mockUser1).setName("test");
 verify(mockUser1).setEmail("test1@test.com");
 verify(mockUser1).setDirectPhone("1234567890");
 verify(mockUser1).setJobTitle("Developer");
 verify(mockUser1).setShowMyStatus(true);
 verify(mockUser1).setOnlyStartChat(true);
 verify(mockUser1).setCanJoinChatRoom(true);
 assertEquals("User Updated Successfully!", updateUserPanel.getSuccessMessage());
 }

 @Test
 public void testJComboBox2ItemStateChanged() {
 verify(adminAppMock, never()).getGroups();

 updateUserPanel.jComboBox2.setSelectedIndex(1);
 updateUserPanel.jComboBox2ItemStateChanged(null);

 verify(adminAppMock).getGroups();
 }
}