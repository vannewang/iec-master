package com.iec.test;

import com.iec.analysis.exception.IllegalFormatException;
import com.iec.analysis.exception.LengthException;
import com.iec.analysis.exception.UnknownTransferReasonException;
import com.iec.analysis.exception.UnknownTypeIdentifierException;
import com.iec.analysis.protocol104.Analysis;
import com.iec.assemble104.ContinuousAddressBuilder;
import com.iec.assemble104.UnContinuousAddressBuilder;
import com.iec.service.DeviceTCPService;
import com.iec.utils.Util;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import lombok.SneakyThrows;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhangyu
 * @create 2019/5/28 14:25
 */
public class Analysis104Test {

    @Test
    public void demo() {
        DeviceTCPService.INSTANCE.initTCPService();
    }


    @Test
    public void analysis() {

        try {
            String analysis = Analysis.analysis("68  0E  00  00  02  00  64  01  06  00  01  00  00  00  00  14".replaceAll(" ", ""));
            System.out.println(analysis);
        } catch (IllegalFormatException e) {
            e.printStackTrace();
        } catch (LengthException e) {
            e.printStackTrace();
        } catch (UnknownTransferReasonException e) {
            e.printStackTrace();
        } catch (UnknownTypeIdentifierException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void build() {
        UnContinuousAddressBuilder<Integer> integerUnContinuousAddressBuilder = new UnContinuousAddressBuilder<>(Util.getInformationTransmitFormat(0, 1), 100, 1, 6, 0, 0, 20);
        System.out.println(integerUnContinuousAddressBuilder.build().equals("68  0E  00  00  02  00  64  01  06  00  01  00  00  00  00  14".replaceAll(" ", "")));
    }


    @Test
    public void build1() {
        ContinuousAddressBuilder<Integer> integerUnContinuousAddressBuilder =
                new ContinuousAddressBuilder<>(Util.getNumberedSupervisoryFunction(4));
        System.out.println(integerUnContinuousAddressBuilder.buildSU().equals(("68  04  01  00  06  00 ").replaceAll(
                " ", "")));
    }


    @Test
    public void build2() {
        ContinuousAddressBuilder<Integer> integerUnContinuousAddressBuilder =
                new ContinuousAddressBuilder<>(Util.getUnnumberedControlFunction(true, false, false));
        System.out.println(integerUnContinuousAddressBuilder.buildSU());
        System.out.println(integerUnContinuousAddressBuilder.buildSU().equals(("68  04  43  00  00  00").replaceAll(
                " ", "")));
    }


    @SneakyThrows
    @Test
    public void build3() {

        String demo =
                "2321414D520A3C911716BE6679E1E001E7AFF0000000800000000000000000000000000000003C487724966679E1E001E7BAF0000000C00000000000000000000000000000003C550088B66679E1E001E7CFF0000000800000000000000000000000000000003C48F91F966679E1E001E78AF0000000C00000000000000000000000000000003C54FD1FB66679E1E001E7CFF0000000800000000000000000000000000000003C48F51F966679E1E001E78AF0000000C00000000000000000000000000000003C54FD1FB66679E1E001E7CFF0000000800000000000000000000000000000003C48F51F966679E1E001E78AF0000000C00000000000000000000000000000003C54FD1FB66679E1E001E7CFF0000000800000000000000000000000000000003C48F51F966679E1E001E78AF0000000C00000000000000000000000000000003C54FD1FB66679E1E001E7CFF0000000800000000000000000000000000000003C48F51F966679E1E001E78AF0000000C00000000000000000000000000000003C54FD1FB66679E1E001E7CFF0000000800000000000000000000000000000003C48F51F966679E1E001E78AF0000000C00000000000000000000000000000003C54FD1FB66679E1E001E7CFF0000000800000000000000000000000000000003C48F51F966679E1E001E78AF0000000C00000000000000000000000000000003C54FD1FB66679E1E001E7CFF0000000800000000000000000000000000000003C48F51F966679E1E001E78AF0000000C00000000000000000000000000000003C54FD1FB66679E1E001E7CFF0000000800000000000000000000000000000003C48F51F966679E1E001E78AF0000000C00000000000000000000000000000003C54FD1FB66679E1E001E7CFF0000000800000000000000000000000000000003C48F51F966679E1E001E78AF0000000C00000000000000000000000000000003C54FD1FB66679E1E001E7CFF0000000800000000000000000000000000000003C48F51F966679E1E001E78AF0000000C00000000000000000000000000000003C54FD1FB66679E1E001E7CFF0000000800000000000000000000000000000003C10023F1A4231E7EC8CC40BF71271304000047351C5E92C0000020D3C2F47203C44C0F82E026E76CE11857A982764382B59D39114FAC4D752B52BAB874AC0F03C0E06342607A7F7C5AAD14CCEB289B5E0ACE5DAFEE4B40BF90391047AE159F03C1A22D62E07EFFBDA4585D9F10417F6C6D0AE317391B6780731A80E4C2F6F303C160E242E1817F54D327E44DA288FA0197DE5326937222A8BD55C48EBC168803C16117F360D5FA60B5529EA4E5D3045E68B4A7B01F1B685FA2AB67449A492F03C1610242607E77ACD332B85D4B1A06D5D24CF3DFF1A2AE695C88AE34A4AD5303C1A186916073FFD5A447B4A66A25D79F409F249646F647E0A8D51C90ACCEFB03C1C1C1F1E06137D7A4580A50AA775925A58B184AF0532481FC102DB4AC0ECB03C1A1E0D7601FFF2D68D2ADB85E8B5636F83ADB27949AD4BBB8089D9B5DFB9503C1C12352603536DB2CD90A81758289717C2539405B10EF80168A67B600A22703C1A0E092603752FDA4586BA2210F5FC18217D0EE43E33F795BFDBE0D01401D03C1C13A01E07997E8F138C6A05AC3BD92BF43BB828787F9474ACFA44F67FFF203C1A18021E07DFCB6F15807814360354FA353E8EBA4D26A67CF0B7C1159942B03C1B781B760D6D7DDC33C0F5160EFBB877F180016DD415EF1A6CADE01BC6ED503C1A13951E19A7E17E012ABAEFE4C31704E7570EAF46B9353C2FB4254C0220603C1A1D8B1E1987DA7E092AE8CBCC1DA305AD3CF2E278008305DEDCA06F3CBFE03C161C241E18DDE97E092A9BB8E0C5F2402A96DB4F155A92DB4FF1B45108E1A03C1A277F16186FE03E192CADF30A13CCB942D041030E528B0BCDFA1B536937903C1C26242618636F9E01809CFBC158B336FA0DD12EF267E7F4FE52717DB939F03C1A2419760D5FF8169D845B9668A8137C59A86B19CCCFB11A8D563BA177B9D03C1211A32E18965B1E112A8AF159B6A9EC588D17C9DEC65C0B63EFA629979AA03C161A3C1619867BD68986EBC73BD034442AB3C1ED2F42CC3B828E45369967703C1606321E19D7E40B56D41B190BAAB55AEC03FB799B764C6DEA30D10EDB2D303C1EB80A1619E1FF8F1D3BA7E916489A21FC90A8E3127CD3DF3FA1C44EEB7A603C1A28D2161E11FD1E14D7BDB6E24DA4825D198B336349AC723208CD5FB15E303C20B8DC161E1DE07E0989ADCB7CCAA385EBBB5D2E435FAE2A15F7E4508CE6803C201D7F161F987D3AC5802D41FBB8B08459361660C681486F2B640378B940A03C1A358B161F9DE15C272BACF0F211121FD9A60D757A0D5B82A351B39EF548603C1E28240E1FB3E0FE032AFD8C52ABA19A0C227FC8C45BA7969E88BD7290BEF03C1AB9881E1F85EF0F1ED7AB39A73FD0FF4F2460549252798710E9D99D81C4903C1C1C34161F3B2C6F1382AD2DFBE3A48E62B8CEB8AA6E3AFA964DBC3FB845303C1B781A161FB12FDC22D5C83FAE81442DAAC0DC083337EBEDF8AB45B70459103C1211A5561FDE3DDE08D06C31C15B681FA31ED3FFFBA400DC7DE4CB485053303C1B79A81E1F3FE68B57829C8E844FFA075EA534CAFE98D51E90DA443D4770703C1E280D0E1E67F51E06D57C0427C551D5AE843DA85A171C8D07ACBBC01D77A03CD83415161B75F93E07902CF8869F5AF060CAE569E94EF81D7079E02229D6D03C1A281C161EC7E05E092EACD2B1A175C7C3EE767F72622932A5184A7FC050103C2023";
        byte[] decode = HexBin.decode(demo);
        byte[] bytes = byteSwitch(decode);
        System.out.println(bytes);
        writeFile("D:/", "a.amr", bytes);
    }

    @Test
    public void build4() {

        UnContinuousAddressBuilder<Integer> UnContinuousAddressBuilder =
                new UnContinuousAddressBuilder<>(Util.getInformationTransmitFormat(Integer.valueOf(0),
                        Integer.valueOf(0)), 100, 1, 6, 1, 0, 20);
        System.out.println(UnContinuousAddressBuilder.build());

    }


    /**
     * 将byte数组写入文件
     *
     * @param path
     * @param fileName
     * @param content
     * @throws IOException
     */
    public static void writeFile(String path, String fileName, byte[] content)
            throws IOException {
        try {
            File f = new File(path);
            if (!f.exists()) {
                f.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(path + fileName);
            fos.write(content);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private byte[] byteSwitch(byte[] data) {
        if ((null == data) || (data.length < 2)) {
            return data;
        }
        List<Byte> list = new ArrayList();//用来存放插入数据后新数组的集合
        for (int i = 0; i < data.length; i++) {
            if (data[i] == (byte) 126) {
                list.add(Byte.valueOf((byte) 125));
                list.add(Byte.valueOf((byte) 1));
            } else if (data[i] == (byte) 91) {
                list.add(Byte.valueOf((byte) 125));
                list.add(Byte.valueOf((byte) 2));
            } else if (data[i] == (byte) 93) {
                list.add(Byte.valueOf((byte) 125));
                list.add(Byte.valueOf((byte) 3));
            } else if (data[i] == (byte) 44) {
                list.add(Byte.valueOf((byte) 125));
                list.add(Byte.valueOf((byte) 4));
            } else if (data[i] == (byte) 42) {
                list.add(Byte.valueOf((byte) 125));
                list.add(Byte.valueOf((byte) 5));
            } else {
                list.add(Byte.valueOf(data[i]));
            }
        }
        byte[] returndata = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            returndata[i] = ((Byte) list.get(i)).byteValue();
        }
        return returndata;
    }
}
