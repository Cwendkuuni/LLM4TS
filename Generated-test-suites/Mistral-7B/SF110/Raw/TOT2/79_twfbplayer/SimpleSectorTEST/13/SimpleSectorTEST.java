package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;

public class SimpleSectorTest {

    static SimpleSector sector;
    static CharacterClass testClass = new CharacterClass("TestClass");

    @BeforeClass static void setupClass() {
        sector = new SimpleSector(10, true, true, 5, 5, false, 5, testClass);
    }

    // Test cases for other methods go here
}