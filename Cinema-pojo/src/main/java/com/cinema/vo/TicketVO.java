package com.cinema.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketVO implements Serializable {

    private Long id;

    private String number;

    private Integer status;

    private Long userId;

    private String userName;

    private String phone;

    private Long scheduleId;

    private String movieName;

    private String cinemaName;

    private String hallName;

    private LocalDate showDate;

    private LocalTime showTime;

    private LocalDateTime orderTime;

    private LocalDateTime payTime;

    private Integer payMethod;

    private Integer payStatus;

    private BigDecimal amount;

    private String remark;

    private String cancelReason;

    private LocalDateTime cancelTime;

    private List<TicketDetailVO> ticketDetails;

}
