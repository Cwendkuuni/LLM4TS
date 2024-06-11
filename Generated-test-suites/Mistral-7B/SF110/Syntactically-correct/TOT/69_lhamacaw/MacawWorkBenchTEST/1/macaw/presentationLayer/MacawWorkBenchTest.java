package macaw.presentationLayer.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import macaw.businessLayer.User;
import macaw.io.UserInterfaceFactory;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.demo.DemonstrationRetrievalService;
import macaw.persistenceLayer.production.ProductionCurationService;
import macaw.persistenceLayer.production.ProductionRetrievalService;
import macaw.presentationLayer.MacawWorkBench;
import macaw.presentationLayer.MacawWorkBench.ActionEventWrapper;
import macaw.system.SessionProperties;

public class MacawWorkBenchTest {

 private SessionProperties sessionProperties;
 private MacawWorkBench macawWorkBench;
 private UserInterfaceFactory userInterfaceFactory;
 private User currentUser;
 private DemonstrationCurationService database;
 private ProductionCurationService database2;
 private DemonstrationRetrievalService retrievalService;
 private ProductionRetrievalService macawRetrievalAPI;
 private ActionEventWrapper actionEventWrapper;

 @Before
 public void setUp() {
 this.sessionProperties = new SessionProperties();
 this.userInterfaceFactory = mock(UserInterfaceFactory.class);
 this.currentUser = new User("jsmith", "cool");
 this.sessionProperties.setProperty("currentUser", this.currentUser);
 this.database = mock(DemonstrationCurationService.class);
 this.database2 = mock(ProductionCurationService.class);
 this.retrievalService = mock(DemonstrationRetrievalService.class);
 this.macawRetrievalAPI = mock(ProductionRetrievalService.class);
 this.sessionProperties.setProperty("retrievalService", this.retrievalService);
 this.sessionProperties.setProperty("database", this.database);
 this.sessionProperties.setProperty("database2", this.database2);
 this.macawWorkBench = new MacawWorkBench(this.sessionProperties);
 this.macawWorkBench.userInterfaceFactory = this.userInterfaceFactory;
 this.actionEventWrapper = mock(ActionEventWrapper.class);
 }

 @Test
 public void testMain() {
 MacawWorkBench.main(new String[0]);
 }

 @Test
 public void testCreateDialog() {
 when(this.userInterfaceFactory.createDialog()).thenReturn(new JDialog());
 when(this.userInterfaceFactory.createPanel()).thenReturn(new JPanel());
 this.macawWorkBench.createButtonPanel(this.userInterfaceFactory);
 }

 @Test
 public void testCreateButtonPanel() {
 when(this.userInterfaceFactory.createButton(MacawMessages.getMessage("macawWorkBench.editConstants"))).thenReturn(new JButton());
 when(this.userInterfaceFactory.createButton(MacawMessages.getMessage("macawWorkBench.editVariables"))).thenReturn(new JButton());
 when(this.userInterfaceFactory.createButton(MacawMessages.getMessage("macawWorkBench.exportVariableData"))).thenReturn(new JButton());
 when(this.userInterfaceFactory.createButton(MacawMessages.getMessage("general.buttons.exit"))).thenReturn(new JButton());
 this.macawWorkBench.createButtonPanel(this.userInterfaceFactory);
 }

 @Test
 public void testEditConstants() {
 when(this.userInterfaceFactory.createDialog()).thenReturn(new JDialog());
 this.macawWorkBench.editConstants();
 }

 @Test
 public void testEditVariables() {
 when(this.userInterfaceFactory.createDialog()).thenReturn(new JDialog());
 this.macawWorkBench.editVariables();
 }

 @Test
 public void testExportVariableData() {
 when(this.sessionProperties.getProperty("database")).thenReturn(this.database);
 when(this.database.exportVariablesToMacawXML()).thenReturn(true);
 this.macawWorkBench.exportVariableData();
 }

 @Test
 public void testExit() {
 this.macawWorkBench.exit();
 }

 @Test(expected = Exception.class)
 public void testException() {
 MacawWorkBench.main(new String[]{"invalidArgument"});
 }

 @Test(expected = Exception.class)
 public void testInvalidUserCredentials() {
 when(this.sessionProperties.getProperty("database")).thenReturn(this.database);
 when(this.sessionProperties.getProperty("retrievalService")).thenReturn(this.retrievalService);
 when(this.retrievalService.login(this.currentUser)).thenReturn(false);
 MacawWorkBench macawWorkBench = new MacawWorkBench(this.sessionProperties);
 macawWorkBench.show();
 }

 @Test(expected = Exception.class)
 public void testInvalidDatabaseConnection() {
 when(this.sessionProperties.getProperty("database2")).thenThrow(new Exception());
 MacawWorkBench macawWorkBench = new MacawWorkBench(this.sessionProperties);
 macawWorkBench.show();
 }
}