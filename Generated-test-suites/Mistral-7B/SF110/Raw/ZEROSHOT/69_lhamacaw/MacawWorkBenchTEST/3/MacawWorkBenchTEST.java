package macaw.presentationLayer.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import macaw.businessLayer.VariableConstants;
import macaw.businessLayer.VariableConstantsEditor;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.production.ProductionCurationService;
import macaw.persistenceLayer.production.ProductionRetrievalService;
import macaw.presentationLayer.MacawWorkBench;
import macaw.presentationLayer.UserInterfaceFactory;
import macaw.system.SessionProperties;
import macaw.system.StartupOptions;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.JButton;

public class MacawWorkBenchTest {

 private MacawWorkBench workBench;
 private UserInterfaceFactory userInterfaceFactory;
 private SessionProperties sessionProperties;
 private JButton editConstants;
 private JButton editVariables;
 private JButton exportVariableData;
 private JButton exit;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = new UserInterfaceFactory(sessionProperties);
 workBench = new MacawWorkBench(sessionProperties);
 editConstants = workBench.createButtonPanel(userInterfaceFactory).getComponent(0);
 editVariables = workBench.createButtonPanel(userInterfaceFactory).getComponent(1);
 exportVariableData = workBench.createButtonPanel(userInterfaceFactory).getComponent(2);
 exit = workBench.createButtonPanel(userInterfaceFactory).getComponent(3);
 }

 @After
 public void tearDown() {
 workBench.dispose();
 sessionProperties = null;
 userInterfaceFactory = null;
 workBench = null;
 editConstants = null;
 editVariables = null;
 exportVariableData = null;
 exit = null;
 }

 @Test
 public void testMacawWorkBenchConstruction() {
 assertTrue(workBench != null);
 }

 @Test
 public void testCreateButtonPanel() {
 assertFalse(editConstants == null);
 assertFalse(editVariables == null);
 assertFalse(exportVariableData == null);
 assertFalse(exit == null);
 }

 @Test
 public void testEditConstantsButton() {
 VariableConstantsEditor variableConstantsEditor = null;
 try {
 variableConstantsEditor = new VariableConstantsEditor(sessionProperties, true);
 } catch (Exception e) {
 e.printStackTrace();
 }
 assertTrue(variableConstantsEditor != null);
 workBench.editConstants();
 assertTrue(variableConstantsEditor.isShowing());
 }

 @Test
 public void testEditVariablesButton() {
 MacawVariableBrowser macawVariableBrowser = null;
 try {
 macawVariableBrowser = new MacawVariableBrowser(sessionProperties);
 } catch (Exception e) {
 e.printStackTrace();
 }
 assertTrue(macawVariableBrowser != null);
 workBench.editVariables();
 assertTrue(macawVariableBrowser.isShowing());
 }

 @Test
 public void testExportVariableDataButton() {
 ExportVariablesToMacawXML exportToDSSFormat = null;
 ProductionRetrievalService retrievalService = new ProductionRetrievalService(sessionProperties);
 ProductionCurationService database = new ProductionCurationService(sessionProperties);
 sessionProperties.setProperty("retrievalService", retrievalService);
 sessionProperties.setProperty("database", database);
 try {
 exportToDSSFormat = new ExportVariablesToMacawXML(sessionProperties);
 } catch (Exception e) {
 e.printStackTrace();
 }
 assertNull(exportToDSSFormat.getExportedFile());
 workBench.exportVariableData();
 // This test is not comprehensive as we cannot test the file writing in JUnit
 // but we can check if the method is executed correctly
 assertTrue(exportToDSSFormat.isExporting());
 }

 @Test
 public void testExitButton() {
 workBench.exit();
 assertFalse(workBench.isShowing());
 }
}