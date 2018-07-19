package com.myself.seckill.entity;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SuccessKilled extends SuccessKilledKey {
    private Byte state;

    private Date createTime;

    /**
     * 多对一
     */
    private SecKill secKill;
}