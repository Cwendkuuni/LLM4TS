package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.event.ActionEvent;

import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.presentationLayer.UserInterfaceFactory;
import macaw.system.MacawMessages;

import org.junit.Before;
import org.junit.Test;

public class MacawWorkBenchTest {

 private MacawWorkBench workBench;
 private UserInterfaceFactory userInterfaceFactory;
 private SessionProperties sessionProperties;
 private DemonstrationCurationService database;
 private ProductionCurationService database2;

 @Before
 public void setup() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 when(userInterfaceFactory.createButton(anyString())).thenReturn(mock(JButton.class));
 when(userInterfaceFactory.createDialog()).thenReturn(mock(JDialog.class));
 when(userInterfaceFactory.createImmutableTextArea(anyInt(), anyInt())).thenReturn(mock(JTextArea.class));
 when(userInterfaceFactory.createPanel()).thenReturn(mock(JPanel.class));

 if (sessionProperties.useDemo()) {
 database = new DemonstrationCurationService(false);
 } else {
 database2 = new ProductionCurationService(sessionProperties);
 }
 sessionProperties.setProperty("database", database);
 sessionProperties.setProperty("retrievalService", new DemonstrationRetrievalService(sessionProperties));
 workBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testEditConstants() {
 workBench.editConstants();
 // Add assertions for the expected behavior of editConstants()
 }

 @Test
 public void testEditVariables() {
 workBench.editVariables();
 // Add assertions for the expected behavior of editVariables()
 }

 @Test
 public void testExportVariableData() {
 workBench.exportVariableData();
 // Add assertions for the expected behavior of exportVariableData()
 }

 @Test
 public void testActionPerformed() {
 final ActionEvent actionEvent = new ActionEvent(workBench.editConstants, ActionEvent.ACTION_PERFORMED, "actionPerformed");
 workBench.actionPerformed(actionEvent);
 // Add assertions for the expected behavior of actionPerformed() when editConstants button is clicked

 final ActionEvent actionEvent2 = new ActionEvent(workBench.editVariables, ActionEvent.ACTION_PERFORMED, "actionPerformed");
 workBench.actionPerformed(actionEvent2);
 // Add assertions for the expected behavior of actionPerformed() when editVariables button is clicked

 final ActionEvent actionEvent3 = new ActionEvent(workBench.exportVariableData, ActionEvent.ACTION_PERFORMED, "actionPerformed");
 workBench.actionPerformed(actionEvent3);
 // Add assertions for the expected behavior of actionPerformed() when exportVariableData button is clicked

 final ActionEvent actionEvent4 = new ActionEvent(workBench.exit, ActionEvent.ACTION_PERFORMED, "actionPerformed");
 workBench.actionPerformed(actionEvent4);
 // Add assertions for the expected behavior of actionPerformed() when exit button is clicked
 }

}