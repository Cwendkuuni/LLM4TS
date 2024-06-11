package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.system.StartupOptions;
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
 // Testing if the constructor is executed successfully
 assertNotNull(macawWorkBench);
 }

 @Test
 public void testCreateButtonPanel() {
 // Testing if createButtonPanel method creates and returns a JPanel
 JPanel buttonPanel = macawWorkBench.createButtonPanel(null);
 assertNotNull(buttonPanel);
 }

 @Test
 public void testEditConstantsMethod() {
 // Testing if editConstants method opens VariableConstantsEditor dialog
 macawWorkBench.editConstants();
 // Add assertions to check if VariableConstantsEditor dialog is displayed or not
 }

 @Test
 public void testEditVariablesMethod() {
 // Testing if editVariables method opens MacawVariableBrowser dialog
 macawWorkBench.editVariables();
 // Add assertions to check if MacawVariableBrowser dialog is displayed or not
 }

 @Test
 public void testExportVariableDataMethod() {
 // Testing if exportVariableData method exports variables to MacawXML format
 ExportVariablesToMacawXML exportToDSSFormat = new ExportVariablesToMacawXML(sessionProperties);
 macawWorkBench.exportVariableData();
 exportToDSSFormat.export();
 // Add assertions to check if the file is created and contains the expected data
 }

 @Test(expected = NullPointerException.class)
 public void testExitMethod() {
 macawWorkBench.exit();
 System.exit(0); // This line should not be reached
 }

 @Test
 public void testMainMethodWithDemoDatabase() {
 // Testing if MacawWorkBench is instantiated with demo database
 StartupOptions startupOptions = new StartupOptions();
 startupOptions.setUseDemoDatabase(true);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 assertEquals(DemostrationCurationService.class, sessionProperties.getProperty("database").getClass());
 }

 @Test
 public void testMainMethodWithProductionDatabase() {
 // Testing if MacawWorkBench is instantiated with production database
 StartupOptions startupOptions = new StartupOptions();
 startupOptions.setUseDemoDatabase(false);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 assertEquals(ProductionCurationService.class, sessionProperties.getProperty("database").getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoginFailedThreeTimes() {
 LoginDialog loginDialog = new LoginDialog(sessionProperties, 0);
 for (int i = 0; i < 3; i++) {
 loginDialog.show();
 assertFalse(loginDialog.isLoginSuccessful());
 }
 // Add assertions to check if the application exits after three failed login attempts
 }
}