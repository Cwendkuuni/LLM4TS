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
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        assertNotNull(macawWorkBench.dialog);
        assertNotNull(macawWorkBench.editConstants);
        assertNotNull(macawWorkBench.editVariables);
        assertNotNull(macawWorkBench.exportVariableData);
        assertNotNull(macawWorkBench.exit);
    }

    @Test
    public void testCreateButtonPanel() {
        UserInterfaceFactory userInterfaceFactory = MockUserInterfaceFactory.getInstance();
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);

        assertEquals(5, buttonPanel.getComponentCount());
        for (Component component : buttonPanel.getComponents()) {
            assertTrue(component instanceof JButton);
        }
    }

    @Test
    public void testActionPerformedEditConstants() {
        UserInterfaceFactory userInterfaceFactory = MockUserInterfaceFactory.getInstance();
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        ActionEvent actionEvent = new ActionEvent(macawWorkBench.editConstants, 0, "ActionPerformed");
        macawWorkBench.actionPerformed(actionEvent);
        verify(macawWorkBench).editConstants();
    }

    @Test
    public void testActionPerformedEditVariables() {
        UserInterfaceFactory userInterfaceFactory = MockUserInterfaceFactory.getInstance();
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        ActionEvent actionEvent = new ActionEvent(macawWorkBench.editVariables, 0, "ActionPerformed");
        macawWorkBench.actionPerformed(actionEvent);
        verify(macawWorkBench).editVariables();
    }

    @Test
    public void testActionPerformedExportVariableData() {
        UserInterfaceFactory userInterfaceFactory = MockUserInterfaceFactory.getInstance();
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        ActionEvent actionEvent = new ActionEvent(macawWorkBench.exportVariableData, 0, "ActionPerformed");
        macawWorkBench.actionPerformed(actionEvent);
        verify(macawWorkBench).exportVariableData();
    }

    @Test
    public void testActionPerformedExit() {
        UserInterfaceFactory userInterfaceFactory = MockUserInterfaceFactory.getInstance();
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        ActionEvent actionEvent = new ActionEvent(macawWorkBench.exit, 0, "ActionPerformed");
        macawWorkBench.actionPerformed(actionEvent);
        verify(macawWorkBench).exit();
    }
}