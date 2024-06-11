package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.Vector;

public class UpdateUserPanelTest {

 private UpdateUserPanel updateUserPanel;
 private AdminApp adminApp;
 private JComboBox<String> jComboBox1;
 private JComboBox<User> jComboBox2;
 private JTextField jTextField1;
 private JTextField jTextField2;
 private JTextField jTextField3;
 private JTextField jTextField4;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 jComboBox1 = updateUserPanel.jComboBox1;
 jComboBox2 = updateUserPanel.jComboBox2;
 jTextField1 = updateUserPanel.jTextField1;
 jTextField2 = updateUserPanel.jTextField2;
 jTextField3 = updateUserPanel.jTextField3;
 jTextField4 = updateUserPanel.jTextField4;
 }

 @Test
 public void testUpdateModel() {
 Group group = new Group("Group1");
 adminApp.setGroups(Arrays.asList(group));
 updateUserPanel.updateModel();
 assertEquals("Group1", jComboBox1.getSelectedItem());
 assertEquals(0, jComboBox2.getItemCount());

 Group group1 = new Group("Group2");
 group.getUsers().add(new User("User1", "user1@example.com", "1234567890", "Job Title 1", true, true, true));
 group.getUsers().add(new User("User2", "user2@example.com", "2345678901", "Job Title 2", false, false, false));
 adminApp.setGroups(Arrays.asList(group, group1));
 updateUserPanel.updateModel();
 assertEquals("Group1", jComboBox1.getSelectedItem());
 assertEquals(2, jComboBox2.getItemCount());

 User user1 = group.getUsers().get(0);
 User user2 = group.getUsers().get(1);
 updateUserPanel.updateModel();
 assertEquals(user1, jComboBox2.getItemAt(0));
 assertEquals(user2, jComboBox2.getItemAt(1));
 }

 @Test
 public void testFillUser() {
 User user = new User("User3", "user3@example.com", "3456789012", "Job Title 3", true, true, true);
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.fillUser(user);
 assertEquals("User3", jTextField1.getText());
 assertEquals("user3@example.com", jTextField2.getText());
 assertEquals("3456789012", jTextField3.getText());
 assertEquals("Job Title 3", jTextField4.getText());
 assertTrue(jCheckBox1.isSelected());
 assertTrue(jCheckBox2.isSelected());
 assertTrue(jCheckBox3.isSelected());
 }

 @Test
 public void testReset() {
 updateUserPanel.reset();
 assertNull(jTextField1.getText());
 assertNull(jTextField2.getText());
 assertNull(jTextField3.getText());
 assertNull(jTextField4.getText());
 assertFalse(jCheckBox1.isSelected());
 assertFalse(jCheckBox2.isSelected());
 assertFalse(jCheckBox3.isSelected());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 Group group = new Group("Group1");
 group.setName("Group1");
 adminApp.setGroups(Arrays.asList(group));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(jComboBox1, ItemEvent.ITEM_STATE_CHANGED, 0, ItemEvent.DESELECTED));
 assertEquals(0, jComboBox2.getItemCount());
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(jComboBox1, ItemEvent.ITEM_STATE_CHANGED, 0, ItemEvent.SELECTED));
 assertEquals(2, jComboBox2.getItemCount());
 }
}