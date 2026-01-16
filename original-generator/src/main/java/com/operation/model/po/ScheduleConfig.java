package com.operation.model.po;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Table(name = "schedule_config")
@Data
public class ScheduleConfig {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Integer id;

    /**
     * 编码
     */
    private String code;

    /**
     * 定时任务名称
     */
    private String name;

    /**
     * cron表达式
     */
    private String cron;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人
     */
    @Column(name = "modify_user_id")
    private Integer modifyUserId;

    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    private Date modifyTime;
}