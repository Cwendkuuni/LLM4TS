import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import static org.mockito.Mockito.*;

public class UpdateUserPanelTest {
 private UpdateUserPanel updateUserPanel;
 private AdminApp adminAppMock;
 private ControlPanel controlPanelMock;

 @Before
 public void setup() {
 adminAppMock = mock(AdminApp.class);
 controlPanelMock = mock(ControlPanel.class);
 updateUserPanel = new UpdateUserPanel(adminAppMock, controlPanelMock);
 }

 @Test
 public void testUpdateModel() {
 Group groupMock = mock(Group.class);
 when(adminAppMock.getGroups()).thenReturn(Arrays.asList(groupMock));
 User userMock = mock(User.class);
 when(groupMock.getUsers()).thenReturn(Arrays.asList(userMock));
 when(userMock.getName()).thenReturn("Test User");
 when(userMock.getEmail()).thenReturn("test@email.com");
 when(userMock.getDirectPhone()).thenReturn("123-456-7890");
 when(userMock.isShowMyStatus()).thenReturn(true);
 when(userMock.isOnlyStartChat()).thenReturn(false);
 when(userMock.isCanJoinChatRoom()).thenReturn(true);

 updateUserPanel.updateModel();

 assertEquals("Test User", updateUserPanel.jTextField1.getText());
 assertEquals("test@email.com", updateUserPanel.jTextField2.getText());
 assertEquals("123-456-7890", updateUserPanel.jTextField3.getText());
 assertTrue(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertTrue(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test(expected = NullPointerException.class)
 public void testUpdateModelNullGroups() {
 when(adminAppMock.getGroups()).thenReturn(null);
 updateUserPanel.updateModel();
 }

 @Test(expected = NullPointerException.class)
 public void testUpdateModelNullUsers() {
 Group groupMock = mock(Group.class);
 when(adminAppMock.getGroups()).thenReturn(Arrays.asList(groupMock));
 when(groupMock.getUsers()).thenReturn(null);
 updateUserPanel.updateModel();
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1ActionPerformed(mock(ActionEvent.class));
 verify(controlPanelMock).getParentDialog().setVisible(false);
 updateUserPanel.reset();
 }

 @Test
 public void testJButton2ActionPerformedNoUserSelected() {
 updateUserPanel.jButton2ActionPerformed(mock(ActionEvent.class));
 verify(adminAppMock, never()).delUser(any(User.class));
 }

 @Test
 public void testJButton2ActionPerformedUserDeleted() {
 Group groupMock = mock(Group.class);
 User userMock = mock(User.class);
 when(adminAppMock.getGroups()).thenReturn(Arrays.asList(groupMock));
 when(groupMock.getUsers()).thenReturn(Arrays.asList(userMock));
 when(userMock.getId()).thenReturn(1L);
 when(userMock.getName()).thenReturn("Test User");

 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton2ActionPerformed(mock(ActionEvent.class));

 verify(adminAppMock).delUser(userMock);
 verify(groupMock).getUsers().remove(0);
 }

 @Test(expected = NullPointerException.class)
 public void testJButton2ActionPerformedNullUser() {
 updateUserPanel.jButton2ActionPerformed(mock(ActionEvent.class));
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 }

 @Test
 public void testJButton3ActionPerformed() {
 updateUserPanel.jButton3ActionPerformed(mock(ActionEvent.class));
 verify(adminAppMock, times(1)).resetUserPass(any(User.class));
 }

 @Test(expected = NullPointerException.class)
 public void testJButton3ActionPerformedNullUserSelected() {
 updateUserPanel.jButton3ActionPerformed(mock(ActionEvent.class));
 }

 @Test
 public void testJButton4ActionPerformed() {
 String newName = "New Name";
 String newEmail = "newemail@email.com";
 String newDirectPhone = "123-456-7890";
 String newJobTitle = "New Job Title";
 updateUserPanel.jTextField1.setText(newName);
 updateUserPanel.jTextField2.setText(newEmail);
 updateUserPanel.jTextField3.setText(newDirectPhone);
 updateUserPanel.jTextField4.setText(newJobTitle);
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));

 User userMock = mock(User.class);
 when(userMock.getId()).thenReturn(1L);
 when(userMock.getName()).thenReturn("Test User");
 when(userMock.getEmail()).thenReturn("test@email.com");
 when(userMock.getDirectPhone()).thenReturn("123-456-7890");
 when(userMock.getJobTitle()).thenReturn("Job Title");
 when(userMock.isShowMyStatus()).thenReturn(true);
 when(userMock.isOnlyStartChat()).thenReturn(false);
 when(userMock.isCanJoinChatRoom()).thenReturn(true);

 Group groupMock = mock(Group.class);
 when(adminAppMock.getGroups()).thenReturn(Arrays.asList(groupMock));
 when(groupMock.getUsers()).thenReturn(Arrays.asList(userMock));

 verify(adminAppMock).updateUser(userMock);
 verify(groupMock.getUsers()).set(0, userMock);
 }

 @Test
 public void testJButton4ActionPerformedSameEmailUserExists() {
 Group groupMock = mock(Group.class);
 User userMock1 = mock(User.class);
 User userMock2 = mock(User.class);
 when(adminAppMock.getGroups()).thenReturn(Arrays.asList(groupMock));
 when(groupMock.getUsers()).thenReturn(Arrays.asList(userMock1, userMock2));
 when(userMock1.getId()).thenReturn(1L);
 when(userMock1.getName()).thenReturn("Test User 1");
 when(userMock1.getEmail()).thenReturn("test@email.com");
 when(userMock1.getDirectPhone()).thenReturn("123-456-7890");
 when(userMock1.getJobTitle()).thenReturn("Job Title 1");
 when(userMock1.isShowMyStatus()).thenReturn(true);
 when(userMock1.isOnlyStartChat()).thenReturn(false);
 when(userMock1.isCanJoinChatRoom()).thenReturn(true);
 when(userMock2.getId()).thenReturn(2L);
 when(userMock2.getName()).thenReturn("Test User 2");
 when(userMock2.getEmail()).thenReturn("test@email.com");
 when(userMock2.getDirectPhone()).thenReturn("456-789-1234");
 when(userMock2.getJobTitle()).thenReturn("Job Title 2");
 when(userMock2.isShowMyStatus()).thenReturn(false);
 when(userMock2.isOnlyStartChat()).thenReturn(true);
 when(userMock2.isCanJoinChatRoom()).thenReturn(false);

 updateUserPanel.updateModel();
 updateUserPanel.jTextField1.setText("New Name");
 updateUserPanel.jTextField2.setText("test@email.com");
 updateUserPanel.jTextField3.setText("123-456-7890");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));

 verify(adminAppMock, never()).updateUser(userMock1);
 }
}