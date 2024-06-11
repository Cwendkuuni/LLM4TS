package com.allenstudio.ir.ui;

import static org.junit.Assert.*;

import com.allenstudio.ir.core.*;
import org.junit.Test;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;

public class MainMenuTest {

 private static final String UI_RESOURCES_STRING = "menu";
 private static final String[] MENU_ITEMS = {"File", "Edit", "Help"};
 private static final String[] SUB_MENU_ITEMS = {"New", "Open", "Save As", "Exit", "Cut", "Copy", "Paste", "Separator", "About"};
 private static final String[] CHECKBOX_MENU_ITEMS = {"Show Grid", "Show Grid Lines", "Zoom In", "Zoom Out"};
 private static final String[] SHORTCUT_KEYS = {"F1", "Ctrl+O", "Ctrl+S", "Alt+F4", "Ctrl+X", "Ctrl+C", "Ctrl+V"};
 private static final String[] ICON_PATHS = {"file.png", "edit.png", "help.png"};
 private static final String[] CHECKBOX_ICON_PATHS = {"grid.png", "gridlines.png", "zoom_in.png", "zoom_out.png"};

 @Test
 public void testMainMenuConstruction() {
 MainMenu mainMenu = new MainMenu(new InspirentoMediator());
 JMenuBar jMenuBar = mainMenu.getJMenuBar();
 assertNotNull(jMenuBar);

 for (String menuName : MENU_ITEMS) {
 JMenu menu = (JMenu) jMenuBar.getComponent(0).getComponent(jMenuBar.getComponentCount() - 1);
 assertEquals(menuName, menu.getText());

 for (String itemName : SUB_MENU_ITEMS) {
 if (itemName.contains("Separator")) {
 continue;
 }
 JMenuItem item = null;
 if (itemName.charAt(itemName.length() - 1) == '>') {
 JMenu subMenu = (JMenu) menu.getComponent(0);
 for (Component component : subMenu.getComponents()) {
 JMenuItem subItem = (JMenuItem) component;
 if (subItem.getText().equals(itemName.substring(0, itemName.length() - 1))) {
 item = subItem;
 break;
 }
 }
 } else {
 for (Component component : menu.getComponents()) {
 JMenuItem item = (JMenuItem) component;
 if (item.getText().equals(itemName)) {
 break;
 }
 }
 }
 assertNotNull(item);

 String shortcutKey = SHORTCUT_KEYS[Arrays.asList(MENU_ITEMS).indexOf(menuName) * SUB_MENU_ITEMS.length + Arrays.asList(SUB_MENU_ITES).indexOf(itemName)];
 assertEquals(shortcutKey, item.getAccelerator().getKeyCode(), "Shortcut key doesn't match");

 if (itemName.length() > 1 && itemName.charAt(itemName.length() - 1) == '>') {
 continue;
 }
 String iconPath = ICON_PATHS[Arrays.asList(MENU_ITEMS).indexOf(menuName)];
 ImageIcon expectedIcon = new ImageIcon(getClass().getResource(iconPath));
 ImageIcon actualIcon = (ImageIcon) item.getIcon();
 assertEquals("Icon doesn't match", expectedIcon.getImage(), actualIcon.getImage());
 }
 }
 }

 @Test
 public void testCreateMenu() {
 String menuName = "File";
 JMenu menu = new MainMenu(null).createMenu(menuName);
 assertNotNull(menu);

 String menuLabel = UIResources.getString(menuName + "Label");
 assertEquals(menuLabel, menu.getText());

 for (String itemName : SUB_MENU_ITEMS) {
 if (itemName.contains("Separator")) {
 continue;
 }
 JMenuItem item = null;
 if (itemName.charAt(itemName.length() - 1) == '>') {
 JMenu subMenu = new MainMenu(null).createMenu(itemName.substring(0, itemName.length() - 1));
 menu.add(subMenu);
 for (Component component : subMenu.getComponents()) {
 JMenuItem subItem = (JMenuItem) component;
 if (subItem.getText().equals(itemName.substring(0, itemName.length() - 1))) {
 item = subItem;
 break;
 }
 }
 } else {
 for (Component component : menu.getComponents()) {
 JMenuItem item = (JMenuItem) component;
 if (item.getText().equals(itemName)) {
 break;
 }
 }
 }
 assertNotNull(item);

 String shortcutKey = SHORTCUT_KEYS[Arrays.asList(MENU_ITEMS).indexOf(menuName) * SUB_MENU_ITEMS.length + Arrays.asList(SUB_MENU_ITES).indexOf(itemName)];
 assertEquals(shortcutKey, item.getAccelerator().getKeyCode(), "Shortcut key doesn't match");

 if (itemName.length() > 1 && itemName.charAt(itemName.length() - 1) == '>') {
 continue;
 }
 String iconPath = ICON_PATHS[Arrays.asList(MENU_ITEMS).indexOf(menuName)];
 ImageIcon expectedIcon = new ImageIcon(getClass().getResource(iconPath));
 ImageIcon actualIcon = (ImageIcon) item.getIcon();
 assertEquals("Icon doesn't match", expectedIcon.getImage(), actualIcon.getImage());
 }
 }

 @Test
 public void testAddShortcutAndIcon() {
 String menuName = "File";
 String itemName = "New";
 JMenu menu = new MainMenu(null).createMenu(menuName);
 JMenuItem item = new JMenuItem(UIResources.getString(itemName + "Label"));

 new MainMenu(null).addShortcutAndIcon(item, itemName);

 assertEquals(SHORTCUT_KEYS[Arrays.asList(MENU_ITEMS).indexOf(menuName) * SUB_MENU_ITEMS.length + Arrays.asList(SUB_MENU_ITES).indexOf(itemName)], item.getAccelerator().getKeyCode(), "Shortcut key doesn't match");

 String iconPath = ICON_PATHS[Arrays.asList(MENU_ITEMS).indexOf(menuName)];
 ImageIcon expectedIcon = new ImageIcon(getClass().getResource(iconPath));
 ImageIcon actualIcon = (ImageIcon) item.getIcon();
 assertEquals("Icon doesn't match", expectedIcon.getImage(), actualIcon.getImage());

 menu.add(item);
 }
}