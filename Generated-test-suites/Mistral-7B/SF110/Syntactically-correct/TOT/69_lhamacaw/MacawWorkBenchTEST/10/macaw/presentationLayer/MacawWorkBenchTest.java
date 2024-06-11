import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.businessLayer.VariableConstantsEditor;
import macaw.businessLayer.MacawVariableBrowser;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.production.ProductionCurationService;
import macaw.persistenceLayer.SessionProperties;
import macaw.system.UserInterfaceFactory;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private SessionProperties sessionProperties;
 private UserInterfaceFactory userInterfaceFactory;
 private DemonstrationCurationService demonstrationCurationService;
 private ProductionCurationService productionCurationService;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = new SwingUserInterfaceFactory();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 if (sessionProperties.useDemo()) {
 demonstrationCurationService = (DemonstrationCurationService) sessionProperties.getProperty("database");
 } else {
 productionCurationService = (ProductionCurationService) sessionProperties.getProperty("database");
 }
 macawWorkBench.setSessionProperties(sessionProperties);
 macawWorkBench.setUserInterfaceFactory(userInterfaceFactory);
 }

 @Test
 public void testShowDialog() {
 macawWorkBench.show();
 assertTrue("Dialog is not visible", macawWorkBench.dialog.isVisible());
 }

 @Test
 public void testEditConstantsButton() {
 macawWorkBench.editConstants_buttonClicked();
 assertTrue("VariableConstantsEditor dialog is not visible", new VariableConstantsEditor(sessionProperties, true).dialog.isVisible());
 }

 @Test
 public void testExportVariableDataButton() {
 macawWorkBench.exportVariableData_buttonClicked();
 assertTrue("ExportVariablesToMacawXML dialog is not visible", new ExportVariablesToMacawXML(sessionProperties).dialog.isVisible());
 }

 @Test
 public void testEditVariablesButton() {
 macawWorkBench.editVariables_buttonClicked();
 assertTrue("MacawVariableBrowser dialog is not visible", new MacawVariableBrowser(sessionProperties).dialog.isVisible());
 }

 @Test
 public void testExitButton() {
 macawWorkBench.exit_buttonClicked();
 assertFalse("Dialog is still visible", macawWorkBench.dialog.isVisible());
 }

 @Test(expected = IllegalStateException.class)
 public void testShowDialogTwice() {
 macawWorkBench.show();
 macawWorkBench.show();
 fail();
 }

 @Test
 public void testEditConstantsWithEmptySessionProperties() {
 sessionProperties = null;
 macawWorkBench = new MacawWorkBench(sessionProperties);
 try {
 macawWorkBench.editConstants();
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testCreateButtonPanelWithoutUserInterfaceFactory() {
 macawWorkBench.sessionProperties = null;
 MacawWorkBench macawWorkBench = new MacawWorkBench(macawWorkBench.sessionProperties);
 try {
 macawWorkBench.createButtonPanel(null);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDatabaseCreationWithNullSessionProperties() {
 try {
 if (sessionProperties.useDemo()) {
 demonstrationCurationService = new DemonstrationCurationService(null);
 } else {
 productionCurationService = new ProductionCurationService(null);
 }
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDatabaseCreationWithEmptySessionProperties() {
 try {
 if (sessionProperties.useDemo()) {
 demonstrationCurationService = new DemonstrationCurationService(new SessionProperties());
 } else {
 productionCurationService = new ProductionCurationService(new SessionProperties());
 }
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testLoginDialogWithInvalidCredentials() {
 sessionProperties.setProperty("currentUser", new User("jsmith", "invalidPassword"));
 LoginDialog loginDialog = new LoginDialog(sessionProperties, 0);
 loginDialog.show();
 assertFalse("LoginDialog is not cancelled", loginDialog.isCancelled());
 }

 @Test
 public void testLoginDialogWithValidCredentials() {
 sessionProperties.setProperty("currentUser", new User("jsmith", "cool"));
 LoginDialog loginDialog = new LoginDialog(sessionProperties, 0);
 loginDialog.show();
 assertTrue("LoginDialog is cancelled", loginDialog.isCancelled());
 }

 @Test
 public void testLoginDialogWithSuccessfulCredentials() {
 sessionProperties.setProperty("currentUser", new User("jsmith", "cool"));
 loginDialog.setLoginSuccessful(true);
 LoginDialog loginDialog = new LoginDialog(sessionProperties, 0);
 loginDialog.show();
 assertTrue("Dialog is not visible after successful login", macawWorkBench.dialog.isVisible());
 }

 @Test
 public void testLoginDialogWithMaximumNumberOfAttempts() {
 sessionProperties.setProperty("currentUser", new User("jsmith", "cool"));
 for (int i = 0; i < 3; i++) {
 LoginDialog loginDialog = new LoginDialog(sessionProperties, i);
 loginDialog.show();
 assertFalse("Dialog is not cancelled after maximum number of attempts", loginDialog.isCancelled());
 }
 System.exit(0);
 }
}