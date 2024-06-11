import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jpa.commons.junit.JpaDatabaseTestUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
        assertEquals(3, roleEntities.size());
        // add more assertions to check the content of roleEntities
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> roleEntity = roleRepository.findByNameAndOrganizationId("admin", 1L);
        assertTrue(roleEntity.isPresent());
        assertEquals("admin", roleEntity.get().getName());
        // add more assertions to check other fields
    }

    @Test
    @Transactional
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("updated_admin");
        int rowsAffected = roleRepository.update(roleEntity);
        assertEquals(1, rowsAffected);
        Optional<RoleEntity> updatedRoleEntity = roleRepository.findById(1L);
        assertTrue(updatedRoleEntity.isPresent());
        assertEquals("updated_admin", updatedRoleEntity.get().getName());
    }

    // add more test cases for other methods

}