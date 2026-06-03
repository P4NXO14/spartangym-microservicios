package com.spartangym.clientes.DataLoader;

import com.spartangym.clientes.model.Cliente;
import com.spartangym.clientes.model.Rol;
import com.spartangym.clientes.repository.ClienteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ClienteDataLoader {

    @Bean
    CommandLineRunner initClientes(ClienteRepository clienteRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (clienteRepository.count() == 0) {

                clienteRepository.save(new Cliente(
                    null,
                    "11.111.111-1",
                    "Administrador Spartan",
                    "admin@spartangym.com",
                    passwordEncoder.encode("admin123"),
                    "+56911111111",
                    "Activo",
                    Rol.ADMIN));

                clienteRepository.save(new Cliente(
                    null,
                    "22.222.222-2",
                    "Recepcionista Spartan",
                    "recepcion@spartangym.com",
                    passwordEncoder.encode("recepcion123"),
                    "+56922222222",
                    "Activo",
                    Rol.RECEPCIONISTA));

                clienteRepository.save(new Cliente(
                    null,
                    "33.333.333-3",
                    "Carlos Soto",
                    "carlos@spartangym.com",
                    passwordEncoder.encode("cliente123"),
                    "+56933333333",
                    "Activo",
                    Rol.CLIENTE));
                    
                clienteRepository.save(new Cliente(
                    null,
                    "44.444.444-4",
                    "Fernanda Soto",
                    "fernanda@spartangym.com",
                    passwordEncoder.encode("cliente123"),
                    "+56944444444",
                    "Activo",
                    Rol.CLIENTE));

                clienteRepository.save(new Cliente(
                    null,
                    "55.555.555-5",
                    "Pedro Inactivo",
                    "pedro@spartangym.com",
                    passwordEncoder.encode("cliente123"),
                    "+56955555555",
                    "Inactivo",
                    Rol.CLIENTE));
            }
        };
    }
}