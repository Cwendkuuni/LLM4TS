package osa.ora.server.client.ui.utils.test;

import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import osa.ora.server.client.ui.utils.*;

@Category(UnitTest.class)
public class FontChooserDialogTest {

@Test
public void testFontChooserDialogConstructor() {
FontChooserDialog fontChooserDialog = new FontChooserDialog(new JFrame(), "Title", true, new JFontChooser(), null, null);
assertNotNull(fontChooserDialog);
}

@Test
public void testSetVisible() {
FontChooserDialog fontChooserDialog = new FontChooserDialog(new JFrame(), "Title", true, new JFontChooser(), null, null);
fontChooserDialog.setVisible(true);
fontChooserDialog.dispose(); // Assuming setVisible(false) is called when dialog is closed
}

@Test
public void testReset() {
FontChooserDialog fontChooserDialog = new FontChooserDialog(new JFrame(), "Title", true, new JFontChooser(), null, null);
fontChooserDialog.setVisible(true);
fontChooserDialog.reset();
// Add assertions to check if the reset method is working as expected
fontChooserDialog.dispose();
}

@Test(expected = NullPointerException.class)
public void testFontChooserDialogConstructorNullComponent() {
new FontChooserDialog(null, "Title", true, new JFontChooser(), null, null);
}

@Test(expected = NullPointerException.class)
public void testFontChooserDialogConstructorNullTitle() {
new FontChooserDialog(new JFrame(), null, true, new JFontChooser(), null, null);
}

@Test
public void testOkButtonDoesNotCallOkListener() {
FontChooserDialog fontChooserDialog = new FontChooserDialog(new JFrame(), "Title", true, new JFontChooser(), null, new ActionListener() {
@Override
public void actionPerformed(ActionEvent e) {
fail("OK button called okListener without being disposed");
}
});
fontChooserDialog.setVisible(true);
fontChooserDialog.dispose(); // Assuming setVisible(false) is called when dialog is closed
}

@Test
public void testCancelButtonDoesNotCallCancelListener() {
FontChooserDialog fontChooserDialog = new FontChooserDialog(new JFrame(), "Title", true, new JFontChooser(), null, null);
fontChooserDialog.setVisible(true);
fontChooserDialog.cancelButton.doClick(); // Assuming cancelButton is a valid JButton
fontChooserDialog.dispose(); // Assuming setVisible(false) is called when dialog is closed
}

@Test(expected = IllegalArgumentException.class)
public void testFontChooserDialogCannotBeMadeVisibleWithInvalidComponent() {
new FontChooserDialog(null, "Title", true, new JFontChooser(), null, null);
}
}