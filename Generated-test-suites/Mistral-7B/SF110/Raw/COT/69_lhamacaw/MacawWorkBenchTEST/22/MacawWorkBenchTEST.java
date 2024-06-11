package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.*;
import macaw.system.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private SessionProperties sessionProperties;
 private UserInterfaceFactory userInterfaceFactory;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 sessionProperties = new SessionProperties();
 userInterfaceFactory = new UserInterfaceFactory();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testMacawWorkBenchConstructor() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 assertNotNull(macawWorkBench.dialog);
 assertNotNull(macawWorkBench.editConstants);
 assertNotNull(macawWorkBench.editVariables);
 assertNotNull(macawWorkBench.exportVariableData);
 assertNotNull(macawWorkBench.exit);
 }

 @Test
 public void testShow() {
 macawWorkBench.show();
 assertTrue(macawWorkBench.dialog.isVisible());
 }

 @Test
 public void testEditConstants() {
 macawWorkBench.editConstants();
 // Add assertions here to check if the VariableConstantsEditor is displayed correctly
 }

 @Test
 public void testEditVariables() {
 macawWorkBench.editVariables();
 // Add assertions here to check if the MacawVariableBrowser is displayed correctly
 }

 @Test
 public void testExportVariableData() {
 macawWorkBench.exportVariableData();
 // Add assertions here to check if the ExportVariablesToMacawXML is executed correctly
 }

 @Test(expected = Exception.class)
 public void testExceptionDuringLogin() {
 final LoginDialog loginDialog = new LoginDialog(sessionProperties, 1);
 loginDialog.show();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.show();
 // Add assertions here to check if the expected exception is thrown
 }

 @Test
 public void testUseDemoDatabase() {
 sessionProperties.setProperty("useDemo", true);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 assertNotNull(macawWorkBench.sessionProperties.getProperty("database"));
 assertInstanceOf(DemonstrationCurationService.class, macawWorkBench.sessionProperties.getProperty("database"));
 }

 @Test
 public void testUseProductionDatabase() {
 sessionProperties.setProperty("useDemo", false);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 assertNotNull(macawWorkBench.sessionProperties.getProperty("database"));
 assertInstanceOf(ProductionCurationService.class, macawWorkBench.sessionProperties.getProperty("database"));
 }

}