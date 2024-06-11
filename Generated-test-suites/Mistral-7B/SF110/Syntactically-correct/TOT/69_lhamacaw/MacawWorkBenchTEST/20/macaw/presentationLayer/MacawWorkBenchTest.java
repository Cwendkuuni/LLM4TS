package macaw.presentationLayer.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.event.ActionEvent;

import macaw.businessLayer.VariableConstantsEditor;
import macaw.businessLayer.VariableConstantsEditorFactory;
import macaw.io.ExportVariablesToMacawXML;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.production.ProductionCurationService;
import macaw.presentationLayer.MacawWorkBench;
import macaw.presentationLayer.MacawWorkBenchActions;
import macaw.presentationLayer.UserInterfaceFactory;
import macaw.system.User;
import macaw.system.UserInterfaceFactoryImpl;

import org.junit.Before;
import org.junit.Test;

public class MacawWorkBenchTest {

 private MacawWorkBench workBench;
 private UserInterfaceFactory uiFactory;
 private User user;

 @Before
 public void setUp() {
 uiFactory = new UserInterfaceFactoryImpl();
 user = new User("testUser", "testPassword");
 workBench = new MacawWorkBench(new SessionProperties());
 workBench.setUserInterfaceFactory(uiFactory);
 workBench.setSessionProperties(new SessionProperties());
 workBench.setSessionProperties("currentUser", user);
 }

 @Test
 public void testShowDialog() {
 workBench.show();
 assertTrue(workBench.dialog.isVisible());
 }

 @Test
 public void testCreateButtonPanel() {
 final JPanel buttonPanel = workBench.createButtonPanel(uiFactory);
 assertFalse(buttonPanel.isEmpty());
 }

 @Test
 public void testEditConstantsButton() {
 workBench.editConstants();
 assertTrue(VariableConstantsEditor.class.isInstance(workBench.sessionProperties.getProperty("variableConstantsEditor")));
 }

 @Test
 public void testEditVariablesButton() {
 workBench.editVariables();
 assertTrue(MacawVariableBrowser.class.isInstance(workBench.sessionProperties.getProperty("macawVariableBrowser")));
 }

 @Test
 public void testExportVariableDataButton() {
 workBench.exportVariableData();
 assertTrue(ExportVariablesToMacawXML.class.isInstance(workBench.sessionProperties.getProperty("exportToDSSFormat")));
 }

 @Test
 public void testExitButton() {
 workBench.exit();
 assertTrue(System.exit.getClass().isAssignableFrom(workBench.getClass()));
 }

 @Test
 public void testFailedLogin() {
 workBench.sessionProperties.setProperty("startupOptions", new StartupOptions());
 workBench.sessionProperties.setProperty("database", new ProductionCurationService(workBench.sessionProperties));
 workBench.sessionProperties.setProperty("retrievalService", new ProductionRetrievalService(workBench.sessionProperties));
 workBench.sessionProperties.setProperty("currentUser", null);
 LoginDialog dialog = new LoginDialog(workBench.sessionProperties, 0);
 dialog.show();
 assertFalse(workBench.dialog.isVisible());
 }

 @Test(expected = Exception.class)
 public void testFailedDatabaseCreation() {
 workBench.sessionProperties.setProperty("startupOptions", new StartupOptions());
 workBench.sessionProperties.setProperty("database", new UnsupportedDatabase());
 workBench.sessionProperties.setProperty("retrievalService", new ProductionRetrievalService(workBench.sessionProperties));
 workBench.sessionProperties.setProperty("currentUser", user);
 workBench.show();
 }
}