package com.allenstudio.ir.ui;

import static org.junit.Assert.*;

import com.allenstudio.ir.core.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class MainMenuTest {

 private InspirentoMediator mediatorMock;
 private MainMenu mainMenu;

 @Before
 public void setUp() {
 mediatorMock = Mockito.mock(InspirentoMediator.class);
 mainMenu = new MainMenu(mediatorMock);
 }

 @Test
 public void testMainMenuConstruction() {
 // Check if the MainMenu object is initialized correctly
 assertNotNull(mainMenu);
 // Check if the mediator is properly set
 assertEquals(mediatorMock, mainMenu.mediator);
 }

 @Test
 public void testCreateMenu() {
 // Given
 String menu = "File";
 JMenu expectedMenu = new JMenu("File");
 // When
 JMenu actualMenu = mainMenu.createMenu(menu);
 // Then
 assertEquals(expectedMenu, actualMenu);
 }

 @Test
 public void testCreateSubMenu() {
 // Given
 String menu = "File";
 String subMenu = "Open";
 JMenu expectedSubMenu = new JMenu("Open");
 // When
 JMenu actualMenu = mainMenu.createMenu(menu);
 JMenu actualSubMenu = (JMenu) actualMenu.add(mainMenu.createMenu(subMenu));
 // Then
 assertEquals(expectedSubMenu, actualSubMenu);
 }

 @Test
 public void testCreateCheckBoxMenuItem() {
 // Given
 String key = "File.Open";
 JCheckBoxMenuItem expectedMenuItem = new JCheckBoxMenuItem("Open");
 // When
 JCheckBoxMenuItem actualMenuItem = mainMenu.createMenuItemWithShortcutAndIcon(key, "Open", false);
 // Then
 assertEquals(expectedMenuItem, actualMenuItem);
 }

 @Test
 public void testCreateMenuItem() {
 // Given
 String key = "File.Save";
 JMenuItem expectedMenuItem = new JMenuItem("Save");
 // When
 JMenuItem actualMenuItem = mainMenu.createMenuItemWithShortcutAndIcon(key, "Save", false);
 // Then
 assertEquals(expectedMenuItem, actualMenuItem);
 }

 @Test
 public void testAddShortcutAndIcon() {
 // Given
 String key = "File.Open";
 JMenuItem menuItem = Mockito.mock(JMenuItem.class);
 // When
 mainMenu.addShortcutAndIcon(menuItem, key);
 // Then
 Mockito.verify(menuItem).setAccelerator(Mockito.any(KeyStroke.class));
 Mockito.verify(menuItem).setIcon(Mockito.any(ImageIcon.class));
 }

 @Test
 public void testGetResource() {
 // Given
 String key = "File.Open.Shortcut";
 String expectedValue = "Ctrl+O";
 // When
 String actualValue = UIResources.getString(key);
 // Then
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testGetResourceWithMissingKey() {
 // Given
 String key = "MissingKey";
 // When
 // Then
 assertNull(UIResources.getString(key));
 }

 @Test
 public void testGetResourceWithMissingFile() {
 // Given
 String key = "File.OpenIcon";
 // When
 // Then
 assertNull(mainMenu.getIcon(key));
 }

 @Test
 public void testGetIcon() {
 // Given
 String key = "File.OpenIcon";
 Image expectedIcon = Toolkit.getDefaultToolkit().getImage(new File("icon.png").getAbsolutePath());
 // When
 Image actualIcon = mainMenu.getIcon(key);
 // Then
 assertEquals(expectedIcon, actualIcon);
 }
}