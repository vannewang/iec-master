package com.iec.work;

import com.iec.analysis.common.RuleCommand;
import com.iec.assemble104.ContinuousAddressBuilder;
import com.iec.utils.Util;

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
     * @return
     */
    protected String IBuild104() {


        return null;
    }


    /**
     * 建造U帧
     * @return
     */
    protected String UBuild104(boolean tester, boolean stopdt, boolean startdt) {
        ContinuousAddressBuilder<Integer> integerUnContinuousAddressBuilder =
                new ContinuousAddressBuilder<>(Util.getUnnumberedControlFunction(tester, stopdt, startdt));
        return integerUnContinuousAddressBuilder.buildSU();
    }


    /**
     * 建造S帧
     * @param iCount
     * @return
     */
    protected String SBuild104(Integer iCount) {
        ContinuousAddressBuilder<Integer> integerUnContinuousAddressBuilder =
                new ContinuousAddressBuilder<>(Util.getNumberedSupervisoryFunction(iCount));
        return integerUnContinuousAddressBuilder.buildSU();
    }

}
