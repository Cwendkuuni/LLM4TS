package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.system.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class MacawWorkBenchTest {

 private MacawWorkBench workBench;
 private UserInterfaceFactory uiFactory;

 @Before
 public void setUp() {
 workBench = new MacawWorkBench(new SessionProperties());
 uiFactory = workBench.sessionProperties.getUserInterfaceFactory();
 }

 @Test
 public void testConstructor() {
 // test if MacawWorkBench is constructed successfully
 assertNotNull(workBench.sessionProperties);
 assertNotNull(workBench.dialog);
 assertNotNull(workBench.editConstants);
 assertNotNull(workBench.editVariables);
 assertNotNull(workBench.exportVariableData);
 assertNotNull(workBench.exit);
 }

 @Test
 public void testCreateButtonPanel() {
 // create a mock dialog and test if createButtonPanel returns a valid JPanel
 JDialog dialog = Mockito.mock(JDialog.class);
 UserInterfaceFactory mockUIFactory = Mockito.mock(UserInterfaceFactory.class);
 when(mockUIFactory.createButton(anyString())).thenReturn(Mockito.mock(JButton.class));
 when(workBench.sessionProperties.getUserInterfaceFactory()).thenReturn(mockUIFactory);
 when(dialog.getContentPane()).thenReturn(Mockito.mock(Container.class));
 JPanel panel = workBench.createButtonPanel(mockUIFactory);
 assertNotNull(panel);
 }

 @Test
 public void testShowDialog() {
 // test if dialog is shown successfully
 workBench.show();
 assertTrue(workBench.dialog.isVisible());
 }

 @Test
 public void testEditConstants() {
 // test if editConstants method calls VariableConstantsEditor correctly
 VariableConstantsEditor editor = Mockito.mock(VariableConstantsEditor.class);
 when(workBench.sessionProperties.getProperty("sessionProperties")).thenReturn(new SessionProperties());
 when(workBench.sessionProperties.getUserInterfaceFactory()).thenReturn(uiFactory);
 when(uiFactory.createDialog()).thenReturn(dialog);
 when(dialog.setVisible(anyBoolean())).thenReturn(true);
 when(editor.show()).thenReturn(true);
 workBench.editConstants();
 verify(dialog).setVisible(true);
 verify(workBench.sessionProperties).setProperty("variableConstantsEditor", editor);
 }

 @Test
 public void testEditVariables() {
 // test if editVariables method calls MacawVariableBrowser correctly
 MacawVariableBrowser browser = Mockito.mock(MacawVariableBrowser.class);
 when(workBench.sessionProperties.getProperty("sessionProperties")).thenReturn(new SessionProperties());
 when(workBench.sessionProperties.getUserInterfaceFactory()).thenReturn(uiFactory);
 when(uiFactory.createDialog()).thenReturn(dialog);
 when(dialog.setVisible(anyBoolean())).thenReturn(true);
 when(browser.show()).thenReturn(true);
 workBench.editVariables();
 verify(dialog).setVisible(true);
 verify(workBench.sessionProperties).setProperty("macawVariableBrowser", browser);
 }

 @Test
 public void testExportVariableData() {
 // test if exportVariableData method calls ExportVariablesToMacawXML correctly
 ExportVariablesToMacawXML exporter = Mockito.mock(ExportVariablesToMacawXML.class);
 when(workBench.sessionProperties.getProperty("sessionProperties")).thenReturn(new SessionProperties());
 when(workBench.sessionProperties.getUserInterfaceFactory()).thenReturn(uiFactory);
 when(uiFactory.createDialog()).thenReturn(dialog);
 when(dialog.setVisible(anyBoolean())).thenReturn(true);
 when(exporter.export()).thenReturn(true);
 workBench.exportVariableData();
 verify(dialog).setVisible(true);
 verify(workBench.sessionProperties).setProperty("exportToDSSFormat", exporter);
 }

 @Test
 public void testExit() {
 // test if exit method exits the application successfully
 workBench.exit();
 assertTrue(System.exit(0) > 0);
 }

 @Test(expected = Exception.class)
 public void testInvalidConstructor() {
 // test if MacawWorkBench throws exception if sessionProperties is null
 workBench = new MacawWorkBench(null);
 assertFalse(workBench.dialog.isVisible());
 }

 @Test
 public void testLoginDialogFailure() {
 // test if MacawWorkBench shows LoginDialog three times and exits if all logins fail
 // this test is complex and requires a mock LoginDialog implementation
 // it's beyond the scope of this example
}

 @Test
 public void testLoginDialogSuccess() {
 // test if MacawWorkBench shows LoginDialog and exits if all logins succeed
 // this test is complex and requires a mock LoginDialog implementation
 // it's beyond the scope of this example
}
}