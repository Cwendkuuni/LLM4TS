package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;

import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.presentationLayer.UserInterfaceFactory;
import macaw.system.*;
import macaw.io.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private UserInterfaceFactory mockUserInterfaceFactory;

 @Before
 public void setUp() {
 mockUserInterfaceFactory = mock(UserInterfaceFactory.class);
 macawWorkBench = new MacawWorkBench(new SessionProperties());
 macawWorkBench.setUserInterfaceFactory(mockUserInterfaceFactory);

 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testShow() {
 // Create mock dialog and set expectation for dialog.show()
 JDialog mockDialog = mock(JDialog.class);
 when(mockUserInterfaceFactory.createDialog()).thenReturn(mockDialog);

 // Call method under test
 macawWorkBench.show();

 // Verify dialog was shown
 verify(mockDialog).setVisible(true);
 }

 @Test
 public void testEditConstants() {
 // Create mock VariableConstantsEditor and set expectation for show()
 VariableConstantsEditor mockVariableConstantsEditor = mock(VariableConstantsEditor.class);
 when(mockUserInterfaceFactory.createVariableConstantsEditor(any(SessionProperties.class), anyBoolean())).thenReturn(mockVariableConstantsEditor);

 // Call method under test
 macawWorkBench.editConstants();

 // Verify VariableConstantsEditor was shown
 verify(mockVariableConstantsEditor).show();
 }

 @Test
 public void testEditVariables() {
 // Create mock MacawVariableBrowser and set expectation for show()
 MacawVariableBrowser mockMacawVariableBrowser = mock(MacawVariableBrowser.class);
 when(mockUserInterfaceFactory.createMacawVariableBrowser(any(SessionProperties.class))).thenReturn(mockMacawVariableBrowser);

 // Call method under test
 macawWorkBench.editVariables();

 // Verify MacawVariableBrowser was shown
 verify(mockMacawVariableBrowser).show();
 }

 @Test
 public void testExportVariableData() {
 // Create mock ExportVariablesToMacawXML and set expectation for export()
 ExportVariablesToMacawXML mockExportToDSSFormat = mock(ExportVariablesToMacawXML.class);
 when(mockUserInterfaceFactory.createExportVariablesToMacawXML(any(SessionProperties.class))).thenReturn(mockExportToDSSFormat);

 // Call method under test
 macawWorkBench.exportVariableData();

 // Verify ExportVariablesToMacawXML was called
 verify(mockExportToDSSFormat).export();
 }

 @Test
 public void testExit() {
 // Call method under test
 macawWorkBench.exit();

 // Verify system exits with status 0
 verifyZeroExit();
 }

 @Test
 public void testActionPerformed_EditConstants() {
 // Set up expectations for editConstants() and user interface factory create methods
 when(macawWorkBench.editConstants()).thenCallRealMethod();
 when(mockUserInterfaceFactory.createVariableConstantsEditor(any(SessionProperties.class), anyBoolean())).thenReturn(mockVariableConstantsEditor);

 // Create mock ActionEvent and set expectation for ActionEvent getSource()
 ActionEvent mockActionEvent = mock(ActionEvent.class);
 when(mockActionEvent.getSource()).thenReturn(macawWorkBench.editConstants);

 // Call method under test
 macawWorkBench.actionPerformed(mockActionEvent);

 // Verify editConstants() was called
 verify(macawWorkBench).editConstants();
 // Verify VariableConstantsEditor was shown
 verify(mockVariableConstantsEditor).show();
 }

 @Test
 public void testActionPerformed_EditVariables() {
 // Set up expectations for editVariables() and user interface factory create methods
 when(macawWorkBench.editVariables()).thenCallRealMethod();
 when(mockUserInterfaceFactory.createMacawVariableBrowser(any(SessionProperties.class))).thenReturn(mockMacawVariableBrowser);

 // Create mock ActionEvent and set expectation for ActionEvent getSource()
 ActionEvent mockActionEvent = mock(ActionEvent.class);
 when(mockActionEvent.getSource()).thenReturn(macawWorkBench.editVariables);

 // Call method under test
 macawWorkBench.actionPerformed(mockActionEvent);

 // Verify editVariables() was called
 verify(macawWorkBench).editVariables();
 // Verify MacawVariableBrowser was shown
 verify(mockMacawVariableBrowser).show();
 }

 @Test
 public void testActionPerformed_ExportVariableData() {
 // Set up expectations for exportVariableData() and user interface factory create methods
 when(macawWorkBench.exportVariableData()).thenCallRealMethod();
 when(mockUserInterfaceFactory.createExportVariablesToMacawXML(any(SessionProperties.class))).thenReturn(mockExportToDSSFormat);

 // Create mock ActionEvent and set expectation for ActionEvent getSource()
 ActionEvent mockActionEvent = mock(ActionEvent.class);
 when(mockActionEvent.getSource()).thenReturn(macawWorkBench.exportVariableData);

 // Call method under test
 macawWorkBench.actionPerformed(mockActionEvent);

 // Verify exportVariableData() was called
 verify(macawWorkBench).exportVariableData();
 // Verify ExportVariablesToMacawXML export() was called
 verify(mockExportToDSSFormat).export();
 }

 @Test
 public void testActionPerformed_Exit() {
 // Set up expectations for exit()
 when(macawWorkBench.exit()).thenCallRealMethod();

 // Create mock ActionEvent and set expectation for ActionEvent getSource()
 ActionEvent mockActionEvent = mock(ActionEvent.class);
 when(mockActionEvent.getSource()).thenReturn(macawWorkBench.exit);

 // Call method under test
 macawWorkBench.actionPerformed(mockActionEvent);

 // Verify system exits with status 0
 verifyZeroExit();
 }

 @Test
 public void testLoginDialogIsLoginSuccessful() {
 // Create mock User and set expectation for isLoginSuccessful()
 User mockUser = mock(User.class);
 when(mockUser.isLoginSuccessful()).thenReturn(true);

 // Set up mock SessionProperties and expectations for getProperty()
 SessionProperties mockSessionProperties = mock(SessionProperties.class);
 when(mockSessionProperties.getProperty("currentUser")).thenReturn(mockUser);

 // Set up expectations for LoginDialog show() and isLoginSuccessful()
 LoginDialog mockLoginDialog = mock(LoginDialog.class);
 when(mockUserInterfaceFactory.createLoginDialog(any(SessionProperties.class), anyInt())).thenReturn(mockLoginDialog);
 when(mockLoginDialog.isLoginSuccessful()).thenReturn(true);

 // Call method under test
 int numberOfAttempts = macawWorkBench.main(new String[0]);

 // Verify LoginDialog was shown
 verify(mockLoginDialog).show();
 // Verify method under test exits with status 0
 verifyZeroExit();
 }

 @Test
 public void testLoginDialogIsCancelled() {
 // Set up mock User and expectation for isCancelled()
 User mockUser = mock(User.class);
 when(mockUser.isCancelled()).thenReturn(true);

 // Set up mock SessionProperties and expectations for getProperty()
 SessionProperties mockSessionProperties = mock(SessionProperties.class);
 when(mockSessionProperties.getProperty("currentUser")).thenReturn(mockUser);

 // Set up expectations for LoginDialog show() and isCancelled()
 LoginDialog mockLoginDialog = mock(LoginDialog.class);
 when(mockUserInterfaceFactory.createLoginDialog(any(SessionProperties.class), anyInt())).thenReturn(mockLoginDialog);
 when(mockLoginDialog.isCancelled()).thenReturn(true);

 // Call method under test
 int numberOfAttempts = macawWorkBench.main(new String[0]);

 // Verify system exits with status 0
 verifyZeroExit();
 }

 @Test
 public void testLoginDialogThreeUnsuccessfulAttempts() {
 // Set up mock User and expectation for isLoginSuccessful()
 User mockUser = mock(User.class);
 when(mockUser.isLoginSuccessful()).thenReturn(false);

 // Set up mock SessionProperties and expectations for getProperty() and setProperty()
 SessionProperties mockSessionProperties = mock(SessionProperties.class);
 when(mockSessionProperties.getProperty("currentUser")).thenReturn(mockUser);
 when(mockSessionProperties.setProperty(anyString(), any())).thenCallRealMethod();

 // Set up expectations for LoginDialog show() and isLoginSuccessful()
 LoginDialog mockLoginDialog = mock(LoginDialog.class);
 when(mockUserInterfaceFactory.createLoginDialog(any(SessionProperties.class), anyInt())).thenReturn(mockLoginDialog);
 when(mockLoginDialog.isLoginSuccessful()).thenReturn(false);

 // Call method under test
 int numberOfAttempts = macawWorkBench.main(new String[0]);

 // Verify system exits with status 0
 verifyZeroExit();
 }
}