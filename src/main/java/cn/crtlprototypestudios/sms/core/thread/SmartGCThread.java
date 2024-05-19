package cn.crtlprototypestudios.sms.core.thread;

import cn.crtlprototypestudios.sms.core.util.GCUtil;

import java.util.concurrent.CountDownLatch;

/**
 * 实现智能GC的多线程类<p>
 * 该线程会在后台运行，当GC完成后会调用onSuccess()方法<p>
 * 可使用getGcUtil()方法获取GCUtil对象，该对象可获取本次GC的相关信息<p>
 *
 * @see GCUtil
 * @author Tki_sor
 */
public abstract class SmartGCThread extends Thread {

    @Override
    public void run() {
        GCUtil gcUtil = new GCUtil();
        onStarted(gcUtil);
        var latch = new CountDownLatch(1);
        gcUtil.smartGC(latch);
        try {
            latch.await();
        } catch (InterruptedException ignored) {
        }
        if (gcUtil.getIsGC()) {
            onSuccess(gcUtil);
        } else {
            onFailed(gcUtil);
        }
    }

    public abstract void onStarted(GCUtil gcUtil);
    public abstract void onSuccess(GCUtil gcUtil);
    public abstract void onFailed(GCUtil gcUtil);
}
