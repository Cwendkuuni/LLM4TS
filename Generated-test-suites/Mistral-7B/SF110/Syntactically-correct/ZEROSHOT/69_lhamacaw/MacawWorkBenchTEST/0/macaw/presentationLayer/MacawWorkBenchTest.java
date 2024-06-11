package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.VariableConstants;
import macaw.businessLayer.VariableEditor;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.production.ProductionCurationService;
import macaw.presentationLayer.MacawWorkBench;
import macaw.system.SessionProperties;
import macaw.ui.UserInterfaceFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private SessionProperties sessionProperties;
 private UserInterfaceFactory userInterfaceFactory;
 private JDialog dialog;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = Mockito.spy(new UserInterfaceFactory());
 macawWorkBench = new MacawWorkBench(sessionProperties);
 dialog = macawWorkBench.dialog;
 }

 @After
 public void tearDown() {
 if (dialog != null) {
 dialog.dispose();
 }
 }

 @Test
 public void testMacawWorkBenchConstruction() {
 assertNotNull(macawWorkBench);
 assertNotNull(macawWorkBench.dialog);
 assertNotNull(macawWorkBench.sessionProperties);
 }

 @Test
 public void testCreateButtonPanel() {
 UserInterfaceFactory mockUserInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 JPanel buttonPanel = macawWorkBench.createButtonPanel(mockUserInterfaceFactory);
 assertNotNull(buttonPanel);
 }

 @Test
 public void testShowDialog() {
 macawWorkBench.show();
 assertTrue(dialog.isDisplayable());
 }

 @Test
 public void testEditConstantsButton() {
 when(userInterfaceFactory.createButton(anyString())).thenReturn(macawWorkBench.editConstants);
 macawWorkBench.editConstants();
 verify(userInterfaceFactory).createButton(MacawMessages.getMessage("macawWorkBench.editConstants"));
 VariableConstantsEditor variableConstantsEditor = Mockito.mock(VariableConstantsEditor.class);
 when(variableConstantsEditor.show()).thenReturn(true);
 macawWorkBench.editConstants = Mockito.mock(JButton.class);
 macawWorkBench.editConstants.addActionListener(macawWorkBench);
 macawWorkBench.editConstants();
 verify(userInterfaceFactory).createButton(MacawMessages.getMessage("macawWorkBench.editConstants"));
 verify(variableConstantsEditor).show();
 }

 @Test
 public void testEditVariablesButton() {
 when(userInterfaceFactory.createButton(anyString())).thenReturn(macawWorkBench.editVariables);
 macawWorkBench.editVariables();
 verify(userInterfaceFactory).createButton(MacawMessages.getMessage("macawWorkBench.editVariables"));
 MacawVariableBrowser macawVariableBrowser = Mockito.mock(MacawVariableBrowser.class);
 when(macawVariableBrowser.show()).thenReturn(true);
 macawWorkBench.editVariables = Mockito.mock(JButton.class);
 macawWorkBench.editVariables.addActionListener(macawWorkBench);
 macawWorkBench.editVariables();
 verify(userInterfaceFactory).createButton(MacawMessages.getMessage("macawWorkBench.editVariables"));
 verify(macawVariableBrowser).show();
 }

 @Test
 public void testExportVariableDataButton() {
 when(userInterfaceFactory.createButton(anyString())).thenReturn(macawWorkBench.exportVariableData);
 when(sessionProperties.getProperty("database")).thenReturn(new ProductionCurationService(sessionProperties));
 macawWorkBench.exportVariableData();
 verify(userInterfaceFactory).createButton(MacawMessages.getMessage("macawWorkBench.exportVariableData"));
 ExportVariablesToMacawXML exportToDSSFormat = Mockito.mock(ExportVariablesToMacawXML.class);
 macawWorkBench.exportVariableData = Mockito.mock(JButton.class);
 macawWorkBench.exportVariableData.addActionListener(macawWorkBench);
 macawWorkBench.exportVariableData();
 verify(userInterfaceFactory).createButton(MacawMessages.getMessage("macawWorkBench.exportVariableData"));
 verify(exportToDSSFormat).export();
 }

 @Test
 public void testExitButton() {
 when(userInterfaceFactory.createButton(anyString())).thenReturn(macawWorkBench.exit);
 macawWorkBench.exit();
 verify(macawWorkBench.exit).doClick();
 }

 @Test(expected = IllegalStateException.class)
 public void testLoginDialogIsNotModal() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.dialog.setModal(false);
 macawWorkBench.show();
 fail("Login dialog should be modal.");
 }
}