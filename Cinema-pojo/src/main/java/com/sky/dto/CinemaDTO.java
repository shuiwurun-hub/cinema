package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CinemaDTO implements Serializable {

    private Long id;

    private String name;

    private String address;

    private String phone;

    private String image;

    private String description;

    private Integer status;

}