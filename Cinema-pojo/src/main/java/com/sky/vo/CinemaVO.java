package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CinemaVO implements Serializable {

    private Long id;

    private String name;

    private String address;

    private String phone;

    private String image;

    private String description;

    private Integer status;

}