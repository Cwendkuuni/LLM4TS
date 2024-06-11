package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import org.junit.*;
import org.junit.Test;

public class UpdateUserPanelTest {

    UpdateUserPanel updateUserPanel;
    AdminApp adminApp = new AdminApp();
    Group group = new Group("Group1", new String[]{"User1", "User2", "User3"});
    User user = new User("User1", "user1@example.com", "1234567890", "Job Title1", true, false, true);

    @Before
    public void setUp() {
        adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
        group.setUsers(new Vector<User>(Arrays.asList(user)));
        updateUserPanel = new UpdateUserPanel(adminApp, new ControlPanel());
    }

    @Test
    public void testUpdateUserPanelConstructor() {
        assertNotNull(updateUserPanel);
    }

    @Test
    public void testUpdateModel() {
        updateUserPanel.updateModel();
        assertNotNull(updateUserPanel.jComboBox2);
        assertEquals(user.getName(), updateUserPanel.jTextField1.getText());
        assertEquals(user.getEmail(), updateUserPanel.jTextField2.getText());
        assertEquals(user.getDirectPhone(), updateUserPanel.jTextField3.getText());
        assertEquals(user.getJobTitle(), updateUserPanel.jTextField4.getText());
        assertEquals(true, updateUserPanel.jCheckBox1.isSelected());
        assertEquals(user.isOnlyStartChat(), updateUserPanel.jCheckBox2.isSelected());
        assertEquals(user.isCanJoinChatRoom(), updateUserPanel.jCheckBox3.isSelected());
        assertEquals(group.getName(), updateUserPanel.jComboBox1.getSelectedItem().toString());
        assertEquals(user.getName(), updateUserPanel.jComboBox2.getSelectedItem().toString());
    }

