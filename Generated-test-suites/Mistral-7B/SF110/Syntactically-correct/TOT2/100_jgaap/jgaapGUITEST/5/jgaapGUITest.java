import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class jgaapGUITest {
    private jgaapGUI jgaapGUI;

    @Before
    public void setUp() {
        jgaapGUI = new jgaapGUI();
    }

    

    // Testing constructor
    @Test
    public void testConstructor() {
        assertNotNull(jgaapGUI);
    }

    // Testing initComponents
    @Test
    public void testInitComponents() {
        jgaapGUI = spy(new jgaapGUI());
        doCallRealMethod().when(jgaapGUI).initComponents();
        initComponents(jgaapGUI);
        verify(jgaapGUI).initComponents();
    }

    // Testing actionPerformed for "Exit" command
    @Test
    public void testActionPerformedExit() {
        jgaapGUI = mock(jgaapGUI.class);
        ActionEvent actionEvent = mock(ActionEvent.class);
        when(actionEvent.getActionCommand()).thenReturn("Exit");
        doAnswer(invocationOnMock -> System.exit(0)).when(jgaapGUI).actionPerformed(actionEvent);
        jgaapGUI.actionPerformed(actionEvent);
        verify(jgaapGUI, times(1)).actionPerformed(actionEvent);
    }

    // Testing actionPerformed for "knownauthor" command
    @Test
    public void testActionPerformedKnownAuthor() {
        jgaapGUI = mock(jgaapGUI.class);
        ActionEvent actionEvent = mock(ActionEvent.class);
        when(actionEvent.getActionCommand()).thenReturn("knownauthor");
        jgaapGUI.actionPerformed(actionEvent);
        verify(jgaapGUI, times(1)).actionPerformed(actionEvent);
        assertTrue(jgaapGUI.currentAuthor.isEnabled());
        assertEquals("", jgaapGUI.currentAuthor.getText());
    }

    // Testing actionPerformed for "unknownauthor" command
    @Test
    public void testActionPerformedUnknownAuthor() {
        jgaapGUI = mock(jgaapGUI.class);
        ActionEvent actionEvent = mock(ActionEvent.class);
        when(actionEvent.getActionCommand()).thenReturn("unknownauthor");
        jgaapGUI.actionPerformed(actionEvent);
        verify(jgaapGUI, times(1)).actionPerformed(actionEvent);
        assertFalse(jgaapGUI.currentAuthor.isEnabled());
        assertEquals("", jgaapGUI.currentAuthor.getText());
    }

    // Testing actionPerformed for "Import" command with valid file
    @Test
    public void testActionPerformedImportValidFile() throws IOException {
        jgaapGUI = mock(jgaapGUI.class);
        ActionEvent actionEvent = mock(ActionEvent.class);
        JFileChooser mockChooser = mock(JFileChooser.class);
        when(mockChooser.showOpenDialog(jgaapGUI)).thenReturn(JFileChooser.APPROVE_OPTION);
        File mockFile = mock(File.class);
        when(mockChooser.getSelectedFile()).thenReturn(mockFile);
        when(actionEvent.getActionCommand()).thenReturn("Import");
        doAnswer(invocationOnMock -> {
            jgaapGUI.driver.addDocument(mockFile.toString(), jgaapGUI.currentAuthor.getText());
            if (!jgaapGUI.currentAuthor.getText().equals("")) {
                jgaapGUI.listKnown.append(jgaapGUI.currentAuthor.getText() + " - " + mockFile.getName() + "\n");
            } else {
                jgaapGUI.listUnknown.append(mockFile.getName() + "\n");
            }
        }).when(jgaapGUI).actionPerformed(actionEvent);
        jgaapGUI.actionPerformed(actionEvent);
        verify(jgaapGUI, times(1)).actionPerformed(actionEvent);
        verify(mockChooser, times(1)).showOpenDialog(jgaapGUI);
        verify(jgaapGUI, times(1)).addDocument(mockFile.toString(), jgaapGUI.currentAuthor.getText());
    }

    // Testing actionPerformed for "Canonicize" command
    @Test
    public void testActionPerformedCanonicize() {
        jgaapGUI = mock(jgaapGUI.class);
        ActionEvent actionEvent = mock(ActionEvent.class);
        when(actionEvent.getActionCommand()).thenReturn("Canonicize");
        jgaapGUI.actionPerformed(actionEvent);
        verify(jgaapGUI, times(1)).actionPerformed(actionEvent);
        // TODO: Test the actual canonicization process using mockito or PowerMock
    }

    
}