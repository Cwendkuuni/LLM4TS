package macaw.presentationLayer;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import javax.swing.*;
import java.awt.*;
import macaw.io.*;
import static macaw.presentationLayer.MacawMessages.getMessage;

public class MacawWorkBenchTest {

    private MacawWorkBench macawWorkBench;
    private UserInterfaceFactory userInterfaceFactory;

    @Before
    public void setUp() {
        User user = new User("testUser", "testPassword");
        SessionProperties sessionProperties = new SessionProperties();
        sessionProperties.setProperty("currentUser", user);
        userInterfaceFactory = new UserInterfaceFactory() {
            @Override
            public JDialog createDialog() {
                return new JDialog();
            }

            @Override
            public JPanel createPanel() {
                return new JPanel();
            }

            @Override
            public JTextArea createImmutableTextArea(int rows, int columns) {
                return new JTextArea(rows, columns);
            }

            @Override
            public JButton createButton(String label) {
                return new JButton(label);
            }

            @Override
            public GridBagConstraints createGridBagConstraints() {
                return new GridBagConstraints();
            }

            @Override
            public Insets createInsets(int top, int left, int bottom, int right) {
                return new Insets(top, left, bottom, right);
            }
        };
        macawWorkBench = new MacawWorkBench(sessionProperties);
    }

    @Test
    public void testCreateButtonPanel() {
        JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
        assertNotNull(buttonPanel);
        assertEquals(3, buttonPanel.getComponentCount());
    }

    @Test
    public void testEditConstants() {
        // This method cannot be tested directly as it opens a dialog
        // Instead, test the behavior after the dialog is closed
        macawWorkBench.editConstants();
        // Assuming VariableConstantsEditor dialog closes successfully
        assertNotNull(macawWorkBench.sessionProperties.getProperty("variableConstants"));
    }

    @Test
    public void testEditVariables() {
        // This method cannot be tested directly as it opens a dialog
        // Instead, test the behavior after the dialog is closed
        macawWorkBench.editVariables();
        // Assuming MacawVariableBrowser dialog closes successfully
        assertNotNull(macawWorkBench.sessionProperties.getProperty("variableListModel"));
    }

    @Test
    public void testExportVariableData() {
        // This method cannot be tested directly as it writes to a file
        // Instead, test the behavior after the method is called
        macawWorkBench.exportVariableData();
        // Assuming the export is successful
        File file = new File("output.xml");
        assertTrue(file.exists());
        file.delete();
    }

    @Test
    public void testActionPerformed() {
        ActionEvent event = new ActionEvent(new Object(), 0, "test");
        macawWorkBench.actionPerformed(event);
        // Assuming the correct method is called based on the source of the event
        if (event.getSource() == macawWorkBench.editConstants) {
            macawWorkBench.editConstants();
        } else if (event.getSource() == macawWorkBench.editVariables) {
            macawWorkBench.editVariables();
        } else if (event.getSource() == macawWorkBench.exportVariableData) {
            macawWorkBench.exportVariableData();
        } else if (event.getSource() == macawWorkBench.exit) {
            assertEquals(0, System.exit(0));
        }
    }
}