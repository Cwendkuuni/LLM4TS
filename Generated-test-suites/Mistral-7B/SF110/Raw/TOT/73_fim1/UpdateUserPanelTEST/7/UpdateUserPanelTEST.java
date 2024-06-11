package osa.ora.server.admin.ui;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import static org.junit.Assert.*;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private ControlPanel controlPanel;
 private UpdateUserPanel updateUserPanel;

 @Before
 public void setup() {
 adminApp = Mockito.mock(AdminApp.class);
 controlPanel = Mockito.mock(ControlPanel.class);
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 }

 @Test
 public void testUpdateModel() {
 // Test case for successful scenario
 Group group = Mockito.mock(Group.class);
 Vector<User> users = new Vector<>();
 User user = Mockito.mock(User.class);
 users.add(user);
 Mockito.when(adminApp.getGroups()).thenReturn(new ArrayList<>(Arrays.asList(group)));
 Mockito.when(group.getUsers()).thenReturn(users);
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

 // Test case for null group scenario
 Mockito.when(adminApp.getGroups()).thenReturn(null);
 updateUserPanel.updateModel();
 assertNull(updateUserPanel.jComboBox2);

 // Test case for null user scenario
 Mockito.when(group.getUsers()).thenReturn(null);
 updateUserPanel.updateModel();
 assertNull(updateUserPanel.jComboBox2);
}

 @Test
 public void testJButton1ActionPerformed() {
 // Test case for successful scenario
 updateUserPanel.jButton1ActionPerformed(Mockito.mock(ActionEvent.class));
 assertTrue(controlPanel.getParentDialog().isVisible());
 reset(updateUserPanel);

 // Test case for cancel button pressed when no user is selected
 updateUserPanel.jComboBox2 = null;
 updateUserPanel.jButton1ActionPerformed(Mockito.mock(ActionEvent.class));
 assertTrue(controlPanel.getParentDialog().isVisible());
}

 @Test
 public void testJButton2ActionPerformed() {
 // Test case for successful scenario
 Group group = Mockito.mock(Group.class);
 Vector<User> users = new Vector<>();
 User user = Mockito.mock(User.class);
 users.add(user);
 User selectedUser = Mockito.mock(User.class);
 Mockito.when(adminApp.getGroups()).thenReturn(new ArrayList<>(Arrays.asList(group)));
 Mockito.when(group.getUsers()).thenReturn(users);
 Mockito.when(user.getId()).thenReturn(1);
 Mockito.when(selectedUser.getId()).thenReturn(2);
 Mockito.when(adminApp.delUser(Mockito.eq(user))).thenReturn(selectedUser);
 Mockito.when(updateUserPanel.jComboBox2.getSelectedItem()).thenReturn(user);

 updateUserPanel.jButton2ActionPerformed(Mockito.mock(ActionEvent.class));

 assertEquals(1, updateUserPanel.jComboBox2.getSelectedIndex());
 assertNotNull(updateUserPanel.jComboBox2.getItemAt(0));
 assertNull(updateUserPanel.jComboBox2.getSelectedItem());

 // Test case for error scenario when selected user is null
 updateUserPanel.jComboBox2 = null;
 updateUserPanel.jButton2ActionPerformed(Mockito.mock(ActionEvent.class));
 assertNull(updateUserPanel.jComboBox2.getSelectedItem());
}

 @Test
 public void testJButton3ActionPerformed() {
 // Test case for successful scenario
 User user = Mockito.mock(User.class);
 Mockito.when(user.getId()).thenReturn(1);
 Mockito.when(adminApp.resetUserPass(Mockito.eq(user))).thenReturn(user);
 Mockito.when(updateUserPanel.jComboBox2.getSelectedItem()).thenReturn(user);

 updateUserPanel.jButton3ActionPerformed(Mockito.mock(ActionEvent.class));

 assertEquals("Password Reset Successfully!", updateUserPanel.getLastMessage());

 // Test case for error scenario when user is null
 updateUserPanel.jComboBox2 = null;
 updateUserPanel.jButton3ActionPerformed(Mockito.mock(ActionEvent.class));
 assertNull(updateUserPanel.getLastMessage());
}

 @Test
 public void testJButton4ActionPerformed() {
 // Test case for successful scenario
 Group group = Mockito.mock(Group.class);
 Vector<User> users = new Vector<>();
 User user = Mockito.mock(User.class);
 User selectedUser = Mockito.mock(User.class);
 Mockito.when(adminApp.getGroups()).thenReturn(new ArrayList<>(Arrays.asList(group)));
 Mockito.when(group.getUsers()).thenReturn(users);
 Mockito.when(user.getId()).thenReturn(1);
 Mockito.when(selectedUser.getId()).thenReturn(2);
 Mockito.when(adminApp.updateUser(Mockito.eq(user))).thenReturn(selectedUser);
 Mockito.when(updateUserPanel.jComboBox2.getSelectedItem()).thenReturn(user);
 Mockito.when(updateUserPanel.jTextField1.getText()).thenReturn("Test User");Mockito.when(updateUserPanel.jTextField2.getText()).thenReturn("test@email.com");
 Mockito.when(updateUserPanel.jTextField3.getText()).thenReturn("123-456-7890");
 Mockito.when(updateUserPanel.jTextField4.getText()).thenReturn("Test Title");
 Mockito.when(updateUserPanel.jCheckBox1.isSelected()).thenReturn(true);
 Mockito.when(updateUserPanel.jCheckBox2.isSelected()).thenReturn(false);
 Mockito.when(updateUserPanel.jCheckBox3.isSelected()).thenReturn(true);

 updateUserPanel.jButton4ActionPerformed(Mockito.mock(ActionEvent.class));

 assertEquals("Test User", updateUserPanel.jTextField1.getText());
 assertEquals("test@email.com", updateUserPanel.jTextField2.getText());
 assertEquals("Test Title", updateUserPanel.jTextField4.getText());
 assertTrue(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertTrue(updateUserPanel.jCheckBox3.isSelected());

 // Test case for error scenario when user is null
 updateUserPanel.jComboBox2 = null;
 updateUserPanel.jButton4ActionPerformed(Mockito.mock(ActionEvent.class));
 assertNull(updateUserPanel.jComboBox2.getSelectedItem());
}

 private void reset(UpdateUserPanel updateUserPanel) {
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jTextField3.setText("");
 updateUserPanel.jTextField4.setText("");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(false);
}
}