package com.myself.seckill.entity;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SeckillUser {
    private Long id;

    private String nickname;

    private String password;

    private String salt;

    private String head;

    private Date registerTime;

    private Date lastLoginTime;

    private Integer loginCount;
}