package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.ui.*;
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
 public void testMainMenuConstruction() {
 MainMenu menu = new MainMenu(mediator);
 assertNotNull(menu);
 }

 @Test
 public void testCreateMenu() {
 String menu = "File";
 JMenu expectedMenu = new JMenu(menu);
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertEquals(expectedMenu, actualMenu);
 }

 @Test
 public void testCreateMenuWithLabel() {
 String menu = "File*New";
 JMenu expectedMenu = new JMenu("File");
 expectedMenu.setMnemonic('N');
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertEquals(expectedMenu, actualMenu);
 }

 @Test
 public void testCreateMenuWithSeparator() {
 String menu = "File*-";
 JMenu expectedMenu = new JMenu("");
 expectedMenu.setSeparator(true);
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertEquals(expectedMenu, actualMenu);
 }

 @Test
 public void testCreateMenuItem() {
 String menu = "File";
 String item = "New";
 JMenuItem expectedMenuItem = new JMenuItem(item);
 JMenuItem actualMenuItem = mainMenu.createMenu(menu).findJMenuItem(item);
 assertEquals(expectedMenuItem, actualMenuItem);
 }

 @Test
 public void testCreateMenuItemWithLabelAndShortcut() {
 String menu = "File";
 String item = "New*Ctrl N";
 JMenuItem expectedMenuItem = new JMenuItem(item);
 expectedMenuItem.setAccelerator(KeyStroke.getKeyStroke('N', KeyEvent.CTRL_DOWN_MASK));
 JMenuItem actualMenuItem = mainMenu.createMenu(menu).findJMenuItem(item);
 assertEquals(expectedMenuItem, actualMenuItem);
 }

 @Test
 public void testCreateMenuItemWithLabelAndIcon() {
 String menu = "File";
 String item = "New#icons/new.png";
 JMenuItem expectedMenuItem = new JMenuItem(item);
 expectedMenuItem.setIcon(new ImageIcon(getClass().getResource(item.substring(item.lastIndexOf("#") + 1))));
 JMenuItem actualMenuItem = mainMenu.createMenu(menu).findJMenuItem(item);
 assertEquals(expectedMenuItem, actualMenuItem);
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuLabel() {
 String menu = "MissingLabel";
 mainMenu.createMenu(menu);
 fail("Expected MissingResourceException");
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingSubMenuLabel() {
 String menu = "File*MissingSubMenu";
 JMenu menuObject = mainMenu.createMenu(menu);
 fail("Expected MissingResourceException");
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuItemLabel() {
 String menu = "File";
 String item = "MissingItem";
 mainMenu.createMenu(menu).add(new JMenuItem(item));
 fail("Expected MissingResourceException");
 }

 @Test(expected = NullPointerException.class)
 public void testNullMediator() {
 MainMenu menu = new MainMenu(null);
 fail("Expected NullPointerException");
 }

 @Test
 public void testChanged() {
 mainMenu.changed();
 // Not testing for anything specific as the method is empty
 }

}