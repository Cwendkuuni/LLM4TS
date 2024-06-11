package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.ui.*;
import com.allenstudio.ir.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import org.junit.Test;

public class MainMenuTest {

 private static final String RESOURCE_BASE = "com.allenstudio.ir.ui.MainMenu.";

 @Test
 public void testCreateMenu() throws Exception {
  InspirentoMediator mediator = new InspirentoMediator();
  JMenuBar menuBar = new MainMenu(mediator);
  String[] menus = InspirentoUtilities.tokenize(UIResources.getString(RESOURCE_BASE + "menu"));
  for (String menu : menus) {
   JMenu menuComponent = menuBar.createMenu(menu);
   assertNotNull(menuComponent);
  }
 }

 @Test
 public void testCreateMenuWithSubMenu() throws Exception {
  InspirentoMediator mediator = new InspirentoMediator();
  JMenuBar menuBar = new MainMenu(mediator);
  String[] menus = InspirentoUtilities.tokenize(UIResources.getString(RESOURCE_BASE + "menu"));
  for (String menu : menus) {
   JMenu menuComponent = menuBar.createMenu(menu);
   if (menu.endsWith(">")) {
    String submenuName = menu.substring(0, menu.length() - 1);
    JMenu submenuComponent = menuComponent.add(menuBar.createMenu(submenuName));
    assertNotNull(submenuComponent);
   }
  }
 }

 @Test
 public void testCreateMenuItem() throws Exception {
  InspirentoMediator mediator = new InspirentoMediator();
  JMenuBar menuBar = new MainMenu(mediator);
  String[] menus = InspirentoUtilities.tokenize(UIResources.getString(RESOURCE_BASE + "menu"));
  for (String menu : menus) {
   JMenu menuComponent = menuBar.createMenu(menu);
   String[] items = InspirentoUtilities.tokenize(UIResources.getString(menu));
   for (String item : items) {
    if (item.charAt(0) != '#') {
     JMenuItem jMenuItem = menuComponent.add(menuBar.createMenuItem(item));
     assertNotNull(jMenuItem);
    }
   }
  }
 }

 @Test
 public void testCreateCheckboxMenuItem() throws Exception {
  InspirentoMediator mediator = new InspirentoMediator();
  JMenuBar menuBar = new MainMenu(mediator);
  String[] menus = InspirentoUtilities.tokenize(UIResources.getString(RESOURCE_BASE + "menu"));
  for (String menu : menus) {
   JMenu menuComponent = menuBar.createMenu(menu);
   String[] items = InspirentoUtilities.tokenize(UIResources.getString(menu));
   for (String item : items) {
    if (item.charAt(0) == '#') {
     JCheckBoxMenuItem jMenuItem = (JCheckBoxMenuItem) menuComponent.add(menuBar.createMenuItem(item));
     assertNotNull(jMenuItem);
    }
   }
  }
 }

 @Test
 public void testAddShortcutAndIcon() throws Exception {
  InspirentoMediator mediator = new InspirentoMediator();
  JMenuBar menuBar = new MainMenu(mediator);
  String[] menus = InspirentoUtilities.tokenize(UIResources.getString(RESOURCE_BASE + "menu"));
  for (String menu : menus) {
   JMenu menuComponent = menuBar.createMenu(menu);
   String[] items = InspirentoUtilities.tokenize(UIResources.getString(menu));
   for (String item : items) {
    if (!"-".equals(item)) {
     JMenuItem jMenuItem = menuComponent.add(menuBar.createMenuItem(item));
     if (item.length() > 1 && item.charAt(item.length() - 1) == '>') {
      String submenuName = item.substring(0, item.length() - 1);
      JMenu submenuComponent = menuComponent.add(menuBar.createMenu(submenuName));
      String shortcut = UIResources.getString(item + "Shortcut");
      assertEquals(KeyStroke.getKeyStroke(shortcut), jMenuItem.getAccelerator());
      File iconFile = new File(UIResources.getString(item + "Icon"));
      InputStream inputStream = new FileInputStream(iconFile);
      ImageIcon icon = (ImageIcon) jMenuItem.getIcon();
      assertEquals(inputStream.readAllBytes(), icon.getImage().getRGB(0, 0, icon.getIconWidth(), icon.getIconHeight(), null, 0, icon.getIconWidth(), icon.getIconHeight()));
      inputStream.close();
      menuComponent.remove(submenuComponent);
     } else {
      String shortcut = UIResources.getString(item + "Shortcut");
      assertEquals(KeyStroke.getKeyStroke(shortcut), jMenuItem.getAccelerator());
      String iconPath = UIResources.getString(item + "Icon");
      URL url = menuBar.getClass().getResource(iconPath);
      ImageIcon icon = (ImageIcon) jMenuItem.getIcon();
      assertEquals(url.toString(), icon.getDescription(null).toString());
     }
    }
   }
  }
 }

 @Test
 public void testGetMnemonic() throws Exception {
  InspirentoMediator mediator = new InspirentoMediator();
  JMenuBar menuBar = new MainMenu(mediator);
  String[] menus = InspirentoUtilities.tokenize(UIResources.getString(RESOURCE_BASE + "menu"));
  for (String menu : menus) {
   JMenu menuComponent = menuBar.createMenu(menu);
   String[] items = InspirentoUtilities.tokenize(UIResources.getString(menu));
   for (String item : items) {
    JMenuItem jMenuItem = menuComponent.add(menuBar.createMenuItem(item));
    if (item.length() > 2 && item.charAt(item.length() - 2) == '*') {
     assertEquals(item.charAt(item.length() - 1), jMenuItem.getMnemonic());
    }
   }
  }
 }

 @Test
 public void testNullMediator() {
  MainMenu menu = new MainMenu(null);
  assertNull(menu);
 }
}