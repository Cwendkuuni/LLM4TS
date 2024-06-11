package com.allenstudio.ir.ui;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.ui.UIResources;
import com.allenstudio.ir.util.InspirentoUtilities;
import com.allenstudio.ir.util.MissingResourceException;

import javax.swing.*;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class MainMenuTest {

 private InspirentoMediator mediatorMock;
 private JMenuBar mainMenu;

 @Before
 public void setUp() {
 mediatorMock = Mockito.mock(InspirentoMediator.class);
 mainMenu = new MainMenu(mediatorMock);
 }

 @Test
 public void testMainMenuConstruction() {
 // Test that the MainMenu constructor initializes a JMenuBar with the correct menus
 JMenu[] menus = mainMenu.getMenus();
 String[] expectedMenuLabels = InspirentoUtilities.tokenize(UIResources.getString("menu"));
 assertEquals(expectedMenuLabels.length, menus.length);
 for (int i = 0; i < menus.length; ++i) {
 String menuLabel = expectedMenuLabels[i];
 JMenu menu = menus[i];
 assertEquals(menuLabel, menu.getText());
 }
 }

 @Test
 public void testCreateMenuWithLabel() {
 String menuLabel = "File";
 JMenu expectedMenu = new JMenu(menuLabel);
 JMenu actualMenu = mainMenu.createMenu(menuLabel);
 assertEquals(expectedMenu, actualMenu);
 }

 @Test
 public void testCreateMenuWithLabelAndSubmenu() {
 String parentMenuLabel = "File";
 String submenuLabel = "Edit";
 JMenu expectedParentMenu = new JMenu(parentMenuLabel);
 JMenu expectedSubmenu = new JMenu(submenuLabel);
 JMenu actualMenu = mainMenu.createMenu(parentMenuLabel + ">" + submenuLabel);
 assertEquals(expectedParentMenu, actualMenu.getParent());
 assertEquals(expectedSubmenu, actualMenu.getComponentCount() > 0 ? (JMenu) actualMenu.getComponent(0) : null);
 }

 @Test
 public void testCreateMenuWithLabelAndCheckboxMenuItem() {
 String menuLabel = "Preferences";
 String checkboxMenuItemLabel = "Show Tooltips";
 JCheckBoxMenuItem expectedMenuItem = new JCheckBoxMenuItem(checkboxMenuItemLabel);
 JMenuItem actualMenuItem = null;
 try {
 actualMenuItem = mainMenu.createMenu("Preferences")
 .add(mainMenu.createMenuItem("#PreferencesTooltips", checkboxMenuItemLabel + "*", null, null));
 }
 catch (MissingResourceException e) {
 fail("Could not load resource for Preferences menu label");
 }
 assertEquals(expectedMenuItem, actualMenuItem);
 }

 @Test
 public void testCreateMenuWithLabelAndNormalMenuItem() {
 String menuLabel = "Help";
 String menuItemLabel = "About";
 JMenuItem expectedMenuItem = new JMenuItem(menuItemLabel);
 JMenuItem actualMenuItem = null;
 try {
 actualMenuItem = mainMenu.createMenu(menuLabel)
 .add(mainMenu.createMenuItem(menuItemLabel, null, null, null));
 }
 catch (MissingResourceException e) {
 fail("Could not load resource for Help menu label");
 }
 assertEquals(expectedMenuItem, actualMenuItem);
 }

 @Test
 public void testAddShortcutAndIcon() {
 String menuLabel = "Edit";
 String shortcut = "Ctrl E";
 String iconPath = "edit.png";
 JMenuItem menuItem = mainMenu.createMenu(menuLabel).add(mainMenu.createMenuItem("#" + menuLabel, null, null, null));
 mainMenu.addShortcutAndIcon(menuItem, menuLabel);
 assertEquals(KeyStroke.getKeyStroke(shortcut.charAt(0), shortcut.charAt(1) == 'C' ? KeyEvent.CTRL_DOWN_MASK : KeyEvent.ALT_DOWN_MASK), menuItem.getAccelerator());
 File expectedIconFile = new File("edit.png");
 URL expectedIconURL = new URL("file:" + expectedIconFile.getAbsolutePath());
 URL actualIconURL = menuItem.getIcon().getDescription(null).getImage().getSource();
 assertEquals(expectedIconURL, actualIconURL);
 }
}