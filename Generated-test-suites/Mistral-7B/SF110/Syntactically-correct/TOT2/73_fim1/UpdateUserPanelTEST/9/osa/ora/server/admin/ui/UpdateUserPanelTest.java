import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.admin.ui.UpdateUserPanel;
import osa.ora.server.admin.AdminApp;
import osa.ora.server.beans.Group;
import osa.ora.server.beans.User;

public class UpdateUserPanelTest {

 private AdminApp adminApp;
 private UpdateUserPanel updateUserPanel;
 private Group group;
 private User user;

 @Before
 public void setUp() {
 adminApp = new AdminApp();
 group = new Group();
 group.setName("TestGroup");
 user = new User();
 user.setName("TestUser");
 user.setEmail("test@test.com");
 }

 @Test
 public void testUpdatePanelConstruction() {
 UpdateUserPanel panel = new UpdateUserPanel(adminApp, new ControlPanel());
 assertNotNull(panel);
 }

 @Test
 public void testUpdateModelNullGroups() {
 updateUserPanel = new UpdateUserPanel(adminApp, new ControlPanel());
 assertNull(updateUserPanel.jComboBox2);
 }

 @Test
 public void testUpdateModelEmptyGroups() {
 updateUserPanel = new UpdateUserPanel(adminApp, new ControlPanel());
 adminApp.setGroups(new Vector<>());
 updateUserPanel.updateModel();
 assertNull(updateUserPanel.jComboBox2);
 }

 @Test
 public void testUpdateModelSingleGroup() {
 updateUserPanel = new UpdateUserPanel(adminApp, new ControlPanel());
 adminApp.setGroups(new Vector<Group>() {{
 add(group);
 }});
 updateUserPanel.updateModel();
 assertFalse(updateUserPanel.jComboBox2.isEmpty());
 }

 @Test
 public void testUpdateModelSingleUser() {
 updateUserPanel = new UpdateUserPanel(adminApp, new ControlPanel());
 adminApp.setGroups(new Vector<Group>() {{
 add(group);
 group.setUsers(new Vector<User>() {{
 add(user);
 }});
 }});
 updateUserPanel.updateModel();
 assertFalse(updateUserPanel.jComboBox2.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testFillUserNullUser() {
 updateUserPanel = new UpdateUserPanel(adminApp, new ControlPanel());
 updateUserPanel.fillUser(null);
 }

 @Test
 public void testFillUser() {
 updateUserPanel = new UpdateUserPanel(adminApp, new ControlPanel());
 updateUserPanel.fillUser(user);
 String expectedName = user.getName();
 String actualName = updateUserPanel.jTextField1.getText();
 assertEquals(expectedName, actualName);
 }

 @Test
 public void testReset() {
 updateUserPanel = new UpdateUserPanel(adminApp, new ControlPanel());
 updateUserPanel.reset();
 assertEquals("", updateUserPanel.jTextField1.getText());
 }

 @Test
 public void testJButton1ActionPerformedNullDialog() {
 UpdateUserPanel panel = new UpdateUserPanel(adminApp, new ControlPanel());
 panel.jButton1ActionPerformed(new ActionEvent(new Object(), 0, ""));
 }

 @Test
 public void testJButton2ActionPerformedNullGroup() {
 updateUserPanel = new UpdateUserPanel(adminApp, new ControlPanel());
 updateUserPanel.jComboBox1 = new JComboBox<>();
 updateUserPanel.jComboBox2 = new JComboBox<>();
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 0, ""));
 }

 @Test
 public void testJButton2ActionPerformedNullUser() {
 updateUserPanel = new UpdateUserPanel(adminApp, new ControlPanel());
 updateUserPanel.jComboBox1 = new JComboBox<>(new String[]{"Group1"});
 updateUserPanel.jComboBox2 = new JComboBox<>();
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 0, ""));
 }

 @Test
 public void testJButton2ActionPerformedValidUser() {
 updateUserPanel = new UpdateUserPanel(adminApp, new ControlPanel());
 adminApp.setGroups(new Vector<Group>() {{
 add(group);
 group.setUsers(new Vector<User>() {{
 add(user);
 }});
 }});
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1 = new JComboBox<>(new String[]{"Group1"});
 updateUserPanel.jComboBox2 = new JComboBox<>(new String[]{"TestUser"});
 updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 0, ""));
 assertEquals(1, group.getUsers().size());
 }
}