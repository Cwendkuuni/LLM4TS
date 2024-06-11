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
    public void testMacawWorkBench() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        assertNotNull(macawWorkBench.dialog);
        assertNotNull(macawWorkBench.editConstants);
        assertNotNull(macawWorkBench.editVariables);
        assertNotNull(macawWorkBench.exportVariableData);
        assertNotNull(macawWorkBench.exit);
    }

    @Test
    public void testCreateButtonPanel() {
        UserInterfaceFactory userInterfaceFactory = new UserInterfaceFactory();
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);

        assertEquals(5, buttonPanel.getComponentCount());
    }

    @Test
    public void testEditConstantsButton() {
        UserInterfaceFactory userInterfaceFactory = new UserInterfaceFactory();
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        JButton editConstantsButton = macawWorkBench.editConstants;
        assertNotNull(editConstantsButton);
        assertEquals("Edit Constants", editConstantsButton.getText());
    }

    @Test
    public void testEditVariablesButton() {
        UserInterfaceFactory userInterfaceFactory = new UserInterfaceFactory();
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        JButton editVariablesButton = macawWorkBench.editVariables;
        assertNotNull(editVariablesButton);
        assertEquals("Edit Variables", editVariablesButton.getText());
    }

    @Test
    public void testExportVariableDataButton() {
        UserInterfaceFactory userInterfaceFactory = new UserInterfaceFactory();
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        JButton exportVariableDataButton = macawWorkBench.exportVariableData;
        assertNotNull(exportVariableDataButton);
        assertEquals("Export Variable Data", exportVariableDataButton.getText());
    }

    @Test
    public void testExitButton() {
        UserInterfaceFactory userInterfaceFactory = new UserInterfaceFactory();
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        JButton exitButton = macawWorkBench.exit;
        assertNotNull(exitButton);
        assertEquals("Exit", exitButton.getText());
    }

    @Test
    public void testMacawWorkBenchActionPerformed() {
        UserInterfaceFactory userInterfaceFactory = new UserInterfaceFactory();
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        ActionEvent actionEvent = new ActionEvent(new Object(), 0, "actionPerformed");

        macawWorkBench.actionPerformed(actionEvent);

        verify(macawWorkBench, times(0)).editConstants();
        verify(macawWorkBench, times(0)).editVariables();
        verify(macawWorkBench, times(0)).exportVariableData();
        verify(macawWorkBench, times(0)).exit();
    }

    @Test
    public void testEditConstantsButtonActionPerformed() {
        UserInterfaceFactory userInterfaceFactory = new UserInterfaceFactory();
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        ActionEvent actionEvent = new ActionEvent(macawWorkBench.editConstants, 0, "actionPerformed");

        macawWorkBench.actionPerformed(actionEvent);

        verify(macawWorkBench, times(1)).editConstants();
    }

    @Test
    public void testEditVariablesButtonActionPerformed() {
        UserInterfaceFactory userInterfaceFactory = new UserInterfaceFactory();
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        ActionEvent actionEvent = new ActionEvent(macawWorkBench.editVariables, 0, "actionPerformed");

        macawWorkBench.actionPerformed(actionEvent);

        verify(macawWorkBench, times(1)).editVariables();
    }

    @Test
    public void testExportVariableDataButtonActionPerformed() {
        UserInterfaceFactory userInterfaceFactory = new UserInterfaceFactory();
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        ActionEvent actionEvent = new ActionEvent(macawWorkBench.exportVariableData, 0, "actionPerformed");

        macawWorkBench.actionPerformed(actionEvent);

        verify(macawWorkBench, times(1)).exportVariableData();
    }

    @Test
    public void testExitButtonActionPerformed() {
        UserInterfaceFactory userInterfaceFactory = new UserInterfaceFactory();
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        ActionEvent actionEvent = new ActionEvent(macawWorkBench.exit, 0, "actionPerformed");

        macawWorkBench.actionPerformed(actionEvent);

        verify(macawWorkBench, times(1)).exit();
    }
}