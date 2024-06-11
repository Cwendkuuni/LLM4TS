package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogCreation() {
        Component c = mock(Component.class);
        String title = "Font Dialog";
        boolean modal = true;
        JFontChooser chooserPane = mock(JFontChooser.class);
        ActionListener okListener = mock(ActionListener.class);
        ActionListener cancelListener = mock(ActionListener.class);

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        assertEquals("Font Dialog", fontChooserDialog.getTitle());
        assertTrue(fontChooserDialog.isModal());
        assertEquals(chooserPane, fontChooserDialog.getChooserPane());
        assertNull(fontChooserDialog.getInitialFont());
        assertEquals(okListener, fontChooserDialog.getOkButton().getActionListeners()[0]);
        assertEquals(cancelListener, fontChooserDialog.getCancelButton().getActionListeners()[0]);
    }

    @Test
    public void testSetVisibleAndGetInitialFont() {
        Component c = mock(Component.class);
        String title = "Font Dialog";
        boolean modal = true;
        JFontChooser chooserPane = mock(JFontChooser.class);
        ActionListener okListener = mock(ActionListener.class);
        ActionListener cancelListener = mock(ActionListener.class);
        Font initialFont = new Font("Arial", Font.BOLD, 12);

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        fontChooserDialog.setVisible(true);
        assertEquals(initialFont, fontChooserDialog.getInitialFont());
    }

    @Test
    public void testReset() {
        Component c = mock(Component.class);
        String title = "Font Dialog";
        boolean modal = true;
        JFontChooser chooserPane = mock(JFontChooser.class);
        ActionListener okListener = mock(ActionListener.class);
        ActionListener cancelListener = mock(ActionListener.class);
        Font initialFont = new Font("Arial", Font.BOLD, 12);
        Font newFont = new Font("Times New Roman", Font.ITALIC, 14);

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        when(chooserPane.getFont()).thenReturn(newFont);
        fontChooserDialog.setVisible(true);
        assertEquals(newFont, fontChooserDialog.getInitialFont());

        fontChooserDialog.reset();
        assertEquals(initialFont, fontChooserDialog.getInitialFont());
        assertEquals(initialFont, chooserPane.getFont());
    }
}