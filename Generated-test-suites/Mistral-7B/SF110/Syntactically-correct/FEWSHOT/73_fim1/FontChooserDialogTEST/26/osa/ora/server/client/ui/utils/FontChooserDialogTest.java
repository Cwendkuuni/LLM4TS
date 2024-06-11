package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogConstruction() {
        Component c = mock(Component.class);
        String title = "Font Dialog";
        boolean modal = true;
        JFontChooser chooserPane = mock(JFontChooser.class);
        ActionListener okListener = mock(ActionListener.class);
        ActionListener cancelListener = mock(ActionListener.class);

        new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        verify(c, times(1)).getFont();
        verify(c, times(1)).getFrameForComponent(c);
    }

    @Test
    public void testSetVisibleWithInitialFont() {
        Component c = mock(Component.class);
        String title = "Font Dialog";
        boolean modal = true;
        JFontChooser chooserPane = mock(JFontChooser.class);
        ActionListener okListener = mock(ActionListener.class);
        ActionListener cancelListener = mock(ActionListener.class);
        Font initialFont = new Font("Arial", Font.BOLD, 12);

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true);

        verify(chooserPane, times(1)).getFont();
        assertEquals(initialFont, dialog.initialFont);
    }

    @Test
    public void testSetVisibleWithoutInitialFont() {
        Component c = mock(Component.class);
        String title = "Font Dialog";
        boolean modal = true;
        JFontChooser chooserPane = mock(JFontChooser.class);
        ActionListener okListener = mock(ActionListener.class);
        ActionListener cancelListener = mock(ActionListener.class);

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true);
        dialog.setVisible(false);

        verify(chooserPane, never()).getFont();
        assertNull(dialog.initialFont);
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

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);
        dialog.reset();

        verify(chooserPane, times(1)).setFont(initialFont);
    }
}