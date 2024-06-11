package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

import org.junit.Before;
import org.junit.Test;

public class UpdateUserPanelTest {

    private UpdateUserPanel updateUserPanel;
    private AdminApp adminApp;
    private Group mockGroup;
    private User mockUser;

    @Before
    public void setup() {
        adminApp = mock(AdminApp.class);
        mockGroup = mock(Group.class);
        mockUser = mock(User.class);

        updateUserPanel = new UpdateUserPanel(adminApp, null);

        when(adminApp.getGroups()).thenReturn(new ArrayList<>(Arrays.asList(mockGroup)));
        when(mockGroup.getUsers()).thenReturn(new ArrayList<>());
        when(mockGroup.getUsers().size()).thenReturn(0);
    }

    @Test
    public void testInitializeComponents() {
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(adminApp, null);
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
        when(mockGroup.getUsers()).thenReturn(new ArrayList<>(Arrays.asList(mockUser)));
        when(mockUser.getName()).thenReturn("User Name");

        updateUserPanel.updateModel();
        assertEquals("User Name", updateUserPanel.jComboBox2.getSelectedItem());
    }

    @Test
    public void testJComboBox1ItemStateChanged() {
        when(mockGroup.getUsers()).thenReturn(new ArrayList<>(Arrays.asList(mockUser)));
        when(mockUser.getName()).thenReturn("User Name");

        updateUserPanel.updateModel();

        ItemEvent itemEvent = new ItemEvent(updateUserPanel.jComboBox1, ItemEvent.SELECTED, 0);
        updateUserPanel.jComboBox1ItemStateChanged(itemEvent);

        assertEquals("User Name", updateUserPanel.jTextField1.getText());
    }

    @Test
    public void testFillUser() {
        when(mockUser.getName()).thenReturn("User Name");
        when(mockUser.getEmail()).thenReturn("user.email@example.com");
        when(mockUser.getDirectPhone()).thenReturn("123456789");
        when(mockUser.isShowMyStatus()).thenReturn(true);
        when(mockUser.isOnlyStartChat()).thenReturn(false);
        when(mockUser.isCanJoinChatRoom()).thenReturn(true);

        updateUserPanel.fillUser(mockUser);

        assertEquals("User Name", updateUserPanel.jTextField1.getText());
        assertEquals("user.email@example.com", updateUserPanel.jTextField2.getText());
        assertEquals("123456789", updateUserPanel.jTextField3.getText());
        assertTrue(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertTrue(updateUserPanel.jCheckBox3.isSelected());
    }

    @Test
    public void testJButton1ActionPerformed() {
        updateUserPanel.controlPanel = mock(ControlPanel.class);

        updateUserPanel.jButton1ActionPerformed(new ActionEvent(updateUserPanel.jButton1, ActionEvent.ACTION_PERFORMED, "Cancel"));

        verify(updateUserPanel.controlPanel).getParentDialog().setVisible(false);
        assertTrue(updateUserPanel.reset());
    }

    @Test
    public void testJButton2ActionPerformed() {
        when(mockGroup.getUsers()).thenReturn(new ArrayList<>(Arrays.asList(mockUser)));
        when(mockUser.getName()).thenReturn("User Name");
        when(mockUser.getId()).thenReturn(1);

        updateUserPanel.updateModel();

        updateUserPanel.jButton2ActionPerformed(new ActionEvent(updateUserPanel.jButton2, ActionEvent.ACTION_PERFORMED, "Delete"));

        verify(adminApp).delUser(mockUser);
        verify(mockGroup.getUsers()).remove(0);
        assertFalse(updateUserPanel.jComboBox2.getModel().contains(mockUser));
    }

    @Test
    public void testJButton3ActionPerformed() {
        when(mockUser.getId()).thenReturn(1);

        updateUserPanel.updateModel();

        updateUserPanel.jTextField1.setText("New User Name");
        updateUserPanel.jTextField2.setText("new.email@example.com");
        updateUserPanel.jTextField3.setText("987654321");
        updateUserPanel.jCheckBox1.setSelected(false);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(false);

        updateUserPanel.jButton3ActionPerformed(new ActionEvent(updateUserPanel.jButton3, ActionEvent.ACTION_PERFORMED, "Reset Password"));

        verify(adminApp).resetUserPass(mockUser);
        assertEquals("New User Name", updateUserPanel.jTextField1.getText());
        assertEquals("new.email@example.com", updateUserPanel.jTextField2.getText());
        assertEquals("987654321", updateUserPanel.jTextField3.getText());
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
        assertTrue(updateUserPanel.jCheckBox2.isSelected());
        assertFalse(updateUserPanel.jCheckBox3.isSelected());
    }

    @Test
    public void testJButton4ActionPerformed() {
        when(mockGroup.getUsers()).thenReturn(new ArrayList<>(Collections.nCopies(1, mockUser)));
        when(mockUser.getId()).thenReturn(1);
        when(mockUser.getName()).thenReturn("User Name");
        when(mockUser.getEmail()).thenReturn("user.email@example.com");
        when(mockUser.getDirectPhone()).thenReturn("123456789");
        when(mockUser.isShowMyStatus()).thenReturn(true);
        when(mockUser.isOnlyStartChat()).thenReturn(false);
        when(mockUser.isCanJoinChatRoom()).thenReturn(true);

        updateUserPanel.updateModel();

        updateUserPanel.jTextField1.setText("New User Name");
        updateUserPanel.jTextField2.setText("new.email@example.com");
        updateUserPanel.jTextField3.setText("987654321");
        updateUserPanel.jCheckBox1.setSelected(false);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(false);

        updateUserPanel.jButton4ActionPerformed(new ActionEvent(updateUserPanel.jButton4, ActionEvent.ACTION_PERFORMED, "Update"));

        verify(adminApp).updateUser(mockUser);
        assertEquals("New User Name", updateUserPanel.jTextField1.getText());
        assertEquals("new.email@example.com", updateUserPanel.jTextField2.getText());
        assertEquals("987654321", updateUserPanel.jTextField3.getText());
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
        assertTrue(updateUserPanel.jCheckBox2.isSelected());
        assertFalse(updateUserPanel.jCheckBox3.isSelected());
        assertEquals("User Name", updateUserPanel.jComboBox2.getSelectedItem());
    }
}