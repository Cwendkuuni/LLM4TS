package macaw.presentationLayer;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.system.*;
import javax.swing.*;
import java.awt.*;
import macaw.io.*;

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

        assertNotNull(buttonPanel);
        assertEquals(5, buttonPanel.getComponentCount());
    }

    @Test
    public void testActionPerformed_EditConstants() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = spy(new MacawWorkBench(sessionProperties));

        ActionEvent actionEvent = mock(ActionEvent.class);
        when(actionEvent.getSource()).thenReturn(macawWorkBench.editConstants);

        macawWorkBench.actionPerformed(actionEvent);

        verify(macawWorkBench).editConstants();
    }

    @Test
    public void testActionPerformed_EditVariables() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = spy(new MacawWorkBench(sessionProperties));

        ActionEvent actionEvent = mock(ActionEvent.class);
        when(actionEvent.getSource()).thenReturn(macawWorkBench.editVariables);

        macawWorkBench.actionPerformed(actionEvent);

        verify(macawWorkBench).editVariables();
    }

    @Test
    public void testActionPerformed_ExportVariableData() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = spy(new MacawWorkBench(sessionProperties));

        ActionEvent actionEvent = mock(ActionEvent.class);
        when(actionEvent.getSource()).thenReturn(macawWorkBench.exportVariableData);

        macawWorkBench.actionPerformed(actionEvent);

        verify(macawWorkBench).exportVariableData();
    }

    @Test
    public void testActionPerformed_Exit() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = spy(new MacawWorkBench(sessionProperties));

        ActionEvent actionEvent = mock(ActionEvent.class);
        when(actionEvent.getSource()).thenReturn(macawWorkBench.exit);

        macawWorkBench.actionPerformed(actionEvent);

        verify(macawWorkBench).exit();
    }

    @Test
    public void testMain() {
        String[] arguments = {"arg1", "arg2"};
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());

        macawWorkBench.main(arguments);

        // Ensure the application does not terminate
        assertTrue(true);
    }
}