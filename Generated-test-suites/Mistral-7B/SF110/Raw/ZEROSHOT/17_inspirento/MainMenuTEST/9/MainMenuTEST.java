package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class MainMenuTest {

 @Mock
 private InspirentoMediator mediator;

 private MainMenu mainMenu;

 @Before
 public void setup() {
 mainMenu = new MainMenu(mediator);
 }

 @Test
 public void testMainMenuConstruction() {
 // Test that MainMenu is initialized with the correct mediator
 assertEquals(mediator, mainMenu.getMediator());

 // Test that menus are created correctly
 int numberOfMenus = mainMenu.getComponentCount();
 assertEquals(numberOfMenus, 7); // Assume there are 7 menus based on UIResources.getString("menu")

 // Test that each menu has the correct label and mnemonic
 JMenu firstMenu = (JMenu) mainMenu.getComponent(0);
 assertEquals("File", firstMenu.getText());
 assertEquals(KeyEvent.VK_F, firstMenu.getMnemonic());
 }

 @Test
 public void testCreateMenu() {
 String menuName = "Edit";
 JMenu menu = mainMenu.createMenu(menuName);

 assertEquals(menuName, menu.getText());

 // Test that submenus are created correctly
 String submenuName = "Undo";
 JMenu submenu = menu.add(mainMenu.createMenu(submenuName));
 assertEquals(submenuName, submenu.getText());
 }

 @Test
 public void testAddShortcutAndIcon() {
 String label = "Cut";
 JMenuItem menuItem = mock(JMenuItem.class);

 mainMenu.addShortcutAndIcon(menuItem, label);

 verify(menuItem, times(1)).setAccelerator(any(KeyStroke.class));
 verify(menuItem, atLeastOnce()).setIcon(any(ImageIcon.class));
 }

 @Test
 public void testMenuItemActionPerformed() {
 JMenuItem menuItem = mock(JMenuItem.class);

 mainMenu.addShortcutAndIcon(menuItem, "Cut");

 doCallRealMethod().when(menuItem).actionPerformed(any(ActionEvent.class));

 mainMenu.fireEvent(new InspirentoEvent(this, InspirentoEvent.TYPE_MENU_SELECTED, menuItem.getText()));

 verify(menuItem, times(1)).actionPerformed(any(ActionEvent.class));
 }

 @Test
 public void testMenuItemCheckboxActionPerformed() {
 JCheckBoxMenuItem checkboxMenuItem = mock(JCheckBoxMenuItem.class);

 mainMenu.addShortcutAndIcon(checkboxMenuItem, "Preferences");

 doCallRealMethod().when(checkboxMenuItem).actionPerformed(any(ActionEvent.class));

 mainMenu.fireEvent(new InspirentoEvent(this, InspirentoEvent.TYPE_MENU_CHECKED, checkboxMenuItem.getText(), checkboxMenuItem.isSelected()));

 verify(checkboxMenuItem, times(1)).actionPerformed(any(ActionEvent.class));
 }
}