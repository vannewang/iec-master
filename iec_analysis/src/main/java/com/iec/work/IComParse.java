package com.iec.work;

import com.iec.analysis.common.TransferReason;
import com.iec.analysis.common.TypeIdentifier;
import com.iec.analysis.protocol104.ASDU;
import com.iec.entity.RuleEntity;
import com.iec.utils.BytesUtils;
import com.iec.utils.Util;
import io.netty.channel.ChannelHandlerContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import static com.iec.utils.Constant.CT_TEMP_MSG;


/**
 * IComParse
 *
 * @author wxp
 * @version 1.0
 * @date 2020-09-10
 */
@Slf4j
public class IComParse extends RuleBuild {
    public static IComParse I = new IComParse();

    private IComParse() {
    }

    @SneakyThrows
    public String analysis(ChannelHandlerContext channel, byte[] reg, RuleEntity ruleEntity) {
        Integer length = Integer.valueOf(reg[1]);
        //发送序列号
        Integer sendOrder = BytesUtils.leToInt(reg, 2, 2);
        //接受序列号
        Integer receiveOrder = BytesUtils.leToInt(reg, 4, 2);
        int TI = (int) reg[6];
        TypeIdentifier type = TypeIdentifier.type(TI);
        byte[] asdu = Arrays.copyOf(reg, reg.length - 6);
        System.arraycopy(reg, 6, asdu, 0, asdu.length);
        log.error("类属性标识符:" + TI);
        //可变结构限定词
        Map<String, String> tureDete = ASDU.variTureDete(asdu[1]);
        //传送原因[9 byte - 10 byte]
        int reason = BytesUtils.leToInt(reg, 8, 2);
        //应用服务数据单元公共地址[11 byte - 12 byte]
        String address = Util.address(asdu[4], asdu[5]);
        int[] info = new int[asdu.length - 6];
        // 将[信息元素+限定词+（时标）]装入数组info
        for (int i = 0; i < info.length; i++) {
            info[i] = asdu[6 + i];
        }
        String order = tureDete.get("order");
        String number = tureDete.get("number");
        /**
         * 解析协议数据
         */
        String infoanalysis = ASDU.Infoanalysis(info, asdu[0], Integer.valueOf(order), Integer.valueOf(number));
        String result = "";
        String iCount = CT_TEMP_MSG.get("ICOUNT");
        if (iCount == null) {
            log.error("协议解析失败:" + type.getDescribe());
            return null;
        }
        switch (type) {
            //不带时标的单点信息
            case SINGLE_POINT:
                //单点遥信 YX帧 响应总召唤
                result = SBuild104(Integer.valueOf(iCount));
                break;
            //召唤命令
            case CALL_COMMAND:

                //激活确认
                if (reason == TransferReason.ACTIVATE_CONFIRMATION.getCode()) {
                    result = SBuild104(Integer.valueOf(iCount));
                } else if (reason == TransferReason.ACTIVATE_TERMINATION.getCode()) {
                    //激活终止
                    int code = TypeIdentifier.code(TypeIdentifier.CALL_COMMAND);
                    int VSQ = 1;
                    int infoAddress = 1;
                    //TODO infoLength 信息体长度
                    int infoLength = 0;
                    //TODO qualifier      限定词
                    int qualifier = -1;
                    result = IBuild104(TypeIdentifier.code(TypeIdentifier.CLOCK_SYNCHRONIZATIon), VSQ, TransferReason.ACTIVATE.getCode(), infoAddress, infoLength, qualifier, new Date());
                }

                break;
            default:
                return null;
        }
        return result;
    }
}
