package com.wing.user.entity;

import com.wing.user.enums.UserTypeEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by 廖师兄
 * 2017-07-23 23:02
 */
@Data
@Entity
@DynamicUpdate
public class User {

    @Id
    private String id;

    private String username;

    private String password;

    private String openid;

    @Enumerated(EnumType.STRING)
    private UserTypeEnum UserType;

    private Date createTime;

    private Date updateTime;


}
