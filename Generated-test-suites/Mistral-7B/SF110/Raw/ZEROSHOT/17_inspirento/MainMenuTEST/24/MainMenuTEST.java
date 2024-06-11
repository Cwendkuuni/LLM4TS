package com.allenstudio.ir.ui;

import static org.junit.Assert.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.*;
import java.util.*;

public class MainMenuTest {

 private MainMenu mainMenu;
 private InspirentoMediator mediator;

 @Before
 public void setup() {
 MockInspirentoMediator mediatorMock = new MockInspirentoMediator();
 this.mediator = mediatorMock;
 this.mainMenu = new MainMenu(this.mediator);
 }

 @Test
 public void testMainMenuCreation() {
 JMenuBar menuBar = new JMenuBar();
 menuBar.add(this.mainMenu);
 assertNotNull(this.mainMenu);
 assertEquals(MainMenu.class, this.mainMenu.getClass());
 }

 @Test
 public void testCreateMenu() {
 String menuName = "File";
 JMenu expectedMenu = new JMenu("File");
 JMenu actualMenu = this.mainMenu.createMenu(menuName);
 assertEquals(expectedMenu, actualMenu);
 }

 @Test
 public void testCreateMenuWithLabelAndShortcut() {
 String menuName = "File";
 String itemName = "Open";
 String expectedLabel = "Open";
 char expectedKeyChar = 'O';
 int expectedKeyCode = KeyEvent.VK_O;
 JMenuItem expectedMenuItem = new JMenuItem(expectedLabel);
 expectedMenuItem.setAccelerator(KeyStroke.getKeyStroke(expectedKeyChar, expectedKeyCode));
 String iconPath = "icon.png";
 URL expectedIconUrl = new File(iconPath).toURI().toURL();
 expectedMenuItem.setIcon(new ImageIcon(expectedIconUrl));
 JMenuItem actualMenuItem = null;
 try {
 actualMenuItem = (JMenuItem) this.mainMenu.createMenu("File")
 .add(this.mainMenu.createMenuItem("Open", "*Open", "#icon.png").getComponent());
 } catch (MissingResourceException e) {
 fail("Could not load resource for testing");
 }
 assertEquals(expectedLabel, actualMenuItem.getText());
 assertEquals(expectedMenuItem.getMnemonic(), expectedKeyChar);
 assertEquals(expectedMenuItem.getAccelerator().getKeyCode(), expectedKeyCode);
 assertEquals(expectedIconUrl, actualMenuItem.getIcon().getImage().getSource());
 }

 @Test
 public void testCreateMenuWithLabelAndNoShortcut() {
 String menuName = "Help";
 String itemName = "About";
 String expectedLabel = "About";
 JMenuItem expectedMenuItem = new JMenuItem(expectedLabel);
 JMenuItem actualMenuItem = null;
 try {
 actualMenuItem = (JMenuItem) this.mainMenu.createMenu("Help")
 .add(this.mainMenu.createMenuItem(itemName, null, null).getComponent());
 } catch (MissingResourceException e) {
 fail("Could not load resource for testing");
 }
 assertEquals(expectedLabel, actualMenuItem.getText());
 assertNull(actualMenuItem.getAccelerator());
 assertNull(actualMenuItem.getIcon());
 }

 @Test
 public void testCreateMenuWithSeparator() {
 String menuName = "Edit";
 this.mainMenu.createMenu(menuName).addSeparator();
 }

 @Test
 public void testCreateSubMenu() {
 String menuName = "Tools";
 JMenu expectedParentMenu = this.mainMenu.createMenu(menuName);
 JMenu expectedSubMenu = this.mainMenu.createMenu("Sub Menu");
 expectedSubMenu.setText("Sub Menu");
 expectedParentMenu.add(expectedSubMenu);
 JMenu actualSubMenu = null;
 try {
 actualSubMenu = (JMenu) this.mainMenu.createMenu(menuName)
 .add(this.mainMenu.createMenuItem("Sub Menu", "*Sub Menu", null).getComponent())
 .getComponent(1);
 } catch (MissingResourceException e) {
 fail("Could not load resource for testing");
 }
 assertEquals(expectedSubMenu, actualSubMenu);
 }
}

class MockInspirentoMediator implements InspirentoMediator {
 @Override
 public void changed() {
 // Empty implementation
 }
}