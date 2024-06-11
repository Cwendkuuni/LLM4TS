package macaw.presentationLayer;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import javax.swing.*;
import java.awt.*;
import macaw.io.*;
import org.junit.Test;

public class MacawWorkBenchTest {

 private SessionProperties sessionProperties;
 private MacawWorkBench macawWorkBench;

 @Test
 public void testMacawWorkBenchCreation() {
 sessionProperties = new SessionProperties();
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 assertNotNull(macawWorkBench);
 }

 @Test
 public void testCreateButtonPanel() {
 UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertNotNull(buttonPanel);
 }

 @Test
 public void testDialogCreation() {
 UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 assertNotNull(macawWorkBench.dialog);
 }

 @Test
 public void testEditConstantsButtonAction() {
 UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 ActionEvent event = mock(ActionEvent.class);
 when(userInterfaceFactory.createButton("MacawWorkBench.editConstants")).thenReturn(macawWorkBench.editConstants);
 doNothing().when(macawWorkBench).editConstants();
 macawWorkBench.editConstants.actionPerformed(event);
 verify(macawWorkBench).editConstants();
 }

 @Test
 public void testEditVariablesButtonAction() {
 UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 ActionEvent event = mock(ActionEvent.class);
 when(userInterfaceFactory.createButton("MacawWorkBench.editVariables")).thenReturn(macawWorkBench.editVariables);
 doNothing().when(macawWorkBench).editVariables();
 macawWorkBench.editVariables.actionPerformed(event);
 verify(macawWorkBench).editVariables();
 }

 @Test
 public void testExportVariableDataButtonAction() {
 UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 ActionEvent event = mock(ActionEvent.class);
 when(userInterfaceFactory.createButton("MacawWorkBench.exportVariableData")).thenReturn(macawWorkBench.exportVariableData);
 doNothing().when(macawWorkBench).exportVariableData();
 macawWorkBench.exportVariableData.actionPerformed(event);
 verify(macawWorkBench).exportVariableData();
 }

 @Test
 public void testExitButtonAction() {
 UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 ActionEvent event = mock(ActionEvent.class);
 when(userInterfaceFactory.createButton("general.buttons.exit")).thenReturn(macawWorkBench.exit);
 macawWorkBench.exitActionPerformed(event);
 verify(macawWorkBench).exit();
 }

 @Test
 public void testMainMethod() {
 String[] arguments = new String[0];
 String dbUser = null;
 String dbPassword = null;
 SessionProperties sessionProperties = new SessionProperties();
 StartupOptions startupOptions = new StartupOptions();
 User currentUser = new User("jsmith", "cool");
 sessionProperties.setProperty("currentUser", currentUser);
 try {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 fail("Main method should not be executed in tests");
 } catch (Exception e) {
 // Expected
 }
 assertFalse(macawWorkBench.isShowing());
 }

 @Test
 public void testLoginDialog() {
 SessionProperties sessionProperties = new SessionProperties();
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 LoginDialog loginDialog = mock(LoginDialog.class);
 when(macawWorkBench.new LoginDialog(sessionProperties, 0)).thenReturn(loginDialog);
 when(loginDialog.isLoginSuccessful()).thenReturn(true);
 MacawWorkBench newMacawWorkBench = new MacawWorkBench(sessionProperties);
 assertTrue(newMacawWorkBench.isShowing());
 }

 @Test
 public void testLoginDialogCancel() {
 SessionProperties sessionProperties = new SessionProperties();
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 LoginDialog loginDialog = mock(LoginDialog.class);
 when(macawWorkBench.new LoginDialog(sessionProperties, 0)).thenReturn(loginDialog);
 when(loginDialog.isCancelled()).thenReturn(true);
 System.exit(0);
 }

 @Test(expected = AssertionError.class)
 public void testLoginDialogFailedLogin() {
 SessionProperties sessionProperties = new SessionProperties();
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 LoginDialog loginDialog = mock(LoginDialog.class);
 when(macawWorkBench.new LoginDialog(sessionProperties, 0)).thenReturn(loginDialog);
 when(loginDialog.isLoginSuccessful()).thenReturn(false);
 MacawWorkBench newMacawWorkBench = new MacawWorkBench(sessionProperties);
 fail("MacawWorkBench should not be displayed if login fails");
 }

 @Test
 public void testUseDemoDatabase() {
 SessionProperties sessionProperties = new SessionProperties();
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 ProductionRetrievalService macawRetrievalAPI = mock(ProductionRetrievalService.class);
 DemonstrationRetrievalService demonstratinRetrievalService = mock(DemonstrationRetrievalService.class);
 ProductionCurationService database = mock(ProductionCurationService.class);
 DemonstrationCurationService database2 = mock(DemonstrationCurationService.class);
 when(sessionProperties.getProperty("retrievalService")).thenReturn(macawRetrievalAPI);
 when(sessionProperties.getProperty("database")).thenReturn(database);
 when(sessionProperties.useDemo()).thenReturn(true);
 MacawWorkBench newMacawWorkBench = new MacawWorkBench(sessionProperties);
 verify(sessionProperties).setProperty("retrievalService", demonstratinRetrievalService);
 verify(sessionProperties).setProperty("database", database2);
 }

 @Test
 public void testUseProductionDatabase() {
 SessionProperties sessionProperties = new SessionProperties();
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 ProductionRetrievalService macawRetrievalAPI = mock(ProductionRetrievalService.class);
 ProductionCurationService database = mock(ProductionCurationService.class);
 when(sessionProperties.getProperty("retrievalService")).thenReturn(macawRetrievalAPI);
 when(sessionProperties.getProperty("database")).thenReturn(database);
 when(sessionProperties.useDemo()).thenReturn(false);
 MacawWorkBench newMacawWorkBench = new MacawWorkBench(sessionProperties);
 verify(sessionProperties).setProperty("retrievalService", macawRetrievalAPI);
 verify(sessionProperties).setProperty("database", database);
 }

}