package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.ui.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainMenuTest {

 private MainMenu mainMenu;
 private InspirentoMediator mediator;

 @Before
 public void setup() {
 mediator = new InspirentoMediator();
 mainMenu = new MainMenu(mediator);
 }

 @Test
 public void testMainMenuConstructor() {
 MainMenu menu = new MainMenu(mediator);
 assertNotNull(menu);
 }

 @Test
 public void testCreateMenu_ValidMenuName() {
 String menuName = "File";
 JMenu expectedResult = new JMenu("File");
 JMenu actualResult = mainMenu.createMenu(menuName);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testCreateMenu_MenuNameWithLabelSuffix() {
 String menuName = "File*";
 JMenu expectedResult = new JMenu("File");
 JMenu actualResult = mainMenu.createMenu(menuName);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMenu_EmptyMenuName() {
 String menuName = "";
 mainMenu.createMenu(menuName);
 fail("Expected IllegalArgumentException not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMenu_NullMenuName() {
 String menuName = null;
 mainMenu.createMenu(menuName);
 fail("Expected IllegalArgumentException not thrown");
 }

 @Test
 public void testAddShortcutAndIcon_ValidKey() {
 String key = "FileNew";
 JMenuItem expectedResult = new JMenuItem("New");
 JMenuItem actualResult = mainMenu.createMenu("File")
 .add(mainMenu.createMenuItem("New", key));
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testAddShortcutAndIcon_ValidKeyWithLabelSuffix() {
 String key = "FileNew*";
 JMenuItem expectedResult = new JMenuItem("New");
 JMenuItem actualResult = mainMenu.createMenu("File")
 .add(mainMenu.createMenuItem("New", key));
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testAddShortcutAndIcon_MissingKeyResource() {
 String key = "MissingKey";
 JMenuItem actualResult = mainMenu.createMenu("File")
 .add(mainMenu.createMenuItem("MissingMenuItem", key));
 assertNull(actualResult);
 }

 @Test
 public void testAddShortcutAndIcon_MissingIconResource() {
 String key = "FileNew";
 JMenuItem expectedResult = new JMenuItem("New");
 JMenuItem actualResult = mainMenu.createMenu("File")
 .add(mainMenu.createMenuItem("New", key));
 assertNotNull(actualResult);
 assertNull(actualResult.getIcon());
 }

 @Test
 public void testAddShortcutAndIcon_ValidKeyAndIcon() {
 String key = "FileNew";
 JMenuItem expectedResult = new JMenuItem("New");
 expectedResult.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0));
 expectedResult.setIcon(new ImageIcon(getClass().getResource("/com/allenstudio/ir/ui/icons/new.png")));
 JMenuItem actualResult = mainMenu.createMenu("File")
 .add(mainMenu.createMenuItem("New", key));
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testChanged() {
 mainMenu.changed();
 // No action expected for this test case
 }
}