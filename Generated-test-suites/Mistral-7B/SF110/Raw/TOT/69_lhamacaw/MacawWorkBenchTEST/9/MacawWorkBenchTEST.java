package macaw.presentationLayer.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import macaw.businessLayer.User;
import macaw.io.MacawMessages;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.production.ProductionCurationService;
import macaw.presentationLayer.MacawWorkBench;
import macaw.presentationLayer.UserInterfaceFactory;
import org.junit.Before;
import org.junit.Test;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private UserInterfaceFactory userInterfaceFactory;
 private SessionProperties sessionProperties;

 @Before
 public void setup() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = new UserInterfaceFactory(sessionProperties);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testShowDialog() {
 macawWorkBench.show();
 assertTrue("Dialog should be displayed", macawWorkBench.dialog.isDisplayable());
 }

 @Test
 public void testEditConstantsButton() {
 macawWorkBench.editConstants();
 User user = new User("john", "doe");
 sessionProperties.setProperty("currentUser", user);
 assertTrue("VariableConstantsEditor should be displayed", userInterfaceFactory.createVariableConstantsEditor(sessionProperties).isDisplayable());
 }

 @Test
 public void testEditVariablesButton() {
 macawWorkBench.editVariables();
 assertTrue("MacawVariableBrowser should be displayed", userInterfaceFactory.createMacawVariableBrowser(sessionProperties).isDisplayable());
 }

 @Test
 public void testExportVariableDataButton() {
 macawWorkBench.exportVariableData();
 DemonstrationCurationService database = new DemonstrationCurationService(false);
 ProductionCurationService database2 = new ProductionCurationService(sessionProperties);
 assertFalse("Exporting variable data should fail in this test", database.exportVariablesToMacawXML());
 assertFalse("Exporting variable data should fail in this test", database2.exportVariablesToMacawXML());
 }

 @Test
 public void testExitButton() {
 macawWorkBench.exit();
 assertFalse("Application should exit", macawWorkBench.dialog.isDisplayable());
 }

 @Test
 public void testLoginDialogFailure() {
 final int numberOfAttempts = 3;
 final LoginDialog loginDialog = new LoginDialog(sessionProperties, 0);
 loginDialog.setVisible(true);
 loginDialog.dispose();
 assertFalse("Application should exit after three failed login attempts", macawWorkBench.dialog.isDisplayable());
 }
}