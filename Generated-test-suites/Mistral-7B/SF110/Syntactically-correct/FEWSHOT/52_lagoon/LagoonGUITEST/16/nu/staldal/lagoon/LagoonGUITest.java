package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;

public class LagoonGUITest {

    @Test
    public void testInit() {
        LagoonGUI appFrame = new LagoonGUI();
        assertNull(appFrame.getProcessor());
        appFrame.init();
        assertNotNull(appFrame.getProcessor());
    }

    @Test
    public void testLoadButtonActionPerformed() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.loadPropertyFile = mock(File.class);
        when(appFrame.loadPropertyFile.exists()).thenReturn(true);
        appFrame.loadButton.actionPerformed(mock(ActionEvent.class));
        verify(appFrame).loadProperties(appFrame.loadPropertyFile);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadButtonActionPerformedWithInvalidFile() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.loadPropertyFile = mock(File.class);
        when(appFrame.loadPropertyFile.exists()).thenReturn(false);
        appFrame.loadButton.actionPerformed(mock(ActionEvent.class));
    }

    @Test
    public void testSaveButtonActionPerformed() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.savePropertyFile = mock(FileDialog.class);
        when(appFrame.savePropertyFile.getFile()).thenReturn(new File("test.properties"));
        appFrame.saveButton.actionPerformed(mock(ActionEvent.class));
        verify(appFrame).saveProperties(appFrame.savePropertyFile);
    }

    @Test
    public void testBuildButtonActionPerformedWithoutForce() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.init();
        appFrame.processor = mock(LagoonProcessor.class);
        appFrame.buildButton.actionPerformed(mock(ActionEvent.class));
        verify(appFrame.processor).build(false);
    }

    @Test
    public void testBuildButtonActionPerformedWithForce() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.init();
        appFrame.processor = mock(LagoonProcessor.class);
        appFrame.buildButton.actionPerformed(mock(ActionEvent.class));
        verify(appFrame.processor).build(true);
    }

    @Test
    public void testExitButtonActionPerformed() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.exitButton.actionPerformed(mock(ActionEvent.class));
        verify(appFrame).exit();
    }
}