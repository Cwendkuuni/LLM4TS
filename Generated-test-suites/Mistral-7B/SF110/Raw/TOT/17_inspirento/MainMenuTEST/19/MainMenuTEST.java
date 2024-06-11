package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import org.junit.*;
import org.junit.runner.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

@RunWith(MockitoJUnitRunner.class)
public class MainMenuTest {

 private MainMenu mainMenu;
 private InspirentoMediator mediator;

 @Before
 public void setUp() {
 mediator = mock(InspirentoMediator.class);
 mainMenu = new MainMenu(mediator);
 }

 @Test
 public void testCreateMenu() {
 String[] menus = InspirentoUtilities.tokenize(UIResources.getString("menu"));
 for (String menu : menus) {
 JMenu jMenu = mainMenu.createMenu(menu);
 assertNotNull(jMenu);
 }
 }

 @Test
 public void testCreateMenuWithLabelAndShortcut() {
 String menuLabel = "File";
 String shortcut = "F";
 String key = menuLabel + SHORTCUT_SUFFIX;
 JMenuItem item = mainMenu.createMenu("File")
 .getComponent(0);
 assertEquals(menuLabel, item.getText());
 assertEquals(shortcut, item.getAccelerator().toString());
 }

 @Test
 public void testCreateMenuWithLabelAndIcon() {
 String menuLabel = "Help";
 String iconPath = "help.png";
 String key = menuLabel + LABEL_SUFFIX;
 JMenu jMenu = mainMenu.createMenu(menuLabel);
 JMenuItem item = jMenu.getComponent(0);
 assertEquals(menuLabel, item.getText());
 assertNull(item.getIcon());

 String iconKey = key + ICON_SUFFIX;
 UIManager.put(iconKey, new ImageIcon(getClass().getResource(iconPath)));
 mainMenu = new MainMenu(mediator);
 jMenu = mainMenu.createMenu(menuLabel);
 item = jMenu.getComponent(0);
 assertNotNull(item.getIcon());
 }

 @Test
 public void testCreateMenuWithSeparator() {
 String menuLabel = "Separator";
 JMenu jMenu = mainMenu.createMenu(menuLabel);
 assertTrue(jMenu.getComponent(0) instanceof JSeparator);
 }

 @Test
 public void testCreateMenuWithNestedMenu() {
 String menuLabel = "Edit";
 String nestedMenuLabel = "Copy";
 JMenu parentMenu = mainMenu.createMenu(menuLabel);
 JMenu nestedMenu = mainMenu.createMenu(nestedMenuLabel);
 parentMenu.add(nestedMenu);
 JMenuItem copyItem = nestedMenu.getComponent(0);
 parentMenu.add(copyItem);
 assertEquals(nestedMenuLabel, copyItem.getText());
 }

 @Test
 public void testCreateMenuWithMissingResource() {
 try {
 mainMenu = new MainMenu(mediator);
 fail("Should have thrown MissingResourceException");
 }
 catch (MissingResourceException e) {
 // expected
 }
 }

 @Test
 public void testAddShortcutAndIcon() {
 String menuLabel = "Preferences";
 String shortcut = "P";
 String iconPath = "preferences.png";
 String key = menuLabel + SHORTCUT_SUFFIX;
 JMenuItem item = mainMenu.createMenu(menuLabel)
 .getComponent(0);
 assertEquals(menuLabel, item.getText());
 assertNull(item.getAccelerator());
 assertNull(item.getIcon());

 mainMenu.addShortcutAndIcon(item, key);
 assertEquals(shortcut, item.getAccelerator().toString());
 UIManager.put(key + ICON_SUFFIX, new ImageIcon(getClass().getResource(iconPath)));
 mainMenu = new MainMenu(mediator);
 item = mainMenu.createMenu(menuLabel)
 .getComponent(0);
 assertNotNull(item.getIcon());
 }

 @Test
 public void testAddShortcutAndIconWithMissingResource() {
 String menuLabel = "Preferences";
 String shortcut = "P";
 String iconPath = "preferences.png";
 String key = menuLabel + SHORTCUT_SUFFIX;
 JMenuItem item = mainMenu.createMenu(menuLabel)
 .getComponent(0);
 assertEquals(menuLabel, item.getText());
 assertNull(item.getAccelerator());
 assertNull(item.getIcon());
mainMenu.addShortcutAndIcon(item, key);
 UIManager.put(key + ICON_SUFFIX, new ImageIcon(getClass().getResource(iconPath)));
 mainMenu = new MainMenu(mediator);
 item = mainMenu.createMenu(menuLabel)
 .getComponent(0);
 assertNotNull(item.getIcon());
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingResourceForLabel() {
 mainMenu = new MainMenu(mediator);
 mainMenu.createMenu("InvalidMenuLabel");
 fail("Should have thrown MissingResourceException");
 }

 @Test(expected = NullPointerException.class)
 public void testNullMediator() {
 mainMenu = new MainMenu(null);
 fail("Should have thrown NullPointerException");
 }
}