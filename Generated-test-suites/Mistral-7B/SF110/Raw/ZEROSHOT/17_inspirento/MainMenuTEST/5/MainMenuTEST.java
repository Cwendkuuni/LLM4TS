package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.*;

public class MainMenuTest {

 private MainMenu mainMenu;
 private InspirentoMediator mediatorMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 mediatorMock = mock(InspirentoMediator.class);
 mainMenu = new MainMenu(mediatorMock);
 }

 @Test
 public void testMainMenuInitialization() {
 // Test that the MainMenu is initialized with the correct number of menus
 assertEquals(3, mainMenu.getMenuCount());

 // Test that each menu has the correct name
 String[] expectedMenuNames = {"File", "Edit", "Help"};
 JMenu[] menus = mainMenu.getMenus();
 for (int i = 0; i < menus.length; i++) {
 assertEquals(expectedMenuNames[i], menus[i].getText());
 }
 }

 @Test
 public void testCreateMenu() {
 String menuName = "TestMenu";
 String submenuName = "Submenu";
 String label = "Test Label";
 String shortcut = "T";
 String iconPath = "test.png";

 // Test that createMenu creates a JMenu with the correct name and mnemonic
 JMenu menu = mainMenu.createMenu(menuName);
 assertEquals(menuName, menu.getText());
 assertTrue(menu.isMenuComponent(mainMenu.createMenu(submenuName)));

 // Test that createMenu creates a JMenuItem with the correct label, mnemonic, shortcut, and icon
 JMenuItem item = mainMenu.createMenu(menuName + ">" + label);
 assertEquals(label, item.getText());
 assertEquals(menuName.charAt(menuName.length() - 1), item.getMnemonic());
 assertEquals(ShortcutKeyStroke.getKeyStroke(shortcut), item.getAccelerator());
 assertNull(item.getIcon());

 // Test that createMenu sets the icon for a JCheckBoxMenuItem
 JCheckBoxMenuItem checkboxItem = (JCheckBoxMenuItem) mainMenu.createMenu(menuName + ">" + label + ">Checkbox");
 assertEquals(label, checkboxItem.getText());
 assertFalse(checkboxItem.isSelected());
 assertEquals(ShortcutKeyStroke.getKeyStroke(shortcut), checkboxItem.getAccelerator());
 assertNull(checkboxItem.getIcon());

 // Test that createMenu sets the icon for a JMenuItem
 JMenuItem imageItem = mainMenu.createMenu(menuName + ">" + label + ">Image");
 assertEquals(label, imageItem.getText());
 assertFalse(imageItem.isEnabled());
 assertNull(imageItem.getAccelerator());
 File iconFile = new File(iconPath);
 assertTrue(iconFile.exists());
 URL url = new File(iconPath).toURI().toURL();
 assertEquals(new ImageIcon(url), imageItem.getIcon());
 }

 @Test
 public void testAddShortcutAndIcon() {
 JMenuItem item = new JMenuItem("TestItem");
 mainMenu.addShortcutAndIcon(item, "TestKey");

 // Test that addShortcutAndIcon sets the accelerator for a JMenuItem
 assertEquals(ShortcutKeyStroke.getKeyStroke("TestKey"), item.getAccelerator());

 JCheckBoxMenuItem checkboxItem = new JCheckBoxMenuItem("TestCheckbox");
 mainMenu.addShortcutAndIcon(checkboxItem, "TestKey");

 // Test that addShortcutAndIcon sets the accelerator for a JCheckBoxMenuItem
 assertEquals(ShortcutKeyStroke.getKeyStroke("TestKey"), checkboxItem.getAccelerator());
 }
}