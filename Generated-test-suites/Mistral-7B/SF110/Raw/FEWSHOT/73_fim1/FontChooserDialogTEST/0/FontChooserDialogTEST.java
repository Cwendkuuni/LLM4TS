package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialog() {
        Component c = mock(Component.class);
        String title = "Font Dialog";
        boolean modal = true;
        JFontChooser chooserPane = mock(JFontChooser.class);
        ActionListener okListener = arg -> {};
        ActionListener cancelListener = arg -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        assertEquals(title, dialog.getTitle());
        assertEquals(modal, dialog.isModal());
        assertTrue(dialog.isFocusTraversable());
        assertSame(chooserPane, dialog.getChooserPane());

        Container contentPane = dialog.getContentPane();
        assertSame(chooserPane, contentPane.getComponent(0));
        assertNotNull(dialog.getRootPane().getDefaultButton());
    }

    @Test
    public void testSetVisible() {
        Component c = mock(Component.class);
        String title = "Font Dialog";
        boolean modal = true;
        JFontChooser chooserPane = mock(JFontChooser.class);
        ActionListener okListener = arg -> {};
        ActionListener cancelListener = arg -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true);
        verify(dialog).setVisible(true);
        verify(dialog).setVisible(true); // Verifies the call inside actionPerformed of okButton
        assertEquals(dialog.initialFont, dialog.chooserPane.getFont());

        dialog.reset();
        verify(dialog.chooserPane).setFont(eq(dialog.initialFont));
        dialog.setVisible(false);
    }

    @Test
    public void testActionPerformedOfDefaultButton() {
        Component c = mock(Component.class);
        String title = "Font Dialog";
        boolean modal = true;
        JFontChooser chooserPane = mock(JFontChooser.class);
        ActionListener okListener = arg -> {};
        ActionListener cancelListener = arg -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true);
        verify(dialog).setVisible(true);
        verify(dialog).setVisible(false); // Verifies the call inside actionPerformed of okButton
    }

    @Test
    public void testActionPerformedOfCancelButton() {
        Component c = mock(Component.class);
        String title = "Font Dialog";
        boolean modal = true;
        JFontChooser chooserPane = mock(JFontChooser.class);
        ActionListener okListener = arg -> {};
        ActionListener cancelListener = arg -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true);
        verify(dialog).setVisible(true);
        verify(dialog).setVisible(false); // Verifies the call inside actionPerformed of cancelButton
    }

    @Test
    public void testActionPerformedOfResetButton() {
        Component c = mock(Component.class);
        String title = "Font Dialog";
        boolean modal = true;
        JFontChooser chooserPane = mock(JFontChooser.class);
        ActionListener okListener = arg -> {};
        ActionListener cancelListener = arg -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true);
        dialog.reset();
        verify(dialog.chooserPane).setFont(eq(dialog.initialFont));
    }
}