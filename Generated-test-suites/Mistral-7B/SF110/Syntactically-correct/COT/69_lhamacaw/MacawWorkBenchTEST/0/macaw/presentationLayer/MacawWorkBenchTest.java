package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
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
 MacawWorkBench expectedMacawWorkBench = new MacawWorkBench(sessionProperties);
 assertSame("MacawWorkBench instances should be equal", expectedMacawWorkBench, macawWorkBench);
 }

 @Test
 public void testCreateButtonPanel() {
 JPanel buttonPanel = macawWorkBench.createButtonPanel(null);
 assertEquals("Number of buttons in buttonPanel should be 4", 4, buttonPanel.getComponentCount());
 }

 @Test
 public void testShow() {
 macawWorkBench.show();
 assertTrue("Dialog should be visible", macawWorkBench.dialog.isVisible());
 }

 @Test(expected = NullPointerException.class)
 public void testMainWithoutSessionProperties() {
 MacawWorkBench.main(new String[0]);
 }

 @Test(expected = NullPointerException.class)
 public void testMacawWorkBenchConstructorWithoutSessionProperties() {
 new MacawWorkBench(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoginDialogLoginSuccessfulMoreThanThreeTimes() {
 MacawWorkBench.main(new String[0]);
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 for (int i = 0; i < 4; i++) {
 macawWorkBench.loginDialog.isLoginSuccessful();
 }
 }

 @Test
 public void testEditConstantsMethod() {
 VariableConstantsEditor variableConstantsEditor = spy(VariableConstantsEditor.class);
 doNothing().when(variableConstantsEditor).show();
 macawWorkBench.sessionProperties.setProperty("variableConstantsEditor", variableConstantsEditor);
 macawWorkBench.editConstants();
 verify(variableConstantsEditor).show();
 }

 @Test
 public void testEditVariablesMethod() {
 MacawVariableBrowser macawVariableBrowser = spy(MacawVariableBrowser.class);
 doNothing().when(macawVariableBrowser).show();
 macawWorkBench.editVariables();
 verify(macawVariableBrowser).show();
 }

 @Test
 public void testExportVariableDataMethod() {
 ExportVariablesToMacawXML exportToDSSFormat = spy(ExportVariablesToMacawXML.class);
 doNothing().when(exportToDSSFormat).export();
 macawWorkBench.exportVariableData();
 verify(exportToDSSFormat).export();
 }

 @Test
 public void testExitMethod() {
 SystemMock systemMock = new SystemMock();
 System.setOut(systemMock.out);
 macawWorkBench.exit();
 assertEquals("Expected exit message", "Goodbye!\n", systemMock.getConsoleText());
 }

}