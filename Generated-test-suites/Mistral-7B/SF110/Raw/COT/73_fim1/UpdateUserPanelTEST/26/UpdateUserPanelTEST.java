import static org.junit.Assert.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class UpdateUserPanelTest {

    private AdminApp adminApp;
    private ControlPanel controlPanel;
    private UpdateUserPanel updateUserPanel;
    private Group group;
    private User user;
    private User dummyUser;

    @Before
    public void setUp() {
        adminApp = mock(AdminApp.class);
        controlPanel = mock(ControlPanel.class);
        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
        group = new Group("Group1");
        user = new User("User1", "user1@example.com", "1234567890", "Title1", true, true, true);
        dummyUser = new User("DummyUser", "dummy@example.com", null, null, false, false, false);
        when(adminApp.getGroups()).thenReturn(new ArrayList<>());
        when(adminApp.getGroups()).thenReturn(Arrays.asList(group));
        when(adminApp.getGroups().get(0).getUsers()).thenReturn(new ArrayList<>());
        when(adminApp.getGroups().get(0).getUsers()).thenReturn(Arrays.asList(user, dummyUser));
    }

    @Test
    public void testUpdateUserPanelConstruction() {
        assertNotNull(updateUserPanel);
    }

    @Test
    public void testUpdateModel() {
        updateUserPanel.updateModel();
        assertEquals(adminApp.getGroups().get(0).getUsers().get(0), updateUserPanel.jComboBox2.getSelectedItem());
    }

    @Test
    public void testJComboBox1ItemStateChanged() {
        updateUserPanel.jComboBox1.setSelectedIndex(1);
        updateUserPanel.jComboBox1ItemStateChanged(null);
        assertEquals(adminApp.getGroups().get(1).getUsers(), updateUserPanel.jComboBox2.getModel().toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton2ActionPerformedWithNullUser() {
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(new Object(), new ActionEvent.ActionEventHandler(), 0, "Button2Clicked", ""));
    }

    @Test
    public void testJButton2ActionPerformedWithValidUser() {
        updateUserPanel.jButton2ActionPerformed(new ActionEvent(user, new ActionEvent.ActionEventHandler(), 0, "Button2Clicked", ""));
        verify(adminApp, times(1)).delUser(user);
        assertEquals(adminApp.getGroups().get(0).getUsers().size(), 1);
        assertEquals(adminApp.getGroups().get(0).getUsers().get(0), dummyUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton3ActionPerformedWithNullUser() {
        updateUserPanel.jButton3ActionPerformed(new ActionEvent(new Object(), new ActionEvent.ActionEventHandler(), 0, "Button3Clicked", ""));
    }

    @Test
    public void testJButton3ActionPerformedWithValidUser() {
        updateUserPanel.jButton3ActionPerformed(new ActionEvent(user, new ActionEvent.ActionEventHandler(), 0, "Button3Clicked", ""));
        verify(adminApp, times(1)).resetUserPass(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton4ActionPerformedWithNullUser() {
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(new Object(), new ActionEvent.ActionEventHandler(), 0, "Button4Clicked", ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton4ActionPerformedWithEmptyUserName() {
        updateUserPanel.jTextField1.setText("");
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(user, new ActionEvent.ActionEventHandler(), 0, "Button4Clicked", ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton4ActionPerformedWithEmptyUserEmail() {
        updateUserPanel.jTextField2.setText("");
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(user, new ActionEvent.ActionEventHandler(), 0, "Button4Clicked", ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJButton4ActionPerformedWithTwoUsersWithSameEmail() {
        when(adminApp.getGroups().get(0).getUsers().get(1).getEmail()).thenReturn("user1@example.com");
        updateUserPanel.jTextField1.setText("User1");
        updateUserPanel.jTextField2.setText("user1@example.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("Title1");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(user, new ActionEvent.ActionEventHandler(), 0, "Button4Clicked", ""));
    }

    @Test
    public void testJButton4ActionPerformedWithValidUserData() {
        updateUserPanel.jTextField1.setText("User1");
        updateUserPanel.jTextField2.setText("user1@example.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("Title1");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(new ActionEvent(user, new ActionEvent.ActionEventHandler(), 0, "Button4Clicked", ""));
        verify(adminApp, times(1)).updateUser(user);
        assertEquals(user.getName(), updateUserPanel.jTextField1.getText());
        assertEquals(user.getEmail(), updateUserPanel.jTextField2.getText());
        assertEquals(user.getDirectPhone(), updateUserPanel.jTextField3.getText());
        assertEquals(user.getJobTitle(), updateUserPanel.jTextField4.getText());
        assertEquals(user.isShowMyStatus(), updateUserPanel.jCheckBox1.isSelected());
        assertEquals(user.isOnlyStartChat(), updateUserPanel.jCheckBox2.isSelected());
        assertEquals(user.isCanJoinChatRoom(), updateUserPanel.jCheckBox3.isSelected());
    }
}