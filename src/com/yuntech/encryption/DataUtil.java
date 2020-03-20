package com.yuntech.encryption;
 
/**
 * 
 * @author wuyy
 * @since 20200228
 *
 */
public class DataUtil {
	public static byte[] int2bytes(int src) {
		byte[] rt = new byte[4];
		for(int i=0; i<4; ++i) {
			rt[i] = (byte)(src>>(i*8));
		}
		return rt;
	}
	public static int bytes2int(byte[] src) {
		int rt = 0;
		for(int i=0; i<4; ++i) {
			rt |= (src[i]&0xFF)<<(i*8);
			//字节在进行移位运算时，首先会被转换成int类型，
			//此时若字节的符号位为1，它前面就会补全1，比如：
			//0x80在byte类型时是-128，而转换成int，它的值还是
			//-128，即0xffffff80，而我们移位运算想要的是
			//0x00000080，即前面补全0，跟我们拆时一致。为此，
			//我们让它与0xFF相与，从0xffffff80变为0x00000080。
		}
		return rt;
	}
}