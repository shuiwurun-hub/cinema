package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket implements Serializable {

    public static final Integer PENDING_PAYMENT = 1;
    public static final Integer PAID = 2;
    public static final Integer CANCELLED = 3;
    public static final Integer REFUND = 4;

    public static final Integer UN_PAID = 0;
    public static final Integer PAID_STATUS = 1;
    public static final Integer REFUND_STATUS = 2;

    private static final long serialVersionUID = 1L;

    private Long id;

    private String number;

    private Integer status;

    private Long userId;

    private Long scheduleId;

    private LocalDateTime orderTime;

    private LocalDateTime payTime;

    private Integer payMethod;

    private Integer payStatus;

    private BigDecimal amount;

    private String remark;

    private String userName;

    private String phone;

    private String cancelReason;

    private LocalDateTime cancelTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}