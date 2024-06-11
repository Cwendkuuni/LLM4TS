package macaw.presentationLayer;

import static org.junit.Assert.*;
import static macaw.businessLayer.MacawMessages.*;

import macaw.businessLayer.*;
import macaw.presentationLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import javax.swing.*;
import javax.swing.border.*;
import macaw.io.*;
import java.awt.*;
import java.awt.event.*;

public class MacawWorkBenchTest {

    @Test
    public void testMain() {
        SessionProperties sessionProperties = new SessionProperties();
        String[] arguments = new String[0];
        MacawWorkBench.main(arguments);
        assertNotNull(MacawWorkBench.sessionProperties);
    }

    @Test
    public void testCreateDialog() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        assertNotNull(workBench.dialog);
        assertEquals(MacawMessages.getMessage("macawWorkBench.title"), workBench.dialog.getTitle());
    }

    @Test
    public void testCreateButtonPanel() {
        SessionProperties sessionProperties = new SessionProperties();
        UserInterfaceFactory userInterfaceFactory = new StubUserInterfaceFactory();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        JPanel buttonPanel = workBench.createButtonPanel(userInterfaceFactory);
        assertEquals(5, buttonPanel.getComponentCount());

        JButton editConstants = workBench.editConstants;
        JButton editVariables = workBench.editVariables;
        JButton exportVariableData = workBench.exportVariableData;
        JButton exit = workBench.exit;

        assertNotNull(editConstants);
        assertNotNull(editVariables);
        assertNotNull(exportVariableData);
        assertNotNull(exit);

        assertEquals(MacawMessages.getMessage("macawWorkBench.editConstants"), editConstants.getText());
        assertEquals(MacawMessages.getMessage("macawWorkBench.editConstants.instructions"), editConstants.getToolTipText());
        assertEquals(MacawMessages.getMessage("macawWorkBench.editVariables"), editVariables.getText());
        assertEquals(MacawMessages.getMessage("macawWorkBench.editVariables.instructions"), editVariables.getToolTipText());
        assertEquals(MacawMessages.getMessage("macawWorkBench.exportVariableData"), exportVariableData.getText());
        assertEquals(MacawMessages.getMessage("macawWorkBench.exportVariableData.instructions"), exportVariableData.getToolTipText());
        assertEquals(MacawMessages.getMessage("general.buttons.exit"), exit.getText());

        ActionListener actionListener = mock(ActionListener.class);
        editConstants.addActionListener(actionListener);
        editVariables.addActionListener(actionListener);
        exportVariableData.addActionListener(actionListener);
        exit.addActionListener(actionListener);
        verify(actionListener, times(4)).actionPerformed(any(ActionEvent.class));
    }

    @Test
    public void testShowDialog() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        workBench.show();
        assertTrue(workBench.dialog.isVisible());
    }

    @Test
    public void testEditConstants() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        workBench.editConstants();
        assertTrue(workBench.sessionProperties.getPropertyNames().contains("constantEditor"));
    }

    @Test
    public void testEditVariables() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        workBench.editVariables();
        assertTrue(workBench.sessionProperties.getPropertyNames().contains("variableBrowser"));
    }

    @Test
    public void testExportVariableData() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        workBench.exportVariableData();
        assertTrue(workBench.sessionProperties.getPropertyNames().contains("exportToDSSFormat"));
    }

    @Test
    public void testExit() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        workBench.exit();
        assertTrue(workBench.dialog.isVisible());
        System.exitCalledaTime();
        fail("System.exit() was called unexpectedly");
    }

    @Test
    public void testActionPerformed() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        ActionEvent actionEvent = mock(ActionEvent.class);
        workBench.actionPerformed(actionEvent);
        verify(workBench, times(4)).actionPerformed(actionEvent);
    }

    @Test
    public void testMainWithCommandLineArguments() {
        SessionProperties sessionProperties = new SessionProperties();
        String[] arguments = new String[]{"--useDemo", "--database", "demo"};
        MacawWorkBench.main(arguments);
        assertTrue(MacawWorkBench.sessionProperties.getPropertyNames().contains("database"));
        assertTrue(MacawWorkBench.sessionProperties.getProperty("database") instanceof DemonstrationCurationService);
    }
}