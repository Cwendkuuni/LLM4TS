package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;

import macaw.businessLayer.VariableConstants;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.demo.DemonstrationRetrievalService;
import macaw.persistenceLayer.production.ProductionCurationService;
import macaw.persistenceLayer.production.ProductionRetrievalService;
import macaw.presentationLayer.MacawWorkBench;
import macaw.presentationLayer.UserInterfaceFactory;
import macaw.system.SessionProperties;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private UserInterfaceFactory userInterfaceFactoryMock;
 private SessionProperties sessionPropertiesMock;
 private DemonstrationRetrievalService demonstrationRetrievalServiceMock;
 private ProductionRetrievalService productionRetrievalServiceMock;
 private DemonstrationCurationService demonstrationCurationServiceMock;
 private ProductionCurationService productionCurationServiceMock;

 @Before
 public void setUp() {
 userInterfaceFactoryMock = mock(UserInterfaceFactory.class);
 sessionPropertiesMock = mock(SessionProperties.class);
 demonstrationRetrievalServiceMock = mock(DemonstrationRetrievalService.class);
 productionRetrievalServiceMock = mock(ProductionRetrievalService.class);
 demonstrationCurationServiceMock = mock(DemonstrationCurationService.class);
 productionCurationServiceMock = mock(ProductionCurationService.class);
 when(sessionPropertiesMock.getUserInterfaceFactory()).thenReturn(userInterfaceFactoryMock);
 when(sessionPropertiesMock.getProperty("retrievalService")).thenReturn(demonstrationRetrievalServiceMock);
 when(sessionPropertiesMock.getProperty("database")).thenReturn(demonstrationCurationServiceMock);
 when(sessionPropertiesMock.getProperty("retrievalService")).thenReturn(productionRetrievalServiceMock);
 when(sessionPropertiesMock.getProperty("database")).thenReturn(productionCurationServiceMock);
 macawWorkBench = new MacawWorkBench(sessionPropertiesMock);
 }

 @Test
 public void testMacawWorkBenchConstruction() {
 assertNotNull(macawWorkBench);
 }

 @Test
 public void testCreateButtonPanel() {
 UserInterfaceFactory userInterfaceFactorySpy = spy(userInterfaceFactoryMock);
 MacawWorkBench macawWorkBenchSpy = spy(macawWorkBench);
 MacawWorkBench.JPanel createButtonPanel = macawWorkBenchSpy.createButtonPanel(userInterfaceFactorySpy);
 assertNotNull(createButtonPanel);
 verify(userInterfaceFactorySpy, atLeastOnce()).createPanel();
 }

 @Test
 public void testShowDialog() {
 MacawWorkBench macawWorkBenchSpy = spy(macawWorkBench);
 macawWorkBenchSpy.show();
 verify(macawWorkBenchSpy.dialog, atLeastOnce()).setVisible(true);
 }

 @Test
 public void testEditConstantsButton_whenClicked_callsEditConstantsMethod() {
 ActionEvent editConstantsButtonMock = mock(ActionEvent.class);
 macawWorkBench.editConstants(editConstantsButtonMock);
 verify(macawWorkBench).editConstants();
 }

 @Test
 public void testEditVariablesButton_whenClicked_callsEditVariablesMethod() {
 ActionEvent editVariablesButtonMock = mock(ActionEvent.class);
 macawWorkBench.editVariables(editVariablesButtonMock);
 verify(macawWorkBench).editVariables();
 }

 @Test
 public void testExportVariableDataButton_whenClicked_callsExportVariableDataMethod() {
 ActionEvent exportVariableDataButtonMock = mock(ActionEvent.class);
 macawWorkBench.exportVariableData(exportVariableDataButtonMock);
 verify(macawWorkBench).exportVariableData();
 }

 @Test
 public void testExitButton_whenClicked_callsExitMethod() {
 ActionEvent exitButtonMock = mock(ActionEvent.class);
 macawWorkBench.exit(exitButtonMock);
 verify(macawWorkBench).exit();
 System.out.println("Test Exit Button: System.exit(0) called.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedMethod_whenCalled_throwsUnsupportedOperationException() {
 macawWorkBench.unsupportedMethod();
 }

 @After
 public void tearDown() {
 macawWorkBench = null;
 userInterfaceFactoryMock = null;
 sessionPropertiesMock = null;
 demonstrationRetrievalServiceMock = null;
 productionRetrievalServiceMock = null;
 demonstrationCurationServiceMock = null;
 productionCurationServiceMock = null;
 }

}