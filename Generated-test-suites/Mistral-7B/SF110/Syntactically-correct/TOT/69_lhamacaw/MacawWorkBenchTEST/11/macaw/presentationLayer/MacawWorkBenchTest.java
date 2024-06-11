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

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private UserInterfaceFactory userInterfaceFactory;
 private SessionProperties sessionProperties;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 sessionProperties = mock(SessionProperties.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.userInterfaceFactory = userInterfaceFactory;
 }

 @Test
 public void testMacawWorkBenchMain() {
 // Testing if the main method starts the application correctly
 MacawWorkBench.main(new String[0]);
 verify(sessionProperties, times(1)).setProperty("currentUser", any(User.class));
 // Add more test cases as needed
 }

 @Test
 public void testCreateDialog() {
 // Testing if the dialog is created correctly
 assertTrue(macawWorkBench.dialog != null);
 // Testing if the instructions area is created with the correct text
 String instructionsText = MacawMessages.getMessage("macawWorkBench.instructions");
 assertEquals(instructionsText, macawWorkBench.dialog.getContentPane().getComponent(0).getText());
 }

 @Test
 public void testCreateButtonPanel() {
 // Testing if the button panel is created correctly
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 // Testing if the edit constants button is added to the panel
 assertEquals(1, buttonPanel.getComponentCount());
 assertTrue(buttonPanel.getComponent(0) instanceof JButton);
 assertEquals("macawWorkBench.editConstants", ((JButton) buttonPanel.getComponent(0)).getText());
 }

 @Test
 public void testShowDialog() {
 // Testing if the dialog is shown correctly
 macawWorkBench.show();
 assertTrue(macawWorkBench.dialog.isVisible());
 }

 @Test
 public void testEditConstantsButtonAction() {
 // Creating a mock VariableConstantsEditor to test the editConstants method
 VariableConstantsEditor variableConstantsEditor = mock(VariableConstantsEditor.class);
 macawWorkBench.editConstants = new JButton("Edit Constants") {
 @Override
 public void actionPerformed(ActionEvent e) {
 macawWorkBench.editConstants();
 }
 };
 doReturn(variableConstantsEditor).when(userInterfaceFactory).createVariableConstantsEditor(any(SessionProperties.class), anyBoolean());

 // Testing if the editConstants method opens the VariableConstantsEditor dialog
 macawWorkBench.editConstants.doClick();
 verify(variableConstantsEditor, times(1)).show();
 }

 @Test
 public void testEditVariablesButtonAction() {
 // Creating a mock MacawVariableBrowser to test the editVariables method
 MacawVariableBrowser macawVariableBrowser = mock(MacawVariableBrowser.class);
 macawWorkBench.editVariables = new JButton("Edit Variables") {
 @Override
 public void actionPerformed(ActionEvent e) {
 macawWorkBench.editVariables();
 }
 };
 doReturn(macawVariableBrowser).when(userInterfaceFactory).createMacawVariableBrowser(any(SessionProperties.class));

 // Testing if the editVariables method opens the MacawVariableBrowser dialog
 macawWorkBench.editVariables.doClick();
 verify(macawVariableBrowser, times(1)).show();
 }

 @Test
 public void testExportVariableDataButtonAction() {
 // Creating a mock ExportVariablesToMacawXML to test the exportVariableData method
 ExportVariablesToMacawXML exportToDSSFormat = mock(ExportVariablesToMacawXML.class);
 macawWorkBench.exportVariableData = new JButton("Export Variable Data") {
 @Override
 public void actionPerformed(ActionEvent e) {
 macawWorkBench.exportVariableData();
 }
 };
 doReturn(exportToDSSFormat).when(userInterfaceFactory).exportVariablesToMacawXML(any(SessionProperties.class));
 // Testing if the exportVariableData method exports the variables correctly
 macawWorkBench.exportVariableData.doClick();
 verify(exportToDSSFormat, times(1)).export();
 }

 @Test
 public void testExitButtonAction() {
 // Creating a mock System.exit to test the exit method
 doNothing().when(System.class).exit(0);
 macawWorkBench.exit = new JButton("Exit") {
 @Override
 public void actionPerformed(ActionEvent e) {
 macawWorkBench.exit();
 }
 };
 macawWorkBench.exit.doClick();
 verify(System.class, times(1)).exit(0);
 }

}