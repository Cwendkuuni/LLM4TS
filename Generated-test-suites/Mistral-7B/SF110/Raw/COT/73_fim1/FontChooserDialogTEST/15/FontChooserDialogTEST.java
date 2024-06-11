import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.client.ui.utils.FontChooserDialog;
import javax.swing.*;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogConstructor() {
        Component c = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        assertNotNull(fontChooserDialog);
    }

    @Test
    public void testSetVisibleTrue() {
        Component c = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        fontChooserDialog.setVisible(true);
        assertTrue(fontChooserDialog.isVisible());
    }

    @Test
    public void testSetVisibleFalse() {
        Component c = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        fontChooserDialog.setVisible(true);
        fontChooserDialog.setVisible(false);
        assertFalse(fontChooserDialog.isVisible());
    }

    @Test
    public void testReset() {
        Component c = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        fontChooserDialog.setVisible(true);
        fontChooserDialog.reset();
        assertEquals(fontChooserDialog.getInitialFont(), fontChooserPane.getFont());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetVisibleFalseBeforeSetVisibleTrue() {
        Component c = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        fontChooserDialog.setVisible(false);
        fontChooserDialog.setVisible(true);
    }

    @Test
    public void testWindowClosing() {
        Component c = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        fontChooserDialog.setVisible(true);
        final FontChooserDialog expectedFontChooserDialog = fontChooserDialog;
        fontChooserDialog.addWindowListener(new FontChooserDialog.Closer() {
            @Override
            public void windowClosing(WindowEvent e) {
                assertEquals(expectedFontChooserDialog, e.getWindow());
            }
        });
        fontChooserDialog.dispatchEvent(new WindowEvent(fontChooserDialog, WindowEvent.WINDOW_CLOSING));
    }

    @Test
    public void testComponentHidden() {
        Component c = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        fontChooserDialog.setVisible(true);
        final FontChooserDialog expectedFontChooserDialog = fontChooserDialog;
        fontChooserDialog.addComponentListener(new FontChooserDialog.DisposeOnClose() {
            @Override
            public void componentHidden(ComponentEvent e) {
                assertEquals(expectedFontChooserDialog, (Window)e.getComponent());
            }
        });
        fontChooserDialog.dispatchEvent(new WindowEvent(fontChooserDialog, WindowEvent.WINDOW_CLOSED));
    }
}