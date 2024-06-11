import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("test123");
        assertNotNull(encodedPassword);
    }

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);

        ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(new CustomOutputSerializer()
                        .addSerializer(Internationalizable.class, new I18nOutputSerializer())));

        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = new DefaultHttpFirewall();
        defaultHttpFirewall.setAllowUrlEncodedSlash(true);

        assertEquals(true, defaultHttpFirewall.getAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        NullCloudMetadataClient cloudMetadataClient = new NullCloudMetadataClient();

        assertEquals(false, cloudMetadataClient.supportsCloudMetadata());
        assertEquals(false, cloudMetadataClient.supportsTenantInstance());
        assertEquals(false, cloudMetadataClient.needsOBTenantName());
        assertEquals(false, cloudMetadataClient.needsSysTenantUser());
        assertEquals(true, cloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());

        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listInstances());
        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listTenants("instanceId"));
        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getTenant("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getSysTenantUser("instanceId"));
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("JSESSIONID");

        assertEquals("JSESSIONID", serializer.getCookieName());
    }

    static class CustomOutputSerializer extends JacksonModules.CustomOutputSerializer {

        @Override
        public void configureModule(com.fasterxml.jackson.databind.Module module) {
            // Nothing to configure
        }
    }

    static class NullCloudMetadataClient implements CloudMetadataClient {

        @Override
        public boolean supportsCloudMetadata() {
            return false;
        }

        @Override
        public boolean supportsTenantInstance() {
            return false;
        }

        @Override
        public boolean needsOBTenantName() {
            return false;
        }

        @Override
        public boolean needsSysTenantUser() {
            return false;
        }

        @Override
        public boolean includeClusterNameForJdbcUsername() {
            return true;
        }

        @Override
        public OceanBaseAccessMode oceanbaseAccessMode() {
            return OceanBaseAccessMode.DIRECT;
        }

        @Override
        public List<OBInstance> listInstances() {
            throw new UnsupportedException("CloudMetadata not supported");
        }

        @Override
        public List<OBTenant> listTenants(String instanceId) {
            throw new UnsupportedException("CloudMetadata not supported");
        }

        @Override
        public OBTenant getTenant(String instanceId, String tenantId) {
            throw new UnsupportedException("getTenant not supported in non-could");
        }

        @Override
        public List<OBDatabaseUser> listDatabaseUsers(String instanceId, String tenantId) {
            throw new UnsupportedException("CloudMetadata not supported");
        }

        @Override
        public OBTenantEndpoint getTenantEndpoint(String instanceId, String tenantId) {
            OBTenantEndpoint endpoint = new OBTenantEndpoint();
            endpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
            return endpoint;
        }

        @Override
        public OBDatabaseUser getSysTenantUser(String instanceId) {
            throw new UnsupportedException("CloudMetadata not supported");
        }
    }

    static class TestSensitivePropertyHandler implements SensitivePropertyHandler {

        @Override
        public String decrypt(String value) {
            return null;
        }
    }

    static class TestOBInstance {

    }

    static class TestOBTenant {

    }

    static class TestOBTenantEndpoint {

    }

    static class TestOBDatabaseUser {

    }
}