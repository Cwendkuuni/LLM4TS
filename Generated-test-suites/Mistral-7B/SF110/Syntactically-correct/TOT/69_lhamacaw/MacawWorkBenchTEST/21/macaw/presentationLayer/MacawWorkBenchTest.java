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
 public void setup() {
 sessionProperties = new SessionProperties();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testConstructor() {
 // Test that the constructor is called correctly
 assertNotNull(macawWorkBench);
 }

 @Test
 public void testMain() {
 // This method cannot be directly tested as it is a static method and does not return anything
 // Instead, we can test the expected output of the main method by creating a test case for each method called within it
 }

 @Test
 public void testCreateDialog() {
 // Test that the createDialog method creates a JDialog with the correct title
 assertEquals("macawWorkBench.title", macawWorkBench.dialog.getTitle());
 }

 @Test
 public void testCreateButtonPanel() {
 // Test that the createButtonPanel method creates a JPanel with three buttons: editConstants, editVariables, and exportVariableData
 assertEquals(3, macawWorkBench.createButtonPanel(null).getComponentCount());
 }

 @Test
 public void testEditConstants() {
 // Test that the editConstants method opens the VariableConstantsEditor dialog
 macawWorkBench.editConstants();
 assertTrue(VariableConstantsEditor.isVisible());
 }

 @Test
 public void testEditVariables() {
 // Test that the editVariables method opens the MacawVariableBrowser dialog
 macawWorkBench.editVariables();
 assertTrue(MacawVariableBrowser.isVisible());
 }

 @Test
 public void testExportVariableData() {
 // Test that the exportVariableData method exports variables to MacawXML format
 // This test requires a production database and a valid user with permissions to export variables
 // Replace the following code with actual assertions when you have access to a working system
 ExportVariablesToMacawXML exportToDSSFormat = new ExportVariablesToMacawXML(sessionProperties);
 exportToDSSFormat.export();
 // Add assertions to check the exported file content here
 }

 @Test
 public void testExit() {
 // Test that the exit method closes the dialog and exits the application
 macawWorkBench.exit();
 assertFalse(macawWorkBench.dialog.isVisible());
 System.exit(0);
 // We cannot test the exit event here as the test will be terminated
 }

 @Test
 public void testActionPerformed() {
 // Test that the actionPerformed method correctly handles button events
 ActionEvent event = new ActionEvent(new Object(), 0, "testButton");
 macawWorkBench.actionPerformed(event);
 if (event.getSource() == macawWorkBench.editConstants) {
 macawWorkBench.editConstants();
 }
 else if (event.getSource() == macawWorkBench.editVariables) {
 macawWorkBench.editVariables();
 }
 else if (event.getSource() == macawWorkBench.exportVariableData) {
 macawWorkBench.exportVariableData();
 }
 else if (event.getSource() == macawWorkBench.exit) {
 macawWorkBench.exit();
 }
 // Add assertions to check the expected behavior of each method here
 }
}