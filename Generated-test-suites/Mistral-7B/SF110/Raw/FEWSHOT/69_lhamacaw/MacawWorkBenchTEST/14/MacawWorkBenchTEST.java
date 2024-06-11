package macaw.presentationLayer;

import static org.junit.Assert.*;
import org.junit.Test;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.system.*;
import javax.swing.*;
import java.awt.*;
import macaw.io.*;

public class MacawWorkBenchTest {

    @Test
    public void testMacawWorkBenchCreation() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        assertNotNull(macawWorkBench.dialog);
    }

    @Test
    public void testCreateButtonPanel() {
        UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
        MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);

        // Test number of buttons
        int numberOfButtons = 4;
        assertEquals(numberOfButtons, buttonPanel.getComponentCount());

        // Test button labels
        JButton[] buttons = new JButton[numberOfButtons];
        for (int i = 0; i < numberOfButtons; ++i) {
            String buttonLabel = null;
            if (i == 0) {
                buttonLabel = "macawWorkBench.editConstants";
            } else if (i == 1) {
                buttonLabel = "macawWorkBench.editVariables";
            } else if (i == 2) {
                buttonLabel = "macawWorkBench.exportVariableData";
            } else {
                buttonLabel = "general.buttons.exit";
            }
            JButton button = (JButton) buttonPanel.getComponent(i);
            assertEquals(buttonLabel, button.getText());
        }
    }

    @Test
    public void testActionPerformed() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        ActionEvent actionEvent = mock(ActionEvent.class);
        macawWorkBench.actionPerformed(actionEvent);

        // Test that methods are called based on button pressed
        JButton[] buttons = new JButton[]{macawWorkBench.editConstants, macawWorkBench.editVariables, macawWorkBench.exportVariableData, macawWorkBench.exit};
        for (JButton button : buttons) {
            assertFalse(button.isEnabled());
        }

        when(actionEvent.getSource()).thenReturn(macawWorkBench.editConstants);
        macawWorkBench.actionPerformed(actionEvent);
        assertTrue(macawWorkBench.editConstants.isEnabled());
        verify(macawWorkBench).editConstants();

        when(actionEvent.getSource()).thenReturn(macawWorkBench.editVariables);
        macawWorkBench.actionPerformed(actionEvent);
        assertTrue(macawWorkBench.editVariables.isEnabled());
        verify(macawWorkBench).editVariables();

        when(actionEvent.getSource()).thenReturn(macawWorkBench.exportVariableData);
        macawWorkBench.actionPerformed(actionEvent);
        assertTrue(macawWorkBench.exportVariableData.isEnabled());
        verify(macawWorkBench).exportVariableData();

        when(actionEvent.getSource()).thenReturn(macawWorkBench.exit);
        macawWorkBench.actionPerformed(actionEvent);
        assertFalse(macawWorkBench.exit.isEnabled());
        verify(macawWorkBench).exit();
    }
}