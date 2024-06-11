package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import org.junit.Test;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

    private UpdateUserPanel updateUserPanel;
    private AdminApp adminApp;
    private ControlPanel controlPanel;

    @Test
    public void testUpdateUserPanel() {
        adminApp = new AdminApp();
        controlPanel = new ControlPanel();
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        assertNotNull(updateUserPanel);
    }

    @Test
    public void testInitComponents() {
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
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
    public void testUpdateModel() {
        adminApp = new AdminApp();
        controlPanel = new ControlPanel();
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);

        Group group = new Group();
        User user = new User();
        adminApp.setGroups(new Vector<Group>());
        adminApp.getGroups().add(group);
        group.setUsers(new Vector<User>());
        group.getUsers().add(user);

        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        updateUserPanel.updateModel();

        assertNotNull(updateUserPanel.jComboBox2);
        assertEquals(1, updateUserPanel.jComboBox2.getItemCount());
        assertEquals(user.toString(), updateUserPanel.jComboBox2.getItemAt(0).toString());
    }

    @Test
    public void testjComboBox1ItemStateChanged() {
        adminApp = new AdminApp();
        controlPanel = new ControlPanel();
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);

        Group group = new Group();
        User user = new User();
        Vector<Group> groups = new Vector<Group>();
        groups.add(group);
        adminApp.setGroups(groups);
        group.setUsers(new Vector<User>());
        group.getUsers().add(user);

        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        updateUserPanel.initComponents();
        updateUserPanel.jComboBox1 = mock(JComboBox.class);
        doNothing().when(updateUserPanel.jComboBox1).addItemListener(any(ItemListener.class));

        updateUserPanel.jComboBox1.setSelectedItem(group);
        updateUserPanel.jComboBox1ItemStateChanged(null);

        verify(updateUserPanel.jComboBox1).addItemListener(any(ItemListener.class));
    }

    @Test
    public void testjButton1ActionPerformed() {
        adminApp = new AdminApp();
        controlPanel = new ControlPanel();
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);

        updateUserPanel.jButton1ActionPerformed(mock(ActionEvent.class));
        assertTrue(updateUserPanel.controlPanel.getParentDialog().isVisible() == false);
        updateUserPanel.reset();
    }

    @Test
    public void testjButton2ActionPerformed() {
        adminApp = new AdminApp();
        controlPanel = new ControlPanel();
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);

        Group group = new Group();
        User user = new User();
        Vector<Group> groups = new Vector<Group>();
        groups.add(group);
        Vector<User> users = new Vector<User>();
        users.add(user);
        group.setUsers(users);
        adminApp.setGroups(groups);

        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        updateUserPanel.initComponents();
        updateUserPanel.jComboBox1 = mock(JComboBox.class);
        updateUserPanel.jComboBox2 = mock(JComboBox.class);
        doNothing().when(updateUserPanel.jComboBox1).setSelectedIndex(anyInt());
        doNothing().when(updateUserPanel.jComboBox2).setSelectedIndex(anyInt());
        doNothing().when(updateUserPanel.jComboBox2).validate();
        doNothing().when(updateUserPanel.controlPanel).getParentDialog();

        updateUserPanel.jComboBox1.setSelectedItem(group);
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jButton2ActionPerformed(mock(ActionEvent.class));

        verify(updateUserPanel.adminApp).delUser(user);
        verify(group.getUsers()).remove(user);
        verify(updateUserPanel.controlPanel).getParentDialog().setVisible(false);
        updateUserPanel.reset();
    }

    @Test
    public void testjButton3ActionPerformed() {
        adminApp = new AdminApp();
        controlPanel = new ControlPanel();
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);

        User user = new User();
        updateUserPanel.jComboBox2 = mock(JComboBox.class);
        doNothing().when(updateUserPanel.jComboBox2).setSelectedIndex(anyInt());
        doNothing().when(updateUserPanel.adminApp).resetUserPass(any(User.class));

        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jButton3ActionPerformed(mock(ActionEvent.class));

        verify(updateUserPanel.adminApp).resetUserPass(user);
    }

    @Test
    public void testjButton4ActionPerformed() {
        adminApp = new AdminApp();
        controlPanel = new ControlPanel();
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);

        Group group = new Group();
        User user = new User();
        Vector<Group> groups = new Vector<Group>();
        groups.add(group);
        Vector<User> users = new Vector<User>();
        users.add(user);
        group.setUsers(users);
        adminApp.setGroups(groups);

        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        updateUserPanel.initComponents();
        updateUserPanel.jComboBox1 = mock(JComboBox.class);
        updateUserPanel.jComboBox2 = mock(JComboBox.class);
        updateUserPanel.jTextField1 = mock(JTextField.class);
        updateUserPanel.jTextField2 = mock(JTextField.class);
        updateUserPanel.jTextField3 = mock(JTextField.class);
        updateUserPanel.jTextField4 = mock(JTextField.class);
        updateUserPanel.jCheckBox1 = mock(JCheckBox.class);
        updateUserPanel.jCheckBox2 = mock(JCheckBox.class);
        updateUserPanel.jCheckBox3 = mock(JCheckBox.class);
        doNothing().when(updateUserPanel.adminApp).updateUser(any(User.class));

        updateUserPanel.jComboBox1.setSelectedItem(group);
        updateUserPanel.jComboBox2.setSelectedItem(user);
        updateUserPanel.jTextField1.setText("newName");
        updateUserPanel.jTextField2.setText("newEmail");
        updateUserPanel.jTextField3.setText("newPhone");
        updateUserPanel.jTextField4.setText("newTitle");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));

        verify(user).setName("newName");
        verify(user).setEmail("newEmail");
        verify(user).setDirectPhone("newPhone");
        verify(user).setJobTitle("newTitle");
        verify(user).setShowMyStatus(true);
        verify(user).setOnlyStartChat(true);
        verify(user).setCanJoinChatRoom(true);
        verify(updateUserPanel.adminApp).updateUser(user);
    }
}