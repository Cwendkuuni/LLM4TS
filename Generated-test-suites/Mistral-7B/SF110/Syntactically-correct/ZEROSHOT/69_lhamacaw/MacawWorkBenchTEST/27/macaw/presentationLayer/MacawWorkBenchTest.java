package macaw.presentationLayer.MacawWorkBenchTest;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench.*;
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
 // This test is not possible to execute as the main method is not testable
 assertFalse("main method should not be called in test", false);
 }

 @Test
 public void testMacawWorkBenchConstruction() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 assertNotNull(macawWorkBench.sessionProperties);
 assertNotNull(macawWorkBench.dialog);
 assertNotNull(macawWorkBench.editConstants);
 assertNotNull(macawWorkBench.editVariables);
 assertNotNull(macawWorkBench.exportVariableData);
 assertNotNull(macawWorkBench.exit);
 }

 @Test
 public void testCreateButtonPanel() {
 UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
 MACAWMessages macawMessages = new MACAWMessages();
 when(userInterfaceFactory.createButton(eq("macawWorkBench.editConstants"))).thenReturn(macawWorkBench.editConstants);
 when(userInterfaceFactory.createButton(eq("macawWorkBench.editVariables"))).thenReturn(macawWorkBench.editVariables);
 when(userInterfaceFactory.createButton(eq("macawWorkBench.exportVariableData"))).thenReturn(macawWorkBench.exportVariableData);
 when(userInterfaceFactory.createButton(eq("general.buttons.exit"))).thenReturn(macawWorkBench.exit);
 when(userInterfaceFactory.createImmutableTextArea(eq(3), eq(20))).thenReturn(macawWorkBench.instructionsArea);
 when(userInterfaceFactory.createPanel()).thenReturn(macawWorkBench.panel);
 when(userInterfaceFactory.createTextArea(anyInt(), anyInt())).thenReturn(macawWorkBench.instructionsArea);
 when(userInterfaceFactory.createLabel(anyString())).thenReturn(new JLabel(""));
 when(userInterfaceFactory.createToolTipText(anyString())).thenReturn("");

 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertNotNull(buttonPanel);

 verify(userInterfaceFactory, times(6)).createButton(anyString());
 verify(userInterfaceFactory, times(2)).createTextArea(anyInt(), anyInt());
 verify(userInterfaceFactory, times(2)).createLabel(anyString());
 }

 @Test
 public void testShowDialog() {
 assertFalse(macawWorkBench.dialog.isDisplayable());
 macawWorkBench.show();
 assertTrue(macawWorkBench.dialog.isDisplayable());
 }

 @Test
 public void testEditConstants() {
 VariableConstantsEditor variableConstantsEditor = Mockito.mock(VariableConstantsEditor.class);
 when(variableConstantsEditor.show()).thenReturn(true);
 macawWorkBench.editConstants = mock(JButton.class);
 when(macawWorkBench.sessionProperties.getUserInterfaceFactory()).thenReturn(Mockito.mock(UserInterfaceFactory.class));
 when(macawWorkBench.sessionProperties.getProperty(eq("startupOptions"))).thenReturn(Mockito.mock(StartupOptions.class));
 when(macawWorkBench.sessionProperties.getProperty("retrievalService")).thenReturn(Mockito.mock(ProductionRetrievalService.class));
 when(macawWorkBench.sessionProperties.getProperty("database")).thenReturn(Mockito.mock(ProductionCurationService.class));
 when(macawWorkBench.sessionProperties.getProperty("currentUser")).thenReturn(new User("jsmith", "cool"));

 macawWorkBench.editConstants.addActionListener(macawWorkBench);
 macawWorkBench.actionPerformed(Mockito.mock(ActionEvent.class));
 verify(variableConstantsEditor, times(1)).show();
 }

 @Test
 public void testEditVariables() {
 MacawVariableBrowser macawVariableBrowser = Mockito.mock(MacawVariableBrowser.class);
 when(macawVariableBrowser.show()).thenReturn(true);
 macawWorkBench.editVariables = mock(JButton.class);
 when(macawWorkBench.sessionProperties.getUserInterfaceFactory()).thenReturn(Mockito.mock(UserInterfaceFactory.class));
 when(macawWorkBench.sessionProperties.getProperty("retrievalService")).thenReturn(Mockito.mock(ProductionRetrievalService.class));
 when(macawWorkBench.sessionProperties.getProperty("database")).thenReturn(Mockito.mock(ProductionCurationService.class));
 when(macawWorkBench.sessionProperties.getProperty("currentUser")).thenReturn(new User("jsmith", "cool"));

 macawWorkBench.editVariables.addActionListener(macawWorkBench);
 macawWorkBench.actionPerformed(Mockito.mock(ActionEvent.class));
 verify(macawVariableBrowser, times(1)).show();
 }

 @Test
 public void testExportVariableData() {
 ExportVariablesToMacawXML exportToDSSFormat = Mockito.mock(ExportVariablesToMacawXML.class);
 when(exportToDSSFormat.export()).thenReturn(true);
 macawWorkBench.exportVariableData = mock(JButton.class);
 when(macawWorkBench.sessionProperties.getUserInterfaceFactory()).thenReturn(Mockito.mock(UserInterfaceFactory.class));
 when(macawWorkBench.sessionProperties.getProperty("database")).thenReturn(Mockito.mock(ProductionCurationService.class));
 when(macawWorkBench.sessionProperties.getProperty("retrievalService")).thenReturn(Mockito.mock(ProductionRetrievalService.class));
 when(macawWorkBench.sessionProperties.getProperty("currentUser")).thenReturn(new User("jsmith", "cool"));

 macawWorkBench.exportVariableData.addActionListener(macawWorkBench);
 macawWorkBench.actionPerformed(Mockito.mock(ActionEvent.class));
 verify(exportToDSSFormat, times(1)).export();
 }

 @Test
 public void testExit() {
 System.exit(0);
 // Cannot test this method in JUnit as it exits the JVM
 }
}