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
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);

        assertEquals(5, buttonPanel.getComponentCount());
    }

    @Test
    public void testActionPerformed() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        macawWorkBench.actionPerformed(actionEvent);

        verify(macawWorkBench, times(0)).editConstants();
        verify(macawWorkBench, times(0)).editVariables();
        verify(macawWorkBench, times(0)).exportVariableData();
        verify(macawWorkBench, times(0)).exit();
    }

    @Test
    public void testEditConstants() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        macawWorkBench.editConstants();

        // Add verification for VariableConstantsEditor showing up
    }

    @Test
    public void testEditVariables() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        macawWorkBench.editVariables();

        // Add verification for MacawVariableBrowser showing up
    }

    @Test
    public void testExportVariableData() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        macawWorkBench.exportVariableData();

        // Add verification for ExportVariablesToMacawXML exporting variables
    }

    @Test
    public void testExit() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        macawWorkBench.exit();

        // Add verification for System.exit(0) being called
    }
}