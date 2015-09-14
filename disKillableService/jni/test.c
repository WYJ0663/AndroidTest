#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <signal.h>
#include <unistd.h>
#include <android/log.h>
#include <sys/inotify.h>
#include <sys/ptrace.h>
#include <fcntl.h>

#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "native", __VA_ARGS__))
#define LOGW(...) ((void)__android_log_print(ANDROID_LOG_WARN, "native", __VA_ARGS__))
#define LOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR, "native", __VA_ARGS__))

static int ok = -1;

void handler(int signo) {
	if (ok == 1) {
		pid_t pid = fork();
		LOGI("success handler pid->%d\n", pid);
		if (pid < 0) {
			LOGI("fork fail\n");
			exit(-1);
		} else if (pid == 0) {
			sleep(1);
			//4.2 以下 am start -a android.intent.action.VIEW -d http://www.baidu.com
			//4.2 (包含)以上 am start --user 0 -a android.intent.action.VIEW -d http://www.baidu.com
			//4.2 (包含)以上 am startservice --user 0 -n com.xxx.xxx/com.xxx.service.xxx
			int lockFileDescriptor = open("/data/data/com.example.jnitest",
					O_RDONLY);
			if (lockFileDescriptor == -1) {
				//应用已被卸载
				LOGI("app h uninstalled\n");
			} else {
				//应用被杀死
				LOGI("app has been killed\n");
				execlp("am", "am", "startservice", "--user", "0", "-n",
						"com.example.jnitest/com.example.jnitest.TestService",
						(char *) NULL);
			}
		} else {
			exit(0);
		}
	}
}

void test() {
	pid_t pp = getpid();
	pid_t pid = fork();
	if (0 == pid) {
		LOGI("start signal");
		signal(SIGCHLD, handler);
		ptrace(PTRACE_ATTACH, pp, NULL, NULL);
		wait (NULL);
		ptrace(PTRACE_CONT, pp, NULL, NULL);
		ok = 1;
		pause();
	} else if (pid < 0) {
		LOGI("fork fail\n");
		exit(-1);
	}
}
