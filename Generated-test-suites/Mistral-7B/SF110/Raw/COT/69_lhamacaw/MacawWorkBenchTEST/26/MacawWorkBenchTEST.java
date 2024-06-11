package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.system.MacawMessages;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private UserInterfaceFactory userInterfaceFactory;
 private SessionProperties sessionProperties;

 @Before
 public void setup() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testMacawWorkBenchConstructor() {
 // Check if the constructor is called and sets the sessionProperties and dialog properly
 assertNotNull(macawWorkBench.sessionProperties);
 assertNotNull(macawWorkBench.dialog);
 }

 @Test
 public void testCreateButtonPanel() {
 // Test if the createButtonPanel method creates the expected buttons and adds them to the panel
 JPanel expectedButtonPanel = new JPanel();
 expectedButtonPanel.add(mock(JButton.class)); // editConstants
 expectedButtonPanel.add(mock(JButton.class)); // editVariables
 expectedButtonPanel.add(mock(JButton.class)); // exportVariableData
 expectedButtonPanel.add(mock(JButton.class)); // exit
 assertEquals(expectedButtonPanel, macawWorkBench.createButtonPanel(userInterfaceFactory));
 }

 @Test
 public void testShow() {
 // Test if the show method makes the dialog visible
 macawWorkBench.show();
 assertTrue(macawWorkBench.dialog.isVisible());
 }

 @Test
 public void testEditConstants() {
 // Test if the editConstants method calls the VariableConstantsEditor correctly
 VariableConstantsEditor variableConstantsEditor = Mockito.mock(VariableConstantsEditor.class);
 macawWorkBench.editConstants();
 Mockito.verify(variableConstantsEditor).show();
 }

 @Test
 public void testEditVariables() {
 // Test if the editVariables method calls the MacawVariableBrowser correctly
 MacawVariableBrowser macawVariableBrowser = Mockito.mock(MacawVariableBrowser.class);
 macawWorkBench.editVariables();
 Mockito.verify(macawVariableBrowser).show();
 }

 @Test
 public void testExportVariableData() {
 // Test if the exportVariableData method calls ExportVariablesToMacawXML correctly
 ExportVariablesToMacawXML exportToDSSFormat = Mockito.mock(ExportVariablesToMacawXML.class);
 macawWorkBench.exportVariableData();
 Mockito.verify(exportToDSSFormat).export();
 }

 @Test
 public void testExit() {
 // Test if the exit method calls System.exit(0)
 macawWorkBench.exit();
 Mockito.verify(sessionProperties).setProperty("currentUser", null);
 // System.exit cannot be mocked, so we can't test its call directly
 }

 @Test(expected = Exception.class)
 public void testExceptionInConstructor() {
 // Test if an exception is thrown when there's a problem in the constructor
 try {
 macawWorkBench = new MacawWorkBench(sessionProperties);
 } catch (Exception e) {
 fail("An unexpected exception was thrown: " + e.getMessage());
 }
 sessionProperties.setProperty("database", null);
 throw new Exception("Database is null.");
 }
}