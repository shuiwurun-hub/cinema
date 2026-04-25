package com.cinema.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MoviePageQueryDTO implements Serializable {

    private String name;

    private Integer categoryId;

    private Integer status;

    private Integer page;

    private Integer pageSize;

}
