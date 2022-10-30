package com.comport.comports.portwrapper;

import com.comport.comports.portwrapper.utils.ByteUtils;

import java.nio.charset.StandardCharsets;
import java.util.stream.IntStream;

import static com.comport.comports.portwrapper.utils.DataDecoder.decode;
import static com.comport.comports.portwrapper.utils.DataEncoder.encode;

public class PacketBuilder {

    public static void main(String[] args) {
        byte[] toEncode = ("Hel0lo wor0ld!visndvsnovn9043hf-908h3fhiodhsnonfsljdnvl;" +
                "knsdlknfsndfsfmjklsdfmklsdnvklns;lnvl;sdnvlk;jsdnv;jnuvwbnpuwbvpw" +
                "qeqweqwkkdjmklnjsikjdfsjdfkl;sjdkfljsdkl;jfjsdifeqwe99937r843hfd" +
                "aksmnfokivndspfoubdbnpioubwepiuvbpuiervuibpiuerbvpurevbpuierbvuibev" +
                "dasdkjmm cmxkcn[oj12390e302  23r  92r i1 1-0pjd[ijf[asd=-0-=-skdkskldsk" +
                "fksdfnniuowefhuirefjisaaaaaaaaaaaaadiieieieiiiiiiiiiiiiiiiiiidijskdks")
                .getBytes(StandardCharsets.US_ASCII);
        toEncode[0] = ByteUtils.intToByte(0xff);
        toEncode[2] = 0x00;
        toEncode[7] = 0x00;

        System.out.println("Original value:");
        IntStream.range(0, toEncode.length).forEach(i -> System.out.print(ByteUtils.byteToInt(toEncode[i]) + " "));

        System.out.println();

        byte[] encoded = encode(toEncode);
        System.out.println("Encoded value:");
        IntStream.range(0, encoded.length).forEach(i -> System.out.print(ByteUtils.byteToInt(encoded[i]) + " "));

        System.out.println();

        byte[] decoded = decode(encoded);
        System.out.println("Decoded value:");
        IntStream.range(0, decoded.length).forEach(i -> System.out.print(ByteUtils.byteToInt(decoded[i]) + " "));
    }
}
