package com.cinema.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class RatingVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private Long movieId;

    private String movieName;

    private Integer rating;

    private String comment;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
