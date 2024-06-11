package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entidadades.Entidade;
import jaw.metamodelo.ProcessarEntidades;
import jaw.salvar.Salvar;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SalvarTest {

 private ProcessarEntidades form;
 private Salvar salvar;
 private List<Entidade> entidades;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 entidades = new ArrayList<>();
 entidades.add(new Entidade("Teste1", "Teste1", "Teste1"));
 entidades.add(new Entidade("Teste2", "Teste2", "Teste2"));
 form.setEntidades(entidades);
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 }

 @Test
 public void testSalvarXml() {
 File file = new File("test.jaw.xml");
 salvar.salvar(entidades, "test.jaw.xml");
 assertTrue(file.exists());

 try {
 FileWriter writer = new FileWriter("test.jaw.xml");
 String xml = salvar.getXml();
 writer.write(xml);
 writer.close();

 String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n<entidade>\n<nome>Teste1</nome>\n<descricao>Teste1</descricao>\n<ra>Teste1</ra>\n</entidade>\n<entidade>\n<nome>Teste2</nome>\n<descricao>Teste2</descricao>\n<ra>Teste2</ra>\n</entidade>\n</entidades>";
 assertEquals(expectedXml, xml);
 } catch (IOException e) {
 e.printStackTrace();
 fail("Failed to read file");
 }

 file.delete();
 }

 @Test
 public void testSalvarJaw() {
 File file = new File("test.jaw");
 salvar.salvar(entidades, "test.jaw");
 assertTrue(file.exists());

 file.delete();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarNullEntidades() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.salvar(null, "test.jaw.xml");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarNullArquivo() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.salvar(entidades, null);
 }
}