package com.ebac.modulo65.controller;

import com.ebac.modulo65.dto.Usuario;
import com.ebac.modulo65.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    private static final Logger log = LoggerFactory.getLogger(UsuarioControllerTest.class);
    @Mock
    UsuarioService usuarioService;

    @InjectMocks
    UsuarioController usuarioController;

    @Test
    void obtenerUsuarios() {
        int usuarios = 5;
        List<Usuario> usuariosListExpected = crearUsuarios(usuarios);

        // Configuramos el comportamiento del mock
        when(usuarioService.obtenerUsuarios()).thenReturn(usuariosListExpected);

        // Ejecutamos el metodo del controlador
        ResponseWrapper<List<Usuario>> usuariosListActual = usuarioController.obtenerUsuarios();

        // Validamos el resultado
        if(usuariosListActual.isSuccess()) {
            ResponseEntity<List<Usuario>> usuarioResponseEntity = usuariosListActual.getResponseEntity();

            List<Usuario> actualList = usuarioResponseEntity.getBody();

            assert actualList != null;
            assertEquals(usuarios,actualList.size());
            assertEquals(usuariosListExpected, actualList);

        }

    }

    @Test
    void obtenerUsuariosCuandoNoExisten() {
        // Configuramos el comportamiento del mock
        when(usuarioService.obtenerUsuarios()).thenReturn(List.of());

        // Ejecutamos el metodo del controlador
        ResponseWrapper<List<Usuario>> usuarioListActual = usuarioController.obtenerUsuarios();

        // Validamos el resultado

        if(usuarioListActual.isSuccess()) {
            ResponseEntity<List<Usuario>> usuarioResponseEntity = usuarioListActual.getResponseEntity();

            List<Usuario> actualList = usuarioResponseEntity.getBody();

            assert actualList != null;
            assertTrue(actualList.isEmpty());
        }

        verify(usuarioService, times(1)).obtenerUsuarios();
    }

    @Test
    void obtenerUsuarioPorId() {
        long idUsuario = 1;
        Optional<Usuario> usuarioExpected = Optional.of(crearUsuarios(1).get(0));

        // Configuramos el comportamiento del mock
        when(usuarioService.obtenerUsuarioPorId(idUsuario)).thenReturn(usuarioExpected);

        // Ejecutamos el metodo del controlador
        ResponseWrapper<Usuario> usuarioResponseEntity = usuarioController.obtenerUsuarioPorId(idUsuario);

        Usuario usuarioActual = usuarioResponseEntity.getResponseEntity().getBody();

        // Validamos el resultado
        assertEquals(200, usuarioResponseEntity.getResponseEntity().getStatusCode().value());
        assertNotNull(usuarioActual);
        assertEquals("Nombre1", usuarioActual.getNombre());
    }


    @Test
    void obtenerUsuarioPorIdCuandoNoExiste() {
        long idUsuario = 1;

        // Configuramos el comportamiento del mock
        when(usuarioService.obtenerUsuarioPorId(idUsuario)).thenReturn(Optional.empty());

        // Ejecutamos el metodo del controlador
        ResponseWrapper<Usuario> usuarioResponseEntity = usuarioController.obtenerUsuarioPorId(idUsuario);
        Usuario usuarioActual = usuarioResponseEntity.getResponseEntity().getBody();

        // Validamos el resultado
        assertEquals(404, usuarioResponseEntity.getResponseEntity().getStatusCode().value());
        assertTrue(Objects.isNull(usuarioActual));
    }

    @Test
    void crearUsuario() throws Exception {
        Usuario usuarioExpected = crearUsuarios(1).get(0);

        // Configuramos el comportamiento del mock
        when(usuarioService.crearUsuario(usuarioExpected)).thenReturn(usuarioExpected);

        // Ejecutamos el metodo del controlador
        ResponseWrapper<Usuario> usuarioResponseEntity = usuarioController.crearUsuario(usuarioExpected);
        Usuario usuarioActual = usuarioResponseEntity.getResponseEntity().getBody();

        // Validamos el resultado
        assertEquals(201, usuarioResponseEntity.getResponseEntity().getStatusCode().value());
        //assertTrue(Objects.isNull(usuarioActual));
    }

    @Test
    void actualizarUsuario() {
        int idUsuario = 5;
        String nombreActualizado = "Beatriz";
        int edadActualizada = 25;

        Usuario usuarioAntiguo = new Usuario();
        usuarioAntiguo.setIdUsuario(idUsuario);
        usuarioAntiguo.setNombre("Julieta");
        usuarioAntiguo.setEdad(28);

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre(nombreActualizado);
        usuarioActualizado.setEdad(edadActualizada);

        // Configuramos el comportamiento del mock
        when(usuarioService.obtenerUsuarioPorId((long) idUsuario)).thenReturn(Optional.of(usuarioAntiguo));
        doNothing().when(usuarioService).actualizarUsuario(usuarioActualizado);

        // Ejecutamos el metodo del controlador
        ResponseWrapper<Usuario> usuarioResponseEntity = usuarioController.actualizarUsuario((long) idUsuario, usuarioActualizado);
        Usuario usuarioActual = usuarioResponseEntity.getResponseEntity().getBody();

        // Validamos el resultado
        assertEquals(200, usuarioResponseEntity.getResponseEntity().getStatusCode().value());
        assertNotNull(usuarioActual);
        assertEquals(idUsuario, usuarioActual.getIdUsuario());
        assertEquals(nombreActualizado, usuarioActual.getNombre());
        assertEquals(edadActualizada, usuarioActual.getEdad());
    }

    @Test
    void actualizarUsuarioCuandoElUsuarioNoExiste() {
        long idUsuario = 5;
        String nombreActualizado = "Beatriz";
        int edadActualizada = 25;

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre(nombreActualizado);
        usuarioActualizado.setEdad(edadActualizada);

        // Configuramos el comportamiento del mock
        when(usuarioService.obtenerUsuarioPorId(idUsuario)).thenReturn(Optional.empty());

        // Ejecutamos el metodo del controlador
        ResponseWrapper<Usuario> usuarioResponseEntity = usuarioController.actualizarUsuario(idUsuario, usuarioActualizado);
        Usuario usuarioActual = usuarioResponseEntity.getResponseEntity().getBody();

        // Validamos el resultado
        assertEquals(404, usuarioResponseEntity.getResponseEntity().getStatusCode().value());
        assertNull(usuarioActual);
        verify(usuarioService, never()).actualizarUsuario(usuarioActualizado);
    }

    @Test
    void eliminarUsuario() {
        long idUsuario = 1;

        // Configuramos el comportamiento del mock
        doNothing().when(usuarioService).eliminarUsuario(idUsuario);

        // Ejecutamos el metodo del controlador
        ResponseWrapper<Void> responseEntity = usuarioController.eliminarUsuario(idUsuario);

        // Validamos el resultado
        assertEquals(204, responseEntity.getResponseEntity().getStatusCode().value());
        verify(usuarioService, atLeastOnce()).eliminarUsuario(idUsuario);
    }

    @Test
    void crearUsuarioCuandoSeaMenorA18() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setNombre("Nombre");
        usuario.setEdad(10);

        // Configuramos el comportamiento del mock
        doThrow(Exception.class).when(usuarioService).crearUsuario(usuario);

        // Ejecutamos el metodo del controlador
        ResponseWrapper<Usuario> responseEntity = usuarioController.crearUsuario(usuario);
        Usuario usuarioActual = responseEntity.getResponseEntity().getBody();

        assertEquals(400, responseEntity.getResponseEntity().getStatusCode().value());
        assertNull(usuarioActual);
    }

    private List<Usuario> crearUsuarios(int elementos) {
        return IntStream.range(1, elementos+1)
                .mapToObj(i -> {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(i);
                    usuario.setNombre("Nombre" + i);
                    usuario.setEdad(15 + i);
                    return usuario;
                }).collect(Collectors.toList());
    }
}