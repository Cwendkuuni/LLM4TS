import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {
    private UpdateUserPanel updateUserPanel;
    private AdminApp adminApp;
    private Group group;
    private User user;

    @Before
    public void setUp() {
        adminApp = new AdminApp();
        group = new Group("Group1");
        user = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
        group.addUser(user);
        updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.updateModel();
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
        updateUserPanel.jButton1ActionPerformed(null);
        assertEquals("", updateUserPanel.jTextField1.getText());
        assertEquals("", updateUserPanel.jTextField2.getText());
        assertEquals("", updateUserPanel.jTextField3.getText());
        assertEquals("", updateUserPanel.jTextField4.getText());
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertFalse(updateUserPanel.jCheckBox3.isSelected());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton2ActionPerformedWithNullUser() {
        updateUserPanel.jButton2 = null;
        updateUserPanel.jComboBox2ItemStateChanged(null);
        updateUserPanel.jButton2ActionPerformed(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testJButton2ActionPerformedWithInvalidIndex() {
        updateUserPanel.jComboBox2.setSelectedIndex(-1);
        updateUserPanel.jButton2ActionPerformed(null);
    }

    @Test
    public void testJButton2ActionPerformedWithValidUser() {
        updateUserPanel.jButton2ActionPerformed(null);
        User deletedUser = adminApp.delUser(user);
        assertTrue(deletedUser.isDeleted());
        assertEquals(0, group.getUsers().size());
        updateUserPanel.updateModel();
        assertEquals(0, updateUserPanel.jComboBox2.getItemCount());
        JOptionPane.showMessageDialog(updateUserPanel, "User Deleted Successfully!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton3ActionPerformedWithNullUser() {
        updateUserPanel.jComboBox2.setSelectedItem(null);
        updateUserPanel.jButton3ActionPerformed(null);
    }

    @Test
    public void testJButton3ActionPerformedWithValidUser() {
        updateUserPanel.jButton3ActionPerformed(null);
        assertEquals("NewTitle1", updateUserPanel.jTextField1.getText());
        assertEquals("newemail@example.com", updateUserPanel.jTextField2.getText());
        assertEquals("9876543210", updateUserPanel.jTextField3.getText());
        assertEquals("NewJobTitle", updateUserPanel.jTextField4.getText());
        assertTrue(updateUserPanel.jCheckBox1.isSelected());
        assertTrue(updateUserPanel.jCheckBox2.isSelected());
        assertTrue(updateUserPanel.jCheckBox3.isSelected());

        User updatedUser = adminApp.getUserByName("User1");
        assertEquals("NewTitle1", updatedUser.getTitle());
        assertEquals("newemail@example.com", updatedUser.getEmail());
        assertEquals("9876543210", updatedUser.getDirectPhone());
        assertEquals("NewJobTitle", updatedUser.getJobTitle());
        assertTrue(updatedUser.isShowMyStatus());
        assertTrue(updatedUser.isOnlyStartChat());
        assertTrue(updatedUser.isCanJoinChatRoom());
    }

    @Test
    public void testJButton4ActionPerformedWithInvalidEmail() {
        updateUserPanel.jTextField2.setText("user1@example.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("NewJobTitle");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(null);
        assertEquals("", updateUserPanel.jTextField2.getText());
        JOptionPane.showMessageDialog(updateUserPanel, "Please Enter User email");
    }

    @Test
    public void testJButton4ActionPerformedWithExistingEmail() {
        User existingUser = new User("User2", "user1@example.com", "1234567890", "Title2", true, true, true);
        group.addUser(existingUser);
        updateUserPanel.jComboBox2.setSelectedIndex(1);

        updateUserPanel.jTextField1.setText("User1");
        updateUserPanel.jTextField2.setText("user1@example.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("NewJobTitle");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(null);
        assertEquals("User1", updateUserPanel.jTextField1.getText());
        JOptionPane.showMessageDialog(updateUserPanel, "User with Same email Already Exist!");
    }
}