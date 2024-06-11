package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.system.*;
import javax.swing.*;
import javax.swing.event.*;
import org.junit.Before;
import org.junit.Test;

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
 userInterfaceFactory = new UserInterfaceFactory();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 dialog = macawWorkBench.dialog;
 editConstants = macawWorkBench.editConstants;
 editVariables = macawWorkBench.editVariables;
 exportVariableData = macawWorkBench.exportVariableData;
 exit = macawWorkBench.exit;
 }

 @Test
 public void testMacawWorkBenchConstructor() {
 MacawWorkBench obj = new MacawWorkBench(sessionProperties);
 assertNotNull(obj);
 }

 @Test
 public void testMainMethod() {
 try {
 MacawWorkBench.main(new String[0]);
 fail("main method should not be called directly");
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testCreateButtonPanel() {
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertNotNull(buttonPanel);
 }

 @Test
 public void testEditConstantsMethod() {
 // This test assumes that VariableConstantsEditor and its show method are correctly implemented
 editConstants.doClick();
 assertTrue(dialog.isVisible());
 }

 @Test
 public void testEditVariablesMethod() {
 // This test assumes that MacawVariableBrowser and its show method are correctly implemented
 editVariables.doClick();
 assertTrue(dialog.isVisible());
 }

 @Test
 public void testExportVariableDataMethod() {
 // This test assumes that ExportVariablesToMacawXML and its export method are correctly implemented
 exportVariableData.doClick();
 // Add assertions to check if the exported file is created correctly
 }

 @Test(expected = IllegalStateException.class)
 public void testExitMethod() {
 exit.doClick();
 System.exit(0); // This line should not be reached
 fail("exit method should close the application");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEditConstantsMethodWithNullSessionProperties() {
 MacawWorkBench macawWorkBenchWithNullSessionProperties = new MacawWorkBench(null);
 macawWorkBenchWithNullSessionProperties.editConstants();
 fail("editConstants method should not be called with null sessionProperties");
 }

 @Test(expected = NullPointerException.class)
 public void testCreateButtonPanelWithNullUserInterfaceFactory() {
 JPanel buttonPanel = macawWorkBench.createButtonPanel(null);
 fail("createButtonPanel method should not be called with null userInterfaceFactory");
 }

 @Test(expected = NullPointerException.class)
 public void testActionPerformedWithNullEvent() {
 macawWorkBench.actionPerformed(null);
 fail("actionPerformed method should not be called with null event");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEditConstantsMethodWithEmptyStringButtonText() {
 final String emptyButtonText = "";
 macawWorkBench.editConstants = userInterfaceFactory.createButton(emptyButtonText);
 macawWorkBench.editConstants.addActionListener(macawWorkBench);
 macawWorkBench.actionPerformed(new ActionEvent(new Object(), 0, ""));
 fail("editConstants method should not be called with empty button text");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEditVariablesMethodWithEmptyStringButtonText() {
 final String emptyButtonText = "";
 macawWorkBench.editVariables = userInterfaceFactory.createButton(emptyButtonText);
 macawWorkBench.editVariables.addActionListener(macawWorkBench);
 macawWorkBench.actionPerformed(new ActionEvent(new Object(), 0, ""));
 fail("editVariables method should not be called with empty button text");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExportVariableDataMethodWithEmptyStringButtonText() {
 final String emptyButtonText = "";
 macawWorkBench.exportVariableData = userInterfaceFactory.createButton(emptyButtonText);
 macawWorkBench.exportVariableData.addActionListener(macawWorkBench);
 macawWorkBench.actionPerformed(new ActionEvent(new Object(), 0, ""));
 fail("exportVariableData method should not be called with empty button text");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExitMethodWithEmptyStringButtonText() {
 final String emptyButtonText = "";
 macawWorkBench.exit = userInterfaceFactory.createButton(emptyButtonText);
 macawWorkBench.exit.addActionListener(macawWorkBench);
 macawWorkBench.actionPerformed(new ActionEvent(new Object(), 0, ""));
 fail("exit method should not be called with empty button text");
 }
}