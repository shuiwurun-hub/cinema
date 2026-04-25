package com.cinema.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hall implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long cinemaId;

    private String name;

    private String type;

    private Integer rowCount;

    private Integer colCount;

    private Integer totalSeats;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

}
