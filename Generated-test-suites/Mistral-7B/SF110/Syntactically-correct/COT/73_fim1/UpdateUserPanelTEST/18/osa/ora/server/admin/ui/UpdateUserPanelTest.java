package osa.ora.server.admin.ui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import osa.ora.server.admin.AdminApp;
import osa.ora.server.admin.Group;
import osa.ora.server.beans.User;

public class UpdateUserPanelTest {

    private AdminApp adminApp;
    private UpdateUserPanel updateUserPanel;
    private Group group;
    private User user;

    @Before
    public void setUp() {
        adminApp = new AdminApp();
        group = new Group("Group1");
        user = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
        group.getUsers().add(user);
        adminApp.setGroups(new Vector<Group>() {{
            add(group);
        }});

        updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.updateModel();
    }

    @Test
    public void testUpdateUserPanelConstructor() {
        assertNotNull(updateUserPanel);
    }

    @Test
    public void testUpdateModel() {
        assertEquals(1, updateUserPanel.jComboBox1.getItemCount());
        assertEquals("Group1", updateUserPanel.jComboBox1.getItemAt(0).toString());

        assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
        assertEquals("User1", updateUserPanel.jComboBox2.getItemAt(0).toString());
    }

    @Test
    public void testJButton1ActionPerformed() {
        updateUserPanel.jButton1.doClick();
        assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible());
        updateUserPanel.reset();
    }

    @Test
    public void testJButton2ActionPerformedWithValidUser() {
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jButton2.doClick();
        assertEquals(0, group.getUsers().size());
        assertTrue(updateUserPanel.jComboBox2.getItemCount() == 0);
        assertTrue(updateUserPanel.jComboBox1.isSelectedIndex(0));
        JOptionPane.showMessageDialog(updateUserPanel, "User Deleted Successfully!");
    }

    @Test
    public void testJButton2ActionPerformedWithInvalidUserIndex() {
        updateUserPanel.jComboBox2.setSelectedIndex(-1);
        updateUserPanel.jButton2.doClick();
        assertEquals(1, group.getUsers().size());
        assertTrue(updateUserPanel.jComboBox2.getItemCount() == 1);
        JOptionPane.showMessageDialog(updateUserPanel, "Can't identify selected User!");
    }

    @Test
    public void testJButton2ActionPerformedWithNullUser() {
        updateUserPanel.jComboBox2.setSelectedItem(null);
        updateUserPanel.jButton2.doClick();
        assertEquals(1, group.getUsers().size());
        assertTrue(updateUserPanel.jComboBox2.getItemCount() == 1);
        JOptionPane.showMessageDialog(updateUserPanel, "Can't identify selected User!");
    }

    @Test
    public void testJButton3ActionPerformed() {
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("new.user@example.com");
        updateUserPanel.jButton3.doClick();
        assertEquals("NewUserName", user.getName());
        assertEquals("new.user@example.com", user.getEmail());
        JOptionPane.showMessageDialog(updateUserPanel, "User Password Reset Successfully!");
    }

    @Test
    public void testJButton4ActionPerformedWithValidUser() {
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("new.user@example.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("NewTitle");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4.doClick();
        assertEquals("NewUserName", user.getName());
        assertEquals("new.user@example.com", user.getEmail());
        assertEquals("1234567890", user.getDirectPhone());
        assertEquals("NewTitle", user.getJobTitle());
        assertEquals(true, user.isShowMyStatus());
        assertEquals(true, user.isOnlyStartChat());
        assertEquals(true, user.isCanJoinChatRoom());
        JOptionPane.showMessageDialog(updateUserPanel, "User Updated Successfully!");
    }

    @Test
    public void testJButton4ActionPerformedWithEmptyName() {
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField1.setText("");
        updateUserPanel.jButton4.doClick();
        JOptionPane.showMessageDialog(updateUserPanel, "Please Enter User Name");
    }

    @Test
    public void testJButton4ActionPerformedWithEmptyEmail() {
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField2.setText("");
        updateUserPanel.jButton4.doClick();
        JOptionPane.showMessageDialog(updateUserPanel, "Please Enter User email");
    }

    @Test
    public void testJButton4ActionPerformedWithExistingEmail() {
        User existingUser = new User("ExistingUser", "new.user@example.com", "1234567890", "Title1", true, true, true);
        group.getUsers().add(existingUser);
        updateUserPanel.jComboBox2.setSelectedIndex(1);
        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("new.user@example.com");
        updateUserPanel.jButton4.doClick();
        assertEquals(1, group.getUsers().size());
        JOptionPane.showMessageDialog(updateUserPanel, "User with Same email Already Exist!");
    }
}