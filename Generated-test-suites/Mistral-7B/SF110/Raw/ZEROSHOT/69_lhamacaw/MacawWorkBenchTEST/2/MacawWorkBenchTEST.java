package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.*;
import macaw.presentationLayer.*;
import org.junit.*;
import javax.swing.*;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private SessionProperties sessionProperties;
 private UserInterfaceFactory userInterfaceFactory;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = new DefaultUserInterfaceFactory();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testMacawWorkBenchConstructor() {
 assertNotNull(macawWorkBench);
 assertEquals("macawWorkBench.title", macawWorkBench.dialog.getTitle());
 }

 @Test
 public void testCreateButtonPanel() {
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertEquals("2", String.valueOf(buttonPanel.getComponentCount()));
 }

 @Test
 public void testEditConstantsButton() {
 ActionEvent editConstantsEvent = new ActionEvent(macawWorkBench.editConstants, ActionEvent.ACTION_PERFORMED, "Edit Constants");
 macawWorkBench.actionPerformed(editConstantsEvent);
 verify(macawWorkBench).editConstants();
 }

 @Test
 public void testEditVariablesButton() {
 ActionEvent editVariablesEvent = new ActionEvent(macawWorkBench.editVariables, ActionEvent.ACTION_PERFORMED, "Edit Variables");
 macawWorkBench.actionPerformed(editVariablesEvent);
 verify(macawWorkBench).editVariables();
 }

 @Test
 public void testExportVariableDataButton() {
 ActionEvent exportVariableDataEvent = new ActionEvent(macawWorkBench.exportVariableData, ActionEvent.ACTION_PERFORMED, "Export Variable Data");
 macawWorkBench.actionPerformed(exportVariableDataEvent);
 verify(macawWorkBench).exportVariableData();
 }

 @Test
 public void testExitButton() {
 ActionEvent exitEvent = new ActionEvent(macawWorkBench.exit, ActionEvent.ACTION_PERFORMED, "Exit");
 macawWorkBench.actionPerformed(exitEvent);
 verify(macawWorkBench).exit();
 }

 @After
 public void tearDown() {
 macawWorkBench = null;
 sessionProperties = null;
 userInterfaceFactory = null;
 }

}