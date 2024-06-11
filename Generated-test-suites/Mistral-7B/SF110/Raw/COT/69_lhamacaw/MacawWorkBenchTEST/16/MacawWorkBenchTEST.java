package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private SessionProperties sessionProperties;
 private DemonstrationCurationService demonstrationCurationService;
 private ProductionCurationService productionCurationService;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 if (sessionProperties.isUseDemo()) {
 demonstrationCurationService = (DemonstrationCurationService) sessionProperties.getProperty("database");
 } else {
 productionCurationService = (ProductionCurationService) sessionProperties.getProperty("database");
 }
 }

 @Test
 public void testMacawWorkBenchConstructor() {
 MacawWorkBench expectedMacawWorkBench = new MacawWorkBench(sessionProperties);
 assertSame(expectedMacawWorkBench, macawWorkBench);
 }

 @Test
 public void testCreateButtonPanel() {
 JPanel buttonPanel = macawWorkBench.createButtonPanel(null);
 assertEquals(4, buttonPanel.getComponentCount());
 }

 @Test
 public void testEditConstantsMethod() {
 try {
 macawWorkBench.editConstants();
 } catch (Exception e) {
 fail("Edit Constants method should not throw an exception");
 }
 // Add more tests for editConstants method functionality and edge cases here
 }

 @Test
 public void testEditVariablesMethod() {
 try {
 macawWorkBench.editVariables();
 } catch (Exception e) {
 fail("Edit Variables method should not throw an exception");
 }
 // Add more tests for editVariables method functionality and edge cases here
 }

 @Test
 public void testExportVariableDataMethod() {
 try {
 macawWorkBench.exportVariableData();
 } catch (Exception e) {
 fail("Export Variable Data method should not throw an exception");
 }
 // Add more tests for exportVariableData method functionality and edge cases here
 }

 @Test
 public void testExitMethod() {
 macawWorkBench.exit();
 assertTrue(System.exit.called);
 }

 @After
 public void tearDown() {
 macawWorkBench = null;
 sessionProperties = null;
 if (sessionProperties.isUseDemo()) {
 demonstrationCurationService = null;
 } else {
 productionCurationService = null;
 }
 }

}