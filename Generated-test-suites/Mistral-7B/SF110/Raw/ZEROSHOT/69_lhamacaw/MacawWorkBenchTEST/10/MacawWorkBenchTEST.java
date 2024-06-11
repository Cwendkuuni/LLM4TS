package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.system.SessionProperties;
import macaw.ui.UserInterfaceFactory;
import org.junit.Before;
import org.junit.Test;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private UserInterfaceFactory userInterfaceFactory;
 private SessionProperties sessionProperties;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = new UserInterfaceFactory(sessionProperties);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testMain() {
 MacawWorkBench.main(new String[0]);
 assertTrue(macawWorkBench != null);
 }

 @Test
 public void testConstructor() {
 assertEquals("macawWorkBench.title", macawWorkBench.dialog.getTitle());
 }

 @Test
 public void testCreateButtonPanel() {
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertNotNull(buttonPanel);
 assertNotNull(macawWorkBench.editConstants);
 assertNotNull(macawWorkBench.editVariables);
 assertNotNull(macawWorkBench.exportVariableData);
 assertNotNull(macawWorkBench.exit);
 }

 @Test
 public void testShowDialog() {
 macawWorkBench.show();
 assertTrue(macawWorkBench.dialog.isVisible());
 }

 @Test
 public void testEditConstants() {
 macawWorkBench.editConstants();
 assertTrue(sessionProperties.getProperty("variableConstantsEditor") != null);
 }

 @Test
 public void testEditVariables() {
 macawWorkBench.editVariables();
 assertTrue(sessionProperties.getProperty("macawVariableBrowser") != null);
 }

 @Test
 public void testExportVariableData() {
 macawWorkBench.exportVariableData();
 assertTrue(sessionProperties.getProperty("exportToDSSFormat") != null);
 }

 @Test
 public void testExit() {
 macawWorkBench.exit();
 assertFalse(macawWorkBench.dialog.isVisible());
 }

 @Test
 public void testActionPerformed() {
 macawWorkBench.editConstants();
 macawWorkBench.actionPerformed(new ActionEvent(macawWorkBench.editConstants, ActionEvent.ACTION_PERFORMED, "editConstants"));
 assertTrue(sessionProperties.getProperty("variableConstantsEditor") != null);
 }
}