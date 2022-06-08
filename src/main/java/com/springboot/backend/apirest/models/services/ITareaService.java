package com.springboot.backend.apirest.models.services;

import com.springboot.backend.apirest.models.entity.Tarea;

import java.util.List;

public interface ITareaService {

    List<Tarea> findAll();

    Tarea save(Tarea tarea);

    Tarea findById(Long id);

}
