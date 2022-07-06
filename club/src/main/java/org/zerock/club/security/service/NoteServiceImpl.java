package org.zerock.club.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.club.entity.Note;
import org.zerock.club.repository.NoteRepostiory;
import org.zerock.club.security.dto.NoteDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Log4j2
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService{

    private final NoteRepostiory noteRepostiory;

    @Override
    public Long register(NoteDTO noteDTO) {

        Note note = dtoToEntity(noteDTO);

        log.info("======================================");
        log.info(note);

        noteRepostiory.save(note);

        return note.getNum();
    }

    @Override
    public NoteDTO get(Long num) {

        Optional<Note> result = noteRepostiory.getWithWriter(num);

        if (result.isPresent()) {
            return entityToDTO(result.get());
        }

        return null;
    }

    @Override
    public void modify(NoteDTO noteDTO) {

        Long num = noteDTO.getNum();

        Optional<Note> result = noteRepostiory.findById(num);

        if (result.isPresent()) {

            Note note = result.get();

            note.changeTitle(noteDTO.getTitle());

            note.changeContent(noteDTO.getContent());

            noteRepostiory.save(note);
        }
    }

    @Override
    public void remove(Long num) {

        noteRepostiory.deleteById(num);
    }

    @Override
    public List<NoteDTO> getAllWithWriter(String writerEmail) {

        List<Note> noteList = noteRepostiory.getList(writerEmail);

        return noteList.stream().map(note -> entityToDTO(note)).collect(Collectors.toList());
    }
}
