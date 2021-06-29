package com.example.demo.common;

/**
 * XORShift乱数生成器
 */
public class XORShiftRandom {

	private long last;

	public XORShiftRandom(long seed) {
		super();
		this.last = seed;
	}

	public XORShiftRandom() {
		this(System.currentTimeMillis());
	}

	public int nextInt(int max) {
		last ^= (last << 21);
		last ^= (last >>> 35);
		last ^= (last << 4);
		int out = (int) last % max;
		return (out < 0) ? -out : out;
	}

}
