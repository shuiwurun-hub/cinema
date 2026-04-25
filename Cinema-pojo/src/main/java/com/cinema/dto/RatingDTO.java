package com.cinema.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RatingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long movieId;

    private Integer rating;

    private String comment;
}
