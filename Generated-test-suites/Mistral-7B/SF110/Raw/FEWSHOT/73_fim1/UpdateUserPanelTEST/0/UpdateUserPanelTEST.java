package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import org.junit.Test;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

    @Test
    public void testUpdateModel() {
        AdminApp adminApp = new AdminApp();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
        Group group = new Group("Group1");
        User user = new User("User1", "user1@example.com", "1234567890", true, true, true, "Software Engineer");
        group.addUser(user);
        adminApp.setGroups(Collections.singletonList(group));

        updateUserPanel.updateModel();

        DefaultComboBoxModel<User> userComboBoxModel = (DefaultComboBoxModel<User>) updateUserPanel.jComboBox2.getModel();
        assertEquals(1, userComboBoxModel.getSize());
        assertEquals(user, userComboBoxModel.getElementAt(0));
    }

    @Test
    public void testInitComponents() {
        AdminApp adminApp = mock(AdminApp.class);
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);

        verifyNoMoreInteractions(adminApp);

        JLabel jLabel1 = updateUserPanel.jLabel1;
        assertEquals("Username:", jLabel1.getText());

        JLabel jLabel2 = updateUserPanel.jLabel2;
        assertEquals("Login Email:", jLabel2.getText());

        JLabel jLabel3 = updateUserPanel.jLabel3;
        assertEquals("Phone:", jLabel3.getText());

        // Other assertions for other components can be added here
    }

    @Test
    public void testJButton1ActionPerformed() {
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(null, null);
        updateUserPanel.controlPanel = mock(ControlPanel.class);

        when(updateUserPanel.controlPanel.getParentDialog()).thenReturn(mock(JDialog.class));

        updateUserPanel.jButton1ActionPerformed(mock(ActionEvent.class));

        verify(updateUserPanel.controlPanel.getParentDialog()).setVisible(false);
        updateUserPanel.reset();
    }

    @Test
    public void testJButton2ActionPerformed() {
        AdminApp adminApp = new AdminApp();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
        Group group = new Group("Group1");
        User user1 = new User("User1", "user1@example.com", "1234567890", true, true, true, "Software Engineer");
        group.addUser(user1);
        adminApp.setGroups(Collections.singletonList(group));

        updateUserPanel.updateModel();

        when(updateUserPanel.jComboBox1.getSelectedItem()).thenReturn(group);
        when(updateUserPanel.jComboBox2.getSelectedItem()).thenReturn(user1);

        updateUserPanel.jButton2ActionPerformed(mock(ActionEvent.class));

        verify(adminApp).delUser(user1);
        verify(group).getUsers().remove(0);
        JOptionPane.showMessageDialog(updateUserPanel, "User Deleted Successfully!");
    }

    @Test
    public void testJButton3ActionPerformed() {
        AdminApp adminApp = new AdminApp();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
        Group group = new Group("Group1");
        User user = new User("User1", "user1@example.com", "1234567890", true, true, true, "Software Engineer");
        group.addUser(user);
        adminApp.setGroups(Collections.singletonList(group));

        updateUserPanel.updateModel();

        when(updateUserPanel.jComboBox1.getSelectedItem()).thenReturn(group);
        when(updateUserPanel.jComboBox2.getSelectedItem()).thenReturn(user);

        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("new.user@example.com");

        updateUserPanel.jButton3ActionPerformed(mock(ActionEvent.class));

        assertEquals("NewUserName", user.getName());
        assertEquals("new.user@example.com", user.getEmail());
        JOptionPane.showMessageDialog(updateUserPanel, "User Password Reset Successfully!");
    }

    @Test
    public void testJButton4ActionPerformed() {
        AdminApp adminApp = new AdminApp();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
        Group group = new Group("Group1");
        User user = new User("User1", "user1@example.com", "1234567890", true, true, true, "Software Engineer");
        group.addUser(user);
        adminApp.setGroups(Collections.singletonList(group));

        updateUserPanel.updateModel();

        when(updateUserPanel.jComboBox1.getSelectedItem()).thenReturn(group);
        when(updateUserPanel.jComboBox2.getSelectedItem()).thenReturn(user);

        updateUserPanel.jTextField1.setText("NewUserName");
        updateUserPanel.jTextField2.setText("new.user@example.com");
        updateUserPanel.jTextField3.setText("1111111111");
        updateUserPanel.jTextField4.setText("New Job Title");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);

        updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));

        assertEquals("NewUserName", user.getName());
        assertEquals("new.user@example.com", user.getEmail());
        assertEquals("1111111111", user.getDirectPhone());
        assertEquals("New Job Title", user.getJobTitle());
        user.setShowMyStatus(true);
        user.setOnlyStartChat(false);
        user.setCanJoinChatRoom(true);
        adminApp.updateUser(user);
        verify(adminApp).updateUser(user);
        JOptionPane.showMessageDialog(updateUserPanel, "User Updated Successfully!");
    }

    @Test
    public void testJComboBox1ItemStateChanged() {
        AdminApp adminApp = new AdminApp();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);

        when(updateUserPanel.jComboBox1.getSelectedItem()).thenReturn(new Group("Group1"));

        updateUserPanel.jComboBox1ItemStateChanged(mock(ItemEvent.class));

        assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
    }

    @Test
    public void testJComboBox2ItemStateChanged() {
        AdminApp adminApp = new AdminApp();
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
        Group group = new Group("Group1");
        User user = new User("User1", "user1@example.com", "1234567890", true, true, true, "Software Engineer");
        group.addUser(user);
        adminApp.setGroups(Collections.singletonList(group));

        updateUserPanel.updateModel();

        when(updateUserPanel.jComboBox1.getSelectedItem()).thenReturn(group);
        when(updateUserPanel.jComboBox2.getSelectedItem()).thenReturn(user);

        updateUserPanel.jComboBox2ItemStateChanged(mock(ItemEvent.class));

        assertEquals("User1", updateUserPanel.jTextField1.getText());
        assertEquals("user1@example.com", updateUserPanel.jTextField2.getText());
        assertEquals("1234567890", updateUserPanel.jTextField3.getText());
        assertEquals("Software Engineer", updateUserPanel.jTextField4.getText());
        updateUserPanel.reset();
    }
}