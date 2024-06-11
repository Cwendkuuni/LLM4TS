package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;

import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.presentationLayer.UserInterfaceFactory;
import macaw.system.*;
import macaw.io.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private UserInterfaceFactory userInterfaceFactoryMock;

 @Before
 public void setUp() {
 userInterfaceFactoryMock = mock(UserInterfaceFactory.class);
 macawWorkBench = new MacawWorkBench(new SessionProperties());
 macawWorkBench.setUserInterfaceFactory(userInterfaceFactoryMock);
 }

 @Test
 public void testShow() {
 macawWorkBench.show();
 verify(userInterfaceFactoryMock).createDialog();
 }

 @Test
 public void testCreateButtonPanel() {
 final JPanel result = macawWorkBench.createButtonPanel(userInterfaceFactoryMock);
 final int expectedNumberOfComponents = 4;
 assertEquals(expectedNumberOfComponents, result.getComponentCount());
 }

 @Test
 public void testEditConstants() {
 macawWorkBench.editConstants();
 verify(userInterfaceFactoryMock).createDialog();
 }

 @Test
 public void testEditVariables() {
 macawWorkBench.editVariables();
 verify(userInterfaceFactoryMock).createDialog();
 }

 @Test
 public void testExportVariableData() {
 macawWorkBench.exportVariableData();
 verify(userInterfaceFactoryMock, times(1)).createDialog();
 }

 @Test
 public void testExit() {
 macawWorkBench.exit();
 verify(userInterfaceFactoryMock, never()).createDialog();
 }

 @Test
 public void testActionPerformedWithEditConstantsButton() {
 final ActionEvent editConstantsButtonEvent = mock(ActionEvent.class);
 macawWorkBench.actionPerformed(editConstantsButtonEvent);
 macawWorkBench.editConstants();
 verify(userInterfaceFactoryMock).createDialog();
 }

 @Test
 public void testActionPerformedWithEditVariablesButton() {
 final ActionEvent editVariablesButtonEvent = mock(ActionEvent.class);
 macawWorkBench.actionPerformed(editVariablesButtonEvent);
 macawWorkBench.editVariables();
 verify(userInterfaceFactoryMock).createDialog();
 }

 @Test
 public void testActionPerformedWithExportVariableDataButton() {
 final ActionEvent exportVariableDataButtonEvent = mock(ActionEvent.class);
 macawWorkBench.actionPerformed(exportVariableDataButtonEvent);
 macawWorkBench.exportVariableData();
 verify(userInterfaceFactoryMock, times(1)).createDialog();
 }

 @Test
 public void testActionPerformedWithExitButton() {
 final ActionEvent exitButtonEvent = mock(ActionEvent.class);
 macawWorkBench.actionPerformed(exitButtonEvent);
 macawWorkBench.exit();
 verify(userInterfaceFactoryMock, never()).createDialog();
 }
}