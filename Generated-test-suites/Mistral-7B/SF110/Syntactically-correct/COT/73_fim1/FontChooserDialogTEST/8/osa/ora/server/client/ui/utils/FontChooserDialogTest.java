package osa.ora.server.client.ui.utils.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class FontChooserDialogTest {

    private JFrame frame;
    private JComponent parentComponent;
    private FontChooserDialog fontChooserDialog;

    @Before
    public void setUp() {
        parentComponent = new JLabel();
        frame = new JFrame();
        frame.setVisible(true);
        fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, new JFontChooser(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        }, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
    }

    // Test constructor method
    @Test
    public void testFontChooserDialogConstructor() {
        Assert.assertNotNull(fontChooserDialog);
    }

    // Test setVisible method
    @Test
    public void testSetVisible() {
        fontChooserDialog.setVisible(true);
        Assert.assertEquals(true, fontChooserDialog.isVisible());
        fontChooserDialog.setVisible(false);
        Assert.assertEquals(false, fontChooserDialog.isVisible());
    }

    // Test reset method
    @Test
    public void testReset() {
        fontChooserDialog.reset();
        JFontChooser chooserPane = fontChooserDialog.chooserPane;
        Font font = chooserPane.getFont();
        Assert.assertNotNull(font);
        fontChooserDialog.setVisible(true);
        chooserPane.setFont(new Font("Arial", Font.BOLD, 12));
        fontChooserDialog.reset();
        chooserPane.getFont();
        Assert.assertEquals(font, chooserPane.getFont());
    }

    // Test exception handling for null listener
    @Test(expected = NullPointerException.class)
    public void testNullListener() {
        new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, null, null, null);
    }

    // Test exception handling for null initialFont
    @Test
    public void testNullInitialFont() {
        fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, new JFontChooser(), null, null);
        fontChooserDialog.reset();
        JFontChooser chooserPane = fontChooserDialog.chooserPane;
        Assert.assertNotNull(chooserPane.getFont());
    }

    // Test ok button action
    @Test
    public void testOkButtonAction() {
        fontChooserDialog.setVisible(true);
        fontChooserDialog.getOkButton().doClick();
        Assert.assertEquals(false, fontChooserDialog.isVisible());
    }

    // Test cancel button action
    @Test
    public void testCancelButtonAction() {
        fontChooserDialog.setVisible(true);
        fontChooserDialog.getCancelButton().doClick();
        Assert.assertEquals(false, fontChooserDialog.isVisible());
    }

    // Test reset button action
    @Test
    public void testResetButtonAction() {
        fontChooserDialog.setVisible(true);
        fontChooserDialog.getResetButton().doClick();
        JFontChooser chooserPane = fontChooserDialog.chooserPane;
        Font font = chooserPane.getFont();
        Assert.assertNotNull(font);
        chooserPane.setFont(new Font("Arial", Font.BOLD, 12));
        fontChooserDialog.getResetButton().doClick();
        chooserPane.getFont();
        Assert.assertEquals(font, chooserPane.getFont());
    }

    // Test ok button action with custom okListener
    @Test
    public void testOkButtonActionWithCustomOkListener() {
        int count = 0;
        ActionListener customOkListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                count++;
            }
        };
        fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, new JFontChooser(), customOkListener, null);
        fontChooserDialog.setVisible(true);
        fontChooserDialog.getOkButton().doClick();
        Assert.assertEquals(1, count);
    }

    // Test cancel button action with custom cancelListener
    @Test
    public void testCancelButtonActionWithCustomCancelListener() {
        int count = 0;
        ActionListener customCancelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                count++;
            }
        };
        fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, new JFontChooser(), null, customCancelListener);
        fontChooserDialog.setVisible(true);
        fontChooserDialog.getCancelButton().doClick();
        Assert.assertEquals(1, count);
    }

    // Test ok button action with default actionCommand
    @Test
    public void testOkButtonActionWithDefaultActionCommand() {
        fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, new JFontChooser(), null, null);
        fontChooserDialog.setVisible(true);
        fontChooserDialog.getOkButton().doClick();
        Assert.assertEquals(false, fontChooserDialog.isVisible());
    }

    // Test cancel button action with default actionCommand
    @Test
    public void testCancelButtonActionWithDefaultActionCommand() {
        fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, new JFontChooser(), null, null);
        fontChooserDialog.setVisible(true);
        fontChooserDialog.getCancelButton().doClick();
        Assert.assertEquals(false, fontChooserDialog.isVisible());
    }

    // Test ok button action with custom actionCommand
    @Test
    public void testOkButtonActionWithCustomActionCommand() {
        String customCommand = "TEST";
        fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, new JFontChooser(), null, null);
        fontChooserDialog.getOkButton().setActionCommand(customCommand);
        fontChooserDialog.setVisible(true);
        fontChooserDialog.getOkButton().doClick();
        Assert.assertEquals(customCommand, fontChooserDialog.getOkButton().getActionCommand());
        Assert.assertEquals(false, fontChooserDialog.isVisible());
    }

    // Test cancel button action with custom actionCommand
    @Test
    public void testCancelButtonActionWithCustomActionCommand() {
        String customCommand = "TEST";
        fontChooserdialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, new JFontChooser(), null, null);
        fontChooserDialog.getCancelButton().setActionCommand(customCommand);
        fontChooserDialog.setVisible(true);
        fontChooserDialog.getCancelButton().doClick();
        Assert.assertEquals(customCommand, fontChooserDialog.getCancelButton().getActionCommand());
        Assert.assertEquals(false, fontChooserDialog.isVisible());
    }

    // Test ok button action with custom actionListener and custom actionCommand
    @Test
    public void testOkButtonActionWithCustomActionListenerAndCustomActionCommand() {
        String customCommand = "TEST";
        int count = 0;
        ActionListener customOkListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                count++;
            }
        };
        fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, new JFontChooser(), null, null);
        fontChooserDialog.getOkButton().setActionCommand(customCommand);
        fontChooserDialog.getOkButton().addActionListener(customOkListener);
        fontChooserDialog.setVisible(true);
        fontChooserDialog.getOkButton().doClick();
        Assert.assertEquals(1, count);
        Assert.assertEquals(customCommand, fontChooserDialog.getOkButton().getActionCommand());
        Assert.assertEquals(false, fontChooserDialog.isVisible());
    }

    // Test cancel button action with custom actionListener and custom actionCommand
    @Test
    public void testCancelButtonActionWithCustomActionListenerAndCustomActionCommand() {
        String customCommand = "TEST";
        int count = 0;
        ActionListener customCancelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                count++;
            }
        };
        fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, new JFontChooser(), null, null);
        fontChooserDialog.getCancelButton().setActionCommand(customCommand);
        fontChooserDialog.getCancelButton().addActionListener(customCancelListener);
        fontChooserDialog.setVisible(true);
        fontChooserDialog.getCancelButton().doClick();
        Assert.assertEquals(1, count);
        Assert.assertEquals(customCommand, fontChooserDialog.getCancelButton().getActionCommand());
        Assert.assertEquals(false, fontChooserDialog.isVisible());
    }

    // Test ok button action with custom actionCommand and enter key
    @Test
    public void testOkButtonActionWithCustomActionCommandAndEnterKey() {
        String customCommand = "TEST";
        fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, new JFontChooser(), null, null);
        fontChooserDialog.getOkButton().setActionCommand(customCommand);
        focusOnButton(fontChooserDialog.getOkButton());
        pressKey(KeyEvent.VK_ENTER);
        Assert.assertEquals(false, fontChooserDialog.isVisible());
    }

    // Test cancel button action with custom actionCommand and escape key
    @Test
    public void testCancelButtonActionWithCustomActionCommandAndEscapeKey() {
        String customCommand = "TEST";
        fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, new JFontChooser(), null, null);
        fontChooserDialog.getCancelButton().setActionCommand(customCommand);
        focusOnButton(fontChooserDialog.getCancelButton());
        pressKey(KeyEvent.VK_ESCAPE);
        Assert.assertEquals(false, fontChooserDialog.isVisible());
    }

    // Test close window event
    @Test
    public void testCloseWindowEvent() {
        WindowAdapter closer = new FontChooserDialog.Closer();
        fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, new JFontChooser(), null, null);
        fontChooserDialog.addWindowListener(closer);
        focusOnWindow(fontChooserDialog);
        pressKey(KeyEvent.VK_X);
        Assert.assertEquals(false, fontChooserDialog.isVisible());
    }

    // Test dispose on close event
    @Test
    public void testDisposeOnCloseEvent() {
        ComponentAdapter disposeOnClose = new FontChooserDialog.DisposeOnClose();
        fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, new JFontChooser(), null, null);
        fontChooserDialog.addComponentListener(disposeOnClose);
        focusOnWindow(fontChooserDialog);
        pressKey(KeyEvent.VK_X);
        Assert.assertTrue(fontChooserDialog.isDisposed());
    }

    // Test exception handling for invalid initialFont
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInitialFont() {
        new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, null, null, null);
    }

    private void focusOnButton(AbstractButton button) {
        Robot robot = new Robot();
        Point location = button.getLocationOnScreen();
        robot.mouseMove(location.x, location.y);
        robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
    }

    private void focusOnWindow(Window window) {
        Robot robot = new Robot();
        Point location = window.getLocationOnScreen();
        robot.mouseMove(location.x, location.y);
        robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
    }

    private void pressKey(int keyCode) {
        Robot robot = new Robot();
        KeyEvent keyEvent = new KeyEvent(KeyboardFocusManager.getCurrentKeyEventSource(), 0, 0, keyCode, 0, 0, false);
        robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
        robot.keyPress(keyEvent);
        robot.keyRelease(keyEvent);
        robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
    }
}