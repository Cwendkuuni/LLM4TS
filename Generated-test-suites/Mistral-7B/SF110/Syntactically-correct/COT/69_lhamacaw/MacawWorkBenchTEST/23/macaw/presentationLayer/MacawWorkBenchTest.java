package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.system.*;
import org.junit.Before;
import org.junit.Test;

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
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 assertNotNull(macawWorkBench);
 }

 @Test
 public void testCreateButtonPanel() {
 UserInterfaceFactory userInterfaceFactory = sessionProperties.getUserInterfaceFactory();
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertNotNull(buttonPanel);
 }

 @Test
 public void testEditConstantsMethod() {
 VariableConstantsEditor variableConstantsEditor = mock(VariableConstantsEditor.class);
 when(variableConstantsEditor.show()).thenReturn(true);
 doAnswer(invocation -> macawWorkBench.editConstants()).when(macawWorkBench).editConstants();
 macawWorkBench.editConstants();
 verify(variableConstantsEditor).show();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEditConstantsMethodWithNullSessionProperties() {
 macawWorkBench = new MacawWorkBench(null);
 macawWorkBench.editConstants();
 }

 @Test
 public void testEditVariablesMethod() {
 MacawVariableBrowser macawVariableBrowser = mock(MacawVariableBrowser.class);
 when(macawVariableBrowser.show()).thenReturn(true);
 doAnswer(invocation -> macawWorkBench.editVariables()).when(macawWorkBench).editVariables();
 macawWorkBench.editVariables();
 verify(macawVariableBrowser).show();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEditVariablesMethodWithNullSessionProperties() {
 macawWorkBench = new MacawWorkBench(null);
 macawWorkBench.editVariables();
 }

 @Test
 public void testExportVariableDataMethod() {
 ExportVariablesToMacawXML exportToDSSFormat = mock(ExportVariablesToMacawXML.class);
 doAnswer(invocation -> macawWorkBench.exportVariableData()).when(macawWorkBench).exportVariableData();
 macawWorkBench.exportVariableData();
 verify(exportToDSSFormat).export();
 }

 @Test(expected = IllegalStateException.class)
 public void testExportVariableDataMethodWithNullSessionProperties() {
 macawWorkBench = new MacawWorkBench(null);
 macawWorkBench.exportVariableData();
 }

 @Test
 public void testExitMethod() {
 System.exit(0);
 }

 @Test
 public void testActionPerformedMethodWithEditConstantsButton() {
 ActionEvent actionEvent = mock(ActionEvent.class);
 doAnswer(invocation -> macawWorkBench.editConstants()).when(macawWorkBench).editConstants();
 macawWorkBench.actionPerformed(actionEvent);
 verify(macawWorkBench).editConstants();
 }

 @Test
 public void testActionPerformedMethodWithEditVariablesButton() {
 ActionEvent actionEvent = mock(ActionEvent.class);
 doAnswer(invocation -> macawWorkBench.editVariables()).when(macawWorkBench).editVariables();
 macawWorkBench.actionPerformed(actionEvent);
 verify(macawWorkBench).editVariables();
 }

 @Test
 public void testActionPerformedMethodWithExportVariableDataButton() {
 ActionEvent actionEvent = mock(ActionEvent.class);
 doAnswer(invocation -> macawWorkBench.exportVariableData()).when(macawWorkBench).exportVariableData();
 macawWorkBench.actionPerformed(actionEvent);
 verify(macawWorkBench).exportVariableData();
 }

 @Test
 public void testActionPerformedMethodWithExitButton() {
 ActionEvent actionEvent = mock(ActionEvent.class);
 doAnswer(invocation -> macawWorkBench.exit()).when(macawWorkBench).exit();
 macawWorkBench.actionPerformed(actionEvent);
 verify(macawWorkBench).exit();
 }
}