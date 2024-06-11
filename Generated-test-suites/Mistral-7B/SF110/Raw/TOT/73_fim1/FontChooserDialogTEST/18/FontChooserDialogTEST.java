package osa.ora.server.client.ui.utils;

import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;

public class FontChooserDialogTest {

 private Component testComponent;
 private JFrame testFrame;

 @Test
 public void testFontChooserDialog() {
  String title = "Font Chooser Dialog Test";
  boolean modal = true;
  JFontChooser chooserPane = new JFontChooser();
  ActionListener okListener = e -> {
   System.out.println("OK button clicked");
  };
  ActionListener cancelListener = e -> {
   System.out.println("Cancel button clicked");
  };

  // Create and show the dialog
  FontChooserDialog fontDialog = new FontChooserDialog(testComponent, title, modal, chooserPane, okListener, cancelListener);
  fontDialog.setVisible(true);

  // Test initial state
  JButton okButton = fontDialog.getRootPane().getDefaultButton();
  JButton cancelButton = fontDialog.getRootPane().getComponentsByFunctionalInterfaceType(ActionListener.class)[1];
  JButton resetButton = fontDialog.getRootPane().getComponent(2);
  JFontChooser dialogChooserPane = fontDialog.getContentPane().getComponent(0);

  // Test OK button
  okButton.doClick();
  assertFalse("Dialog should be hidden after OK button clicked", fontDialog.isVisible());
  okListener.actionPerformed(new ActionEvent(testComponent, 0, "OK"));

  // Test Cancel button
  cancelButton.doClick();
  assertFalse("Dialog should be hidden after Cancel button clicked", fontDialog.isVisible());
  cancelListener.actionPerformed(new ActionEvent(testComponent, 0, "Cancel"));

  // Test Reset button
  resetButton.doClick();
  JFontChooser initialChooserPane = chooserPane;
  fontDialog.reset();
  JFontChooser newChooserPane = dialogChooserPane;
  assertNotSame("Font chooser pane should not be the same after reset", initialChooserPane, newChooserPane);
  assertEquals("Font chooser pane initial font should be reset to the initial dialog font", initialFont, newChooserPane.getFont());

  // Test keyboard shortcut for Cancel button
  testFrame = new JFrame();
  testFrame.add(fontDialog);
  testFrame.setVisible(true);
  testFrame.requestFocus();
  testFrame.dispatchEvent(new KeyEvent(testFrame, KeyEvent.KEY_PRESSED, 0, '\u001b'));
  assertFalse("Dialog should be hidden after Esc key pressed", fontDialog.isVisible());

  // Test window closing event
  testDialogClosingEvent(fontDialog, "OK");
  testDialogClosingEvent(fontDialog, "Cancel");

  // Test component hidden event
  testDialogComponentHiddenEvent(fontDialog);

  testDialog.dispose();
  testFrame.dispose();
 }

 private void testDialogClosingEvent(FontChooserDialog testDialog, String actionCommand) {
  ActionListener closeListener = e -> {
   System.out.println("WindowClosing event received: " + actionCommand);
   assertSame("Correct listener should be called for window closing event", testDialog, e.getSource());
  };
  testDialog.addWindowListener(new FontChooserDialog.Closer() {
   @Override
   public void windowClosing(WindowEvent e) {
    super.windowClosing(e);
    if (e.getActionCommand() != null) {
     closeListener.actionPerformed(e);
    }
   }
  });
  testDialog.setVisible(true);
  testDialog.dispose();
  verify(closeListener, times(1)).actionPerformed(any(ActionEvent.class));
 }

 private void testDialogComponentHiddenEvent(FontChooserDialog testDialog) {
  ComponentAdapter hiddenListener = e -> {
   System.out.println("ComponentHidden event received");
   assertSame("Correct listener should be called for component hidden event", testDialog, e.getComponent());
  };
  testDialog.addComponentListener(new FontChooserDialog.DisposeOnClose() {
   @Override
   public void componentHidden(ComponentEvent e) {
    super.componentHidden(e);
    if (e != null) {
     hiddenListener.componentHidden(e);
    }
   }
  });
  testDialog.setVisible(true);
  testDialog.dispose();
  verify(hiddenListener, times(1)).componentHidden(any(ComponentEvent.class));
 }
}