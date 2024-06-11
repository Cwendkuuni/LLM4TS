package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.system.MacawMessages;
import org.junit.Test;

public class MacawWorkBenchTest {

 private SessionProperties sessionProperties;
 private MacawWorkBench macawWorkBench;

 @Test
 public void testMacawWorkBenchConstruction() {
 sessionProperties = new SessionProperties();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 assertNotNull(macawWorkBench.dialog);
 }

 @Test
 public void testEditConstantsMethod() {
 sessionProperties = new SessionProperties();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.editConstants();
 // Add assertions for expected behavior when editing constants
 }

 @Test
 public void testEditVariablesMethod() {
 sessionProperties = new SessionProperties();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.editVariables();
 // Add assertions for expected behavior when editing variables
 }

 @Test
 public void testExportVariableDataMethod() {
 sessionProperties = new SessionProperties();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.exportVariableData();
 // Add assertions for expected behavior when exporting variable data
 }

 @Test(expected = Exception.class)
 public void testExportVariableDataWithNullSessionProperties() {
 macawWorkBench = new MacawWorkBench(null);
 macawWorkBench.exportVariableData();
 }

 @Test
 public void testExitMethod() {
 sessionProperties = new SessionProperties();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.exit();
 // System.exit() cannot be tested directly in JUnit
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMacawWorkBenchConstructorWithNullSessionProperties() {
 macawWorkBench = new MacawWorkBench(null);
 }

 @Test
 public void testCreateButtonPanel() {
 sessionProperties = new SessionProperties();
 UserInterfaceFactory userInterfaceFactory = sessionProperties.getUserInterfaceFactory();
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertNotNull(buttonPanel.getComponent(0)); // editConstants
 assertNotNull(buttonPanel.getComponent(1)); // editVariables
 assertNotNull(buttonPanel.getComponent(2)); // exportVariableData
 assertNotNull(buttonPanel.getComponent(3)); // exit
 }
}