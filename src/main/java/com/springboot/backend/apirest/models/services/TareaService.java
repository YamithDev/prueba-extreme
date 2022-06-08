package com.springboot.backend.apirest.models.services;

import com.springboot.backend.apirest.models.dao.ITareaDao;
import com.springboot.backend.apirest.models.entity.Tarea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TareaService implements ITareaService {

    @Autowired
    private ITareaDao repo;

    @Override
    public List<Tarea> findAll() {
        return repo.findAll();
    }

    @Override
    public Tarea save(Tarea tarea) {
        return repo.save(tarea);
    }

    @Override
    public Tarea findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void deleteTarea(Long id){
        repo.deleteById(id);
    }
}
