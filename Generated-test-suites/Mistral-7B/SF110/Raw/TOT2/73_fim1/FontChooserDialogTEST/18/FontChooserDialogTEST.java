package osa.ora.server.client.ui.utils;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import osa.ora.server.client.ui.utils.FontChooserDialog;

@RunWith(Parameterized.class)
public class FontChooserDialogTest {

    private Component component;
    private String title;
    private boolean modal;
    private JFontChooser chooserPane;
    private ActionListener okListener;
    private ActionListener cancelListener;

    @Parameterized.Parameters
    public static Iterable<Object[]> data() {
        return new IteratorTestDataFactory<Object[]>()
                .create(new Object[][]{
                        {null, "Title", true, null, null, null},
                        {"", "Title", true, null, null, null},
                        {new JFrame(), null, true, null, null, null},
                        {new JFrame(), "Title", false, null, null, null},
                        {new JFrame(), "Title", true, null, null, null},
                        {new JFrame(), "Title", true, new JFontChooser(), null, null},
                        {new JFrame(), "Title", true, new JFontChooser(), null, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                throw new RuntimeException();
                            }
                        }}
                });
    }

    public FontChooserDialogTest(Component component, String title, boolean modal, JFontChooser chooserPane, ActionListener okListener, ActionListener cancelListener) {
        this.component = component;
        this.title = title;
        this.modal = modal;
        this.chooserPane = chooserPane;
        this.okListener = okListener;
        this.cancelListener = cancelListener;
    }

    @Test
    public void testFontChooserDialogConstruction() {
        new FontChooserDialog(component, title, modal, chooserPane, okListener, cancelListener);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFontChooserDialogWithNullComponent() {
        new FontChooserDialog(null, title, modal, chooserPane, okListener, cancelListener);
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogWithEmptyOrNullTitle() {
        new FontChooserDialog(component, null, modal, chooserPane, okListener, cancelListener);
        new FontChooserDialog(component, "", modal, chooserPane, okListener, cancelListener);
    }

    @Test(expected = IllegalStateException.class)
    public void testFontChooserDialogWhenAlreadyVisible() {
        new FontChooserDialog(component, title, modal, chooserPane, okListener, cancelListener);
        this.setVisible(true);
        new FontChooserDialog(component, title, modal, chooserPane, okListener, cancelListener);
    }

    @Test
    public void testFontChooserDialogReset() {
        new FontChooserDialog(component, title, modal, chooserPane, okListener, cancelListener);
        this.reset();
        assertEquals(this.initialFont, this.chooserPane.getFont());
    }

    // Add other test methods here
}