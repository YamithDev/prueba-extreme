package com.springboot.backend.apirest.models.dao;

import com.springboot.backend.apirest.models.entity.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IRoleDao extends CrudRepository<Role, Long> {

    Role findByNombre(String nombre);

    Optional<Role> findById(Long id);
}
