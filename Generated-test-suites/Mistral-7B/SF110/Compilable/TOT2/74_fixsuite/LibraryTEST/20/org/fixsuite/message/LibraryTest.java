package org.fixsuite.message;
import org.junit.Test;
import static org.junit.Assert.*;

public class LibraryTest {
    @Test
    public void testLibraryConstructor() {
        Library library = new Library();
        assertNotNull(library);
    }
}