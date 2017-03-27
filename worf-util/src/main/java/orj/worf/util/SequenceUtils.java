package orj.worf.util;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class SequenceUtils {

	/**
	 * 用于存放递增序列号，序列区间[1-999999].
	 */
	private static final ArrayBlockingQueue<String> SEQ_QUEUE = new ArrayBlockingQueue<String>(10000);
	private static final AtomicInteger COUNTER = new AtomicInteger(1);
	/**
	 * 当前机器IP地址的最后一位数字(IPV4).
	 */
	private static final String LAST_DIGIT_OF_IP;
	/**
	 * 递增序号基数,值为:LAST_DIGIT_OF_IP * 1000000.
	 */
	private static final int COUNTER_RADIX;

	static {
		String localIp = NetworkUtils.getLocalAddress();
		LAST_DIGIT_OF_IP = String.valueOf(localIp.charAt(localIp.length() - 1));
		COUNTER_RADIX = Integer.parseInt(LAST_DIGIT_OF_IP) * 1000000;
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				for (;;) {
					int currentIdx = COUNTER.getAndIncrement();
					if (currentIdx > 999999) {
						currentIdx = 1;
						COUNTER.set(2);
					}
					try {
						SEQ_QUEUE.put(Integer.toString(COUNTER_RADIX + currentIdx));
					} catch (InterruptedException e) {
					}
				}
			}
		});
		thread.setName("生成递增序号");
		thread.setDaemon(true);
		thread.start();
	}

	private SequenceUtils() {
	}

	/**
	 * 获取序列号，格式为(yyMMddHHmmssSSS+机器IP最后一位数字+6位数字,共22位长度)
	 */
	public static String getSequence() {
		String current = DateUtils.format("yyMMddHHmmssSSS", new Date());
		StringBuilder buff = new StringBuilder(22);
		buff.append(current);
		try {
			buff.append(SEQ_QUEUE.poll(500L, TimeUnit.MILLISECONDS));
		} catch (Exception e) {
			buff.append(LAST_DIGIT_OF_IP);
			buff.append(RandomStringGenerator.randomNumeric(6));
		}
		return buff.toString();
	}
}
