package com.iec.analysis.common;

/**
 * RuleCommand
 *
 * @author wxp
 * @version 1.0
 * @date 2020-09-10
 */
public interface RuleCommand {

    String[] T104_COMMAND_FORMAT = {
            "68", // 104规约开头
            "ZJ", //APDU 长度(最大, 253)
            "01", // 控制域八位位组 1
            "01", // 控制域八位位组 2
            "01",     //控制域八位位组 3
            "01", // 控制域八位位组 4
            "ASDU"
    };


}
