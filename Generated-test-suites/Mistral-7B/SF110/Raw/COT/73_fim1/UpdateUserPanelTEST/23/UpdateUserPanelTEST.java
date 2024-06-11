package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

    private UpdateUserPanel updateUserPanel;
    private AdminApp adminApp;
    private ControlPanel controlPanel;
    private Group group;
    private User user;
    
    @Before
    public void setUp() {
        adminApp = new AdminApp();
        controlPanel = new ControlPanel();
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        group = new Group("Group1");
        user = new User("User1", "user1@example.com", "1234567890", "Job Title", true, false, true);
        group.addUser(user);
        adminApp.setGroups(new Vector<Group>() {{
            add(group);
        }});
    }
    
    @Test
    public void testUpdateModel() {
        updateUserPanel.updateModel();
        assertEquals(group.getUsers().get(0), updateUserPanel.jComboBox2.getSelectedItem());
    }
    
    @Test
    public void testInitComponents() {
        updateUserPanel.initComponents();
        assertEquals(7, updateUserPanel.getComponentCount());
    }
    
    @Test
    public void testJButton1ActionPerformedWithNullAdminApp() {
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(null, controlPanel);
        updateUserPanel.jButton1ActionPerformed(new ActionEvent(new JButton(), 0, "Cancel"));
        assertTrue(updateUserPanel.isVisible());
    }
    
    @Test(expected = NullPointerException.class)
    public void testJButton2ActionPerformedWithNullUser() {
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(new JButton(), 0, "Delete"));
    }
    
    @Test
    public void testJButton2ActionPerformedWithNullGroup() {
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(new JButton(), 0, "Delete"));
        adminApp.setGroups(null);
        updateUserPanel.updateModel();
        assertFalse(updateUserPanel.jComboBox1.isEnabled());
    }
    
    @Test
    public void testJButton2ActionPerformedWithInvalidIndex() {
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(new JButton(), 0, "Delete"));
        adminApp.setGroups(new Vector<Group>() {{
            add(group);
        }});
        updateUserPanel.updateModel();
        updateUserPanel.jComboBox2.setSelectedIndex(-1);
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(new JButton(), 0, "Delete"));
        assertFalse(updateUserPanel.isVisible());
    }
    
    @Test
    public void testJButton2ActionPerformedWithNullAdminAppAndNullGroup() {
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(null, controlPanel);
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(new JButton(), 0, "Delete"));
        adminApp.setGroups(null);
        updateUserPanel.updateModel();
        assertFalse(updateUserPanel.isVisible());
    }
    
    @Test
    public void testJButton3ActionPerformedWithNullAdminApp() {
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(null, controlPanel);
        updateUserPanel.jButton3ActionPerformed(new ActionEvent(new JButton(), 0, "Reset Password"));
        assertTrue(updateUserPanel.isVisible());
    }
    
    @Test
    public void testJButton3ActionPerformedWithNullUser() {
        updateUserPanel.jButton3ActionPerformed(new ActionEvent(new JButton(), 0, "Reset Password"));
        assertFalse(updateUserPanel.isVisible());
    }
    
    @Test
    public void testJButton3ActionPerformedWithNullAdminAppAndNullUser() {
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(null, controlPanel);
        updateUserPanel.jButton3ActionPerformed(new ActionEvent(new JButton(), 0, "Reset Password"));
        assertTrue(updateUserPanel.isVisible());
    }
    
    @Test
    public void testJButton4ActionPerformedWithNullAdminApp() {
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(null, controlPanel);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new JButton(), 0, "Update"));
        assertTrue(updateUserPanel.isVisible());
    }
    
    @Test
    public void testJButton4ActionPerformedWithNullUser() {
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new JButton(), 0, "Update"));
        assertFalse(updateUserPanel.isVisible());
    }
    
    @Test
    public void testJButton4ActionPerformedWithEmptyName() {
        updateUserPanel.jTextField1.setText("");
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new JButton(), 0, "Update"));
        assertFalse(updateUserPanel.isVisible());
    }
    
    @Test
    public void testJButton4ActionPerformedWithEmptyEmail() {
        updateUserPanel.jTextField2.setText("");
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new JButton(), 0, "Update"));
        assertFalse(updateUserPanel.isVisible());
    }
    
    @Test
    public void testJButton4ActionPerformedWithEmailAlreadyExists() {
        User existingUser = new User("User2", "user1@example.com", "1234567890", "Job Title", true, false, true);
        adminApp.setGroups(new Vector<Group>() {{
            add(new Group("Group1"));
            add(new Group("Group2"));
        }});
        Group group1 = adminApp.getGroups().get(0);
        group1.addUser(user);
        group1.addUser(existingUser);
        updateUserPanel.jComboBox1.setSelectedItem(group1);
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jTextField2.setText("user1@example.com");
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new JButton(), 0, "Update"));
        assertFalse(updateUserPanel.isVisible());
    }
    
    @Test
    public void testJButton4ActionPerformedWithAllFieldsFilled() {
        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("new.user@example.com");
        updateUserPanel.jTextField3.setText("1111111111");
        updateUserPanel.jTextField4.setText("New Job Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new JButton(), 0, "Update"));
        assertTrue(updateUserPanel.isVisible());
        User updatedUser = (User) updateUserPanel.jComboBox2.getSelectedItem();
        assertEquals("NewUserName", updatedUser.getName());
        assertEquals("new.user@example.com", updatedUser.getEmail());
        assertEquals("1111111111", updatedUser.getDirectPhone());
        assertEquals("New Job Title", updatedUser.getJobTitle());
        assertTrue(updatedUser.isShowMyStatus());
        assertFalse(updatedUser.isOnlyStartChat());
        assertTrue(updatedUser.isCanJoinChatRoom());
    }
}