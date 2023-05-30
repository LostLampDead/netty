package com.choice.netty.soucecode;

/**
 * SelectedSelectionKeySetSelector 是 Netty 中的一个辅助类，用于提高 Selector 在高并发场景下的性能和吞吐量。
 * Selector 是 Java NIO 中的一个关键组件，用于实现非阻塞 IO 操作。在高并发场景下，Selector 可能会成为系统性能的瓶颈，因此需要使用 SelectedSelectionKeySetSelector 来优化。
 * SelectedSelectionKeySetSelector 的核心思想是使用 SelectedSelectionKeySet 对象来保存 Selector 中已选择的 SelectionKey 集合，
 * 从而避免重复创建和销毁 SelectionKey 对象的开销。SelectedSelectionKeySet 是一个自定义的 SelectionKey 集合类型，
 * 它继承自 HashSet，并且在 add 和 remove 操作中做了一些优化。当 SelectionKey 对象被取消或者失效时，它会被放入一个失效的 SelectionKey 集合中，
 * 并在下一次选择操作之前进行清理，从而避免了内存泄漏。
 * SelectedSelectionKeySetSelector 还实现了一个自定义的 selectNow 方法，用于在 Selector 中选择一组已准备好的通道，并将结果保存到 SelectedSelectionKeySet 对象中。
 * 在 selectNow 方法中，首先会从失效的 SelectionKey 集合中清除已经失效的 SelectionKey 对象，然后调用 Selector 的 selectNow 方法，
 * 将结果保存到 SelectedSelectionKeySet 对象中，并返回已选择的通道数目。这个过程中，SelectedSelectionKeySetSelector 还会对 Selector 锁进行优化，
 * 使用不同的锁来保证线程安全性和可伸缩性。
 */
public class ExploreSelectedSelectionKeySetSelector {
    public static void main(String[] args) {
        // SelectedSelectionKeySetSelector selector = new SelectedSelectionKeySetSelector();
    }
}
