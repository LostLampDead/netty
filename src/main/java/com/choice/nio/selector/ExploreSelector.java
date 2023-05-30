package com.choice.nio.selector;

import java.nio.channels.Selector;

/**
 * @see Selector
 */
public class ExploreSelector {
    public static void main(String[] args) throws Exception {
        // Selector selector = SelectorProvider.provider().openSelector();
        Selector selector = Selector.open();

    }
}
