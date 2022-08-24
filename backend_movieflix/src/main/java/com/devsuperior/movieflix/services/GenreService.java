package com.devsuperior.movieflix.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.GenreDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class GenreService {

	@Autowired
	private AuthService authService;

	@Autowired
	private GenreRepository repository;

	// Busca todos os generos. 
	@Transactional(readOnly = true)
	public List<GenreDTO> findAllGenres() {
		User user = authService.authenticated();
		List<Genre> result = repository.findAll();
		List<GenreDTO> list = result.stream().map(x -> new GenreDTO(x)).collect(Collectors.toList());
		return list;
	}

	// Buscar por Genero. 
	@Transactional(readOnly = true)
	public GenreDTO findByIdGenre(Long id) {
		User user = authService.authenticated();
		Optional<Genre> obj = repository.findById(id);
		Genre entity = obj.orElseThrow(() -> new ResourceNotFoundException("Genre not found"));
		return new GenreDTO(entity);
	}
}
