package br.com.sistemas.chamados.repository;

import br.com.sistemas.chamados.entity.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository repository;

    @Test
    @DisplayName("Deve criar um cliente com sucesso")
    public void deveCriarClienteComSucesso() {
        Cliente cliente = new Cliente();
        cliente.setNome("Empresa Teste");
        cliente.setEmail("teste@empresa.com");

        Cliente salvo = repository.save(cliente);

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getEmail()).isEqualTo("teste@empresa.com");
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar salvar e-mail duplicado")
    public void deveLancarExcecaoAoSalvarEmailDuplicado() {
        Cliente cliente1 = new Cliente();
        cliente1.setNome("Empresa 1");
        cliente1.setEmail("unico@email.com");
        repository.save(cliente1);

        Cliente cliente2 = new Cliente();
        cliente2.setNome("Empresa 2");
        cliente2.setEmail("unico@email.com");

        assertThrows(DataIntegrityViolationException.class, () ->
                repository.saveAndFlush(cliente2)
        );
    }
}