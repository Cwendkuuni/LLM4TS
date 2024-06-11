package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.system.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private SessionProperties sessionProperties;
 private UserInterfaceFactory userInterfaceFactory;
 private JDialog dialog;
 private JButton editConstants;
 private JButton editVariables;
 private JButton exportVariableData;
 private JButton exit;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = sessionProperties.getUserInterfaceFactory();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 dialog = macawWorkBench.dialog;
 editConstants = macawWorkBench.editConstants;
 editVariables = macawWorkBench.editVariables;
 exportVariableData = macawWorkBench.exportVariableData;
 exit = macawWorkBench.exit;
 }

 @Test
 public void testMacawWorkBenchCreation() {
 assertTrue(macawWorkBench != null);
 assertNotNull(dialog);
 assertNotNull(editConstants);
 assertNotNull(editVariables);
 assertNotNull(exportVariableData);
 assertNotNull(exit);
 }

 @Test
 public void testEditConstantsButtonClick() {
 editConstants.doClick();
 verify(sessionProperties).getVariableConstantsEditor(true).show();
 }

 @Test
 public void testEditVariablesButtonClick() {
 editVariables.doClick();
 verify(sessionProperties).getMacawVariableBrowser().show();
 }

 @Test
 public void testExportVariableDataButtonClick() {
 exportVariableData.doClick();
 // Add assertions for expected behavior, such as file being created and containing expected data
 }

 @Test
 public void testExitButtonClick() {
 exit.doClick();
 assertEquals(0, System.exit(0));
 }

 @After
 public void tearDown() {
 dialog.dispose();
 macawWorkBench = null;
 sessionProperties = null;
 userInterfaceFactory = null;
 }

}