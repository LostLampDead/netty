package com.choice.netty.task.advance;

/**
 * ScheduledFutureTask 是 Java 中 ScheduledExecutorService 接口的一个实现类，用于在指定的延迟时间后执行或按固定的周期执行任务。
 * ScheduledFutureTask 继承了 FutureTask 类，因此具有 FutureTask 的所有特性，同时还有以下特性：
 * - 可以在指定的延迟时间后执行，也可以按固定的周期执行。
 * - 可以取消任务的执行，可以查询任务的执行状态和结果，也可以等待任务执行完成。
 * - ScheduledFutureTask 可以在执行时捕获并处理异常，或者通过 get() 方法获取任务执行结果。

 * 基本属性
 * deadline：表示任务的截止时间，也就是任务最晚的执行时间。该属性是一个 long 类型的时间戳，用 System.nanoTime() 方法获取。
 * period：表示任务的周期时间，如果为 0 表示任务只执行一次。该属性是一个 long 类型的时间戳，用 TimeUnit 枚举类表示。
 * isPeriodic：表示该任务是否是周期性任务。
 * sequenceNumber：表示该任务在 ScheduledThreadPoolExecutor 内部的序列号，用于保证任务执行的顺序。
 * callable、runnable：表示任务需要执行的操作，可以是 Runnable 或 Callable 接口的实现类。
 * outcome：表示任务执行的结果，是一个 Object 类型的变量，可以通过 get() 方法获取任务执行结果。
 */
public class ExploreScheduledFutureTask {

    public static void main(String[] args) throws Exception {
        // ScheduledFutureTask —> Netty，没有被 public 修饰，无法演示
        /*ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        ScheduledFutureTask futureTask = new ScheduledFutureTask(new Runnable() {
            @Override
            public void run() {
                // 执行任务代码
            }
        }, 1, TimeUnit.SECONDS);

        executorService.schedule(futureTask, 1, TimeUnit.SECONDS);
        */
    }
}
