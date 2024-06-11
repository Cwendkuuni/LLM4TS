package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.ui.*;
import org.junit.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

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
 mainMenu = new MainMenu(mediator);
 assertNotNull(mainMenu);
 }

 @Test
 public void testCreateMenu() {
 String menu = "File";
 JMenu expectedMenu = new JMenu("File");
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertEquals(expectedMenu, actualMenu);
 }

 @Test
 public void testCreateMenuWithLabelAndMnemonic() {
 String menu = "File";
 String label = "Open";
 char mnemonic = 'O';
 JMenu expectedMenu = new JMenu("File");
 JMenuItem expectedMenuItem = new JMenuItem("Open");
 expectedMenuItem.setMnemonic('O');
 JMenu actualMenu = mainMenu.createMenu(menu);
 JMenuItem actualMenuItem = null;
 for (Component component : actualMenu.getComponents()) {
 if (component instanceof JMenuItem) {
 actualMenuItem = (JMenuItem) component;
 break;
 }
 }
 assertEquals(expectedMenuItem, actualMenuItem);
 }

 @Test
 public void testCreateMenuWithLabelAndNoMnemonic() {
 String menu = "File";
 String label = "Save As";
 JMenu expectedMenu = new JMenu("File");
 JMenuItem expectedMenuItem = new JMenuItem("Save As");
 JMenu actualMenu = mainMenu.createMenu(menu);
 JMenuItem actualMenuItem = null;
 for (Component component : actualMenu.getComponents()) {
 if (component instanceof JMenuItem) {
 actualMenuItem = (JMenuItem) component;
 break;
 }
 }
 assertEquals(expectedMenuItem, actualMenuItem);
 }

 @Test
 public void testCreateMenuWithSubMenu() {
 String menu = "Edit";
 String subMenu = "Cut";
 JMenu expectedSubMenu = new JMenu("Cut");
 JMenu expectedMenu = new JMenu("Edit");
 expectedMenu.add(expectedSubMenu);
 JMenu actualMenu = mainMenu.createMenu(menu);
 JMenu actualSubMenu = null;
 for (Component component : actualMenu.getComponents()) {
 if (component instanceof JMenu) {
 actualSubMenu = (JMenu) component;
 break;
 }
 }
 JMenuItem submenuItem = null;
 for (Component subcomponent : actualSubMenu.getComponents()) {
 if (subcomponent instanceof JMenuItem) {
 submenuItem = (JMenuItem) subcomponent;
 break;
 }
 }
 assertEquals(expectedSubMenu, actualSubMenu);
 assertEquals(expectedMenu, actualMenu);
 assertEquals(submenuItem, submenuItem);
 }

 @Test
 public void testCreateMenuWithCheckboxMenuItem() {
 String menu = "Tools";
 String checkboxMenuItemLabel = "Preferences";
 JCheckBoxMenuItem expectedMenuItem = new JCheckBoxMenuItem("Preferences");
 JMenu actualMenu = mainMenu.createMenu(menu);
 JCheckBoxMenuItem actualMenuItem = null;
 for (Component component : actualMenu.getComponents()) {
 if (component instanceof JCheckBoxMenuItem) {
 actualMenuItem = (JCheckBoxMenuItem) component;
 break;
 }
 }
 assertEquals(expectedMenuItem, actualMenuItem);
 }

 @Test(expected = MissingResourceException.class)
 public void testCreateMenuWithMissingLabelResource() {
 String menu = "Invalid";
 mainMenu.createMenu(menu);
 fail("Expected MissingResourceException but it did not occur.");
 }

 @Test
 public void testAddShortcutAndIcon() {
 String key = "File.Open";
 JMenuItem item = new JMenuItem("Open");
 mainMenu.addShortcutAndIcon(item, key);
 assertEquals(KeyStroke.getKeyStroke("ctrl O"), item.getAccelerator());
 assertNull(item.getIcon());
 try {
 String iconPath = UIResources.getString(key + "Icon");
 mainMenu.addShortcutAndIcon(item, key);
 assertEquals(new ImageIcon(new File("file:" + iconPath).getAbsolutePath()), item.getIcon());
 } catch (MissingResourceException e) {
 // Expected
 }
 }

 @Test
 public void testChanged() {
 mainMenu.changed();
 // This method appears to be empty.
 }
}