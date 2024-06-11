package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.junit.Before;
import org.junit.Test;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.ui.MainMenu;

public class MainMenuTest {

 private MainMenu mainMenu;
 private InspirentoMediator mediator;

 @Before
 public void setUp() {
 mediator = new InspirentoMediator();
 mainMenu = new MainMenu(mediator);
 }

 @Test
 public void testMainMenuConstruction() {
 JMenuBar menuBar = new JMenuBar();
 assertEquals("Unexpected number of components", 0, menuBar.getComponentCount());
 mainMenu = new MainMenu(mediator);
 menuBar.add(mainMenu);
 assertEquals("Unexpected number of components", 8, menuBar.getComponentCount()); // Based on the given UIResources string
 }

 @Test
 public void testCreateMenu() {
 String menu = "File";
 JMenu expectedMenu = new JMenu("File");
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertEquals("Unexpected menu label", expectedMenu.getText(), actualMenu.getText());
 }

 @Test
 public void testAddMenuItem() {
 String menu = "File";
 String item = "#Open";
 JMenuItem expectedMenuItem = new JMenuItem("Open");
 JMenuItem actualMenuItem = null;
 JMenu menuToCheck = mainMenu.createMenu(menu);
 for (int i = 0; i < menuToCheck.getItemCount(); i++) {
 actualMenuItem = (JMenuItem) menuToCheck.getItem(i);
 if (actualMenuItem.getText().equals(expectedMenuItem.getText())) {
 break;
 }
 }
 assertEquals("Unexpected menu item label", expectedMenuItem, actualMenuItem);
 }

 @Test
 public void testAddCheckBoxMenuItem() {
 String menu = "Options";
 String item = "-"; // Separator
 String subItem = "Save Settings";
 JCheckBoxMenuItem expectedMenuItem = new JCheckBoxMenuItem("Save Settings");
 JCheckBoxMenuItem actualMenuItem = null;
 JMenu menuToCheck = mainMenu.createMenu(menu);
 for (int i = 0; i < menuToCheck.getItemCount(); i++) {
 JMenu subMenu = (JMenu) menuToCheck.getItem(i);
 for (int j = 0; j < subMenu.getItemCount(); j++) {
 actualMenuItem = (JCheckBoxMenuItem) subMenu.getItem(j);
 if (actualMenuItem.getText().equals(expectedMenuItem.getText())) {
 break;
 }
 }
 assertEquals("Unexpected checkbox menu item label", expectedMenuItem, actualMenuItem);
 }
}

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuLabel() {
 String menu = "UnknownMenu";
 mainMenu.createMenu(menu);
 }

 @Test
 public void testAddSeparator() {
 String menu = "Help";
 JMenuItem separator = mainMenu.createMenu(menu + "Separator");
 assertNotNull("Separator should not be null", separator);
 }

 @Test
 public void testAddShortcutAndIcon() {
 String menu = "File";
 String item = "#Open";
 JMenuItem expectedMenuItem = new JMenuItem("Open");
 JMenuItem actualMenuItem = null;
 JMenu menuToCheck = mainMenu.createMenu(menu);
 for (int i = 0; i < menuToCheck.getItemCount(); i++) {
 actualMenuItem = (JMenuItem) menuToCheck.getItem(i);
 if (actualMenuItem.getText().equals(expectedMenuItem.getText())) {
 break;
 }
 }
 String shortcut = "Ctrl + O";
 assertEquals("Unexpected shortcut", KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK), actualMenuItem.getAccelerator());
 String iconPath = "test_icon.png";
 File expectedIconFile = new File(iconPath);
 File actualIconFile = null;
 if (actualMenuItem instanceof JCheckBoxMenuItem) {
 actualIconFile = ((JCheckBoxMenuItem) actualMenuItem).getIcon().getImage().getSource();
 }
 else {
 actualIconFile = ((JMenuItem) actualMenuItem).getIcon().getImage().getSource();
 }
 assertTrue("Icon file should exist", expectedIconFile.exists() && actualIconFile != null && actualIconFile.getAbsolutePath().equals(expectedIconFile.getAbsolutePath()));
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingShortcut() {
 String menu = "File";
 String item = "#Open";
 mainMenu.addShortcutAndIcon(new JMenuItem("Open"), item);
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingIcon() {
 String menu = "File";
 String item = "#Open";
 JMenuItem itemMenu = new JMenuItem("Open");
 mainMenu.addShortcutAndIcon(itemMenu, item);
 }

 @Test
 public void testChanged() {
 mainMenu.changed();
 // This method is empty in the given code
 // Add an assertion here if you expect it to behave differently
 }
}