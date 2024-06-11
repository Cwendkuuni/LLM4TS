import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import osa.ora.server.admin.AdminApp;
import osa.ora.server.admin.ui.UpdateUserPanel;
import osa.ora.server.beans.Group;
import osa.ora.server.beans.User;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class UpdateUserPanelTest {

 private UpdateUserPanel updateUserPanel;
 private AdminApp adminApp;
 private Group group;
 private User user, user1, user2;

 @Before
 public void setUp() {
 adminApp = mock(AdminApp.class);
 group = new Group("Group1");
 updateUserPanel = new UpdateUserPanel(adminApp, null);

 user = new User("User1", "user1@email.com", "123456789", "User1 Title", true, true, true, "N/A");
 user1 = new User("User2", "user2@email.com", null, "User2 Title", false, false, false, "N/A");
 user2 = new User("User3", "user3@email.com", "098765432", "User3 Title", true, true, true, "User3 Title");

 group.setUsers(new Vector<User>(Arrays.asList(user, user1, user2)));
 doReturn(group).when(adminApp).getGroups();
 doReturn(new Vector<Group>(Arrays.asList(group))).when(adminApp).getGroups();
 }

 @Test
 public void testUpdateModel_InitiallyEmptyGroups_SetsComboBox1ToEmpty() {
 updateUserPanel.updateModel();
 verify(adminApp, times(1)).getGroups();
 verify(adminApp, times(1)).getGroups();
 assertEquals("", updateUserPanel.jComboBox1.getSelectedItem().toString());
 }

 @Test
 public void testUpdateModel_InitiallyHasGroupsAndUsers_SetsComboBoxesCorrectly() {
 updateUserPanel.updateModel();
 verify(adminApp, times(1)).getGroups();
 verify(adminApp, times(1)).getGroups();
 verify(updateUserPanel.jComboBox1, times(1)).setModel;
 verify(updateUserPanel.jComboBox2, times(1)).setModel;
 verify(updateUserPanel.jComboBox1).setModel(isA(DefaultComboBoxModel.class));
 verify(updateUserPanel.jComboBox2).setModel(isA(DefaultComboBoxModel.class));
 assertEquals(user.getName(), updateUserPanel.jComboBox2.getSelectedItem().toString());
 }

 @Test
 public void testInitComponents_InitializesComponentsCorrectly() {
 updateUserPanel = spy(new UpdateUserPanel(adminApp, null));
 updateUserPanel.initComponents();
 verify(updateUserPanel, times(1)).add(any(JLabel.class));
 verify(updateUserPanel, times(1)).add(any(JLabel.class));
 verify(updateUserPanel, times(1)).add(any(JCheckBox.class));
 verify(updateUserPanel, times(1)).add(any(JCheckBox.class));
 verify(updateUserPanel, times(1)).add(any(JCheckBox.class));
 verify(updateUserPanel, times(1)).add(any(JLabel.class));
 verify(updateUserPanel, times(1)).add(any(JComboBox.class));
 verify(updateUserPanel, times(1)).add(any(JTextField.class));
 verify(updateUserPanel, times(1)).add(any(JButton.class));
 verify(updateUserPanel, times(1)).add(any(JButton.class));
 verify(updateUserPanel, times(1)).add(any(JComboBox.class));
 verify(updateUserPanel, times(1)).add(any(JButton.class));
 verify(updateUserPanel, times(1)).add(any(JLabel.class));
 verify(updateUserPanel, times(1)).add(any(JTextField.class));
 verify(updateUserPanel, times(1)).add(any(JTextField.class));
 verify(updateUserPanel, times(1)).add(any(JLabel.class));
 }

 @Test
 public void testUpdateUser_ValidInputs_UpdatesUserSuccessfully() {
 updateUserPanel.updateModel();
 updateUserPanel.jTextField1.setText("NewUserName");
 updateUserPanel.jTextField2.setText("new.user@email.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("NewUser Title");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton4.doClick();
 verify(adminApp, times(1)).updateUser(user);
 verify(user, times(1)).setName("NewUserName");
 verify(user, times(1)).setEmail("new.user@email.com");
 verify(user, times(1)).setDirectPhone("1234567890");
 verify(user, times(1)).setJobTitle("NewUser Title");
 verify(user, times(1)).setShowMyStatus(false);
 verify(user, times(1)).setOnlyStartChat(true);
 verify(user, times(1)).setCanJoinChatRoom(false);
 }

 @Test
 public void testUpdateUser_InvalidInputs_ShowsErrorMessage() {
 updateUserPanel.updateModel();
 updateUserPanel.jTextField1.setText("");
 updateUserPanel.jTextField2.setText("user@email.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton4.doClick();
 verify(adminApp, never()).updateUser(user);
 verify(updateUserPanel, times(1)).showMessageDialog(eq(UpdateUserPanel.this), eq("Please Enter User Name"), eq("Error"), eq(0), any(ImageIcon.class));
 }

 @Test
 public void testDeleteUser_ValidUserSelected_DeletesUserSuccessfully() {
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton2.doClick();
 verify(adminApp, times(1)).delUser(user);
 verify(group.getUsers(), times(1)).remove(0);
 }

 @Test
 public void testDeleteUser_InvalidSelection_ShowsErrorMessage() {
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jButton2.doClick();
 verify(adminApp, never()).delUser(user);
 verify(updateUserPanel, times(1)).showMessageDialog(eq(UpdateUserPanel.this), eq("Can't identify selected User!"), eq("Error"), eq(0), any(ImageIcon.class));
 }

 @Test
 public void testDeleteUser_UserDoesntExistInGroup_ShowsErrorMessage() {
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(1);
 updateUserPanel.jComboBox2.setSelectedIndex(2);
 updateUserPanel.jButton2.doClick();
 verify(adminApp, never()).delUser(user);
 verify(updateUserPanel, times(1)).showMessageDialog(eq(UpdateUserPanel.this), eq("Can't identify selected User!"), eq("Error"), eq(0), any(ImageIcon.class));
 }

 @Test
 public void testResetPassword_ValidUserSelected_ResetsPasswordSuccessfully() {
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton3.doClick();
 verify(adminApp, times(1)).resetUserPass(user);
 verify(updateUserPanel, times(1)).showMessageDialog(eq(UpdateUserPanel.this), eq("User Password Reset Successfully!"), eq("Info"), eq(0), any(ImageIcon.class));
 }

 @Test
 public void testResetPassword_InvalidSelection_ShowsErrorMessage() {
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jButton3.doClick();
 verify(adminApp, never()).resetUserPass(user);
 verify(updateUserPanel, times(1)).showMessageDialog(eq(UpdateUserPanel.this), eq("Can't identify selected User!"), eq("Error"), eq(0), any(ImageIcon.class));
 }

 @Test
 public void testResetPassword_UserDoesntExistInGroup_ShowsErrorMessage() {
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1.setSelectedIndex(1);
 updateUserPanel.jComboBox2.setSelectedIndex(2);
 updateUserPanel.jButton3.doClick();
 verify(adminApp, never()).resetUserPass(user);
 verify(updateUserPanel, times(1)).showMessageDialog(eq(UpdateUserPanel.this), eq("Can't identify selected User!"), eq("Error"), eq(0), any(ImageIcon.class));
 }
}