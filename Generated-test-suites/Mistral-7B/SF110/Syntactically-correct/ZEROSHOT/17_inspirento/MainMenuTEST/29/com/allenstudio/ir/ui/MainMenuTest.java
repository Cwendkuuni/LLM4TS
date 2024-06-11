package com.allenstudio.ir.ui;

import static org.junit.Assert.*;

import com.allenstudio.ir.core.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class MainMenuTest {

    @Test
    public void testMainMenuConstruction() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        assertNotNull(menu);
        assertNotNull(menu.mediator);
        assertEquals(menu.mediator, mediator);
    }

    @Test
    public void testCreateMenu() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        String[] menus = InspirentoUtilities.tokenize(UIResources.getString("menu"));
        for (String menu : menus) {
            JMenu createdMenu = menu.createMenu(menu);
            assertNotNull(createdMenu);
        }
    }

    @Test
    public void testAddShortcutAndIcon() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        String key = "TestKey";
        JMenuItem item = new JMenuItem();

        String label = "TestLabel";
        MenuItemTestUtil.setMenuItemLabel(item, label);

        String iconPath = "TestIcon.png";
        URL url = new File("TestIcon.png").toURI().toURL();

        menu.addShortcutAndIcon(item, key);

        assertEquals(KeyEvent.VK_T, item.getAccelerator().getKeyCode());
        assertEquals(KeyEvent.CTRL_MASK, item.getAccelerator().getModifiers());
        assertEquals(iconPath, item.getIcon().toString().substring(7));
    }

    @Test
    public void testCreateMenuWithNestedMenu() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        String menuName = "Test Nested Menu";
        JMenu nestedMenu = menu.createMenu(menuName + ">");
        assertNotNull(nestedMenu);
    }

    @Test(expected = MissingResourceException.class)
    public void testCreateMenuWithMissingLabel() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        String menuName = "Test Missing Label";
        JMenu nestedMenu = menu.createMenu(menuName);
        assertNull(nestedMenu);
    }

    @Test
    public void testCreateMenuWithCheckboxMenuItem() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        String key = "TestCheckboxMenuItemKey";
        String label = "TestCheckboxMenuItemLabel";

        JCheckBoxMenuItem checkboxMenuItem = new JCheckBoxMenuItem();
        MenuItemTestUtil.setMenuItemLabel(checkboxMenuItem, label);

        menu.addShortcutAndIcon(checkboxMenuItem, key);

        assertNull(checkboxMenuItem.getAccelerator());
        assertNull(checkboxMenuItem.getIcon());
    }
}

class MenuItemTestUtil {
    static void setMenuItemLabel(JMenuItem item, String label) {
        JLabel labelComponent = new JLabel(label);
        item.add(labelComponent);
    }
}