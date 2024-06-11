import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

import org.mockito.MockitoAnnotations;

public class jgaapGUITest {

    private jgaapGUI gui;

    @Test
    public void testConstructor() {
        gui = new jgaapGUI();
        assertNotNull(gui);
    }

    @Test
    public void testInitComponents() {
        gui = new jgaapGUI();
        verify(gui, times(1)).initComponents();
    }

    @Test
    public void testActionPerformed_Exit() {
        gui = new jgaapGUI();
        ActionEvent actionEvent = mock(ActionEvent.class);
        gui.actionPerformed(actionEvent);
        verify(gui).Systemexit(-1);
    }

    @Test
    public void testActionPerformed_ImportKnownDocument() {
        gui = new jgaapGUI();
        ActionEvent actionEvent = mock(ActionEvent.class);
        File file = mock(File.class);
        when(gui.chooseFile.getSelectedFile()).thenReturn(file);
        gui.actionPerformed(actionEvent);
        verify(gui.driver).addDocument(eq(file.toString()), anyString());
        verify(gui.listKnown).append(anyString());
    }

    @Test
    public void testActionPerformed_ImportUnknownDocument() {
        gui = new jgaapGUI();
        ActionEvent actionEvent = mock(ActionEvent.class);
        File file = mock(File.class);
        when(gui.chooseFile.getSelectedFile()).thenReturn(file);
        gui.actionPerformed(actionEvent);
        verify(gui.listUnknown).append(anyString());
    }

    @Test
    public void testActionPerformed_Canonicize() {
        gui = new jgaapGUI();
        ActionEvent actionEvent = mock(ActionEvent.class);
        gui.actionPerformed(actionEvent);
        guiDriver preprocessEngineDriver = mock(guiDriver.preprocessEngineDriver.class);
        when(gui.driver.preprocessEngineDriver(anyArray(), any(), anyProgressBar())).thenReturn(preprocessEngineDriver);
        preprocessEngineDriver.start();
        verify(gui).progressCanonicize.setIndeterminate(true);
        verify(gui).progressCanonicize.setValue(0);
        verify(gui).progressCanonicize.setMaximum(100);
        verify(gui).buttonCanonicize.setText("Cancel");
    }

    @Test
    public void testActionPerformed_CreateEventSet() {
        gui = new jgaapGUI();
        ActionEvent actionEvent = mock(ActionEvent.class);
        gui.actionPerformed(actionEvent);
        guiDriver createEventSetDriver = mock(guiDriver.createEventSetDriver.class);
        when(gui.driver.createEventSetDriver(eq("characters"))).thenReturn(createEventSetDriver);
        createEventSetDriver.start();
        verify(gui).progressEventSet.setIndeterminate(true);
        verify(gui).progressEventSet.setValue(0);
        verify(gui).progressEventSet.setMaximum(100);
        verify(gui).buttonEventSet.setText("Cancel");
    }

    @Test
    public void testActionPerformed_Analyze() {
        gui = new jgaapGUI();
        ActionEvent actionEvent = mock(ActionEvent.class);
        gui.actionPerformed(actionEvent);
        guiDriver runStatisticalAnalysisDriver = mock(guiDriver.runStatisticalAnalysisDriver.class);
        when(gui.driver.runStatisticalAnalysisDriver(eq("crossentropy"))).thenReturn(runStatisticalAnalysisDriver);
        runStatisticalAnalysisDriver.start();
        String results = "Results: ";
        verify(gui.listResults).append(results);
    }

    @Test
    public void testActionPerformed_LoadDemo() {
        gui = new jgaapGUI();
        ActionEvent actionEvent = mock(ActionEvent.class);
        gui.actionPerformed(actionEvent);
        verify(gui).loadDemo();
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
}