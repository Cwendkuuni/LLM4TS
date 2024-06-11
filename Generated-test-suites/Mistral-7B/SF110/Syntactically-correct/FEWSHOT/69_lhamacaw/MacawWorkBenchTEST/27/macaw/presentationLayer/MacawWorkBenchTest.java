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
    }

    @Test
    public void testCreateButtonPanel() {
        UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
        MacawWorkBench macawWorkBench = new MacawWorkBench(mock(SessionProperties.class));
        JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);

        assertEquals(3, buttonPanel.getComponentCount());
    }

    @Test
    public void testActionPerformed_EditConstants() {
        MacawWorkBench macawWorkBench = mock(MacawWorkBench.class);
        ActionEvent actionEvent = mock(ActionEvent.class);
        doAnswer(invocation -> macawWorkBench.editConstants()).when(macawWorkBench).actionPerformed(actionEvent);
        macawWorkBench.actionPerformed(actionEvent);
        verify(macawWorkBench).editConstants();
    }

    @Test
    public void testActionPerformed_EditVariables() {
        MacawWorkBench macawWorkBench = mock(MacawWorkBench.class);
        ActionEvent actionEvent = mock(ActionEvent.class);
        doAnswer(invocation -> macawWorkBench.editVariables()).when(macawWorkBench).actionPerformed(actionEvent);
        macawWorkBench.actionPerformed(actionEvent);
        verify(macawWorkBench).editVariables();
    }

    @Test
    public void testActionPerformed_ExportVariableData() {
        MacawWorkBench macawWorkBench = mock(MacawWorkBench.class);
        ActionEvent actionEvent = mock(ActionEvent.class);
        doAnswer(invocation -> macawWorkBench.exportVariableData()).when(macawWorkBench).actionPerformed(actionEvent);
        macawWorkBench.actionPerformed(actionEvent);
        verify(macawWorkBench).exportVariableData();
    }

    @Test
    public void testActionPerformed_Exit() {
        MacawWorkBench macawWorkBench = mock(MacawWorkBench.class);
        ActionEvent actionEvent = mock(ActionEvent.class);
        doAnswer(invocation -> System.exit(0)).when(macawWorkBench).exit();
        macawWorkBench.actionPerformed(actionEvent);
        verify(macawWorkBench).exit();
    }

    @Test
    public void testShowDialog() {
        MacawWorkBench macawWorkBench = mock(MacawWorkBench.class);
        macawWorkBench.show();
        verify(macawWorkBench.dialog).setVisible(true);
    }
}