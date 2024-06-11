package macaw.presentationLayer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MacawWorkBenchTest {

 sessionProperties mockSessionProperties = null;
 MacawWorkBench macawWorkBench = null;
 UserInterfaceFactory mockUserInterfaceFactory = null;

 @Before
 public void setUp() {
 mockSessionProperties = mock(SessionProperties.class);
 mockUserInterfaceFactory = mock(UserInterfaceFactory.class);
 macawWorkBench = new MacawWorkBench(mockSessionProperties);
 }

 @Test
 public void testConstructor() {
 // Your test cases for constructor go here
 }

 @Test
 public void testMain() {
 // Your test cases for main method go here
 }

 @Test
 public void testCreateButtonPanel() {
 JPanel buttonPanel = macawWorkBench.createButtonPanel(mockUserInterfaceFactory);
 assertNotNull(buttonPanel);

 JButton editConstants = macawWorkBench.editConstants;
 JButton editVariables = macawWorkBench.editVariables;
 JButton exportVariableData = macawWorkBench.exportVariableData;
 JButton exit = macawWorkBench.exit;

 assertNotNull(editConstants);
 assertNotNull(editVariables);
 assertNotNull(exportVariableData);
 assertNotNull(exit);

 assertTrue(editConstants.isEnabled());
 assertTrue(editVariables.isEnabled());
 assertTrue(exportVariableData.isEnabled());
 assertTrue(exit.isEnabled());
 }

 @Test
 public void testActionPerformed_editConstants() {
 // Your test cases for editConstants method go here
 }

 @Test
 public void testActionPerformed_editVariables() {
 // Your test cases for editVariables method go here
 }

 @Test
 public void testActionPerformed_exportVariableData() {
 // Your test cases for exportVariableData method go here
 }

 @Test
 public void testActionPerformed_exit() {
 // Your test cases for exit method go here
 }

 @Test
 public void testShowDialog() {
 JFrame frame = new JFrame();
 macawWorkBench.dialog = new JDialog(frame, true);
 SwingUtilities.invokeLater(() -> macawWorkBench.dialog.setVisible(true));
 assertTrue(macawWorkBench.dialog.isVisible());
 }

 @Test
 public void testEditConstants() {
 // Your test cases for editConstants method go here
 }

 @Test
 public void testEditVariables() {
 // Your test cases for editVariables method go here
 }

 @Test
 public void testExportVariableData() {
 // Your test cases for exportVariableData method go here
 }

 @Test
 public void testExit() {
 // Your test cases for exit method go here
 }

 @After
 public void tearDown() {
 mockSessionProperties = null;
 macawWorkBench = null;
 mockUserInterfaceFactory = null;
 }

}