/*
 	Utils
  Class for contining utility functions.

  This file is part of JavaDBF packege.

  author: anil@linuxense.com
  license: LGPL (http://www.gnu.org/copyleft/lesser.html)

  $Id: Utils.java,v 1.7 2004/03/31 16:00:34 anil Exp $
*/
package trs.linuxense.javadbf;

import java.io.*;
import java.util.*;
import java.text.*;

/**
 * Miscelaneous functions required by the JavaDBF package.
 */
public final class Utils {

    private static final int ALIGN_LEFT = 10;
    public static final int ALIGN_RIGHT = 12;

    private Utils() {
    }

    public static int readLittleEndianInt(DataInput in)
            throws IOException {
        int bigEndian = 0;
        for (int shiftBy = 0; shiftBy < 32; shiftBy += 8) {
            bigEndian |= (in.readUnsignedByte() & 0xff) << shiftBy;
        }
        return bigEndian;
    }

    public static short readLittleEndianShort(DataInput in)
            throws IOException {
        int low = in.readUnsignedByte() & 0xff;
        int high = in.readUnsignedByte();
        return (short) (high << 8 | low);
    }

    public static byte[] trimLeftSpaces(byte[] arr) {
        StringBuffer t_sb = new StringBuffer(arr.length);
        for (byte anArr : arr) {
            if (anArr != ' ') {
                t_sb.append((char) anArr);
            }
        }
        return t_sb.toString().getBytes();
    }

    public static short littleEndian(short value) {
        short mask = (short) 0xff;
        short num2 = (short) (value & mask);
        num2 <<= 8;
        mask <<= 8;
        num2 |= (value & mask) >> 8;
        return num2;
    }

    public static int littleEndian(int value) {
        int mask = 0xff;
        int num2 = 0x00;
        num2 |= value & mask;
        for (int i = 1; i < 4; i++) {
            num2 <<= 8;
            mask <<= 8;
            num2 |= (value & mask) >> (8 * i);
        }
        return num2;
    }

    public static byte[] textPadding(String text, String characterSetName, int length) throws UnsupportedEncodingException {
        return textPadding(text, characterSetName, length, Utils.ALIGN_LEFT);
    }

    public static byte[] textPadding(String text, String characterSetName, int length, int alignment) throws UnsupportedEncodingException {
        return textPadding(text, characterSetName, length, alignment, (byte) ' ');
    }

    public static byte[] textPadding(String text, String characterSetName, int length, int alignment,
                                     byte paddingByte) throws UnsupportedEncodingException {
        if (text.length() >= length) {
            return text.substring(0, length).getBytes(characterSetName);
        }
        byte byte_array[] = new byte[length];
        Arrays.fill(byte_array, paddingByte);
        //支持中文字符长度
        byte[] bytes = text.getBytes(characterSetName);
        switch (alignment) {
            case ALIGN_LEFT:
                System.arraycopy(bytes, 0, byte_array, 0, bytes.length);
                break;
            case ALIGN_RIGHT:
                int t_offset = length - text.length();
                System.arraycopy(bytes, 0, byte_array, t_offset, bytes.length);
                break;
        }
        return byte_array;
    }

    public static byte[] doubleFormating(Double doubleNum, String characterSetName, int fieldLength, int sizeDecimalPart) throws UnsupportedEncodingException {
        int sizeWholePart = fieldLength - (sizeDecimalPart > 0 ? (sizeDecimalPart + 1) : 0);
        StringBuffer format = new StringBuffer(fieldLength);
        for (int i = 0; i < sizeWholePart; i++) {
            format.append("#");
        }
        if (sizeDecimalPart > 0) {
            format.append(".");
            for (int i = 0; i < sizeDecimalPart; i++) {
                format.append("0");
            }
        }
        DecimalFormat df = new DecimalFormat(format.toString());
        return textPadding(df.format(doubleNum.doubleValue()), characterSetName, fieldLength, ALIGN_RIGHT);
    }

    public static boolean contains(byte[] arr, byte value) {
        boolean found = false;
        for (byte anArr : arr) {
            if (anArr == value) {
                found = true;
                break;
            }
        }
        return found;
    }
}
