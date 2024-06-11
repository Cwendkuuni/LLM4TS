package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.util.UIResources;

public class MainMenuTest {

 private InspirentoMediator mediatorMock; // mocking mediator for dependency injection
 private MainMenu mainMenu;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 mediatorMock = mock(InspirentoMediator.class);
 mainMenu = new MainMenu(mediatorMock);
 }

 @Test
 public void testMainMenuCreation() {
 // Testing that menus are created correctly
 String[] arr = {"File", "Edit", "Help"}; // given that menu string is "File|Edit|Help"
 List<String> menuNames = Arrays.asList(arr);
 JMenuBar menuBar = mainMenu;
 List<JMenu> menus = menuBar.getComponentsOfType(JMenu.class);
 assertEquals(menuNames.size(), menus.size());
 for (int i = 0; i < menuNames.size(); i++) {
 String menuName = menuNames.get(i);
 JMenu menu = menus.get(i);
 String expectedLabel = UIResources.getString(menuName + "Label");
 assertEquals(expectedLabel, menu.getText()); // checks if labels are loaded correctly
 }

 // Testing that checkboxes are created correctly
 String checkboxLabel = "CheckboxExample";
 String menuNameCheckboxIsUnder = "Edit";
 String menuCheckboxIsUnderLabel = UIResources.getString(menuNameCheckboxIsUnder + "Label");
 JMenu editMenu = findMenuWithName(menuCheckboxIsUnderLabel, menus);
 JCheckBoxMenuItem checkboxMenuItem = (JCheckBoxMenuItem) findMenuItemWithName(checkboxLabel, editMenu.getComponents());
 assertEquals(checkboxLabel, checkboxMenuItem.getText());
 }

 @Test
 public void testMenuCreationWithMissingLabel() {
 String missingLabel = "MissingLabel";
 // Given that menu string is "MissingLabel|Edit|Help"
 String[] arr = {"MissingLabel", "Edit", "Help"};
 List<String> menuNames = Arrays.asList(arr);
 JMenuBar menuBar = mainMenu;
 List<JMenu> menus = menuBar.getComponentsOfType(JMenu.class);
 // When creating the menu
 JMenu menu = mainMenu.createMenu(missingLabel);
 // Then it should not be null
 assertNotNull(menu);
 // and check that label is not displayed
 assertEquals("", menu.getText());
 }

 @Test
 public void testCheckboxWithMissingLabelAndIcon() {
 String checkboxWithMissingLabel = "MissingCheckbox";
 String menuNameCheckboxIsUnder = "Edit";
 // Given that menu string is "MissingLabel|Edit|Help"
 String[] arr = {"MissingLabel", "Edit", "Help"};
 List<String> menuNames = Arrays.asList(arr);
 JMenuBar menuBar = mainMenu;
 List<JMenu> menus = menuBar.getComponentsOfType(JMenu.class);
 JMenu editMenu = findMenuWithName(UIResources.getString(menuNameCheckboxIsUnder), menus);
 // When creating the checkbox
 JCheckBoxMenuItem checkboxMenuItem = mainMenu.createMenu(checkboxWithMissingLabel);
 // Then it should not be null
 assertNull(checkboxMenuItem);
 }

 @Test
 public void testCheckboxMenuItemShortcutKey() {
 String checkboxMenuItemShortcut = "Ctrl+S";
 String checkboxLabel = "CheckboxExample";
 String menuNameCheckboxIsUnder = "Edit";
 // Given that menu string is "File|Edit|Help"
 String[] arr = {"File", "Edit", "Help"};
 List<String> menuNames = Arrays.asList(arr);
 JMenuBar menuBar = mainMenu;
 List<JMenu> menus = menuBar.getComponentsOfType(JMenu.class);
 JMenu editMenu = findMenuWithName(UIResources.getString(menuNameCheckboxIsUnder), menus);
 JCheckBoxMenuItem checkboxMenuItem = (JCheckBoxMenuItem) findMenuItemWithName(checkboxLabel, editMenu.getComponents());
 // When setting shortcut key
 mainMenu.addShortcutAndIcon(checkboxMenuItem, checkboxMenuItemShortcut);
 // Then shortcut key should be set
 KeyStroke expectedShortcut = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK);
 KeyStroke actualShortcut = checkboxMenuItem.getAccelerator();
 assertEquals(expectedShortcut, actualShortcut);
 }

 @Test
 public void testMenuItemShortcutKey() {
 String menuItemShortcut = "Ctrl+O";
 String menuItemLabel = "OpenFile";
 String menuName = "File";
 // Given that menu string is "File|Edit|Help"
 String[] arr = {"File", "Edit", "Help"};
 List<String> menuNames = Arrays.asList(arr);
 JMenuBar menuBar = mainMenu;
 List<JMenu> menus = menuBar.getComponentsOfType(JMenu.class);
 JMenu fileMenu = findMenuWithName(UIResources.getString(menuName), menus);
 JMenuItem menuItem = (JMenuItem) findMenuItemWithName(menuItemLabel, fileMenu.getComponents());
 // When setting shortcut key
 mainMenu.addShortcutAndIcon(menuItem, menuItemShortcut);
 // Then shortcut key should be set
 KeyStroke expectedShortcut = KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK);
 KeyStroke actualShortcut = menuItem.getAccelerator();
 assertEquals(expectedShortcut, actualShortcut);
 }

 @Test
 public void testMenuCreationWithMissingResource() {
 String missingResourceLabel = "MissingResourceLabel";
 String menuNameMissingResourceIsUnder = "File";
 // Given that menu string is "File|Edit|MissingResourceLabel"
 String[] arr = {"File", "Edit", "MissingResourceLabel"};
 List<String> menuNames = Arrays.asList(arr);
 JMenuBar menuBar = mainMenu;
 List<JMenu> menus = menuBar.getComponentsOfType(JMenu.class);
 // When creating the menu
 JMenu menu = mainMenu.createMenu(missingResourceLabel);
 // Then it should print a warning message to the console
 verify(System.out, times(1)).println("Warning: icon file lost!");
 }

 @Test
 public void testMenuCreationWithMissingLabelAndIcon() {
 String missingLabel = "MissingLabel";
 String menuNameMissingLabelIsUnder = "File";
 // Given that menu string is "MissingLabel|Edit|Help"
 String[] arr = {"MissingLabel", "Edit", "Help"};
 List<String> menuNames = Arrays.asList(arr);
 JMenuBar menuBar = mainMenu;
 List<JMenu> menus = menuBar.getComponentsOfType(JMenu.class);
 // When creating the menu
 JMenu menu = mainMenu.createMenu(missingLabel);
 // Then it should not be null
 assertNull(menu);
 }

 private JMenu findMenuWithName(String label, List<JMenu> menus) {
 for (JMenu menu : menus) {
 if (menu.getText().equals(label)) {
 return menu;
 }
 }
 return null;
 }

 private JMenuItem findMenuItemWithName(String label, Object[] components) {
 for (Object component : components) {
 if (component instanceof JMenuItem && ((JMenuItem) component).getText().equals(label)) {
 return (JMenuItem) component;
 }
 }
 return null;
 }
}