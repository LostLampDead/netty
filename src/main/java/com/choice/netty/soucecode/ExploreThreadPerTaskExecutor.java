package com.choice.netty.soucecode;

import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.ThreadPerTaskExecutor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 在 Netty 中，ThreadPerTaskExecutor 是一种执行器（Executor），它会为每个任务创建一个新的线程来执行。
 * 它的作用是让每个任务都在自己的线程中执行，以避免任务之间的相互影响，从而提高系统的并发能力和响应性能。
 * 在 Netty 中，ThreadPerTaskExecutor 经常被用来执行一些需要长时间运行的任务，比如处理 I/O 事件、执行计算密集型任务等。
 * 它的工作原理是创建一个新的线程来执行每个任务，这样可以避免任务之间的相互影响，从而提高系统的并发能力和响应性能。
 * 需要注意的是，ThreadPerTaskExecutor 可能会创建大量的线程，因此在使用它时要注意控制线程数，避免出现过多的线程竞争导致性能下降。
 * 此外，由于每个任务都会创建一个新的线程，因此使用 ThreadPerTaskExecutor 也可能会带来一定的线程开销，需要在实际场景中进行权衡和选择。
 */
public class ExploreThreadPerTaskExecutor {
    public static void main(String[] args) throws Exception {
        // exploreThreadPerTaskExecutor();
        exploreNormalExecutor();
    }

    private static void exploreNormalExecutor() throws Exception {
        // 会出现复用
        Executor executor = Executors.newFixedThreadPool(5);
        CountDownLatch countDownLatch = new CountDownLatch(20);
        for (int i = 0; i < 20; i++) {
            executor.execute(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.printf("current thread [%s] running...\n", threadName);
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();
        System.exit(1);
    }

    private static void exploreThreadPerTaskExecutor() throws Exception {
        // ThreadPerTaskExecutor 为每一个任务都创建新的线程，不会复用
        Executor executor = new ThreadPerTaskExecutor(new DefaultThreadFactory("choice"));
        CountDownLatch countDownLatch = new CountDownLatch(20);
        for (int i = 0; i < 20; i++) {
            executor.execute(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.printf("current thread [%s] running...\n", threadName);
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();
        System.exit(1);
    }
}
