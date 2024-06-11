package com.allenstudio.ir.ui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.ui.MainMenu;

public class MainMenuTest {
    private MainMenu mainMenu;
    private InspirentoMediator mediator;

    @Before
    public void init() {
        mediator = new InspirentoMediator();
        mainMenu = new MainMenu(mediator);
    }

    @Test
    public void testCreateMenu() {
        String menuString = UIResources.getString("menu");
        String[] expectedMenus = InspirentoUtilities.tokenize(menuString);

        String[] menus = mainMenu.getComponentsOfType(JMenu.class);

        assertEquals(expectedMenus.length, menus.length);
        for (int i = 0; i < expectedMenus.length; i++) {
            assertEquals(expectedMenus[i], menus[i].getText());
            JMenu jMenu = (JMenu) menus[i];
            assertEquals(1, jMenu.getComponentCount());
            JMenu subMenu = null;
            if (jMenu.getText().charAt(jMenu.getText().length() - 1) == '>') {
                subMenu = (JMenu) jMenu.getComponent(0);
            }
            assertNull(subMenu);
        }
    }

    @Test
    public void testCreateMenuItem() {
        String menuString = UIResources.getString("menu");
        String[] menus = mainMenu.getComponentsOfType(JMenu.class);
        JMenu menu = (JMenu) menus[0];

        String itemString = UIResources.getString("menu1.item1");
        JMenuItem item = (JMenuItem) menu.getComponent(0);

        assertEquals("Item 1", item.getText());
        assertNull(item.getIcon());
        assertNull(item.getAccelerator());

        String label = UIResources.getString("menu1.item1Label");
        assertEquals(label, item.getText());
        assertEquals(label.charAt(label.length() - 1), item.getMnemonic());

        String shortcut = UIResources.getString("menu1.item1Shortcut");
        item.setAccelerator(KeyStroke.getKeyStroke(shortcut));
        assertEquals(shortcut, item.getAccelerator().toString());

        String iconPath = UIResources.getString("menu1.item1Icon");
        UIManager.put("Menu.background", new Color(255, 255, 255));
        mainMenu.addShortcutAndIcon(item, "menu1.item1");
        assertEquals(iconPath, item.getIcon().getDescription(null).toString());
    }

    @Test
    public void testCreateCheckboxMenuItem() {
        String menuString = UIResources.getString("menu");
        String[] menus = mainMenu.getComponentsOfType(JMenu.class);
        JMenu menu = (JMenu) menus[1];

        String checkboxMenuItemString = UIResources.getString("menu2.item1");
        JCheckBoxMenuItem checkboxMenuItem = (JCheckBoxMenuItem) menu.getComponent(0);

        assertEquals("Checkbox Item 1", checkboxMenuItem.getText());
        assertNull(checkboxMenuItem.getIcon());
        assertNull(checkboxMenuItem.getAccelerator());

        String label = UIResources.getString("menu2.item1Label");
        assertEquals(label, checkboxMenuItem.getText());
        assertEquals(label.charAt(label.length() - 1), checkboxMenuItem.getMnemonic());

        checkboxMenuItem.setSelected(true);
        assertTrue(checkboxMenuItem.isSelected());

        String shortcut = UIResources.getString("menu2.item1Shortcut");
        checkboxMenuItem.setAccelerator(KeyStroke.getKeyStroke(shortcut));
        assertEquals(shortcut, checkboxMenuItem.getAccelerator().toString());

        String iconPath = UIResources.getString("menu2.item1Icon");
        UIManager.put("Menu.background", new Color(255, 255, 255));
        mainMenu.addShortcutAndIcon(checkboxMenuItem, "menu2.item1");
        assertEquals(iconPath, checkboxMenuItem.getIcon().getDescription(null).toString());
    }
}