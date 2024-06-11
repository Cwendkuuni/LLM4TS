package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.JOptionPane;

import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.presentationLayer.UserInterfaceFactory;
import macaw.system.MacawMessages;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class MacawWorkBenchTest {

 private MacawWorkBench workBench;
 private UserInterfaceFactory mockUIFactory;
 private SessionProperties sessionProperties;

 @Before
 public void setUp() {
 mockUIFactory = mock(UserInterfaceFactory.class);
 sessionProperties = new SessionProperties();
 workBench = new MacawWorkBench(sessionProperties);
 workBench.setUserInterfaceFactory(mockUIFactory);
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testMain() {
 // Test case for main method
 try {
 SessionProperties sessionProperties = new SessionProperties();
 StartupOptions startupOptions = new StartupOptions();
 startupOptions.processCommandLineArguments(new String[0]);
 User currentUser = new User("jsmith", "cool");
 sessionProperties.setProperty("currentUser", currentUser);
 ProductionCurationService database2 = mock(ProductionCurationService.class);
 ProductionRetrievalService retrievalService = mock(ProductionRetrievalService.class);
 sessionProperties.setProperty("database", database2);
 sessionProperties.setProperty("retrievalService", retrievalService);
 LoginDialog loginDialog = mock(LoginDialog.class);
 when(loginDialog.isLoginSuccessful()).thenReturn(true);
 when(loginDialog.isCancelled()).thenReturn(false);
 MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
 workBench.show();
 verify(loginDialog).show();
 } catch (Exception e) {
 fail("Exception should not be thrown in main method.");
 }
 }

 @Test
 public void testCreateButtonPanel() {
 // Test case for createButtonPanel method
 UserInterfaceFactory mockUserInterfaceFactory = mock(UserInterfaceFactory.class);
 MacawWorkBench workBench = new MacawWorkBench(null);
 JPanel buttonPanel = workBench.createButtonPanel(mockUserInterfaceFactory);
 assertNotNull(buttonPanel);
 assertEquals(5, buttonPanel.getComponentCount());
 }

 @Test
 public void testEditConstants() {
 // Test case for editConstants method
 VariableConstantsEditor mockVariableConstantsEditor = mock(VariableConstantsEditor.class);
 workBench.editConstants = mockVariableConstantsEditor;
 workBench.editConstants();
 verify(mockVariableConstantsEditor).show();
 }

 @Test
 public void testEditVariables() {
 // Test case for editVariables method
 MacawVariableBrowser mockMacawVariableBrowser = mock(MacawVariableBrowser.class);
 workBench.editVariables = mockMacawVariableBrowser;
 workBench.editVariables();
 verify(mockMacawVariableBrowser).show();
 }

 @Test
 public void testExportVariableData() {
 // Test case for exportVariableData method
 ExportVariablesToMacawXML mockExportToDSSFormat = mock(ExportVariablesToMacawXML.class);
 workBench.exportVariableData = mockExportToDSSFormat;
 workBench.exportVariableData.export();
 verify(mockExportToDSSFormat).export();
 }

 @Test
 public void testExit() {
 // Test case for exit method
 workBench.exit();
 assertEquals(0, System.exit(0));
 }

 @Test
 public void testActionPerformedWithEditConstantsButton() {
 // Test case for actionPerformed method with editConstants button
 ActionEvent mockActionEvent = mock(ActionEvent.class);
 when(mockActionEvent.getSource()).thenReturn(workBench.editConstants);
 workBench.actionPerformed(mockActionEvent);
 verify(workBench.editConstants).editConstants();
 }

 @Test
 public void testActionPerformedWithEditVariablesButton() {
 // Test case for actionPerformed method with editVariables button
 ActionEvent mockActionEvent = mock(ActionEvent.class);
 when(mockActionEvent.getSource()).thenReturn(workBench.editVariables);
 workBench.actionPerformed(mockActionEvent);
 verify(workBench.editVariables).show();
 }

 @Test
 public void testActionPerformedWithExportVariableDataButton() {
 // Test case for actionPerformed method with exportVariableData button
 ActionEvent mockActionEvent = mock(ActionEvent.class);
 when(mockActionEvent.getSource()).thenReturn(workBench.exportVariableData);
 workBench.actionPerformed(mockActionEvent);
 verify(workBench.exportVariableData).export();
 }

 @Test
 public void testActionPerformedWithExitButton() {
 // Test case for actionPerformed method with exit button
 ActionEvent mockActionEvent = mock(ActionEvent.class);
 when(mockActionEvent.getSource()).thenReturn(workBench.exit);
 workBench.actionPerformed(mockActionEvent);
 assertEquals(0, System.exit(0));
 }

 @Test
 public void testLoginDialogIsCancelled() {
 // Test case for loginDialog isCancelled return true
 UserInterfaceFactory mockUIFactory = mock(UserInterfaceFactory.class);
 SessionProperties sessionProperties = new SessionProperties();
 LoginDialog mockLoginDialog = mock(LoginDialog.class);
 when(mockLoginDialog.isCancelled()).thenReturn(true);
 MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
 workBench.setUserInterfaceFactory(mockUIFactory);
 workBench.setLoginDialog(mockLoginDialog);
 try {
 workBench.show();
 fail("Application should not be displayed if login dialog is cancelled.");
 } catch (Exception e) {
 // Expected
 }
 }

 @Test
 public void testLoginDialogLoginSuccessful() {
 // Test case for loginDialog loginSuccessful return true
 UserInterfaceFactory mockUIFactory = mock(UserInterfaceFactory.class);
 SessionProperties sessionProperties = new SessionProperties();
 LoginDialog mockLoginDialog = mock(LoginDialog.class);
 when(mockLoginDialog.isLoginSuccessful()).thenReturn(true);
 MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
 workBench.setUserInterfaceFactory(mockUIFactory);
 workBench.setLoginDialog(mockLoginDialog);
 try {
 workBench.show();
 // Application should be displayed if login dialog is successful
 } catch (Exception e) {
 fail("Application should be displayed if login dialog is successful.");
 }
 }

 @Test
 public void testLoginDialogLoginFailed() {
 // Test case for loginDialog loginSuccessful return false
 UserInterfaceFactory mockUIFactory = mock(UserInterfaceFactory.class);
 SessionProperties sessionProperties = new SessionProperties();
 LoginDialog mockLoginDialog = mock(LoginDialog.class);
 when(mockLoginDialog.isLoginSuccessful()).thenReturn(false);
 MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
 workBench.setUserInterfaceFactory(mockUIFactory);
 workBench.setLoginDialog(mockLoginDialog);
 try {
 workBench.show();
 fail("Application should not be displayed if login dialog fails.");
 } catch (Exception e) {
 // Expected
 }
 }

 @Test
 public void testCreateDialogWithInstructionsArea() {
 // Test case for createDialog method with instructionsArea
 UserInterfaceFactory mockUIFactory = mock(UserInterfaceFactory.class);
 MacawWorkBench workBench = new MacawWorkBench(null);
 JDialog dialog = workBench.createDialog();
 JTextArea instructionsArea = (JTextArea) dialog.getContentPane().getComponent(0);
 assertNotNull(instructionsArea);
 }

 @Test
 public void testCreateDialogWithoutInstructionsArea() {
 // Test case for createDialog method without instructionsArea
 UserInterfaceFactory mockUIFactory = mock(UserInterfaceFactory.class);
 MacawWorkBench workBench = new MacawWorkBench(null);
 JDialog dialog = workBench.createDialog();
 assertFalse(dialog.getContentPane().getComponent(0) instanceof JTextArea);
 }
}