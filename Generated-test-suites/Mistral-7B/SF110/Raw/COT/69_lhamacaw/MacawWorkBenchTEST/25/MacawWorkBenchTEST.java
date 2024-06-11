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

 // Test for the constructor
 @Test
 public void testMacawWorkBenchConstructor() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 assertNotNull(macawWorkBench);
 }

 // Test for the show method
 @Test
 public void testShow() {
 macawWorkBench.show();
 assertTrue(macawWorkBench.dialog.isVisible());
 }

 // Test for the editConstants method
 @Test
 public void testEditConstants() {
 macawWorkBench.editConstants();
 // Add assertions here based on the expected behavior of the VariableConstantsEditor
 }

 // Test for the editVariables method
 @Test
 public void testEditVariables() {
 macawWorkBench.editVariables();
 // Add assertions here based on the expected behavior of the MacawVariableBrowser
 }

 // Test for the exportVariableData method
 @Test
 public void testExportVariableData() {
 macawWorkBench.exportVariableData();
 // Add assertions here based on the expected behavior of ExportVariablesToMacawXML
 }

 // Test for the exit method
 @Test
 public void testExit() {
 macawWorkBench.exit();
 // Add system exit verification here
 }

 // Test for the main method with demo database
 @Test
 public void testMacawWorkBenchMainWithDemoDatabase() {
 final String dbUser = null;
 final String dbPassword = null;
 final SessionProperties sessionProperties = new SessionProperties();
 final StartupOptions startupOptions = new StartupOptions();
 sessionProperties.setProperty("startupOptions", startupOptions);
 final User currentUser = new User("jsmith", "cool");
 sessionProperties.setProperty("currentUser", currentUser);
 sessionProperties.setProperty("retrievalService", new DemonstrationRetrievalService(sessionProperties));
 sessionProperties.setProperty("database", new DemonstrationCurationService(false));
 MacawWorkBench.main(new String[0]);
 // Add assertions here based on the expected behavior of the application with demo database
 }

 // Test for the main method with production database
 @Test
 public void testMacawWorkBenchMainWithProductionDatabase() {
 final String dbUser = null;
 final String dbPassword = null;
 final SessionProperties sessionProperties = new SessionProperties();
 final StartupOptions startupOptions = new StartupOptions();
 sessionProperties.setProperty("startupOptions", startupOptions);
 final User currentUser = new User("jsmith", "cool");
 sessionProperties.setProperty("currentUser", currentUser);
 sessionProperties.setProperty("retrievalService", new ProductionRetrievalService(sessionProperties));
 sessionProperties.setProperty("database", new ProductionCurationService(sessionProperties));
 MacawWorkBench.main(new String[0]);
 // Add assertions here based on the expected behavior of the application with production database
 }

 // Test for the exception handling in the main method
 @Test(expected = Exception.class)
 public void testMacawWorkBenchMainWithException() {
 final String dbUser = null;
 final String dbPassword = null;
 final SessionProperties sessionProperties = new SessionProperties();
 final StartupOptions startupOptions = new StartupOptions();
 sessionProperties.setProperty("startupOptions", startupOptions);
 sessionProperties.setProperty("currentUser", new User("jsmith", "cool"));
 sessionProperties.setProperty("retrievalService", new ExceptionThrowingRetrievalService());
 sessionProperties.setProperty("database", new ExceptionThrowingCurationService());
 MacawWorkBench.main(new String[0]);
 }

}