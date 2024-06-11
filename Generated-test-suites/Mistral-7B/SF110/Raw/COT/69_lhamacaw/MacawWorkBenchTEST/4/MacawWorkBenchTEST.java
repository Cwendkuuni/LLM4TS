package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.system.*;
import org.junit.Before;
import org.junit.Test;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private SessionProperties sessionProperties;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 // Test for MacawWorkBench constructor
 @Test
 public void testMacawWorkBenchConstructor() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 assertNotNull(macawWorkBench);
 }

 // Test for show method
 @Test
 public void testShow() {
 macawWorkBench.show();
 assertTrue(macawWorkBench.dialog.isVisible());
 }

 // Test for editConstants method
 @Test
 public void testEditConstants() {
 macawWorkBench.editConstants();
 // Add assertions here to check the behavior of the VariableConstantsEditor
 }

 // Test for editVariables method
 @Test
 public void testEditVariables() {
 macawWorkBench.editVariables();
 // Add assertions here to check the behavior of the MacawVariableBrowser
 }

 // Test for exportVariableData method
 @Test
 public void testExportVariableData() {
 macawWorkBench.exportVariableData();
 // Add assertions here to check the behavior of ExportVariablesToMacawXML
 }

 // Test for exit method
 @Test
 public void testExit() {
 macawWorkBench.exit();
 // Add system exit verification here
 }

 // Test for main method (with demo database)
 @Test
 public void testMacawWorkBenchWithDemoDatabase() {
 final String dbUser = "demoUser";
 final String dbPassword = "demoPassword";
 sessionProperties.setProperty("dbUser", dbUser);
 sessionProperties.setProperty("dbPassword", dbPassword);
 MacawWorkBench.main(new String[]{});
 // Add assertions here to check the behavior of the application with a demo database
 }

 // Test for main method (with production database)
 @Test
 public void testMacawWorkBenchWithProductionDatabase() {
 final String dbUser = "productionUser";
 final String dbPassword = "productionPassword";
 sessionProperties.setProperty("dbUser", dbUser);
 sessionProperties.setProperty("dbPassword", dbPassword);
 MacawWorkBench.main(new String[]{});
 // Add assertions here to check the behavior of the application with a production database
 }

 // Test for login dialog with correct credentials
 @Test
 public void testLoginDialogWithCorrectCredentials() {
 final LoginDialog loginDialog = new LoginDialog(sessionProperties, 0);
 loginDialog.show();
 assertTrue(loginDialog.isLoginSuccessful());
 }

 // Test for login dialog with incorrect credentials (max number of attempts)
 @Test(expected = SystemExitException.class)
 public void testLoginDialogWithIncorrectCredentials() {
 final LoginDialog loginDialog = new LoginDialog(sessionProperties, 2);
 loginDialog.show();
 // Add assertions here to check the behavior of the application when maximum number of login attempts is reached
 }

}