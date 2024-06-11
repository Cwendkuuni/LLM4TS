package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.*;
import java.util.*;

import org.junit.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;

public class MainMenuTest {

 private InspirentoMediator mediatorMock;
 private MainMenu mainMenu;

 @Before
 public void setUp() {
 mediatorMock = mock(InspirentoMediator.class);
 mainMenu = new MainMenu(mediatorMock);
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testCreateMenu_ValidMenuData_CreateMenuWithLabelAndShortcut() {
 String menuName = "File";
 String label = "Open";
 String shortcut = "Ctrl+O";

 String[] menuItems = {"File", "#Open>", "-", "#Save"};

 when(UIResources.getString(menuName + LABEL_SUFFIX)).thenReturn(menuName);
 when(UIResources.getString(menuName + "Label")).thenReturn(label);
 when(UIResources.getString(menuName + SHORTCUT_SUFFIX)).thenReturn(shortcut);

 JMenu menu = mainMenu.createMenu(menuName);

 assertEquals(menuName, menu.getText());
 assertEquals(label, menu.getComponent(0).getText());
 JMenuItem openItem = (JMenuItem) menu.getComponent(1);
 assertEquals(shortcut, openItem.getAcceleratorKeyStroke().getKeyCode());
 }

 @Test
 public void testCreateMenu_EmptyMenuData_CreateEmptyMenu() {
 String menuName = "Edit";
 String label = "Cut";
 String shortcut = "Ctrl+X";

 String[] menuItems = {"Edit", "#Cut>", "-"};

 when(UIResources.getString(menuName + LABEL_SUFFIX)).thenReturn(menuName);
 when(UIResources.getString(menuName + "Label")).thenReturn(label);
 when(UIResources.getString(menuName + SHORTCUT_SUFFIX)).thenReturn(shortcut);

 JMenu menu = mainMenu.createMenu(menuName);

 assertEquals(menuName, menu.getText());
 assertEquals(0, menu.getComponentCount());
 }

 @Test(expected = MissingResourceException.class)
 public void testCreateMenu_MissingMenuLabel_ThrowException() {
 String menuName = "MissingLabel";
 String label = "Open";
 String shortcut = "Ctrl+O";

 String[] menuItems = {"MissingLabel", "#Open>", "-", "#Save"};

 when(UIResources.getString(menuName + LABEL_SUFFIX)).thenThrow(new MissingResourceException("Missing label", null, "MissingLabelLabel"));

 JMenu menu = mainMenu.createMenu(menuName);
 fail("Expected MissingResourceException to be thrown");
 }

 @Test(expected = MissingResourceException.class)
 public void testCreateMenu_MissingMenuShortcut_ThrowException() {
 String menuName = "MissingShortcut";
 String label = "Open";
 String shortcut = "";

 String[] menuItems = {"MissingShortcut", "#Open>", "-", "#Save"};

 when(UIResources.getString(menuName + LABEL_SUFFIX)).thenReturn(label);
 when(UIResources.getString(menuName + SHORTCUT_SUFFIX)).thenThrow(new MissingResourceException("Missing shortcut", null, "MissingShortcutShortcut"));

 JMenu menu = mainMenu.createMenu(menuName);
 fail("Expected MissingResourceException to be thrown");
 }

 @Test
 public void testAddShortcutAndIcon_ValidData_SetAcceleratorAndIcon() {
 String key = "Open";
 String label = "Open";
 String iconPath = "icons/open.png";

 when(UIResources.getString(key + SHORTCUT_SUFFIX)).thenReturn("Ctrl+O");
 when(UIResources.getString(key + "Label")).thenReturn(label);
 when(UIResources.getString(key + "Icon")).thenReturn(iconPath);

 JMenuItem item = new JMenuItem();
 mainMenu.addShortcutAndIcon(item, key);

 assertEquals("Ctrl+O", item.getAcceleratorKeyStroke().getKeyCode());
 assertTrue(item.getIcon() instanceof ImageIcon);
 File iconFile = new File(iconPath);
 assertTrue(iconFile.exists());
 }

 @Test
 public void testAddShortcutAndIcon_MissingIconData_SetNullIcon() {
 String key = "Save";
 String label = "Save";
 String iconPath = "";

 when(UIResources.getString(key + SHORTCUT_SUFFIX)).thenReturn("Ctrl+S");
 when(UIResources.getString(key + "Label")).thenReturn(label);
 when(UIResources.getString(key + "Icon")).thenReturn("");

 JMenuItem item = new JMenuItem();
 mainMenu.addShortcutAndIcon(item, key);

 assertNull(item.getIcon());
 }
}