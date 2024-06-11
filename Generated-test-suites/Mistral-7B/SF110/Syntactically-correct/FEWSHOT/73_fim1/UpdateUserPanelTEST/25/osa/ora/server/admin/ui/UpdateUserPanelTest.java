package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private ControlPanel controlPanel;
 private UpdateUserPanel updateUserPanel;

 @Before
 public void setUp() {
 adminApp = mock(AdminApp.class);
 controlPanel = mock(ControlPanel.class);
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 }

 @Test
 public void testUpdateModel() {
 Group group = mock(Group.class);
 when(adminApp.getGroups()).thenReturn(new ArrayList<Group>() {{
 add(group);
 }});
 User user = mock(User.class);
 when(group.getUsers()).thenReturn(new ArrayList<User>() {{
 add(user);
 }});
 updateUserPanel.updateModel();
 assertEquals(group, updateUserPanel.getjComboBox1().getSelectedItem());
 assertEquals(user, updateUserPanel.getjComboBox2().getSelectedItem());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 Group group = mock(Group.class);
 User user = mock(User.class);
 when(adminApp.getGroups()).thenReturn(new ArrayList<Group>() {{
 add(group);
 }});
 when(group.getUsers()).thenReturn(new ArrayList<User>() {{
 add(user);
 }});
 updateUserPanel.updateModel();
 updateUserPanel.getjComboBox1().setSelectedItem(group);
 assertEquals(group, updateUserPanel.getjComboBox1().getSelectedItem());
 }

 @Test
 public void testJComboBox2ItemStateChanged() {
 Group group = mock(Group.class);
 User user1 = mock(User.class);
 User user2 = mock(User.class);
 when(adminApp.getGroups()).thenReturn(new ArrayList<Group>() {{
 add(group);
 }});
 when(group.getUsers()).thenReturn(new ArrayList<User>() {{
 add(user1);
 add(user2);
 }});
 updateUserPanel.updateModel();
 updateUserPanel.getjComboBox1().setSelectedItem(group);
 updateUserPanel.getjComboBox2().setSelectedItem(user1);
 assertEquals(user1, updateUserPanel.getjComboBox2().getSelectedItem());
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1 = mock(JButton.class);
 when(controlPanel.getParentDialog()).thenReturn(mock(Dialog.class));
 updateUserPanel.jButton1ActionPerformed(mock(ActionEvent.class));
 verify(controlPanel).getParentDialog().setVisible(false);
 }

 @Test
 public void testJButton2ActionPerformed() {
 Group group = mock(Group.class);
 User user = mock(User.class);
 when(adminApp.getGroups()).thenReturn(new ArrayList<Group>() {{
 add(group);
 }});
 when(group.getUsers()).thenReturn(new ArrayList<User>() {{
 add(user);
 }});
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton2 = mock(JButton.class);
 when(user.getId()).thenReturn(1);
 when(user.getName()).thenReturn("User1");
 when(adminApp.delUser(user)).thenReturn(user);
 updateUserPanel.jButton2ActionPerformed(mock(ActionEvent.class));
 assertEquals("User1", updateUserPanel.getjComboBox2().getSelectedItem().getName());
 }

 @Test
 public void testJButton3ActionPerformed() {
 Group group = mock(Group.class);
 User user = mock(User.class);
 when(adminApp.getGroups()).thenReturn(new ArrayList<Group>() {{
 add(group);
 }});
 when(group.getUsers()).thenReturn(new ArrayList<User>() {{
 add(user);
 }});
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton3 = mock(JButton.class);
 when(adminApp.resetUserPass(user)).thenReturn(user);
 updateUserPanel.jButton3ActionPerformed(mock(ActionEvent.class));
 assertEquals("User1", updateUserPanel.getjComboBox2().getSelectedItem().getName());
 }

 @Test
 public void testJButton4ActionPerformed() {
 Group group = mock(Group.class);
 User user = mock(User.class);
 when(adminApp.getGroups()).thenReturn(new ArrayList<Group>() {{
 add(group);
 }});
 when(group.getUsers()).thenReturn(new ArrayList<User>() {{
 add(user);
 }});
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton4 = mock(JButton.class);
 when(adminApp.updateUser(user)).thenReturn(user);
 updateUserPanel.jTextField1.setText("newUserName");
 updateUserPanel.jTextField2.setText("newEmail@example.com");
 updateUserPanel.jTextField3.setText("newPhone");
 updateUserPanel.jTextField4.setText("newTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));
 assertEquals("newUserName", updateUserPanel.getjComboBox2().getSelectedItem().getName());
 }

 @Test(expected = NullPointerException.class)
 public void testJButton4ActionPerformedNullName() {
 Group group = mock(Group.class);
 User user = mock(User.class);
 when(adminApp.getGroups()).thenReturn(new ArrayList<Group>() {{
 add(group);
 }});
 when(group.getUsers()).thenReturn(new ArrayList<User>() {{
 add(user);
 }});
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton4 = mock(JButton.class);
 updateUserPanel.jTextField1 = mock(JTextField.class);
 when(updateUserPanel.jTextField1.getText()).thenReturn(null);
 updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));
 }

 @Test(expected = NullPointerException.class)
 public void testJButton4ActionPerformedNullEmail() {
 Group group = mock(Group.class);
 User user = mock(User.class);
 when(adminApp.getGroups()).thenReturn(new ArrayList<Group>() {{
 add(group);
 }});
 when(group.getUsers()).thenReturn(new ArrayList<User>() {{
 add(user);
 }});
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton4 = mock(JButton.class);
 updateUserPanel.jTextField2 = mock(JTextField.class);
 when(updateUserPanel.jTextField2.getText()).thenReturn(null);
 updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));
 }

 @Test(expected = IllegalStateException.class)
 public void testJButton4ActionPerformedSameEmail() {
 Group group = mock(Group.class);
 User user1 = mock(User.class);
 User user2 = mock(User.class);
 when(adminApp.getGroups()).thenReturn(new ArrayList<Group>() {{
 add(group);
 }});
 when(group.getUsers()).thenReturn(new ArrayList<User>() {{
 add(user1);
 add(user2);
 }});
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox2.setSelectedIndex(1);
 updateUserPanel.jButton4 = mock(JButton.class);
 updateUserPanel.jTextField1.setText("newUserName");
 updateUserPanel.jTextField2.setText("existingEmail@example.com");
 updateUserPanel.jTextField3.setText("newPhone");
 updateUserPanel.jTextField4.setText("newTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));
 }

 @Test
 public void testReset() {
 updateUserPanel.reset();
 assertTrue(updateUserPanel.jTextField1.getText().isEmpty());
 assertTrue(updateUserPanel.jTextField2.getText().isEmpty());
 assertTrue(updateUserPanel.jTextField3.getText().isEmpty());
 assertTrue(updateUserPanel.jTextField4.getText().isEmpty());
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

}