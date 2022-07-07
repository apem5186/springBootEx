package org.zerock.mreview.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.mreview.dto.MovieDTO;

@SpringBootTest
public class MovieServiceTests {

    private MovieService movieService;

    @Test
    public void removeTest() {
        Long mno = 10L;

        movieService.removeWithReplies(mno);
    }
}
