module com.ridge {
    requires rxjava;
    requires jackson.databind;
    requires jackson.core;
    requires org.slf4j;
    requires org.slf4j.simple;
    requires transitive spring.messaging;
    requires spring.core;
    requires spring.beans;
    requires spring.web;
    requires spring.context;
    requires spring.websocket;
    requires java.net.http;
    requires java.sql;

    exports com.ridge;
    exports com.ridge.api;
    exports com.ridge.socket;
    exports com.ridge.mapper;
}
