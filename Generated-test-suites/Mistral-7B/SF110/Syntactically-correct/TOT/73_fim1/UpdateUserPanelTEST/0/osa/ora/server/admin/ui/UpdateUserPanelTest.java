import org.junit.Before;
import org.junit.Test;
import osa.ora.server.admin.AdminApp;
import osa.ora.server.admin.ui.UpdateUserPanel;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
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
 public void testUpdateModelWithValidData() {
 when(adminApp.getGroups()).thenReturn(new Vector<>());
 when(adminApp.getGroups().get(0)).thenReturn(new Group());
 when(adminApp.getGroups().get(0).getUsers()).thenReturn(new Vector<>());
 when(adminApp.getGroups().get(0).getUsers().get(0)).thenReturn(new User());

 updateUserPanel.updateModel();

 assertEquals(1, updateUserPanel.jComboBox1.getItemCount());
 assertEquals("Item 1", updateUserPanel.jComboBox1.getItemAt(0).toString());
 assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
 assertEquals("Item 1", updateUserPanel.jComboBox2.getItemAt(0).toString());
 }

 @Test
 public void testUpdateModelWithEmptyGroups() {
 when(adminApp.getGroups()).thenReturn(null);

 updateUserPanel.updateModel();

 assertEquals(0, updateUserPanel.jComboBox1.getItemCount());
 }

 @Test
 public void testUpdateModelWithEmptyUsers() {
 when(adminApp.getGroups()).thenReturn(new Vector<>());
 when(adminApp.getGroups().get(0)).thenReturn(new Group());
 when(adminApp.getGroups().get(0).getUsers()).thenReturn(null);

 updateUserPanel.updateModel();

 assertEquals(0, updateUserPanel.jComboBox1.getItemCount());
 }

 @Test(expected = NullPointerException.class)
 public void testUpdateModelWithNullAdminApp() {
 updateUserPanel = new UpdateUserPanel(null, controlPanel);
 updateUserPanel.updateModel();
 }

 @Test(expected = NullPointerException.class)
 public void testUpdateModelWithNullControlPanel() {
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 updateUserPanel.updateModel();
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1ActionPerformed(mock(ActionEvent.class));

 assertTrue(updateUserPanel.isVisible());
 assertTrue(controlPanel.getParentDialog().isVisible());
 }

 @Test
 public void testJButton2ActionPerformedWithValidData() {
 when(adminApp.getGroups()).thenReturn(new Vector<>());
 when(adminApp.getGroups().get(0)).thenReturn(new Group());
 when(adminApp.getGroups().get(0).getUsers()).thenReturn(new Vector<>());
 when(adminApp.getGroups().get(0).getUsers().get(0)).thenReturn(new User());
 when(adminApp.delUser(mock(User.class))).thenReturn(new User());

 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton2ActionPerformed(mock(ActionEvent.class));

 assertEquals(0, updateUserPanel.jComboBox2.getItemCount());
 }

 @Test(expected = NullPointerException.class)
 public void testJButton2ActionPerformedWithNullSelectedUser() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jButton2ActionPerformed(mock(ActionEvent.class));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJButton2ActionPerformedWithInvalidSelectedIndex() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(10);
 updateUserPanel.jButton2ActionPerformed(mock(ActionEvent.class));
 }

 @Test
 public void testJButton3ActionPerformedWithValidData() {
 when(adminApp.getGroups()).thenReturn(new Vector<>());
 when(adminApp.getGroups().get(0)).thenReturn(new Group());
 when(adminApp.getGroups().get(0).getUsers()).thenReturn(new Vector<>());
 when(adminApp.getGroups().get(0).getUsers().get(0)).thenReturn(new User());
 when(adminApp.resetUserPass(mock(User.class))).thenReturn(new User());

 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jButton3ActionPerformed(mock(ActionEvent.class));

 assertTrue(JOptionPane.showConfirmDialog(updateUserPanel, "Password reset for user: " + updateUserPanel.jComboBox2.
 getSelectedItem().toString() + " was successful!") == JOptionPane.OK_OPTION);
 }

 @Test
 public void testJButton3ActionPerformedWithNullSelectedUser() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jButton3ActionPerformed(mock(ActionEvent.class));

 assertFalse(JOptionPane.showConfirmDialog(updateUserPanel, "Password reset for user: null was successful!") > 0);
 }

 @Test
 public void testJButton4ActionPerformedWithValidData() {
 when(adminApp.getGroups()).thenReturn(new Vector<>());
 when(adminApp.getGroups().get(0)).thenReturn(new Group());
 when(adminApp.getGroups().get(0).getUsers()).thenReturn(new Vector<>());
 when(adminApp.getGroups().get(0).getUsers().get(0)).thenReturn(new User());
 when(adminApp.updateUser(mock(User.class))).thenReturn(new User());

 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(0);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("New User Email");
 updateUserPanel.jTextField3.setText("New User Phone");
 updateUserPanel.jTextField4.setText("New User Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));

 assertEquals("New User Name", updateUserPanel.jComboBox2.getSelectedItem().getName());
 assertEquals("New User Email", updateUserPanel.jComboBox2.getSelectedItem().getEmail());
 assertEquals("New User Phone", updateUserPanel.jComboBox2.getSelectedItem().getDirectPhone());
 assertEquals("New User Job Title", updateUserPanel.jComboBox2.getSelectedItem().getJobTitle());
 assertTrue(updateUserPanel.jComboBox2.getSelectedItem().isShowMyStatus());
 assertTrue(updateUserPanel.jComboBox2.getSelectedItem().isOnlyStartChat());
 assertTrue(updateUserPanel.jComboBox2.getSelectedItem().isCanJoinChatRoom());
 }

 @Test(expected = NullPointerException.class)
 public void testJButton4ActionPerformedWithNullSelectedUser() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(-1);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("New User Email");
 updateUserPanel.jTextField3.setText("New User Phone");
 updateUserPanel.jTextField4.setText("New User Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJButton4ActionPerformedWithInvalidSelectedIndex() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox2.setSelectedIndex(10);
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("New User Email");
 updateUserPanel.jTextField3.setText("New User Phone");
 updateUserPanel.jTextField4.setText("New User Job Title");
 updateUserPanel.jCheckBox1.setSelected(true);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(true);
 updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));
 }

 @Test
 public void testJComboBox1ItemStateChanged() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 updateUserPanel.jComboBox1ItemStateChanged(mock(ItemEvent.class));

 assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
 }

 @Test
 public void testJComboBox1ItemStateChangedWithEmptyUsers() {
 updateUserPanel.jComboBox1.setSelectedIndex(0);
 when(adminApp.getGroups().get(0).getUsers()).thenReturn(null);
 updateUserPanel.jComboBox1ItemStateChanged(mock(ItemEvent.class));

 assertEquals(0, updateUserPanel.jComboBox2.getItemCount());
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
}