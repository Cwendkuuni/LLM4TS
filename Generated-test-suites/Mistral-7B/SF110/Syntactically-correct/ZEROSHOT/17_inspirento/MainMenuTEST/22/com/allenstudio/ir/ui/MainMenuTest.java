package com.allenstudio.ir.ui;

import static org.junit.Assert.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.*;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class MainMenuTest {

 private InspirentoMediator mediatorMock;
 private MainMenu mainMenu;

 @Before
 public void setup() {
 mediatorMock = Mockito.mock(InspirentoMediator.class);
 mainMenu = new MainMenu(mediatorMock);
 }

 @Test
 public void testMainMenuConstruction() {
 // Check that MainMenu constructor takes InspirentoMediator as a parameter
 assertEquals(mediatorMock, mainMenu.mediator);
 // Check that menus are created based on UIResources.getString("menu")
 String[] menus = InspirentoUtilities.tokenize(UIResources.getString("menu"));
 int expectedNumberOfMenus = menus.length;
 int actualNumberOfMenus = mainMenu.getMenuCount();
 assertEquals(expectedNumberOfMenus, actualNumberOfMenus);
 }

 @Test
 public void testCreateMenu() {
 String menu = "File";
 JMenu expectedMenu = new JMenu(menu);
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertEquals(expectedMenu, actualMenu);
 }

 @Test
 public void testCreateSubMenu() {
 String menu = "File";
 String subMenu = "Open";
 JMenu expectedSubMenu = new JMenu(subMenu);
 JMenu actualSubMenu = mainMenu.createMenu(menu + ">" + subMenu);
 assertEquals(expectedSubMenu, actualSubMenu);
 }

 @Test
 public void testCreateCheckboxMenuItem() {
 String key = "File.Open";
 JCheckBoxMenuItem expectedMenuItem = new JCheckBoxMenuItem("Open");
 JCheckBoxMenuItem actualMenuItem = mainMenu.createMenuItem(key);
 assertEquals(expectedMenuItem, actualMenuItem);
 }

 @Test
 public void testCreateMenuItem() {
 String key = "File.Exit";
 JMenuItem expectedMenuItem = new JMenuItem("Exit");
 JMenuItem actualMenuItem = mainMenu.createMenuItem(key);
 assertEquals(expectedMenuItem, actualMenuItem);
 }

 @Test
 public void testAddShortcutAndIcon() {
 String key = "File.Open";
 UIManager uiManager = Mockito.mock(UIManager.class);
 Mockito.when(uiManager.getString(eq("File.OpenLabel"))).thenReturn("Open");
 Mockito.when(uiManager.getString(eq("File.OpenShortcut"))).thenReturn("alt F");
 Mockito.when(uiManager.getString(eq("File.OpenIcon"))).thenReturn("icon.png");

 mainMenu = new MainMenu(mediatorMock);

 JMenuItem menuItem = mainMenu.createMenuItem(key);
 assertNull(menuItem.getAccelerator());
 assertNull(menuItem.getIcon());

 mainMenu.addShortcutAndIcon(menuItem, key);
 assertEquals(KeyEvent.VK_ALT, menuItem.getAccelerator().getKeyChar());
 assertEquals("icon.png", menuItem.getIcon().getDescription());
 }

 @Test(expected = MissingResourceException.class)
 public void testCannotLoadCertainMenuLabel() {
 String key = "Invalid.Menu.Label";
 mainMenu = new MainMenu(mediatorMock);
 mainMenu.createMenuItem(key);
 }

 @Test
 public void testChanged() {
 mainMenu.changed();
 // No action to be taken here
 }
}