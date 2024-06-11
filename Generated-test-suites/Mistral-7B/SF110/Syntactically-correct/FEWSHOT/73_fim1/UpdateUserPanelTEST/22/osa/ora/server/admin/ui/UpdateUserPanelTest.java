package osa.ora.server.admin.ui;

import org.junit.Before;
import org.junit.Test;

import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import static org.junit.Assert.*;

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
    public void testInitComponents() {
        // Check that all components are added to the panel and have the correct bounds
        assertEquals("Username:", updateUserPanel.jLabel1.getText());
        assertNotNull(updateUserPanel.jLabel1);
        assertNotNull(updateUserPanel.jLabel2);
        assertNotNull(updateUserPanel.jLabel3);
        assertNotNull(updateUserPanel.jCheckBox1);
        assertNotNull(updateUserPanel.jCheckBox2);
        assertNotNull(updateUserPanel.jCheckBox3);
        assertNotNull(updateUserPanel.jLabel4);
        assertNotNull(updateUserPanel.jComboBox1);
        assertNotNull(updateUserPanel.jTextField2);
        assertNotNull(updateUserPanel.jTextField3);
        assertNotNull(updateUserPanel.jButton1);
        assertNotNull(updateUserPanel.jButton2);
        assertNotNull(updateUserPanel.jComboBox2);
        assertNotNull(updateUserPanel.jButton3);
        assertNotNull(updateUserPanel.jButton4);
        assertNotNull(updateUserPanel.jLabel5);
        assertNotNull(updateUserPanel.jTextField1);
        assertNotNull(updateUserPanel.jTextField4);
        assertNotNull(updateUserPanel.jLabel6);

        assertEquals(10, updateUserPanel.jLabel1.getX(), 0.1);
        assertEquals(50, updateUserPanel.jLabel1.getY(), 0.1);
        assertEquals(90, updateUserPanel.jTextField2.getX(), 0.1);
        assertEquals(140, updateUserPanel.jTextField2.getY(), 0.1);
        assertEquals(90, updateUserPanel.jTextField3.getX(), 0.1);
        assertEquals(170, updateUserPanel.jTextField3.getY(), 0.1);
        assertEquals(80, updateUserPanel.jLabel6.getX(), 0.1);
        assertEquals(80, updateUserPanel.jLabel5.getX(), 0.1);
        assertEquals(110, updateUserPanel.jLabel6.getY(), 0.1);
        assertEquals(110, updateUserPanel.jLabel5.getY(), 0.1);

        // Check that listeners are added to the JComboBoxes
        assertNotNull(updateUserPanel.jComboBox1.getItemListeners());
        assertNotNull(updateUserPanel.jComboBox2.getItemListeners());
    }

    @Test
    public void testUpdateModel() {
        // Test case for when adminApp.getGroups() is null
        updateUserPanel.adminApp.setGroups(null);
        updateUserPanel.updateModel();

        assertNull(updateUserPanel.jComboBox2.getModel());
        assertNull(updateUserPanel.jComboBox1);

        // Test case for when adminApp.getGroups() is not empty
        Group group = new Group("Group1");
        adminApp.setGroups(Collections.singletonList(group));
        updateUserPanel.updateModel();
        User user = new User("User1", "user1@example.com", null, null, null, null, null, null, null);
        group.getUsers().add(user);

        assertEquals(1, updateUserPanel.jComboBox1.getItemCount());
        assertEquals("Group1", updateUserPanel.jComboBox1.getItemAt(0).toString());

        assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
        assertEquals("User1", updateUserPanel.jComboBox2.getItemAt(0).toString());

        // Test case for when adminApp.getGroups() is not empty but the first group's users are empty
        updateUserPanel.adminApp.getGroups().get(0).setUsers(Collections.emptyList());
        updateUserPanel.updateModel();

        assertEquals(1, updateUserPanel.jComboBox1.getItemCount());
        assertEquals("Group1", updateUserPanel.jComboBox1.getItemAt(0).toString());
        assertNull(updateUserPanel.jComboBox2.getModel());
        assertNull(updateUserPanel.jComboBox2);

        // Test case for when adminApp.getGroups() is not empty and the first group's users are not empty
        updateUserPanel.adminApp.getGroups().get(0).setUsers(Arrays.asList(user));
        updateUserPanel.updateModel();

        assertEquals(1, updateUserPanel.jComboBox1.getItemCount());
        assertEquals("Group1", updateUserPanel.jComboBox1.getItemAt(0).toString());

        assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
        assertEquals("User1", updateUserPanel.jComboBox2.getItemAt(0).toString());
    }

    @Test
    public void testJComboBox1ItemStateChanged() {
        Group group = new Group("Group1");
        adminApp.setGroups(Collections.singletonList(group));
        updateUserPanel.updateModel();
        User user1 = new User("User1", "user1@example.com", null, null, null, null, null, null, null);
        User user2 = new User("User2", "user2@example.com", null, null, null, null, null, null, null);
        group.getUsers().add(user1);
        group.getUsers().add(user2);

        updateUserPanel.jComboBox1.setSelectedItem("Group1");
        updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, ItemEvent.SELECTED, 0));

        assertEquals("User1", updateUserPanel.jComboBox2.getItemAt(0).toString());
    }

    @Test
    public void testJComboBox2ItemStateChanged() {
        Group group = new Group("Group1");
        adminApp.setGroups(Collections.singletonList(group));
        updateUserPanel.updateModel();
        User user1 = new User("User1", "user1@example.com", null, null, null, null, null, null, null);
        User user2 = new User("User2", "user2@example.com", null, null, null, null, null, null, null);
        group.getUsers().add(user1);
        group.getUsers().add(user2);

        updateUserPanel.jComboBox1.setSelectedItem("Group1");
        updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, ItemEvent.SELECTED, 0));

        updateUserPanel.jComboBox2.setSelectedItem("User1");
        updateUserPanel.jComboBox2ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox2, ItemEvent.SELECTED, 0));

        assertEquals(user1.getName(), updateUserPanel.jTextField1.getText());
        assertEquals(user1.getEmail(), updateUserPanel.jTextField2.getText());
        assertEquals(user1.getDirectPhone(), updateUserPanel.jTextField3.getText());
        assertEquals(user1.getJobTitle(), updateUserPanel.jTextField4.getText());
        assertTrue(updateUserPanel.jCheckBox1.isSelected());
        assertTrue(updateUserPanel.jCheckBox2.isSelected());
        assertTrue(updateUserPanel.jCheckBox3.isSelected());
    }

    @Test
    public void testJButton1ActionPerformed() {
        updateUserPanel.controlPanel.getParentDialog().setVisible(true); // Hack to make the dialog visible for the test
        updateUserPanel.jButton1ActionPerformed(null);

        // TODO: Add assertions to check that the dialog is hidden and the user data is reset
    }

    @Test
    public void testJButton2ActionPerformed() {
        Group group = new Group("Group1");
        adminApp.setGroups(Collections.singletonList(group));
        updateUserPanel.updateModel();
        User user = new User("User1", "user1@example.com", null, null, null, null, null, null, null);
        group.getUsers().add(user);

        updateUserPanel.jComboBox1.setSelectedItem("Group1");
        updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, ItemEvent.SELECTED, 0));
        updateUserPanel.jComboBox2.setSelectedIndex(0);

        updateUserPanel.jButton2ActionPerformed(null);

        // TODO: Add assertions to check that the user is deleted from the group's users list and from the panel
    }

    @Test
    public void testJButton3ActionPerformed() {
        Group group = new Group("Group1");
        adminApp.setGroups(Collections.singletonList(group));
        updateUserPanel.updateModel();
        User user = new User("User1", "user1@example.com", null, null, null, null, null, null, null);
        Group group2 = new Group("Group2");
        adminApp.setGroups(Arrays.asList(group, group2));
        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1.setSelectedItem("Group2");
        updateUserPanel.jComboBox2.setSelectedItem("User1");

        updateUserPanel.jButton3ActionPerformed(null);

        // TODO: Add assertions to check that the user's password is reset
    }

    @Test
    public void testJButton4ActionPerformed() {
        Group group = new Group("Group1");
        adminApp.setGroups(Collections.singletonList(group));
        updateUserPanel.updateModel();
        User user = new User("User1", "user1@example.com", null, null, null, null, null, null, null);
        group.getUsers().add(user);

        updateUserPanel.jComboBox1.setSelectedItem("Group1");
        updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, ItemEvent.SELECTED, 0));
        updateUserPanel.jComboBox2.setSelectedItem("User1");

        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("new.user@example.com");
        updateUserPanel.jTextField3.setText("555-555-5555");
        updateUserPanel.jTextField4.setText("Software Engineer");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);

        updateUserPanel.jButton4ActionPerformed(null);

        // TODO: Add assertions to check that the user's data is updated in the group's users list and in the panel
    }
}