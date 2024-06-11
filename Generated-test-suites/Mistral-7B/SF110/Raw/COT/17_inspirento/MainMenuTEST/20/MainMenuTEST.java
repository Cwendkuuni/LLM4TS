package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.ui.*;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.net.URL;
import java.util.*;

public class MainMenuTest {

    @Test
    public void testMainMenuConstructor() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        assertNotNull(menu);
    }

    @Test
    public void testCreateMenuNormalCase() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        String[] menus = {"File", "Edit", "Help"};
        for (String menu : menus) {
            JMenu jMenu = menu.createMenu(menu);
            assertNotNull(jMenu);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testCreateMenuMissingLabel() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        String menu = "MissingLabel";
        JMenu jMenu = menu.createMenu(menu);
        assertNull(jMenu);
    }

    @Test(expected = MissingResourceException.class)
    public void testCreateMenuMissingResourceLabel() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        String menu = "MenuWithMissingLabel";
        JMenu jMenu = menu.createMenu(menu);
        assertNull(jMenu);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMenuEmptyMenuItems() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        String menu = "EmptyMenu";
        JMenu jMenu = menu.createMenu(menu);
        assertNull(jMenu);
    }

    @Test
    public void testCreateMenuSubMenu() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        String menu = "SubMenu";
        JMenu parentMenu = new JMenu(menu);
        JMenu childMenu = menu.createMenu("ChildMenu");
        parentMenu.add(childMenu);

        assertTrue(parentMenu.getMenuComponents().length > 1);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateMenuMissingShortcut() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        String key = "MissingShortcut";
        JMenuItem jMenuItem = menu.createMenu(key).findComponentAt(0);
        assertNull(jMenuItem);
    }

    @Test(expected = MissingResourceException.class)
    public void testCreateMenuMissingResourceShortcut() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        String key = "KeyWithMissingShortcut";
        JMenuItem jMenuItem = menu.createMenu(key).findComponentAt(0);
        assertNull(jMenuItem);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateMenuMissingIcon() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        String key = "MissingIcon";
        JMenuItem jMenuItem = menu.createMenu(key).findComponentAt(0);
        assertNull(jMenuItem.getIcon());
    }

    @Test
    public void testCreateMenuHasIcon() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        String key = "IconKey";
        JMenuItem jMenuItem = menu.createMenu(key).findComponentAt(0);
        assertNotNull(jMenuItem.getIcon());
    }

    @Test
    public void testAddShortcutAndIconNormalCase() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        String key = "Key";
        JMenuItem jMenuItem = new JMenuItem("Item");
        menu.addShortcutAndIcon(jMenuItem, key);

        assertEquals("Item", jMenuItem.getText());
        assertNotNull(jMenuItem.getAccelerator());
        assertNull(jMenuItem.getIcon());

        String iconPath = "IconPath";
        URL url = new URL("file:" + iconPath);
        menu.addShortcutAndIcon(jMenuItem, key);

        assertEquals("Item", jMenuItem.getText());
        assertNotNull(jMenuItem.getAccelerator());
        assertNotNull(jMenuItem.getIcon());
        assertEquals(url, jMenuItem.getIcon().getImage().getSource());
    }

    @Test(expected = MissingResourceException.class)
    public void testAddShortcutAndIconMissingLabel() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        String key = "KeyWithMissingLabel";
        JMenuItem jMenuItem = new JMenuItem();
        menu.addShortcutAndIcon(jMenuItem, key);

        assertNull(jMenuItem.getText());
        assertNotNull(jMenuItem.getAccelerator());
        assertNull(jMenuItem.getIcon());
    }

    @Test(expected = MissingResourceException.class)
    public void testAddShortcutAndIconMissingShortcut() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        String key = "KeyWithMissingShortcut";
        JMenuItem jMenuItem = new JMenuItem();
        menu.addShortcutAndIcon(jMenuItem, key);

        assertNull(jMenuItem.getText());
        assertNull(jMenuItem.getAccelerator());
        assertNull(jMenuItem.getIcon());
    }

    @Test(expected = MissingResourceException.class)
    public void testAddShortcutAndIconMissingIcon() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        String key = "KeyWithMissingIcon";
        JMenuItem jMenuItem = new JMenuItem();
        menu.addShortcutAndIcon(jMenuItem, key);

        assertNull(jMenuItem.getText());
        assertNotNull(jMenuItem.getAccelerator());
        assertNull(jMenuItem.getIcon());
    }

    @Test
    public void testChanged() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        menu.changed();
    }
}