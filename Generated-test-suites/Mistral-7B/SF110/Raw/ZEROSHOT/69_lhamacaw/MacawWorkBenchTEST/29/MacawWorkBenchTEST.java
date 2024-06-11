package macaw.presentationLayer.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import macaw.businessLayer.VariableConstants;
import macaw.businessLayer.VariableEditorManager;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.production.ProductionCurationService;
import macaw.presentationLayer.MacawWorkBench;
import macaw.presentationLayer.UserInterfaceFactory;
import macaw.system.SessionProperties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private UserInterfaceFactory userInterfaceFactory;
 private SessionProperties sessionProperties;
 private VariableConstants variableConstants;
 private VariableEditorManager variableEditorManager;
 private DemonstrationCurationService demoDatabase;
 private ProductionCurationService productionDatabase;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = new UserInterfaceFactory(sessionProperties);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 demoDatabase = new DemonstrationCurationService(false);
 productionDatabase = new ProductionCurationService(sessionProperties);
 variableConstants = new VariableConstants(demoDatabase);
 variableEditorManager = new VariableEditorManager(sessionProperties, userInterfaceFactory, variableConstants);
 }

 @After
 public void tearDown() {
 macawWorkBench = null;
 userInterfaceFactory = null;
 sessionProperties = null;
 variableConstants = null;
 variableEditorManager = null;
 demoDatabase.close();
 productionDatabase.close();
 }

 @Test
 public void testMacawWorkBenchConstruction() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 assertNotNull(macawWorkBench.dialog);
 assertNotNull(macawWorkBench.editConstants);
 assertNotNull(macawWorkBench.editVariables);
 assertNotNull(macawWorkBench.exportVariableData);
 assertNotNull(macawWorkBench.exit);
 }

 @Test
 public void testShowDialog() {
 macawWorkBench.show();
 assertTrue(macawWorkBench.dialog.isVisible());
 }

 @Test
 public void testEditConstantsButton() {
 macawWorkBench.editConstants();
 assertTrue(variableEditorManager.isVariableConstantsEditorOpen());
 macawWorkBench.dialog.dispose();
 }

 @Test
 public void testEditVariablesButton() {
 macawWorkBench.editVariables();
 assertTrue(variableEditorManager.isVariableBrowserOpen());
 macawWorkBench.dialog.dispose();
 }

 @Test
 public void testExportVariableDataButton() {
 macawWorkBench.exportVariableData();
 assertTrue(variableConstants.isVariablesExported());
 macawWorkBench.dialog.dispose();
 }

 @Test
 public void testExitButton() {
 macawWorkBench.exit();
 assertFalse(macawWorkBench.dialog.isVisible());
 }
}