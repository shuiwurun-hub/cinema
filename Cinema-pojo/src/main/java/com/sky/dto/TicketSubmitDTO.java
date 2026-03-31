package com.sky.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class TicketSubmitDTO implements Serializable {

    private Long scheduleId;

    private List<Long> seatIds;

    private Integer payMethod;

    private String remark;

}