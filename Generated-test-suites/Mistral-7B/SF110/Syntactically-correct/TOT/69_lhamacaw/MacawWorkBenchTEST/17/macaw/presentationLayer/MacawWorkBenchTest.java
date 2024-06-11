package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.VariableConstants;
import macaw.io.MacawVariableBrowser;
import macaw.io.export.ExportVariablesToMacawXML;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.production.ProductionCurationService;
import macaw.presentationLayer.MacawWorkBench;
import macaw.presentationLayer.SessionProperties;
import macaw.system.UserInterfaceFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private SessionProperties sessionProperties;
 private UserInterfaceFactory userInterfaceFactory;
 private DemonstrationCurationService demoDatabase;
 private ProductionCurationService productionDatabase;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = new UserInterfaceFactory(sessionProperties);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 if (sessionProperties.isUseDemo()) {
 demoDatabase = new DemonstrationCurationService(false);
 sessionProperties.setProperty("database", demoDatabase);
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
 public void testCreateButtonPanel() {
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertNotNull(buttonPanel);
 }

 @Test
 public void testShowDialog() {
 macawWorkBench.show();
 assertNotNull(macawWorkBench.dialog);
 macawWorkBench.dialog.isDisplayable();
 }

 @Test
 public void testEditConstants() {
 macawWorkBench.editConstants();
 assertTrue(sessionProperties.getProperty("variableConstantsEditor") instanceof VariableConstantsEditor);
 }

 @Test
 public void testEditVariables() {
 macawWorkBench.editVariables();
 assertTrue(sessionProperties.getProperty("macawVariableBrowser") instanceof MacawVariableBrowser);
 }

 @Test
 public void testExportVariableData() {
 macawWorkBench.exportVariableData();
 assertTrue(sessionProperties.getProperty("exportToDSSFormat") instanceof ExportVariablesToMacawXML);
 }

 @Test(expected = IllegalStateException.class)
 public void testThreeLoginAttemptsFailure() throws Exception {
 macawWorkBench = new MacawWorkBench(sessionProperties);
 LoginDialog loginDialog = new LoginDialog(sessionProperties, 0);
 loginDialog.show();
 loginDialog.isLoginSuccessful();
 macawWorkBench.show();
 loginDialog = new LoginDialog(sessionProperties, 1);
 loginDialog.show();
 loginDialog.isLoginSuccessful();
 macawWorkBench.show();
 loginDialog = new LoginDialog(sessionProperties, 2);
 loginDialog.show();
 macawWorkBench.exit();
 }

 @Test(expected = IllegalStateException.class)
 public void testLoginDialogCancellation() throws Exception {
 macawWorkBench = new MacawWorkBench(sessionProperties);
 LoginDialog loginDialog = new LoginDialog(sessionProperties, 0);
 loginDialog.show();
 loginDialog.isCancelled();
 macawWorkBench.exit();
 }

}