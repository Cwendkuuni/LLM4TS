package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.*;
import macaw.presentationLayer.*;
import org.junit.Before;
import org.junit.Test;

public class MacawWorkBenchTest {

 private SessionProperties sessionProperties;
 private MacawWorkBench macawWorkBench;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 // Test case for MacawWorkBench constructor
 @Test
 public void testMacawWorkBenchConstructor() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 assertNotNull(macawWorkBench.dialog);
 assertNotNull(macawWorkBench.editConstants);
 assertNotNull(macawWorkBench.editVariables);
 assertNotNull(macawWorkBench.exportVariableData);
 assertNotNull(macawWorkBench.exit);
 }

 // Test case for show method
 @Test
 public void testShow() {
 macawWorkBench.show();
 assertTrue(macawWorkBench.dialog.isVisible());
 }

 // Test case for editConstants method
 @Test
 public void testEditConstants() {
 macawWorkBench.editConstants();
 // Add assertions here based on the behavior of VariableConstantsEditor
 }

 // Test case for editVariables method
 @Test
 public void testEditVariables() {
 macawWorkBench.editVariables();
 // Add assertions here based on the behavior of MacawVariableBrowser
 }

 // Test case for exportVariableData method
 @Test
 public void testExportVariableData() {
 macawWorkBench.exportVariableData();
 // Add assertions here based on the behavior of ExportVariablesToMacawXML
 }

 // Test case for exit method
 @Test
 public void testExit() {
 macawWorkBench.exit();
 // Check if System.exit(0) is called
 assertFalse(macawWorkBench.dialog.isVisible());
 }

 // Test case for main method with demo database
 @Test(expected = Exception.class)
 public void testMainWithDemoDatabase() throws Exception {
 MacawWorkBench.main(new String[]{"--useDemo"});
 fail("MacawWorkBench.main() should have thrown an exception with demo database");
 }

 // Test case for main method with production database
 @Test
 public void testMainWithProductionDatabase() {
 try {
 MacawWorkBench.main(new String[]{"--useProduction"});
 } catch (Exception e) {
 e.printStackTrace();
 fail("MacawWorkBench.main() should not have thrown an exception with production database");
 }
 }

 // Test case for login dialog with invalid credentials
 @Test(expected = Exception.class)
 public void testLoginDialogWithInvalidCredentials() {
 MacawWorkBench.main(new String[]{"--login", "invalidUser", "invalidPassword"});
 fail("Login dialog should have thrown an exception with invalid credentials");
 }

 // Test case for login dialog with valid credentials
 @Test
 public void testLoginDialogWithValidCredentials() {
 MacawWorkBench.main(new String[]{"--login", "validUser", "validPassword"});
 // Check if the MacawWorkBench is shown
 assertFalse(MacawWorkBench.sessionProperties.isLoginFailed());
 assertTrue(macawWorkBench.dialog.isVisible());
 }
}