package macaw.presentationLayer.test;

import static org.junit.Assert.*;

import macaw.businessLayer.*;
import macaw.persistenceLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.system.*;
import javax.swing.*;
import java.awt.*;
import macaw.io.*;
import org.junit.Before;
import org.junit.Test;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private UserInterfaceFactory userInterfaceFactory;
 private SessionProperties sessionProperties;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = new UserInterfaceFactory();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testMain() {
 MacawWorkBench.main(new String[0]);
 // Add asserts here to test the behavior of the MacawWorkBench.main() method
 }

 @Test
 public void testMacawWorkBenchConstructor() {
 // Add asserts here to test the behavior of the MacawWorkBench constructor
 }

 @Test
 public void testCreateButtonPanel() {
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 // Add asserts here to test the behavior of the createButtonPanel() method
 }

 @Test
 public void testShow() {
 macawWorkBench.show();
 // Add asserts here to test the behavior of the show() method
 }

 @Test
 public void testEditConstants() {
 macawWorkBench.editConstants();
 // Add asserts here to test the behavior of the editConstants() method
 }

 @Test
 public void testEditVariables() {
 macawWorkBench.editVariables();
 // Add asserts here to test the behavior of the editVariables() method
 }

 @Test
 public void testExportVariableData() {
 macawWorkBench.exportVariableData();
 // Add asserts here to test the behavior of the exportVariableData() method
 }

 @Test
 public void testExit() {
 macawWorkBench.exit();
 // Add asserts here to test the behavior of the exit() method
 }

 @Test
 public void testActionPerformedEditConstants() {
 ActionEvent editConstantsEvent = new ActionEvent(macawWorkBench.editConstants, ActionEvent.ACTION_PERFORMED, "Edit Constants");
 macawWorkBench.actionPerformed(editConstantsEvent);
 // Add asserts here to test the behavior of the actionPerformed() method when the Edit Constants button is clicked
 }

 @Test
 public void testActionPerformedEditVariables() {
 ActionEvent editVariablesEvent = new ActionEvent(macawWorkBench.editVariables, ActionEvent.ACTION_PERFORMED, "Edit Variables");
 macawWorkBench.actionPerformed(editVariablesEvent);
 // Add asserts here to test the behavior of the actionPerformed() method when the Edit Variables button is clicked
 }

 @Test
 public void testActionPerformedExportVariableData() {
 ActionEvent exportVariableDataEvent = new ActionEvent(macawWorkBench.exportVariableData, ActionEvent.ACTION_PERFORMED, "Export Variable Data");
 macawWorkBench.actionPerformed(exportVariableDataEvent);
 // Add asserts here to test the behavior of the actionPerformed() method when the Export Variable Data button is clicked
 }

 @Test
 public void testActionPerformedExit() {
 ActionEvent exitEvent = new ActionEvent(macawWorkBench.exit, ActionEvent.ACTION_PERFORMED, "Exit");
 macawWorkBench.actionPerformed(exitEvent);
 // Add asserts here to test the behavior of the actionPerformed() method when the Exit button is clicked
 }

}