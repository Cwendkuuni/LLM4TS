package osa.ora.server.admin.ui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private UpdateUserPanel updateUserPanel;
 private Group group;
 private User user;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 group = new Group("Group1");
 user = new User("User1", "user1@email.com", "1234567890", "Job Title", true, true, true);
 group.addUser(user);
 adminApp.setGroups(Collections.singletonList(group));
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 @Test
 public void testUpdateModel() {
 updateUserPanel.updateModel();
 assertEquals("Group1", updateUserPanel.jComboBox1.getSelectedItem().toString());
 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test
 public void testInitComponents() {
 // Check layout and bounds of components
 // ...

 // Check initial values of components
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
 assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible());
 assertEquals("", updateUserPanel.jTextField1.getText());
 assertEquals("", updateUserPanel.jTextField2.getText());
 assertEquals("", updateUserPanel.jTextField3.getText());
 assertEquals("", updateUserPanel.jTextField4.getText());
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testJButton2ActionPerformed() {
 updateUserPanel.jComboBox1.setSelectedItem("Group1");
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jButton2.doClick();
 assertEquals(0, group.getUsers().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton2ActionPerformedWithNullUser() {
 updateUserPanel.jComboBox1.setSelectedItem("Group1");
 updateUserPanel.jComboBox2.setSelectedItem(null);
 updateUserPanel.jButton2.doClick();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJButton2ActionPerformedWithInvalidIndex() {
 updateUserPanel.jComboBox1.setSelectedItem("Group1");
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jButton2.doClick();
 }

 @Test
 public void testJButton3ActionPerformed() {
 updateUserPanel.jComboBox1.setSelectedItem("Group1");
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@email.com");
 updateUserPanel.jButton3.doClick();
 assertEquals("NewUserName", user.getName());
 assertEquals("new.user@email.com", user.getEmail());
 }

 @Test
 public void testJButton4ActionPerformed() {
 updateUserPanel.jComboBox1.setSelectedItem("Group1");
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@email.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("NewJobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton4.doClick();
 assertEquals("NewUserName", user.getName());
 assertEquals("new.user@email.com", user.getEmail());
 assertEquals("1234567891", user.getDirectPhone());
 assertEquals("NewJobTitle", user.getJobTitle());
 assertTrue(user.isShowMyStatus());
 assertTrue(user.isOnlyStartChat());
 assertFalse(user.isCanJoinChatRoom());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformedWithNullUser() {
 updateUserPanel.jComboBox1.setSelectedItem("Group1");
 updateUserPanel.jComboBox2.setSelectedItem(null);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@email.com");
 updateUserPanel.jTextField3.setText("1234567891");
 updateUserPanel.jTextField4.setText("NewJobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton4.doClick();
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 updateUserPanel.jComboBox1.setSelectedItem("Group1");
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jComboBox1ItemStateChanged(null);
 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().toString());
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
 public void testFillUser() {
 updateUserPanel.fillUser(user);
 assertEquals("User1", updateUserPanel.jTextField1.getText());
 assertEquals("user1@email.com", updateUserPanel.jTextField2.getText());
 assertEquals("1234567890", updateUserPanel.jTextField3.getText());
 assertEquals("Job Title", updateUserPanel.jTextField4.getText());
 assertTrue(updateUserPanel.jCheckBox1.isSelected());
 assertTrue(updateUserPanel.jCheckBox2.isSelected());
 assertTrue(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testJComboBox2ItemStateChanged() {
 updateUserPanel.jComboBox1.setSelectedItem("Group1");
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jComboBox2ItemStateChanged(null);
 assertEquals("User1", updateUserPanel.jTextField1.getText());
 assertEquals("user1@email.com", updateUserPanel.jTextField2.getText());
 assertEquals("1234567890", updateUserPanel.jTextField3.getText());
 assertEquals("Job Title", updateUserPanel.jTextField4.getText());
 assertTrue(updateUserPanel.jCheckBox1.isSelected());
 assertTrue(updateUserPanel.jCheckBox2.isSelected());
 assertTrue(updateUserPanel.jCheckBox3.isSelected());
 }
}