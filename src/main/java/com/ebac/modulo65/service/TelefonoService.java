package com.ebac.modulo65.service;

import com.ebac.modulo65.dto.Telefono;
import com.ebac.modulo65.repository.TelefonoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TelefonoService {

    @Autowired
    TelefonoRepository telefonoRepository;

    public Telefono crearTelefono(Telefono telefono) throws Exception {
        if (telefono.getNumero().length() <= 15) {
            return telefonoRepository.save(telefono);
        }
        throw new Exception("Telefono invalido");
    }

    public Optional<Telefono> obtenerTelefonoPorId(Long idTelefono) {
        return telefonoRepository.findById(idTelefono);
    }

    public List<Telefono> obtenerTelefonos() {
        return telefonoRepository.findAll();
    }

    public void actualizarTelefono(Telefono telefono) {
        telefonoRepository.save(telefono);
    }

    public void eliminarTelefono(Long id) {
        telefonoRepository.deleteById(id);
    }
}
