package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.VariableConstants;
import macaw.businessLayer.VariableConstantsEditor;
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
 private DemonstrationCurationService database;
 private ProductionCurationService productionDatabase;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = new UserInterfaceFactory();
 macawWorkBench = new MacawWorkBench(sessionProperties);

 if (sessionProperties.useDemo()) {
 database = new DemonstrationCurationService(false);
 sessionProperties.setProperty("database", database);
 } else {
 productionDatabase = new ProductionCurationService(sessionProperties);
 sessionProperties.setProperty("database", productionDatabase);
 }
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
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertNotNull(buttonPanel);
 }

 @Test
 public void testEditConstantsMethod() {
 MacawWorkBench spyMacawWorkBench = spy(macawWorkBench);
 doCallRealMethod().when(spyMacawWorkBench).editConstants();
 VariableConstantsEditor variableConstantsEditor = new VariableConstantsEditor(sessionProperties, true);
 verify(spyMacawWorkBench).editConstants();
 variableConstantsEditor.show();
 verify(spyMacawWorkBench).editConstants();
 }

 @Test
 public void testEditVariablesMethod() {
 MacawWorkBench spyMacawWorkBench = spy(macawWorkBench);
 doCallRealMethod().when(spyMacawWorkBench).editVariables();
 MacawVariableBrowser macawVariableBrowser = new MacawVariableBrowser(sessionProperties);
 verify(spyMacawWorkBench).editVariables();
 macawVariableBrowser.show();
 verify(spyMacawWorkBench).editVariables();
 }

 @Test
 public void testExportVariableDataMethod() {
 MacawWorkBench spyMacawWorkBench = spy(macawWorkBench);
 doCallRealMethod().when(spyMacawWorkBench).exportVariableData();
 ExportVariablesToMacawXML exportToDSSFormat = new ExportVariablesToMacawXML(sessionProperties);
 verify(spyMacawWorkBench).exportVariableData();
 exportToDSSFormat.export();
 verify(spyMacawWorkBench).exportVariableData();
 }

 @Test
 public void testExitMethod() {
 MacawWorkBench spyMacawWorkBench = spy(macawWorkBench);
 doCallRealMethod().when(spyMacawWorkBench).exit();
 System.exit(0);
 verify(spyMacawWorkBench).exit();
 }

 @Test
 public void testActionPerformedMethodWithEditConstantsButton() {
 ActionEvent editConstantsEvent = new ActionEvent(macawWorkBench.editConstants, ActionEvent.ACTION_PERFORMED, "editConstantsAction");
 macawWorkBench.actionPerformed(editConstantsEvent);
 macawWorkBench.editConstants();
 }

 @Test
 public void testActionPerformedMethodWithEditVariablesButton() {
 ActionEvent editVariablesEvent = new ActionEvent(macawWorkBench.editVariables, ActionEvent.ACTION_PERFORMED, "editVariablesAction");
 macawWorkBench.actionPerformed(editVariablesEvent);
 macawWorkBench.editVariables();
 }

 @Test
 public void testActionPerformedMethodWithExportVariableDataButton() {
 ActionEvent exportVariableDataEvent = new ActionEvent(macawWorkBench.exportVariableData, ActionEvent.ACTION_PERFORMED, "exportVariableDataAction");
 macawWorkBench.actionPerformed(exportVariableDataEvent);
 macawWorkBench.exportVariableData();
 }

 @Test
 public void testActionPerformedMethodWithExitButton() {
 ActionEvent exitEvent = new ActionEvent(macawWorkBench.exit, ActionEvent.ACTION_PERFORMED, "exitAction");
 macawWorkBench.actionPerformed(exitEvent);
 macawWorkBench.exit();
 }
}