package com.allenstudio.ir.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Image;
import java.awt.KeyEvent;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEventListener;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.ui.MainMenu;

public class MainMenuTest {

 private MainMenu mainMenu;
 private InspirentoMediator mediator;

 @Before
 public void setUp() {
 mediator = new InspirentoMediator();
 mainMenu = new MainMenu(mediator);
 }

 @Test
 public void testMainMenuCreation() {
 assertNotNull(mainMenu);
 }

 @Test
 public void testCreateMenu() {
 String[] menus = {"File", "Edit", "View", "Help"};
 int numMenus = menus.length;
 JMenu menu = mainMenu.createMenu("File");
 assertEquals(numMenus, mainMenu.getMenuComponentCount());
 assertEquals("File", menu.getText());
 }

 @Test
 public void testCreateMenuItem() {
 String[] menuItems = {"New", "Open", "Save", "Save As", "Exit"};
 int numItems = menuItems.length;
 String label = "New";
 JMenuItem newMenuItem = (JMenuItem) mainMenu.getMenuComponent(0).getMenuComponent(0);
 assertEquals(numItems, newMenuItem.getMenuComponentCount());
 assertEquals(label, newMenuItem.getText());
 }

 @Test
 public void testCreateCheckboxMenuItem() {
 String label = "Show Grid";
 JCheckBoxMenuItem checkboxMenuItem = (JCheckBoxMenuItem) mainMenu.getMenuComponent(0).getMenuComponent(1);
 assertEquals(label, checkboxMenuItem.getText());
 assertFalse(checkboxMenuItem.isSelected());
 }

 @Test
 public void testAddShortcutAndIcon() {
 String shortcut = "Ctrl N";
 KeyEvent keyEvent = new KeyEvent(new StringSelection(""), KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_N, 0);
 JMenuItem newMenuItem = (JMenuItem) mainMenu.getMenuComponent(0).getMenuComponent(0);
 newMenuItem.addKeyListener(new KeyEventListener() {
 @Override
 public void keyPressed(KeyEvent e) {
 assertEquals(shortcut, KeyStroke.getKeyStroke(e.getKeyCode(), e.getModifiers()).toString());
 }

 @Override
 public void keyReleased(KeyEvent e) {
 // Do nothing
 }

 @Override
 public void keyTyped(KeyEvent e) {
 // Do nothing
 }
 });
 }

 @Test
 public void testAddShortcutAndIconWithIcon() {
 String iconPath = "icon.png";
 String shortcut = "Ctrl O";
 JMenuItem openMenuItem = (JMenuItem) mainMenu.getMenuComponent(1).getMenuComponent(0);
 assertEquals(iconPath, mainMenu.getClass().getResource(iconPath).toString());
 openMenuItem.addKeyListener(new KeyEventListener() {
 @Override
 public void keyPressed(KeyEvent e) {
 assertEquals(shortcut, KeyStroke.getKeyStroke(e.getKeyCode(), e.getModifiers()).toString());
 }

 @Override
 public void keyReleased(KeyEvent e) {
 // Do nothing
 }

 @Override
 public void keyTyped(KeyEvent e) {
 // Do nothing
 }
 });
 }

 @Test
 public void testAddSeparator() {
 JMenuItem separator = (JMenuItem) mainMenu.getMenuComponent(1).getMenuComponent(1);
 assertTrue(separator.isSeparator());
 }

 @Test
 public void testChanged() {
 // Not tested as method is empty
 }

 @After
 public void tearDown() {
 mainMenu = null;
 mediator = null;
 }
}