package com.iec.work;

import com.iec.analysis.common.TransferReason;
import com.iec.analysis.common.TypeIdentifier;
import com.iec.analysis.exception.UnknownTransferReasonException;
import com.iec.analysis.exception.UnknownTypeIdentifierException;
import com.iec.entity.RuleEntity;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * UComParse
 *
 * @author wxp
 * @version 1.0
 * @date 2020-09-10
 */
@Slf4j
public class UComParse extends RuleBuild {
    public static UComParse I = new UComParse();

    private UComParse() {
    }


    public String analysis(ChannelHandlerContext channel, byte[] reg, RuleEntity ruleEntity) {

        String order = ruleEntity.getAttach1();
        boolean bool = Boolean.valueOf(ruleEntity.getAttach2());
        String result = "";
        switch (order) {
            //链路测试TESTFR
            case "TESTFR":
                if (!bool) {
                    result = UBuild104(true,"TESTFR");
                } else {
//                    result = UBuild104();
                }
                break;
            //断开数据传输STOPDT
            case "STOPDT":
                if (!bool) {
//                    result = IBuild104(100, 1, 6, 0, 0, 20);
                }
                break;
            //启动数据传输STARTDT
            case "STARTDT":
                if (!bool) {
//                    result = IBuild104(100, 1, 6, 0, 0, 20);
                } else {
                    //TI
                    int code = 0;
                    try {
                        code = TypeIdentifier.code(TypeIdentifier.CALL_COMMAND);
                    } catch (UnknownTypeIdentifierException e) {
                        e.printStackTrace();
                    }
                    //TODO 可变结构限定词 1代表不连续值并且只有一个信息组,8f（1000 1111），表示数据为连续数值，数量为15
                    int VSQ = 1;
                    //传输原因COT transferReason
                    int transferReason = 0;
                    try {
                        transferReason = TransferReason.code(TransferReason.ACTIVATE);
                    } catch (UnknownTransferReasonException e) {
                        e.printStackTrace();
                    }
                    //TODO ASDU公共地址
                    int infoAddress = 1;
                    //TODO infoLength 信息体长度
                    int infoLength = 0;
                    //TODO qualifier      限定词
                    int qualifier = 20;
                    result = IBuild104(code, VSQ, transferReason, infoAddress, infoLength, qualifier);
                }
                break;
            default:
                log.error("未知U帧，解析失败!");
        }

        return result;
    }

}
