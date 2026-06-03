package com.spartangym.clientes.service;

import com.spartangym.clientes.model.Cliente;
import com.spartangym.clientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Cliente> listarTodos() {
        logger.info("Listando todos los clientes");
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Integer id) {
        logger.info("Buscando cliente con id {}", id);

        return clienteRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Cliente con id {} no encontrado", id);
                    return new RuntimeException("Cliente con id " + id + " no encontrado.");
                });
    }

    public Cliente guardarCliente(Cliente cliente) {
        logger.info("Iniciando registro de cliente con RUT {}", cliente.getRut());

        String passwordEncriptado = passwordEncoder.encode(cliente.getPassword());
        cliente.setPassword(passwordEncriptado);

        Cliente clienteGuardado = clienteRepository.save(cliente);

        logger.info("Cliente registrado correctamente con id {}", clienteGuardado.getIdCliente());

        return clienteGuardado;
    }

    public Cliente actualizar(Integer id, Cliente clienteActualizado) {
        logger.info("Iniciando actualizacion de cliente con id {}", id);

        Cliente existente = buscarPorId(id);

        existente.setNombreCompleto(clienteActualizado.getNombreCompleto());
        existente.setEmail(clienteActualizado.getEmail());
        existente.setTelefono(clienteActualizado.getTelefono());
        existente.setEstado(clienteActualizado.getEstado());
        existente.setRol(clienteActualizado.getRol());

        if (clienteActualizado.getPassword() != null && !clienteActualizado.getPassword().isBlank()) {
            existente.setPassword(passwordEncoder.encode(clienteActualizado.getPassword()));
            logger.info("Password actualizado para cliente con id {}", id);
        }

        Cliente clienteGuardado = clienteRepository.save(existente);

        logger.info("Cliente actualizado correctamente con id {}", clienteGuardado.getIdCliente());

        return clienteGuardado;
    }

    public void eliminar(Integer id) {
        logger.info("Iniciando eliminacion de cliente con id {}", id);

        buscarPorId(id);
        clienteRepository.deleteById(id);

        logger.info("Cliente eliminado correctamente con id {}", id);
    }
}