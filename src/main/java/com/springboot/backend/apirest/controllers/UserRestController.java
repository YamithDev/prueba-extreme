package com.springboot.backend.apirest.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.springboot.backend.apirest.models.entity.Usuario;
import com.springboot.backend.apirest.models.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = {"http://localhost:8100", "http://localhost:4200", "*"})
@RestController
@RequestMapping("/api/v1/")
public class UserRestController {

  @Autowired
  private UsuarioService service;

  @GetMapping("/usuarios")
  public List<Usuario> index() {
    return service.findAll();
  }


  @Secured({"ROLE_ADMIN", "ROLE_USER"})
  @GetMapping("/usuario/{id}")
  public ResponseEntity<?> show(@PathVariable Long id) {

    Usuario usuario;
    Map<String, Object> response = new HashMap<>();

    try {
      usuario = service.findById(id);
    } catch (DataAccessException e) {
      response.put("mensaje", "Error al realizar la consulta");
      response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    if (usuario == null) {
      response.put("mensaje", "El usuario con el ID:".concat(id.toString().concat(" no existe")));
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(usuario, HttpStatus.OK);
  }

  @Secured("ROLE_ADMIN")
  @PostMapping("/usuario/new")
  public ResponseEntity<?> create(@Valid @RequestBody Usuario usuario, BindingResult result) {

    Usuario usuarioNew = null;
    Map<String, Object> response = new HashMap<>();

    if (result.hasErrors()) {

      List<String> errors = result.getFieldErrors()
          .stream()
          .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
          .collect(Collectors.toList());

      response.put("errors", errors);
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
    }

    try {
      usuarioNew = service.crearNuevoUsuario(usuario);
    } catch (DataAccessException e) {
      response.put("mensaje", "Error al realizar la inserción");
      response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    response.put("mensaje", "El usuario ha sido creado con éxito!");
    response.put("usuario", usuarioNew);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PutMapping("/usuario/update/{id}")
  public ResponseEntity<?> update(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id) {

    Usuario usrAct = service.findById(id);
    Usuario usrUpd;

    Map<String, Object> response = new HashMap<>();

    if (result.hasErrors()) {

      List<String> errors = result.getFieldErrors()
          .stream()
          .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
          .collect(Collectors.toList());

      response.put("errors", errors);
      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    if (usrAct == null) {
      response.put("mensaje", "Eror: no se puede editar, el usuario con el ID: ".concat(id.toString().concat(". No existe")));
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    try {

      usrAct.setNombreCompleto(usuario.getNombreCompleto());
      usrAct.setCelular(usuario.getCelular());
      usrAct.setIdentificacion(usuario.getIdentificacion());
      usrAct.setEmail(usuario.getEmail());
      usrAct.setEnabled(usuario.getEnabled());
      usrAct.setPassword(usuario.getPassword());
      usrAct.setRoles(usuario.getRoles());
      usrUpd = service.save(usrAct);

    } catch (DataAccessException e) {

      response.put("mensaje", "Error al actualizar el usuario");
      response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    response.put("mensaje", "El usuario ha sido actualizado con éxito!");
    response.put("usuario", usrUpd);

    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @Secured("ROLE_ADMIN")
  @DeleteMapping("/usuario/delete/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id) {

    Map<String, Object> response = new HashMap<>();

    try {
      Usuario user = service.findById(id);
      service.delete(id);
    } catch (DataAccessException e) {
      response.put("mensaje", "Error al Eliminar el usuario");
      response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    response.put("mensaje", "El usuario ha sido eliminado con éxito!");
    return new ResponseEntity<>(response, HttpStatus.OK);

  }

}
