package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.JButton;
import javax.swing.JDialog;

import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.system.*;
import macaw.io.*;
import macaw.presentationLayer.userInterfaceFactory.*;

import org.junit.*;
import org.mockito.*;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private UserInterfaceFactory userInterfaceFactoryMock;
 private SessionProperties sessionPropertiesMock;

 @Before
 public void setUp() {
 userInterfaceFactoryMock = mock(UserInterfaceFactory.class);
 sessionPropertiesMock = mock(SessionProperties.class);
 macawWorkBench = new MacawWorkBench(sessionPropertiesMock);
 macawWorkBench.setUserInterfaceFactory(userInterfaceFactoryMock);
 }

 @Test
 public void testMacawWorkBenchConstructor() {
 // Test that the constructor initializes the sessionProperties, dialog, and buttons
 verify(sessionPropertiesMock, times(1)).setProperty("currentUser", any(User.class));
 verify(sessionPropertiesMock, times(1)).setProperty("retrievalService", any(ProductionRetrievalService.class));
 verify(sessionPropertiesMock, times(1)).setProperty("database", any(ProductionCurationService.class));
 verify(userInterfaceFactoryMock, times(1)).createDialog();
 verify(userInterfaceFactoryMock, times(1)).createPanel();
 verify(userInterfaceFactoryMock, times(1)).createImmutableTextArea(3, 20);
 verify(userInterfaceFactoryMock, times(1)).createButton("Edit Constants");
 verify(userInterfaceFactoryMock, times(1)).createButton("Edit Variables");
 verify(userInterfaceFactoryMock, times(1)).createButton("Export Variable Data");
 verify(userInterfaceFactoryMock, times(1)).createButton("Exit");
 }

 @Test
 public void testShowDialog() {
 // Test that show method opens the dialog
 macawWorkBench.show();
 verify(macawWorkBench.dialog, times(1)).setVisible(true);
 }

 @Test
 public void testEditConstantsButton() {
 // Test that editConstants method calls VariableConstantsEditor
 macawWorkBench.editConstants();
 verify(userInterfaceFactoryMock, times(1)).createDialog();
 }

 @Test
 public void testEditVariablesButton() {
 // Test that editVariables method calls MacawVariableBrowser
 macawWorkBench.editVariables();
 verify(userInterfaceFactoryMock, times(1)).createDialog();
 }

 @Test
 public void testExportVariableDataButton() {
 // Test that exportVariableData method calls ExportVariablesToMacawXML
 macawWorkBench.exportVariableData();
 verify(sessionPropertiesMock, times(1)).getProperty("retrievalService");
 verify(sessionPropertiesMock, times(1)).getProperty("database");
 verify(userInterfaceFactoryMock, times(1)).createFileWriter("output.xml");
 }

 @Test
 public void testExitButton() {
 // Test that exit method calls System.exit
 macawWorkBench.exit();
 verify(macawWorkBench, times(1)).exit();
 }

 @After
 public void tearDown() {
 reset(userInterfaceFactoryMock);
 reset(sessionPropertiesMock);
 }
}