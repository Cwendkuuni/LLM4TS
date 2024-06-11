package jaw.entrada;

import org.junit.Assert;
import org.junit.Test;

public class AbrirTest {
@Test
public void testAbrirWithXmlFile() {
ProcessarEntidades form = new ProcessarEntidades();
Abrir abrir = new Abrir(form, "test.jaw.xml");
Assert.assertNotNull(abrir.xml);
}

@Test
public void testOpenValidXmlFile() {
ProcessarEntidades form = new ProcessarEntidades();
Abrir abrir = new Abrir(form, "test.jaw.xml");
Assert.assertNotNull(abrir.xml);
// Add more assertions to check the state of the form and entidades
}

@Test(expected = FileNotFoundException.class)
public void testOpenInvalidXmlFile() {
ProcessarEntidades form = new ProcessarEntidades();
Abrir abrir = new Abrir(form, "non_existent_file.jaw.xml");
}

@Test(expected = FileNotFoundException.class)
public void testOpenInvalidSerializedFile() {
ProcessarEntidades form = new ProcessarEntidades();
Abrir abrir = new Abrir(form, "non_existent_file.jaw");
}

@Test(expected = Exception.class)
public void testOpenInvalidXmlFileFormat() {
ProcessarEntidades form = new ProcessarEntidades();
Abrir abrir = new Abrir(form, "test.invalid_format.xml");
}

@Test(expected = Exception.class)
public void testOpenInvalidSerializedFileFormat() {
ProcessarEntidades form = new ProcessarEntidades();
Abrir abrir = new Abrir(form, "test.invalid_format.jaw");
}
}