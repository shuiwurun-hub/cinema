package com.sky.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MovieDTO implements Serializable {

    private Long id;

    private String name;

    private Long categoryId;

    private BigDecimal price;

    private String image;

    private String description;

    private Integer duration;

    private String director;

    private String actors;

    private LocalDate releaseDate;

    private Integer status;

}