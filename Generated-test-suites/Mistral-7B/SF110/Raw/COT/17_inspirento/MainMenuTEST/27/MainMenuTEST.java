package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.ui.*;
import com.allenstudio.ir.util.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import org.junit.*;

public class MainMenuTest {

 private MainMenu mainMenu;
 private InspirentoMediator mediator;

 @Before
 public void setUp() {
 mediator = new InspirentoMediator();
 mainMenu = new MainMenu(mediator);
 }

 @Test
 public void testMainMenuConstructor() {
 // Test that the constructor does not throw an exception
 mainMenu = new MainMenu(null);
 }

 @Test
 public void testCreateMenu() {
 String menuLabel = "File";
 JMenu expectedMenu = new JMenu(menuLabel);
 JMenu actualMenu = mainMenu.createMenu(menuLabel);
 assertEquals(expectedMenu.getText(), actualMenu.getText());
 }

 @Test
 public void testCreateMenuWithMnemonicAndLabelTruncated() {
 String menuLabel = "Edit*";
 JMenu expectedMenu = new JMenu("Edit");
 expectedMenu.setMnemonic('E');
 JMenu actualMenu = mainMenu.createMenu(menuLabel);
 assertEquals(expectedMenu.getText(), actualMenu.getText());
 assertEquals(expectedMenu.getMnemonic(), actualMenu.getMnemonic());
 }

 @Test
 public void testCreateMenuWithSubMenus() {
 String menuLabel = "Tools";
 JMenu expectedMenu = new JMenu(menuLabel);
 JMenu editSubmenu = new JMenu("Edit");
 JMenu exportSubmenu = new JMenu("Export");
 expectedMenu.add(editSubmenu);
 expectedMenu.add(exportSubmenu);
 JMenu actualMenu = mainMenu.createMenu(menuLabel);
 assertEquals(expectedMenu.getText(), actualMenu.getText());
 assertTrue(actualMenu.getMenuComponentCount() > 0);
 JMenu actualEditSubmenu = (JMenu) actualMenu.getMenuComponent(0);
 assertEquals(editSubmenu.getText(), actualEditSubmenu.getText());
 JMenu actualExportSubmenu = (JMenu) actualMenu.getMenuComponent(1);
 assertEquals(exportSubmenu.getText(), actualExportSubmenu.getText());
 }

 @Test
 public void testCreateMenuWithCheckboxMenuItem() {
 String menuLabel = "Options";
 String checkboxMenuItemLabel = "Show Grid";
 JCheckBoxMenuItem expectedMenuItem = new JCheckBoxMenuItem(checkboxMenuItemLabel);
 JCheckBoxMenuItem actualMenuItem = null;
 try {
 actualMenuItem = (JCheckBoxMenuItem) mainMenu.createMenu(menuLabel + ">" + checkboxMenuItemLabel)[0];
 }
 catch (ArrayIndexOutOfBoundsException e) {
 fail("Expected a checkbox menu item, but got an array out of bounds exception");
 }
 assertEquals(expectedMenuItem.getText(), actualMenuItem.getText());
 }

 @Test
 public void testCreateMenuWithMenuItem() {
 String menuLabel = "Help";
 String menuItemLabel = "About";
 JMenuItem expectedMenuItem = new JMenuItem(menuItemLabel);
 JMenuItem actualMenuItem = null;
 try {
 actualMenuItem = (JMenuItem) mainMenu.createMenu(menuLabel)[0];
 }
 catch (ArrayIndexOutOfBoundsException e) {
 fail("Expected a menu item, but got an array out of bounds exception");
 }
 assertEquals(expectedMenuItem.getText(), actualMenuItem.getText());
 }

 @Test
 public void testAddShortcutAndIcon() {
 String key = "fileOpen";
 String shortcut = "Ctrl+O";
 char expectedMnemonic = 'O';
 String iconPath = "file:icon.png";
 JMenuItem menuItem = new JMenuItem(key);
 mainMenu.addShortcutAndIcon(menuItem, key);
 assertEquals(KeyStroke.getKeyStroke(shortcut), menuItem.getAccelerator());
 assertEquals(expectedMnemonic, menuItem.getMnemonic());
 try {
 URL url = new URL(iconPath);
 InputStream inputStream = url.openStream();
 Image expectedIcon = ImageIO.read(inputStream);
 Image actualIcon = menuItem.getIcon().getImage();
 assertEquals(expectedIcon, actualIcon);
 }
 catch (IOException e) {
 fail("Failed to load expected icon");
 }
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuLabel() {
 String menuLabel = "NonExistentMenu";
 JMenu actualMenu = mainMenu.createMenu(menuLabel);
 fail("Expected a MissingResourceException, but got a valid JMenu instead");
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuItemLabel() {
 String menuLabel = "File";
 String menuItemLabel = "NonExistentMenuItem";
 JMenuItem actualMenuItem = (JMenuItem) mainMenu.createMenu(menuLabel)[0];
 fail("Expected a MissingResourceException, but got a valid JMenuItem instead");
 }

 @Test(expected = NullPointerException.class)
 public void testNullMenuLabel() {
 String menuLabel = null;
 try {
 mainMenu.createMenu(menuLabel);
 fail("Expected a NullPointerException, but got no exception");
 }
 catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test(expected = NullPointerException.class)
 public void testNullMediator() {
 InspirentoMediator nullMediator = null;
 try {
 new MainMenu(nullMediator);
 fail("Expected a NullPointerException, but got no exception");
 }
 catch (NullPointerException e) {
 // Expected exception
 }
}

 @Test
 public void testChanged() {
 // Add a stub implementation for the changed() method
 mainMenu = mock(MainMenu.class);
 when(mainMenu.changed()).thenReturn(true);
 assertFalse(mainMenu.changed());
 }

 @After
 public void tearDown() {
 mainMenu = null;
 mediator = null;
 }

}