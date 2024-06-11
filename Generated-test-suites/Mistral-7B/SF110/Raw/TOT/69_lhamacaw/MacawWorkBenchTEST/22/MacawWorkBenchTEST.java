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
import org.mockito.*;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private SessionProperties sessionProperties;
 private UserInterfaceFactory userInterfaceFactory;
 private DemonstrationRetrievalService demonstrationRetrievalService;
 private ProductionRetrievalService productionRetrievalService;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 demonstrationRetrievalService = mock(DemonstrationRetrievalService.class);
 productionRetrievalService = mock(ProductionRetrievalService.class);
 when(sessionProperties.getUserInterfaceFactory()).thenReturn(userInterfaceFactory);
 when(sessionProperties.getRetrievalService()).thenReturn(demonstrationRetrievalService);
 when(sessionProperties.getProperty("database")).thenReturn(new DemonstrationCurationService(false));
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testMain() {
 MacawWorkBench.main(new String[0]);
 verify(sessionProperties, times(1)).setProperty("retrievalService", demonstrationRetrievalService);
 verify(sessionProperties, times(1)).setProperty("database", new DemonstrationCurationService(false));
 }

 @Test
 public void testMacawWorkBenchConstructor() {
 macawWorkBench = new MacawWorkBench(sessionProperties);
 assertNotNull(macawWorkBench.dialog);
 }

 @Test
 public void testCreateButtonPanel() {
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertNotNull(buttonPanel);
 }

 @Test
 public void testEditConstantsButtonClick() {
 ActionEvent editConstantsEvent = mock(ActionEvent.class);
 when(userInterfaceFactory.createButton(eq("macawWorkBench.editConstants"))).thenReturn(mock(JButton.class));
 macawWorkBench.editConstants();
 verify(userInterfaceFactory, times(1)).createButton(eq("macawWorkBench.editConstants"));
 }

 @Test
 public void testEditVariablesButtonClick() {
 ActionEvent editVariablesEvent = mock(ActionEvent.class);
 when(userInterfaceFactory.createButton(eq("macawWorkBench.editVariables"))).thenReturn(mock(JButton.class));
 macawWorkBench.editVariables();
 verify(userInterfaceFactory, times(1)).createButton(eq("macawWorkBench.editVariables"));
 }

 @Test
 public void testExportVariableDataButtonClick() {
 ActionEvent exportVariableDataEvent = mock(ActionEvent.class);
 when(userInterfaceFactory.createButton(eq("macawWorkBench.exportVariableData"))).thenReturn(mock(JButton.class));
 macawWorkBench.exportVariableData();
 verify(userInterfaceFactory, times(1)).createButton(eq("macawWorkBench.exportVariableData"));
 }

 @Test
 public void testExitButtonClick() {
 ActionEvent exitEvent = mock(ActionEvent.class);
 when(userInterfaceFactory.createButton(eq("general.buttons.exit"))).thenReturn(mock(JButton.class));
 macawWorkBench.exit();
 verify(userInterfaceFactory, times(1)).createButton(eq("general.buttons.exit"));
 System.exit(0);
 }
}