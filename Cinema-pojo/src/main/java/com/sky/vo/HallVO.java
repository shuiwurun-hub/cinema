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
public class HallVO implements Serializable {

    private Long id;

    private Long cinemaId;

    private String cinemaName;

    private String name;

    private String type;

    private Integer rowCount;

    private Integer colCount;

    private Integer totalSeats;

    private Integer status;

}