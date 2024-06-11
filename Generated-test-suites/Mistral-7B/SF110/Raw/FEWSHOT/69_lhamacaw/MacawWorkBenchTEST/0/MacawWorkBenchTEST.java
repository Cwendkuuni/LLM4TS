package macaw.presentationLayer;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;
import java.util.Properties;

@RunWith(MockitoJUnitRunner.class)
public class MacawWorkBenchTest {

    @Test
    public void testMacawWorkBenchConstruction() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        assertEquals("macawWorkBench.title", macawWorkBench.dialog.getTitle());

        JPanel panel = macawWorkBench.dialog.getContentPane();
        JTextArea instructionsArea = (JTextArea) panel.getComponent(0);
        assertEquals("macawWorkBench.instructions", instructionsArea.getText());

        JPanel buttonPanel = (JPanel) panel.getComponent(1);
        assertEquals(4, buttonPanel.getComponentCount());

        JButton editConstants = macawWorkBench.editConstants;
        assertNotNull(editConstants);
        assertEquals("macawWorkBench.editConstants", editConstants.getText());

        JButton editVariables = macawWorkBench.editVariables;
        assertNotNull(editVariables);
        assertEquals("macawWorkBench.editVariables", editVariables.getText());

        JButton exportVariableData = macawWorkBench.exportVariableData;
        assertNotNull(exportVariableData);
        assertEquals("macawWorkBench.exportVariableData", exportVariableData.getText());

        JButton exit = macawWorkBench.exit;
        assertNotNull(exit);
        assertEquals("general.buttons.exit", exit.getText());
    }

    @Test
    public void testEditConstantsButton() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        Mockito.verify(userInterfaceFactory).createDialog();
        Mockito.verify(userInterfaceFactory).createButton(eq("macawWorkBench.editConstants"));
        MacawWorkBench macawWorkBenchSpy = Mockito.spy(macawWorkBench);
        macawWorkBenchSpy.editConstants.doClick();
        Mockito.verify(macawWorkBenchSpy).editConstants();
    }

    @Test
    public void testEditVariablesButton() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        Mockito.verify(userInterfaceFactory).createDialog();
        Mockito.verify(userInterfaceFactory).createButton(eq("macawWorkBench.editVariables"));
        MacawWorkBench macawWorkBenchSpy = Mockito.spy(macawWorkBench);
        macawWorkBenchSpy.editVariables.doClick();
        Mockito.verify(macawWorkBenchSpy).editVariables();
    }

    @Test
    public void testExportVariableDataButton() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        Mockito.verify(userInterfaceFactory).createDialog();
        Mockito.verify(userInterfaceFactory).createButton(eq("macawWorkBench.exportVariableData"));
        MacawWorkBench macawWorkBenchSpy = Mockito.spy(macawWorkBench);
        macawWorkBenchSpy.exportVariableData.doClick();
        Mockito.verify(macawWorkBenchSpy).exportVariableData();
    }

    @Test
    public void testExitButton() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        Mockito.verify(userInterfaceFactory).createDialog();
        Mockito.verify(userInterfaceFactory).createButton(eq("general.buttons.exit"));
        MacawWorkBench macawWorkBenchSpy = Mockito.spy(macawWorkBench);
        macawWorkBenchSpy.exit.doClick();
        Mockito.verify(macawWorkBenchSpy).exit();
        System.exit(0);
    }

    @Test
    public void testMainMethod() {
        String[] arguments = {"--arg1", "--arg2"};
        String dbUser = null;
        String dbPassword = null;
        SessionProperties sessionProperties = new SessionProperties();
        StartupOptions startupOptions = new StartupOptions();
        User currentUser = new User("jsmith", "cool");
        sessionProperties.setProperty("currentUser", currentUser);
        Properties startupProperties = new Properties();
        startupProperties.setProperty("startupOptions", startupOptions.toString());
        sessionProperties.setProperty("startupOptions", startupProperties);

        String dbType = "demo";
        if (!startupOptions.useDemo()) {
            dbType = "production";
        }

        try {
            if (dbType.equalsIgnoreCase("demo")) {
                DemonstrationCurationService database = new DemonstrationCurationService(false);
                DemonstrationRetrievalService retrievalService = new DemonstrationRetrievalService(sessionProperties);
                ProductionRetrievalService macawRetrievalAPI = null;
                sessionProperties.setProperty("database", database);
                sessionProperties.setProperty("retrievalService", retrievalService);
            } else {
                ProductionCurationService database = new ProductionCurationService(sessionProperties);
                ProductionRetrievalService macawRetrievalAPI = new ProductionRetrievalService(sessionProperties);
                sessionProperties.setProperty("database", database);
                sessionProperties.setProperty("retrievalService", macawRetrievalAPI);
            }

            MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
            macawWorkBench.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}