import static org.junit.Assert.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import org.junit.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class UpdateUserPanelTest {

    private AdminApp adminApp;
    private ControlPanel controlPanel;
    private UpdateUserPanel updateUserPanel;

    @Before
    public void setUp() {
        adminApp = new AdminApp();
        controlPanel = new ControlPanel();
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
    }

    

    @Test
    public void testUpdateUserPanelConstructor() {
        assertNotNull(updateUserPanel);
        assertTrue(updateUserPanel instanceof JPanel);
    }

    @Test
    public void testUpdateModel() {
        updateUserPanel.updateModel();
        assertNotNull(updateUserPanel.jComboBox1);
        assertNotNull(updateUserPanel.jComboBox2);

        Group group = new Group("Group1");
        User user = new User("User1", "user1@example.com", "123456", "N/A", true, true, true);
        group.addUser(user);
        adminApp.setGroups(new Vector<>(Arrays.asList(group)));

        assertEquals(group.getName(), updateUserPanel.jComboBox1.getSelectedItem().toString());
        assertEquals(user.getName(), updateUserPanel.jComboBox2.getSelectedItem().toString());
    }

    @Test
    public void testInitComponents() {
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

    @Test(expected = NullPointerException.class)
    public void testUpdateUserPanelConstructorNullAdminApp() {
        updateUserPanel = new UpdateUserPanel(null, controlPanel);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateUserPanelConstructorNullControlPanel() {
        updateUserPanel = new UpdateUserPanel(adminApp, null);
    }

    @Test
    public void testJButton1ActionPerformed() {
        updateUserPanel.jButton1ActionPerformed(new ActionEvent(new JButton(), 0, "Cancel"));
        assertTrue(updateUserPanel.isVisible() == false);
        updateUserPanel.reset();
    }

    @Test
    public void testJButton2ActionPerformedWithValidUser() {
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(new JButton(), 0, "Delete"));

        Group group = (Group)updateUserPanel.jComboBox1.getSelectedItem();
        User user = (User)updateUserPanel.jComboBox2.getSelectedItem();
        assertEquals(group.getUsers().size(), 0);
        assertNull(user);
        JOptionPane.showMessageDialog(updateUserPanel, "User Deleted Successfully!");
    }

    @Test(expected = NullPointerException.class)
    public void testJButton2ActionPerformedNullSelectedItem() {
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(-1);
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(new JButton(), 0, "Delete"));
    }

    @Test(expected = NullPointerException.class)
    public void testJButton2ActionPerformedNullUser() {
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(new JButton(), 0, "Delete"));
        User user = (User)updateUserPanel.jComboBox2.getSelectedItem();
        assertNull(user);
    }

    @Test
    public void testJButton3ActionPerformedWithValidUser() {
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jButton3ActionPerformed(new ActionEvent(new JButton(), 0, "Reset Password"));
        User user = (User)updateUserPanel.jComboBox2.getSelectedItem();
        String oldPassword = user.getPassword();
        updateUserPanel.adminApp.resetUserPass(user);
        String newPassword = user.getPassword();
        assertNotEquals(oldPassword, newPassword);
        JOptionPane.showMessageDialog(updateUserPanel, "User Password Reset Successfully!");
    }

    @Test(expected = NullPointerException.class)
    public void testJButton3ActionPerformedNullSelectedItem() {
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(-1);
        updateUserPanel.jButton3ActionPerformed(new ActionEvent(new JButton(), 0, "Reset Password"));
    }

    @Test
    public void testJButton4ActionPerformedWithValidUser() {
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);

        User user = (User)updateUserPanel.jComboBox2.getSelectedItem();
        String oldName = user.getName();
        String oldEmail = user.getEmail();
        String oldPhone = user.getDirectPhone();
        String oldJobTitle = user.getJobTitle();
        boolean oldShowMyStatus = user.isShowMyStatus();
        boolean oldOnlyStartChat = user.isOnlyStartChat();
        boolean oldCanJoinChatRoom = user.isCanJoinChatRoom();

        updateUserPanel.jTextField1.setText("NewName");
        updateUserPanel.jTextField2.setText("new.email@example.com");
        updateUserPanel.jTextField3.setText("123457");
        updateUserPanel.jTextField4.setText("New Job Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new JButton(), 0, "Update"));

        User newUser = (User)updateUserPanel.jComboBox2.getSelectedItem();
        assertEquals(updateUserPanel.jTextField1.getText(), newUser.getName());
        assertEquals(updateUserPanel.jTextField2.getText(), newUser.getEmail());
        assertEquals(updateUserPanel.jTextField3.getText(), newUser.getDirectPhone());
        assertEquals(updateUserPanel.jTextField4.getText(), newUser.getJobTitle());
        assertTrue(newUser.isShowMyStatus() == oldShowMyStatus);
        assertFalse(newUser.isOnlyStartChat() == oldOnlyStartChat);
        assertTrue(newUser.isCanJoinChatRoom() == oldCanJoinChatRoom);
        JOptionPane.showMessageDialog(updateUserPanel, "User Updated Successfully!");
    }

    @Test(expected = NullPointerException.class)
    public void testJButton4ActionPerformedNullSelectedItem() {
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(-1);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new JButton(), 0, "Update"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton4ActionPerformedWithEmptyName() {
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField1.setText("");
        updateUserPanel.jTextField2.setText("new.email@example.com");
        updateUserPanel.jTextField3.setText("123457");
        updateUserPanel.jTextField4.setText("New Job Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new JButton(), 0, "Update"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton4ActionPerformedWithEmptyEmail() {
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField1.setText("NewName");
        updateUserPanel.jTextField2.setText("");
        updateUserPanel.jTextField3.setText("123457");
        updateUserPanel.jTextField4.setText("New Job Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new JButton(), 0, "Update"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton4ActionPerformedWithInvalidEmailFormat() {
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField1.setText("NewName");
        updateUserPanel.jTextField2.setText(" example.com");
        updateUserPanel.jTextField3.setText("123457");
        updateUserPanel.jTextField4.setText("New Job Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new JButton(), 0, "Update"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton4ActionPerformedWithDuplicateEmail() {
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        Group group = new Group("Group1");
        User user1 = new User("User1", "user1@example.com", "123456", "N/A", true, true, true);
        User user2 = new User("User2", "user1@example.com", "123457", "N/A", true, true, true);
        group.addUser(user1);
        adminApp.setGroups(new Vector<>(Arrays.asList(group)));
        updateUserPanel.jComboBox2.setModel(new DefaultComboBoxModel<User>(group.getUsers()));
        updateUserPanel.jComboBox2.setSelectedIndex(0);

        updateUserPanel.jTextField1.setText("NewName");
        updateUserPanel.jTextField2.setText("user1@example.com");
        updateUserPanel.jTextField3.setText("123457");
        updateUserPanel.jTextField4.setText("New Job Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new JButton(), 0, "Update"));
    }

    
}