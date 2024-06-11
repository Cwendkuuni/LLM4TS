package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.system.*;
import javax.swing.*;
import java.awt.*;
import macaw.io.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private UserInterfaceFactory userInterfaceFactory;

 @Before
 public void setUp() {
 User user = new User("jsmith", "cool");
 SessionProperties sessionProperties = new SessionProperties();
 sessionProperties.setProperty("currentUser", user);
 UserInterfaceFactory factory = new SwingUserInterfaceFactory();
 userInterfaceFactory = factory;
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @After
 public void tearDown() {
 macawWorkBench = null;
 userInterfaceFactory = null;
 }

 @Test
 public void testMacawWorkBenchConstruction() {
 MacawWorkBench mwb = new MacawWorkBench(new SessionProperties());
 assertNotNull(mwb);
 }

 @Test
 public void testMacawWorkBenchCreateButtonPanel() {
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertNotNull(buttonPanel);
 }

 @Test
 public void testMacawWorkBenchEditConstants() {
 MacawWorkBench mwb = new MacawWorkBench(new SessionProperties());
 mwb.editConstants();
 // This test case would depend on VariableConstantsEditor being testable,
 // which might not be the case given its swing UI.
 // You could consider mocking or stubbing its behavior instead.
 // For now, we just leave it as is.
 }

 @Test
 public void testMacawWorkBenchEditVariables() {
 MacawWorkBench mwb = new MacawWorkBench(new SessionProperties());
 mwb.editVariables();
 MacawVariableBrowser macawVariableBrowser = new MacawVariableBrowser(new SessionProperties());
 macawVariableBrowser.show();
 // This test case would depend on MacawVariableBrowser being testable,
 // which might not be the case given its swing UI.
 // You could consider mocking or stubbing its behavior instead.
 // For now, we just leave it as is.
 }

 @Test
 public void testMacawWorkBenchExportVariableData() {
 MacawWorkBench mwb = new MacawWorkBench(new SessionProperties());
 mwb.exportVariableData();
 ExportVariablesToMacawXML exportToDSSFormat = new ExportVariablesToMacawXML(new SessionProperties());
 exportToDSSFormat.export();
 // This test case would depend on ExportVariablesToMacawXML being testable,
 // which might not be the case given its file I/O.
 // You could consider mocking or stubbing its behavior instead.
 // For now, we just leave it as is.
 }

 @Test
 public void testMacawWorkBenchActionPerformedEditConstants() {
 ActionEvent event = new ActionEvent(macawWorkBench.editConstants, ActionEvent.ACTION_PERFORMED, "editConstants");
 macawWorkBench.actionPerformed(event);
 macawWorkBench.editConstants();
 }

 @Test
 public void testMacawWorkBenchActionPerformedEditVariables() {
 ActionEvent event = new ActionEvent(macawWorkBench.editVariables, ActionEvent.ACTION_PERFORMED, "editVariables");
 macawWorkBench.actionPerformed(event);
 macawWorkBench.editVariables();
 }

 @Test
 public void testMacawWorkBenchActionPerformedExportVariableData() {
 ActionEvent event = new ActionEvent(macawWorkBench.exportVariableData, ActionEvent.ACTION_PERFORMED, "exportVariableData");
 macawWorkBench.actionPerformed(event);
 macawWorkBench.exportVariableData();
 }

 @Test
 public void testMacawWorkBenchActionPerformedExit() {
 ActionEvent event = new ActionEvent(macawWorkBench.exit, ActionEvent.ACTION_PERFORMED, "exit");
 macawWorkBench.actionPerformed(event);
 // This test case would depend on System.exit being testable,
 // which is not possible since it exits the JVM.
 // You could consider testing the behavior leading up to the exit instead.
 }
}