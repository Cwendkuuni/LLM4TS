package macaw.presentationLayer.test;

import static org.junit.Assert.assertTrue;
import macaw.businessLayer.VariableConstants;
import macaw.businessLayer.VariableEditor;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.production.ProductionCurationService;
import macaw.persistenceLayer.production.ProductionRetrievalService;
import macaw.presentationLayer.MacawWorkBench;
import macaw.presentationLayer.UserInterfaceFactory;
import macaw.system.SessionProperties;
import org.junit.Test;

public class MacawWorkBenchTest {

 private SessionProperties sessionProperties;
 private UserInterfaceFactory userInterfaceFactory;
 private MacawWorkBench macawWorkBench;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = new UserInterfaceFactory();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testMacawWorkBenchConstructor() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 assertTrue(macawWorkBench != null);
 }

 @Test
 public void testCreateButtonPanel() {
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertTrue(buttonPanel != null);
 }

 @Test
 public void testEditConstantsMethod() {
 VariableConstants variableConstants = new VariableConstants();
 macawWorkBench.editConstants();
 // add assertions for edge cases and exception handling here
 }

 @Test
 public void testEditVariablesMethod() {
 MacawVariableBrowser macawVariableBrowser = new MacawVariableBrowser(sessionProperties);
 macawWorkBench.editVariables();
 // add assertions for edge cases and exception handling here
 }

 @Test
 public void testExportVariableDataMethod() {
 DemonstrationCurationService database = new DemonstrationCurationService(false);
 ProductionRetrievalService retrievalService = new ProductionRetrievalService(sessionProperties);
 sessionProperties.setProperty("retrievalService", retrievalService);
 sessionProperties.setProperty("database", database);
 macawWorkBench.exportVariableData();
 // add assertions for edge cases and exception handling here
 }

 @Test
 public void testExitMethod() {
 macawWorkBench.exit();
 assertTrue(System.exit(0) == 0);
 }

 @After
 public void tearDown() {
 macawWorkBench.dialog.dispose();
 macawWorkBench = null;
 }
}