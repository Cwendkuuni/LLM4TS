package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.ui.MainMenu;
import com.allenstudio.ir.ui.MainMenuTest;
import com.allenstudio.ir.util.UIResources;
import com.allenstudio.ir.util.UIResourcesException;

public class MainMenuTest {

 private MainMenu menu;
 private InspirentoMediator mediator;

 @Before
 public void setup() {
 mediator = new InspirentoMediator();
 menu = new MainMenu(mediator);
 }

 @Test
 public void testMainMenuConstruction() {
 // Test the construction of the MainMenu object
 assertNotNull(menu);
 }

 @Test
 public void testCreateMenuNullMenuName() {
 try {
 // Test creating a menu with a null name
 menu.createMenu(null);
 fail("Expected MissingResourceException");
 } catch (MissingResourceException e) {
 // Expected result
 }
 }

 @Test
 public void testCreateMenuEmptyMenuName() {
 try {
 // Test creating a menu with an empty name
 menu.createMenu("");
 fail("Expected MissingResourceException");
 } catch (MissingResourceException e) {
 // Expected result
 }
 }

 @Test
 public void testCreateMenuWithMenuItems() {
 String[] arr = {"Menu1", "Menu2>SubMenu1", "#MenuItem1Label", "MenuItem2Label", "-"};
 JMenu menu = menu.createMenu("Menu");

 for (String item : arr) {
 if (!item.equals("-")) {
 JMenuItem jMenuItem = null;
 if (item.charAt(0) == '#') {
 jMenuItem = (JCheckBoxMenuItem) menu.getComponent(menu.getComponentCount() - 1);
 } else {
 jMenuItem = (JMenuItem) menu.getComponent(menu.getComponentCount() - 1);
 }
 assertEquals(UIResources.getString(item + "Label"), jMenuItem.getText());
 if (item.charAt(0) == '#') {
 assertNotNull(jMenuItem.getIcon());
 } else {
 // Add a shortcut key test here if needed
 }
 menu.getComponent(menu.getComponentCount() - 2).doClick(); // Click on the separator to test the functionality
 }
 }
 }

 @Test
 public void testAddShortcutAndIconNullKey() {
 JMenuItem item = new JMenuItem("TestItem");
 menu.addShortcutAndIcon(item, null);
 assertEquals(0, item.getAccelerator().getKeyCode(), 0); // The accelerator should not be set
 assertNull(item.getIcon());
 }

 @Test
 public void testAddShortcutAndIconMissingShortcutKey() {
 JMenuItem item = new JMenuItem("TestItem");
 menu.addShortcutAndIcon(item, "MissingShortcutKey");
 assertEquals(0, item.getAccelerator().getKeyCode(), 0); // The accelerator should not be set
 assertNull(item.getIcon());
 }

 @Test
 public void testAddShortcutAndIconMissingIconKey() {
 JMenuItem item = new JMenuItem("TestItem");
 menu.addShortcutAndIcon(item, "IconKey");
 assertNotNull(item.getAccelerator());
 assertNull(item.getIcon());
 }

 @Test
 public void testAddShortcutAndIconWithValidShortcutAndIcon() {
 JMenuItem item = new JMenuItem("TestItem");
 menu.addShortcutAndIcon(item, "ShortcutKey>IconKey");
 assertNotNull(item.getAccelerator());
 assertNotNull(item.getIcon());
 }

 @Test
 public void testGetResourceFromUIResourcesWithMissingResource() {
 try {
 UIResources.getString("MissingResourceKey");
 fail("Expected MissingResourceException");
 } catch (MissingResourceException e) {
 // Expected result
 }
 }

 @Test
 public void testGetResourceFromUIResourcesWithValidResource() {
 String resource = UIResources.getString("ValidResourceKey");
 assertNotNull(resource);
 }

 @Test
 public void testGetterSetterForMediator() {
 InspirentoMediator oldMediator = menu.getMediator();
 menu.setMediator(new InspirentoMediator());
 assertNotEquals(oldMediator, menu.getMediator());
 }
}