package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.*;
import macaw.system.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private SessionProperties sessionProperties;
 private UserInterfaceFactory userInterfaceFactory;
 private DemonstrationRetrievalService demonstrationRetrievalService;
 private ProductionRetrievalService productionRetrievalService;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
 macawWorkBench = new MacawWorkBench(sessionProperties);

 if (macawWorkBench.sessionProperties.useDemo()) {
 demonstrationRetrievalService = new DemonstrationRetrievalService(sessionProperties);
 macawWorkBench.sessionProperties.setProperty("retrievalService", demonstrationRetrievalService);
 } else {
 productionRetrievalService = new ProductionRetrievalService(sessionProperties);
 macawWorkBench.sessionProperties.setProperty("retrievalService", productionRetrievalService);
 }
 }

 @Test
 public void testMacawWorkBenchConstruction() {
 assertEquals("jsmith", macawWorkBench.sessionProperties.getProperty("currentUser").getUsername());
 assertNull(macawWorkBench.dialog);
 assertNotNull(macawWorkBench.editConstants);
 assertNotNull(macawWorkBench.editVariables);
 assertNotNull(macawWorkBench.exportVariableData);
 assertNotNull(macawWorkBench.exit);
 }

 @Test
 public void testMainMethod() {
 final String dbUser = null;
 final String dbPassword = null;
 final MacawWorkBench expectedMacawWorkBench = new MacawWorkBench(sessionProperties);

 try {
 MacawWorkBench.main(new String[]{"test"});
 MacawWorkBench actualMacawWorkBench = (MacawWorkBench)sessionProperties.getProperty("macawWorkBench");
 assertEquals(expectedMacawWorkBench, actualMacawWorkBench);
 } catch (Exception e) {
 fail("Exception should not be thrown");
 }
 }

 @Test
 public void testEditConstants() {
 macawWorkBench.editConstants();
 verify(userInterfaceFactory, times(1)).createDialog();
 }

 @Test
 public void testEditVariables() {
 macawWorkBench.editVariables();
 verify(userInterfaceFactory, times(1)).createMacawVariableBrowser();
 }

 @Test
 public void testExportVariableData() {
 macawWorkBench.exportVariableData();
 if (macawWorkBench.sessionProperties.useDemo()) {
 verify(demonstrationRetrievalService, times(1)).exportVariablesToMacawXML();
 } else {
 verify(productionRetrievalService, times(1)).exportVariablesToMacawXML();
 }
 }

 @Test
 public void testExit() {
 macawWorkBench.exit();
 assertEquals(0, System.exit(0));
 }
}