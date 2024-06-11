package macaw.presentationLayer.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import macaw.businessLayer.VariableConstantsEditor;
import macaw.businessLayer.MacawVariableBrowser;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.production.ProductionCurationService;
import macaw.persistenceLayer.production.ProductionRetrievalService;
import macaw.persistenceLayer.demo.DemonstrationRetrievalService;
import macaw.system.SessionProperties;
import macaw.system.User;
import macaw.system.UserInterfaceFactory;
import macaw.io.ExportVariablesToMacawXML;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private UserInterfaceFactory userInterfaceFactory;
 private SessionProperties sessionProperties;
 private JFrame frame;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 macawWorkBench = new MacawWorkBench(sessionProperties);

 // Mock dependencies
 ProductionRetrievalService retrievalService = mock(ProductionRetrievalService.class);
 ProductionCurationService database = mock(ProductionCurationService.class);
 when(sessionProperties.getProperty("retrievalService")).thenReturn(retrievalService);
 when(sessionProperties.getProperty("database")).thenReturn(database);

 DemonstrationRetrievalService demonstrationRetrievalService = mock(DemonstrationRetrievalService.class);
 DemonstrationCurationService database2 = mock(DemonstrationCurationService.class);
 when(sessionProperties.getProperty("retrievalService")).thenReturn(demonstrationRetrievalService);
 when(sessionProperties.getProperty("database")).thenReturn(database2);

 User user = new User("jsmith", "cool");
 sessionProperties.setProperty("currentUser", user);

 UserInterfaceFactory spy = mock(UserInterfaceFactory.class);
 when(spy.createDialog()).thenReturn(new JDialog());
 when(spy.createPanel()).thenReturn(new JPanel());
 when(spy.createButton(anyString())).thenAnswer(invocation -> new JButton());
 when(sessionProperties.getUserInterfaceFactory()).thenReturn(spy);
 }

 @After
 public void tearDown() {
 frame.dispose();
 }

 @Test
 public void testMacawWorkBenchConstruction() {
 JDialog dialog = macawWorkBench.dialog;
 assertTrue(dialog != null);
 }

 @Test
 public void testCreateButtonPanel() {
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertTrue(buttonPanel != null);
 }

 @Test
 public void testShowDialog() {
 SwingUtilities.invokeLater(() -> {
 macawWorkBench.show();
 frame = macawWorkBench.dialog;
 assertTrue(frame.isVisible());
 });
 }

 @Test
 public void testEditConstants() {
 SwingUtilities.invokeLater(() -> macawWorkBench.editConstants());
 VariableConstantsEditor editor = mock(VariableConstantsEditor.class);
 assertTrue(editor.isShowing());
 }

 @Test
 public void testEditVariables() {
 SwingUtilities.invokeLater(() -> macawWorkBench.editVariables());
 MacawVariableBrowser browser = mock(MacawVariableBrowser.class);
 assertTrue(browser.isShowing());
 }

 @Test
 public void testExportVariableData() {
 SwingUtilities.invokeLater(() -> macawWorkBench.exportVariableData());
 ExportVariablesToMacawXML exportToDSSFormat = new ExportVariablesToMacawXML(sessionProperties);
 exportToDSSFormat.export();
 }

 @Test
 public void testExit() {
 SwingUtilities.invokeLater(() -> macawWorkBench.exit());
 assertFalse(macawWorkBench.dialog.isVisible());
 }

 @Test
 public void testActionPerformed() {
 SwingUtilities.invokeLater(() -> {
 macawWorkBench.dialog.setVisible(true);
 JButton button = macawWorkBench.editVariables;
 ActionEvent event = new ActionEvent(button, ActionEvent.ACTION_PERFORMED, "test");
 macawWorkBench.actionPerformed(event);
 });
 MacawVariableBrowser browser = mock(MacawVariableBrowser.class);
 assertTrue(browser.isShowing());
 }
}