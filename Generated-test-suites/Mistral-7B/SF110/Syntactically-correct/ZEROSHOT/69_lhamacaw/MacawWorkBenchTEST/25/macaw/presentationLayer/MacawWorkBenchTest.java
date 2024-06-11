package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.*;
import org.junit.Before;
import org.junit.Test;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private SessionProperties sessionProperties;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testMacawWorkBenchConstruction() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 assertNotNull(macawWorkBench.sessionProperties);
 assertNotNull(macawWorkBench.dialog);
 assertNotNull(macawWorkBench.editConstants);
 assertNotNull(macawWorkBench.editVariables);
 assertNotNull(macawWorkBench.exportVariableData);
 assertNotNull(macawWorkBench.exit);
 }

 @Test
 public void testCreateButtonPanel() {
 UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertNotNull(buttonPanel);
 }

 @Test
 public void testShowDialog() {
 macawWorkBench.show();
 assertTrue(macawWorkBench.dialog.isVisible());
 }

 @Test
 public void testEditConstantsMethod() {
 macawWorkBench = spy(MacawWorkBench.class);
 doCallRealMethod().when(macawWorkBench).editConstants();
 macawWorkBench.show();
 verify(macawWorkBench).editConstants();
 }

 @Test
 public void testEditVariablesMethod() {
 macawWorkBench = spy(MacawWorkBench.class);
 doCallRealMethod().when(macawWorkBench).editVariables();
 macawWorkBench.show();
 verify(macawWorkBench).editVariables();
 }

 @Test
 public void testExportVariableDataMethod() {
 macawWorkBench = spy(MacawWorkBench.class);
 doCallRealMethod().when(macawWorkBench).exportVariableData();
 macawWorkBench.show();
 verify(macawWorkBench).exportVariableData();
 }

 @Test
 public void testExitMethod() {
 macawWorkBench = spy(MacawWorkBench.class);
 doCallRealMethod().when(macawWorkBench).exit();
 macawWorkBench.show();
 verify(macawWorkBench).exit();
 System.exit(0);
 }

 @Test(expected = AssertionError.class)
 public void testExceptionInConstructor() {
 try {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 fail("Exception should have been thrown");
 } catch (Exception e) {
 // expected
 }
 }
}