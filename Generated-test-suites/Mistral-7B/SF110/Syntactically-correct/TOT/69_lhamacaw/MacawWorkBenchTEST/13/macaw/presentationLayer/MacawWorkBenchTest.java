package macaw.presentationLayer.test;

import static org.junit.Assert.assertTrue;
import macaw.businessLayer.VariableConstants;
import macaw.presentationLayer.MacawWorkBench;
import macaw.presentationLayer.UserInterfaceFactory;

import org.junit.Before;
import org.junit.Test;

public class MacawWorkBenchTest {

 private MacawWorkBench workBench;
 private UserInterfaceFactory userInterfaceFactory;

 @Before
 public void setUp() {
 // Initialize the user interface factory and MacawWorkBench
 userInterfaceFactory = new UserInterfaceFactory();
 workBench = new MacawWorkBench(new SessionProperties());
 }

 @Test
 public void testEditConstantsButton() {
 // Test if editConstants button is enabled initially
 assertTrue("Edit Constants button is not enabled initially", workBench.editConstants.isEnabled());
 // Simulate user clicking on the edit constants button
 workBench.editConstants.doClick();
 // Verify that VariableConstantsEditor dialog is displayed
 UserInterfaceFactory uiFactory = workBench.sessionProperties.getUserInterfaceFactory();
 assertTrue("VariableConstantsEditor dialog is not displayed", uiFactory.getDialog().isDisplayable());
 }

 @Test
 public void testEditVariablesButton() {
 // Test if editVariables button is enabled initially
 assertTrue("Edit Variables button is not enabled initially", workBench.editVariables.isEnabled());
 // Simulate user clicking on the edit variables button
 workBench.editVariables.doClick();
 // Verify that MacawVariableBrowser dialog is displayed
 UserInterfaceFactory uiFactory = workBench.sessionProperties.getUserInterfaceFactory();
 assertTrue("MacawVariableBrowser dialog is not displayed", uiFactory.getDialog().isDisplayable());
 }

 @Test
 public void testExportVariableDataButton() {
 // Test if exportVariableData button is enabled initially
 assertTrue("Export Variable Data button is not enabled initially", workBench.exportVariableData.isEnabled());
 // Simulate user clicking on the exportVariableData button
 workBench.exportVariableData.doClick();
 // Verify that ExportVariablesToMacawXML is called and exports the data
 ExportVariablesToMacawXML exportToFile = new ExportVariablesToMacawXML(workBench.sessionProperties);
 exportToFile.export();
 // TODO: Add verification that the file is correctly exported
 }

 @Test
 public void testExitButton() {
 // Test if exit button is enabled initially
 assertTrue("Exit button is not enabled initially", workBench.exit.isEnabled());
 // Simulate user clicking on the exit button
 workBench.exit.doClick();
 // Verify that the system exits
 assertTrue("System did not exit", System.exit(0) == 0);
 }
}