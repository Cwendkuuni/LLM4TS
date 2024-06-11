package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.*;
import macaw.presentationLayer.MacawWorkBench;
import org.junit.Test;

public class MacawWorkBenchTest {

 private SessionProperties sessionProperties;
 private MacawWorkBench macawWorkBench;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testMacawWorkBenchConstructor() {
 // Test the MacawWorkBench constructor
 macawWorkBench = new MacawWorkBench(sessionProperties);
 assertNotNull(macawWorkBench.dialog);
 assertNotNull(macawWorkBench.editConstants);
 assertNotNull(macawWorkBench.editVariables);
 assertNotNull(macawWorkBench.exportVariableData);
 assertNotNull(macawWorkBench.exit);
 }

 @Test
 public void testShowDialog() {
 // Test the show() method
 macawWorkBench.show();
 assertTrue(macawWorkBench.dialog.isVisible());
 }

 @Test
 public void testEditConstants() {
 // Test the editConstants() method
 macawWorkBench.editConstants();
 // Add assertions here based on the behavior of the VariableConstantsEditor
 }

 @Test
 public void testEditVariables() {
 // Test the editVariables() method
 macawWorkBench.editVariables();
 // Add assertions here based on the behavior of the MacawVariableBrowser
 }

 @Test
 public void testExportVariableData() {
 // Test the exportVariableData() method
 macawWorkBench.exportVariableData();
 // Add assertions here based on the behavior of ExportVariablesToMacawXML
 }

 @Test(expected = NullPointerException.class)
 public void testNullSessionProperties() {
 sessionProperties = null;
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidSessionProperties() {
 sessionProperties = new SessionProperties();
 sessionProperties.setProperty("sessionKey", null);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyDatabase() {
 Database database = new DemonstrationCurationService(true);
 sessionProperties.setProperty("database", database);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullRetrievalService() {
 RetrievalService retrievalService = null;
 sessionProperties.setProperty("retrievalService", retrievalService);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test(expected = LoginException.class)
 public void testLoginFailed() {
 sessionProperties.setProperty("currentUser", new User("invalidUser", "invalidPassword"));
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testLoginSuccess() {
 sessionProperties.setProperty("currentUser", new User("jsmith", "cool"));
 macawWorkBench = new MacawWorkBench(sessionProperties);
 assertTrue(macawWorkBench.dialog.isVisible());
 }

}