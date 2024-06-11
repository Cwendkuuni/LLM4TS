package macaw.presentationLayer.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.event.ActionEvent;

import macaw.businessLayer.ISessionProperties;
import macaw.io.UserInterfaceFactory;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.production.ProductionCurationService;
import macaw.presentationLayer.MacawWorkBench;
import macaw.presentationLayer.MacawWorkBench.SessionProperties;
import macaw.system.StartupOptions;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MacawWorkBenchTest {

 private MacawWorkBench workBench;
 private UserInterfaceFactory userInterfaceFactory;
 private ISessionProperties sessionProperties;
 private StartupOptions startupOptions;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = new UserInterfaceFactory(sessionProperties);
 startupOptions = new StartupOptions();
 }

 @After
 public void tearDown() {
 workBench = null;
 sessionProperties = null;
 userInterfaceFactory = null;
 }

 @Test
 public void testMacawWorkBenchCreation() {
 workBench = new MacawWorkBench(sessionProperties);
 assertNotNull(workBench);
 }

 @Test
 public void testMainMethod() {
 try {
 MacawWorkBench.main(new String[] {});
 assertTrue(true);
 } catch (Exception e) {
 fail("Exception was thrown during main method call");
 }
 }

 @Test
 public void testEmptyDatabase() {
 sessionProperties.setProperty("useDemo", true);
 MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
 assertNull(workBench.getDatabase());
 assertNull(workBench.getRetrievalService());
 }

 @Test
 public void testDemoDatabase() {
 sessionProperties.setProperty("useDemo", true);
 MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
 DemonstrationCurationService db = (DemonstrationCurationService) workBench.getDatabase();
 assertNotNull(db);
 }

 @Test
 public void testProductionDatabase() {
 sessionProperties.setProperty("useDemo", false);
 MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
 ProductionCurationService db = (ProductionCurationService) workBench.getDatabase();
 assertNotNull(db);
 }

 @Test
 public void testShowDialog() {
 workBench = new MacawWorkBench(sessionProperties);
 workBench.show();
 assertTrue(workBench.dialog.isVisible());
 }

 @Test
 public void testCloseDialog() {
 workBench = new MacawWorkBench(sessionProperties);
 workBench.show();
 workBench.exit.doClick();
 assertFalse(workBench.dialog.isVisible());
 }

 @Test
 public void testEditConstantsButton() {
 workBench = new MacawWorkBench(sessionProperties);
 workBench.show();
 workBench.editConstants.doClick();
 assertNotNull(workBench.sessionProperties.getVariableConstantsEditor());
 }

 @Test
 public void testEditVariablesButton() {
 workBench = new MacawWorkBench(sessionProperties);
 workBench.show();
 workBench.editVariables.doClick();
 assertNotNull(workBench.sessionProperties.getMacawVariableBrowser());
 }

 @Test
 public void testExportVariableDataButton() {
 workBench = new MacawWorkBench(sessionProperties);
 workBench.show();
 workBench.exportVariableData.doClick();
 // We cannot assert anything here since the method export() is not mocked
}

 @Test(expected = NullPointerException.class)
 public void testEditConstantsWhenNoSessionProperties() {
 sessionProperties = null;
 MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
 workBench.editConstants.doClick();
 }
}