package macaw.presentationLayer.test;

import static org.junit.Assert.assertEquals;
import macaw.businessLayer.VariableConstantsEditor;
import macaw.businessLayer.MacawVariableBrowser;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.production.ProductionCurationService;
import macaw.presentationLayer.MacawWorkBench;
import macaw.system.SessionProperties;
import org.junit.Before;
import org.junit.Test;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private SessionProperties sessionProperties;

 @Before
 public void setup() {
 sessionProperties = new SessionProperties();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testMacawWorkBenchConstructor() {
 // Test the constructor of MacawWorkBench
 }

 @Test
 public void testCreateButtonPanel() {
 // Test the createButtonPanel method
 }

 @Test
 public void testShow() {
 // Test the show method
 macawWorkBench.show();
 // Add assertions to check the dialog's properties after show method is called
 }

 @Test
 public void testEditConstants() {
 // Test the editConstants method
 macawWorkBench.editConstants();
 }

 @Test
 public void testEditVariables() {
 // Test the editVariables method
 macawWorkBench.editVariables();
 }

 @Test
 public void testExportVariableData() {
 // Test the exportVariableData method
 macawWorkBench.exportVariableData();
 }

 @Test
 public void testExit() {
 // Test the exit method
 macawWorkBench.exit();
 }

 @Test(expected = Exception.class)
 public void testMainMethodWithInvalidArguments() {
 MacawWorkBench.main(new String[]{"--invalid","argument"});
 }

 @Test(expected = Exception.class)
 public void testMainMethodWithDatabaseConnectionFailure() {
 SessionProperties sessionProperties = new SessionProperties();
 sessionProperties.setProperty("dbUser", "invalidUser");
 sessionProperties.setProperty("dbPassword", "invalidPassword");
 try {
 MacawWorkBench.main(new String[0]);
 } catch (Exception e) {
 // Expected exception
 }
 }

 @Test
 public void testMainMethodWithSuccessfulLogin() {
 SessionProperties sessionProperties = new SessionProperties();
 sessionProperties.setProperty("dbUser", "validUser");
 sessionProperties.setProperty("dbPassword", "validPassword");
 try {
 MacawWorkBench.main(new String[0]);
 } catch (Exception e) {
 // Unexpected exception
 fail("Unexpected exception during successful login");
 }
 }

 @Test
 public void testLoginDialogIsCancelled() {
 // Test the LoginDialog isCancelled method
 }

 @Test
 public void testLoginDialogIsLoginSuccessful() {
 // Test the LoginDialog isLoginSuccessful method
 }

 @Test
 public void testSessionPropertiesGetProperty() {
 // Test the getProperty method of SessionProperties
 sessionProperties.setProperty("testProperty", "testValue");
 assertEquals("testValue", macawWorkBench.sessionProperties.getProperty("testProperty"));
 }

 @Test
 public void testStartupOptionsProcessCommandLineArguments() {
 // Test the processCommandLineArguments method of StartupOptions
 }

 @Test
 public void testUserConstructor() {
 // Test the User constructor
 new User("jsmith", "cool");
 }

 @Test
 public void testDemonstrationCurationService() {
 SessionProperties sessionProperties = new SessionProperties();
 sessionProperties.setProperty("dbUser", "testUser");
 sessionProperties.setProperty("dbPassword", "testPassword");
 DemonstrationCurationService database = new DemonstrationCurationService(true);
 }

 @Test
 public void testProductionCurationService() {
 SessionProperties sessionProperties = new SessionProperties();
 sessionProperties.setProperty("dbUser", "testUser");
 sessionProperties.setProperty("dbPassword", "testPassword");
 ProductionCurationService database = new ProductionCurationService(sessionProperties);
 }

 @Test
 public void testLoginDialogIsLoginSuccessfulWithValidCredentials() {
 LoginDialog loginDialog = new LoginDialog(sessionProperties, 0);
 loginDialog.show();
 loginDialog.setLoginSuccessful(true);
 assertTrue(loginDialog.isLoginSuccessful());
 }

 @Test
 public void testLoginDialogIsCancelled() {
 LoginDialog loginDialog = new LoginDialog(sessionProperties, 0);
 loginDialog.cancel();
 assertTrue(loginDialog.isCancelled());
 }

 @Test
 public void testVariableConstantsEditorShow() {
 VariableConstantsEditor variableConstantsEditor = new VariableConstantsEditor(sessionProperties, true);
 variableConstantsEditor.show();
 }

 @Test
 public void testMacawVariableBrowserShow() {
 MacawVariableBrowser macawVariableBrowser = new MacawVariableBrowser(sessionProperties);
 macawVariableBrowser.show();
 }

 @Test
 public void testExportVariablesToMacawXMLExport() {
 ExportVariablesToMacawXML exportToDSSFormat = new ExportVariablesToMacawXML(sessionProperties);
 exportToDSSFormat.export();
 }
}