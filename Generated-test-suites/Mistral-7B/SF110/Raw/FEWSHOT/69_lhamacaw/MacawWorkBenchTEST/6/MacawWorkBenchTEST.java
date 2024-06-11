package macaw.presentationLayer;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import static macaw.system.MacawMessages.getMessage;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MacawWorkBenchTest {

    private MacawWorkBench macawWorkBench;
    private SessionProperties sessionProperties;
    private UserInterfaceFactory userInterfaceFactory;
    private JDialog dialog;

    @Before
    public void setup() {
        sessionProperties = new SessionProperties();
        userInterfaceFactory = new UserInterfaceFactory();
        macawWorkBench = new MacawWorkBench(sessionProperties);
        dialog = macawWorkBench.dialog;
    }

    @Test
    public void testCreateButtonPanel() {
        JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);

        int numberOfComponents = buttonPanel.getComponentCount();
        assertEquals(4, numberOfComponents);

        for (int i = 0; i < numberOfComponents; ++i) {
            JButton button = (JButton) buttonPanel.getComponent(i);
            assertTrue(button.isEnabled());
            assertTrue(button.getText().startsWith(getMessage((String) getClass().getField(button.getText()).getName())));
        }
    }

    @Test
    public void testShow() {
        macawWorkBench.show();
        assertTrue(dialog.isDisplayable());
        assertTrue(dialog.isModal());
        assertEquals(300, dialog.getWidth());
        assertEquals(300, dialog.getHeight());
    }

    @Test
    public void testActionPerformed_EditConstants() {
        macawWorkBench.editConstants = new JButton(getMessage("macawWorkBench.editConstants"));
        macawWorkBench.addActionListener(e -> macawWorkBench.editConstants());

        macawWorkBench.actionPerformed(new ActionEvent(macawWorkBench.editConstants, SwingConstants.LEFT, "Edit Constants"));
        assertTrue(macawWorkBench.sessionProperties.getProperty("editConstants") != null);
    }

    @Test
    public void testActionPerformed_EditVariables() {
        macawWorkBench.editVariables = new JButton(getMessage("macawWorkBench.editVariables"));
        macawWorkBench.addActionListener(e -> macawWorkBench.editVariables());

        macawWorkBench.actionPerformed(new ActionEvent(macawWorkBench.editVariables, SwingConstants.LEFT, "Edit Variables"));
        assertTrue(macawWorkBench.sessionProperties.getProperty("MacawVariableBrowser") != null);
    }

    @Test
    public void testActionPerformed_ExportVariableData() {
        macawWorkBench.exportVariableData = new JButton(getMessage("macawWorkBench.exportVariableData"));
        macawWorkBench.addActionListener(e -> macawWorkBench.exportVariableData());

        ExportVariablesToMacawXML exportToDSSFormat = mock(ExportVariablesToMacawXML.class);
        when(exportToDSSFormat.export()).thenReturn(true);
        macawWorkBench.sessionProperties.setProperty("exportToDSSFormat", exportToDSSFormat);

        macawWorkBench.actionPerformed(new ActionEvent(macawWorkBench.exportVariableData, SwingConstants.LEFT, "Export Variable Data"));
        verify(exportToDSSFormat).export();
    }

    @Test
    public void testActionPerformed_Exit() {
        macawWorkBench.exit = new JButton(getMessage("general.buttons.exit"));

        macawWorkBench.actionPerformed(new ActionEvent(macawWorkBench.exit, SwingConstants.LEFT, "Exit"));
        assertEquals(0, System.exit(0));
    }
}