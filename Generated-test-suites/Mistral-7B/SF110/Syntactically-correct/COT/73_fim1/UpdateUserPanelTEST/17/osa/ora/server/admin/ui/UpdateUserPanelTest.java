package osa.ora.server.admin.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import osa.ora.server.admin.AdminApp;
import osa.ora.server.admin.Group;
import osa.ora.server.beans.User;

public class UpdateUserPanelTest {

    private static final String ITEM_1 = "Item 1";
    private static final String ITEM_2 = "Item 2";
    private static final String ITEM_3 = "Item 3";
    private static final String ITEM_4 = "Item 4";
    private static final String USER_NAME = "TestUserName";
    private static final String USER_EMAIL = "test.email@test.com";
    private static final String USER_NAME_1 = "TestUserName_1";
    private static final String USER_EMAIL_1 = "test1.email@test.com";
    private static final String USER_PHONE = "1234567890";
    private static final String USER_TITLE = "TestTitle";
    private static final String USER_GROUP = "TestGroup";
    private static final boolean SHOW_USER_STATUS = true;
    private static final boolean USER_ONLY_START_CHAT = true;
    private static final boolean CAN_JOIN_CHAT_ROOM = true;

    private AdminApp adminApp;
    private ControlPanel controlPanel;
    private UpdateUserPanel updateUserPanel;
    private Group testGroup;
    private List<User> testUsers;
    private User testUser;
    private User testUser1;

    @Before
    public void setUp() {
        adminApp = mock(AdminApp.class);
        controlPanel = mock(ControlPanel.class);
        testGroup = new Group();
        testUsers = new ArrayList<>();
        testUser = new User(USER_NAME, USER_EMAIL, USER_PHONE, USER_TITLE, SHOW_USER_STATUS, USER_ONLY_START_CHAT, CAN_JOIN_CHAT_ROOM);
        testUser1 = new User(USER_NAME_1, USER_EMAIL_1, USER_PHONE, USER_TITLE, SHOW_USER_STATUS, USER_ONLY_START_CHAT, CAN_JOIN_CHAT_ROOM);
        testUsers.add(testUser);
        testUsers.add(testUser1);
        testGroup.setUsers(testUsers);
        testGroup.setName(USER_GROUP);
        when(adminApp.getGroups()).thenReturn(Arrays.asList(testGroup));
        when(adminApp.delUser(testUser)).thenReturn(null);
        when(adminApp.getGroups().get(0).getUsers().get(0)).thenReturn(testUser);
        when(adminApp.updateUser(testUser)).thenReturn(testUser);
        when(adminApp.getErrorIcon()).thenReturn(new ImageIcon(UpdateUserPanelTest.class.getResource("/images/error.png")));

        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
    }

    @Test
    public void testUpdateModel_whenAdminAppHasGroups_thenUserComboBoxShouldContainGroups() {
        updateUserPanel.updateModel();
        assertEquals(1, updateUserPanel.jComboBox1.getItemCount());
        assertSame(testGroup, updateUserPanel.jComboBox1.getSelectedItem());
    }

    @Test
    public void testUpdateModel_whenAdminAppHasGroupsAndUsers_thenSecondComboBoxShouldContainUsers() {
        updateUserPanel.updateModel();
        assertEquals(2, updateUserPanel.jComboBox2.getItemCount());
        assertSame(testUser, updateUserPanel.jComboBox2.getSelectedItem());
    }

    @Test
    public void testUpdateModel_whenAdminAppHasGroupsButNoUsers_thenSecondComboBoxShouldContainNoItems() {
        when(testGroup.getUsers()).thenReturn(null);
        updateUserPanel.updateModel();
        assertEquals(0, updateUserPanel.jComboBox2.getItemCount());
    }

    @Test
    public void testInitComponents_whenCalled_thenAllComponentsShouldBeAddedToThePanel() {
        updateUserPanel.initComponents();

        // Check all UI components are added to the panel
        List<Component> components = new ArrayList<>();
        for (Component component : updateUserPanel.getComponents()) {
            components.add(component);
        }

        List<String> expectedComponents = Arrays.asList(
                "Username:",
                "Login Email:",
                "Phone:",
                "Show User Status To Others",
                "User Can Only Start Chat With Others",
                "Can be a Member of Chat Rooms",
                "User Group:",
                "Cancel",
                "Delete",
                "Reset Password",
                "Update",
                "Title:",
                "Username:",
                "User:");

        assertEquals(expectedComponents.size(), components.size());
        assertTrue(expectedComponents.containsAll(components.stream().map(Component::getName).collect(Collectors.toList())));
    }

