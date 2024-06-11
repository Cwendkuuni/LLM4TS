import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
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
 Group group = new Group("Group1");
 adminApp.addGroup(group);
 User user = new User("User1", "user1@example.com", "1234567890", "Job Title1", true, true, true);
 group.addUser(user);
 adminApp.setGroups(Arrays.asList(group));

 updateUserPanel.updateModel();

 assertEquals(group, updateUserPanel.getAdminApp().getGroups().get(0));
 assertNotNull(updateUserPanel.getJComboBox2());
 assertFalse(updateUserPanel.getJComboBox2().getItemCount() == 0);
 User userInComboBox = (User) updateUserPanel.getJComboBox2().getSelectedItem();
 assertEquals(user, userInComboBox);
 }

 @Test
 public void testInitComponents() {
 // Perform an assertion on the layout of the UpdateUserPanel
 SpringLayout layout = new SpringLayout();
 updateUserPanel.setLayout(layout);
 updateUserPanel.initComponents();

 // Perform an assertion on the location and size of the UI components
 JLabel jLabel1 = updateUserPanel.getjLabel1();
 assertEquals(10, jLabel1.getX(), 0.1);
 assertEquals(50, jLabel1.getY(), 0.1);

 JLabel jLabel2 = updateUserPanel.getjLabel2();
 assertEquals(10, jLabel2.getX(), 0.1);
 assertEquals(140, jLabel2.getY(), 0.1);

 JCheckBox jCheckBox1 = updateUserPanel.getjCheckBox1();
 assertEquals(30, jCheckBox1.getX(), 0.1);
 assertEquals(200, jCheckBox1.getY(), 0.1);
 }

 @Test
 public void testJButton1ActionPerformed() {
 updateUserPanel.jButton1ActionPerformed(new ActionEvent(new Object(), 0, "Cancel"));
 assertTrue(controlPanel.getParentDialog().isVisible() == false);
 }

 @Test
 public void testJButton2ActionPerformed() {
 Group group = new Group("Group1");
 adminApp.addGroup(group);
 User user = new User("User1", "user1@example.com", "1234567890", "Job Title1", true, true, true);
 group.addUser(user);
 adminApp.setGroups(Arrays.asList(group));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1 = new JComboBox<>(adminApp.getGroups().toArray());
 updateUserPanel.jComboBox2 = new JComboBox<>(group.getUsers().toArray());
 updateUserPanel.jButton2.doClick();

 assertTrue(updateUserPanel.getAdminApp().getGroups().get(0).getUsers().size() == group.getUsers().size() - 1);
 }

 @Test
 public void testJButton3ActionPerformed() {
 Group group = new Group("Group1");
 adminApp.addGroup(group);
 User user = new User("User1", "user1@example.com", "1234567890", "Job Title1", true, true, true);
 group.addUser(user);
 adminApp.setGroups(Arrays.asList(group));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1 = new JComboBox<>(adminApp.getGroups().toArray());
 updateUserPanel.jComboBox2 = new JComboBox<>(group.getUsers().toArray());
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("newuser@example.com");
 updateUserPanel.jTextField3.setText("New Phone Number");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton3.doClick();

 User newUser = group.getUsers().get(group.getUsers().size() - 1);
 assertEquals("New User Name", newUser.getName());
 assertEquals("newuser@example.com", newUser.getEmail());
 assertEquals("New Phone Number", newUser.getDirectPhone());
 assertEquals("New Job Title", newUser.getJobTitle());
 assertFalse(newUser.isShowMyStatus());
 assertTrue(newUser.isOnlyStartChat());
 assertFalse(newUser.isCanJoinChatRoom());
 }

 @Test
 public void testJButton4ActionPerformed() {
 Group group = new Group("Group1");
 adminApp.addGroup(group);
 User user = new User("User1", "user1@example.com", "1234567890", "Job Title1", true, true, true);
 group.addUser(user);
 adminApp.setGroups(Arrays.asList(group));
 updateUserPanel.updateModel();
 updateUserPanel.jComboBox1 = new JComboBox<>(adminApp.getGroups().toArray());
 updateUserPanel.jComboBox2 = new JComboBox<>(group.getUsers().toArray());
 updateUserPanel.jTextField1.setText("New User Name");
 updateUserPanel.jTextField2.setText("newuser@example.com");
 updateUserPanel.jTextField3.setText("New Phone Number");
 updateUserPanel.jTextField4.setText("New Job Title");
 updateUserPanel.jCheckBox1.setSelected(false);
 updateUserPanel.jCheckBox2.setSelected(true);
 updateUserPanel.jCheckBox3.setSelected(false);
 updateUserPanel.jButton4.doClick();

 User newUser = group.getUsers().get(group.getUsers().size() - 1);
 assertEquals("New User Name", newUser.getName());
 assertEquals("newuser@example.com", newUser.getEmail());
 assertEquals("New Phone Number", newUser.getDirectPhone());
 assertEquals("New Job Title", newUser.getJobTitle());
 assertFalse(newUser.isShowMyStatus());
 assertTrue(newUser.isOnlyStartChat());
 assertFalse(newUser.isCanJoinChatRoom());
 }
}