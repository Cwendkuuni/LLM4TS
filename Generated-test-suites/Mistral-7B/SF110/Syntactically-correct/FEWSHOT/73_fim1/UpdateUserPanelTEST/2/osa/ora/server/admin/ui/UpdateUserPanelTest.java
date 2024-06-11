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
 adminApp.setGroups(new ArrayList<>());
 adminApp.setGroups(Arrays.asList(new Group("Group1", new ArrayList<>()), new Group("Group2", new ArrayList<>())));
 updateUserPanel.updateModel();
 assertNotNull(updateUserPanel.jComboBox1);
 assertFalse(updateUserPanel.jComboBox1.isEnabled());
 assertFalse(updateUserPanel.jComboBox2.isEnabled());

 Group group = new Group("Group1", new ArrayList<>());
 adminApp.getGroups().set(0, group);
 updateUserPanel.updateModel();
 assertNotNull(updateUserPanel.jComboBox1);
 assertTrue(updateUserPanel.jComboBox1.isEnabled());
 assertFalse(updateUserPanel.jComboBox2.isEnabled());

 User user = new User("User1", "user1@example.com", "User1", "N/A", true, true, true, null);
 group.getUsers().add(user);
 updateUserPanel.updateModel();
 assertTrue(updateUserPanel.jComboBox2.isEnabled());
 assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
 }

 @Test
 public void testInitComponents() {
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 assertNotNull(updateUserPanel.jLabel1);
 assertNotNull(updateUserPanel.jLabel2);
 assertNotNull(updateUserPanel.jLabel3);
 assertNotNull(updateUserPanel.jCheckBox1);
 assertNotNull(updateUserPanel.jCheckBox2);
 assertNotNull(updateUserPanel.jCheckBox3);
 assertNotNull(updateUserPanel.jLabel4);
 assertNotNull(updateUserPanel.jComboBox1);
 assertNotNull(updateUserPanel.jTextField2);
 assertNotNull(updateUserPanel.jTextField3);
 assertNotNull(updateUserPanel.jButton1);
 assertNotNull(updateUserPanel.jButton2);
 assertNotNull(updateUserPanel.jComboBox2);
 assertNotNull(updateUserPanel.jButton3);
 assertNotNull(updateUserPanel.jButton4);
 assertNotNull(updateUserPanel.jLabel5);
 assertNotNull(updateUserPanel.jTextField1);
 assertNotNull(updateUserPanel.jTextField4);
 assertNotNull(updateUserPanel.jLabel6);
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 updateUserPanel.setVisible(true);
 mockStatic(JOptionPane.class);
 when(JOptionPane.showMessageDialog(updateUserPanel, anyString(), anyString(), anyInt(), any(ImageIcon.class))).thenReturn(true);
 updateUserPanel.jButton1.doClick();
 verify(updateUserPanel).reset();
 verifyStatic(JOptionPane.class).showMessageDialog(updateUserPanel, "User Updated Successfully!", "Success", 0, eq(adminApp.getSuccessIcon()));
 }

 @Test
 public void testJButton2ActionPerformed() {
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 User user = new User("User1", "user1@example.com", "User1", "N/A", true, true, true, null);
 Group group = new Group("Group1", new ArrayList<>());
 group.getUsers().add(user);
 adminApp.setGroups(new ArrayList<>());
 adminApp.setGroups(Arrays.asList(group));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1 = new JComboBox<>();
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2 = new JComboBox<>();
 updateUserPanel.jComboBox2.setSelectedItem(user);
 mockStatic(JOptionPane.class);
 when(JOptionPane.showMessageDialog(updateUserPanel, anyString(), anyString(), anyInt(), any(ImageIcon.class))).thenReturn(true);
 updateUserPanel.jButton2.doClick();
 verify(group).getUsers().remove(user);
 verify(updateUserPanel).reset();
 verifyStatic(JOptionPane.class).showMessageDialog(updateUserPanel, "User Deleted Successfully!", "Success", 0, eq(adminApp.getSuccessIcon()));
 }

 @Test
 public void testJButton3ActionPerformed() {
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 User user = new User("User1", "user1@example.com", "User1", "N/A", true, true, true, null);
 updateUserPanel.jComboBox2 = new JComboBox<>();
 updateUserPanel.jComboBox2.setSelectedItem(user);
 mockStatic(JOptionPane.class);
 when(JOptionPane.showMessageDialog(updateUserPanel, anyString(), anyString(), anyInt(), any(ImageIcon.class))).thenReturn(true);
 updateUserPanel.jButton3.doClick();
 verify(adminApp).resetUserPass(user);
 verifyStatic(JOptionPane.class).showMessageDialog(updateUserPanel, "User Password Reset Successfully!", "Success", 0, eq(adminApp.getSuccessIcon()));
 }

 @Test
 public void testJButton4ActionPerformed() {
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 Group group = new Group("Group1", new ArrayList<>());
 User user = new User("User1", "user1@example.com", "User1", "N/A", true, true, true, null);
 group.getUsers().add(user);
 adminApp.setGroups(new ArrayList<>());
 adminApp.setGroups(Arrays.asList(group));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1 = new JComboBox<>();
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2 = new JComboBox<>();
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("Software Tester");
 mockStatic(JOptionPane.class);
 when(JOptionPane.showMessageDialog(updateUserPanel, anyString(), anyString(), anyInt(), any(ImageIcon.class))).thenReturn(true);
 updateUserPanel.jButton4.doClick();
 User updatedUser = new User("User1", "new.user@example.com", "NewUserName", "1234567890", true, true, true, "Software Tester");
 verify(user).setName("NewUserName");
 verify(user).setEmail("new.user@example.com");
 verify(user).setDirectPhone("1234567890");
 verify(user).setJobTitle("Software Tester");
 verify(user).setShowMyStatus(true);
 verify(user).setOnlyStartChat(true);
 verify(user).setCanJoinChatRoom(true);
 verify(group.getUsers()).set(0, updatedUser);
 verifyStatic(JOptionPane.class).showMessageDialog(updateUserPanel, "User Updated Successfully!", "Success", 0, eq(adminApp.getSuccessIcon()));
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 AdminApp adminAppSpy = spy(adminApp);
 when(adminAppSpy.getGroups()).thenReturn(Arrays.asList(new Group("Group1", new ArrayList<>()), new Group("Group2", new ArrayList<>()), new Group("Group3", new ArrayList<>()), new Group("Group4", new ArrayList<>())));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1 = new JComboBox<>();
 updateUserPanel.jComboBox1.setSelectedItem("Group1");
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, ItemEvent.ITEM_STATE_CHANGED, 0, ItemEvent.DESELECTED, null, null));
 assertNull(updateUserPanel.jComboBox2.getSelectedItem());
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, ItemEvent.ITEM_STATE_CHANGED, 1, ItemEvent.SELECTED, null, null));
 Group group = new Group("Group2", new ArrayList<>());
 User user = new User("User1", "user1@example.com", "User1", "N/A", true, true, true, null);
 group.getUsers().add(user);
 when(adminAppSpy.getGroups().get(1)).thenReturn(group);
 updateUserPanel.jComboBox1 = new JComboBox<>(adminAppSpy.getGroups().toArray());
 updateUserPanel.jComboBox1.setSelectedIndex(1);
 updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, ItemEvent.ITEM_STATE_CHANGED, 1, ItemEvent.SELECTED, null, null));
 assertEquals(user, updateUserPanel.jComboBox2.getSelectedItem());
 }
}