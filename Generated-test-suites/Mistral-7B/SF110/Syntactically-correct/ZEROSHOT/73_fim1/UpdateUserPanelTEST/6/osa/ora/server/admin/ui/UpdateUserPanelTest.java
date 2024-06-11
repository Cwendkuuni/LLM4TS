package osa.ora.server.admin.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UpdateUserPanel.class})
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
 List<Group> groups = Arrays.asList(new Group("Group1"), new Group("Group2"));
 PowerMockito.whenNew(AdminApp.class).withArguments().thenReturn(adminApp);
 PowerMockito.when(adminApp.getGroups()).thenReturn(groups);

 updateUserPanel.updateModel();

 assertEquals("Group1", updateUserPanel.jComboBox1.getSelectedItem().toString());
 assertTrue(updateUserPanel.jComboBox2.getModel().getSize() > 0);
 assertSame(groups.get(0).getUsers().get(0), updateUserPanel.jComboBox2.getSelectedItem());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 Group group = new Group("Group1");
 List<User> users = Arrays.asList(new User("User1"), new User("User2"));
 group.setUsers(users);
 PowerMockito.whenNew(AdminApp.class).withArguments().thenReturn(adminApp);
 PowerMockito.when(adminApp.getGroups()).thenReturn(Arrays.asList(group));

 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, ItemEvent.ITEM_STATE_CHANGED, 0, ItemEvent.DESELECTED));

 assertEquals(0, updateUserPanel.jComboBox2.getSelectedIndex());
 assertNull(updateUserPanel.jComboBox2.getSelectedItem());
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1ActionPerformed(new ActionEvent(updateUserPanel.jButton1, ActionEvent.ACTION_PERFORMED, ""));

 assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible());
 updateUserPanel.reset();
 }

 @Test
 public void testJButton2ActionPerformed() {
 User user = new User("User1");
 Group group = new Group("Group1");
 group.setUsers(Arrays.asList(user));
 PowerMockito.whenNew(AdminApp.class).withArguments().thenReturn(adminApp);
 PowerMockito.when(adminApp.getGroups()).thenReturn(Arrays.asList(group));
 PowerMockito.when(adminApp.delUser(Mockito.eq(user))).thenReturn(null);

 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(updateUserPanel.jButton2, ActionEvent.ACTION_PERFORMED, ""));

 assertTrue(group.getUsers().isEmpty());
 }

 @Test
 public void testJButton3ActionPerformed() {
 User user = new User("User1");
 PowerMockito.whenNew(AdminApp.class).withArguments().thenReturn(adminApp);
 PowerMockito.when(adminApp.resetUserPass(Mockito.eq(user))).thenReturn(new User("User1"));

 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField2.setText("newemail@email.com");
 updateUserPanel.jButton3ActionPerformed(new ActionEvent(updateUserPanel.jButton3, ActionEvent.ACTION_PERFORMED, ""));

 assertEquals("newemail@email.com", updateUserPanel.jComboBox2.getSelectedItem().getEmail());
 }

 @Test
 public void testJButton4ActionPerformed() {
 User user = new User("User1");
 Group group = new Group("Group1");
 group.setUsers(Arrays.asList(user));
 PowerMockito.whenNew(AdminApp.class).withArguments().thenReturn(adminApp);
 PowerMockito.when(adminApp.getGroups()).thenReturn(Arrays.asList(group));
 PowerMockito.when(adminApp.updateUser(Mockito.eq(user))).thenReturn(new User("User1"));

 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("newName");
 updateUserPanel.jTextField2.setText("newemail@email.com");
 updateUserPanel.jTextField3.setText("newPhone");
 updateUserPanel.jTextField4.setText("newTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(new ActionEvent(updateUserPanel.jButton4, ActionEvent.ACTION_PERFORMED, ""));

 assertSame(user, updateUserPanel.jComboBox2.getSelectedItem());
 assertTrue(user.isShowMyStatus());
 assertTrue(user.isOnlyStartChat());
 assertTrue(user.isCanJoinChatRoom());
 assertEquals("newName", user.getName());
 assertEquals("newemail@email.com", user.getEmail());
 assertEquals("newPhone", user.getDirectPhone());
 assertEquals("newTitle", user.getJobTitle());
 }

 @Test
 public void testReset() {
 updateUserPanel.reset();

 assertFalse(updateUserPanel.jTextField1.getText().isEmpty());
 assertFalse(updateUserPanel.jTextField2.getText().isEmpty());
 assertFalse(updateUserPanel.jTextField3.getText().isEmpty());
 assertFalse(updateUserPanel.jTextField4.getText().isEmpty());
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

}