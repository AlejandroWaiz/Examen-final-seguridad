package cl.duoc.animals.api.controller;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.duoc.animals.api.entity.Pet;
import cl.duoc.animals.api.repository.PetRepository;

@ExtendWith(MockitoExtension.class)
class PetControllerStandaloneTest {

    @InjectMocks
    private PetController controller;

    @Mock
    private PetRepository repo;

    private MockMvc mvc;
    private ObjectMapper om;

    @BeforeEach
    void setup() {
        om = new ObjectMapper();
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(om))
                .build();
    }

    private static Pet pet(Long id, String name, String species, int age, boolean adopted) {
        Pet p = new Pet();
        p.setId(id);
        p.setName(name);
        p.setSpecies(species);
        p.setAge(age);
        p.setAdopted(adopted);
        return p;
    }

    @Test
    void list_ok() throws Exception {
        when(repo.findAll()).thenReturn(List.of(
                pet(1L, "Luna", "cat", 2, false),
                pet(2L, "Rocky", "dog", 4, true)
        ));

        mvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is("Luna")))
                .andExpect(jsonPath("$[1].adopted", is(true)));

        verify(repo).findAll();
    }

    @Test
    void create_ok() throws Exception {
        Pet body = pet(null, "Niebla", "cat", 1, false);
        Pet saved = pet(10L, "Niebla", "cat", 1, false);

        when(repo.save(any(Pet.class))).thenReturn(saved);

        mvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.name", is("Niebla")));

        verify(repo).save(any(Pet.class));
    }

    @Test
    void update_ok_found() throws Exception {
        Long id = 5L;
        Pet db = pet(id, "Old", "dog", 3, false);
        Pet payload = pet(null, "NewName", "dog", 5, true);
        Pet updated = pet(id, "NewName", "dog", 5, true);

        when(repo.findById(id)).thenReturn(Optional.of(db));
        when(repo.save(any(Pet.class))).thenReturn(updated);

        mvc.perform(put("/api/pets/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.name", is("NewName")))
                .andExpect(jsonPath("$.adopted", is(true)));

        verify(repo).findById(id);
        verify(repo).save(any(Pet.class));
    }

    @Test
    void update_not_found_404() throws Exception {
        Long id = 123L;
        Pet payload = pet(null, "Ghost", "cat", 2, false);

        when(repo.findById(id)).thenReturn(Optional.empty());

        mvc.perform(put("/api/pets/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(payload)))
                .andExpect(status().isNotFound());

        verify(repo).findById(id);
        verify(repo, never()).save(any());
    }

    @Test
    void delete_ok_noContent() throws Exception {
        Long id = 7L;
        when(repo.existsById(id)).thenReturn(true);
        doNothing().when(repo).deleteById(id);

        mvc.perform(delete("/api/pets/{id}", id))
                .andExpect(status().isNoContent());

        verify(repo).existsById(id);
        verify(repo).deleteById(id);
    }

    @Test
    void delete_not_found_404() throws Exception {
        Long id = 999L;
        when(repo.existsById(id)).thenReturn(false);

        mvc.perform(delete("/api/pets/{id}", id))
                .andExpect(status().isNotFound());

        verify(repo).existsById(id);
        verify(repo, never()).deleteById(anyLong());
    }

    @Test
    void list_empty_returns200_andEmptyArray() throws Exception {
        when(repo.findAll()).thenReturn(List.of());

        mvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));

        verify(repo).findAll();
    }

    @Test
    void create_ok_echoes_fields() throws Exception {
        Pet body = pet(null, "Toby", "dog", 3, true);
        Pet saved = pet(11L, "Toby", "dog", 3, true);
        when(repo.save(any(Pet.class))).thenReturn(saved);

        mvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(11)))
                .andExpect(jsonPath("$.name", is("Toby")))
                .andExpect(jsonPath("$.species", is("dog")))
                .andExpect(jsonPath("$.age", is(3)))
                .andExpect(jsonPath("$.adopted", is(true)));

        verify(repo).save(any(Pet.class));
    }

}
