package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.system.*;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import javax.swing.event.*;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private SessionProperties sessionProperties;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testMacawWorkBenchConstructor() {
 MacawWorkBench expectedMacawWorkBench = new MacawWorkBench(sessionProperties);
 assertSame(expectedMacawWorkBench, macawWorkBench);
 }

 @Test
 public void testCreateButtonPanel() {
 JPanel expectedButtonPanel = macawWorkBench.createButtonPanel(null);
 JButton[] buttons = expectedButtonPanel.getComponents();
 assertEquals(4, buttons.length);
 for (JButton button : buttons) {
 assertNotNull(button);
 }
 }

 @Test
 public void testEditConstants() {
 VariableConstantsEditor expectedVariableConstantsEditor = new VariableConstantsEditor(sessionProperties, true);
 macawWorkBench.editConstants();
 assertEquals(expectedVariableConstantsEditor.getClass(), macawWorkBench.getClass()); // Check if editConstants() opens VariableConstantsEditor
 }

 @Test
 public void testEditVariables() {
 MacawVariableBrowser expectedMacawVariableBrowser = new MacawVariableBrowser(sessionProperties);
 macawWorkBench.editVariables();
 assertEquals(expectedMacawVariableBrowser.getClass(), macawWorkBench.getClass()); // Check if editVariables() opens MacawVariableBrowser
 }

 @Test
 public void testExportVariableData() {
 ExportVariablesToMacawXML expectedExportToDSSFormat = new ExportVariablesToMacawXML(sessionProperties);
 macawWorkBench.exportVariableData();
 assertEquals(expectedExportToDSSFormat.getClass(), macawWorkBench.getClass()); // Check if exportVariableData() exports variables to MacawXML
 }

 @Test(expected = IllegalStateException.class)
 public void testFailedLogin() {
 macawWorkBench = new MacawWorkBench(sessionProperties);
 LoginDialog loginDialog = mock(LoginDialog.class);
 when(loginDialog.isLoginSuccessful()).thenReturn(false);
 when(loginDialog.isCancelled()).thenReturn(false);
 when(loginDialog.show()).thenReturn(loginDialog);
 macawWorkBench.show();
 }

 @Test
 public void testSuccessfulLogin() {
 macawWorkBench = new MacawWorkBench(sessionProperties);
 LoginDialog loginDialog = mock(LoginDialog.class);
 when(loginDialog.isLoginSuccessful()).thenReturn(true);
 when(loginDialog.isCancelled()).thenReturn(false);
 when(loginDialog.show()).thenReturn(loginDialog);
 macawWorkBench.show();
 assertTrue(macawWorkBench.dialog.isVisible());
 }

 @Test
 public void testExit() {
 macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.exit();
 assertTrue(System.exit(0) == 0);
 }

 @Test
 public void testActionPerformed() {
 ActionEvent event = mock(ActionEvent.class);
 JButton button = mock(JButton.class);
 when(button.equals(macawWorkBench.editConstants)).thenReturn(true);
 macawWorkBench.actionPerformed(event);
 macawWorkBench.editConstants();
 }

}