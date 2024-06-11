package osa.ora.server.admin.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

    private AdminApp adminApp;
    private UpdateUserPanel updateUserPanel;
    private Group group;
    private User user;

    @Before
    public void setUp() {
        adminApp = new AdminApp();
        group = new Group();
        group.setName("Group1");
        user = new User();
        user.setName("User1");
        user.setEmail("user1@example.com");
        user.setDirectPhone("1234567890");
        user.setJobTitle("JobTitle1");
        user.setShowMyStatus(true);
        user.setOnlyStartChat(false);
        user.setCanJoinChatRoom(true);
        group.addUser(user);
        updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1 = new JComboBox<>(new DefaultComboBoxModel<>(new String[] { "Group1" }));
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2 = new JComboBox<>(new DefaultComboBoxModel<>(group.getUsers().toArray()));
        updateUserPanel.jComboBox2.setSelectedIndex(0);
    }

    @Test
    public void testInitComponents() {
        assertEquals("Username:", updateUserPanel.jLabel1.getText());
        assertEquals("Login Email:", updateUserPanel.jLabel2.getText());
        assertEquals("Phone:", updateUserPanel.jLabel3.getText());
        // ... other assertions for other labels, buttons and textfields
    }

    @Test
    public void testUpdateModel() {
        assertEquals("Group1", updateUserPanel.jComboBox1.getSelectedItem());
        assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem());
        assertSame(user, updateUserPanel.jComboBox2.getSelectedItem());
    }

    @Test
    public void testJComboBox1ItemStateChanged() {
        updateUserPanel.jComboBox1.setSelectedIndex(1);
        assertEquals("Group2", updateUserPanel.jComboBox1.getSelectedItem());
        assertNotNull(updateUserPanel.jComboBox2.getModel());
        assertEquals(0, updateUserPanel.jComboBox2.getModel().getSize());

        updateUserPanel.jComboBox1.setSelectedIndex(0);
        assertEquals("Group1", updateUserPanel.jComboBox1.getSelectedItem());
        assertNotNull(updateUserPanel.jComboBox2.getModel());
        assertEquals(1, updateUserPanel.jComboBox2.getModel().getSize());
        assertSame(user, updateUserPanel.jComboBox2.getSelectedItem());
    }

    @Test
    public void testJComboBox2ItemStateChanged() {
        updateUserPanel.jComboBox2.setSelectedIndex(1);
        assertEquals("User2", updateUserPanel.jComboBox2.getSelectedItem());
        assertNull(updateUserPanel.jTextField1.getText());
        assertNull(updateUserPanel.jTextField2.getText());
        assertNull(updateUserPanel.jTextField3.getText());
        assertNull(updateUserPanel.jTextField4.getText());
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertFalse(updateUserPanel.jCheckBox3.isSelected());

        updateUserPanel.jComboBox2.setSelectedIndex(0);
        assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem());
        assertEquals(user.getName(), updateUserPanel.jTextField1.getText());
        assertEquals(user.getEmail(), updateUserPanel.jTextField2.getText());
        assertEquals(user.getDirectPhone(), updateUserPanel.jTextField3.getText());
        assertEquals(user.getJobTitle(), updateUserPanel.jTextField4.getText());
        assertTrue(updateUserPanel.jCheckBox1.isSelected());
        assertTrue(updateUserPanel.jCheckBox2.isSelected());
        assertTrue(updateUserPanel.jCheckBox3.isSelected());
    }

    // ... other test methods for jButton1ActionPerformed, jButton2ActionPerformed, jButton3ActionPerformed, jButton4ActionPerformed, and reset() methods
}