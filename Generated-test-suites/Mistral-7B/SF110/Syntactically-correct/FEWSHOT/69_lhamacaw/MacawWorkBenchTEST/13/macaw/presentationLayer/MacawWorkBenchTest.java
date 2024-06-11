package macaw.presentationLayer;

import static org.junit.Assert.*;
import org.junit.Test;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.*;

public class MacawWorkBenchTest {

    @Test
    public void testMacawWorkBenchCreation() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
        assertNotNull(workBench.dialog);
        assertEquals("macawWorkBench.title", workBench.dialog.getTitle());
    }

    @Test
    public void testCreateButtonPanel() {
        UserInterfaceFactory userInterfaceFactory = MockUserInterfaceFactory.getInstance();
        MacawWorkBench workBench = new MacawWorkBench(new SessionProperties());
        JPanel buttonPanel = workBench.createButtonPanel(userInterfaceFactory);

        int buttonsCount = 4;
        assertEquals(buttonsCount, buttonPanel.getComponentCount());

        for (int i = 0; i < buttonsCount; i++) {
            JButton button = (JButton) buttonPanel.getComponent(i);
            assertNotNull(button);
        }
    }

    @Test
    public void testActionPerformed() {
        UserInterfaceFactory userInterfaceFactory = MockUserInterfaceFactory.getInstance();
        MacawWorkBench workBench = new MacawWorkBench(new SessionProperties());
        JButton button = MockJButton.createMockButton();
        workBench.editConstants = button;

        ActionEvent event = MockActionEvent.createMockActionEvent();
        workBench.actionPerformed(event);

        verify(workBench).editConstants();
    }

    @Test
    public void testShow() {
        UserInterfaceFactory userInterfaceFactory = MockUserInterfaceFactory.getInstance();
        MacawWorkBench workBench = new MacawWorkBench(new SessionProperties());

        SwingUtilities.invokeLater(() -> workBench.dialog.setVisible(true));
        assertTrue(workBench.dialog.isVisible());
    }

    @Test
    public void testEditConstants() {
        MacawWorkBench workBench = new MacawWorkBench(new SessionProperties());
        workBench.editConstants();

        VariableConstantsEditor editor = MockVariableConstantsEditor.createMockEditor();
        verify(workBench).showVariableConstantsEditor(editor);
    }

    @Test
    public void testEditVariables() {
        MacawWorkBench workBench = new MacawWorkBench(new SessionProperties());
        workBench.editVariables();

        MacawVariableBrowser browser = MockMacawVariableBrowser.createMockBrowser();
        verify(workBench).showVariableBrowser(browser);
    }

    @Test
    public void testExportVariableData() {
        MacawWorkBench workBench = new MacawWorkBench(new SessionProperties());
        workBench.exportVariableData();

        ExportVariablesToMacawXML exportToDSSFormat = MockExportVariablesToMacawXML.createMockExportToDSSFormat();
        verify(workBench).exportVariableDataToXML(exportToDSSFormat);
    }

    @Test
    public void testExit() {
        MacawWorkBench workBench = new MacawWorkBench(new SessionProperties());
        workBench.exit();

        verify(workBench).quitApplication();
    }
}