package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

 private UpdateUserPanel updateUserPanel;
 private AdminApp adminAppMock;

 @Before
 public void setUp() {
 adminAppMock = mock(AdminApp.class);
 updateUserPanel = new UpdateUserPanel(adminAppMock, null);
 }

 @Test
 public void testUpdateModel() {
 // Given
 Group groupMock = mock(Group.class);
 when(adminAppMock.getGroups()).thenReturn(Arrays.asList(groupMock));
 when(groupMock.getUsers()).thenReturn(Arrays.asList(new User("John Doe", "john.doe@example.com", "555-123-4567", "Software Engineer", true, true, true), new User("Jane Doe", "jane.doe@example.com", "555-987-6543", "Project Manager", false, false, false)));

 // When
 updateUserPanel.updateModel();

 // Then
 assertEquals(2, updateUserPanel.jComboBox1.getItemCount());
 assertEquals("Item 1", updateUserPanel.jComboBox1.getItemAt(0));
 assertEquals("Item 2", updateUserPanel.jComboBox1.getItemAt(1));
 assertEquals("John Doe", updateUserPanel.jComboBox2.getItemAt(0).getName());
 assertEquals("john.doe@example.com", updateUserPanel.jComboBox2.getItemAt(0).getEmail());
 assertEquals("555-123-4567", updateUserPanel.jComboBox2.getItemAt(0).getDirectPhone());
 assertEquals("Software Engineer", updateUserPanel.jComboBox2.getItemAt(0).getJobTitle());
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 // Given
 when(adminAppMock.getGroups()).thenReturn(Arrays.asList(groupMock));
 when(groupMock.getUsers()).thenReturn(Arrays.asList(new User("John Doe", "john.doe@example.com", "555-123-4567", "Software Engineer", true, true, true), new User("Jane Doe", "jane.doe@example.com", "555-987-6543", "Project Manager", false, false, false)));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(1);

 // When
 updateUserPanel.jComboBox1ItemStateChanged(null);

 // Then
 assertEquals("Item 2", updateUserPanel.jComboBox2.getItemAt(0).getName());
 assertEquals("jane.doe@example.com", updateUserPanel.jComboBox2.getItemAt(0).getEmail());
 assertEquals("555-987-6543", updateUserPanel.jComboBox2.getItemAt(0).getDirectPhone());
 assertEquals("Project Manager", updateUserPanel.jComboBox2.getItemAt(0).getJobTitle());
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testJButton1ActionPerformed() {
 // Given
 updateUserPanel.controlPanel = mock(ControlPanel.class);
 doNothing().when(updateUserPanel.controlPanel).getParentDialog().setVisible(false);

 // When
 updateUserPanel.jButton1.doClick();

 // Then
 verify(updateUserPanel.controlPanel).getParentDialog().setVisible(false);
 assertTrue(updateUserPanel.jTextField1.getText().isEmpty());
 assertTrue(updateUserPanel.jTextField2.getText().isEmpty());
 assertTrue(updateUserPanel.jTextField3.getText().isEmpty());
 assertTrue(updateUserPanel.jTextField4.getText().isEmpty());
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testJButton2ActionPerformed() {
 // Given
 User userMock = mock(User.class);
 when(adminAppMock.getGroups()).thenReturn(Arrays.asList(new Group("Group 1", Arrays.asList(userMock))));
 when(userMock.getId()).thenReturn(1);
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);

 // When
 updateUserPanel.jButton2.doClick();

 // Then
 verify(adminAppMock).delUser(userMock);
 verify(updateUserPanel.jComboBox1).setSelectedIndex(0);
 verify(updateUserPanel.jComboBox1).validate();
 verify(updateUserPanel.jComboBox2).setModel(new DefaultComboBoxModel<User>(updateUserPanel.adminApp.getGroups().get(0).getUsers()));
 }

 @Test
 public void testJButton3ActionPerformed() {
 // Given
 User userMock = mock(User.class);
 when(adminAppMock.getGroups()).thenReturn(Arrays.asList(new Group("Group 1", Arrays.asList(userMock))));
 when(userMock.getId()).thenReturn(1);
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("555-456-7890");
 updateUserPanel.jTextField4.setText("Software Tester");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);

 // When
 updateUserPanel.jButton3.doClick();

 // Then
 verify(adminAppMock).resetUserPass(userMock);
 verify(updateUserPanel.jTextField1).setText("");
 verify(updateUserPanel.jTextField2).setText("");
 verify(updateUserPanel.jTextField3).setText("");
 verify(updateUserPanel.jTextField4).setText("");
 verify(updateUserPanel.jCheckBox1).setSelected(false);
 verify(updateUserPanel.jCheckBox2).setSelected(false);
 verify(updateUserPanel.jCheckBox3).setSelected(false);
 }

 @Test
 public void testJButton4ActionPerformed() {
 // Given
 User userMock = mock(User.class);
 when(adminAppMock.getGroups()).thenReturn(Arrays.asList(new Group("Group 1", Arrays.asList(userMock))));
 when(userMock.getId()).thenReturn(1);
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("555-456-7890");
 updateUserPanel.jTextField4.setText("Software Tester");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);

 // When
 updateUserPanel.jButton4.doClick();

 // Then
 verify(adminAppMock).updateUser(userMock);
 verify(updateUserPanel.jComboBox2).setSelectedIndex(0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformedWithEmptyName() {
 // Given
 User userMock = mock(User.class);
 when(adminAppMock.getGroups()).thenReturn(Arrays.asList(new Group("Group 1", Arrays.asList(userMock))));
 when(userMock.getId()).thenReturn(1);
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("555-456-7890");
 updateUserPanel.jTextField4.setText("Software Tester");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);

 // When and Then
 try {
 updateUserPanel.jButton4.doClick();
 fail("Should throw exception with empty name");
 }
 catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformedWithEmptyEmail() {
 // Given
 User userMock = mock(User.class);
 when(adminAppMock.getGroups()).thenReturn(Arrays.asList(new Group("Group 1", Arrays.asList(userMock))));
 when(userMock.getId()).thenReturn(1);
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("");
 updateUserPanel.jTextField3.setText("555-456-7890");
 updateUserPanel.jTextField4.setText("Software Tester");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);

 // When and Then
 try {
 updateUserPanel.jButton4.doClick();
 fail("Should throw exception with empty email");
 }
 catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformedWithEmailAlreadyExists() {
 // Given
 User userMock1 = mock(User.class);
 User userMock2 = mock(User.class);
 when(adminAppMock.getGroups()).thenReturn(Arrays.asList(new Group("Group 1", Arrays.asList(userMock1, userMock2))));
 when(userMock1.getId()).thenReturn(1);
 when(userMock2.getId()).thenReturn(2);
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("555-456-7890");
 updateUserPanel.jTextField4.setText("Software Tester");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);

 // When and Then
 try {
 updateUserPanel.jButton4.doClick();
 fail("Should throw exception with email already exists");
 }
 catch (IllegalArgumentException e) {
 // Pass
 }
 }
}