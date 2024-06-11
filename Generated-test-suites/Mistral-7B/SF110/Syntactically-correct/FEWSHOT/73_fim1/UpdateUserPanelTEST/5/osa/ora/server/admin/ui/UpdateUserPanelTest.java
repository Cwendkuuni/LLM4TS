package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

import org.junit.Test;

public class UpdateUserPanelTest {

 UpdateUserPanel updateUserPanel;
 AdminApp adminApp = new AdminApp();
 ControlPanel controlPanel = new ControlPanel();

 @Test
 public void testUpdateUserPanel() {
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 assertNotNull(updateUserPanel);
 }

 @Test
 public void testUpdateModel() {
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 adminApp.setGroups(new Vector<Group>());
 adminApp.setGroups(Arrays.asList(new Group[3]));
 updateUserPanel.updateModel();
 assertEquals(3, updateUserPanel.jComboBox1.getItemCount());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 adminApp.setGroups(new Vector<Group>());
 adminApp.setGroups(Arrays.asList(new Group[3]));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(1);
 updateUserPanel.jComboBox1ItemStateChanged(null);
 assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
 }

 @Test
 public void testReset() {
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.reset();
 assertNull(updateUserPanel.jTextField1.getText());
 assertNull(updateUserPanel.jTextField2.getText());
 assertNull(updateUserPanel.jTextField3.getText());
 assertNull(updateUserPanel.jTextField4.getText());
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testFillUser() {
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.reset();
 User user = new User("Name1", "email1@email.com", "1234567890", "Title1", true, false, true);
 updateUserPanel.fillUser(user);
 assertEquals("Name1", updateUserPanel.jTextField1.getText());
 assertEquals("email1@email.com", updateUserPanel.jTextField2.getText());
 assertEquals("1234567890", updateUserPanel.jTextField3.getText());
 assertEquals("Title1", updateUserPanel.jTextField4.getText());
 assertTrue(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.jButton1 = mock(JButton.class);
 doNothing().when(updateUserPanel.jButton1).actionPerformed(any(ActionEvent.class));
 updateUserPanel.controlPanel = mock(ControlPanel.class);
 updateUserPanel.jButton1ActionPerformed(mock(ActionEvent.class));
 verify(updateUserPanel.controlPanel).getParentDialog().setVisible(false);
 }

 @Test
 public void testJButton2ActionPerformed() {
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 adminApp.setGroups(new Vector<Group>());
 adminApp.setGroups(Arrays.asList(new Group[3]));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(1);
 updateUserPanel.jComboBox1ItemStateChanged(null);
 updateUserPanel.jComboBox2.setModel(new DefaultComboBoxModel<User>(updateUserPanel.adminApp.getGroups().get(1).getUsers()));
 updateUserPanel.jComboBox2.setSelectedIndex(1);
 updateUserPanel.jButton2 = mock(JButton.class);
 doNothing().when(updateUserPanel.jButton2).actionPerformed(any(ActionEvent.class));
 updateUserPanel.jButton2ActionPerformed(mock(ActionEvent.class));
 verify(updateUserPanel).jComboBox1ItemStateChanged(any(ItemEvent.class));
 }

 @Test
 public void testJButton3ActionPerformed() {
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 adminApp.setGroups(new Vector<Group>());
 adminApp.setGroups(Arrays.asList(new Group[3]));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(1);
 updateUserPanel.jComboBox1ItemStateChanged(null);
 updateUserPanel.jComboBox2.setModel(new DefaultComboBoxModel<User>(updateUserPanel.adminApp.getGroups().get(1).getUsers()));
 updateUserPanel.jComboBox2.setSelectedIndex(1);
 updateUserPanel.jTextField1.setText("Name1");
 updateUserPanel.jTextField2.setText("email1@email.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("Title1");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton3 = mock(JButton.class);
 doNothing().when(updateUserPanel.jButton3).actionPerformed(any(ActionEvent.class));
 updateUserPanel.jButton3ActionPerformed(mock(ActionEvent.class));
 verify(adminApp).updateUser(updateUserPanel.jComboBox2.getSelectedItem());
 }

 @Test
 public void testJButton4ActionPerformed() {
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 adminApp.setGroups(new Vector<Group>());
 adminApp.setGroups(Arrays.asList(new Group[3]));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(1);
 updateUserPanel.jComboBox1ItemStateChanged(null);
 updateUserPanel.jComboBox2.setModel(new DefaultComboBoxModel<User>(updateUserPanel.adminApp.getGroups().get(1).getUsers()));
 updateUserPanel.jComboBox2.setSelectedIndex(1);
 updateUserPanel.jTextField1.setText("Name1");
 updateUserPanel.jTextField2.setText("email1@email.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("Title1");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4 = mock(JButton.class);
 doNothing().when(updateUserPanel.jButton4).actionPerformed(any(ActionEvent.class));
 updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));
 verify(adminApp).updateUser(updateUserPanel.jComboBox2.getSelectedItem());
 }
}