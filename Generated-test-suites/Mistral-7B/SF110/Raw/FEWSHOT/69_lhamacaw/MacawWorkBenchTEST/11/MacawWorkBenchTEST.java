package macaw.presentationLayer;

import static org.junit.Assert.*;
import org.junit.Test;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.production.ProductionCurationService;
import macaw.persistenceLayer.ProductionRetrievalService;
import macaw.persistenceLayer.demo.DemonstrationRetrievalService;
import macaw.system.MacawMessages;
import macaw.system.SessionProperties;
import macaw.system.User;
import macaw.system.UserInterfaceFactory;
import javax.swing.*;
import javax.swing.event.ActionEvent;
import java.awt.*;
import java.awt.event.ActionListener;

public class MacawWorkBenchTest {

    @Test
    public void testMacawWorkBench() {
        SessionProperties sessionProperties = new SessionProperties();
        UserInterfaceFactory userInterfaceFactory = new UserInterfaceFactory();

        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);

        assertEquals("macawWorkBench.title", macawWorkBench.dialog.getTitle());

        JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
        assertTrue(buttonPanel.getComponentCount() == 5);
    }

    @Test
    public void testCreateButtonPanel() {
        SessionProperties sessionProperties = new SessionProperties();
        UserInterfaceFactory userInterfaceFactory = new UserInterfaceFactory();

        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);

        JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);

        JButton editConstantsButton = (JButton) buttonPanel.getComponent(0);
        assertEquals("macawWorkBench.editConstants", editConstantsButton.getText());

        JButton editVariablesButton = (JButton) buttonPanel.getComponent(1);
        assertEquals("macawWorkBench.editVariables", editVariablesButton.getText());

        JButton exportVariableDataButton = (JButton) buttonPanel.getComponent(2);
        assertEquals("macawWorkBench.exportVariableData", exportVariableDataButton.getText());

        JButton exitButton = (JButton) buttonPanel.getComponent(4);
        assertEquals("general.buttons.exit", exitButton.getText());
    }

    @Test
    public void testActionPerformed() {
        SessionProperties sessionProperties = new SessionProperties();
        UserInterfaceFactory userInterfaceFactory = new UserInterfaceFactory();

        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        macawWorkBench.editConstants = mock(JButton.class);
        macawWorkBench.editVariables = mock(JButton.class);
        macawWorkBench.exportVariableData = mock(JButton.class);
        macawWorkBench.exit = mock(JButton.class);

        ActionEvent editConstantsEvent = mock(ActionEvent.class);
        ActionEvent editVariablesEvent = mock(ActionEvent.class);
        ActionEvent exportVariableDataEvent = mock(ActionEvent.class);
        ActionEvent exitEvent = mock(ActionEvent.class);

        when(macawWorkBench.editConstants.getSource()).thenReturn(macawWorkBench.editConstants);
        when(macawWorkBench.editVariables.getSource()).thenReturn(macawWorkBench.editVariables);
        when(macawWorkBench.exportVariableData.getSource()).thenReturn(macawWorkBench.exportVariableData);
        when(macawWorkBench.exit.getSource()).thenReturn(macawWorkBench.exit);

        macawWorkBench.actionPerformed(editConstantsEvent);
        macawWorkBench.editConstants();

        macawWorkBench.actionPerformed(editVariablesEvent);
        macawWorkBench.editVariables();

        macawWorkBench.actionPerformed(exportVariableDataEvent);
        new ExportVariablesToMacawXML(sessionProperties).export();

        macawWorkBench.actionPerformed(exitEvent);
        System.exit(0);
    }

    @Test
    public void testMain() {
        String[] arguments = {"--dbuser", "testUser", "--dbpassword", "testPassword"};
        SessionProperties sessionProperties = new SessionProperties();
        User currentUser = new User("jsmith", "cool");
        sessionProperties.setProperty("currentUser", currentUser);

        try {
            MacawWorkBench.main(arguments);
            fail("Should have thrown an exception");
        } catch (Exception e) {
            // Expected
        }

        sessionProperties.setProperty("startupOptions", new StartupOptions());
        sessionProperties.getStartupOptions().processCommandLineArguments(arguments);

        DemonstrationCurationService db = new DemonstrationCurationService(false);
        sessionProperties.setProperty("database", db);
        sessionProperties.setProperty("retrievalService", new DemonstrationRetrievalService(sessionProperties));

        MacawWorkBench.main(arguments);
    }
}