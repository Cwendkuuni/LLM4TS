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
 private SessionProperties sessionProperties;
 private UserInterfaceFactory userInterfaceFactory;
 private DemonstrationCurationService demoDatabase;
 private ProductionCurationService productionDatabase;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = new UserInterfaceFactory();
 demoDatabase = new DemonstrationCurationService(false);
 productionDatabase = new ProductionCurationService(sessionProperties);
 sessionProperties.setProperty("database", (demoDatabase.isDemo()) ? demoDatabase : productionDatabase);
 sessionProperties.setProperty("retrievalService", new ProductionRetrievalService(sessionProperties));
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @After
 public void tearDown() {
 macawWorkBench.exit();
 }

 @Test
 public void testMacawWorkBenchCreation() {
 assertEquals("macawWorkBench.title", macawWorkBench.dialog.getTitle(), MacawMessages.getMessage("macawWorkBench.title"));
 }

 @Test
 public void testCreateButtonPanel() {
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertNotNull("buttonPanel should not be null", buttonPanel);
 }

 @Test
 public void testEditConstantsButton() {
 macawWorkBench.editConstants();
 assertTrue("VariableConstantsEditor should have been displayed", new VariableConstantsEditor(sessionProperties, true).isDisplayed());
 }

 @Test
 public void testEditVariablesButton() {
 macawWorkBench.editVariables();
 assertTrue("MacawVariableBrowser should have been displayed", new MacawVariableBrowser(sessionProperties).isDisplayed());
 }

 @Test
 public void testExportVariableDataButton() {
 macawWorkBench.exportVariableData();
 assertTrue("ExportVariablesToMacawXML should have been called", new ExportVariablesToMacawXML(sessionProperties).isExportCalled());
 }

 @Test
 public void testExitButton() {
 macawWorkBench.exit();
 assertTrue("System should have been exited", System.exit(0) == 0);
 }
}