package com.allenstudio.ir.ui;

import org.junit.*;
import static org.junit.Assert.*;
import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import javax.swing.*;

public class MainMenuTest {
    private MainMenu menu;
    private InspirentoMediator mediator;

    @Before
    public void setup() {
        mediator = new InspirentoMediator();
        menu = new MainMenu(mediator);
    }

    @Test
    public void testCreateMenu() {
        // Test single menus
        String[] menus = {"File", "Edit", "Help"};
        for (String menu : menus) {
            JMenu expectedMenu = new JMenu(menu);
            JMenu actualMenu = menu.createMenu(menu);
            assertEquals(expectedMenu.getText(), actualMenu.getText());
            if (!menu.equals("Help")) {
                assertEquals(expectedMenu, actualMenu);
            }
        }

        // Test menu with nested menus and items
        String menuString = "File>Save<#Save.png,Ctrl+S>Help>SubMenu>SubMenuItem";
        JMenu expectedFileMenu = new JMenu("File");
        JMenu expectedHelpMenu = new JMenu("Help");
        JMenu expectedSubMenu = new JMenu("SubMenu");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem subMenuItem = new JMenuItem("SubMenuItem");

        menu.createMenu(menuString);

        assertTrue(menu.getItemCount() == 4);
        assertEquals(expectedFileMenu, menu.getItem(0));
        assertEquals(expectedHelpMenu, menu.getItem(1));
        assertEquals(expectedSubMenu, expectedHelpMenu.getItem(0));
        assertEquals(subMenuItem, expectedSubMenu.getItem(0));
        assertEquals(saveMenuItem, expectedFileMenu.getItem(0));
    }

    @Test
    public void testAddShortcutAndIcon() {
        String key = "save";
        JMenuItem item = new JMenuItem("Save");
        String shortcut = "Ctrl S";
        String iconPath = "/path/to/icon.png";

        menu.addShortcutAndIcon(item, key);

        assertEquals(shortcut, item.getAccelerator().toString());
        assertNotNull(item.getIcon());
    }

    @Test
    public void testCreateMenuWithMissingResource() {
        String menu = "MissingMenu";

        try {
            menu.createMenu(menu);
            fail("Expected MissingResourceException");
        } catch (MissingResourceException e) {
            // Expected
        }
    }
}