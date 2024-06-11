package osa.ora.server.client.ui.utils;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class FontChooserDialogTest {

    //... (Expert 1's test cases)

    @Test
    void testFontChooserDialogCloser() {
        WindowEvent windowClosingEvent = new WindowEvent(new FontChooserDialog(null, "Test Dialog", true, null, null, null), WindowEvent.WINDOW_CLOSING);
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        closer.windowClosing(windowClosingEvent);
        assertTrue(windowClosingEvent.getWindow().isVisible());
    }

    @Test
    void testFontChooserDialogDisposeOnClose() {
        ComponentEvent componentHiddenEvent = new ComponentEvent(new FontChooserDialog(null, "Test Dialog", true, null, null, null), ComponentEvent.COMPONENT_HIDDEN);
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        disposeOnClose.componentHidden(componentHiddenEvent);
        assertFalse(new FontChooserDialog(null, "Test Dialog", true, null, null, null).isDisplayable());
    }
}