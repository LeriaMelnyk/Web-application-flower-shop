package floristry.shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class AccessTests {

    private static final String FLOWERS = "/api/v1/flowers";
    private static final String AUDIT = "/api/v1/audit";

    private static final String NEW_FLOWER = """
            {
              "name": "Test Daisy",
              "category": "DAISY",
              "price": 1.2,
              "stock": 50,
              "description": "Created by access test"
            }
            """;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    // 1. Анонім не має доступу -> 401
    @Test
    @WithAnonymousUser
    void whenAnonymousThenStatusUnauthorized() throws Exception {
        var result = mockMvc.perform(get(FLOWERS));
        var response = result.andReturn().getResponse();
        assertEquals(401, response.getStatus());
        assertEquals("Unauthorized", response.getErrorMessage());
    }

    // 2. USER може читати квіти -> 200
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void whenUserReadsFlowersThenStatusOk() throws Exception {
        var result = mockMvc.perform(get(FLOWERS));
        assertEquals(200, result.andReturn().getResponse().getStatus());
    }

    // 3. USER бачить свій hello -> 200 + текст
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void whenUserHelloUserThenStatusOkAndText() throws Exception {
        var result = mockMvc.perform(get(FLOWERS + "/hello/user"));
        var response = result.andReturn().getResponse();
        assertEquals(200, response.getStatus());
        assertEquals("Welcome to the flower shop, dear customer! Browse our bouquets.",
                response.getContentAsString());
    }

    // 4. USER не може створювати квіти -> 403
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void whenUserCreatesFlowerThenStatusForbidden() throws Exception {
        var result = mockMvc.perform(post(FLOWERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(NEW_FLOWER));
        var response = result.andReturn().getResponse();
        assertEquals(403, response.getStatus());
        assertEquals("Forbidden", response.getErrorMessage());
    }

    // 5. ADMIN бачить свій hello -> 200 + текст
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void whenAdminHelloAdminThenStatusOkAndText() throws Exception {
        var result = mockMvc.perform(get(FLOWERS + "/hello/admin"));
        var response = result.andReturn().getResponse();
        assertEquals(200, response.getStatus());
        assertEquals("Hello, Admin! Fresh roses just arrived, time to update the stock.",
                response.getContentAsString());
    }

    // 6. ADMIN на hello для USER -> 403 (ієрархії ролей немає)
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void whenAdminHelloUserThenStatusForbidden() throws Exception {
        var result = mockMvc.perform(get(FLOWERS + "/hello/user"));
        var response = result.andReturn().getResponse();
        assertEquals(403, response.getStatus());
        assertEquals("Forbidden", response.getErrorMessage());
    }

    // 7. ADMIN може створювати квіти -> 201
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void whenAdminCreatesFlowerThenStatusCreated() throws Exception {
        var result = mockMvc.perform(post(FLOWERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(NEW_FLOWER));
        assertEquals(201, result.andReturn().getResponse().getStatus());
    }

    // 8. ADMIN не може видаляти (@Secured SUPERADMIN) -> 403
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void whenAdminDeletesFlowerThenStatusForbidden() throws Exception {
        var result = mockMvc.perform(delete(FLOWERS + "/any-id"));
        var response = result.andReturn().getResponse();
        assertEquals(403, response.getStatus());
        assertEquals("Forbidden", response.getErrorMessage());
    }

    // 9. SUPERADMIN бачить свій hello (@Secured) -> 200 + текст
    @Test
    @WithMockUser(username = "superadmin", roles = {"SUPERADMIN"})
    void whenSuperadminHelloSuperadminThenStatusOkAndText() throws Exception {
        var result = mockMvc.perform(get(FLOWERS + "/hello/superadmin"));
        var response = result.andReturn().getResponse();
        assertEquals(200, response.getStatus());
        assertEquals("Greetings, Superadmin! The whole garden is in your hands.",
                response.getContentAsString());
    }

    // 10. Аудит: SUPERADMIN -> 200
    @Test
    @WithMockUser(username = "superadmin", roles = {"SUPERADMIN"})
    void whenSuperadminReadsAuditThenStatusOk() throws Exception {
        var result = mockMvc.perform(get(AUDIT));
        assertEquals(200, result.andReturn().getResponse().getStatus());
    }
}