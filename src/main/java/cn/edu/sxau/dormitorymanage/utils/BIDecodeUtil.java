package cn.edu.sxau.dormitorymanage.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

/**
 * 智能解码器
 */
public final class BIDecodeUtil {

	private static final CharsetDecoder[]	DECODER_ARRAY	= new CharsetDecoder[] {
			Charset.forName("UTF-8").newDecoder(), Charset.forName("GBK").newDecoder(),
            Charset.forName("GB2312").newDecoder(), Charset.forName("GB18030").newDecoder(),
            Charset.forName("BIG5").newDecoder()       };

    /**
     * 目前支持UTF-8,GBK,GB2312
     * @param b
     * @return
     */
    public static String decode(byte[] b) {
        ByteBuffer bb = ByteBuffer.wrap(b);
        CharBuffer cb = CharBuffer.allocate(b.length);
        for (CharsetDecoder decoder : DECODER_ARRAY) {
            decoder.reset();
            CoderResult cr = decoder.decode(bb, cb, true);
            if (!cr.isError()) {
                decoder.flush(cb);
                break;
            }
        }
        return new String(cb.array(), 0, cb.position());// cb.position() 有效的长度
    }

    /**
     * 指定编码解码
     * @param b
     * @param encode
     * @return
     */
    public static String decode(byte[] b, String encode) {
        ByteBuffer bb = ByteBuffer.wrap(b);
        CharBuffer cb = CharBuffer.allocate(bb.capacity());
        CharsetDecoder decoder = Charset.forName(encode).newDecoder();
        CoderResult cr = decoder.decode(bb, cb, true);
        if (cr.isError()) {// 解码失败
            return new String(b);
        }
        decoder.flush(cb);
        return new String(cb.array(), 0, cb.position());
    }

    public static void main(String[] args) {
        System.out.println(BIDecodeUtil.decode(new String("你好!").getBytes(),"utf-8"));
    }

}
