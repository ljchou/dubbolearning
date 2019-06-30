package com.ljchou.learning.extension;

@SPI
public interface ExtensionFactory {

    <T> T getExtension(Class<T> type, String name);
}
