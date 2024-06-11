// MainMenuTest.java

package com.allenstudio.ir.ui;

import static org.junit.Assert.*;

import org.junit.Test;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;

public class MainMenuTest {

    @Test
    public void testMainMenuConstruction() {
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu menu = new MainMenu(mediator);
        assertNotNull(menu);
    }

    @Test
    public void testCreateMenu() {
        String menu = "SampleMenu";
        MainMenu menu = new MainMenu(null);
        JMenu jMenu = menu.createMenu(menu);
        assertNotNull(jMenu);
    }

    @Test
    public void testAddShortcutAndIcon() {
        MainMenu menu = new MainMenu(null);
        JMenuItem item = new JMenuItem();
        String key = "SampleKey";
        menu.addShortcutAndIcon(item, key);
        assertNotNull(item.getAccelerator());
        assertNotNull(item.getIcon());
    }

    @Test(expected = NullPointerException.class)
    public void testMainMenuEmptyConstructor() {
        MainMenu menu = new MainMenu();
        assertNull(menu.mediator);
    }

    @Test(expected = NullPointerException.class)
    public void testMainMenuNullMediator() {
        MainMenu menu = new MainMenu(null);
        assertNull(menu.mediator);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateMenuNullMenu() {
        MainMenu menu = new MainMenu(null);
        String menu = null;
        JMenu jMenu = menu.createMenu(menu);
        assertNull(jMenu);
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingMenuLabel() {
        MainMenu menu = new MainMenu(null);
        String menu = "MissingLabel";
        JMenu jMenu = menu.createMenu(menu);
        assertNull(jMenu);
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingShortcut() {
        MainMenu menu = new MainMenu(null);
        JMenuItem item = new JCheckBoxMenuItem();
        String key = "MissingShortcut";
        menu.addShortcutAndIcon(item, key);
        assertNull(item.getAccelerator());
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingIcon() {
        MainMenu menu = new MainMenu(null);
        JMenuItem item = new JMenuItem();
        String key = "MissingIcon";
        menu.addShortcutAndIcon(item, key);
        assertNull(item.getIcon());
    }
}