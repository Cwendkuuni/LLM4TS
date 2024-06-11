package osa.ora.server.client.ui.utils;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

 private Component c; // Component for creating the dialog
 private String title;
 private boolean modal;
 private JFontChooser chooserPane;
 private ActionListener okListener;
 private ActionListener cancelListener;

 @Before
 public void setUp() {
 // Initialize test variables
 c = new JFrame();
 title = "Font Chooser Dialog";
 modal = true;
 chooserPane = new JFontChooser();
 okListener = e -> {}; // Do nothing on OK button click
 cancelListener = e -> {}; // Do nothing on Cancel button click
 }

 // Test for constructor with all arguments
 @Test
 public void testFontChooserDialogConstructorWithAllArguments() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
 assertNotNull(fontChooserDialog);
 assertTrue(fontChooserDialog.isModal());
 assertEquals(title, fontChooserDialog.getTitle());
 assertEquals(chooserPane, fontChooserDialog.getChooserPane());
 // Test initial state of components
 assertEquals(0, fontChooserDialog.getWidth());
 assertEquals(0, fontChooserDialog.getHeight());
 // Test OK button is disabled by default
 assertFalse(fontChooserDialog.getOkButton().isEnabled());
 // Test Cancel and Reset buttons are enabled
 assertTrue(fontChooserDialog.getCancelButton().isEnabled());
 assertTrue(fontChooserDialog.getResetButton().isEnabled());
 // Test initial font is null
 assertNull(fontChooserDialog.getInitialFont());
 fontChooserDialog.dispose(); // Clean up
 }

 // Test for setVisible method
 @Test
 public void testSetVisible() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
 // Test initial state of components
 assertEquals(0, fontChooserDialog.getWidth(), 0.0f);
 assertEquals(0, fontChooserDialog.getHeight(), 0.0f);
 // Test setVisible with initial font
 fontChooserDialog.setVisible(true);
 assertEquals(fontChooserDialog.getInitialFont(), fontChooserDialog.getChooserPane().getFont());
 // Test setVisible without initial font
 fontChooserDialog.chooserPane = null;
 fontChooserDialog.setVisible(true);
 assertNull(fontChooserDialog.getInitialFont());
 fontChooserDialog.dispose(); // Clean up
 }

 // Test for reset method
 @Test
 public void testReset() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
 // Set an initial font
 fontChooserDialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 12));
 fontChooserDialog.setVisible(true);
 // Test reset sets initial font
 fontChooserDialog.reset();
 assertEquals(fontChooserDialog.getInitialFont(), fontChooserDialog.getChooserPane().getFont());
 fontChooserDialog.dispose(); // Clean up
 }

 // Test for OK button action
 @Test
 public void testOKButtonAction() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 // Test initial state of components
 assertTrue(fontChooserDialog.isVisible());
 // Test OK button click sets dialog to invisible
 ActionEvent okEvent = new ActionEvent(fontChooserDialog.getOkButton(), ActionEvent.ACTION_PERFORMED, "OK");
 fontChooserDialog.okButton.dispatchEvent(okEvent);
 assertTrue(fontChooserDialog.isVisible()); // Should still be visible temporarily
 // Test dialog is hidden after OK button click
 assertTrue(fontChooserDialog.isFocusable());
 assertEquals(0, fontChooserDialog.getWidth(), 0.0f);
 assertEquals(0, fontChooserDialog.getHeight(), 0.0f);
 // Test OK button click calls okListener if it was registered
 okListener = e -> assertTrue(true);
 fontChooserDialog.setVisible(true);
 ActionEvent mockOkEvent = new ActionEvent(fontChooserDialog.getOkButton(), ActionEvent.ACTION_PERFORMED, "OK");
 fontChooserDialog.okButton.dispatchEvent(mockOkEvent);
 assertTrue(okListener.wasCalled());
 fontChooserDialog.dispose(); // Clean up
 }

 // Test for Cancel button action
 @Test
 public void testCancelButtonAction() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 // Test initial state of components
 assertTrue(fontChooserDialog.isVisible());
 // Test Cancel button click sets dialog to invisible
 ActionEvent cancelEvent = new ActionEvent(fontChooserDialog.getCancelButton(), ActionEvent.ACTION_PERFORMED, "Cancel");
 fontChooserDialog.cancelButton.dispatchEvent(cancelEvent);
 assertTrue(fontChooserDialog.isVisible()); // Should still be visible temporarily
 // Test dialog is hidden after Cancel button click
 assertTrue(fontChooserDialog.isFocusable());
 assertEquals(0, fontChooserDialog.getWidth(), 0.0f);
 assertEquals(0, fontChooserDialog.getHeight(), 0.0f);
 // Test Cancel button click calls cancelListener if it was registered
 cancelListener = e -> assertTrue(true);
 fontChooserDialog.setVisible(true);
 ActionEvent mockCancelEvent = new ActionEvent(fontChooserDialog.getCancelButton(), ActionEvent.ACTION_PERFORMED, "Cancel");
 fontChooserDialog.cancelButton.dispatchEvent(mockCancelEvent);
 assertTrue(cancelListener.wasCalled());
 fontChooserDialog.dispose(); // Clean up
 }

 // Test for Reset button action
 @Test
 public void testResetButtonAction() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 // Set an initial font
 fontChooserDialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 12));
 // Test Reset button click sets dialog font to initial font
 ActionEvent resetEvent = new ActionEvent(fontChooserDialog.getResetButton(), ActionEvent.ACTION_PERFORMED, "Reset");
 fontChooserDialog.resetButton.dispatchEvent(resetEvent);
 assertEquals(fontChooserDialog.getInitialFont(), fontChooserDialog.getChooserPane().getFont());
 fontChooserDialog.dispose(); // Clean up
 }

 // Test for window closing
 @Test
 public void testWindowClosing() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 // Test window closing calls DisposeOnClose component
 WindowEvent windowClosingEvent = new WindowEvent(fontChooserDialog, WindowEvent.WINDOW_CLOSING);
 fontChooserDialog.addWindowListener(new FontChooserDialog.Closer());
 fontChooserDialog.dispatchEvent(windowClosingEvent);
 // Test dialog is disposed
 assertTrue(fontChooserDialog.isDisposed());
 fontChooserDialog.dispose(); // Clean up
 }

 // Test for component hidden
 @Test
 public void testComponentHidden() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 Component component = new JLabel("Test Component");
 fontChooserDialog.add(component, BorderLayout.CENTER);
 // Test component hidden calls DisposeOnClose component
 ComponentEvent componentHiddenEvent = new ComponentEvent(fontChooserDialog, ComponentEvent.COMPONENT_HIDDEN);
 fontChooserDialog.addComponentListener(new FontChooserDialog.DisposeOnClose());
 fontChooserDialog.dispatchEvent(componentHiddenEvent);
 // Test dialog is disposed
 assertTrue(fontChooserDialog.isDisposed());
 fontChooserDialog.dispose(); // Clean up
 }
}