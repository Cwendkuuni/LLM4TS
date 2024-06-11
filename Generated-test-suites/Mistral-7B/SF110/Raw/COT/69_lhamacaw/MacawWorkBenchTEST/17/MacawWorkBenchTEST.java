package macaw.presentationLayer.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import macaw.businessLayer.VariableConstants;
import macaw.businessLayer.VariableConstantsEditor;
import macaw.persistanceLayer.demo.DemonstrationCurationService;
import macaw.persistanceLayer.production.ProductionCurationService;
import macaw.presentationLayer.MacawWorkBench;
import macaw.presentationLayer.MacawVariableBrowser;
import macaw.system.SessionProperties;
import macaw.io.ExportVariablesToMacawXML;

import javax.swing.JOptionPane;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private SessionProperties sessionProperties;
 private DemonstrationCurationService demonstrationCurationService;
 private ProductionCurationService productionCurationService;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 if (sessionProperties.useDemo()) {
 demonstrationCurationService = (DemonstrationCurationService) sessionProperties.getProperty("database");
 } else {
 productionCurationService = (ProductionCurationService) sessionProperties.getProperty("database");
 }
 }

 @After
 public void tearDown() {
 macawWorkBench.dispose();
 sessionProperties = null;
 macawWorkBench = null;
 }

 @Test
 public void testMacawWorkBenchConstructor() {
 MacawWorkBench mwb = new MacawWorkBench(sessionProperties);
 assertNotNull(mwb.dialog);
 assertNotNull(mwb.editConstants);
 assertNotNull(mwb.editVariables);
 assertNotNull(mwb.exportVariableData);
 assertNotNull(mwb.exit);
 }

 @Test
 public void testShowDialog() {
 macawWorkBench.show();
 assertTrue(macawWorkBench.dialog.isVisible());
 }

 @Test
 public void testEditConstants() {
 macawWorkBench.editConstants();
 assertTrue(new VariableConstantsEditor(sessionProperties, true).isOpen());
 }

 @Test
 public void testEditVariables() {
 macawWorkBench.editVariables();
 assertTrue(new MacawVariableBrowser(sessionProperties).isOpen());
 }

 @Test
 public void testExportVariableData() {
 macawWorkBench.exportVariableData();
 assertTrue(new ExportVariablesToMacawXML(sessionProperties).isExporting());
 }

 @Test
 public void testExit() {
 macawWorkBench.exit();
 assertTrue(JOptionPane.showConfirmDialog(macawWorkBench.dialog, "Do you want to save your changes before exiting?", "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.CLOSED_OPTION);
 System.exit(0);
 }

 @Test(expected = Exception.class)
 public void testExceptionHandlingMainMethod() {
 final String dbUser = null;
 final String dbPassword = null;
 final SessionProperties sessionProperties = new SessionProperties();
 new MacawWorkBench(sessionProperties);
 }

 @Test(expected = NullPointerException.class)
 public void testNullPointerExceptionInMainMethod() {
 final String dbUser = null;
 final String dbPassword = null;
 final SessionProperties sessionProperties = new SessionProperties();
 new MacawWorkBench(sessionProperties);
 sessionProperties.setProperty("retrievalService", null);
 }

 @Test
 public void testInvalidLoginAttempts() {
 final SessionProperties sessionProperties = new SessionProperties();
 final MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 assertFalse(macawWorkBench.dialog.isVisible());
 }

 @Test
 public void testDatabaseInitialization() {
 if (sessionProperties.useDemo()) {
 assertTrue(demonstrationCurationService.isInitialized());
 } else {
 assertTrue(productionCurationService.isInitialized());
 }
 }

}