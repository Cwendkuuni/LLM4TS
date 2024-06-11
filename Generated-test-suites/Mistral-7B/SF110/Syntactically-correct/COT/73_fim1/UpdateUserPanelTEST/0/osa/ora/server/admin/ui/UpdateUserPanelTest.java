package osa.ora.server.admin.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class UpdateUserPanelTest {

    private AdminApp adminApp;
    private ControlPanel controlPanel;
    private UpdateUserPanel updateUserPanel;

    @Before
    public void setUp() {
        adminApp = mock(AdminApp.class);
        controlPanel = mock(ControlPanel.class);
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
    }

    @Test
    public void testUpdateUserPanelConstructor() {
        assertNotNull(updateUserPanel);
        assertEquals(adminApp, updateUserPanel.adminApp);
        assertEquals(controlPanel, updateUserPanel.controlPanel);
    }

    @Test
    public void testUpdateModel_whenGroupsAndUsersArePresent_thenUserComboBoxIsPopulatedWithUsers() {
        when(adminApp.getGroups()).thenReturn(Collections.singletonList(createGroup()));

        updateUserPanel.updateModel();

        assertEquals(Collections.singletonList(createUser()), updateUserPanel.jComboBox2.getItemAt(0));
    }

    @Test
    public void testUpdateModel_whenGroupsArePresentButUsersAreAbsent_thenUserComboBoxIsPopulatedWithGroups() {
        when(adminApp.getGroups()).thenReturn(Collections.singletonList(createGroup()));

        updateUserPanel.updateModel();

        assertEquals(createGroup().getName(), updateUserPanel.jComboBox1.getItemAt(0).toString());
    }

    @Test
    public void testUpdateModel_whenGroupsAreAbsent_thenUserComboBoxIsEmpty() {
        when(adminApp.getGroups()).thenReturn(Collections.emptyList());

        updateUserPanel.updateModel();

        assertNull(updateUserPanel.jComboBox1.getItemAt(0));
        assertNull(updateUserPanel.jComboBox2.getItemAt(0));
    }

    @Test
    public void testJComboBox1ItemStateChanged_whenGroupIsSelected_thenUserComboBoxIsPopulatedWithUsersOfSelectedGroup() {
        when(adminApp.getGroups()).thenReturn(Arrays.asList(createGroup(), createGroup2()));

        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, 0, ItemEvent.SELECTED, false, 0));

        assertEquals(Arrays.asList(createUser(), createUser2()), updateUserPanel.jComboBox2.getModel().toArray());
    }

    @Test(expected = NullPointerException.class)
    public void testJButton1ActionPerformed_whenParentDialogIsNotSet_thenExceptionIsThrown() {
        updateUserPanel.controlPanel = null;
        updateUserPanel.jButton1ActionPerformed(new ActionEvent(updateUserPanel, 0, ""));
    }

    @Test
    public void testJButton2ActionPerformed_whenUserIsSelected_thenUserIsDeleted() {
        when(adminApp.getGroups()).thenReturn(Collections.singletonList(createGroup()));
        when(adminApp.delUser(createUser())).thenReturn(createUserDeleted());

        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(updateUserPanel, 0, ""));

        assertEquals(Collections.emptyList(), updateUserPanel.jComboBox2.getModel().toList());
        assertEquals(createGroup().getName(), updateUserPanel.jComboBox1.getItemAt(0).toString());
    }

    @Test(expected = NullPointerException.class)
    public void testJButton2ActionPerformed_whenUserIsNotSelected_thenNullPointerExceptionIsThrown() {
        updateUserPanel.updateModel();
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(updateUserPanel, 0, ""));
    }

    @Test
    public void testJButton3ActionPerformed_whenUserIsSelected_thenUserPasswordIsReset() {
        when(adminApp.resetUserPass(createUser())).thenReturn(createUserPasswordReset());

        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField2.setText("newemail@example.com");
        updateUserPanel.jButton3ActionPerformed(new ActionEvent(updateUserPanel, 0, ""));

        assertEquals("newemail@example.com", updateUserPanel.jTextField2.getText());
    }

    @Test(expected = NullPointerException.class)
    public void testJButton3ActionPerformed_whenUserIsNotSelected_thenNullPointerExceptionIsThrown() {
        updateUserPanel.updateModel();
        updateUserPanel.jButton3ActionPerformed(new ActionEvent(updateUserPanel, 0, ""));
    }

    @Test
    public void testJButton4ActionPerformed_whenUserIsSelected_thenUserIsUpdated() {
        when(adminApp.getGroups()).thenReturn(Collections.singletonList(createGroup()));
        when(adminApp.updateUser(createUser())).thenReturn(createUserUpdated());

        updateUserPanel.updateModel();
        updateUserPanel.jComboBox1.setSelectedIndex(0);
        updateUserPanel.jComboBox2.setSelectedIndex(0);
        updateUserPanel.jTextField1.setText("newName");
        updateUserPanel.jTextField2.setText("newemail@example.com");
        updateUserPanel.jTextField3.setText("newPhone");
        updateUserPanel.jTextField4.setText("newTitle");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);

        updateUserPanel.jButton4ActionPerformed(new ActionEvent(updateUserPanel, 0, ""));

        assertEquals("newName", updateUserPanel.jTextField1.getText());
        assertEquals("newemail@example.com", updateUserPanel.jTextField2.getText());
        assertEquals("newPhone", updateUserPanel.jTextField3.getText());
        assertEquals("newTitle", updateUserPanel.jTextField4.getText());
        assertEquals(true, updateUserPanel.jCheckBox1.isSelected());
        assertEquals(true, updateUserPanel.jCheckBox2.isSelected());
        assertEquals(createUserUpdated(), updateUserPanel.jComboBox2.getSelectedItem());
    }

    @Test(expected = NullPointerException.class)
    public void testJButton4ActionPerformed_whenUserIsNotSelected_thenNullPointerExceptionIsThrown() {
        updateUserPanel.updateModel();
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(updateUserPanel, 0, ""));
    }

    private Group createGroup() {
        return new Group("Group1");
    }

    private Group createGroup2() {
        return new Group("Group2");
    }

    private User createUser() {
        return new User("User1", "user1@example.com", "phone1", "JobTitle1", true, true, true);
    }

    private User createUser2() {
        return new User("User2", "user2@example.com", "phone2", "JobTitle2", false, false, false);
    }

    private User createUserDeleted() {
        return new User("UserDeleted", null, null, null, false, false, false);
    }

    private User createUserPasswordReset() {
        return new User("UserPasswordReset", "newemail@example.com", "newPhone", "newTitle", false, false, false);
    }

    private User createUserUpdated() {
        return new User("UserUpdated", "newName", "newemail@example.com", "newPhone", true, true, true);
    }
}