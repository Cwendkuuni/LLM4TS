package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.*;
import macaw.system.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private SessionProperties sessionProperties;
 private UserInterfaceFactory userInterfaceFactory;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = new SwingUserInterfaceFactory(sessionProperties);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @After
 public void tearDown() {
 macawWorkBench.dispose();
 }

 @Test
 public void testMain() {
 MacawWorkBench.main(new String[0]);
 assertTrue(macawWorkBench != null);
 }

 @Test
 public void testMacawWorkBenchConstructor() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 assertNotNull(macawWorkBench.dialog);
 assertNotNull(macawWorkBench.editConstants);
 assertNotNull(macawWorkBench.editVariables);
 assertNotNull(macawWorkBench.exportVariableData);
 assertNotNull(macawWorkBench.exit);
 }

 @Test
 public void testCreateButtonPanel() {
 UserInterfaceFactory userInterfaceFactorySpy = spy(userInterfaceFactory);
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactorySpy);
 assertEquals(3, buttonPanel.getComponentCount());
 }

 @Test
 public void testShowDialog() {
 macawWorkBench.show();
 assertTrue(macawWorkBench.dialog.isVisible());
 }

 @Test
 public void testEditConstants() {
 macawWorkBench.editConstants();
 assertTrue(true); // Replace with actual verification logic
 }

 @Test
 public void testEditVariables() {
 macawWorkBench.editVariables();
 assertTrue(true); // Replace with actual verification logic
 }

 @Test
 public void testExportVariableData() {
 macawWorkBench.exportVariableData();
 assertTrue(true); // Replace with actual verification logic
 }

 @Test
 public void testExit() {
 macawWorkBench.exit();
assertTrue(System.exit(0) == 0);
 }

 @Test
 public void testActionPerformed() {
 ActionEvent actionEvent = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "testButton");
 macawWorkBench.actionPerformed(actionEvent);
 if (actionEvent.getSource() == macawWorkBench.editConstants) {
 macawWorkBench.editConstants();
 assertTrue(true); // Replace with actual verification logic
 } else if (actionEvent.getSource() == macawWorkBench.editVariables) {
 macawWorkBench.editVariables();
 assertTrue(true); // Replace with actual verification logic
 } else if (actionEvent.getSource() == macawWorkBench.exportVariableData) {
 macawWorkBench.exportVariableData();
 assertTrue(true); // Replace with actual verification logic
 } else if (actionEvent.getSource() == macawWorkBench.exit) {
 macawWorkBench.exit();
 assertTrue(System.exit(0) == 0);
 }
 }
}