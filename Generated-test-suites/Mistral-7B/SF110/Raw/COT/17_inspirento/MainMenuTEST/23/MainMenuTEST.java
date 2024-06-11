package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.MissingResourceException;

import org.junit.Before;
import org.junit.Test;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.ui.MainMenu;
import com.allenstudio.ir.util.UIResources;
import com.allenstudio.ir.util.InspirentoUtilities;

public class MainMenuTest {

 private MainMenu mainMenu;
 private InspirentoMediator mediator;

 @Before
 public void setup() {
 mediator = new InspirentoMediator();
 mainMenu = new MainMenu(mediator);
 }

 // Test constructor
 @Test
 public void testMainMenuConstructor() {
 // Check if constructor is called
 assertNotNull(mainMenu);
 }

 // Test createMenu method with valid arguments
 @Test
 public void testCreateMenuValidArguments() {
 String menu = "File";
 JMenu expectedMenu = new JMenu("File");
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertEquals(expectedMenu, actualMenu);
 }

 // Test createMenu method with invalid arguments
 @Test(expected = MissingResourceException.class)
 public void testCreateMenuInvalidArguments() {
 String invalidMenu = "XXX";
 mainMenu.createMenu(invalidMenu);
 }

 // Test addShortcutAndIcon method with valid arguments
 @Test
 public void testAddShortcutAndIconValidArguments() {
 String key = "File.Open";
 JMenuItem item = new JMenuItem("Open");
 mainMenu.addShortcutAndIcon(item, key);
 String expectedShortcut = "Ctrl+O";
 String expectedPath = "file:../icons/open.png";
 assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK), item.getAccelerator());
 assertNotNull(item.getIcon());
 }

 // Test addShortcutAndIcon method with invalid arguments
 @Test
 public void testAddShortcutAndIconInvalidArguments() {
 String key = "File.Open.XXX";
 JMenuItem item = new JMenuItem("Open");
 mainMenu.addShortcutAndIcon(item, key);
 assertNull(item.getAccelerator());
 assertNull(item.getIcon());
 }

 // Test changed method
 @Test
 public void testChanged() {
 mainMenu.changed();
 // Since changed method is empty, no assertions needed
 }

 // Test tokenize method with valid arguments
 @Test
 public void testTokenizeValidArguments() {
 String input = "File|New|Open|Save|Save As|Exit";
 String[] expectedArray = new String[]{"File", "New", "Open", "Save", "Save As", "Exit"};
 String[] actualArray = InspirentoUtilities.tokenize(input);
 assertArrayEquals(expectedArray, actualArray);
 }

 // Test tokenize method with invalid arguments
 @Test(expected = IllegalArgumentException.class)
 public void testTokenizeInvalidArguments() {
 String input = "";
 String[] actualArray = InspirentoUtilities.tokenize(input);
 }

 // Test getString method with valid arguments
 @Test
 public void testGetStringValidArguments() {
 String key = "File.Menu";
 String expectedString = "File";
 String actualString = UIResources.getString(key);
 assertEquals(expectedString, actualString);
 }

 // Test getString method with invalid arguments
 @Test(expected = MissingResourceException.class)
 public void testGetStringInvalidArguments() {
 String key = "File.XXX";
 UIResources.getString(key);
 }

 // Test getResource method with valid arguments
 @Test
 public void testGetResourceValidArguments() {
 String path = "icons/open.png";
 URL expectedURL = new File("../icons/open.png").toURL();
 URL actualURL = UIResources.getResource(path);
 assertEquals(expectedURL, actualURL);
 }

 // Test getResource method with invalid arguments
 @Test
 public void testGetResourceInvalidArguments() {
 String path = "icons/open.XXX.png";
 URL actualURL = UIResources.getResource(path);
 assertNull(actualURL);
 }
}