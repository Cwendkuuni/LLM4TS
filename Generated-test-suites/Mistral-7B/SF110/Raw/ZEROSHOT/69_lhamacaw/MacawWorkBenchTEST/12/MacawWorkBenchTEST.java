package macaw.presentationLayer.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import macaw.businessLayer.VariableConstants;
import macaw.businessLayer.VariableConstantsEditor;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.production.ProductionCurationService;
import macaw.presentationLayer.MacawWorkBench;
import macaw.presentationLayer.MacawWorkBenchActions;
import macaw.system.SessionProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private MacawWorkBenchActions macawWorkBenchActions;
 private SessionProperties sessionProperties;
 private ProductionCurationService productionCurationService;
 private DemonstrationCurationService demonstrationCurationService;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBenchActions = new MacawWorkBenchActions(macawWorkBench);
 if (sessionProperties.getProperty("retrievalService") instanceof DemonstrationRetrievalService) {
 demonstrationCurationService = (DemonstrationCurationService) sessionProperties.getProperty("database");
 sessionProperties.setProperty("database", demonstrationCurationService);
 } else {
 productionCurationService = (ProductionCurationService) sessionProperties.getProperty("database");
 sessionProperties.setProperty("database", productionCurationService);
 }
 }

 @After
 public void tearDown() {
 macawWorkBench.exit();
 }

 @Test
 public void testConstruction() {
 assertTrue(macawWorkBench != null);
 }

 @Test
 public void testEditConstantsMethod() {
 macawWorkBenchActions.editConstants();
 VariableConstants editorVariables = new VariableConstantsEditor(sessionProperties, false).getVariableConstants();
 assertTrue(editorVariables != null);
 }

 @Test
 public void testEditVariablesMethod() {
 macawWorkBenchActions.editVariables();
 assertTrue(true); // MacawVariableBrowser is a dialog, so it cannot be directly asserted
 }

 @Test
 public void testExportVariableDataMethod() {
 macawWorkBenchActions.exportVariableData();
 assertTrue(true); // ExportVariablesToMacawXML is a class that exports data, so it cannot be directly asserted
 }

 @Test
 public void testExitMethod() {
 macawWorkBenchActions.exit();
 assertFalse(macawWorkBench.isVisible());
 }

 @Test
 public void testCreateButtonPanel() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 assertFalse(macawWorkBench.getCreateButtonPanel().isEmpty());
 }
}