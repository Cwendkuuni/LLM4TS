import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches("password", "$2a$10$p8hVwNkk5XVZoYKlJEZ9HehSUXU6VuWvTlDzO8aT0/BkFK/cRxmCu"));
    }

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        when(sensitivePropertyHandler.decrypt(ArgumentMatchers.anyString())).thenReturn("decrypted");

        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());

        ObjectMapper mapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));

        assertEquals("decrypted", mapper.convertValue("encrypted", String.class));
    }

    @Test
    public void testHttpFirewall() {
        DefaultHttpFirewall firewall = new DefaultHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        assertTrue(firewall.getAllowedHttpMethods().contains("GET"));
        assertTrue(firewall.getAllowedUrlEncodedSlash());
    }

    @Test
    public void testNullCloudMetadataClient() {
        CloudMetadataClient client = new BeanConfiguration.NullCloudMetadataClient();

        assertFalse(client.supportsCloudMetadata());
        assertFalse(client.supportsTenantInstance());
        assertFalse(client.needsOBTenantName());
        assertFalse(client.needsSysTenantUser());
        assertTrue(client.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, client.oceanbaseAccessMode());

        assertThrows(UnsupportedException.class, () -> client.listInstances());
        assertThrows(UnsupportedException.class, () -> client.listTenants("instanceId"));
        assertThrows(UnsupportedException.class, () -> client.getTenant("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> client.listDatabaseUsers("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> client.getTenantEndpoint("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> client.getSysTenantUser("instanceId"));
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer serializer = new BeanConfiguration().cookieSerializer();
        assertEquals("JSESSIONID", serializer.getCookieName());
    }

}