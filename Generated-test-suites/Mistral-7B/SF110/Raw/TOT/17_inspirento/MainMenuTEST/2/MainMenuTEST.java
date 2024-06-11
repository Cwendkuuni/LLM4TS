package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Image;
import java.awt.KeyEvent;
import java.awt.event.InputEvent;
import java.io.File;
import java.net.URL;
import java.util.MissingResourceException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.KeyStroke;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.util.UIResources;

public class MainMenuTest {

 private InspirentoMediator mediatorMock;
 private MainMenu mainMenu;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 mediatorMock = mock(InspirentoMediator.class);
 mainMenu = new MainMenu(mediatorMock);
 }

 @Test
 public void testMainMenuCreation() {
 // Test that MainMenu is initialized correctly
 assertEquals("Unexpected number of menus created", 5, mainMenu.getMenuCount());
 }

 @Test
 public void testCreateMenu() {
 String menuName = "File";
 JMenu menu = mainMenu.createMenu(menuName);
 // Test that menu is created with the correct label
 assertEquals("Incorrect menu label", menuName, menu.getText());
 // Test that menu separators are created when necessary
 assertTrue("Menu doesn't have separator", menu.getComponentCount() > 1);
 }

 @Test
 public void testAddShortcutAndIcon() {
 JMenuItem menuItem = mock(JMenuItem.class);
 String menuItemKey = "File.Open";
 String menuItemLabel = "Open";
 String menuItemShortcut = "Ctrl+O";
 String menuItemIcon = "fileopen.png";
 mainMenu.addShortcutAndIcon(menuItem, menuItemKey);

 // Test that menu item shortcut is set correctly
 assertNotNull("MenuItem shortcut not set", menuItem.getAccelerator());
 assertEquals("Unexpected shortcut key", KeyStroke.getKeyStroke(menuItemShortcut), menuItem.getAccelerator());

 // Test that menu item icon is set correctly
 assertNull("MenuItem icon set incorrectly", menuItem.getIcon());
 try {
 URL iconUrl = new URL(new File(".").toURI().toURL(), menuItemIcon);
 Image icon = ImageIO.read(iconUrl);
 menuItem.setIcon(new ImageIcon(icon));
 verify(menuItem).setIcon(new ImageIcon(icon));
 } catch (IOException e) {
 fail("Failed to read icon file: " + e.getMessage());
 }
 }

 @Test
 public void testMissingResourceException() {
 try {
 mainMenu = new MainMenu(null);
 fail("Expected MissingResourceException to be thrown");
 } catch (MissingResourceException e) {
 // Expected
 }
 }

 @After
 public void teardown() {
 // Reset mocks to avoid interference between tests
 reset(mediatorMock);
 }
}