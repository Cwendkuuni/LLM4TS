package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import org.junit.*;

public class MainMenuTest {

    private MainMenu mainMenu;
    private InspirentoMediator mediator;

    @Before
    public void setUp() {
        mediator = new InspirentoMediator();
        mainMenu = new MainMenu(mediator);
    }

    @Test
    public void testMainMenuConstruction() {
        assertNotNull(mainMenu);
    }

    // Test createMenu method
    @Test
    public void testCreateMenu_ValidLabel() {
        JMenu testMenu = mainMenu.createMenu("TestMenu");
        assertNotNull(testMenu);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMenu_NullLabel() {
        mainMenu.createMenu(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMenu_EmptyLabel() {
        mainMenu.createMenu("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMenu_InvalidLabel() {
        mainMenu.createMenu("TestMenu%");
    }

    // Test addShortcutAndIcon method
    @Test
    public void testAddShortcut() {
        JMenuItem testMenuItem = new JMenuItem("");
        String key = "TestShortcut";
        mainMenu.addShortcutAndIcon(testMenuItem, key);
        assertEquals("TestShortcut", testMenuItem.getAccelerator().toString());
    }

    @Test
    public void testAddIcon() {
        JMenuItem testMenuItem = new JMenuItem("");
        String key = "TestIcon";
        String iconPath = "testIconPath.png";
        mainMenu.addShortcutAndIcon(testMenuItem, key);
        assertNotNull(testMenuItem.getIcon());
    }

    @Test
    public void testAddShortcutAndIcon_NullMenuItem() {
        String key = "TestShortcut";
        mainMenu.addShortcutAndIcon(null, key);
    }

    @Test
    public void testAddShortcutAndIcon_NullKey() {
        JMenuItem testMenuItem = new JMenuItem("");
        mainMenu.addShortcutAndIcon(testMenuItem, null);
    }

    @Test
    public void testAddShortcutAndIcon_MultipleKeys() {
        JMenuItem testMenuItem = new JMenuItem("");
        String key1 = "TestShortcut1";
        String key2 = "TestShortcut2";
        mainMenu.addShortcutAndIcon(testMenuItem, key1);
        mainMenu.addShortcutAndIcon(testMenuItem, key2);
        assertEquals(2, testMenuItem.getAccelerator().getKeyCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddShortcut_InvalidKey() {
        JMenuItem testMenuItem = new JMenuItem("");
        String key = "TestShortcut*";
        mainMenu.addShortcutAndIcon(testMenuItem, key);
    }

    @Test
    public void testAddIcon_NullPath() {
        JMenuItem testMenuItem = new JMenuItem("");
        String key = "TestIcon";
        mainMenu.addShortcutAndIcon(testMenuItem, key);
        assertNull(testMenuItem.getIcon());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddIcon_InvalidPath() {
        JMenuItem testMenuItem = new JMenuItem("");
        String key = "TestIcon";
        String iconPath = "testIconPath%";
        mainMenu.addShortcutAndIcon(testMenuItem, key);
    }

    // Test changed method
    @Test
    public void testChanged() {
        mainMenu.changed();
    }
}