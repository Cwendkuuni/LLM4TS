package macaw.presentationLayer;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import static org.mockito.Mockito.*;

public class MacawWorkBenchTest {

    @Test
    public void testMacawWorkBenchCreation() {
        UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        assertEquals("macawWorkBench.title", macawWorkBench.dialog.getTitle());
    }

    @Test
    public void testCreateButtonPanel() {
        UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
        MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
        assertEquals(5, buttonPanel.getComponentCount());
    }

    @Test
    public void testButtonActions() {
        UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        mockStatic(MacawMessages.class);

        when(MacawMessages.getMessage("macawWorkBench.editConstants")).thenReturn("Edit Constants");
        when(MacawMessages.getMessage("macawWorkBench.editConstants.instructions")).thenReturn("Edit Constants Instructions");
        when(MacawMessages.getMessage("macawWorkBench.editVariables")).thenReturn("Edit Variables");
        when(MacawMessages.getMessage("macawWorkBench.editVariables.instructions")).thenReturn("Edit Variables Instructions");
        when(MacawMessages.getMessage("macawWorkBench.exportVariableData")).thenReturn("Export Variable Data");
        when(MacawMessages.getMessage("macawWorkBench.exportVariableData.instructions")).thenReturn("Export Variable Data Instructions");
        when(MacawMessages.getMessage("general.buttons.exit")).thenReturn("Exit");

        macawWorkBench.editConstants();
        verify(userInterfaceFactory).createButton("Edit Constants");

        macawWorkBench.editVariables();
        verify(userInterfaceFactory, times(2)).createButton("Edit Variables");

        macawWorkBench.exportVariableData();
        verify(userInterfaceFactory, times(2)).createButton("Export Variable Data");

        macawWorkBench.exit();
        verify(userInterfaceFactory).createButton("Exit");
    }
}