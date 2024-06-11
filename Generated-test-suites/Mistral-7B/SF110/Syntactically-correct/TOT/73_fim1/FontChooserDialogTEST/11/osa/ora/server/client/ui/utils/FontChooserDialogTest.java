package osa.ora.server.client.ui.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;

public class FontChooserDialogTest {

 private FontChooserDialog dialog;
 private Component parentFrame;

 @Before
 public void setUp() {
 parentFrame = new JFrame();
 dialog = new FontChooserDialog(parentFrame, "Font Chooser Dialog", true, new JFontChooser(), new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // Handle OK button click
 }
 }, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // Handle Cancel button click
 }
 });
 }

 @After
 public void tearDown() {
 dialog.dispose();
 parentFrame.dispose();
 }

 @Test
 public void testDefaultFont() {
 // Test default font when dialog is shown for the first time
 dialog.setVisible(true);
 Font font = dialog.chooserPane.getFont();
 assertTrue("Default font should not be null", font != null);
 // Handle OK button click and get the chosen font
 dialog.getRootPane().getDefaultButton().doClick();
 Font chosenFont = dialog.chooserPane.getFont();
 assertEquals("Default font should be the same as the initial font", font, chosenFont);
 dialog.dispose();
 }

 @Test
 public void testCustomFont() {
 // Test custom font selection
 Font customFont = new Font("Arial", Font.BOLD, 12);
 dialog.chooserPane.setFont(customFont);
 dialog.getRootPane().getDefaultButton().doClick();
 Font chosenFont = dialog.chooserPane.getFont();
 assertNotEquals("Custom font should not be the same as the default font", font, chosenFont);
 dialog.dispose();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullComponent() {
 // Test exception when passing null as parent component
 dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, new JFontChooser(), new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // Handle OK button click
 }
 }, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // Handle Cancel button click
 }
 });
 }

 @Test
 public void testNoListeners() {
 // Test exception when neither OK nor Cancel listeners are provided
 dialog = new FontChooserDialog(parentFrame, "Font Chooser Dialog", true, new JFontChooser(), null, null);
 dialog.setVisible(true);
 dialog.dispose();
 }
}