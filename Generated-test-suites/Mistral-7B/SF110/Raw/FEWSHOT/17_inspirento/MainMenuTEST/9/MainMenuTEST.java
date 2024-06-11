package com.allenstudio.ir.ui;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu menu = new MainMenu(mediator);
        String[] menus = InspirentoUtilities.tokenize(UIResources.getString("menu"));

        for (String menu : menus) {
            JMenu jMenu = menu.createMenu(menu);
            assertNotNull(jMenu);
        }
    }

    @Test
    public void testAddShortcutAndIcon() {
        String key = "testKey";
        JMenuItem item = new JMenuItem();
        MainMenu menu = new MainMenu(null);

        menu.addShortcutAndIcon(item, key);

        assertNull(item.getAccelerator());
        assertNull(item.getIcon());

        String shortcut = UIResources.getString(key + "Shortcut");
        String iconPath = UIResources.getString(key + "Icon");

        menu.addShortcutAndIcon(item, key);

        assertNotNull(item.getAccelerator());
        assertEquals(KeyStroke.getKeyStroke(shortcut), item.getAccelerator());

        try {
            URL url = menu.getClass().getResource(iconPath);
            assertNotNull(url);
            Image icon = Toolkit.getDefaultToolkit().getImage(url);
            assertNotNull(icon);
            item.setIcon(new ImageIcon(icon));
        } catch (MissingResourceException e) {
            System.out.println("Cannot load icon file.");
        }

        assertNotNull(item.getIcon());
    }

    @Test
    public void testCreateMenuWithSubMenusAndCheckBoxItems() {
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu menu = new MainMenu(mediator);
        String[] menus = InspirentoUtilities.tokenize(UIResources.getString("menu"));

        for (String menu : menus) {
            JMenu jMenu = menu.createMenu(menu);
            assertNotNull(jMenu);

            if (menu.contains("submenu")) {
                JMenu subMenu = jMenu.findJsItem(menu + "Label" + " > " + "submenu" + "Label")
                        .getType(JMenu.class);
                assertNotNull(subMenu);
            }

            if (menu.contains("checkBoxItem")) {
                JCheckBoxMenuItem jMenuItem = (JCheckBoxMenuItem) jMenu.findJsItem(menu + "Label" + " > " + "checkBoxItem" + "Label")
                        .getType(JCheckBoxMenuItem.class);
                assertNotNull(jMenuItem);
            }
        }
    }
}