package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
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

 @Test
 public void testMacawWorkBenchConstructor() {
 // Test the constructor
 macawWorkBench = new MacawWorkBench(sessionProperties);
 assertNotNull(macawWorkBench.dialog);
 assertNotNull(macawWorkBench.editConstants);
 assertNotNull(macawWorkBench.editVariables);
 assertNotNull(macawWorkBench.exportVariableData);
 assertNotNull(macawWorkBench.exit);
 }

 @Test
 public void testShowMethod() {
 // Test the show method
 macawWorkBench.show();
 assertTrue(macawWorkBench.dialog.isVisible());
 }

 @Test
 public void testCreateButtonPanelMethod() {
 // Test the createButtonPanel method
 assertTrue(macawWorkBench.createButtonPanel(null).getComponentCount() > 0);
 }

 @Test
 public void testEditConstantsMethod() {
 // Test the editConstants method
 macawWorkBench.editConstants();
 // Add assertions here based on expected behavior
 }

 @Test
 public void testEditVariablesMethod() {
 // Test the editVariables method
 macawWorkBench.editVariables();
 // Add assertions here based on expected behavior
 }

 @Test
 public void testExportVariableDataMethod() {
 // Test the exportVariableData method
 macawWorkBench.exportVariableData();
 // Add assertions here based on expected behavior
 }

 @Test
 public void testExitMethod() {
 // Test the exit method
 macawWorkBench.exit();
 // Add assertions here based on expected behavior
 }

 @Test(expected = Exception.class)
 public void testMainMethodWithInvalidArguments() {
 MacawWorkBench.main(new String[]{"invalidArg1", "invalidArg2"});
 // Add assertions here based on expected exception message
 }

 @Test(expected = Exception.class)
 public void testMainMethodWithoutArguments() {
 MacawWorkBench.main(new String[]{});
 // Add assertions here based on expected exception message
 }

 @Test
 public void testDatabaseInitialization() {
 // Test the initialization of the database based on the useDemo flag
 MacawWorkBench.main(new String[]{"--demo"});
 assertTrue(sessionProperties.getProperty("database") instanceof DemonstrationCurationService);
 MacawWorkBench.main(new String[]{"--production"});
 assertTrue(sessionProperties.getProperty("database") instanceof ProductionCurationService);
 }
}