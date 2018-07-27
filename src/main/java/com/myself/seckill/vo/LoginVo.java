package com.myself.seckill.vo;

import com.myself.seckill.validator.IsMobile;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 暴露秒杀接口
 *
 * @author Created by zion
 * @Date 2018/7/25.
 */
@Builder
@Getter
@Setter
@ToString
public class LoginVo {

    @NotNull
    @IsMobile
    private String phone;

    @NotNull
    @Length(min = 32)
    private String password;


}
