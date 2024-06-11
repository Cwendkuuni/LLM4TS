package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class FontChooserDialogTest {

    private ActionListener okListener;
    private ActionListener cancelListener;
    private Component c;
    private JFontChooser chooserPane;
    private FontChooserDialog fontChooserDialog;

    @Before
    public void setUp() {
        c = Mockito.mock(Component.class);
        chooserPane = Mockito.mock(JFontChooser.class);
        okListener = mock(ActionListener.class);
        cancelListener = mock(ActionListener.class);
        fontChooserDialog = new FontChooserDialog(c, "Test Dialog", true, chooserPane, okListener, cancelListener);
    }

    @Test
    public void testFontChooserDialogConstructor() {
        assertEquals("Test Dialog", fontChooserDialog.getTitle());
        assertTrue(fontChooserDialog.isModal());
        assertEquals(chooserPane, fontChooserDialog.getChooserPane());
        assertEquals(okListener, fontChooserDialog.getOkButton().getActionListeners()[0]);
        assertEquals(cancelListener, fontChooserDialog.getCancelButton().getActionListeners()[0]);
        assertEquals("OK", fontChooserDialog.getOkButton().getText());
        assertEquals("Cancel", fontChooserDialog.getCancelButton().getText());
    }

    @Test
    public void testSetVisible() {
        assertNull(fontChooserDialog.getInitialFont());
        fontChooserDialog.setVisible(true);
        assertEquals(fontChooserDialog.getChooserPane().getFont(), fontChooserDialog.getInitialFont());
        fontChooserDialog.setVisible(false);
        verify(c, times(2)).setFocusableWindowState(true);
    }

    @Test
    public void testReset() {
        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        when(chooserPane.getFont()).thenReturn(initialFont);
        fontChooserDialog.setVisible(true);
        fontChooserDialog.reset();
        assertEquals(initialFont, fontChooserDialog.getChooserPane().getFont());
    }

    @Test
    public void testOkButtonActionPerformed() {
        ActionEvent actionEvent = mock(ActionEvent.class);
        fontChooserDialog.getOkButton().actionPerformed(actionEvent);
        verify(okListener).actionPerformed(actionEvent);
        verify(fontChooserDialog).setVisible(false);
    }

    @Test
    public void testCancelButtonActionPerformed() {
        ActionEvent actionEvent = mock(ActionEvent.class);
        fontChooserDialog.getCancelButton().actionPerformed(actionEvent);
        verify(cancelListener).actionPerformed(actionEvent);
        verify(fontChooserDialog).setVisible(false);
    }
}