package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.VariableConstants;
import macaw.businessLayer.VariableEditor;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.production.ProductionCurationService;
import macaw.presentationLayer.MacawWorkBench;
import macaw.system.SessionProperties;
import macaw.ui.UserInterfaceFactory;

import org.junit.Test;
import org.mockito.Mockito;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MacawWorkBenchTest {

 private MacawWorkBench workBench;
 private UserInterfaceFactory userInterfaceFactory;
 private SessionProperties sessionProperties;
 private DemonstrationCurationService demoDatabase;
 private ProductionCurationService productionDatabase;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
 workBench = new MacawWorkBench(sessionProperties);
 demoDatabase = Mockito.mock(DemonstrationCurationService.class);
 productionDatabase = Mockito.mock(ProductionCurationService.class);
 sessionProperties.setProperty("database", (sessionProperties.useDemo() ? demoDatabase : productionDatabase));
 }

 @Test
 public void testMacawWorkBenchCreation() {
 assertEquals("macawWorkBench.title", workBench.dialog.getTitle());
 assertNotNull(workBench.dialog);
 assertNotNull(workBench.editConstants);
 assertNotNull(workBench.editVariables);
 assertNotNull(workBench.exportVariableData);
 assertNotNull(workBench.exit);
 }

 @Test
 public void testCreateButtonPanel() {
 JPanel buttonPanel = workBench.createButtonPanel(userInterfaceFactory);
 assertNotNull(buttonPanel);
 assertEquals(4, buttonPanel.getComponentCount());
 }

 @Test
 public void testEditConstantsButton() {
 workBench.editConstants = Mockito.mock(JButton.class);
 when(userInterfaceFactory.createButton(anyString())).thenReturn(workBench.editConstants);
 workBench.editConstants.addActionListener(Mockito.mock(ActionListener.class));
 workBench.editConstants();
 verify(userInterfaceFactory, times(1)).createDialog(any(VariableConstantsEditor.class));
 }

 @Test
 public void testEditVariablesButton() {
 workBench.editVariables = Mockito.mock(JButton.class);
 when(userInterfaceFactory.createButton(anyString())).thenReturn(workBench.editVariables);
 workBench.editVariables.addActionListener(Mockito.mock(ActionListener.class));
 workBench.editVariables();
 verify(userInterfaceFactory, times(1)).createDialog(any(MacawVariableBrowser.class));
 }

 @Test
 public void testExportVariableDataButton() {
 workBench.exportVariableData = Mockito.mock(JButton.class);
 when(userInterfaceFactory.createButton(anyString())).thenReturn(workBench.exportVariableData);
 workBench.exportVariableData.addActionListener(Mockito.mock(ActionListener.class));
 workBench.exportVariableData();
 verify(sessionProperties, times(1)).getProperty("database");
 }

 @Test
 public void testExitButton() {
 workBench.exit = Mockito.mock(JButton.class);
 when(userInterfaceFactory.createButton(anyString())).thenReturn(workBench.exit);
 workBench.exit.addActionListener(Mockito.mock(ActionListener.class));
 workBench.exit();
 assertEquals(0, System.exit(0));
 }

 @Test
 public void testShowDialog() {
 workBench.show();
 assertTrue(workBench.dialog.isVisible());
 }

 @Test
 public void testActionPerformed() {
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 workBench.editConstants.addActionListener(workBench);
 workBench.editConstants.actionPerformed(actionEvent);
 workBench.editConstants();
 verify(userInterfaceFactory, times(1)).createDialog(any(VariableConstantsEditor.class));
 }

 @Test
 public void testActionPerformed_Variables() {
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 workBench.editVariables.addActionListener(workBench);
 workBench.editVariables.actionPerformed(actionEvent);
 workBench.editVariables();
 verify(userInterfaceFactory, times(1)).createDialog(any(MacawVariableBrowser.class));
 }

 @Test
 public void testActionPerformed_Export() {
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 workBench.exportVariableData.addActionListener(workBench);
 workBench.exportVariableData.actionPerformed(actionEvent);
 workBench.exportVariableData();
 verify(sessionProperties, times(1)).getProperty("database");
 }

 @Test
 public void testActionPerformed_Exit() {
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 workBench.exit.addActionListener(workBench);
 workBench.exit.actionPerformed(actionEvent);
 assertEquals(0, System.exit(0));
 }
}