    @Test
    public void testJButton1ActionPerformed_whenCalled_thenDialogShouldBeClosedAndPanelShouldBeReset() {
        updateUserPanel.jButton1ActionPerformed(mock(ActionEvent.class));
        assertTrue(controlPanel.getParentDialog().isVisible() == false);
        assertTrue(updateUserPanel.jTextField1.getText().isEmpty());
        assertTrue(updateUserPanel.jTextField2.getText().isEmpty());
        assertTrue(updateUserPanel.jTextField3.getText().isEmpty());
        assertTrue(updateUserPanel.jTextField4.getText().isEmpty());
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertFalse(updateUserPanel.jCheckBox3.isSelected());
    }

    @Test
    public void testJButton2ActionPerformed_whenCalledWithNullUserOrIndexOutOfBounds_thenShouldShowErrorMessage() {
        updateUserPanel.jButton2ActionPerformed(mock(ActionEvent.class));
        verify(adminApp).getErrorIcon(); // Verify error icon is requested
    }

    @Test
    public void testJButton2ActionPerformed_whenCalledWithValidUserAndIndex_thenUserShouldBeDeletedAndGroupShouldBeUpdated() {
        updateUserPanel.jButton2ActionPerformed(mock(ActionEvent.class));
        verify(adminApp).delUser(testUser);
        verify(testGroup.getUsers()).remove(0);
    }

    @Test
    public void testJButton3ActionPerformed_whenCalled_thenUserPasswordShouldBeReset() {
        updateUserPanel.jButton3ActionPerformed(mock(ActionEvent.class));
        verify(adminApp).resetUserPass(testUser);
    }

    @Test
    public void testJButton4ActionPerformed_whenCalledWithValidUserData_thenUserShouldBeUpdated() {
        when(adminApp.updateUser(testUser)).thenReturn(new User("UpdatedTestUserName", USER_EMAIL, USER_PHONE, USER_TITLE, SHOW_USER_STATUS, USER_ONLY_START_CHAT, CAN_JOIN_CHAT_ROOM));
        updateUserPanel.jTextField1.setText("UpdatedTestUserName");
        updateUserPanel.jTextField2.setText(USER_EMAIL);
        updateUserPanel.jTextField3.setText(USER_PHONE);
        updateUserPanel.jTextField4.setText(USER_TITLE);
        updateUserPanel.jCheckBox1.setSelected(SHOW_USER_STATUS);
        updateUserPanel.jCheckBox2.setSelected(USER_ONLY_START_CHAT);
        updateUserPanel.jCheckBox3.setSelected(CAN_JOIN_CHAT_ROOM);
        updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));
        verify(adminApp).updateUser(testUser);
        verify(testGroup.getUsers()).set(0, testUser);
    }

    @Test
    public void testJComboBox1ItemStateChanged_whenCalledWithValidGroup_thenSecondComboBoxShouldBeFilledWithUsers() {
        updateUserPanel.jComboBox1ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox1, ItemEvent.ITEM_STATE_CHANGED, 0));
        assertEquals(2, updateUserPanel.jComboBox2.getItemCount());
        assertSame(testUser, updateUserPanel.jComboBox2.getSelectedItem());
    }

    @Test
    public void testJComboBox2ItemStateChanged_whenCalledWithValidUser_thenUserDataShouldBeFilled() {
        updateUserPanel.jComboBox2ItemStateChanged(new ItemEvent(updateUserPanel.jComboBox2, ItemEvent.ITEM_STATE_CHANGED, 0));
        assertEquals(USER_NAME, updateUserPanel.jTextField1.getText());
        assertEquals(USER_EMAIL, updateUserPanel.jTextField2.getText());
        assertEquals(USER_PHONE, updateUserPanel.jTextField3.getText());
        assertEquals(USER_TITLE, updateUserPanel.jTextField4.getText());
        assertEquals(SHOW_USER_STATUS, updateUserPanel.jCheckBox1.isSelected());
        assertEquals(USER_ONLY_START_CHAT, updateUserPanel.jCheckBox2.isSelected());
        assertEquals(CAN_JOIN_CHAT_ROOM, updateUserPanel.jCheckBox3.isSelected());
    }
}