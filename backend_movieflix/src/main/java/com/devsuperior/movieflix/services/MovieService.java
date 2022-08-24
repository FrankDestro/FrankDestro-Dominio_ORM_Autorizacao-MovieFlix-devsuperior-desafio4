package com.devsuperior.movieflix.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.MovieDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class MovieService {

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private AuthService authService;

	@Autowired
	private GenreRepository genreRepository;

	// Buscar movies por Genero 
	@Transactional(readOnly = true)
	public Page<MovieDTO> findMoviesbyGenre(Long genreId, PageRequest pageRequest) {
		Genre genre = (genreId == 0) ? null : genreRepository.getOne(genreId);
		Page<Movie> list = movieRepository.findMoviesbyGenre(genre, pageRequest);
		return list.map(MovieDTO::new);
	}

	// Busca todos os movies 
	@Transactional(readOnly = true)
	public Page<MovieDTO> findAllMoviesPaged(Pageable pageable) {
		User user = authService.authenticated();
		Page<Movie> result = movieRepository.findAll(pageable);
		Page<MovieDTO> page = result.map(x -> new MovieDTO(x));
		return page;
	}

	// Buscar movies por ID 
	@Transactional(readOnly = true)
	public MovieDTO findMovieById(Long id) {
		User user = authService.authenticated();
		Optional<Movie> obj = movieRepository.findById(id);
		Movie entity = obj.orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
		return new MovieDTO(entity);
	}
}
