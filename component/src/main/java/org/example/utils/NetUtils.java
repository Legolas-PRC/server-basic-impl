package org.example.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @ClassName: NetUtils
 * @Description:
 * @Author: LiaoMoran
 * @Date: 2025/1/14
 */
public class NetUtils {
	private static Logger logger = LoggerFactory.getLogger(NetUtils.class);

	/**
	 * @param defaultPort:
	 * @return int
	 * @author LiaoMoran
	 * @description 查询可用端口
	 * @date 2025/1/14 15:29
	 */
	public static int findAvailablePort(int defaultPort) {
		int portTmp = defaultPort;
		while (portTmp < 65535) {
			if (!isPortUsed(portTmp)) {
				return portTmp;
			} else {
				portTmp++;
			}
		}
		portTmp = defaultPort--;
		while (portTmp > 0) {
			if (!isPortUsed(portTmp)) {
				return portTmp;
			} else {
				portTmp--;
			}
		}
		throw new RuntimeException("no available port.");
	}

	/**
	 * @param port:
	 * @return boolean
	 * @author LiaoMoran
	 * @description 查询端口占用情况
	 * @date 2025/1/14 15:30
	 */
	public static boolean isPortUsed(int port) {
		boolean used = false;
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			used = false;
		} catch (IOException e) {
			logger.info(">>>>>>>>>>> xxl-job, port[{}] is in use.", port);
			used = true;
		} finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					logger.info("");
				}
			}
		}
		return used;
	}
}
