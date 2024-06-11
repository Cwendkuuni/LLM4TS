package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.system.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MacawWorkBenchTest {

 private SessionProperties sessionProperties;
 private MacawWorkBench macawWorkBench;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @After
 public void tearDown() {
 macawWorkBench = null;
 sessionProperties = null;
 }

 @Test
 public void testMacawWorkBenchConstructor() {
 MacawWorkBench expectedMacawWorkBench = new MacawWorkBench(sessionProperties);
 assertNotNull(expectedMacawWorkBench);
 }

 @Test
 public void testCreateButtonPanel() {
 UserInterfaceFactory userInterfaceFactory = sessionProperties.getUserInterfaceFactory();
 JPanel expectedButtonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertNotNull(expectedButtonPanel);
 }

 @Test
 public void testEditConstants() {
 VariableConstantsEditor variableConstantsEditor = mock(VariableConstantsEditor.class);
 when(variableConstantsEditor.show()).thenReturn(true);
 doAnswer(invocationOnMock -> macawWorkBench.editConstants()).when(macawWorkBench).editConstants();
 assertTrue(macawWorkBench.editConstants());
 verify(variableConstantsEditor).show();
 }

 @Test
 public void testEditVariables() {
 MacawVariableBrowser macawVariableBrowser = mock(MacawVariableBrowser.class);
 when(macawVariableBrowser.show()).thenReturn(true);
 doAnswer(invocationOnMock -> macawWorkBench.editVariables()).when(macawWorkBench).editVariables();
 assertTrue(macawWorkBench.editVariables());
 verify(macawVariableBrowser).show();
 }

 @Test
 public void testExportVariableData() {
 ExportVariablesToMacawXML exportToDSSFormat = mock(ExportVariablesToMacawXML.class);
 doAnswer(invocationOnMock -> exportToDSSFormat.export()).when(macawWorkBench).exportVariableData();
 macawWorkBench.exportVariableData();
 verify(exportToDSSFormat).export();
 }

 @Test(expected = NullPointerException.class)
 public void testExit() {
 macawWorkBench.exit();
 }

 @Test
 public void testLoginDialog() {
 // Login dialog testing should be done separately as it's a separate component
 }

}