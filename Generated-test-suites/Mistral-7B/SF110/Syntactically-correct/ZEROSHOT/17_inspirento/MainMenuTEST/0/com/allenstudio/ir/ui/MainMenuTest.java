package com.allenstudio.ir.ui;

import static org.junit.Assert.*;

import java.awt.Image;
import java.awt.KeyEventDispatcher.KeyEventDispatcherEvent;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.ui.MainMenu;
import com.allenstudio.ir.util.UIResources;

public class MainMenuTest {

 private InspirentoMediator mediator;
 private MainMenu mainMenu;

 @Before
 public void setUp() {
 mediator = new InspirentoMediator();
 mainMenu = new MainMenu(mediator);
 KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
 @Override
 public boolean dispatchKeyEvent(KeyEventDispatcherEvent event) {
 if (event.getKeyCode() == KeyEvent.VK_ESCAPE && event.getID() == KeyEvent.ID_KEY_PRESSED) {
 System.exit(0);
 }
 return false;
 }
 });
 }

 @Test
 public void testMainMenuConstruction() {
 String[] menus = InspirentoUtilities.tokenize(UIResources.getString("menu"));
 assertEquals(menus.length, mainMenu.getMenuCount());
 }

 @Test
 public void testCreateMenu() {
 String menu = "File";
 JMenu expectedMenu = new JMenu(menu);
 expectedMenu.setMnemonicKey(KeyEvent.VK_F);
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertEquals(expectedMenu, actualMenu);
 }

 @Test
 public void testCreateSubMenu() {
 String menu = "Edit";
 String subMenu = "Paste";
 JMenu expectedSubMenu = new JMenu(subMenu);
 expectedSubMenu.setMnemonicKey(KeyEvent.VK_P);
 JMenu actualMenu = mainMenu.createMenu(menu);
 JMenu subMenuFromActual = (JMenu) actualMenu.getComponent(0);
 JMenu expectedSubMenuFromActual = mainMenu.createMenu(subMenu);
 assertEquals(expectedSubMenu, subMenuFromActual);
 assertEquals(expectedSubMenuFromActual, mainMenu.getComponent(1));
 }

 @Test
 public void testCreateCheckBoxMenuItem() {
 String menu = "View";
 String checkboxMenuItem = "Show Tooltip";
 JCheckBoxMenuItem expectedMenuItem = new JCheckBoxMenuItem(checkboxMenuItem);
 expectedMenuItem.setMnemonicKey(KeyEvent.VK_T);
 JCheckBoxMenuItem actualMenuItem = (JCheckBoxMenuItem) mainMenu.getComponent(2);
 assertEquals(expectedMenuItem, actualMenuItem);
 }

 @Test
 public void testCreateMenuItem() {
 String menu = "Help";
 String menuItem = "About";
 JMenuItem expectedMenuItem = new JMenuItem(menuItem);
 expectedMenuItem.setMnemonicKey(KeyEvent.VK_A);
 JMenuItem actualMenuItem = (JMenuItem) mainMenu.getComponent(3);
 assertEquals(expectedMenuItem, actualMenuItem);
 }

 @Test
 public void testAddShortcut() {
 String menu = "File";
 String menuItem = "Save";
 String shortcut = "Ctrl S";
 KeyStroke expectedShortcut = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
 JMenuItem actualMenuItem = (JMenuItem) mainMenu.getComponent(mainMenu.getMenuCount() - 1);
 assertEquals(expectedShortcut, actualMenuItem.getAccelerator());
 }

 @Test
 public void testAddIcon() {
 String menu = "File";
 String menuItem = "Save";
 String iconPath = "/icon/save.png";
 URL expectedIconURL = new URL("file:" + iconPath);
 Image expectedIcon = new ImageIcon(expectedIconURL).getImage();
 JMenuItem actualMenuItem = (JMenuItem) mainMenu.getComponent(mainMenu.getMenuCount() - 1);
 Image actualIcon = ((JMenuItem) mainMenu.getComponent(mainMenu.getMenuCount() - 1)).getIcon().getImage();
 assertEquals(expectedIcon, actualIcon);
 }

 @Test
 public void testGetMenuCount() {
 int expectedCount = 4;
 int actualCount = mainMenu.getMenuCount();
 assertEquals(expectedCount, actualCount);
 }

 @Test
 public void testGetComponentAt() {
 MainMenu mainMenuSpy = new MainMenu(mediator) {
 @Override
 public Component getComponent(int index) {
 return super.getComponent(index);
 }
 };
 JMenuItem expectedMenuItem = new JMenuItem("Save");
 expectedMenuItem.setMnemonic(KeyEvent.VK_S);
 JMenuItem actualMenuItem = (JMenuItem) mainMenuSpy.getComponentAt(mainMenuSpy.getMenuCount() - 1);
 assertEquals(expectedMenuItem, actualMenuItem);
 }
}