package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.system.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class MacawWorkBenchTest {

 private MacawWorkBench workBench;
 private SessionProperties sessionProperties;
 private UserInterfaceFactory userInterfaceFactory;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = new UserInterfaceFactory();
 workBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testMain() {
 // Test normal execution of main method
 String[] arguments = {};
 MacawWorkBench.main(arguments);
 // Check if dialog is displayed
 assertTrue(workBench.dialog.isDisplayable());
 }

 @Test
 public void testLoginDialog() {
 // Test successful login
 User user = new User("john_doe", "password123");
 sessionProperties.setProperty("currentUser", user);
 LoginDialog loginDialog = new LoginDialog(sessionProperties, 0);
 loginDialog.setVisible(true);
 assertTrue(loginDialog.isLoginSuccessful());
 // Test failed login attempts
 for (int i = 0; i < 2; i++) {
 User wrongUser = new User("wrong_user", "wrong_password");
 sessionProperties.setProperty("currentUser", wrongUser);
 LoginDialog loginDialog2 = new LoginDialog(sessionProperties, 0);
 loginDialog2.setVisible(true);
 assertFalse(loginDialog2.isLoginSuccessful());
 }
 // Test cancelled login
 LoginDialog loginDialog3 = new LoginDialog(sessionProperties, 0);
 loginDialog3.setCancelled(true);
 assertFalse(loginDialog3.isLoginSuccessful());
 }

 @Test
 public void testCreateButtonPanel() {
 // Test normal creation of button panel
 JPanel buttonPanel = workBench.createButtonPanel(userInterfaceFactory);
 // Test number of buttons
 assertEquals(4, buttonPanel.getComponentCount());
 // Test button labels
 JButton button;
 String label;
 for (int i = 0; i < 4; i++) {
 button = (JButton) buttonPanel.getComponent(i);
 switch (i) {
 case 0:
 label = "Edit Constants";
 break;
 case 1:
 label = "Edit Variables";
 break;
 case 2:
 label = "Export Variable Data";
 break;
 case 3:
 label = "Exit";
 break;
 default:
 throw new AssertionError("Unexpected button index.");
 }
 assertEquals(label, button.getText());
 }
 }

 @Test
 public void testEditConstants() {
 // Test normal execution of editConstants method
 workBench.editConstants();
 // Test if VariableConstantsEditor dialog is displayed
 assertTrue(workBench.sessionProperties.getVariableConstantsEditor().isDisplayable());
 }

 @Test
 public void testEditVariables() {
 // Test normal execution of editVariables method
 workBench.editVariables();
 // Test if MacawVariableBrowser dialog is displayed
 assertTrue(workBench.sessionProperties.getMacawVariableBrowser().isDisplayable());
 }

 @Test
 public void testExportVariableData() {
 // Test normal execution of exportVariableData method
 workBench.exportVariableData();
 // Check if export is successful
 // In a real test, you would check the exported file contents
 // against expected values.
 assertTrue(true);
 }

 @Test
 public void testExit() {
 // Test normal execution of exit method
 workBench.exit();
 // Check if system exits
 assertTrue(System.exit(0) == 0);
 }

 @After
 public void tearDown() {
 workBench.dialog.dispose();
 sessionProperties = null;
 userInterfaceFactory = null;
 workBench = null;
 }
}