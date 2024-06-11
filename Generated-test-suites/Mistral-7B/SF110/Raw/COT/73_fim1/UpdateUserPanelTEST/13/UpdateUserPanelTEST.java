import static org.junit.Assert.*;
import osa.ora.server.admin.AdminApp;
import osa.ora.server.admin.ui.UpdateUserPanel;
import osa.ora.server.beans.Group;
import osa.ora.server.beans.User;

import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class UpdateUserPanelTest {
    private AdminApp adminApp;
    private ControlPanel controlPanel;
    private UpdateUserPanel updateUserPanel;

    @Before
    public void setup() {
        adminApp = new AdminApp();
        controlPanel = new ControlPanel();
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
    }

    @Test
    public void testUpdateUserPanelConstructor() {
        assertNotNull(updateUserPanel);
    }

    @Test
    public void testUpdateModel() {
        updateUserPanel.updateModel();

        Group expectedGroup = new Group("Group1", Arrays.asList(new User("User1", "user1@email.com", "1234567890", "JobTitle1", true, true, true),
                new User("User2", "user2@email.com", "2345678901", "JobTitle2", false, false, false)));
        adminApp.setGroups(Collections.singletonList(expectedGroup));

        User[] expectedUsers = {expectedGroup.getUsers().get(0), expectedGroup.getUsers().get(1)};
        DefaultComboBoxModel<User> expectedUserModel = new DefaultComboBoxModel<>(expectedUsers);

        assertEquals(expectedUserModel, updateUserPanel.jComboBox1.getModel());
        assertEquals("User1", updateUserPanel.jComboBox2.getSelectedItem().getName());
    }

    @Test
    public void testInitComponents() {
        updateUserPanel.initComponents();

        // Check labels
        assertLabelTextEquals("Username:", updateUserPanel.jLabel1);
        assertLabelTextEquals("Login Email:", updateUserPanel.jLabel2);
        assertLabelTextEquals("Phone:", updateUserPanel.jLabel3);

        // Check checkboxes
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertFalse(updateUserPanel.jCheckBox3.isSelected());

        // Check textfields
        assertEquals("", updateUserPanel.jTextField1.getText());
        assertEquals("", updateUserPanel.jTextField2.getText());
        assertEquals("", updateUserPanel.jTextField3.getText());
        assertEquals("", updateUserPanel.jTextField4.getText());

        // Check buttons
        assertNull(updateUserPanel.jButton1.getActionCommand());
        assertNull(updateUserPanel.jButton2.getActionCommand());
        assertNull(updateUserPanel.jButton3.getActionCommand());
        assertNull(updateUserPanel.jButton4.getActionCommand());
    }

    @Test
    public void testJButton1ActionPerformed() {
        updateUserPanel.jButton1ActionPerformed(null);

        assertTrue(controlPanel.getParentDialog().isVisible());
        assertTrue(updateUserPanel.reset());
    }

    @Test
    public void testJButton2ActionPerformedWithNullUser() {
        updateUserPanel.jComboBox2.setSelectedItem(null);
        updateUserPanel.jButton2ActionPerformed(null);

        String errorMessage = "Can't identify selected User!";
        assertEquals(errorMessage, updateUserPanel.getLastErrorMessage());
    }

    @Test
    public void testJButton2ActionPerformedWithInvalidUserIndex() {
        updateUserPanel.jComboBox2.setSelectedIndex(-1);
        updateUserPanel.jButton2ActionPerformed(null);

        String errorMessage = "Can't identify selected User!";
        assertEquals(errorMessage, updateUserPanel.getLastErrorMessage());
    }

    @Test
    public void testJButton2ActionPerformedWithSuccess() {
        Group group = new Group("Group1", Arrays.asList(new User("User1", "user1@email.com", "1234567890", "JobTitle1", true, true, true),
                new User("User2", "user2@email.com", "2345678901", "JobTitle2", false, false, false)));
        adminApp.setGroups(Collections.singletonList(group));

        User user = group.getUsers().get(0);
        updateUserPanel.jComboBox1.setSelectedItem(group);
        updateUserPanel.jComboBox2.setSelectedItem(user);

        updateUserPanel.jButton2ActionPerformed(null);

        User deletedUser = adminApp.delUser(user);
        assertEquals(deletedUser, group.getUsers().get(0));

        assertFalse(updateUserPanel.jComboBox2.getItemCount() > 0);
        assertEquals("User Deleted Successfully!", updateUserPanel.getLastSuccessMessage());
    }

    @Test
    public void testJButton3ActionPerformedWithNullUser() {
        updateUserPanel.jComboBox2.setSelectedItem(null);
        updateUserPanel.jButton3ActionPerformed(null);

        String errorMessage = "Can't identify selected User!";
        assertEquals(errorMessage, updateUserPanel.getLastErrorMessage());
    }

    @Test
    public void testJButton3ActionPerformedWithSuccess() {
        Group group = new Group("Group1", Arrays.asList(new User("User1", "user1@email.com", "1234567890", "JobTitle1", true, true, true),
                new User("User2", "user2@email.com", "2345678901", "JobTitle2", false, false, false)));
        adminApp.setGroups(Collections.singletonList(group));

        User user = group.getUsers().get(0);
        updateUserPanel.jComboBox1.setSelectedItem(group);
        updateUserPanel.jComboBox2.setSelectedItem(user);

        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("newuser@email.com");

        updateUserPanel.jButton3ActionPerformed(null);

        User updatedUser = adminApp.getUsers().get(0);
        assertEquals("NewUserName", updatedUser.getName());
        assertEquals("newuser@email.com", updatedUser.getEmail());
        assertEquals("N/A", updatedUser.getDirectPhone());
        assertEquals("N/A", updatedUser.getJobTitle());
        assertEquals(true, updatedUser.isShowMyStatus());
        assertEquals(false, updatedUser.isOnlyStartChat());
        assertEquals(false, updatedUser.isCanJoinChatRoom());

        assertEquals("User Password Reset Successfully!", updateUserPanel.getLastSuccessMessage());
    }

    @Test
    public void testJButton4ActionPerformedWithInvalidEmail() {
        Group group = new Group("Group1", Arrays.asList(new User("User1", "user1@email.com", "1234567890", "JobTitle1", true, true, true),
                new User("User2", "user2@email.com", "2345678901", "JobTitle2", false, false, false)));
        adminApp.setGroups(Collections.singletonList(group));

        User user = group.getUsers().get(0);
        updateUserPanel.jComboBox1.setSelectedItem(group);
        updateUserPanel.jComboBox2.setSelectedItem(user);

        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("user2@email.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("NewJobTitle");

        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);

        updateUserPanel.jButton4ActionPerformed(null);

        String errorMessage = "User with Same email Already Exist!";
        assertEquals(errorMessage, updateUserPanel.getLastErrorMessage());
    }

    @Test
    public void testJComboBox1ItemStateChanged() {
        Group group1 = new Group("Group1", Arrays.asList(new User("User1", "user1@email.com", "1234567890", "JobTitle1", true, true, true),
                new User("User2", "user2@email.com", "2345678901", "JobTitle2", false, false, false)));
        Group group2 = new Group("Group2", Arrays.asList(new User("User3", "user3@email.com", "3456789012", "JobTitle3", true, true, true),
                new User("User4", "user4@email.com", "4567890123", "JobTitle4", false, false, false)));
        adminApp.setGroups(Arrays.asList(group1, group2));

        updateUserPanel.jComboBox1.setSelectedItem(group1);

        updateUserPanel.jComboBox1ItemStateChanged(null);

        assertEquals(group1, updateUserPanel.jComboBox1.getSelectedItem());
        assertEquals(group1.getUsers().get(0), updateUserPanel.jComboBox2.getSelectedItem());
    }

    @Test
    public void testJComboBox2ItemStateChanged() {
        Group group = new Group("Group1", Arrays.asList(new User("User1", "user1@email.com", "1234567890", "JobTitle1", true, true, true),
                new User("User2", "user2@email.com", "2345678901", "JobTitle2", false, false, false)));
        adminApp.setGroups(Collections.singletonList(group));

        updateUserPanel.jComboBox1.setSelectedItem(group);

        updateUserPanel.jComboBox2ItemStateChanged(null);

        assertEquals(group.getUsers().get(0), updateUserPanel.jComboBox2.getSelectedItem());
        assertEquals(group.getUsers().get(0).getName(), updateUserPanel.jTextField1.getText());
        assertEquals(group.getUsers().get(0).getEmail(), updateUserPanel.jTextField2.getText());
        assertEquals(group.getUsers().get(0).getDirectPhone(), updateUserPanel.jTextField3.getText());
        assertEquals(group.getUsers().get(0).getJobTitle(), updateUserPanel.jTextField4.getText());
    }
}