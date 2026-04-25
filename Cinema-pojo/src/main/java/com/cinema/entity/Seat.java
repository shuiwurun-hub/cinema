package com.cinema.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seat implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long hallId;

    private Integer rowNum;

    private Integer colNum;

    private String seatType;

    private Integer status;

}
