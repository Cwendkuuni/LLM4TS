package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.Before;
import org.junit.Test;

public class FontChooserDialogTest {

    private JFrame frame;
    private Component parentComponent;
    private ActionListener okListener;
    private ActionListener cancelListener;
    private JFontChooser chooserPane;
    private FontChooserDialog dialog;

    @Before
    public void setUp() {
        parentComponent = mock(Component.class);
        okListener = mock(ActionListener.class);
        cancelListener = mock(ActionListener.class);
        chooserPane = mock(JFontChooser.class);
        dialog = new FontChooserDialog(parentComponent, "Font Dialog", true, chooserPane, okListener, cancelListener);
        frame = dialog.getFrameForComponent(parentComponent);
    }

    @Test
    public void testFontChooserDialogConstruction() {
        assertEquals("Font Dialog", dialog.getTitle());
        assertTrue(dialog.isModal());
        assertNotNull(dialog.getRootPane());
        assertNotNull(dialog.getContentPane());
        assertNotNull(dialog.getButtonPane());
        assertNotNull(dialog.getOkButton());
        assertNotNull(dialog.getCancelButton());
        assertNotNull(dialog.getResetButton());
        assertNull(dialog.getInitialFont());
    }

    @Test
    public void testSetVisibleWithInitialFont() {
        when(chooserPane.getFont()).thenReturn(new Font("Arial", Font.BOLD, 12));

        dialog.setVisible(true);

        assertEquals(new Font("Arial", Font.BOLD, 12), dialog.getInitialFont());
        verify(okListener).actionPerformed(any(ActionEvent.class));
        verify(cancelListener).actionPerformed(any(ActionEvent.class));
    }

    @Test
    public void testSetVisibleWithoutInitialFont() {
        dialog.setVisible(true);

        assertNull(dialog.getInitialFont());
        verifyZeroInteractions(okListener);
        verifyZeroInteractions(cancelListener);
    }

    @Test
    public void testResetMethod() {
        Font initialFont = new Font("Arial", Font.BOLD, 12);
        dialog.setInitialFont(initialFont);

        dialog.reset();

        assertEquals(initialFont, dialog.getInitialFont());
        assertEquals(initialFont, chooserPane.getFont());
    }

    @Test
    public void testOkButtonAction() {
        when(chooserPane.getFont()).thenReturn(new Font("Arial", Font.BOLD, 12));

        dialog.okButtonActionPerformed();

        verify(okListener).actionPerformed(any(ActionEvent.class));
        verify(dialog).dispose();
    }

    @Test
    public void testCancelButtonAction() {
        dialog.cancelButtonActionPerformed();

        verify(cancelListener).actionPerformed(any(ActionEvent.class));
        verify(dialog).dispose();
    }

    @Test
    public void testResetButtonAction() {
        Font initialFont = new Font("Arial", Font.BOLD, 12);
        dialog.setInitialFont(initialFont);

        dialog.resetButtonActionPerformed();

        assertEquals(initialFont, dialog.getInitialFont());
        assertEquals(initialFont, chooserPane.getFont());
    }
}