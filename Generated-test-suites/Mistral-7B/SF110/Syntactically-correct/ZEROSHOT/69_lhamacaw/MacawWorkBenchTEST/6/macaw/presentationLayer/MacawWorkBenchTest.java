package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.*;
import macaw.presentationLayer.*;
import macaw.system.*;
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
 public void testMacawWorkBenchConstruction() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 assertNotNull(macawWorkBench.dialog);
 assertNotNull(macawWorkBench.editConstants);
 assertNotNull(macawWorkBench.editVariables);
 assertNotNull(macawWorkBench.exportVariableData);
 assertNotNull(macawWorkBench.exit);
 }

 @Test
 public void testCreateButtonPanel() {
 UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertNotNull(buttonPanel);
 }

 @Test
 public void testEditConstants() {
 when(sessionProperties.getUserInterfaceFactory()).thenReturn(mock(UserInterfaceFactory.class));
 macawWorkBench.editConstants();
 verify(sessionProperties).getProperty("currentUser");
 }

 @Test
 public void testEditVariables() {
 when(sessionProperties.getUserInterfaceFactory()).thenReturn(mock(UserInterfaceFactory.class));
 macawWorkBench.editVariables();
 verify(sessionProperties).getProperty("retrievalService");
 }

 @Test
 public void testExportVariableData() {
 when(sessionProperties.getUserInterfaceFactory()).thenReturn(mock(UserInterfaceFactory.class));
 when(sessionProperties.getProperty("retrievalService")).thenReturn(mock(RetrievalService.class));
 macawWorkBench.exportVariableData();
 verify(sessionProperties.getProperty("retrievalService")).exportVariablesToXML();
 }

 @Test
 public void testExit() {
 macawWorkBench.exit();
 assertTrue(System.exit(0) == 0);
 }
}