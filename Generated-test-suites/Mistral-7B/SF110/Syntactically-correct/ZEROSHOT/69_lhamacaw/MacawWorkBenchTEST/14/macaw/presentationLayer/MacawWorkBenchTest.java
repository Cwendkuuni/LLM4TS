package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.*;
import macaw.system.*;
import javax.swing.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MacawWorkBenchTest {

 private SessionProperties sessionProperties;
 private MacawWorkBench macawWorkBench;
 private UserInterfaceFactory userInterfaceFactory;
 private User user;

 @Before
 public void setUp() {
 sessionProperties = Mockito.mock(SessionProperties.class);
 user = new User("jsmith", "cool");
 userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
 MacawSystem.setSessionProperties(sessionProperties);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testMacawWorkBenchConstruction() {
 assertEquals("jsmith", macawWorkBench.sessionProperties.getCurrentUser().getUsername());
 assertTrue(macawWorkBench.dialog != null);
 }

 @Test
 public void testCreateButtonPanel() {
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertTrue(buttonPanel.getComponentCount() == 5);
 }

 @Test
 public void testEditConstantsMethod() {
 MacawWorkBench spyMacawWorkBench = spy(macawWorkBench);
 doCallRealMethod().when(spyMacawWorkBench).editConstants();
 VariableConstantsEditor variableConstantsEditor = Mockito.mock(VariableConstantsEditor.class);
 when(userInterfaceFactory.createDialog()).thenReturn(variableConstantsEditor);
 spyMacawWorkBench.editConstants();
 verify(variableConstantsEditor).show();
 }

 @Test
 public void testEditVariablesMethod() {
 MacawWorkBench spyMacawWorkBench = spy(macawWorkBench);
 doCallRealMethod().when(spyMacawWorkBench).editVariables();
 MacawVariableBrowser macawVariableBrowser = Mockito.mock(MacawVariableBrowser.class);
 when(userInterfaceFactory.createDialog()).thenReturn(macawVariableBrowser);
 spyMacawWorkBench.editVariables();
 verify(macawVariableBrowser).show();
 }

 @Test
 public void testExportVariableDataMethod() {
 MacawWorkBench spyMacawWorkBench = spy(macawWorkBench);
 doCallRealMethod().when(spyMacawWorkBench).exportVariableData();
 ExportVariablesToMacawXML exportVariablesToMacawXML = Mockito.mock(ExportVariablesToMacawXML.class);
 when(sessionProperties.getDatabase()).thenReturn(Mockito.mock(ProductionDatabase.class));
 when(sessionProperties.getRetrievalService()).thenReturn(Mockito.mock(ProductionRetrievalService.class));
 when(exportVariablesToMacawXML.export()).thenReturn(true);
 spyMacawWorkBench.exportVariableData();
 verify(exportVariablesToMacawXML).export();
 }

 @Test
 public void testExitMethod() {
 MacawWorkBench spyMacawWorkBench = spy(macawWorkBench);
 doCallRealMethod().when(spyMacawWorkBench).exit();
 spyMacawWorkBench.exit();
 verify(systemExit).exit(0);
 }

 @Test
 public void testActionPerformedMethod() {
 MacawWorkBench spyMacawWorkBench = spy(macawWorkBench);
 ActionEvent mockEvent = Mockito.mock(ActionEvent.class);
 when(mockEvent.getSource()).thenReturn(macawWorkBench.editConstants);
 doCallRealMethod().when(spyMacawWorkBench).actionPerformed(mockEvent);
 verify(spyMacawWorkBench).editConstants();
 }

 @Test
 public void testActionPerformedWithExportVariableDataButton() {
 MacawWorkBench spyMacawWorkBench = spy(macawWorkBench);
 ActionEvent mockEvent = Mockito.mock(ActionEvent.class);
 when(mockEvent.getSource()).thenReturn(macawWorkBench.exportVariableData);
 doCallRealMethod().when(spyMacawWorkBench).actionPerformed(mockEvent);
 verify(spyMacawWorkBench).exportVariableData();
 }

 @Test
 public void testActionPerformedWithExitButton() {
 MacawWorkBench spyMacawWorkBench = spy(macawWorkBench);
 ActionEvent mockEvent = Mockito.mock(ActionEvent.class);
 when(mockEvent.getSource()).thenReturn(macawWorkBench.exit);
 doCallRealMethod().when(spyMacawWorkBench).actionPerformed(mockEvent);
 verify(systemExit).exit(0);
 }
}