package com.iec.work;

import com.iec.entity.RuleEntity;
import com.iec.utils.Constant;
import io.netty.channel.ChannelHandlerContext;

/**
 * SComParse
 *
 * @author wxp
 * @version 1.0
 * @date 2020-09-10
 */
public class SComParse {
    public static SComParse I = new SComParse();

    private SComParse() {
    }

    public String analysis(ChannelHandlerContext channel, byte[] reg, RuleEntity ruleEntity) {

        //记录S帧的接受序号
        String receivedNumber = ruleEntity.getAttach1();
        Constant.CT_TEMP_MSG.put("ICOUNT", receivedNumber);
        return null;
    }
}
