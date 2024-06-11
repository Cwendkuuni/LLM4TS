import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import static org.junit.Assert.*;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private ControlPanel controlPanel;
 private UpdateUserPanel updateUserPanel;

 @Before
 public void setUp() {
 adminApp = mock(AdminApp.class);
 controlPanel = mock(ControlPanel.class);
 updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
 }

 @Test
 public void testUpdateModel_whenGroupsAndUsersArePresent_thenUserComboBoxShouldBePopulatedWithUsers() {
 // Given
 Group group = new Group("Group1");
 User user1 = new User("User1", "user1@email.com", "1234567890", "Job1", true, true, true);
 User user2 = new User("User2", "user2@email.com", "2345678901", "Job2", false, false, false);
 group.addUser(user1);
 group.addUser(user2);
 when(adminApp.getGroups()).thenReturn(Arrays.asList(group));

 // When
 updateUserPanel.updateModel();

 // Then
 assertEquals(2, updateUserPanel.jComboBox2.getItemCount());
 assertEquals("User1", updateUserPanel.jComboBox2.getItemAt(0).toString());
 assertEquals("User2", updateUserPanel.jComboBox2.getItemAt(1).toString());
 }

 @Test
 public void testUpdateModel_whenGroupsArePresentButNoUsers_thenUserComboBoxShouldBeEmpty() {
 // Given
 Group group = new Group("Group1");
 when(adminApp.getGroups()).thenReturn(Arrays.asList(group));

 // When
 updateUserPanel.updateModel();

 // Then
 assertEquals(0, updateUserPanel.jComboBox2.getItemCount());
 }

 @Test
 public void testFillUser_whenValidUserIsPassed_thenFieldsShouldBeFilledCorrectly() {
 // Given
 User user = new User("User1", "user1@email.com", "1234567890", "Job1", true, true, true);

 // When
 updateUserPanel.fillUser(user);

 // Then
 assertEquals("User1", updateUserPanel.jTextField1.getText());
 assertEquals("user1@email.com", updateUserPanel.jTextField2.getText());
 assertEquals("1234567890", updateUserPanel.jTextField3.getText());
 assertEquals("Job1", updateUserPanel.jTextField4.getText());
 assertEquals(user.isShowMyStatus(), updateUserPanel.jCheckBox1.isSelected());
 assertEquals(user.isOnlyStartChat(), updateUserPanel.jCheckBox2.isSelected());
 assertEquals(user.isCanJoinChatRoom(), updateUserPanel.jCheckBox3.isSelected());
 }

 @Test
 public void testReset_whenCalled_thenAllFieldsShouldBeEmpty() {
 // Given
 updateUserPanel.jTextField1.setText("User1");
 updateUserPanel.jTextField2.setText("user1@email.com");
 updateUserPanel.jTextField3.setText("1234567890");
 updateUserPanel.jTextField4.setText("Job1");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);

 // When
 updateUserPanel.reset();

 // Then
 assertEquals("", updateUserPanel.jTextField1.getText());
 assertEquals("", updateUserPanel.jTextField2.getText());
 assertEquals("", updateUserPanel.jTextField3.getText());
 assertEquals("", updateUserPanel.jTextField4.getText());
 assertFalse(updateUserPanel.jCheckBox1.isSelected());
 assertFalse(updateUserPanel.jCheckBox2.isSelected());
 assertFalse(updateUserPanel.jCheckBox3.isSelected());
 }

 @Test(expected = NullPointerException.class)
 public void testJComboBox2ItemStateChanged_whenNullUserIsSelected_thenExceptionShouldBeThrown() {
 // Given
 updateUserPanel.jComboBox2.setSelectedItem(null);

 // When & Then
 updateUserPanel.jComboBox2ItemStateChanged(null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJButton2ActionPerformed_whenSelectedUserIsNotFound_thenExceptionShouldBeThrown() {
 // Given
 User user = new User("User1", "user1@email.com", "1234567890", "Job1", true, true, true);
 Group group = new Group("Group1");
 group.addUser(user);
 when(adminApp.getGroups()).thenReturn(Arrays.asList(group));
 updateUserPanel.jComboBox1.setSelectedItem("Group1");
 updateUserPanel.jComboBox2.setSelectedIndex(1);

 // When & Then
 updateUserPanel.jButton2ActionPerformed(mock(ActionEvent.class));
 }

 @Test
 public void testJButton3ActionPerformed_whenValidUserIsSelected_thenPasswordShouldBeReset() {
 // Given
 User user = new User("User1", "user1@email.com", "1234567890", "Job1", true, true, true);
 when(adminApp.getGroups()).thenReturn(Arrays.asList(new Group("Group1")));
 when(adminApp.getGroups().get(0).getUsers()).thenReturn(Arrays.asList(user));
 when(adminApp.resetUserPass(user)).thenReturn(new User("User1", "user1@email.com", "newPassword", "Job1", true, true, true));
 updateUserPanel.jComboBox1.setSelectedItem("Group1");
 updateUserPanel.jComboBox2.setSelectedItem(user);

 // When
 updateUserPanel.jButton3ActionPerformed(mock(ActionEvent.class));

 // Then
 assertEquals("newPassword", updateUserPanel.jTextField2.getText());
 }

 @Test
 public void testJButton3ActionPerformed_whenInvalidEmailIsEntered_thenErrorMessageShouldBeDisplayed() {
 // Given
 User user = new User("User1", "user1@email.com", "1234567890", "Job1", true, true, true);
 when(adminApp.getGroups()).thenReturn(Arrays.asList(new Group("Group1")));
 when(adminApp.getGroups().get(0).getUsers()).thenReturn(Arrays.asList(user));
 updateUserPanel.jComboBox1.setSelectedItem("Group1");
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField2.setText("invalidEmail");

 // When
 updateUserPanel.jButton3ActionPerformed(mock(ActionEvent.class));

 // Then
 assertEquals("Error Happen invalidEmail", updateUserPanel.adminApp.getErrorMessage());
 }
}