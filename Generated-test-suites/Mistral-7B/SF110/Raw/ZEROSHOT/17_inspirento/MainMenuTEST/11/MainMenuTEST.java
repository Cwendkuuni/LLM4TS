package com.allenstudio.ir.ui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.Arrays;

public class MainMenuTest {

 private MainMenu mainMenu;
 private InspirentoMediator mediator; // Assuming InspirentoMediator is mocked or a test spy

 @Before
 public void setUp() {
 mediator = mock(InspirentoMediator.class);
 mainMenu = new MainMenu(mediator);
 }

 @Test
 public void testMainMenuCreation() {
 JMenuBar jMenuBar = new JMenuBar();
 jMenuBar.add(mainMenu);
 assertNotNull(mainMenu);
 assertInstanceOf(JMenuBar.class, jMenuBar);
 }

 @Test
 public void testCreateMenu() {
 String menu = "File";
 JMenu expectedMenu = new JMenu(menu);
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertEquals(expectedMenu.getText(), actualMenu.getText());
 assertSame(expectedMenu, actualMenu);
 }

 @Test
 public void testCreateCheckBoxMenuItem() {
 String menu = "Help";
 String item = "About";
 JCheckBoxMenuItem expectedMenuItem = new JCheckBoxMenuItem("About");
 String key = "Help.About";
 JCheckBoxMenuItem actualMenuItem = mainMenu.createMenu(menu).findJCheckBoxMenuItem(key);
 assertEquals(expectedMenuItem.getText(), actualMenuItem.getText());
 assertEquals(expectedMenuItem.isArmed(), actualMenuItem.isArmed());
 assertEquals(expectedMenuItem.isSelected(), actualMenuItem.isSelected());
 }

 @Test
 public void testCreateJMenuItem() {
 String menu = "File";
 String item = "Open";
 JMenuItem expectedMenuItem = new JMenuItem("Open");
 String key = "File.Open";
 JMenuItem actualMenuItem = mainMenu.createMenu("File").findJMenuItem(key);
 assertEquals(expectedMenuItem.getText(), actualMenuItem.getText());
 assertSame(expectedMenuItem, actualMenuItem);
 }

 @Test
 public void testAddShortcutAndIcon() {
 String menu = "File";
 String item = "Open";
 String shortcut = "Ctrl+O";
 String icon = "icon.png";
 String key = "File.Open";

 JMenuItem jMenuItem = mainMenu.createMenu(menu).findJMenuItem(key);
 mainMenu.addShortcutAndIcon(jMenuItem, key);

 assertEquals(KeyEvent.VK_O, jMenuItem.getAccelerator().getKeyCode());
 assertEquals(KeyEvent.CTRL_MASK, jMenuItem.getAccelerator().getModifiers());
 assertNull(jMenuItem.getIcon());

 File iconFile = new File(getClass().getResource(icon).getFile());
 assertTrue(iconFile.exists());
 URL url = this.getClass().getResource(icon);
 assertEquals(new ImageIcon(url), jMenuItem.getIcon());
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingLabelResource() {
 String menu = "MissingLabel";
 mainMenu.createMenu(menu);
 fail("MissingResourceException expected but not thrown");
 }

 @Test(expected = NullPointerException.class)
 public void testNullMediator() {
 MainMenu menu = new MainMenu(null);
 fail("NullPointerException expected but not thrown");
 }
}