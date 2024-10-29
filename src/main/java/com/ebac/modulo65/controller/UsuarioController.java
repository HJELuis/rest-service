package com.ebac.modulo65.controller;

import com.ebac.modulo65.dto.Usuario;
import com.ebac.modulo65.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/usuarios")
    public ResponseWrapper<List<Usuario>> obtenerUsuarios() {
        // Devuelve un objeto User que se convertirá automáticamente en JSON/XML en la respuesta.
        System.out.println("Obteniendo usuarios");
        log.info("Obteniendo usuarios");
        List<Usuario> usuarioList = usuarioService.obtenerUsuarios();
        ResponseEntity<List<Usuario>> responseEntity = ResponseEntity.ok(usuarioList);

        return new ResponseWrapper<>(true, "Listado de usuarios", responseEntity);
    }

    @GetMapping("/usuarios/{id}")
    public ResponseWrapper<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        // Lógica para obtener el usuario por su ID
        // Devuelve un objeto User que se convertirá automáticamente en JSON/XML en la respuesta.
        Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuarioPorId(id);

        log.info("Obteniendo usuario por id {}", id);
        ResponseEntity<Usuario> usuarioResponseEntity =
                usuarioOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

        boolean success = false;

        if (usuarioOptional.isPresent()) {
            //success
            success = true;
        }

        return new ResponseWrapper<>(success, "Informacion del usuario " + id, usuarioResponseEntity);
    }

    @PostMapping("/usuarios")
    public ResponseWrapper<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        // Lógica para crear un nuevo usuario
        // Retorna ResponseEntity con el objeto User en el cuerpo y un código de estado 201 (CREATED) en la respuesta.

        log.info("Creando usuario");
        try {
            Usuario usuarioCreado = usuarioService.crearUsuario(usuario);
            ResponseEntity<Usuario> responseEntity = ResponseEntity.created(new URI("http://localhost/usuarios")).body(usuarioCreado);
            return new ResponseWrapper<>(true, "Usuario creado exitosamente", responseEntity);
        } catch (Exception e) {
            ResponseEntity<Usuario> responseEntity = ResponseEntity.badRequest().build();
            return new ResponseWrapper<>(false, e.getMessage(), responseEntity);
        }
    }

    @PutMapping("/usuarios/{id}")
    public ResponseWrapper<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        // Lógica para actualizar el usuario con el ID proporcionado
        // Retorna ResponseEntity con el objeto User actualizado en el cuerpo y un código de estado 200 (OK) en la respuesta.
        log.info("Actualizando usuario");
        Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuarioPorId(id);

        if (usuarioOptional.isPresent()) {
            usuarioActualizado.setIdUsuario(usuarioOptional.get().getIdUsuario());
            usuarioService.actualizarUsuario(usuarioActualizado);

            ResponseEntity<Usuario> responseEntity = ResponseEntity.ok(usuarioActualizado);
            return new ResponseWrapper<>(true, "Usuario actualizado correctamente", responseEntity);
        } else {
            ResponseEntity<Usuario> responseEntity = ResponseEntity.notFound().build();
            return new ResponseWrapper<>(false, "El usuario indicado no existe", responseEntity);
        }
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseWrapper<Void> eliminarUsuario(@PathVariable Long id) {
        // Lógica para eliminar el usuario con el ID proporcionado
        // Retorna ResponseEntity con un código de estado 204 (NO_CONTENT) en la respuesta.
        log.info("Eliminando usuario");
        usuarioService.eliminarUsuario(id);

        ResponseEntity<Void> responseEntity = ResponseEntity.noContent().build();
        return new ResponseWrapper<>(true, "Usuario eliminado correctamente", responseEntity);
    }
}
