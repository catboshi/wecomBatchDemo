package tech.wedev.wecombatch.task;

import java.util.concurrent.RecursiveTask;

/**
 *  var forkJoinPool = new ForkJoinPool(10);
 *  forkJoinPool.submit(()->{});
 *
 * @link https://www.modb.pro/db/508051
 */
public class ExportForkJoin extends RecursiveTask<Long> {

    /**
     * 累加起始值
     */
    private final Long start;
    /**
     * 累加结束值
     */
    private final Long end;

    public ExportForkJoin(Long start, Long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        // 该值用来确定最小子任务，当递归拆分还剩10000时，不再拆分，否则无限拆分会报AQS / 栈溢出StackOverflowError
        long temp = 10000L;
        if (end - start < temp) {
            long sum = 0L;
            for (Long i = start; i <= end ; i++) {
                sum += i;
            }
            return sum;
        } else {
            // 中间值，递归拆分
            long middle = (start + end) / 2;

            // fork：将任务拆分并压入线程队列
            ExportForkJoin task1 = new ExportForkJoin(start, middle);
            ExportForkJoin task2 = new ExportForkJoin(middle + 1, end);
            /*
             * 方式一：
             * task1.fork();
             * task2.fork();
             * return task2.join() + task1.join();
             *
             * 方式二：
             * invokeAll(task1, task2);
             * return task1.join() + task2.join();
             *
             * 方式三：
             * task1.fork();
             * return task2.compute() + task1.join();
             */
            invokeAll(task1, task2);
            // join：结果合并
            return task1.join() + task2.join();
        }
    }
}
