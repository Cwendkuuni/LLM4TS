package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.system.*;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private SessionProperties sessionProperties;

 @Before
 public void setup() {
 sessionProperties = new SessionProperties();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testMacawWorkBenchConstructor() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 assertNotNull(macawWorkBench.dialog);
 assertNotNull(macawWorkBench.editConstants);
 assertNotNull(macawWorkBench.editVariables);
 assertNotNull(macawWorkBench.exportVariableData);
 assertNotNull(macawWorkBench.exit);
 }

 @Test
 public void testCreateButtonPanel() {
 final UserInterfaceFactory userInterfaceFactory = sessionProperties.getUserInterfaceFactory();
 final JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertNotNull(buttonPanel);
 }

 @Test
 public void testShow() {
 macawWorkBench.show();
 assertTrue(macawWorkBench.dialog.isVisible());
 }

 @Test
 public void testEditConstants() {
 final VariableConstantsEditor variableConstantsEditor = mock(VariableConstantsEditor.class);
 when(variableConstantsEditor.show()).thenReturn(true);
 when(sessionProperties.getUserInterfaceFactory()).thenReturn(mock(UserInterfaceFactory.class));
 macawWorkBench.editConstants = variableConstantsEditor;
 macawWorkBench.editConstants.doClick();
 assertTrue(variableConstantsEditor.showWasCalled());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEditConstantsNullSessionProperties() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(null);
 }

 @Test(expected = NullPointerException.class)
 public void testShowNullDialog() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.dialog = null;
 macawWorkBench.show();
 }

 @Test(expected = NullPointerException.class)
 public void testCreateButtonPanelNullUserInterfaceFactory() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.sessionProperties.setProperty("userInterfaceFactory", null);
 macawWorkBench.createButtonPanel(macawWorkBench.sessionProperties.getUserInterfaceFactory());
 }

 @Test(expected = NullPointerException.class)
 public void testEditConstantsNullUserInterfaceFactory() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.sessionProperties.setProperty("userInterfaceFactory", null);
 macawWorkBench.editConstants = macawWorkBench.sessionProperties.getUserInterfaceFactory();
 macawWorkBench.editConstants.doClick();
 }
}