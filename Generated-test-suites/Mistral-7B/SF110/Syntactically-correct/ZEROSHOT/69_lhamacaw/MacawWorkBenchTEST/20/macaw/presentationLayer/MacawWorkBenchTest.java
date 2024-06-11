package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.*;
import org.junit.*;
import javax.swing.*;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private SessionProperties sessionProperties;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testMacawWorkBenchConstruction() {
 MacawWorkBench expectedMacawWorkBench = new MacawWorkBench(sessionProperties);
 assertEquals(expectedMacawWorkBench, macawWorkBench);
 }

 @Test
 public void testMacawWorkBenchShow() {
 JFrame frame = macawWorkBench.dialog;
 assertNotNull(frame);
 frame.isDisplayable();
 }

 @Test
 public void testCreateButtonPanel() {
 UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertNotNull(buttonPanel);
 }

 @Test
 public void testEditConstantsActionPerformed() {
 ActionEvent event = new ActionEvent(new Object(), 0, "editConstants");
 macawWorkBench.actionPerformed(event);
 verify(macawWorkBench).editConstants();
 }

 @Test
 public void testEditVariablesActionPerformed() {
 ActionEvent event = new ActionEvent(new Object(), 0, "editVariables");
 macawWorkBench.actionPerformed(event);
 verify(macawWorkBench).editVariables();
 }

 @Test
 public void testExportVariableDataActionPerformed() {
 ActionEvent event = new ActionEvent(new Object(), 0, "exportVariableData");
 macawWorkBench.actionPerformed(event);
 verify(macawWorkBench).exportVariableData();
 }

 @Test
 public void testExitActionPerformed() {
 ActionEvent event = new ActionEvent(new Object(), 0, "exit");
 macawWorkBench.actionPerformed(event);
 verify(macawWorkBench).exit();
 }
}