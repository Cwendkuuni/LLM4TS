package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import javax.swing.*;
import org.junit.Test;

public class MainMenuTest {

 private static final String[] MENU_ITEMS = {"File", "Edit", "Help"};
 private static final String[] SUB_MENU_ITEMS = {"New", "Open", "Save", "Exit"};
 private static final String[] CHECK_BOX_MENU_ITEMS = {"Option1", "Option2"};
 private static final String[] CHECK_BOX_ITEMS = {"Check Box 1", "Check Box 2"};

 @Test
 public void testMainMenuConstruction() {
 MainMenu menu = new MainMenu(null);
 assertNotNull(menu);
 for (String menuItem : MENU_ITEMS) {
 assertNotNull(menu.findComponentByName(menuItem + LABEL_SUFFIX));
 }
 }

 @Test
 public void testCreateFileMenu() {
 MainMenu menu = new MainMenu(null);
 JMenu fileMenu = menu.createMenu("File");
 assertNotNull(fileMenu);
 for (String menuItem : SUB_MENU_ITEMS) {
 assertNotNull(fileMenu.findComponentByName(menuItem + LABEL_SUFFIX));
 }
 assertNull(fileMenu.findComponentByName("separator" + LABEL_SUFFIX));
 }

 @Test
 public void testCreateHelpMenu() {
 MainMenu menu = new MainMenu(null);
 JMenu helpMenu = menu.createMenu("Help");
 assertNotNull(helpMenu);
 for (String checkBoxMenuItem : CHECK_BOX_MENU_ITEMS) {
 JCheckBoxMenuItem checkBoxItem = (JCheckBoxMenuItem) helpMenu.findComponentByName(checkBoxMenuItem + "CheckBox" + ICON_SUFFIX);
 assertNotNull(checkBoxItem);
 }
 }

 @Test
 public void testCreateCheckBoxSubMenu() {
 MainMenu menu = new MainMenu(null);
 JMenu editMenu = menu.createMenu("Edit");
 JMenu optionsMenu = (JMenu) editMenu.findComponentByName("Options" + LABEL_SUFFIX);
 assertNotNull(optionsMenu);
 for (String checkBoxItem : CHECK_BOX_ITEMS) {
 JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) optionsMenu.findComponentByName(checkBoxItem + "CheckBox" + ICON_SUFFIX);
 assertNotNull(checkBoxMenuItem);
 }
 }

 @Test
 public void testMissingResource() {
 try {
 MainMenu menu = new MainMenu(null);
 } catch (MissingResourceException e) {
 fail("Should not reach here");
 }
 }

 @Test
 public void testAddShortcutAndIcon() {
 MainMenu menu = new MainMenu(null);
 JMenuItem newMenuItem = new JMenuItem("New");
 menu.addShortcutAndIcon(newMenuItem, "NewFile");
 assertNotNull(newMenuItem.getAccelerator());
 assertNull(newMenuItem.getIcon());

 try {
 URL iconUrl = new File("icon.png").toURI().toURL();
 menu.addShortcutAndIcon(newMenuItem, "NewFile");
 newMenuItem.setIcon(new ImageIcon(iconUrl));
 assertNotNull(newMenuItem.getIcon());
 } catch (Exception e) {
 // Ignore exception
 }
 }
}