package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.*;

public class UpdateUserPanelTest {

 private UpdateUserPanel updateUserPanel;
 private AdminApp adminApp;
 private ControlPanel controlPanel;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 controlPanel = new ControlPanel();
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.updateModel();
 }

 @Test
 public void testUpdateModel() {
 Group group = adminApp.getGroups().get(0);
 User user = group.getUsers().get(0);
 updateUserPanel.updateModel();
 assertEquals("Expected first item in comboBox1 to be the group", group, updateUserPanel.jComboBox1.getSelectedItem());
 assertEquals("Expected first item in comboBox2 to be the first user in the group", user, updateUserPanel.jComboBox2.getSelectedItem());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 // Simulate an item state changed event in jComboBox1
 ItemEvent evt = new ItemEvent(updateUserPanel.jComboBox1, ItemEvent.SELECTED, 0);
 updateUserPanel.jComboBox1ItemStateChanged(evt);

 // Verify that the comboBox2 model is updated accordingly
 assertEquals("Expected comboBox2 to have 0 items after the first group is selected", 0, updateUserPanel.jComboBox2.getModel().getSize());
 updateUserPanel.updateModel();
 assertEquals("Expected comboBox2 to have users in the first group after the group is selected", 1, updateUserPanel.jComboBox2.getModel().getSize());
 }

 @Test
 public void testJButton1ActionPerformed() {
 // Simulate button1 action event
 ActionEvent evt = new ActionEvent(updateUserPanel.jButton1, ActionEvent.ACTION_PERFORMED, "Cancel");
 updateUserPanel.jButton1ActionPerformed(evt);

 // Verify that the dialog is hidden and reset
 assertTrue("Dialog should be hidden after cancel button is clicked", updateUserPanel.isVisible() == false);
 updateUserPanel.reset();
 }

 @Test
 public void testJButton2ActionPerformed() {
 // Simulate button2 action event
 ActionEvent evt = new ActionEvent(updateUserPanel.jButton2, ActionEvent.ACTION_PERFORMED, "Delete");
 updateUserPanel.jButton2ActionPerformed(evt);

 // Verify that the user is deleted from the group and comboBox2
 Group group = adminApp.getGroups().get(0);
 User user = group.getUsers().get(0);
 assertEquals("Expected user to still be in the group before delete button is clicked", user, group.getUsers().get(0));
 updateUserPanel.jButton2ActionPerformed(evt);
 assertNull("Expected user to be null after delete button is clicked", group.getUsers().get(0));
 assertEquals("Expected comboBox2 to have 0 items after the user is deleted", 0, updateUserPanel.jComboBox2.getModel().getSize());
 }

 @Test
 public void testJButton3ActionPerformed() {
 // Simulate button3 action event
 ActionEvent evt = new ActionEvent(updateUserPanel.jButton3, ActionEvent.ACTION_PERFORMED, "Reset Password");
 updateUserPanel.jButton3ActionPerformed(evt);

 // Verify that a message dialog is displayed with a success message
 assertEquals("Expected a message dialog to be displayed after reset password button is clicked", JOptionPane.showMessageDialog(updateUserPanel, "User Password Reset Successfully!", "Success", JOptionPane.PLAIN_MESSAGE, null), JOptionPane.showMessageDialog(updateUserPanel, (Object)anyString(), (String)anyString(), JOptionPane.PLAIN_MESSAGE, null));
 }

 @Test
 public void testJButton4ActionPerformed() {
 // Simulate button4 action event
 ActionEvent evt = new ActionEvent(updateUserPanel.jButton4, ActionEvent.ACTION_PERFORMED, "Update");
 updateUserPanel.jButton4ActionPerformed(evt);

 // Verify that the user is updated in the group and comboBox2
 User user = new User("newName", "newEmail@example.com", "1234567890", "newTitle", true, true, true, null, null, null);
 Group group = adminApp.getGroups().get(0);
 adminApp.addUser(user);
 group.getUsers().add(user);
 updateUserPanel.jTextField1.setText("newName");
 updateUserPanel.jTextField2.setText("newEmail@example.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("newTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(evt);
 User updatedUser = group.getUsers().get(0);
 assertEquals("Expected the user to have been updated", "newName", updatedUser.getName());
 assertEquals("Expected the user to have been updated", "newEmail@example.com", updatedUser.getEmail());
 assertEquals("Expected the user to have been updated", "1234567890", updatedUser.getDirectPhone());
 assertEquals("Expected the user to have been updated", "newTitle", updatedUser.getJobTitle());
 assertEquals("Expected the updated user to still be in the comboBox2", updatedUser, updateUserPanel.jComboBox2.getSelectedItem());
 }
}