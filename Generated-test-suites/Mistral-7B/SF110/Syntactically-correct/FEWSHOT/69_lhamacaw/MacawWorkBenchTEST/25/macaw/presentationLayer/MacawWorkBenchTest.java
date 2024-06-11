package macaw.presentationLayer;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import javax.swing.*;
import java.awt.*;
import macaw.io.*;
import java.awt.event.*;

public class MacawWorkBenchTest {

    @Test
    public void testMacawWorkBenchConstruction() {
        SessionProperties sessionProperties = new SessionProperties();
        UserInterfaceFactory userInterfaceFactory = sessionProperties.getUserInterfaceFactory();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        assertNotNull(workBench.dialog);
    }

    @Test
    public void testCreateButtonPanel() {
        SessionProperties sessionProperties = new SessionProperties();
        UserInterfaceFactory userInterfaceFactory = sessionProperties.getUserInterfaceFactory();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        JPanel buttonPanel = workBench.createButtonPanel(userInterfaceFactory);
        assertNotNull(buttonPanel.getComponentCount());
    }

    @Test
    public void testEditConstantsButton() {
        SessionProperties sessionProperties = new SessionProperties();
        UserInterfaceFactory userInterfaceFactory = sessionProperties.getUserInterfaceFactory();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        JButton editConstantsButton = workBench.editConstants;
        assertNotNull(editConstantsButton);
        assertEquals("Edit Constants", editConstantsButton.getText());
    }

    @Test
    public void testEditVariablesButton() {
        SessionProperties sessionProperties = new SessionProperties();
        UserInterfaceFactory userInterfaceFactory = sessionProperties.getUserInterfaceFactory();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        JButton editVariablesButton = workBench.editVariables;
        assertNotNull(editVariablesButton);
        assertEquals("Edit Variables", editVariablesButton.getText());
    }

    @Test
    public void testExportVariableDataButton() {
        SessionProperties sessionProperties = new SessionProperties();
        UserInterfaceFactory userInterfaceFactory = sessionProperties.getUserInterfaceFactory();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        JButton exportVariableDataButton = workBench.exportVariableData;
        assertNotNull(exportVariableDataButton);
        assertEquals("Export Variable Data", exportVariableDataButton.getText());
    }

    @Test
    public void testExitButton() {
        SessionProperties sessionProperties = new SessionProperties();
        UserInterfaceFactory userInterfaceFactory = sessionProperties.getUserInterfaceFactory();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        JButton exitButton = workBench.exit;
        assertNotNull(exitButton);
        assertEquals("Exit", exitButton.getText());
    }

    @Test
    public void testLoginDialog() {
        SessionProperties sessionProperties = new SessionProperties();
        try {
            MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
            LoginDialog loginDialog = workBench.dialog;
            assertNotNull(loginDialog);
        } catch (Exception e) {
            fail("LoginDialog should be displayed during MacawWorkBench construction");
        }
    }
}