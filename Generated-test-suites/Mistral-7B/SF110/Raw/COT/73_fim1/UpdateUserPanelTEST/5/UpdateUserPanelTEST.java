package osa.ora.server.admin.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.junit.Before;
import org.junit.Test;

public class UpdateUserPanelTest {

    private AdminApp adminApp;
    private ControlPanel controlPanel;
    private UpdateUserPanel updateUserPanel;
    private JComboBox<String> jComboBox1;
    private JComboBox<User> jComboBox2;
    private User user;
    private Group group;

    @Before
    public void setup() {
        adminApp = mock(AdminApp.class);
        controlPanel = mock(ControlPanel.class);
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);

        group = mock(Group.class);
        when(adminApp.getGroups()).thenReturn(Arrays.asList(group));
        when(group.getUsers()).thenReturn(new ArrayList<>());

        user = mock(User.class);

        jComboBox1 = mock(JComboBox.class);
        when(updateUserPanel.jComboBox1).thenReturn(jComboBox1);

        jComboBox2 = mock(JComboBox.class);
        when(updateUserPanel.jComboBox2).thenReturn(jComboBox2);

        updateUserPanel.initComponents();
    }

    @Test
    public void testUpdateUserPanelConstructor() {
        assertEquals(updateUserPanel.getClass(), UpdateUserPanel.class);
        assertSame(updateUserPanel.adminApp, adminApp);
        assertSame(updateUserPanel.controlPanel, controlPanel);
    }

    @Test
    public void testUpdateModel() {
        when(adminApp.getGroups().get(0).getUsers()).thenReturn(Arrays.asList(user));

        updateUserPanel.updateModel();

        ComboBoxModel<User> expectedComboBoxModel = new DefaultComboBoxModel<>(Arrays.asList(user));
        assertEquals(expectedComboBoxModel, jComboBox2.getModel());
    }

    @Test
    public void testInitComponents() {
        assertEquals(UpdateUserPanel.class, updateUserPanel.getClass());
        assertEquals(15, updateUserPanel.getComponentCount());

        JLabel label;
        JTextField textField;
        JButton button;
        JCheckBox checkBox;
        JComboBox<String> comboBox;

        label = (JLabel) updateUserPanel.getComponent(0);
        assertEquals("Username:", label.getText());

        textField = (JTextField) updateUserPanel.getComponent(7);
        assertNull(textField.getText());

        button = (JButton) updateUserPanel.getComponent(10);
        assertEquals("Cancel", button.getText());

        checkBox = (JCheckBox) updateUserPanel.getComponent(11);
        assertFalse(checkBox.isSelected());

        comboBox = (JComboBox<String>) updateUserPanel.getComponent(12);
        assertEquals(4, comboBox.getItemCount());
        assertEquals("Item 1", comboBox.getItemAt(0));
    }

    @Test
    public void testJButton1ActionPerformed() {
        updateUserPanel.jButton1 = mock(JButton.class);

        ActionEvent event = mock(ActionEvent.class);
        updateUserPanel.jButton1ActionPerformed(event);

        verify(controlPanel.getParentDialog()).setVisible(false);
        updateUserPanel.reset();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton2ActionPerformedNullUser() {
        when(jComboBox2.getSelectedItem()).thenReturn(null);

        updateUserPanel.jButton2ActionPerformed(mock(ActionEvent.class));
    }

    @Test
    public void testJButton2ActionPerformed() {
        when(jComboBox2.getSelectedItem()).thenReturn(user);
        when(group.getUsers()).thenReturn(Arrays.asList(user));

        ActionEvent event = mock(ActionEvent.class);
        updateUserPanel.jButton2ActionPerformed(event);

        verify(adminApp).delUser(user);
        verify(group.getUsers()).remove(0);
        updateUserPanel.reset();
    }

    @Test
    public void testJButton3ActionPerformedNullUser() {
        when(jComboBox2.getSelectedItem()).thenReturn(null);

        updateUserPanel.jButton3ActionPerformed(mock(ActionEvent.class));
    }

    @Test
    public void testJButton3ActionPerformed() {
        when(jComboBox2.getSelectedItem()).thenReturn(user);

        ActionEvent event = mock(ActionEvent.class);
        updateUserPanel.jButton3ActionPerformed(event);

        verify(adminApp).resetUserPass(user);
    }

    @Test
    public void testJButton4ActionPerformedNullUser() {
        when(jComboBox2.getSelectedItem()).thenReturn(null);

        updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));
    }

    @Test
    public void testJButton4ActionPerformed() {
        when(jComboBox2.getSelectedItem()).thenReturn(user);
        when(jTextField1.getText()).thenReturn("newName");
        when(jTextField2.getText()).thenReturn("newEmail");
        when(jTextField3.getText()).thenReturn("newPhone");
        when(jTextField4.getText()).thenReturn("newTitle");
        when(jCheckBox1.isSelected()).thenReturn(true);
        when(jCheckBox2.isSelected()).thenReturn(true);
        when(jCheckBox3.isSelected()).thenReturn(true);

        ActionEvent event = mock(ActionEvent.class);
        updateUserPanel.jButton4ActionPerformed(event);

        User updatedUser = mock(User.class);
        when(updatedUser.getName()).thenReturn("newName");
        when(updatedUser.getEmail()).thenReturn("newEmail");
        when(updatedUser.getDirectPhone()).thenReturn("newPhone");
        when(updatedUser.getJobTitle()).thenReturn("newTitle");
        when(updatedUser.isShowMyStatus()).thenReturn(true);
        when(updatedUser.isOnlyStartChat()).thenReturn(true);
        when(updatedUser.isCanJoinChatRoom()).thenReturn(true);

        verify(user).setName("newName");
        verify(user).setEmail("newEmail");
        verify(user).setDirectPhone("newPhone");
        verify(user).setJobTitle("newTitle");
        verify(user).setShowMyStatus(true);
        verify(user).setOnlyStartChat(true);
        verify(user).setCanJoinChatRoom(true);
        verify(adminApp).updateUser(user);
        verify(group.getUsers()).set(0, updatedUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJComboBox1ItemStateChangedNullEvent() {
        updateUserPanel.jComboBox1ItemStateChanged(null);
    }

    @Test
    public void testJComboBox1ItemStateChanged() {
        when(jComboBox1.getItemAt(0)).thenReturn("newGroup");

        updateUserPanel.jComboBox1ItemStateChanged(mock(ItemEvent.class));

        assertEquals("newGroup", jComboBox2.getModel().getSelectedItem());
    }
}