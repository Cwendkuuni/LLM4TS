package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import java.awt.Image;
import java.awt.KeyEvent;
import java.awt.event.InputEvent;
import java.io.File;
import java.net.URL;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.ui.MainMenu;
import com.allenstudio.ir.util.UIResources;

public class MainMenuTest {

 private MainMenu menu;
 private InspirentoMediator mediator;

 @Before
 public void setUp() {
 mediator = new InspirentoMediator();
 menu = new MainMenu(mediator);
 }

 // Test case for constructor
 @Test
 public void testMainMenuConstruction() {
 assertNotNull(menu);
 }

 // Test case for createMenu method with valid menu and items
 @Test
 public void testCreateMenuValidMenuAndItems() {
 String[] menus = InspirentoUtilities.tokenize(UIResources.getString("menu"));
 assertEquals(menus.length, menu.getComponentCount());
 for (int i = 0; i < menu.getComponentCount(); i++) {
 JComponent component = (JComponent) menu.getComponent(i);
 if (component instanceof JMenu) {
 String menuName = UIResources.getString(menus[i] + "Label");
 String expectedMenuName = menuName.substring(0, menuName.length() - 2);
 assertEquals(expectedMenuName, component.getName());
 testCreateSubMenuOrMenuItem((JMenu) component);
 }
 }
 }

 // Recursive test case for createMenu method with sub menus and items
 private void testCreateSubMenuOrMenuItem(JMenu menu) {
 String label = menu.getName();
 String[] items = InspirentoUtilities.tokenize(UIResources.getString(label));
 for (int i = 0; i < menu.getComponentCount(); i++) {
 JComponent component = (JComponent) menu.getComponent(i);
 if (component instanceof JMenu) {
 testCreateSubMenuOrMenuItem((JMenu) component);
 } else if (component instanceof JMenuItem) {
 String itemName = UIResources.getString(items[i] + "Label");
 String expectedItemName = itemName.substring(0, itemName.length() - 2);
 assertEquals(expectedItemName, component.getName());
 }
 }
 }

 // Test case for addShortcutAndIcon method with valid key and icon
 @Test
 public void testAddShortcutAndIconValidKeyIcon() {
 String key = "TestKey";
 String iconPath = "TestIcon.png";
 String label = "TestLabel";
 JMenuItem jMenuItem = new JMenuItem(label);
 menu.addShortcutAndIcon(jMenuItem, key);
 assertEquals("TestKey", jMenuItem.getActionCommand());
 assertEquals(KeyEvent.VK_T, jMenuItem.getAccelerator().getKeyCode());
 try {
 URL url = new URL(getClass().getResource(iconPath));
 Image expectedIcon = ImageIO.read(new File(url.getFile()));
 Image actualIcon = ((JCheckBoxMenuItem) jMenuItem).getIcon();
 assertEquals(expectedIcon, actualIcon);
 } catch (IOException e) {
 fail("Failed to load icon for test");
 }
 }

 // Test case for addShortcutAndIcon method with missing key
 @Test(expected = MissingResourceException.class)
 public void testAddShortcutAndIconMissingKey() {
 String key = "MissingKey";
 JMenuItem jMenuItem = new JMenuItem("TestLabel");
 menu.addShortcutAndIcon(jMenuItem, key);
 }

 // Test case for addShortcutAndIcon method with missing icon
 @Test
 public void testAddShortcutAndIconMissingIcon() {
 String key = "TestKey";
 String label = "TestLabel";
 JMenuItem jMenuItem = new JMenuItem(label);
 menu.addShortcutAndIcon(jMenuItem, key);
 assertEquals(KeyEvent.VK_T, jMenuItem.getAccelerator().getKeyCode());
 assertNull(jMenuItem.getIcon());
 }
}