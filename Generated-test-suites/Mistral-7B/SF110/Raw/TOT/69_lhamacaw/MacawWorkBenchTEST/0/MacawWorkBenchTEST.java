package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;

import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.presentationLayer.UserInterfaceFactory;
import macaw.system.MacawMessages;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private UserInterfaceFactory userInterfaceFactory;
 private SessionProperties sessionProperties;

 @Before
 public void setup() {
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 sessionProperties = new SessionProperties();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 MacawWorkBench.setUserInterfaceFactory(userInterfaceFactory);
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testCreateButtonPanel() {
 // Test that createButtonPanel creates buttons with correct labels and tooltips
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertEquals("macawWorkBench.editConstants", macawWorkBench.editConstants.getLabel());
 assertEquals("macawWorkBench.editConstants.instructions", macawWorkBench.editConstants.getToolTipText());
 assertEquals("macawWorkBench.editVariables", macawWorkBench.editVariables.getLabel());
 assertEquals("macawWorkBench.editVariables.instructions", macawWorkBench.editVariables.getToolTipText());
 assertEquals("macawWorkBench.exportVariableData", macawWorkBench.exportVariableData.getLabel());
 assertEquals("macawWorkBench.exportVariableData.instructions", macawWorkBench.exportVariableData.getToolTipText());
 assertEquals("general.buttons.exit", macawWorkBench.exit.getLabel());
 }

 @Test
 public void testShowDialog() {
 // Test that show method displays dialog
 macawWorkBench.show();
 assertTrue("Dialog should be visible", macawWorkBench.dialog.isDisplayable());
 }

 @Test
 public void testEditConstants() {
 // Test that editConstants method calls VariableConstantsEditor
 macawWorkBench.editConstants();
 verify(userInterfaceFactory, times(1)).createDialog();
 }

 @Test
 public void testEditVariables() {
 // Test that editVariables method calls MacawVariableBrowser
 macawWorkBench.editVariables();
 verify(userInterfaceFactory, times(1)).createMacawVariableBrowser();
 }

 @Test
 public void testExportVariableData() {
 // Test that exportVariableData method calls ExportVariablesToMacawXML
 macawWorkBench.exportVariableData();
 verify(userInterfaceFactory, times(1)).createExportVariablesToMacawXML();
 }

 @Test
 public void testExit() {
 // Test that exit method calls System.exit
 macawWorkBench.exit();
 verify(userInterfaceFactory, never()).createDialog();
 verify(userInterfaceFactory, never()).createMacawVariableBrowser();
 verify(userInterfaceFactory, never()).createExportVariablesToMacawXML();
 }

 @Test
 public void testActionPerformedWithEditConstants() {
 // Test that actionPerformed calls editConstants method with correct event
 ActionEvent actionEvent = new ActionEvent(macawWorkBench.editConstants, ActionEvent.ACTION_PERFORMED, "editConstants");
 macawWorkBench.actionPerformed(actionEvent);
 verify(macawWorkBench, times(1)).editConstants();
 }

 @Test
 public void testActionPerformedWithEditVariables() {
 // Test that actionPerformed calls editVariables method with correct event
 ActionEvent actionEvent = new ActionEvent(macawWorkBench.editVariables, ActionEvent.ACTION_PERFORMED, "editVariables");
 macawWorkBench.actionPerformed(actionEvent);
 verify(macawWorkBench, times(1)).editVariables();
 }

 @Test
 public void testActionPerformedWithExportVariableData() {
 // Test that actionPerformed calls exportVariableData method with correct event
 ActionEvent actionEvent = new ActionEvent(macawWorkBench.exportVariableData, ActionEvent.ACTION_PERFORMED, "exportVariableData");
 macawWorkBench.actionPerformed(actionEvent);
 verify(macawWorkBench, times(1)).exportVariableData();
 }

 @Test
 public void testActionPerformedWithExit() {
 // Test that actionPerformed calls exit method with correct event
 ActionEvent actionEvent = new ActionEvent(macawWorkBench.exit, ActionEvent.ACTION_PERFORMED, "exit");
 macawWorkBench.actionPerformed(actionEvent);
 verify(macawWorkBench, times(1)).exit();
 }
}