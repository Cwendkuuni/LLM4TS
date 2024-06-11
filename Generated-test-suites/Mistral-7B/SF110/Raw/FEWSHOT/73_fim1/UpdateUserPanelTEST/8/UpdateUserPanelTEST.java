package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import org.junit.Test;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

    @Test
    public void testUpdateUserPanel() {
        AdminApp adminApp = new AdminApp();
        Group group = new Group("Group1");
        User user = new User("User1", "user1@example.com", "1234567890", "User1 Title", true, false, true);
        group.addUser(user);
        adminApp.setGroups(new Vector<Group>() {{
            add(group);
        }});

        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.updateModel();

        assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().getName());
        assertEquals("user1@example.com", updateUserPanel.jTextField2.getText());
        assertEquals("1234567890", updateUserPanel.jTextField3.getText());
        assertTrue(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertTrue(updateUserPanel.jCheckBox3.isSelected());

        updateUserPanel.jTextField1.setText("New User Name");
        updateUserPanel.jTextField2.setText("new.user@example.com");
        updateUserPanel.jTextField3.setText("New Phone Number");
        updateUserPanel.jCheckBox1.setSelected(false);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(false);
        updateUserPanel.jButton4ActionPerformed(null);

        assertEquals("New User Name", user.getName());
        assertEquals("new.user@example.com", user.getEmail());
        assertNull(user.getDirectPhone());
        assertFalse(user.isShowMyStatus());
        assertTrue(user.isOnlyStartChat());
        assertFalse(user.isCanJoinChatRoom());

        updateUserPanel.jButton2ActionPerformed(null);

        assertNull(group.getUsers().get(0));
        assertEquals(0, group.getUsers().size());
        assertTrue(adminApp.getGroups().get(0).isVisible());
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateUserPanelNullGroup() {
        AdminApp adminApp = new AdminApp();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.updateModel();
        updateUserPanel.jButton4ActionPerformed(null);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateUserPanelNullUser() {
        AdminApp adminApp = new AdminApp();
        Group group = new Group("Group1");
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, group);
        updateUserPanel.updateModel();
        updateUserPanel.jComboBox2.setSelectedIndex(-1);
        updateUserPanel.jButton4ActionPerformed(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateUserPanelDuplicateEmail() {
        AdminApp adminApp = new AdminApp();
        Group group1 = new Group("Group1");
        User user1 = new User("User1", "user1@example.com", "1234567890", "User1 Title", true, false, true);
        group1.addUser(user1);
        adminApp.setGroups(new Vector<Group>() {{
            add(group1);
        }});

        Group group2 = new Group("Group2");
        User user2 = new User("User2", "user1@example.com", "2345678901", "User2 Title", true, false, true);
        group2.addUser(user2);
        adminApp.setGroups(new Vector<Group>() {{
            add(group1);
            add(group2);
        }});

        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.updateModel();

        updateUserPanel.jComboBox1.setSelectedIndex(1);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField2.setText("user1@example.com");
        updateUserPanel.jButton4ActionPerformed(null);
    }
}