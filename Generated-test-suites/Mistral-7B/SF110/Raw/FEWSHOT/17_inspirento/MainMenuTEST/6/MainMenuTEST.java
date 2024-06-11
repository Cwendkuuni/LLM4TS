package com.allenstudio.ir.ui;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;

public class MainMenuTest {

    @Test
    public void testMainMenuCreation() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        assertNotNull(menu);
    }

    @Test
    public void testCreateMenu_EmptyMenu() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        JMenu emptyMenu = menu.createMenu("");
        assertNull(emptyMenu);
    }

    @Test
    public void testCreateMenu_SimpleMenu() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        String menuLabel = "File";
        JMenu menu = menu.createMenu(menuLabel);
        assertEquals(menuLabel, menu.getText());
    }

    @Test
    public void testCreateMenu_MenuWithSubMenus() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        String menuLabel = "Edit";
        String subMenuLabel = "Undo";
        JMenu menu = menu.createMenu(menuLabel);
        JMenu subMenu = menu.createMenu(subMenuLabel);
        menu.add(subMenu);
        assertEquals(menuLabel, menu.getText());
        assertEquals(subMenuLabel, subMenu.getText());
    }

    @Test
    public void testCreateMenu_MenuWithCheckboxMenuItem() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        String menuLabel = "Options";
        String checkboxMenuItemLabel = "Show Grid";
        JMenu menu = menu.createMenu(menuLabel);
        JCheckBoxMenuItem jMenuItem = (JCheckBoxMenuItem) menu.add(new JCheckBoxMenuItem(checkboxMenuItemLabel));
        assertEquals(checkboxMenuItemLabel, jMenuItem.getText());
    }

    @Test
    public void testCreateMenu_MenuWithJMenuItem() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        String menuLabel = "Help";
        String itemLabel = "About";
        JMenu menu = menu.createMenu(menuLabel);
        JMenuItem jMenuItem = (JMenuItem) menu.add(new JMenuItem(itemLabel));
        assertEquals(itemLabel, jMenuItem.getText());
    }

    @Test
    public void testCreateMenu_MenuWithSeparator() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        String menuLabel = "File";
        JMenu menu = menu.createMenu(menuLabel);
        menu.addSeparator();
        assertTrue(menu.getComponentCount() > 1);
    }

    @Test
    public void testAddShortcutAndIcon() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        JMenuItem item = new JMenuItem("Open");
        String key = "Open";
        menu.addShortcutAndIcon(item, key);
        assertEquals(KeyEvent.VK_O, item.getAccelerator().getKeyCode());
        assertNull(item.getIcon());

        String iconPath = "/icons/open.png";
        String shortcut = "Ctrl+O";
        UIManager.put(key + "Shortcut", shortcut);
        UIManager.put(key + "Icon", iconPath);
        menu.addShortcutAndIcon(item, key);
        assertEquals(KeyEvent.VK_O, item.getAccelerator().getKeyCode());
        assertNotNull(item.getIcon());
    }

    @Test(expected = MissingResourceException.class)
    public void testCreateMenu_MissingMenuLabel() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        String menuLabel = "NonExistentMenu";
        menu.createMenu(menuLabel);
    }

    @Test(expected = MissingResourceException.class)
    public void testCreateMenu_MissingItemLabel() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        String menuLabel = "File";
        String itemLabel = "NonExistentItem";
        menu.createMenu(menuLabel);
        JMenu menu = (JMenu) menu.getComponents()[0];
        menu.add(new JMenuItem(itemLabel));
    }

    @Test(expected = MissingResourceException.class)
    public void testCreateMenu_MissingShortcut() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        JMenuItem item = new JMenuItem("Open");
        String key = "Open";
        menu.addShortcutAndIcon(item, key);
        assertNotNull(item.getAccelerator());
        UIManager.put(key + "Shortcut", "");
        menu.addShortcutAndIcon(item, key);
    }

    @Test(expected = MissingResourceException.class)
    public void testCreateMenu_MissingIconPath() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        JMenuItem item = new JMenuItem("Open");
        String key = "Open";
        UIManager.put(key + "Shortcut", "Ctrl+O");
        UIManager.put(key + "Icon", "/icons/open.png");
        menu.addShortcutAndIcon(item, key);
        UIManager.put(key + "Icon", "");
        menu.addShortcutAndIcon(item, key);
    }
}