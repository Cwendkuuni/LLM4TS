package macaw.presentationLayer;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import macaw.io.*;

public class MacawWorkBenchTest {

    @Test
    public void testMacawWorkBenchCreation() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        assertNotNull(macawWorkBench.dialog);
        assertEquals("macawWorkBench.title", macawWorkBench.dialog.getTitle());
    }

    @Test
    public void testCreateButtonPanel() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);

        assertEquals(5, buttonPanel.getComponentCount());

        for (Component component : buttonPanel.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                assertTrue(button.isEnabled());
            }
        }
    }

    @Test
    public void testActionPerformedWhenEditConstantsButtonIsClicked() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        JButton editConstantsButton = macawWorkBench.editConstants;

        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        macawWorkBench.actionPerformed(actionEvent);

        verify(userInterfaceFactory, times(1)).createVariableConstantsEditor(sessionProperties, true);
    }

    @Test
    public void testActionPerformedWhenEditVariablesButtonIsClicked() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        JButton editVariablesButton = macawWorkBench.editVariables;

        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        macawWorkBench.actionPerformed(actionEvent);

        verify(userInterfaceFactory, times(1)).createMacawVariableBrowser(sessionProperties);
    }

    @Test
    public void testActionPerformedWhenExportVariableDataButtonIsClicked() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        JButton exportVariableDataButton = macawWorkBench.exportVariableData;

        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        macawWorkBench.actionPerformed(actionEvent);

        ExportVariablesToMacawXML exportVariablesToMacawXML = Mockito.mock(ExportVariablesToMacawXML.class);
        when(userInterfaceFactory.createFileChooser()).thenReturn(Mockito.mock(JFileChooser.class));
        when(userInterfaceFactory.createFileFilter()).thenReturn(Mockito.mock(FileFilter.class));
        when(sessionProperties.getProperty("database")).thenReturn(new ProductionCurationService(sessionProperties));

        macawWorkBench.sessionProperties = sessionProperties;
        macawWorkBench.exportVariableData();

        verify(exportVariablesToMacawXML, times(1)).export();
    }

    @Test
    public void testActionPerformedWhenExitButtonIsClicked() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        JButton exitButton = macawWorkBench.exit;

        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        macawWorkBench.actionPerformed(actionEvent);

        verify(system.System.class, times(1)).exit(0);
    }
}