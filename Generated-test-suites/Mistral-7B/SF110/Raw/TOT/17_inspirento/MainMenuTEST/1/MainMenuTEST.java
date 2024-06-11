package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.KeyStroke;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.util.UIResources;

public class MainMenuTest {

 private InspirentoMediator mediatorMock;
 private MainMenu mainMenu;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 mediatorMock = mock(InspirentoMediator.class);
 mainMenu = new MainMenu(mediatorMock);
 }

 @Test
 public void testCreateMenuWithValidMenuAndItems() {
 String menuString = "File*Open>Open...\nHelp*About";
 JMenu expectedMenu = new JMenu("File");
 JMenuItem openItem = new JMenuItem("Open...");
 JMenu helpMenu = new JMenu("Help");
 JMenuItem aboutItem = new JMenuItem("About");

 when(UIResources.getString("menu")).thenReturn(menuString);
 when(UIResources.getString("FileLabel")).thenReturn("File");
 when(UIResources.getString("OpenLabel")).thenReturn("Open...");
 when(UIResources.getString("HelpLabel")).thenReturn("Help");
 when(UIResources.getString("AboutLabel")).thenReturn("About");

 MainMenu createdMenu = mainMenu.createMenu("File");

 assertEquals(expectedMenu, createdMenu);
 assertEquals(1, createdMenu.getMenuComponentCount());
 JMenuItem firstComponent = (JMenuItem) createdMenu.getMenuComponent(0);
 assertEquals(openItem, firstComponent);
 assertEquals(expectedMenu, firstComponent.getParent());
 assertEquals(expectedMenu, createdMenu.getComponentAt(0).getParent());

 JMenuItem helpSubMenu = (JMenuItem) createdMenu.getComponent(1);
 assertEquals(helpMenu, helpSubMenu.getSubMenu());
 assertEquals(1, helpSubMenu.getMenuComponentCount());
 assertEquals(aboutItem, helpSubMenu.getMenuComponent(0));
 assertEquals(helpMenu, aboutItem.getParent());
 assertEquals(createdMenu, aboutItem.getParent());
 }

 @Test
 public void testCreateMenuWithEmptyMenu() {
 String menuString = "EmptyMenu";
 JMenu expectedMenu = new JMenu("EmptyMenu");

 when(UIResources.getString("menu")).thenReturn(menuString);
 when(UIResources.getString("EmptyMenuLabel")).thenReturn("EmptyMenu");

 MainMenu createdMenu = mainMenu.createMenu("EmptyMenu");

 assertEquals(expectedMenu, createdMenu);
 assertTrue(createdMenu.getMenuComponentCount() == 0);
 }

 @Test(expected = MissingResourceException.class)
 public void testCreateMenuWithMissingMenuLabel() {
 String menuString = "File*Open>Open...\nHelp*About*MissingLabel";

 when(UIResources.getString("menu")).thenReturn(menuString);

 MainMenu createdMenu = mainMenu.createMenu("File");
 fail("Should throw MissingResourceException for missing label");
 }

 @Test
 public void testAddShortcutAndIconWithValidShortcutAndIcon() {
 String key = "Open";
 String iconPath = "iconPath/openIcon.png";
 JMenuItem openItem = new JMenuItem("Open...");

 when(UIResources.getString(key + "Shortcut")).thenReturn("Ctrl+O");
 when(UIResources.getString(key + "Icon")).thenReturn(iconPath);

 mainMenu.addShortcutAndIcon(openItem, key);

 assertEquals(KeyStroke.getKeyStroke("Ctrl+O"), openItem.getAccelerator());
 assertTrue(openItem.getIcon() instanceof ImageIcon);
 URL iconUrl = new URL(iconPath);
 verify(UIResources, times(1)).getString(key + "Icon");
 verifyZeroInteractions(UIResources).getString(eq(UIResources.class), anyString());
 }

 @Test
 public void testAddShortcutAndIconWithNullShortcut() {
 String key = "Open";
 JMenuItem openItem = new JMenuItem("Open...");

 when(UIResources.getString(key + "Shortcut")).thenReturn(null);

 mainMenu.addShortcutAndIcon(openItem, key);

 assertNull(openItem.getAccelerator());
 }

 @Test
 public void testAddShortcutAndIconWithMissingIcon() {
 String key = "Open";
 JMenuItem openItem = new JMenuItem("Open...");
 String iconPath = "missingIcon.png";

 when(UIResources.getString(key + "Icon")).thenThrow(new MissingResourceException("Icon not found", null, key + "Icon"));

 mainMenu.addShortcutAndIcon(openItem, key);

 assertNull(openItem.getIcon());
 }

}