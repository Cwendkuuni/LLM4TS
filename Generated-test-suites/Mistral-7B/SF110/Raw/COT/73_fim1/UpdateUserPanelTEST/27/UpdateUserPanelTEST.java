import static org.junit.Assert.*;
import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import osa.ora.server.admin.ui.*;
import org.junit.Before;
import org.junit.Test;

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
    public void testUpdateModel() {
        updateUserPanel.updateModel();
        assertNotNull(updateUserPanel.jComboBox1);
        assertNotNull(updateUserPanel.jComboBox2);
        if (adminApp.getGroups() != null && adminApp.getGroups().size() > 0) {
            assertFalse(updateUserPanel.jComboBox2.getItemCount() == 0);
        } else {
            assertTrue(updateUserPanel.jComboBox2.getItemCount() == 0);
        }
    }

    @Test
    public void testInitComponents() {
        UpdateUserPanel updateUserPanelSpy = spy(new UpdateUserPanel(adminApp, controlPanel));
        doCallRealMethod().when(updateUserPanelSpy).initComponents();

        updateUserPanelSpy.initComponents();

        verify(updateUserPanelSpy, times(1)).initComponents();
    }

    @Test(expected = NullPointerException.class)
    public void testJButton1ActionPerformedWithNullAdminApp() {
        UpdateUserPanel updateUserPanel = new UpdateUserPanel(null, controlPanel);
        updateUserPanel.jButton1 = mock(JButton.class);
        doNothing().when(updateUserPanel.jButton1).actionPerformed(any(ActionEvent.class));

        updateUserPanel.jButton1ActionPerformed(mock(ActionEvent.class));
    }

    @Test
    public void testJButton2ActionPerformedWithValidUser() {
        final Group group = new Group("TestGroup", new Vector<User>());
        final User user = new User("TestUser", "testuser@test.com", "1234567890", "Test Title", true, true, true, "N/A", "Active");
        group.getUsers().add(user);
        adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
        updateUserPanel.adminApp = adminApp;
        updateUserPanel.controlPanel = controlPanel;
        updateUserPanel.jComboBox1 = mock(JComboBox.class);
        updateUserPanel.jComboBox2 = mock(JComboBox.class);
        when(updateUserPanel.jComboBox1.getSelectedItem()).thenReturn(group);
        when(updateUserPanel.jComboBox2.getSelectedItem()).thenReturn(user);
        doNothing().when(adminApp).delUser(user);

        updateUserPanel.jButton2ActionPerformed(mock(ActionEvent.class));

        verify(adminApp, times(1)).delUser(user);
    }

    @Test(expected = NullPointerException.class)
    public void testJButton2ActionPerformedWithNullUser() {
        final Group group = new Group("TestGroup", new Vector<User>());
        updateUserPanel.adminApp = adminApp;
        updateUserPanel.controlPanel = controlPanel;
        updateUserPanel.jComboBox1 = mock(JComboBox.class);
        updateUserPanel.jComboBox2 = mock(JComboBox.class);
        when(updateUserPanel.jComboBox1.getSelectedItem()).thenReturn(group);
        when(updateUserPanel.jComboBox2.getSelectedItem()).thenReturn(null);

        updateUserPanel.jButton2ActionPerformed(mock(ActionEvent.class));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testJButton2ActionPerformedWithIndexOutOfBounds() {
        final Group group = new Group("TestGroup", new Vector<User>());
        final User user = new User("TestUser", "testuser@test.com", "1234567890", "Test Title", true, true, true, "N/A", "Active");
        group.getUsers().add(user);
        adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
        updateUserPanel.adminApp = adminApp;
        updateUserPanel.controlPanel = controlPanel;
        updateUserPanel.jComboBox1 = mock(JComboBox.class);
        updateUserPanel.jComboBox2 = mock(JComboBox.class);
        when(updateUserPanel.jComboBox1.getSelectedItem()).thenReturn(group);
        when(updateUserPanel.jComboBox2.getSelectedItem()).thenReturn(user);
        when(adminApp.getGroups().get(0).getUsers().size()).thenReturn(0);

        updateUserPanel.jButton2ActionPerformed(mock(ActionEvent.class));
    }

    @Test
    public void testJButton3ActionPerformed() {
        final User user = new User("TestUser", "testuser@test.com", "1234567890", "Test Title", true, true, true, "N/A", "Active");
        updateUserPanel.adminApp = adminApp;
        updateUserPanel.controlPanel = controlPanel;
        updateUserPanel.jComboBox2 = mock(JComboBox.class);
        when(updateUserPanel.jComboBox2.getSelectedItem()).thenReturn(user);
        doNothing().when(adminApp).resetUserPass(user);

        updateUserPanel.jButton3ActionPerformed(mock(ActionEvent.class));

        verify(adminApp, times(1)).resetUserPass(user);
    }

    @Test(expected = NullPointerException.class)
    public void testJButton3ActionPerformedWithNullUser() {
        updateUserPanel.adminApp = adminApp;
        updateUserPanel.controlPanel = controlPanel;
        updateUserPanel.jComboBox2 = mock(JComboBox.class);
        when(updateUserPanel.jComboBox2.getSelectedItem()).thenReturn(null);

        updateUserPanel.jButton3ActionPerformed(mock(ActionEvent.class));
    }

    @Test
    public void testJButton4ActionPerformedWithValidData() {
        final Group group = new Group("TestGroup", new Vector<User>());
        final User user = new User("TestUser", "testuser@test.com", "1234567890", "Test Title", true, true, true, "N/A", "Active");
        group.getUsers().add(user);
        adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
        updateUserPanel.adminApp = adminApp;
        updateUserPanel.controlPanel = controlPanel;
        updateUserPanel.jComboBox1 = mock(JComboBox.class);
        updateUserPanel.jComboBox2 = mock(JComboBox.class);
        when(updateUserPanel.jComboBox1.getSelectedItem()).thenReturn(group);
        when(updateUserPanel.jComboBox2.getSelectedItem()).thenReturn(user);
        when(adminApp.updateUser(user)).thenReturn(new User("TestUserUpdated", "testuserupdated@test.com", "1234567890", "Test Title Updated", true, true, true, "N/A", "Active"));

        updateUserPanel.jTextField1.setText("TestUserUpdated");
        updateUserPanel.jTextField2.setText("testuserupdated@test.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("Test Title Updated");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));

        verify(adminApp, times(1)).updateUser(user);
        assertEquals("TestUserUpdated", updateUserPanel.jTextField1.getText());
        assertEquals("testuserupdated@test.com", updateUserPanel.jTextField2.getText());
        assertEquals("1234567890", updateUserPanel.jTextField3.getText());
        assertEquals("Test Title Updated", updateUserPanel.jTextField4.getText());
        assertTrue(updateUserPanel.jCheckBox1.isSelected());
        assertTrue(updateUserPanel.jCheckBox2.isSelected());
        assertTrue(updateUserPanel.jCheckBox3.isSelected());
    }

    @Test
    public void testJButton4ActionPerformedWithInvalidEmail() {
        final Group group = new Group("TestGroup", new Vector<User>());
        final User user = new User("TestUser", "testuser@test.com", "1234567890", "Test Title", true, true, true, "N/A", "Active");
        group.getUsers().add(user);
        adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
        updateUserPanel.adminApp = adminApp;
        updateUserPanel.controlPanel = controlPanel;
        updateUserPanel.jComboBox1 = mock(JComboBox.class);
        updateUserPanel.jComboBox2 = mock(JComboBox.class);
        when(updateUserPanel.jComboBox1.getSelectedItem()).thenReturn(group);
        when(updateUserPanel.jComboBox2.getSelectedItem()).thenReturn(user);
        when(adminApp.updateUser(user)).thenThrow(new Exception("Invalid Email"));

        updateUserPanel.jTextField1.setText("TestUserUpdated");
        updateUserPanel.jTextField2.setText("testuser@test.comtest.com");
        updateUserPanel.jTextField3.setText("1234567890");
        updateUserPanel.jTextField4.setText("Test Title Updated");
        updateUserPanel.jCheckBox1.setSelected(true);
        updateUserPanel.jCheckBox2.setSelected(true);
        updateUserPanel.jCheckBox3.setSelected(true);
        updateUserPanel.jButton4ActionPerformed(mock(ActionEvent.class));

        verify(adminApp, never()).updateUser(user);
        assertEquals("TestUser", updateUserPanel.jTextField1.getText());
        assertEquals("testuser@test.com", updateUserPanel.jTextField2.getText());
        assertEquals("1234567890", updateUserPanel.jTextField3.getText());
        assertEquals("Test Title Updated", updateUserPanel.jTextField4.getText());
        assertFalse(updateUserPanel.jCheckBox1.isSelected());
        assertFalse(updateUserPanel.jCheckBox2.isSelected());
        assertFalse(updateUserPanel.jCheckBox3.isSelected());
    }
}