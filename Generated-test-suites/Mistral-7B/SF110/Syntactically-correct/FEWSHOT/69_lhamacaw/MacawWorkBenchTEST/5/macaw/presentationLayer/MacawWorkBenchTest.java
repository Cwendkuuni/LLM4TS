package macaw.presentationLayer;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class MacawWorkBenchTest {

    @Test
    public void testMain() {
        // This test case is not possible to implement as the main method is not testable,
        // instead test the individual methods and constructors
    }

    @Test
    public void testMacawWorkBench() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        assertNotNull(macawWorkBench.dialog);
        assertEquals("macawWorkBench.title", macawWorkBench.dialog.getTitle());
    }

    @Test
    public void testCreateButtonPanel() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);

        // Check number of components
        int numberOfComponents = buttonPanel.getComponentCount();
        assertEquals(4, numberOfComponents);

        // Check button labels
        JButton editConstantsButton = (JButton) buttonPanel.getComponent(0);
        assertEquals("macawWorkBench.editConstants", editConstantsButton.getText());

        JButton editVariablesButton = (JButton) buttonPanel.getComponent(1);
        assertEquals("macawWorkBench.editVariables", editVariablesButton.getText());

        JButton exportVariableDataButton = (JButton) buttonPanel.getComponent(2);
        assertEquals("macawWorkBench.exportVariableData", exportVariableDataButton.getText());

        JButton exitButton = (JButton) buttonPanel.getComponent(3);
        assertEquals("general.buttons.exit", exitButton.getText());
    }

    @Test
    public void testActionPerformed() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        ActionEvent event = Mockito.mock(ActionEvent.class);

        // Check that the correct method is called when a button is pressed
        macawWorkBench.actionPerformed(event);
        verify(macawWorkBench).editConstants();
        reset(macawWorkBench);

        macawWorkBench.editVariables = Mockito.mock(JButton.class);
        macawWorkBench.exportVariableData = Mockito.mock(JButton.class);

        macawWorkBench.actionPerformed(event);
        verify(macawWorkBench.editVariables).doClick();
        reset(macawWorkBench);

        macawWorkBench.actionPerformed(event);
        verify(macawWorkBench.exportVariableData).doClick();
        reset(macawWorkBench);

        macawWorkBench.exit = Mockito.mock(JButton.class);
        macawWorkBench.actionPerformed(event);
        verify(macawWorkBench.exit).doClick();
    }

    @Test
    public void testEditConstants() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        VariableConstantsEditor variableConstantsEditor = Mockito.mock(VariableConstantsEditor.class);
        when(variableConstantsEditor.show()).thenReturn(true);

        macawWorkBench.editConstants();
        verify(macawWorkBench).editConstants();
        verify(macawWorkBench).showVariableConstantsEditor();
        reset(macawWorkBench);

        macawWorkBench.sessionProperties = Mockito.mock(SessionProperties.class);
        macawWorkBench.editConstants();
        verify(macawWorkBench.sessionProperties).showVariableConstantsEditor();
    }

    @Test
    public void testEditVariables() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        MacawVariableBrowser macawVariableBrowser = Mockito.mock(MacawVariableBrowser.class);
        when(macawVariableBrowser.show()).thenReturn(true);

        macawWorkBench.editVariables();
        verify(macawWorkBench).editVariables();
        verify(macawWorkBench).showMacawVariableBrowser();
        reset(macawWorkBench);

        macawWorkBench.sessionProperties = Mockito.mock(SessionProperties.class);
        macawWorkBench.editVariables();
        verify(macawWorkBench.sessionProperties).showMacawVariableBrowser();
    }

    @Test
    public void testExportVariableData() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        ExportVariablesToMacawXML exportToMacawXML = Mockito.mock(ExportVariablesToMacawXML.class);
        when(exportToMacawXML.export()).thenReturn(true);

        macawWorkBench.exportVariableData();
        verify(macawWorkBench).exportVariableData();
        verify(macawWorkBench).exportVariablesToMacawXML();
        reset(macawWorkBench);

        macawWorkBench.sessionProperties = Mockito.mock(SessionProperties.class);
        macawWorkBench.exportVariableData();
        verify(macawWorkBench.sessionProperties).exportVariablesToMacawXML();
    }

    @Test
    public void testExit() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        macawWorkBench.exit();
        verify(macawWorkBench).exit();
        System.exit(0);
    }

    @Test
    public void testShowDialog() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        MacawWorkBench macawWorkBench = new MacawWorkBench(null);
        JDialog dialog = macawWorkBench.dialog;

        macawWorkBench.show();
        verify(dialog).setVisible(true);
        reset(dialog);

        macawWorkBench.sessionProperties = Mockito.mock(SessionProperties.class);
        macawWorkBench.show();
        verify(userInterfaceFactory).createDialog();
        reset(userInterfaceFactory);

        macawWorkBench.userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        macawWorkBench.show();
        verify(macawWorkBench.userInterfaceFactory).createDialog();
    }
}