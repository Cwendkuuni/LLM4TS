package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.ui.*;
import com.allenstudio.ir.util.*;

public class MainMenuTest {

 private MainMenu mainMenu;
 private InspirentoMediator mediator;

 @Before
 public void setUp() {
 mediator = mock(InspirentoMediator.class);
 mainMenu = new MainMenu(mediator);
 }

 // Test constructor
 @Test
 public void testConstructor() {
 assertNotNull(mainMenu);
 }

 // Test createMenu method with valid menu
 @Test
 public void testCreateMenuValidMenu() {
 String menu = "File";
 JMenu expectedMenu = new JMenu("File");
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertEquals(expectedMenu.getText(), actualMenu.getText());
 }

 // Test createMenu method with empty menu
 @Test(expected = IllegalArgumentException.class)
 public void testCreateMenuEmptyMenu() {
 String menu = "";
 mainMenu.createMenu(menu);
 fail("IllegalArgumentException expected but not thrown");
 }

 // Test createMenu method with menu containing items
 @Test
 public void testCreateMenuMenuWithItems() {
 String menu = "Edit";
 String item1 = "Undo";
 String item2 = "Redo";
 String item3 = "Separator";
 String item4 = "Cut";
 String item5 = "Copy";
 String item6 = "Paste";
 JMenu expectedMenu = new JMenu("Edit");
 JMenuItem undoMenuItem = new JMenuItem("Undo");
 JMenuItem redoMenuItem = new JMenuItem("Redo");
 JSeparator separator = new JSeparator();
 JMenuItem cutMenuItem = new JMenuItem("Cut");
 JMenuItem copyMenuItem = new JMenuItem("Copy");
 JMenuItem pasteMenuItem = new JMenuItem("Paste");
 expectedMenu.add(undoMenuItem);
 expectedMenu.add(redoMenuItem);
 expectedMenu.add(separator);
 expectedMenu.add(cutMenuItem);
 expectedMenu.add(copyMenuItem);
 expectedMenu.add(pasteMenuItem);
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertEquals(expectedMenu.getMenuComponentCount(), actualMenu.getMenuComponentCount());
 for (int i = 0; i < expectedMenu.getMenuComponentCount(); i++) {
 JComponent expectedComponent = expectedMenu.getMenuComponent(i);
 JComponent actualComponent = actualMenu.getMenuComponent(i);
 if (expectedComponent instanceof JMenuItem) {
 assertEquals(expectedComponent.getName(), actualComponent.getName());
 }
 else if (expectedComponent instanceof JSeparator) {
 assertTrue(actualComponent instanceof JSeparator);
 }
 }
 }

 // Test createMenu method with menu containing nested menus
 @Test
 public void testCreateMenuMenuWithNestedMenus() {
 String menu = "Tools";
 String subMenu = "Submenu";
 String item1 = "Item1";
 String item2 = "Item2";
 JMenu expectedMenu = new JMenu("Tools");
 JMenu subExpectedMenu = new JMenu("Submenu");
 JMenuItem item1MenuItem = new JMenuItem("Item1");
 JMenuItem item2MenuItem = new JMenuItem("Item2");
 expectedMenu.add(subExpectedMenu);
 subExpectedMenu.add(item1MenuItem);
 subExpectedMenu.add(item2MenuItem);
 JMenu actualMenu = mainMenu.createMenu(menu);
 JMenu actualSubMenu = (JMenu) actualMenu.findComponent(subMenu);
 assertEquals(expectedMenu.getMenuComponentCount(), actualMenu.getMenuComponentCount());
 assertEquals(expectedMenu.getName(), actualMenu.getName());
 assertEquals(subExpectedMenu.getName(), actualSubMenu.getName());
 assertEquals(expectedSubMenu.getMenuComponentCount(), actualSubMenu.getMenuComponentCount());
 assertEquals(expectedMenu.getMenuComponent(0).getName(), actualMenu.getMenuComponent(0).getName());
 assertNotNull(actualSubMenu.getMenuComponent(0));
 assertNotNull(actualSubMenu.getMenuComponent(1));
 }

 // Test addShortcutAndIcon method with valid shortcut
 @Test
 public void testAddShortcutAndIconValidShortcut() {
 String key = "File.Open";
 JMenuItem item = mock(JMenuItem.class);
 String shortcut = "Ctrl+O";
 mainMenu.addShortcutAndIcon(item, key);
 assertEquals(KeyStroke.getKeyStroke(shortcut), item.getAccelerator());
 }

 // Test addShortcutAndIcon method with missing shortcut
 @Test
 public void testAddShortcutAndIconMissingShortcut() {
 String key = "File.SaveAs";
 JMenuItem item = mock(JMenuItem.class);
 mainMenu.addShortcutAndIcon(item, key);
 assertNull(item.getAccelerator());
 }

 // Test addShortcutAndIcon method with valid icon
 @Test
 public void testAddShortcutAndIconValidIcon() {
 String key = "File.SaveAs";
 String iconPath = "/icons/save_as.png";
 JMenuItem item = mock(JMenuItem.class);
 mainMenu.addShortcutAndIcon(item, key);
 assertTrue(item.getIcon() instanceof ImageIcon);
 }

 // Test addShortcutAndIcon method with missing icon
 @Test
 public void testAddShortcutAndIconMissingIcon() {
 String key = "File.SaveAs";
 JMenuItem item = mock(JMenuItem.class);
 mainMenu.addShortcutAndIcon(item, key);
 assertNull(item.getIcon());
 }

 // Test changed method
 @Test
 public void testChanged() {
 mainMenu.changed();
 // NOP
 }
}