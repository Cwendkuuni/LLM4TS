import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.junit.Before;
import org.junit.Test;

import osa.ora.server.admin.AdminApp;
import osa.ora.server.admin.Group;
import osa.ora.server.admin.User;
import osa.ora.server.beans.StringVector;

public class UpdateUserPanelTest {

 private UpdateUserPanel updateUserPanel;
 private AdminApp adminApp;
 private Group group;
 private User user;

 @Before
 public void setup() {
 adminApp = new AdminApp();
 group = new Group("Test Group");
 user = new User("Test User", "test@test.com", "password", "Test", true, true, true);
 group.addUser(user);
 adminApp.setGroups(new Vector<Group>(Arrays.asList(group)));
 updateUserPanel = new UpdateUserPanel(adminApp, null);
 }

 // Test cases go here

}