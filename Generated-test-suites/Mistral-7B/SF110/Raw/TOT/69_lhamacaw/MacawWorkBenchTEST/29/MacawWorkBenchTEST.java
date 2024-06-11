package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;

import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.*;
import macaw.system.*;
import macaw.io.*;
import org.junit.*;
import org.mockito.MockitoAnnotations;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private UserInterfaceFactory userInterfaceFactory;
 private SessionProperties sessionProperties;
 private ProductionRetrievalService retrievalService;
 private ProductionCurationService database;

 @Before
 public void setUp() {
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 sessionProperties = new SessionProperties();
 retrievalService = mock(ProductionRetrievalService.class);
 database = mock(ProductionCurationService.class);
 when(userInterfaceFactory.createButton(anyString())).thenAnswer(invocation -> invocation.getArguments()[0]);
 when(userInterfaceFactory.createButton(anyString())).thenReturn(mock(JButton.class));
 when(userInterfaceFactory.createDialog()).thenReturn(mock(JDialog.class));
 when(userInterfaceFactory.createImmutableTextArea(1, 1)).thenReturn(mock(JTextArea.class));
 when(userInterfaceFactory.createPanel()).thenReturn(mock(JPanel.class));
 when(sessionProperties.getUserInterfaceFactory()).thenReturn(userInterfaceFactory);
 when(sessionProperties.getProperty("retrievalService")).thenReturn(retrievalService);
 when(sessionProperties.getProperty("database")).thenReturn(database);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testMacawWorkBenchCreation() {
 assertNotNull(macawWorkBench);
 }

 @Test
 public void testShowDialog() {
 macawWorkBench.show();
 verify(userInterfaceFactory, times(1)).createDialog();
 }

 @Test
 public void testEditConstantsButtonClick() {
 macawWorkBench.actionPerformed(mock(ActionEvent.class));
 verify(userInterfaceFactory, times(1)).createButton("Edit Constants");
 macawWorkBench.editConstants();
 verify(userInterfaceFactory, times(1)).createDialog();
 }

 @Test
 public void testEditVariablesButtonClick() {
 macawWorkBench.actionPerformed(mock(ActionEvent.class));
 verify(userInterfaceFactory, times(1)).createButton("Edit Variables");
 macawWorkBench.editVariables();
 verify(retrievalService, times(1)).retrieveVariables();
 }

 @Test
 public void testExportVariableDataButtonClick() {
 macawWorkBench.actionPerformed(mock(ActionEvent.class));
 verify(userInterfaceFactory, times(1)).createButton("Export Variable Data");
 macawWorkBench.exportVariableData();
 verify(database, times(1)).exportVariablesToMacawXML();
 }

 @Test
 public void testExitButtonClick() {
 macawWorkBench.actionPerformed(mock(ActionEvent.class));
 verify(userInterfaceFactory, times(1)).createButton("Exit");
 }

 @After
 public void tearDown() {
 MockitoAnnotations.initMocks(this);
 }

}