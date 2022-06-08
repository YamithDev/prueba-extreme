package com.springboot.backend.apirest.models.services;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.springboot.backend.apirest.models.dao.IRoleDao;
import com.springboot.backend.apirest.models.entity.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.backend.apirest.models.dao.IUsuarioDao;
import com.springboot.backend.apirest.models.entity.Usuario;

@Service
public class UsuarioService implements IUsuarioService, UserDetailsService{

	private Logger logger = LoggerFactory.getLogger(UsuarioService.class);

	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}

	@Autowired
	private IRoleDao roleDao;
	
	@Autowired
	private IUsuarioDao usuarioDao;
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Usuario usuario = usuarioDao.findByUsername(username);
		
		if (usuario == null) {
			logger.error("Error en el Login: no existe el usuario '"+username+"' En el Sistema!");
			throw new UsernameNotFoundException("Error en el Login: no existe el usuario '"+username+"' En el Sistema!");
		}
		
		List<GrantedAuthority> authorities = usuario.getRoles()
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getNombre()))
				.peek(authority -> logger.info("Role :" + authority.getAuthority()))
				.collect(Collectors.toList());
		
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Usuario findByUsername(String username) {
		return usuarioDao.findByUsername(username);
	}

	@Transactional
	public List<Usuario> findAll(){
		return usuarioDao.ListarTodos();
	}

	public Usuario findById(Long id){
		return usuarioDao.findById(id).orElse(null);
	}

	public Usuario crearNuevoUsuario(Usuario usuario) {

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

		Usuario userNew = new Usuario();

		userNew.setNombreCompleto(usuario.getNombreCompleto());
		userNew.setIdentificacion(usuario.getIdentificacion());
		userNew.setEmail(usuario.getEmail());
		userNew.setEnabled(usuario.getEnabled());
		userNew.setCelular(usuario.getCelular());
		userNew.setUsername(usuario.getUsername());
		userNew.setPassword(passwordEncoder().encode(usuario.getPassword()));
		Role userRole = roleDao.findByNombre("ROLE_ADMIN");
		userNew.setRoles(Arrays.asList(userRole));
		String json;
		try {
			json = ow.writeValueAsString(userNew);
			logger.info("usuario creado: " + json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return usuarioDao.save(userNew);
	}

	public void delete(Long id){
		usuarioDao.deleteById(id);
	}

	public Usuario save(Usuario usuario){
		return  usuarioDao.save(usuario);
	}

}
