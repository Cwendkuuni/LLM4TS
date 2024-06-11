// Test file for MainMenu class
// 
package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import org.junit.Test;

public class MainMenuTest {

    // Test cases for MainMenu construction and createMenu method

    @Test
    public void testMainMenuConstruction() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        assertNotNull(menu);
    }

    @Test
    public void testCreateMenu() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        JMenu createdMenu = menu.createMenu("TestMenu");
        assertNotNull(createdMenu);
    }

    // Test cases for addShortcutAndIcon method

    @Test
    public void testAddShortcutAndIcon() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem("TestItem");
        menu.addShortcutAndIcon(item, "TestKey");
        assertNotNull(item.getAccelerator());
        assertEquals("TestKey", item.getActionCommand());
    }

    // Edge cases and error scenarios

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMenuWithEmptyString() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        menu.createMenu("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMenuWithNullString() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        menu.createMenu(null);
    }

    @Test(expected = MissingResourceException.class)
    public void testCreateMenuWithMissingLabel() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        try {
            menu.createMenu("MissingLabel");
        } catch (MissingResourceException e) {
            // Expected
            assertNotNull(e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddShortcutAndIconWithNullItem() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        JMenuItem item = null;
        menu.addShortcutAndIcon(item, "TestKey");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddShortcutAndIconWithNullKey() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem("TestItem");
        menu.addShortcutAndIcon(item, null);
    }

    @Test(expected = MissingResourceException.class)
    public void testAddShortcutAndIconWithMissingShortcut() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem("TestItem");
        menu.addShortcutAndIcon(item, "MissingShortcut");
    }

    @Test(expected = MissingResourceException.class)
    public void testAddShortcutAndIconWithMissingIcon() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem("TestItem");
        menu.addShortcutAndIcon(item, "TestKey");
    }

    @Test
    public void testAddShortcutAndIconWithInvalidIconUrl() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem("TestItem");
        menu.addShortcutAndIcon(item, "TestKey");
        assertNull(item.getIcon());
    }

    @Test(expected = MissingResourceException.class)
    public void testCreateMenuWithMissingResource() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        try {
            menu.createMenu("MissingMenu");
        } catch (MissingResourceException e) {
            // Expected
            assertNotNull(e.getMessage());
        }
    }
}

//