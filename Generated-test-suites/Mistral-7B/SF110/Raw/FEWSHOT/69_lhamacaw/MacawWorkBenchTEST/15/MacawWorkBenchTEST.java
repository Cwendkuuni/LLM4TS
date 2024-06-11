package macaw.presentationLayer;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@RunWith(MockitoJUnitRunner.class)
public class MacawWorkBenchTest {

    @Test
    public void testMacawWorkBenchCreation() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        assertNotNull(macawWorkBench.dialog);
        assertEquals("macawWorkBench.title", macawWorkBench.dialog.getTitle());
    }

    @Test
    public void testCreateButtonPanel() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        MacawWorkBench macawWorkBench = new MacawWorkBench(Mockito.mock(SessionProperties.class));
        JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
        assertNotNull(buttonPanel);
        int numberOfButtons = 4;
        assertEquals(numberOfButtons, buttonPanel.getComponentCount());
    }

    @Test
    public void testEditConstantsButton() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        macawWorkBench.editConstants.addActionListener(e -> macawWorkBench.editConstants());
        macawWorkBench.editConstants(actionEvent);
        assertEquals(1, macawWorkBench.sessionProperties.getNumberOfConstantsEditCalls());
    }

    @Test
    public void testEditVariablesButton() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        macawWorkBench.editVariables.addActionListener(e -> macawWorkBench.editVariables());
        macawWorkBench.editVariables(actionEvent);
        assertEquals(1, macawWorkBench.sessionProperties.getNumberOfVariablesEditCalls());
    }

    @Test
    public void testExportVariableDataButton() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        macawWorkBench.exportVariableData.addActionListener(e -> macawWorkBench.exportVariableData());
        macawWorkBench.exportVariableData(actionEvent);
        ExportVariablesToMacawXML exportToMacawXML = Mockito.mock(ExportVariablesToMacawXML.class);
        Mockito.when(sessionProperties.getDatabase()).thenReturn(Mockito.mock(Database.class));
        Mockito.when(sessionProperties.getRetrievalService()).thenReturn(Mockito.mock(RetrievalService.class));
        Mockito.when(sessionProperties.getDatabase().exportVariablesToMacawXML()).thenReturn(exportToMacawXML);
        macawWorkBench.sessionProperties.setProperty("database", Mockito.mock(Database.class));
        macawWorkBench.sessionProperties.setProperty("retrievalService", Mockito.mock(RetrievalService.class));
        macawWorkBench.exportVariableData();
        Mockito.verify(exportToMacawXML).export();
    }

    @Test
    public void testExitButton() {
        UserInterfaceFactory userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        macawWorkBench.exit.addActionListener(e -> macawWorkBench.exit());
        macawWorkBench.exit(actionEvent);
        System.exit(0);
    }
}