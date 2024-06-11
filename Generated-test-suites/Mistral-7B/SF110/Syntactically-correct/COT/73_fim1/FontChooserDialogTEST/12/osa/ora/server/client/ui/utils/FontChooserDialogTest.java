import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.client.ui.utils.FontChooserDialog;
import javax.swing.*;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogConstructor() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        assertNotNull(dialog);
        assertEquals(title, dialog.getTitle());
        assertTrue(dialog.isModal());
        assertEquals(chooserPane, dialog.getChooserPane());
    }

    @Test
    public void testSetVisible() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true);
        assertNotNull(dialog.getInitialFont());

        dialog.setVisible(false);
        assertNull(dialog.getInitialFont());
    }

    @Test
    public void testReset() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);
        Font initialFont = dialog.getChooserPane().getFont();

        dialog.reset();
        assertSame(initialFont, dialog.getChooserPane().getFont());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetVisibleAfterDispose() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);
        dialog.dispose();
        dialog.setVisible(false);
    }

    @Test
    public void testWindowEventHandlers() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);

        Dialog d = (Dialog)dialog;
        assertTrue(d.getWindowState() == Dialog.ModalType.MODELESS);

        WindowEvent windowClosingEvent = new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING);
        d.dispatchEvent(windowClosingEvent);
        assertTrue(dialog.isVisible());

        Window w = dialog.getOwnerWindow();
        w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSED));
        assertFalse(dialog.isVisible());
    }
}