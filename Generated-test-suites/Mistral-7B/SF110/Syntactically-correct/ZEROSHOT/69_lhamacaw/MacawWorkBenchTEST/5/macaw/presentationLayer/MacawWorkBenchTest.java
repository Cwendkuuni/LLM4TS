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
 private DemonstrationCurationService demoDatabase;
 private ProductionCurationService productionDatabase;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = new UserInterfaceFactory(sessionProperties);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 if (sessionProperties.isUseDemo()) {
 demoDatabase = (DemonstrationCurationService) sessionProperties.getProperty("database");
 } else {
 productionDatabase = (ProductionCurationService) sessionProperties.getProperty("database");
 }
 }

 @After
 public void tearDown() {
 macawWorkBench.exit();
 }

 @Test
 public void testMacawWorkBenchConstruction() {
 assertEquals("macawWorkBench.title", macawWorkBench.getDialog().getTitle(), MacawMessages.getMessage("macawWorkBench.title"));
 }

 @Test
 public void testCreateButtonPanel() {
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertEquals(4, buttonPanel.getComponentCount());
 }

 @Test
 public void testEditConstantsButton() {
 macawWorkBench.editConstants();
 VariableConstants variableConstants = VariableConstants.getInstance();
 assertEquals(variableConstants.getVariableConstants().size(), demoDatabase.getVariableConstants().size());
 }

 @Test
 public void testEditVariablesButton() {
 macawWorkBench.editVariables();
 VariableEditor variableEditor = new VariableEditor(sessionProperties);
 assertEquals(variableEditor.getVariables().size(), demoDatabase.getVariables().size());
 }

 @Test
 public void testExportVariableDataButton() {
 macawWorkBench.exportVariableData();
 String expectedFilePath = "MacawVariableData.xml";
 assertTrue(new File(expectedFilePath).exists());
 }

 @Test
 public void testExitButton() {
 macawWorkBench.exit();
 assertTrue(macawWorkBench.dialog.isVisible() == false);
 }
}