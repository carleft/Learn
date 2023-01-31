package com.tb.tools;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AsyncTest {
    private static final String TAG = "Tools";
    private final MHandler mHandler;

    //单例
    private AsyncTest() {
        mHandler = new MHandler();
    }
    private static class Holder { static final AsyncTest sInstance = new AsyncTest(); }
    public static AsyncTest getInstance() { return Holder.sInstance; }


    /**
     * 任务结果回调接口
     */
    public interface MCallback {
        void onResult(String result);
    }

    /**
     * 自定义handler
     */
    private static class MHandler extends Handler {
        private static final int ON_BITMAP_READY = 0x00;
        private MCallback mCallback;
        public void setCallback(MCallback callback) {
            mCallback = callback;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == ON_BITMAP_READY &&
                    msg.obj instanceof String &&
                    mCallback != null) {
                mCallback.onResult((String) msg.obj);
            }
        }
    }

    /**
     * 模拟一个异步任务
     * @return
     */
    public String asyncJob()  {
        for (int i = 0; i < 10; i ++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TBLog.e(TAG, "callable is running, count = " + i + " currentThread = " + Thread.currentThread());
        }
        return "Hello World";
    }



    //java实现异步任务

    //1.Thread+Handler
    //Handler的主要作用是将消息发送回主线程，完成线程切换
    public void AsyncImpl1(MCallback callback) {
        //主线程Handler设置回调
        mHandler.setCallback(callback);
        //启动任务
        new Thread(new Runnable() {
            @Override
            public void run() {
                //子线程执行耗时操作
                String result = asyncJob();
                //加载完成后通过Handler通知回调接口
                mHandler.sendMessage(Message.obtain(mHandler, MHandler.ON_BITMAP_READY, result));
            }
        }).start();
    }

    //2.AsyncTask
    //doInBackground方法执行在子线程
    //onPostExecute方法执行在主线程
    public void AsyncImpl2(MCallback callback) {
        new MAsyncTask(callback).execute();
    }
    //自定义AsyncTask
    private static class MAsyncTask extends AsyncTask<Void, Void, String> {
        private MCallback mCallback;
        public MAsyncTask(MCallback callback) {
            mCallback = callback;
        }

        protected String doInBackground(Void... voids) {
            //子线程执行耗时操作
            return AsyncTest.getInstance().asyncJob();
        }

        @Override
        protected void onPostExecute(String s) {
            mCallback.onResult(s);
        }
    }

    //3.HandlerThread
    public void AsyncImpl3(MCallback callback) {
        //主线程Handler设置回调
        mHandler.setCallback(callback);
        //创建HandlerThread对象，name表示线程名称
        HandlerThread handlerThread = new HandlerThread("TB0232");
        //开启线程，否则getLooper()返回为空
        handlerThread.start();
        //构建子线程handler
        Handler subHandler = new Handler(handlerThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                //子线程执行耗时操作
                Message m = Message.obtain(mHandler, 0, AsyncTest.getInstance().asyncJob());
                //主线程Handler发送消息
                mHandler.sendMessage(m);
                return false;
            }
        });
        //执行任务
        subHandler.sendEmptyMessage(0);
    }

    //4.Executor
    public void AsyncImpl4(MCallback callback) {
        //主线程Handler设置回调
        mHandler.setCallback(callback);
        //创建线程池
        ExecutorService service = Executors.newCachedThreadPool();
        //创建Callable对象
        Callable<String> task = new Callable<String>() {
            @Override
            public String call() throws Exception {
                //子线程执行耗时操作
                return asyncJob();
            }
        };
        //提交任务
        Future<String> future = service.submit(task);
        //TODO: get方法依然阻塞，如何解决？
        try {
            callback.onResult(future.get());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            //关闭线程池
            service.shutdown();
        }
    }


}
