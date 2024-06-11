package com.allenstudio.ir.ui;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import java.awt.KeyStroke;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.MissingResourceException;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.KeyStroke.KeyStrokeException;

public class MainMenuTest {

    @Test
    public void testMainMenuConstruction() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        assertNotNull(menu);
    }

    @Test
    public void testCreateMenu() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        String menuName = "ExampleMenu";
        JMenu createdMenu = menu.createMenu(menuName);
        assertNotNull(createdMenu);
    }

    @Test
    public void testAddShortcutAndIcon() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        String key = "exampleKey";
        menu.addShortcutAndIcon(item, key);
        assertNotNull(item.getAccelerator());
    }

    @Test(expected = NullPointerException.class)
    public void testMainMenuNullConstructor() {
        new MainMenu(null);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateMenuWithNullMenuName() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        String nullMenuName = null;
        JMenu createdMenu = menu.createMenu(nullMenuName);
    }

    @Test(expected = MissingResourceException.class)
    public void testCreateMenuWithMissingLabel() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        String missingLabel = "MissingLabel";
        JMenu createdMenu = menu.createMenu(missingLabel);
    }

    @Test(expected = MissingResourceException.class)
    public void testAddShortcutAndIconWithMissingIcon() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        String missingIconKey = "MissingIcon";
        menu.addShortcutAndIcon(item, missingIconKey);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMenuWithInvalidMenuName() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        String invalidMenuName = "123";
        JMenu createdMenu = menu.createMenu(invalidMenuName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddShortcutAndIconWithInvalidKey() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        String invalidKey = "invalidKey";
        menu.addShortcutAndIcon(item, invalidKey);
    }

    @Test(expected = StackOverflowError.class)
    public void testCreateNestedMenusTooDeep() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        int deepNestingLimit = 1000;
        for (int i = 0; i < deepNestingLimit; i++) {
            JMenu nestedMenu = menu.createMenu("NestedMenu" + i);
        }
    }
}