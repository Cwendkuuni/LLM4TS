package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.ui.MainMenu;
import com.allenstudio.ir.util.UIResources;

public class MainMenuTest {

 private MainMenu mainMenu;
 private InspirentoMediator mediator;

 @Before
 public void setup() {
 mediator = new InspirentoMediator();
 mainMenu = new MainMenu(mediator);
 }

 @Test
 public void testMainMenuConstructor() {
 // Test that the MainMenu constructor does not throw an exception
 mainMenu = new MainMenu(mediator);
 }

 @Test
 public void testCreateMenu() {
 String menu = "File";
 JMenu expectedMenu = new JMenu("File");
 try {
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertEquals(expectedMenu.getText(), actualMenu.getText());
 }
 catch (MissingResourceException e) {
 fail("Failed to load menu label: " + e.getMessage());
 }
 }

 @Test
 public void testCreateMenuWithSubMenusAndCheckboxItems() {
 String menu = "Edit";
 String subMenu = "Undo";
 String checkboxItem = "Toggle Select All";
 JMenu expectedMenu = new JMenu("Edit");
 JMenu subExpectedMenu = new JMenu("Undo");
 JCheckBoxMenuItem expectedCheckboxMenuItem = new JCheckBoxMenuItem("Toggle Select All");
 try {
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertTrue(actualMenu.getText().equals("Edit"));
 JMenu subMenuActual = (JMenu) actualMenu.getComponent(0);
 assertEquals(subMenu, subMenuActual.getText());
 JMenu checkboxItemMenu = (JMenu) subMenuActual.getComponent(0);
 assertEquals(checkboxItem, checkboxItemMenu.getText());
 JCheckBoxMenuItem checkboxMenuItem = (JCheckBoxMenuItem) checkboxItemMenu.getComponent(0);
 assertEquals(expectedCheckboxMenuItem.getText(), checkboxMenuItem.getText());
 }
 catch (MissingResourceException e) {
 fail("Failed to load menu label(s): " + e.getMessage());
 }
 }

 @Test
 public void testCreateMenuWithSeparator() {
 String menu = "Help";
 JMenu expectedMenu = new JMenu("Help");
 try {
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertTrue(actualMenu.getText().equals("Help"));
 JMenuItem separator = (JMenuItem) actualMenu.getComponent(1);
 assertTrue(separator.getHorizontalAlignment() == SwingConstants.LEFT);
 }
 catch (MissingResourceException e) {
 fail("Failed to load menu label(s): " + e.getMessage());
 }
 }

 @Test
 public void testCreateMenuWithEmptyLabel() {
 String menu = "";
 try {
 mainMenu.createMenu(menu);
 fail("Expected MissingResourceException but no exception was thrown");
 }
 catch (MissingResourceException e) {
 // Expected
 }
 }

 @Test(expected = MissingResourceException.class)
 public void testCreateMenuWithInvalidLabel() {
 String menu = "InvalidLabel";
 mainMenu.createMenu(menu);
 fail("Expected MissingResourceException but no exception was thrown");
 }

 @Test
 public void testAddShortcutAndIcon() {
 String key = "File.Open";
 String shortcut = "Ctrl+O";
 String iconPath = "icon.png";
 JMenuItem expectedMenuItem = new JMenuItem("Open");
 expectedMenuItem.setMnemonic(KeyEvent.VK_O);
 expectedMenuItem.setAccelerator(KeyStroke.getKeyStroke(shortcut));
 try {
 URL iconUrl = new URL("file:" + new File(iconPath).getAbsolutePath());
 ImageIcon expectedIcon = new ImageIcon(iconUrl);
 expectedMenuItem.setIcon(expectedIcon);
 }
 catch (Exception e) {
 fail("Failed to load icon file: " + iconPath);
 }
 JMenuItem actualMenuItem = new JMenuItem("Open");
 mainMenu.addShortcutAndIcon(actualMenuItem, key);
 assertEquals(expectedMenuItem.getText(), actualMenuItem.getText());
 assertEquals(expectedMenuItem.getMnemonic(), actualMenuItem.getMnemonic());
 assertEquals(expectedMenuItem.getAccelerator(), actualMenuItem.getAccelerator());
 try {
 ImageIcon actualIcon = (ImageIcon) actualMenuItem.getIcon();
 assertEquals(expectedIcon.getImage(), actualIcon.getImage());
 }
 catch (Exception e) {
 fail("Expected an ImageIcon but got: " + actualMenuItem.getIcon().getClass().getName());
 }
 }

 @Test
 public void testAddShortcutAndIconWithEmptyKey() {
 String key = "";
 JMenuItem item = new JMenuItem("Open");
 mainMenu.addShortcutAndIcon(item, key);
 assertNull(item.getAccelerator());
 }

 @Test
 public void testAddShortcutAndIconWithMissingIcon() {
 String key = "File.Open";
 JMenuItem item = new JMenuItem("Open");
 mainMenu.addShortcutAndIcon(item, key);
 assertNull(item.getIcon());
 }

 @Test
 public void testAddShortcutAndIconWithMissingShortcut() {
 String key = "File.Open";
 JMenuItem item = new JMenuItem("Open");
 mainMenu.addShortcutAndIcon(item, key);
 assertNull(item.getAccelerator());
 }
}