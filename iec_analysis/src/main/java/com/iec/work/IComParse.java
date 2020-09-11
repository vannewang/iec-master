package com.iec.work;

import com.iec.analysis.common.TypeIdentifier;
import com.iec.analysis.protocol104.ASDU;
import com.iec.entity.RuleEntity;
import com.iec.utils.BytesUtils;
import com.iec.utils.Util;
import io.netty.channel.ChannelHandlerContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
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
        //发送序列号
        Integer sendOrder = BytesUtils.leToInt(reg, 2, 2);
        //接受序列号
        Integer receiveOrder = BytesUtils.leToInt(reg, 4, 2);
        int messageType = (int) reg[6];
        byte[] asdu = Arrays.copyOf(reg, reg.length - 6);
        System.arraycopy(reg, 6, asdu, 0, asdu.length);
        String describe = TypeIdentifier.getDescribe(messageType);
        log.error("类属性标识符:" + describe);
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
        String infoanalysis = ASDU.Infoanalysis(info, asdu[0], Integer.valueOf(tureDete.get("order")), Integer.valueOf(tureDete.get("number")));
        String result = "";
        switch (reason) {
            //突发(自发)
            case 3:
                break;
            //响应站召唤（总召唤）
            case 20:
                String icount = CT_TEMP_MSG.get("ICOUNT");
                if (icount == null) {
                    log.error("协议解析失败:响应站召唤（总召唤）");
                    return null;
                }
                result = SBuild104(Integer.valueOf(icount));
                break;
            default:
                return null;
        }
        return result;
    }
}
