package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

 private UpdateUserPanel updateUserPanel;
 private AdminApp adminApp;
 private Group group;
 private User user;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 group = new Group();
 user = new User();
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 @Test
 public void testUpdateModel() {
 adminApp.setGroups(new ArrayList<Group>() {{
 add(group);
 }});
 group.setName("TestGroup");
 group.setUsers(new ArrayList<User>() {{
 add(user);
 }});
 user.setName("TestUser");
 updateUserPanel.updateModel();
 assertEquals(group, updateUserPanel.getAdminApp());
 assertNotNull(updateUserPanel.jComboBox1);
 assertNotNull(updateUserPanel.jComboBox2);
 }

 @Test
 public void testInitComponents() {
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
 updateUserPanel.controlPanel = new ControlPanel() {
 @Override
 public JDialog getParentDialog() {
 return new JDialog() {
 @Override
 public void setVisible(boolean b) {
 }
 };
 }
 };
 updateUserPanel.jButton1.doClick();
 assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible() == false);
 }

 @Test
 public void testJButton2ActionPerformed() {
 when(adminApp.delUser(user)).thenReturn(null);
 updateUserPanel.jComboBox1 = new JComboBox<>();
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2 = new JComboBox<>();
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jButton2.doClick();
 verify(adminApp).delUser(user);
 }

 @Test(expected = AssertionError.class)
 public void testJButton2ActionPerformedWithNullUser() {
 updateUserPanel.jComboBox1 = new JComboBox<>();
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2 = new JComboBox<>();
 updateUserPanel.jButton2.doClick();
 fail("Expected User object but got null");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJButton2ActionPerformedWithEmptyGroup() {
 updateUserPanel.jComboBox1 = new JComboBox<>();
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2 = new JComboBox<>();
 group.setUsers(new ArrayList<>());
 updateUserPanel.jButton2.doClick();
 fail("Expected IndexOutOfBoundsException but got null");
 }

 @Test
 public void testJButton3ActionPerformed() {
 when(adminApp.resetUserPass(user)).thenReturn(user);
 updateUserPanel.jComboBox1 = new JComboBox<>();
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2 = new JComboBox<>();
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("TestUserName");
 updateUserPanel.jTextField2.setText("TestEmail@test.com");
 updateUserPanel.jButton3.doClick();
 verify(adminApp).resetUserPass(user);
 assertEquals("TestUserName", user.getName());
 assertEquals("TestEmail@test.com", user.getEmail());
 }

 @Test(expected = AssertionError.class)
 public void testJButton3ActionPerformedWithNullUser() {
 updateUserPanel.jComboBox1 = new JComboBox<>();
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2 = new JComboBox<>();
 updateUserPanel.jButton3.doClick();
 fail("Expected User object but got null");
 }

 @Test
 public void testJButton4ActionPerformed() {
 when(adminApp.updateUser(user)).thenReturn(user);
 updateUserPanel.jComboBox1 = new JComboBox<>();
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2 = new JComboBox<>();
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("TestUserName");
 updateUserPanel.jTextField2.setText("TestEmail@test.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("TestJobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 verify(adminApp).updateUser(user);
 assertEquals("TestUserName", user.getName());
 assertEquals("TestEmail@test.com", user.getEmail());
 assertEquals("1234567890", user.getDirectPhone());
 assertEquals("TestJobTitle", user.getJobTitle());
 assertEquals(true, user.isShowMyStatus());
 assertEquals(true, user.isOnlyStartChat());
 assertEquals(true, user.isCanJoinChatRoom());
 }

 @Test(expected = AssertionError.class)
 public void testJButton4ActionPerformedWithNullUser() {
 updateUserPanel.jComboBox1 = new JComboBox<>();
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2 = new JComboBox<>();
 updateUserPanel.jTextField1.setText("TestUserName");
 updateUserPanel.jTextField2.setText("TestEmail@test.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("TestJobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();
 fail("Expected User object but got null");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJButton4ActionPerformedWithEmptyUserList() {
 updateUserPanel.jComboBox1 = new JComboBox<>();
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2 = new JComboBox<>();
 updateUserPanel.jTextField1.setText("TestUserName");
 updateUserPanel.jTextField2.setText("TestEmail@test.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("TestJobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 group.setUsers(new ArrayList<>());
 updateUserPanel.jButton4.doClick();
 fail("Expected IndexOutOfBoundsException but got null");
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 when(adminApp.getGroups().get(0).getUsers().get(0).getName()).thenReturn("TestUserName");
 when(adminApp.getGroups().get(0).getUsers().get(0).getEmail()).thenReturn("TestEmail@test.com");
 updateUserPanel.jComboBox1 = new JComboBox<>();
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2 = new JComboBox<>();
 updateUserPanel.jComboBox1.addItemListener(mock(ItemListener.class));
 updateUserPanel.jComboBox2.addItemListener(mock(ItemListener.class));
 updateUserPanel.updateModel();
 verify(updateUserPanel.jComboBox1).setModel(isA(DefaultComboBoxModel.class));
 verifyZeroInteractions(updateUserPanel.jComboBox2);
 }

 @Test
 public void testJComboBox1ItemStateChangedWithEmptyUserList() {
 updateUserPanel.jComboBox1 = new JComboBox<>();
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox1.addItemListener(mock(ItemListener.class));
 updateUserPanel.updateModel();
 verifyZeroInteractions(updateUserPanel.jComboBox2);
 }

 @Test
 public void testJComboBox2ItemStateChanged() {
 when(adminApp.getGroups().get(0).getUsers().get(0).getName()).thenReturn("TestUserName");
 updateUserPanel.jComboBox1 = new JComboBox<>();
 updateUserPanel.jComboBox1.setSelectedItem(group);
 updateUserPanel.jComboBox2 = new JComboBox<>();
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jComboBox2.addItemListener(mock(ItemListener.class));
 updateUserPanel.updateModel();
 verifyZeroInteractions(updateUserPanel.jComboBox1);
 }

}