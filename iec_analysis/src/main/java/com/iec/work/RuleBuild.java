package com.iec.work;

import com.iec.analysis.protocol104.ASDU;
import com.iec.assemble104.ContinuousAddressBuilder;
import com.iec.assemble104.UnContinuousAddressBuilder;
import com.iec.utils.Constant;
import com.iec.utils.Util;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.Map;

/**
 * RuleBuild
 *
 * @author wxp
 * @version 1.0
 * @date 2020-09-11
 */
public class RuleBuild {


    /**
     * 建造I帧
     *
     * @return
     */
    protected String IBuild104(int TI, int VSQ, int transferReason, int infoAddress, int infoLength, int qualifier,
                               Date dateTime) {
        String sendNumber = Constant.CT_TEMP_MSG.get("sendNumber");
        if (ObjectUtils.isEmpty(sendNumber)) {
            Constant.CT_TEMP_MSG.put("sendNumber", "0");
            Constant.CT_TEMP_MSG.put("receiveNumber", "0");
        }
        sendNumber = Constant.CT_TEMP_MSG.get("sendNumber");
        String receiveNumber = Constant.CT_TEMP_MSG.get("receiveNumber");
        String result;
        Map<String, String> VSQMap = ASDU.variTureDete(VSQ);

        String informationTransmitFormat = Util.getInformationTransmitFormat(Integer.valueOf(sendNumber), Integer.valueOf(receiveNumber));
        if ("1".equals(VSQMap.get("order"))) {
            //连续值
            ContinuousAddressBuilder<Integer> integerContinuousAddressBuilder =
                    new ContinuousAddressBuilder<>(informationTransmitFormat,
                            TI, VSQ, transferReason, infoAddress, infoLength, qualifier,dateTime);
            result = integerContinuousAddressBuilder.build();
        } else {
            //不连续值

            UnContinuousAddressBuilder<Integer> integerUnContinuousAddressBuilder =
                    new UnContinuousAddressBuilder<>(informationTransmitFormat, TI, VSQ, transferReason, infoAddress, infoLength, qualifier,dateTime);
            result = integerUnContinuousAddressBuilder.build();
        }

        return result;
    }


    /**
     * im
     * 建造U帧
     *
     * @return
     */
    protected String UBuild104(boolean bool, String key) {
        ContinuousAddressBuilder<Integer> integerUnContinuousAddressBuilder =
                new ContinuousAddressBuilder<>(Util.getUnnumberedControlFunction(bool, key));
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
