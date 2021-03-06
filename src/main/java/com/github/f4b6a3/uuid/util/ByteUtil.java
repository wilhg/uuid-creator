/*
 * MIT License
 * 
 * Copyright (c) 2018-2019 Fabio Lima
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.f4b6a3.uuid.util;

/**
 * Class that contains many static methods for byte handling.
 */
public class ByteUtil {
	
	private ByteUtil() {
	}

	/**
	 * Get a number from a given hexadecimal string.
	 *
	 * @param hexadecimal a string
	 * @return a long
	 */
	public static long toNumber(final String hexadecimal) {
		return toNumber(toBytes(hexadecimal));
	}

	/**
	 * Get a number from a given array of bytes.
	 * 
	 * @param bytes a byte array
	 * @return a long
	 */
	public static long toNumber(final byte[] bytes) {
		return toNumber(bytes, 0, bytes.length);
	}
	
	public static long toNumber(final byte[] bytes, final int start, final int end) {
		long result = 0;
		for (int i = start; i < end; i++) {
			result = (result << 8) | (bytes[i] & 0xff);
		}
		return result;
	}
	
	/**
	 * Get an array of bytes from a given number.
	 *
	 * @param number a long value
	 * @return a byte array
	 */
	public static byte[] toBytes(final long number) {
		final int length = 8;
		byte[] bytes = new byte[length];
		for (int i = 0; i < length; i++) {
			bytes[i] = (byte) (number >>> (8 * ((length - 1) - i)));
		}
		return bytes;
	}

	/**
	 * Get an array of bytes from a given hexadecimal string.
	 *
	 * @param hexadecimal a string
	 * @return a byte array
	 */
	public static byte[] toBytes(final String hexadecimal) {
		final int length = hexadecimal.length();
		byte[] bytes = new byte[length / 2];
		for (int i = 0; i < length; i += 2) {
			bytes[i / 2] = (byte) ((fromHexChar(hexadecimal.charAt(i)) << 4) | fromHexChar(hexadecimal.charAt(i + 1)));
		}
		return bytes;
	}

	/**
	 * Get a hexadecimal string from given array of bytes.
	 *
	 * @param bytes byte array
	 * @return a string
	 */
	public static String toHexadecimal(final byte[] bytes) {
		final int length = bytes.length;
		final char[] hexadecimal = new char[length * 2];
		for (int i = 0; i < length; i++) {
			final int v = bytes[i] & 0xFF;
			hexadecimal[i * 2] = toHexChar(v >>> 4);
			hexadecimal[(i * 2) + 1] = toHexChar(v & 0x0F);
		}
		return new String(hexadecimal);
	}

	/**
	 * Get a hexadecimal string from given number.
	 * 
	 * @param number an integer
	 * @return a string
	 */
	public static String toHexadecimal(final long number) {
		return toHexadecimal(toBytes(number));
	}

	/**
	 * Get a number value from a hexadecimal char.
	 * 
	 * @param chr a character
	 * @return an integer
	 */
	public static int fromHexChar(final char chr) {

		if (chr >= 0x61 && chr <= 0x66) {
			// ASCII codes from 'a' to 'f'
			return (int) chr - 0x57;
		} else if (chr >= 0x41 && chr <= 0x46) {
			// ASCII codes from 'A' to 'F'
			return (int) chr - 0x37;
		} else if (chr >= 0x30 && chr <= 0x39) {
			// ASCII codes from 0 to 9
			return (int) chr - 0x30;
		}

		return 0;
	}

	/**
	 * Get a hexadecimal from a number value.
	 * 
	 * @param number an integer
	 * @return a char
	 */
	public static char toHexChar(final int number) {
		if (number >= 0x0a && number <= 0x0f) {
			// ASCII codes from 'a' to 'f'
			return (char) (0x57 + number);
		} else if (number >= 0x00 && number <= 0x09) {
			// ASCII codes from 0 to 9
			return (char) (0x30 + number);
		}
		return 0;
	}

	/**
	 * Get a new array with a specific length and filled with a byte value.
	 *
	 * @param length array size
	 * @param value byte value
	 * @return a byte array
	 */
	public static byte[] array(final int length, final byte value) {
		final byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			result[i] = value;
		}
		return result;
	}

	/**
	 * Copy an entire array.
	 *
	 * @param bytes byte array
	 * @return a byte array
	 */
	public static byte[] copy(final byte[] bytes) {
		return copy(bytes, 0, bytes.length);
	}

	/**
	 * Copy part of an array.
	 *
	 * @param bytes byte array
	 * @param start start position
	 * @param end end position
	 * @return a byte array
	 */
	public static byte[] copy(final byte[] bytes, final int start, final int end) {
		final int length = end - start;
		final byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			result[i] = bytes[start + i];
		}
		return result;
	}

	/**
	 * Concatenates two byte arrays.
	 * 
	 * @param bytes1 byte array 1
	 * @param bytes2 byte array 2
	 * @return a byte array
	 */
	public static byte[] concat(final byte[] bytes1, final byte[] bytes2) {
		final int length1 = bytes1.length;
		final int length2 = bytes2.length;
		final byte[] result = new byte[length1 + length2];
		for (int i = 0; i < length1; i++) {
			result[i] = bytes1[i];
		}
		for (int j = 0; j < length2; j++) {
			result[length1 + j] = bytes2[j];
		}
		return result;
	}

	/**
	 * Replace part of an array of bytes with another subarray of bytes and
	 * starting from a given index.
	 *
	 * @param bytes byte array
	 * @param replacement replacement byte array
	 * @param index start position
	 * @return a byte array
	 */
	public static byte[] replace(final byte[] bytes, final byte[] replacement, final int index) {

		byte[] result = new byte[bytes.length];

		for (int i = 0; i < index; i++) {
			result[i] = bytes[i];
		}

		for (int i = 0; i < replacement.length; i++) {
			result[index + i] = replacement[i];
		}
		return result;
	}

	/**
	 * Check if two arrays of bytes are equal.
	 *
	 * @param bytes1 byte array 1
	 * @param bytes2 byte array 2
	 * @return a boolean
	 */
	public static boolean equalArrays(final byte[] bytes1, final byte[] bytes2) {
		if (bytes1.length != bytes2.length) {
			return false;
		}
		for (int i = 0; i < bytes1.length; i++) {
			if (bytes1[i] != bytes2[i]) {
				return false;
			}
		}
		return true;
	}
}
