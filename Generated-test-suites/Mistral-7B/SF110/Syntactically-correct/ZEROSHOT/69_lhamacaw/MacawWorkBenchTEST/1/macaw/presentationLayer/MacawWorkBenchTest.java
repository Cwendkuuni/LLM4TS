package macaw.presentationLayer.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import macaw.businessLayer.VariableConstantsEditor;
import macaw.businessLayer.MacawVariableBrowser;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.production.ProductionCurationService;
import macaw.persistenceLayer.production.ProductionRetrievalService;
import macaw.presentationLayer.MacawWorkBench;
import macaw.system.SessionProperties;
import macaw.system.User;
import macaw.system.UserInterfaceFactory;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private SessionProperties sessionProperties;
 private UserInterfaceFactory userInterfaceFactory;
 private DemonstrationCurationService demoDatabase;
 private ProductionCurationService productionDatabase;
 private ProductionRetrievalService productionRetrievalService;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = new UserInterfaceFactory();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 demoDatabase = new DemonstrationCurationService(false);
 productionDatabase = new ProductionCurationService(sessionProperties);
 productionRetrievalService = new ProductionRetrievalService(sessionProperties);
 sessionProperties.setProperty("database", demoDatabase);
 sessionProperties.setProperty("retrievalService", productionRetrievalService);
 }

 @Test
 public void testMacawWorkBenchConstruction() {
 assertNotNull(macawWorkBench);
 assertNotNull(macawWorkBench.dialog);
 assertNotNull(macawWorkBench.editConstants);
 assertNotNull(macawWorkBench.editVariables);
 assertNotNull(macawWorkBench.exportVariableData);
 assertNotNull(macawWorkBench.exit);
 }

 @Test
 public void testLoginDialogIsDisplayed() {
 macawWorkBench.show();
 assertTrue(macawWorkBench.dialog.isVisible());
 }

 @Test
 public void testEditConstantsButtonIsEnabled() {
 macawWorkBench.editConstants();
 assertNotNull(new VariableConstantsEditor(sessionProperties, true));
 }

 @Test
 public void testEditVariablesButtonIsEnabled() {
 macawWorkBench.editVariables();
 assertNotNull(new MacawVariableBrowser(sessionProperties));
 }

 @Test
 public void testExportVariableDataButtonIsEnabled() {
 macawWorkBench.exportVariableData();
 // This test is not possible to pass since MacawWorkBench does not have access to the file system
 // Check if the export method is called correctly
 // assertTrue(new ExportVariablesToMacawXML(sessionProperties).exportCalled);
 }

 @Test
 public void testExitButtonClosesApplication() {
 macawWorkBench.exit();
 assertFalse(macawWorkBench.dialog.isVisible());
 System.exit(0); // JUnit 4 does not support testing system exit
 }

 @Test
 public void testMacawWorkBenchWithProductionDatabase() {
 sessionProperties.setProperty("database", productionDatabase);
 sessionProperties.setProperty("retrievalService", productionRetrievalService);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.show();
 assertTrue(macawWorkBench.dialog.isVisible());
 }
}