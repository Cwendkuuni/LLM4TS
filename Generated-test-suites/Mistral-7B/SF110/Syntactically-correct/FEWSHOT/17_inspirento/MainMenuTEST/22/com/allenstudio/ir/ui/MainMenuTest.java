package com.allenstudio.ir.ui;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MainMenuTest {

    @Test
    public void testMainMenuConstruction() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        assertNotNull(menu);
    }

    @Test
    public void testCreateMenuWithLabel() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        String menuName = "TestMenu";
        MainMenu menu = new MainMenu(mediator);
        JMenu createdMenu = menu.createMenu(menuName);
        String expectedLabel = "TestMenu";
        assertEquals(expectedLabel, createdMenu.getText());
    }

    @Test
    public void testCreateMenuWithSubMenus() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        String menuName = "TestMenu";
        String subMenuName = "SubMenu";
        MainMenu menu = new MainMenu(mediator);
        JMenu createdMenu = menu.createMenu(menuName);
        JMenu subCreatedMenu = menu.createMenu(subMenuName);
        assertEquals(subMenuName, subCreatedMenu.getText());
        createdMenu.add(subCreatedMenu);
    }

    @Test
    public void testCreateCheckboxMenuItem() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        String menuName = "TestMenu";
        String itemName = "TestItem";
        MainMenu menu = new MainMenu(mediator);
        JMenu createdMenu = menu.createMenu(menuName);
        JCheckBoxMenuItem createdCheckboxMenuItem = (JCheckBoxMenuItem) menu.createMenuItem(createdMenu, itemName, null);
        String expectedLabel = "TestItem";
        assertEquals(expectedLabel, createdCheckboxMenuItem.getText());
    }

    @Test
    public void testCreateMenuItem() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        String menuName = "TestMenu";
        String itemName = "TestItem";
        MainMenu menu = new MainMenu(mediator);
        JMenu createdMenu = menu.createMenu(menuName);
        JMenuItem createdMenuItem = menu.createMenuItem(createdMenu, itemName, null);
        String expectedLabel = "TestItem";
        assertEquals(expectedLabel, createdMenuItem.getText());
    }

    @Test
    public void testAddShortcutAndIcon() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        String menuName = "TestMenu";
        String itemName = "TestItem";
        String shortcut = "Ctrl+S";
        String iconPath = "icon.png";
        MainMenu menu = new MainMenu(mediator);
        JMenuItem createdMenuItem = menu.createMenuItem(menu.createMenu(menuName), itemName, null);
        menu.addShortcutAndIcon(createdMenuItem, itemName);
        KeyStroke expectedShortcut = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);
        assertEquals(expectedShortcut, createdMenuItem.getAccelerator());
        assertNull(createdMenuItem.getIcon());

        // Assuming that the icon file is present in the classpath
        menu.addShortcutAndIcon(createdMenuItem, itemName);
        assertNotNull(createdMenuItem.getIcon());
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingMenuLabel() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        String menuName = "MissingLabel";
        MainMenu menu = new MainMenu(mediator);
        menu.createMenu(menuName);
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingMenuItemLabel() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        String menuName = "TestMenu";
        String itemName = "MissingLabel";
        MainMenu menu = new MainMenu(mediator);
        JMenu createdMenu = menu.createMenu(menuName);
        menu.createMenuItem(createdMenu, itemName, null);
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingShortcutLabel() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        String menuName = "TestMenu";
        String itemName = "TestItem";
        MainMenu menu = new MainMenu(mediator);
        JMenuItem createdMenuItem = menu.createMenuItem(menu.createMenu(menuName), itemName, null);
        menu.addShortcutAndIcon(createdMenuItem, itemName);
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingIconLabel() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        String menuName = "TestMenu";
        String itemName = "TestItem";
        MainMenu menu = new MainMenu(mediator);
        JMenu createdMenu = menu.createMenu(menuName);
        JMenuItem createdMenuItem = menu.createMenuItem(createdMenu, itemName, null);
        menu.addShortcutAndIcon(createdMenuItem, itemName);
    }
}