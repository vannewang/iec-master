package com.iec.work;

import com.iec.analysis.common.RuleCommand;
import com.iec.analysis.protocol104.ASDU;
import com.iec.assemble104.ContinuousAddressBuilder;
import com.iec.assemble104.UnContinuousAddressBuilder;
import com.iec.utils.Constant;
import com.iec.utils.Util;
import org.springframework.util.ObjectUtils;

import java.util.Map;

/**
 * RuleBuild
 *
 * @author wxp
 * @version 1.0
 * @date 2020-09-11
 */
public class RuleBuild implements RuleCommand {


    /**
     * 建造I帧
     *
     * @return
     */
    protected String IBuild104(int TI, int VSQ, int transferReason, int infoAddress, int infoLength, int qualifier) {
        String sendNumber = Constant.CT_TEMP_MSG.get("sendNumber");
        String receiveNumber = Constant.CT_TEMP_MSG.get("receiveNumber");
        if (ObjectUtils.isEmpty(sendNumber)) {
            Constant.CT_TEMP_MSG.put("sendNumber", "0");
            Constant.CT_TEMP_MSG.put("receiveNumber", "0");
        }
        String result;
        Map<String, String> VSQMap = ASDU.variTureDete(VSQ);

        if ("1".equals(VSQMap.get("order"))) {
            //连续值
            ContinuousAddressBuilder<Integer> integerContinuousAddressBuilder =
                    new ContinuousAddressBuilder<>(Util.getInformationTransmitFormat(Integer.valueOf(sendNumber),
                            Integer.valueOf(receiveNumber)),
                            TI, VSQ, transferReason, infoAddress, infoLength, qualifier);
            result = integerContinuousAddressBuilder.build();
        } else {
            //不连续值
            UnContinuousAddressBuilder<Integer> integerUnContinuousAddressBuilder = new UnContinuousAddressBuilder<>(Util.getInformationTransmitFormat(Integer.valueOf(sendNumber),
                    Integer.valueOf(receiveNumber)), TI, VSQ, transferReason, infoAddress, infoLength, qualifier);
            result = integerUnContinuousAddressBuilder.build();
        }

        return result;
    }


    /**
     * 建造U帧
     *
     * @return
     */
    protected String UBuild104(boolean tester, boolean stopdt, boolean startdt) {
        ContinuousAddressBuilder<Integer> integerUnContinuousAddressBuilder =
                new ContinuousAddressBuilder<>(Util.getUnnumberedControlFunction(tester, stopdt, startdt));
        return integerUnContinuousAddressBuilder.buildSU();
    }


    /**
     * 建造S帧
     *
     * @param iCount
     * @return
     */
    protected String SBuild104(Integer iCount) {
        ContinuousAddressBuilder<Integer> integerUnContinuousAddressBuilder =
                new ContinuousAddressBuilder<>(Util.getNumberedSupervisoryFunction(iCount));
        return integerUnContinuousAddressBuilder.buildSU();
    }

}
