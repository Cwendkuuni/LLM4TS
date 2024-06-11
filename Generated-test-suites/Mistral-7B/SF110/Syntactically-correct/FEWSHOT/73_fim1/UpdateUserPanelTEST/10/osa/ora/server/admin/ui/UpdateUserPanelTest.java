package osa.ora.server.admin.ui;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import osa.ora.server.admin.AdminApp;
import osa.ora.server.admin.Group;
import osa.ora.server.admin.User;
import static osa.ora.server.admin.ui.UpdateUserPanel.*;

public class UpdateUserPanelTest {

    private AdminApp adminApp;
    private Group group;
    private UpdateUserPanel updateUserPanel;

    @Before
    public void setup() {
        adminApp = new AdminApp();
        group = new Group("Group1", null);
        adminApp.addGroup(group);
        updateUserPanel = new UpdateUserPanel(adminApp, null);
    }

    @Test
    public void testUpdateModel() {
        updateUserPanel.updateModel();
        assertEquals(1, updateUserPanel.jComboBox1.getItemCount());
        assertEquals("Group1", updateUserPanel.jComboBox1.getItemAt(0).toString());

        User user = new User("User1", "user1@example.com", "1234567890", "JobTitle1", true, true, true);
        group.addUser(user);

        updateUserPanel.updateModel();
        assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
        assertEquals("User1", updateUserPanel.jComboBox2.getItemAt(0).toString());

        User user2 = new User("User2", "user2@example.com", null, "JobTitle2", true, true, true);
        group.addUser(user2);

        updateUserPanel.updateModel();
        assertEquals(2, updateUserPanel.jComboBox2.getItemCount());
    }

    @Test
    public void testFillUser() {
        User user = new User("User1", "user1@example.com", "1234567890", "JobTitle1", true, true, true);
        group.addUser(user);
        updateUserPanel.updateModel();
        updateUserPanel.jComboBox2.setSelectedIndex(0);

        updateUserPanel.fillUser(user);

        assertEquals("User1", updateUserPanel.jTextField1.getText());
        assertEquals("user1@example.com", updateUserPanel.jTextField2.getText());
        assertEquals("1234567890", updateUserPanel.jTextField3.getText());
        assertEquals("JobTitle1", updateUserPanel.jTextField4.getText());
        assertTrue(updateUserPanel.jCheckBox1.isSelected());
        assertTrue(updateUserPanel.jCheckBox2.isSelected());
        assertTrue(updateUserPanel.jCheckBox3.isSelected());
    }

    @Test
    public void testReset() {
        updateUserPanel.reset();
        assertTrue(updateUserPanel.jTextField1.getText().isEmpty());
        assertTrue(updateUserPanel.jTextField2.getText().isEmpty());
        assertTrue(updateUserPanel.jTextField3.getText().isEmpty());
        assertTrue(updateUserPanel.jTextField4.getText().isEmpty());
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertFalse(updateUserPanel.jCheckBox3.isSelected());
    }
}