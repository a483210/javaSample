package com.psd.test.sample.model.vo;

import lombok.Data;

/**
 * UserInfo
 *
 * @author Created by gold on 2021/4/16 16:09
 * @since 1.0.0
 */
@Data
public class UserInfo {

    /**
     * 用户id
     */
    private long userId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 用户拥有的金额
     */
    private long coin;

}
