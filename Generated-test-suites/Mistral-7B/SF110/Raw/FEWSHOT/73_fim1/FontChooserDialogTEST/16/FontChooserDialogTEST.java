package osa.ora.server.client.ui.utils;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogConstruction() {
        JFrame frame = new JFrame();
        String title = "Test Font Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");

        FontChooserDialog dialog = new FontChooserDialog(frame, title, modal, chooserPane, okListener, cancelListener);
        assertNotNull(dialog);

        assertEquals(title, dialog.getTitle());
        assertSame(modal, dialog.isModal());
        assertEquals(chooserPane, dialog.getChooserPane());

        JPanel buttonPane = (JPanel) dialog.getContentPane().getComponent(1);
        assertEquals(3, buttonPane.getComponentCount());

        JButton okButton = (JButton) buttonPane.getComponent(0);
        assertEquals(UIManager.getString("ColorChooser.okText"), okButton.getText());
        assertEquals("OK", okButton.getActionCommand());

        JButton cancelButton = (JButton) buttonPane.getComponent(1);
        assertEquals(UIManager.getString("ColorChooser.cancelText"), cancelButton.getText());
        assertEquals("cancel", cancelButton.getActionCommand());

        JButton resetButton = (JButton) buttonPane.getComponent(2);
        assertEquals(UIManager.getString("ColorChooser.resetText"), resetButton.getText());
    }

    @Test
    public void testSetVisible() {
        JFrame frame = new JFrame();
        String title = "Test Font Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");

        FontChooserDialog dialog = new FontChooserDialog(frame, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true);
        dialog.setFont(new Font("Arial", Font.BOLD, 12));
        dialog.setVisible(false);

        dialog.setVisible(true);
        assertEquals(dialog.getChooserPane().getFont(), new Font("Arial", Font.BOLD, 12));
    }

    @Test
    public void testReset() {
        JFrame frame = new JFrame();
        String title = "Test Font Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");

        FontChooserDialog dialog = new FontChooserDialog(frame, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true);
        dialog.setFont(new Font("Arial", Font.BOLD, 12));
        dialog.reset();

        assertEquals(dialog.getChooserPane().getFont(), dialog.initialFont);
    }
}