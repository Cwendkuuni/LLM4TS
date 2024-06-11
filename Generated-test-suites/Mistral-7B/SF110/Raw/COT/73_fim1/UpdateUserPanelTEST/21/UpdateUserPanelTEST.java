package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
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
    public void setup() {
        adminApp = new AdminApp();
        group = new Group("Group1");
        user = new User("TestUser1", "testuser1@email.com", "123456789");
        group.addUser(user);
        adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
        updateUserPanel = new UpdateUserPanel(adminApp, new ControlPanel());
    }

    @Test
    public void testUpdateUserPanelConstructor() {
        assertNotNull(updateUserPanel);
    }

    @Test
    public void testUpdateModel() {
        updateUserPanel.updateModel();
        assertEquals(1, updateUserPanel.jComboBox1.getItemCount());
        assertEquals("Group1", updateUserPanel.jComboBox1.getItemAt(0).toString());
        assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
        assertEquals("TestUser1", updateUserPanel.jComboBox2.getItemAt(0).toString());
    }

    @Test
    public void testInitComponents() {
        updateUserPanel.initComponents();
        assertEquals(11, updateUserPanel.getComponentCount());
    }

    @Test
    public void testJButton1ActionPerformed() {
        updateUserPanel.jButton1ActionPerformed(new ActionEvent(new Object(), 0, ""));
        assertTrue(updateUserPanel.isVisible());
        updateUserPanel.reset();
        assertFalse(updateUserPanel.isVisible());
    }

    @Test
    public void testJButton2ActionPerformed_UserExists() {
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 0, ""));
        assertFalse(updateUserPanel.isVisible());
        assertTrue(adminApp.getGroups().get(0).getUsers().remove(user));
        assertEquals(0, updateUserPanel.jComboBox2.getItemCount());
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testJButton2ActionPerformed_UserNull() {
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 0, ""));
    }

    @Test(expected = java.lang.IndexOutOfBoundsException.class)
    public void testJButton2ActionPerformed_NoUserSelected() {
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), 0, ""));
    }

    @Test
    public void testJButton3ActionPerformed() {
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("newuser@email.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jButton3ActionPerformed(new ActionEvent(new Object(), 0, ""));
        assertTrue(group.getUsers().get(0).getName().equals("NewUserName"));
        assertTrue(group.getUsers().get(0).getEmail().equals("newuser@email.com"));
        assertTrue(group.getUsers().get(0).getDirectPhone().equals("1234567890"));
    }

    @Test
    public void testJButton4ActionPerformed_ValidData() {
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("newuser@email.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("Software Tester");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, ""));
        assertTrue(group.getUsers().get(0).isShowMyStatus());
        assertFalse(group.getUsers().get(0).isOnlyStartChat());
        assertTrue(group.getUsers().get(0).isCanJoinChatRoom());
        assertTrue(group.getUsers().get(0).getName().equals("NewUserName"));
        assertTrue(group.getUsers().get(0).getEmail().equals("newuser@email.com"));
        assertTrue(group.getUsers().get(0).getDirectPhone().equals("1234567890"));
        assertTrue(group.getUsers().get(0).getJobTitle().equals("Software Tester"));
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void testJButton4ActionPerformed_NoUserSelected() {
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, ""));
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testJButton4ActionPerformed_InvalidEmail() {
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("email.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("Software Tester");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), 0, ""));
    }

    @Test
    public void testJComboBox1ItemStateChanged() {
        updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(new Object(), 1, 1, ItemEvent.DESELECTED, null));
        assertEquals(1, updateUserPanel.jComboBox1.getItemCount());
        updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(new Object(), 1, 0, ItemEvent.SELECTED, null));
        assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
    }
}