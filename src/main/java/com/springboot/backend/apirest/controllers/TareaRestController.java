package com.springboot.backend.apirest.controllers;


import com.springboot.backend.apirest.models.entity.Tarea;
import com.springboot.backend.apirest.models.services.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = {"*"})
@RequestMapping("/api/v1")
@RestController
public class TareaRestController {

    @Autowired
    private TareaService service;

    @GetMapping("/tareas/list")
    public List<Tarea> findAll(){
        return service.findAll();
    }

    @PostMapping("/tarea/new")
    public ResponseEntity<?> create(@RequestBody Tarea tarea){

        Map<String, Object> response = new HashMap<>();

        try {
            response.put("tarea", service.save(tarea));
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la insercción");
            response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("Exito!", "tarea creada con éxito!");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/tarea/update/{id}")
    public ResponseEntity<?> update(@RequestBody Tarea tarea, @PathVariable Long id){

        Map<String, Object> response = new HashMap<>();

        Tarea tarAct = service.findById(id);

        Tarea tarUpd = null;

        if(tarAct == null){
             response.put("mensaje", "no se ecuentra la tarea con el id: "+ id.toString());
             return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        try {
            tarAct.setMotivo(tarea.getMotivo());
            tarAct.setDireccion(tarea.getDireccion());
            tarAct.setEnabled(tarea.getEnabled());

            tarAct.setConsecutivo(UUID.randomUUID().toString());

            tarUpd = service.save(tarAct);

        } catch ( DataAccessException e) {
            response.put("mensaje", "error al ejecutar la consulta");
            response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("exito", "tarea actualizada con éxito");
        response.put("tarea", tarUpd);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/tarea/estado/{id}")
    public ResponseEntity<?> updateStatus(@RequestBody Tarea tarea, @PathVariable Long id){
        Tarea tarAct = service.findById(id);
        Tarea tarUpd = null;

        Map<String, Object> response = new HashMap<>();
        if(tarAct == null){
            response.put("mensaje", "no se ecuentra la tarea con el id: "+ id.toString());
        }
        try {
            tarAct.setEnabled(tarea.getEnabled());
            tarUpd = service.save(tarAct);
        } catch ( DataAccessException e) {
            response.put("mensaje", "error al ejecutar la consulta");
            response.put("error", e.getMessage()+": "+e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("exito", "tarea desactivada/activada con éxito");
        response.put("tarea", tarUpd);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
