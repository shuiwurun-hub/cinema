package com.cinema.service;

import com.cinema.dto.RatingDTO;
import com.cinema.vo.RatingVO;

import java.util.List;

public interface RatingService {

    void save(RatingDTO ratingDTO);

    void update(RatingDTO ratingDTO);

    void deleteById(Long id);

    RatingVO getByUserIdAndMovieId(Long userId, Long movieId);

    List<RatingVO> listByUserId(Long userId);

    List<RatingVO> listByMovieId(Long movieId);
}
