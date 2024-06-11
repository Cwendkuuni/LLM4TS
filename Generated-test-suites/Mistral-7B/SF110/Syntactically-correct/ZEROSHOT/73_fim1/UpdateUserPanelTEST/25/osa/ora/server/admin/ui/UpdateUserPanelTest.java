package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

 private UpdateUserPanel updateUserPanel;
 private AdminApp adminApp;
 private ControlPanel controlPanel;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 controlPanel = new ControlPanel();
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 }

 @Test
 public void testUpdateModel() {
 Group group = new Group("TestGroup", new ArrayList<>());
 adminApp.setGroups(new ArrayList<>());
 adminApp.getGroups().add(group);
 User user = new User("TestUser", "test@email.com", "", "", "", "", false, false, false, null);
 group.getUsers().add(user);

 updateUserPanel.updateModel();

 assertEquals(group, updateUserPanel.adminApp);
 assertNotNull(updateUserPanel.jComboBox1);
 assertNotNull(updateUserPanel.jComboBox2);

 assertEquals(group.getUsers().get(0), updateUserPanel.jComboBox2.getSelectedItem());
 }

 @Test
 public void testInitComponents() {
 // Testing if the components are added to the panel correctly
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
 updateUserPanel.jButton1ActionPerformed(null);
 assertTrue(controlPanel.getParentDialog().isVisible());
 }

 @Test
 public void testJButton2ActionPerformed() {
 Group group = new Group("TestGroup", new ArrayList<>());
 adminApp.setGroups(new ArrayList<>());
 adminApp.getGroups().add(group);
 User user = new User("TestUser", "test@email.com", "", "", "", "", false, false, false, null);
 group.getUsers().add(user);
 updateUserPanel.jComboBox1 = new JComboBox<>();
 updateUserPanel.jComboBox1.setModel(new DefaultComboBoxModel<>(new String[] { "TestGroup" }));
 updateUserPanel.jComboBox2 = new JComboBox<>();
 updateUserPanel.jComboBox2.setModel(new DefaultComboBoxModel<>(group.getUsers().toArray()));
 updateUserPanel.jComboBox2.setSelectedItem(user);

 updateUserPanel.jButton2ActionPerformed(null);

 assertTrue(group.getUsers().remove(user));
 assertEquals(0, group.getUsers().size());
 }

 @Test
 public void testJButton3ActionPerformed() {
 Group group = new Group("TestGroup", new ArrayList<>());
 adminApp.setGroups(new ArrayList<>());
 adminApp.getGroups().add(group);
 User user = new User("TestUser", "test@email.com", "", "", "", "", false, false, false, null);
 group.getUsers().add(user);
 updateUserPanel.jComboBox1 = new JComboBox<>();
 updateUserPanel.jComboBox1.setModel(new DefaultComboBoxModel<>(new String[] { "TestGroup" }));
 updateUserPanel.jComboBox2 = new JComboBox<>();
 updateUserPanel.jComboBox2.setModel(new DefaultComboBoxModel<>(group.getUsers().toArray()));
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1 = new JTextField();
 updateUserPanel.jTextField2 = new JTextField();
 updateUserPanel.jTextField3 = new JTextField();
 updateUserPanel.jTextField4 = new JTextField();
 updateUserPanel.fillUser(user);

 updateUserPanel.jButton3ActionPerformed(null);

 assertEquals("NewUserName", user.getName());
 assertEquals("NewEmail@email.com", user.getEmail());
 assertEquals("NewPhone", user.getDirectPhone());
 assertEquals("NewJobTitle", user.getJobTitle());
 assertTrue(user.isShowMyStatus());
 assertTrue(user.isOnlyStartChat());
 assertTrue(user.isCanJoinChatRoom());
 }

 @Test
 public void testJButton4ActionPerformed() {
 Group group = new Group("TestGroup", new ArrayList<>());
 adminApp.setGroups(new ArrayList<>());
 adminApp.getGroups().add(group);
 User user = new User("TestUser", "test@email.com", "", "", "", "", false, false, false, null);
 group.getUsers().add(user);
 updateUserPanel.jComboBox1 = new JComboBox<>();
 updateUserPanel.jComboBox1.setModel(new DefaultComboBoxModel<>(new String[] { "TestGroup" }));
 updateUserPanel.jComboBox2 = new JComboBox<>();
 updateUserPanel.jComboBox2.setModel(new DefaultComboBoxModel<>(group.getUsers().toArray()));
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1 = new JTextField();
 updatePanel.jTextField2 = new JTextField();
 updatePanel.jTextField3 = new JTextField();
 updatePanel.jTextField4 = new JTextField();
 updatePanel.jCheckBox1 = new JCheckBox();
 updatePanel.jCheckBox2 = new JCheckBox();
 updatePanel.jCheckBox3 = new JCheckBox();
 updatePanel.jLabel5 = new JLabel();
 updatePanel.jTextField1.setText("NewUserName");
 updatePanel.jTextField2.setText("NewEmail@email.com");
 updatePanel.jTextField3.setText("NewPhone");
 updatePanel.jTextField4.setText("NewJobTitle");
 updatePanel.jCheckBox1.setSelected(true);
 updatePanel.jCheckBox2.setSelected(true);
 updatePanel.jCheckBox3.setSelected(true);
 updatePanel.jLabel5.setText("Title");
 updatePanel.jButton4ActionPerformed(null);

 User newUser = new User("NewUserName", "NewEmail@email.com", "NewPhone", null, null, null, true, true, true, null);
 assertEquals(newUser, user);
 }
}