package macaw.presentationLayer;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.production.ProductionCurationService;
import macaw.persistenceLayer.production.ProductionRetrievalService;
import macaw.persistenceLayer.production.ProductionVariableBrowserModel;
import macaw.presentationLayer.UserInterfaceFactory;
import org.junit.Test;

public class MacawWorkBenchTest {

 SessionProperties sessionProperties = new SessionProperties();
 UserInterfaceFactory userInterfaceFactory = new UserInterfaceFactory();
 MacawWorkBench macawWorkBench;

 @Test
 public void testMacawWorkBenchCreation() {
 macawWorkBench = new MacawWorkBench(sessionProperties);
 assertNotNull(macawWorkBench);
 }

 @Test
 public void testMainMethod() {
 try {
 MacawWorkBench.main(new String[0]);
 } catch (Exception e) {
 fail("Unexpected exception in main method: " + e.getMessage());
 }
 }

 @Test
 public void testCreateButtonPanel() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertNotNull(buttonPanel);
 }

 @Test
 public void testShowDialog() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.show();
 assertTrue(macawWorkBench.dialog.isVisible());
 }

 @Test
 public void testEditConstantsButton() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 ActionEvent editConstantsButtonEvent = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "editConstants");
 macawWorkBench.actionPerformed(editConstantsButtonEvent);
 assertTrue(macawWorkBench.sessionProperties.getProperty("variableConstantsEditor") instanceof VariableConstantsEditor);
 }

 @Test
 public void testEditVariablesButton() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 ActionEvent editVariablesButtonEvent = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "editVariables");
 macawWorkBench.actionPerformed(editVariablesButtonEvent);
 assertTrue(macawWorkBench.sessionProperties.getProperty("macawVariableBrowser") instanceof MacawVariableBrowser);
 }

 @Test
 public void testExportVariableDataButton() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 if (sessionProperties.getProperty("retrievalService") instanceof ProductionRetrievalService) {
 ProductionRetrievalService retrievalService = (ProductionRetrievalService) sessionProperties.getProperty("retrievalService");
 ProductionVariableBrowserModel variableBrowserModel = new ProductionVariableBrowserModel(sessionProperties, retrievalService);
 macawWorkBench.sessionProperties.setProperty("variableBrowserModel", variableBrowserModel);
 ActionEvent exportVariableDataButtonEvent = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "exportVariableData");
 macawWorkBench.actionPerformed(exportVariableDataButtonEvent);
 assertTrue(macawWorkBench.sessionProperties.getProperty("exportToDSSFormat") instanceof ExportVariablesToMacawXML);
 }
 }

 @Test
 public void testExitButton() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 ActionEvent exitButtonEvent = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "exit");
 macawWorkBench.actionPerformed(exitButtonEvent);
 assertTrue(System.exit(0) == 0);
 }

 @Test
 public void testDatabaseSelection() {
 SessionProperties sessionProperties = new SessionProperties();
 sessionProperties.setProperty("startupOptions", new StartupOptions());
 StartupOptions startupOptions = (StartupOptions) sessionProperties.getProperty("startupOptions");
 startupOptions.setUseDemo(true);
 sessionProperties.setProperty("sessionProperties", sessionProperties);
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 assertTrue(macawWorkBench.sessionProperties.getProperty("database") instanceof DemonstrationCurationService);
 sessionProperties.setProperty("startupOptions", new StartupOptions());
 startupOptions.setUseDemo(false);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 assertTrue(macawWorkBench.sessionProperties.getProperty("database") instanceof ProductionCurationService);
 }

 @Test
 public void testLoginDialog() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 MacawWorkBench macawWorkBenchInstance = null;
 for (int i = 0; i < 3; i++) {
 LoginDialog loginDialog = new LoginDialog(sessionProperties, i);
 loginDialog.show();
 if (loginDialog.isLoginSuccessful()) {
 macawWorkBenchInstance = new MacawWorkBench(sessionProperties);
 break;
 }
 }
 assertNotNull(macawWorkBenchInstance);
 }

}