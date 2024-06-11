package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;

import javax.swing.JButton;

import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.presentationLayer.UserInterfaceFactory;
import macaw.system.*;
import macaw.io.*;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private UserInterfaceFactory userInterfaceFactory;
 private SessionProperties sessionProperties;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.setUserInterfaceFactory(userInterfaceFactory);
 }

 @Test
 public void testCreateButtonPanel() {
 // Test creating the button panel
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);

 // Test number of buttons created
 assertEquals(4, buttonPanel.getComponentCount());

 // Test button labels
 String editConstantsText = MacawMessages.getMessage("macawWorkBench.editConstants");
 String editVariablesText = MacawMessages.getMessage("macawWorkBench.editVariables");
 String exportVariableText = MacawMessages.getMessage("macawWorkBench.exportVariableData");
 String exitText = MacawMessages.getMessage("general.buttons.exit");

 JButton editConstantsButton = (JButton)buttonPanel.getComponent(0);
 assertEquals(editConstantsText, editConstantsButton.getText());

 JButton editVariablesButton = (JButton)buttonPanel.getComponent(1);
 assertEquals(editVariablesText, editVariablesButton.getText());

 JButton exportVariableDataButton = (JButton)buttonPanel.getComponent(2);
 assertEquals(exportVariableText, exportVariableDataButton.getText());

 JButton exitButton = (JButton)buttonPanel.getComponent(3);
 assertEquals(exitText, exitButton.getText());
 }

 @Test
 public void testShowDialog() {
 // Test that show method opens dialog
 macawWorkBench.show();
 verify(userInterfaceFactory, times(1)).createDialog();
 }

 @Test
 public void testEditConstants() {
 // Mock the VariableConstantsEditor to return true for show method
 VariableConstantsEditor variableConstantsEditor = mock(VariableConstantsEditor.class);
 when(variableConstantsEditor.show()).thenReturn(true);

 // Test that editConstants method opens VariableConstantsEditor when editConstants button is clicked
 doCallRealMethod().when(macawWorkBench).editConstants();
 macawWorkBench.editConstantsButton.doClick();
 verify(userInterfaceFactory, times(1)).createDialog(eq(VariableConstantsEditor.class));
 verify(variableConstantsEditor, times(1)).show();
 }

 @Test
 public void testEditVariables() {
 // Test that editVariables method opens MacawVariableBrowser when editVariables button is clicked
 doCallRealMethod().when(macawWorkBench).editVariables();
 macawWorkBench.editVariablesButton.doClick();
 verify(userInterfaceFactory, times(1)).createDialog(eq(MacawVariableBrowser.class));
 }

 @Test
 public void testExportVariableData() {
 // Mock the ProductionRetrievalService and ProductionCurationService to return data for export
 ProductionRetrievalService retrievalService = mock(ProductionRetrievalService.class);
 ProductionCurationService database = mock(ProductionCurationService.class);
 when(retrievalService.retrieveVariables()).thenReturn(new Variable[0]);
 when(database.exportVariables()).thenReturn(new FileWriter("test.xml"));

 // Set up session properties with mock objects
 sessionProperties.setProperty("retrievalService", retrievalService);
 sessionProperties.setProperty("database", database);

 // Test that exportVariableData method exports variables to MacawXML file
 macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.exportVariableDataButton.doClick();
 verify(retrievalService, times(1)).retrieveVariables();
 verify(database, times(1)).exportVariables();
 }

 @Test
 public void testExit() {
 // Test that exit method exits application when exit button is clicked
 doCallRealMethod().when(macawWorkBench).exit();
 macawWorkBench.exitButton.doClick();
 verify(macawWorkBench, times(1)).exit();
 }
}