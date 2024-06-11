package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import org.junit.Before;
import org.junit.Test;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private SessionProperties sessionProperties;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testMain() {
 MacawWorkBench.main(new String[0]);
 assertTrue(macawWorkBench instanceof MacawWorkBench);
 }

 @Test
 public void testConstructors() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 assertNotNull(macawWorkBench.dialog);
 assertNotNull(macawWorkBench.editConstants);
 assertNotNull(macawWorkBench.editVariables);
 assertNotNull(macawWorkBench.exportVariableData);
 assertNotNull(macawWorkBench.exit);
 }

 @Test
 public void testCreateButtonPanel() {
 JPanel buttonPanel = macawWorkBench.createButtonPanel(null);
 assertNotNull(buttonPanel);
 assertEquals(4, buttonPanel.getComponentCount());
 }

 @Test
 public void testShow() {
 macawWorkBench.show();
 assertTrue(macawWorkBench.dialog.isVisible());
 }

 @Test
 public void testEditConstants() {
 macawWorkBench.editConstants = mock(JButton.class);
 when(macawWorkBench.editConstants.doClick()).thenCallRealMethod();
 macawWorkBench.actionPerformed(mock(ActionEvent.class));
 verify(macawWorkBench).editConstants();
 }

 @Test
 public void testEditVariables() {
 macawWorkBench.editVariables = mock(JButton.class);
 when(macawWorkBench.editVariables.doClick()).thenCallRealMethod();
 macawWorkBench.actionPerformed(mock(ActionEvent.class));
 verify(macawWorkBench).editVariables();
 }

 @Test
 public void testExportVariableData() {
 macawWorkBench.exportVariableData = mock(JButton.class);
 when(macawWorkBench.exportVariableData.doClick()).thenCallRealMethod();
 macawWorkBench.actionPerformed(mock(ActionEvent.class));
 verify(macawWorkBench).exportVariableData();
 }

 @Test
 public void testExit() {
 macawWorkBench.exit = mock(JButton.class);
 when(macawWorkBench.exit.doClick()).thenCallRealMethod();
 macawWorkBench.actionPerformed(mock(ActionEvent.class));
 verify(macawWorkBench).exit();
 System.exit(0);
 }
}