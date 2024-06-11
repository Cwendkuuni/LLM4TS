package osa.ora.server.admin.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import osa.ora.server.admin.AdminApp;
import osa.ora.server.admin.Group;
import osa.ora.server.admin.User;
import osa.ora.server.beans.Vector;

public class UpdateUserPanelTest {

    private UpdateUserPanel updateUserPanel;
    private AdminApp adminApp;
    private Group group;
    private User user;

    @Before
    public void setUp() {
        adminApp = new AdminApp();
        group = new Group("Group1");
        user = new User("User1", "user1@example.com", "1234567890", "User Title", true, true, true);
        group.addUser(user);

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

        updateUserPanel.updateModel();
        assertEquals(1, updateUserPanel.jComboBox1.getItemCount());
        assertEquals("Group1", updateUserPanel.jComboBox1.getItemAt(0).toString());

        assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
        assertEquals("User1", updateUserPanel.jComboBox2.getItemAt(0).toString());
    }

    @Test
    public void testInitComponents() {
        UpdateUserPanel newUpdateUserPanel = spy(new UpdateUserPanel(adminApp, null));

        newUpdateUserPanel.initComponents();

        verify(newUpdateUserPanel).add(argThat(isA(JLabel.class)).withName("Username:"));
        verify(newUpdateUserPanel).add(argThat(isA(JLabel.class)).withName("Login Email:"));
        verify(newUpdateUserPanel).add(argThat(isA(JLabel.class)).withName("Phone:"));
        verify(newUpdateUserPanel).add(argThat(isA(JCheckBox.class)).withText("Show User Status To Others"));
        verify(newUpdateUserPanel).add(argThat(isA(JCheckBox.class)).withText("User Can Only Start Chat With Others"));
        verify(newUpdateUserPanel).add(argThat(isA(JCheckBox.class)).withText("Can be a Member of Chat Rooms"));
        verify(newUpdateUserPanel).add(argThat(isA(JLabel.class)).withName("User Group:"));
        verify(newUpdateUserPanel).add(argThat(isInstanceOf(JComboBox.class)));
        verify(newUpdateUserPanel).add(argThat(isInstanceOf(JTextField.class)).withName("jTextField2"));
        verify(newUpdateUserPanel).add(argThat(isInstanceOf(JTextField.class)).withName("jTextField3"));
        verify(newUpdateUserPanel).add(argThat(isA(JButton.class)).withText("Cancel"));
        verify(newUpdateUserPanel).add(argThat(isA(JButton.class)).withText("Delete"));
        verify(newUpdateUserPanel).add(argThat(isA(JComboBox.class)));
        verify(newUpdateUserPanel).add(argThat(isA(JButton.class)).withText("Reset Password"));
        verify(newUpdateUserPanel).add(argThat(isA(JButton.class)).withText("Update"));
        verify(newUpdateUserPanel).add(argThat(isA(JLabel.class)).withName("Title:"));
        verify(newUpdateUserPanel).add(argThat(isInstanceOf(JTextField.class)).withName("jTextField1"));
        verify(newUpdateUserPanel).add(argThat(isInstanceOf(JTextField.class)).withName("jTextField4"));
        verify(newUpdateUserPanel).add(argThat(isA(JLabel.class)).withName("Username:"));
    }

    @Test
    public void testJButton1ActionPerformed() {
        updateUserPanel.jButton1ActionPerformed(mock(ActionEvent.class));

        verify(updateUserPanel.controlPanel.getParentDialog()).setVisible(false);
        reset(updateUserPanel.controlPanel.getParentDialog());
        verify(updateUserPanel.controlPanel).getParentDialog().setVisible(false);
        verify(updateUserPanel).reset();
    }

