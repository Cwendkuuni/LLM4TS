import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentsMatchers;
import org.mockito.Mockito;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import static org.junit.Assert.*;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private UpdateUserPanel updateUserPanel;

 @Before
 public void setUp() {
 adminApp = Mockito.mock(AdminApp.class);
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 @Test
 public void testUpdateModel_whenAdminAppHasGroups_thenUpdateModelSetsComboBox1ModelToAdminAppGroups() {
 Mockito.when(adminApp.getGroups()).thenReturn(new ArrayList<Group>() {{
 add(new Group("Group1", null, null));
 add(new Group("Group2", null, null));
 }});

 updateUserPanel.updateModel();

 assertEquals(2, updateUserPanel.jComboBox1.getItemCount());
 assertEquals("Group1", updateUserPanel.jComboBox1.getItemAt(0).toString());
 assertEquals("Group2", updateUserPanel.jComboBox1.getItemAt(1).toString());
 }

 @Test
 public void testUpdateModel_whenAdminAppHasNoGroups_thenUpdateModelSetsComboBox1ModelToEmptyList() {
 Mockito.when(adminApp.getGroups()).thenReturn(new ArrayList<Group>());

 updateUserPanel.updateModel();

 assertEquals(0, updateUserPanel.jComboBox1.getItemCount());
 }

 @Test
 public void testUpdateModel_whenAdminAppHasGroupsAndUserForFirstGroup_thenUpdateModelSetsComboBox2ModelToUsersForFirstGroup() {
 Group group = new Group("Group1", null, null);
 User user = new User("User1", "user1@example.com", null, null, null, null, null, null, null, null, null, null, null, null, null);
 group.addUser(user);
 Mockito.when(adminApp.getGroups()).thenReturn(new ArrayList<Group>() {{
 add(group);
 }});

 updateUserPanel.updateModel();

 assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
 assertEquals("User1", updateUserPanel.jComboBox2.getItemAt(0).toString());
 }

 @Test
 public void testUpdateModel_whenAdminAppHasNoUsersForFirstGroup_thenUpdateModelSetsComboBox2ModelToEmptyList() {
 Group group = new Group("Group1", null, null);
 Mockito.when(adminApp.getGroups()).thenReturn(new ArrayList<Group>() {{
 add(group);
 }});

 updateUserPanel.updateModel();

 assertEquals(0, updateUserPanel.jComboBox2.getItemCount());
 }

 @Test
 public void testJButton1ActionPerformed_whenCancelButtonClicked_thenControlPanelDialogIsHidden() {
 updateUserPanel.jButton1ActionPerformed(Mockito.mock(ActionEvent.class));
 Mockito.verify(updateUserPanel.controlPanel, Mockito.times(1)).getParentDialog().setVisible(false);
 }

 @Test
 public void testJButton2ActionPerformed_whenDeleteButtonClicked_thenUserIsDeletedAndUserListIsUpdated() {
 Group group = new Group("Group1", null, null);
 User user = new User("User1", "user1@example.com", null, null, null, null, null, null, null, null, null, null, null, null, null);
 group.addUser(user);
 Mockito.when(adminApp.getGroups()).thenReturn(new ArrayList<Group>() {{
 add(group);
 }});

 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1 = Mockito.spy(updateUserPanel.jComboBox1);
 updateUserPanel.jComboBox2 = Mockito.spy(updateUserPanel.jComboBox2);
 updateUserPanel.jButton2ActionPerformed(Mockito.mock(ActionEvent.class));

 Mockito.verify(adminApp, Mockito.times(1)).delUser(user);
 Mockito.verify(updateUserPanel.jComboBox1, Mockito.times(1)).setSelectedIndex(0);
 Mockito.verify(updateUserPanel.jComboBox1, Mockito.times(1)).validate();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton2ActionPerformed_whenNoUserSelected_thenThrowsException() {
 updateUserPanel.jButton2ActionPerformed(Mockito.mock(ActionEvent.class));
 }

 @Test
 public void testJButton3ActionPerformed_whenResetPasswordButtonClicked_thenUserPasswordIsReset() {
 Group group = new Group("Group1", null, null);
 User user = new User("User1", "user1@example.com", null, null, null, null, null, null, null, null, null, null, null, null, null);
 group.addUser(user);
 Mockito.when(adminApp.getGroups()).thenReturn(new ArrayList<Group>() {{
 add(group);
 }});

 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1 = Mockito.spy(updateUserPanel.jComboBox1);
 updateUserPanel.jComboBox2 = Mockito.spy(updateUserPanel.jComboBox2);
 updateUserPanel.jButton3ActionPerformed(Mockito.mock(ActionEvent.class));

 Mockito.verify(adminApp, Mockito.times(1)).resetUserPass(user);
 }

 @Test
 public void testJButton4ActionPerformed_whenUpdateButtonClickedWithValidInput_thenUserIsUpdated() {
 Group group = new Group("Group1", null, null);
 User user = new User("User1", "user1@example.com", null, null, null, null, null, null, null, null, null, null, null, null, null);
 group.addUser(user);
 Mockito.when(adminApp.getGroups()).thenReturn(new ArrayList<Group>() {{
 add(group);
 }});

 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1 = Mockito.spy(updateUserPanel.jComboBox1);
 updateUserPanel.jComboBox2 = Mockito.spy(updateUserPanel.jComboBox2);
 updateUserPanel.jTextField1 = Mockito.spy(updateUserPanel.jTextField1);
 updateUserPanel.jTextField2 = Mockito.spy(updateUserPanel.jTextField2);
 updateUserPanel.jTextField3 = Mockito.spy(updateUserPanel.jTextField3);
 updateUserPanel.jTextField4 = Mockito.spy(updateUserPanel.jTextField4);
 updateUserPanel.jCheckBox1 = Mockito.spy(updateUserPanel.jCheckBox1);
 updateUserPanel.jCheckBox2 = Mockito.spy(updateUserPanel.jCheckBox2);
 updateUserPanel.jCheckBox3 = Mockito.spy(updateUserPanel.jCheckBox3);

 updateUserPanel.jComboBox2 = Mockito.argThat(arg -> arg == user);

 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@example.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("NewJobTitle");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(Mockito.mock(ActionEvent.class));

 User updatedUser = group.getUsers().get(0);
 assertEquals("NewUserName", updatedUser.getName());
 assertEquals("new.user@example.com", updatedUser.getEmail());
 assertEquals("1234567890", updatedUser.getDirectPhone());
 assertEquals("NewJobTitle", updatedUser.getJobTitle());
 assertTrue(updatedUser.isShowMyStatus());
 assertTrue(updatedUser.isOnlyStartChat());
 assertTrue(updatedUser.isCanJoinChatRoom());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformed_whenNoUserSelected_thenThrowsException() {
 updateUserPanel.jButton4ActionPerformed(Mockito.mock(ActionEvent.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformed_whenInvalidEmail_thenThrowsException() {
 updateUserPanel.jTextField2.setText("invalidemail");
 updateUserPanel.jButton4ActionPerformed(Mockito.mock(ActionEvent.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformed_whenInvalidUserName_thenThrowsException() {
 updateUserPanel.jTextField1.setText("InvalidUserName");
 updateUserPanel.jButton4ActionPerformed(Mockito.mock(ActionEvent.class));
 }

}