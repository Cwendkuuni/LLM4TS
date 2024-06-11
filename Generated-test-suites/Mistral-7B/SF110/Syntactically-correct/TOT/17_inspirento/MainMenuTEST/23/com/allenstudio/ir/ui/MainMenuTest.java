package com.allenstudio.ir.ui;

import com.allenstudio.ir.core.InspirentoMediator;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.net.URL;
import java.util.Arrays;
import static org.junit.Assert.*;

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
 String[] menus = InspirentoUtilities.tokenize(UIResources.getString("menu"));
 assertEquals(menus.length, mainMenu.getMenuCount());
 String menu = menus[0];
 JMenu expectedMenu = new JMenu(menu.substring(0, menu.length() - 2));
 JMenu actualMenu = mainMenu.getComponent(0);
 assertEquals(expectedMenu.getText(), actualMenu.getName());
 }

 @Test
 public void testCreateMenuItem() {
 String menu = "File";
 String item = "New Project";
 String expectedLabel = UIResources.getString("NewProjectLabel");
 JMenuItem expectedMenuItem = new JMenuItem(expectedLabel);
 expectedMenuItem.setAccelerator(KeyStroke.getKeyStroke("Ctrl N"));
 String key = "NewProject";
 String iconPath = UIResources.getString("NewProjectIcon");
 String shortcut = UIResources.getString(key + "Shortcut");
 expectedMenuItem.setMnemonic(shortcut.charAt(0));
 try {
 URL iconUrl = new URL(new File("").toURI().toURL(), iconPath);
 expectedMenuItem.setIcon(new ImageIcon(iconUrl));
 } catch (MalformedURLException e) {
 e.printStackTrace();
 }
 JMenu menuComponent = mainMenu.getComponent(0);
 JMenu subMenu = menuComponent.getComponent(menu.indexOf(item));
 JMenuItem actualMenuItem = (JMenuItem) subMenu.getComponent(item.indexOf(">"));
 assertEquals(expectedMenuItem.getText(), actualMenuItem.getText());
 assertEquals(expectedMenuItem.getAccelerator(), actualMenuItem.getAccelerator());
 assertEquals(expectedMenuItem.getMnemonic(), actualMenuItem.getMnemonic());
 assertEquals(expectedMenuItem.getIcon(), actualMenuItem.getIcon());
 }

 @Test
 public void testCreateCheckboxMenuItem() {
 String menu = "Edit";
 String item = "#CheckItem";
 String expectedLabel = UIResources.getString("CheckItemLabel");
 JCheckBoxMenuItem expectedMenuItem = new JCheckBoxMenuItem(expectedLabel);
 expectedMenuItem.setMnemonic(item.charAt(item.length() - 1));
 String key = item.substring(1);
 assertEquals(expectedMenuItem.getText(), mainMenu.getItem(1).getName());
 JCheckBoxMenuItem actualMenuItem = (JCheckBoxMenuItem) mainMenu.getItem(1).getComponent(1);
 assertEquals(expectedMenuItem.getText(), actualMenuItem.getText());
 assertEquals(expectedMenuItem.isSelected(), actualMenuItem.isSelected());
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingResourceException() {
 mainMenu.createMenu("MissingMenu");
 fail("Expected MissingResourceException");
 }
}