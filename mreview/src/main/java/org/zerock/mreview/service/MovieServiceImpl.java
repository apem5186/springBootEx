package org.zerock.mreview.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.dto.MovieImageDTO;
import org.zerock.mreview.dto.PageRequestDTO;
import org.zerock.mreview.dto.PageResultDTO;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;
import org.zerock.mreview.repository.MovieImageRepository;
import org.zerock.mreview.repository.MovieRepository;
import org.zerock.mreview.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;  //final

    private final MovieImageRepository imageRepository; //final

    private final ReviewRepository reviewRepository;    // final

    @Transactional
    @Override
    public Long register(MovieDTO movieDTO) {

        Map<String, Object> entityMap = dtoToEntity(movieDTO);
        Movie movie = (Movie) entityMap.get("movie");
        List<MovieImage> movieImageList = (List<MovieImage>) entityMap.get("imgList");

        movieRepository.save(movie);

        movieImageList.forEach(movieImage -> {
            imageRepository.save(movieImage);
        });
        return movie.getMno();
    }

    @Override
    public PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO) {

        Pageable pageable = requestDTO.getPageable(Sort.by("mno").descending());

        Page<Object[]> result = movieRepository.getListPage(pageable);

        Function<Object[], MovieDTO> fn = (arr -> entitiesToDTO(
                (Movie)arr[0],
                (List<MovieImage>) (Arrays.asList((MovieImage)arr[1])),
                (Double)arr[2],
                (Long)arr[3])
        );

        return new PageResultDTO<>(result, fn);

    }

    @Override
    public MovieDTO getMovie(Long mno) {

        List<Object[]> result = movieRepository.getMovieWithAll(mno);

        Movie movie = (Movie) result.get(0)[0]; // Movie 엔티티는 가장 앞에 존재 - 모든 Row가 동일한 값

        List<MovieImage> movieImageList = new ArrayList<>();    //영화의 이미지개수만큼 MovieImage객체 필요

        result.forEach(arr -> {
            MovieImage movieImage = (MovieImage) arr[1];
            movieImageList.add(movieImage);
        });

        Double avg = (Double) result.get(0)[2]; //평균 평점 - 모든 Row가 동일한 값
        Long reviewCnt = (Long) result.get(0)[3];   //리뷰 개수 - 모든 Row가 동일한 값

        return entitiesToDTO(movie, movieImageList, avg, reviewCnt);
    }

    @Transactional
    @Override
    public void removeWithReplies(Long mno) {   // 삭제 기능 구현, 트랜잭션 추가

        Movie movie = Movie.builder().mno(mno).build();

        // 댓글 부터 삭제
        reviewRepository.deleteByMovie(movie);
        // 게시글과 DB 이미지는 삭제가 되나 파일에 있는 이미지는 삭제가 안됨
        imageRepository.deleteByMovie(mno);

        movieRepository.deleteById(mno);
    }

    @Transactional
    @Override
    public void modify(MovieDTO movieDTO) {

        Map<String, Object> entityMap = dtoToEntity(movieDTO);

        Movie movie = (Movie) entityMap.get("movie");

        List<MovieImage> movieImageList = (List<MovieImage>) entityMap.get("imgList");

        Long mno = movie.getMno();

        imageRepository.deleteByMovie(mno);

        movieRepository.save(movie);

        imageRepository.saveAll(movieImageList);



        movie.changeTitle(movieDTO.getTitle());


        //제목과 사진 바꾸기 기능 업데이트
        //movie.change
    }
}
