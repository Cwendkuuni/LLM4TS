import static org.junit.Assert.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import org.junit.Before;
import org.junit.Test;

public class UpdateUserPanelTest {

    private AdminApp adminApp;
    private ControlPanel controlPanel;
    private UpdateUserPanel updateUserPanel;
    private Group group;
    private User user;

    @Before
    public void setUp() {
        adminApp = new AdminApp();
        controlPanel = new ControlPanel();
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        group = new Group("Group1");
        user = new User("User1", "user1@example.com", "1234567890", "Job Title", true, true, true);
        group.addUser(user);
        adminApp.setGroups(new Vector<Group>() {{
            add(group);
        }});
    }

    @Test
    public void testUpdateUserPanelConstructor() {
        assertNotNull(updateUserPanel);
    }

    @Test
    public void testUpdateModel() {
        updateUserPanel.updateModel();
        assertNotNull(updateUserPanel.jComboBox1.getModel());
        assertFalse(updateUserPanel.jComboBox2.getModel().isEmpty());
        assertSame(user, updateUserPanel.jComboBox2.getModel().getElementAt(0));
    }

    @Test
    public void testInitComponents() {
        updateUserPanel.initComponents();

        // Check labels
        equals(updateUserPanel.jLabel1.getText(), "Username:");
        equals(updateUserPanel.jLabel2.getText(), "Login Email:");
        equals(updateUserPanel.jLabel3.getText(), "Phone:");
        equals(updateUserPanel.jLabel4.getText(), "User Group:");
        equals(updateUserPanel.jLabel5.getText(), "Title:");
        equals(updateUserPanel.jLabel6.getText(), "Username:");

        // Check buttons
        assertNotNull(updateUserPanel.jButton1);
        assertNotNull(updateUserPanel.jButton2);
        assertNotNull(updateUserPanel.jButton3);
        assertNotNull(updateUserPanel.jButton4);

        // Check text fields and check boxes
        assertTrue(updateUserPanel.jTextField1.getText().isEmpty());
        assertTrue(updateUserPanel.jTextField2.getText().isEmpty());
        assertTrue(updateUserPanel.jTextField3.getText().isEmpty());
        assertTrue(updateUserPanel.jTextField4.getText().isEmpty());
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertFalse(updateUserPanel.jCheckBox3.isSelected());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateUserPanelConstructorNullAdminApp() {
        new UpdateUserPanel(null, controlPanel);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateUserPanelConstructorNullControlPanel() {
        new UpdateUserPanel(adminApp, null);
    }

    @Test
    public void testJButton1ActionPerformed() {
        updateUserPanel.jButton1ActionPerformed(new ActionEvent(new Object(), 0, "ActionPerformed"));
        assertTrue(controlPanel.getParentDialog().isVisible() == false);
        assertTrue(updateUserPanel.jTextField1.getText().isEmpty());
        assertTrue(updateUserPanel.jTextField2.getText().isEmpty());
        assertTrue(updateUserPanel.jTextField3.getText().isEmpty());
        assertTrue(updateUserPanel.jTextField4.getText().isEmpty());
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertFalse(updateUserPanel.jCheckBox3.isSelected());
    }

    @Test
    public void testJButton2ActionPerformedWithNullUser() {
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 0, "ActionPerformed"));
        assertEquals("Can't identify selected User!", updateUserPanel.getToolTipText(updateUserPanel.jButton2));
    }

    @Test
    public void testJButton2ActionPerformedWithNullGroup() {
        updateUserPanel.jComboBox1.setModel(new DefaultComboBoxModel<Group>(new Vector<Group>()));
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 0, "ActionPerformed"));
        assertEquals("Can't identify selected User!", updateUserPanel.getToolTipText(updateUserPanel.jButton2));
    }

    @Test
    public void testJButton2ActionPerformed() {
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 0, "ActionPerformed"));

        assertEquals(0, group.getUsers().size());
        assertEquals("User Deleted Successfully!", updateUserPanel.getToolTipText(updateUserPanel.jButton2));
    }

    @Test
    public void testJButton3ActionPerformedWithNullUser() {
        updateUserPanel.jButton3ActionPerformed(new ActionEvent(new Object(), 0, "ActionPerformed"));
        assertEquals("Can't identify selected User!", updateUserPanel.getToolTipText(updateUserPanel.jButton3));
    }

    @Test
    public void testJButton3ActionPerformed() {
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("new.user@example.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("New Job Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton3ActionPerformed(new ActionEvent(new Object(), 0, "ActionPerformed"));

        assertEquals("User Password Reset Successfully!", updateUserPanel.getToolTipText(updateUserPanel.jButton3));
        assertEquals(user.getName(), "NewUserName");
        assertEquals(user.getEmail(), "new.user@example.com");
        assertEquals(user.getDirectPhone(), "1234567890");
        assertEquals(user.getJobTitle(), "New Job Title");
        assertTrue(user.isShowMyStatus());
        assertTrue(user.isOnlyStartChat());
        assertTrue(user.isCanJoinChatRoom());
    }

    @Test
    public void testJButton4ActionPerformedWithNullUser() {
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, "ActionPerformed"));
        assertEquals("Can't identify selected User!", updateUserPanel.getToolTipText(updateUserPanel.jButton4));
    }

    @Test
    public void testJButton4ActionPerformedWithEmptyEmail() {
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("New Job Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, "ActionPerformed"));

        assertEquals("Please Enter User email", updateUserPanel.getToolTipText(updateUserPanel.jButton4));
    }

    @Test
    public void testJButton4ActionPerformedWithExistingEmail() {
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("user1@example.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("New Job Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, "ActionPerformed"));

        assertEquals("User with Same email Already Exist!", updateUserPanel.getToolTipText(updateUserPanel.jButton4));
    }

    @Test
    public void testJButton4ActionPerformed() {
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("new.user@example.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("New Job Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, "ActionPerformed"));

        assertEquals("User Updated Successfully!", updateUserPanel.getToolTipText(updateUserPanel.jButton4));
        assertEquals(user.getId(), updateUserPanel.adminApp.getUserById("new.user@example.com").getId());
        assertEquals("NewUserName", updateUserPanel.adminApp.getUserById("new.user@example.com").getName());
        assertEquals("new.user@example.com", updateUserPanel.adminApp.getUserById("new.user@example.com").getEmail());
        assertEquals("1234567890", updateUserPanel.adminApp.getUserById("new.user@example.com").getDirectPhone());
        assertEquals("New Job Title", updateUserPanel.adminApp.getUserById("new.user@example.com").getJobTitle());
        assertTrue(updateUserPanel.adminApp.getUserById("new.user@example.com").isShowMyStatus());
        assertTrue(updateUserPanel.adminApp.getUserById("new.user@example.com").isOnlyStartChat());
        assertTrue(updateUserPanel.adminApp.getUserById("new.user@example.com").isCanJoinChatRoom());
    }
}