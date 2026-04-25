package com.cinema.service.impl;

import com.cinema.context.BaseContext;
import com.cinema.dto.RatingDTO;
import com.cinema.entity.Movie;
import com.cinema.entity.Rating;
import com.cinema.mapper.MovieMapper;
import com.cinema.mapper.RatingMapper;
import com.cinema.service.RatingService;
import com.cinema.vo.RatingVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingMapper ratingMapper;

    @Autowired
    private MovieMapper movieMapper;

    @Override
    public void save(RatingDTO ratingDTO) {
        Rating rating = new Rating();
        BeanUtils.copyProperties(ratingDTO, rating);
        
        rating.setUserId(BaseContext.getCurrentId());
        rating.setCreateTime(LocalDateTime.now());
        rating.setUpdateTime(LocalDateTime.now());
        
        ratingMapper.insert(rating);
    }

    @Override
    public void update(RatingDTO ratingDTO) {
        Rating existingRating = ratingMapper.getByUserIdAndMovieId(
            BaseContext.getCurrentId(), 
            ratingDTO.getMovieId()
        );
        
        if (existingRating != null) {
            existingRating.setRating(ratingDTO.getRating());
            existingRating.setComment(ratingDTO.getComment());
            existingRating.setUpdateTime(LocalDateTime.now());
            
            ratingMapper.update(existingRating);
        }
    }

    @Override
    public void deleteById(Long id) {
        ratingMapper.deleteById(id);
    }

    @Override
    public RatingVO getByUserIdAndMovieId(Long userId, Long movieId) {
        Rating rating = ratingMapper.getByUserIdAndMovieId(userId, movieId);
        
        if (rating == null) {
            return null;
        }
        
        return convertToVO(rating);
    }

    @Override
    public List<RatingVO> listByUserId(Long userId) {
        List<Rating> ratings = ratingMapper.listByUserId(userId);
        
        return ratings.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RatingVO> listByMovieId(Long movieId) {
        List<Rating> ratings = ratingMapper.listByMovieId(movieId);
        
        return ratings.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    private RatingVO convertToVO(Rating rating) {
        RatingVO vo = new RatingVO();
        BeanUtils.copyProperties(rating, vo);
        
        Movie movie = movieMapper.getById(rating.getMovieId());
        if (movie != null) {
            vo.setMovieName(movie.getName());
        }
        
        return vo;
    }
}
