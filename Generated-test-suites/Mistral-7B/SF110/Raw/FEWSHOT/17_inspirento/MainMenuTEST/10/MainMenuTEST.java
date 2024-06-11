package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Arrays;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;

import org.junit.Test;

public class MainMenuTest {

    @Test
    public void testMainMenuConstruction() {
        InspirentoMediator mediator = new InspirentoMediator(); // create a mock mediator for testing
        MainMenu menu = new MainMenu(mediator);
        assertNotNull(menu);
        assertEquals(menu.getClass(), JMenuBar.class);
        // check the number of menus added
        int numMenus = 0;
        for (Component component : menu.getComponents()) {
            if (component instanceof JMenu) {
                numMenus++;
            }
        }
        assertNotEquals(0, numMenus);
    }

    @Test
    public void testCreateMenu() {
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu menu = new MainMenu(mediator);

        String menuName = "File";
        JMenu fileMenu = menu.createMenu(menuName);
        assertNotNull(fileMenu);
        assertEquals(fileMenu.getClass(), JMenu.class);
        assertEquals(fileMenu.getText(), menuName);

        // test case for menu with a label suffix
        String menuLabel = "Edit" + LABEL_SUFFIX;
        JMenu editMenu = menu.createMenu(menuLabel);
        assertEquals(editMenu.getText(), menuLabel.substring(0, menuLabel.length() - LABEL_SUFFIX.length()));
        assertEquals(editMenu.getMnemonic(), menuLabel.charAt(menuLabel.length() - 1));

        // test case for menu without a label suffix
        String menuNoLabel = "Help";
        JMenu helpMenu = menu.createMenu(menuNoLabel);
        assertEquals(helpMenu.getText(), menuNoLabel);
        assertEquals(helpMenu.getMnemonicKey(), KeyEvent.VK_H);
    }

    @Test
    public void testCreateMenuItem() {
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu menu = new MainMenu(mediator);

        // test case for menu item with label suffix and no shortcut or icon
        String itemName = "New" + LABEL_SUFFIX;
        JMenuItem newItem = menu.createMenuItem(itemName);
        assertEquals(newItem.getText(), itemName.substring(0, itemName.length() - LABEL_SUFFIX.length()));
        assertNull(newItem.getAccelerator());
        assertNull(newItem.getIcon());

        // test case for menu item with label suffix, shortcut, and icon
        String itemNameShortcut = "Open" + LABEL_SUFFIX + SHORTCUT_SUFFIX;
        String shortcut = "Ctrl O";
        String iconPath = "icons/open.png";
        JMenuItem openItem = menu.createMenuItem(itemNameShortcut, shortcut, iconPath);
        assertEquals(openItem.getText(), itemNameShortcut.substring(0, itemNameShortcut.length() - LABEL_SUFFIX.length()));
        assertEquals(openItem.getAccelerator(), KeyStroke.getKeyStroke(shortcut));
        assertNotNull(openItem.getIcon());
        assertNotNull(openItem.getIcon().getImage());

        // test case for menu item without label suffix, shortcut, and icon
        String itemNameNoLabel = "Save";
        JMenuItem saveItem = menu.createMenuItem(itemNameNoLabel);
        assertEquals(saveItem.getText(), itemNameNoLabel);
        assertNull(saveItem.getAccelerator());
        assertNull(saveItem.getIcon());

        // test case for menu item without label suffix, shortcut, and icon but with a separator
        String separator = "----";
        JMenuItem separatorItem = menu.createMenuItem(separator);
        assertNull(separatorItem.getText());
        assertNull(separatorItem.getAccelerator());
        assertNull(separatorItem.getIcon());
        assertTrue(separatorItem.isSeparator());
    }

    @Test
    public void testAddShortcutAndIcon() {
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu menu = new MainMenu(mediator);

        String key = "Save";
        JMenuItem saveItem = new JMenuItem(key);
        menu.addShortcutAndIcon(saveItem, key);

        String shortcut = "Ctrl S";
        saveItem.setAccelerator(KeyStroke.getKeyStroke(shortcut));
        assertArrayEquals(new byte[]{KeyEvent.VK_S, KeyEvent.VK_CONTROL}, saveItem.getAccelerator().getKeyCodePoint());

        String iconPath = "icons/save.png";
        URL url = new URL("file:" + iconPath);
        saveItem.setIcon(new ImageIcon(url));
        assertNotNull(saveItem.getIcon().getImage());
    }

    @Test
    public void testMissingResourceException() {
        // This test case assumes that there are missing resource strings in the UIResources.properties file
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu menu = new MainMenu(mediator);
        try {
            menu.createMenu("MissingMenu");
            fail("Expected MissingResourceException");
        } catch(MissingResourceException e) {
            // Expected exception
        }
    }
}