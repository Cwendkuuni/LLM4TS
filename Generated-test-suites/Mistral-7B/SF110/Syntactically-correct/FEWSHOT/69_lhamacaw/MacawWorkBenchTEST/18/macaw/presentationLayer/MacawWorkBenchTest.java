package macaw.presentationLayer;

import static org.junit.Assert.*;
import static macaw.system.MacawMessages.*;

import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

@RunWith(MockitoJUnitRunner.class)
public class MacawWorkBenchTest {

    @Test
    public void testCreateButtonPanel() {
        final UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        final MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        final JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);

        final int numberOfComponents = 4;
        assertEquals(numberOfComponents, buttonPanel.getComponentCount());

        for (int i = 0; i < numberOfComponents; ++i) {
            final AbstractButton button = (AbstractButton) buttonPanel.getComponent(i);
            assertNotNull(button);
        }
    }

    @Test
    public void testActionPerformedEditConstants() {
        final SessionProperties sessionProperties = new SessionProperties();
        final MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        final ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        macawWorkBench.actionPerformed(actionEvent);
        Mockito.verify(macawWorkBench).editConstants();
    }

    @Test
    public void testActionPerformedEditVariables() {
        final SessionProperties sessionProperties = new SessionProperties();
        final MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        final ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        macawWorkBench.actionPerformed(actionEvent);
        Mockito.verify(macawWorkBench).editVariables();
    }

    @Test
    public void testActionPerformedExportVariableData() {
        final SessionProperties sessionProperties = new SessionProperties();
        final MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        final ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        macawWorkBench.actionPerformed(actionEvent);
        Mockito.verify(macawWorkBench).exportVariableData();
    }

    @Test
    public void testActionPerformedExit() {
        final SessionProperties sessionProperties = new SessionProperties();
        final MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        final ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        macawWorkBench.actionPerformed(actionEvent);
        Mockito.verify(macawWorkBench).exit();
    }

    @Test
    public void testIsLoginSuccessful() {
        final LoginDialog loginDialog = Mockito.mock(LoginDialog.class);
        Mockito.when(loginDialog.isLoginSuccessful()).thenReturn(true);
        final MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        macawWorkBench.dialog = loginDialog;
        final boolean isLoginSuccessful = macawWorkBench.isLoginSuccessful();
        assertTrue(isLoginSuccessful);
    }

    @Test
    public void testIsLoginSuccessfulFailed() {
        final LoginDialog loginDialog = Mockito.mock(LoginDialog.class);
        Mockito.when(loginDialog.isLoginSuccessful()).thenReturn(false);
        final MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        macawWorkBench.dialog = loginDialog;
        final boolean isLoginSuccessful = macawWorkBench.isLoginSuccessful();
        assertFalse(isLoginSuccessful);
    }

    @Test
    public void testIsCancelled() {
        final LoginDialog loginDialog = Mockito.mock(LoginDialog.class);
        Mockito.when(loginDialog.isCancelled()).thenReturn(true);
        final MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        macawWorkBench.dialog = loginDialog;
        final boolean isCancelled = macawWorkBench.isCancelled();
        assertTrue(isCancelled);
    }

    @Test
    public void testIsCancelledFalse() {
        final LoginDialog loginDialog = Mockito.mock(LoginDialog.class);
        Mockito.when(loginDialog.isCancelled()).thenReturn(false);
        final MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        macawWorkBench.dialog = loginDialog;
        final boolean isCancelled = macawWorkBench.isCancelled();
        assertFalse(isCancelled);
    }

    @Test
    public void testProcessCommandLineArguments() {
        final StartupOptions startupOptions = Mockito.mock(StartupOptions.class);
        final SessionProperties sessionProperties = new SessionProperties();
        Mockito.when(startupOptions.processCommandLineArguments(Mockito.<String[]>anyArray())).thenReturn(sessionProperties);
        final MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        macawWorkBench.sessionProperties = sessionProperties;
        final String[] arguments = {"arg1", "arg2"};
        macawWorkBench.processCommandLineArguments(arguments);
        Mockito.verify(startupOptions).processCommandLineArguments(arguments);
    }

    @Test
    public void testProcessCommandLineArgumentsNoArguments() {
        final StartupOptions startupOptions = Mockito.mock(StartupOptions.class);
        final SessionProperties sessionProperties = new SessionProperties();
        Mockito.when(startupOptions.processCommandLineArguments(Mockito.<String[]>anyArray())).thenReturn(sessionProperties);
        final MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        macawWorkBench.sessionProperties = sessionProperties;
        macawWorkBench.processCommandLineArguments(new String[0]);
        Mockito.verify(startupOptions, Mockito.never()).processCommandLineArguments(Mockito.<String[]>anyArray());
    }

    @Test
    public void testSetProperty() {
        final MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        final String propertyKey = "testPropertyKey";
        final Object propertyValue = "testPropertyValue";
        macawWorkBench.sessionProperties.setProperty(propertyKey, propertyValue);
        assertEquals(propertyValue, macawWorkBench.sessionProperties.getProperty(propertyKey));
    }

    @Test
    public void testSetPropertyNull() {
        final MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        macawWorkBench.sessionProperties.setProperty(null, "testPropertyValue");
        assertNull(macawWorkBench.sessionProperties.getProperty(null));
    }
}