package org.zerock.mreview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;

import java.util.List;

public interface MovieImageRepository extends JpaRepository<MovieImage, Long> {

    @Query("SELECT mi FROM MovieImage mi WHERE mi.movie =:movie")
    List<MovieImage> findByMovie(Movie movie);

    @Modifying
    @Query("DELETE FROM MovieImage mi WHERE mi.movie.mno =:mno")
    void deleteByMovie(Long mno);
}
