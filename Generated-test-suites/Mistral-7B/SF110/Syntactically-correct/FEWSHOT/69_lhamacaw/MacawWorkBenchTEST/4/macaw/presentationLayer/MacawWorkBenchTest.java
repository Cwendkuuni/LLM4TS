package macaw.presentationLayer;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;

public class MacawWorkBenchTest {

    private SessionProperties sessionProperties;
    private UserInterfaceFactory userInterfaceFactory;
    private MacawWorkBench macawWorkBench;

    @Before
    public void setUp() {
        sessionProperties = new SessionProperties();
        userInterfaceFactory = mock(UserInterfaceFactory.class);
        when(userInterfaceFactory.createDialog()).thenReturn(new JDialog());
        when(userInterfaceFactory.createPanel()).thenReturn(new JPanel());
        when(userInterfaceFactory.createButton(anyString())).thenAnswer(invocation -> new JButton());
        macawWorkBench = new MacawWorkBench(sessionProperties);
    }

    @Test
    public void testCreateButtonPanel() {
        JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);

        // Test number of buttons
        assertEquals(4, buttonPanel.getComponentCount());

        // Test button labels
        JButton editConstantsButton = (JButton) buttonPanel.getComponent(0);
        assertEquals("Edit Constants", editConstantsButton.getText());

        JButton editVariablesButton = (JButton) buttonPanel.getComponent(1);
        assertEquals("Edit Variables", editVariablesButton.getText());

        JButton exportVariableDataButton = (JButton) buttonPanel.getComponent(2);
        assertEquals("Export Variable Data", exportVariableDataButton.getText());

        JButton exitButton = (JButton) buttonPanel.getComponent(3);
        assertEquals("Exit", exitButton.getText());

        // Test button action listeners
        verify(userInterfaceFactory, times(4)).createButton(anyString());
        for (int i = 0; i < 4; i++) {
            JButton button = (JButton) buttonPanel.getComponent(i);
            verify(button).addActionListener(macawWorkBench);
        }
    }

    @Test
    public void testEditConstants() {
        when(userInterfaceFactory.createDialog()).thenReturn(new JDialog());
        macawWorkBench.editConstants();
        verify(userInterfaceFactory).createDialog();
    }

    @Test
    public void testEditVariables() {
        when(userInterfaceFactory.createDialog()).thenReturn(new JDialog());
        macawWorkBench.editVariables();
        verify(userInterfaceFactory).createDialog();
    }

    @Test
    public void testExportVariableData() {
        when(userInterfaceFactory.createDialog()).thenReturn(new JDialog());
        ExportVariablesToMacawXML exportToDSSFormat = new ExportVariablesToMacawXML(sessionProperties);
        macawWorkBench.exportVariableData();
        verify(userInterfaceFactory).createDialog();
        exportToDSSFormat.export();
    }

    @Test
    public void testExit() {
        System.exit(0);
    }
}