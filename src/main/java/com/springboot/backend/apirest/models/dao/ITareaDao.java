package com.springboot.backend.apirest.models.dao;

import com.springboot.backend.apirest.models.entity.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITareaDao extends JpaRepository<Tarea, Long> {
}
