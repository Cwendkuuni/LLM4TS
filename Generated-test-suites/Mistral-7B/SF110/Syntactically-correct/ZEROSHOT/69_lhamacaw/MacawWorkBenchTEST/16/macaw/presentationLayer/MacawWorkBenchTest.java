package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.*;
import macaw.system.*;
import org.junit.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private UserInterfaceFactory userInterfaceFactory;
 private SessionProperties sessionProperties;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = new SwingUserInterfaceFactory(sessionProperties);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testMacawWorkBenchConstruction() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 assertNotNull(macawWorkBench.dialog);
 assertNotNull(macawWorkBench.editConstants);
 assertNotNull(macawWorkBench.editVariables);
 assertNotNull(macawWorkBench.exportVariableData);
 assertNotNull(macawWorkBench.exit);
 }

 @Test
 public void testEditConstantsButtonAction() {
 when(macawWorkBench.editConstants).thenCallRealMethod();
 macawWorkBench.editConstants.doClick();
 verify(macawWorkBench).editConstants();
 }

 @Test
 public void testEditVariablesButtonAction() {
 when(macawWorkBench.editVariables).thenCallRealMethod();
 macawWorkBench.editVariables.doClick();
 verify(macawWorkBench).editVariables();
 }

 @Test
 public void testExportVariableDataButtonAction() {
 when(macawWorkBench.exportVariableData).thenCallRealMethod();
 macawWorkBench.exportVariableData.doClick();
 verify(macawWorkBench).exportVariableData();
 }

 @Test
 public void testExitButtonAction() {
 when(macawWorkBench.exit).thenCallRealMethod();
 macawWorkBench.exit.doClick();
 verify(macawWorkBench).exit();
 }

 @Test
 public void testShowDialog() {
 macawWorkBench.show();
 assertTrue(macawWorkBench.dialog.isVisible());
 }

 @After
 public void tearDown() {
 macawWorkBench.dialog.dispose();
 }
}