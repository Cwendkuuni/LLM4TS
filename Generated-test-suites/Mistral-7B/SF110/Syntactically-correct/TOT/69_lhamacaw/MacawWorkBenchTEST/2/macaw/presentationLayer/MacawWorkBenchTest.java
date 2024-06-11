package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;

import macaw.businessLayer.VariableConstants;
import macaw.businessLayer.VariableConstantsEditor;
import macaw.io.ExportVariablesToMacawXML;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.production.ProductionCurationService;
import macaw.persistenceLayer.production.ProductionRetrievalService;
import macaw.presentationLayer.MacawWorkBench;
import macaw.presentationLayer.UserInterfaceFactory;
import macaw.system.SessionProperties;
import macaw.system.StartupOptions;

public class MacawWorkBenchTest {

 private MacawWorkBench workBench;
 private UserInterfaceFactory uiFactory;
 private SessionProperties sessionProperties;
 private ProductionCurationService database;
 private ProductionRetrievalService retrievalService;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 uiFactory = mock(UserInterfaceFactory.class);
 workBench = new MacawWorkBench(sessionProperties);
 workBench.setUserInterfaceFactory(uiFactory);
 database = new ProductionCurationService(sessionProperties);
 retrievalService = new ProductionRetrievalService(sessionProperties);
 sessionProperties.setProperty("database", database);
 sessionProperties.setProperty("retrievalService", retrievalService);
 }

 @Test
 public void testMacawWorkbenchInitialization() {
 MacawWorkBench expectedWorkbench = new MacawWorkBench(sessionProperties);
 verify(uiFactory, times(1)).createButton(anyString());
 verify(uiFactory, times(1)).createDialog();
 verify(uiFactory, times(1)).createImmutableTextArea(anyInt(), anyInt());
 verify(uiFactory, times(1)).createPanel();
 assertEquals(expectedWorkbench, workBench);
 }

 @Test
 public void testEditConstantsButtonClick() {
 final VariableConstantsEditor variableConstantsEditor = mock(VariableConstantsEditor.class);
 when(uiFactory.createDialog()).thenReturn(variableConstantsEditor);
 workBench.editConstants();
 verify(uiFactory, times(1)).createDialog();
 verify(variableConstantsEditor, times(1)).show();
 }

 @Test
 public void testEditVariablesButtonClick() {
 final MacawVariableBrowser variableBrowser = mock(MacawVariableBrowser.class);
 when(uiFactory.createDialog()).thenReturn(variableBrowser);
 workBench.editVariables();
 verify(uiFactory, times(1)).createDialog();
 verify(variableBrowser, times(1)).show();
 }

 @Test
 public void testExportVariablesButtonClick() {
 final ExportVariablesToMacawXML exporter = mock(ExportVariablesToMacawXML.class);
 when(uiFactory.createButton(anyString())).thenReturn(exporter);
 workBench.exportVariableData();
 verify(exporter, times(1)).export();
 }

 @Test
 public void testExitButtonClick() {
 System.setProperty("macaw.presentationLayer.MacawWorkBench.exit", "true");
 workBench.exit();
 assertTrue(System.getProperty("macaw.presentationLayer.MacawWorkBench.exit").equals("true"));
 }

 @Test(expected = NullPointerException.class)
 public void testExceptionThrownOnInvalidDatabaseInitialization() {
 sessionProperties.setProperty("database", null);
 workBench = new MacawWorkBench(sessionProperties);
 workBench.show();
 fail("Exception not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExceptionThrownOnInvalidRetrievalServiceInitialization() {
 sessionProperties.setProperty("retrievalService", null);
 workBench = new MacawWorkBench(sessionProperties);
 workBench.show();
 fail("Exception not thrown");
 }

 @After
 public void tearDown() {
 workBench = null;
 sessionProperties = null;
 uiFactory = null;
 database = null;
 retrievalService = null;
 }
}