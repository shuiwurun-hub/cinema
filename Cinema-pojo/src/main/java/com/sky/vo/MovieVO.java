package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieVO implements Serializable {

    private Long id;

    private String name;

    private String categoryName;

    private BigDecimal price;

    private String image;

    private String description;

    private Integer duration;

    private String director;

    private String actors;

    private LocalDate releaseDate;

    private Integer status;

}