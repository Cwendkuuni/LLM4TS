package macaw.presentationLayer.test;

import macaw.businessLayer.SessionProperties;
import macaw.io.UserInterfaceFactory;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MacawWorkBenchTest {
    @Test
    public void testMacawWorkBenchConstruction() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        assertNotNull(macawWorkBench.dialog);
        assertNotNull(macawWorkBench.editConstants);
        assertNotNull(macawWorkBench.editVariables);
        assertNotNull(macawWorkBench.exportVariableData);
        assertNotNull(macawWorkBench.exit);
    }

    @Test
    public void testCreateButtonPanel() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);

        // Check if the number of buttons is correct
        int numberOfButtons = buttonPanel.getComponentCount();
        assertEquals(4, numberOfButtons);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateButtonPanelWithNullUserInterfaceFactory() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        macawWorkBench.createButtonPanel(null);
    }

    @Test
    public void testCreateDialogFailure() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        when(userInterfaceFactory.createDialog()).thenThrow(new RuntimeException());
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        try {
            macawWorkBench.createButtonPanel(userInterfaceFactory);
            fail("Exception expected");
        } catch (RuntimeException e) {
            // Expected
        }
    }

    @Test
    public void testCreateEditConstantsButtonFailure() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        when(userInterfaceFactory.createButton(anyString())).thenThrow(new RuntimeException());
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        try {
            macawWorkBench.createButtonPanel(userInterfaceFactory);
            fail("Exception expected");
        } catch (RuntimeException e) {
            // Expected
        }
    }

    @Test
    public void testCreateEditVariablesButtonFailure() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        when(userInterfaceFactory.createButton(anyString())).thenThrow(new RuntimeException());
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        try {
            macawWorkBench.createButtonPanel(userInterfaceFactory);
            fail("Exception expected");
        } catch (RuntimeException e) {
            // Expected
        }
    }

    @Test
    public void testCreateExportVariableDataButtonFailure() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        when(userInterfaceFactory.createButton(anyString())).thenThrow(new RuntimeException());
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        try {
            macawWorkBench.createButtonPanel(userInterfaceFactory);
            fail("Exception expected");
        } catch (RuntimeException e) {
            // Expected
        }
    }

    @Test
    public void testCreateExitButtonFailure() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        when(userInterfaceFactory.createButton(anyString())).thenThrow(new RuntimeException());
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        try {
            macawWorkBench.createButtonPanel(userInterfaceFactory);
            fail("Exception expected");
        } catch (RuntimeException e) {
            // Expected
        }
    }
}