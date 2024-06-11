package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.ui.MainMenu;
import com.allenstudio.ir.ui.MainMenuTest;
import com.allenstudio.ir.util.*;
import javax.swing.*;

public class MainMenuTest {
    private MainMenu mainMenu;
    private InspirentoMediator mediator;

    @Before
    public void setUp() {
        mediator = new InspirentoMediator();
        mainMenu = new MainMenu(mediator);
    }

    @Test
    public void testMainMenuConstructor() {
        assertNotNull(mainMenu);
    }

    @Test
    public void testCreateMenuMethod() {
        String menu = "File";
        JMenu actualMenu = mainMenu.createMenu(menu);
        assertNotNull(actualMenu);

        // Test createMenu method with invalid menu string
        String invalidMenu = "";
        assertNull(mainMenu.createMenu(invalidMenu));
    }

    @Test
    public void testAddShortcutAndIconMethod() {
        String key = "File.Open";
        JMenuItem item = new JMenuItem("Open");
        mainMenu.addShortcutAndIcon(item, key);
        assertNotNull(item.getAccelerator());
        assertTrue(item.getIcon() != null);

        // Test addShortcutAndIcon method with null item
        JMenuItem nullItem = null;
        mainMenu.addShortcutAndIcon(nullItem, key);
        // Test addShortcutAndIcon method with null or empty key
        String emptyKey = "";
        mainMenu.addShortcutAndIcon(item, emptyKey);

        // Test addShortcutAndIcon method with missing resource exceptions for shortcuts and icons
        try {
            mainMenu.addShortcutAndIcon(item, "InvalidShortcutKey");
        } catch (MissingResourceException e) {
            // Expected
        }

        try {
            mainMenu.addShortcutAndIcon(item, "InvalidIconPath");
        } catch (MissingResourceException e) {
            // Expected
        }
    }

    @Test
    public void testMainMenuWithNullMediator() {
        mainMenu = new MainMenu(null);
        assertNull(mainMenu);
    }
}