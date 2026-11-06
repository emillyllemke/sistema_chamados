package br.com.sistemas.chamados.controller;

import br.com.sistemas.chamados.dto.ClienteResponse;
import br.com.sistemas.chamados.service.ClienteService;
import br.com.sistemas.chamados.service.ChamadoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import br.com.sistemas.chamados.exception.RecursoNaoEncontradoException;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService service;

    @MockitoBean
    private ChamadoService chamadoService;

    @Test
    @DisplayName("Deve retornar 201 Created na criação com dados válidos")
    public void deveRetornar201AoCriarCliente() throws Exception {
        ClienteResponse response = new ClienteResponse(1L, "Empresa Teste", "teste@empresa.com", null);
        Mockito.when(service.criar(any())).thenReturn(response);

        String jsonValido = "{\"nome\": \"Empresa Teste\", \"email\": \"teste@empresa.com\"}";

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonValido))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request ao falhar validação")
    public void deveRetornar400AoFalharValidacao() throws Exception {
        String jsonInvalido = "{\"email\": \"teste@empresa.com\"}";

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("Deve retornar 404 Not Found ao procurar ID inexistente")
    public void deveRetornar404AoBuscarIdInexistente() throws Exception {
        Mockito.when(service.buscar(eq(99L)))
                .thenThrow(new RecursoNaoEncontradoException(
                        "Cliente 99 não encontrado"));

        mockMvc.perform(get("/clientes/99"))
                .andExpect(status().isNotFound());
    }
}