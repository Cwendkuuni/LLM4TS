import org.junit.Before;
import org.junit.Test;
import osa.ora.server.admin.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class UpdateUserPanelTest {

 private UpdateUserPanel updateUserPanel;
 private AdminApp adminAppMock;
 private ControlPanel controlPanelMock;

 @Before
 public void setUp() {
 adminAppMock = mock(AdminApp.class);
 controlPanelMock = mock(ControlPanel.class);
 updateUserPanel = new UpdateUserPanel(adminAppMock, controlPanelMock);
 }

 @Test
 public void testUpdateModel() {
 // Given adminApp has groups and users
 when(adminAppMock.getGroups()).thenReturn( Arrays.asList(new Group("Group1")) );
 when(adminAppMock.getGroups().get(0).getUsers()).thenReturn( Arrays.asList(new User("User1")) );

 // When updateModel is called
 updateUserPanel.updateModel();

 // Then jComboBox2 should have User1 as selected item
 assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem());
 }

 @Test
 public void testJButton1ActionPerformed() {
 // Given updateUserPanel is displayed
 updateUserPanel.setVisible(true);

 // When jButton1 is clicked
 updateUserPanel.jButton1.doClick();

 // Then controlPanel dialog should be hidden
 verify(controlPanelMock).getParentDialog().setVisible(false);
 }

 @Test
 public void testJButton2ActionPerformed() {
 // Given updateUserPanel has a user selected in jComboBox2
 User user = new User("User1");
 when(adminAppMock.getGroups().get(0).getUsers()).thenReturn( Arrays.asList(user) );
 when(adminAppMock.delUser(user)).thenReturn(null);
 updateUserPanel.jComboBox2.setSelectedItem(user);

 // When jButton2 is clicked
 updateUserPanel.jButton2.doClick();

 // Then user should be deleted from adminApp
 verify(adminAppMock).delUser(user);
 }

 @Test(expected = IllegalStateException.class)
 public void testJButton2ActionPerformedWithoutUserSelected() {
 // Given updateUserPanel has no user selected in jComboBox2
 updateUserPanel.jComboBox2.setSelectedIndex(-1);

 // When jButton2 is clicked
 updateUserPanel.jButton2.doClick();

 // Then an IllegalStateException should be thrown
 }

 @Test
 public void testJButton3ActionPerformed() {
 // Given updateUserPanel has a user selected in jComboBox2
 User user = new User("User1");
 when(adminAppMock.getGroups().get(0).getUsers()).thenReturn( Arrays.asList(user) );
 when(adminAppMock.resetUserPass(user)).thenReturn(new User("User1_Reset"));
 updateUserPanel.jComboBox2.setSelectedItem(user);

 // When jButton3 is clicked
 updateUserPanel.jButton3.doClick();

 // Then user password should be reset
 verify(adminAppMock).resetUserPass(user);
 }

 @Test
 public void testJButton4ActionPerformed() {
 // Given updateUserPanel has a user selected in jComboBox2 and all text fields filled
 User user = new User("User1");
 when(adminAppMock.getGroups().get(0).getUsers()).thenReturn( Arrays.asList(user) );
 when(adminAppMock.updateUser(user)).thenReturn(new User("User1_Updated"));
 updateUserPanel.jComboBox2.setSelectedItem(user);
 updateUserPanel.jTextField1.setText("User1_Updated_Name");
 updateUserPanel.jTextField2.setText("user1_updated_email@example.com");
 updateUserPanel.jTextField3.setText("555-555-5555");
 updateUserPanel.jTextField4.setText("Software Tester");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();

 // Then user should be updated in adminApp
 verify(adminAppMock).updateUser(user);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJButton4ActionPerformedWithoutUserSelected() {
 // Given updateUserPanel has no user selected in jComboBox2
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jTextField1.setText("User1_Updated_Name");
 updateUserPanel.jTextField2.setText("user1_updated_email@example.com");
 updateUserPanel.jTextField3.setText("555-555-5555");
 updateUserPanel.jTextField4.setText("Software Tester");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(false);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4.doClick();

 // Then an IllegalArgumentException should be thrown
 }
}