package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;

import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.*;
import macaw.presentationLayer.userInterfaceFactory.*;
import macaw.system.*;
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
 public void testMainMethod() {
 // Test the main method with demo database
 given(sessionPropertiesMock.getProperty("startupOptions")).willReturn(new StartupOptions(true));
 given(sessionPropertiesMock.getUserInterfaceFactory()).willReturn(userInterfaceFactoryMock);
 given(userPropertiesMock.getSessionProperties()).willReturn(sessionPropertiesMock);
 MacawWorkBench.main(new String[0]);
 verify(sessionPropertiesMock, times(1)).setProperty("retrievalService", instanceOf(DemonstrationRetrievalService.class));
 verify(sessionPropertiesMock, times(1)).setProperty("database", instanceOf(DemonstrationCurationService.class));

 // Test the main method with production database
 given(sessionPropertiesMock.getProperty("startupOptions")).willReturn(new StartupOptions(false));
 given(sessionPropertiesMock.getUserInterfaceFactory()).willReturn(userInterfaceFactoryMock);
 given(userPropertiesMock.getSessionProperties()).willReturn(sessionPropertiesMock);
 MacawWorkBench.main(new String[0]);
 verify(sessionPropertiesMock, times(1)).setProperty("retrievalService", instanceOf(ProductionRetrievalService.class));
 verify(sessionPropertiesMock, times(1)).setProperty("database", instanceOf(ProductionCurationService.class));
 }

 @Test
 public void testMacawWorkBenchConstructor() {
 macawWorkBench = new MacawWorkBench(sessionPropertiesMock);
 assertNotNull(macawWorkBench.sessionProperties);
 assertNotNull(macawWorkBench.dialog);
 assertNotNull(macawWorkBench.editConstants);
 assertNotNull(macawWorkBench.editVariables);
 assertNotNull(macawWorkBench.exportVariableData);
 assertNotNull(macawWorkBench.exit);
 }

 @Test
 public void testCreateButtonPanel() {
 final JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactoryMock);
 assertNotNull(buttonPanel);
 verify(userInterfaceFactoryMock, times(3)).createButton(anyString());
 }

 @Test
 public void testShowDialog() {
 macawWorkBench.show();
 verify(dialogMock, times(1)).setVisible(true);
 }

 @Test
 public void testEditConstants() {
 macawWorkBench.editConstants();
 verify(userInterfaceFactoryMock, times(1)).createVariableConstantsEditor(sessionPropertiesMock, true);
 }

 @Test
 public void testEditVariables() {
 macawWorkBench.editVariables();
 verify(userInterfaceFactoryMock, times(1)).createMacawVariableBrowser(sessionPropertiesMock);
 }

 @Test
 public void testExportVariableData() {
 macawWorkBench.exportVariableData();
 verify(sessionPropertiesMock, times(1)).setProperty("exportToDSSFormat", instanceOf(ExportVariablesToMacawXML.class));
 macawWorkBench.exportVariableData();
 verify(exportToDSSFormatMock, times(1)).export();
 }

 @Test
 public void testExit() {
 macawWorkBench.exit();
 verify(sessionPropertiesMock, times(1)).setProperty("exportToDSSFormat", null);
 verify(sessionPropertiesMock, times(1)).setProperty("database", null);
 verify(sessionPropertiesMock, times(1)).setProperty("retrievalService", null);
 verify(userPropertiesMock, times(1)).setProperty("currentUser", null);
 verify(sessionPropertiesMock, times(1)).setProperty("startupOptions", null);
 verify(sessionPropertiesMock, times(1)).setProperty("sessionProperties", null);
 System.exit(0);
 }

 @After
 public void tearDown() {
 reset(sessionPropertiesMock);
 reset(userInterfaceFactoryMock);
 reset(dialogMock);
 reset(exportToDSSFormatMock);
 }
}