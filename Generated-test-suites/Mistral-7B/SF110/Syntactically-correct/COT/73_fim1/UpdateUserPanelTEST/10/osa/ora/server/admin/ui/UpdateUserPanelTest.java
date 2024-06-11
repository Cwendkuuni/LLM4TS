package osa.ora.server.admin.ui;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {
    private UpdateUserPanel updateUserPanel;
    private AdminApp adminApp;

    @Before
    public void setUp() {
        adminApp = new AdminApp();
        updateUserPanel = new UpdateUserPanel(adminApp, null);
    }

    @Test
    public void testUpdateModel() {
        updateUserPanel.updateModel();
        assertNotNull(updateUserPanel.jComboBox1);
        assertNotNull(updateUserPanel.jComboBox2);
    }

    @Test
    public void testInitComponents() {
        updateUserPanel.initComponents();
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
    }

    @Test
    public void testJButton1ActionPerformed() {
        updateUserPanel.jButton1 = new JButton("Test Button");
        updateUserPanel.jButton1.addActionListener(e -> updateUserPanel.jButton1ActionPerformed(e));
        updateUserPanel.jButton1.doClick();
        assertTrue(updateUserPanel.isVisible() == false);
    }

    @Test
    public void testJButton2ActionPerformedWithNullUser() {
        updateUserPanel.jComboBox2.setModel(new DefaultComboBoxModel<User>(new User[0]));
        updateUserPanel.jButton2.doClick();
        assertTrue(updateUserPanel.isVisible() == true);
    }

    @Test
    public void testJButton2ActionPerformedWithValidUser() {
        Group group = new Group("Group 1");
        User user = new User("User 1", "user1@example.com", "1234567890", "Title 1", true, true, true);
        group.addUser(user);
        adminApp.setGroups(Arrays.asList(group));
        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1.setSelectedItem(group);
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jButton2.doClick();
        assertTrue(updateUserPanel.isVisible() == true);
        assertEquals(adminApp.getGroups().get(0).getUsers().size(), 0);
    }

    @Test(expected = Exception.class)
    public void testJButton2ActionPerformedWithNullGroup() {
        updateUserPanel.jComboBox1.setSelectedItem(null);
        updateUserPanel.jButton2.doClick();
    }

    @Test(expected = Exception.class)
    public void testJButton2ActionPerformedWithNullUser() {
        updateUserPanel.jComboBox2.setSelectedItem(null);
        updateUserPanel.jButton2.doClick();
    }

    @Test
    public void testJButton3ActionPerformed() {
        Group group = new Group("Group 1");
        User user = new User("User 1", "user1@example.com", "1234567890", "Title 1", true, true, true);
        group.addUser(user);
        adminApp.setGroups(Arrays.asList(group));
        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1.setSelectedItem(group);
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jButton3.doClick();
        assertTrue(updateUserPanel.isVisible() == true);
        assertEquals(user.getEmail(), "user1@example.com");
        assertEquals(user.getPassword(), "1234567890");
        assertFalse(user.isPasswordReset());
    }

    @Test(expected = Exception.class)
    public void testJButton3ActionPerformedWithNullGroup() {
        updateUserPanel.jComboBox1.setSelectedItem(null);
        updateUserPanel.jButton3.doClick();
    }

    @Test(expected = Exception.class)
    public void testJButton3ActionPerformedWithNullUser() {
        updateUserPanel.jComboBox2.setSelectedItem(null);
        updateUserPanel.jButton3.doClick();
    }

    @Test
    public void testJButton4ActionPerformed() {
        Group group = new Group("Group 1");
        User user = new User("User 1", "user1@example.com", "1234567890", "Title 1", true, true, true);
        group.addUser(user);
        adminApp.setGroups(Arrays.asList(group));
        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1.setSelectedItem(group);
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jTextField1.setText("New Username");
        updateUserPanel.jTextField2.setText("new.user@example.com");
        updateUserPanel.jTextField3.setText("New Phone");
        updateUserPanel.jTextField4.setText("New Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4.doClick();
        assertTrue(updateUserPanel.isVisible() == true);
        assertEquals(user.getName(), "New Username");
        assertEquals(user.getEmail(), "new.user@example.com");
        assertEquals(user.getDirectPhone(), "New Phone");
        assertEquals(user.getJobTitle(), "New Title");
        assertTrue(user.isShowMyStatus());
        assertFalse(user.isOnlyStartChat());
        assertTrue(user.isCanJoinChatRoom());
    }

    @Test(expected = Exception.class)
    public void testJButton4ActionPerformedWithNullGroup() {
        updateUserPanel.jComboBox1.setSelectedItem(null);
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jTextField1.setText("New Username");
        updateUserPanel.jTextField2.setText("new.user@example.com");
        updateUserPanel.jTextField3.setText("New Phone");
        updateUserPanel.jTextField4.setText("New Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4.doClick();
    }

    @Test(expected = Exception.class)
    public void testJButton4ActionPerformedWithNullUser() {
        updateUserPanel.jComboBox1.setSelectedItem(group);
        updateUserPanel.jComboBox2.setSelectedItem(null);
        updateUserPanel.jTextField1.setText("New Username");
        updateUserPanel.jTextField2.setText("new.user@example.com");
        updateUserPanel.jTextField3.setText("New Phone");
        updateUserPanel.jTextField4.setText("New Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4.doClick();
    }

    @Test
    public void testJComboBox1ItemStateChanged() {
        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox1ItemStateChanged(null);
        assertTrue(updateUserPanel.jComboBox2.isVisible());
    }
}