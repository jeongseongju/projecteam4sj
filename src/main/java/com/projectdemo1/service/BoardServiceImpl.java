package com.projectdemo1.service;

import com.projectdemo1.board4.domain.Cboard;
import com.projectdemo1.board4.dto.CboardDTO;
import com.projectdemo1.board4.dto.CpageRequestDTO;
import com.projectdemo1.board4.dto.CpageResponseDTO;
import com.projectdemo1.domain.Board;
import com.projectdemo1.domain.User;
import com.projectdemo1.dto.BoardDTO;
import com.projectdemo1.dto.BoardListReplyCountDTO;
import com.projectdemo1.dto.PageRequestDTO;
import com.projectdemo1.dto.PageResponseDTO;
import com.projectdemo1.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;


    @Override
    public void register(Board board, User user) {
        board.setUser(user);
        boardRepository.save(board);
    }

    @Override
    public List<Board> list() {
        return List.of();
    }

    //    @Override
//    public List<Board> list() {
//       // return List.of();
//        return boardRepository.findAll();
//    }
    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");
        Page<Board> result = boardRepository.searchAll(types,keyword, pageable);

        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board,BoardDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }


    @Override
    public Board findById(Long bno) {
        Board board = boardRepository.findById(bno).get();
        board.setHitCount(board.getHitCount() + 1);
        boardRepository.save(board);
        return board;
    }

    @Override
    public void modify(Board board) {
       // Board b = boardRepository.findById(board.getBno()).get();
        Board b = boardRepository.findById(board.getBno())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        b.setContent(board.getContent());
        b.setTitle(board.getTitle());
        boardRepository.save(b);
    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);
    }

  /* 박경미 쌤 참조
  @Override
    public void insert(Board board, User user) {
        board.setUser(user);
        boardRepository.save(board);

    }

    @Override
    public List<Board> list() {
        return boardRepository.findAll();
    }

    @Override
    public Board findById(Long num) {
        Board board = boardRepository.findById(num).get();
        board.setHitCount(board.getHitCount()+1);
        boardRepository.save(board);
        return board;
    }

    @Override
    public void update(Board board) {
        Board b = boardRepository.findById(board.getBno()).get();
        b.setContent(board.getContent());
        b.setTitle(board.getTitle());
    }

    @Override
    public void delete(Long num) {
        boardRepository.deleteById(num);
    }*/


   /* //윤요섭쌤 참조
    @Override
    public Long register(BoardDTO boardDTO) {
        return 0L;
    }

    @Override
    public BoardDTO readOne(Long bno) {
        return null;
    }

    @Override
    public void modify(BoardDTO boardDTO) {

    }

    @Override
    public void remove(Long bno) {

    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
        return null;
    }

    @Override
    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO) {
        return null;
    }*/
}
