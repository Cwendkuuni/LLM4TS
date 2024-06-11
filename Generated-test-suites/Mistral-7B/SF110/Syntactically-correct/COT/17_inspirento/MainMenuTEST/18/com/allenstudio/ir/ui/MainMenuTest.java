package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.ui.*;
import com.allenstudio.ir.util.*;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;

public class MainMenuTest {

    @Test
    public void testMainMenuConstruction() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        assertNotNull(menu);
    }

    @Test
    public void testCreateMenu() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        String[] menus = {"File", "Edit", "Help"};
        for (String menu : menus) {
            JMenu jMenu = menu.createMenu(menu);
            assertNotNull(jMenu);
        }
    }

    @Test
    public void testAddShortcutAndIcon_ValidInputs() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        String key = "testKey";
        String label = "testLabel";
        String shortcut = "control C";
        String iconPath = "icon.png";
        JMenuItem jMenuItem = new JMenuItem(label);

        menu.addShortcutAndIcon(jMenuItem, key);

        assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK), jMenuItem.getAccelerator());
        try {
            URL url = new File("icon.png").toURI().toURL();
            assertEquals(new ImageIcon(url), jMenuItem.getIcon());
        } catch (MalformedURLException e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = MissingResourceException.class)
    public void testCreateMenu_MissingLabel() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        String menuName = "missingLabel";
        JMenu jMenu = menu.createMenu(menuName);
        assertNull(jMenu);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMenu_EmptyMenuItem() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        String menuName = "emptyMenuItem";
        menu.createMenu(menuName);
    }

    @Test
    public void testCreateMenu_NestedMenu() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        String menuName = "nestedMenu";
        String subMenuName = "subMenu";
        JMenu parentMenu = menu.createMenu(menuName);
        JMenu subMenu = menu.createMenu(subMenuName);

        parentMenu.add(subMenu);
        assertNotNull(subMenu);
    }

    @Test(expected = MissingResourceException.class)
    public void testAddShortcutAndIcon_MissingShortcut() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        String key = "missingShortcut";
        JMenuItem jMenuItem = new JMenuItem("Test Item");

        menu.addShortcutAndIcon(jMenuItem, key);
    }

    @Test(expected = MissingResourceException.class)
    public void testAddShortcutAndIcon_MissingIcon() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        String key = "missingIcon";
        JMenuItem jMenuItem = new JMenuItem("Test Item");

        menu.addShortcutAndIcon(jMenuItem, key);
    }
}