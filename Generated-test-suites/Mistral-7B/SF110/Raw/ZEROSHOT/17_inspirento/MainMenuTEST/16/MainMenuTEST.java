package com.allenstudio.ir.ui;

import static org.junit.Assert.*;

import org.junit.Test;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class MainMenuTest {

 private MainMenu mainMenu;
 private InspirentoMediator mediator;

 @Before
 public void setUp() {
 mediator = new InspirentoMediator();
 mainMenu = new MainMenu(mediator);
 }

 @Test
 public void testCreateMenu() {
 String menuLabel = "File";
 String expectedMenuLabel = "File";
 JMenu expectedMenu = new JMenu(expectedMenuLabel);
 String[] items = {"New", "Save", "Save As", "-", "Open", "Import", "Export", "-", "Exit"};
 JMenuItem newMenuItem = null;
 JMenuItem saveMenuItem = null;
 JMenuItem saveAsMenuItem = null;
 JMenuItem openMenuItem = null;
 JMenuItem importMenuItem = null;
 JMenuItem exportMenuItem = null;
 JMenuItem exitMenuItem = null;

 JMenu actualMenu = mainMenu.createMenu(menuLabel);
 assertEquals(expectedMenuLabel, actualMenu.getText());

 for (String item : items) {
 if ("New".equals(item)) {
 newMenuItem = (JMenuItem) actualMenu.getComponent(0);
 assertEquals("New", newMenuItem.getText());
 }
 if ("Save".equals(item)) {
 saveMenuItem = (JMenuItem) actualMenu.getComponent(1);
 assertEquals("Save", saveMenuItem.getText());
 }
 if ("Save As".equals(item)) {
 saveAsMenuItem = (JMenuItem) actualMenu.getComponent(2);
 assertEquals("Save As", saveAsMenuItem.getText());
 }
 if ("Open".equals(item)) {
 openMenuItem = (JMenuItem) actualMenu.getComponent(5);
 assertEquals("Open", openMenuItem.getText());
 }
 if ("Import".equals(item)) {
 importMenuItem = (JMenuItem) actualMenu.getComponent(6);
 assertEquals("Import", importMenuItem.getText());
 }
 if ("Export".equals(item)) {
 exportMenuItem = (JMenuItem) actualMenu.getComponent(7);
 assertEquals("Export", exportMenuItem.getText());
 }
 if ("Exit".equals(item)) {
 exitMenuItem = (JMenuItem) actualMenu.getComponent(10);
 assertEquals("Exit", exitMenuItem.getText());
 }
 }

 assertNotNull(newMenuItem);
 assertNotNull(saveMenuItem);
 assertNotNull(saveAsMenuItem);
 assertNotNull(openMenuItem);
 assertNotNull(importMenuItem);
 assertNotNull(exportMenuItem);
 assertNotNull(exitMenuItem);
 }

 @Test
 public void testAddShortcutAndIcon() {
 String key = "New";
 String shortcut = "N";
 String iconPath = "icon1.png";

 JMenuItem newMenuItem = new JMenuItem(key);
 mainMenu.addShortcutAndIcon(newMenuItem, key);

 assertEquals(shortcut, newMenuItem.getAccelerator().getKeyChar());
 assertNotNull(newMenuItem.getIcon());
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuLabel() {
 String menuLabel = "InvalidLabel";
 mainMenu.createMenu(menuLabel);
 fail("Menu creation should have thrown a MissingResourceException");
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuItemLabel() {
 String menuLabel = "File";
 String itemKey = "MissingItem";
 mainMenu.createMenu(menuLabel);
 JMenu menu = (JMenu) mainMenu.getComponent(0);
 JMenuItem item = new JMenuItem(itemKey);
 mainMenu.addShortcutAndIcon(item, itemKey);
 menu.add(item);
 fail("Creation of menu item with missing label should have thrown a MissingResourceException");
 }

 @Test(expected = NullPointerException.class)
 public void testNullMenuLabel() {
 String menuLabel = null;
 mainMenu.createMenu(menuLabel);
 fail("Menu creation should have thrown a NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testNullMediator() {
 String menuLabel = "File";
 MainMenu mainMenu = new MainMenu(null);
 mainMenu.createMenu(menuLabel);
 fail("Menu creation should have thrown a NullPointerException");
 }
}