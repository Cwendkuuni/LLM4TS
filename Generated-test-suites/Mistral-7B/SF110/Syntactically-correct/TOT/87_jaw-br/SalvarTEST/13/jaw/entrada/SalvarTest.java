import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import jaw.entrada.Salvar;
import java.util.ArrayList;
import java.util.List;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades form;

 @Before
 public void setup() {
 form = new ProcessarEntidades();
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 }

 @Test
 public void testSalvarXmlFile() {
 // Prepare
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade("Nome1", "Descricao1", 1);
 Entidade entidade2 = new Entidade("Nome2", "Descricao2", 2);
 entidades.add(entidade1);
 entidades.add(entidade2);

 // Action
 salvar.entidade = entidades;

 // Assert
 String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<entidades>\n<entidade>\n<nome>Nome1</nome>\n<descricao>Descricao1</descricao>\n<id>1</id>\n</entidade>\n<entidade>\n<nome>Nome2</nome>\n<descricao>Descricao2</descricao>\n<id>2</id>\n</entidade>\n</entidades>";
 String actualXml = salvar.xml;
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testSalvarNullFile() {
 // Prepare
 salvar = new Salvar(form, Salvar.SALVAR_XML);

 // Action
 salvar.vela = true;

 // Assert
 assertNull(salvar.arquivo);
 }

 @Test
 public void testSalvarInvalidFileExtension() {
 // Prepare
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 File file = new File("path/to/invalidfile.txt");

 // Action
 salvar.arquivo = file.getAbsolutePath();
 salvar.vela = true;

 // Assert
 assertFalse(salvar.vela);
 }

 @Test
 public void testSalvarValidFile() {
 // Prepare
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 File file = new File("path/to/validfile.xml");

 // Action
 salvar.arquivo = file.getAbsolutePath();
 salvar.vela = true;

 // Assert
 assertTrue(salvar.vela);
 }

 @Test
 public void testSerializacaoXmlException() {
 // Prepare
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 Salvar salvarSpy = spy(salvar);
 doThrow(IOException.class).when(salvarSpy).salvar(anyList(), anyString());

 // Action
 Salvar.SerializacaoXML serializacaoXml = new Salvar.SerializacaoXML();
 String xml = serializacaoXml.entidades2XML(entidades);
 File file = new File("path/to/file.xml");
 salvarSpy.salvar(entidades, file.getAbsolutePath());

 // Assert
 assertFalse(salvarSpy.vela);
 }
}