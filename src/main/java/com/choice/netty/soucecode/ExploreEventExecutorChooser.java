package com.choice.netty.soucecode;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.DefaultEventExecutorChooserFactory;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorChooserFactory;

/**
 * 在 Netty 中，事件执行器（EventExecutor）是用来执行 I/O 事件的执行器。由于 Netty 中的 I/O 操作可能涉及到大量的并发操作，因此选择合适的事件执行器是非常重要的。
 * 事件执行器选择器（EventExecutorChooser）则是用来选择执行器的工具，它可以根据一定的策略选择合适的执行器来执行事件。
 * 具体来说，EventExecutorChooser 的作用有以下几点：
 * - 选择合适的执行器：EventExecutorChooser 可以根据一定的策略选择合适的执行器来执行事件。
 * 例如，可以选择一个能够提供最大并发数的执行器，或者选择一个与当前线程相同的执行器，以避免线程切换的开销。
 * - 提高系统并发性能：通过选择合适的执行器，可以充分利用系统的多核资源，提高系统的并发能力。
 * - 优化系统响应性能：通过选择合适的执行器，可以避免出现线程饥饿或者线程竞争的情况，从而提高系统的响应性能。
 * 总之，EventExecutorChooser 是一个重要的工具，它可以帮助我们选择合适的执行器来执行事件，从而提高系统的并发能力和响应性能。
 */
public class ExploreEventExecutorChooser {
    public static void main(String[] args) {
        // create default EventLoop
        EventLoop el1 = new DefaultEventLoop();
        EventLoop el2 = new DefaultEventLoop();
        System.out.printf("EventLoop address -> %s | %s \n", el1, el2);

        // create EventExecutorChooser
        EventExecutor[] eventLoop = new EventExecutor[]{el1, el2};
        EventExecutorChooserFactory.EventExecutorChooser chooser = DefaultEventExecutorChooserFactory.INSTANCE.newChooser(eventLoop);

        // 在Netty中，当有连接进到EventLoopGroup，由EventExecutorChooser选择EventLoopGroup中的那个EventLoop执行
        for (int i = 0; i < 10; i++) {
            EventExecutor eventExecutor = chooser.next();
            System.out.printf("the choose result: %s\n", eventExecutor);
        }
    }
}
