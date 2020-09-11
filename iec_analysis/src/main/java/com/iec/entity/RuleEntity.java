package com.iec.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RuleEntity
 * 帧结构实体
 *
 * @author wxp
 * @version 1.0
 * @date 2020-09-10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleEntity {

    /**
     * 帧名称
     */
    private String structure;
    /**
     * 附加值1
     */
    private String attach1;
    /**
     * 附加值2
     */
    private String attach2;

}
