package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import org.junit.Test;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {

    UpdateUserPanel updateUserPanel;
    AdminApp adminApp = new AdminApp();
    Group group = new Group("Group1");
    User user1 = new User("User1", "user1@example.com", "1234567890", true, true, true, "Title1");
    User user2 = new User("User2", "user2@example.com", "2345678901", false, false, false, "Title2");
    Group testGroup = new Group("TestGroup");
    Vector<User> testUsers = new Vector<>();

    @Test
    public void testUpdateModel() {
        updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.updateModel();
        assertEquals(4, updateUserPanel.jComboBox1.getItemCount());
        assertNull(updateUserPanel.jComboBox2.getSelectedItem());

        adminApp.setGroups(Arrays.asList(group, testGroup));
        updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.updateModel();
        assertEquals(2, updateUserPanel.jComboBox1.getItemCount());
        assertEquals(group, updateUserPanel.jComboBox1.getSelectedItem());
        assertEquals(0, updateUserPanel.jComboBox2.getItemCount());

        group.setUsers(testUsers);
        group.getUsers().add(user1);
        group.getUsers().add(user2);
        adminApp.setGroups(Arrays.asList(group));
        updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.updateModel();
        assertEquals(2, updateUserPanel.jComboBox1.getItemCount());
        assertEquals(group, updateUserPanel.jComboBox1.getSelectedItem());
        assertEquals(2, updateUserPanel.jComboBox2.getItemCount());
        assertEquals(user1, updateUserPanel.jComboBox2.getSelectedItem());
    }

    @Test
    public void testInitComponents() {
        updateUserPanel = new UpdateUserPanel(adminApp, null);
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
    public void testjComboBox1ItemStateChanged() {
        updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1 = new JComboBox<>(new DefaultComboBoxModel<>(new String[]{"Item 1"}));
        updateUserPanel.jComboBox1.addItemListener(mock(ItemListener.class));
        updateUserPanel.jComboBox1ItemStateChanged(mock(ItemEvent.class));
        verify(updateUserPanel.jComboBox1, times(1)).setModel(new DefaultComboBoxModel<>(group.getUsers().toArray()));
        verify(updateUserPanel, times(1)).reset();

        group.setUsers(testUsers);
        group.getUsers().add(user1);
        group.getUsers().add(user2);
        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1 = new JComboBox<>(new DefaultComboBoxModel<>(new String[]{"Group1"}));
        updateUserPanel.jComboBox1.addItemListener(mock(ItemListener.class));
        updateUserPanel.jComboBox1ItemStateChanged(mock(ItemEvent.class));
        verify(updateUserPanel.jComboBox1, times(1)).setModel(new DefaultComboBoxModel<>(user1.getGroups().toArray()));
        verify(updateUserPanel, times(1)).reset();
    }

    @Test
    public void testjButton1ActionPerformed() {
        updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.jButton1 = mock(JButton.class);
        doAnswer(invocationOnMock(ActionListener.class), new Action(){
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUserPanel.jButton1ActionPerformed(e);
            }
        }).when(updateUserPanel.jButton1);
        updateUserPanel.jButton1ActionPerformed(mock(ActionEvent.class));
        verify(updateUserPanel.controlPanel.getParentDialog(), times(1)).setVisible(false);
        verify(updateUserPanel, times(1)).reset();
    }

    @Test
    public void testjButton2ActionPerformed() {
        updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.jComboBox1 = new JComboBox<>(new DefaultComboBoxModel<>(new String[]{"Group1"}));
        updateUserPanel.jComboBox2 = new JComboBox<>(new DefaultComboBoxModel<>(new String[]{"User1"}));
        updateUserPanel.jButton2 = mock(JButton.class);
        doAnswer(invocationOnMock(ActionListener.class), new Action(){
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUserPanel.jButton2ActionPerformed(e);
            }
        }).when(updateUserPanel.jButton2);

        User testUser = new User("TestUser", "testuser@example.com", "1234567890", false, false, false, "TestTitle");
        group.getUsers().add(testUser);
        updateUserPanel.jComboBox1 = new JComboBox<>(new DefaultComboBoxModel<>(new String[]{"Group1"}));
        updateUserPanel.jComboBox2 = new JComboBox<>(new DefaultComboBoxModel<>(new String[]{"User1"}));
        updateUserPanel.updateModel();
        updateUserPanel.jButton2.doClick();

        verify(updateUserPanel, times(1)).adminApp.delUser(testUser);
        verify(updateUserPanel, times(1)).jComboBox1.setSelectedIndex(0);
        verify(updateUserPanel, times(1)).jComboBox1.validate();
        verify(updateUserPanel, times(1)).jComboBox2.setModel(new DefaultComboBoxModel<>(group.getUsers().toArray()));
        verify(updateUserPanel, times(1)).reset();
        verify(updateUserPanel, times(1)).jOptionPane.showMessageDialog(updateUserPanel, (String)isNull(), "User Deleted Successfully!", 0, updateUserPanel.adminApp.getErrorIcon());
    }

    @Test
    public void testjButton3ActionPerformed() {
        updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.jComboBox1 = new JComboBox<>(new DefaultComboBoxModel<>(new String[]{"Group1"}));
        updateUserPanel.jComboBox2 = new JComboBox<>(new DefaultComboBoxModel<>(new String[]{"User1"}));
        updateUserPanel.jButton3 = mock(JButton.class);
        doAnswer(invocationOnMock(ActionListener.class), new Action(){
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUserPanel.jButton3ActionPerformed(e);
            }
        }).when(updateUserPanel.jButton3);

        User testUser = new User("TestUser", "testuser@example.com", "1234567890", false, false, false, "TestTitle");
        group.getUsers().add(testUser);
        updateUserPanel.jComboBox1 = new JComboBox<>(new DefaultComboBoxModel<>(new String[]{"Group1"}));
        updateUserPanel.jComboBox2 = new JComboBox<>(new DefaultComboBoxModel<>(new String[]{"User1"}));
        updateUserPanel.updateModel();
        updateUserPanel.jTextField1.setText("TestName");
        updateUserPanel.jTextField2.setText("testuser@anotherexample.com");
        updateUserPanel.jTextField3.setText("1234567891");
        updateUserPanel.jTextField4.setText("TestTitle2");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton3.doClick();

        verify(updateUserPanel, times(1)).adminApp.resetUserPass(testUser);
        verify(updateUserPanel, times(1)).jOptionPane.showMessageDialog(updateUserPanel, "User Password Reset Successfully!", "Success", 0, updateUserPanel.adminApp.getDefaultIcon());
    }

    @Test
    public void testjButton4ActionPerformed() {
        updateUserPanel = new UpdateUserPanel(adminApp, null);
        updateUserPanel.jComboBox1 = new JComboBox<>(new DefaultComboBoxModel<>(new String[]{"Group1"}));
        updateUserPanel.jComboBox2 = new JComboBox<>(new DefaultComboBoxModel<>(new String[]{"User1"}));
        updateUserPanel.jButton4 = mock(JButton.class);
        doAnswer(invocationOnMock(ActionListener.class), new Action(){
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUserPanel.jButton4ActionPerformed(e);
            }
        }).when(updateUserPanel.jButton4);

        User testUser = new User("TestUser", "testuser@example.com", "1234567890", false, false, false, "TestTitle");
        group.getUsers().add(testUser);
        updateUserPanel.jComboBox1 = new JComboBox<>(new DefaultComboBoxModel<>(new String[]{"Group1"}));
        updateUserPanel.jComboBox2 = new JComboBox<>(new DefaultComboBoxModel<>(new String[]{"User1"}));
        updateUserPanel.updateModel();
        updateUserPanel.jTextField1.setText("TestName");
        updateUserPanel.jTextField2.setText("testuser@anotherexample.com");
        updateUserPanel.jTextField3.setText("1234567891");
        updateUserPanel.jTextField4.setText("TestTitle2");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(false);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4.doClick();

        verify(updateUserPanel, times(1)).adminApp.updateUser(testUser);
        verify(updateUserPanel, times(1)).jComboBox1.setSelectedIndex(0);
        verify(updateUserPanel, times(1)).jComboBox1.validate();
        verify(updateUserPanel, times(1)).jOptionPane.showMessageDialog(updateUserPanel, "User Updated Successfully!", "Success", 0, updateUserPanel.adminApp.getDefaultIcon());
    }
}