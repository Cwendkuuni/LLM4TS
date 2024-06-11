package macaw.presentationLayer;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;

public class MacawWorkBenchTest {

    @Test
    public void testMacawWorkBenchConstruction() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        assertNotNull(workBench);
    }

    @Test
    public void testCreateButtonPanel() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        MacawWorkBench workBench = new MacawWorkBench(new SessionProperties());
        JPanel buttonPanel = workBench.createButtonPanel(userInterfaceFactory);

        // Check if the number of components in the buttonPanel is 5
        int numberOfComponents = buttonPanel.getComponentCount();
        assertEquals(5, numberOfComponents);

        // Check if each button has a correct label
        JButton editConstantsButton = (JButton) buttonPanel.getComponent(0);
        assertEquals("Edit Constants", editConstantsButton.getText());

        JButton editVariablesButton = (JButton) buttonPanel.getComponent(1);
        assertEquals("Edit Variables", editVariablesButton.getText());

        JButton exportVariableDataButton = (JButton) buttonPanel.getComponent(2);
        assertEquals("Export Variable Data", exportVariableDataButton.getText());

        JButton exitButton = (JButton) buttonPanel.getComponent(3);
        assertEquals("Exit", exitButton.getText());

        // Check if each button has a correct tooltip text
        String editConstantsTooltipText = MacawMessages.getMessage("macawWorkBench.editConstants.instructions");
        assertEquals(editConstantsTooltipText, editConstantsButton.getToolTipText());

        String editVariablesTooltipText = MacawMessages.getMessage("macawWorkBench.editVariables.instructions");
        assertEquals(editVariablesTooltipText, editVariablesButton.getToolTipText());

        String exportVariableDataTooltipText = MacawMessages.getMessage("macawWorkBench.exportVariableData.instructions");
        assertEquals(exportVariableDataTooltipText, exportVariableDataButton.getToolTipText());

        // Check if the exit button is disabled by default
        assertTrue(exitButton.isEnabled() == false);

        // Check if all buttons are focused on creation
        for (Component component : buttonPanel.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                assertTrue(button.isFocusable());
                assertTrue(button.isFocusOwner());
            }
        }
    }

    @Test
    public void testActionPerformed() {
        MacawWorkBench workBench = new MacawWorkBench(new SessionProperties());
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        JButton editConstantsButton = Mockito.mock(JButton.class);
       when(editConstantsButton.equals(actionEvent.getSource())).thenReturn(true);
        workBench.actionPerformed(actionEvent);
        verify(workBench, times(1)).editConstants();

        ActionEvent actionEvent2 = Mockito.mock(ActionEvent.class);
        JButton editVariablesButton = Mockito.mock(JButton.class);
        when(editVariablesButton.equals(actionEvent2.getSource())).thenReturn(true);
        workBench.actionPerformed(actionEvent2);
        verify(workBench, times(1)).editVariables();

        ActionEvent actionEvent3 = Mockito.mock(ActionEvent.class);
        JButton exportVariableDataButton = Mockito.mock(JButton.class);
        when(exportVariableDataButton.equals(actionEvent3.getSource())).thenReturn(true);
        workBench.actionPerformed(actionEvent3);
        verify(workBench, times(1)).exportVariableData();

        ActionEvent actionEvent4 = Mockito.mock(ActionEvent.class);
        JButton exitButton = Mockito.mock(JButton.class);
        when(exitButton.equals(actionEvent4.getSource())).thenReturn(true);
        workBench.actionPerformed(actionEvent4);
        verify(workBench, times(1)).exit();
    }

    @Test
    public void testExit() {
        MacawWorkBench workBench = new MacawWorkBench(new SessionProperties());
        workBench.exit();
        assertTrue(System.exit(0) == 0);
    }

    @Test
    public void testEditConstants() {
        MacawWorkBench workBench = new MacawWorkBench(new SessionProperties());
        VariableConstantsEditor variableConstantsEditor = Mockito.mock(VariableConstantsEditor.class);
        when(variableConstantsEditor.show()).thenReturn(true);
        workBench.editConstants();
        verify(workBench, times(1)).showVariableConstantsEditor();
        verify(variableConstantsEditor, times(1)).show();
    }

    @Test
    public void testEditVariables() {
        MacawWorkBench workBench = new MacawWorkBench(new SessionProperties());
        MacawVariableBrowser macawVariableBrowser = new MacawVariableBrowser(new SessionProperties());
        when(macawVariableBrowser.show()).thenReturn(true);
        workBench.editVariables();
        verify(workBench, times(1)).showVariableBrowser();
        verify(macawVariableBrowser, times(1)).show();
    }

    @Test
    public void testExportVariableData() {
        MacawWorkBench workBench = new MacawWorkBench(new SessionProperties());
        ExportVariablesToMacawXML exportToDSSFormat = new ExportVariablesToMacawXML(new SessionProperties());
        when(exportToDSSFormat.export()).thenReturn(true);
        workBench.exportVariableData();
        verify(workBench, times(1)).exportVariablesToMacawXML();
        verify(exportToDSSFormat, times(1)).export();
    }
}