    @Test
    public void testJButton2ActionPerformedWithUser() {
        updateUserPanel.jButton2ActionPerformed(mock(ActionEvent.class));

        verify(adminApp).delUser(argThat(isA(User.class)).andReturn(user));
        verify(group).getUsers().remove(0);
        verify(updateUserPanel).reset();
        verify(updateUserPanel.jComboBox2).setSelectedIndex(0);
        verify(updateUserPanel.jComboBox2).validate();
        verify(updateUserPanel.jComboBox1).setSelectedIndex(0);
        verify(updateUserPanel.jComboBox1).validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton2ActionPerformedWithoutUser() {
        updateUserPanel.jComboBox2.setModel(new DefaultComboBoxModel<Object>());
        updateUserPanel.jButton2ActionPerformed(mock(ActionEvent.class));
    }

    @Test
    public void testJButton3ActionPerformed() {
        updateUserPanel.jButton3ActionPerformed(mock(ActionEvent.class));

        verify(adminApp).resetUserPass(argThat(isA(User.class)).andReturn(user));
        verify(updateUserPanel).jTextField1.setText("User1");
        verify(updateUserPanel).jTextField2.setText("user1@example.com");
        verify(updateUserPanel).jTextField3.setText("1234567890");
        verify(updateUserPanel).jTextField4.setText("User Title");
        verify(updateUserPanel.jCheckBox1).setSelected(true);
        verify(updateUserPanel.jCheckBox2).setSelected(true);
        verify(updateUserPanel.jCheckBox3).setSelected(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton3ActionPerformedWithoutUser() {
        updateUserPanel.jComboBox2.setModel(new DefaultComboBoxModel<Object>());
        updateUserPanel.jButton3ActionPerformed(mock(ActionEvent.class));
    }

    @Test
    public void testJButton4ActionPerformed() {
        UpdateUserPanel spyUpdateUserPanel = spy(updateUserPanel);

        doCallRealMethod().when(spyUpdateUserPanel).jButton4ActionPerformed(mock(ActionEvent.class));

        String newName = "NewUserName";
        String newEmail = "newemail@example.com";
        String newDirectPhone = "12345678";
        String newJobTitle = "New Job Title";

        spyUpdateUserPanel.jTextField1.setText(newName);
        spyUpdateUserPanel.jTextField2.setText(newEmail);
        spyUpdateUserPanel.jTextField3.setText(newDirectPhone);
        spyUpdateUserPanel.jTextField4.setText(newJobTitle);
        spyUpdateUserPanel.jCheckBox1.setSelected(true);
        spyUpdateUserPanel.jCheckBox2.setSelected(true);
        spyUpdateUserPanel.jCheckBox3.setSelected(true);

        spyUpdateUserPanel.jComboBox1.setSelectedIndex(0);
        group.getUsers().set(0, user);
        verify(adminApp).updateUser(argThat(isA(User.class)).andReturn(new User(newName, newEmail, newDirectPhone, newJobTitle, true, true, true)));
        verify(group).getUsers().set(0, argThat(isA(User.class)).andReturn(new User(newName, newEmail, newDirectPhone, newJobTitle, true, true, true)));
        verify(spyUpdateUserPanel).jComboBox2.setSelectedIndex(0);
        verify(spyUpdateUserPanel).jComboBox2.validate();
        verify(spyUpdateUserPanel).jTextField1.setText("");
        verify(spyUpdateUserPanel).jTextField2.setText("");
        verify(spyUpdateUserPanel).jTextField3.setText("");
        verify(spyUpdateUserPanel).jTextField4.setText("");
        verify(spyUpdateUserPanel).jCheckBox1.setSelected(false);
        verify(spyUpdateUserPanel).jCheckBox2.setSelected(false);
        verify(spyUpdateUserPanel).jCheckBox3.setSelected(false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton4ActionPerformedWithoutSelectedUser() {
        updateUserPanel.jComboBox2.setModel(new DefaultComboBoxModel<Object>());
        updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));
    }

    @Test
    public void testJComboBox1ItemStateChanged() {
        updateUserPanel.jComboBox1ItemStateChanged(mock(ItemEvent.class));

        verify(adminApp).getGroups().get(0).getUsers();
        verify(updateUserPanel).jComboBox2.setModel(argThat(isA(DefaultComboBoxModel.class)).andReturn(new DefaultComboBoxModel<User>(group.getUsers())));

        if (group.getUsers() != null && group.getUsers().size() > 0) {
            User user = group.getUsers().get(0);
            verify(updateUserPanel).fillUser(user);
        } else {
            verify(updateUserPanel).reset();
        }

        verify(updateUserPanel.jTextField1).setText("");
        verify(updateUserPanel.jTextField2).setText("");
        verify(updateUserPanel.jTextField3).setText("");
        verify(updateUserPanel.jTextField4).setText("");
        verify(updateUserPanel.jCheckBox1).setSelected(false);
        verify(updateUserPanel.jCheckBox2).setSelected(false);
        verify(updateUserPanel.jCheckBox3).setSelected(false);
    }

    @Test
    public void testReset() {
        updateUserPanel.reset();

        verify(updateUserPanel.jTextField1).setText("");
        verify(updateUserPanel.jTextField2).setText("");
        verify(updateUserPanel.jTextField3).setText("");
        verify(updateUserPanel.jTextField4).setText("");
        verify(updateUserPanel.jCheckBox1).setSelected(false);
        verify(updateUserPanel.jCheckBox2).setSelected(false);
        verify(updateUserPanel.jCheckBox3).setSelected(false);
    }

    @Test
    public void testFillUser() {
        updateUserPanel.fillUser(user);

        verify(updateUserPanel.jTextField1).setText("User1");
        verify(updateUserPanel.jTextField2).setText("user1@example.com");
        verify(updateUserPanel.jTextField3).setText("1234567890");
        verify(updateUserPanel.jTextField4).setText("User Title");
        verify(updateUserPanel.jCheckBox1).setSelected(true);
        verify(updateUserPanel.jCheckBox2).setSelected(true);
        verify(updateUserPanel.jCheckBox3).setSelected(true);
    }

    @Test
    public void testJComboBox2ItemStateChanged() {
        updateUserPanel.jComboBox2ItemStateChanged(mock(ItemEvent.class));

        if (updateUserPanel.jComboBox2.getSelectedItem() != null) {
            User selectedUser = (User) updateUserPanel.jComboBox2.getSelectedItem();
            verify(updateUserPanel).fillUser(selectedUser);
        } else {
            verify(updateUserPanel).reset();
        }

        verify(updateUserPanel.jTextField1).setText("");
        verify(updateUserPanel.jTextField2).setText("");
        verify(updateUserPanel.jTextField3).setText("");
        verify(updateUserPanel.jTextField4).setText("");
        verify(updateUserPanel.jCheckBox1).setSelected(false);
        verify(updateUserPanel.jCheckBox2).setSelected(false);
        verify(updateUserPanel.jCheckBox3).setSelected(false);
    }
}