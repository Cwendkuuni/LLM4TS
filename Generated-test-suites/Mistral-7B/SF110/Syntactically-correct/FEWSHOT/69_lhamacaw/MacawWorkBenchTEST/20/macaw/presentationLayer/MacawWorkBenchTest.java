package macaw.presentationLayer;

import static org.junit.Assert.*;
import org.junit.Test;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import javax.swing.*;
import java.awt.*;
import macaw.io.*;
import java.awt.event.*;

public class MacawWorkBenchTest {

 // Test for main method
 @Test
 public void testMain() {
 SessionProperties sessionProperties = new SessionProperties();
 // Test with null dbUser and dbPassword
 try {
 MacawWorkBench.main(new String[0]);
 fail("Expected NumberFormatException or IllegalArgumentException, but no exception was thrown");
 } catch (Exception e) {
 // Expected exception
 }
 // Test with valid arguments
 String[] arguments = {"--dbUser", "testUser", "--dbPassword", "testPassword"};
 MacawWorkBench.main(arguments);
 // Verify that sessionProperties have been set correctly
 assertEquals("testUser", sessionProperties.getProperty("currentUser").getUsername());
 assertEquals("testPassword", sessionProperties.getProperty("currentUser").getPassword());
 }

 // Test for MacawWorkBench constructor
 @Test
 public void testMacawWorkBench() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
 // Test dialog creation
 assertNotNull(macawWorkBench.dialog);
 // Test button creation and action listeners
 ActionListener listener = mock(ActionListener.class);
 UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
 when(userInterfaceFactory.createButton(anyString())).thenReturn(mock(JButton.class));
 when(userInterfaceFactory.createPanel()).thenReturn(mock(JPanel.class));
 when(userInterfaceFactory.createImmutableTextArea(anyInt(), anyInt())).thenReturn(mock(JTextArea.class));
 MacawWorkBench macawWorkBenchSpy = spy(macawWorkBench);
 doCallRealMethod().when(macawWorkBenchSpy).createButtonPanel(userInterfaceFactory);
 macawWorkBenchSpy.dialog.getContentPane().removeAll();
 macawWorkBenchSpy.dialog.getContentPane().add(macawWorkBenchSpy.createButtonPanel(userInterfaceFactory), new GridBagConstraints());
 macawWorkBenchSpy.dialog.pack();
 macawWorkBenchSpy.show();
 macawWorkBenchSpy.editConstants.doClick();
 verify(macawWorkBenchSpy).editConstants();
 macawWorkBenchSpy.editVariables.doClick();
 verify(macawWorkBenchSpy).editVariables();
 macawWorkBenchSpy.exportVariableData.doClick();
 verify(macawWorkBenchSpy).exportVariableData();
 macawWorkBenchSpy.exit.doClick();
 verify(macawWorkBenchSpy).exit();
 }

 // Test for editConstants method
 @Test
 public void testEditConstants() {
 // Stub sessionProperties to create VariableConstantsEditor and show it
 VariableConstantsEditor variableConstantsEditor = mock(VariableConstantsEditor.class);
 when(variableConstantsEditor.show()).thenReturn(true);
 SessionProperties sessionProperties = new SessionProperties();
 sessionProperties.setProperty("userInterfaceFactory", mock(UserInterfaceFactory.class));
 when(sessionProperties.getUserInterfaceFactory()).thenReturn(sessionProperties.getProperty("userInterfaceFactory"));
 when(sessionProperties.getUserInterfaceFactory().createDialog()).thenReturn(mock(JDialog.class));
 MacawWorkBench macawWorkBench = spy(new MacawWorkBench(sessionProperties));
 doCallRealMethod().when(macawWorkBench).editConstants();
 verify(macawWorkBench).editConstants();
 verify(sessionProperties.getUserInterfaceFactory()).createDialog();
 verifyZeroInteractions(variableConstantsEditor);
 // Test with VariableConstantsEditor creation failure
 VariableConstantsEditor variableConstantsEditorFailure = mock(VariableConstantsEditor.class);
 when(variableConstantsEditorFailure.show()).thenThrow(new RuntimeException());
 sessionProperties.setProperty("userInterfaceFactory", mock(UserInterfaceFactory.class));
 macawWorkBench = spy(new MacawWorkBench(sessionProperties));
 doCallRealMethod().when(macawWorkBench).editConstants();
 verify(macawWorkBench).editConstants();
 verify(sessionProperties.getUserInterfaceFactory()).createDialog();
 verify(sessionProperties.getUserInterfaceFactory()).createDialog().setVisible(never());
 }

 // Test for editVariables method
 @Test
 public void testEditVariables() {
 // Stub sessionProperties to create MacawVariableBrowser and show it
 MacawVariableBrowser macawVariableBrowser = mock(MacawVariableBrowser.class);
 when(macawVariableBrowser.show()).thenReturn(true);
 SessionProperties sessionProperties = new SessionProperties();
 sessionProperties.setProperty("userInterfaceFactory", mock(UserInterfaceFactory.class));
 when(sessionProperties.getUserInterfaceFactory()).thenReturn(sessionProperties.getProperty("userInterfaceFactory"));
 MacawWorkBench macawWorkBench = spy(new MacawWorkBench(sessionProperties));
 doCallRealMethod().when(macawWorkBench).editVariables();
 verify(macawWorkBench).editVariables();
 verify(sessionProperties.getUserInterfaceFactory()).createDialog();
 verifyZeroInteractions(macawVariableBrowser);
 // Test with MacawVariableBrowser creation failure
 MacawVariableBrowser macawVariableBrowserFailure = mock(MacawVariableBrowser.class);
 when(macawVariableBrowserFailure.show()).thenThrow(new RuntimeException());
 sessionProperties.setProperty("userInterfaceFactory", mock(UserInterfaceFactory.class));
 macawWorkBench = spy(new MacawWorkBench(sessionProperties));
 doCallRealMethod().when(macawWorkBench).editVariables();
 verify(macawWorkBench).editVariables();
 verify(sessionProperties.getUserInterfaceFactory()).createDialog();
 verify(sessionProperties.getUserInterfaceFactory()).createDialog().setVisible(never());
 }

 // Test for exportVariableData method
 @Test
 public void testExportVariableData() {
 // Stub sessionProperties to create ExportVariablesToMacawXML and call export method
 ExportVariablesToMacawXML exportVariablesToMacawXML = mock(ExportVariablesToMacawXML.class);
 when(exportVariablesToMacawXML.export()).thenReturn(true);
 SessionProperties sessionProperties = new SessionProperties();
 sessionProperties.setProperty("userInterfaceFactory", mock(UserInterfaceFactory.class));
 when(sessionProperties.getUserInterfaceFactory()).thenReturn(sessionProperties.getProperty("userInterfaceFactory"));
 when(sessionProperties.getProperty("database")).thenReturn(mock(IDatabase.class));
 MacawWorkBench macawWorkBench = spy(new MacawWorkBench(sessionProperties));
 doCallRealMethod().when(macawWorkBench).exportVariableData();
 verify(macawWorkBench).exportVariableData();
 verify(sessionProperties.getUserInterfaceFactory()).createDialog();
 verifyZeroInteractions(exportVariablesToMacawXML);
 // Test with ExportVariablesToMacawXML creation failure
 ExportVariablesToMacawXML exportVariablesToMacawXMLFailure = mock(ExportVariablesToMacawXML.class);
 when(exportVariablesToMacawXMLFailure.export()).thenThrow(new RuntimeException());
 sessionProperties.setProperty("userInterfaceFactory", mock(UserInterfaceFactory.class));
 macawWorkBench = spy(new MacawWorkBench(sessionProperties));
 doCallRealMethod().when(macawWorkBench).exportVariableData();
 verify(macawWorkBench).exportVariableData();
 verify(sessionProperties.getUserInterfaceFactory()).createDialog();
 verify(sessionProperties.getUserInterfaceFactory()).createDialog().setVisible(never());
 }

 // Test for exit method
 @Test
 public void testExit() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
 doCallRealMethod().when(macawWorkBench).exit();
 verify(macawWorkBench.dialog).setVisible(false);
 System.exit(0);
 // Verify that system exits
 assertEquals(0, System.exit(0));
 }
}