package com.iec.work;

import com.iec.analysis.protocol104.Analysis;
import com.iec.entity.RuleEntity;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.regex.PatternSyntaxException;

/**
 * ruleComParse
 *
 * @author wxp
 * @version 1.0
 * @date 2020-09-10
 */
@Component
public class ruleComParse {
    public static ruleComParse I = new ruleComParse();

    private ruleComParse() {
    }

    public String parseRuleCom(ChannelHandlerContext channel, byte[] reg) throws IndexOutOfBoundsException, PatternSyntaxException {
        if (reg.length == 0) {
            return "message parse error！null data. 空数据";
        }

        if (0x68 == reg[0]) {
            //104命令解析
            RuleEntity ruleEntity = Analysis.Control(new int[]{reg[2], reg[3], reg[4], reg[5]});
            if ("I".equals(ruleEntity.getStructure())) {
                return IComParse.I.analysis(channel, reg, ruleEntity);
            } else if ("U".equals(ruleEntity.getStructure())) {
                return UComParse.I.analysis(channel, reg, ruleEntity);
            } else if ("S".equals(ruleEntity.getStructure())) {
                return SComParse.I.analysis(channel, reg, ruleEntity);
            }
            return " message parse error！未知的帧结构.";
        } else {
            return " message parse error！未知的协议.";
        }
    }

}
