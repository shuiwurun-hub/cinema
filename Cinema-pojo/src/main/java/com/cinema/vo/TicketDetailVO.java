package com.cinema.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDetailVO implements Serializable {

    private Long id;

    private Long ticketId;

    private Long seatId;

    private BigDecimal price;

    private Integer rowNum;

    private Integer colNum;

    private String seatType;

}
