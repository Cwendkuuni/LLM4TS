package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.JOptionPane;

import macaw.businessLayer.*;
import macaw.io.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.presentationLayer.UserInterfaceFactory;
import macaw.system.*;
import org.junit.*;
import org.mockito.MockitoAnnotations;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private UserInterfaceFactory userInterfaceFactoryMock;
 private SessionProperties sessionPropertiesMock;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 sessionPropertiesMock = mock(SessionProperties.class);
 userInterfaceFactoryMock = mock(UserInterfaceFactory.class);
 when(userInterfaceFactoryMock.createDialog()).thenReturn(new JDialog());
 when(userInterfaceFactoryMock.createButton(anyString())).thenAnswer(invocation -> invocation.getArguments()[0]);
 when(userInterfaceFactoryMock.createTextArea(3, 20)).thenReturn(new JTextArea());
 when(userInterfaceFactoryMock.createPanel()).thenReturn(new JPanel());
 when(sessionPropertiesMock.getUserInterfaceFactory()).thenReturn(userInterfaceFactoryMock);
 macawWorkBench = new MacawWorkBench(sessionPropertiesMock);
 }

 @Test
 public void testCreateButtonPanel() {
 final JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactoryMock);
 assertEquals("Unexpected number of components in button panel", 4, buttonPanel.getComponentCount());
 }

 @Test
 public void testShow() {
 macawWorkBench.show();
 verify(userInterfaceFactoryMock).createDialog();
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
 verify(userInterfaceFactoryMock).createOptionDialog(any(String.class), anyString(), anyInt(), eq(JOptionPane.PLAIN_MESSAGE), any(ActionListener.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullSessionProperties() {
 new MacawWorkBench(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullUserInterfaceFactory() {
 macawWorkBench = new MacawWorkBench(sessionPropertiesMock);
 macawWorkBench.setUserInterfaceFactory(null);
 macawWorkBench.show();
 }

 @After
 public void tearDown() {
 macawWorkBench = null;
 userInterfaceFactoryMock = null;
 sessionPropertiesMock = null;
 }
}