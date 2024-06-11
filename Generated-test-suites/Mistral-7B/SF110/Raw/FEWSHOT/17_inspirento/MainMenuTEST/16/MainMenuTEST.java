package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.awt.Image;
import java.io.File;
import java.net.URL;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.KeyStroke;

import org.junit.Test;

public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        InspirentoMediator mediator = mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        String[] menus = InspirentoUtilities.tokenize(UIResources.getString("menu"));
        for (String menu : menus) {
            JMenu jMenu = menu.createMenu(menu);
            assertNotNull(jMenu);
        }
    }

    @Test
    public void testCreateMenuWithSubmenus() {
        InspirentoMediator mediator = mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        String menuName = "MenuWithSubmenu";
        String submenuName = "Submenu";

        JMenu parentMenu = menu.createMenu(menuName);
        JMenu submenu = menu.createMenu(submenuName);

        parentMenu.add(submenu);
        assertEquals(1, parentMenu.getMenuComponentCount());
    }

    @Test
    public void testCreateMenuWithCheckboxMenuItem() {
        InspirentoMediator mediator = mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        String menuName = "MenuWithCheckboxMenuItem";
        String checkboxMenuItemName = "CheckboxMenuItem";

        JMenu jMenu = menu.createMenu(menuName);
        JCheckBoxMenuItem jMenuItem = (JCheckBoxMenuItem) jMenu.getMenuComponent(0);
        assertNotNull(jMenuItem);
        assertTrue(jMenuItem.isCheckable());
    }

    @Test
    public void testCreateMenuWithJMenuItem() {
        InspirentoMediator mediator = mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        String menuName = "MenuWithJMenuItem";
        String itemName = "Item";

        JMenu jMenu = menu.createMenu(menuName);
        JMenuItem jMenuItem = (JMenuItem) jMenu.getMenuComponent(0);
        assertNotNull(jMenuItem);
    }

    @Test
    public void testCreateMenuWithSeparator() {
        InspirentoMediator mediator = mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        String menuName = "MenuWithSeparator";

        JMenu jMenu = menu.createMenu(menuName);
        assertEquals(1, jMenu.getMenuComponentCount());
        assertTrue(jMenu.getMenuComponent(0) instanceof JSeparator);
    }

    @Test
    public void testAddShortcutAndIcon() {
        InspirentoMediator mediator = mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        String key = "testKey";
        JMenuItem item = new JMenuItem();

        menu.addShortcutAndIcon(item, key);

        assertNull(item.getAccelerator());
        assertNull(item.getIcon());

        String shortcut = UIResources.getString(key + "Shortcut");
        String iconPath = UIResources.getString(key + "Icon");

        menu.addShortcutAndIcon(item, key);

        assertNotNull(item.getAccelerator());
        assertEquals(KeyStroke.getKeyStroke(shortcut), item.getAccelerator());

        try {
            URL url = new File("icon.png").toURI().toURL();
            String expectedIconPath = "path/to/icon.png";
            if (iconPath != null && iconPath.equals(expectedIconPath)) {
                Image icon = new ImageIcon(url).getImage();
                assertEquals(icon.getWidth(null), item.getIcon().getImage().getWidth(null));
                assertEquals(icon.getHeight(null), item.getIcon().getImage().getHeight(null));
            }
        } catch (Exception e) {
            // Ignore
        }
    }
}