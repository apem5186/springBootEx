package org.zerock.mreview.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.dto.MovieImageDTO;
import org.zerock.mreview.dto.PageRequestDTO;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.service.MovieService;
import org.zerock.mreview.service.MovieServiceImpl;

import java.net.URI;
import java.net.URL;
import java.util.List;

@Controller
@RequestMapping("/movie")
@Log4j2
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;    //final

    @GetMapping("/register")
    public void register() {

    }

    @PostMapping("/register")
    public String register(MovieDTO movieDTO, RedirectAttributes redirectAttributes) {
        log.info("movieDTO: " + movieDTO);

        Long mno = movieService.register(movieDTO);

        redirectAttributes.addFlashAttribute("msg", mno);

        return "redirect:/movie/list";
    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {

        log.info("pageRequestDTO: " + pageRequestDTO);

        model.addAttribute("result", movieService.getList(pageRequestDTO));
    }

    @GetMapping({"/read", "/modify"})
    public void read(long mno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {

        log.info("read or modify mno: " + mno);

        MovieDTO movieDTO = movieService.getMovie(mno);

        log.info("movieDTO : " + movieDTO.getMno());
        log.info(movieDTO.getImageDTOList());

        model.addAttribute("dto", movieDTO);
    }

    @PostMapping("/modify")     // 수정기능 업데이트 중
    public String modify(MovieDTO movieDTO, RedirectAttributes redirectAttributes) {

        return "redirect:/movie/list";
    }

    @GetMapping("/{mno}")
    public ResponseEntity<List<MovieImageDTO>> getImageList(@PathVariable("mno") Long mno) {
        log.info("-------------imageList-------------");
        log.info("mno : " + mno);

        List<MovieImageDTO> imageDTOList = movieService.getListImage(mno);

        return new ResponseEntity<>(imageDTOList, HttpStatus.OK);
    }

    @PostMapping("/remove")
    public String remove(@RequestParam("mno")Long mno, RedirectAttributes redirectAttributes) {

        log.info("removeController mno: " + mno);

        movieService.removeWithReplies(mno);

        redirectAttributes.addFlashAttribute("msg", mno);

        return "redirect:/movie/list";
    }
}
