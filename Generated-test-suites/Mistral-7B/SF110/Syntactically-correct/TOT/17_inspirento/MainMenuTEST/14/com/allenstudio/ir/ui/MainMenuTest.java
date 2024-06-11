package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Image;
import java.awt.KeyEvent;
import java.awt.event.InputEvent;
import java.io.File;
import java.net.URL;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.util.UIResources;

public class MainMenuTest {

 private InspirentoMediator mediator;
 private MainMenu mainMenu;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 mediator = mock(InspirentoMediator.class);
 mainMenu = new MainMenu(mediator);
 }

 @Test
 public void testMenuCreation() {
 String[] arr = {"File", "Edit", "Help"}; // Menus from InspirentoUtilities.tokenize(UIResources.getString("menu"))

 for (String menu : arr) {
 JMenu expectedMenu = new JMenu(menu);
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertEquals(expectedMenu.getText(), actualMenu.getText());
 }
 }

 @Test
 public void testMenuItemCreation() {
 String[] menuItems = {"New", "Open", "Save", "Save As", "Exit"}; // Items under "File" menu

 for (String menuItem : menuItems) {
 if (menuItem.equals("-")) {
 continue;
 }

 JMenuItem expectedMenuItem = new JMenuItem(menuItem);
 JMenuItem actualMenuItem = null;

 if (menuItem.length() > 1 && menuItem.charAt(menuItem.length() - 1) == '>') {
 JMenu expectedSubmenu = new JMenu(menuItem.substring(0, menuItem.length() - 1));
 String[] submenuItems = {"Save As Template"}; // Item under "Save As" menu
 JMenu actualSubmenu = mainMenu.createMenu(menuItem.substring(0, menuItem.length() - 1));

 assertNotNull(expectedSubmenu);
 assertNotNull(actualSubmenu);
 assertFalse(expectedSubmenu.isVisible()); // No need to test submenu visibility in test case
 assertFalse(actualSubmenu.isVisible());

 for (String submenuItem : submenuItems) {
 JMenuItem subexpectedMenuItem = new JMenuItem(submenuItem);
 JMenuItem subactualMenuItem = actualSubmenu.getItem(0);

 assertEquals(subexpectedMenuItem.getText(), subactualMenuItem.getText());
 }

 JMenuItem expectedGrandchildMenuItem = new JMenuItem(UIResources.getString("SaveAsTemplateLabel"));
 JMenuItem actualGrandchildMenuItem = ((JCheckBoxMenuItem) actualSubmenu.getItem(0)).getTopLevelAncestor().getItem(1);

 assertEquals(expectedGrandchildMenuItem.getText(), actualGrandchildMenuItem.getText());
 }
 else {
 actualMenuItem = mainMenu.createMenu(menuItem).getItem(0);
 }

 assertEquals(expectedMenuItem.getText(), actualMenuItem.getText());
 }
 }

 @Test
 public void testShortcutAndIcon() {
 String[] menuItems = {"New", "Open", "Save", "Save As", "Exit"}; // Items under "File" menu
 String[] shortcutsAndIcons = {"Ctrl+N", "Ctrl+O", "Ctrl+S", "Ctrl+Shift+S", "Alt+F4"};

 for (int i = 0; i < menuItems.length; i++) {
 if (menuItems[i].equals("-")) {
 continue;
 }

 JMenuItem menuItem = mainMenu.createMenu(menuItems[i]).getItem(0);

 assertEquals(KeyStroke.getKeyStroke(shortcutsAndIcons[i].charAt(0), shortcutsAndIcons[i].charAt(1)), menuItem.getAccelerator());

 if (menuItem instanceof JCheckBoxMenuItem) {
 Image expectedIcon = new ImageIcon(new File("icon.png").getAbsoluteFile()).getImage(); // Assuming an icon.png file exists
 Image actualIcon = ((JCheckBoxMenuItem) menuItem).getIcon();

 assertEquals(expectedIcon.getWidth(null), actualIcon.getWidth(null));
 assertEquals(expectedIcon.getHeight(null), actualIcon.getHeight(null));
 assertEquals(expectedIcon, actualIcon);
 }
 }
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingResourceException() {
 mainMenu.createMenu("");
 }
}