package com.allenstudio.ir.ui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import static java.lang.String.format;

public class MainMenuTest {

 private static final String TEST_MENU_LABEL = "TestMenu";
 private static final String TEST_SUBMENU_LABEL = "TestSubmenu";
 private static final String TEST_CHECKED_ITEM_LABEL = "TestCheckedItem";
 private static final String TEST_UNCHECKED_ITEM_LABEL = "TestUncheckedItem";
 private static final String TEST_SEPARATOR = "-";
 private static final String TEST_SHORTCUT_KEY = "F1";
 private static final String TEST_ICON_PATH = "/com/allenstudio/ir/ui/testIcon.png";

 private InspirentoMediator mediator;
 private MainMenu mainMenu;

 @Before
 public void setUp() {
 mediator = new InspirentoMediator(); // Create a test mediator
 mainMenu = new MainMenu(mediator); // Create a MainMenu instance with the test mediator
 }

 @Test
 public void testMainMenuConstruction() {
 // Test that the MainMenu is created with the correct menus
 String[] menus = InspirentoUtilities.tokenize(UIResources.getString("menu"));
 assertEquals("Incorrect number of menus created", menus.length, mainMenu.getMenuCount());

 for (String menu : menus) {
 JMenu menuComponent = mainMenu.getMenu(menu);
 assertNotNull("Menu component was null", menuComponent);
 }
 }

 @Test
 public void testCreateMenu() {
 // Test the creation of a single-level menu
 String testMenuLabel = "TestMenu";
 JMenu testMenu = mainMenu.createMenu(TEST_MENU_LABEL);
 assertEquals("Incorrect label for created menu", testMenuLabel, testMenu.getText());

 // Test the creation of a menu with a submenu
 String testSubmenuLabel = "TestSubmenu";
 JMenu testSubmenu = mainMenu.createMenu(TEST_SUBMENU_LABEL);
 testMenu.add(testSubmenu);
 assertEquals("Incorrect label for created submenu", testSubmenuLabel, testSubmenu.getText());
 }

 @Test
 public void testAddShortcutAndIcon() {
 // Test adding shortcut and icon to a checkbox menu item
 JCheckBoxMenuItem testCheckedMenuItem = new JCheckBoxMenuItem(TEST_CHECKED_ITEM_LABEL);
 String testCheckedItemKey = "TestCheckedItem";
 mainMenu.addShortcutAndIcon(testCheckedMenuItem, testCheckedItemKey);
 assertEquals("Incorrect shortcut key for checkbox menu item", TEST_SHORTCUT_KEY, testCheckedMenuItem.getAccelerator().getKeyCode());
 assertNull("Icon file present but should not be", testCheckedMenuItem.getIcon());

 try {
 String testCheckedItemIconPath = UIResources.getString(testCheckedItemKey + "Icon");
 assertEquals("Incorrect icon path for checkbox menu item", TEST_ICON_PATH, testCheckedItemIconPath);
 URL testCheckedItemIconUrl = new URL(testCheckedItemIconPath);
 testCheckedMenuItem.setIcon(new ImageIcon(testCheckedItemIconUrl));
 ImageIcon testCheckedMenuItemIcon = (ImageIcon) testCheckedMenuItem.getIcon();
 assertNotNull("Icon should not be null", testCheckedMenuItemIcon);
 } catch (MissingResourceException e) {
 // This is expected as we don't have a test icon file
 }

 // Test adding shortcut and icon to a non-checkbox menu item
 JMenuItem testUncheckedMenuItem = new JMenuItem(TEST_UNCHECKED_ITEM_LABEL);
 String testUncheckedItemKey = "TestUncheckedItem";
 mainMenu.addShortcutAndIcon(testUncheckedMenuItem, testUncheckedItemKey);
 assertEquals("Incorrect shortcut key for non-checkbox menu item", TEST_SHORTCUT_KEY, testUncheckedMenuItem.getAccelerator().getKeyCode());
 assertNull("Icon file present but should not be", testUncheckedMenuItem.getIcon());
 }

 @Test
 public void testMenuItemAction() {
 // Test handling of menu item action events
 ActionListener actionListener = e -> {
 System.out.println("Menu item with label '" + e.getActionCommand() + "' was clicked.");
 };
 JMenuItem testMenuItem = new JMenuItem(TEST_SEPARATOR);
 testMenuItem.addActionListener(actionListener);
 mainMenu.add(testMenuItem);
 testMenuItem.doClick();
 assertTrue("Action listener not called when separator is clicked", actionListener.wasInvoked());

 testMenuItem = new JMenuItem(TEST_UNCHECKED_ITEM_LABEL);
 testMenuItem.addActionListener(actionListener);
 mainMenu.add(testMenuItem);
 testMenuItem.doClick();
 assertTrue("Action listener not called when unchecked item is clicked", actionListener.wasInvoked());
 }
}