package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.*;
import org.junit.Test;

public class MacawWorkBenchTest {

 private SessionProperties sessionProperties;
 private MacawWorkBench macawWorkBench;

 @Test
 public void testMacawWorkBenchConstruction() {
 sessionProperties = new SessionProperties();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 assertNotNull(macawWorkBench.dialog);
 }

 @Test
 public void testCreateButtonPanel() {
 UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertNotNull(buttonPanel);
 }

 @Test
 public void testShowDialog() {
 UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.show();
 assertTrue(macawWorkBench.dialog.isVisible());
 }

 @Test
 public void testEditConstants() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.editConstants();
 // add verification code here for the VariableConstantsEditor, if needed
 }

 @Test
 public void testEditVariables() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.editVariables();
 // add verification code here for MacawVariableBrowser, if needed
 }

 @Test
 public void testExportVariableData() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.exportVariableData();
 // add verification code here for ExportVariablesToMacawXML, if needed
 }

 @Test
 public void testExit() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.exit();
 // verification code for System.exit(0) is not possible in JUnit
 }

 @Test
 public void testActionPerformedEditingConstants() {
 MacawWorkBench macawWorkBench = spy(new MacawWorkBench(sessionProperties));
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 macawWorkBench.editConstants = Mockito.mock(JButton.class);
 macawWorkBench.editConstants.addActionListener(macawWorkBench);
 macawWorkBench.editConstants.actionPerformed(actionEvent);
 verify(macawWorkBench).editConstants();
 }

 @Test
 public void testActionPerformedEditingVariables() {
 MacawWorkBench macawWorkBench = spy(new MacawWorkBench(sessionProperties));
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 macawWorkBench.editVariables = Mockito.mock(JButton.class);
 macawWorkBench.editVariables.addActionListener(macawWorkBench);
 macawWorkBench.editVariables.actionPerformed(actionEvent);
 verify(macawWorkBench).editVariables();
 }

 @Test
 public void testActionPerformedExportingVariableData() {
 MacawWorkBench macawWorkBench = spy(new MacawWorkBench(sessionProperties));
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 macawWorkBench.exportVariableData = Mockito.mock(JButton.class);
 macawWorkBench.exportVariableData.addActionListener(macawWorkBench);
 macawWorkBench.exportVariableData.actionPerformed(actionEvent);
 verify(macawWorkBench).exportVariableData();
 }

 @Test
 public void testActionPerformedExiting() {
 MacawWorkBench macawWorkBench = spy(new MacawWorkBench(sessionProperties));
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 macawWorkBench.exit = Mockito.mock(JButton.class);
 macawWorkBench.exit.addActionListener(macawWorkBench);
 macawWorkBench.exit.actionPerformed(actionEvent);
 // verifySystemExit(); // This verification is not possible in JUnit
 }
}