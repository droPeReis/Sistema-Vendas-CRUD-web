package com.cavaleirosDaNoite.demo.Dominio.Repositorios;

import com.cavaleirosDaNoite.demo.Persistencia.dao_interfaces.IRepClientes;
import com.cavaleirosDaNoite.demo.Dominio.Entidades.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RepClientes {
    private final IRepClientes clienteRepository;

    // Injeção de dependência do IRepClientes

    @Autowired
    public RepClientes(IRepClientes clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // Adicione outras consultas conforme necessário


    public Optional<Cliente> findById(long id) {  // Procurar pela Id
        return clienteRepository.findById(id);
    }

    public List<Cliente> findAll() { //  Listar todos

        return (List<Cliente>) clienteRepository.findAll();
    }
    public void deleteById(long id) {
        clienteRepository.deleteById(id);
    }

    public Cliente save(Cliente cliente) { return clienteRepository.save(cliente);
    }

    // Implemente outros métodos de consulta, se necessário
}
