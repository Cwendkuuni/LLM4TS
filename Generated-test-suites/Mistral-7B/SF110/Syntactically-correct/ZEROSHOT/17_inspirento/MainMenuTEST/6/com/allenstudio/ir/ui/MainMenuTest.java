package com.allenstudio.ir.ui;

import static org.junit.Assert.*;

import org.junit.Test;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class MainMenuTest {

 private MainMenu mainMenu;
 private InspirentoMediator mediator;

 @Test
 public void testMainMenuConstruction() {
  mediator = new InspirentoMediator();
  mainMenu = new MainMenu(mediator);
  assertNotNull(mainMenu);
 }

 @Test
 public void testCreateMenu() {
  String menuLabel = UIResources.getString("menu1"); // Replace with actual valid menu label
  JMenu menu = mainMenu.createMenu(menuLabel);
  assertNotNull(menu);
 }

 @Test
 public void testMenuLabelCreation() {
  String menuLabel = UIResources.getString("menu1Label"); // Replace with actual valid menu label
  JMenu menu = mainMenu.createMenu(UIResources.getString("menu1"));
  assertEquals(menuLabel, menu.getText());
 }

 @Test
 public void testMenuMnemonicSetUp() {
  String menuLabel = UIResources.getString("menu1"); // Replace with actual valid menu label
  JMenu menu = mainMenu.createMenu(UIResources.getString("menu1"));
  int mnemonic = menuLabel.charAt(menuLabel.length() - 1);
  assertEquals(mnemonic, menu.getMnemonic());
 }

 @Test
 public void testCreateSubMenu() {
  String menuLabel = UIResources.getString("menu1"); // Replace with actual valid menu label
  String subMenuLabel = UIResources.getString("subMenu1"); // Replace with actual valid sub menu label
  JMenu menu = mainMenu.createMenu(menuLabel);
  JMenu subMenu = menu.add(mainMenu.createMenu(subMenuLabel));
  assertNotNull(subMenu);
 }

 @Test
 public void testCheckBoxMenuItemCreation() {
  String checkBoxMenuItemKey = "checkBoxMenuItem1"; // Replace with actual valid check box menu item key
  String checkBoxMenuItemLabel = UIResources.getString(checkBoxMenuItemKey + "Label"); // Replace with actual valid check box menu item label
  JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) mainMenu.createMenu("menu1").add(mainMenu.createMenuItem(checkBoxMenuItemKey));
  assertEquals(checkBoxMenuItemLabel, checkBoxMenuItem.getText());
 }

 @Test
 public void testMenuItemCreation() {
  String menuItemKey = "menuItem1"; // Replace with actual valid menu item key
  String menuItemLabel = UIResources.getString(menuItemKey + "Label"); // Replace with actual valid menu item label
  JMenuItem menuItem = (JMenuItem) mainMenu.createMenu("menu1").add(mainMenu.createMenuItem(menuItemKey));
  assertEquals(menuItemLabel, menuItem.getText());
 }

 @Test
 public void testMenuItemMnemonicSetUp() {
  String menuItemKey = "menuItem1"; // Replace with actual valid menu item key
  String menuItemLabel = UIResources.getString(menuItemKey + "Label"); // Replace with actual valid menu item label
  JMenuItem menuItem = (JMenuItem) mainMenu.createMenu("menu1").add(mainMenu.createMenuItem(menuItemKey));
  int mnemonic = menuItemLabel.charAt(menuItemLabel.length() - 1);
  assertEquals(mnemonic, menuItem.getMnemonic());
 }

 @Test
 public void testShortcutKeySetUp() {
  String menuItemKey = "menuItem1"; // Replace with actual valid menu item key
  String shortcutKey = UIResources.getString(menuItemKey + "Shortcut"); // Replace with actual valid shortcut key
  JMenuItem menuItem = (JMenuItem) mainMenu.createMenu("menu1").add(mainMenu.createMenuItem(menuItemKey));
  KeyStroke expectedShortcut = KeyStroke.getKeyStroke(shortcutKey);
  KeyStroke actualShortcut = menuItem.getAccelerator();
  assertEquals(expectedShortcut, actualShortcut);
 }

 @Test
 public void testIconSetUp() {
  String menuItemKey = "menuItem1"; // Replace with actual valid menu item key
  String iconPath = UIResources.getString(menuItemKey + "Icon"); // Replace with actual valid icon path
  JMenuItem menuItem = (JMenuItem) mainMenu.createMenu("menu1").add(mainMenu.createMenuItem(menuItemKey));
  ImageIcon expectedIcon = new ImageIcon(new File(iconPath).getAbsolutePath());
  ImageIcon actualIcon = (ImageIcon) menuItem.getIcon();
  assertEquals(expectedIcon.getImage(), actualIcon.getImage());
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuLabel() {
  mainMenu = new MainMenu(mediator);
  mainMenu.createMenu("missingMenuLabel");
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuItemLabel() {
  String menuLabel = UIResources.getString("menu1");
  JMenu menu = mainMenu.createMenu(menuLabel);
  menu.add(mainMenu.createMenuItem("missingMenuItemLabel"));
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingShortcutKey() {
  String menuItemKey = "menuItem1";
  JMenuItem menuItem = (JMenuItem) mainMenu.createMenu("menu1").add(mainMenu.createMenuItem(menuItemKey));
  menuItem.setAccelerator(null);
  menuItem.getAccelerator();
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingIconPath() {
  String menuItemKey = "menuItem1";
  JMenuItem menuItem = (JMenuItem) mainMenu.createMenu("menu1").add(mainMenu.createMenuItem(menuItemKey));
  menuItem.setIcon(null);
  menuItem.getIcon();
 }
}