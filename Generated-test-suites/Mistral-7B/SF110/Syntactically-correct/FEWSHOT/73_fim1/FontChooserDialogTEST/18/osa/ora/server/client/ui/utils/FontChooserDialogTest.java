package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogConstruction() {
        Component c = new JFrame();
        FontChooserDialog dialog = new FontChooserDialog(c, "Test Dialog", true, null, null, null);
        assertEquals("Test Dialog", dialog.getTitle());
        assertTrue(dialog.isModal());
        assertNull(dialog.getFont());
        assertNotNull(dialog.getChooserPane());
        assertFalse(dialog.isVisible());
    }

    @Test
    public void testFontChooserDialogWithOkListener() {
        Component c = new JFrame();
        ActionListener okListener = e -> System.out.println("OK clicked");
        FontChooserDialog dialog = new FontChooserDialog(c, "Test Dialog", true, null, okListener, null);
        assertEquals("Test Dialog", dialog.getTitle());
        assertTrue(dialog.isModal());
        assertNull(dialog.getFont());
        assertNotNull(dialog.getChooserPane());
        assertFalse(dialog.isVisible());
        dialog.setVisible(true);
        dialog.okButton.doClick();
        assertTrue(okListener.wasInvoked());
    }

    @Test
    public void testFontChooserDialogWithCancelListener() {
        Component c = new JFrame();
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");
        FontChooserDialog dialog = new FontChooserDialog(c, "Test Dialog", true, null, null, cancelListener);
        assertEquals("Test Dialog", dialog.getTitle());
        assertTrue(dialog.isModal());
        assertNull(dialog.getFont());
        assertNotNull(dialog.getChooserPane());
        assertFalse(dialog.isVisible());
        dialog.setVisible(true);
        dialog.cancelButton.doClick();
        assertTrue(cancelListener.wasInvoked());
    }

    @Test
    public void testFontChooserDialogWithInitialFont() {
        Component c = new JFrame();
        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        JFontChooser chooserPane = mock(JFontChooser.class);
        when(chooserPane.getFont()).thenReturn(initialFont);
        FontChooserDialog dialog = new FontChooserDialog(c, "Test Dialog", true, chooserPane, null, null);
        assertEquals("Test Dialog", dialog.getTitle());
        assertTrue(dialog.isModal());
        assertEquals(initialFont, dialog.getFont());
        assertNotNull(dialog.getChooserPane());
        assertFalse(dialog.isVisible());
    }

    @Test
    public void testFontChooserDialogWithInitialFontAndOkListener() {
        Component c = new JFrame();
        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        JFontChooser chooserPane = mock(JFontChooser.class);
        when(chooserPane.getFont()).thenReturn(initialFont);
        ActionListener okListener = e -> System.out.println("OK clicked");
        FontChooserDialog dialog = new FontChooserDialog(c, "Test Dialog", true, chooserPane, okListener, null);
        assertEquals("Test Dialog", dialog.getTitle());
        assertTrue(dialog.isModal());
        assertEquals(initialFont, dialog.getFont());
        assertNotNull(dialog.getChooserPane());
        assertFalse(dialog.isVisible());
        dialog.setVisible(true);
        dialog.okButton.doClick();
        assertTrue(okListener.wasInvoked());
    }

    @Test
    public void testFontChooserDialogWithInitialFontAndCancelListener() {
        Component c = new JFrame();
        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        JFontChooser chooserPane = mock(JFontChooser.class);
        when(chooserPane.getFont()).thenReturn(initialFont);
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");
        FontChooserDialog dialog = new FontChooserDialog(c, "Test Dialog", true, chooserPane, null, cancelListener);
        assertEquals("Test Dialog", dialog.getTitle());
        assertTrue(dialog.isModal());
        assertEquals(initialFont, dialog.getFont());
        assertNotNull(dialog.getChooserPane());
        assertFalse(dialog.isVisible());
        dialog.setVisible(true);
        dialog.cancelButton.doClick();
        assertTrue(cancelListener.wasInvoked());
    }

    @Test
    public void testFontChooserDialogReset() {
        Component c = new JFrame();
        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        JFontChooser chooserPane = mock(JFontChooser.class);
        when(chooserPane.getFont()).thenReturn(initialFont);
        FontChooserDialog dialog = new FontChooserDialog(c, "Test Dialog", true, chooserPane, null, null);
        assertEquals("Test Dialog", dialog.getTitle());
        assertTrue(dialog.isModal());
        assertEquals(initialFont, dialog.getFont());
        assertNotNull(dialog.getChooserPane());
        assertFalse(dialog.isVisible());
        dialog.reset();
        assertEquals(initialFont, dialog.getFont());
    }

    @Test
    public void testFontChooserDialogWithInitialFontReset() {
        Component c = new JFrame();
        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        JFontChooser chooserPane = mock(JFontChooser.class);
        when(chooserPane.getFont()).thenReturn(initialFont);
        FontChooserDialog dialog = new FontChooserDialog(c, "Test Dialog", true, chooserPane, null, null);
        assertEquals("Test Dialog", dialog.getTitle());
        assertTrue(dialog.isModal());
        assertEquals(initialFont, dialog.getFont());
        assertNotNull(dialog.getChooserPane());
        assertFalse(dialog.isVisible());
        dialog.setVisible(true);
        dialog.reset();
        assertEquals(initialFont, dialog.getFont());
    }
}