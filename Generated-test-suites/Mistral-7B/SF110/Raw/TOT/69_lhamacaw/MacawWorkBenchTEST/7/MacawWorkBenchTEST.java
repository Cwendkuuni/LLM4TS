package macaw.presentationLayer.test;

import static org.junit.Assert.*;
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
 private UserInterfaceFactory userInterfaceFactory;
 private SessionProperties sessionProperties;
 private DemonstrationRetrievalService demonstrationRetrievalService;
 private ProductionRetrievalService productionRetrievalService;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = new UserInterfaceFactory();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 if (sessionProperties.useDemo()) {
 demonstrationRetrievalService = new DemonstrationRetrievalService(sessionProperties);
 sessionProperties.setProperty("retrievalService", demonstrationRetrievalService);
 sessionProperties.setProperty("database", new DemonstrationCurationService(false));
 } else {
 productionRetrievalService = new ProductionRetrievalService(sessionProperties);
 sessionProperties.setProperty("retrievalService", productionRetrievalService);
 sessionProperties.setProperty("database", new ProductionCurationService(sessionProperties));
 }
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
 public void testEditConstantsButton() {
 macawWorkBench.editConstants();
 UserInterfaceFactory uiFactorySpy = new SpyUserInterfaceFactory();
 sessionProperties.setProperty("userInterfaceFactory", uiFactorySpy);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 assertTrue(uiFactorySpy.variableConstantsEditorCalled);
 }

 @Test
 public void testEditVariablesButton() {
 macawWorkBench.editVariables();
 UserInterfaceFactory uiFactorySpy = new SpyUserInterfaceFactory();
 sessionProperties.setProperty("userInterfaceFactory", uiFactorySpy);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 assertTrue(uiFactorySpy.macawVariableBrowserCalled);
 }

 @Test
 public void testExportVariableDataButton() {
 macawWorkBench.exportVariableData();
 assertTrue(productionRetrievalService.exportVariablesCalled);
 }

 @Test
 public void testExitButton() {
 macawWorkBench.exit();
 assertEquals(0, System.exit(0));
 }

 @Test
 public void testEditConstantsButtonNullDB() {
 macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.editConstants();
 assertNull(sessionProperties.getProperty("database"));
 }

 @Test
 public void testEditConstantsButtonNullRetrievalService() {
 sessionProperties.setProperty("database", new DemonstrationCurationService(false));
 macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.editConstants();
 assertNull(sessionProperties.getProperty("retrievalService"));
 }

 @Test(expected = NullPointerException.class)
 public void testEditConstantsButtonWithoutUserInterfaceFactory() {
 sessionProperties.setProperty("userInterfaceFactory", null);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.editConstants();
 }

 @Test(expected = NullPointerException.class)
 public void testEditConstantsButtonWithoutSessionProperties() {
 macawWorkBench = new MacawWorkBench(null);
 macawWorkBench.editConstants();
 }

 @Test
 public void testEditConstantsButtonWithNoLogin() {
 sessionProperties.setProperty("currentUser", new User("jsmith", "cool"));
 sessionProperties.setProperty("retrievalService", new DemonstrationRetrievalService(sessionProperties));
 sessionProperties.setProperty("database", new DemonstrationCurationService(false));
 macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.editConstants();
 assertTrue(sessionProperties.getLoginDialog().isLoginSuccessful());
 }

 @Test
 public void testEditConstantsButtonWithFailedLogin() {
 sessionProperties.setProperty("currentUser", new User("jsmith", "wrongPassword"));
 sessionProperties.setProperty("retrievalService", new DemonstrationRetrievalService(sessionProperties));
 sessionProperties.setProperty("database", new DemonstrationCurationService(false));
 macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.editConstants();
 assertFalse(sessionProperties.getLoginDialog().isLoginSuccessful());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEditVariablesButtonWithNoSessionProperties() {
 MacawVariableBrowser macawVariableBrowser = new MacawVariableBrowser(null);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.editVariables();
 macawWorkBench.show();
 macawVariableBrowser.show();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEditVariablesButtonWithNoRetrievalService() {
 sessionProperties.setProperty("database", new DemonstrationCurationService(false));
 MacawVariableBrowser macawVariableBrowser = new MacawVariableBrowser(sessionProperties);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.editVariables();
 macawWorkBench.show();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEditVariablesButtonWithNoDatabase() {
 sessionProperties.setProperty("database", null);
 sessionProperties.setProperty("retrievalService", new ProductionRetrievalService(sessionProperties));
 MacawVariableBrowser macawVariableBrowser = new MacawVariableBrowser(sessionProperties);
 macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.editVariables();
 macawWorkBench.show();
 }

 @Test(expected = NullPointerException.class)
 public void testExportVariableDataButtonWithNullRetrievalService() {
 macawWorkBench = new MacawWorkBench(sessionProperties);
 sessionProperties.setProperty("retrievalService", null);
 macawWorkBench.exportVariableData();
 }

 @Test(expected = NullPointerException.class)
 public void testExportVariableDataButtonWithNullDatabase() {
 macawWorkBench = new MacawWorkBench(sessionProperties);
 sessionProperties.setProperty("database", null);
 macawWorkBench.exportVariableData();
 }

 @Test(expected = NullPointerException.class)
 public void testExitButtonWithNullSessionProperties() {
 macawWorkBench = new MacawWorkBench(null);
 macawWorkBench.exit();
 }
}