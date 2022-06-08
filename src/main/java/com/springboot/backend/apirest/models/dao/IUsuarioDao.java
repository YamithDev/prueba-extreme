package com.springboot.backend.apirest.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.springboot.backend.apirest.models.entity.Usuario;

import java.util.List;

public interface IUsuarioDao extends CrudRepository<Usuario, Long>{
	
	Usuario findByUsername(String username);
	
	@Query("select u from Usuario u where u.username=?1")
	Usuario findByUsername2(String username);

	@Query("FROM Usuario ")
	List<Usuario> ListarTodos();
	
}
