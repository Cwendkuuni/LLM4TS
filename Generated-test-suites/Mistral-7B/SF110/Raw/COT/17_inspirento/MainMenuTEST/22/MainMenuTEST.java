package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.ui.*;
import org.junit.*;
import org.junit.platform.commons.annotation.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

public class MainMenuTest {

 private MainMenu mainMenu;
 private InspirentoMediator mediator;

 @Before
 public void setUp() {
 mediator = new InspirentoMediator();
 mainMenu = new MainMenu(mediator);
 }

 @Test
 public void testMainMenuConstruction() {
 assertNotNull(mainMenu);
 }

 @Test
 public void testCreateMenu() {
 String menu = "File";
 JMenu expectedMenu = new JMenu("File");
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertEquals(expectedMenu, actualMenu);
 }

 @Test
 public void testCreateMenuWithLabelAndShortcut() {
 String menu = "Edit";
 String itemLabel = "Cut";
 String shortcut = "Ctrl+X";
 String iconPath = "icon/cut.png";
 JMenuItem expectedMenuItem = new JMenuItem("Cut");
 expectedMenuItem.setAccelerator(KeyStroke.getKeyStroke(shortcut, 0, false));
 JMenuItem actualMenuItem = null;
 try {
 actualMenuItem = (JMenuItem) mainMenu.createMenu("Edit")
 .getItem(InspirentoUtilities.tokenize("Edit Cut")[0].length());
 } catch (ArrayIndexOutOfBoundsException e) {
 fail("There should be an item with label 'Cut' under the 'Edit' menu.");
 }
 assertEquals(expectedMenuItem, actualMenuItem);
 try {
 assertEquals(UIResources.getString(iconPath), actualMenuItem.getIcon().getDescription(null));
 } catch (MissingResourceException e) {
 fail("The icon file 'icon/cut.png' should be found.");
 }
 }

 @Test
 public void testCreateMenuWithLabelAndNoShortcut() {
 String menu = "Help";
 String itemLabel = "About";
 JMenuItem expectedMenuItem = new JMenuItem("About");
 JMenuItem actualMenuItem = null;
 try {
 actualMenuItem = (JMenuItem) mainMenu.createMenu("Help")
 .getItem(InspirentoUtilities.tokenize("Help About")[0].length());
 } catch (ArrayIndexOutOfBoundsException e) {
 fail("There should be an item with label 'About' under the 'Help' menu.");
 }
 assertEquals(expectedMenuItem, actualMenuItem);
 }

 @Test
 public void testCreateMenuWithSeparator() {
 String menu = "Tools";
 JMenu expectedMenu = new JMenu("Tools");
 expectedMenu.addSeparator();
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertEquals(expectedMenu, actualMenu);
 }

 @Test(expected = MissingResourceException.class)
 public void testLoadMenuWithMissingLabel() {
 String menu = "MissingLabel";
 mainMenu.createMenu(menu);
 fail("A MissingResourceException should have been thrown.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMenuWithEmptyMenuName() {
 mainMenu.createMenu("");
 fail("An IllegalArgumentException should have been thrown.");
 }

}