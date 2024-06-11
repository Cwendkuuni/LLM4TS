package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.VariableConstants;
import macaw.businessLayer.VariableEditor;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.production.ProductionCurationService;
import macaw.presentationLayer.MacawWorkBench;
import macaw.system.SessionProperties;
import macaw.ui.UserInterfaceFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private UserInterfaceFactory userInterfaceFactory;
 private SessionProperties sessionProperties;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = new UserInterfaceFactory();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @After
 public void tearDown() {
 macawWorkBench.exit();
 }

 @Test
 public void testMacawWorkBenchConstruction() {
 MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
 assertNotNull(workBench);
 }

 @Test
 public void testCreateButtonPanel() {
 UserInterfaceFactory uiFactory = userInterfaceFactory;
 MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
 JPanel buttonPanel = workBench.createButtonPanel(uiFactory);
 assertNotNull(buttonPanel);
 }

 @Test
 public void testEditConstantsButton() {
 MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
 workBench.editConstants();
 assertTrue(workBench.sessionProperties.hasProperty("variableConstantsEditor"));
 }

 @Test(expected = IllegalStateException.class)
 public void testEditConstantsButton_NoDatabase() {
 MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
 workBench.sessionProperties.setProperty("database", null);
 workBench.editConstants();
 fail("An exception should have been thrown.");
 }

 @Test
 public void testEditVariablesButton() {
 MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
 workBench.editVariables();
 assertTrue(workBench.sessionProperties.hasProperty("macawVariableBrowser"));
 }

 @Test(expected = IllegalStateException.class)
 public void testEditVariablesButton_NoDatabase() {
 MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
 workBench.sessionProperties.setProperty("database", null);
 workBench.editVariables();
 fail("An exception should have been thrown.");
 }

 @Test
 public void testExportVariableDataButton() {
 MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
 workBench.exportVariableData();
 assertTrue(workBench.sessionProperties.hasProperty("exportVariablesToMacawXML"));
 }

 @Test(expected = IllegalStateException.class)
 public void testExportVariableDataButton_NoDatabase() {
 MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
 workBench.sessionProperties.setProperty("database", null);
 workBench.exportVariableData();
 fail("An exception should have been thrown.");
 }

 @Test
 public void testExportVariableDataButton_NoVariables() {
 ProductionCurationService productionCurationService = new ProductionCurationService(sessionProperties);
 sessionProperties.setProperty("database", productionCurationService);
 MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
 workBench.sessionProperties.removeProperty("variables");
 workBench.exportVariableData();
 assertFalse(new File("variableData.xml").exists());
 }

 @Test
 public void testExitButton() {
 MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
 workBench.exit();
 assertEquals(0, System.exitCode());
 }

 @Test
 public void testMainMethod() {
 String[] arguments = new String[0];
 MacawWorkBench.main(arguments);
 assertTrue(sessionProperties.hasProperty("retrievalService"));
 assertTrue(sessionProperties.hasProperty("database"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMainMethod_InvalidArguments() {
 String[] arguments = new String[] { "invalid arg" };
 MacawWorkBench.main(arguments);
 fail("An exception should have been thrown.");
 }
}