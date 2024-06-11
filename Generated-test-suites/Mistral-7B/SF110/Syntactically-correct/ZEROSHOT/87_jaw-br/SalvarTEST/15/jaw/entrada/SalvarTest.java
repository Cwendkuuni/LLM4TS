package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Salvar;
import jaw.gui.ProcessarEntidades;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SalvarTest {

 private ProcessarEntidades form;
 private Salvar salvar;
 private File selectedFile;
 private List<Entidade> entidades;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 entidades = new ArrayList<Entidade>();
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 }

 @Test
 public void testSalvarXmlWithValidFile() {
 // Arrange
 Entidade entidade1 = new Entidade("Test1", "Test Type1", "Test Description1");
 entidade1.setId(1);
 Entidade entidade2 = new Entidade("Test2", "Test Type2", "Test Description2");
 entidade2.setId(2);
 entidades.add(entidade1);
 entidades.add(entidade2);
 Salvar.Filtro2 xmlFilter = new Salvar.Filtro2();
 selectedFile = new File("test.xml");

 // Act
 salvar.setEntidade(entidades);
 salvar.setArquivo(selectedFile.getAbsolutePath());
 salvar.salvar();

 // Assert
 assertTrue(selectedFile.exists());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarXmlWithNullEntidades() {
 // Act & Assert
 salvar.salvar();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarXmlWithNullArquivo() {
 // Act & Assert
 salvar.setEntidade(entidades);
 salvar.salvar();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarXmlWithInvalidFile() {
 // Arrange
 Salvar.Filtro2 xmlFilter = new Salvar.Filtro2();
 selectedFile = new File("test");

 // Act & Assert
 salvar.setEntidade(entidades);
 salvar.setArquivo(selectedFile.getAbsolutePath());
 salvar.salvar();
 }

 @Test
 public void testSalvarJawWithValidFile() {
 // Arrange
 Entidade entidade1 = new Entidade("Test1", "Test Type1", "Test Description1");
 entidade1.setId(1);
 Entidade entidade2 = new Entidade("Test2", "Test Type2", "Test Description2");
 entidade2.setId(2);
 entidades.add(entidade1);
 entidades.add(entidade2);
 Salvar.Filtro jawFilter = new Salvar.Filtro();
 selectedFile = new File("test.jaw");

 // Act
 salvar.setEntidade(entidades);
 salvar.setArquivo(selectedFile.getAbsolutePath());
 salvar.salvar();

 // Assert
 assertTrue(selectedFile.exists());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarJawWithNullEntidades() {
 // Act & Assert
 salvar.salvar();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarJawWithNullArquivo() {
 // Act & Assert
 salvar.salvar();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarJawWithInvalidFile() {
 // Arrange
 Salvar.Filtro jawFilter = new Salvar.Filtro();
 selectedFile = new File("test");

 // Act & Assert
 salvar.setEntidade(entidades);
 salvar.setArquivo(selectedFile.getAbsolutePath());
 salvar.salvar();
 }

 @Test
 public void testSalvarJawWithValidFileAndXml() {
 // Arrange
 Entidade entidade1 = new Entidade("Test1", "Test Type1", "Test Description1");
 entidade1.setId(1);
 Entidade entidade2 = new Entidade("Test2", "Test Type2", "Test Description2");
 entidade2.setId(2);
 entidades.add(entidade1);
 entidades.add(entidade2);
 Salvar.Filtro jawFilter = new Salvar.Filtro();
 selectedFile = new File("test.jaw");

 // Act
 salvar.setEntidade(entidades);
 salvar.setArquivo(selectedFile.getAbsolutePath());
 salvar.salvar();

 // Assert
 assertFalse(selectedFile.exists()); // Jaw file should not exist because we are testing XML export
 File xmlFile = new File(selectedFile.getParentFile(), selectedFile.getName() + ".xml");
 assertTrue(xmlFile.exists());
 }

 @After
 public void tearDown() {
 if (selectedFile != null && selectedFile.exists()) {
 selectedFile.delete();
 }
 }
}