    @Test
    public void testJButton1ActionPerformed() {
        updateUserPanel.jButton1.doClick();
        assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible() == false);
        assertEquals("", updateUserPanel.jTextField1.getText());
        assertEquals("", updateUserPanel.jTextField2.getText());
        assertEquals("", updateUserPanel.jTextField3.getText());
        assertEquals("", updateUserPanel.jTextField4.getText());
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertFalse(updateUserPanel.jCheckBox3.isSelected());
    }

    @Test
    public void testJButton2ActionPerformedWithInvalidUser() {
        User invalidUser = null;
        updateUserPanel.jComboBox2.setSelectedItem(invalidUser);
        updateUserPanel.jButton2.doClick();
        assertTrue(JOptionPane.showMessageDialog(updateUserPanel, "Can't identify selected User!", "Error", 0, adminApp.getErrorIcon()).equals("Can't identify selected User!"));
    }

    @Test
    public void testJButton2ActionPerformedWithNullUser() {
        User nullUser = null;
        updateUserPanel.jComboBox2.setSelectedItem(nullUser);
        updateUserPanel.jButton2.doClick();
        assertTrue(JOptionPane.showMessageDialog(updateUserPanel, "Can't identify selected User!", "Error", 0, adminApp.getErrorIcon()).equals("Can't identify selected User!"));
    }

    @Test
    public void testJButton2ActionPerformedWithExistingEmail() {
        User existingUser = new User("User2", "user1@example.com", "1234567890", "Job Title2", true, false, true);
        group.getUsers().add(existingUser);
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jTextField2.setText("user1@example.com");
        updateUserPanel.jButton2.doClick();
        assertTrue(JOptionPane.showMessageDialog(updateUserPanel, "User with Same email Already Exist!", "Error", 0, adminApp.getErrorIcon()).equals("User with Same email Already Exist!"));
    }

    @Test
    public void testJButton2ActionPerformedWithSuccess() {
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jTextField2.setText("user1@example2.com");
        updateUserPanel.jButton2.doClick();
        User deletedUser = adminApp.delUser(user);
        assertTrue(deletedUser != null);
        assertEquals(group.getUsers().size(), 2);
        assertEquals(group.getUsers().get(1).getName(), updateUserPanel.jComboBox2.getSelectedItem().toString());
    }

    @Test
    public void testJButton3ActionPerformedWithInvalidUser() {
        User invalidUser = null;
        updateUserPanel.jComboBox2.setSelectedItem(invalidUser);
        updateUserPanel.jButton3.doClick();
        assertTrue(JOptionPane.showMessageDialog(updateUserPanel, "Can't identify selected User!", "Error", 0, adminApp.getErrorIcon()).equals("Can't identify selected User!"));
    }

    @Test
    public void testJButton3ActionPerformedWithNullUser() {
        User nullUser = null;
        updateUserPanel.jComboBox2.setSelectedItem(nullUser);
        updateUserPanel.jButton3.doClick();
        assertTrue(JOptionPane.showMessageDialog(updateUserPanel, "Can't identify selected User!", "Error", 0, adminApp.getErrorIcon()).equals("Can't identify selected User!"));
    }

    @Test
    public void testJButton3ActionPerformedWithSuccess() {
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jTextField1.setText("User1_Updated");
        updateUserPanel.jTextField2.setText("user1_updated@example.com");
        updateUserPanel.jTextField3.setText("1234567891");
        updateUserPanel.jTextField4.setText("Job Title_Updated");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton3.doClick();
        User updatedUser = adminApp.getGroups().get(0).getUsers().get(0);
        assertTrue(updatedUser.getName().equals("User1_Updated"));
        assertTrue(updatedUser.getEmail().equals("user1_updated@example.com"));
        assertTrue(updatedUser.getDirectPhone().equals("1234567891"));
        assertTrue(updatedUser.getJobTitle().equals("Job Title_Updated"));
        assertTrue(updatedUser.isShowMyStatus());
        assertFalse(updatedUser.isOnlyStartChat());
        assertTrue(updatedUser.isCanJoinChatRoom());
    }

    @Test
    public void testJButton4ActionPerformedWithInvalidName() {
        updateUserPanel.jTextField1.setText(" ");
        updateUserPanel.jButton4.doClick();
        assertTrue(JOptionPane.showMessageDialog(updateUserPanel, "Please Enter User Name", "Error", 0, adminApp.getErrorIcon()).equals("Please Enter User Name"));
    }

    @Test
    public void testJButton4ActionPerformedWithInvalidEmail() {
        updateUserPanel.jTextField2.setText("");
        updateUserPanel.jButton4.doClick();
        assertTrue(JOptionPane.showMessageDialog(updateUserPanel, "Please Enter User email", "Error", 0, adminApp.getErrorIcon()).equals("Please Enter User email"));
    }

    @Test
    public void testJButton4ActionPerformedWithExistingEmail() {
        User existingUser = new User("User2", "user1@example.com", "1234567890", "Job Title2", true, false, true);
        group.getUsers().add(existingUser);
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jTextField2.setText("user1@example.com");
        updateUserPanel.jButton4.doClick();
        assertTrue(JOptionPane.showMessageDialog(updateUserPanel, "User with Same email Already Exist!", "Error", 0, adminApp.getErrorIcon()).equals("User with Same email Already Exist!"));
    }

    @Test
    public void testJButton4ActionPerformedWithSuccess() {
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jTextField1.setText("User1_Updated");
        updateUserPanel.jTextField2.setText("user1_updated@example.com");
        updateUserPanel.jTextField3.setText("1234567891");
        updateUserPanel.jTextField4.setText("Job Title_Updated");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4.doClick();
        User updatedUser = adminApp.getGroups().get(0).getUsers().get(0);
        assertTrue(updatedUser.getName().equals("User1_Updated"));
        assertTrue(updatedUser.getEmail().equals("user1_updated@example.com"));
        assertTrue(updatedUser.getDirectPhone().equals("1234567891"));
        assertTrue(updatedUser.getJobTitle().equals("Job Title_Updated"));
        assertTrue(updatedUser.isShowMyStatus());
        assertFalse(updatedUser.isOnlyStartChat());
        assertTrue(updatedUser.isCanJoinChatRoom());
    }
}