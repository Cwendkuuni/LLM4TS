package macaw.presentationLayer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import org.junit.Test;
import javax.swing.*;

public class MacawWorkBenchTest {

    @Test
    public void testMacawWorkBenchCreation() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        assertNotNull(workBench);
        assertTrue(workBench instanceof MacawWorkBench);
    }

    @Test
    public void testCreateButtonPanel() {
        UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
        MacawWorkBench workBench = new MacawWorkBench(mock(SessionProperties.class));
        JPanel buttonPanel = workBench.createButtonPanel(userInterfaceFactory);

        int numberOfComponents = buttonPanel.getComponentCount();
        for (int i = 0; i < numberOfComponents; ++i) {
            JButton button = (JButton) buttonPanel.getComponent(i);
            assertTrue(button.getModel().isEnabled());
        }
    }

    @Test
    public void testEditConstantsButton() {
        UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        workBench.createButtonPanel(userInterfaceFactory);
        workBench.editConstants();
        verify(userInterfaceFactory).createDialog();
    }

    @Test
    public void testEditVariablesButton() {
        UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        workBench.createButtonPanel(userInterfaceFactory);
        workBench.editVariables();
        verify(userInterfaceFactory).createDialog();
    }

    @Test
    public void testExportVariableDataButton() {
        UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        workBench.createButtonPanel(userInterfaceFactory);
        workBench.exportVariableData();
        verify(userInterfaceFactory).createFileDialog(JFileChooser.SAVE_DIALOG);
    }

    @Test
    public void testExitButton() {
        UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        workBench.createButtonPanel(userInterfaceFactory);
        workBench.exit();
        verify(workBench).isVisible();
        verify(workBench).setVisible(false);
        verify(System.out).println(isA(PrintStream.class));
    }
}