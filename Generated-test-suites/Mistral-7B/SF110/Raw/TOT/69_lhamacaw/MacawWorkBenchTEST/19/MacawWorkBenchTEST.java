package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;

import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.presentationLayer.UserInterfaceFactory;
import macaw.system.SessionProperties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class MacawWorkBenchTest {

 private MacawWorkBench workBench;
 private UserInterfaceFactory userInterfaceFactory;
 private SessionProperties sessionProperties;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 when(userInterfaceFactory.createDialog()).thenReturn(new JDialog());
 when(userInterfaceFactory.createPanel()).thenReturn(new JPanel());
 when(userInterfaceFactory.createTextArea()).thenReturn(new JTextArea());
 when(userInterfaceFactory.createButton(String.class)).thenAnswer(invocation -> new JButton(invocation.getArguments()[0]));
 when(userInterfaceFactory.createImmutableTextArea(int.class, int.class)).thenReturn(new JTextArea());
 workBench = new MacawWorkBench(sessionProperties);
 workBench.setUserInterfaceFactory(userInterfaceFactory);
 }

 @Test
 public void testShowDialog() {
 workBench.show();
 verify(userInterfaceFactory).createDialog();
 }

 @Test
 public void testCreateButtonPanel() {
 final JPanel panel = workBench.createButtonPanel(userInterfaceFactory);
 assertEquals(4, panel.getComponentCount());
 }

 @Test
 public void testEditConstants() {
 workBench.editConstants();
 verify(userInterfaceFactory).createDialog();
 }

 @Test
 public void testEditVariables() {
 workBench.editVariables();
 verify(userInterfaceFactory).createDialog();
 }

 @Test
 public void testExportVariableData() {
 workBench.exportVariableData();
 verifyNoMoreInteractions(userInterfaceFactory);
 }

 @Test
 public void testExit() {
 workBench.exit();
 verify(workBench).exit();
 }

 @Test
 public void testActionPerformedWithEditConstantsButton() {
 final ActionEvent editConstantsEvent = mock(ActionEvent.class);
 workBench.actionPerformed(editConstantsEvent);
 verify(userInterfaceFactory).createDialog();
 }

 @Test
 public void testActionPerformedWithEditVariablesButton() {
 final ActionEvent editVariablesEvent = mock(ActionEvent.class);
 workBench.actionPerformed(editVariablesEvent);
 verify(userInterfaceFactory).createDialog();
 }

 @Test
 public void testActionPerformedWithExportVariableDataButton() {
 final ActionEvent exportVariableDataEvent = mock(ActionEvent.class);
 workBench.actionPerformed(exportVariableDataEvent);
 verifyNoMoreInteractions(userInterfaceFactory);
 }

 @Test
 public void testActionPerformedWithExitButton() {
 final ActionEvent exitEvent = mock(ActionEvent.class);
 workBench.actionPerformed(exitEvent);
 verify(workBench).exit();
 }
}